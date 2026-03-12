<%@page import="java.util.List"%>
<%@page import="dto.NewsDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>News Management</title>
</head>

<body>

<h2>News Management</h2>

<!-- ADD FORM -->

<h3>Add News</h3>

<form action="NewsController" method="post">

<input type="hidden" name="action" value="add"/>

Title:
<input type="text" name="title" required/>

Content:
<input type="text" name="content" required/>

<button type="submit">Add</button>

</form>

<br><br>

<!-- NEWS TABLE -->

<table border="1">

<tr>
<th>ID</th>
<th>Title</th>
<th>Content</th>
<th>Created At</th>
<th>Staff</th>
<th>Action</th>
</tr>

<%
List<NewsDTO> list =
(List<NewsDTO>) request.getAttribute("list");

if(list != null){
for(NewsDTO n : list){
%>

<tr>

<td><%=n.getNews_id()%></td>

<td><%=n.getTitle()%></td>

<td><%=n.getContent()%></td>

<td><%=n.getCreated_at()%></td>

<td><%=n.getStaff_id()%></td>

<td>

<a href="NewsController?action=delete&id=<%=n.getNews_id()%>">
Delete
</a>

</td>

</tr>

<%
}
}
%>

</table>

</body>
</html>