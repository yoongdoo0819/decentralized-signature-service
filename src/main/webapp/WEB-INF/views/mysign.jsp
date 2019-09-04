<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>

    <link href="bootstrap.min.css" rel="stylesheet" type="text/css"></link>

    <!-- Custom styles for this template -->
    <link href="shop-item.css" rel="stylesheet">
</head>

<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
        <a class="navbar-brand" href="#">POSTECH</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="#">Home
                        <span class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/assets/index">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Services</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Contact</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">

    <div class="row">

        <div class="col-lg-3">
            <h1 class="my-4">Signature Service</h1>
            <div class="list-group">
                <a href="/assets/main" class="list-group-item">Make a signature</a>
                <a href="#" class="list-group-item active">My Signature</a>
                <a href="/assets/mydoclist?userid=${sessionUser.id}" class="list-group-item">My Document</a>
                <a href="/assets/addUser" class="list-group-item"l>Upload File</a>
            </div>
        </div>

        <div class="col-lg-9">
            <!-- /.card -->

            <div class="card card-outline-secondary my-4">
                <div class="card-header">
                    <h1>${sessionUser.id}'s Signatures</h1>
                </div>
                <div class="card-body">
                    <%
                        String pathList[] = (String[])request.getAttribute("path");
                        //User_Doc doc;

                        String imgSrc[] = new String[pathList.length];

                        int i=0;
                        for(i=0; i<pathList.length; i++) {
                            imgSrc[i] = "<img src=" + pathList[i] + ">";

                    %>
                    <%=imgSrc[i]%><br><hr>
                    <%
                        }
                    %>
<%--                    <hr>--%>
<%--                    <a href="#" class="btn btn-success">Leave a Review</a>--%>
                </div>
            </div>
    <!-- /.card -->
        </div>
    </div>
</div>
<!--<h2>Your token is ${accessToken}</h2>-->
<!--<img src="${sigId}" width="150"/>-->


<%--<%--%>
<%--    String sigid[] = (String[])request.getAttribute("sigId");--%>
<%--    //User_Doc doc;--%>

<%--    String imgSrc[] = new String[sigid.length];--%>

<%--    int i=0;--%>
<%--    for(i=0; i<sigid.length; i++) {--%>
<%--        imgSrc[i] = "<img src=" + sigid[i] + ">";--%>

<%--%>--%>
<%--<%=imgSrc[i]%><br>--%>
<%--<%--%>
<%--    }--%>
<%--%>--%>

</body>

<script src="${ctx}/js/jquery-min.js"></script>
</html>