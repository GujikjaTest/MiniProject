package company.domain;

public class CompanyDTO {
      //오라클의 tbl_company 테이블에 해당한다.
	
	//field
	
	private String companyId; 		//회사 아이디
	private String passwd;			// 회사 비번
	private String email; 			//이메일
	private String name; 			// 회사명
	private String businessNo; 	//사업자등록번호
	private String address; 		//주소
	private int businessType; 	//기업형태
	private String tel;  			//연락처
	private String industry;   		//업종
	private int	status;				//가입상태
	
	
	
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	public int getBusinessType() {
		return businessType;
	}
	public void setBusinessType (int businessType){
		this.businessType = businessType;
	}
	
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	
	
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "--<"+name+"기업의 정보>--------------------------\n"
				+ "▣ 아이디 : " + companyId +"\n"
				+ "▣ 비밀번호 : " + passwd +"\n"
				+ "▣ 이메일 : " +email + "\n"
				+ "▣ 회사명 : " + name +"\n"
				+ "▣ 사업자등록번호 : " + businessNo+"\n"
				+ "▣ 기업형태 : " + businessType + "\n"
				+ "▣ 주소 : " + address + "\n"
				+ "▣ 연락처 : " + tel + "\n"
				+ "▣ 업종 : " + industry + "\n";
		
	}
	
}