<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

</head>





<body>


	<% String titleattr = request.getParameter("title");

	if(titleattr == null){


	%>

	<form name="AddBookRecord" method=get action="AddRecord.jsp">
		<h1>Adding a Book Record...</h1>
		Title:
		<br>
		<input type=hidden name="title">	
		<input type="text" name="titleValue">
		<br>
		ISBN:
		<br>
		<input type=hidden name="isbn">
		<input type="text" name="isbnValue">
		<br>
		Author(s):<br>
		<textarea name="authorsValue" cols="50" rows="2">author1,author2,author3...
		</textarea>
		<br>
		Summary:<br>
		<textarea name="summaryValue" cols="50" rows="2">Place summary here...
		</textarea>
		<br>
		Publisher:<br>
		<input type=hidden name="publisher">
		<input type="text" name="publisherValue">
		<br>
		Subject:<br>
		<input type=hidden name="subject">
		<input type="text" name="subjectValue">
		<br>
		Format:<br>
		<input type=hidden name="format">
		<input type="text" name="formatValue">
		<br>
		Year Of Publication:<br>
		<input type=hidden name="pubyear">
		<input type="text" name="pubyearValue">
		<br>
		<br>
		<input type=submit value="Add Record">

	</form>

	<% } else {

	String[] authors = request.getParameter("authorsValue").split(",");
	String title = request.getParameter("titleValue");
	String isbn = request.getParameter("isbnValue");
	String summary = request.getParameter("summaryValue");
	String publisher = request.getParameter("publisherValue");
	String subject = request.getParameter("subjectValue");
	String format = request.getParameter("formatValue");
	String pubYear = request.getParameter("pubyearValue");

	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();

	out.println(d.AddBookRecordWeb(isbn,title,publisher,pubYear, format, subject, summary, authors, conn.con));
	conn.closeStatement();
	conn.closeConnection();
}
%>

<BR><a href="AddRecord.jsp">Add another book record</a></p>
</body>
</html>