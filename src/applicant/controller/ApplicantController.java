package applicant.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import applicant.domain.ApplicantDTO;
import applicant.model.*;
import apply.controller.ApplyController;
import company.controller.CompanyController;
import notification.controller.NotificationController;
import recruitment.controller.RecruitmentApplicantController;
import resume.controller.Resumecontroller;
import review.controller.ReviewController;
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
		do {
			System.out.print("▷ 성명 : ");
			name = sc.nextLine();
			if(!name.isBlank() && name.length()<10) {
				break;
			}
			else {
				Msg.W("이름은 공백없이 10자 이하로 입력해야 합니다.");
			}
		} while(true);

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
			System.out.println(">> 회원가입이 완료되었습니다.(*´▽`*) <<");
		}
		else if(n==-1){
			Msg.W("중복되는 아이디가 있어 회원가입이 불가합니다.");
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
	public void applicantMenu(ApplicantDTO applicantDTO, Scanner sc) {
		do {
			System.out.println("\n========< 구직자 메인 메뉴("+applicantDTO.getName()+"님 로그인 중) >=========\n"
							 + "1.회원정보 관리   2.이력서 관리   3.구인회사 찾기\n"
							 + "4.채용공고 찾기   5.입사지원 현황   6.리뷰 작성\n"
							 + "7.공지사항 조회\n"
							 + "0.로그아웃\n"
							 + "=".repeat(50));
			System.out.print("▷ 메뉴번호 선택 : ");
			String menu = sc.nextLine();
			
			switch (menu) {
			case "1": // 내 정보 관리
				// 김규빈
				boolean isLeave = getApplicant(applicantDTO, sc);
				if(isLeave) {
					return;
				}
				break;
			
			case "2": // 이력서 관리
				// 이상우
				Resumecontroller resumecontroller= new Resumecontroller();
				resumecontroller.list_Resume(applicantDTO);
				break;
			
			case "3": // 구인회사 찾기
				// 이지혜
				CompanyController companyCtrl = new CompanyController();
				companyCtrl.searchCompany(sc);
				break;
			
			case "4": // 채용공고 찾기
				// 강이훈
				RecruitmentApplicantController raCtrl = new RecruitmentApplicantController();
				raCtrl.findRecruitment(applicantDTO, sc); 
				break;
			
			case "5": // 입사지원 현황
				// 이상우
				ApplyController applyCtrl = new ApplyController();
				applyCtrl.showAllapply(applicantDTO.getApplicantId() );
				break;
			
			case "6": // 리뷰 작성
				// 김규빈
				ReviewController reviewCtrl = new ReviewController();
				reviewCtrl.reviewMenu(applicantDTO, sc);
				break;
			
			case "7": // 공지사항 조회
				// 김진성
				NotificationController notiCtrl = new NotificationController();
				notiCtrl.notificationMenu(null, false, sc);
				break;
			
			case "0": // 로그아웃
				return;

			default:
				Msg.W("입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다.");
				break;
			}
			
		} while(true);
	}
	

	// === 구직자 정보관리 === //
	private boolean getApplicant(ApplicantDTO applicantDTO, Scanner sc) {
		do {
			System.out.println("\n=== 회원정보 관리 ===");
			
			System.out.println(applicantDTO.toString());
			System.out.print("=============< 메뉴 >=============\n"
							 + "1.회원정보 수정   2.회원탈퇴   0.돌아가기\n"
							 + "==============================\n"
							 + "▷ 메뉴번호 선택 : ");
			
			String menu = sc.nextLine();
			
			switch (menu) {
			case "0": // 돌아가기
				return false;
			
			case "1": // 회원정보 수정
				updateApplicant(applicantDTO, sc);
				break;
			
			case "2": // 회원탈퇴
				boolean isLeave = deleteApplicant(applicantDTO.getApplicantId(), sc);
				if(isLeave) {
					return isLeave;
				}
				break;
	
			default:
				Msg.W("입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다.");
				break;
			}
		} while(true);
	}


	// === 구직자 정보수정 === //
	private void updateApplicant(ApplicantDTO applicantDTO, Scanner sc) {
		System.out.println("\n=== 회원정보 수정 ===");
		
		System.out.println(">> [주의사항] 변경하지 않고 예전의 데이터값을 그대로 사용하시려면 그냥 엔터하세요!! <<\n");
		
		String passwd, email, name, birthday, tel;
		
		// 비밀번호
		do {
			System.out.print("▷ 비밀번호[영문,숫자,특수문자 조합] : ");
			passwd = sc.nextLine();
			
			if(passwd.isEmpty() || ValidationUtil.isPasswordValid(passwd)) {
				break;
			}
		} while(true);
		
		// 이메일
		do {
			System.out.print("▷ 이메일 : ");
			email = sc.nextLine();
			
			if(email.isEmpty()) {
				email = applicantDTO.getEmail();
				break;
			}
			else if(ValidationUtil.isEmailValid(email)) {
				break;
			}
		} while(true);

		// 성명
		do {
			System.out.print("▷ 성명 : ");
			name = sc.nextLine();
			if(name.isEmpty()) {
				name = applicantDTO.getName();
				break;
			}
			else if(name.length()<10) {
				break;
			}
			else {
				Msg.W("이름은 공백없이 10자 이하로 입력해야 합니다.");
			}
		} while(true);

		// 생일
		do {
			System.out.print("▷ 생일[1999-01-01] : ");
			birthday = sc.nextLine();
			
			if(birthday.isEmpty()) {
				birthday = applicantDTO.getBirthday();
				break;
			}
			
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
		int gender = 0;
		do {
			System.out.print("▷ 성별[남/여] : ");
			String genderStr = sc.nextLine();
			
			if(genderStr.isEmpty()) {
				gender = applicantDTO.getGender();
				break;
			}
			else if(getGenderNumber(genderStr)==-1) {
				Msg.W("'남' 또는 '여'만 입력 가능합니다.");
			}
			else {
				gender = getGenderNumber(genderStr);
				break;
			}
		} while(true);
		
		// 연락처
		do {
			System.out.print("▷ 연락처 : ");
			tel = sc.nextLine();
			
			if(tel.isEmpty()) {
				tel = applicantDTO.getTel();
				break;
			}
			else if(ValidationUtil.isTelValid(tel)) {
				break;
			}
		} while(true);
		
		applicantDTO.setBirthday(birthday);
		applicantDTO.setEmail(email);
		applicantDTO.setGender(gender);
		applicantDTO.setName(name);
		applicantDTO.setTel(tel);
		
		String old_passwd = applicantDTO.getPasswd();
		applicantDTO.setPasswd(passwd);
		
		boolean yn = Msg.YN("회원정보 수정", sc);
		
		if(yn) {
			int n = applicantDAO.updateApplicant(applicantDTO);
			
			if(n==1) {
				if(passwd.isEmpty()) {
					applicantDTO.setPasswd(old_passwd);
				}
				else {
					applicantDTO.setPasswd(passwd.substring(0,3) + "*".repeat(passwd.length()-3));
				}
				System.out.println(">> 회원정보 수정이 완료되었습니다.(*´▽`*) <<");
			}
			else {
				System.out.println(">> 회원정보 수정에 실패했습니다. <<");
			}
		}
	}


	/*
	 * 구직자 회원탈퇴
	 * 회원탈퇴가 완료되면 true, 아니면 false
	 */
	private boolean deleteApplicant(String applicantId, Scanner sc) {

		System.out.println("\n=== 회원탈퇴 ===");

		boolean yn = Msg.YN("회원탈퇴", sc);
		
		if(yn) {
			int n = applicantDAO.deleteApplicant(applicantId);
			
			if(n==1) {
				Msg.N("회원탈퇴가 완료되었습니다.");
				return true;
			}
		}
		
		return false;
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
