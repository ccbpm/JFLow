<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	
	<%
		BillModel billModel = new BillModel(request, response);
		billModel.loadPage();
	%>
<%@ include file="/WF/head/head1.jsp"%>
<title><%=billModel.Title %></title>

<script  language=javascript>
    function Del(mypk, fk_flow, refoid)
    {
        if (window.confirm('您确定要删除吗？') ==false)
            return ;
    
        var url='Do.jsp?DoType=Del&MyPK='+mypk+'&RefOID='+refoid;
        var b=window.showModalDialog( url , 'ass' ,'dialogHeight: 400px; dialogWidth: 600px;center: yes; help: no'); 
        window.location.href = window.location.href;
    }
    function AddBillType()
    {
        var url='../Comm/Search.jsp?EnsName=BP.WF.BillTypes';
        var b = window.showModalDialog(url, 'ass', 'dialogHeight: 400px; dialogWidth: 600px;center: yes; help: no'); 
        window.location.href = window.location.href;
    }

    function openEidt(name) {
        name = name.replace(/-/g,"\\");
        var dateNow = new Date();
        var date = dateNow.getFullYear() + "" + dateNow.getMonth() + "" + dateNow.getDay() + "" + dateNow.getHours() + "" + dateNow.getMinutes() + "" + dateNow.getSeconds();
        var url = "../WorkOpt/GridEdit.jsp?grf=" + name + ".grf&t=" + date;

        var newWindow = window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
        newWindow.focus();
    }
    
    function btn_Del_Click(){
		$("#BodyHtml").val($("#form_id").html());
		document.getElementById('form_id').action="Wf/Admin/btn_Del_Click.do";
		document.getElementById('form_id').submit();
    }
    
   // Btn_Del
    function  btn_Del_Click(){
    	  if(confirm("确定删除当前数据？")){
    		  $("#BodyHtml").val($("#form1").html());
    			document.getElementById('form1').action="<%=basePath%>Wf/Admin/btn_Del_Click.do";
    			document.getElementById('form1').submit();
    	    }else{
    	        return;
    	    }
    	
   }
    
    function btn_do_click(){
		$("#BodyHtml").val($("#form_id").html());
		document.getElementById('form_id').submit();
    }
   function  btn_Click(){
		$("#BodyHtml").val($("#form1").html());
		document.getElementById('form1').action="<%=basePath%>Wf/Admin/btn_Click.do";
		document.getElementById('form1').submit();
	   
   } 
   function btn_SaveTypes_Click(){
		$("#BodyHtml").val($("#form1").html());
	document.getElementById('form1').action="<%=basePath%>Wf/Admin/btn_SaveTypes_Click.do";
	document.getElementById('form1').submit();
   }
    </script>
</head>
<body class="Body<%=WebUser.getStyle()%>"   leftMargin="0"  topMargin="0" >
    <form id="form1" method="post" enctype="multipart/form-data">
    <input type="hidden" name="BodyHtml" id="BodyHtml">
	<input type="hidden" name="FK_Flow" value="<%= billModel.getFK_Flow()%>">
	<input type="hidden" name="FK_Node" value="<%= billModel.getFK_Node() %>" >
	<input type="hidden" name="NodeID" value="<%= billModel.getNodeID() %>">
	<input type="hidden" name="RefNo" value="<%= billModel.getRefNo() %>">

	    <%=billModel.Ucsys1.toString() %>
	    <%=billModel.UCSys2.toString() %>
	    
    </form>
</body>
</html>