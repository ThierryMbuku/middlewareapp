-- run as sys
DROP USER srdclient_owner CASCADE;
DROP USER srdclient_user CASCADE;
DROP USER srdclient_read CASCADE;
DROP ROLE srdclient_rw_role;
DROP ROLE srdclient_ro_role;

-- schema owner
CREATE USER srdclient_owner IDENTIFIED BY srdclient_owner
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA UNLIMITED ON users;

GRANT CONNECT, CREATE TABLE, CREATE SEQUENCE, CREATE TRIGGER TO srdclient_owner;

-- app user
CREATE USER srdclient_user IDENTIFIED BY srdclient_user
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp;

GRANT CONNECT TO srdclient_user;

-- read user
CREATE USER srdclient_read IDENTIFIED BY srdclient_read
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp;

GRANT CONNECT TO srdclient_read;

-- roles
CREATE ROLE srdclient_rw_role;
CREATE ROLE srdclient_ro_role;

GRANT srdclient_rw_role TO srdclient_user;
GRANT srdclient_ro_role TO srdclient_read;

-- app user default schema
CREATE OR REPLACE TRIGGER srdclient_user.after_logon_trg
AFTER LOGON ON srdclient_user.SCHEMA
  BEGIN
    DBMS_APPLICATION_INFO.set_module(USER, 'Initialized');
    EXECUTE IMMEDIATE 'ALTER SESSION SET CURRENT_SCHEMA = srdclient_owner';
  END;
/

-- app read default schema
CREATE OR REPLACE TRIGGER srdclient_read.after_logon_trg
AFTER LOGON ON srdclient_read.SCHEMA
  BEGIN
    DBMS_APPLICATION_INFO.set_module(USER, 'Initialized');
    EXECUTE IMMEDIATE 'ALTER SESSION SET CURRENT_SCHEMA = srdclient_owner';
  END;
/