package cn.jflow.model.wf.rpt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataType;
import BP.En.UIContralType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;

public class S5SearchCondModel{

	private HttpServletRequest req;
	private HttpServletResponse res;
	public UiFatory Pub2=null;
	public S5SearchCondModel(){}
	public S5SearchCondModel(HttpServletRequest request,HttpServletResponse response){
		this.req=request;
		this.res=response;
	}
	
//	 #region 属性.
	private String FK_Flow;
     public String getFK_Flow() {
		return req.getParameter("FK_Flow");
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}

//	public string FK_Flow
//     {
//         get
//         {
//             return this.Request.QueryString["FK_Flow"];
//
//         }
//     }
	private String RptNo;
	
     public String getRptNo() {
		return req.getParameter("RptNo");
	}
	public void setRptNo(String rptNo) {
		RptNo = rptNo;
	}

//	public string RptNo
//     {
//         get
//         {
//             return this.Request.QueryString["RptNo"];
//
//         }
//     }
	private String FK_MapData;
     public String getFK_MapData() {
		return req.getParameter("FK_MapData");
	}
	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
	}

//	public string FK_MapData
//     {
//         get
//         {
//             return this.Request.QueryString["FK_MapData"];
//
//         }
//     }
//     #endregion 属性.

     public void Page_Load()
     {
    	 Pub2=new UiFatory();
    	 Pub2.append("<div data-options=\"region:'center',title:'4. 设置报表查询条件',border:false\" style=\"padding: 5px; height: auto\">");
         MapData md = new MapData();
         md.setNo(this.getRptNo());
         md.RetrieveFromDBSources();

         MapAttrs attrs = new MapAttrs(this.getRptNo());

//         #region 查询条件定义

         this.Pub2.append("<div style='width:100%'>");
         this.Pub2.append("<div class='easyui-panel' title='是否增加关键字查询' data-options=\"iconCls:'icon-tip',fit:true\" style='height:auto;padding:10px'>");
         this.Pub2.append("关键字查询是接受用户输入一个关键字，在整个报表的显示列中使用like查询(外键、枚举、数值类型的除外)");
         this.Pub2.append(BaseModel.AddBR());

         CheckBox mycb = Pub2.creatCheckBox("CB_IsSearchKey");
//         mycb.ID = "CB_IsSearchKey";
         mycb.setText("是否增加关键字查询");
         mycb.setChecked(md.getRptIsSearchKey());
         this.Pub2.append(mycb);
         this.Pub2.append(BaseModel.AddDivEnd());
         this.Pub2.append(BaseModel.AddDivEnd());
         this.Pub2.append(BaseModel.AddBR());

         this.Pub2.append("<div style='width:100%'>");
         this.Pub2.append("<div class='easyui-panel' title='外键与枚举类型' data-options=\"iconCls:'icon-tip',fit:true\" style='height:auto;padding:10px'>");
         this.Pub2.append("外键、枚举类型的数据可以添加到查询条件中，请选择要添加的查询条件：");
         this.Pub2.append(BaseModel.AddBR());

         for (int i = 0; i < attrs.size(); i++) {
        	 MapAttr mattr=(MapAttr) attrs.get(i);
        	 if (mattr.getUIContralType() != UIContralType.DDL)
                 continue;

             CheckBox cb = Pub2.creatCheckBox("CB_F_" + mattr.getKeyOfEn());
//             cb.ID = "CB_F_" + mattr.KeyOfEn;
             if (md.getRptSearchKeys().contains("*" + mattr.getKeyOfEn()))
                 cb.setChecked(true);

             cb.setText(mattr.getName() + "(" + mattr.getKeyOfEn() + ")");
             this.Pub2.append(cb);
             this.Pub2.append(BaseModel.AddBR());
		}
//         for(MapAttr mattr : attrs)
//         {
//             if (mattr.UIContralType != UIContralType.DDL)
//                 continue;
//
//             CheckBox cb = new CheckBox();
//             cb.ID = "CB_F_" + mattr.KeyOfEn;
//             if (md.RptSearchKeys.Contains("*" + mattr.KeyOfEn))
//                 cb.Checked = true;
//
//             cb.Text = mattr.Name + "(" + mattr.KeyOfEn + ")";
//             this.Pub2.Add(cb);
//             this.Pub2.AddBR();
//         }

         this.Pub2.append(BaseModel.AddDivEnd());
         this.Pub2.append(BaseModel.AddDivEnd());
         this.Pub2.append(BaseModel.AddBR());

         boolean isHave = false;

         for (int i = 0; i < attrs.size(); i++) {
        	 MapAttr mattr=(MapAttr)attrs.get(i);
        	 if (mattr.getMyDataType() == DataType.AppDate || mattr.getMyDataType() == DataType.AppDateTime)
             {
                 isHave = true;
                 break;
             }
		}
//         for(MapAttr mattr : attrs)
//         {
//             if (mattr.MyDataType == DataType.AppDate || mattr.MyDataType == DataType.AppDateTime)
//             {
//                 isHave = true;
//                 break;
//             }
//         }

         if (isHave)
         {
             this.Pub2.append("<div style='width:100%'>");
             this.Pub2.append("<div class='easyui-panel' title='时间段' data-options=\"iconCls:'icon-tip',fit:true\" style='height:auto;padding:10px'>");
             this.Pub2.append("对数据按照时间段进行查询，比如：按流程的发起时间，在指定时间段内进行查询。");
             this.Pub2.append(BaseModel.AddBR());

             this.Pub2.append("选择方式：");
             DDL ddl = Pub2.creatDDL("DDL_DTSearchWay");
//             ddl.ID = "DDL_DTSearchWay";
             ddl.BindSysEnum("DTSearchWay");
             ddl.SetSelectItem(md.getRptDTSearchWay().getValue());
             this.Pub2.append(ddl);
             this.Pub2.append(BaseModel.AddSpace(3));

             this.Pub2.append("字段：");
             ddl = Pub2.creatDDL("DDL_DTSearchKey");
//             ddl.ID = "DDL_DTSearchKey";

             for (int i = 0; i < attrs.size(); i++) {
            	 MapAttr mattr=(MapAttr) attrs.get(i);
            	 if (mattr.getMyDataType() == DataType.AppDate || mattr.getMyDataType() == DataType.AppDateTime)
                 {
                     if (mattr.getUIVisible() == false)
                         continue;
                     ddl.Items.add(new ListItem(mattr.getKeyOfEn() + "  " + mattr.getName(), mattr.getKeyOfEn()));
                 }
			}
//             for (MapAttr mattr in attrs)
//             {
//                 if (mattr.MyDataType == DataType.AppDate || mattr.MyDataType == DataType.AppDateTime)
//                 {
//                     if (mattr.UIVisible == false)
//                         continue;
//                     ddl.Items.Add(new ListItem(mattr.KeyOfEn + "  " + mattr.Name, mattr.KeyOfEn));
//                 }
//             }

             ddl.SetSelectItem(md.getRptDTSearchKey());
             this.Pub2.append(ddl);
             this.Pub2.append(BaseModel.AddDivEnd());
             this.Pub2.append(BaseModel.AddDivEnd());
         }
//         #endregion
     }

//     protected void Btn_Save_Click(object sender, EventArgs e)
//     {
//         Save();
//
//         this.Response.Redirect("S5_SearchCond.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
//     }
//
//     protected void Btn_Cancel_Click(object sender, EventArgs e)
//     {
//         this.WinClose();
//     }
//
//     protected void Btn_SaveAndNext1_Click(object sender, EventArgs e)
//     {
//         Save();
//
//         this.Response.Redirect("S6_Power.aspx?FK_MapData=" + this.FK_MapData + "&RptNo=" + this.RptNo + "&FK_Flow=" + this.FK_Flow, true);
//     }
//
//     private void Save()
//     {
//         MapData md = new MapData();
//         md.No = this.RptNo;
//         md.RetrieveFromDBSources();
//
//         MapAttrs mattrs = new MapAttrs(this.RptNo);
//         string keys = "";
//         foreach (MapAttr mattr in mattrs)
//         {
//             if (mattr.UIContralType != UIContralType.DDL)
//                 continue;
//             CheckBox cb = this.Pub2.GetCBByID("CB_F_" + mattr.KeyOfEn);
//             if (cb.Checked)
//                 keys += "*" + mattr.KeyOfEn;
//         }
//
//         md.RptSearchKeys = keys + "*";
//         md.RptIsSearchKey = this.Pub2.GetCBByID("CB_IsSearchKey").Checked;
//
//         if (this.Pub2.IsExit("DDL_DTSearchWay"))
//         {
//             BP.Web.Controls.DDL ddl = this.Pub2.GetDDLByID("DDL_DTSearchWay");
//             md.RptDTSearchWay = (DTSearchWay)ddl.SelectedItemIntVal;
//
//             ddl = this.Pub2.GetDDLByID("DDL_DTSearchKey");
//             md.RptDTSearchKey = ddl.SelectedItemStringVal;
//         }
//         md.Update();
//
//         Cash.Map_Cash.remove(this.RptNo);
//     }
}
