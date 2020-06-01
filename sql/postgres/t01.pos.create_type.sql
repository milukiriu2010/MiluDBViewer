CREATE TYPE compfoo AS (f1 int, f2 text);

CREATE FUNCTION getfoo() RETURNS SETOF compfoo AS $$
    SELECT league, head_name FROM m_npb_team_list
$$ LANGUAGE SQL;
