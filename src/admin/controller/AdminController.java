package admin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import admin.domain.AdminDTO;
import admin.model.AdminDAO;
import admin.model.AdminDAO_imple;
import applicant.domain.ApplicantDTO;
import applicant.model.ApplicantDAO;
import applicant.model.ApplicantDAO_imple;
import common.Transaction;
import company.domain.CompanyDTO;
import company.model.CompanyDAO;
import company.model.CompanyDAO_imple;
import notification.controller.NotificationController;
import review.domain.ReviewAuthDTO;
import review.model.ReviewAuthDAO;
import review.model.ReviewAuthDAO_imple;
import statistics.controller.StatisticsController;
import utils.AlignUtil;
import utils.Msg;
import utils.ValidationUtil;

/*
 * 관리자 컨트롤러
 */
public class AdminController {

	private final AdminDAO adminDAO = new AdminDAO_imple(); // AdminDAO 구현체

	private final ApplicantDAO applicantDAO = new ApplicantDAO_imple(); // ApplicantDAO 구현체

	private final ReviewAuthDAO reviewAuthDAO = new ReviewAuthDAO_imple(); // ReviewAuthDAO 구현체
	
	private final CompanyDAO companyDAO = new CompanyDAO_imple(); // CompanyDAO 구현체

	private final NotificationController notificationController = new NotificationController(); // 공지사항 Controller 인스턴스
	
	private final StatisticsController statisticsController = new StatisticsController(); // 통계 조회 Controller 인스턴스

	private StringBuilder sb = new StringBuilder(); // StringBuilder 객체 초기화

	/*
	 * 관리자 로그인
	 */
	public AdminDTO login(Scanner sc) {
		Map<String, String> loginMap = new HashMap<>(); // 로그인 Map
		AdminDTO adminDTO = null; // 관리자 DTO 객체 초기화
		String adminId; // 관리자 아이디
		String passwd; // 관리자 비밀번호

		System.out.println("\n=== 관리자 로그인 ===");

		while (true) {
			System.out.print("아이디 : ");
			adminId = sc.nextLine();

			System.out.print("비밀번호 : ");
			passwd = sc.nextLine();

			// 아이디, 비밀번호 공백 검사
			if (adminId.isBlank() || passwd.isBlank()) {
				Msg.W("아이디 및 비밀번호는 공백이 될 수 없습니다.");
				continue;
			}

			// 관리자 로그인 DTO 객체 설정
			loginMap.put("adminId", adminId);
			loginMap.put("passwd", passwd);

			// 관리자 아이디, 비밀번호 인증
			// AdminDTO 객체를 반환하는 AdminDAO_imple login 메소드 실행
			// 아이디 또는 비밀번호가 일치하지 않는 경우 null 이 반환됨
			adminDTO = adminDAO.login(loginMap);

			if (adminDTO == null) {
				Msg.W("아이디 또는 비밀번호가 일치하지 않습니다.");
				continue;
			}

			break;
		}

		return adminDTO;
	}

	/*
	 * 관리자 전용메뉴
	 */
	public void adminMenu(AdminDTO adminDTO, Scanner sc) {
		String menu = null; // 메뉴번호

		do {
			System.out.println("\n================ 관리자 전용메뉴(" + adminDTO.getName() + "님 로그인 중) ===============");
			System.out.println("1.구직자 관리\t2.구인회사 관리\t3.구직자 회사 인증 관리\n4.공지사항 관리\t5.통계\t\t0.로그아웃");
			System.out.println("=".repeat(60));

			System.out.print("▷ 메뉴번호 선택 : ");
			menu = sc.nextLine();

			switch (menu) {
			case "0": { // 로그아웃
				adminDTO = null;
				return;
			}
			case "1": { // 구직자 관리
				applicantManage(sc);
				break;
			}
			case "2": { // 구인회사 관리
				companyManage(sc);
				break;
			}
			case "3": { // 구직자 회사 인증 관리 (구직자의 재직 이력 인증)
				manageReviewAuth(sc);
				break;
			}
			case "4": { // 공지사항 관리
				notificationController.notificationMenu(adminDTO, true, sc);
				break;
			}
			case "5": { // 통계
				statisticsController.statisticsMenu(adminDTO, sc);
				break;
			}
			default:
				Msg.W("메뉴에 없는 번호입니다.");
			}
		} while (!"0".equals(menu));
	}

	/*
	 * 구직자 관리
	 */
	private void applicantManage(Scanner sc) {
		String menu = null; // 메뉴번호

		do {
			System.out.println("\n=== 구직자 관리 ===");

			System.out.println("===============< 메뉴 >===============");
			System.out.println("1.성명 검색\t2.나이대 검색\n3.아이디 검색\t4.구직자 차단\n5.구직자 차단해제\t0.돌아가기");
			System.out.println("=====================================");

			System.out.print("▷ 메뉴번호 선택 : ");
			menu = sc.nextLine();

			switch (menu) {
			case "0": { // 돌아가기
				return;
			}
			case "1": { // 성명으로 구직자 검색
				getApplicantListByName(sc);
				break;
			}
			case "2": { // 나이대로 구직자 검색
				getApplicantListByAgeline(sc);
				break;
			}
			case "3": { // 아이디로 구직자 검색
				getApplicantById(sc);
				break;
			}
			case "4": { // 구직자 차단
				blockApplicant(true, sc);
				break;
			}
			case "5": { // 구직자 차단 해제
				blockApplicant(false, sc);
				break;
			}
			default:
				Msg.W("메뉴에 없는 번호입니다.");
			}
		} while (!"0".equals(menu));
	}
	
	/*
	 * 구인회사 관리
	 */
	private void companyManage(Scanner sc) {
		// TODO 구인회사 관리는 고민해보고 구현
		String menu = null; // 메뉴번호

		do {
			System.out.println("\n=== 구인회사 관리 ===");

			System.out.println("=====================< 메뉴 >========================");
			System.out.println("1.회사명\t2.업종별\t3.지역별\t0.돌아가기");
			System.out.println("====================================================");

			System.out.print("▷ 메뉴번호 선택 : ");
			menu = sc.nextLine();

			switch (menu) {
			case "0": { // 돌아가기
				return;
			}
			case "1": { // 회사명
				getCompanyByName(sc);
				break;
			}
			case "2": { // 업종별
				getCompanyByIndustry(sc);
				break;
			}
			case "3": { // 지역별
				getCompanyByAddr(sc);
				break;
			}
			default:
				Msg.W("메뉴에 없는 번호입니다.");
			}
		} while (!"0".equals(menu));
	}

	/*
	 * 구직자 회사 인증 관리 (구직자의 재직 이력 인증)
	 */
	private void manageReviewAuth(Scanner sc) {
		String menu = null; // 메뉴 번호

		do {
			System.out.println("\n=== 구직자 회사인증 관리 ===");
			
			List<ReviewAuthDTO> reviewAuthList = getReqReviewAuthList();// 현재 대기중인 회사 인증 요청 목록

			if (reviewAuthList.size() < 1) {
				System.out.println(">> 현재 요청중인 회사인증이 없습니다. <<\n");
				return;
			}

			System.out.println("============< 메뉴 >=============");
			System.out.println("1.회사인증 요청 관리 \t0.돌아가기");
			System.out.println("================================");

			System.out.print("▷ 메뉴번호 선택 : ");
			menu = sc.nextLine();

			switch (menu) {
			case "0": { // 돌아가기
				return;
			}
			case "1": { // 회사인증 요청 관리
				manageRequest(reviewAuthList, sc);
				break;
			}
			default:
				Msg.W("메뉴에 없는 번호입니다.");
			}
		} while (!"0".equals(menu));
	}


	/*
	 * 성명으로 구직자 검색
	 */
	private void getApplicantListByName(Scanner sc) {
		sb.setLength(0); // StringBuilder 객체 초기화
		String name = null; // 구직자 성명
		List<ApplicantDTO> applicantList = new ArrayList<>(); // 구직자 리스트

		System.out.print("▷ 성명 입력 : ");
		name = sc.nextLine();

		// ApplicantDAO_imple에서 해당 성명의 구직자 반환
		applicantList = applicantDAO.getApplicantListByName(name);

		// 구직자가 존재하지 않는 경우
		if (applicantList.size() < 1) {
			System.out.println(">> 조회된 결과가 없습니다. <<\n");
			return;
		}

		for (ApplicantDTO applicantDTO : applicantList) {
			sb.append(applicantDTO.toString());
		}

		System.out.println("\n" + sb.toString());
	}

	/*
	 * 나이대로 구직자 검색
	 */
	private void getApplicantListByAgeline(Scanner sc) {
		sb.setLength(0); // StringBuilder 객체 초기화
		String ageline = null; // 나이대
		String tab = ""; // 출력 간격을 조절하기 위한 tab
		List<ApplicantDTO> applicantList = new ArrayList<>(); // 구직자 리스트

		while (true) {
			System.out.print("▷ 나이대 입력[10의 자리수] : ");
			ageline = sc.nextLine();

			// 나이대 유효성 검사
			// 10의 자리수 10, 20, 30, 40, 50 ...
			if (!ValidationUtil.isNumberValid(ageline)) {
				continue;
			}

			if (Integer.parseInt(ageline) % 10 != 0) {
				Msg.W("나이대는 10의 자리수로 입력해야 합니다.");
				continue;
			}

			break;
		}

		// applicantDAO_imple에서 해당 나이대의 구직자 목록 반환
		applicantList = applicantDAO.getApplicantListByAgeline(ageline);

		if (applicantList.size() < 1) {
			System.out.println(">> 조회된 결과가 없습니다. <<\n");
			return;
		}

		System.out.println("\n-< " + ageline + "대의 구직자 목록 >" + "-".repeat(41));
		System.out.println("이름\t아이디\t\t나이\t성별\t핸드폰 번호\t상태");
		System.out.println("-".repeat(60));

		for (ApplicantDTO applicantDTO : applicantList) {
			tab = applicantDTO.getApplicantId().length() < 8 ? "\t\t" : "\t"; // tab 간격 조절
			
			sb.append(applicantDTO.getName() + "\t");
			sb.append(applicantDTO.getApplicantId() + tab);
			sb.append(getAge(applicantDTO.getBirthday()) + "\t");
			sb.append(Transaction.gender(applicantDTO.getGender()) + "\t");
			sb.append(applicantDTO.getTel() + "\t");
			sb.append(Transaction.memberStatus(applicantDTO.getStatus()) + "\n");
		}

		System.out.println(sb.toString());
	}

	/*
	 * 구직자 아이디로 구직자 검색
	 */
	private void getApplicantById(Scanner sc) {
		String applicantId = null; // 구직자 아이디

		System.out.print("▷ 아이디 입력 : ");
		applicantId = sc.nextLine();

		// ApplicantDAO_imple에서 해당 아이디의 구직자 반환
		ApplicantDTO applicantDTO = applicantDAO.getApplicantById(applicantId);

		// 구직자가 존재하지 않는 경우
		if (applicantDTO == null) {
			System.out.println(">> 조회된 결과가 없습니다. <<\n");
			return;
		}

		System.out.println("\n" + applicantDTO.toString());
	}

	/*
	 * 구직자 차단 관리 block = true : 차단, block = false : 차단 해제
	 */
	private void blockApplicant(boolean block, Scanner sc) {
		String applicantId = null; // 구직자 아이디
		String isblock = block ? "차단" : "차단해제";
		int result = 0; // DML 쿼리 결과

		System.out.println("\n=== 구직자 차단 관리 ===");

		System.out.print("▷ 아이디 입력 : ");
		applicantId = sc.nextLine();

		// ApplicantDAO_imple에서 해당 아이디의 구직자 차단
		result = applicantDAO.blockApplicant(applicantId, block);

		// 구직자가 존재하지 않는 경우
		// 1 : 정상처리, 2 : 이미 처리된 상태, 0 : 처리실패
		if (result == 1) {
			System.out.println(">> 구직자 아이디 " + applicantId + "의 " + isblock + "이 완료되었습니다. ദ്ദി*ˊᗜˋ*) <<\n");
		} 
		else if(result == 2) {
			System.out.println(">> 구직자 아이디 " + applicantId + "의 " + isblock + "은(는) 이미 처리된 상태입니다. <<\n");
		}
		else {
			System.out.println(">> 구직자 아이디 " + applicantId + "의 " + isblock + "(을)를 실패하였습니다. <<\n");
		}
	}
	
	/*
	 * 회사명으로 회사 검색
	 */
	private void getCompanyByName(Scanner sc) {
		String companyName = null; // 회사명
		List<CompanyDTO> companyList = new ArrayList<>();
		
		System.out.print("▷ 회사명 입력 : ");
		companyName = sc.nextLine();
		
		companyList = companyDAO.companyNameList(companyName);
		
		if(companyList.size() < 1) {
			System.out.println(">> 조회된 결과가 없습니다. <<\n");
			return;
		}
		
		companyInfo(companyList);
	}
	
	/*
	 * 업종명으로 회사 검색
	 */
	private void getCompanyByIndustry(Scanner sc) {
		String businessType = null; // 업종명
		List<CompanyDTO> companyList = new ArrayList<>();
		
		System.out.print("▷ 업종명 입력 : ");
		businessType = sc.nextLine();
		
		companyList = companyDAO.companyIndustryList(businessType);
		
		if(companyList.size() < 1) {
			System.out.println(">> 조회된 결과가 없습니다. <<\n");
			return;
		}
		
		companyInfo(companyList);
	}
	
	/*
	 * 주소로 회사 검색
	 */
	private void getCompanyByAddr(Scanner sc) {
		String addr = null; // 회사명
		List<CompanyDTO> companyList = new ArrayList<>();
		
		System.out.print("▷ 주소 입력 : ");
		addr = sc.nextLine();
		
		companyList = companyDAO.companyAddressList(addr);
		
		if(companyList.size() < 1) {
			System.out.println(">> 조회된 결과가 없습니다. <<\n");
			return;
		}
		
		companyInfo(companyList);
	}

	/*
	 * 회사인증 요청 관리 요청된 인증을 허가 또는 거부할 수 있다.
	 */
	private void manageRequest(List<ReviewAuthDTO> reviewAuthList, Scanner sc) {
		ReviewAuthDTO newReviewAuthDTO = new ReviewAuthDTO(); // DAO 메소드에 파라미터로 지정할 DTO
		String reviewAuthId = null; // 회사 인증 순번
		int count = 0; // 해당 순번이 회사인증 요청 목록에 존재하는지 확인하기 위한 카운트
		String yn = null; // 요청에 대한 허가/거부 입력 문자열
		int result = 0; // DML 쿼리 결과

		while (true) {
			System.out.print("▷ 순번 입력 : ");
			reviewAuthId = sc.nextLine();

			// 순번 유효성 검사
			if (!ValidationUtil.isNumberValid(reviewAuthId)) {
				continue;
			}

			break;
		}

		// 회사인증 목록을 순회하며 해당 순번에 포함된 회사인증이 존재하는지 확인
		for (ReviewAuthDTO reviewAuthDTO : reviewAuthList) {
			if (reviewAuthDTO.getReviewAuthId() == Integer.parseInt(reviewAuthId)) {
				count++;
			}
		}

		if (count != 1) {
			System.out.println(">> 입력하신 순번 " + reviewAuthId + "에 해당하는 회사인증 요청 건이 존재하지 않습니다. <<");
			return;
		}

		while (true) {
			System.out.print("▷ 회사인증을 허가하시겠습니까?[Y(허가)/N(거부)] : ");
			yn = sc.nextLine();

			if (!ValidationUtil.isYNValid(yn)) {
				continue;
			}

			break;
		}

		// 허용이면 1 거부이면 2를 적용한다.
		int status = "y".equalsIgnoreCase(yn) ? 1 : 2;
		
		newReviewAuthDTO.setIsPermitted(status);
		newReviewAuthDTO.setReviewAuthId(Integer.parseInt(reviewAuthId));
		
		result = reviewAuthDAO.manageRequest(newReviewAuthDTO);

		if (result == 1) {
			System.out.println(">> 회사인증 처리가 완료되었습니다. ദ്ദി*ˊᗜˋ*) <<\n");
		} else {
			System.out.println(">> 회사인증 처리를 실패하였습니다. <<\n");
		}
	}

	/*
	 * 현재 대기중인 회사 인증 요청 목록 조회 및 목록 반환
	 */
	private List<ReviewAuthDTO> getReqReviewAuthList() {
		List<ReviewAuthDTO> reviewAuthList = new ArrayList<>();
		String tab = ""; // 출력 간격을 조절하기 위한 tab
		sb.setLength(0); // StringBuilder 초기화

		System.out.print("\n"+AlignUtil.title("-현재 대기중인 회사 인증 요청 목록", 70));
		System.out.println("순번\t구직자 성명\t구직자 아이디\t회사명\t\t회사아이디");
		System.out.println("-".repeat(70));

		// ReviewAuthDAO_imple로부터 회사 인증 요청 목록 반환
		reviewAuthList = reviewAuthDAO.getReqReviewAuthList();

		for (ReviewAuthDTO reviewAuthDTO : reviewAuthList) {
			sb.append(reviewAuthDTO.getReviewAuthId() + "\t");
			sb.append(reviewAuthDTO.getApplicantName() + "\t\t");
			sb.append(reviewAuthDTO.getFkApplicantId() + "\t\t");
			sb.append(reviewAuthDTO.getCompanyName() + "\t\t");
			sb.append(reviewAuthDTO.getFkCompanyId() + "\n");
		}

		System.out.println(AlignUtil.tab(sb).toString());

		return reviewAuthList;
	}

	/*
	 * 생일에서 나이를 반환하는 메소드
	 */
	private int getAge(String birthday) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		int thisYear = cal.get(Calendar.YEAR); // 이번년도
		String thisYearBirthDay = thisYear + birthday.substring(4); // 올해생일

		int age = 0; // 나이
		Date today = new Date();

		Date birth; // 생년월일
		Date thisYearBirth; // 올해생일
		try {
			birth = sdf.parse(birthday); // 생년월일 Date 객체
			cal.setTime(birth);
			age = thisYear - cal.get(Calendar.YEAR); // 이번년도 - 태어난 년도

			thisYearBirth = sdf.parse(thisYearBirthDay); // 올해 생일 Date 객체

			// 만나이 처리
			if (thisYearBirth.after(today)) {
				age -= 1;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return age;
	}
	
	/*
	 * 회사 목록 출력
	 */
	private void companyInfo(List<CompanyDTO> companyList) {
		sb.setLength(0);

		for(CompanyDTO companyDTO : companyList) {
			sb.append("-------------------------------------------------\n"	  
						+ "▣ 아이디 : " + companyDTO.getCompanyId() + "\n"
						+ "▣ 이메일 : " + companyDTO.getEmail() + "\n"
						+ "▣ 회사명 : " + companyDTO.getName() + "\n"
						+ "▣ 사업자등록번호 : " + companyDTO.getBusinessNo() + "\n"
						+ "▣ 기업형태 : " + companyDTO.getBusinessTypeStr() + "\n"
						+ "▣ 주소 : " + companyDTO.getAddress() + "\n"
						+ "▣ 연락처 : " + companyDTO.getTel() + "\n"
						+ "▣ 업종 : " + companyDTO.getIndustry() + "\n"
						+ "▣ 가입상태 : " + Transaction.memberStatus(companyDTO.getStatus()) + "\n"
						+ "-------------------------------------------------\n"
					);
		}
		System.out.println("\n" + sb.toString());
	}
}
