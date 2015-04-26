<%@ page language="java" import="cs5530.*" %>
<html>

<head>
 <link rel="stylesheet" type="text/css" href="main.css" />

 <script LANGUAGE="javascript">

 </script>

</head>

<h1>Leave book review...</h1>



<body>

  <%

  String usernameattr = request.getParameter("username");

  if(usernameattr == null){


  %>

  <form name="LeaveReview" method=get onsubmit="return check_all_fields(this)" action="LeaveReview.jsp">
    Username:<BR>
    <input type=hidden name="username">
    <input type=text name="userValue" value="simp123" onFocus="value=''">
    <br>ISBN:<br>
    <input type=hidden name="isbn">
    <input type="text" name="isbnValue" value="1122334455667788" onFocus="value=''">
    <br>Review:<br>
    <textarea name="reviewValue" cols="50" rows="2">Your review here</textarea>
    <br>Score:<br>

    <select name="scoreSelection">
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
    <br><br>
    <input type=submit value="Add Review">
  </form>

  <%
} else {

String usernameval = request.getParameter("userValue");
String isbnval = request.getParameter("isbnValue");
String reviewval = request.getParameter("reviewValue");
String score = request.getParameter("scoreSelection");

cs5530.Connector conn = new Connector();
cs5530.Database d = new Database();


out.println(d.LeaveReviewWeb(usernameval, isbnval, reviewval, score, conn.con));

conn.closeStatement();
conn.closeConnection();



}
%>

<BR><a href="LeaveReview.jsp">Leave Another Review</a></p>
</body>
</html>