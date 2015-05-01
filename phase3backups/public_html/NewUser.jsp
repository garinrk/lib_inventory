<%@ page language="java" import="cs5530.*" %>
<html>
<!-- 

Author: Garin Richards
For Phase 3 of Semester Project
CS 5530 - Database Systems - University of Utah
Spring 2015

-->
<head>
	<link rel="stylesheet" type="text/css" href="bootstrap.css" />

	<meta name="viewport" content="width=device-width, initial-scale=1">


	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head> 

<body>


	<% 

	String usernameattr = request.getParameter("username");
	String idattr = request.getParameter("cardid");
	String nameattr = request.getParameter("full_name");
	String addrattr = request.getParameter("address");
	String phoneattr = request.getParameter("phonenumber");
	String emailattr = request.getParameter("email");

	if(usernameattr == null) {

	%>

	<div class="jumbotron">
		<h1 class="text-center">Add New User</h1>
	</div>

	<form role="form" name="UserAdd" method=get action="NewUser.jsp">

		<br>Unique Username<br>
		<input class="form-control" type=hidden name="username">
		<input class="form-control" type=text name="userValue" >

		<br>Identification Number<br>
		<input class="form-control" type=text name="idValue"  >


		<br>Full Name <br>
		<input class="form-control" type=text name="nameValue">

		<br>Address <br>
		<input class="form-control" type=text name="addressValue">

		<br>Phone Number<br>
		<input class="form-control" type=text name="phoneNumberValue">

		<br>Email Address<br>
		<input class="form-control" type=text name="emailValue">		

		<br><br>
		<input type=submit class="btn btn-info" value="Add New User">

	</form>

	<%

} else {

	String userNameVal = request.getParameter("userValue");
	String idVal = request.getParameter("idValue");
	String nameVal = request.getParameter("nameValue");
	String addressVal = request.getParameter("addressValue");
	String phoneVal = request.getParameter("phoneNumberValue");
	String emailVal = request.getParameter("emailValue");

	cs5530.Connector conn = new Connector();

	if(userNameVal == "" || idVal == "" || nameVal == "" || addressVal == "" || phoneVal == "" || emailVal == "") {
		out.println("<BR><BR><h3>No empty fields, please try again</h3>");
	}

	else {
		boolean existing = cs5530.Database.CheckForUserWeb(userNameVal, conn.con);

		if(existing){

		out.println("There already exists a user with that username in the database");

		conn.closeStatement();
		conn.closeConnection();

		}
		else {

		cs5530.Database.AddUserWeb(userNameVal, idVal, nameVal, addressVal, phoneVal, emailVal, conn.con);
		out.println("<h1> User added! </h1>");
		out.println("Username: " + userNameVal + "<br>");
		out.println("UserID: " + idVal + "<br>");
		out.println("Full Name: " + nameVal + "<br>");
		out.println("Address: " + addressVal + "<br>");
		out.println("Phone Number: " + phoneVal + "<br>");
		out.println("Email: " + emailVal + "<br>");

		conn.closeStatement();
		conn.closeConnection();
		}
	}
}


%>

<a href="NewUser.jsp" class="btn btn-primary" role="button">Add Another User</a>
	<div class="text-center"> 
		<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
		</div>


</body>
</html>