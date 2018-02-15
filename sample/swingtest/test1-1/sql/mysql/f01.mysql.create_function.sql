-- # https://www.techonthenet.com/mysql/functions.php

DELIMITER //

CREATE FUNCTION CalcIncome ( starting_value INT )
RETURNS INT

BEGIN

   DECLARE income INT;

   SET income = 0;

   label1: WHILE income <= 3000 DO
     SET income = income + starting_value;
   END WHILE label1;

   RETURN income;

END; //

DELIMITER ;
