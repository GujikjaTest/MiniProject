package notification.domain;
/*
 * 관리자 공지사항 DTO
 * TBL_NOTIFICATION 테이블
 */
public class NotificationDTO {

	private int notificationId; // 공지일련번호

	private String fKAdminId; // 관리자아이디
	
	private String adminName; // 관리자 이름, 관리자 전용 메뉴 외의 위치에서 사용 불가능  

	private String title; // 공지제목

	private String contents; // 공지내용

	private String registerday; // 등록일자

	private int fix; // 공지 고정여부 0 : IS_FIXED, 1 : IS_NOT_FIXED
	
	private String updateday; // 최신 수정일자
	
	private int is_delete; // 삭제여부 0 : IS_DELETE, 1 : IS_NOT_DELETE

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getfKAdminId() {
		return fKAdminId;
	}

	public void setfKAdminId(String fKAdminId) {
		this.fKAdminId = fKAdminId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getRegisterday() {
		return registerday;
	}

	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}

	public int getFix() {
		return fix;
	}

	public void setFix(int fix) {
		this.fix = fix;
	}

	public String getUpdateday() {
		return updateday;
	}

	public void setUpdateday(String updateday) {
		this.updateday = updateday;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	/*
	 * 공지사항 상세 출력을 위한 메소드
	 * 작성자는 관리자 접속 시 관리자명이 출력되고 사용자 접속 시 "관리자"로 출력
	 */
	@Override
	public String toString() {
		return "-< " + notificationId + "번 공지사항 >-------------------------------------\n"
				+ " ▣ 제목 : " + (fix == 1 ? "[★]" : "") + title + "\n"
				+ " ▣ 내용 : " + (contents == null ? "" : contents) + "\n"
				+ " ▣ 작성자 : " + (adminName == null ? "관리자" : adminName) + "\n"
				+ " ▣ 작성일 : " + registerday + "\n"
				+ (updateday == null ? "" : " ▣ 최신수정일 : " + updateday + "\n")
				+ "----------------------------------------------------\n";
	}
}
