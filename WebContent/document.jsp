<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="Management.User" %>
    <%@ page import="MainLogic.Document.Document" %>
    <%@ page import="java.util.ArrayList" %>
    <%User user = (User)request.getAttribute("user");Document d = (Document)request.getAttribute("document");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%out.print(d.getFileName()); %></title>
</head>
<body>
<%out.print(d.getFileName()); %>
<br>
Document path:
<%out.print(request.getAttribute("documentPath"));%>
<br>
Type:
<%out.print(d.getType()); %>
<br>
Last modified date:
<%out.print(d.getLastModifiedDate()); %>
<br>
<%
switch(d.getType()){
case "text/plain":
	out.print("content:<br><p>"+d.getContent()+"</p>");
	break;
case "image":
	out.print("<img src=\""+d.getPath()+"\" alt=\"Mountain View\">");
	break;
default:
	out.print("<a href=\""+request.getAttribute("realPath")+"\">Download the document</a>");
}
%>
</body>
</html>