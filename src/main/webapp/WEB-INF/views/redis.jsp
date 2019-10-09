
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
</head>

<body>
<h1><strong>${sessionUser.id}!</strong> Welcome to PosLedger Assets World!</h1>
<h2>Your token is ${accessToken}</h2>
<%
    Long count = (Long)request.getAttribute("count");
    %>
<%=count%>


</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>