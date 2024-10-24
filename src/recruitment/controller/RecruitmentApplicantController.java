package recruitment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import applicant.domain.ApplicantDTO;
import apply.model.ApplyDAO;
import apply.model.ApplyDAO_imple;
import common.Transaction;
import job.controller.JobController;
import job.model.JobDAO;
import job.model.JobDAO_imple;
import recruitment.domain.RecruitmentDTO;
import recruitment.model.RecruitmentDAO;
import recruitment.model.RecruitmentDAO_imple;
import utils.Msg;

public class RecruitmentApplicantController {

	// field
	RecruitmentDAO rdao = new RecruitmentDAO_imple();
	ApplyDAO adao = new ApplyDAO_imple();
	JobDAO jdao = new JobDAO_imple();
	List<RecruitmentDTO> RecruitmentList;
	JobController jCtrl = new JobController();
	
	// method
	
	// *** 채용공고 찾기를 보여주는 메소드 *** //
	public void findRecruitment(ApplicantDTO applicant, Scanner sc) {
		
		StringBuilder sb = new StringBuilder();
		
		System.out.println("\n=== 채용공고 찾기 ===");
		
		do {
			////////////////////////////////////////////////////////////////////
			RecruitmentList = rdao.recruitmenTopList(); // 지원자수 상위 10개의 채용공고를 불러와주는 메소드
			
			if(RecruitmentList.size()==0) {
				System.out.println(">> 조회된 결과가 없습니다. <<\n");
			}
			else {
				sb.setLength(0);
				sb.append("-< 지원자수 상위 10개 채용공고 >"+"-".repeat(56)+"\n");
				sb.append("순위  회사명    직종          제목                       경력    채용형태    마감일\n");
				sb.append("-".repeat(80)+"\n");
				
				for(int i=0; i<RecruitmentList.size();i++) {
					sb.append(align(RecruitmentList.get(i).getRank(), 4)+"  "+
							  align(RecruitmentList.get(i).getComdto().getName(), 5)+"  "+
							  align(RecruitmentList.get(i).getJobdto().getName(), 10)+"  "+
							  RecruitmentList.get(i).getTitle()+"\t\t"+
							  Transaction.experience(RecruitmentList.get(i).getExperience()) +"  "+
							  Transaction.empType(RecruitmentList.get(i).getEmpType())+"  "+
							  RecruitmentList.get(i).getDeadlineday()+"\n" );
				} // end of for------------
				System.out.println(sb.toString());
			}
			System.out.println("=".repeat(17)+"< 카테고리 메뉴 >"+"=".repeat(17));
			System.out.println("1.회사명   2.직종별   3.지역별   4.경력   0.돌아가기");
			System.out.println("=".repeat(47));
			
			System.out.print("▷ 검색메뉴번호 입력 : ");
			String menu = sc.nextLine();
			
			String search = null;
			String status = null;
			
			switch (menu) {
			case "1": // 회사명
				System.out.print("▷ 회사명 입력 : ");
				search = sc.nextLine();
				status = "1";
				break;

			case "2": // 직종별
				search =  String.valueOf(jCtrl.jobShowList(sc)); // 직종목록을 뽑아주는 메소드
				status = "2";
				break;
				
			case "3": // 지역별
				System.out.print("▷ 지역별 입력 : ");
				search = sc.nextLine();
				status = "3";
				break;
				
			case "4": // 경력
				do {
					System.out.print("▷ 경력[신입/경력직] 입력 : ");
					search = sc.nextLine();
					search = String.valueOf(Transaction.experience(search));  // 0, 1 로 변경 후 보낼 예정
					
					if(search.equals("-1")) {
						Msg.W("경력은 '신입', '경력직'으로만 입력해주세요");
					}
					else {
						break;
					}
				} while(true);
				status = "4";
				break;
				
			case "0":
				
				return;
			default:
				System.out.println(">> [경고] 입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다. <<\n");
				break;
			}
			if(search != null) {
				Map<String, String> map = new HashMap<>();
				map.put("search", search);
				map.put("status", status);
				
				showAllRecruitment(map, sc); // 각 항목별로 입력받은 search 값을 넘겨서 한 메소드로 처리할 것이다!
			}
			////////////////////////////////////////////////////////////////////
		}while(true);
		
	} // end of public void findRecruitment(ApplicantDTO applicant, Scanner sc)-------------



	// 회사명, 직종별, 지역별, 경력 검색
	private void showAllRecruitment(Map<String, String> map, Scanner sc) {
		
		RecruitmentList = new ArrayList<>();
		RecruitmentList = rdao.showAllRecruitment(map);
		StringBuilder sb = new StringBuilder();
		
		if(RecruitmentList == null) {
			System.out.println(">> 검색 결과가 없습니다. <<\n");
			return;
		}
		else {
			if(map.get("status").equals("1")) { // 회사명
				sb.append("-< "+map.get("search")+" 회사명 검색결과 >"+"-".repeat(56)+"\n");
			}
			else if(map.get("status").equals("2")) { // 직종별
				sb.append("-< "+ RecruitmentList.get(0).getJobdto().getName()+" 직종 검색결과 >"+"-".repeat(56)+"\n");
			}
			else if(map.get("status").equals("3")) { // 지역별
				sb.append("-< "+map.get("search")+" 지역 검색결과 >"+"-".repeat(56)+"\n");
			}
			else { // 경력
				sb.append("-< "+Transaction.experience(Integer.parseInt(map.get("search")))+" 경력 검색결과 >"+"-".repeat(56)+"\n");
			}
			sb.append("채용공고순번  회사명    직종          제목                       경력    채용형태    마감일\n"+
					  "-".repeat(86)+"\n");
			
			for(int i=0; i<RecruitmentList.size(); i++) {
				sb.append(align(RecruitmentList.get(i).getRecruitmentId(), 8)+"  "+
						  align(RecruitmentList.get(i).getComdto().getName(), 5)+"  "+
						  align(RecruitmentList.get(i).getJobdto().getName(), 10)+"  "+
						  RecruitmentList.get(i).getTitle()+"\t\t"+
						  Transaction.experience(RecruitmentList.get(i).getExperience()) +"  "+
						  Transaction.empType(RecruitmentList.get(i).getEmpType())+"  "+
						  RecruitmentList.get(i).getDeadlineday()+"\n");
			} // end of for()--------------------------
			sb.append("-".repeat(86)+"\n");
			System.out.println(sb.toString());
		}
		
		do {
			////////////////////////////////////////////////////////////////////
			System.out.println("\n"+"=".repeat(10)+"< 메뉴 >"+"=".repeat(10));
			System.out.println("1.채용공고 상세보기   0.돌아가기");
			System.out.println("=".repeat(27));
			
			System.out.print("▷ 검색메뉴번호 입력 : ");
			String menu = sc.nextLine();
			
			switch (menu) {
			case "1": // 채용공고 상세보기
				recruitmentInfo(sc); // 채용공고 상세보기
				break;
	
			case "0": // 돌아가기
				
				return;
			default:
				System.out.println(">> [경고] 입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다. <<\n");
				break;
			}
			////////////////////////////////////////////////////////////////////
		} while(true);
		
	}


	
	// 채용공고 상세보기
	private void recruitmentInfo(Scanner sc) {

		StringBuilder sb = new StringBuilder();
		
		System.out.print("▷ 채용공고순번 입력 : ");
		String recruitmentId = sc.nextLine();
		
		RecruitmentDTO recruitmentDTO = rdao.recruitmentInfoSelect(recruitmentId);
		
		if(recruitmentDTO==null) { // 채용공고가 존재하지 않거나 문자로 입력한 경우
			System.out.println(">> 입력하신 글번호 "+recruitmentId+"은 존재하지 않습니다. <<\n");
			return;
		}
		else { // 만약 채용공고가 있을 경우
			System.out.println("=== 채용공고 상세보기 ===");
			System.out.println(recruitmentDTO.toString());
			
			System.out.println(sb.toString());
			
			do {
			////////////////////////////////////////////////////////////////////
			System.out.println("=".repeat(6)+"< 메뉴 >"+"=".repeat(6));
			System.out.println("1.입사지원   0.돌아가기");
			System.out.println("=".repeat(18));
			
			System.out.print("▷ 검색메뉴번호 입력 : ");
			String menu = sc.nextLine();
			
			switch (menu) {
			case "1": // 입사지원
				// ----------------------------------상우
				break;
				
			case "0": // 돌아가기
				
				return;

			default:
				System.out.println(">> [경고] 입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다. <<\n");
				break;
			}
			////////////////////////////////////////////////////////////////////
			}while(true);
			
		}
		
	} // end of private void recruitmentInfo(Scanner sc)---------



	// === 제목 정렬을 위한 메소드 === //
	private String align(String str, int n) {
	   return str +" ".repeat(n-str.length());
	}
	
	private String align(int no, int n) {
		String num = String.valueOf(no);
	    return no +" ".repeat(n-num.length());
	}
}
