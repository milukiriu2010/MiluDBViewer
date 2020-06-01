create table m_country
(
    ID          numeric(4),
    English     text,
    Japanese    text,
    Chinese     text,
    French      text,
    Spanish     text,
    constraint pk_m_country primary key( ID )
);


insert into m_country values( 1, 'America', 'アメリカ', '美国', 'Amérique', 'America' );

insert into m_country values( 81, 'Japan', '日本', '日本', 'Japon', 'Japon' );
