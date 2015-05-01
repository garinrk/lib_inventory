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

		<br>Unique Username EX: "MrSimpson"<br>
		<input class="form-control" type=hidden name="username">
		<input class="form-control" type=text name="userValue" >

		<br>Identification Number EX: "1234"<br>
		<input class="form-control" type=text name="idValue"  >


		<br>Full Name EX: "Homer Simpson"<br>
		<input class="form-control" type=text name="nameValue">

		<br>Address Ex: "742 Evergreen Terrace"<br>
		<input class="form-control" type=text name="addressValue">

		<br>Phone Number Ex: 555-8707<br>
		<input class="form-control" type=text name="phoneNumberValue">

		<br>Email Address EX: "CEO@MrPlow.com<br>
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
		out.println("There was an empty field, please try again");
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
<br>
<a href="NewUser.jsp" class="btn btn-primary" role="button">Add Another User</a>


</body>
</html>