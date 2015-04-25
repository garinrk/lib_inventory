<%@ page language="java" import="cs5530.*" %>
<html>

	<head>
		 <link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

	</head>

	<h1>Leave book review...</h1>



<body>
Username:
<br>	
<input type="text" name="username">
<br>
ISBN:
<br>	
<input type="text" name="isbn">
<br>
Review:<br>
<textarea name="authorstosearchfor" cols="50" rows="2">
author1,author2,author3...
</textarea>
<br>
Score:<br>

<select>
  <option value="">---</option>
  <option value="1">1</option>
  <option value="2">2</option>
  <option value="3">3</option>
  <option value="4">4</option>
  <option value="5">5</option>
  <option value="6">6</option>
  <option value="7">7</option>
  <option value="8">8</option>
  <option value="9">9</option>
  <option value="10">10</option>
</select>
<br>
<br>
<input type=submit value="Add Review">
</body>
</html>