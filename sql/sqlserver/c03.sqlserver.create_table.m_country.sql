create table m_country
(
    ID          numeric(4),
    English     varchar(40),
    Japanese    nvarchar(40),
    Chinese     nvarchar(40),
    French      varchar(40),
    Spanish     varchar(40),
    constraint pk_m_country primary key( ID )
);


insert into m_country values( 1, 'America', 'アメリカ', '美国', 'Amérique', 'America' );

insert into m_country values( 81, 'Japan', '日本', '日本', 'Japon', 'Japon' );
