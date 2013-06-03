-bash-4.1$ cat query.sh
#!/bin/bash

############ Initiate
for counter in {0..8}
do
 limit=238
 offset=`expr $limit \* $counter`

 psql -d open_lmis -U postgres -t -A -F"," -c "SELECT U.username,U.facilityId, RA.programId, RA.programId+2, 13 FROM users U JOIN role_assignments RA ON U.id = RA.userid
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

############ Authorize ###############
for counter in {0..8}
do
 limit=400
 offset=`expr $limit \* $counter`

psql -d open_lmis -U postgres -t -A -F"," -c "
SELECT username, facilityid, programid, 13 FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY facilityid,programid) AS rownumber,username,facilityid,programid from (
	select U.username as username, R.facilityid, R.programid
	from requisitions R, users U
	where R.periodid=13 and R.status='SUBMITTED' and U.facilityid=R.facilityid
	order by U.facilityid, R.programid,U.id) AS Q ) AS foo where rownumber%2=0
 LIMIT $limit OFFSET $offset"  > authorization`expr $counter + 1`.csv;
done