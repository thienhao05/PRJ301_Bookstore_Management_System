<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="dto.PublisherDTO"%>

<h2>Manage Publishers</h2>

<form action="PublisherController" method="post">

<input type="hidden" name="action" value="add"/>

Name: <input type="text" name="name" required/>
Description: <input type="text" name="description"/>

<button type="submit">Add</button>

</form>

<hr>

<table border="1">

<tr>
<th>ID</th>
<th>Name</th>
<th>Description</th>
<th>Action</th>
</tr>

<%
List<PublisherDTO> list = (List<PublisherDTO>)request.getAttribute("list");

if(list != null){
for(PublisherDTO p : list){
%>

<tr>

<td><%=p.getId()%></td>
<td><%=p.getName()%></td>
<td><%=p.getDescription()%></td>

<td>

<a href="PublisherController?action=delete&id=<%=p.getId()%>">
Delete
</a>

</td>

</tr>

<%
}
}
%>

</table>