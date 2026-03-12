<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="dto.CategoryDTO"%>

<h2>Manage Categories</h2>

<form action="CategoryController" method="post">
    <input type="hidden" name="action" value="add"/>

    Name: <input type="text" name="name" required/>
    Description: <input type="text" name="description"/>
    Status: 
    <select name="status">
        <option value="1">Active</option>
        <option value="0">Inactive</option>
    </select>

    <button type="submit">Add</button>
</form>

<hr>

<table border="1">

<tr>
<th>ID</th>
<th>Name</th>
<th>Description</th>
<th>Status</th>
<th>Action</th>
</tr>

<%
List<CategoryDTO> list = (List<CategoryDTO>)request.getAttribute("list");

if(list != null){
for(CategoryDTO c : list){
%>

<tr>
<td><%=c.getId()%></td>
<td><%=c.getName()%></td>
<td><%=c.getDescription()%></td>
<td><%=c.getStatus()%></td>

<td>

<a href="CategoryController?action=delete&id=<%=c.getId()%>">
Delete
</a>

</td>

</tr>

<%
}
}
%>

</table>