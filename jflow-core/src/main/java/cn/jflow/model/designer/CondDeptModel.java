package cn.jflow.model.designer;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import BP.Port.Dept;
import BP.Port.Depts;
import BP.WF.Node;
import BP.WF.Template.Cond;
import BP.WF.Template.CondType;
import BP.WF.Template.ConnDataFrom;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.TextBox;

public class CondDeptModel {
	public StringBuffer Pub1=new StringBuffer();
	public Map<String,Object> map=null;
	private HttpServletRequest request;
	private HttpServletResponse respone;
	public CondDeptModel()
	{
		
	}
	public CondDeptModel(HttpServletRequest request,HttpServletResponse respone)
	{
		this.request=request;
		this.respone=respone;
	}
	 //#region 属性
     /// <summary>
     /// 主键
     /// </summary>
	private String MyPK; 
     public String getMyPK() {
		return request.getParameter("MyPK");
	}
	public void setMyPK(String myPK) {
		MyPK = myPK;
	}
//	public new string MyPK
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
		return request.getParameter("FK_Flow");
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}

	private int FK_Attr;
     public int getFK_Attr() {
    	 try {
    		 return Integer.parseInt(request.getParameter("FK_Attr"));
		} catch (Exception e) {
			try
            {
                return DDL_Attr.getSelectedItemIntVal();
            }
            catch(Exception e1)
            {
                return 0;
            }
		}
	}
	public void setFK_Attr(int fK_Attr) {
		FK_Attr = fK_Attr;
	}

     /// <summary>
     /// 节点
     /// </summary>
	private int FK_Node;
     public int getFK_Node() {
    	//sunxd
  		//问题：原始判断(request.getParameter("FK_Node") == null)成立时，
  		//如果request.getParameter("FK_Node")==""时Integer.parseInt("")会报错
  		//解决：判断request.getParameter("FK_Node") == null 替换为  StringUtils.isEmpty(request.getParameter("FK_Node"))
  		return  StringUtils.isEmpty(request.getParameter("FK_Node")) ? 0 : Integer
  				.parseInt(request.getParameter("FK_Node"));
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}

	private int FK_MainNode;
     public int getFK_MainNode() {
    	//sunxd
  		//问题：原始判断(request.getParameter("FK_MainNode") == null)成立时，
  		//如果request.getParameter("FK_MainNode")==""时Integer.parseInt("")会报错
  		//解决：判断request.getParameter("FK_MainNode") == null 替换为  StringUtils.isEmpty(request.getParameter("FK_MainNode"))
  		return  StringUtils.isEmpty(request.getParameter("FK_MainNode")) ? 0 : Integer
  				.parseInt(request.getParameter("FK_MainNode"));
	}
	public void setFK_MainNode(int fK_MainNode) {
		FK_MainNode = fK_MainNode;
	}
	private int ToNodeID;
     public int getToNodeID() {
    	 try {
    		 return Integer.parseInt(request.getParameter("ToNodeID"));
		} catch (Exception e) {
			return 0;
		}
		
	}
	public void setToNodeID(int toNodeID) {
		ToNodeID = toNodeID;
	}

     /// <summary>
     /// 执行类型
     /// </summary>
     public int HisCondType;
     public int getHisCondType() {
		return Integer.parseInt(request.getParameter("CondType").trim());
	}
	public void setHisCondType(int hisCondType) {
		HisCondType = hisCondType;
	}

	private String GetOperValText;
     public String getGetOperValText() {
    	 if (map.get("TB_Val")!=null)
             return ((TextBox)map.get("TB_Val")).getText();
         return ((DDL)map.get("DDL_Val")).getSelectedItem().getText();
	}
	public void setGetOperValText(String getOperValText) {
		GetOperValText = getOperValText;
	}

     //#endregion 属性

     public void Page_Load() throws IOException
     {
         if ("Del".equals(request.getParameter("DoType")))
         {
             Cond nd = new Cond(this.getMyPK());
             nd.Delete();
             respone.sendRedirect("CondDept.jsp?CondType=" + getHisCondType() + "&FK_Flow=" + this.getFK_Flow() + "&FK_MainNode=" + nd.getNodeID() + "&FK_Node=" + this.getFK_MainNode() + "&ToNodeID=" + nd.getToNodeID());
             return;
         }

         this.BindCond();
     }

     public void BindCond()
     {
         Cond cond = new Cond();
         cond.setMyPK(getGenerMyPK());
         cond.RetrieveFromDBSources();

         Node nd = new Node(this.getFK_MainNode());
         Node tond = new Node(this.getToNodeID());

         this.Pub1.append(BaseModel.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
         this.Pub1.append(BaseModel.AddTR());
         this.Pub1.append(BaseModel.AddTD("colspan=3 class='GroupTitle'", "部门选择"));
         this.Pub1.append(BaseModel.AddTREnd());

         Depts sts = new Depts();
         sts.RetrieveAllFromDBSource();

         int i = 0;

         for (int j = 0; j < sts.size(); j++) {
        	 Dept st=(Dept)sts.get(j);
        	 i++;

             if (i == 4)
                 i = 1;

             if (i == 1)
                 Pub1.append(BaseModel.AddTR());

             CheckBox cb = new CheckBox();
             cb.setId("CB_" + st.getNo());
             cb.setText(st.getName());
             if (cond.getOperatorValue().toString().contains("@_" + st.getNo() + "@"))
                 cb.setChecked(true);

             //map.put("CB_" + st.getNo(), cb);
             this.Pub1.append(BaseModel.AddTD(cb));

             if (i == 3)
                 Pub1.append(BaseModel.AddTREnd());
		}


         switch (i)
         {
             case 1:
                 Pub1.append(BaseModel.AddTD());
                 Pub1.append(BaseModel.AddTD());
                 Pub1.append(BaseModel.AddTREnd());
                 break;
             case 2:
                 Pub1.append(BaseModel.AddTD());
                 Pub1.append(BaseModel.AddTREnd());
                 break;
             default:
                 break;
         }

        this.Pub1.append(BaseModel.AddTableEnd());
         Pub1.append(BaseModel.AddBR());
         
         /*  DDL ddl = new DDL();
 		  ddl.setId("DDL_" + CondAttr.SpecOperWay); 
 		 // ddl.setWidth(200);
 		  ddl.Items.add(new ListItem("当前操作员", "0"));
 		  ddl.Items.add(new ListItem("指定节点的操作员", "1"));
 		  ddl.Items.add(new ListItem("指定表单字段作为操作员", "2"));
 		  ddl.Items.add(new ListItem("指定操作员编号", "3"));
 		  ddl.SetSelectItem((int)cond.getSpecOperWay().getValue());
 		  //ddl.AutoPostBack = true;
 		 // ddl.SelectedIndexChanged += new EventHandler(ddl_SelectedIndexChanged);
 		  Pub1.append("指定的操作员：");
 		  Pub1.append(ddl);
 		  Pub1.append(BaseModel.AddBR());
 		  Pub1.append(BaseModel.AddBR());

 		 Label lbl = new Label();
 		 lbl.setId("LBL1");

 		switch (cond.getSpecOperWay()) {
 			case SpecNodeOper:
 				lbl.setText("节点编号：");
 				break;
 			case SpecSheetField:
 				lbl.setText("表单字段：");
 				break;
 			case SpenEmpNo:
 				lbl.setText("操作员编号：");
 				break;
 			case CurrOper:
 				lbl.setText("参数：");
 				break;
 		}

 		Pub1.append(lbl);

 		TB tb = new TB();
 		tb.ID = "TB_" + CondAttr.SpecOperPara;
 		tb.setWidth(200);
 		tb.setText(cond.getSpecOperPara());
 		tb.setEnabled(cond.getSpecOperWay() != SpecOperWay.CurrOper);
 		Pub1.append(tb);
 		Pub1.append(BaseModel.AddSpace(1));
 		Pub1.append("多个值请用英文“逗号”来分隔。");
 		Pub1.append(BaseModel.AddBR());
		  Pub1.append(BaseModel.AddBR());

         Pub1.append(BaseModel.AddSpace(1));

         LinkButton btn = new LinkButton(false, NamesOfBtn.Save.getCode(), "保存");
         btn.setHref("onsave()");
         this.Pub1.append(btn);
         Pub1.append(BaseModel.AddSpace(1));

         btn = new LinkButton(false, NamesOfBtn.Delete.getCode(), "删除");
         btn.addAttr("onclick", " return confirm('您确定要删除吗？');");
         btn.setHref("btn_Del_Click()");
         this.Pub1.append(btn);*/
         
     }

     public Label Lab_Msg;
     public Label getLab_Msg() {
		return (Label) map.get("Lab_Msg");
	}
	public void setLab_Msg(Label lab_Msg) {
		Lab_Msg = lab_Msg;
	}

//	
//         get
//         {
//             return this.Pub1.GetLabelByID("Lab_Msg");
//         }{
//     }

     public Label Lab_Note;
     public Label getLab_Note() {
		return (Label) map.get("Lab_Note");
	}
	public void setLab_Note(Label lab_Note) {
		Lab_Note = lab_Note;
	}

//	{
//         get
//         {
//             return this.Pub1.GetLabelByID("Lab_Note");
//         }
//     }

     /// <summary>
     /// 属性
     /// </summary>
     private DDL DDL_Attr;
     public DDL getDDL_Attr() {
		return (DDL) map.get("DDL_Attr");
	}
	public void setDDL_Attr(DDL dDL_Attr) {
		DDL_Attr = dDL_Attr;
	}

//	{
//         get
//         {
//             return this.Pub1.GetDDLByID("DDL_Attr");
//         }
//     }

     private DDL DDL_Oper;
     public DDL getDDL_Oper() {
		return (DDL) map.get("DDL_Oper");
	}
	public void setDDL_Oper(DDL dDL_Oper) {
		DDL_Oper = dDL_Oper;
	}

//	{
//         get
//         {
//             return this.Pub1.GetDDLByID("DDL_Oper");
//         }
//     }

	
     private DDL DDL_ConnJudgeWay;
     public DDL getDDL_ConnJudgeWay() {
		return (DDL) map.get("DDL_ConnJudgeWay");
	}
	public void setDDL_ConnJudgeWay(DDL dDL_ConnJudgeWay) {
		DDL_ConnJudgeWay = dDL_ConnJudgeWay;
	}

//	{
//         get
//         {
//             return this.Pub1.GetDDLByID("DDL_ConnJudgeWay");
//         }
//     }

     private String GenerMyPK;
     public String getGenerMyPK() {
    	 return this.getFK_MainNode() + "_" + getToNodeID() + "_" + CondType.forValue(getHisCondType()) + "_" + ConnDataFrom.Depts;
	}
	public void setGenerMyPK(String generMyPK) {
		GenerMyPK = generMyPK;
	}

//	{
//         get
//         {
//             return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.Depts.ToString();
//         }
//     }

}
