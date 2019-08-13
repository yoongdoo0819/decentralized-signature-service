<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
</head>

<body>
<form method="post" action="${ctx}/oauth/token">
    Id : <input type="text" name="userId"><br>
    PW : <input type="password" name="userPasswd"><br>
    <input type="submit" value="login">
</form>
<form method="post" action="/assets/signup">
    <input type="submit" value="signup">
</form>

</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>
