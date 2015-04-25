<%@ page language="java" import="cs5530.*" %>
<html>

	<head>
		 <link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

	</head>

	<h1>Adding a Book Record...</h1>



<body>
Title:
<br>	
<input type="text" name="booktitle">
<br>
ISBN:
<br>	
<input type="text" name="isbn">
<br>
Author(s):<br>
<textarea name="authorstosearchfor" cols="50" rows="2">
author1,author2,author3...
</textarea>
<br>
Summary:<br>
<textarea name="booksummary" cols="50" rows="2">
Place summary here...
</textarea>
<br>
Publisher:<br>
	<input type="text" name="publisher">
<br>
Subject:<br>
	<input type="text" name="subject">
<br>
Format:<br>
	<input type="text" name="format">
<br>
Year Of Publication:<br>
	<input type="text" name="pubyear">
<br>
<br>
<input type=submit value="Add Record">
</body>
</html>