package review.model;

import java.util.List;
import java.util.Map;

import review.domain.ReviewDTO;

public interface ReviewDAO {

	// === 리뷰 등록 === //
	int registerReview(ReviewDTO reviewDTO);

	// === 구직자 1명의 리뷰 select === //
	List<ReviewDTO> getApplicantReviewList(String applicantId);
	
	// === 한 회사의 리뷰 select === //
	List<ReviewDTO> getCompanyReviewList(String companyId);

	// === 한 회사의 리뷰평균 select === //
	Map<String, String> getCompanyReviewAvg(String companyId);
	
	// === 리뷰 상세보기 === //
	ReviewDTO getReview(int reviewId);

	// === 리뷰 수정하기 === //
	int updateReview(ReviewDTO reviewDTO);
	
	// === 리뷰 삭제하기 === //
	int deleteReview(int reviewId);
	
}
