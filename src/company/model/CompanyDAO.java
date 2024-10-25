package company.model;

import java.util.List;
import java.util.Map;
//import java.util.Scanner;

import company.domain.CompanyDTO;

public interface CompanyDAO {

	//회원가입 메소드
	int register(CompanyDTO companyDTO);

	//로그인 메소드
	CompanyDTO login(Map<String, String> paraMap);

	//회사정보 수정
	int cpInfoUpdate(CompanyDTO companyDTO);

	//회사명 검색
	List<CompanyDTO> companyNameList(String companyName);
	
	//업종별 검색	
	List<CompanyDTO> companyIndustryList(String industry);

	//지역별검색
	List<CompanyDTO> companyAddressList(String address);

	// 기업형태별 회사 통계
	Map<String, Integer> getBusinessTypeRatio();
	
	//검색통합
	List<CompanyDTO> companySearchList(String searchStr, String select);
}
