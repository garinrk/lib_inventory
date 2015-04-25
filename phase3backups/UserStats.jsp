<%@ page language="java" import="cs5530.*" %>
<html>

	<head>
		 <link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	</script>

	</head>

	<h1>Printing User Statistics</h1>


<body>
Please select an option:<br>
<select>
  <option value="">---</option>
  <option value="checkoutmost">Users who have checked out the most books</option>
  <option value="ratedmost">Users who have rated the most number of books</option>
  <option value="lostmost">Users who have lost the most books</option>
</select>
<br>
<br>
How many users are you interested in?<br>
<input type=text name="numberofuseres">
<br>
<br>
<input type=submit value="Get User Stats">




</body>
</html>