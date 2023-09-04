-- run as sys
-- drop tables
drop table mqaudit_rw.mqevent;
drop table mqaudit_rw.outcome_error;

-- drop sequence
drop sequence mqaudit_rw.mqevent_seq;
drop sequence mqaudit_rw.outcome_error_seq;


-- create sequence
create sequence mqaudit_rw.mqevent_seq start with 50 increment by 1 minvalue 50 maxvalue 9999999999999999999;
create sequence mqaudit_rw.outcome_error_seq start with 50 increment by 1 minvalue 50 maxvalue 9999999999999999999;


 -- create tables
 
	 --------------------------------------------------------------------------
  -- mqevent
  -- -----------------------------------------------------------------------
  create table mqaudit_rw.mqevent
    (
      id               	number (19) not null,
      transaction      	number (19),
      sequence      	number (19),
      message_id      	varchar2(60),
      correlation     	varchar2(60),
      type      		varchar2(10),
	  effected_by     	varchar2(20),
	  effected_on      	varchar2(50),
      occurred          timestamp,
	  context       	varchar2 (20),
      idDob      		varchar2(20),
	  creator       	varchar2 (50) not null,
      created          	timestamp not null,
      constraint mqevent_pk primary key (id)
    );
	 --------------------------------------------------------------------------
  -- mqevent
  -- -----------------------------------------------------------------------
  create table mqaudit_rw.outcome_error
    (
      id               	number (19) not null,
      application      	number (19),
      id_number      	varchar2(60),
      socpen_user      	varchar2(60),
      office      		varchar2(60),
      socpen_time      	varchar2(60),
      method      		varchar2(60),
      ref		      	varchar2(60),
      type1		      	varchar2(60),
      type2		      	varchar2(60),
      type3		      	varchar2(60),
      type4		      	varchar2(60),
      status1	      	varchar2(60),
      status2	      	varchar2(60),
      status3	      	varchar2(60),
      status4	      	varchar2(60),
      reason1	      	varchar2(100),
      reason2	      	varchar2(100),
      reason3	      	varchar2(100),
      reason4	      	varchar2(100),
      message_id      	varchar2(60),
      error_code      	varchar2(60),
      error_message     varchar2(100),
      raw_message	    varchar2(1000),
      api			    varchar2(60),
      message_status    varchar2(60),
	  creator       	varchar2 (50) not null,
      created          	timestamp not null,
	  updator       	varchar2 (50),
      updated          	timestamp,
      constraint outcome_error_pk primary key (id)
    );
