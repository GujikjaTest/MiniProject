package apply.domain;

import applicant.domain.ApplicantDTO;
import resume.domain.ResumeDTO;
import company.domain.CompanyDTO;
import job.domain.JobDTO;
import recruitment.domain.RecruitmentDTO;

public class ApplyDTO {
	
	// insert field, attribute, property, 속성
	
	private int apply_id; 			/* 입사지원서일련번호 */
	private int fk_recruitment_id;  /* 채용공고일련번호 */
	private int fk_resume_id;		/* 이력서일련번호 */
	private String registerday; 	/* 지원서작성일 */
	private String motivation; 		/* 지원동기 */
	private String updateday;		/* 최신수정일 */
	
	private ResumeDTO resumeDTO;	/* 이력서 조인용 */
	
	private ApplicantDTO applicantDTO;	/* 회원 조인용 */
	
	// select field
	private RecruitmentDTO recruitmentDTO;
	private CompanyDTO companyDTO;
	private JobDTO jobDTO;
	
	
	// method, operation, 기능
	
	
	public int getApply_id() {
		return apply_id;
	}
	public void setApply_id(int apply_id) {
		this.apply_id = apply_id;
	}
	public int getFk_recruitment_id() {
		return fk_recruitment_id;
	}
	public void setFk_recruitment_id(int fk_recruitment_id) {
		this.fk_recruitment_id = fk_recruitment_id;
	}
	public int getFk_resume_id() {
		return fk_resume_id;
	}
	public void setFk_resume_id(int fk_resume_id) {
		this.fk_resume_id = fk_resume_id;
	}
	public String getRegisterday() {
		return registerday;
	}
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	public String getMotivation() {
		return motivation;
	}
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	public String getUpdateday() {
		return updateday;
	}
	public void setUpdateday(String updateday) {
		this.updateday = updateday;
	}
	
	
	public RecruitmentDTO getRecruitmentDTO() {
		return recruitmentDTO;
	}
	public void setRecruitmentDTO(RecruitmentDTO recruitmentDTO) {
		this.recruitmentDTO = recruitmentDTO;
	}
	public CompanyDTO getCompanyDTO() {
		return companyDTO;
	}
	public void setCompanyDTO(CompanyDTO companyDTO) {
		this.companyDTO = companyDTO;
	}
	public JobDTO getJobDTO() {
		return jobDTO;
	}
	public void setJobDTO(JobDTO jobDTO) {
		this.jobDTO = jobDTO;
	}

	
	
	public ResumeDTO getResumeDTO() {
		return resumeDTO;
	}
	public void setResumeDTO(ResumeDTO resumeDTO) {
		this.resumeDTO = resumeDTO;
	}
	public ApplicantDTO getApplicantDTO() {
		return applicantDTO;
	}
	public void setApplicantDTO(ApplicantDTO applicantDTO) {
		this.applicantDTO = applicantDTO;
	}
	
}
