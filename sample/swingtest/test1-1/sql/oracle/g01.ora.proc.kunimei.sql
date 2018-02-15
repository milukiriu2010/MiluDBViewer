create or replace procedure kunimei_cnt
(
	in_id in number,
	out_cnt out number,
	out_err out number
)
is
begin
	out_err := 0;
	select count(*) into out_cnt from m_国名 where id = in_id;
exception
	when others then
	     out_err := sqlcode;
end;
/
