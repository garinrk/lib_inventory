<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

</head>

<h1>Check Late List</h1>



<body>
	<% String monthattr = request.getParameter("month");

	if(monthattr == null){


	%>

	<form name="CheckLateList" method=get action="CheckLateList.jsp">
		Date [MM/DD/YYYY]:
		<input type=hidden name="month">
		<input type="text" name="monthValue">
		/ 
		<input type="text" name="dayValue"> / 
		<input type="text" name="yearValue">
		<br>
		<br>
		<input type=submit value="Get List of late books">
	</form>

	<% } else {

	String month = request.getParameter("monthValue");
	String day = request.getParameter("dayValue");
	String year = request.getParameter("yearValue");

	String lateDate = year + "-" + month + "-" + day;

	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();

	out.println(d.CheckLateListWeb(lateDate, conn.con));
	conn.closeStatement();
	conn.closeConnection();
}
%>
<BR><a href="AddRecord.jsp">Check another date</a></p>
</body>
</html>