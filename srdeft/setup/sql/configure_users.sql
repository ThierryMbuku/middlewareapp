-- run as sys
DROP USER srdeft_owner CASCADE;
DROP USER srdeft_user CASCADE;
DROP USER srdeft_read CASCADE;
DROP ROLE srdeft_rw_role;
DROP ROLE srdeft_ro_role;

-- schema owner
CREATE USER srdeft_owner IDENTIFIED BY srdeft_owner
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA UNLIMITED ON users;

GRANT CONNECT, CREATE TABLE, CREATE SEQUENCE, CREATE TRIGGER TO srdeft_owner;

-- app user
CREATE USER srdeft_user IDENTIFIED BY srdeft_user
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp;

GRANT CONNECT TO srdeft_user;

-- read user
CREATE USER srdeft_read IDENTIFIED BY srdeft_read
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp;

GRANT CONNECT TO srdeft_read;

-- roles
CREATE ROLE srdeft_rw_role;
CREATE ROLE srdeft_ro_role;

GRANT srdeft_rw_role TO srdeft_user;
GRANT srdeft_ro_role TO srdeft_read;

-- app user default schema
CREATE OR REPLACE TRIGGER srdeft_user.after_logon_trg
AFTER LOGON ON srdeft_user.SCHEMA
  BEGIN
    DBMS_APPLICATION_INFO.set_module(USER, 'Initialized');
    EXECUTE IMMEDIATE 'ALTER SESSION SET CURRENT_SCHEMA = srdeft_owner';
  END;
/

-- app read default schema
CREATE OR REPLACE TRIGGER srdeft_read.after_logon_trg
AFTER LOGON ON srdeft_read.SCHEMA
  BEGIN
    DBMS_APPLICATION_INFO.set_module(USER, 'Initialized');
    EXECUTE IMMEDIATE 'ALTER SESSION SET CURRENT_SCHEMA = srdeft_owner';
  END;
/