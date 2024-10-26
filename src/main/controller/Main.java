package main.controller;

import java.util.Scanner;

import common.ProjectDBConnection;
import member.controller.MemberController;
import utils.Msg;

public class Main {

	public static void main(String[] args) {
		
		MemberController ctrl = new MemberController();
		Scanner sc = new Scanner(System.in);
		String menu;
		
		do {
			System.out.println("\n=====< 메인메뉴 >=====\n"
					+ "1. 회원가입   2. 로그인\n"
					+ "0. 프로그램 종료\n"
					+ "====================");
			
			System.out.print("▷ 메뉴 번호 선택 : ");
			menu = sc.nextLine();
			
			switch (menu) {
				case "1": // 회원가입
					ctrl.register(sc);
					break;
				
				case "2": // 로그인
					ctrl.login(sc);
					break;
					
				case "0": // 프로그램 종료
					ProjectDBConnection.closeConnection();
					break;
		
				default:
					Msg.W("입력하신 메뉴 번호 " + menu + "는 존재하지 않습니다.");
					break;
			}
		} while(!"0".equals(menu));
		
		sc.close();
		
		Msg.N("저희 프로그램을 이용해 주셔서 감사합니다. 좋은 하루 되세요!(*´▽`*)");
	}

}
