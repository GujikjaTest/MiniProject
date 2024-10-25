package member.controller;

import java.util.Scanner;

import applicant.controller.ApplicantController;
import applicant.domain.ApplicantDTO;
import utils.Msg;
import company.controller.CompanyController;
import company.domain.CompanyDTO;
import admin.controller.AdminController;
import admin.domain.AdminDTO;

public class MemberController {
	
	// field
	ApplicantController applicantCtrl = new ApplicantController();
	CompanyController companyCtrl = new CompanyController();
	AdminController adminCtrl = new AdminController();
	
	// method
	// === 멤버 회원가입 === //
	public void register(Scanner sc) {
		System.out.println("\n=====< 회원가입 >=====\n"
						 + "1.구직자   2.구인회사\n"
						 + "0.돌아가기\n"
						 + "======================\n");
		System.out.print("▷ 메뉴 번호 선택 : ");
		String menu = sc.nextLine();
		
		switch (menu) {
			case "1": // 구직자 회원가입
				applicantCtrl.register(sc);
				break;
	
			case "2": // 구인회사 회원가입
				companyCtrl.register(sc);
				break;
	
			case "0": // 돌아가기
				return;
	
			default:
				Msg.W("입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다.");
				break;
		}// end of switch (menu)--------------------
	}// end of public void memberRegister(Scanner sc)-------------------------
	
	
	// === 멤버 로그인 메뉴 === //
	public void login(Scanner sc) {
		do {
			System.out.println("\n==========< 로그인 >============\r\n"
						 	 + "1.구직자   2.구인회사   3.관리자\r\n"
							 + "0.돌아가기\r\n"
							 + "================================");
			System.out.print("▷ 메뉴 번호 선택 : ");
			String menu = sc.nextLine();
			
			switch (menu) {
			case "1": // 구직자 로그인
				ApplicantDTO applicantDTO = applicantCtrl.login(sc);
				if(applicantDTO != null) {
					applicantCtrl.applicantMenu(applicantDTO, sc);
				}
				
				break;
	
			case "2": // 구인회사 로그인
				CompanyDTO companyDTO = companyCtrl.login(sc);
				if(companyDTO != null) {
					companyCtrl.companyMenu(companyDTO, sc);
				}
				break;
	
			case "3": // 관리자 로그인
				AdminDTO adminDTO = adminCtrl.login(sc);
				if(adminDTO != null) {
					adminCtrl.adminMenu(adminDTO, sc);
				}
				break;
			
			case "0": // 돌아가기
				return;
	
			default:
				Msg.W("입력하신 메뉴 번호 "+menu+"는 존재하지 않습니다.");
				break;
			}// end of switch (menu)------------------------------
			
		} while(true);
	}// end of public void memberLogin(Scanner sc)-------------------------
}
