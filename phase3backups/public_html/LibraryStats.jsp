<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

</head>

<h1>Library Stats</h1>


<body>

	  <%

  String numberofbooksattr = request.getParameter("numberofbooks");

  if(numberofbooksattr == null){


  %>

	<form name="GetLibraryStats" method=get onsubmit="return check_all_fields(this)" action="LibraryStats.jsp">
		Number of books (N) :<BR>
		<input type=hidden name="numberofbooks">
		<input type=text name="booksValue" value="2" onFocus="value=''">
		<BR>
		<BR>Stats to return: 
		<select name="statsSelection">
			<option value="1">N most requested books </option>
			<option value="2">N most checked out books</option>
			<option value="3">N most lost books</option>
			<option value="4">N most popular authors</option>
		</select>
		<br><BR>
		<input type=submit value="Get Statistics">

	</form>


	  <%
} else {

String amountval = request.getParameter("booksValue");
String selection = request.getParameter("statsSelection");

cs5530.Connector conn = new Connector();
cs5530.Database d = new Database();

out.println(cs5530.Database.PrintLibraryStatisticsWeb(amountval, selection, conn.con));


conn.closeStatement();
conn.closeConnection();



}
%>


<BR><a href="LibraryStats.jsp">Get more Library Statistics</a></p>
</body>
</html>