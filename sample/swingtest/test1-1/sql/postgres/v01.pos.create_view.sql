create view country_by_region
as
  select
    region,
	count(*) cnt
  from
    country
  group by region
  order by region
;

    