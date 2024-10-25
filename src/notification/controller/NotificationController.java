package notification.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import admin.domain.AdminDTO;
import notification.domain.NotificationDTO;
import notification.model.NotificationDAO;
import notification.model.NotificationDAO_imple;
import utils.AlignUtil;
import utils.Msg;
import utils.ValidationUtil;

public class NotificationController {

	private final NotificationDAO notificationDAO = new NotificationDAO_imple(); // NotificationDAO 구현체

	private StringBuilder sb = new StringBuilder(); // StringBuilder 객체

	/*
	 * 관리자 : 공지 관리 메뉴, 일반 사용자 : 공지 조회 메뉴 
	 */
	public void notificationMenu(AdminDTO adminDTO, boolean isAdmin, Scanner sc) {
		String menu = null; // 메뉴번호
		String menuButton= isAdmin ? "1.공지사항 등록   2.공지사항 상세보기   0.돌아가기" : "1.공지사항 상세보기   0.돌아가기";
		String menuTitle = isAdmin ? "공지사항 관리 메뉴" : "공지사항 조회 메뉴";

		do {
			sb.setLength(0); // StringBuilder 초기화
			System.out.println("\n=== 공지사항 ===");

			// 공지사항 리스트 출력
			getNotificationList(isAdmin);

			System.out.println("==================< " + menuTitle + " >=====================");
			System.out.println(menuButton);
			System.out.println("=========================================================");

			System.out.print("▷ 메뉴번호 선택 : ");
			menu = sc.nextLine();

			switch (menu) {
			case "0": { // 돌아가기
				return;
			}
			case "1": { // 관리자 : 공지사항 등록, 일반 사용자 : 공지사항 상세보기
				if(isAdmin) {
					registerNotification(adminDTO, sc);
				}
				else {
					getNotificationDetails(isAdmin, sc);
				}
				break;
			}
			case "2": { // 공지사항 상세보기
				if(isAdmin) {
					getNotificationDetails(isAdmin, sc);
					break;
				}
			}
			default:
				Msg.W("메뉴에 없는 번호입니다.");
			}
		} while (!"0".equals(menu));
	}

	/*
	 * 공지사항 리스트 출력 고정된 공지사항은 상위에 노출 관리자일 경우 관리자 이름을 출력하고 일반 사용자인 경우 "관리자"로 작성자 이름 출력
	 * isAdmin이 true면 관리자, false면 비 관리자로 간주
	 */
	public void getNotificationList(boolean isAdmin) {
		// notificationDAO_imple로부터 공지사항 리스트 반환
		List<NotificationDTO> noticeList = notificationDAO.getNotificationList(isAdmin);

		// 공지사항이 존재하지 않는 경우
		if (noticeList.size() < 1) {
			System.out.println(">> 조회된 결과가 없습니다. <<\n");
			return;
		}

		System.out.println("\n-< 공지사항 목록 >" + "-".repeat(53));
		System.out.println("순번\t제목\t\t\t작성자\t작성일\t\t최신수정일"); 
		System.out.println("-".repeat(68));

		// 공지사항이 존재하는 경우 StringBuilder에 저장
		for (NotificationDTO notificationDTO : noticeList) {

			// 고정 여부는 제목 앞에 ★ 표시
			String fixed = notificationDTO.getFix() == 1 ? "[★] " : "";

			sb.append(notificationDTO.getNotificationId() + "\t");
			sb.append(fixed + notificationDTO.getTitle() + "\t\t\t" );
			sb.append((notificationDTO.getAdminName() == null ? "관리자" : notificationDTO.getAdminName()) + "\t");
			sb.append((notificationDTO.getUpdateday() == null ? 
						(notificationDTO.getRegisterday() + "\t\t" + notificationDTO.getRegisterday() + "\n") : 
						(notificationDTO.getRegisterday() + "\t\t" + notificationDTO.getUpdateday() + "\n")));
		}

		// StringBuilder에 저장된 공지사항 리스트 출력
		System.out.println(AlignUtil.tab(sb).toString());
	}

	/*
	 * 공지사항 등록
	 */
	private void registerNotification(AdminDTO adminDTO, Scanner sc) {
		int result = 0; // DML 쿼리 결과
		String title = null; // 제목
		String contents = null; // 내용
		String isFixed = null; // 고정 여부
		Map<String, String> noticeMap = new HashMap<>();

		System.out.println("\n=== 공지사항 등록 ===");

		// 제목 입력
		while (true) {
			System.out.print("▷ 제목[최대 20글자] : ");
			title = sc.nextLine();

			// 제목 유효성 검사, 공백 불허용, 최대길이 20글자
			if (title.isBlank() || title.length() > 20) {
				Msg.W("제목은 공백 미포함 최대 20글자 이내로 입력해야 합니다.");
				continue;
			}

			break;
		}

		// 내용 입력
		while (true) {
			System.out.print("▷ 내용[최대 200글자] : ");
			contents = sc.nextLine();

			// 내용 유효성 검사, 최대길이 200글자
			if (contents.length() > 200) {
				Msg.W("[경고] 내용은 최대 200글자 이내로 입력해야 합니다.");
				continue;
			}

			break;
		}

		// 고정 여부 입력
		while (true) {
			System.out.print("▷ 고정여부[Y/N] : ");
			isFixed = sc.nextLine();

			// 고정 여부 유효성 검사 Y/N
			if (!ValidationUtil.isYNValid(isFixed)) {
				continue;
			}

			break;
		}

		// map에 공지 제목, 내용, 고정여부 저장
		noticeMap.put("title", title);
		noticeMap.put("contents", contents);
		// TODO Y/N에 숫자 변환에 대한 입력 처리 공통화 필요
		noticeMap.put("fix", "y".equalsIgnoreCase(isFixed) ? "1" : "0");
		noticeMap.put("adminId", adminDTO.getAdminId());

		// notificationDAO_imple에 공지 저장 요청
		result = notificationDAO.registerNotification(noticeMap);

		if (result == 1) {
			System.out.println("\n>> 공지사항 등록이 완료되었습니다. ദ്ദി*ˊᗜˋ*) <<\n");
		} else {
			System.out.println(">> 공지사항 등록을 실패하였습니다. <<\n");
		}
	}

	/*
	 * 공지사항 상세보기 관리자일 경우 관리자 이름을 출력하고 일반 사용자인 경우 "관리자"로 작성자 이름 출력 isAdmin이 true면
	 * 관리자, false면 비 관리자로 간주
	 */
	public void getNotificationDetails(boolean isAdmin, Scanner sc) {
		String no = null; // 공지사항 번호
		NotificationDTO notificationDTO = null; // NotificationDTO 객체 초기화

		while (true) {
			// 공지사항에 대한 공지 번호 입력
			System.out.print("▷ 공지사항 번호 입력 : ");
			no = sc.nextLine();

			// 숫자 입력값에 대한 유효성 검사
			if (!ValidationUtil.isNumberValid(no)) {
				continue;
			}

			break;
		}

		if(!isAdmin) {
			// NotificationDAO_imple 로부터 공지사항 상세내역 반환
			notificationDTO = notificationDAO.getNotificationDetails(isAdmin, Integer.parseInt(no));

			// 공지사항이 존재하지 않으면 메소드 return
			if (notificationDTO == null) {
				System.out.println(">> 조회된 결과가 없습니다. <<\n");
				return;
			}

			// 공지사항 상세내역 출력
			System.out.println("\n=== 공지사항 상세보기 ===\n");
			System.out.println(notificationDTO.toString());
		}
		// 관리자만 접근 가능한 공지사항 수정, 삭제 메뉴
		else {
			String menu = null;
			do {
				
				notificationDTO = notificationDAO.getNotificationDetails(isAdmin, Integer.parseInt(no));
				
				System.out.println("\n=== 공지사항 상세보기 ===\n");
				System.out.println(notificationDTO.toString());
				
				System.out.println("================< 공지사항 수정 메뉴 >==================");
				System.out.println("1.공지사항 수정\t2.공지사항 삭제\t0.돌아가기");
				System.out.println("====================================================");

				System.out.print("▷ 메뉴번호 선택 : ");
				menu = sc.nextLine();

				switch (menu) {
				case "0": { // 공지사항 관리 메뉴로 돌아가기
					return;
				}
				case "1": { // 공지사항 수정
					updateNotification(true, no, sc);
					break;
				}
				case "2": { // 공지사항 삭제
					deleteNotification(no, sc);
					return;
				}
				default:
					Msg.W("메뉴에 없는 번호입니다.");
				}
			} while (!"0".equals(menu));
		}
	}

	/*
	 * 공지사항 수정 isAdmin이 true면 관리자, false면 비 관리자로 간주 no : 공지사항 번호
	 */
	private void updateNotification(boolean isAdmin, String no, Scanner sc) {
		NotificationDTO notificationDTO = null; // NotificationDTO 객체 초기화

		String title = null; // 제목
		String contents = null; // 내용
		String isFixed = null; // 고정 여부
		Map<String, String> noticeMap = new HashMap<>();

		int result = 0; // DML 쿼리 결과

		System.out.println("\n=== 공지사항 수정하기 ===");
		System.out.println(">> [주의사항] 변경하지 않고 예전의 데이터값을 그대로 사용하시려면 그냥 엔터하세요!! <<\n");

		// NotificationDAO_imple 로부터 공지사항 상세내역 반환
		notificationDTO = notificationDAO.getNotificationDetails(isAdmin, Integer.parseInt(no));

		// 제목 입력
		while (true) {
			System.out.print("▷ 수정할 제목[최대 20글자] : ");
			title = sc.nextLine();

			// 엔터 입력이면 기존 데이터 사용
			if (title.isBlank()) {
				title = notificationDTO.getTitle();
				break;
			}

			// 제목 유효성 검사, 최대길이 20글자
			if (title.length() > 20) {
				Msg.W("제목은 최대 20글자 이내로 입력해야 합니다.");
				continue;
			}

			break;
		}

		// 내용 입력
		while (true) {
			System.out.print("▷ 내용[최대 200글자] : ");
			contents = sc.nextLine();

			// 엔터 입력이면 기존 데이터 사용
			if (contents.isBlank()) {
				contents = notificationDTO.getContents();
				break;
			}

			// 내용 유효성 검사, 최대길이 200글자
			if (contents.length() > 200) {
				Msg.W("내용은 최대 200글자 이내로 입력해야 합니다.");
				continue;
			}

			break;
		}

		// 고정 여부 입력
		while (true) {
			System.out.print("▷ 고정여부[Y/N] : ");
			isFixed = sc.nextLine();

			// 엔터 입력이면 기존 데이터 사용
			if (isFixed.isBlank()) {
				isFixed = notificationDTO.getFix() == 1 ? "y" : "n";
				break;
			}

			// 고정 여부 유효성 검사 Y/N
			if (!ValidationUtil.isYNValid(isFixed)) {
				continue;
			}

			break;
		}

		// map에 공지 제목, 내용, 고정여부 저장
		noticeMap.put("title", title);
		noticeMap.put("contents", contents);
		// TODO Y/N에 숫자 변환에 대한 입력 처리 공통화 필요
		noticeMap.put("fix", "y".equalsIgnoreCase(isFixed) ? "1" : "0");
		noticeMap.put("notificationId", no);

		// notificationDAO_imple에 공지 저장 요청
		result = notificationDAO.updateNotification(noticeMap);

		if (result == 1) {
			System.out.println(">> 공지사항 수정을 완료되었습니다. ദ്ദി*ˊᗜˋ*) <<\n");
		} else {
			System.out.println(">> 공지사항 수정을 실패하였습니다. <<\n");
		}
	}

	/*
	 * 공지사항 삭제 no : 공지사항 번호
	 */
	private void deleteNotification(String no, Scanner sc) {
		String yn = null; // 삭제 확정 여부
		int result = 0; // DML 쿼리 결과

		while (true) {
			System.out.println("\n=== 공지사항 삭제하기 ===\n");

			// 삭제 확정 확인 절차
			System.out.print(">> 정말로 삭제하시겠습니까? [Y/N] : ");
			yn = sc.nextLine();

			if (!ValidationUtil.isYNValid(yn)) {
				continue;
			}

			break;
		}

		// 삭제 취소 절차
		if ("n".equalsIgnoreCase(yn)) {
			System.out.println(">> 공지사항 삭제를 취소하였습니다. <<\n");
			return;
		}

		// NotificationDAO_imple 로부터 공지사항 삭제 결과 반환
		result = notificationDAO.deleteNotification(Integer.parseInt(no));

		if (result == 1) {
			System.out.println(">> 공지사항 삭제가 완료되었습니다. ദ്ദി*ˊᗜˋ*) <<\n");
		} else {
			System.out.println(">> 공지사항 삭제를 실패하였습니다. <<\n");
		}
	}
}
