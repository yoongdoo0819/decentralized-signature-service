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
            <img src="https://t1.daumcdn.net/cfile/tistory/194B6A384F6922CB11" id="icon" alt="User Icon" />
        </div>


        <!-- Login Form -->
        <form method="post" action="${ctx}/oauth/token">
            <input type="text" id="login" class="fadeIn second" name="userId" placeholder="ID">
            <input type="password" id="password" class="fadeIn third" name="userPasswd" placeholder="password"><br>
            <input type="submit" class="fadeIn fourth" value="Log In">
        </form>

        <div id="formFooter">
            <a class="underlineHover" href="/assets/signUpForm">Sign Up</a>
        </div>

    </div>
</div>



</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>
