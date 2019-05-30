<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
<title>PosLedger Assets Application</title>
<script>
function goLogin() {
    location.href = "${ctx}/oauth/login";
}
</script>
</head>

<body>
    <h1>Welcome!!</h1>
    <button type="button" onclick="goLogin();">로그인하러 가기</button>
</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>