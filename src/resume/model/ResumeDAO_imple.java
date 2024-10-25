package resume.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import applicant.domain.ApplicantDTO;
import common.ProjectDBConnection;
import job.domain.JobDTO;
import resume.domain.ResumeDTO;

public class ResumeDAO_imple implements ResumeDAO{
	
	

	Connection conn = ProjectDBConnection.getConn();
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	ResumeDTO resumeDTO = null;
	JobDTO jobDTO = null;
	String sql = "";
	
	// === 자원반납 === //
	private void close() {
		try {
			if(rs != null) { rs.close(); rs = null; }
			if(pstmt != null) { pstmt.close(); pstmt = null; }
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}// end of close()-------------------------
	
	
	
	// *** 이력서가 작성되어있는지 검사 *** //
	
	@Override
	public boolean resume_Completed(ApplicantDTO applicant) {
		
		boolean completed = false;
		
		sql = " select * "
			+ " from TBL_RESUME R JOIN tbl_job J "
			+ " ON R.fk_job_id = J.job_id "
			+ " JOIN TBL_APPLICANT A "
			+ " ON R.fk_applicant_id = A.applicant_id "
			+ " where applicant_id = ? ";
		
			try {
				pstmt = conn.prepareStatement(sql);
			
				pstmt.setString(1,applicant.getApplicantId());
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					completed = true;
					return completed;	//행이 존재함(이력서를 이미 작성함)
				}
				
				
			} catch (SQLException e) {
			}
			finally {
				close();
			}
		return completed;	// 행이 존재하지 않음 (이력서를 작성하지 않음 즉, 이력서 작성 실행가능)
	}

	
	
	// *** 이력서 관리(이력서관리탭에 들어오면 이력서 무조건 보여주기위함) *** //
	@Override
	public ResumeDTO list_Resume(ApplicantDTO applicant) {
		
		resumeDTO = new ResumeDTO();
		jobDTO = new JobDTO();
		
	try {
		
		sql = " select R.experience,R.education,R.hope_location,J.job_id,J.name,R.job_description,R.hope_salary "
				+ " from TBL_RESUME R JOIN tbl_job J "
				+ " ON R.fk_job_id = J.job_id "
				+ " JOIN TBL_APPLICANT A "
				+ " ON R.fk_applicant_id = A.applicant_id "
				+ " where applicant_id = ? ";
		
		
		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,applicant.getApplicantId());
			
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				
				
				resumeDTO.setExperience(rs.getInt("experience"));
				resumeDTO.setEducation(rs.getInt("education"));
				resumeDTO.setHope_location(rs.getString("hope_location"));
				jobDTO.setName(rs.getString("name")); 					// Jobdto의 setName에 희망직종 set. 
				resumeDTO.setJob_description(rs.getString("job_description"));
				resumeDTO.setHope_salary(rs.getInt("hope_salary"));
				jobDTO.setJob_id(rs.getInt("job_id"));
				resumeDTO.setJobDTO(jobDTO);
			}
			
		    } catch (SQLException e) {
				e.printStackTrace();
		    }finally {
				close();
			}
			
		return resumeDTO;
	}
	
	

	
	// *** 이력서 작성 *** //
	@Override
	public int writeResume(ApplicantDTO applicant,ResumeDTO resumeDTO) {
		System.out.println("");
		int result = 0;
		try {
			
			sql = " insert into TBL_RESUME (resume_id,fk_applicant_id,fk_job_id "
				+ " ,experience,education,hope_location,job_description,hope_salary) "
				+ " values(seq_resume_id.nextval,?,?,?,?,?,?,?) ";
			

			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, applicant.getApplicantId());// 로그인된 회원의 아이디를 인자로 받아 고정입력
			pstmt.setInt(2, resumeDTO.getFk_job_id()); //희망직종코드 
			pstmt.setInt(3, resumeDTO.getExperience());
			pstmt.setInt(4, resumeDTO.getEducation());
			pstmt.setString(5, resumeDTO.getHope_location());
			pstmt.setString(6, resumeDTO.getJob_description());
			pstmt.setInt(7, resumeDTO.getHope_salary());

			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return result;
	}// end of public void writeResume() ----


	
	

	// *** 이력서 수정 *** //
	@Override
	public int modifyResume(ResumeDTO resumeDTO,ApplicantDTO applicant) {
		int result = 0;
		
		try {
			
			sql = " update TBL_RESUME  "
					+ " set fk_job_id=? "
					+ "    ,experience=? "
					+ "    ,education=? "
					+ "    ,hope_location=? "
					+ "    ,job_description=? "
					+ "    ,hope_salary=? "
					+ " where fk_applicant_id = ? "; // 로그인된 구직자의 아이디로 조건 설정
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, resumeDTO.getFk_job_id());
			pstmt.setInt(2, resumeDTO.getExperience());
			pstmt.setInt(3, resumeDTO.getEducation());
			pstmt.setString(4, resumeDTO.getHope_location());
			pstmt.setString(5, resumeDTO.getJob_description());
			pstmt.setInt(6, resumeDTO.getHope_salary());
			pstmt.setString(7, applicant.getApplicantId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return result;
	}
	
	
	// *** 구직자 경력 통계 *** //
	@Override
	public Map<String, Integer> getExperienceRatio() {
		Map<String, Integer> map = new HashMap<>();
		
		map.put("junior", 0);
		map.put("senior", 0);
		map.put("total", 0);
		
		try {
			String sql 	= " select decode(experience, 0, 'junior', 1, 'senior', 'total') as experience, count(*) as count "
						+ " from tbl_resume "
						+ " group by rollup(experience) ";  
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			// total, junior, senior
			while(rs.next()) {
				map.put(rs.getString("experience"), rs.getInt("count"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return map;
	}


	// *** 구직자 학력 통계 *** //
	@Override
	public Map<String, Integer> getEducationRatio() {
		Map<String, Integer> map = new HashMap<>();
		
		map.put("0", 0);
		map.put("1", 0);
		map.put("2", 0);
		map.put("3", 0);
		map.put("4", 0);
		
		try {
			String sql 	= " select nvl(to_char(education), 'total') as experience, count(*) as count "
						+ " from tbl_resume "
						+ " group by rollup(education) ";  
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			// total, junior, senior
			while(rs.next()) {
				map.put(rs.getString("experience"), rs.getInt("count"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return map;
	}


	// *** 구직자 인기 희망직종 통계 *** //
	@Override
	public List<String[]> getHopeJobRatio() {
		List<String[]> list = new ArrayList<>(); // String[0] = [순위], String[1] = [직종명], String[2] = [비율]
		
		try {
			String sql 	= " select * "
						+ " from "
						+ " ( "
						+ " 	select j.name, round(count(*) / t.total * 100) as ratio, dense_rank() over (order by count(*) desc) as rank "
						+ "     from "
						+ "		("
						+ "		 	select count(*) as total from tbl_resume "
						+ "		) t "
						+ " 	cross join tbl_resume r "
						+ " 	join tbl_job j on r.fk_job_id = j.job_id "
						+ " 	group by j.name, t.total "
						+ " ) "
						+ " where rownum <= 10 ";
			   
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			while(rs.next()) {
				String[] arr = new String[3]; // String[0] = [순위], String[1] = [직종명], String[2] = [비율]
				
				arr[0] = rs.getString("rank");
				arr[1] = rs.getString("name");
				arr[2] = rs.getString("ratio");
				
 				list.add(arr);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return list;
	}


	// *** 입사지원 중복체크 메서드 *** //
	@Override
	public boolean apply_Duplication_Check(String recruitmentId,boolean result,ApplicantDTO applicant) {
		
		
		try {
			
			sql = " select resume_id from tbl_applicant A "
					+ " join tbl_resume R "
					+ " on A.applicant_id = R.fk_applicant_id "
					+ " where A.applicant_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,applicant.getApplicantId());
			rs = pstmt.executeQuery();
			rs.next();
			int resume_id = rs.getInt("resume_id");
			
			
			sql = " select * from tbl_apply "
				+ " where fk_recruitment_id = ? and fk_resume_id = ? "; 
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,recruitmentId);
			pstmt.setInt(2,resume_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return result;
		
	}
}
