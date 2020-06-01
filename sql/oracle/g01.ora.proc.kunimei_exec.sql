set serveroutput on

declare
  i_id   number(4) := 33;
  o_cnt  number(10);
  o_err  number(10);
begin
  kunimei_cnt(i_id,o_cnt,o_err);
  dbms_output.put_line( 'o_cnt[' || o_cnt || ']o_err[' || o_err || ']' );
end;
/
