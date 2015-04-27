<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	

	<script LANGUAGE="javascript">

	</script>
	<meta name="viewport" content="width=device-width, initial-scale=1">
 
	<link rel="stylesheet" type="text/css" href="bootstrap.css" />
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>

	  <%

  String numberofusersattr = request.getParameter("numberofusers");

  if(numberofusersattr == null){


  %>


<div class="jumbotron">
	<h1 class="text-center">Printing User Statistics</h1>
</div>

	<form role="form" name="GetUserStats" method=get onsubmit="return check_all_fields(this)" action="UserStats.jsp">
		Please select an option:<br>
		<select class="form-control" name="statsSelection">
			<option value="1">Users who have checked out the most books</option>
			<option value="2">Users who have rated the most number of books</option>
			<option value="3">Users who have lost the most books</option>
		</select>
		<br><br>
		How many users are you interested in?<br>
		<input class="form-control" type=hidden name="numberofusers">
		<input class="form-control" type=text name="usersValue" value="2" onFocus="value=''">
		<br><br>
		<input type=submit class="btn btn-info" value="Get User Statistics">
	</form>
	  <%
} else {

String amountval = request.getParameter("booksValue");
String selection = request.getParameter("statsSelection");

cs5530.Connector conn = new Connector();
cs5530.Database d = new Database();
out.println("<h2>Results:</h2><BR>");

out.println(d.PrintUserStatisticsWeb(amountval, selection, conn.con));

conn.closeStatement();
conn.closeConnection();



}
%>



<br><BR>
	<a href="UserStats.jsp" class="btn btn-primary" role="button">Get More Statistics</a>
</body>
</html>