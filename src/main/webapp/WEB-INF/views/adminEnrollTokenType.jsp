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
                <a href="#" class="list-group-item active">토큰 타입 등록</a>
                <a href="/assets/adminTokenTypesOf" class="list-group-item">전체 토큰 타입 목록 조회</a>
                <a href="/assets/adminUpdateTokenType" class="list-group-item"l>토큰 타입 수정</a>
                <a href="/assets/adminRetrieveTokenType" class="list-group-item">토큰 타입 조회</a>
                <a href="/assets/adminEnrollAttributeOfTokenType" class="list-group-item">토큰 타입 속성 등록</a>
                <a href="/assets/adminUpdateAttributeOfTokenType" class="list-group-item">토큰 타입 속성 수정</a>
                <a href="/assets/adminRetrieveAttributeOfTokenType" class="list-group-item"l>토큰 타입 속성 조회</a>
                <a href="/assets/adminDropAttributeTokenType" class="list-group-item">토큰 타입 속성 삭제</a>
                <a href="/assets/adminDropTokenType" class="list-group-item">토큰 타입 삭제</a>
            </div>
        </div>
        <!-- /.col-lg-3 -->

        <div class="col-lg-9">
            <div class="card card-outline-secondary my-4">
                <div class="card-header">
                    <h1>${sessionUser}'s Document List</h1>
                </div>
                <div class="card-body">
                    <%
                        //List<User_Doc> docList = (List<User_Doc>)request.getAttribute("docList");
                        //User_Doc doc;

                        String docList[] = (String[])request.getAttribute("docIdList");
                        String docPathList[] = (String[])request.getAttribute("docPathList");
                        String docNum[] = (String[])request.getAttribute("docNumList");
                        String tokenId[] = (String[])request.getAttribute("tokenIdList");
                        String sigStatus[] = (String[])request.getAttribute("sigStatus");
                        String ownerKey = (String)request.getAttribute("ownerKey");
                        String token="";
                        String sigProcess="";
                        String docid[] = new String[docList.length];

                        String queryDoc="";
                        int i=0;
                        for(i=0; i<docid.length; i++) {
                            docid[i] = "<a href=/mydoc?ownerKey=" + ownerKey + "&docid=" + docList[i] + "&docnum=" + docNum[i] + "&tokenid=" + tokenId[i] +">" + docPathList[i] + "</a>";
                            queryDoc = "<a href=/queryDoc?docid=" + docList[i] + "&docnum=" + docNum[i] + "&tokenid=" + tokenId[i] + ">" + "- Final Document " + "</a>";
                            if(sigStatus[i].equals("true"))
                                sigProcess= " <button type='button' class='btn btn-success'  style='width: 30pt; height:28pt; float:right;' onclick=checkStatus("+tokenId[i]+")>O</button> ";
                            else
                                sigProcess= " <button type='button' class='btn btn-danger'  style='width: 30pt; height:28pt; float:right;' onclick=checkStatus("+tokenId[i]+")>X</button> ";
                            token = " <input type=submit value='√' class='btn btn-outline-info' style='background-image:url(https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSY5Mu3vrHZi-N1ntwu6F0lTYc2IQekwho9WjK1gl5s_BxWwhI); style='width: 2pt; height:20pt; float:right;' onclick=checkStatus("+tokenId[i]+")> ";

                    %>
                    <table width="750px">
                        <tr>
                            <td>
                                <%=docid[i]%>
                                <%=queryDoc%>
                            </td>
                            <td align="right">
                                <%--<%=token%>&nbsp--%>
                                <%=sigProcess%>
                            </td>
                        </tr>
                    </table>
                    <hr>
                    <%
                        }
                    %>
                    <hr>
                    <%--                    <a href="#" class="btn btn-success">Leave a Review</a>--%>
                </div>
            </div>
            <!-- /.card -->

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
        var owner = document.getElementById("signer").value;
        //alert(signer);
        canvas = document.getElementById("myCanvas");
        var dataURL = canvas.toDataURL("image/png", 1.0);//.replace("image/png", "image/octet-stream");
        //var implement = document.getElementById("canvasImg");
        //implement.src = dataURL;
        canvas.getContext("2d").clearRect(0, 0, canvas.width, canvas.height);

        $.ajax({
            type: "POST",
            url: "/img",
            data: {
                "owner":  owner,
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