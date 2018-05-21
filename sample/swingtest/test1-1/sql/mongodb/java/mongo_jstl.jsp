<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*,mongodb.jdbc.MongoStatement" %>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE html>
<html>
<head>
<title>MongoDB Query with JSP/JSTL Example</title>
</head>
<body>

<sql:setDataSource var="data" driver="mongodb.jdbc.MongoDriver"
     url="jdbc:mongo://ds029847.mongolab.com:29847/tpch?rebuildschema=false"
     user="dbuser"  password="dbuser"/>

<sql:query dataSource="${data}" var="result">
SELECT _id, n_nationkey, n_name, n_regionkey FROM nation WHERE n_name >= 'C';
</sql:query>
 
<table border="1">
<tr>
<th>_id</th>
<th>Nation Id</th>
<th>Nation Name</th>
<th>Region Id</th>
</tr>
<c:forEach var="row" items="${result.rows}">
<tr>
<td><c:out value="${row._id}"/></td>
<td><c:out value="${row.n_nationkey}"/></td>
<td><c:out value="${row.n_name}"/></td>
<td><c:out value="${row.n_regionkey}"/></td>
</tr>
</c:forEach>
</table>

</body>
</html>