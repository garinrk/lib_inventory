	<%@ page language="java" import="cs5530.*" %>
	<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
 
	<link rel="stylesheet" type="text/css" href="bootstrap.css" />
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

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


	<body>

		<%

		String usernameattr = request.getParameter("username");

		if(usernameattr == null)
			{

		%>

		
<div class="jumbotron">
	<h1 class="text-center">Print User Record</h1>
</div>
		<form name="UserLookup" method=get onsubmit="return check_all_fields(this)" action="PrintUserRecord.jsp">
			Please enter a username:<br>

			<input type=hidden name="username">
			<input class="form-control" type=text name="userValue" value="simp123" onFocus="value=''">

			<br><br>
			<input type=submit class="btn btn-info" value="Lookup User Record">
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
	<br><BR>
	<a href="PrintUserRecord.jsp" class="btn btn-primary" role="button">Lookup Another User</a>

</body>
</html>