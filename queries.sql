
--READY to initiate ----------
psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username,U.facilityId, RA.programId, 13
FROM users U JOIN role_assignments RA ON U.id = RA.userid
JOIN roles R ON RA.roleId = R.id
WHERE RA.supervisoryNodeId IS NULL
AND R.name = 'Store In-Charge'
AND facilityId IN (
SELECT Id  FROM facilities WHERE Id NOT IN(
SELECT DISTINCT(facilityId) FROM requisitions WHERE periodId = 13))
ORDER BY U.facilityId, RA.programId  LIMIT 375;
" > readytoinitiate1.csv;

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username,U.facilityId, RA.programId, 13
FROM users U JOIN role_assignments RA ON U.id = RA.userid
JOIN roles R ON RA.roleId = R.id
WHERE RA.supervisoryNodeId IS NULL
AND R.name = 'Store In-Charge'
AND facilityId IN (
SELECT Id  FROM facilities WHERE Id NOT IN(
SELECT DISTINCT(facilityId) FROM requisitions WHERE periodId = 13))
ORDER BY U.facilityId, RA.programId  LIMIT 375 OFFSET 375;
" > readytoinitiate2.csv;



-- READY for submission ---------
psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT username, facilityid, programid, 13 FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
	select U.username as username, R.facilityid, R.programid
	from requisitions R, users U
	where R.periodid=13 and R.status='INITIATED' and U.facilityid=R.facilityid
	order by U.facilityid, R.programid,U.id) AS Q ) AS foo where rownumber%2=1 LIMIT 1250;
	" > submission1.csv;

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT username, facilityid, programid, 13 FROM (
SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
select U.username as username, R.facilityid, R.programid
from requisitions R, users U
where R.periodid=13 and R.status='INITIATED' and U.facilityid=R.facilityid
order by U.facilityid, R.programid,U.id) AS Q ) AS foo where rownumber%2=1 LIMIT 1250 OFFSET 1250;
" > submission2.csv;


-- READY for authorization ---------
psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT username, facilityid, programid, 13 FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
	select U.username as username, R.facilityid, R.programid
	from requisitions R, users U
	where R.periodid=13 and R.status='SUBMITTED' and U.facilityid=R.facilityid
	order by U.facilityid, R.programid,U.id) AS Q ) AS foo where rownumber%2=0 LIMIT 125;
	" > authorization1.csv;

	psql -d open_lmis -U postgres -t -A -F"," -c "
	SELECT username, facilityid, programid, 13 FROM (
    SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
  	select U.username as username, R.facilityid, R.programid
  	from requisitions R, users U
  	where R.periodid=13 and R.status='SUBMITTED' and U.facilityid=R.facilityid
  	order by U.facilityid, R.programid,U.id) AS Q ) AS foo where rownumber%2=0 LIMIT 125 OFFSET 125;
  	" > authorization2.csv;



-- READY for first approval -------
psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT Q1.username,Q1.facilityId,Q1.programId, Q1.requisitionId,Q2.facilityId, Q2.programId, Q2.requisitionId FROM (
SELECT U.username, R.facilityId, R.programId, 13 AS periodId, R.id as requisitionId
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='LMU' and R.periodId=13 and R.status = 'AUTHORIZED' AND RA.programId=R.programId AND R.id %2 =0
ORDER BY R.facilityId, R.programId
) AS Q1 JOIN (SELECT U.username, R.facilityId, R.programId, 13 AS periodId, R.id as requisitionId
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='LMU' and R.periodId=13 and R.status = 'AUTHORIZED' AND RA.programId=R.programId AND R.id %2 =1
ORDER BY R.facilityId, R.programId) AS Q2 ON Q1.facilityId = Q2.facilityId AND Q1.programId = Q2.programId+1 ORDER BY Q1.facilityId, Q1.programId;
" > firstapproval1.csv;


-- READY for second approval ------
psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username, R.facilityId, R.programId, 13 AS periodId, R.id as requisitionId
from requisitions R JOIN supervisory_nodes SN ON R.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='Medical-Officer' and R.periodId=13 and R.status = 'IN_APPROVAL' AND RA.programId=R.programId ORDER BY R.facilityId, R.programId LIMIT 125;
" > secondapproval1.csv;

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username, R.facilityId, R.programId, 13 AS periodId, R.id as requisitionId
from requisitions R JOIN supervisory_nodes SN ON R.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='Medical-Officer' and R.periodId=13 and R.status = 'IN_APPROVAL' AND RA.programId=R.programId ORDER BY R.facilityId, R.programId LIMIT 125 OFFSET 125;
" > secondapproval2.csv;



-- View Rnr ---------------
psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username, R.facilityId, R.programId, 13
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='LMU' and R.periodId=13 and R.status = 'AUTHORIZED' AND RA.programId=R.programId ORDER BY R.facilityId, R.programId LIMIT 250 OFFSET 250;
" > viewrnr1.csv

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username, R.facilityId, R.programId, 13
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='LMU' and R.periodId=13 and R.status = 'AUTHORIZED' AND RA.programId=R.programId ORDER BY R.facilityId, R.programId LIMIT 250 OFFSET 500;
" > viewrnr2.csv


-- Convert to order ------------------
psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username, R.id
from roles RO, role_assignments RA, users U, requisitions R WHERE
RO.name='LMU In-Charge' AND
RO.id = RA.roleId AND
R.status = 'APPROVED' AND
U.id = RA.userId LIMIT 10;
" > converttoorder1.csv

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username, R.id
from roles RO, role_assignments RA, users U, requisitions R WHERE
RO.name='LMU In-Charge' AND
RO.id = RA.roleId AND
R.status = 'APPROVED' AND
U.id = RA.userId LIMIT 10 OFFSET 10;
" > converttoorder2.csv



--- others
SELECT COUNT(*) FROM pg_stat_activity;

SELECT pg_size_pretty(pg_database_size('open_lmis'));

pg_dump -Ft open_lmis > backup.tar;
pg_restore -Ft --clean -d open_lmis backup.tar

drop table users,requisitions, requisition_groups,comments CASCADE;
