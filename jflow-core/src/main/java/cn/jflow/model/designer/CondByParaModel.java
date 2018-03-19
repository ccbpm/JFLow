package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import BP.WF.Template.Cond;
import BP.WF.Template.CondType;
import BP.WF.Template.ConnDataFrom;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;


public class CondByParaModel {
	private HttpServletRequest req;
	private HttpServletResponse res;
	public UiFatory ui=null;
	public CondByParaModel()
	{
		
	}
	public CondByParaModel(HttpServletRequest request,HttpServletResponse response)
	{
		ui=new UiFatory();	
		this.req=request;
		this.res=response;
	}
	 //#region 属性
     /// <summary>
     /// 主键
     /// </summary>
	private String MyPK;
	
     public String getMyPK() {
		return req.getParameter("MyPK");
	}
	public void setMyPK(String myPK) {
		MyPK = myPK;
	}
//	string MyPK
//     {
//         get
//         {
//             return this.Request.QueryString["MyPK"];
//         }
//     }
     /// <summary>
     /// 流程编号
     /// </summary>
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
//         }
//     }
     /// <summary>
     /// 节点
     /// </summary>
	private int FK_Node;
     public int getFK_Node() {
		try {
			return Integer.parseInt(req.getParameter("FK_Node"));
		} catch (Exception e) {
			return getFK_MainNode();
		}
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}
//	public int FK_Node
//     {
//         get
//         {
//             try
//             {
//                 return int.Parse(this.Request.QueryString["FK_Node"]);
//             }
//             catch
//             {
//                 return this.FK_MainNode;
//             }
//         }
//     }
	private int FK_MainNode;
     public int getFK_MainNode() {
    	//sunxd
  		//问题：原始判断(request.getParameter("FK_MainNode") == null)成立时，
  		//如果request.getParameter("FK_MainNode")==""时Integer.parseInt("")会报错
  		//解决：判断request.getParameter("FK_MainNode") == null 替换为  StringUtils.isEmpty(request.getParameter("FK_MainNode"))
  		return  StringUtils.isEmpty(req.getParameter("FK_MainNode")) ? 0 : Integer
  				.parseInt(req.getParameter("FK_MainNode"));
	}
	public void setFK_MainNode(int fK_MainNode) {
		FK_MainNode = fK_MainNode;
	}
//	public int FK_MainNode
//     {
//         get
//         {
//             return int.Parse(this.Request.QueryString["FK_MainNode"]);
//         }
//     }
	private int ToNodeID;
     public int getToNodeID() {
		try {
			return Integer.parseInt(req.getParameter("ToNodeID"));
		} catch (Exception e) {
			return 0;
		}
	}
	public void setToNodeID(int toNodeID) {
		ToNodeID = toNodeID;
	}
//	public int ToNodeID
//     {
//         get
//         {
//             try
//             {
//                 return int.Parse(this.Request.QueryString["ToNodeID"]);
//             }
//             catch
//             {
//                 return 0;
//             }
//         }
//     }
     /// <summary>
     /// 执行类型
     /// </summary>
	private int HisCondType;
     public int getHisCondType() {
		return Integer.parseInt(req.getParameter("CondType").trim());
	}
	public void setHisCondType(int hisCondType) {
		HisCondType = hisCondType;
	}
//	public CondType HisCondType
//     {
//         get
//         {
//             return (CondType)int.Parse(this.Request.QueryString["CondType"]);
//         }
//     }
	private String GetOperVal;
     public String getGetOperVal() {
    	 if(ui.GetUIByID("TB_Val") !=null)
    	 {
    		 return ((TextBox)ui.GetUIByID("TB_Val")).getText();
    	 }
		return ((DDL)ui.GetUIByID("DDL_Val")).getSelectedItemStringVal();
	}
	public void setGetOperVal(String getOperVal) {
		GetOperVal = getOperVal;
	}
//	public string GetOperVal
//     {
//         get
//         {
//             if (this.IsExit("TB_Val"))
//                 return this.GetTBByID("TB_Val").Text;
//             return this.GetDDLByID("DDL_Val").SelectedItemStringVal;
//         }
//     }
	private String GetOperValText;
     public String getGetOperValText() {
    	 if(ui.GetUIByID("TB_Val")!=null)
    	 {
    		 return ((TextBox)ui.GetUIByID("TB_Val")).getText();
    	 }
		return ((DDL)ui.GetUIByID("DDL_Val")).getSelectedItem().getText();
	}
	public void setGetOperValText(String getOperValText) {
		GetOperValText = getOperValText;
	}
//	public string GetOperValText
//     {
//         get
//         {
//             if (this.IsExit("TB_Val"))
//                 return this.GetTBByID("TB_Val").Text;
//             return this.GetDDLByID("DDL_Val").SelectedItem.Text;
//         }
//     }
	private String GenerMyPK;
     public String getGenerMyPK() {
    	 return getFK_MainNode() + "_" + getToNodeID() + "_" + CondType.forValue(getHisCondType()) + "_" + ConnDataFrom.Paras;
	}
	public void setGenerMyPK(String generMyPK) {
		GenerMyPK = generMyPK;
	}
//	public string GenerMyPK
//     {
//         get
//         {
//             return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.Paras.ToString();
//         }
//     }
   //  #endregion 属性

     public void Page_Load()
     {
         Cond cond = new Cond();
         cond.setMyPK(getGenerMyPK());
         cond.RetrieveFromDBSources();

         ui.append(BaseModel.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
         ui.append(BaseModel.AddTR());
         ui.append(BaseModel.AddTD("class='GroupTitle'", "设置JFlow要求格式的系统参数"));
         ui.append(BaseModel.AddTREnd());

         TextBox tb = ui.creatTextBox("TB_Para");
         //tb.ID = "TB_Para";
         tb.setTextMode(TextBoxMode.MultiLine);
         tb.setRows(1);
         tb.setCols(80);
         tb.addAttr("style", "width:99%");
         //tb.Style.Add("width", "99%");
         tb.setText(cond.getOperatorValue().toString());
         ui.append(BaseModel.AddTD("", tb));

         ui.append(BaseModel.AddTREnd());
         ui.append(BaseModel.AddTableEnd());

         ui.append(BaseModel.AddBR());
         ui.append(BaseModel.AddSpace(1));

         LinkButton btn = ui.creatLinkButton(false, NamesOfBtn.Save.getCode(), "保存");
         //btn.Click += new EventHandler(btn_Click);
         btn.setHref("btn_Save_Click()");
         ui.append(btn);
         ui.append(BaseModel.AddSpace(1));

         btn = ui.creatLinkButton(false, NamesOfBtn.Delete.getCode(), "删除");
         btn.addAttr("onclick", " return confirm('您确定要删除吗？');");
         //btn.Attributes["onclick"] = " return confirm('您确定要删除吗？');";
         btn.setHref("btn_Del_Click()");
         //btn.Click += new EventHandler(btn_Click);
         ui.append(btn);
         ui.append(BaseModel.AddBR());
         ui.append(BaseModel.AddBR());

         BaseModel base=new BaseModel(req,res);
         ui.append(base.AddEasyUiPanelInfo("说明", "表达式格式：参数+空格+操作符+空格+值，仅支持一个表达式。格式如下：<br />" + "</br>"
             + "<ul>" + "</br>"
             + "<li>Emp = zhangsan</li>" + "</br>"
             + "<li>JinE = 30</li>" + "</br>"
             + "<li>JinE >= 30</li>" + "</br>"
             + "<li>JinE > 30</li>" + "</br>"
             + "<li>Way = '1'</li>" + "</br>"
             + "<li>Way != '1'</li>" + "</br>"
             + "<li>Name LIKE %li%</li>" + "</br>"
             + "</ul>" + "</br>"));
     }
}
