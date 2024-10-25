package company.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import company.domain.CompanyDTO;
import company.model.CompanyDAO_imple;
import notification.controller.NotificationController;
import recruitment.controller.RecruitmentController;
import review.controller.ReviewController;
import utils.AlignUtil;
import utils.Msg;
import utils.ValidationUtil;


public class CompanyController {

	//field
	CompanyDAO_imple cdao = new CompanyDAO_imple();

	//method
	
	//register
	// == 구인회사 회원가입 메소드 ==//
	
	public void register(Scanner sc) {


		String name = "";
		String businessNo= "";
		String address = "";
		int businessType = 0;
		String industry = "";
		String tel= "";
		String companyId = "";
		String passwd ="";
		String email ="";
		String businessTypeStr = "";
				
		System.out.println("\n=== 구인회사 회원가입 ===\n");
		
		
		do {
			System.out.print("1. 아이디 : ");
			companyId = sc.nextLine();
			
			if(ValidationUtil.isIDValid(companyId)) {
				break;
			}
		
		}while(true);
		
		
		
		do {
			System.out.print("2. 비밀번호 : ");
			passwd = sc.nextLine(); 
			
			if(ValidationUtil.isPasswordValid(passwd)) {
				break;
		    }
		
		}while(true);
	
		do {
			System.out.print("3. 이메일 : ");
			email = sc.nextLine();
		
			if(ValidationUtil.isEmailValid(email)) {
				break;
		    }
		
		}while(true);
		
		do {
			System.out.print("4. 회사명 : ");
			name = sc.nextLine();
			
			if(!name.isBlank()) {
				break;
			}
		
		}while(true);
		

		

		do {
			System.out.print("5. 사업자등록번호 : ");
			businessNo = sc.nextLine();
			
			
			if(!businessNo.isBlank()) {
				break;
			}
		
		}while(true);
		
		
		do {
			System.out.print("6. 주소 : ");
			address = sc.nextLine();
			
			if(!address.isBlank()) {
				break;
			}
		
		}while(true);
		
		
		do {
			
			System.out.print("7. 기업형태 [0:대기업, 1:중견기업 2:중소기업]: ");
			businessTypeStr = sc.nextLine();
		
			if(!businessTypeStr.isBlank()) {
				try {
					businessType = Integer.parseInt(businessTypeStr);
				}catch(NumberFormatException e) {
					Msg.W("숫자로 입력하세요.");
					continue ;
			    }
			 
			break;
			}
	
		}while(true);
		
		
		
		do {		
			System.out.print("8. 연락처 : ");
			tel = sc.nextLine();
			if(ValidationUtil.isTelValid(tel)) {
				break;
		    }
		}while(true);
		
		
		do {
			System.out.print("9. 업종 : ");
			 industry = sc.nextLine();
			if(!industry.isBlank()) {
				break;
			}
		 
		}while(true);
			
		
		
	
		//---------------------------------------------------------------//
		
		CompanyDTO company = new CompanyDTO();
		company.setCompanyId(companyId);
		company.setPasswd(passwd);
		company.setEmail(email);
		company.setName(name);
		company.setBusinessNo(businessNo);
		company.setAddress(address);
		company.setBusinessType(businessType);
		company.setIndustry(industry);
		company.setTel(tel);
		
		int n = cdao.register(company);
		
		if(n==1) {
			Msg.N("회원가입을 축하드립니다");
		}
		else {
			Msg.N("회원가입이 실패되었습니다");
		}
		
	
	}//public void register(Scanner sc)

	
	
	

	//login
	public CompanyDTO login(Scanner sc){
		
		System.out.println("=== 구인회사 로그인 ===");
		
		System.out.print("아이디 : ");
		String company_id = sc.nextLine();
		
		

		System.out.print("비밀번호 : ");
		String passwd = sc.nextLine();
	
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("company_id",company_id);
		paraMap.put("passwd",passwd);
		
		CompanyDTO company = cdao.login(paraMap);
		
		if (company != null) {
			System.out.println(">> ---- 로그인 성공! ---- <<");
		}
		else {
			System.out.println(">> ---- 로그인 실패 ---- <<");
		}
		
		return company;

	}//public void login(Scanner sc)
	
	
	
	
	
	
	
	//구인회사 전용 메뉴
	public void companyMenu(CompanyDTO companyDTO ,Scanner sc ){
		
		do {
		/////////////////////////////
			System.out.println("\n====< 구인회사 전용메뉴 ("+companyDTO.getName()+" 로그인 중...) >====");
			
			System.out.println("1. 우리회사 정보 관리      2.우리회사 리뷰 조회\n"
					         + "3. 우리회사 채용공고 관리   4.공지사항   0.로그아웃");
			System.out.println("==========================================");
			
			System.out.print("▷ 메뉴번호 선택 : ");
			String choiceNo = sc.nextLine();
			
			
			
			switch (choiceNo) {
			case "1"://우리회사정보관리 - 이지혜
				companyInfoManagement(companyDTO,sc);
				break;
					
			case "2"://우리회사리뷰조회 - 김규빈
				ReviewController reviewCtrl = new ReviewController();
				reviewCtrl.reviewCompanyMenu(companyDTO.getCompanyId(), sc);
				break;
				
			case "3"://우리회사채용공고 - 강이훈
				RecruitmentController recruitmentCtrl = new RecruitmentController();
				recruitmentCtrl.menuRecruitment(companyDTO , sc);
				break;
				
				
			case "4"://공지사항
				NotificationController notiCtrl = new NotificationController();
				notiCtrl.getNotificationDetails(false, sc);
				break;
		
		
			case "0"://로그아웃
		
				return;
	
			default:
				Msg.W("메뉴에 없는 번호입니다. 다시 선택해주세요.");
				break;
			}
		/////////////////////////////////////////
		}while(true);
		
	}


	
	

	//우리회사정보관리
	private void companyInfoManagement(CompanyDTO companyDTO, Scanner sc) {
		
		
		System.out.println("\n==== 우리 회사 정보관리 =====");
		System.out.println("\n--<"+companyDTO.getName()+"기업의 정보>--------------------------");
		System.out.println(companyDTO.toString());//companyDTO > toString 에 정보 들어가있음 
		System.out.println("-".repeat(45));
		
		
		
		System.out.println("\n=======< 메뉴 >==========");
		System.out.println("1. 회사정보수정  0.돌아가기");
		System.out.println("-".repeat(30));
		
		System.out.print("▷ 메뉴번호선택 : ");
		String choiceNo = sc.nextLine();
		
		switch (choiceNo) {
		case "1"://회사정보수정
			cpInfoUpdate(companyDTO, sc); //CompanyInfoModify
			
			break;
			
		case "0"://돌아가기
			
			break;

		default:
			Msg.W("메뉴에 없는 번호입니다. 다시 선택해주세요.");
			break;
		}
		
		
		
		
	}//end of private void companyInfoManagement(String name, Scanner sc) {


	
	

	//----회사정보 수정---
	private void cpInfoUpdate(CompanyDTO  companyDTO, Scanner sc) {
		
		

		System.out.println("\n>> [ 주의사항 ] 변경하지 않고 예전의 데이터값을 그대로 사용하시려면 그냥 엔터하세요.<<");

		
		
		System.out.print("▷ 비밀번호 [영어, 숫자, 특수문자 조합] :");
		String passwd = sc.nextLine();
		if(passwd.isBlank()) {
			passwd = companyDTO.getPasswd();
		}
		
		System.out.print("▷ 이메일 : ");
		String email = sc.nextLine();
		if(email.isBlank()) {
			email = companyDTO.getEmail();
		}
		
		System.out.print("▷ 회사명 : ");
		String name = sc.nextLine();
		if(name.isBlank()) {
			name = companyDTO.getName();
		}
		
		
		
		System.out.print("▷ 사업자등록번호 : ");
		String businessNo = sc.nextLine();
		if(businessNo.isBlank() ) {
			businessNo =companyDTO.getBusinessNo();
		}
		
		int businessType = 0;
		
		
		do {
			System.out.print("▷ 기업형태 [0:대기업/1:중견기업/2:중소기업] : ");
			String businessTypeStr = sc.nextLine();
			if(businessTypeStr.isBlank()) {
				businessType =  companyDTO.getBusinessType();
			}
			else {
				try {
					
					businessType = Integer.parseInt(businessTypeStr);
				
				}catch(NumberFormatException e) {
					Msg.W("숫자로 입력하세요.");
			
				}
			}
			break;
		}while(true);

		
		System.out.print("▷ 주소 : ");
		String address = sc.nextLine();
		if(address.isBlank()) {
			address = companyDTO.getAddress();
		}
		
		
		System.out.print("▷ 연락처 : ");
		String tel = sc.nextLine();
		if(tel.isBlank()) {
			tel = companyDTO.getTel();
		}
		
		
		System.out.print("▷ 업종 : ");
		String industry = sc.nextLine();
		if(industry.isBlank()) {
			industry = companyDTO.getIndustry();
		}
	    
	
	    companyDTO.setPasswd(passwd);
	    companyDTO.setEmail(email);
	    companyDTO.setName(name);
	    companyDTO.setBusinessNo(businessNo);
	    companyDTO.setBusinessType(businessType);
	    companyDTO.setAddress(address);
	    companyDTO.setTel(tel);
	    companyDTO.setIndustry(industry);
		
		//-------------------------------------------------------
	    String yn = "";
	    
	    do {
		    System.out.print(">> 정말로 수정하시겠습니까? [ Y / N ] : ");
		    yn = sc.nextLine();
			//--------------------------------------------------------------
		    
		    if("y".equalsIgnoreCase(yn)) {//y를 입력 했을 경우
		    

		    	int n = cdao.cpInfoUpdate(companyDTO);
		    	
		    	if(n==1) {
		    		System.out.println(">> 정보 수정 성공! <<\n");
		    		return;
		    	}
		    	else {
		    		System.out.println(">> SQL 구문 오류 발생으로 인해 글수정이 실패되었습니다. << \n");
		    		return;
		        }
		    	
		    }
	    	else if("n".equalsIgnoreCase(yn)) {//n을 입력했을 경우
	    		System.out.println("글 수정을 취소했습니다.\n");
	    		return;
	    	}
	    
	    	else {
	    		Msg.W("수정확인은 Y 또는 N 으로만 해주세요!");
	    	}
	    }while(true);
	}//end of private void cpInfoUpdate(companyDTO companyDTO, Scanner sc)
	

	
	public String title() {
		return "번호\t회사명\t업종\t지역\t사업자등록번호\t채용진행상황";
	}
	public String titleWithScore() {
		return "순위\t별점\t\t회사명\t업종\t지역\t채용진행상황";
	}
	

	
	public String kategorie() {
		return "=======< 카테고리별 검색 메뉴 >=======\n"
			 + "1.회사명  2.업종별  3.지역별  0.나가기\n"
			 + "==================================";
	}
	
	// === 구인회사찾기 === //
	public void searchCompany(Scanner sc) {
		System.out.println("=== 구인회사 검색 ===");
		System.out.println("-< 리뷰점수 상위 10개 구인회사 >---------------------------------");

		System.out.println(titleWithScore() );
		System.out.println("-".repeat(60));
		companyTopTenList(); // 리뷰 top10 출력
		System.out.println("-".repeat(60));
		
		
		do {
			 System.out.println(kategorie());//카테고리별검색메뉴
			 System.out.print("▷메뉴 입력 : ");
			 String selectKategorie = sc.nextLine();
			 
			 
			 //카테고리별 검색 메뉴
			switch (selectKategorie ) {
			case "1": //회사명 검색
				searchCompanyWithReview(sc, selectKategorie);
				break;
			case "2"://업종별 검색
				searchCompanyWithReview(sc, selectKategorie);
				break;
			case "3"://지역별 검색
				searchCompanyWithReview(sc, selectKategorie);
				break;
				
			case "0"://나가기
			
				return;
	
			default:
				Msg.W("메뉴에 없는 번호입니다. 다시 입력해주세요.");
				break;
			}
		 
		}while(true);
		 
	}//end of public void searchCompany(CompanyDTO companyDTO, Scanner sc) --------------------
	
	
	// 리뷰 Top 10 회사 목록 출력
	private void companyTopTenList() {
		List<CompanyDTO> companyList = cdao.companyTopTenList();
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<companyList.size();i++) {
			sb.append(companyList.get(i).companyInfoWithScore()+"\n");
		}
		System.out.print(AlignUtil.tab(sb).toString());
	}

	
	// 회사 통합 검색
	public String searchCompanyWithReview(Scanner sc, String select) {
		String searchType = "";
		switch (select) {
		default:
		case "1":
			searchType = "회사명";
			break;
		case "2":
			searchType = "업종별";
			break;
		case "3":
			searchType = "지역별";
			break;
		}
		System.out.print("▷ "+searchType+" 검색 : ");
		String companyName = sc.nextLine();
		
		List<CompanyDTO> companyList = cdao.companySearchList(companyName, select);
		
	
		if(companyList.size()>0) {//검색한 회사가 존재할때
			
			StringBuilder sb = new StringBuilder();
			
			System.out.println("-< '"+companyName+"'에 대한 검색결과 >-------------------------------------------");
			System.out.println(title());
			System.out.println("-".repeat(60));
			
			for(int i = 0; i<companyList.size();i++) {
				sb.append((i+1)+"\t"+companyList.get(i).companyInfo()+"\n");
			}
			sb.append("-".repeat(60));
			System.out.println(AlignUtil.tab(sb).toString());
			
		}

		else {//없을때
			Msg.N("검색하신 결과가 없습니다.");
			return null;
		}
		
		do {
			System.out.print("▷ 회사 번호 선택[0: 취소] : ");
			String num = sc.nextLine();
			
			if(ValidationUtil.isNumberValid(num)
					&& Integer.parseInt(num) != 0
					&& Integer.parseInt(num) <= companyList.size()) {
				
				int i = Integer.parseInt(num);
				
				System.out.println((companyList.get(i-1).companyInfoWithoutPasswd()));
				
				ReviewController reviewCtrl = new ReviewController();
				reviewCtrl.reviewCompanyMenu(companyList.get(i-1).getCompanyId(), sc);
				
				System.out.println();
				
				return companyList.get(i-1).getCompanyId();
			}
			else if(Integer.parseInt(num) == 0) {
				return null;
			}
			else {
				Msg.W("'"+num+"'은 없는 회사 번호입니다.");
			}
		} while(true);
	}//end of private void searchCompanyName(Scanner sc) 
	
}//end of public void mainMenu(Scanner sc){

