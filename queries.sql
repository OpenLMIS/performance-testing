
--READY to initiate ----------
SELECT U.id,U.username,U.facilityId, RA.programId, 13
FROM users U JOIN role_assignments RA ON U.id = RA.userid
JOIN roles R ON RA.roleId = R.id
WHERE RA.supervisoryNodeId IS NULL
AND R.name = 'Store In-Charge'
AND facilityId IN (
SELECT Id  FROM facilities WHERE Id NOT IN(
SELECT DISTINCT(facilityId) FROM requisitions WHERE periodId = 13))
ORDER BY U.facilityId, RA.programId  LIMIT 160;



-- READY for submission ---------
SELECT username, facilityid, programid, 13 FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
	select U.username as username, R.facilityid, R.programid
	from requisitions R, users U
	where R.periodid=13 and R.status='INITIATED' and U.facilityid=R.facilityid
	order by U.facilityid, R.programid,U.id) AS Q ) AS foo where rownumber%2=1 ;

-- READY for authorization ---------
SELECT username, facilityid, programid, 13 FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
	select U.username as username, R.facilityid, R.programid
	from requisitions R, users U
	where R.periodid=13 and R.status='SUBMITTED' and U.facilityid=R.facilityid
	order by U.facilityid, R.programid,U.id) AS Q ) AS foo where rownumber%2=0;


-- READY for first approval -------
SELECT U.username, R.facilityId, R.programId, 13
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='LMU' and R.periodId=13 and R.status = 'AUTHORIZED' AND RA.programId=R.programId ORDER BY R.facilityId, R.programId ;

-- READY for second approval ------
SELECT U.username, R.facilityId, R.programId, 13
from requisitions R JOIN supervisory_nodes SN ON R.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='Medical-Officer' and R.periodId=13 and R.status = 'IN_APPROVAL' AND RA.programId=R.programId ORDER BY R.facilityId, R.programId ;


-- READY for order ---------------

