create table m_npb_position
(
	id	varchar2(10),
	name	varchar2(20),
	constraint pk_m_npb_position primary key( id )
)
/

insert into m_npb_position
values
( 'P', 'Pitcher' );

insert into m_npb_position
values
( 'F', 'First Baseman' );

insert into m_npb_position
values
( 'S', 'Second Baseman' );

insert into m_npb_position
values
( 'T', 'Third Baseman' );

insert into m_npb_position
values
( 'SS', 'Short Stop' );

insert into m_npb_position
values
( 'LF', 'Left Fielder' );

insert into m_npb_position
values
( 'CF', 'Center Fielder' );

insert into m_npb_position
values
( 'RF', 'Right Fielder' );

insert into m_npb_position
values
( 'C', 'Catcher' );
