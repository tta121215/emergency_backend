package com.emergency.rollcall.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emergency.rollcall.entity.AssemblyCheckIn;

@Repository
public interface AssemblyCheckInDao extends JpaRepository<AssemblyCheckIn, Long> {

	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id")
	List<AssemblyCheckIn> getAllListByActivationId(@Param("id") Long id);

	@Query("SELECT a.assemblyPoint, COUNT(a) FROM AssemblyCheckIn a WHERE a.emergencySyskey = :emergencyActivateSyskey GROUP BY a.assemblyPoint")
	List<Object[]> findCheckInCountsByAssemblyPoint(@Param("emergencyActivateSyskey") Long emergencyActivateSyskey);

	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :activateId and a.assemblyPoint = :assemblyId ")
	Page<AssemblyCheckIn> getListByAssemblyAndActivate(@Param("activateId") Long activateId,
			@Param("assemblyId") Long assemblyId, Pageable pageable);

	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id")
	Page<AssemblyCheckIn> getListByActivationId(@Param("id") Long id, Pageable pageable);

	@Query("SELECT a.emergencySyskey, COUNT(a) FROM AssemblyCheckIn a WHERE a.emergencySyskey = :emergencyActivateSyskey GROUP BY a.emergencySyskey")
	List<Object[]> findCheckInCountsByEmergencyActivate(@Param("emergencyActivateSyskey") Long emergencyActivateSyskey);

	@Query(value = "SELECT * FROM PYM_User", nativeQuery = true)
	List<Map<String, Object>> findAllUsers();

	@Query(value = "SELECT u.* FROM PYM_User u JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id WHERE ac.emergency_syskey = :emergencyActivateId", nativeQuery = true)
	List<Map<String, Object>> findCheckedInUsersByEmergencyActivate(
			@Param("emergencyActivateId") Long emergencyActivateId);

	@Query(value = "SELECT u.*,v.visitororvip AS visitor,d.name AS deptName FROM PYM_User u " + "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "AND ac.emergency_syskey = :emergencyActivateId "
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "WHERE ac.staff_id IS NULL", 
			countQuery = "SELECT COUNT(*) FROM PYM_User u "
					+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
					+ "AND ac.emergency_syskey = :emergencyActivateId "
					+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
					+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
					+ "WHERE ac.staff_id IS NULL", nativeQuery = true)
	Page<Map<String, Object>> findUsersNotCheckedInByEmergencyActivate(
			@Param("emergencyActivateId") Long emergencyActivateId, Pageable pageable);

	@Query(value = "SELECT u.*,v.visitororvip AS visitor,d.name AS deptName FROM PYM_User u " + "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "AND ac.emergency_syskey = :emergencyActivateId " 
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "WHERE ac.staff_id IS NULL "
			+ " AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%')"
			+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%')"
			+ "OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') OR LOWER(d.name) LIKE LOWER('%' || :params || '%') ) ",
			countQuery = "SELECT COUNT(*) FROM PYM_User u "
					+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
					+ "AND ac.emergency_syskey = :emergencyActivateId " 
					+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
					+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
					+ "WHERE ac.staff_id IS NULL "
					+ " AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%')"
					+ "OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') OR LOWER(d.name) LIKE LOWER('%' || :params || '%') ) ", nativeQuery = true)
	Page<Map<String, Object>> findUsersNotCheckedInByEmergencyActivate(
			@Param("emergencyActivateId") Long emergencyActivateId, Pageable pageable, @Param("params") String params);

	@Query(value = "SELECT * FROM PYM_User where id = :id", nativeQuery = true)
	Map<String, Object> findByUserId(@Param("id") Long id);

	@Query("SELECT ac.assemblyPoint, MAX(ac.currenttime) FROM AssemblyCheckIn ac WHERE ac.emergencySyskey = :emergencyActivateId GROUP BY ac.assemblyPoint")
	List<Object[]> findMaxCheckInTimesByAssemblyPoint(@Param("emergencyActivateId") Long emergencyActivateId);

	@Query(value = "SELECT u.*,ac.currenttime AS currenttime,ac.currentdate AS currentdate,a.name AS assembly,v.visitororvip AS visitor,d.name AS deptName "
			+ "FROM PYM_User u "
			+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "AND ac.emergency_syskey = :emergencyActivateId "
			+ "WHERE ac.emergency_syskey = :emergencyActivateId", countQuery = "SELECT COUNT(*) FROM PYM_User u "
					+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
					+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
					+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
					+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
					+ "AND ac.emergency_syskey = :emergencyActivateId "
					+ "WHERE ac.emergency_syskey = :emergencyActivateId", nativeQuery = true)
	Page<Map<String, Object>> findUsersCheckedInByEmergencyActivate(
			@Param("emergencyActivateId") Long emergencyActivateId, Pageable pageable);

	@Query(value = "SELECT u.*,ac.currenttime AS currenttime,ac.currentdate AS currentdate,a.name AS assembly,v.visitororvip AS visitor,d.name AS deptName "
			+ "FROM PYM_User u "
			+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "AND ac.emergency_syskey = :emergencyActivateId " + "WHERE  ac.emergency_syskey = :emergencyActivateId "
			+ " AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%')"
			+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(a.name) LIKE LOWER('%' || :params || '%') OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(d.name) LIKE LOWER('%' || :params || '%') )", 
			countQuery = "SELECT COUNT(*) FROM PYM_User u "
					+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
					+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
					+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
					+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
					+ "AND ac.emergency_syskey = :emergencyActivateId "
					+ "WHERE  ac.emergency_syskey = :emergencyActivateId "
					+ " AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(a.name) LIKE LOWER('%' || :params || '%') OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(d.name) LIKE LOWER('%' || :params || '%') )", nativeQuery = true)
	Page<Map<String, Object>> findUsersCheckedInByEmergencyActivate(
			@Param("emergencyActivateId") Long emergencyActivateId, Pageable pageable, @Param("params") String params);
	
	@Query(value = "SELECT u.*,ac.currenttime AS currenttime,ac.currentdate AS currentdate,a.name AS assembly,v.visitororvip AS visitor,d.name AS deptName "
			+ "FROM PYM_User u "
			+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "AND ac.emergency_syskey = :emergencyActivateId "
			+ "WHERE ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId", countQuery = "SELECT COUNT(*) FROM PYM_User u "
					+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
					+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
					+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
					+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
					+ "AND ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId "
					+ "WHERE ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId", nativeQuery = true)
	Page<Map<String, Object>> findUsersCheckedInByEmergencyAndAssembly(
			@Param("emergencyActivateId") Long emergencyActivateId,@Param("assemblyId") Long assemblyId, Pageable pageable);

	@Query(value = "SELECT u.*,ac.currenttime AS currenttime,ac.currentdate AS currentdate,a.name AS assembly,v.visitororvip AS visitor,d.name AS deptName  "
			+ "FROM PYM_User u "
			+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "AND ac.emergency_syskey = :emergencyActivateId " + "WHERE  ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId "
			+ "AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%')"
			+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(a.name) LIKE LOWER('%' || :params || '%') OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(d.name) LIKE LOWER('%' || :params || '%') ) "
			, countQuery = "SELECT COUNT(*) FROM PYM_User u "
					+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
					+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
					+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
					+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
					+ "AND ac.emergency_syskey = :emergencyActivateId "
					+ "WHERE  ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId "
					+ "AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(a.name) LIKE LOWER('%' || :params || '%') OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(d.name) LIKE LOWER('%' || :params || '%') )", nativeQuery = true)
	Page<Map<String, Object>> findUsersCheckedInByEmergencyAndAssembly(
			@Param("emergencyActivateId") Long emergencyActivateId,@Param("assemblyId") Long assemblyId, Pageable pageable, @Param("params") String params);

}
