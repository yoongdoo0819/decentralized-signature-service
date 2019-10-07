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
                <a href="#" class="list-group-item active">Make signature</a>
                <a href="/assets/mysign?userid=${sessionUser.id}" class="list-group-item">My Signature</a>
                <a href="/assets/addUser" class="list-group-item"l>Upload File</a>
                <a href="/assets/mydoclist?userid=${sessionUser.id}" class="list-group-item">My Document</a>
            </div>
        </div>
        <!-- /.col-lg-3 -->

        <div class="col-lg-9">

            <div class="card mt-4">
                <canvas id="myCanvas" style="background-color:aliceblue" width="850" height="400">
                </canvas>

<%--                <img class="card-img-top img-fluid" src="http://placehold.it/900x400" alt="">--%>
                <div class="card-body">
                    <h3 class="card-title">Signature</h3>

                </div>
            </div>

            <div class="card card-outline-secondary my-4">
                <div class="card-header">
                    Store Your Signature
                </div>
                <div class="card-body" align="right">

                    <input type="hidden" id="signer" value="${sessionUser.id}">
                    <input type="submit" class="btn btn-success"  value="store" onclick="store(this)">

                </div>
            </div>
        </div>
    </div>
</div>

</body>

<script >
    var canvas, context;
    function goLogin() {
        location.href = "${ctx}/oauth/login";
    }

    function init() {
        canvas = document.getElementById("myCanvas");
        context = canvas.getContext("2d");

        context.lineWidth = 2; // 선 굵기를 2로 설정
        context.strokeStyle = "blue";

        // 마우스 리스너 등록. e는 MouseEvent 객체
        canvas.addEventListener("mousemove", function (e) { move(e) }, false);
        canvas.addEventListener("mousedown", function (e) { down(e) }, false);
        canvas.addEventListener("mouseup", function (e) { up(e) }, false);
        canvas.addEventListener("mouseout", function (e) { out(e) }, false);
    }

    var startX=0, startY=0; // 드래깅동안, 처음 마우스가 눌러진 좌표
    var drawing=false;
    function draw(curX, curY) {
        context.beginPath();
        context.moveTo(startX, startY);
        context.lineTo(curX, curY);
        context.stroke();
    }
    function down(e) {
        startX = e.offsetX; startY = e.offsetY;
        drawing = true;
    }
    function up(e) { drawing = false; }
    function move(e) {
        if(!drawing) return; // return if mouse is not clicked
        var curX = e.offsetX, curY = e.offsetY;
        draw(curX, curY);
        startX = curX; startY = curY;
    }
    function out(e) { drawing = false; }

    function store(link) {
        //downloadCanvas(this, myCanvas, 'test.png');
        var signer = document.getElementById("signer").value;
        //alert(signer);
        canvas = document.getElementById("myCanvas");
        var dataURL = canvas.toDataURL("image/png", 1.0);//.replace("image/png", "image/octet-stream");
        //var implement = document.getElementById("canvasImg");
        //implement.src = dataURL;
        canvas.getContext("2d").clearRect(0, 0, canvas.width, canvas.height);

        $.ajax({
            type: "POST",
            url: "/assets/img",
            data: {
                "signer": signer,
                "strImg": dataURL
                //"test": "test string"
            },
            //dataType: "json",
            success: function() {
                swal({title: "Success", icon: "success", button: "close",});

            },
            error: function(err) {
                swal("error" + err);
            }
        });

    }
</script>


<script src="${ctx}/js/jquery-min.js"></script>
</html>