package applicant.model;

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

public class ApplicantDAO_imple implements ApplicantDAO {

	// field
	private Connection conn = ProjectDBConnection.getConn();
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	// method
	// === 자원반납 === //
	private void close() {
		try {
			if(rs != null) { rs.close(); rs = null; }
			if(pstmt != null) { pstmt.close(); pstmt = null; }
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}// end of close()-------------------------
	
	
	// === 회원가입 === //
	@Override
	public int register(ApplicantDTO applicantDTO) {
		
		int result = 0;
		
		try {
			String sql = " insert into tbl_applicant(applicant_id, passwd, email, name, birthday, gender, tel) "
					   + " values(?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'), ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  applicantDTO.getApplicantId());
			pstmt.setString(2,  applicantDTO.getPasswd());
			pstmt.setString(3,  applicantDTO.getEmail());
			pstmt.setString(4,  applicantDTO.getName());
			pstmt.setString(5,  applicantDTO.getBirthday());
			pstmt.setInt(6,  applicantDTO.getGender());
			pstmt.setString(7,  applicantDTO.getTel());
			
			result = pstmt.executeUpdate(); // sql문 실행
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}// end of public int register(ApplicantDTO applicantDTO)------------------
	
	
	// === 로그인 === //
	@Override
	public ApplicantDTO login(Map<String, String> paraMap) {
		
		ApplicantDTO applicantDTO = null;
		
		try {
			String sql = " select applicant_id"
					   + " , substr(passwd, 1, 3) || lpad('*', length(passwd)-3, '*') as passwd"
					   + " , email, name, to_char(birthday, 'yyyy-mm-dd') AS birthday, gender, tel, status "
					   + " from tbl_applicant "
					   + " where status = 1 and applicant_id = ? and passwd = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("applicantId"));
			pstmt.setString(2, paraMap.get("passwd"));
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			if(rs.next()) {
				
				applicantDTO = new ApplicantDTO();
				
				applicantDTO.setApplicantId(rs.getString("applicant_id"));
				applicantDTO.setPasswd(rs.getString("passwd"));
				applicantDTO.setEmail(rs.getString("email"));
				applicantDTO.setName(rs.getString("name"));
				applicantDTO.setBirthday(rs.getString("birthday"));
				applicantDTO.setGender(rs.getInt("gender"));
				applicantDTO.setTel(rs.getString("tel"));
				applicantDTO.setStatus(rs.getInt("status"));
				
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return applicantDTO;
	}
	
	// === 회원정보수정 === //
	@Override
	public int updateApplicant(ApplicantDTO applicantDTO) {
		int result = 0;
		
		try {
			String sql = " UPDATE tbl_applicant"
					   + " SET passwd = ?, email = ?, name = ?"
					   + "   , birthday = to_date(?, 'yyyy-mm-dd')"
					   + "   , gender = ?, tel = ? "
					   + " WHERE applicant_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  applicantDTO.getPasswd());
			pstmt.setString(2,  applicantDTO.getEmail());
			pstmt.setString(3,  applicantDTO.getName());
			pstmt.setString(4,  applicantDTO.getBirthday());
			pstmt.setInt(5,  applicantDTO.getGender());
			pstmt.setString(6,  applicantDTO.getTel());
			pstmt.setString(7,  applicantDTO.getApplicantId());
			
			result = pstmt.executeUpdate(); // sql문 실행
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}


	// === 회원탈퇴 === //
	@Override
	public int deleteApplicant(String applicantId) {
		
		int result = 0;
		
		try {
			String sql = " UPDATE tbl_applicant SET status = 1 "
					   + " WHERE applicant_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, applicantId);
			
			result = pstmt.executeUpdate(); // sql문 실행
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

	// === 성명으로 구직자 목록 조회 === //
	@Override
	public List<ApplicantDTO> getApplicantListByName(String name) {
		
		ApplicantDTO applicantDTO = null;
		List<ApplicantDTO> applicantList = new ArrayList<>();
		
		try {
			String sql = " select applicant_id, passwd, email, name, to_char(birthday,'yyyy-mm-dd') as birthday, gender, tel, status "
					   + " from tbl_applicant "
					   + " where name = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			while(rs.next()) {
				
				applicantDTO = new ApplicantDTO();
				
				applicantDTO.setApplicantId(rs.getString("applicant_id"));
				applicantDTO.setPasswd(rs.getString("passwd"));
				applicantDTO.setEmail(rs.getString("email"));
				applicantDTO.setName(rs.getString("name"));
				applicantDTO.setBirthday(rs.getString("birthday"));
				applicantDTO.setGender(rs.getInt("gender"));
				applicantDTO.setTel(rs.getString("tel"));
				applicantDTO.setStatus(rs.getInt("status"));
				
				applicantList.add(applicantDTO);
				
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return applicantList;
	}

	// == 나이대로 구직자 목록 조회 == //
	@Override
	public List<ApplicantDTO> getApplicantListByAgeline(String ageline) {
		
		ApplicantDTO applicantDTO = null;
		List<ApplicantDTO> applicantList = new ArrayList<>();
		
		// 만나이 기준으로 나이대 검사
		try {
			String sql = " select applicant_id, name, to_char(birthday,'yyyy-mm-dd') as birthday, gender, tel, status "
					   + " from tbl_applicant "
					   + " where trunc(extract(year from sysdate) - extract(year from birthday) "
					   + " + case when to_char(birthday, 'mmdd') > to_char(sysdate, 'mmdd') then -1 else 0 end, -1) = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ageline);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			while(rs.next()) {
				
				applicantDTO = new ApplicantDTO();
				
				applicantDTO.setApplicantId(rs.getString("applicant_id"));
				applicantDTO.setName(rs.getString("name"));
				applicantDTO.setBirthday(rs.getString("birthday"));
				applicantDTO.setGender(rs.getInt("gender"));
				applicantDTO.setTel(rs.getString("tel"));
				applicantDTO.setStatus(rs.getInt("status"));
				
				applicantList.add(applicantDTO);
				
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return applicantList;
	}

	// == 구직자 아이디로 구직자 조회 == //
	@Override
	public ApplicantDTO getApplicantById(String applicantId) {

		ApplicantDTO applicantDTO = null;
		
		try {
			String sql = " select applicant_id, passwd, email, name, to_char(birthday,'yyyy-mm-dd') as birthday, gender, tel, status "
					   + " from tbl_applicant "
					   + " where applicant_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, applicantId);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			if(rs.next()) {
				
				applicantDTO = new ApplicantDTO();
				
				applicantDTO.setApplicantId(rs.getString("applicant_id"));
				applicantDTO.setPasswd(rs.getString("passwd"));
				applicantDTO.setEmail(rs.getString("email"));
				applicantDTO.setName(rs.getString("name"));
				applicantDTO.setBirthday(rs.getString("birthday"));
				applicantDTO.setGender(rs.getInt("gender"));
				applicantDTO.setTel(rs.getString("tel"));
				applicantDTO.setStatus(rs.getInt("status"));
				
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return applicantDTO;
	}

	// == 구직자 차단 관리 == //
	@Override
	public int blockApplicant(String applicantId, boolean block) {

		int result = 0;
		// block = true : 차단, block = false : 차단 해제
		int status = block ? 2 : 1;
		
		try {
			// 구직자 상태가 이미 차단/해제되었는지 확인하는 쿼리
			String sql = " select count(*) as count from tbl_applicant where applicant_id = ? and status = ? ";
			
			pstmt  = conn.prepareStatement(sql);
			pstmt.setString(1, applicantId);
			pstmt.setInt(2, status);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			// 이미 처리된 구직자 상태이면 2를 반환
			if(rs.getInt("count") == 1) {
				return 2;
			}
			
			sql = " update tbl_applicant set status = ? "
					   + " where applicant_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setString(2, applicantId);
			
			result = pstmt.executeUpdate(); // sql문 실행
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

	// == 구직자 성별 통계 == //
	@Override
	public Map<String, Integer> getGenderRatio() {
		Map<String, Integer> map = new HashMap<>();
		
		map.put("men", 0);
		map.put("women", 0);
		map.put("total", 0);
		
		try {
			String sql 	= " select decode(gender, 0, 'men', 1, 'women', 'total') as gender, count(*) as count "
						+ " from tbl_applicant "
						+ " where status = 1 "
						+ " group by rollup(gender) "; 
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			// total, men, women
			while(rs.next()) {
				map.put(rs.getString("gender"), rs.getInt("count"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return map;
	}
}
