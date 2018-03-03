# this doesn't work Japanese.
create database miludb with owner milu encoding utf8;
# invalid locale name
#create database miludb2 with owner milu encoding 'utf8' lc_collate = 'ja_JP.UTF-8' lc_ctype = 'ja_JP.UTF-8';
# ERROR: new collation (Japanese_Japan.932) is incompatible with the collation of the template database (English _United States.1252)
#create database miludb2 with owner milu encoding 'utf8' lc_collate = 'Japanese_Japan.932' lc_ctype = 'Japanese_Japan.932';
# OK!!
create database miludb2 with owner milu encoding 'utf8' lc_collate = 'Japanese_Japan.932' lc_ctype = 'Japanese_Japan.932' template template0;

#grant all privileges on database miludb to milu;
