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

<h1>Returning a book...</h1>


<body>

	<%

	String usernameattr = request.getParameter("username");

	if(usernameattr == null){


	%>
	<form name="ReturnBook" method=get onsubmit="return check_all_fields(this)" action="Return.jsp">
		Username:<BR>
		<input type=hidden name="username">
		<input type=text name="userValue" value="simp123" onFocus="value=''">
		
		<br>ISBN:<br>
		
		<input type=hidden name="isbn">
		<input type="text" name="isbnValue" value="1122334455667788" onFocus="value=''">
	
		<br>Date:<br>
		<input type="text" name="monthValue" value="3" onFocus="value=''">
		 / 
		<input type="text" name="dayValue" value="11" onFocus="value=''"> / 
		<input type="text" name="yearValue" value="2552" onFocus="value=''">
		<br>Mark book as:
		<input type="radio" name=lostvalue value="0" checked/>Returned
		<input type="radio" name=lostvalue value="1"/>Lost
		

		<br>
		<input type=submit value="Return Book">

	<%
	} else {

	String usernameval = request.getParameter("userValue");
	String isbnval = request.getParameter("isbnValue");
	String lost = request.getParameter("lostvalue");
	String returnDate = request.getParameter("yearValue") + "-" + request.getParameter("monthValue") + "-" + request.getParameter("dayValue");
	boolean isLost;

	if(lost.equals("1"))
		isLost = true;
	else
		isLost = false;

	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();

	out.println(d.ReturnBookWeb(usernameval, isbnval, isLost, returnDate, conn.con));
	

	


//	Date today = new Date();

//	out.println(d.AddToWaitListWeb(usernameval, isbnval, today, conn.con));

	conn.closeStatement();
	conn.closeConnection();
	}
	%>

	<BR><BR><a href="Return.jsp">Return another book</a></p>
	</body>
	</html>