-- # https://qiita.com/SRsawaguchi/items/411801e254ee66f511f1

DROP TABLE IF EXISTS programming;

CREATE TABLE programming(
  id SERIAL,
  name VARCHAR(255) NOT NULL,
  first_appeared INT NOT NULL
);

INSERT INTO programming(name, first_appeared) VALUES
  ('Lisp', 1958),
  ('C', 1972),
  ('SQL', 1974),
  ('python', 1991),
  ('Java', 1995),
  ('PHP', 1995),
  ('Scala', 2004),
  ('Rust', 2010);
  
DROP FUNCTION IF EXISTS years_ago2(INTEGER);

CREATE OR REPLACE FUNCTION years_ago2(INTEGER)
RETURNS INTEGER AS $$
  SELECT (extract(year from current_date)::INTEGER - first_appeared)
  FROM programming
  WHERE id = $1;
$$ LANGUAGE sql;

DROP FUNCTION IF EXISTS years_ago3(INTEGER);

CREATE OR REPLACE FUNCTION years_ago3(first_appeared INTEGER)
RETURNS INTEGER AS $$
  DECLARE
    current_year INTEGER := extract(year from current_date)::INTEGER;
  BEGIN
    RETURN current_year - first_appeared;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS era(INTEGER);

CREATE OR REPLACE FUNCTION era(first_appeared INTEGER)
RETURNS TEXT AS $$
  BEGIN
    IF first_appeared < 1990 THEN
      RETURN 'ancient';
    ELSEIF first_appeared >= 1990 AND first_appeared < 2000 THEN
      RETURN 'middle';
    ELSE
      RETURN 'modern';
    END IF;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS add_all_lang(VARCHAR(255)[], INTEGER);

CREATE OR REPLACE FUNCTION add_all_lang(names VARCHAR(255)[], first_appeared INTEGER)
RETURNS VOID AS $$
  BEGIN
    FOR i IN 1 .. array_length(names, 1) LOOP
      INSERT INTO programming(name, first_appeared)
        VALUES(names[i], first_appeared);
    END LOOP;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS add_all_lang2(VARCHAR(255)[], INTEGER);

CREATE OR REPLACE FUNCTION add_all_lang2(names VARCHAR(255)[], first_appeared INTEGER)
RETURNS VOID AS $$
  DECLARE
    name VARCHAR(255);
  BEGIN
    FOREACH name IN ARRAY names LOOP
      INSERT INTO programming(name, first_appeared)
        VALUES(name, first_appeared);
    END LOOP;
  END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS print(TEXT);

CREATE OR REPLACE FUNCTION print(message TEXT)
RETURNS VOID AS $$
  BEGIN
    RAISE INFO '%', message;
  END;
$$ LANGUAGE plpgsql;
