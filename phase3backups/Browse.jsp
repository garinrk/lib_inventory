<%@ page language="java" import="cs5530.*" %>
<html>

	<head>
		 <link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

	</head>

	<h1>Browse Library</h1>



<body>
Title Word(s):<br>
<textarea name="titlewords" cols="50" rows="2">
titleword1,titleword2,titleword3...
</textarea>
<br>
Author(s)
<br>	

<textarea name="authorstosearchfor" cols="50" rows="2">
author1,author2,author3...
</textarea>
<br>
Publisher:<br>
	<input type="text" name="publisher">
<br>
Subject:<br>
	<input type="text" name="subject">
<br>


<br><br>
Only list...<br>
<INPUT TYPE=RADIO NAME="availability" VALUE="availablebooks">Books that are available for checkout<BR>
<INPUT TYPE=RADIO NAME="availability" VALUE="allbooks">All books in the library record system<BR>
<br>
Sort by...<br>
<INPUT TYPE=RADIO NAME="sort" VALUE="pubyear">Year published<BR>
<INPUT TYPE=RADIO NAME="sort" VALUE="allbooks">Average review score<BR>
<INPUT TYPE=RADIO NAME="sort" VALUE="allbooks">Popularity<BR>
<br>
<input type=submit value="Search Library">
</body>
</html>