package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import BP.Sys.SFDBSrcs;
import BP.Sys.SFTable;
import BP.Sys.SFTableAttr;
import BP.Tools.StringHelper;
import BP.WF.Glo;

public class SFSQLModel extends BaseModel{
	
	private StringBuilder pubBuilder;
	
	private String refNo;
	
	private String title;
	
	public String getTitle() {
		return title;
	}
	
	public String getPubBuilder() {
		return pubBuilder.toString();
	}

	public SFSQLModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public String getIDX(){
		return getParameter("IDX");
	}
	public final String getFromApp() {
		return getParameter("FromApp");
	}
	public final String getDoType() {
		return getParameter("DoType");
	}


	private void appendPubBuilder(String str){
		pubBuilder.append(str);
	}
	
	public void pageLoad()
	{
		pubBuilder = new StringBuilder();
		refNo = getRefNo();
		
		SFTable main = new SFTable();
		if (!StringHelper.isNullOrEmpty(refNo))
		{
			main.setNo(refNo);
			main.Retrieve();
		}
		this.BindSFTable(main);
	}
	
	public void BindSFTable(SFTable en){
		boolean isItem = false;
		String star = "<font color=red><b>(*)</b></font>";
		appendPubBuilder(AddTable());
		if ("SL".equals(this.getFromApp())) {
			if (StringHelper.isNullOrEmpty(refNo)) {
				appendPubBuilder(AddCaption("新建表"));
			} else {
				appendPubBuilder(AddCaption("编辑表"));
			}
		} else {

			if (StringHelper.isNullOrEmpty(refNo)) {
				appendPubBuilder(AddCaption("<a href='Do.jsp?DoType=AddF&MyPK="
						+ this.getMyPK()
						+ "&IDX="
						+ this.getIDX()
						+ "'>增加新字段向导</a> - <a href='Do.jsp?DoType=AddSFTable&MyPK="
						+ this.getMyPK() + "&IDX=" + this.getIDX()
						+ "'>外键</a> - 新建表"));
			} else {
				appendPubBuilder(AddCaption("<a href='Do.jsp?DoType=AddF&MyPK="
						+ this.getMyPK()
						+ "&IDX="
						+ this.getIDX()
						+ "'>增加新字段向导</a> - <a href='Do.jsp?DoType=AddSFTable&MyPK="
						+ this.getMyPK() + "&IDX=" + this.getIDX()
						+ "'>外键</a> - 编辑表"));
			}
		}
		if (StringHelper.isNullOrEmpty(refNo)) {
			this.title = "新建表";
		} else {
			this.title = "编辑表";
		}
		int idx = 0;
		appendPubBuilder(AddTR());
		appendPubBuilder(AddTDTitle("Idx"));
		appendPubBuilder(AddTDTitle("项目"));
		appendPubBuilder(AddTDTitle("采集"));
		appendPubBuilder(AddTDTitle("备注"));
		appendPubBuilder(AddTREnd());
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("数据源" + star));
		DDL ddl = new DDL();
		ddl.setId("DDL_FK_SFDBSrc");
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAllFromDBSource();
		ddl.Bind(srcs, en.getFK_SFDBSrc());
		appendPubBuilder(AddTD(ddl));
		appendPubBuilder(AddTD("选择数据源,点击这里<a href=\"javascript:WinOpen('"+Glo.getCCFlowAppPath()+"WF/Comm/Search.jsp?EnsName=BP.Sys.SFDBSrcs')\">创建</a>，<a href='SFSQL.jsp?DoType=New&MyPK=" + this.getMyPK() + "&Idx='>刷新</a>。"));
		appendPubBuilder(AddTREnd());

		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("表中文名称" + star));
		TextBox tb = new TextBox();
		tb.setId("TB_Name");
		tb.setText(en.getName());
		tb.addAttr("onblur", "tbNameToPinyin();");
		appendPubBuilder(AddTD(tb));
		appendPubBuilder(AddTD("该表的中文名称，比如：物料类别，科目。"));
		appendPubBuilder(AddTREnd());

		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("表英文名称" + star));
		tb = new TextBox();
		tb.setId("TB_No");
		tb.setText(en.getNo());
		if (StringHelper.isNullOrEmpty(refNo)) {
			tb.setEnabled(true);
		} else {
			tb.setEnabled(false);
		}

		if (tb.getText().equals("")) {
			//tb.setText("SF_");
			tb.setText("");
		}
		appendPubBuilder(AddTD(tb));
		appendPubBuilder(AddTD("必须以字母或者下划线开头，不能包含特殊字符"));
		appendPubBuilder(AddTREnd());
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("数据结构"));
		ddl = new DDL();
		ddl.setId ("DDL_" + SFTableAttr.CodeStruct);
		ddl.BindSysEnum(SFTableAttr.CodeStruct, en.getCodeStruct().getValue());
		appendPubBuilder(AddTD(ddl));
		appendPubBuilder(AddTD("用于在下拉框中不同格式的展现。"));
		appendPubBuilder(AddTREnd());

		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("colspan=3", "查询SQL" + star + "支持jform表达式，允许有WebUser.No,@WebUser.Name,@WebUser.FK_Dept变量。"));
		appendPubBuilder(AddTREnd());
		
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		tb = new TextBox();
		tb.setId("TB_"+ SFTableAttr.SelectStatement);//查询
		tb.setText(en.getSelectStatement()); //查询语句.
		tb.setTextMode(TextBoxMode.MultiLine);
		//tb.setRows(4);
		//tb.setColumns(70);
		tb.addAttr("style", "width:95%;height:50px;");
		appendPubBuilder(AddTD("colspan=3", tb));
		appendPubBuilder(AddTREnd());
		

		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("colspan=3", "比如:SELECT BH AS No, MC as Name FROM CC_USER WHERE CCType=3<br>SELECT BH AS No, MC as Name FROM CC_USER WHERE FK_Dept=@WebUser.FK_Dept"));
		appendPubBuilder(AddTREnd());
		

		
		appendPubBuilder(AddTR());
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder("<TD colspan=3 align=center>");
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setCssClass("Btn");
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setText("创建");
		}
		else
		{
			btn.setText("保存");
		}
		btn.addAttr("onclick", " btn_Save_Click();");
		appendPubBuilder(btn.toString());

		/*btn = new Button();
		btn.setId("Btn_Edit");
		btn.setCssClass("Btn");
		btn.setText("编辑数据"); // "编辑数据"
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setEnabled(false);
		}
		if (en.getIsClass()) {
			btn.addAttr("onclick", "WinOpen('"+Glo.getCCFlowAppPath()+"WF/Search.jsp?EnsName=" + en.getNo() + "','dg' ); return false;");
		}else {
			btn.addAttr("onclick", "WinOpen('"+Glo.getCCFlowAppPath()+"WF/MapDef/SFTableEditData.jsp?RefNo=" + refNo + "','dg' ); return false;");
		}
		appendPubBuilder(btn.toString());*/

		if (!"SL".equals(this.getFromApp())) {
			btn = new Button();
			btn.setId("Btn_Add");
			btn.setCssClass("Btn");
	
			btn.setText("添加到表单"); // "添加到表单";
			btn.addAttr("onclick", " return confirm('您确认吗？');");
			btn.addAttr("onclick", " btn_Add_Click();");
			if (StringHelper.isNullOrEmpty(refNo))
			{
				btn.setEnabled(false);
			}
	
			appendPubBuilder(btn.toString());
		}
		
		
		
		btn = new Button();
		btn.setId("Btn_Del");
		btn.setCssClass("Btn");

		btn.setText("删除");
		btn.addAttr("onclick", " return confirm('您确认吗？');");
		btn.addAttr("onclick", " btn_Del_Click();");
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setEnabled(false);
		}

		appendPubBuilder(btn.toString());
		appendPubBuilder("</TD>");
		appendPubBuilder(AddTREnd());
		appendPubBuilder(AddTableEnd());
	}

}
