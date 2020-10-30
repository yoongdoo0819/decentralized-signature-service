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
                <a href="#" class="list-group-item active">토큰 타입 수정</a>
                <a href="/assets/adminRetrieveTokenType" class="list-group-item">토큰 타입 조회</a>
                <a href="/assets/adminEnrollAttributeOfTokenType" class="list-group-item">토큰 타입 속성 등록</a>
                <a href="/assets/adminUpdateAttributeOfTokenType" class="list-group-item">토큰 타입 속성 수정</a>
                <a href="/assets/adminRetrieveAttributeOfTokenType" class="list-group-item"l>토큰 타입 속성 조회</a>
                <a href="/assets/adminDropAttributeTokenType" class="list-group-item">토큰 타입 속성 삭제</a>
                <a href="/assets/adminDropTokenType" class="list-group-item">토큰 타입 삭제</a>
            </div>
        </div>
        <div class="card card-outline-secondary my-4">
            <div class="card-header">
                <h1>Update Token Type </h1>
                <div align="right">
                    <input name="addButton" class="btn btn-success" type="button" style="cursor:hand" onClick="insRowForXAttr()" value="Add XAttr">
                </div>
            </div>
            <div class="card-body" algin="right">
                <form action="/updateTokenType" method="post" enctype="multipart/form-data">
                    <table width="400" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="25">
                                <table id="addTable" width="400" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" border="0">

                                </table>
                            </td>

                        </tr>
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
                                <input type="submit" class="btn btn-success" value="submit">
                            </td>
                        </tr>
                    </table>
                </form>
                <hr>
                <%--                    <a href="#" class="btn btn-success">Leave a Review</a>--%>
            </div>
        </div>
    </div>
</div>

</body>

<script >
    var oTbl;
    var oTbl2;
    var xattrCount = 0;
    var uriCount = 0;

    function insRowForXAttr() {

        var xattrName = 'xattrName'+xattrCount;
        var xattrValue = 'xattrValue'+xattrCount;
        var xattrType = 'xattrType'+xattrCount;
        oTbl = document.getElementById("addTable");
        var oRow = oTbl.insertRow();
        oRow.onmouseover=function(){oTbl.clickedRowIndex=this.rowIndex};
        var oCell = oRow.insertCell();


        frmTag = "<input type=text name=" + "'" + xattrName + "'" + "style=width:200px; height:20px; placeholder='Name'>";
        frmTag += " <input type=text name=" + "'" + xattrValue + "'" + "style=width:200px; height:20px; placeholder='Initial Value'>";
        frmTag += " <select name=" + "'" + xattrType + "'" + ">\n" +
            "                    <option value=\"\">Data Type</option>\n" +
            "                    <option value=\"String\" selected=\"selected\">String</option>\n" +
            "                    <option value=\"String[]\">String[]</option>\n" +
            "                    <option value=\"Integer\">Integer</option>\n" +
            "                    <option value=\"Boolean\">Boolean</option>\n" +
            "                </select>"
        frmTag += " <button type=button style='width:45pt; height:25pt' class='btn btn-outline-danger' onClick=removeRowForXAttr()>삭제</button>"
        frmTag += "<br><hr>"
        oCell.innerHTML = frmTag;
        xattrCount++;

        document.getElementById("xattrCount").value = String(xattrCount);
    }

    function insRowForURI() {

        var uriName = 'uriName'+uriCount;
        var uriType = 'uriType'+uriCount;
        oTbl2 = document.getElementById("addTable2");
        var oRow2 = oTbl2.insertRow();
        oRow2.onmouseover=function(){oTbl2.clickedRowIndex=this.rowIndex};
        var oCell2 = oRow2.insertCell();

        var frmTag = "<input type=text name=" + "'" + uriName + "'" + "style=width:200px; height:20px; placeholder='ID'>";
        frmTag += " <select name=" + "'" + xattrType + "'" + ">\n" +
            "                    <option value= " + "'\'" + ">Data Type</option>\n" +
            "                    <option value= " + "'String'" + ">String</option>\n" +
            "                    <option value= " + "'[String]'" + "selected=selected >[String]</option>\n" +
            "                    <option value= " + "'Boolean'" + ">Boolean</option>\n" +
            "                    <option value= " + "'Integer'" + ">Integer</option>\n" +
            "                </select>"
        frmTag += " <button type=button style='width:45pt; height:25pt' class='btn btn-outline-danger' onClick=removeRowForURI()>삭제</button>"
        frmTag += "<br><hr>"
        oCell2.innerHTML = frmTag;
        uriCount++;

        document.getElementById("uriCount").value = String(uriCount);
    }

    //Row 삭제
    function removeRowForXAttr() {
        oTbl.deleteRow(oTbl.clickedRowIndex);
    }

    function removeRowForURI() {
        oTbl2.deleteRow(oTbl2.clickedRowIndex);
    }

    function frmCheck() {
        var frm = document.form;

        for( var i = 0; i <= frm.elements.length - 1; i++ ) {
            if( frm.elements[i].name == "addText"+i ) {
                if( !frm.elements[i].value ) {
                    alert("write in textbox");
                    frm.elements[i].focus();
                    return;
                }
            }
        }
    }

    function store(link) {

        hash = document.getElementById("hash").value;
        pages = document.getElementById("pages").value;
        signers = document.getElementById("signers").value;
        signatures = document.getElementById("signatures").value;
        tokenType = document.getElementById("tokenType").value;

        $.ajax({
            type: "POST",
            url: "/enrollTokenType",
            data: {
                "hash":  hash,
                "pages": pages,
                "signers": signers,
                "signatures": signatures,
                "tokenType" : tokenType
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