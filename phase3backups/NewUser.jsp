<%@ page language="java" import="cs5530.*" %>
<html>

	<head>
		 <link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	function add_user(form_obj)
	{
		alert("Submitted form");

		return true;
	}

	</script>

	</head>

	<h1>Adding a new user to the library</h1>

Please enter the required information:<br>
<body>




<br>Unique Username<br>
<form name="UserAdd" method=post action="NewUser.jsp">
	<input type=hidden name="userAttribute">
	<input type=text name="userValue">

<br>Full Name<br>
	<input type="text" name="fullname">

<br>Address<br>
	<input type="text" name="address">

<br>Phone Number<br>
	<input type="text" name="phonenumber">

<br>Email Address<br>
	<input type="text" name="email">

<br><br>
<input type=submit value="Add user">

</form>

<%

String attributeValue = request.getParameter("userAttribute");

%>

</body>
</html>