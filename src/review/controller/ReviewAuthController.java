package review.controller;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import company.controller.CompanyController;
import review.domain.ReviewAuthDTO;
import review.model.ReviewAuthDAO;
import review.model.ReviewAuthDAO_imple;
import utils.AlignUtil;
import utils.Msg;
import utils.ValidationUtil;

public class ReviewAuthController {
	
	// field
	List<Map<String, String>> mapList;
	
	// method
	/*
	 * 회사 인증목록 출력 후 선택
	 * 선택한 ReviewAuthMap 반환
	 */
	public Map<String, String> selectReviewAuth(String applicantId, Scanner sc) {
		ReviewAuthDAO reviewAuthDAO = new ReviewAuthDAO_imple();
		
		mapList = reviewAuthDAO.getReviewAuth(applicantId);

		if(mapList.size()>0) {
			StringBuilder sb = new StringBuilder();
			
			sb.append(AlignUtil.title("-인증된 회사 목록")
					+ "번호\t회사명\t인증여부\t리뷰작성여부\n"
					+ "-".repeat(50)+"\n");
			
			for(int i=0; i<mapList.size(); i++) {
				sb.append((i+1) + "\t"
						+ mapList.get(i).get("company_name") + "\t"
						+ mapList.get(i).get("is_permitted") + "\t"
						+ mapList.get(i).get("is_registerd") + "\n");
			}
			
			sb.append("-".repeat(50));
			System.out.println(AlignUtil.tab(sb).toString());
			
			// 메뉴 출력
			boolean isPermitted = reviewAuthMenu(applicantId, sc, true);
			if(!isPermitted) {
				return null;
			}
			
			do {
				System.out.print("▷ 회사 번호 선택[0: 취소] : ");
				String companyNum = sc.nextLine();
				
				if(ValidationUtil.isNumberValid(companyNum) &&
						0 <= Integer.parseInt(companyNum) &&
						Integer.parseInt(companyNum)-1 < mapList.size()) { // 입력된 숫자가 mapList 크기보다 작아야 함
					int n = Integer.parseInt(companyNum)-1;
					
					if(n==-1) {
						return null;
					}
					if(!"완료".equals(mapList.get(n).get("is_permitted"))) { // 회사인증이 완료되지 않은 경우
						Msg.W("인증이 완료된 회사만 리뷰를 남길 수 있습니다.");
						return null;
					}
					else if("작성됨".equals(mapList.get(n).get("is_registerd"))){ // 리뷰가 이미 작성된 경우
						Msg.W("선택한 회사 '"+mapList.get(n).get("company_name")+"'에 대한 리뷰가 이미 작성되어있습니다.");
					}
					else { // 조건 모두 통과시 인증 map 반환
						return mapList.get(n);
					}
				}
				else {
					Msg.W("입력하신 '" + companyNum + "'번에 해당하는 회사는 존재하지 않습니다.");
				}
			} while(true);
		}
		else {
			Msg.W("먼저 회사인증을 받아야 합니다.");

			reviewAuthMenu(applicantId, sc, false);
			return null;
		}
		
	}
	
	
	/*
	 * 회사 인증을 추가하는 메뉴
	 * 인증된 회사 목록이 없다면 "2.인증된 회사 중 선택" 메뉴를 표시하지 않는다.
	 * 돌아가기를 선택하면 false
	 * 새로 회사 인증을 신청하면 false
	 * 인증된 회사 목록 중 선택하면 true
	 */
	public boolean reviewAuthMenu(String applicantId, Scanner sc, boolean isPermitted) {
		do {
			String permittedStr = (isPermitted)?"2.인증된 회사 중 선택   ":"";

			System.out.print("===================< 메뉴 선택 >====================\n"
				       	   + "1.회사 인증 요청하기   "+permittedStr+"0.돌아가기\n"
				       	   + "=".repeat(50)+"\n"
				       	   + "▷ 메뉴 번호 선택 : ");
			String menu = sc.nextLine();
			switch (menu) {
			case "0": // 돌아가기
				return false;
			
			case "1": // 회사 인증 추가하기
				registerReviewAuth(applicantId, sc);
				return false;
				
			case "2": // 인증된 회사 중 선택
				if(isPermitted) {
					return true;
				}

			default:
				Msg.W("입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다.");
				break;
			}
		} while(true);
	}
	
	
	private void registerReviewAuth(String applicantId, Scanner sc){

		System.out.println("\n=== 회사 인증 요청 ===");
		// TODO 회사검색 후 출력
		CompanyController companyCtrl = new CompanyController();
		String companyId = companyCtrl.searchCompanyWithReview(sc, "0");
		
		if(companyId == null) {
			Msg.N("회사 인증을 취소하였습니다.");
			return;
		}
		
		// 인증 요청이 겹치는지 확인
		for(Map<String, String> reviewAuthMap : mapList) {
			if(companyId.equals(reviewAuthMap.get("fk_company_id"))) {
				if("완료".equals(reviewAuthMap.get("is_permitted"))) {
					Msg.W("이미 인증완료한 회사입니다.");
				}
				else {
					Msg.W("이미 인증요청한 회사입니다.");
				}
				return;
			}
		}
		
		ReviewAuthDTO reviewAuthDTO = new ReviewAuthDTO();
		reviewAuthDTO.setFkApplicantId(applicantId);
		reviewAuthDTO.setFkCompanyId(companyId);
		
		ReviewAuthDAO reviewAuthDAO = new ReviewAuthDAO_imple();
		
		int n = reviewAuthDAO.registerReviewAuth(reviewAuthDTO);
		
		if(n==1) {
			System.out.println(">> 회사 인증 요청이 완료되었습니다.\n>> 관리자가 인증을 완료하면 리뷰를 남길 수 있습니다.\n");
		}
		else {
			System.out.println(">> 회사 인증 요청에 실패했습니다. <<");
		}
	}
}
