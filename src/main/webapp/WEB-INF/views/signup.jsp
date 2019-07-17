<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
</head>

<body>
    <form method="post" action="/assets/signupInfo">
        Id : <input type="text" name="userId">
        PW : <input type="password" name="userPasswd">
        <input type="submit" value="signUp">
    </form>
</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>