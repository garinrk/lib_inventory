<%@ page language="java" import="cs5530.*" %>
<html>

<head>
	<link rel="stylesheet" type="text/css" href="main.css" />

	<script LANGUAGE="javascript">

	function check_all_fields(form_obj){
		alert(form_obj.searchAttribute.value+"='"+form_obj.attributeValue.value+"'");
		if( form_obj.attributeValue.value == ""){
			alert("Search field should be nonempty");
			return false;
		}
		return true;
	}

	</script>

</head>

<h1>Print user record</h1>


<body>
<!-- Please enter a valid username:
<br>	<input type="text" name="fullname">
<br>
<br> -->
<!-- <input type=submit value="Get User Record"> -->

Form1: Search orders on user name:
<form name="user_search" method=get onsubmit="return check_all_fields(this)" action="orders.jsp">
	<input type=hidden name="searchAttribute" value="login">
	<input type=text name="attributeValue" length=10>
	<input type=submit>
</form>




</body>
</html>