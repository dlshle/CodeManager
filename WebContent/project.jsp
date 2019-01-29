<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="Management.User" %>
    <%@ page import="MainLogic.VersionControl.VersionIndex" %>
    <%@ page import="java.util.ArrayList" %>
    <%User user = (User)request.getAttribute("user");VersionIndex vIndex = (VersionIndex)request.getAttribute("project");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%out.print(user.getName()+"'s"+vIndex.getProjectName()); %></title>
</head>
<body>
Project:
<br>
<%out.print(vIndex.getProjectName()); %>
<br>
Authors:
<br>
<%
for(String s:vIndex.getAuthors())
	out.print(s+"<br>");
%>
Created date:
<br>
<%out.print(vIndex.getCreatedDate()); %>
Last modified date:
<br>
<%out.print(vIndex.getLastModifiedDate()); %>
<br>
Versions:
<br>
<%
ArrayList<String> vNames = vIndex.getVersionNames();
for(int i=0;i<vIndex.getNumOfVersions();i++){
	out.print(i+"&#09;"+"<a href=\""+"dispatch?viewVersion="+vIndex.getId()+","+i+"\">"+vNames.get(i)+"</a><br>");
}
%>
<br>
Descriptions:
<%out.print(vIndex.getDescriptions()); %>
<br>
<hr>
<form action="uploadversion" enctype= "multipart/form-data" method="post">
  Project name:<br>
  <input type="text" name="projectName" value="">
  <br>
  Authors(separated by comma):<br>
  <input type="text" name="authors" value="<%out.print(user.getName());%>">
  <br>
  Version descriptions:
  <br>
  <textarea id="vDes" class="text" cols="25" rows ="20" name="vdescriptions"></textarea>
  <br>
  Zipped version files:<br>
  <input type="file" name="ver" accept=".zip">
  <br>
  <input type="submit" value="Submit">
</form> 
</body>
</html>