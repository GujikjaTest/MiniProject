package notification.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.ProjectDBConnection;
import notification.domain.NotificationDTO;

/*
 * 공지와 관련된 DB 데이터 접근 클래스
 */
public class NotificationDAO_imple implements NotificationDAO {

	private final Connection conn = ProjectDBConnection.getConn(); // DB Connection 객체

	private PreparedStatement pstmt; // 쿼리 실행 객체

	private ResultSet rs; // 쿼리 결과를 담을 객체

	/*
	 * 자원 해제
	 */
	private void close() {
		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 공지사항 리스트 반환
	 */
	@Override
	public List<NotificationDTO> getNotificationList(boolean isAdmin) {
		List<NotificationDTO> noticeList = new ArrayList<>();

		String sql = " select notification_id, name, to_char(registerday, 'yyyy-mm-dd') as registerday, fix, to_char(updateday, 'yyyy-mm-dd') as updateday, "
				+ " case when length(title) > 12 then substr(title, 1, 12) || '..' else title end as title "
				+ " from tbl_notification N join tbl_admin A on N.fk_admin_id = A.admin_id " + " where is_delete = 0 "
				+ " order by fix desc, registerday desc ";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			// 쿼리 조회 결과 행을 순회하며 리스트에 저장
			while (rs.next()) {
				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setNotificationId(rs.getInt("notification_id"));

				if (isAdmin) {
					notificationDTO.setAdminName(rs.getString("name"));
				}

				notificationDTO.setTitle(rs.getString("title"));
				notificationDTO.setRegisterday(rs.getString("registerday"));
				notificationDTO.setUpdateday(rs.getString("updateday"));
				notificationDTO.setFix(rs.getInt("fix"));

				noticeList.add(notificationDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return noticeList;
	}

	/*
	 * 공지사항 등록
	 */
	@Override
	public int registerNotification(Map<String, String> noticeMap) {
		int result = 0; // DML 쿼리 결과

		String sql = " insert into tbl_notification(notification_id, fk_admin_id, title, contents, fix)"
				+ " values(seq_notification_id.nextval, ?, ?, ?, ? ) ";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, noticeMap.get("adminId"));
			pstmt.setString(2, noticeMap.get("title"));
			pstmt.setString(3, noticeMap.get("contents"));
			pstmt.setString(4, noticeMap.get("fix"));

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return result;
	}

	/*
	 * 공지사항 상세 조회
	 */
	@Override
	public NotificationDTO getNotificationDetails(boolean isAdmin, int notificationId) {
		NotificationDTO notificationDTO = new NotificationDTO();

		String sql = " select notification_id, name, title, contents, to_char(registerday, 'yyyy-mm-dd') as registerday, fix, to_char(updateday, 'yyyy-mm-dd') as updateday "
				+ " from tbl_notification N join tbl_admin A on N.fk_admin_id = A.admin_id "
				+ " where is_delete = 0 and notification_id = ? ";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, notificationId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				notificationDTO.setNotificationId(rs.getInt("notification_id"));

				// 관리자인 경우에만 관리자명 저장
				if (isAdmin) {
					notificationDTO.setAdminName(rs.getString("name"));
				}

				notificationDTO.setTitle(rs.getString("title"));
				notificationDTO.setContents(rs.getString("contents"));
				notificationDTO.setRegisterday(rs.getString("registerday"));
				notificationDTO.setUpdateday(rs.getString("updateday"));
				notificationDTO.setFix(rs.getInt("fix"));
			} else {
				notificationDTO = null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return notificationDTO;
	}

	/*
	 * 공지사항 수정
	 */
	@Override
	public int updateNotification(Map<String, String> noticeMap) {
		int result = 0; // DML 쿼리 결과

		String sql = " update tbl_notification set title = ?, contents = ?, fix = ?, updateday = sysdate "
				+ " where notification_id = ? and is_delete = 0 ";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, noticeMap.get("title"));
			pstmt.setString(2, noticeMap.get("contents"));
			pstmt.setString(3, noticeMap.get("fix"));
			pstmt.setString(4, noticeMap.get("notificationId"));

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return result;
	}

	/*
	 * 공지사항 유무 확인 공지사항 ID를 통해 해당 공지사항이 존재하면 1, 존재하지 않으면 0을 반환
	 */
	@Override
	public int selectCountNotification(int notificationId) {
		int result = 0;
		String sql = " select count(*) as count from tbl_notification where notification_id = ? ";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, notificationId);

			rs = pstmt.executeQuery();

			rs.next();

			result = rs.getInt("count");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return result;
	}

	/*
	 * 공지사항 삭제
	 */
	@Override
	public int deleteNotification(int notificationId) {
		int result = 0;
		String sql = " update tbl_notification set is_delete = 1 where notification_id = ? ";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, notificationId);

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return result;
	}
}
