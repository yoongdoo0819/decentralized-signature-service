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


</body>


<script type="text/javascript">

var oTbl;
var count = 0;
//var ID = 'ID';
//var PHONE = 'PHONE';

// add Row
function insRow() {

    var ID = 'ID'+count;
    var PHONE = 'PHONE'+count;
    oTbl = document.getElementById("addTable");
    var oRow = oTbl.insertRow();
    oRow.onmouseover=function(){oTbl.clickedRowIndex=this.rowIndex};
    var oCell = oRow.insertCell();

    var frmTag = "<input type=text name=" + "'" + ID + "'" + "style=width:200px; height:20px; placeholder='ID'>";
    //frmTag += "PHONE <input type=text name=" + "'" + PHONE + "'" + "style=width:200px; height:20px;>";
    frmTag += " <button type=button style='width:45pt; height:25pt' class='btn btn-outline-danger' onClick=remove()>삭제</button>"
    frmTag += "<br><hr>"
    oCell.innerHTML = frmTag;
    count++;

    document.getElementById("count").value = String(count);
}

//Row 삭제
function removeRow() {
    oTbl.deleteRow(oTbl.clickedRowIndex);
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
//-->
</script>
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

<!-- Page Content -->
<div class="container">

    <div class="row">

        <div class="col-lg-3">
            <h1 class="my-4">Signature Service</h1>
            <div class="list-group">
                <a href="/assets/main" class="list-group-item ">Make signature</a>
                <a href="/assets/mysign?userid=${sessionUser.id}" class="list-group-item">My Signature</a>
                <a href="#" class="list-group-item active"l>Upload File</a>
                <a href="/assets/mydoclist?userid=${sessionUser.id}" class="list-group-item">My Document</a>
            </div>
        </div>
        <!-- /.col-lg-3 -->

        <div class="col-lg-9">

            <!-- /.card -->

            <div class="card card-outline-secondary my-4">
                <div class="card-header">
                    <h1>Add user and Upload file</h1>
                    <div align="right">
                    <input name="addButton" class="btn btn-success" type="button" style="cursor:hand" onClick="insRow()" value="Add User">
<%--                    <font color="#FF0000">*</font>Add user--%>
                    </div>
                </div>
                <div class="card-body" algin="right">
                    <form action="/assets/upload" method="post" enctype="multipart/form-data">
                        <table width="400" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td colspan="2" align="left" bgcolor="#FFFFFF">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">

                                        <tr>
                                            <td height="25">
                                                <table id="addTable" width="600" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" border="0">

                                                </table></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                        <input type="hidden" name="userid" value="${sessionUser.id}">
                        <input type="hidden" id="count" name="count">
                        <table width="780">
                            <tr>
                                <td>
                                    <input type="file" name="file" class="btn-outline-info">
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
            <!-- /.card -->

        </div>
        <!-- /.col-lg-9 -->

    </div>

</div>

</body>


<script src="${ctx}/js/jquery-min.js"></script>
</html>