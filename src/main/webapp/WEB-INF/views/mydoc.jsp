<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
</head>

<body>
<h1><strong>${sessionUser.id}!</strong> Welcome to PosLedger Assets World!</h1>
<iframe style="width:60%; height:600px;" src="${docPath}"></iframe>
<img src="${sigId}" width="150"/>
</body>

<form action="/assets/itext" method="post">
    <input type="hidden" name="signer" value="${sessionUser.id}">
    <!--<input type="hidden" name="docId" value=${docId}>-->
    <input type="hidden" name="docPath" value=${docPath}>
    <input type="text" name="sigId" value=${sigId}>

    <input type="submit" value="sign">
</form>
<script src="${ctx}/js/jquery-min.js"></script>
</html>