<%@ page language="java" import="cs5530.*, java.util.*, java.text.SimpleDateFormat"%>
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

<h1>Checking out a book...</h1>


<body>

	<% String usernameattr = request.getParameter("username");

	if(usernameattr	== null){

	%>
	<form name="CheckoutBook" method=get onsubmit="return check_all_fields(this)" action="Checkout.jsp">
		Username:<BR>
		<input type=hidden name="username">
		<input type=text name="userValue" value="simp123" onFocus="value=''">
		
		<br>ISBN of book to checkout:<br>
		
		<input type=hidden name="isbn">
		<input type="text" name="isbnValue" value="1122334455667788" onFocus="value=''">
		<br>
		<br>
		<input type=submit value="Checkout Book">
	</form>

	<%
	} else {

	String usernameval = request.getParameter("userValue");
	String isbnval = request.getParameter("isbnValue");

	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();

	Date today = new Date();
	SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");

	out.println(d.CheckoutBookWeb(usernameval, isbnval, today, conn.con));

	

	conn.closeStatement();
	conn.closeConnection();
	}
	%>

<BR><BR><a href="Checkout.jsp">Checkout another book</a></p>
</body>
</html>