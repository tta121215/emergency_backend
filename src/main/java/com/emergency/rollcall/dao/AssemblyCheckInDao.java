package com.emergency.rollcall.dao;

import java.sql.Blob;
import java.util.Calendar;
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
	
	@Query(value="SELECT plal1.name  FROM PYM_VENDOR_PASS_LA pvpl1 "
			+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
			+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID  where pvp1.STAFFNO=:staffid",nativeQuery=true)
	String getlocationVisited(@Param("staffid") String staffid);

	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id")
	List<AssemblyCheckIn> getAllListByActivationId(@Param("id") Long id);
	
	@Query("SELECT COUNT(a) FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id")
	Long countByEmergencyId(@Param("id") Long id);
	
	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id AND a.checkInStatus = 1")
	List<AssemblyCheckIn> getCheckInList(@Param("id") Long id);
	
	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id AND a.checkInStatus = 1")
	Page<AssemblyCheckIn> getCheckInPage(@Param("id") Long id, Pageable pageable);
	
	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id AND a.checkInStatus = 1 AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(a.icNumber) LIKE LOWER(CONCAT('%', :params, '%'))"
			+ " OR LOWER(a.type) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(a.staffOrVisitor) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(a.department) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(a.contactNumber) LIKE LOWER(CONCAT('%', :params, '%')))")
	Page<AssemblyCheckIn> getCheckInPage(@Param("id") Long id, Pageable pageable,@Param("params") String params);
	
	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id AND a.checkInStatus = 0")
	Page<AssemblyCheckIn> getUnCheckInList(@Param("id") Long id, Pageable pageable);
	
	@Query("SELECT a FROM AssemblyCheckIn a WHERE a.emergencySyskey = :id AND a.checkInStatus = 0 AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(a.icNumber) LIKE LOWER(CONCAT('%', :params, '%'))"
			+ " OR LOWER(a.type) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(a.staffOrVisitor) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(a.department) LIKE LOWER(CONCAT('%', :params, '%')) OR LOWER(a.contactNumber) LIKE LOWER(CONCAT('%', :params, '%')))")
	Page<AssemblyCheckIn> getUnCheckInList(@Param("id") Long id, Pageable pageable,@Param("params") String params);
	
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
	
	@Query(value = "SELECT ac.* "
			+ "FROM Erc_assembly_check_in ac "			
			+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
			+ "WHERE ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId AND ac.check_in_status = 1 ", countQuery = "SELECT COUNT(*) FROM Erc_assembly_check_in ac "
					+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
					+ "WHERE ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId AND ac.check_in_status = 1 ", nativeQuery = true)
	Page<Map<String, Object>> findCheckedInByEmergencyAndAssembly(
			@Param("emergencyActivateId") Long emergencyActivateId,@Param("assemblyId") Long assemblyId, Pageable pageable);
	
	@Query(value = "SELECT ac.* "
			+ "FROM erc_assembly_check_in ac "			
			+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
			+ "WHERE  ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId AND ac.check_in_status = 1 "
			+ "AND ( LOWER(ac.name) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(ac.ic_number) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(ac.staff_or_visitor) LIKE LOWER('%' || :params || '%') OR LOWER(ac.contact_number) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(ac.department) LIKE LOWER('%' || :params || '%') OR LOWER(ac.type) LIKE LOWER('%' || :params || '%') "
			+ " ) "
			, countQuery = "SELECT COUNT(*) FROM erc_assembly_check_in ac "					
					+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "					
					+ "WHERE ac.emergency_syskey = :emergencyActivateId AND ac.assembly_point = :assemblyId AND ac.check_in_status = 1 "
					+ "AND ( LOWER(ac.name) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(ac.ic_number) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(ac.staff_or_visitor) LIKE LOWER('%' || :params || '%') OR LOWER(ac.contact_number) LIKE LOWER('%' || :params || '%') "
					+ "OR LOWER(ac.department) LIKE LOWER('%' || :params || '%') OR LOWER(ac.type) LIKE LOWER('%' || :params || '%') "
					+ " )", nativeQuery = true)
	Page<Map<String, Object>> findCheckedInByEmergencyAndAssembly(
			@Param("emergencyActivateId") Long emergencyActivateId,@Param("assemblyId") Long assemblyId, Pageable pageable, @Param("params") String params);
	

//	@Query(value = "SELECT u.*,v.visitororvip AS visitor,d.name AS deptName FROM PYM_User u " + "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
//			+ "AND ac.emergency_syskey = :emergencyActivateId "
//			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
//			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
//			+ "WHERE ac.staff_id IS NULL", 
//			countQuery = "SELECT COUNT(*) FROM PYM_User u "
//					+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
//					+ "AND ac.emergency_syskey = :emergencyActivateId "
//					+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
//					+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
//					+ "WHERE ac.staff_id IS NULL", nativeQuery = true)
//	Page<Map<String, Object>> findUsersNotCheckedInByEmergencyActivate(
//			@Param("emergencyActivateId") Long emergencyActivateId, Pageable pageable);
	
	@Query(value = "SELECT distinct pdl.NAME AS deptName ,pvp.FULLNAME AS username, pvp.VISITORORVIP AS visitor,pvp.CONTACTNO AS mobileNo ,pvp.COMPANYNAME,pvp.STAFFNO AS staffid,pvp.ICNO AS icnumber, pvp.ID,pvp.email AS Email "
			+ "FROM PYM_VENDOR_PASS pvp "
			+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
			+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
			+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
			+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
			+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
			+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
			+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
			+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
			+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
			+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%') "
			+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
			+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
			+ "WHERE ("
			+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
			+ "    OR "
			+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
			+ " OR "
			+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
			+ " ) "
			+ "AND ps.NAME <> 'Rejected' AND pvp.id not in (select staff_id from erc_assembly_check_in where emergency_syskey=:emergencyActivateId ) AND (pwa.current_location IN (:params) OR pwa.DOOR_NAME IN (:doors)) "
			+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location,pvp.ICNO,pvp.ID,pvp.email  "
			
			,countQuery = "SELECT COUNT(*) FROM PYM_VENDOR_PASS pvp "
					+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
					+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
					+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
					+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
					+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
					+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
					+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
					+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
					+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
					+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
					+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
					+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%' or plal1.NAME LIKE '%LOK KAWI%') "
					+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
					+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
					+ "WHERE ("
					+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
					+ "    OR "
					+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
					+ " OR "
					+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
					+ " ) "
					+ "AND ps.NAME <> 'Rejected' AND pvp.id not in (select staff_id from erc_assembly_check_in where emergency_syskey=:emergencyActivateId ) AND (pwa.current_location IN (:params) OR pwa.DOOR_NAME IN (:doors)) "
					+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location,pvp.ICNO,pvp.ID,pvp.email  "
					
					,nativeQuery = true)
Page<Map<String, Object>> findUsersNotCheckedInByEmergencyActivate(
	@Param("emergencyActivateId") Long emergencyActivateId,@Param("params") List<String> params, Pageable pageable,@Param("doors") List<String> doors,@Param("calendar")String calendar);
	
	@Query(value = "SELECT distinct pdl.NAME AS deptName ,pvp.FULLNAME AS username, pvp.VISITORORVIP AS visitor,pvp.CONTACTNO AS mobileNo ,pvp.COMPANYNAME,pvp.STAFFNO AS staffid,pvp.ICNO AS icnumber, pvp.ID,pvp.email AS Email "
			+ "FROM PYM_VENDOR_PASS pvp "
			+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
			+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
			+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
			+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
			+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
			+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
			+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
			+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
			+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
			+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%') "
			+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
			+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
			+ "WHERE ("
			+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
			+ "    OR "
			+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
			+ " OR "
			+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
			+ " ) "
			+ "AND ps.NAME <> 'Rejected' AND pvp.staffno not in (select staff_id from erc_assembly_check_in where emergency_syskey=:emergencyActivateId ) AND (pwa.current_location IN (:mainBuilding) OR pwa.DOOR_NAME IN (:doors))"
			+ "AND ( LOWER(pvp.FULLNAME) LIKE LOWER('%' || :search || '%') OR LOWER(pvp.VISITORORVIP) LIKE LOWER('%' || :search || '%')"
			+ "OR LOWER(pvp.ICNO) LIKE LOWER('%' || :search || '%') OR LOWER(pdl.NAME) LIKE LOWER('%' || :search || '%') "
			+ "OR LOWER(pvp.STAFFNO) LIKE LOWER('%' || :search || '%') OR LOWER(pvp.CONTACTNO) LIKE LOWER('%' || :search || '%') )"			
			+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location,pvp.ICNO,pvp.ID,pvp.email  "
			
			,countQuery = "SELECT COUNT(*) FROM PYM_VENDOR_PASS pvp "
					+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
					+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
					+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
					+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
					+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
					+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
					+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
					+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
					+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
					+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
					+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
					+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%' or plal1.NAME LIKE '%LOK KAWI%') "
					+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
					+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
					+ "WHERE ("
					+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
					+ "    OR "
					+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
					+ " OR "
					+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
					+ " ) "
					+ "AND ps.NAME <> 'Rejected' AND pvp.staffno not in (select staff_id from erc_assembly_check_in where emergency_syskey=:emergencyActivateId ) AND (pwa.current_location IN (:mainBuilding) OR pwa.DOOR_NAME IN (:doors))"
					+ "AND ( LOWER(pvp.FULLNAME) LIKE LOWER('%' || :search || '%') OR LOWER(pvp.VISITORORVIP) LIKE LOWER('%' || :search || '%')"
					+ "OR LOWER(pvp.ICNO) LIKE LOWER('%' || :search || '%') OR LOWER(pdl.NAME) LIKE LOWER('%' || :search || '%') "
					+ "OR LOWER(pvp.STAFFNO) LIKE LOWER('%' || :search || '%') OR LOWER(pvp.CONTACTNO) LIKE LOWER('%' || :search || '%') )"			
					+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location,pvp.ICNO,pvp.ID,pvp.email   "
					
					,nativeQuery = true)
	Page<Map<String, Object>> findNotCheckIn(
			@Param("emergencyActivateId") Long emergencyActivateId, Pageable pageable,@Param("mainBuilding") List<String> mainBuilding,@Param("search") String search,@Param("doors") List<String> doors,@Param("calendar")String calendar);

//	@Query(value = "SELECT u.*,v.visitororvip AS visitor,d.name AS deptName FROM PYM_User u " + "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
//			+ "AND ac.emergency_syskey = :emergencyActivateId " 
//			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
//			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
//			+ "WHERE ac.staff_id IS NULL "
//			+ " AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
//			+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%')"
//			+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%')"
//			+ "OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') OR LOWER(d.name) LIKE LOWER('%' || :params || '%') ) ",
//			countQuery = "SELECT COUNT(*) FROM PYM_User u "
//					+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
//					+ "AND ac.emergency_syskey = :emergencyActivateId " 
//					+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
//					+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
//					+ "WHERE ac.staff_id IS NULL "
//					+ " AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
//					+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%') "
//					+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%')"
//					+ "OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') OR LOWER(d.name) LIKE LOWER('%' || :params || '%') ) ", nativeQuery = true)
//	Page<Map<String, Object>> findUsersNotCheckedInByEmergencyActivate(
//			@Param("emergencyActivateId") Long emergencyActivateId, Pageable pageable, @Param("params") String params,@Param("mainBuilding") List<String> mainBuilding);

	
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
	
	@Query(value = "SELECT u.*,v.visitororvip AS visitor,d.name AS deptName FROM PYM_User u " + "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "AND ac.emergency_syskey = :emergencyActivateId "
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "WHERE ac.staff_id IS NULL", nativeQuery = true)
	List<Map<String, Object>> findUsersNotCheckedInByExcel(
			@Param("emergencyActivateId") Long emergencyActivateId);
	
	@Query(value = "SELECT u.*,v.visitororvip AS visitor,d.name AS deptName FROM PYM_User u " + "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "AND ac.emergency_syskey = :emergencyActivateId " 
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "WHERE ac.staff_id IS NULL "
			+ " AND ( LOWER(u.name) LIKE LOWER('%' || :params || '%') "
			+ "OR LOWER(u.icnumber) LIKE LOWER('%' || :params || '%') OR LOWER(u.passportnumber) LIKE LOWER('%' || :params || '%')"
			+ "OR LOWER(u.staffid) LIKE LOWER('%' || :params || '%') OR LOWER(u.mobileno) LIKE LOWER('%' || :params || '%')"
			+ "OR LOWER(v.visitororvip) LIKE LOWER('%' || :params || '%') OR LOWER(d.name) LIKE LOWER('%' || :params || '%') ) ", nativeQuery = true)
	List<Map<String, Object>> findUsersNotCheckedInByExcel(
			@Param("emergencyActivateId") Long emergencyActivateId, @Param("params") String params);
	
	@Query(value = "SELECT u.*,ac.currenttime AS currenttime,ac.currentdate AS currentdate,a.name AS assembly,v.visitororvip AS visitor,d.name AS deptName "
			+ "FROM PYM_User u "
			+ "LEFT JOIN Erc_assembly_check_in ac ON u.id = ac.staff_id "
			+ "LEFT JOIN Erc_Assembly a ON ac.assembly_point = a.syskey "
			+ "LEFT JOIN PYM_vendor_pass v ON v.icno = u.icnumber AND v.is_active=1 "
			+ "LEFT JOIN PYM_department_lookup d ON d.id = v.department "
			+ "AND ac.emergency_syskey = :emergencyActivateId "
			+ "WHERE ac.emergency_syskey = :emergencyActivateId", nativeQuery = true)
	List<Map<String, Object>> findUsersCheckedInByExcel(
			@Param("emergencyActivateId") Long emergencyActivateId);
	
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
			nativeQuery = true)
	List<Map<String, Object>> findUsersCheckedInByExcel(
			@Param("emergencyActivateId") Long emergencyActivateId, @Param("params") String params);
	
	@Query(value = "SELECT photo from PYM_VENDOR_PASS WHERE STAFFNO=:staffNo AND PHOTO IS NOT NULL",nativeQuery = true)
	Blob findStaffPhoto(@Param("staffNo") String staffNo);
	
	@Query(value = "SELECT distinct pwa.CHECKIN , pwa.CHECKOUT , pdl.NAME , PVP.DEPARTMENT , pvpc.COLLECT_DATE , pvpc.RETURNCARDDATE,pvp.NAMEOFPERSONVISITED ,pvp.FULLNAME, pvp.VISITORORVIP,pvp.CONTACTNO  ,pvp.COMPANYNAME,pvp.STAFFNO,pvp.REASON ,pvp.ID,pwa.DOOR_NAME "
			+ ",LISTAGG(PLAL.NAME, ', ') WITHIN GROUP (ORDER BY PLAL.NAME) as locName,pwa.current_location FROM PYM_VENDOR_PASS pvp "
			+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
			+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
			+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
			+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
			+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
			+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
			+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
			+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
			+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
			+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%') "
			+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
			+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
			+ "WHERE ("
			+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
			+ "    OR "
			+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
			+ " OR "
			+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
			+ " ) "
			+ "AND ps.NAME <> 'Rejected' AND (pwa.current_location IN (:params) OR pwa.DOOR_NAME IN (:doors)) "
			+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location "
//			+ "ORDER BY CASE WHEN :sortColumn = 'FULLNAME' THEN pvp.FULLNAME" 
//	        + "WHEN :sortColumn = 'DEPARTMENT' THEN pdl.NAME "
//	        + "WHEN :sortColumn = 'STAFFNO' THEN pvp.STAFFNO "
//	        + "WHEN :sortColumn = 'VISITORORVIP' THEN pvp.VISITORORVIP END ,"
//	        + "DECODE(:sortDirection, 'ASC', 1, 'DESC', -1) "
			,countQuery = "SELECT COUNT(*) FROM PYM_VENDOR_PASS pvp "
					+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
					+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
					+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
					+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
					+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
					+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
					+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
					+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
					+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
					+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
					+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
					+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%' or plal1.NAME LIKE '%LOK KAWI%') "
					+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
					+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
					+ "WHERE ("
					+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
					+ "    OR "
					+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
					+ " OR "
					+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
					+ " ) "
					+ "AND ps.NAME <> 'Rejected' AND (pwa.current_location IN (:params) OR pwa.DOOR_NAME IN (:doors))"
					+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location "
//					+ "ORDER BY CASE WHEN :sortColumn = 'FULLNAME' THEN pvp.FULLNAME" 
//			        + "WHEN :sortColumn = 'DEPARTMENT' THEN pdl.NAME "
//			        + "WHEN :sortColumn = 'STAFFNO' THEN pvp.STAFFNO "
//			        + "WHEN :sortColumn = 'VISITORORVIP' THEN pvp.VISITORORVIP END ,"
//			        + "DECODE(:sortDirection, 'ASC', 1, 'DESC', -1) "
					,nativeQuery = true)
	Page<Map<String, Object>> findHeadCountReport(Pageable pageable,@Param("params") List<String> params,@Param("doors") List<String> doors,@Param("calendar")String calendar);
	
	@Query(value = "SELECT icnumber FROM PYM_User where staffid =:staffid", nativeQuery = true)
	String findICByUser(@Param("staffid") String staffid );
	
	@Query(value = "SELECT distinct pwa.CHECKIN , pwa.CHECKOUT , pdl.NAME , PVP.DEPARTMENT , pvpc.COLLECT_DATE , pvpc.RETURNCARDDATE,pvp.NAMEOFPERSONVISITED ,pvp.FULLNAME, pvp.VISITORORVIP,pvp.CONTACTNO  ,pvp.COMPANYNAME,pvp.STAFFNO,pvp.REASON ,pvp.ID,pwa.DOOR_NAME "
			+ ",LISTAGG(PLAL.NAME, ', ') WITHIN GROUP (ORDER BY PLAL.NAME) as locName,pwa.current_location FROM PYM_VENDOR_PASS pvp "
			+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
			+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
			+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
			+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
			+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
			+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
			+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
			+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
			+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
			+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%') "
			+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
			+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
			+ "WHERE ("
			+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
			+ "    OR "
			+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
			+ " OR "
			+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
			+ " ) "
			+ "AND ps.NAME <> 'Rejected' AND (pwa.current_location IN (:params) OR pwa.DOOR_NAME IN (:doors)) "
			+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location "
			
			,nativeQuery = true)
	List<Map<String, Object>> findHeadCount(@Param("params") List<String> params,@Param("doors") List<String> doors,@Param("calendar")String calendar);
	
	@Query(value = "SELECT distinct pwa.CHECKIN , pwa.CHECKOUT , pdl.NAME , PVP.DEPARTMENT , pvpc.COLLECT_DATE , pvpc.RETURNCARDDATE,pvp.NAMEOFPERSONVISITED ,pvp.FULLNAME, pvp.VISITORORVIP,pvp.CONTACTNO  ,pvp.COMPANYNAME,pvp.STAFFNO,pvp.REASON ,pvp.ID,pwa.DOOR_NAME "
			+ ",LISTAGG(PLAL.NAME, ', ') WITHIN GROUP (ORDER BY PLAL.NAME) as locName,pwa.current_location,pvp.ICNO FROM PYM_VENDOR_PASS pvp "
			+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
			+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
			+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
			+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
			+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
			+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
			+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
			+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
			+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
			+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
			+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%') "
			+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
			+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
			+ "WHERE ("
			+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
			+ "    OR "
			+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
			+ " OR "
			+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
			+ " ) "
			+ "AND ps.NAME <> 'Rejected' AND (pwa.current_location IN (:params) OR pwa.DOOR_NAME IN (:doors)) "
			+ "AND ( LOWER(pvp.FULLNAME) LIKE LOWER('%' || :search || '%') OR LOWER(pvp.VISITORORVIP) LIKE LOWER('%' || :search || '%')"
			+ "OR LOWER(pvp.ICNO) LIKE LOWER('%' || :search || '%') OR LOWER(pdl.NAME) LIKE LOWER('%' || :search || '%') "
			+ "OR LOWER(pvp.STAFFNO) LIKE LOWER('%' || :search || '%') OR LOWER(pvp.CONTACTNO) LIKE LOWER('%' || :search || '%') )"			
			+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location,pvp.ICNO "

			,countQuery = "SELECT COUNT(*) FROM PYM_VENDOR_PASS pvp "
					+ "LEFT JOIN (select pwa.* from PYM_WORKER_ATT pwa join (select vp_id, max(CHECKIN) as checkin from PYM_WORKER_ATT "
					+ "group by vp_id) pwa1 on pwa.VP_ID = pwa1.VP_ID and pwa.CHECKIN = pwa1.checkin ) pwa ON pvp.id = pwa.VP_ID "
					+ "LEFT JOIN PYM_VENDOR_PASS_CARD pvpc ON pvp.id = pvpc.VP_ID "
					+ "LEFT JOIN PYM_STATUS ps ON pvp.STATUSID = ps.STATUSID "
					+ "LEFT JOIN PYM_DEPARTMENT_LOOKUP pdl ON pvp.DEPARTMENT = pdl.ID "
					+ "LEFT JOIN PYM_VENDOR_PASS_LA pvpl ON pvp.id=PVPL.VP_ID AND pvp.VISITORORVIP LIKE '%VISITOR%' "
					+ "LEFT JOIN (SELECT DISTINCT pvp1.id  FROM PYM_VENDOR_PASS_LA pvpl1 "
					+ "JOIN PYM_LOCATION_ACCESS_LOOKUP plal1 ON PVPL1.LOCATION_ACCESS_ID =PLAL1.ID "
					+ "JOIN PYM_VENDOR_PASS pvp1 ON pvp1.id=PVPL1.VP_ID AND pvp1.VISITORORVIP LIKE '%STAFF%'  "
					+ "LEFT JOIN PYM_USER pu ON pu.USERNAME = pvp1.STAFFNO  "
					+ "LEFT JOIN PYM_USER_ROLE pur ON pur.USER_ID  = pu.ID  "
					+ "where (plal1.NAME LIKE '%KKIP%' or plal1.NAMe LIKE '%MERCU%' or plal1.NAME LIKE '%WMC%' or plal1.NAME LIKE '%EPIC%' or plal1.NAME LIKE '%LOK KAWI%') "
					+ "OR pur.ROLE_ID = 10) mobstaff ON mobstaff.id = pvp.ID AND pvp.VISITORORVIP LIKE '%STAFF%' "
					+ "LEFT JOIN PYM_LOCATION_ACCESS_LOOKUP plal ON PVPL.LOCATION_ACCESS_ID =PLAL.ID "
					+ "WHERE ("
					+ "    (pvp.VISITORORVIP LIKE '%VISITOR%' AND PVPC.RETURNCARDDATE IS NULL AND PVPC.COLLECT_DATE IS NOT NULL AND  PVPC.COLLECT_DATE IS NOT null) "
					+ "    OR "
					+ "    (pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) and pwa.INPREMESIS = 1 AND pwa.CHECKIN <TO_TIMESTAMP(:calendar, 'YYYYMMDD HH24:MI')) "
					+ " OR "
					+ " (pwa.SOURCE = 'Mobile' AND pwa.current_location IS NOT NULL  AND pvp.VISITORORVIP LIKE '%STAFF%' AND pwa.CHECKIN IS NOT NULL AND (pwa.CHECKOUT IS NULL OR pwa.CHECKOUT = pwa.CHECKIN) AND  pwa.CHECKIN > trunc(SYSDATE)) "
					+ " ) "
					+ "AND ps.NAME <> 'Rejected' AND (pwa.current_location IN (:params) OR pwa.DOOR_NAME IN (:doors)) "
					+ "AND ( LOWER(pvp.FULLNAME) LIKE LOWER('%' || :search || '%') OR LOWER(pvp.VISITORORVIP) LIKE LOWER('%' || :search || '%')"
					+ "OR LOWER(pvp.ICNO) LIKE LOWER('%' || :search || '%') OR LOWER(pdl.NAME) LIKE LOWER('%' || :search || '%') "
					+ "OR LOWER(pvp.STAFFNO) LIKE LOWER('%' || :search || '%') OR LOWER(pvp.CONTACTNO) LIKE LOWER('%' || :search || '%')) "			
					+ "GROUP BY pwa.CHECKIN, pwa.CHECKOUT, pdl.NAME, PVP.DEPARTMENT, pvpc.COLLECT_DATE, pvpc.RETURNCARDDATE, pvp.NAMEOFPERSONVISITED, pvp.FULLNAME, pvp.VISITORORVIP, pvp.CONTACTNO, pvp.COMPANYNAME, pvp.STAFFNO, pvp.REASON, pvp.ID, pwa.DOOR_NAME,pwa.current_location,pvp.ICNO "

					,nativeQuery = true)
	Page<Map<String, Object>> findHeadCountReport(Pageable pageable,@Param("params") List<String> params,@Param("doors") List<String> doors,@Param("calendar")String calendar, @Param("search") String search);


}
