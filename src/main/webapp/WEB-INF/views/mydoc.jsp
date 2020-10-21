<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
    <link href="bootstrap.min.css" rel="stylesheet" type="text/css"></link>
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

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
                <a href="/assets/main" class="list-group-item">Make signature</a>
                <a href="/assets/mysign?userid=${sessionUser.id}" class="list-group-item">My Signature</a>
                <a href="/assets/addUser" class="list-group-item"l>Upload File</a>
                <a href="/assets/mydoclist?userid=${sessionUser.id}" class="list-group-item">My Document</a>

            </div>
        </div>
        <!-- /.col-lg-3 -->

        <div class="col-lg-9">

            <div class="card mt-4">


                <%--                <img class="card-img-top img-fluid" src="http://placehold.it/900x400" alt="">--%>
                <div class="card-body" align="center">
                    <table width="800" >
                        <tr align="center">
                            <td >
                                <button type="button" class="btn btn-outline-success" onclick="checkInfo(${tokenId})">Document Info</button>
                                <hr>
                            </td>
                        </tr>

                        <tr>
                            <td align="center">
                                <iframe style="width:60%; height:600px;" src="${docPath}"></iframe>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div class="card card-outline-secondary my-4">
                <div class="card-header">

                </div>
                <div class="card-body" align="right">

                    <table width="600">
                        <tr>

                            <td>
                                <input type="text" name="verification" id="verificationId" placeholder="Signature Token Id">
                                <input type="text" name="verification" id="verificationOwner" placeholder="Signature Token Owner">
                                <button type="button" class="btn btn-success" onclick="verification()">verification</button>
                            </td>
                            <td>
                                <form action="/assets/doSign" method="post">
                                    <input type="hidden" name="signer" id="signer" value="${sessionUser.id}">
                                    <input type="hidden" name="docNum" value=${docNum}>
                                    <input type="hidden" name="docId" value=${docId}>
                                    <input type="hidden" name="tokenId" id="tokenId" value=${tokenId}>
                                    <input type="hidden" name="docPath" value=${docPath}>
                                    <input type="hidden" name="sigId" value=${sigId}>

                                    <table>
                                        <tr>
                                            <td align="right">
                                                <input type="submit" class="btn btn-success" value="sign">
                                            </td>
                                        </tr>
                                    </table>
                                </form>

                            </td>
                        </tr>
                    </table>

                    <hr>
                    <input type="text" name="receiverId" id="receiverId" placeholder="ID">
                    <button type="button" class="btn btn-success" onclick="transferFrom()">transferFrom</button>
                    <button type="button" class="btn btn-success" onclick="finalize()">finalize</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
<script>

    function checkInfo(tokenId) {

        $.ajax({
            type: "POST",
            url: "/assets/checkInfo",
            data: {
                "tokenId": tokenId
                //"strImg": dataURL
                //"test": "test string"
            },
            //dataType: "json",
            success: function (data) {
                swal({title: "Info", text: data, icon: "success", button: "close"});
            },
            error: function (err) {
                swal("error" + err);
            }
        });
    }

    function transferFrom() {

        userId = document.getElementById("signer").value;
        tokenId = document.getElementById("tokenId").value;
        receiver = document.getElementById("receiverId").value;

        $.ajax({
            type: "POST",
            url: "/assets/transferFrom",
            data: {
                "userId": userId,
                "tokenId": tokenId,
                "receiverId": receiver
            },
            //dataType: "json",
            success: function (data) {
                swal({title: data, icon: "success", button: "close"});
            },
            error: function (err) {
                swal("error" + err);
            }
        });
    }

    function finalize() {

        tokenId = document.getElementById("tokenId").value;

        $.ajax({
            type: "POST",
            url: "/assets/finalize",
            data: {
                "tokenId": tokenId
            },
            //dataType: "json",
            success: function (data) {
                swal({title: data, icon: "success", button: "close"});
            },
            error: function (err) {
                swal("error" + err);
            }
        });
    }

    function verification() {

        tokenId = document.getElementById("verificationId").value;
        owner = document.getElementById("verificationOwner").value;

        $.ajax({
            type: "POST",
            url: "/assets/verification",
            data: {
                "tokenId": tokenId,
                "owner": owner
            },
            //dataType: "json",
            success: function (data) {
                if(data == 'true')
                    swal({title: "Success", text: "Verification Success", icon: "success", button: "close"});
                else if(data == 'false')
                    swal({title: "Success", text: "Verification Failure", icon: "error", button: "close"});
            },
            error: function (err) {
                swal("error" + err);
            }
        });
    }

</script>

<script src="${ctx}/js/jquery-min.js"></script>
</html>