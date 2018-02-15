-- http://www.fromdual.com/mysql-materialized-views

CREATE TABLE sales (
    sales_id       INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
  , product_name   VARCHAR(128) NOT NULL
  , product_price  DECIMAL(8,2) NOT NULL
  , product_amount SMALLINT     NOT NULL
);

INSERT INTO sales VALUES
  (NULL, 'Apple', 1.25, 1), (NULL, 'Apple', 2.40, 2)
, (NULL, 'Apple', 4.05, 3), (NULL, 'Pear', 6.30, 2)
, (NULL, 'Pear', 12.20, 4), (NULL, 'Plum', 4.85, 3)
;


DROP TABLE sales_mv;
CREATE TABLE sales_mv (
    product_name VARCHAR(128)  NOT NULL
  , price_sum    DECIMAL(10,2) NOT NULL
  , amount_sum   INT           NOT NULL
  , price_avg    FLOAT         NOT NULL
  , amount_avg   FLOAT         NOT NULL
  , sales_cnt    INT           NOT NULL
  , UNIQUE INDEX product (product_name)
);

INSERT INTO sales_mv
SELECT product_name
    , SUM(product_price), SUM(product_amount)
    , AVG(product_price), AVG(product_amount)
    , COUNT(*)
  FROM sales
GROUP BY product_name;

DROP PROCEDURE refresh_mv_now;

DELIMITER $$

CREATE PROCEDURE refresh_mv_now (
    OUT rc INT
)
BEGIN

  TRUNCATE TABLE sales_mv;

  INSERT INTO sales_mv
  SELECT product_name
      , SUM(product_price), SUM(product_amount)
      , AVG(product_price), AVG(product_amount)
      , COUNT(*)
    FROM sales
  GROUP BY product_name;

-------------------------------------------------------------------

DELIMITER $$

CREATE TRIGGER sales_ins
AFTER INSERT ON sales
FOR EACH ROW
BEGIN

  SET @old_price_sum = 0;
  SET @old_amount_sum = 0;
  SET @old_price_avg = 0;
  SET @old_amount_avg = 0;
  SET @old_sales_cnt = 0;

  SELECT IFNULL(price_sum, 0), IFNULL(amount_sum, 0), IFNULL(price_avg, 0)
       , IFNULL(amount_avg, 0), IFNULL(sales_cnt, 0)
    FROM sales_mv
   WHERE product_name = NEW.product_name
    INTO @old_price_sum, @old_amount_sum, @old_price_avg
       , @old_amount_avg, @old_sales_cnt
  ;

  SET @new_price_sum = @old_price_sum + NEW.product_price;
  SET @new_amount_sum = @old_amount_sum + NEW.product_amount;
  SET @new_sales_cnt = @old_sales_cnt + 1;
  SET @new_price_avg = @new_price_sum / @new_sales_cnt;
  SET @new_amount_avg = @new_amount_sum / @new_sales_cnt;

  REPLACE INTO sales_mv
  VALUES(NEW.product_name, @new_price_sum, @new_amount_sum, @new_price_avg
       , @new_amount_avg, @new_sales_cnt)
  ;

END;
$$

DELIMITER ;



  SET rc = 0;
END;
$$

DELIMITER ;
