package recruitment.model;

import java.util.List;
import java.util.Map;

import company.domain.CompanyDTO;
import recruitment.domain.RecruitmentDTO;


public interface RecruitmentDAO {

	// 글목록보기
	List<RecruitmentDTO> recruitmenList(String companyId);

	// 채용공고 상세보기
	// 단 1개에 관한 select문
	RecruitmentDTO recruitmentInfoSelect(String recruitmentId);
	
	// 채용공고 등록
	int recruitmentInsert(CompanyDTO companyDTO, RecruitmentDTO recruitmentDTO);
	
	// 채용공고 수정
	int recruitmentUpdate(RecruitmentDTO recruitmentDTO);

	// 채용공고 삭제
	int recruitmentDelete(RecruitmentDTO recruitmentDTO, CompanyDTO companyDTO);

	// 지원자수 상위 10개의 채용공고 보기
	List<RecruitmentDTO> recruitmenTopList();

	// 채용공고에서 회사명, 직종별, 지역별, 경력 검색
	List<RecruitmentDTO> showAllRecruitment(Map<String, String> map);

	// 직종별 top 10 채용공고 수 가져오기
	List<Map<String, String>> recruitmentStatistics(String status);
}
