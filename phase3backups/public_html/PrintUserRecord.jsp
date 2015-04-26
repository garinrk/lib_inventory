	<%@ page language="java" import="cs5530.*" %>
	<html>

	<head>
		<link rel="stylesheet" type="text/css" href="main.css" />

		<script LANGUAGE="javascript">

		function check_all_fields(form_obj){
			alert(form_obj.searchAttribute.value+"='"+form_obj.attributeValue.value+"'");
			if( form_obj.attributeValue.value == ""){
				alert("Search field should be nonempty");
				return false;
			}
			return true;
		}

		</script>

	</head>

	<h1>Print user record</h1>


	<body>

		<%

		String usernameattr = request.getParameter("username");

		if(usernameattr == null)
			{

		%>

		

		<form name="UserLookup" method=get onsubmit="return check_all_fields(this)" action="PrintUserRecord.jsp">
			Please enter a username:<br>

			<input type=hidden name="username">
			<input type=text name="userValue" value="simp123" onFocus="value=''">

			<br><br>
			<input type=submit value="Lookup User Record">
		</form>

	<%
	}	else {

	String usernameval = request.getParameter("userValue");

	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();
	
	//call function
	out.println(d.PrintUserRecordWeb(usernameval, conn.con));


	conn.closeStatement();
	conn.closeConnection();
	}
	%>

	<BR><a href="PrintUserRecord.jsp"> Lookup another user </a></p>

</body>
</html>