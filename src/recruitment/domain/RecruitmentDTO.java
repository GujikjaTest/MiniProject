package recruitment.domain;

import apply.domain.ApplyDTO;
import common.Transaction;
import company.domain.CompanyDTO;
import job.domain.JobDTO;
import utils.AlignUtil;

//RecruitmenDTO 은 오라클의 TBL_RECRUITMENT에 해당하며, TBL_COMMPANY 와 TBL_JOB 을 참조합니다.
public class RecruitmentDTO { 
	
	// === field ===
	private int recruitmentId; 		    // 채용공고일련번호
	private String fkCompanyId; 		// 회사아이디
	private int fkJobId; 				// 직종일련번호
	private String title; 				// 채용공고제목
	private String contents; 			// 채용공고내용
	private int empType; 			    // 채용형태
	private int people; 				// 채용인원
	private int salary; 				// 연봉
	private String registerday; 		// 등록일자
	private String deadlineday; 		// 채용마감일자
	private int experience; 			// 경력
	private String updateday; 			// 최신수정일
	private int isDelete; 				// 삭제여부
	private int rank;				// 진성씨 관리자 파트를 위한 rank 저장소
	
	
	
	private JobDTO jobdto;				// TBL_JOB 테이블과 join하기 위함
	
	private CompanyDTO comdto;			// TBL_COMPANY 테이블과 join하기 위함
	
	private ApplyDTO appdto;			// TBL_APPLY 테이블과 join하기 위함
	
	
	
	// === meythod ===
	public int getRecruitmentId() {
		return recruitmentId;
	}
	public void setRecruitmentId(int recruitmentId) {
		this.recruitmentId = recruitmentId;
	}
	public String getFkCompanyId() {
		return fkCompanyId;
	}
	public void setFkCompanyId(String fk_companyId) {
		this.fkCompanyId = fk_companyId;
	}
	public int getFkJobId() {
		return fkJobId;
	}
	public void setFkJobId(int fkJobId) {
		this.fkJobId = fkJobId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getEmpType() {
		return empType;
	}
	public void setEmpType(int empType) {
		this.empType = empType;
	}
	public int getPeople() {
		return people;
	}
	public void setPeople(int people) {
		this.people = people;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public String getRegisterday() {
		return registerday;
	}
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	public String getDeadlineday() {
		return deadlineday;
	}
	public void setDeadlineday(String deadlineday) {
		this.deadlineday = deadlineday;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	// 최종수정날짜
	public String getUpdateday() {
		
		if(updateday == null){
			return registerday;
		}
		else {
			return updateday;
		}
	} // end of public String getUpdateday()----
	
	public void setUpdateday(String updateday) {
		this.updateday = updateday;
	}
	//
	
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	// join
	public JobDTO getJobdto() {
		return jobdto;
	}
	public void setJobdto(JobDTO jobdto) {
		this.jobdto = jobdto;
	}
	
	public CompanyDTO getComdto() {
		return comdto;
	}
	public void setComdto(CompanyDTO comdto) {
		this.comdto = comdto;
	}
	
	public ApplyDTO getAppdto() {
		return appdto;
	}
	public void setAppdto(ApplyDTO appdto) {
		this.appdto = appdto;
	}

	/////////////////////////////////////////
	
	// 채용공고 출력
	@Override
	public String toString() {
        return "-< "+recruitmentId+"번 채용공고 >------------------------------\n"+
        	   "1.회사명 : " + comdto.getName()+"\n"+
		       "2.채용제목 : " + title+"\n"+
			   "3.채용내용 : " + AlignUtil.contents(contents) +"\n"+
			   "4.직종 : " + jobdto.getName()+"\n"+ // ----------------------------- 확인해보기
			   "5.경력 : " + Transaction.experience(experience)+"\n"+
			   "6.채용형태 : " + Transaction.empType(empType)+"\n"+
			   "7.지역 : " + comdto.getAddress()+"\n"+
			   "8.채용인원 : " + people+"\n"+
			   "9.연봉 : " + Transaction.salary(salary)+"\n"+
			   "10.최종수정일자 : " + getUpdateday()+"\n"+
			   "11.채용마감일자 : " + deadlineday+"\n"+
			   "-".repeat(46);
    }
	
}
