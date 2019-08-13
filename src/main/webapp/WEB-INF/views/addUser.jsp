<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- <%@ include file="common/taglibs.jsp" %> --%>

<!DOCTYPE html>
<html lang="ko" class="high">
<head>
    <title>PosLedger Assets Application</title>
</head>

<body>
<h1><strong>${sessionUser.id}!</strong> Welcome to PosLedger Assets World!</h1>

</body>


<script type="text/javascript">

    /*
    function add_item(){
        // pre_set 에 있는 내용을 읽어와서 처리..
        var div = document.createElement('div');
        div.innerHTML = document.getElementById('pre_set').innerHTML;
        document.getElementById('field').appendChild(div);
    }

    function remove_item(obj){
        // obj.parentNode 를 이용하여 삭제
        document.getElementById('field').removeChild(obj.parentNode);
    }
    */
var oTbl;
var count = 0;
//var ID = 'ID';
//var PHONE = 'PHONE';

//Row 추가
function insRow() {

var ID = 'ID'+count;
var PHONE = 'PHONE'+count;
oTbl = document.getElementById("addTable");
var oRow = oTbl.insertRow();
oRow.onmouseover=function(){oTbl.clickedRowIndex=this.rowIndex}; //clickedRowIndex - 클릭한 Row의 위치를 확인;
var oCell = oRow.insertCell();

//삽입될 Form Tag
var frmTag = "ID <input type=text name=" + "'" + ID + "'" + "style=width:200px; height:20px;>";
//frmTag += "PHONE <input type=text name=" + "'" + PHONE + "'" + "style=width:200px; height:20px;>";
frmTag += "<input type=button value='삭제' onClick='removeRow()' style='cursor:hand'>";
oCell.innerHTML = frmTag;
count++;

document.getElementById("count").value = String(count);
}

//Row 삭제
function removeRow() {
oTbl.deleteRow(oTbl.clickedRowIndex);
}

function frmCheck()
{
var frm = document.form;

for( var i = 0; i <= frm.elements.length - 1; i++ ){
if( frm.elements[i].name == "addText"+i )
{
if( !frm.elements[i].value ){
alert("텍스트박스에 값을 입력하세요!");
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
<form action="/assets/upload" method="post" enctype="multipart/form-data">
    <table width="400" border="0" cellspacing="0" cellpadding="0">
    <tr>
    <td colspan="2" align="left" bgcolor="#FFFFFF">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
    <td colspan="5" bgcolor="#FFFFFF" height="25" align="left">
    <input name="addButton" type="button" style="cursor:hand" onClick="insRow()" value="추가">
    <font color="#FF0000">*</font>추가버튼을 클릭해 보세요.</td>
</tr>
<tr>
<td height="25">
    <table id="addTable" width="600" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" border="0">
    <!--<tr>
    <td><input type="text" name="addText" style="width:350px; height:20px;"></td>
    <td align="left"></td>
    </tr>-->
    </table></td>
</tr>
</table>
</td>
</tr>
</table>
<hr>
    <input type="hidden" name="userid" value="${sessionUser.id}">
    <input type="hidden" id="count" name="count">
    <input type="file" name="file">
    <input type="submit" value="submit">
</form>
</body>
    <!--
<table width="400" border="0" cellspacing="0" cellpadding="0">
    <tr>
    <td height="10">
    </td>
    </tr>
    <tr>
    <td align="center">
    <input type="button" name="button" value="확인"> onClick="frmCheck();">
    </td>
    </tr>
    </table>
    </form>
    </body>
    -->

<!--
<body>

<div id="pre_set" style="display:none">
    ID <input type="text" name="" value="" style="width:200px"> Phone<input type="text" name="" value="" style="width:200px"> <input type="button" value="삭제" onclick="remove_item(this)">
</div>

<div id="field"></div>

<input type="button" value=" 추가 " onclick="add_item()"><br>
-->


<script src="${ctx}/js/jquery-min.js"></script>
</html>