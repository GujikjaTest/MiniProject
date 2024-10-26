package statistics.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import admin.domain.AdminDTO;
import applicant.model.ApplicantDAO;
import applicant.model.ApplicantDAO_imple;
import company.model.CompanyDAO;
import company.model.CompanyDAO_imple;
import recruitment.controller.RecruitmentController;
import resume.model.ResumeDAO;
import resume.model.ResumeDAO_imple;
import utils.AlignUtil;
import utils.DisplayScore;
import utils.Msg;

/*
 * 관리자 통계 조회를 위한 Controller
 */
public class StatisticsController {

	private final ApplicantDAO applicantDAO = new ApplicantDAO_imple(); // 구직자 DAO 구현체

	private final CompanyDAO companyDAO = new CompanyDAO_imple(); // 구인회사 DAO 구현체

	private final ResumeDAO resumeDAO = new ResumeDAO_imple(); // 이력서 DAO 구현체

	private StringBuilder sb = new StringBuilder(); // StringBuilder 객체 초기화

	/*
	 * 관리자 통계 메뉴
	 */
	public void statisticsMenu(AdminDTO adminDTO, Scanner sc) {
		String menu = null; // 메뉴번호

		do {
			System.out.println("\n=============== < 관리자 통계 메뉴(" + adminDTO.getName() + "님 로그인 중) > ===============");
			System.out.println("1.구직자 통계   2.구인회사 통계   3.채용공고 통계   0.돌아가기");
			System.out.println("=".repeat(64));

			System.out.print("▷ 메뉴번호 선택 : ");
			menu = sc.nextLine();

			switch (menu) {
			case "0": { // 돌아가기
				adminDTO = null;
				return;
			}
			case "1": { // 구직자 통계
				applicantStats(sc);
				break;
			}
			case "2": { // 구인회사 통계
				companyStats(sc);
				break;
			}
			case "3": { // 채용공고 통계
				RecruitmentController recruitmentCtrl = new RecruitmentController();
				recruitmentCtrl.recruitmentStatistics(sc);
				break;
			}
			default:
				Msg.W("메뉴에 없는 번호입니다.");
			}
		} while (!"0".equals(menu));
	}

	/*
	 * 구직자 통계 전체 구직자 남녀 비교 전체 구직자 경력 비교 전체 구직자 학력 비교 전체 구직자 희망 직종 비교
	 */
	private void applicantStats(Scanner sc) {
		String menu = null; // 메뉴 번호
		do {
			System.out.println("\n===================== < 구직자 통계 메뉴 > =========================");
			System.out.println("1.구직자 성별 통계   2.구직자 경력 통계   3.구직자 학력 통계\n4.구직자 희망직종 통계(이력서가 존재하는 구직자 한정)   0.돌아가기");
			System.out.println("=".repeat(64));

			System.out.print("▷ 메뉴번호 선택 : ");
			menu = sc.nextLine();

			switch (menu) {
			case "0": { // 돌아가기
				return;
			}
			case "1": { // 구직자 성별 통계
				getGenderRatio();
				break;
			}
			case "2": { // 구직자 경력 통계
				getExperienceRatio();
				break;
			}
			case "3": { // 구직자 학력 통계
				getEducationRatio();
				break;
			}
			case "4": { // 구직자 인기 희망직종 통계 (1~10순위)
				getHopeJobRatio();
				break;
			}
			default:
				Msg.W("메뉴에 없는 번호입니다.");
			}
		} while (!"0".equals(menu));

	}

	/*
	 * 구직자 성별 통계
	 */
	private void getGenderRatio() {
		Map<String, Integer> map = new HashMap<>(); // 구직자 성별 통계 값을 저장하는 Map
		sb.setLength(0);
		
		map = applicantDAO.getGenderRatio();

		sb.append("\n-< 구직자 성별 통계 >-------------------\n");
		sb.append(" ▣ 전체 : " + map.get("total") + "명\n");
		sb.append(" ▣ 남자 : " + map.get("men") + "명\n"); 
		sb.append(" ▣ 여자 : " + map.get("women") + "명\n\n"); 
		
		// int 타입 백분율 변환
		int men = getRatio(map.get("men"), map.get("total"));
		int women = getRatio(map.get("women"), map.get("total"));
		
		// 게이지바 적용
		sb.append(" ■ 남자 비율 " + DisplayScore.getBar(men) + "%\n");
		sb.append(" ■ 여자 비율 " + DisplayScore.getBar(women) + "%\n");
		
		sb.append("------------------------------------\n");
		
		System.out.println(sb.toString());
	}

	/*
	 * 구직자 경력 통계
	 */
	private void getExperienceRatio() {
		sb.setLength(0);
		Map<String, Integer> map = resumeDAO.getExperienceRatio(); // 구직자 경력/신입 통계 값을 저장하는 Map

		sb.append("\n-< 구직자 경력 통계 >-------------------\n");
		sb.append(" ▣ 전체 인원수(이력서를 작성한 구직자 대상) : " + map.get("total") + "명\n"); 
		sb.append(" ▣ 신입 : " + map.get("junior") + "명\n"); 
		sb.append(" ▣ 경력 : " + map.get("senior") + "명\n\n"); 
		
		// int 타입 백분율 변환
		int junior = getRatio(map.get("junior"), map.get("total"));
		int senior = getRatio(map.get("senior"), map.get("total"));
		
		// 게이지바 적용
		sb.append(" ■ 신입 비율 " + DisplayScore.getBar(junior) + "%\n");
		sb.append(" ■ 경력 비율 " + DisplayScore.getBar(senior) + "%\n");
		
		sb.append("------------------------------------\n");
		
		System.out.println(sb.toString());
	}

	/*
	 * 구직자 학력 통계
	 */
	private void getEducationRatio() {
		Map<String, Integer> map = resumeDAO.getEducationRatio(); // 구직자 학력 통계 값을 저장하는 Map
		sb.setLength(0);

		sb.append("\n-< 구직자 학력 통계 >-------------------\n");
		sb.append(" ▣ 전체(이력서를 작성한 구직자 대상) : " + map.get("total") + "명\n"); 
		sb.append(" ▣ 대졸 : " + map.get("0") + "명\n"); 
		sb.append(" ▣ 고졸 : " + map.get("1") + "명\n");
		sb.append(" ▣ 초대졸 : " + map.get("2") + "명\n");
		sb.append(" ▣ 대학교재학 : " + map.get("3") + "명\n");
		sb.append(" ▣ 대학교휴학 : " + map.get("4") + "명\n\n");
		
		// int 타입 백분율 변환
		int n1 = getRatio(map.get("0"), map.get("total")); // 대졸
		int n2 = getRatio(map.get("1"), map.get("total")); // 고졸
		int n3 = getRatio(map.get("2"), map.get("total")); // 초대졸
		int n4 = getRatio(map.get("3"), map.get("total")); // 대학교재학
		int n5 = getRatio(map.get("4"), map.get("total")); // 대학교휴학
				
		// 게이지바 적용
		sb.append(" ■ 대졸 비율" + "\t\t" + DisplayScore.getBar(n1) + "%\n");
		sb.append(" ■ 고졸 비율" + "\t\t" + DisplayScore.getBar(n2) + "%\n");
		sb.append(" ■ 초대졸 비율" + "\t\t" + DisplayScore.getBar(n3) + "%\n");
		sb.append(" ■ 대학교재학 비율" + "\t\t" + DisplayScore.getBar(n4) + "%\n");
		sb.append(" ■ 대학교휴학 비율" + "\t\t" + DisplayScore.getBar(n5) + "%\n");

		sb.append("------------------------------------\n");

		System.out.println(AlignUtil.tab(sb).toString());
	}

	/*
	 * 구직자 인기 희망직종 통계 (최대 10개)
	 */
	private void getHopeJobRatio() {
		List<String[]> list = new ArrayList<>(); // String[0] = [순위], String[1] = [직종명], String[2] = [비율]
		sb.setLength(0);

		System.out.println("\n-< 구직자 인기 희망직종 통계(최대 10개) >-");
		System.out.println("순위\t직종명\t\t비율(%)");
		System.out.println("-----------------------------------");

		list = resumeDAO.getHopeJobRatio();

		for (String[] arr : list) {
			String tab = "   \t"; // 균일한 문자 출력을 위한 탭 설정
			
			if(arr[1].length() < 4) {
				tab = "\t\t";
			}
			else if(arr[1].length() > 9) {
				tab = "\t";
			}
			
			sb.append(arr[0] + "\t" + arr[1] + tab + arr[2] + "%\n");
		}

		System.out.println(sb.toString());
	}

	/*
	 * 구인회사 통계
	 */
	private void companyStats(Scanner sc) {
		System.out.println("\n=== 구인회사 통계 ===\n");

		getBusinessTypeRatio(); // 기업형태별 회사 통계
		// TODO 이번달 채용공고 개수
		
	}

	/*
	 * 기업형태별 회사통계 
	 */
	private void getBusinessTypeRatio() {
		Map<String, Integer> map = new HashMap<>(); // 기업형태별로 회사 개수 통계
		sb.setLength(0);
		
		map = companyDAO.getBusinessTypeRatio(); // "big" : 대기업, "mid" : 중견기업, "small" : 중소기업
		
		sb.append("-< 기업형태별 통계 >------------------------\n");
		sb.append(" ▣ 전체 : " + map.get("total") + "개\n"); 
		sb.append(" ▣ 대기업 : " + map.get("big") + "개\n"); 
		sb.append(" ▣ 중견기업 : " + map.get("mid") + "개\n");
		sb.append(" ▣ 중소기업 : " + map.get("small") + "개\n\n");
		
		// int 타입 백분율 변환
		int big = getRatio(map.get("big"), map.get("total")); // 대기업
		int mid = getRatio(map.get("mid"), map.get("total")); // 중견기업
		int small = getRatio(map.get("small"), map.get("total")); // 중소기업
		
		// 게이지바 적용
		sb.append(" ■ 대기업 비율" + "\t\t" + DisplayScore.getBar(big) + "%\n");
		sb.append(" ■ 중견기업 비율" + "\t\t" + DisplayScore.getBar(mid) + "%\n");
		sb.append(" ■ 중소기업 비율" + "\t\t" + DisplayScore.getBar(small) + "%\n");
		
		sb.append("----------------------------------------\n");
		
		System.out.println(AlignUtil.tab(sb).toString());
	}
	
	/*
	 * 백분율 산정
	 * total : 전체, num : 개수
	 */
	private int getRatio(int num, int total) {
		// double 형변환
		double n = (double) num;
		double t = (double) total;
		
		// 형변환된 double에서 백분율 산출 후 int 타입으로 형변환
		return (int)Math.round(n/t*100);
	}
}
