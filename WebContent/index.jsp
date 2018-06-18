<%@page import="java.util.List"%>
<%@page import="xmlParser.xmltodb"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>  
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
table {
    border-collapse: collapse;
    width: 60%;
    margin: auto;
    border: 3px solid green;
    padding: 10px;
}

th, td {
    text-align: left;
    padding: 8px;
    border-style: inset;
    border-width: 2px;
}

tr:nth-child(even){background-color: #f2f2f2}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
String token = request.getParameter("xml");
System.out.println(token);
if(token != null){
	List <String> list = xmltodb.saveUser(token);
	System.out.println(list); 	
	request.setAttribute("list",list);  
	%>
	
	<h2>Insertion </h2>
	<p>A responsive table will display a horizontal scroll bar if the screen is too 
	<div style="overflow-x:auto;">
  <table>
    <tr> <th>Société</th><th>Nom</th><th>Prenom</th><th>Téléphone</th><th>Fax</th><th>Client</th><th>Contrat</th><th>Lot</th></tr>
	<tr>
	<c:forEach items="${list}" var="item">
   		<td>	 ${item}</td>
	</c:forEach>
	</tr>
	</table>
	</div>
	<%
}else {
%>
No parameter was given
<%
}
%>
</body>
</html>