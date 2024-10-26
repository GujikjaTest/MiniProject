package utils;

import java.util.Scanner;

public class Msg {
	public static void W(String msg){
		System.out.println(">> [경고] " + msg + " <<\n");
	}
	
	public static void N(String msg){
		System.out.println(">> " + msg + " <<\n");
	}
	
	public static boolean YN(String msg, Scanner sc) {
		do {
			System.out.print("▷ 정말로 "+msg+"(을)를 하시겠습니까? [ Y / N ] : ");
			String yn = sc.nextLine();
			
			if("y".equalsIgnoreCase(yn)) {
				return true;
			}
			else if("n".equalsIgnoreCase(yn)) {
				Msg.N(msg+"(을)를 취소하였습니다.");
				return false;
			}
			else {
				Msg.W("Y 또는 N 만 입력 가능합니다.");
			}
		} while(true);
	}
	
	private Msg() {}
}
