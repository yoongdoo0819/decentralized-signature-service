<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
</head>

<body>
<h1><strong>${sessionUser.id}!</strong> Welcome to PosLedger Assets World!</h1>
<iframe style="width:80%; height:800px;" src="${docId}"></iframe>
</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>