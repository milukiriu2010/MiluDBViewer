create materialized view country_by_continent
as
  select
    continent,
	count(*) cnt
  from
    country
  group by continent
  order by continent
with no data;

REFRESH MATERIALIZED VIEW country_by_continent;
