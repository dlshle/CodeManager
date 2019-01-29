<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="Management.User" %>
    <%@ page import="MainLogic.VersionControl.Version" %>
    <%@ page import="FileImpl.FileTree.FileTree" %>
    <%@ page import="java.util.ArrayList" %>
    <%User user = (User)request.getAttribute("user");Version v = (Version)request.getAttribute("version");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%out.print(user.getName()+"'s "+v.getProjectName()+" v"+v.getVersion()); %></title>
</head>
<body>
Project:
<%out.print(v.getProjectName()); %>
<br>
Version:
<%out.print(v.getVersion()); %>
<br>
Authors:
<br>
<%
for(String s:v.getAuthors())
	out.print(s+"<br>");
%>
Last modified date:
<%out.print(v.getDate()); %>
Documents:
<br>
<%
String[] documents = v.getDocumentNames();
for(int i=0;i<documents.length;i++){
	out.print("<a href=\""+"dispatch?viewDocument="+v.getProjectId()+","+v.getVersionId()+","+i+"\">"+documents[i]+"</a><br>");
}
%>
Descriptions:<br>
<%out.print(v.getDescription()); %>
</body>
</html>