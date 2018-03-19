package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import BP.DA.DataType;
import BP.En.UIContralType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;

public class S5SearchCond extends BaseModel{

	private String FK_Flow;
	
	private String RptNo;
	
	private String FK_MapData;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	public StringBuffer Pub2=null;
	
	public S5SearchCond(HttpServletRequest request, HttpServletResponse response,String FK_Flow,String RptNo,String FK_MapData) {
		super(request, response);
		this.request=request;
		this.response=response;
		this.FK_Flow=FK_Flow;
		this.RptNo=RptNo;
		this.FK_MapData=FK_MapData;
		Pub2=new StringBuffer();
	}

	public void init(){
		 MapData md = new MapData();
         md.setNo(this.RptNo);
         md.RetrieveFromDBSources();

         MapAttrs attrs = new MapAttrs(this.RptNo);


         this.Pub2.append("<div style='width:100%'>");
         this.Pub2.append("<div class='easyui-panel' title='是否增加关键字查询' data-options=\"iconCls:'icon-tip',fit:true\" style='height:auto;padding:10px'>");
         this.Pub2.append("关键字查询是接受用户输入一个关键字，在整个报表的显示列中使用like查询(外键、枚举、数值类型的除外)");
         this.Pub2.append(AddBR());

         CheckBox mycb = new CheckBox();
         mycb.setId("CB_IsSearchKey");
         mycb.setText("是否增加关键字查询");
         mycb.setChecked(md.getRptIsSearchKey());
         this.Pub2.append(mycb);
         this.Pub2.append(AddDivEnd());
         this.Pub2.append(AddDivEnd());
         this.Pub2.append(AddBR());

         this.Pub2.append("<div style='width:100%'>");
         this.Pub2.append("<div class='easyui-panel' title='外键与枚举类型' data-options=\"iconCls:'icon-tip',fit:true\" style='height:auto;padding:10px'>");
         this.Pub2.append("外键、枚举类型的数据可以添加到查询条件中，请选择要添加的查询条件：");
         this.Pub2.append(AddBR());

         for (MapAttr mattr: attrs.ToJavaList())
         {
             if (mattr.getUIContralType() != UIContralType.DDL)
                 continue;

             CheckBox cb = new CheckBox();
             cb.setId("CB_F_" + mattr.getKeyOfEn());;
             if (md.getRptSearchKeys().contains("*" + mattr.getKeyOfEn()))
                 cb.setChecked(true);

             cb.setText(mattr.getName() + "(" + mattr.getKeyOfEn() + ")");
             this.Pub2.append(cb);
             this.Pub2.append(AddBR());
         }

         this.Pub2.append(AddDivEnd());
         this.Pub2.append(AddDivEnd());
         this.Pub2.append(AddBR());

         boolean isHave = false;

         for (MapAttr mattr: attrs.ToJavaList())
         {
             if (mattr.getMyDataType() == DataType.AppDate || mattr.getMyDataType() == DataType.AppDateTime)
             {
                 isHave = true;
                 break;
             }
         }

         if (isHave)
         {
             this.Pub2.append("<div style='width:100%'>");
             this.Pub2.append("<div class='easyui-panel' title='时间段' data-options=\"iconCls:'icon-tip',fit:true\" style='height:auto;padding:10px'>");
             this.Pub2.append("对数据按照时间段进行查询，比如：按流程的发起时间，在指定时间段内进行查询。");
             this.Pub2.append(AddBR());

             this.Pub2.append("选择方式：");
             DDL ddl = new DDL();
             ddl.setId("DDL_DTSearchWay");
             ddl.BindSysEnum("DTSearchWay");
             ddl.SetSelectItem(md.getRptDTSearchWay().getValue());
             this.Pub2.append(ddl);
             this.Pub2.append(AddSpace(3));

             this.Pub2.append("字段：");
             ddl = new DDL();
             ddl.setId("DDL_DTSearchKey");

             for (MapAttr mattr: attrs.ToJavaList())
             {
                 if (mattr.getMyDataType() == DataType.AppDate || mattr.getMyDataType() == DataType.AppDateTime)
                 {
                     if (mattr.getUIVisible() == false)
                         continue;
                     ddl.Items.add(new ListItem(mattr.getKeyOfEn() + "  " + mattr.getName(), mattr.getKeyOfEn()));
                 }
             }

             ddl.SetSelectItem(md.getRptDTSearchKey());
             this.Pub2.append(ddl);
             this.Pub2.append(AddDivEnd());
             this.Pub2.append(AddDivEnd());
         }
	}
	
//	protected void Btn_Save_Click(object sender, EventArgs e)
//	{
//		Save();
//
//		this.Response.Redirect("S5_SearchCond.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
//	}
//
//	protected void Btn_Cancel_Click(object sender, EventArgs e)
//	{
//		this.WinClose();
//	}
//
//	protected void Btn_SaveAndNext1_Click(object sender, EventArgs e)
//	{
//		Save();
//
//		this.Response.Redirect("S6_Power.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
//	}
//
//	private void Save()
//	{
//		MapData md = new MapData();
//		md.No = this.RptNo;
//		md.RetrieveFromDBSources();
//
//		MapAttrs mattrs = new MapAttrs(this.RptNo);
//		string keys = "";
//		foreach (MapAttr mattr in mattrs)
//		{
//			if (mattr.UIContralType != UIContralType.DDL)
//				continue;
//			CheckBox cb = this.Pub2.GetCBByID("CB_F_" + mattr.KeyOfEn);
//			if (cb.Checked)
//				keys += "*" + mattr.KeyOfEn;
//		}
//
//		md.RptSearchKeys = keys + "*";
//		md.RptIsSearchKey = this.Pub2.GetCBByID("CB_IsSearchKey").Checked;
//
//		if (this.Pub2.IsExit("DDL_DTSearchWay"))
//		{
//			BP.Web.Controls.DDL ddl = this.Pub2.GetDDLByID("DDL_DTSearchWay");
//			md.RptDTSearchWay = (DTSearchWay)ddl.SelectedItemIntVal;
//
//			ddl = this.Pub2.GetDDLByID("DDL_DTSearchKey");
//			md.RptDTSearchKey = ddl.SelectedItemStringVal;
//		}
//		md.Update();
//
//		Cash.Map_Cash.remove(this.RptNo);
//	}
	
}
