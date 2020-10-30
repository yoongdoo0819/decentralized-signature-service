<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="../../WEB-INF/views/common/taglibs.jsp" %> -->
<%-- <%@ include file="/src/main/webapp/js/jquery-min.js" %>--%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
    <meta charset="UTF-8">

    <link href="bootstrap.min.css" rel="stylesheet" type="text/css"></link>
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <!-- Custom styles for this template -->
    <link href="shop-item.css" rel="stylesheet">
</head>
<body onload="init()">


<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
        <a class="navbar-brand" href="#">POSTECH</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/assets/main">Home
                        <span class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/assets/index">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/assets/admin">Token Type Management</a>
                </li>

            </ul>
        </div>
    </div>
</nav>

<div class="container">

    <div class="row">

        <div class="col-lg-3">
            <h1 class="my-4">Token Type</h1>
            <div class="list-group">
                <a href="/assets/admin" class="list-group-item ">토큰 타입 등록</a>
                <a href="/assets/adminTokenTypesOf" class="list-group-item">전체 토큰 타입 목록 조회</a>
                <a href="/assets/adminUpdateTokenType" class="list-group-item"l>토큰 타입 수정</a>
                <a href="/assets/adminRetrieveTokenType" class="list-group-item">토큰 타입 조회</a>
                <a href="/assets/adminEnrollAttributeOfTokenType" class="list-group-item">토큰 타입 속성 등록</a>
                <a href="/assets/adminUpdateAttributeOfTokenType" class="list-group-item">토큰 타입 속성 수정</a>
                <a href="#" class="list-group-item active">토큰 타입 속성 조회</a>
                <a href="/assets/adminDropAttributeTokenType" class="list-group-item">토큰 타입 속성 삭제</a>
                <a href="/assets/adminDropTokenType" class="list-group-item">토큰 타입 삭제</a>
            </div>
        </div>
        <div class="card card-outline-secondary my-4">
            <div class="card-header">
                <h1>Retrieve Attribute Of Token Type </h1>



                <%--                    <input type="text" id="pages" class="fadeIn third" name="pages" placeholder="pages"><br>--%>
                <%--                    <input type="text" id="signers" class="fadeIn second" name="signers" placeholder="signers"><br>--%>
                <%--                    <input type="text" id="signatures" class="fadeIn third" name="signatures" placeholder="signatures"><br>--%>
                <%--                    <input type="text" id="tokenType" class="fadeIn third" name="tokenType" placeholder="tokenType"><br>--%>
                <div align="right">
                    <input name="addButton" class="btn btn-success" type="button" style="cursor:hand" onClick="insRowForXAttr()" value="Add XAttr">
                </div>
            </div>
            <div class="card-body" algin="right">
                <table width="400" border="0" cellspacing="0" cellpadding="0">
                    <%--                        <tr>--%>
                    <%--                            <td colspan="2" align="left" bgcolor="#FFFFFF">--%>
                    <%--                                <table width="100%" border="0" cellpadding="0" cellspacing="0">--%>

                    <tr>
                        <td height="25">
                            <table id="addTable" width="700" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" border="0">
                                <input type=text name="xattrName" id="xattrName" style="width:200px; height:20px"; placeholder='xattrName'> &nbsp

                            </table>
                        </td>
                    </tr>
                    <tr>

                    </tr>
                    <%--                            </td>--%>
                    <%--                        </tr>--%>
                </table>
                <input type="hidden" id="ownerKey" name="ownerKey" value="${sessionUser.id}">
                <input type="hidden" id="xattrCount" name="xattrCount">
                <input type="hidden" id="uriCount" name="uriCount">
                <table width="780">
                    <tr>
                        <td>
                            token Type <input type="text" name="tokenType" id="tokenType" class="btn-outline-info">
                        </td>
                        <td align="right">
                            <input type="submit" class="btn btn-success" value="submit" onclick="retrieveAttributeOfTokenType()">
                        </td>
                    </tr>
                </table>
                <hr>
            </div>

        </div>
    </div>
</div>

</body>

<script >
    function retrieveAttributeOfTokenType() {

        ownerKey = document.getElementById("ownerKey").value;
        xattrName = document.getElementById("xattrName").value;
        tokenType = document.getElementById("tokenType").value;

        $.ajax({
            type: "POST",
            url: "/assets/retrieveAttributeOfTokenType",
            data: {
                "ownerKey" : ownerKey,
                "xattrName" : xattrName,
                "tokenType" : tokenType
            },
            //dataType: "json",
            success: function(data) {
                swal({text: data, icon: "success", button: "close"});

            },
            error: function(err) {
                swal("error" + err);
            }
        });
    }
</script>


<script src="${ctx}/js/jquery-min.js"></script>
</html>