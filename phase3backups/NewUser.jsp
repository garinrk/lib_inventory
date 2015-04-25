<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="main.css" />

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

</head> 

<h1>Adding a new user to the library</h1>

Please enter the required information:<br>
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


	<br>Unique Username<br>
	<form name="UserAdd" method=get onsubmit="return check_all_fields(this)" action="NewUser.jsp">
		<input type=hidden name="username">
		<input type=text name="userValue">

		<br>Identification Number<br>
		<input type=hidden name="cardid">
		<input type=text name="idValue">


		<br>Full Name<br>
		<input type="text" name="fullname">
		<input type=hidden name="full_name">
		<input type=text name="nameValue">

		<br>Address<br>
		<input type="text" name="address">
		<input type=hidden name="address">
		<input type=text name="addressValue">

		<br>Phone Number<br>
		<input type="text" name="phonenumber">
		<input type=hidden name="phonenumber">
		<input type=text name="phoneNumberValue">

		<br>Email Address<br>
		<input type="text" name="email">
		<input type=hidden name="email">
		<input type=text name="emailValue">		

		<br><br>
		<input type=submit value="Add New User">

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
	Database d = new Database();


%>

<!-- Show confirmation of user being added to data base -->
<h1> User added to database </h1>
	<!-- The orders for query: <b><%=usernameattr%>='<%=userNameVal%>'</b> are  as follows:<BR><BR> -->
	<%

	
	%>
	<!-- d.AddUserWeb(userNameVal, idVal, nameVal, addressVal, phoneVal, emailVal, conn.stmt) -->
<!-- Close connection  -->

conn.closeStatement();
conn.closeConnection();
}
%>

<BR><a href="NewUser.jsp"> Add another user </a></p>

</body>
</html>