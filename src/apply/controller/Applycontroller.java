package apply.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import apply.domain.ApplyDTO;
import apply.model.ApplyDAO;
import apply.model.ApplyDAO_imple;
import common.Transaction;
import recruitment.controller.RecruitmentController;
import recruitment.domain.RecruitmentDTO;
import recruitment.model.RecruitmentDAO;
import recruitment.model.RecruitmentDAO_imple;
import utils.AlignUtil;
import utils.Msg;

public class ApplyController {

	RecruitmentDAO rdao = new RecruitmentDAO_imple();
	ApplyDAO adao = new ApplyDAO_imple();
	
	public void showAllapply(String applicant_Id) {		// 입사지원 현황
		RecruitmentController RecruitmentController = new RecruitmentController();
		
		//입사지원서 출력 메소드
		show_motivation_Apply(applicant_Id); 
			
	}
	
	//입사지원서 출력 메소드
		private void show_motivation_Apply(String applicant_Id) {
			
			
			StringBuilder sb = new StringBuilder();
			List<ApplyDTO> applyList = adao.InfoApply(applicant_Id); //DAO_imple 이동 
			
			if(applyList == null) {	// 입사지원서가 존재하지 않는 경우
				System.out.println("\n>> [경고] 응모된 입사지원서가 없습니다. <<\n");
				
				return;	// 메소드 종료
			}
			
			// 입사지원서가 존재하는 경우 
			for(int i=0;i<applyList.size();i++) {
				
				sb.append(
						  "\n-< "+ applyList.get(i).getRecruitmentDTO().getRecruitmentId() +"번 채용공고 >------------------------------\n"+
						  "1.회사명 : " 		+ applyList.get(i).getCompanyDTO().getName()+"\n"+ //회사 아이디를 통해 
						  "2.채용제목 : " 		+ applyList.get(i).getRecruitmentDTO().getTitle() + "\n" +  
						  "3.채용내용 : " 		+ applyList.get(i).getRecruitmentDTO().getContents() + "\n" + 
						  "4.직종 : "			+ applyList.get(i).getJobDTO().getName()+"\n" + 	//직종 일련번호를 통해
						  "5.경력 : " 		+ Transaction.experience(applyList.get(i).getRecruitmentDTO().getExperience())  + "\n" + 
						  "6.채용형태 : " 		+ Transaction.empType(applyList.get(i).getRecruitmentDTO().getEmpType())  + "\n" + 
						  "7.지역 : " 		+ applyList.get(i).getCompanyDTO().getAddress() +  "\n" +     //회사아이디를 통해 구인회사테이블에서 추출
						  "8.채용인원 : " 		+ applyList.get(i).getRecruitmentDTO().getPeople() + "\n" + 
						  "9.연봉 : " 		+ applyList.get(i).getRecruitmentDTO().getSalary() + "만원\n" + 
						  "10.등록일자 : "		+ applyList.get(i).getRecruitmentDTO().getRegisterday() + "\n" + 
						  "11.채용마감일자 : " 	+ applyList.get(i).getRecruitmentDTO().getDeadlineday() +  "\n" + 
						  "-".repeat(50)+"\n" +
						  "▣ 지원동기 : "		+ applyList.get(i).getMotivation() + "\n" + 
						  "▣ 지원일자 : " 	+ applyList.get(i).getRegisterday() +"\n");
				
			}//end of for -------------
			
			System.out.println(sb.toString());
		} //end of private void show_motivation_Apply(String applicant_Id) -------
	
	
	// 해당하는 공고의 지원자들을 출력하는 메소드
	public List<ApplyDTO> applyShowList(RecruitmentDTO recruitmentDTO) {
		
		List<ApplyDTO> ApplyList = adao.applyList(recruitmentDTO); // 입자지원자 목록 조회
		StringBuilder sb = new StringBuilder();
		
		sb.append(AlignUtil.title("-입사지원자 목록", 48)
				+ "지원서순번\t이름\t지원동기\t\t입사지원일\n"
				+ "-".repeat(46)+"\n");// 입자지원자 목록 타이틀
		
		if(ApplyList.size()==0) {
			Msg.N("입자지원자가 존재하지 않습니다.");
		}
		else {
			for(int i=0; i<ApplyList.size(); i++) {
				sb.append(ApplyList.get(i).getApply_id()+"\t"+
						  ApplyList.get(i).getApplicantDTO().getName()+"\t"+
						  ApplyList.get(i).getMotivation()+"\t\t"+
						  ApplyList.get(i).getRegisterday()+"\n");
			} // end of for()-------------
			sb.append("-".repeat(46)+"\n");
			
			System.out.println(AlignUtil.tab(sb).toString());
		}
		return ApplyList;

	} // end of public void applyShowList(RecruitmentDTO recruitmentDTO)----------


	
	// 지원자 이력서 및 정보를 조회하는 메소드
	public void applySelect(RecruitmentDTO recruitmentDTO, Scanner sc) {

		Map<String, Integer> map = new HashMap<>(); 
		StringBuilder sb = new StringBuilder();
		
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 지원서순번 입력 : ");
			String put_applyId = sc.nextLine();
			
			try {
				int applyId = Integer.parseInt(put_applyId);
				map.put("applyId", applyId);
				map.put("recruitmentId", recruitmentDTO.getRecruitmentId());
				// join 을 5번 하기에 DTO 말고 MAP에 담기
				break;
			} catch(NumberFormatException e) {
				Msg.W("지원서순번은 정수로만 입력해주세요");
			}
			/////////////////////////////////////////////
		} while(true);
		
		Map<String, String> Info = adao.applySelect(map);// 해당 채용공고에 대한 지원자 정보 조회
		
		if(Info == null) {
			Msg.N("검색된 결과가 없습니다.");
			return;
		}
		else {
			System.out.println("\n=== 지원자 이력서 조회 ===\n"
							 + "-< "+Info.get("applicantName")+"님의 정보 >"+"-".repeat(30)+"\n"
							 + "1.채용응모번호 : " + Info.get("apply_id") +"\n"
							 + "2.채용제목 : "+Info.get("title")+"\n"
							 + "3.지원자명 : "+Info.get("applicantName")+"\n"
							 + "4.성별 : "+Transaction.gender(Integer.parseInt(Info.get("gender")))+"\n"
							 + "5.생년월일 : "+Info.get("birthday")+"\n"
							 + "6.휴대폰번호 : "+Info.get("tel")+"\n"
							 + "7.이메일 : "+Info.get("email")+"\n"
							 +"-".repeat(46)+"\n");
			
			
			sb.append(AlignUtil.title("-이력서", 52)
					+ "경력\t학력\t희망근무지역 희망직종 직무기술\t희망연봉\n"
					+ "-".repeat(50)+"\n");
			
			sb.append(Transaction.experience(Integer.parseInt(Info.get("Rexperience")))+"\t"+Transaction.education(Integer.parseInt(Info.get("education")))+"\t"+Info.get("hope_location")+"  "+"\t"+Info.get("hopejobName")+" "+"\t"+Info.get("job_description")+"\t"+Transaction.salary(Integer.parseInt(Info.get("hope_salary")))+"\t"+"\n"
					+ "-".repeat(50)+"\n"
					+ "지원동기 : "+Info.get("motivation")+"\n"
					+ "작성일자 : "+Info.get("registerday")+"\n");
			
			System.out.println(AlignUtil.tab(sb).toString());
			
		}
		
	}



	
}
