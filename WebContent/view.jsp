<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="Management.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>
<%User user = (User)request.getAttribute("user");
out.print(user.getName());
%>'s CodeBase</h1>
<%out.print(user.getName()); %>'s projects:
<%
String[] projects = (String[])request.getAttribute("projects");
out.print("<br>");
for(int i=0;i<projects.length;i++){
	out.print(i+", "+"<a href=\""+"dispatch?viewProject="+i+"\">"+projects[i]+"</a><br>");
}
%>
<hr>
Upload a new project:
<form action="uploadproject" enctype="multipart/form-data" method="post">
  Project name:<br>
  <input type="text" name="projectName" value="">
  <br>
  Authors(separated by comma):<br>
  <input type="text" name="authors" value="<%out.print(user.getName());%>">
  <br>
  Project descriptions:
  <br>
  <textarea class="text" cols="25" rows ="20" name="pdescriptions"></textarea>
  <br>
  First version descriptions:
  <br>
  <textarea class="text" cols="25" rows ="20" name="vdescriptions"></textarea>
  <br>
  Zipped project files:<br>
  <input type="file" name="prj" accept=".zip">
  <br>
  <input type="hidden" name="pid" value="<%out.print(projects.length); %>" />
  <input type="hidden" name="username" value="<%out.print(user.getUserName()); %>" />
  <br>
  <input type="submit" value="Submit">
</form> 
</body>
</html>