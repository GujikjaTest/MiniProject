package applicant.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import applicant.domain.ApplicantDTO;
import applicant.model.*;
import apply.controller.Applycontroller;
import recruitment.controller.RecruitmentApplicantController;
import resume.controller.Resumecontroller;
//import review.controller.ReviewController;
import utils.Msg;
import utils.ValidationUtil;

public class ApplicantController {

	// field
	ApplicantDAO applicantDAO = new ApplicantDAO_imple();
	
	// method
	
	// === 구직자 회원가입 === //
	public void register(Scanner sc) {
		System.out.println("\n=== 구직자 회원가입 ===\n");
		
		String applicantId, passwd, email, name, birthday, gender, tel;
		
		// 아이디
		do {
			System.out.print("▷ 아이디 : ");
			applicantId = sc.nextLine();
			
			if(ValidationUtil.isIDValid(applicantId)) {
				break;
			}
		} while(true);
		
		// 비밀번호
		do {
			System.out.print("▷ 비밀번호[영문,숫자,특수문자 조합] : ");
			passwd = sc.nextLine();
			
			if(ValidationUtil.isPasswordValid(passwd)) {
				break;
			}
		} while(true);
		
		// 이메일
		do {
			System.out.print("▷ 이메일 : ");
			email = sc.nextLine();
			
			if(ValidationUtil.isEmailValid(email)) {
				break;
			}
		} while(true);

		// 성명
		System.out.print("▷ 성명 : ");
		name = sc.nextLine();

		// 생일
		do {
			System.out.print("▷ 생일[1999-01-01] : ");
			birthday = sc.nextLine();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false); // 엄격하게 파싱
			
			try {
				sdf.parse(birthday); // 달력에 존재하는 날짜인지 확인
				break;
			} catch (ParseException e) {
				Msg.W(birthday+"는 달력에 존재하지 않는 값입니다.");
			}
		} while(true);
		
		// 성별
		do {
			System.out.print("▷ 성별[남/여] : ");
			gender = sc.nextLine();
			
			if(getGenderNumber(gender)==-1) {
				Msg.W("'남' 또는 '여'만 입력 가능합니다.");
			}
			else {
				break;
			}
		} while(true);
		
		// 연락처
		do {
			System.out.print("▷ 연락처 : ");
			tel = sc.nextLine();
			
			if(ValidationUtil.isTelValid(tel)) {
				break;
			}
		} while(true);
		
		ApplicantDTO applicantDTO = new ApplicantDTO();
		
		applicantDTO.setApplicantId(applicantId);
		applicantDTO.setBirthday(birthday);
		applicantDTO.setEmail(email);
		applicantDTO.setGender(getGenderNumber(gender));
		applicantDTO.setName(name);
		applicantDTO.setPasswd(passwd);
		applicantDTO.setTel(tel);
		
		int n = applicantDAO.register(applicantDTO);
		
		if(n==1) {
			System.out.println(">> 회원가입이 완료되었습니다. <<");
		}
		else {
			System.out.println(">> 회원가입에 실패했습니다. <<");
		}
	}


	// === 구직자 로그인 === //
	public ApplicantDTO login(Scanner sc) {
		System.out.print("▷ 아이디 : ");
		String applicantId = sc.nextLine();
		
		System.out.print("▷ 비밀번호 : ");
		String passwd = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("applicantId", applicantId);
		paraMap.put("passwd", passwd);
		
		ApplicantDTO applicantDTO = applicantDAO.login(paraMap);
		
		if(applicantDTO != null) {
			System.out.println("\n>> 로그인이 완료되었습니다. <<\n");
		}
		else {
			System.out.println("\n>> 로그인에 실패하였습니다. <<\n");
		}
		
		return applicantDTO;
	}
	
	
	// === 구직자 메인 메뉴 === //
	public void applicantMenu(ApplicantDTO applicant, Scanner sc) {
		do {
			System.out.println("\n=======< 구직자 메인 메뉴("+applicant.getName()+"님 로그인 중) >=======\n"
							 + "1.내 정보 관리   2.이력서 관리   3.구인회사 찾기\n"
							 + "4.채용공고 찾기   5.입사지원 현황   6.리뷰 작성\n"
							 + "7.공지사항 조회\n"
							 + "0.로그아웃\n"
							 + "=====================================================\n");
			System.out.print("▷ 메뉴번호 선택 : ");
			String menu = sc.nextLine();
			
			switch (menu) {
			case "1": // 내 정보 관리
				// 김규빈
				break;
			
			case "2": // 이력서 관리
				// 이상우
				Resumecontroller resumecontroller= new Resumecontroller();
				resumecontroller.list_Resume(applicant); //구직자DTO를 인자값으로 넘기면 됨
				break;
			
			case "3": // 구인회사 찾기
				// 이지혜
				break;
			
			case "4": // 채용공고 찾기
				// 강이훈
				RecruitmentApplicantController raCtrl = new RecruitmentApplicantController();
				raCtrl.findRecruitment(applicant, sc); 
				break;
			
			case "5": // 입사지원 현황
				// 이상우
				Applycontroller applycontroller = new Applycontroller();
				applycontroller.showAllapply(applicant.getApplicantId() );
				break;
			
			case "6": // 리뷰 작성 - 김규빈
//				ReviewController reviewCtrl = new ReviewController();
//				reviewCtrl.reviewMenu(applicant, sc);
				break;
			
			case "7": // 공지사항 조회
				// 김진성
				break;
			
			case "0": // 로그아웃
				return;

			default:
				Msg.W("입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다.");
				break;
			}
			
		} while(true);
	}
	
	
	// === 성별 "남", "여"를 받아 0, 1로 반환해주는 메소드 === //
	private int getGenderNumber(String gender) {
		int genderNumber = -1;
		
		if("남".equals(gender)) {
			genderNumber = 0;
		}
		else if("여".equals(gender)) {
			genderNumber = 1;
		}
		
		return genderNumber;
	}
}
