<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> --%>
<%@ page import="com.poscoict.posledger.assets.model.User_Doc" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
</head>

<body>
<h1><strong>${sessionUser.id}!</strong> Welcome to PosLedger Assets World!</h1>
<!--<h2>Your token is ${accessToken}</h2>-->

<%
    List<User_Doc> docList = (List<User_Doc>)request.getAttribute("docList");
    //User_Doc doc;

    String docid[] = new String[docList.size()];
    String queryDoc="";
    int i=0;
    for(i=0; i<docid.length; i++) {
        docid[i] = "File - <a href=/assets/mydoc?userid=" + docList.get(i).getUserid() + "&docid=" + docList.get(i).getDocid() + ">" + docList.get(i).getDocid() + "</a>";
        queryDoc = "query - <a href=/assets/queryDoc?docid=" + docList.get(i).getDocid() + ">" + docList.get(i).getDocid() + "</a>";
        %>
         <%=docid[i]%><br>
         <%=queryDoc%><br>
<%
    }
%>

<input type="hidden" name="userid" value="${sessionUser.id}">
<%--
<c:forEach items="${docList}" var="docList">
    <a href="">${list}</a>
</c:forEach>
--%>
</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>