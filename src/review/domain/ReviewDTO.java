package review.domain;

import utils.AlignUtil;
import utils.DisplayScore;

public class ReviewDTO {

	// field
	// insert field
	private int reviewId;          // 리뷰일련번호
	private String fkCompanyId;    // 회사아이디
	private String fkApplicantId;  // 구직자아이디
	private int fkJobId;           // 직종일련번호
	private int score;             // 전체별점(1~5)
	private String contents;       // 내용
	private String registerday;    // 기입시간
	private String updateday;      // 최신수정일
	private int scoreSalary;       // 연봉·급여점수(1~5)
	private int scoreWlb;          // 워라밸점수(1~5)
	private int scoreCulture;      // 조직문화점수(1~5)
	private int scoreWelfare;      // 복리후생점수(1~5)
	private int scoreStability;    // 고용안정성점수(1~5)
	private int scoreImprove;      // 커리어성장점수(1~5)
	private int isDelete;          // 삭제여부(0:유지 1:삭제)
	
	// select field
	private String companyName;    // 회사명
	private String applicantName;  // 구직자성명
	private String jobName;        // 직종명

	// method
	public int getReviewId() {
		return reviewId;
	}
	
	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}
	
	public String getFkCompanyId() {
		return fkCompanyId;
	}
	
	public void setFkCompanyId(String fkCompanyId) {
		this.fkCompanyId = fkCompanyId;
	}
	
	public String getFkApplicantId() {
		return fkApplicantId;
	}
	
	public void setFkApplicantId(String fkApplicantId) {
		this.fkApplicantId = fkApplicantId;
	}
	
	public int getFkJobId() {
		return fkJobId;
	}

	public void setFkJobId(int fkJobId) {
		this.fkJobId = fkJobId;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getContents() {
		if(contents == null) {
			return "(내용 없음)";
		}
		else {
			return AlignUtil.contents(contents);
		}
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public String getRegisterday() {
		return registerday;
	}
	
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	
	public String getUpdateday() {
		if(updateday == null) {
			return registerday;
		}
		else {
			return updateday;
		}
	}
	
	public void setUpdateday(String updateday) {
		this.updateday = updateday;
	}
	
	public int getScoreSalary() {
		return scoreSalary;
	}
	
	public void setScoreSalary(int scoreSalary) {
		this.scoreSalary = scoreSalary;
	}
	
	public int getScoreWlb() {
		return scoreWlb;
	}
	
	public void setScoreWlb(int scoreWlb) {
		this.scoreWlb = scoreWlb;
	}
	
	public int getScoreCulture() {
		return scoreCulture;
	}
	
	public void setScoreCulture(int scoreCulture) {
		this.scoreCulture = scoreCulture;
	}
	
	public int getScoreWelfare() {
		return scoreWelfare;
	}
	
	public void setScoreWelfare(int scoreWelfare) {
		this.scoreWelfare = scoreWelfare;
	}
	
	public int getScoreStability() {
		return scoreStability;
	}
	
	public void setScoreStability(int scoreStability) {
		this.scoreStability = scoreStability;
	}
	
	public int getScoreImprove() {
		return scoreImprove;
	}
	
	public void setScoreImprove(int scoreImprove) {
		this.scoreImprove = scoreImprove;
	}
	
	public int getIsDelete() {
		return isDelete;
	}
	
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	
	/////////////////////////////////////////////////////

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	/////////////////////////////////////////////////////

	@Override
	public String toString() {
		return "-< "+reviewId+"번 리뷰 >-------------------------------------\n"
			 + "회사명\t" + companyName + "\n"
			 + "근무직종\t" + jobName + "\n"
			 + "최종수정일\t" + getUpdateday() + "\n\n"
			 + "연봉·급여\t" + DisplayScore.getScoreBar(scoreSalary) + "\t" + scoreSalary+ "\n"
			 + "워라밸\t" + DisplayScore.getScoreBar(scoreWlb) + "\t" + scoreWlb + "\n"
			 + "조직문화\t" + DisplayScore.getScoreBar(scoreCulture) + "\t" + scoreCulture + "\n"
			 + "복리후생\t" + DisplayScore.getScoreBar(scoreWelfare) + "\t" + scoreWelfare + "\n"
			 + "고용안정성\t" + DisplayScore.getScoreBar(scoreStability) + "\t" + scoreStability +"\n"
			 + "커리어성장\t" + DisplayScore.getScoreBar(scoreImprove) + "\t" + scoreImprove + "\n\n"
			 + "총점\t" + DisplayScore.getStar(score) + "\t" + score + "\n"
			 + "총평\t" + getContents() + "\n"
			 + "-".repeat(50);
	}


	
}
