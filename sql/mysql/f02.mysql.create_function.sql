http://proengineer.internous.co.jp/content/columnfeature/7078#section400

DELIMITER //
CREATE FUNCTION func_test01(input INT) RETURNS FLOAT(10,2) DETERMINISTIC
BEGIN
  DECLARE zei_ritsu FLOAT(3,2);
  SET zei_ritsu = 1.08;
  RETURN input * zei_ritsu;
END;
//

DELIMITER ;
