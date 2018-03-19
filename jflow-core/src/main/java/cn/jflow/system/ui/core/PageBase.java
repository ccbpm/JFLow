package cn.jflow.system.ui.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PageBase {

	protected StringBuilder _content = null;
	protected List<String> _include = null;
	protected List<String> _script = null;
	protected HttpServletRequest _request;
	protected HttpServletResponse _response;

	public PageBase(HttpServletRequest request, HttpServletResponse response) {
		this._response = response;
		this._request = request;
		this._content = new StringBuilder();
		this._include = new ArrayList<String>();
		this._script = new ArrayList<String>();
	}

	public StringBuilder get_content() {
		return _content;
	}

	public void set_content(StringBuilder _content) {
		this._content = _content;
	}

	public List<String> get_include() {
		return _include;
	}

	public void set_include(List<String> _include) {
		this._include = _include;
	}

	public List<String> get_script() {
		return _script;
	}

	public void set_script(List<String> _script) {
		this._script = _script;
	}

	public HttpServletRequest get_request() {
		return _request;
	}

	public void set_request(HttpServletRequest _request) {
		this._request = _request;
	}

	public HttpServletResponse get_response() {
		return _response;
	}

	public void set_response(HttpServletResponse _response) {
		this._response = _response;
	}


	public void AddBR() {
		_content.append("<BR/>");
	}

	public void AddBtn() {
		_content.append("<input type='button'></input>");
	}

	public void AddBtn(String id, String name) {
		_content.append("<input type='button' id='" + id + "' name='" + name
				+ "'></input>");
	}

	public void Add(String text) {
		_content.append(text);
	}

	public void AddTable() {
		_content.append("<TABLE>");
	}

	public void AddTableEnd() {
		_content.append("</TABLE>");
	}

	public void AddTDTitle(String title) {
		_content.append("<TD>" + title + "</TD>");
	}
	
    public void AddFieldSet(String title) {
       Add("<fieldset width='100%' ><legend>&nbsp;" + title + "&nbsp;</legend>");
    }
   
    public void AddFieldSetEnd(){
       Add("</fieldset>");
    }
    
    public void AddTDTitleExt(String str){
    	Add("\n<TD class='TitleExt' nowrap=true >" + str + "</TD>");
	}
    
	public void AddTR(){
		Add("\n<TR>");
	}
	public void AddTR(String attr){
	    this.Add("\n<TR " + attr + " >");
	}
	
	public void AddTREnd(){
		Add("\n</TR>");
	}
	
	public void AddTDDesc(String str) {
        this.Add("\n<TD class='FDesc' nowrap=true >" + str + "</TD>");
    }
	
	public void AddFieldSetRed(String title, String msg)
    {
		Add("<fieldset class=FieldSetRed ><legend>&nbsp;" + title + "&nbsp;</legend>");
		Add(msg);
		Add("</fieldset>");
    }
	
	public void AddTD()
    {
		Add("\n<TD >&nbsp;</TD>");
    }
    public void AddTD(String str){
        if (str == null || "".equals(str))
        	Add("\n<TD  nowrap >&nbsp;</TD>");
        else
        	Add("\n<TD  nowrap >" + str + "</TD>");
   	}
    public void AddTDEnd()
    {
        this.Add("\n</TD>");
    }
    public void AddTD(String attr, String str)
    {
    	Add( "\n<TD " + attr + " >" + str + "</TD>");
    }
    
    public void AddTDIdx(int idx)
    {
    	Add("\n<TD class='Idx' nowrap>" + idx + "</TD>");
    }
    
    public void AddTDCenter(String str)
    {
    	Add("\n<TD align=center nowrap >" + str + "</TD>");
    }
    
    public void AddTRSum()
    {
    	Add("\n<TR class='TRSum' >");
    }
    
    public void addTableEnd()
    {
    	Add("</Table>");
    }
   
    public void AddMsgOfInfo(String title, String doc) {
       AddFieldSet(title);
       if (doc != null){
    	   Add(doc.replace("@", "<BR>@"));
       }
       AddFieldSetEnd();
    }
   
	/**
	 * 关闭窗口
	 */
	public void WinClose(){
		try {
			_response.getWriter().write("<script language='JavaScript'> window.close();</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
     }
	
	public void WinClose(String val){
		try {
			 //经测试谷歌,IE都走window.top.returnValue 方法
			String clientscript = "<script language='javascript'> if(window.opener != undefined){window.top.returnValue = '" + val + "';} else { window.returnValue = '" + val + "';} window.close(); </script>";
			_response.getWriter().write(clientscript);
		} catch (IOException e) {
			e.printStackTrace();
		}
     }
	
	public void WinCloseWithMsg(String mess) {
       mess = mess.replace("'", "＇");

       mess = mess.replace("\"", "＂");

       mess = mess.replace(";", "；");
       mess = mess.replace(")", "）");
       mess = mess.replace("(", "（");

       mess = mess.replace(",", "，");
       mess = mess.replace(":", "：");


       mess = mess.replace("<", "［");
       mess = mess.replace(">", "］");

       mess = mess.replace("[", "［");
       mess = mess.replace("]", "］");


       mess = mess.replace("@", "\\n@");

       mess = mess.replace("\r\n", "");

       try {
			_response.getWriter().write("<script language='JavaScript'>alert('" + mess + "'); window.close()</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
	
	/**
	 * 切换到信息也面。
	 * @param mess
	 * @param context 
	 */
	public void ToWFMsgPage(String mess, ServletContext context) {
		mess = mess.replace("@", "<BR>@");
		mess = mess.replace("~", "@");
		HttpSession session = _request.getSession();
		session.setAttribute("info", mess);
		
		try {
			if (context.getAttribute("PageMsg") == null) {
				_response.sendRedirect(BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Port/InfoPage.jsp?d=" + new Date());
			} else {
				_response.sendRedirect(context.getAttribute("PageMsg") + "WF/Comm/Port/InfoPage.jsp?d=" + new Date());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 切换到错误信息页面。
	 * @param mess
	 */
    public void ToErrorPage(String mess) {
    	HttpSession session = _request.getSession();
		session.setAttribute("info", mess);
		try {
			_response.sendRedirect(BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Port/ToErrorPage.jsp?d=" + new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    public void AddBtn(String id) {
    	String name = GetTextById(id);
		_content.append("<input type='button' id='" + id + "' name='" + name
				+ "'></input>");
	}
    
    private String GetTextById(String id)
    {
    	String text = "";
    	if(NamesOfBtn.UnDo.equals(id)){
    		text = "撤消操作";
    	}else if(NamesOfBtn.ChoseField.equals(id)){
    		text = "选择字段";
    	}else if(NamesOfBtn.DataGroup.equals(id)){
    		text = "分组查询";
    	}else if(NamesOfBtn.Copy.equals(id)){
    		text = "复制";
    	}else if(NamesOfBtn.Go.equals(id)){
    		text = "转到";
    	}else if(NamesOfBtn.ExportToModel.equals(id)){
    		text = "模板";
    	}else if(NamesOfBtn.DataCheck.equals(id)){
    		text = "数据检查";
    	}else if(NamesOfBtn.DataIO.equals(id)){
    		text = "数据导入";
    	}else if(NamesOfBtn.Statistic.equals(id)){
    		text = "统计";
    	}else if(NamesOfBtn.Balance.equals(id)){
    		text = "持平";
    	}else if(NamesOfBtn.Down.equals(id)){
    		text = "下降";
    	}else if(NamesOfBtn.Up.equals(id)){
    		text = "上升";
    	}else if(NamesOfBtn.Chart.equals(id)){
    		text = "图形";
    	}else if(NamesOfBtn.Rpt.equals(id)){
    		text = "报表";
    	}else if(NamesOfBtn.ChoseCols.equals(id)){
    		text = "选择列查询";
    	}else if(NamesOfBtn.Excel.equals(id)){
    		text = "导出全部";
    	}else if(NamesOfBtn.Excel_S.equals(id)){
    		text = "导出当前";
    	}else if(NamesOfBtn.Xml.equals(id)){
    		text = "导出到Xml";
    	}else if(NamesOfBtn.Send.equals(id)){
    		text = "发送";
    	}else if(NamesOfBtn.Reply.equals(id)){
    		text = "回复";
    	}else if(NamesOfBtn.Forward.equals(id)){
    		text = "转发";
    	}else if(NamesOfBtn.Next.equals(id)){
    		text = "下一个";
    	}else if(NamesOfBtn.Previous.equals(id)){
    		text = "上一个";
    	}else if(NamesOfBtn.Selected.equals(id)){
    		text = "选择";
    	}else if(NamesOfBtn.Add.equals(id)){
    		text = "增加";
    	}else if(NamesOfBtn.Adjunct.equals(id)){
    		text = "附件";
    	}else if(NamesOfBtn.AllotTask.equals(id)){
    		text = "分批任务";
    	}else if(NamesOfBtn.Apply.equals(id)){
    		text = "申请";
    	}else if(NamesOfBtn.ApplyTask.equals(id)){
    		text = "申请任务";
    	}else if(NamesOfBtn.Back.equals(id)){
    		text = "后退";
    	}else if(NamesOfBtn.Card.equals(id)){
    		text = "卡片";
    	}else if(NamesOfBtn.Close.equals(id)){
    		text = "关闭";
    	}else if(NamesOfBtn.Confirm.equals(id)){
    		text = "确定";
    	}else if(NamesOfBtn.Delete.equals(id)){
    		text = "删除";
    	}else if(NamesOfBtn.Edit.equals(id)){
    		text = "编辑";
    	}else if(NamesOfBtn.EnList.equals(id)){
    		text = "列表";
    	}else if(NamesOfBtn.Cancel.equals(id)){
    		text = "取消";
    	}else if(NamesOfBtn.Export.equals(id)){
    		text = "导出";
    	}else if(NamesOfBtn.FileManager.equals(id)){
    		text = "文件管理";
    	}else if(NamesOfBtn.Help.equals(id)){
    		text = "帮助";
    	}else if(NamesOfBtn.Insert.equals(id)){
    		text = "插入";
    	}else if(NamesOfBtn.LogOut.equals(id)){
    		text = "注销";
    	}else if(NamesOfBtn.Messagers.equals(id)){
    		text = "消息";
    	}else if(NamesOfBtn.New.equals(id)){
    		text = "新建";
    	}else if(NamesOfBtn.Print.equals(id)){
    		text = "打印";
    	}else if(NamesOfBtn.Refurbish.equals(id)){
    		text = "刷新";
    	}else if(NamesOfBtn.Reomve.equals(id)){
    		text = "移除";
    	}else if(NamesOfBtn.Save.equals(id)){
    		text = "保存";
    	}else if(NamesOfBtn.SaveAndClose.equals(id)){
    		text = "保存并关闭";
    	}else if(NamesOfBtn.SaveAndNew.equals(id)){
    		text = "保存并新建";
    	}else if(NamesOfBtn.SaveAsDraft.equals(id)){
    		text = "保存草稿";
    	}else if(NamesOfBtn.Search.equals(id)){
    		text = "查找(F)";
    	}else if(NamesOfBtn.SelectAll.equals(id)){
    		text = "选择全部";
    	}else if(NamesOfBtn.SelectNone.equals(id)){
    		text = "不选";
    	}else if(NamesOfBtn.View.equals(id)){
    		text = "查看";
    	}else if(NamesOfBtn.Update.equals(id)){
    		text = "更新";
    	}else{
    		throw new RuntimeException("@没有定义ToolBarBtn 标记 " + id);
    	}

    	return text;
    }

}
