-- run as sys
DROP USER mqaudit_rw CASCADE;
DROP USER mqaudit_ro CASCADE;

-- rw
CREATE USER mqaudit_rw IDENTIFIED BY mqaudit_rw
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA UNLIMITED ON users;

GRANT CONNECT, CREATE TABLE, CREATE SEQUENCE TO mqaudit_rw;


-- ro
CREATE USER mqaudit_ro IDENTIFIED BY mqaudit_ro;

GRANT CREATE SESSION TO mqaudit_ro;
GRANT SELECT ON mqaudit_rw.mqevent TO mqaudit_ro;
