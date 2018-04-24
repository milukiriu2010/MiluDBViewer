
CREATE USER miluad for login miluad; 
GO

CREATE USER milu for login milu; 
GO


==================================================================

sqlcmd -U milu -P milu -d miludb
sqlcmd -E -S localhost -d miludb


==================================================================

sqlcmd -E -S W-T-KIRIU2018


>>> NG
sqlcmd -U miluad -P miluad -d miludb
sqlcmd -U milu   -P milu -d miludb
sqlcmd -Usa -P miluad -S W-T-KIRIU2018
