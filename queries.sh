#!/bin/bash

############ Initiate    ###############
for counter in {0..8}
do
 limit=240
 offset=`expr $limit \* $counter`

 psql -d open_lmis -U postgres -t -A -F"," -c "
 SELECT U.username,U.facilityId, RA.programId, RA.programId+2, 13 FROM users U JOIN role_assignments RA ON U.id = RA.userid
 JOIN roles R ON RA.roleId = R.id
 WHERE RA.supervisoryNodeId IS NULL
 AND R.name = 'Store In-Charge'
 AND RA.programId in (1,2)
 AND facilityId IN (
 SELECT Id  FROM facilities WHERE Id NOT IN( SELECT DISTINCT(facilityId) FROM requisitions WHERE periodId = 13))
 ORDER BY U.facilityId, RA.programId  LIMIT $limit OFFSET $offset"  > readytoinitiate`expr $counter + 1`.csv;
done

###########  Submit    ###############
for counter in {0..8}
do
 limit=730
 offset=`expr $limit \* $counter`

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT username, facilityid, programId, programId+2, 13 FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
	select U.username as username, R.facilityid, R.programid
	from requisitions R, users U
	where R.periodid=13 and R.status='INITIATED' and U.facilityid=R.facilityid AND R.programId in (1,2)
	order by U.facilityid, R.programid,U.id) AS Q ) AS foo
	WHERE rownumber%2=1
 LIMIT $limit OFFSET $offset"  > submission`expr $counter + 1`.csv;
done

############ View ############### 4209 #####
for counter in {0..8}
do
 limit=450
 offset=`expr $limit \* $counter`

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT U.username, R.facilityId, R.programId, R.programId+2, 13
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='LMU' and R.periodId=13 and R.status IN ('IN_APPROVAL','APPROVED','AUTHORIZED') AND RA.programId=R.programId AND  R.programId in (1,2) ORDER BY R.facilityId, R.programId
 LIMIT $limit OFFSET $offset"  > viewrnr`expr $counter + 1`.csv;
done

############ Authorize ############### 2814 #####
for counter in {0..8}
do
 limit=160
 offset=`expr $limit \* $counter`

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT username, facilityid, programid, programId+2, 13 FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
	select U.username as username, R.facilityid, R.programid
	from requisitions R, users U
	where R.periodid=13 and R.status='SUBMITTED' and U.facilityid=R.facilityid
	AND R.programId in (1,2)
	order by U.facilityid, R.programid,U.id) AS Q ) AS foo where rownumber%2=0
 LIMIT $limit OFFSET $offset"  > authorization`expr $counter + 1`.csv;
done

############ FirstApproval ####################
for counter in {0..8}
do
 limit=81
 offset=`expr $limit \* $counter`

  psql -d open_lmis -U postgres -t -A -F"," -c "
  SELECT Q1.username, Q1.programId, Q1.requisitionId, Q2.programId, Q2.requisitionId FROM (
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
  ORDER BY R.facilityId, R.programId) AS Q2 ON Q1.facilityId = Q2.facilityId AND Q1.programId = Q2.programId+1 ORDER BY Q1.facilityId, Q1.programId
  LIMIT $limit OFFSET $offset"  > firstapproval`expr $counter + 1`.csv;
done


############ SecondApproval ####################
for counter in {0..8}
do
  limit=75
  offset=`expr $limit \* $counter`

  psql -d open_lmis -U postgres -t -A -F"," -c "
  SELECT Q1.username, Q1.programId, Q1.requisitionId, Q2.programId, Q2.requisitionId FROM (
  SELECT U.username, R.facilityId, R.programId, 13 AS periodId, R.id as requisitionId
  from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
  JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
  JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
  JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
  JOIN roles RO on RO.id = RA.roleId
  JOIN users U on U.id = RA.userid
  AND RO.name='Medical-Officer' and R.periodId=13 and R.status = 'IN_APPROVAL' AND RA.programId=R.programId AND R.id %2 =0
  ORDER BY R.facilityId, R.programId
  ) AS Q1 JOIN (SELECT U.username, R.facilityId, R.programId, 13 AS periodId, R.id as requisitionId
  from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
  JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
  JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
  JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
  JOIN roles RO on RO.id = RA.roleId
  JOIN users U on U.id = RA.userid
  AND RO.name='Medical-Officer' and R.periodId=13 and R.status = 'IN_APPROVAL' AND RA.programId=R.programId AND R.id %2 =1
  ORDER BY R.facilityId, R.programId) AS Q2 ON Q1.facilityId = Q2.facilityId AND Q1.programId = Q2.programId+1 ORDER BY Q1.facilityId, Q1.programId
  LIMIT $limit OFFSET $offset"  > secondapproval`expr $counter + 1`.csv;
done