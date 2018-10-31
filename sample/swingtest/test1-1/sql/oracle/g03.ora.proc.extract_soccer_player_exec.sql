set serveroutput on

declare
  i_country_id   number(3)    := 81;
  i_position     varchar2(2)  := 'GK';
  o_name         varchar2(20);
  o_birthday     date;
begin
  extract_soccer_player(i_country_id,i_position,o_name,o_birthday);
  dbms_output.put_line( 'o_name[' || o_name || ']o_birthday[' || o_birthday || ']' );
end;
/
