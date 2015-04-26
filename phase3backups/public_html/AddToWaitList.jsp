<%@ page language="java" import="cs5530.*, java.util.*" %>
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

<h1>Add user to waitlist</h1>


<body>

	<%

	String usernameattr = request.getParameter("username");

	if(usernameattr == null){


	%>
	<form name="AddToWaitList" method=get onsubmit="return check_all_fields(this)" action="AddToWaitList.jsp">
		Username:<BR>
		<input type=hidden name="username">
		<input type=text name="userValue" value="simp123" onFocus="value=''">
		
		<br>ISBN:<br>
		
		<input type=hidden name="isbn">
		<input type="text" name="isbnValue" value="1122334455667788" onFocus="value=''">
		<br>
		<br>
		<input type=submit value="Add User To WaitList">

	<%
	} else {

	String usernameval = request.getParameter("userValue");
	String isbnval = request.getParameter("isbnValue");

	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();

	Date today = new Date();

	out.println(d.AddToWaitListWeb(usernameval, isbnval, today, conn.con));

	conn.closeStatement();
	conn.closeConnection();
	}
	%>

	<BR><BR><a href="AddToWaitList.jsp">Add another user to a waitlist</a></p>
	</body>
	</html>