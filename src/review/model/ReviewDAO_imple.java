package review.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.ProjectDBConnection;
import review.domain.ReviewDTO;

public class ReviewDAO_imple implements ReviewDAO {

	// field
	private Connection conn = ProjectDBConnection.getConn();
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// method
	// === 자원반납 === //
	private void close() {
		try {
			if(rs != null) { rs.close(); rs = null; }
			if(pstmt != null) { pstmt.close(); pstmt = null; }
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}// end of close()-------------------------
	
	
	// === 리뷰 등록 === //
	@Override
	public int registerReview(ReviewDTO reviewDTO) {
		int result = 0;
		
		try {
			String sql = " insert into tbl_review(review_id, fk_applicant_id, fk_company_id"
					   + "                      , fk_job_id, score, score_salary, score_wlb"
					   + "                      , score_culture, score_welfare, score_stability"
					   + "                      , score_improve, contents) "
					   + " values(seq_review_id.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reviewDTO.getFkApplicantId());
			pstmt.setString(2, reviewDTO.getFkCompanyId());
			pstmt.setInt(3, reviewDTO.getFkJobId());
			pstmt.setInt(4, reviewDTO.getScore());
			pstmt.setInt(5, reviewDTO.getScoreSalary());
			pstmt.setInt(6, reviewDTO.getScoreWlb());
			pstmt.setInt(7, reviewDTO.getScoreCulture());
			pstmt.setInt(8, reviewDTO.getScoreWelfare());
			pstmt.setInt(9, reviewDTO.getScoreStability());
			pstmt.setInt(10, reviewDTO.getScoreImprove());
			pstmt.setString(11, reviewDTO.getContents());
			
			result = pstmt.executeUpdate(); // sql문 실행
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}


	// === 구직자 1명의 리뷰 select === //
	@Override
	public List<ReviewDTO> getApplicantReviewList(String applicantId) {

		List<ReviewDTO> reviewList = new ArrayList<>();
		
		try {
			String sql = " SELECT review_id"
					   + "      , CASE WHEN length(contents) > 15 THEN substr(contents, 1, 13) || '..' "
					   + "             ELSE contents END AS contents "
					   + "      , score "
					   + "      , to_char(registerday, 'yyyy-mm-dd') AS registerday "
					   + "      , to_char(updateday, 'yyyy-mm-dd') AS updateday "
					   + "      , C.name AS company_name "
					   + "      , J.name AS job_name "
					   + " FROM tbl_review R JOIN tbl_company C "
					   + " ON R.fk_company_id = C.company_id "
					   + " JOIN tbl_job J "
					   + " ON R.fk_job_id = J.job_id "
					   + " WHERE fk_applicant_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, applicantId);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			while(rs.next()) {
				
				ReviewDTO reviewDTO = new ReviewDTO();

				reviewDTO.setReviewId(rs.getInt("review_id"));
				reviewDTO.setScore(rs.getInt("score"));
				reviewDTO.setContents(rs.getString("contents"));
				reviewDTO.setRegisterday(rs.getString("registerday"));
				reviewDTO.setUpdateday(rs.getString("updateday"));
				reviewDTO.setCompanyName(rs.getString("company_name"));
				reviewDTO.setJobName(rs.getString("job_name"));
				
				reviewList.add(reviewDTO);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return reviewList;
	}


	// === 한 회사의 리뷰 목록 select === //
	@Override
	public List<ReviewDTO> getCompanyReviewList(String companyId) {

		List<ReviewDTO> reviewList = new ArrayList<>();
		
		try {
			String sql = " SELECT review_id "
					   + "      , contents "
					   + "      , score "
					   + "      , to_char(registerday, 'yyyy-mm-dd') AS registerday "
					   + "      , to_char(updateday, 'yyyy-mm-dd') AS updateday "
					   + "      , substr(A.name, 1, 1) || lpad('*', length(A.name)-1, '*') AS applicant_name "
					   + "      , J.name AS job_name "
					   + " FROM tbl_review R JOIN tbl_applicant A "
					   + " ON R.fk_applicant_id = A.applicant_id "
					   + " JOIN tbl_job J "
					   + " ON R.fk_job_id = J.job_id "
					   + " WHERE fk_company_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, companyId);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			while(rs.next()) {
				
				ReviewDTO reviewDTO = new ReviewDTO();

				reviewDTO.setReviewId(rs.getInt("review_id"));
				reviewDTO.setScore(rs.getInt("score"));
				reviewDTO.setContents(rs.getString("contents"));
				reviewDTO.setRegisterday(rs.getString("registerday"));
				reviewDTO.setUpdateday(rs.getString("updateday"));
				reviewDTO.setApplicantName(rs.getString("applicant_name"));
				reviewDTO.setJobName(rs.getString("job_name"));
				
				reviewList.add(reviewDTO);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return reviewList;
	}


	// === 한 회사의 리뷰 평균점수 select === //
	@Override
	public Map<String, String> getCompanyReviewAvg(String companyId) {

		Map<String, String> reviewAvg = null;
		
		try {
			String sql = " SELECT round(avg(score),1) AS score "
					   + "      , round(avg(score_salary-1)/4*100, 0) AS score_salary "
					   + "      , round(avg(score_wlb-1)/4*100, 0) AS score_wlb "
					   + "      , round(avg(score_culture-1)/4*100, 0) AS score_culture "
					   + "      , round(avg(score_welfare-1)/4*100, 0) AS score_welfare "
					   + "      , round(avg(score_stability-1)/4*100, 0) AS score_stability "
					   + "      , round(avg(score_improve-1)/4*100, 0) AS score_improve "
					   + "      , C.name AS company_name "
					   + " FROM tbl_review R JOIN tbl_company C "
					   + " ON R.fk_company_id = C.company_id "
					   + " WHERE fk_company_id = ? "
					   + "     and registerday >= sysdate - to_yminterval('05-00') "
					   + "     or updateday >= sysdate - to_yminterval('05-00') "
					   + " GROUP BY C.name ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, companyId);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			if(rs.next()) {
				reviewAvg = new HashMap<>();
				reviewAvg.put("score", rs.getString("score"));
				reviewAvg.put("score_salary", rs.getString("score_salary"));
				reviewAvg.put("score_wlb", rs.getString("score_wlb"));
				reviewAvg.put("score_culture", rs.getString("score_culture"));
				reviewAvg.put("score_welfare", rs.getString("score_welfare"));
				reviewAvg.put("score_stability", rs.getString("score_stability"));
				reviewAvg.put("score_improve", rs.getString("score_improve"));
				reviewAvg.put("company_name", rs.getString("company_name"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return reviewAvg;
	}

	
	// === 1개의 리뷰 select === //
	@Override
	public ReviewDTO getReview(int reviewId) {

		ReviewDTO reviewDTO = null;
		
		try {
			String sql = " SELECT review_id, fk_company_id, fk_applicant_id "
					   + "      , fk_job_id, score, contents, score_salary "
					   + "      , score_wlb, score_culture, score_welfare "
					   + "      , score_stability, score_improve "
					   + "      , to_char(registerday, 'yyyy-mm-dd  hh24:mi') AS registerday "
					   + "      , to_char(updateday, 'yyyy-mm-dd  hh24:mi') AS updateday "
					   + "      , C.name AS company_name"
					   + "      , J.name AS job_name "
					   + " FROM tbl_review R JOIN tbl_company C "
					   + " ON R.fk_company_id = C.company_id "
					   + " JOIN tbl_job J "
					   + " ON R.fk_job_id = J.job_id "
					   + " WHERE review_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewId);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			if(rs.next()) {
				
				reviewDTO = new ReviewDTO();

				reviewDTO.setReviewId(rs.getInt("review_id"));
				reviewDTO.setFkCompanyId(rs.getString("fk_company_id"));
				reviewDTO.setFkApplicantId(rs.getString("fk_applicant_id"));
				reviewDTO.setFkJobId(rs.getInt("fk_job_id"));
				reviewDTO.setScore(rs.getInt("score"));
				reviewDTO.setContents(rs.getString("contents"));
				reviewDTO.setScoreSalary(rs.getInt("score_salary"));
				reviewDTO.setScoreWlb(rs.getInt("score_wlb"));
				reviewDTO.setScoreCulture(rs.getInt("score_culture"));
				reviewDTO.setScoreWelfare(rs.getInt("score_welfare"));
				reviewDTO.setScoreStability(rs.getInt("score_stability"));
				reviewDTO.setScoreImprove(rs.getInt("score_improve"));
				reviewDTO.setRegisterday(rs.getString("registerday"));
				reviewDTO.setUpdateday(rs.getString("updateday"));
				reviewDTO.setCompanyName(rs.getString("company_name"));
				reviewDTO.setJobName(rs.getString("job_name"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return reviewDTO;
	}
	

	// === 리뷰 수정하기 === //
	@Override
	public int updateReview(ReviewDTO reviewDTO) {
		int result = 0;
		
		try {
			String sql = " UPDATE tbl_review "
					   + " SET fk_job_id = ?, score = ?, score_salary = ? "
					   + "   , score_wlb = ?, score_culture = ?, score_welfare = ?"
					   + "   , score_stability = ?, score_improve = ? "
					   + "   , contents = ?, updateday = sysdate "
					   + " WHERE review_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewDTO.getFkJobId());
			pstmt.setInt(2, reviewDTO.getScore());
			pstmt.setInt(3, reviewDTO.getScoreSalary());
			pstmt.setInt(4, reviewDTO.getScoreWlb());
			pstmt.setInt(5, reviewDTO.getScoreCulture());
			pstmt.setInt(6, reviewDTO.getScoreWelfare());
			pstmt.setInt(7, reviewDTO.getScoreStability());
			pstmt.setInt(8, reviewDTO.getScoreImprove());
			pstmt.setString(9, reviewDTO.getContents());
			pstmt.setInt(10, reviewDTO.getReviewId());
			
			result = pstmt.executeUpdate(); // sql문 실행
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}


	// === 리뷰 삭제하기 === //
	@Override
	public int deleteReview(int reviewId) {
		int result = 0;
		
		try {
			String sql = " UPDATE tbl_review SET is_delete = 1 "
					   + " WHERE review_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewId);
			
			result = pstmt.executeUpdate(); // sql문 실행
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
	
}
