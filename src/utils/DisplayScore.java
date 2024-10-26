package utils;

public class DisplayScore {
	// 점수 표시
	// 1~5점을 별점으로 반환해주는 메소드
	public static String getStar(int num) {
		return "★".repeat(num) + "☆".repeat(5-num);
	}

	// 1~5점을 점으로 반환해주는 메소드
	public static String getScoreBar(int num) {
		return "●".repeat(num) + "○".repeat(5-num);
	}
	

	// 통계
	// 소수점이 있는 1~5점을 별점으로 반환해주는 메소드
	public static String getStar(float num) {
		StringBuilder sb = new StringBuilder();
		sb.append("★".repeat((int) num));
		
		if(num % 1 != 0.0) {
			sb.append("☆");
		}
		
		sb.append("\t"+Math.floor(num*10)/10.0);
		
		return sb.toString();
	}

	// 총 100/5(==100/div)점을 막대 num/div개로 반환해주는 메소드
	public static String getBar(int num) {
		int div = 5;
		int n = num/div;
		return "■".repeat(n) + "□".repeat(100/div-n) + " "+ num;
	}
	
	private DisplayScore() {}
}
