<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="bootstrap.css" />

	<script LANGUAGE="javascript">

	function check_all_fields(form_obj){
		alert(form_obj.searchAttribute.value+"='"+form_obj.attributeValue.value+"'");
		if( form_obj.attributeValue.value == ""){
			alert("No empty fields!");
			return false;
		}
		return true;
	}
	</script>

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

	if(usernameattr == null){

	%>

	<div class="jumbotron">
	<h1 class="text-center">Add New User</h1>
</div>

	<form role="form" name="UserAdd" method=get onsubmit="return check_all_fields(this)" action="NewUser.jsp">

		<br>Unique Username<br>
		<input class="form-control" type=hidden name="username">
		<input class="form-control" type=text name="userValue" value="simp123" onFocus="value=''">

		<br>Identification Number<br>
		<input class="form-control" type=hidden name="cardid">
		<input class="form-control" type=text name="idValue" value="123456789" onFocus="value=''">


		<br>Full Name<br>
		<!-- <input type="text" name="fullname"> -->
		<input class="form-control" type=hidden name="full_name">
		<input class="form-control" type=text name="nameValue" value="Homer Simpson" onFocus="value=''">

		<br>Address<br>
		<!-- <input type="text" name="address"> -->
		<input class="form-control" type=hidden name="address">
		<input class="form-control" type=text name="addressValue" value="123 Fake Street" onFocus="value=''">

		<br>Phone Number<br>
		<!-- <input type="text" name="phonenumber"> -->
		<input class="form-control" type=hidden name="phonenumber">
		<input class="form-control" type=text name="phoneNumberValue" value="555-555-5555" onFocus="value=''">

		<br>Email Address<br>
		<!-- <input type="text" name="email"> -->
		<input class="form-control" type=hidden name="email">
		<input class="form-control" type=text name="emailValue" value="user@site.com" onFocus="value=''">		

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
	cs5530.Database d = new Database();


%>
<h1> User added! </h1>

<!-- Show confirmation of user being added to data base -->
	<%

	d.AddUserWeb(userNameVal, idVal, nameVal, addressVal, phoneVal, emailVal, conn.con);
	out.println("Username: " + userNameVal + "<br>");
	out.println("UserID: " + idVal + "<br>");
	out.println("Full Name: " + nameVal + "<br>");
	out.println("Address: " + addressVal + "<br>");
	out.println("Phone Number: " + phoneVal + "<br>");
	out.println("Email: " + emailVal + "<br>");

conn.closeStatement();
conn.closeConnection();
}
%>
<br>
<a href="NewUser.jsp" class="btn btn-primary" role="button">Add Another User</a>


</body>
</html>