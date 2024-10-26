package company.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Scanner;


import common.ProjectDBConnection;
import company.domain.CompanyDTO;

public class CompanyDAO_imple implements CompanyDAO {

	//field
	private Connection conn = ProjectDBConnection.getConn();
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	
	//method 기능 
	
	// === 자원을 반납해주는 메소드 === //
	
	private void close() {
		try {
			if(rs != null)    {rs.close();	  rs = null;}
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// end of private void close()----------------
	
	
	
	
	
	// === 구인회사 회원가입 메소드 === //

	@Override
	public int register(CompanyDTO companyDTO) {
		int result = 0;
		
	try {
		
		String sql =" insert into TBL_COMPANY(company_id, passwd, email, name, business_no, address, business_type, tel, industry)  "
				+ "            values(?,?,?,?,?,?,?,?,?) ";
			
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, companyDTO.getCompanyId());
		pstmt.setString(2, companyDTO.getPasswd());
		pstmt.setString(3, companyDTO.getEmail());
		pstmt.setString(4, companyDTO.getName());
		pstmt.setString(5, companyDTO.getBusinessNo());
		pstmt.setString(6, companyDTO.getAddress());
		pstmt.setInt(7, companyDTO.getBusinessType());
		pstmt.setString(8, companyDTO.getTel());
		pstmt.setString(9, companyDTO.getIndustry());
			
		result = pstmt.executeUpdate(); // sql문 실행
		
	} catch (SQLException e) {
	
		e.printStackTrace();
	}	finally {
		close();//자원반납.
	}

	
		return result;
	}//public int register(companyDTO company)



	
	// === 구인회사 로그인 메소드 ==== //
	@Override
	public CompanyDTO login(Map<String, String> paraMap) {
		CompanyDTO company = null;
		
		try {
			String sql = " SELECT company_id"
					+ " , passwd, email, name, business_no, address, tel, industry,  "
					+ "       business_type "
					+ " FROM tbl_company  "
					+ " WHERE status = 1  "
					+ "  AND company_id = ? "
					+ "  AND passwd = ?";

		
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, paraMap.get("company_id"));
		pstmt.setString(2, paraMap.get("passwd"));
		
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
		company = new CompanyDTO();
		
		company.setCompanyId(rs.getString("company_id"));
		company.setPasswd(rs.getString("passwd"));
		company.setEmail(rs.getString("email"));
		company.setName(rs.getString("name"));
		company.setBusinessNo(rs.getString("business_no"));
		company.setAddress(rs.getString("address"));
		company.setBusinessType(rs.getInt("business_type"));
		company.setTel(rs.getString("tel"));
		company.setIndustry(rs.getString("industry"));
		}
		
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		
		return company;
	}//public companyDTO login(Map<String, String> paraMap)



	



	//우리회사정보수정하기
	@Override
	public int cpInfoUpdate(CompanyDTO companyDTO) {
		
		int result = 0;
		
		try {
		
		String sql = " update tbl_company set  passwd = ?, email = ?, name = ?, business_no = ?, business_type = ?, address =?, tel = ?, industry = ? "
				   + " where company_id = ? " ;
		
		
			pstmt = conn.prepareStatement(sql);
			
			
			pstmt.setString(1, companyDTO.getPasswd());
			pstmt.setString(2, companyDTO.getEmail());
			pstmt.setString(3, companyDTO.getName());
			pstmt.setString(4, companyDTO.getBusinessNo());
			pstmt.setInt(5, companyDTO.getBusinessType());
			pstmt.setString(6, companyDTO.getAddress() );
			pstmt.setString(7, companyDTO.getTel());
			pstmt.setString(8, companyDTO.getIndustry());
			pstmt.setString(9, companyDTO.getCompanyId());
			
			result = pstmt.executeUpdate();//sql 실행
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		
		return result;
	}// end of public int cpInfoUpdate(Map<String, String> paraMap) 

	@Override
	public List<CompanyDTO> companySearchList(String searchStr, String select){
		
		
		String str = "";
		
		switch(select){
		default:
		case "1":
			str = " where upper(name) like upper(?) ";
			break;
		case "2":
			str = " where upper(industry) like upper(?) ";
			break;
		case "3":
			str = " where upper(address) like upper(?) ";
		}
		
		List<CompanyDTO> companyList = new ArrayList<>();
	    
	    try {
	        String sql = " select name, industry, business_no, address, status, email, company_id, tel, "
	                   + " case when progress is null then '채용없음' else progress end as progress "
	                   + " from ("
	                   + "    select name, industry, business_no, address, company_Id, status, email, tel "
	                   + "    from tbl_company "
	                   + str
	                   + " ) C "
	                   + " LEFT JOIN "
	                   + " ( "
	                   + "    select case when max(deadlineday) > sysdate then '진행중' else '마감' end as progress, fk_company_id "
	                   + "    from tbl_recruitment "
	                   + "    group by fk_company_id "
	                   + " ) R "
	                   + " ON fk_company_id = company_Id ";
	        
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setString(1, "%"+searchStr+"%");  // 첫 번째 파라미터
	        
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {  // 여러 행을 처리할 수 있도록 변경
	            CompanyDTO companyDTO = new CompanyDTO();
	            
	            companyDTO.setName(rs.getString("name"));
	            companyDTO.setIndustry(rs.getString("industry"));
	            companyDTO.setBusinessNo(rs.getString("business_no"));
	            companyDTO.setAddress(rs.getString("address"));
	            companyDTO.setProgress(rs.getString("progress"));
	        	companyDTO.setStatus(rs.getInt("status"));
	        	companyDTO.setEmail(rs.getString("email"));
	        	companyDTO.setCompanyId(rs.getString("company_id"));
	        	companyDTO.setTel(rs.getString("tel"));
	            
	            companyList.add(companyDTO);  // 리스트에 추가
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        close();
	    }
	    
	    return companyList;


	}//companySearchList(String searchStr, String select)---------


	//회사명으로 조회
	@Override
	public List<CompanyDTO> companyNameList(String companyName) {
	    List<CompanyDTO> companyNameList = new ArrayList<>();
	    
	    try {
	        String sql = " select name, industry, business_no, address, status, email, company_id, tel, "
	                   + " case when progress is null then '채용없음' else progress end as progress "
	                   + " from ("
	                   + "    select name, industry, business_no, address, company_Id, status, email, tel"
	                   + "    from tbl_company "
	                   + "    where upper(name) like  upper(?) "
	                   + " ) C "
	                   + " LEFT JOIN "
	                   + " ( "
	                   + "    select case when max(deadlineday) > sysdate then '진행중' else '마감' end as progress, fk_company_id "
	                   + "    from tbl_recruitment "
	                   + "    group by fk_company_id "
	                   + " ) R "
	                   + " ON fk_company_id = company_Id ";
	        
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setString(1, "%"+companyName+"%");  // 첫 번째 파라미터
	        
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {  // 여러 행을 처리할 수 있도록 변경
	            CompanyDTO companyDTO = new CompanyDTO();
	            
	            companyDTO.setName(rs.getString("name"));
	            companyDTO.setIndustry(rs.getString("industry"));
	            companyDTO.setBusinessNo(rs.getString("business_no"));
	            companyDTO.setAddress(rs.getString("address"));
	            companyDTO.setProgress(rs.getString("progress"));
	        	companyDTO.setStatus(rs.getInt("status"));
	        	companyDTO.setEmail(rs.getString("email"));
	        	companyDTO.setCompanyId(rs.getString("company_id"));
	        	companyDTO.setTel(rs.getString("tel"));
	            
	            companyNameList.add(companyDTO);  // 리스트에 추가
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        close();
	    }
	    
	    return companyNameList;  // 리스트 반환
	}


	




	//업종별 검색

	@Override
	public List<CompanyDTO> companyIndustryList(String industry) {
		List<CompanyDTO>companyIndustryList = new ArrayList<>();
		
		try {
			String sql = " select name, industry, business_no, address, status, email, company_id, tel, "
					+ " case when progress is null then '채용없음' else progress end as progress "
					+ " from ("
					+ " select  name, industry, business_no, address, company_Id, status, email, tel "
					+ " from tbl_company "
					+ " where  upper(industry) like upper(?) "
					+ " )C "
					+ " LEFT JOIN "
					+ " ( "
					+ " select case when max(deadlineday) > sysdate then '진행중' else '마감' end as progress, fk_company_id "
					+ " from tbl_recruitment "
					+ " group by fk_company_id "
					+ " )R "
					+ " ON fk_company_id = company_Id ";
		
		pstmt=conn.prepareStatement(sql);

		
		pstmt.setString(1, "%"+industry+"%");

		
		rs = pstmt.executeQuery();
		while(rs.next()) {
			CompanyDTO companyDTO = new CompanyDTO();
			
			companyDTO.setName(rs.getString("name"));
			companyDTO.setIndustry(rs.getString("industry"));
			companyDTO.setBusinessNo(rs.getString("business_no"));
			companyDTO.setAddress(rs.getString("address"));
			companyDTO.setProgress(rs.getString("progress"));
			companyDTO.setStatus(rs.getInt("status"));
        	companyDTO.setEmail(rs.getString("email"));
        	companyDTO.setCompanyId(rs.getString("company_id"));
        	companyDTO.setTel(rs.getString("tel"));
        	
			companyIndustryList.add(companyDTO); 
		}
		
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return  companyIndustryList;
	}





	//지역별 검색
	@Override
	public List<CompanyDTO> companyAddressList(String address) {
	
		List<CompanyDTO>  companyAddressList = new ArrayList<>();
		
		try {
			String sql = " select name, industry, business_no, address, status, email, company_id, tel, "
					+ " case when progress is null then '채용없음' else progress end as progress "
					+ " from ( "
					+ " select  name, industry, business_no, address, company_Id, status, email, tel "
					+ " from tbl_company "
					+ " where upper(address) like upper(?) "
					+ " )C "
					+ " LEFT JOIN "
					+ " ( "
					+ " select case when max(deadlineday) > sysdate then '진행중' else '마감' end as progress, fk_company_id "
					+ " from tbl_recruitment "
					+ " group by fk_company_id "
					+ " )R "
					+ " ON fk_company_id = company_Id ";
		pstmt=conn.prepareStatement(sql);

		
		pstmt.setString(1, "%"+address+"%");
		
		
		rs = pstmt.executeQuery();
		while(rs.next()) {
			
			CompanyDTO companyDTO = new CompanyDTO();
			
			companyDTO.setName(rs.getString("name"));
			companyDTO.setIndustry(rs.getString("industry"));
			companyDTO.setBusinessNo(rs.getString("business_no"));
			companyDTO.setAddress(rs.getString("address"));
			companyDTO.setProgress(rs.getString("progress"));
			companyDTO.setStatus(rs.getInt("status"));
        	companyDTO.setEmail(rs.getString("email"));
        	companyDTO.setCompanyId(rs.getString("company_id"));
        	companyDTO.setTel(rs.getString("tel"));
			
			companyAddressList.add(companyDTO); 
		}
		
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return  companyAddressList ;
	}


	// 기업형태별 회사 통계
	@Override
	public Map<String, Integer> getBusinessTypeRatio() {
		Map<String, Integer> map = new HashMap<>(); // "big" : 대기업, "mid" : 중견기업, "small" : 중소기업
		
		map.put("0", 0); // 대기업
		map.put("1", 0); // 중견기업
		map.put("2", 0); // 중소기업
		
		try {
			String sql 	= " select decode(business_type, 0, 'big', 1, 'mid', 2, 'small', 'total') as business_type, count(*) as count "
						+ " from tbl_company "
						+ " group by rollup(business_type) ";  
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			// total, big, mid, small
			while(rs.next()) {
				map.put(rs.getString("business_type"), rs.getInt("count"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	
		return map;
	}


	// 리뷰 순위 top10 회사 목록
	@Override
	public List<CompanyDTO> companyTopTenList(){
		
		List<CompanyDTO> companyList = new ArrayList<>();
	    
	    try {
	        String sql = " select name, industry, business_no, address, status, email, C.company_id, tel, score, rank, "
	                   + " case when progress is null then '채용없음' else progress end as progress "
	                   + " from ("
	                   + "    select name, industry, business_no, address, company_Id, status, email, tel "
	                   + "    from tbl_company "
	                   + " ) C "
	                   + " LEFT JOIN "
	                   + " ( "
	                   + "    select case when max(deadlineday) > sysdate then '진행중' else '마감' end as progress, fk_company_id "
	                   + "    from tbl_recruitment "
	                   + "    group by fk_company_id "
	                   + " ) RM "
	                   + " ON RM.fk_company_id = C.company_Id "
	                   + " JOIN"
	                   + " ( "
	                   + "    select company_id, avg(SCORE) as score, RANK() OVER(ORDER BY avg(SCORE) DESC) AS rank "
	                   + "    from tbl_company JOIN tbl_review "
	                   + "    on company_id = fk_company_id "
	                   + "    group by company_id"
	                   + " ) V1 "
	                   + " ON V1.company_id = RM.fk_company_Id "
	                   + " where rownum <= 10 ";
	        
	        pstmt = conn.prepareStatement(sql);
	        
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {  // 여러 행을 처리할 수 있도록 변경
	            CompanyDTO companyDTO = new CompanyDTO();
	            
	            companyDTO.setName(rs.getString("name"));
	            companyDTO.setIndustry(rs.getString("industry"));
	            companyDTO.setBusinessNo(rs.getString("business_no"));
	            companyDTO.setAddress(rs.getString("address"));
	            companyDTO.setProgress(rs.getString("progress"));
	        	companyDTO.setStatus(rs.getInt("status"));
	        	companyDTO.setEmail(rs.getString("email"));
	        	companyDTO.setCompanyId(rs.getString("company_id"));
	        	companyDTO.setTel(rs.getString("tel"));
	        	companyDTO.setScore(rs.getFloat("score"));
	        	companyDTO.setRank(rs.getInt("rank"));
	            
	            companyList.add(companyDTO);  // 리스트에 추가
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        close();
	    }
	    
	    return companyList;


	}//companySearchList(String searchStr, String select)---------
}
