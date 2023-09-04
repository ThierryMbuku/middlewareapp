-- run as sys
DROP USER smshelper_owner CASCADE;
DROP USER smshelper_user CASCADE;
DROP USER smshelper_read CASCADE;
DROP ROLE smshelper_rw_role;
DROP ROLE smshelper_ro_role;

-- schema owner
CREATE USER smshelper_owner IDENTIFIED BY smshelper_owner
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA UNLIMITED ON users;

GRANT CONNECT, CREATE TABLE, CREATE SEQUENCE, CREATE TRIGGER TO smshelper_owner;

-- app user
CREATE USER smshelper_user IDENTIFIED BY smshelper_user
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp;

GRANT CONNECT TO smshelper_user;

-- read user
CREATE USER smshelper_read IDENTIFIED BY smshelper_read
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp;

GRANT CONNECT TO smshelper_read;

-- roles
CREATE ROLE smshelper_rw_role;
CREATE ROLE smshelper_ro_role;

GRANT smshelper_rw_role TO smshelper_user;
GRANT smshelper_ro_role TO smshelper_read;

-- app user default schema
CREATE OR REPLACE TRIGGER smshelper_user.after_logon_trg
AFTER LOGON ON smshelper_user.SCHEMA
  BEGIN
    DBMS_APPLICATION_INFO.set_module(USER, 'Initialized');
    EXECUTE IMMEDIATE 'ALTER SESSION SET CURRENT_SCHEMA = smshelper_owner';
  END;
/

-- app read default schema
CREATE OR REPLACE TRIGGER smshelper_read.after_logon_trg
AFTER LOGON ON smshelper_read.SCHEMA
  BEGIN
    DBMS_APPLICATION_INFO.set_module(USER, 'Initialized');
    EXECUTE IMMEDIATE 'ALTER SESSION SET CURRENT_SCHEMA = smshelper_owner';
  END;
/