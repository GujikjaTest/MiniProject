package review.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import applicant.domain.ApplicantDTO;
import company.domain.CompanyDTO;
import job.controller.JobController;
import review.domain.ReviewDTO;
import review.model.ReviewDAO;
import review.model.ReviewDAO_imple;
import utils.DisplayScore;
import utils.Msg;

public class ReviewController {
	
	// field
	ReviewDAO reviewDAO = new ReviewDAO_imple();
	
	// method
	
	// === 구직자 - 리뷰 작성 메뉴 === //
	public void reviewMenu(ApplicantDTO applicantDTO, Scanner sc){
		
		do {
			System.out.println("\n=== 리뷰 작성 ===");
			// 리뷰 목록 보여주기
			boolean isReviewExist = getApplicantReview(applicantDTO.getApplicantId());
			String getReviewMenuStr = isReviewExist?"2.작성한 리뷰 상세보기   " : "";
			
			System.out.println("===================< 메뉴 선택 >====================\n"
					   		 + "1.새로운 리뷰 작성하기   "+getReviewMenuStr+"0.돌아가기\n"
					   		 + "=".repeat(50));
			
			
			System.out.print("▷ 메뉴 번호 선택 : ");
			String menu = sc.nextLine();
			
			
			switch (menu) {
			case "0": // 돌아가기
				return;
			
			case "1": // 새로운 리뷰 작성하기
				registerReview(applicantDTO.getApplicantId(), sc);
				break;

			case "2": // 작성한 리뷰 상세보기
				if(isReviewExist) {
					getReview(sc);
					break;
				}

			default:
				Msg.W("입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다.");
				break;
			}
		} while(true);
	}


	/*
	 * 구직자 1명의 리뷰 보기
	 * 리뷰가 있으면 true, 없으면 false 반환
	 */
	private boolean getApplicantReview(String applicantId) {

		List<ReviewDTO> reviewList = reviewDAO.getApplicantReviewList(applicantId);
		
		if(reviewList.size() == 0) {
			Msg.N("리뷰 목록이 없습니다.");
			return false;
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append("-< 작성한 리뷰 목록 >--------------------------------\n"
					 + "번호\t회사명\t총점\t내용\t\t최종수정일\n"
					 + "-".repeat(50)+"\n");
			
			for(int i=0; i<reviewList.size(); i++) {
				sb.append(reviewList.get(i).getReviewId() + "\t"
						+ reviewList.get(i).getCompanyName() + "\t"
						+ DisplayScore.getStar(reviewList.get(i).getScore()) + "\t"
						+ align(reviewList.get(i).getContents(), 15)
						+ reviewList.get(i).getUpdateday() + "\n");
			}
			
			sb.append("-".repeat(50));
			System.out.println(sb.toString());
			return true;
		}
		
	}
	
	/*
	 * 제목 정렬을 위한 메소드
	 * 숫자만큼 뒤에 띄어쓰기를 맞춰준다.
	 */
	private String align(String str, int n) {
		return str +" ".repeat(n-str.length());
	}
	
	
	// === 구직자 - 리뷰 상세보기 === //
	private void getReview(Scanner sc) {
		
		System.out.print("▷ 리뷰 번호 선택 : ");
		String reviewIdStr = sc.nextLine();
		int reviewId;
		try {
			reviewId = Integer.parseInt(reviewIdStr);
		} catch(NumberFormatException e) {
			Msg.W("숫자만 입력 가능합니다.");
			return;
		}
		
		System.out.println("=== 작성한 리뷰 상세보기 ===");
		ReviewDTO reviewDTO = reviewDAO.getReview(reviewId);
		
		System.out.println("\n"+reviewDTO.toString());
		
		System.out.print("==================< 메뉴 선택 >=====================\n"
					   + "1.리뷰 수정하기   2.리뷰 삭제하기   0.돌아가기\n"
					   + "==================================================\n"
					   + "▷ 메뉴 번호 선택 : ");
		String menu = sc.nextLine();
		switch (menu) {
		case "0":
			return;
		
		case "1":
			updateReview(reviewDTO, sc);
			break;
		
		case "2":
			deleteReview(reviewDTO.getReviewId(), sc);
			break;

		default:
			break;
		}
	}


	// === 구직자 - 리뷰 작성하기 === //
	private void registerReview(String applicantId, Scanner sc) {
		System.out.println("\n=== 새로운 리뷰 작성하기 ===");
		
		// 인증된 회사 목록 출력
		ReviewAuthController raCtrl = new ReviewAuthController();
		Map<String, String> reviewAuthMap = raCtrl.selectReviewAuth(applicantId, sc);
		
		if(reviewAuthMap == null) { // 인증회사 목록이 하나도 없다면 리뷰 작성 종료
			return;
		}
		
		// TODO 직종목록 출력
		JobController jobCtrl = new JobController();
		int jobId = jobCtrl.jobShowList(sc);
		
		int scoreSalary = research("[질문1] 이 회사의 연봉은 성과와 경력에 적합하다고 생각하나요?", sc, false);
		int scoreWlb = research("[질문2] 이 회사에서 일과 삶의 균형이 적절하게 유지되나요?", sc, false);
		int scoreCulture = research("[질문3] 이 회사의 문화가 업무를 하는데 긍정적이라고 생각하나요?\n"
								+ "(업무 방식, 근무 환경, 조직 분위기 등)", sc, false);
		int scoreWelfare = research("[질문4] 이 회사의 복지 제도는 업무 능력 향상에 도움이 되는 수준인가요?", sc, false);
		int scoreStability = research("[질문5] 이 회사는 앞으로도 발전할까요?", sc, false);
		int scoreImprove = research("[질문6] 이 회사에서 일하는 경험은 나의 경력에 긍정적이라고 생각하나요?", sc, false);
		
		int score;
		do {
			System.out.print("▷ 총점[1~5] : ");
			String scoreStr = sc.nextLine();
			scoreStr = scoreStr.replace("점", "");
			if(isScoreValid(scoreStr)) {
				score = Integer.parseInt(scoreStr);
				break;
			}
			else {
				Msg.W("1~5점 중에 입력하세요.");
			}
		} while(true);
		
		System.out.print("▷ 총평[최대 200자] : ");
		String contents = sc.nextLine();
		
		ReviewDTO reviewDTO = new ReviewDTO();
		
		reviewDTO.setFkApplicantId(reviewAuthMap.get("fk_applicant_id"));
		reviewDTO.setFkCompanyId(reviewAuthMap.get("fk_company_id"));
		reviewDTO.setFkJobId(jobId);
		reviewDTO.setScore(score);
		reviewDTO.setScoreSalary(scoreSalary);
		reviewDTO.setScoreWlb(scoreWlb);
		reviewDTO.setScoreCulture(scoreCulture);
		reviewDTO.setScoreWelfare(scoreWelfare);
		reviewDTO.setScoreStability(scoreStability);
		reviewDTO.setScoreImprove(scoreImprove);
		reviewDTO.setContents(contents);
		
		int n = reviewDAO.registerReview(reviewDTO);
		
		if(n==1) {
			Msg.N("리뷰 등록이 완료되었습니다.");
			// TODO 결과 출력
		}
		else {
			Msg.N("리뷰 등록에 실패했습니다.");
		}
	}


	// === 구직자 - 리뷰 수정하기 === //
	private void updateReview(ReviewDTO reviewDTO, Scanner sc) {
		
		System.out.println(">> [주의사항] 변경하지 않고 예전의 데이터값을 그대로 사용하시려면 그냥 엔터하세요!! <<\n");
		
		// TODO 직종목록 출력
		JobController jobCtrl = new JobController();
		int jobId = jobCtrl.jobUpdateList(reviewDTO.getFkJobId(), sc);
		
		int scoreSalary = research("[질문1] 이 회사의 연봉은 성과와 경력에 적합하다고 생각하나요?", sc, true);
		if(scoreSalary == 0) {
			scoreSalary = reviewDTO.getScoreSalary();
		}
		int scoreWlb = research("[질문2] 이 회사에서 일과 삶의 균형이 적절하게 유지되나요?", sc, true);
		if(scoreWlb == 0) {
			scoreWlb = reviewDTO.getScoreWlb();
		}
		int scoreCulture = research("[질문3] 이 회사의 문화가 업무를 하는데 긍정적이라고 생각하나요?\n"
								+ "(업무 방식, 근무 환경, 조직 분위기 등)", sc, true);
		if(scoreCulture == 0) {
			scoreCulture = reviewDTO.getScoreCulture();
		}
		int scoreWelfare = research("[질문4] 이 회사의 복지 제도는 업무 능력 향상에 도움이 되는 수준인가요?", sc, true);
		if(scoreWelfare == 0) {
			scoreWelfare = reviewDTO.getScoreWelfare();
		}
		int scoreStability = research("[질문5] 이 회사는 앞으로도 발전할까요?", sc, true);
		if(scoreStability == 0) {
			scoreStability = reviewDTO.getScoreStability();
		}
		int scoreImprove = research("[질문6] 이 회사에서 일하는 경험은 나의 경력에 긍정적이라고 생각하나요?", sc, true);
		if(scoreImprove == 0) {
			scoreImprove = reviewDTO.getScoreImprove();
		}
		
		int score;
		do {
			System.out.print("▷ 총점[1~5] : ");
			String scoreStr = sc.nextLine();
			scoreStr = scoreStr.replace("점", "");
			if(scoreStr.isEmpty()) {
				score = reviewDTO.getScore();
				break;
			}
			if(isScoreValid(scoreStr)) {
				score = Integer.parseInt(scoreStr);
				break;
			}
			else {
				Msg.W("1~5점 중에 입력하세요.");
			}
		} while(true);
		
		System.out.print("▷ 총평[최대 200자] : ");
		String contents = sc.nextLine();
		
		if(contents.isEmpty()) {
			contents = reviewDTO.getContents();
		}
		
		reviewDTO.setFkJobId(jobId);
		reviewDTO.setScore(score);
		reviewDTO.setScoreSalary(scoreSalary);
		reviewDTO.setScoreWlb(scoreWlb);
		reviewDTO.setScoreCulture(scoreCulture);
		reviewDTO.setScoreWelfare(scoreWelfare);
		reviewDTO.setScoreStability(scoreStability);
		reviewDTO.setScoreImprove(scoreImprove);
		reviewDTO.setContents(contents);
		
		int n = reviewDAO.updateReview(reviewDTO);
		
		if(n==1) {
			Msg.N("리뷰 수정이 완료되었습니다.");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
			reviewDTO.setUpdateday(sdf.format(new Date()));
			System.out.println(reviewDTO.toString());
		}
		else {
			Msg.N("리뷰 수정에 실패했습니다.");
		}
		
	}


	// === 구직자 - 리뷰 삭제하기 === //
	private void deleteReview(int reviewId, Scanner sc) {
		
		do {
			System.out.print("▷ 정말로 리뷰를 삭제하시겠습니까?[Y/N] : ");
			String yn = sc.nextLine();
			
			if("y".equalsIgnoreCase(yn)) {
				break;
			}
			else if("n".equalsIgnoreCase(yn)){
				Msg.N("리뷰 삭제를 취소하였습니다.");
				return;
			}
			else {
				Msg.W("Y 또는 N 만 입력하세요.");
			}
		} while(true);
		
		int n = reviewDAO.deleteReview(reviewId);
		
		if(n==1) {
			Msg.N("리뷰 삭제가 완료되었습니다.");
		}
		else {
			Msg.N("리뷰 삭제에 실패했습니다.");
		}
	}
	

	/*
	 * 회사 리뷰 확인 메뉴
	 */
	public void reviewCompanyMenu(String companyId, Scanner sc){
		
		System.out.println("=== 리뷰 상세보기 ===");
		// 리뷰 목록 보여주기
		boolean isReviewExist = getCompanyReviewAvg(companyId);
		if(isReviewExist) {
			do {
				System.out.print("=========< 메뉴 선택 >==========\n"
						+ "1.모든 리뷰 조회   0.돌아가기\n"
						+ "=".repeat(30)+"\n"
						+ "▷ 메뉴 번호 선택 : ");
				String menu = sc.nextLine();
				switch (menu) {
				case "0":
					return;
				
				case "1":
					getCompanyReview(companyId);
					return;

				default:
					Msg.W("입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다.");
					break;
				}
			} while(true);
		}
	}

	/*
	 * 한 회사의 리뷰 보기
	 * 리뷰가 있으면 true, 없으면 false 반환
	 */
	public boolean getCompanyReview(String companyId) {

		List<ReviewDTO> reviewList = reviewDAO.getCompanyReviewList(companyId);
		
		if(reviewList.size() == 0) {
			Msg.N("리뷰 목록이 없습니다.");
			return false;
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append("\n=== 리뷰 목록 ===\n");
			
			for(int i=0; i<reviewList.size(); i++) {
				sb.append("-< "+reviewList.get(i).getApplicantName()+"님의 리뷰 >----------------------------------\n");
				sb.append("직종\t" + reviewList.get(i).getJobName() + "\n"
						+ "총점\t" + DisplayScore.getStar(reviewList.get(i).getScore()) + "\n"
						+ "최종수정일" + reviewList.get(i).getUpdateday() + "\n"
						+ "내용\t"+reviewList.get(i).getContents() + "\n"
						+ "-".repeat(50)+"\n");
			}
			System.out.println(sb.toString());
			return true;
		}
		
	}
	

	/*
	 * 한 회사의 리뷰 통계
	 * 리뷰가 있으면 true, 없으면 false 반환
	 */
	public boolean getCompanyReviewAvg(String companyId) {

		Map<String, String> reviewAvgMap = reviewDAO.getCompanyReviewAvg(companyId);
		
		if(reviewAvgMap == null) {
			Msg.N("리뷰 목록이 없습니다.");
			return false;
		}
		else {
			StringBuilder sb = new StringBuilder();
			
			sb.append("-< '"+reviewAvgMap.get("company_name")+"'의 최근 5년간 리뷰 통계 >---------------------\n");
			sb.append("총점\t" + DisplayScore.getStar(Float.parseFloat(reviewAvgMap.get("score"))) + "\n"
					+ "연봉·급여\t" + DisplayScore.getBar(Integer.parseInt(reviewAvgMap.get("score_salary"))) + "\n"
					+ "워라밸\t" + DisplayScore.getBar(Integer.parseInt(reviewAvgMap.get("score_wlb"))) + "\n"
					+ "조직문화\t" + DisplayScore.getBar(Integer.parseInt(reviewAvgMap.get("score_culture"))) + "\n"
					+ "복리후생\t" + DisplayScore.getBar(Integer.parseInt(reviewAvgMap.get("score_welfare"))) + "\n"
					+ "고용안정성\t" + DisplayScore.getBar(Integer.parseInt(reviewAvgMap.get("score_stability"))) + "\n"
					+ "커리어성장\t" + DisplayScore.getBar(Integer.parseInt(reviewAvgMap.get("score_improve"))) + "\n"
					+ "-".repeat(50));
			System.out.println(sb.toString());
			return true;
		}
		
	}


	/*
	 * 설문조사 문항 출력 함수
	 * 선택한 번호를 int 형식으로 반환
	 */
	private int research(String title, Scanner sc, boolean isEdit) {
		String selectStr;
		int result = 0;
		
		do {
			System.out.println("\n"+title);
			
			System.out.print("=================< 만족도 선택 >===================\n"
						   + "① 매우 그렇다\n"
						   + "② 그렇다\n"
						   + "③ 보통이다\n"
						   + "④ 그렇지 않다\n"
						   + "⑤ 전혀 그렇지 않다\n"
						   + "===================================================\n"
						   + "▷ 번호 선택 : ");
	
			selectStr = sc.nextLine();
			selectStr = selectStr.replace("번", "");
			
			if(isScoreValid(selectStr)) {
				int selectNum = Integer.parseInt(selectStr);
				result = 6-selectNum;
				break;
			}
			else if(isEdit && selectStr.isEmpty()) {
				break;
			}
			else {
				Msg.W("1~5번 중에 선택하세요.");
			}
		} while(true);
		
		return result;
	}
	
	
	/*
	 * 점수가 1~5사이 값인지 확인하는 메소드
	 * 맞으면 true 아니면 false 반환
	 */
	private boolean isScoreValid(String number) {
		String regex = "^[1-5]{1}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(number);

		if (!matcher.find()) {
			return false;
		}
		
		return true;
	}
	
	
}
