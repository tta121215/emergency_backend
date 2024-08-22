package com.emergency.rollcall.dao;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emergency.rollcall.entity.NotiReadLog;

@Repository
public interface NotiReadLogDao extends JpaRepository<NotiReadLog, Long> {

	@Query(value = "SELECT u.name AS username,u.staffid AS staffid,n.read_noti_date AS notidate, n.read_noti_time AS notitime,ec.name AS ename "
			+ "FROM ERC_NOTI_READ_LOG n LEFT JOIN PYM_User u ON u.id = n.staff_id "
			+ "LEFT JOIN ERC_Emergency_ACTIVATE ec ON ec.syskey = n.emergency_id "
			+ "WHERE ec.syskey = :emergencyId AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') OR LOWER(ec.name) LIKE LOWER('%' || :params || '%')) ", countQuery = "SELECT COUNT(*) FROM ERC_NOTI_READ_LOG n LEFT JOIN PYM_User u ON u.id = n.staff_id "
					+ "LEFT JOIN ERC_Emergency_ACTIVATE ec ON ec.syskey = n.emergency_id "
					+ "WHERE ec.syskey = :emergencyId AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%')  OR LOWER(ec.name) LIKE LOWER('%' || :params || '%') ) ", nativeQuery = true)
	Page<Map<String, Object>> findByUserName(Pageable pageable, @Param("params") String params,
			@Param("emergencyId") Long emergencyId);

	@Query(value = "SELECT u.name AS username,u.staffid AS staffid,n.read_noti_date AS notidate, n.read_noti_time AS notitime,ec.name AS ename "
			+ " FROM ERC_NOTI_READ_LOG n LEFT JOIN PYM_User u ON u.id = n.staff_id "
			+ "LEFT JOIN ERC_Emergency_ACTIVATE ec ON ec.syskey = n.emergency_id "
			+ "WHERE ec.syskey = :emergencyId ", countQuery = "SELECT COUNT(*) FROM ERC_NOTI_READ_LOG n LEFT JOIN PYM_User u ON u.id = n.staff_id "
					+ "LEFT JOIN ERC_Emergency_ACTIVATE ec ON ec.syskey = n.emergency_id "
					+ "WHERE ec.syskey = :emergencyId ", nativeQuery = true)
	Page<Map<String, Object>> findByUserName(Pageable pageable, @Param("emergencyId") Long emergencyId);

}
