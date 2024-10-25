package resume.controller;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import applicant.domain.ApplicantDTO;
import common.Transaction;
import job.domain.JobDTO;
import resume.domain.ResumeDTO;
import resume.model.ResumeDAO;
import resume.model.ResumeDAO_imple;


public class Resumecontroller {
	
	
	Scanner sc = new Scanner(System.in);
	StringBuilder sb = new StringBuilder();
	ResumeDAO resumeDAO = new ResumeDAO_imple();
	ResumeDTO resumeDTO = null;
	JobDTO jobDTO = null;
	
	
	/*
	 =====< 메뉴 >======================
	 1.이력서 작성  2.이력서 수정  0.나가기
	 */
	public void list_Resume(ApplicantDTO applicant) {	
		StringBuilder sb = new StringBuilder();
		String menumno = "";
		do {
			int n = 0; // 1이면 입사지원 탭 n 아니면 다른 것 ex_ 1이면 == 입사지원 ==  그 외 == 이력서 관리 ==
			
			boolean completed = resumeDAO.resume_Completed(applicant);
			if(completed == true) {
				resumeInfo(sb,jobDTO,resumeDTO,resumeDAO,applicant,n);
			}
			
 		
 		
 		////////////////////////////////////////////////////////////////////////////////////////////
 		
 		resumeDAO = new ResumeDAO_imple();
 		
 		
		
			System.out.println("=====< 메뉴 >======================\n"
								+ "1.이력서 작성  2.이력서 수정  0.돌아가기\n"
								+ "==================================\n");
			
			
			System.out.print("메뉴번호 선택 : ");
			menumno = sc.nextLine();
			
			switch (menumno) {
				case "1": // 이력서 작성
					writeResume(applicant);
					
					break;
					
				case "2": // 이력서 수정
					modifyResume(applicant);
					break;
					
				case "0": // 나가기
					
					break;
		
				default:
					System.out.println(">> [경고] 입력하신 메뉴 번호 "+menumno+"은(는) 존재하지 않는 번호입니다. <<");
					break;
			} //end of switch (menumno)----------------
		}while(!"0".equals(menumno)); //end of do while ----
	}



	// int 형 변수를 유효성 검사하기 위해 str 임시 변수 추가 writeResume()과 modifyResume()에 필요.
	String str_Experience = "";
	String str_Education = "";
	String str_fk_Job_id = "";
	String str_Hope_salary = "";
	
	//while 문을 탈출하기 위한 변수
	boolean is_Exit = false;


	// 이력서 작성
	public void writeResume(ApplicantDTO applicant) {
		try {
			// ** 이력서가 이미 작성 돼있는지, 안돼있는지 ** //
			boolean completed = resumeDAO.resume_Completed(applicant);
			if(completed == true) {
				System.out.println(">> [경고] 이미 작성된 이력서가 있습니다. <<\n");
				return;
			}
			
			int experience = 0;
			int education = 0;
			String hope_location = "";
			String job_description = "";
			int fk_job_id = 0;
			int hope_salary = 0;
				
			
		   //경력
		   do{
				resumeDTO = new ResumeDTO();
				
				System.out.print("[0:신입 1:경력]\n"
								+ "▷ 경력[위의 번호 중 선택하세요!] : ");
				str_Experience = sc.nextLine();
				
				if(!("0".equals(str_Experience)|| "1".equals(str_Experience))){
					System.out.println(">> [경고] 경력은 0 또는 1만 입력하셔야합니다. <<\n");
					
					continue;
				}
				else if(str_Experience.isBlank()){
					System.out.println(">> [경고] 번호를 선택해주세요!<< \n");
				}
				else {
					experience = Integer.parseInt(str_Experience);
					is_Exit = true;
				}
		    }while(!is_Exit); // end of do while()--------------
			   
			   
		   is_Exit = false;
			   
				
			//학력	
			do {
				try {
					System.out.print("\n[0:대졸 1:고졸 2:초대졸 3.대학교재학 4.대학교휴학]\n"
								     + "▷ 학력[위의 번호 중 선택하세요!] : ");
					str_Education = sc.nextLine();
					if(str_Education.isBlank()) {
						System.out.println(">> [경고] 번호를 선택해주세요!<< \n");
					}
					 
					else if(!(Integer.parseInt(str_Education) >= 0 && (Integer.parseInt(str_Education) <= 4))){
						System.out.println(">> [경고] 경력은 0,1,2,3,4 만 입력하셔야합니다. <<\n");
						
						continue;
					}
					else {
						education = Integer.parseInt(str_Education);
						is_Exit = true;
					}
				}catch(NumberFormatException e) {
					System.out.println(">> [경고] 숫자로만 입력하셔야합니다! <<\n");
				}
			}while(!is_Exit); // end of do while()--------------	
				
			is_Exit = false;
			
			
			//희망근무지역
			do {
				System.out.print("\n[서울,경기도,충청도,전라도] \n"
								+ "▷ 희망근무지역[위의 지역 중 선택하세요!] : ");
				hope_location = sc.nextLine();

				if(hope_location.isBlank()) { //
					System.out.println("\n[경고] 지역을 입력하세요! \n");
					continue;
				}
				else if(!(hope_location.equals("서울") || hope_location.equals("경기도") 
						|| hope_location.equals("충청도") || hope_location.equals("전라도"))) {
					System.out.println(">> [경고] [서울,경기도,충청도,전라도] 중 입력하세요! << \n");
				}
				else {
					is_Exit = true;
				}
				
			}while(!is_Exit); // end of do while()--------------
			
			
			is_Exit = false;
			
			
			//스펙(직무기술)
			do {
				System.out.print("\n직무기술 : ");
				job_description = sc.nextLine();
				
				if(job_description.isBlank()) { //
					System.out.println("[경고] 직무기술을 입력하세요! \n");
					continue;
				}
				else {
					is_Exit = true;
				}
				
			}while(!is_Exit); // end of do while()--------------
			
			
			is_Exit = false;
			
			
			//희망직종목록
			do {
				try {
					
						System.out.println("\n====================< 희망직종 목록 >=====================\r\n"
								+ "1.기획·전략         2.법무·사무·총무       3.인사·HR\r\n"
								+ "4.회계·세무         5.마케팅·광고·MD       6.개발·데이터\r\n"
								+ "7.디자인            8.물류·무역           9.운전·운송·배송\r\n"
								+ "10.영업            11.고객상담·TM        12.금융·보험\r\n"
								+ "13.식·음료          14.고객서비스·리테일    15.엔지니어링·설계\r\n"
								+ "16.제조·생산        17.교육               18.건축·시설\r\n"
								+ "19.의료·바이오      20.미디어·문화·스포츠     21.공공·복지\r\n"
								+ "===========================================================");
						System.out.print("희망직종 번호입력 : ");
						str_fk_Job_id = sc.nextLine();	
						
						
						if(str_fk_Job_id.isBlank()) {
							System.out.println("[경고] 희망직종번호를 입력하세요! \n");
						}
						else if(!(Integer.parseInt(str_fk_Job_id)  >0 && Integer.parseInt(str_fk_Job_id) < 22)){
							System.out.println(">> [경고] 경력은 1부터 21까지 숫자로 입력하셔야합니다. <<\n");
							
							continue;
						}
						else {
							fk_job_id = Integer.parseInt(str_fk_Job_id);
							is_Exit = true;
						}
				}catch(InputMismatchException e) {
					System.out.println(">>> [경고] 숫자로만 입력하셔야합니다! <<<\n");
					
					continue;
				}catch(NumberFormatException e) {
					System.out.println(">> [경고] 숫자로만 입력하셔야합니다! <<\n");
				}
	        }while(!is_Exit); // end of do while()--------------
					
		   
		   is_Exit = false;
				
		   
		   //희망연봉
		   do {
			   try {
				   
					System.out.print("희망연봉 : ");
					str_Hope_salary = sc.nextLine();
					
					if(str_Hope_salary.isBlank()){
						System.out.println("\n>> [경고] 희망연봉을 입력하세요! <<\n");
						
						continue;
					}
					else {
						hope_salary = Integer.parseInt(str_Hope_salary);
						is_Exit = true;
					}
					
			   }catch(InputMismatchException e) {
				   
					System.out.println(">>> [경고] 숫자로 입력하세요! <<<\n");
					
					continue;
					
				} catch(NumberFormatException e) {
					
					System.out.println(">>> [경고] 숫자로 입력하세요! <<<\n");
					
					continue;
				}
				
		   }while(!is_Exit); // end of do while()--------------
				
				
			
		   
			resumeDTO.setExperience(experience);
			resumeDTO.setEducation(education);
			resumeDTO.setHope_location(hope_location);
			resumeDTO.setJob_description(job_description);
			resumeDTO.setFk_job_id(fk_job_id);
			resumeDTO.setHope_salary(hope_salary);
			
			
			int result = resumeDAO.writeResume(applicant,resumeDTO);//sql문 실행 메소드 DAO(작성됨)
			
			if(result != 0) {
				System.out.println("이력서 작성을 성공하셨습니다!");
				//resumeInfo(sb,jobDTO,resumeDTO,resumeDAO,applicant); //작성된 이력서 보여주기(본인이력서 보여주는 메소드)
			}
			else {
				System.out.println("이력서 작성을 실패하였습니다!");
			}
			
		}catch(NullPointerException e) {
		}catch(InputMismatchException e) {
		System.out.println(">>> [경고] 숫자로 입력하세요! <<<\n");
		return;
		}
	}// end of private void writeResume()------------
	
	
	// 이력서 수정
	private void modifyResume(ApplicantDTO applicant) {
		
		boolean completed = resumeDAO.resume_Completed(applicant);
		if(completed == false) {
			System.out.println(">> [경고] 작성된 이력서가 없습니다. <<\n");
			return;
		}
		
		// 새로운 ResumeDTO()객체 생성 (ResumeDTO에 기존에 존재하는 이력서의 값을 set해주기 위함)
		ResumeDTO resumeDTO = new ResumeDTO();
		
		int n = 0; // 1이면 입사지원 탭 n 아니면 다른 것 ex_ 1이면 == 입사지원 ==  그 외 == 이력서 관리 ==
		resumeDTO = resumeInfo(sb,jobDTO,resumeDTO,resumeDAO,applicant,n); 
		
		
		
		int experience = 0;
		int education = 0;
		String hope_location = "";
		String job_description = "";
		int fk_job_id = 0;
		int hope_salary = 0;
		
		System.out.println(">> [주의사항] 변경하지 않고 예전의 데이터값을 그대로 사용하시려면 그냥 엔터하세요!! <<\n");
		
		
		//경력
		do {
			System.out.print("[0:신입 1:경력]\n"
							+ "▷ 경력[위의 번호 중 선택하세요!] : ");
			str_Experience = sc.nextLine();
			
			if(str_Experience.isBlank()){ // 변경하지 않고 그냥 엔터일 경우
				
				experience = resumeDTO.getExperience();
				is_Exit = true;
			}
			else if(!("0".equals(str_Experience)|| "1".equals(str_Experience))){ 
				System.out.println(">> [경고] 경력은 0 또는 1만 입력하셔야합니다. <<\n");
				
				continue;
			}
			else {
				experience = Integer.parseInt(str_Experience);
				is_Exit = true;
			}
		}while(!is_Exit); // end of do while()--------------
	
		
		is_Exit = false;
		
		
		//학력
		do {
			try {
				System.out.print("\n[0:대졸 1:고졸 2:초대졸 3.대학교재학 4.대학교휴학]\n"
							     + "▷ 학력[위의 번호 중 선택하세요!] : ");
				str_Education = sc.nextLine();
				
				if(str_Education.isBlank()) { // 변경하지 않고 그냥 엔터일 경우
					education = resumeDTO.getEducation();
				    is_Exit = true;
				}
				else if(!(Integer.parseInt(str_Education) >= 0 && (Integer.parseInt(str_Education) <= 4))){
					System.out.println(">> [경고] 경력은 0,1,2,3,4 만 입력하셔야합니다. <<\n");
					
					continue;
				}
				else {
					education = Integer.parseInt(str_Education);
					is_Exit = true;
				}
			}catch(NumberFormatException e) {
				System.out.println(">> [경고] 숫자로만 입력하셔야합니다! <<\n");
			}
		}while(!is_Exit); // end of do while()--------------
		
		
		is_Exit = false;
		
		//희망직종
		do {
			try {
				
					System.out.println("\n====================< 희망직종 목록 >=====================\r\n"
							+ "1.기획·전략         2.법무·사무·총무       3.인사·HR\r\n"
							+ "4.회계·세무         5.마케팅·광고·MD       6.개발·데이터\r\n"
							+ "7.디자인            8.물류·무역           9.운전·운송·배송\r\n"
							+ "10.영업            11.고객상담·TM        12.금융·보험\r\n"
							+ "13.식·음료          14.고객서비스·리테일    15.엔지니어링·설계\r\n"
							+ "16.제조·생산        17.교육               18.건축·시설\r\n"
							+ "19.의료·바이오      20.미디어·문화·스포츠     21.공공·복지\r\n"
							+ "===========================================================");
					System.out.print("희망직종 번호입력 : ");
					str_fk_Job_id = sc.nextLine();	
					
					
					if(str_fk_Job_id.isBlank()) {
						fk_job_id = resumeDTO.getJobDTO().getJob_id();
						is_Exit = true;
					}
					else if(!(Integer.parseInt(str_fk_Job_id)  >0 && Integer.parseInt(str_fk_Job_id) < 22)){
						System.out.println(">> [경고] 경력은 1부터 21까지 숫자로 입력하셔야합니다. <<\n");
						
						continue;
					}
					else {
						fk_job_id = Integer.parseInt(str_fk_Job_id);
						is_Exit = true;
					}
			}catch(InputMismatchException e) {
				System.out.println(">>> [경고] 숫자로만 입력하셔야합니다! <<<\n");
				
				continue;
			}catch(NumberFormatException e) {
				System.out.println(">> [경고] 숫자로만 입력하셔야합니다! <<\n");
			}
        }while(!is_Exit); // end of do while()--------------
		
		is_Exit = false;
		
		//스펙(직무기술)
		do {
			System.out.print("\n직무기술 : ");
			job_description = sc.nextLine();
			
			if(job_description.isBlank()) { 
				job_description = resumeDTO.getJob_description();
				
				is_Exit = true;
			}
			else {
				is_Exit = true;
			}
			
				
			
		}while(!is_Exit); // end of do while()--------------
		
		is_Exit = false;
		
		//희망근무지역
		do {
			System.out.print("\n[서울,경기도,충청도,전라도] \n"
							+ "▷ 희망근무지역[위의 지역 중 선택하세요!] : ");
			hope_location = sc.nextLine();

			if(hope_location.isBlank()) {
				
				hope_location = resumeDTO.getHope_location();
				is_Exit = true;
				
			}
			else if(!(hope_location.equals("서울") || hope_location.equals("경기도") 
					|| hope_location.equals("충청도") || hope_location.equals("전라도"))) {
				System.out.println("[경고] [서울,경기도,충청도,전라도] 중 입력하세요! \n");
			}
			else {
				is_Exit = true;
			}
			
		}while(!is_Exit); // end of do while()--------------
		
		
		
		
		
		is_Exit = false;
		
		
		//희망연봉
		do {
			   try {
				   
					System.out.print("희망연봉 : ");
					str_Hope_salary = sc.nextLine();
					
					if(str_Hope_salary.isBlank()){
						hope_salary = resumeDTO.getHope_salary();
						
						is_Exit = true;
					}
					else { 
						hope_salary = Integer.parseInt(str_Hope_salary);
						is_Exit = true;
					}
					
			   }catch(InputMismatchException e) {
				   
					System.out.println(">>> [경고] 숫자로 입력하세요! <<<\n");
					
					continue;
					
				} catch(NumberFormatException e) {
					
					System.out.println(">>> [경고] 숫자로 입력하세요! <<<\n");
					
					continue;
				}
				
		   }while(!is_Exit); // end of do while()--------------
		
		
		resumeDTO.setExperience(experience);
		resumeDTO.setEducation(education);
		resumeDTO.setFk_job_id(fk_job_id);
		resumeDTO.setJob_description(job_description);
		resumeDTO.setHope_location(hope_location);
		resumeDTO.setHope_salary(hope_salary);
		
		
		int result = resumeDAO.modifyResume(resumeDTO,applicant);
		
		if(result != 0) {
			//resumeInfo(sb,jobDTO,resumeDTO,resumeDAO,applicant);
			System.out.println("\n>> 이력서 수정이 완료되었습니다. <<\n");
		}
		else {
			System.out.println(">> 이력서 수정에 실패하였습니다. <<\n");
		}
		
	}// end of private void modifyResume(ApplicantDTO applicant) ------------
	
   /*
    * 문자열이 한글인지 검사
    * 문자열에 한글이 아닌 문자가 포함되는 것을 허용
    */
   public boolean isKoreanValid(String str) {
      String regex = "[가-힣]";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(str);

      // 한글이 전혀 포함되지 않은 경우
      if (!matcher.find()) {
         return false;
      }
      
      return true;
   }
   
   // *** 이력서 보여주기 *** //
	public ResumeDTO resumeInfo(StringBuilder sb,JobDTO jobDTO,ResumeDTO resumeDTO,ResumeDAO resumeDAO,ApplicantDTO applicant,int n){
		sb = new StringBuilder();
		if(n==1) {
			sb.append("\n=== 입사지원 ===\n");
		}else {
			sb.append("=== 이력서 관리 ===\n");
		}
		sb.append("-< "+ applicant.getName() +"님의 이력서 >-------------------------------------------\n");
		sb.append("경력     학력    희망근무지역      희망직종       직무기술   희망연봉\n");
		sb.append("-------------------------------------------------------------");

		System.out.println(sb.toString());
		/*
		 ex 신입 	남서울대학교	서울	백엔드	Spring	3,000만원
		*/
 		
 		resumeDTO = resumeDAO.list_Resume(applicant); // 이력서 관리(이력서를 보여줌)
 		
 		
 		sb = new StringBuilder(); //StringBuilder 초기화
 		
 		
 		sb.append(Transaction.experience((resumeDTO.getExperience())) +"   "+Transaction.education(resumeDTO.getEducation())   + "   " +
 				resumeDTO.getHope_location() + "        " + resumeDTO.getJobDTO().getName() + "       " + //getName -> 희망직종
 				resumeDTO.getJob_description() + "      " + resumeDTO.getHope_salary());
 		
 		System.out.println(sb.toString()); 
 		
 		return resumeDTO;
	}
	
}//end of public class Resumecontroller-------------
