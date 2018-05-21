<!-- 
This code is freely distributed and modified.  Produced by Unity Data Inc.
The MongoDB JDBC driver and UnityJDBC driver are free to download at www.unityjdbc.com.
 -->
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

<h2>Mongo query: SELECT _id, n_nationkey, n_name, n_regionkey FROM nation WHERE n_name &gt;= 'C'</h2>  
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

<sql:query dataSource="${data}" var="result2">
SELECT _id, r_regionkey, r_name FROM region WHERE r_regionkey <= 3;
</sql:query>

<h2>Mongo query: SELECT _id, r_regionkey, r_name FROM region WHERE r_regionkey &lt;= 3;</h2>
<table border="1">
<tr>
<th>_id</th>
<th>Region Id</th>
<th>Region Name</th>
</tr>
<c:forEach var="row" items="${result2.rows}">
<tr>
<td><c:out value="${row._id}"/></td>
<td><c:out value="${row.r_regionkey}"/></td>
<td><c:out value="${row.r_name}"/></td>
</tr>
</c:forEach>
</table>

<sql:query dataSource="${data}" var="result3">
SELECT nation._id, r_regionkey, r_name, n_nationkey, n_regionkey, n_name FROM region INNER JOIN nation on r_regionkey = n_regionkey WHERE r_regionkey <= 3 and n_name >= 'C'
</sql:query>

<h2>Mongo query with SQL join between collections: SELECT nation._id, r_regionkey, r_name, n_nationkey, n_name FROM region INNER JOIN nation on r_regionkey = n_regionkey WHERE r_regionkey &lt;= 3 and n_name &gt;= 'C';</h2>
<table border="1">
<tr>
<th>_id (nation)</th>
<th>Region Id</th>
<th>Region Name</th>
<th>Nation Id</th>
<th>Nation Name</th>
</tr>
<c:forEach var="row" items="${result3.rows}">
<tr>
<td><c:out value="${row._id}"/></td>
<td><c:out value="${row.r_regionkey}"/></td>
<td><c:out value="${row.r_name}"/></td>
<td><c:out value="${row.n_nationkey}"/></td>
<td><c:out value="${row.n_name}"/></td>
</tr>
</c:forEach>
</table>

</body>
</html>