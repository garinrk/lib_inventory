<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

</head>



<body>
	<h1>Adding book copy...</h1>

	<% String isbnattr = request.getParameter("isbn");

	if(isbnattr	== null){

	%>
	<form name="AddBookCopy" method=get action="AddCopy.jsp">
		<br>
		ISBN:
		<br>
		<input type=hidden name="isbn">
		<input type="text" name="isbnValue">
		<br>
		Number of copies:
		<br>
		<input type="text" name="copiesValue">
		<br>
		<br>
		<input type=submit value="Add Copies">

	</form>

	<% } else {
	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();
	String isbn = request.getParameter("isbnValue");
	String amount = request.getParameter("copiesValue");


	out.println(d.AddBookCopyWeb(isbn, amount, conn.con));



	conn.closeStatement();
	conn.closeConnection();	
	}
	%>

<BR><a href="AddCopy.jsp">Add another copy of a book</a></p>
</body>
</html>