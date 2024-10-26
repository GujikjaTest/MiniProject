package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlignUtil {
	
	private AlignUtil() {}
	
	/*
	 * 내용을 30자씩 엔터 쳐주는 메소드
	 * 두번째 줄부터 자동으로 \t 을 추가 해준다.
	 */
	public static String contents(String contentsStr) {
		StringBuilder sb = new StringBuilder();
		int strLength = 30;
		
		if(contentsStr.length()/strLength>0) {
			int i=0;
			sb.append(contentsStr.substring(strLength*i, strLength*(i+1)) + "\n");
			for(i=1; i<contentsStr.length()/strLength; i++) {
				sb.append("\t"+contentsStr.substring(strLength*i, strLength*(i+1)) + "\n");
			}
			sb.append("\t"+contentsStr.substring(strLength*i, contentsStr.length()));
		}
		else {
			sb.append(contentsStr);
		}
		
		return sb.toString();
	}
	
	/*
	 * 제목을 자동으로 생성해주는 메소드
	 * 타이틀 바의 길이를 50으로 고정한 메소드이다.
	 */
	public static String title(String titleStr) { // 자동 50칸 타이블 바
		String resultStr = title(titleStr, 50);
		return resultStr;
	}
	
	/*
	 * 제목을 자동으로 생성해주는 메소드
	 * 
	 * 사용법
	 * -데이터베이스 제목
	 * =메뉴 제목
	 * 을 넣으면 알아서
	 * -< 데이터베이스 제목 >--------
	 * ===< 메뉴 제목 >===
	 * 으로 수정해준다.
	 * 뒤에 숫자는 타이틀 바의 길이이다.
	 */
	public static String title(String titleStr, int n) { // 직접 타이틀 바 수 설정
		
		String resultStr = "";
		
		String bar = "";
		int align = 0; // 0:왼쪽정렬, 1:중앙정렬
		
		if(titleStr.startsWith("-")) {
			bar = "-";
			align = 0;
		}
		else if(titleStr.startsWith("=")) {
			bar = "=";
			align = 1;
		}
		titleStr = titleStr.substring(1);
		double cnt = countStr(titleStr);
		
		if(align == 0) {
			resultStr = bar.repeat(1)
					+ "< "+titleStr+" >"
					+ bar.repeat((int)Math.floor(n-5-cnt));
		}
		else {
			resultStr = bar.repeat((int)Math.floor(n/2-2-cnt/2))
					+ "< "+titleStr+" >"
					+ bar.repeat((int)Math.ceil(n/2-2-cnt/2));
		}
		
		return resultStr + "\n";
	}
	
	/*
	 * 자동으로 탭 간격을 조정해주는 메소드
	 * 데이터의 길이가 들쑥날쑥하고, 한글의 길이와 영어의 길이가 다른 것을 계산하여 탭 간격을 조정해주는 메소드이다.
	 * 
	 * 사용법
	 * 데이터1\t데이터2\t\t데이터3
	 * 을 StringBuilder로 넣었을 때 만약 데이터2의 길이가 길다면 \t을 하나로 줄여주는 등 알아서 계산해준다.
	 */
	public static StringBuilder tab(StringBuilder sb) {
		
		sb.append("\\\\"); // 마지막 문자로 사용
		String beforeStr = sb.toString();
		
		String[] strArr =  beforeStr.split("\t");
		
		StringBuilder resultSb = new StringBuilder();
		
		for(int i=0; i<strArr.length; i++) {
			if(strArr[i].isEmpty()) {
				continue;
			}
			else if(strArr[i].endsWith("\\\\")) { // 마지막 문자로 사용
				resultSb.append(strArr[i].split("\\\\")[0]);
				continue;
			}
			else if(strArr[i].contains("\n")) {
				int lastIdx = strArr[i].lastIndexOf("\n");
				resultSb.append(strArr[i].substring(0, lastIdx+1));
				strArr[i] = strArr[i].substring(lastIdx+1);
			}
			
			// 반복문 돌려서 뒤에 나오는 문자열이 빈칸인지 확인
			int tabCount = 1;
			for(int j = i+1; j<strArr.length; j++) {
				if(strArr[j].isEmpty()) {
					tabCount++;
				}
				else {
					break;
				}
			}
			
			double d = countStr(strArr[i]);
			
			int tab = tabCount-(int) Math.floor(d/8.0);
			if(tab<0) {
				tab = 1;
			}
			
			resultSb.append(strArr[i] + "\t".repeat(tab));
		}
		return resultSb;
	}
	
	/*
	 * 한글과 한글 외 문자가 몇 자 있는지 계산하여 총 글자의 표시 길이를 계산해주는 메소드
	 * 한글은 콘솔 창에서 1.5칸을 차지한다고 보고 계산한 결과값을 반환한다.
	 */
	private static double countStr(String str) {
		String regex = "[가-힣ㄱ-ㅎㅏ-ㅣ]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		double cnt = 0.0;
		
		while (matcher.find()) {
			cnt = cnt+1.5;
		}

		regex = "[^가-힣ㄱ-ㅎㅏ-ㅣ]";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			cnt = cnt+1.0;
		}
		
		return cnt;
	}
}
