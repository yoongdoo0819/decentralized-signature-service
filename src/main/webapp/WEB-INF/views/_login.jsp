<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
<title>PosLedger Assets Application</title>
<script type="text/javascript">
function login(id) {
    $("#userId").val(id);
    $("#_loginForm").submit();
}
</script>
</head>

<body>
    <form id="_loginForm" name="" method="post" action="${ctx}/oauth/token">
    <input type="text" id="userId" name="userId" />
    </form>
    <h1>Login PosLedger Assets</h1>
    <c:forEach items='${userList}' var="user" varStatus="loop">
        <h2>ID : ${user.id}, NAME : ${user.name}</h2>&nbsp;<button type="button" onclick="login('${user.name}');">로그인</button><br/>
    </c:forEach>
</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>