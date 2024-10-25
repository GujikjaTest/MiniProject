package resume.domain;

import job.domain.JobDTO;

public class ResumeDTO {


	// field, attribute, property, 속성
	
	private int resume_id; /* 이력서일련번호 */
	private String fk_applicant_id;/* 구직자아이디 */
	private int fk_job_id;/* 희망직종코드 */
	private int experience; /* 경력 */
	private int education; /* 학력 */
	private String hope_location; /* 희망근무지역 */
	private String job_description ;/* 스펙 */
	private int hope_salary; /* 희망연봉 */
	private int is_delete ;/* 삭제여부 */
	private JobDTO jobDTO;
	
	
	// method, operation, 기능
	
	
	public int getResume_id() {
		return resume_id;
	}
	public void setResume_id(int resume_id) {
		this.resume_id = resume_id;
	}
	public String getFk_applicant_id() {
		return fk_applicant_id;
	}
	public void setFk_applicant_id(String fk_applicant_id) {
		this.fk_applicant_id = fk_applicant_id;
	}
	public int getFk_job_id() {
		return fk_job_id;
	}
	public void setFk_job_id(int fk_job_id) {
		this.fk_job_id = fk_job_id;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public int getEducation() {
		return education;
	}
	public void setEducation(int education) {
		this.education = education;
	}
	public String getHope_location() {
		return hope_location;
	}
	public void setHope_location(String hope_location) {
		this.hope_location = hope_location;
	}
	public String getJob_description() {
		return job_description;
	}
	public void setJob_description(String job_description) {
		this.job_description = job_description;
	}
	public int getHope_salary() {
		return hope_salary;
	}
	public void setHope_salary(int hope_salary) {
		this.hope_salary = hope_salary;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	public JobDTO getJobDTO() {
		return jobDTO;
	}
	public void setJobDTO(JobDTO jobDTO) {
		this.jobDTO = jobDTO;
	}
	
	
	
	
	
	
	
	
	
    
}
