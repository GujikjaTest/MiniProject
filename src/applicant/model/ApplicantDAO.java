package applicant.model;

import java.util.List;
import java.util.Map;

import applicant.domain.ApplicantDTO;

public interface ApplicantDAO {

	// === 회원가입 === //
	public int register(ApplicantDTO applicantDTO);
	
	// === 로그인 === //
	ApplicantDTO login(Map<String, String> paraMap);
	
	// === 회원정보수정 === //
	public int updateApplicant(ApplicantDTO applicantDTO);

	// === 회원탈퇴 === //
	public int deleteApplicant(String applicantId);

	// == 성명으로 구직자 목록 조회 == //
	public List<ApplicantDTO> getApplicantListByName(String name);

	// == 나이대로 구직자 목록 조회 == //
	public List<ApplicantDTO> getApplicantListByAgeline(String ageline);

	// == 구직자 아이디로 구직자 조회 == //
	public ApplicantDTO getApplicantById(String applicantId);

	// == 구직자 차단 관리 == //
	public int blockApplicant(String applicantId, boolean block);

	// == 구직자 성별 통계 == //
	public Map<String, Integer> getGenderRatio();

}
