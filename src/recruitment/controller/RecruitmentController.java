package recruitment.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import common.Transaction;
import company.domain.CompanyDTO;
import job.controller.JobController;
import recruitment.domain.RecruitmentDTO;
import recruitment.model.*;
import utils.AlignUtil;
import utils.Msg;
import apply.controller.ApplyController;
import apply.domain.ApplyDTO;
import apply.model.*;
import job.model.*;

public class RecruitmentController {
	
	// field
	RecruitmentDAO rdao = new RecruitmentDAO_imple();
	ApplyDAO adao = new ApplyDAO_imple();
	JobDAO jdao = new JobDAO_imple();
	List<RecruitmentDTO> RecruitmentList;
	JobController jCtrl = new JobController();
	ApplyController applyCtrl = new ApplyController();
	
	// method
	
	// *** 우리회사 채용공고 관리를 보여주는 메소드 *** //
	public void menuRecruitment(CompanyDTO companyDTO, Scanner sc) {
		
		StringBuilder sb = new StringBuilder();
		
		System.out.println("=== 채용공고 관리 ===");
		
		do {
			////////////////////////////////////////////////////////////////////	
			RecruitmentList = rdao.recruitmenList(companyDTO.getCompanyId());
			
			if(RecruitmentList.size()==0) {
				System.out.println(">> 조회된 결과가 없습니다. <<\n");
			}
			else {
				sb.setLength(0);
				sb.append(AlignUtil.title("-"+companyDTO.getName()+" 기업의 채용공고 목록", 76));
				sb.append("채용공고순번\t회사명\t직종\t\t제목\t\t경력\t채용형태\t마감일\n");
				sb.append("-".repeat(75)+"\n");
				
				for(int i=0; i<RecruitmentList.size();i++) {
					sb.append(RecruitmentList.get(i).getRecruitmentId()+"\t"+
							  RecruitmentList.get(i).getComdto().getName()+"\t"+
							  RecruitmentList.get(i).getJobdto().getName()+"\t\t"+
							  RecruitmentList.get(i).getTitle()+"\t\t"+
							  Transaction.experience(RecruitmentList.get(i).getExperience()) +"\t"+
							  Transaction.empType(RecruitmentList.get(i).getEmpType())+"\t"+
							  RecruitmentList.get(i).getDeadlineday()+"\n" );
				} // end of for------------
				System.out.println(AlignUtil.tab(sb).toString());
			}
			System.out.println("=".repeat(16)+"< 메뉴 >"+"=".repeat(16));
			System.out.println("1.채용공고 상세보기   2.채용공고 등록   0.돌아가기");
			System.out.println("=".repeat(39));
			
			System.out.print("▷ 메뉴 선택 : ");
			String menu = sc.nextLine();
			
			switch (menu) {
			case "1": // 채용공고 상세보기
				recruitmentInfo(companyDTO, sc);
				
				break;
				
			case "2": // 채용공고 등록
				recruitmentInsert(companyDTO, sc); // 우리회사에 insert하니 우리 회사의 DTO를 가져간다.
				
				break;
				
			case "0": // 돌아가기
				System.out.println("");
				return;
			default:
				System.out.println(">> [경고] 입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다. <<\n");
				break;
			}
			////////////////////////////////////////////////////////////////////
		}while(true);
	
	} // end of public void menuRecruitment(companyDTO company, Scanner sc)---------



	// *** 채용공고 상세보기를 보여주는 메소드 *** //
	private void recruitmentInfo(CompanyDTO companyDTO, Scanner sc) {
		
		RecruitmentDTO recruitmentDTO = recruitmentInfoShow(sc); // 채용공고 상세보기를 출력해주는 메소드
		// 값이 없다면 리턴값이 null이 나온다
		if(recruitmentDTO==null) { // 채용공고가 존재하지 않거나 문자로 입력한 경우
			return;
		}
		else { // 만약 채용공고가 있을 경우
			
			List<ApplyDTO> ApplyList = applyCtrl.applyShowList(recruitmentDTO); // 해당하는 공고의 지원자들을 조회하는 메소드
			
			do {
				////////////////////////////////////////////////////////////////////
				System.out.println("=".repeat(24)+"< 메뉴 >"+"=".repeat(24));
				System.out.println("1.지원자 이력서 조회   2.채용공고 수정   3.채용공고 삭제   0.돌아가기");
				System.out.println("=".repeat(55));
				
				System.out.print("▷ 메뉴 선택 : ");
				String menu = sc.nextLine();
				
				switch (menu) {
				case "1": // 지원자 이력서 조회
					if(ApplyList.size()==0) {
						Msg.N("입자지원자가 존재하지 않습니다.");
						continue;
					}
					else {
						applyCtrl.applySelect(recruitmentDTO, sc); // 지원자 이력서 및 정보를 조회하는 메소드
						break;
					}
				case "2": // 채용공고 수정
					recruitmentUpdate(recruitmentDTO, sc);
					break;
					
				case "3": // 채용공고 삭제
					int deleteResult = recruitmentDelete(recruitmentDTO, companyDTO, sc);
					
					if(deleteResult == 0) {
						System.out.println(">> 채용공고 삭제가 취소되었습니다. <<\n");
					}
					else {
						System.out.println(">> 채용공고 삭제가 완료되었습니다. <<\n");
						return;
					}
					break;
					
				case "0": // 돌아가기
					System.out.println("");
					return;
	
				default:
					System.out.println(">> [경고] 입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다. <<\n");
					break;
				}
				////////////////////////////////////////////////////////////////////
			} while(true);
			
		}
		
	} // end of private void recruitmentInfo(Scanner sc)------------



	// *** 채용공고 상세보기를 출력해주는 메소드 *** //
	RecruitmentDTO recruitmentInfoShow(Scanner sc) {
		
		System.out.print("▷ 채용공고순번 입력 : ");
		String recruitmentId = sc.nextLine();
		
		RecruitmentDTO recruitmentDTO = rdao.recruitmentInfoSelect(recruitmentId);
		
		if(recruitmentDTO==null) { // 채용공고가 존재하지 않거나 문자로 입력한 경우
			System.out.println(">> 입력하신 글번호 "+recruitmentId+"은 존재하지 않습니다. <<\n");
			
		}
		else { // 만약 채용공고가 있을 경우
			System.out.println(recruitmentDTO.toString()); // 채용공고를 보여주는 메소드
		}
		
		return recruitmentDTO;
		
	} // end of private void recruitmentInfoShow(String recruitmentId)----------



	// 채용공고 등록
	private void recruitmentInsert(CompanyDTO companyDTO, Scanner sc) {
		
		RecruitmentDTO recruitmentDTO = new RecruitmentDTO();
		
		System.out.println("\n=== 채용공고 등록 ===");
		
		String title; // 제목 전역변수
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용제목 : ");
			title = sc.nextLine();
			
			if(title.isEmpty()) {
				System.out.println(">> [경고] 제목은 필수 입력사항입니다. <<\n");
			}
			else if(title.length() > 20) {
				Msg.W("채용제목은 최대 20글자까지 입력 가능합니다.");
			}
			else {
				break;
			}
			/////////////////////////////////////////////
		} while(true);
		// 채용제목
		
		String contents; // 제목 내용변수
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용내용 : ");
			contents = sc.nextLine();
			
			if(contents.length() > 200) {
				Msg.W("채용내용은 최대 200글자까지 입력 가능합니다.");
			}
			else {
				break;
			}
			/////////////////////////////////////////////
		} while(true);
		// 채용내용
		
		int fk_job_id = jCtrl.jobShowList(sc); // 직종목록을 뽑아주는 메소드
		// 직종 번호 입력
		
		int experienceNo; // 경력 전역변수
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 경력[신입/경력직] : ");
			String experience = sc.nextLine();
			
			if(experience.isBlank()) {
				Msg.W("경력은 필수 입력사항입니다.");
			}
			else {
				experienceNo = Transaction.experience(experience); // experience(String) 를 통해 타입을 맞춰 experienceNo에 넣는다.
				if(experienceNo == -1) {
					Msg.W("경력은 '신입', '경력직'으로만 입력해주세요");
				}
				else {
					break;
				}
			}
			
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 경력[신입/경력직] 입력
		
		int emp_typeNO; // 채용형태 전역변수
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용형태[정규직/계약직/인턴/프리랜서] : ");
			String emp_type = sc.nextLine();
			
			if(emp_type.isEmpty()) {
				Msg.W("채용형태는 필수 입력사항입니다.");
			}
			else {
				emp_typeNO = Transaction.empType(emp_type); // empType(String) 를 통해 타입을 맞춰 emp_typeNO에 넣는다.
				if(emp_typeNO == -1) {
					System.out.println(">> [경고] 경력은 '정규직', '계약직', '인턴', '프리랜서'으로만 입력해주세요 <<\n");
				}
				else {
					break;
				}
			}
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 채용형태[정규직/계약직/인턴/프리랜서] 입력
		
		int people; // 채용인원 전역변수
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용인원 : ");
			String put_people = sc.nextLine();
			
			if(put_people.isBlank()) {
				Msg.W("채용인원은 필수 입력사항입니다.");
			}
			else if(put_people.length() > 2) {
				Msg.W("채용인원은 최대 99명까지 입력 가능합니다.");
			}
			else {
				try {
					people = Integer.parseInt(put_people);
					
					if(people < 0) {
						System.out.println(">> [경고] 채용인원은 정수로만 입력해주세요 <<\n");
					}
					else {
						break;
					}
				}catch(NumberFormatException e) {
					System.out.println(">> [경고] 채용인원은 정수로만 입력해주세요 <<\n");
				}
			}
			/////////////////////////////////////////////
		} while(true);
		// do~while()-----------------------
		// 채용인원 입력
		
		int salary; // 연봉 전역변수
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 연봉[단위: 만원] : ");
			String put_salary = sc.nextLine();
			
			if(put_salary.isBlank()) {
				Msg.W("연봉은 필수 입력사항입니다.");
			}
			else if(put_salary.length() > 10) {
				Msg.W("연봉은 최대 10억까지 입력 가능합니다.");
			}
			else {
				try {
					salary = Integer.parseInt(put_salary);
					
					break;
				}catch(NumberFormatException e) {
					System.out.println(">> [경고] 연봉은 정수로만 입력해주세요 <<\n");
				}
			}
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 연봉[단위: 만원] 입력
		
		String deadlineday; // 채용마감일자 전역변수
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용마감일자[1999-01-01] : ");
			deadlineday = sc.nextLine();
			
			if(deadlineday.isBlank()) {
				Msg.W("채용마감일자는 필수 입력사항입니다.");
			}
			else {
			
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false); // 입력한 deadlineday 가 실제 달력에 있는지 확인
				String nowDate = sdf.format(now);
		
				try {
					Date newDate = sdf.parse(deadlineday); // 새로 수정할 날짜
					
					Date todayDate = sdf.parse(nowDate);   // 현재 날짜
					
					if(newDate.before(todayDate)) {
						System.out.println(">> [경고] 채용마감일자는 현재 날짜보다 이후로 입력해주세요 <<\n");
					} // '새로 수정할 날짜' 가 '현재 날짜' 보다 이전일 경우 true 반환
					else { 
						break;
					} // '새로 수정할 날짜' 가 '현재 날짜' 와 같거나 이후일 경우 false 반환
					
				} catch (ParseException e) {
					System.out.println(">> 달력에 존재하지 않는 값입니다. <<");
				} 
			}
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 채용마감일자[1999-01-01] 입력
		
		
		recruitmentDTO.setTitle(title);
		recruitmentDTO.setContents(contents);
		recruitmentDTO.setFkJobId(fk_job_id);
		recruitmentDTO.setExperience(experienceNo);
		recruitmentDTO.setEmpType(emp_typeNO);
		recruitmentDTO.setPeople(people);
		recruitmentDTO.setSalary(salary);
		recruitmentDTO.setDeadlineday(deadlineday);
		
		int n = rdao.recruitmentInsert(companyDTO, recruitmentDTO);
		
		if(n != 1) {
			System.out.println("쿼리문 오류");
		}
		else {
			System.out.println("\n>> 채용공고 등록이 완료되었습니다. <<\n");
			RecruitmentList.add(recruitmentDTO);
			return;
		}
		
	} // end of private void recruitmentInsert(Scanner sc)--------------
	
	
	
	// 채용공고 수정
	private void recruitmentUpdate(RecruitmentDTO recruitmentDTO, Scanner sc) {
		
		System.out.println(">> [주의사항] 변경하지 않고 예전의 데이터값을 그대로 사용하시려면 그냥 엔터하세요!! <<\n");
		System.out.println("=== 채용공고 수정 ===");
		
		String title;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용제목 : ");
			title = sc.nextLine();
			
			if(title.isEmpty()) {
				title = recruitmentDTO.getTitle();
				break;
			}
			else if(title.length() > 20) {
				Msg.W("채용제목은 최대 20글자까지 입력 가능합니다.");
			}
			else {
				break;
			}
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 채용제목
		
		String contents;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용내용 : ");
			contents = sc.nextLine();
			
			if(contents.isEmpty()) {
				contents = recruitmentDTO.getContents();
				break;
			}
			else if(contents.length() > 200) {
				Msg.W("채용내용은 최대 200글자까지 입력 가능합니다.");
			}
			else {
				break;
			}
			/////////////////////////////////////////////
		} while(true);
		// do~while()-----------------------
		// 채용내용
		
		int fk_job_id = jCtrl.jobUpdateList(recruitmentDTO.getFkJobId(), sc); // 직종목록을 뽑아주는 메소드
		// 직종 번호 입력
		
		int experienceNo;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 경력[신입/경력직] : ");
			String experience = sc.nextLine();
			
			if(experience.isEmpty()) {
				experienceNo = recruitmentDTO.getExperience();
			}
			else {
				experienceNo = Transaction.experience(experience);
			}
			// experience(String) 를 통해 타입을 맞춰 experienceNo에 넣는다.
			
			if(experienceNo == -1) {
				System.out.println(">> [경고] 경력은 '신입', '경력직'으로만 입력해주세요 <<\n");
			}
			else {
				break;
			}
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 경력[신입/경력직] 입력
		
		int emp_typeNO;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용형태[정규직/계약직/인턴/프리랜서] : ");
			String emp_type = sc.nextLine();
			
			if(emp_type.isEmpty()) {
				emp_typeNO = recruitmentDTO.getEmpType();
			}
			else {
				emp_typeNO = Transaction.empType(emp_type);
			}
			// empType(String) 를 통해 타입을 맞춰 emp_typeNO에 넣는다.
			
			if(emp_typeNO == -1) {
				System.out.println(">> [경고] 경력은 '정규직', '계약직', '인턴', '프리랜서'으로만 입력해주세요 <<\n");
			}
			else {
				break;
			}
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 채용형태[정규직/계약직/인턴/프리랜서] 입력
		
		int people;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용인원 : ");
			String put_people = sc.nextLine();
			
			if(put_people.isEmpty()) {
				people =  recruitmentDTO.getPeople();
				break;
			}
			else if(put_people.length() > 2) {
				Msg.W("채용인원은 최대 99명까지 입력 가능합니다.");
			}
			else {
				try {
					people = Integer.parseInt(put_people);
					
					if(people < 0) {
						System.out.println(">> [경고] 채용인원은 정수로만 입력해주세요 <<\n");
					}
					else {
						break;
					}
				}catch(NumberFormatException e) {
					System.out.println(">> [경고] 채용인원은 정수로만 입력해주세요 <<\n");
				}
			}
			/////////////////////////////////////////////
		} while(true);
		// do~while()-----------------------
		// 채용인원 입력
		
		int salary;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 연봉[단위: 만원] : ");
			String put_salary = sc.nextLine();
			
			if(put_salary.isEmpty()) {
				salary = recruitmentDTO.getSalary();
				break;
			}
			else if(put_salary.length() > 10) {
				Msg.W("연봉은 최대 10억까지 입력 가능합니다.");
			}
			else {
				try {
					salary = Integer.parseInt(put_salary);
					
					break;
				}catch(NumberFormatException e) {
					System.out.println(">> [경고] 연봉은 정수로만 입력해주세요 <<\n");
				}
			}
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 연봉[단위: 만원] 입력
		
		String deadlineday;
		do {
			/////////////////////////////////////////////
			System.out.print("▷ 채용마감일자[1999-01-01] : ");
			deadlineday = sc.nextLine();
			
			if(deadlineday.isEmpty()) {
				deadlineday = recruitmentDTO.getDeadlineday();
				break;
			}
			else {
			
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false); // 입력한 deadlineday 가 실제 달력에 있는지 확인
				String nowDate = sdf.format(now);
		
				try {
					Date newDate = sdf.parse(deadlineday); // 새로 수정할 날짜
					
					Date todayDate = sdf.parse(nowDate);   // 현재 날짜
					
					if(newDate.before(todayDate)) {
						System.out.println(">> [경고] 채용마감일자는 현재 날짜보다 이후로 입력해주세요 <<\n");
					} // '새로 수정할 날짜' 가 '현재 날짜' 보다 이전일 경우 true 반환
					else {
						break;
					} // '새로 수정할 날짜' 가 '현재 날짜' 와 같거나 이후일 경우 false 반환
					
				} catch (ParseException e) {
					System.out.println(">> 달력에 존재하지 않는 값입니다. <<");
				} 
			}
			/////////////////////////////////////////////
		}while(true);
		// do~while()-----------------------
		// 채용마감일자[1999-01-01] 입력
		
		
		recruitmentDTO.setTitle(title);
		recruitmentDTO.setContents(contents);
		recruitmentDTO.setFkJobId(fk_job_id);
		recruitmentDTO.setExperience(experienceNo);
		recruitmentDTO.setEmpType(emp_typeNO);
		recruitmentDTO.setPeople(people);
		recruitmentDTO.setSalary(salary);
		recruitmentDTO.setDeadlineday(deadlineday);
		recruitmentDTO.setUpdateday(deadlineday);
		
		int n = rdao.recruitmentUpdate(recruitmentDTO);
		
		if(n != 1) {
			System.out.println("쿼리문 오류");
		}
		else {
			System.out.println("\n>> 채용공고 수정이 완료되었습니다. <<\n");
			RecruitmentList.add(recruitmentDTO);
			return;
		}
		
	} // end of private void recruitmentUpdate(Scanner sc)--------------



	// 채용공고 삭제
	private int recruitmentDelete(RecruitmentDTO recruitmentDTO, CompanyDTO companyDTO, Scanner sc) {
		
		int result = 0;
		do {
			System.out.print("▷ "+recruitmentDTO.getRecruitmentId()+"번 채용공고를 정말로 삭제하시겠습니까?[Y/N] : ");
			String yn = sc.nextLine();
			
			if("y".equalsIgnoreCase(yn)) {
				result = rdao.recruitmentDelete(recruitmentDTO, companyDTO);
				RecruitmentList.add(recruitmentDTO);
				break;
			}
			else if("n".equalsIgnoreCase(yn)) {
				result = 0;
				break;
			}
			else {
				Msg.W("Y 또는 N 만 입력해주세요.");
			}
		} while(true);
		
		return result;
		
	} // end of private void recruitmentDelete(RecruitmentDTO recruitmentDTO, Scanner sc)---------

	
	// === 채용공고 통계 조회 메뉴 === //
	public void recruitmentStatistics(Scanner sc) {
		do {
			System.out.println("=== 채용공고 통계 조회 ===");
			
			System.out.print(AlignUtil.title("=메뉴 선택")
						   + "1.직종별 채용공고 수 순위   2.직종별 평균 연봉 순위\n"
						   + "0.돌아가기\n"
						   + "=".repeat(50)+"\n"
						   + "▷ 메뉴 번호 선택 : ");
			
			String menu = sc.nextLine();
			switch (menu) {
			case "0":
				return;
			case "1": // 직종별 채용공고 순위
				getRecruitmentStatistics(menu);
				break;
			case "2": // 직종별 연봉 순위
				getRecruitmentStatistics(menu);
				break;
	
			default:
				Msg.W("메뉴에 없는 번호입니다.");
				break;
			}
		} while(true);
		
	}


	// === 직종별 채용공고 순위를 출력하는 메소드 === //
	private void getRecruitmentStatistics(String status) {
		List<Map<String, String>> rsMapList = rdao.recruitmentStatistics(status);
		
		
		if(rsMapList.size()==0) {
			Msg.N("등록된 채용공고가 없습니다.");
		}
		else {
			StringBuilder sb = new StringBuilder();
			
			String menuStr = ("1".equals(status))?"채용공고 수":"평균 연봉";
			sb.append(AlignUtil.title("-직종별 "+ menuStr +" 순위")
					+ "순위\t직종\t\t"+menuStr+"\n"
					+ "-".repeat(50)+"\n");
			for(Map<String, String> rsMap : rsMapList) {
				String result = "1".equals(status) ?
						rsMap.get("result") : Transaction.salary(Integer.parseInt(rsMap.get("result")));
				sb.append(rsMap.get("rank") + "\t"
						+ rsMap.get("jobName") + "\t\t"
						+ result + "\n");
			}
			sb.append("-".repeat(50));
			System.out.println(AlignUtil.tab(sb).toString());
		}
	}
	
}
