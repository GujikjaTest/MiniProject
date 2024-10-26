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
import job.domain.JobDTO;
import job.model.JobDAO;
import job.model.JobDAO_imple;
import recruitment.domain.RecruitmentDTO;
import recruitment.model.RecruitmentDAO;
import recruitment.model.RecruitmentDAO_imple;
import utils.AlignUtil;
import resume.controller.Resumecontroller;
import resume.domain.ResumeDTO;
import resume.model.ResumeDAO;
import resume.model.ResumeDAO_imple;
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
				sb.append(AlignUtil.title("-지원자수 상위 10개 채용공고", 83));
				sb.append("순위\t회사명\t\t직종\t\t제목\t\t경력\t채용형태\t마감일\n");
				sb.append("-".repeat(83)+"\n");
				
				for(int i=0; i<RecruitmentList.size();i++) {
					sb.append(RecruitmentList.get(i).getRank()+"\t"+
							  RecruitmentList.get(i).getComdto().getName()+"\t\t"+
							  RecruitmentList.get(i).getJobdto().getName()+"\t\t"+
							  RecruitmentList.get(i).getTitle()+"\t\t"+
							  Transaction.experience(RecruitmentList.get(i).getExperience()) +"\t"+
							  Transaction.empType(RecruitmentList.get(i).getEmpType())+"\t"+
							  RecruitmentList.get(i).getDeadlineday()+"\n" );
				} // end of for------------
				System.out.println(AlignUtil.tab(sb).toString());
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
				
				showAllRecruitment(map, sc,applicant); // 각 항목별로 입력받은 search 값을 넘겨서 한 메소드로 처리할 것이다!
			}
			////////////////////////////////////////////////////////////////////
		}while(true);
		
	} // end of public void findRecruitment(ApplicantDTO applicant, Scanner sc)-------------



	// 회사명, 직종별, 지역별, 경력 검색
	private void showAllRecruitment(Map<String, String> map, Scanner sc,ApplicantDTO applicant) {
		
		RecruitmentList = new ArrayList<>();
		RecruitmentList = rdao.showAllRecruitment(map);
		
		if(RecruitmentList == null) {
			System.out.println(">> 검색 결과가 없습니다. <<\n");
			return;
		}
		else {
			do {
				///////////////////////////////////////////////////////////////
				StringBuilder sb = new StringBuilder(); // 검색결과를 반복해서 출력하기 위해 StringBuilder 선언 위치를 변경함 - 김규빈
				
				if(map.get("status").equals("1")) { // 회사명
					sb.append(AlignUtil.title("-"+map.get("search")+" 회사명 검색결과", 83));
				}
				else if(map.get("status").equals("2")) { // 직종별
					sb.append(AlignUtil.title("-"+ RecruitmentList.get(0).getJobdto().getName()+" 직종 검색결과", 83));
				}
				else if(map.get("status").equals("3")) { // 지역별
					sb.append(AlignUtil.title("-"+map.get("search")+" 지역 검색결과", 83));
				}
				else { // 경력
					sb.append(AlignUtil.title("-"+Transaction.experience(Integer.parseInt(map.get("search")))+" 경력 검색결과", 83));
				}
				sb.append("채용공고순번\t 회사명\t\t직종\t\t제목\t\t경력\t채용형태\t마감일\n"+
						  "-".repeat(83)+"\n");
				
				for(int i=0; i<RecruitmentList.size(); i++) {
					sb.append(RecruitmentList.get(i).getRecruitmentId()+"\t"+" "+
							  RecruitmentList.get(i).getComdto().getName()+"\t\t"+
							  RecruitmentList.get(i).getJobdto().getName()+"\t\t"+
							  RecruitmentList.get(i).getTitle()+"\t\t"+
							  Transaction.experience(RecruitmentList.get(i).getExperience()) +"\t"+
							  Transaction.empType(RecruitmentList.get(i).getEmpType())+"\t"+
							  RecruitmentList.get(i).getDeadlineday()+"\n");
				} // end of for()--------------------------
				sb.append("-".repeat(83));
				System.out.println(AlignUtil.tab(sb).toString());
				
				boolean isReturn = recruitmentInfo(sc,applicant); // 채용공고 상세보기
				// 채용공고번호 입력을 잘못했을 때, 입사지원을 하지 않고 돌아가기를 선택한 경우 false 반환 - 김규빈
				// 즉, 위의 두 가지 경우에는 검색 결과를 다시 출력하도록 함
				
				if(isReturn) {
					return;
				}
				///////////////////////////////////////////////////////////////
			} while(true);
		}
		
		
	}


	
	// 채용공고 상세보기
	// 채용공고번호 입력을 잘못했을 때, 입사지원을 하지 않고 돌아가기를 선택한 경우 false 반환 - 김규빈
	// 나머지는 true 반환
	private boolean recruitmentInfo(Scanner sc,ApplicantDTO applicant) {

		StringBuilder sb = new StringBuilder();
		
		System.out.print("▷ 채용공고순번 입력[0: 취소] : ");
		String recruitmentId = sc.nextLine();
		
		if("0".equals(recruitmentId)) { // 0을 입력한 경우 - 김규빈
			return true;
		}
		
		RecruitmentDTO recruitmentDTO = rdao.recruitmentInfoSelect(recruitmentId);
		
		if(recruitmentDTO==null) { // 채용공고가 존재하지 않거나 문자로 입력한 경우
			System.out.println(">> 입력하신 글번호 "+recruitmentId+"은 존재하지 않습니다. <<\n");
			return false;
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
					Resumecontroller resumecontroller = new Resumecontroller();
					ResumeDAO resumeDAO = new ResumeDAO_imple();
					ResumeDTO resumeDTO = null;
					JobDTO jobDTO = null;
					
					/////////////////////////////////
					// *** 입사지원 중복체크 메서드 *** //
					
					boolean result = false;
					result = resumeDAO.apply_Duplication_Check(recruitmentId,result,applicant);
					
					if(result == true) {
						System.out.println("\n>> [경고] 중복된 입사지원입니다. <<");
						return true; // 중복된 지원일 경우 메소드 종료
					}
					//////////////////////////////////
					
					int n = 1; // 1이면 입사지원 탭 n 아니면 다른 것 ex_ 1이면 == 입사지원 ==  그 외 == 이력서 관리 ==
					
					boolean completed = resumeDAO.resume_Completed(applicant);
					if(completed == false) {
						System.out.println("\n>> 이력서를 먼저 등록해주세요! <<\n");
						/*
						 *  이력서가 없을 경우 => 1.입사지원 -> 이력서 등록 -> 이력서 등록 -> 지원동기 작성
						 * 
						 *
						/*
						여기서도 사용해주기 위해 Resumecontroller 클래스의 
						private void writeResume(ApplicantDTO applicant) 을
						public void writeResume(ApplicantDTO applicant)으로 변경
						*/
						Resumecontroller Resumecontroller = new Resumecontroller();
						Resumecontroller.writeResume(applicant);
						
						//return; 리턴 삭제, 이력서가 있을 시 completed가 true 이므로  if문을 실행하지 않음
						// 이력서가 없을 시 if문을 실행하여 이력서 작성 후 밑의 지원동기입력(입사지원서 테이블 insert)실행
					}
					
					resumecontroller.resumeInfo(sb,jobDTO,resumeDTO,resumeDAO,applicant,n); // 이력서 출력
					
					
					// *** 채용응모의 지원동기 입력 메소드 *** //
					apply_Applicant(sc,resumeDAO,applicant,recruitmentId); 
					
					
					return true;
					
				case "0": // 돌아가기
					
					return false;
	
				default:
					System.out.println(">> [경고] 입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다. <<\n");
					break;
				}
				////////////////////////////////////////////////////////////////////
			}while(true);
			
		}
		
	} // end of private void recruitmentInfo(Scanner sc)---------
	
	
	
	// *** 지원동기 입력을 위한 메소드 *** //
	private void apply_Applicant(Scanner sc,ResumeDAO resumeDAO,ApplicantDTO applicant,String recruitmentId) {
		
		ApplyDAO applyDAO = new ApplyDAO_imple();
		
		System.out.println("-".repeat(62));
		System.out.print("▷ 지원동기 : ");
		String apply_Applicant = sc.nextLine();
		applyDAO.apply_Applicant(apply_Applicant,applicant,recruitmentId); // 입사지원서 삽입 메서드
		
	} // end of private void apply_Applicant(Scanner sc) -----
	
}
