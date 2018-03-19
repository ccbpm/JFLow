package cn.jflow.model.designer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.GPM.Station;
import BP.GPM.StationType;
import BP.WF.Template.Cond;
import BP.WF.Template.CondType;
import BP.WF.Template.ConnDataFrom;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.TextBox;

public class CondStationModel {
	private HttpServletRequest request;
	private HttpServletResponse respone;
	public CondStationModel(HttpServletRequest request,HttpServletResponse respone)
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
	public void setMuPK(String myPK) {
		MyPK = myPK;
	}
//	public new string MyPK
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
		return request.getParameter("FK_Flow");
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
	private int FK_Attr;
      public int getFK_Attr() {
    	  try
          {
              return Integer.parseInt(request.getParameter("FK_Attr"));//int.Parse(this.Request.QueryString["FK_Attr"]);
          }
          catch(Exception e)
          {
              try
              {
                  return this.getDDL_Attr().getSelectedItemIntVal();
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
//	public int FK_Attr
//      {
//          get
//          {
//              try
//              {
//                  return int.Parse(this.Request.QueryString["FK_Attr"]);
//              }
//              catch
//              {
//                  try
//                  {
//                      return this.DDL_Attr.SelectedItemIntVal;
//                  }
//                  catch
//                  {
//                      return 0;
//                  }
//              }
//          }
//      }
      /// <summary>
      /// 节点
      /// </summary>
	private int FK_Node;
      public int getFK_Node() {
    	  try {
    		  return Integer.parseInt(request.getParameter("FK_Node"));
		} catch (Exception e) {
			return 0;
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
//                  return 0;
//              }
//          }
//      }
	private int FK_MainNode;
      public int getFK_MainNode() {
    	//suxd
  		//问题：原始判断(request.getParameter("FK_MainNode") == null)成立时，
  		//如果request.getParameter("FK_MainNode")==""时Integer.parseInt("")会报错
  		//解决：判断request.getParameter("FK_MainNode") == null 替换为  StringUtils.isEmpty(request.getParameter("FK_MainNode"))
  		return  StringUtils.isEmpty(request.getParameter("FK_MainNode")) ? 0 : Integer
  				.parseInt(request.getParameter("FK_MainNode"));
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
    	  try
          {
              return Integer.parseInt(request.getParameter("ToNodeID"));//int.Parse(this.Request.QueryString["ToNodeID"]);
          }
          catch(Exception e)
          {
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
	public CondStationModel()
	{
		
	}
      private int HisCondType;
      public int getHisCondType() {
		return Integer.parseInt(request.getParameter("CondType").trim());
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
	private String GetOperValText;
      public String getGetOperValText() {
    	  if (Pub1.GetUIByID("TB_Val")!=null)
    	  {
    		  return ((TextBox)Pub1.GetUIByID("TB_Val")).getText();
    	  }
          return ((DDL)Pub1.GetUIByID("DDL_Val")).getSelectedItem().getText();//this.Pub1.GetDDLByID("DDL_Val").SelectedItem.Text;
	}
	public void setGetOperValText(String getOperValText) {
		GetOperValText = getOperValText;
	}
//	public string GetOperValText
//      {
//          get
//          {
//              if (this.Pub1.IsExit("TB_Val"))
//                  return this.Pub1.GetTBByID("TB_Val").Text;
//              return this.Pub1.GetDDLByID("DDL_Val").SelectedItem.Text;
//          }
//      }
      //#endregion 属性
	public UiFatory Pub1=null;
      public void Page_Load()
      {
    	  Pub1=new UiFatory();
    	  String dotype=request.getParameter("DoType")==null?"":request.getParameter("DoType");
    	  if(dotype.equals("Del"))
    	  {
    		  Cond nd = new Cond(this.getMyPK());
              nd.Delete();
              try {
				respone.sendRedirect("CondStation.jsp?CondType=" + getHisCondType() + "&FK_Flow=" + this.getFK_Flow() + "&FK_MainNode=" + nd.getNodeID() + "&FK_Node=" + this.getFK_MainNode() + "&ToNodeID=" + nd.getToNodeID());
			} catch (IOException e) {
				e.printStackTrace();
			}
              //this.Response.Redirect("CondStation.aspx?CondType=" + HisCondType.getValue() + "&FK_Flow=" + this.FK_Flow + "&FK_MainNode=" + nd.getNodeID() + "&FK_Node=" + this.FK_MainNode + "&ToNodeID=" + nd.getToNodeID(), true);
              return;
    	  }
//          if (this.Request.QueryString["DoType"] == "Del")
//          {
//              Cond nd = new Cond(this.MyPK);
//              nd.Delete();
//              this.Response.Redirect("CondStation.aspx?CondType=" + (int)this.HisCondType + "&FK_Flow=" + this.FK_Flow + "&FK_MainNode=" + nd.NodeID + "&FK_Node=" + this.FK_MainNode + "&ToNodeID=" + nd.ToNodeID, true);
//              return;
//          }

          this.BindCond();
      }
      public void BindCond()
      {
          String msg = "";
          String note = "";

          Cond cond = new Cond();
          cond.setMyPK(this.getGenerMyPK());
          cond.RetrieveFromDBSources();

         /* if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
          {
              SysEnums ses = new SysEnums("StaGrade");
              Stations sts = new Stations();
              sts.RetrieveAll();

              String sql = "SELECT No,Name FROM Port_Station WHERE StaGrade  NOT IN (SELECT IntKey FROM Sys_Enum WHERE EnumKey='StaGrade')";
              DataTable dt = DBAccess.RunSQLReturnTable(sql);
              if (dt.Rows.size() != 0)
              {
                  if(ses.size() == 0)
                  {
                      SysEnum se = new SysEnum();
                      se.setEnumKey("StaGrade");
                      se.setLab("普通岗");
                      se.setIntKey(0);
                      se.Insert();

                      ses.AddEntity(se);
                  }

                  for (int i = 0; i < sts.size(); i++) {
                	  Station st=(Station) sts.get(i);
                	  st.setStaGrade(0);
                      st.Save();
				}

              }

              this.Pub1.append(BaseModel.AddTable("class='Table' cellSpacing='0' cellPadding='0'  border='0' style='width:100%'"));
              for (int j = 0; j < ses.size(); j++) {
            	  SysEnum se=(SysEnum) ses.get(j);
            	  this.Pub1.append(BaseModel.AddTR());
                  CheckBox mycb = Pub1.creatCheckBox("CB_s_d" + se.getIntKey());
                  mycb.setText(se.getLab());
                  //mycb.setId("CB_s_d" + se.getIntKey());
                  this.Pub1.append("\n<TD nowrap = 'nowrap' colspan=3 class='GroupTitle'>");
                  this.Pub1.append(mycb);
                  this.Pub1.append("</TD>");
                  //this.Pub1.append(BaseModel.AddTD("colspan=3 class='GroupTitle'", mycb));
                  this.Pub1.append(BaseModel.AddTREnd());

                  int i = 0;
                  String ctlIDs = "";

                  for (int k = 0; k < sts.size(); k++) {
                	  Station st=(Station) sts.get(k);
                	  if (st.getStaGrade() != se.getIntKey())
                          continue;

                      i++;

                      if (i == 4)
                          i = 1;

                      if (i == 1)
                          Pub1.append(BaseModel.AddTR());

                      CheckBox cb = Pub1.creatCheckBox("CB_" + st.getNo());
                      //cb.setId("CB_" + st.getNo());
                      ctlIDs += cb.getId() + ",";
                      cb.setText(st.getName());
                      if (cond.getOperatorValue().toString().contains("@" + st.getNo() + "@"))
                          cb.setChecked(true);
                      Pub1.append(BaseModel.AddTD(cb));

                      if (i == 3)
                          Pub1.append(BaseModel.AddTREnd());
                  	}
//                  for (Station st : sts)
//                  {
//                      
//                  }
                  mycb.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");
                  //mycb.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";

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
			}
//              for (SysEnum se : ses)
//              {
//                  
//              }
          }
          else
          {*/
              /*BPM 模式*/
              BP.GPM.StationTypes tps = new BP.GPM.StationTypes();
              tps.RetrieveAll();

              BP.GPM.Stations sts = new BP.GPM.Stations();
              sts.RetrieveAll();

              String sql = "SELECT No,Name FROM Port_Station WHERE FK_StationType NOT IN (SELECT No FROM Port_StationType)";
              DataTable dt = DBAccess.RunSQLReturnTable(sql);
              if (dt.Rows.size() != 0)
              {
                  if(tps.size() == 0)
                  {
                	  StationType stp = new StationType();
                	 // { No = "01", Name = "普通岗" };
                	  stp.setNo("01");
                	  stp.setName("普通岗");
                      stp.Save();
                      
                      tps.AddEntity(stp);
                  }

                  //更新所有对不上岗位类型的岗位，岗位类型为01或第一个
                  for (int j = 0; j < sts.size(); j++) {
                	  Station st=(Station) sts.get(j);
                	  st.setFK_StationType(((StationType)tps.get(0)).getNo());//tps.get(0).); = tps[0].No;
                      st.Update();
				}
              }
              this.Pub1.append(BaseModel.AddTable("class='Table' cellSpacing='0' cellPadding='0'  border='0' style='width:100%'"));
              
              for (int j = 0; j < tps.size(); j++) {
            	  StationType tp=(StationType) tps.get(j);
            	  this.Pub1.append(BaseModel.AddTR());
                  CheckBox mycb = Pub1.creatCheckBox("CB_s_d" + tp.getNo());
                  mycb.setText(tp.getName());
                  this.Pub1.append("\n<TD nowrap = 'nowrap' colspan=3 class='GroupTitle'>");
                  this.Pub1.append(mycb);
                  this.Pub1.append("</TD>");
                  this.Pub1.append(BaseModel.AddTREnd());

                  int i = 0;
                  String ctlIDs = "";
                  for (int k = 0; k < sts.size(); k++) {
                	  Station st=(Station) sts.get(k);
                	  if (!st.getFK_StationType().equals(tp.getNo())){
                          continue;
                	  }
                      i++;
                      if (i == 4)i = 1;
                      if (i == 1){
                          Pub1.append(BaseModel.AddTR());
                      }
                      CheckBox cb = Pub1.creatCheckBox("CB_" + st.getNo());
                      ctlIDs += cb.getId() + ",";
                      cb.setText(st.getName());
                      if (cond.getOperatorValue().toString().contains("@" + st.getNo() + "@"))
                          cb.setChecked(true);
                      this.Pub1.append(BaseModel.AddTD(cb));
                      if (i == 3)
                          Pub1.append(BaseModel.AddTREnd());
				    }

                  mycb.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");
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
              }
			//}
          
          

          this.Pub1.append(BaseModel.AddTableEnd());
          Pub1.append(BaseModel.AddBR());
          
         /* DDL ddl = new DDL();
  		  ddl.setId("DDL_" + CondAttr.SpecOperWay); 
  		  ddl.Items.add(new ListItem("当前操作员", "0"));
  		  ddl.Items.add(new ListItem("指定节点的操作员", "1"));
  		  ddl.Items.add(new ListItem("指定表单字段作为操作员", "2"));
  		  ddl.Items.add(new ListItem("指定操作员编号", "3"));
  		  ddl.SetSelectItem((int)cond.getSpecOperWay().getValue());
  		  ddl.addAttr("onchange", "ss()");
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
  		
  		TextBox tb = new TextBox();
  		tb.setId("TB_" + CondAttr.SpecOperPara);
  		//tb.setWidth(200);
  		tb.setText(cond.getSpecOperPara());
  		tb.setEnabled(cond.getSpecOperWay() != SpecOperWay.CurrOper);
  		Pub1.append(tb);
  		Pub1.append(BaseModel.AddSpace(1));
  		Pub1.append("多个值请用英文“逗号”来分隔。");
  		Pub1.append(BaseModel.AddBR());
		  Pub1.append(BaseModel.AddBR());

          
          Pub1.append(BaseModel.AddSpace(1));

          LinkButton btn = Pub1.creatLinkButton(false, NamesOfBtn.Save.getCode(), "保存");
          btn.setHref("btn_save()");
          this.Pub1.append(btn);
          Pub1.append(BaseModel.AddSpace(1));

          btn = Pub1.creatLinkButton(false, NamesOfBtn.Delete.getCode(), "删除");
          btn.addAttr("onclick", " return confirm('您确定要删除吗？');");
          btn.setHref("btn_del()");
          this.Pub1.append(btn);*/
      }
   

      private Label Lab_Msg;
      public Label getLab_Msg() {
		return (Label) Pub1.GetUIByID("Lab_Msg");
	}
	public void setLab_Msg(Label lab_Msg) {
		Lab_Msg = lab_Msg;
	}
//	public Label Lab_Msg
//      {
//          get
//          {
//              return this.Pub1.GetLabelByID("Lab_Msg");
//          }
//      }
	private Label Lab_Note;
      public Label getLab_Note() {
		return (Label) Pub1.GetUIByID("Lab_Note");
	}
	public void setLab_Note(Label lab_Note) {
		Lab_Note = lab_Note;
	}
//	public Label Lab_Note
//      {
//          get
//          {
//              return this.Pub1.GetLabelByID("Lab_Note");
//          }
//      }
      /// <summary>
      /// 属性
      /// </summary>
      private DDL DDL_Attr;
      public DDL getDDL_Attr() {
		return (DDL) Pub1.GetUIByID("DDL_Attr");
	}
	public void setDDL_Attr(DDL dDL_Attr) {
		DDL_Attr = dDL_Attr;
	}
//	public DDL DDL_Attr
//      {
//          get
//          {
//              return this.Pub1.GetDDLByID("DDL_Attr");
//          }
//      }
	private DDL DDL_Oper;
      public DDL getDDL_Oper() {
		return (DDL) Pub1.GetUIByID("DDL_Oper");
	}
	public void setDDL_Oper(DDL dDL_Oper) {
		DDL_Oper = dDL_Oper;
	}
//	public DDL DDL_Oper
//      {
//          get
//          {
//              return this.Pub1.GetDDLByID("DDL_Oper");
//          }
//      }
	private DDL DDL_ConnJudgeWay;
      public DDL getDDL_ConnJudgeWay() {
		return (DDL) Pub1.GetUIByID("DDL_ConnJudgeWay");
	}
	public void setDDL_ConnJudgeWay(DDL dDL_ConnJudgeWay) {
		DDL_ConnJudgeWay = dDL_ConnJudgeWay;
	}
//	public DDL DDL_ConnJudgeWay
//      {
//          get
//          {
//              return this.Pub1.GetDDLByID("DDL_ConnJudgeWay");
//          }
//      }
	private String GenerMyPK;
      public String getGenerMyPK() {
    	  return this.getFK_MainNode() + "_" + getToNodeID() + "_" + CondType.forValue(getHisCondType()) + "_" + ConnDataFrom.Stas;
	}
	public void setGenerMyPK(String generMyPK) {
		GenerMyPK = generMyPK;
	}
//	public string GenerMyPK
//      {
//          get
//          {
//              return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.Stas.ToString();
//          }
//      }
}
