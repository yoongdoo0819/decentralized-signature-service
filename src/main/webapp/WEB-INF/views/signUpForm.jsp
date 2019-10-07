
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>


<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <link href="index_style.css" rel="stylesheet" type="text/css"></link>
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

        <div id="formFooter">
            <a class="underlineHover" href="/assets/index">Login</a>
        </div>

    </div>
</div>



</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>
