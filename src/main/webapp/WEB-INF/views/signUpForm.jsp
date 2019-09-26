
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>


<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <link href="index_style.css" rel="stylesheet" type="text/css"></link>
    <!--<link rel="stylesheet" href="//unpkg.com/bootstrap@4/dist/css/bootstrap.min.css">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>-->
    <title>PosLedger Assets Application</title>
</head>

<body>

<div class="wrapper fadeInDown">
    <div id="formContent">
        <!-- Tabs Titles -->

        <div class="fadeIn first">
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcT0uh4h5i5PLv0UDZsykVCyC-2XvVnlQ2IJzxnHVHwO5G9-C4oZ" id="icon" alt="User Icon" />
        </div>


        <!-- Login Form -->
        <form method="post" action="/assets/signUp">
            <input type="text" id="login" class="fadeIn second" name="userId" placeholder="ID">
            <input type="password" id="password" class="fadeIn third" name="userPasswd" placeholder="password"><br>
            <input type="submit" class="fadeIn fourth" value="Register">
        </form>
        <!--
        <form method="post" action="/assets/signup">
            <input type="submit" value="Sign Up">
        </form>
        -->
        <!--
        <form method="post" action="${ctx}/oauth/token">
            Id : <input type="text" name="userId" class="fadeIn second"><br>
            PW : <input type="password" name="userPasswd" class="fadeIn third"><br>
            <input type="submit" value="login">
        </form>
        <form method="post" action="/assets/signup">
            <input type="submit" value="signup">
        </form>
        -->

        <!-- Remind Passowrd -->
        <div id="formFooter">
            <a class="underlineHover" href="/assets/index">Login</a>
            <!--<a class="underlineHover" href="#">Forgot Password?</a>-->
        </div>

    </div>
</div>



</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>
