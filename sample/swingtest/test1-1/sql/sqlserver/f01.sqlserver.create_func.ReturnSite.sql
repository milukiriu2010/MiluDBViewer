CREATE FUNCTION ReturnSite
( @site_id INT )

RETURNS VARCHAR(50)

AS

BEGIN

   DECLARE @site_name VARCHAR(50);

   IF @site_id < 10
      SET @site_name = 'TechOnTheNet.com';
   ELSE
      SET @site_name = 'CheckYourMath.com';

   RETURN @site_name;

END;
