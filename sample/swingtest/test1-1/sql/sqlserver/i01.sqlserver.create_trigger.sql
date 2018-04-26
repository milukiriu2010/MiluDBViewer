create table tblOrders
(
  OrderID integer Identity(1,1) primary key,
  OrderApprovalDateTime datetime,
  OrderStatus varchar(20)
)

create table tblOrdersAudit
(
  OrderAuditID integer Identity(1,1) primary key,
  OrderID integer,
  OrderApprovalDateTime datetime,
  OrderStatus varchar(20),
  UpdatedBy nvarchar(128),
  UpdatedOn datetime
)
go
  
create trigger tblTriggerAuditRecord on tblOrders
after update, insert
as
begin
  insert into tblOrdersAudit 
  (OrderID, OrderApprovalDateTime, OrderStatus, UpdatedBy, UpdatedOn )
  select i.OrderID, i.OrderApprovalDateTime, i.OrderStatus, SUSER_SNAME(), getdate() 
  from  tblOrders t 
  inner join inserted i on t.OrderID=i.OrderID 
end
go

insert into tblOrders values (NULL, 'Pending')
insert into tblOrders values (NULL, 'Pending')
insert into tblOrders values (NULL, 'Pending')
go

select * from tblOrders
select * from tblOrdersAudit

update tblOrders 
set OrderStatus='Approved', 
OrderApprovalDateTime=getdate()  
where OrderID=1
go

select * from tblOrders
select * from tblOrdersAudit order by OrderID, OrderAuditID
go

update tblOrders 
set OrderStatus='Approved', 
OrderApprovalDateTime=getdate()  
where OrderID=2

go

select * from tblOrders
select * from tblOrdersAudit order by OrderID, OrderAuditID
go

update tblOrders 
set OrderStatus='Cancelled'
where OrderID=1
go

select * from tblOrders
select * from tblOrdersAudit order by OrderID, OrderAuditID
go
