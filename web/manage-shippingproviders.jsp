<%@page import="java.util.List"%>
<%@page import="dto.ShippingProviderDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Shipping Provider Management</title>
</head>
<body>

<h2>Shipping Provider Management</h2>

<!-- ADD FORM -->

<h3>Add Shipping Provider</h3>

<form action="ShippingProviderController" method="post">

<input type="hidden" name="action" value="add"/>

Name:
<input type="text" name="name" required/>

Phone:
<input type="text" name="phone" required/>

Fee:
<input type="number" step="0.01" name="fee" required/>

<button type="submit">Add</button>

</form>

<br><br>

<!-- LIST TABLE -->

<table border="1">

<tr>
<th>ID</th>
<th>Name</th>
<th>Phone</th>
<th>Fee</th>
<th>Action</th>
</tr>

<%
List<ShippingProviderDTO> list =
(List<ShippingProviderDTO>) request.getAttribute("list");

if(list != null){
for(ShippingProviderDTO s : list){
%>

<tr>

<td><%=s.getShipping_provider_id()%></td>

<td><%=s.getName()%></td>

<td><%=s.getPhone()%></td>

<td><%=s.getFee()%></td>

<td>

<a href="ShippingProviderController?action=delete&id=<%=s.getShipping_provider_id()%>">
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