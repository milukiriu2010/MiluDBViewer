create or replace package calc_date as
       function now_month
       (
       p_DATE  IN DATE := SYSDATE
       )
       return date;
end calc_date;
/

create or replace package body calc_date as
       function now_month
       (
       p_DATE  IN DATE
       )
       return date
       is
       begin
		return trunc( 'DD', p_DATE );
       end;
end calc_date;
/
