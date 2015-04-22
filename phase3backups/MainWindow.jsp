<%@ page language="java" import="cs5530.*" %>
<html>


<h1> Library Database </h2>

Welcome to the library!
<br>
<br>
<br>
Please log in: 


<body>

	<form name="user_search" method=get onsubmit="return check_all_fields(this)" action="orders.jsp">
		<input type=hidden name="searchAttribute" value="login">
		<input type=text name="attributeValue" length=10>
		<input type=submit>
	</form>

<a href="http://georgia.eng.utah.edu:8080/~cs5530u18/NewUser.jsp">Create an account here</a>


<!-- Student Information -->
<footer>

  <p>Created by Garin Richards for
  CS5530 - Database Systems - Spring 2015</p>

</footer>

</body>

</html>