set serveroutput on

declare
  io_name     varchar2(10) := 'Spain';
  o_spanish   varchar2(10);
  o_err       number(10);
begin
  kunimei_en2jp(io_name,o_spanish,o_err);
  dbms_output.put_line( 'io_name[' || io_name || ']o_spanish[' || o_spanish || ']o_err[' || o_err || ']' );
end;
/
