create or replace procedure kunimei_en2jp
(
	io_name     in out varchar2,
	out_spanish out    varchar2,
	out_err     out    number
)
is
begin
	out_err := 0;
	select japanese, spanish into io_name,out_spanish from m_country where english = io_name;
exception
	when others then
	     out_err := sqlcode;
end;
/
