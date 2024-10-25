package apply.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import applicant.domain.ApplicantDTO;
import apply.domain.ApplyDTO;
import common.ProjectDBConnection;
import company.domain.CompanyDTO;
import job.domain.JobDTO;
import recruitment.domain.RecruitmentDTO;

public class ApplyDAO_imple implements ApplyDAO {

	// field, attribute, property, 속성
	private Connection conn = ProjectDBConnection.getConn();
	private PreparedStatement pstmt;
	private ResultSet rs;
	String sql ="";

	
	
	// method, operation, 기능
	
	// === 자원반납을 해주는 메소드 === //
	private void close() { // 조회, 삽입, 수정, 삭제마다 전부 넣으면 너무 길어지고 쓸데없으니 메소드화 해서 한줄로 끝내자!
		try {
			if(rs != null) { rs.close(); rs = null;} // 확인사살
			if(pstmt != null) { pstmt.close(); pstmt = null; }
		} catch (SQLException e) {e.printStackTrace();}
	} // end of private void close()------------
	
	
	
	// === 입자지원자 목록을 보여주는 메소드 === //
	   @Override
	   public List<ApplyDTO> applyList(RecruitmentDTO recruitmentDTO) {

	      List<ApplyDTO> ApplyList = new ArrayList<>();
	      ApplyDTO applyDTO = new ApplyDTO();
	      ApplicantDTO applicantDTO = new ApplicantDTO();
	      
	      try {
	         
	         String sql = " select apply_id, A.name, case when length(motivation) > 12 then substr(motivation, 1, 10) || '..' else motivation end AS motivation, to_char(registerday, 'yyyy-mm-dd') AS registerday "
	                  + " from "
	                  + " ( "
	                  + " select name, applicant_id "
	                  + " from TBL_APPLICANT "
	                  + " )  "
	                  + " A join "
	                  + " ( "
	                  + " select fk_applicant_id, resume_id "
	                  + " from TBL_RESUME "
	                  + " ) R "
	                  + " on R.fk_applicant_id = A.applicant_id "
	                  + " join "
	                  + " ( "
	                  + " select registerday, motivation, fk_resume_id, apply_id, fk_recruitment_id "
	                  + " from TBL_APPLY "
	                  + " ) P "
	                  + " on P.fk_resume_id = R.resume_id "
	                  + " join "
	                  + " ( "
	                  + " select recruitment_id "
	                  + " from TBL_RECRUITMENT "
	                  + " ) E "
	                  + " on E.recruitment_id = P.fk_recruitment_id "
	                  + " where recruitment_id = ? and rownum < 10 ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setInt(1, recruitmentDTO.getRecruitmentId());
	         
	         rs = pstmt.executeQuery();

	         while(rs.next()) {
	            applyDTO.setApply_id(rs.getInt("apply_id"));
	            
	            applicantDTO.setName(rs.getString("name"));
	            applyDTO.setApplicantDTO(applicantDTO); // 구직자에 이름넣기
	            
	            applyDTO.setMotivation(rs.getString("motivation"));
	            applyDTO.setRegisterday(rs.getString("registerday"));
	            
	            ApplyList.add(applyDTO);
	         }
	         
	      } catch(SQLException e) {
	         e.printStackTrace();
	      } finally {
	         close();
	      }
	      
	      return ApplyList;
	   }


	   
	   // 해당 채용공고에 대한 지원자 정보 조회
	   @Override
	   public Map<String, String> applySelect(Map<String, Integer> map) {
	      
	      Map<String, String> resultMap = new HashMap<>();
	      
	      try {
	      
	         String sql = " select apply_id, recruitment_id, title, A.name AS applicantName "
	                  + "      , gender, to_char(birthday, 'yyyy-mm-dd') AS birthday, tel, email, R.experience AS Rexperience, education "
	                  + "      , hope_location, J.name AS hopejobName, job_description, hope_salary, motivation, to_char(registerday, 'yyyy-mm-dd') AS registerday "
	                  + " from "
	                  + " ( "
	                  + "     select name, applicant_id, gender, birthday, tel, email "
	                  + "     from TBL_APPLICANT "
	                  + " )  "
	                  + " A join "
	                  + " ( "
	                  + "     select fk_applicant_id, resume_id, experience, education "
	                  + "          , hope_location, fk_job_id, job_description, hope_salary "
	                  + "     from TBL_RESUME "
	                  + " ) R "
	                  + " on R.fk_applicant_id = A.applicant_id "
	                  + " join "
	                  + " ( "
	                  + "     select registerday, motivation, fk_resume_id "
	                  + "          , apply_id, fk_recruitment_id "
	                  + "     from TBL_APPLY "
	                  + " ) P "
	                  + " on P.fk_resume_id = R.resume_id "
	                  + " join "
	                  + " ( "
	                  + "     select recruitment_id, title "
	                  + "     from TBL_RECRUITMENT "
	                  + " ) E "
	                  + " on E.recruitment_id = P.fk_recruitment_id "
	                  + " join TBL_JOB J "
	                  + " on R.fk_job_id = J.job_id "
	                  + " where apply_id = ? and recruitment_id = ? ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setInt(1, map.get("applyId"));
	         pstmt.setInt(2, map.get("recruitmentId"));
	         
	         rs = pstmt.executeQuery();
	         
	         rs.next();
	            resultMap.put("apply_id", String.valueOf(rs.getInt("apply_id")));
	            resultMap.put("recruitment_id", String.valueOf(rs.getInt("recruitment_id")));
	            resultMap.put("title", rs.getString("title"));
	            resultMap.put("applicantName", rs.getString("applicantName"));
	            resultMap.put("gender", String.valueOf(rs.getInt("gender")));
	            resultMap.put("birthday", rs.getString("birthday"));
	            resultMap.put("tel", rs.getString("tel"));
	            resultMap.put("email", rs.getString("email"));
	            resultMap.put("Rexperience", String.valueOf(rs.getInt("Rexperience")));
	            resultMap.put("education", rs.getString("education"));
	            resultMap.put("hope_location", rs.getString("hope_location"));
	            resultMap.put("hopejobName", rs.getString("hopejobName"));
	            resultMap.put("job_description", rs.getString("job_description"));
	            resultMap.put("hope_salary", String.valueOf(rs.getInt("hope_salary")));
	            resultMap.put("motivation", rs.getString("motivation"));
	            resultMap.put("registerday", rs.getString("registerday"));
	         
	      } catch(SQLException e) {
	         e.printStackTrace();
	      } finally {
	         close();
	      }
	      
	      return resultMap;
	   } // end of public Map<String, String> applySelect(Map<String, Integer> map)------------
	


	// *** 입사지원서 출력 메소드 *** //
	public List<ApplyDTO> InfoApply(String applicant_Id) {
		
		List<ApplyDTO> Applylist = null;
		
		
		String sql = " select C.name, E.title, E.contents, J.name as J_name, E.emp_type, E.experience, C.address,"
					+ " E.recruitment_id, E.people, E.salary, TO_CHAR(E.registerday,'yyyy-mm-dd') as E_registerday, "
					+ " TO_CHAR(E.deadlineday,'yyyy-mm-dd') AS E_deadlineday, B.motivation, TO_CHAR(B.registerday,'yyyy-mm-dd') as B_registerday "
	               + " from TBL_APPLICANT A  "
	               + " join TBL_RESUME R on A.applicant_id = R.fk_applicant_id "
	               + " join TBL_APPLY B on B.fk_resume_id = R.resume_id "
	               + " join TBL_RECRUITMENT E on E.recruitment_id = B.fk_recruitment_id "
	               + " join TBL_COMPANY C on C.company_id = E.fk_company_id "
	               + " join TBL_JOB J on J.job_id = E.fk_job_id "
	               + " where A.applicant_Id = ?  ";
			
			try {
				
			
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, applicant_Id);
				rs = pstmt.executeQuery();
				
				int cnt = 0;
				while(rs.next()) {
					cnt++;
					
					if(cnt == 1) {
						Applylist = new ArrayList<>();
					}
					
					ApplyDTO applyDTO = new ApplyDTO();
					CompanyDTO  companyDTO = new CompanyDTO();
					JobDTO jobDTO = new JobDTO();
					RecruitmentDTO recruitmentDTO = new RecruitmentDTO();
					
					// 채용공고 dto에 set
					jobDTO.setName(rs.getString("J_name"));
					companyDTO.setName(rs.getString("name"));
					companyDTO.setAddress(rs.getString("address"));
					recruitmentDTO.setTitle(rs.getString("title"));
					recruitmentDTO.setContents(rs.getString("contents"));
					recruitmentDTO.setEmpType(rs.getInt("emp_type"));
					recruitmentDTO.setPeople(rs.getInt("people"));
					recruitmentDTO.setSalary(rs.getInt("salary"));
					recruitmentDTO.setRegisterday(rs.getString("E_registerday"));
					recruitmentDTO.setDeadlineday(rs.getString("E_deadlineday"));
					recruitmentDTO.setExperience(rs.getInt("experience"));
					recruitmentDTO.setRecruitmentId(rs.getInt("recruitment_id"));
					

					applyDTO.setRecruitmentDTO(recruitmentDTO);
					applyDTO.setJobDTO(jobDTO);
					applyDTO.setCompanyDTO(companyDTO); //구인회사의 dto 즉, 회사명과 같은 컬럼값이 필요해서
					
					//채용공고 dto에 set 한 것을 입사지원서에 필요한 것만 set
					applyDTO.setFk_recruitment_id(recruitmentDTO.getRecruitmentId());	//채용공고일련번호
					applyDTO.setRegisterday(rs.getString("B_registerday")); 					//지원서 작성일
					applyDTO.setMotivation(rs.getString("motivation")); 					//지원동기
					
					
					
					
					
					
					Applylist.add(applyDTO);
					
					
				} // end of while(rs.next())----

			} catch (SQLException e) {
			}
			finally {
				close();
			}
			
			return Applylist;
	}//end of public List<ApplyDTO> InfoApply(String applicant_Id, ApplyDTO applyDTO) -------



	// *** 입사지원서 삽입 메서드 *** //
	@Override
	public void apply_Applicant(String apply_Applicant, ApplicantDTO applicant,String recruitmentId) {
		
		String resume_id = "";
		
		// select문으로 현재 로그인된 회원의 이력서일련번호 추출, 이것을 insert의 위치홀더값으로 사용
		sql =" select resume_id from tbl_resume "
				+ " where fk_applicant_id = ? "; //applicant.getApplicantId()로 위치홀더
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, applicant.getApplicantId());
			rs = pstmt.executeQuery();
			
			rs.next();
			resume_id = rs.getString("resume_id");
			
		} catch (SQLException e) {
		}
		finally {
			close();
		}
		
				// 채용공고 순번 =recruitmentId /  fk_recruitment_id 위치홀더 값으로 사용
		sql = " insert into tbl_apply(apply_id,fk_recruitment_id, "
					+ " fk_resume_id,motivation) "
					+ " values(seq_apply_id.nextval,?,?,?) ";
			
		try {
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(recruitmentId));
			pstmt.setString(2, resume_id);
			pstmt.setString(3, apply_Applicant);
			
			pstmt.executeUpdate();
			
			

		} catch (SQLException e) {
		}
		finally {
			close();
		}
		
	}// end of public void apply_Applicant(String apply_Applicant, ApplicantDTO applicant)----

}
