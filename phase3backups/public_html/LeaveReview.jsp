<%@ page language="java" import="cs5530.*" %>
<html>

<!-- 

Author: Garin Richards
For Phase 3 of Semester Project
CS 5530 - Database Systems - University of Utah
Spring 2015

-->

<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
 
  <link rel="stylesheet" type="text/css" href="bootstrap.css" />
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>

<body>

  <%

  String usernameattr = request.getParameter("username");

  if(usernameattr == null){

  %>
  <div class="jumbotron">
    <h1 class="text-center">Add Review</h1>
  </div>

  <form name="LeaveReview" method=get action="LeaveReview.jsp">
    Username:<BR>
    <input type=hidden class="form-control" name="username">
    <input type=text class="form-control" name="userValue">
    <br>ISBN:<br>
    <input type=hidden class="form-control" name="isbn">
    <input type=text class="form-control" name="isbnValue">
    <br>Review:<br>
    <textarea name="reviewValue" class="form-control"  cols="50" rows="2"></textarea>
    <br>Score:<br>

    <select class="form-control" name="scoreSelection">
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
    <input type=submit class="btn btn-info" value="Leave Review">
  </form>

  <%
} else {

String usernameval = request.getParameter("userValue");
String isbnval = request.getParameter("isbnValue");
String reviewval = request.getParameter("reviewValue");
String score = request.getParameter("scoreSelection");

if(usernameval != "" && isbnval != "" && reviewval != "")
{


cs5530.Connector conn = new Connector();

out.println(cs5530.Database.LeaveReviewWeb(usernameval, isbnval, reviewval, score, conn.con));

conn.closeStatement();
conn.closeConnection();
}
else
{
  out.println("<BR><BR><h3>No empty fields, please try again</h3>");
}
}
%>

<a href="LeaveReview.jsp" class="btn btn-primary" role="button">Leave Another Review</a>
  <div class="text-center"> 
    <BR><a class="btn btn-success" href="index.html">Library Home</a></p>
    </div>
</body>
</html>