#!/bin/bash

############ Initiate    ###############
for counter in {0..8}
do
 limit=142
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
 limit=312
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

############ FirstApproval-1 ############### 1839 ######
for counter in {0..8}
do
 limit=200
 offset=`expr $limit \* $counter`

psql -d open_lmis -U postgres -t -A -F"," -c "
select w.user1,w.facility1,w.program1, w.requisitionId1,w.program2, w.requisitionId2, 13 from (
(SELECT U.username as user1, R.facilityId as facility1, R.programId as program1, 13 AS periodId, R.id as requisitionId1
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='LMU' and R.periodId=13 and R.status = 'AUTHORIZED' AND RA.programId=R.programId and mod(R.id,2)=0
ORDER BY R.facilityId, R.programId )as a join
(SELECT U.username as user2, R.facilityId as facility2, R.programId as program2, 13 AS periodId, R.id as requisitionId2
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='LMU' and R.periodId=13 and R.status = 'AUTHORIZED' AND RA.programId=R.programId and mod(R.id,2)=1
ORDER BY R.facilityId, R.programId )as b on (a.facility1=b.facility2 and a.user1=b.user2)
)as w;
LIMIT $limit OFFSET $offset"  > firstapproval`expr $counter + 1`.csv;
done


############ SecondApproval-1 ############### 1803 #######
for counter in {0..8}
do
 limit=200
 offset=`expr $limit \* $counter`

psql -d open_lmis -U postgres -t -A -F"," -c "
select w.user1,w.facility1,w.program1, w.requisitionId1,w.program2, w.requisitionId2, 13 from (
(SELECT U.username as user1, R.facilityId as facility1, R.programId as program1, 13 AS periodId, R.id as requisitionId1
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='Medical-Officer' and R.periodId=13 and R.status = 'IN_APPROVAL' AND RA.programId=R.programId and mod(R.id,2)=0
ORDER BY R.facilityId, R.programId )as a join
(SELECT U.username as user2, R.facilityId as facility2, R.programId as program2, 13 AS periodId, R.id as requisitionId2
from requisitions R JOIN requisition_group_members RGM ON R.facilityId = RGM.facilityId
JOIN requisition_groups RG ON RGM.requisitionGroupId = RG.id
JOIN supervisory_nodes SN ON RG.supervisoryNodeId = SN.id
JOIN role_assignments RA ON SN.id = RA.supervisoryNodeId
JOIN roles RO on RO.id = RA.roleId
JOIN users U on U.id = RA.userid
AND RO.name='Medical-Officer' and R.periodId=13 and R.status = 'IN_APPROVAL' AND RA.programId=R.programId and mod(R.id,2)=1
ORDER BY R.facilityId, R.programId )as b on (a.facility1=b.facility2 and a.user1=b.user2)
)as w;
LIMIT $limit OFFSET $offset"  > secondapproval`expr $counter + 1`.csv;
done
