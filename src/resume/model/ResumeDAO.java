package resume.model;

import java.util.List;
import java.util.Map;

import applicant.domain.ApplicantDTO;
import resume.domain.ResumeDTO;

//이력서
public interface ResumeDAO {
	
	
	// *** 이력서가 작성되어있는지 검사 *** //
	boolean resume_Completed(ApplicantDTO applicant);
	
	// *** 이력서 관리 *** //
	ResumeDTO list_Resume(ApplicantDTO applicant);
	
	
	// *** 이력서 작성 *** //
	int writeResume(ApplicantDTO applicant,ResumeDTO resumeDTO);

	// *** 이력서 수정 *** //
	int modifyResume(ResumeDTO resumeDTO,ApplicantDTO applicant);

	// *** 구직자 경력 통계 *** //
	Map<String, Integer> getExperienceRatio();

	// *** 구직자 학력 통계 *** //
	Map<String, Integer> getEducationRatio();

	// *** 구직자 인기 희망직종 통계 *** //
	List<String[]> getHopeJobRatio();

	// *** 입사지원 중복체크 메서드 *** //
	boolean apply_Duplication_Check(String recruitmentId,boolean result,ApplicantDTO applicant); //불리언으로 수정

	

	




}
