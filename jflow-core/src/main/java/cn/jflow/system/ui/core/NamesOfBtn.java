package cn.jflow.system.ui.core;

public enum NamesOfBtn {
	Option("Btn_Option", "选项")
	,Shift("Btn_Shift", "移交")
	,Copy("Btn_Copy", "复制")
	,DataIO("Btn_DataIO", "数据导入导出")
	,Do("Btn_Do", "执行")
	,Go("Btn_Go", "到")
	,Up("Btn_Up", "上升")
	,Upload("Btn_Upload", "上传")
	,Down("Btn_Down", "下降")
	,Balance("Btn_Balance", "不变化")
	,Chart("Btn_Chart", "图形")
	,DTS("Btn_DTS", "调度")
	,ChoseCols("Btn_ChoseCols", "选择列查询")
	,ChoseField("Btn_ChoseField", "选择字段")
	,DataGroup("Btn_DataGroup", "分组查询")
	,Excel("Btn_Excel", "导出全部")
	,Excel_S("Btn_Excel_S", "导出当前")
	,ExportToModel("Btn_ExportToModel", "模板")
	,Xml("Btn_Xml", "导出到Xml")
	,Next("Btn_Next", "下一个")
	,Previous("Btn_Previous", "上一个")
	,Send("Btn_Send", "发送")
	,UnDo("Btn_UnDo", "撤消操作")
	,Reply("Btn_Reply", "回复")
	,Forward("Btn_Forward", "转发")
	,Add("Btn_Add", "增加")
	,Adjunct("Btn_Adjunct", "附件")
	,AllotTask("Btn_AllotTask", "分批任务")
	,Apply("Btn_Apply", "申请")
	,ApplyTask("Btn_ApplyTask", "申请任务")
	,Back("Btn_Back", "后退")
	,Cancel("Btn_Cancel", "取消")
	,Card("Btn_Card", "卡片")
	,Close("Btn_Close", "关闭")
	,Confirm("Btn_Confirm", "确定")
	,Delete("Btn_Delete", "删除")
	,Edit("Btn_Edit", "编辑")
	,EnList("Btn_EnList", "列表")
	,Export("Btn_Export", "导出")
	,FileManager("Btn_FileManager", "文件管理")
	,Help("Btn_Help", "帮助")
	,DataCheck("Btn_DataCheck", "数据检查")
	,Rpt("Btn_Rpt", "报表")
	,Insert("Btn_Insert", "插入")
	,LogOut("Btn_LogOut", "注销")
	,Messagers("Btn_Messagers", "消息")
	,New("Btn_New", "新建")
	,Print("Btn_Print", "打印")
	,Refurbish("Btn_Refurbish", "刷新")
	,Reomve("Btn_Reomve", "移除")
	,Save("Btn_Save", "保存")
	,HandOver("Btn_HandOver", "HandOver")
	,SaveAndClose("Btn_SaveAndClose", "保存并关闭")
	,SaveAndNew("Btn_SaveAndNew", "保存并新建")
	,SaveAsDraft("Btn_SaveAsDraft", "保存草稿")
	,Search("Btn_Search", "查找(F)")
	,Statistic("Btn_Statistic", "统计")
	,SelectAll("Btn_SelectAll", "选择全部")
	,SelectNone("Btn_SelectNone", "不选")
	,Selected("Btn_Selected", "选择")
	,Update("Btn_Update", "更新")
	,View("Btn_View", "查看")
	,Accept("Btn_Accept", "接受")
	,Refuse("Btn_Refuse", "拒绝")
	,Open("Btn_Open", "打开")
	,Picture("Btn_Picture", "图片")
	,Seal("Btn_Seal", "签章")
	,FlowImage("Btn_FlowImage", "流程图片")
	,Download("Btn_Download", "下载")
	,Setting("Setting", "设置")
	,None("NONE","NONE")
	;
	
	private NamesOfBtn(String code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static NamesOfBtn getEnumByCode(String code){
		NamesOfBtn r = NamesOfBtn.None;
		for(NamesOfBtn freq :NamesOfBtn.values()){
			if(freq.getCode().equals(code)){
				r=freq;
			}
		}
		return r;
	}
	
	private String code;
	private String desc;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
