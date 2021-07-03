window.onload = function() {

	// 设置表单元素的显示和隐藏
	var user = new WebUser();
	console.log(user);

	var FK_Node = GetQueryString("FK_Node");

	if (FK_Node == '501') {
		// 拟稿节点
		$("#TB_BiaoTi").css("display", "block"); // 标题输入框
		$("#TB_BiaoTiA").css("display", "none"); // 标题a标签
		$("#TB_ZW1").css("display", "block"); // 起草正文按钮
		$("#FR_ShengPi").css("display", "none"); // 主管部门审核和拟稿处事审核栏
	} else {
		$("#TB_BiaoTiA").text($("#TB_BiaoTi").val()); // a标签设置内容
		$("#TB_BiaoTi").css("display", "none"); // 标题输入框
		$("#TB_BiaoTiA").css("display", "block"); // 标题a标签
		$("#TB_ZW1").css("display", "none"); // 起草正文按钮
	}

	// 获取处理记录
	var handler = new HttpHandler("BP.WF.HttpHandler.WF_WorkOpt_OneWork");
	handler.AddUrlData();
	handler.AddFormData();
	var data = handler.DoMethodReturnString("TimeBase_Init");
	data = JSON.parse(data);
	var datalist = data.Track;
	for (var i = 0; i < datalist.length; i++) {
		if (datalist[i].NDFromT == "拟稿") {
			// 设置主办部门和联系人
			// $("#TB_ZhuBanShanWei").val(datalist[i].NodeData.split('=')[2]);
			// $("#TB_LianXiRen").val(datalist[i].EmpFromT);
		}
		if (datalist[i].NDToT == "协办部门") {
			// 协办部门签批显示
			$("#FR_XieBan").css("display", "table-row");
		}
		if (datalist[i].NDToT == "承办") {
			// 承办签批显示
			$("#FR_ChengBan").css("display", "table-row");
		}
	}
}

// 打印通知公告。
function Print() {
	var workID = GetQueryString("WorkID");
	var url = "/AppTLJ/PrintTemplate/TongZhiGongGao/Default.html?WorkID="
			+ workID;
	window.open(url);
}
