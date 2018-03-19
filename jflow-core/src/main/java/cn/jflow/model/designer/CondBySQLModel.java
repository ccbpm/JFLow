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

public class CondBySQLModel {
	public UiFatory ui=null;
	private HttpServletRequest req;
	private HttpServletResponse res;
	public CondBySQLModel()
	{
		
	}
	public CondBySQLModel(HttpServletRequest request,HttpServletResponse response)
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
//	 public new string MyPK
//      {
//          get
//          {
//              return this.Request.QueryString["MyPK"];
//          }
//      }
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
//      {
//          get
//          {
//              return this.Request.QueryString["FK_Flow"];
//          }
//      }
      /// <summary>
      /// 节点
      /// </summary>
	private int FK_Node;
	
      public int getFK_Node() {
    	  try {
			return Integer.parseInt(req.getParameter("FK_Node"));
		} catch (Exception e) {
			return  this.getFK_MainNode();
		}
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}
//	public int FK_Node
//      {
//          get
//          {
//              try
//              {
//                  return int.Parse(this.Request.QueryString["FK_Node"]);
//              }
//              catch
//              {
//                  return this.FK_MainNode;
//              }
//          }
//      }
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
//      {
//          get
//          {
//              return int.Parse(this.Request.QueryString["FK_MainNode"]);
//          }
//      }
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
//      {
//          get
//          {
//              try
//              {
//                  return int.Parse(this.Request.QueryString["ToNodeID"]);
//              }
//              catch
//              {
//                  return 0;
//              }
//          }
//      }
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
//      {
//          get
//          {
//              return (CondType)int.Parse(this.Request.QueryString["CondType"]);
//          }
//      }
	private String GetOperVal;
      public String getGetOperVal() {
    	  if(ui.GetUIByID("TB_Val")!=null)
    	  {
    		  return ((TextBox)ui.GetUIByID("TB_Val")).getText();
    	  }
    	  return ((DDL)ui.GetUIByID("DDL_Val")).getSelectedItemStringVal();
	}
	public void setGetOperVal(String getOperVal) {
		GetOperVal = getOperVal;
	}
//	public string GetOperVal
//      {
//          get
//          {
//              if (this.IsExit("TB_Val"))
//                  return this.GetTBByID("TB_Val").Text;
//              return this.GetDDLByID("DDL_Val").SelectedItemStringVal;
//          }
//      }
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
//      {
//          get
//          {
//              if (this.IsExit("TB_Val"))
//                  return this.GetTBByID("TB_Val").Text;
//              return this.GetDDLByID("DDL_Val").SelectedItem.Text;
//          }
//      }
	private String GenerMyPK;
      public String getGenerMyPK() {
    	  return getFK_MainNode() + "_" + getToNodeID() + "_" + CondType.forValue(getHisCondType()) + "_" + ConnDataFrom.SQL;
	}
	public void setGenerMyPK(String generMyPK) {
		GenerMyPK = generMyPK;
	}
//	public string GenerMyPK
//      {
//          get
//          {
//              return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.SQL.ToString();
//          }
//      }
      //#endregion 属性

      public void Page_Load()
      {
          Cond cond = new Cond();
          cond.setMyPK(getGenerMyPK());
          cond.RetrieveFromDBSources();

          ui.append(BaseModel.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
          ui.append(BaseModel.AddTR());
          ui.append(BaseModel.AddTD("class='GroupTitle'", "设置SQL"));
          ui.append(BaseModel.AddTREnd());

          ui.append(BaseModel.AddTR());

          TextBox tb = ui.creatTextBox("TB_SQL");
          //tb.ID = "TB_SQL";
          tb.setTextMode(TextBoxMode.MultiLine);
          tb.setRows(10);
          tb.setCols(80);
          tb.addAttr("style", "width:99%;");
          //tb.setStyle().Add("width", "99%");
          tb.setText(cond.getOperatorValueStr());
          ui.append(BaseModel.AddTD("", tb));

          ui.append(BaseModel.AddTREnd());
          ui.append(BaseModel.AddTableEnd());

          ui.append(BaseModel.AddBR());
          ui.append(BaseModel.AddSpace(1));

          LinkButton btn =ui.creatLinkButton(false, NamesOfBtn.Save.getCode(), "保存");
          btn.setHref("save()");
          //btn.Click += new EventHandler(btn_Click);
          ui.append(btn);
          ui.append(BaseModel.AddSpace(1));

          btn = ui.creatLinkButton(false, NamesOfBtn.Delete.getCode(), "删除");
          btn.addAttr("onclick", " return confirm('您确定要删除吗？');");
          //.Attributes["onclick"] = " return confirm('您确定要删除吗？');";
          btn.setHref("del()");
          //btn.Click += new EventHandler(btn_Click);
          ui.append(btn);
          ui.append(BaseModel.AddBR());
          ui.append(BaseModel.AddBR());

          String help = "";
          help += "<ul>";
          help += "<li>在文本框里设置一个查询SQL，它返回一行一列。比如: SELECT COUNT(*) AS Num FROM MyTable WHERE NAME='@MyFieldName'。 </li>";
          help += "<li>该SQL参数支持系统的表达式，什么是ccflow的表达式请查看说明书。</li>";
          help += "<li>系统就会获取该返回的值把它转化为decimal类型</li>";
          help += "<li>如果该值大于零，该条件就是成立的否则不成立。</li>";
          help += "</ul>";
          BaseModel base=new BaseModel(req, res);
          ui.append(base.AddEasyUiPanelInfo("帮助",  help));
      }

}
