<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="../../WEB-INF/views/common/taglibs.jsp" %> -->
<%-- <%@ include file="/src/main/webapp/js/jquery-min.js" %>--%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
    <meta charset="UTF-8">
</head>
<body onload="init()">
<h3>PosLedger</h3>
<hr>
<h3>Sign service</h3>
<%--<form method="post" action="/assets/img">
    <input type="text" id="signer">
    <input type="submit" value="저장" onclick="store(this)">
</form>--%>


<canvas id="myCanvas" style="background-color:aliceblue" width="400" height="300">
</canvas>
<img id="canvasImg" alt="마우스 오른쪽 버튼 클릭"><br>
<input type="hidden" id="signer" value="${sessionUser.id}">
<input type="submit" value="저장" onclick="store(this)">
<hr>
<h3>Sign service</h3>
<form action="/assets/upload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="userid" value="${sessionUser.id}">
    <input type="file" name="file">
    <input type="submit" value="submit">
</form>
<a href="/assets/mysign?userid=${sessionUser.id}">confirm my sign</a>
<a href="/assets/mydoc?userid=${sessionUser.id}">confirm my doc</a>



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
        if(!drawing) return; // 마우스가 눌러지지 않았으면 리턴
        var curX = e.offsetX, curY = e.offsetY;
        draw(curX, curY);
        startX = curX; startY = curY;
    }
    function out(e) { drawing = false; }

    function store(link) {
        //downloadCanvas(this, myCanvas, 'test.png');
        var signer = document.getElementById("signer").value;
        alert(signer);
        canvas = document.getElementById("myCanvas");
        var dataURL = canvas.toDataURL("image/png", 1.0);//.replace("image/png", "image/octet-stream");
        var implement = document.getElementById("canvasImg");
        implement.src = dataURL;

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
                alert("success");
            },
            error: function(err) {
                alert("error" + err);
            }
        });


        //<a href=dataURL download="test.png">download</a>
        //window.location.download = 'test.png';
        // window.location.href = dataURL;

        //link.href = dataURL;
        //link.download = 'test.png';

        //alert('store');

        /*
                var saveToFile = function (path) {
                    //var canvas = document.getElementById('canvas'),
                    //  data = canvas.toDataURL('image/png'),
                    localFolder = Windows.Storage.ApplicationData.current.localFolder,
                        encodeData = dataURL.replace("dataURL:image/png;base64,", ""),
                        decode = Windows.Security.Cryptography.CryptographicBuffer.decodeFromBase64String(encodeData);

                    // mySample.txt의 이름으로 파일을 생성하고 동일한 이름이 있을 경우, 덮어쓴다.
                    localFolder.createFileAsync(path, Windows.Storage.CreationCollisionOption.replaceExisting)
                        .then(function (file) {
                            // writeTextAsync메소들 통해 파일에 텍스트를 쓴다.
                            return Windows.Storage.FileIO.writeBufferAsync(file, decode);
                            alert('success');
                        });
                };
        */
        //alert('fail');
    }
</script>


<script src="${ctx}/js/jquery-min.js"></script>
</html>