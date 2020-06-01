create table m_country
(
	ID			number(4),
	English		varchar2(40),
	Japanese	varchar2(40),
	Chinese		varchar2(40),
	French		varchar2(40),
	Spanish		varchar2(40),
	constraint pk_m_country primary key( ID )
)
/

insert into m_country values( 1, 'America', 'アメリカ', '美国', 'Amérique', 'America' );

insert into m_country values( 81, 'Japan', '日本', '日本', 'Japon', 'Japon' );


-- create table m_国名
-- (
-- 	ID		number(4),
-- 	English		varchar2(40),
-- 	日本語		varchar2(40),
-- 	中文简体字	varchar2(40),
-- 	中文繁體字	varchar2(40),
-- 	한국어		varchar2(40),
-- 	Français	varchar2(40),
-- 	Deutsch		varchar2(40),
-- 	Litaliano	varchar2(40),
-- 	Español		varchar2(40),
-- 	Português	varchar2(40),
-- 	constraint pk_m_国名 primary key( English )
-- )
-- /

--create unique index ui_m_国名_English on m_国名( ENGLISH );
