<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

</head>




<body>

	<%

	String isbnattr = request.getParameter("isbn");

	if(isbnattr == null){


	%>

	<h1>Print Book Record...</h1>

	<form name="GetRecord" method=get onsubmit="return check_all_fields(this)" action="BookRecord.jsp">
		<br>ISBN:<br>
		<input type=hidden name="isbn">
		<input type="text" name="isbnValue" value="1122334455667788" onFocus="value=''">

		<BR><input type=submit value="Get Book Record">
	</form>


	<%
} else {


String isbnval = request.getParameter("isbnValue");


cs5530.Connector conn = new Connector();
cs5530.Database d = new Database();

out.println(d.PrintBookRecordWeb(isbnval, conn.con));

conn.closeStatement();
conn.closeConnection();



}
%>

<a href="BookRecord.jsp">Print Another Book Record</a></p>
</body>
</html>