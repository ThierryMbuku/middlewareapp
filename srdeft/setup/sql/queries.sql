-------------------------------------------------------------------------
-- outcome_staging data setup -- srd
-------------------------------------------------------------------------
update outcome set filed = null, payday=13, paymonth=7, payyear=2020, outcome='approved' where id between 2 and 11;
delete from outcome_staging;
insert into outcome_staging
select o.id, o.party, b.id, o.period, o.outcome, o.payday, o.paymonth, o.payyear, 1,
		  p.idnumber, p.res_province,
		  b.bank, b.branch, b.account, b.type, b.holder, nvl(b.verified, 'ignored') verified, null, null
   from outcome o
   inner join party p on o.party = p.id
   inner join
	 (select * from
	   (select b.id, b.party, b.bank, b.branch, b.account, b.type, b.holder, b.verified,
		 row_number() over (partition by party order by created desc) rn
	   from bank b) b where rn=1) b on o.party = b.party
     where o.filed is null
     and o.paid is null
     and o.payday = 13
     and o.paymonth = 7
     and o.payyear = 2020
     and o.outcome = 'approved'
     and p.freeze = 1
     and p.banked = 1;
commit;
-------------------------------------------------------------------------
-- records to be processed -- srd
-------------------------------------------------------------------------
select o.id, p.res_province, b.bank, b.branch, b.account, b.type, b.holder, p.freeze, p.banked
from (select * from (select b.*,row_number() over (partition by party order by created desc) rn from bank b) where rn=1) b,
outcome o, party p 
where  o.period='MAY2020' and o.payday = 23 and o.outcome='approved'
and o.filed is null and o.paid is null and p.freeze = 1 and p.banked = 1
and o.party = b.party and o.party = p.id order by o.id;
-------------------------------------------------------------------------
-- hashtotal by province -- srd
-------------------------------------------------------------------------
select p.res_province, mod(sum(case when b.account > 11 then mod(to_number(b.account), 100000000000) else to_number(b.account) end) 
                                    + case p.res_province
                                      when 'western_cape' then 80333117
                                      when 'eastern_cape' then 80333028
                                      when 'northern_cape' then 80333087
                                      when 'freestate' then 80333036
                                      when 'kwazulu_natal' then 80333052
                                      when 'gauteng' then 80333044
                                      when 'north_west' then 80333109
                                      when 'mpumalanga' then 80333079
                                      when 'limpopo' then 80333095
                                      else 0 end, 1000000000000) as hash_total
from (select * from (select b.*,row_number() over (partition by party order by created desc) rn from bank b) where rn=1) b,
outcome o, party p 
where  o.period='MAY2020' and o.payday = 23 and o.outcome='approved'
and o.filed > (sysdate - 4/24) and o.paid is null and p.freeze = 1 and p.banked = 1
and o.party = b.party and o.party = p.id and b.account is not null 
group by p.res_province;
-------------------------------------------------------------------------
-- total payment amount and outcome processed by province -- srdeft
-------------------------------------------------------------------------
select u.region, to_char(sum(to_number(c.payment_amount)/100),'9G999G999G999') as amount, to_char(sum(to_number(c.payment_amount)/35000),'9G999G999G999') as outcomes
from cash_book c, user_detail u, cov_job cj
where c.department_code = u.department_code
and c.cov_job = cj.id
and cj.status = 'completed'
and cj.created > trunc(sysdate) group by u.region
union all
select 'total', to_char(sum(to_number(c.payment_amount)/100),'9G999G999G999'), to_char(sum(to_number(c.payment_amount)/35000),'9G999G999G999')
from cash_book c, user_detail u, cov_job cj
where c.department_code = u.department_code
and c.cov_job = cj.id
and cj.status = 'completed'
and cj.created > trunc(sysdate);
-------------------------------------------------------------------------
-- total data files per batch -- srdeft
-------------------------------------------------------------------------
select b.id, b.pay_day, b.period, b.verification_period, count(df.id) as "Number of Files" 
from batch_cov_job b, cov_job cj, cash_book cb, data_file df
where b.id = cj.batch_cov_job
and cj.id = cb.cov_job
and cb.id = df.cash_book
group by b.id, b.pay_day, b.period, b.verification_period;
