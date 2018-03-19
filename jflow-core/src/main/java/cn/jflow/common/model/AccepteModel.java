package cn.jflow.common.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.Attrs;
import BP.Sys.OSModel;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.DeliveryWay;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.TurnToDeal;
import BP.WF.Work;
import BP.WF.WorkNode;
import BP.WF.Data.GERpt;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondModel;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeStations;
import BP.WF.Template.Selector;
import BP.WF.Template.SelectorModel;
import BP.WF.Template.TurnTo;
import BP.WF.Template.TurnTos;
import BP.Web.WebUser;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;


public class AccepteModel extends BaseModel{
	
	public StringBuffer Pub1=null;

	public StringBuilder Left = null;
	
	public AccepteModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Pub1=new StringBuffer();
		Left=new StringBuilder();
	}	
	//#region 属性.
     /// <summary>
     /// 打开
     /// </summary>
     public int getIsWinOpen()
     {
             String str = this.get_request().getParameter("IsWinOpen");
             if (str == null || "".equals(str) || "1".equals(str))
                 return 1;
             return 0;
         
     }
     /// <summary>
     /// 到达的节点
     /// </summary>
     public int getToNode()
     {
             if (this.get_request().getParameter("ToNode") == null)
                 return 0;
             return Integer.parseInt(this.get_request().getParameter("ToNode"));
     }
//     public int getFK_Node()
//     {
//             return Integer.parseInt(this.get_request().getParameter("FK_Node"));
//     }
//     public int getWorkID()
//     {
//             return Integer.parseInt(this.get_request().getParameter("WorkID"));
//     }
//     public int getFID()
//     {
//             if (this.get_request().getParameter("FID") != null)
//                 return Integer.parseInt(this.getParameter("FID"));
//             return 0;
//         
//     }
     public String getFK_Dept()
     {
             String s = this.getParameter("FK_Dept");
             if (s == null)
                 s = WebUser.getFK_Dept();
             return s;
     }
     public String getFK_Station()
     {
             return this.get_request().getParameter("FK_Station");
     }
     public String getWorkIDs()
     {
             return this.get_request().getParameter("WorkIDs");
     }
     public String getDoFunc()
     {
             return this.get_request().getParameter("DoFunc");
     }
     public String getCFlowNo()
     {
             return this.get_request().getParameter("CFlowNo");
     }
//     public String getFK_Flow()
//     {
//             return this.get_request().getParameter("FK_Flow");
//     }

     private boolean IsMultiple = false;
     /// <summary>
     /// 获取传入参数
     /// </summary>
     /// <param name="param">参数名</param>
     /// <returns></returns>
//     public String getUTF8ToString(String param)
//     {
//         return HttpUtility.UrlDecode(this.Request[param], System.Text.Encoding.UTF8);
//     }

     
     public DataTable GetTable() throws Exception
     {
         if (this.getToNode() == 0)
             throw new Exception("@流程设计错误，没有转向的节点。举例说明: 当前是A节点。如果您在A点的属性里启用了[接受人]按钮，那么他的转向节点集合中(就是A可以转到的节点集合比如:A到B，A到C, 那么B,C节点就是转向节点集合)，必须有一个节点是的节点属性的[访问规则]设置为[由上一步发送人员选择]");

         NodeStations stas = new NodeStations(this.getToNode());
         if (stas.size() == 0)
         {
             Node toNd = new Node(this.getToNode());
             throw new Exception("@流程设计错误：设计员没有设计节点[" + toNd.getName() + "]，接受人的岗位范围。");
         }

         String BindByStationSql = "";
         if (this.get_request().getParameter("IsNextDept") != null)
         {
             int len = this.getFK_Dept().length() + 2;
             String sqlDept = "SELECT No FROM Port_Dept WHERE " + SystemConfig.getAppCenterDBLengthStr() + "(No)=" + len + " AND No LIKE '" + this.getFK_Dept() + "%'";
             BindByStationSql = "SELECT A.No,A.Name, A.FK_Dept, B.Name as DeptName FROM Port_Emp A,Port_Dept B WHERE A.FK_Dept=B.No AND a.NO IN ( ";
             BindByStationSql += "SELECT FK_EMP FROM Port_EmpSTATION WHERE FK_STATION ";
             BindByStationSql += "IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node=" + this.getToNode() + ") ";
             BindByStationSql += ") AND A.No IN( SELECT No FROM Port_Emp WHERE  " + SystemConfig.getAppCenterDBLengthStr() + "(FK_Dept)=" + len + " AND FK_Dept LIKE '" + this.getFK_Dept() + "%')";
             BindByStationSql += " ORDER BY FK_DEPT ";
             return BP.DA.DBAccess.RunSQLReturnTable(BindByStationSql);
         }

         String ParSql = "select No from Port_Dept where ParentNo='0'";
         DataTable ParDt = DBAccess.RunSQLReturnTable(ParSql);
   
         // 优先解决本部门的问题。
         BindByStationSql = "select No,Name,ParentNo,'1' IsParent from Port_Dept where ParentNo='0' union" +
                                               " select No,Name,b.FK_Station as ParentNo,'0' IsParent  from Port_Emp a inner" +
                                               " join Port_DeptEmpStation b on a.No=b.FK_Emp and b.FK_Station in" +
                                               " (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"')  WHERE No in" +
                                               "  (SELECT FK_EMP FROM Port_DeptEmpStation " +
                                               " WHERE FK_STATION IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"'))" +
                                               " AND No IN (SELECT No FROM Port_Emp WHERE FK_Dept ='"+WebUser.getFK_Dept()+"') " +
                                               " union select No,Name,'"+ParDt.Rows.get(0).get(0)+"' ParentNo,'1' IsParent  from Port_Station where no " +
                                               "in(SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"')";
         String DdlEmpSql = "select No,Name from Port_Emp a inner" +
                                             " join Port_DeptEmpStation b on a.No=b.FK_Emp and b.FK_Station in" +
                                             " (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"')  WHERE No in" +
                                             "  (SELECT FK_EMP FROM Port_DeptEmpStation " +
                                             " WHERE FK_STATION IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"'))" +
                                             " AND No IN (SELECT No FROM Port_Emp WHERE FK_Dept ='"+ WebUser.getFK_Dept()+"')";

         if (this.getFK_Dept() == WebUser.getFK_Dept())
         {

             if (BP.WF.Glo.getOSModel() == OSModel.OneMore)
             {

             }
             else
             {
                 BindByStationSql.replace("Port_DeptEmpStation", "Port_EmpSTATION");
                 DdlEmpSql.replace("Port_DeptEmpStation", "Port_EmpSTATION");
             }
             return DBAccess.RunSQLReturnTable(BindByStationSql);
         }

         BindByStationSql = "select No,Name,ParentNo,'1' IsParent from Port_Dept where ParentNo='0' union" +
                                            " select No,Name,b.FK_Station as ParentNo,'0' IsParent  from Port_Emp a inner" +
                                            " join Port_EmpSTATION b on a.No=b.FK_Emp and b.FK_Station in" +
                                            " (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"')  WHERE No in" +
                                            "  (SELECT FK_EMP FROM Port_EmpSTATION " +
                                            " WHERE FK_STATION IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"'))" +
                                            " AND No IN (SELECT No FROM Port_Emp) " +
                                            " union select No,Name,'"+ParDt.Rows.get(0).get(0)+"' ParentNo,'1' IsParent  from Port_Station where no " +
                                            "in(SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"')";

         DdlEmpSql = "select No,Name,b.FK_Station as ParentNo,'0' IsParent  from Port_Emp a inner" +
                                            " join Port_EmpSTATION b on a.No=b.FK_Emp and b.FK_Station in" +
                                            " (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"')  WHERE No in" +
                                            "  (SELECT FK_EMP FROM Port_EmpSTATION " +
                                            " WHERE FK_STATION IN (SELECT FK_STATION FROM WF_NodeStation WHERE FK_Node='"+getToNode()+"'))" +
                                            " AND No IN (SELECT No FROM Port_Emp) ";

         return DBAccess.RunSQLReturnTable(BindByStationSql);
     }
     private Node _HisNode = null;
     /// <summary>
     /// 它的节点
     /// </summary>
     public Node getHisNode()
     {
             if (_HisNode == null)
                 _HisNode = new Node(this.getFK_Node());
             return _HisNode;
         
     }
     /// <summary>
     /// 是否多分支
     /// </summary>
     public boolean IsMFZ()
     {
             Nodes nds = this.getHisNode().getHisToNodes();
             int num = 0;
             for (Node mynd: nds.ToJavaList())
             {
                 //#region 过滤不能到达的节点.
                 Cond cond = new Cond();
				int i = cond.Retrieve(CondAttr.FK_Node, this.getHisNode().getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
                 if (i == 0)
                     continue; // 没有设置方向条件，就让它跳过去。
                 cond.setWorkID (this.getWorkID());
                 cond.en = getwk();

                 if (cond.getIsPassed() == false)
                     continue;
                 //#endregion 过滤不能到达的节点.

                 if (mynd.getHisDeliveryWay() == DeliveryWay.BySelected)
                 {
                     num++;
                 }
             }
             if (num == 0)
                 return false;
             if (num == 1)
                 return false;
             return true;
     }
     /// <summary>
     /// 绑定多分支
     /// </summary>
     public void BindMStations() throws Exception
     {

         this.BindByStation();

         Nodes mynds = this.getHisNode().getHisToNodes();
         this.Left.append("<fieldset><legend>&nbsp;选择方向:列出所选方向设置的人员&nbsp;</legend>");
         String str = "<p>";
         for (Node mynd: mynds.ToJavaList())
         {
             if (mynd.getHisDeliveryWay() != DeliveryWay.BySelected)
                 continue;

             //#region 过滤不能到达的节点.
             Cond cond = new Cond();
             int i = cond.Retrieve(CondAttr.FK_Node, this.getHisNode().getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
             if (i == 0)
                 continue; // 没有设置方向条件，就让它跳过去。

             cond.setWorkID(this.getWorkID());
             cond.en = getwk();
             if (cond.getIsPassed() == false)
                 continue;
             //#endregion 过滤不能到达的节点.

             if (this.getToNode() == mynd.getNodeID())
                 str += "&nbsp;&nbsp;<b class='l-link'><font color='red' >" + mynd.getName() + "</font></b>";
             else
                 str += "&nbsp;&nbsp;<b><a class='l-link' href='Accepter.jsp?FK_Node=" + this.getFK_Node() + "&type=1&ToNode=" + mynd.getNodeID() + "&WorkID=" + this.getWorkID() + "' >" + mynd.getName() + "</a></b>";
         }
         this.Left.append(str + "</p>");
         this.Left.append(AddFieldSetEnd());
     }

     public Selector mySelector = null;
     public GERpt _wk = null;
     public GERpt getwk()
     {
             if (_wk == null)
             {
                 _wk = this.getHisNode().getHisFlow().getHisGERpt();
                 _wk.setOID(this.getWorkID());
                 _wk.Retrieve();
                 _wk.ResetDefaultVal();
             }
             return _wk;
     }

     public void init() throws IOException
     {
//         this.Title = "选择下一步骤接受的人员";

         //判断是否需要转向。
         if (this.getToNode() == 0)
         {
             int num = 0;
             int tempToNodeID = 0;
             /*如果到达的点为空 */
             /*首先判断当前节点的ID，是否配置到了其他节点里面，
              * * 如果有则需要转向高级的选择框中去，当前界面不能满足公文类的选择人需求。*/
             String sql = "SELECT COUNT(*) FROM WF_Node WHERE FK_Flow='" + this.getHisNode().getFK_Flow() + "' AND " + NodeAttr.DeliveryWay + "=" + DeliveryWay.BySelected.getValue() + " AND " + NodeAttr.DeliveryParas + " LIKE '%" + this.getHisNode().getNodeID() + "%' ";

             if (DBAccess.RunSQLReturnValInt(sql, 0) > 0)
             {
                 /*说明以后的几个节点人员处理的选择 */
                 String url = "AccepterAdv.jsp?1=3" + this.get_request().getQueryString();
                 this.get_response().sendRedirect(url);
                 return;
             }

             Nodes nds = this.getHisNode().getHisToNodes();
             if (nds.size() == 0)
             {
                 this.Pub1.append(AddFieldSetRed("提示", "当前点是最后的一个节点，不能使用此功能。"));
                 return;
             }
             else if (nds.size() == 1)
             {
                 Node toND = (Node) nds.get(0);
                 tempToNodeID = toND.getNodeID();
             }
             else
             {
                 Node nd = new Node(this.getFK_Node());
                 for(Node mynd:nds.ToJavaList())
                 {
                     if (mynd.getHisDeliveryWay() != DeliveryWay.BySelected)
                         continue;

                     //#region 过滤不能到达的节点.
                     if (nd.getCondModel() == CondModel.ByLineCond)
                     {
                         Cond cond = new Cond();
                         int i = cond.Retrieve(CondAttr.FK_Node, this.getHisNode().getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
                         if (i == 0)
                             continue; // 没有设置方向条件，就让它跳过去。
                         cond.setWorkID(this.getWorkID());
                         cond.en = getwk();
                         if (cond.getIsPassed() == false)
                             continue;
                     }
                     //#endregion 过滤不能到达的节点.
                     tempToNodeID = mynd.getNodeID();
                     num++;
                 }
             }

             if (tempToNodeID == 0)
             {
                 this.winCloseWithMsg("@流程设计错误：\n\n 当前节点的所有分支节点没有一个接受人员规则为按照选择接受。");
                 return;
             }


             this.get_response().sendRedirect("Accepter.jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&ToNode=" + tempToNodeID + "&FID=" + this.getFID() + "&type=1&WorkID=" + this.getWorkID() + "&IsWinOpen=" + this.getIsWinOpen());
             return;
         }


         try
         {
             /* 首先判断是否有多个分支的情况。*/
             if (getToNode() == 0 && this.IsMFZ())
             {
                 IsMultiple = true;
                 //this.BindMStations();
                 return;
             }
             mySelector = new Selector(this.getToNode());
             
             SelectorModel sm=mySelector.getSelectorModel();
//             if(sm==SelectorModel.Station){
//            	 returnValue("BindByStation");
//             }
//             else if(sm==SelectorModel.SQL){
//            	 returnValue("BindBySQL");
//             }
//             else if(sm==SelectorModel.Dept){
//            	 returnValue("BindByDept");
//             }
//             else if(sm==SelectorModel.Emp){
//            	 returnValue("BindByEmp");
//             }
//             else if(sm==SelectorModel.Url){
//            	 if (MySelector.getSelectorP1().contains("?"))
//                     this.get_response().sendRedirect(MySelector.getSelectorP1() + "&WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node());
//                 else
//                     this.get_response().sendRedirect(MySelector.getSelectorP1() + "?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node());
//                 return;
//             }
//             else{
//            	 
//             }
             switch (mySelector.getSelectorModel())
 			{
 				case Station:
 					//this.BindByStation();
 					returnValue("BindByStation");
 					break;
 				case SQL:
 					//this.BindBySQL();
 					returnValue("BindBySQL");
 					break;
 				case Dept:
 					//this.BindByDept();
 					returnValue("BindByDept");
 					break;
 				case Emp:
 					//this.BindByEmp();
 					returnValue("BindByEmp");
 					break;
 				case Url:
 					if (mySelector.getSelectorP1().contains("?"))
 					{
 						try {
 							get_response().sendRedirect(mySelector.getSelectorP1() + "&WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node());
 						} catch (IOException e) {
 							e.printStackTrace();
 						}
 					}
 					else
 					{
 						try {
 							get_response().sendRedirect(mySelector.getSelectorP1() + "?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node());
 						} catch (IOException e) {
 							e.printStackTrace();
 						}
 					}
 					return;
 				default:
 					break;
 			}
         }
         catch (Exception ex)
         {
//             this.Pub1.clear();
             this.Pub1.append(AddMsgOfWarning("错误", ex.getMessage()));
         }
     }
     /// <summary>
     /// 按sql方式
     /// </summary>
     public String BindBySQL()
     {
         String sqlGroup = mySelector.getSelectorP1();
         sqlGroup = sqlGroup.replace("WebUser.No", WebUser.getNo());
         sqlGroup = sqlGroup.replace("@WebUser.Name", WebUser.getName());
         sqlGroup = sqlGroup.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

         String sqlDB = mySelector.getSelectorP2();
         sqlDB = sqlDB.replace("WebUser.No", WebUser.getNo());
         sqlDB = sqlDB.replace("@WebUser.Name", WebUser.getName());
         sqlDB = sqlDB.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

         DataTable ParDt = DBAccess.RunSQLReturnTable("select No from Port_Dept where ParentNo='0'");
         String BindBySQL = "select No,Name,FK_Dept as ParentNo,'0' IsParent from ("+sqlDB+") emp" +
                                          " union  select No,Name,'"+ParDt.Rows.get(0).get(0)+"' ParentNo,'1' IsParent from ("+sqlGroup+") dept" +
                                          " union  select No,Name,'0' ParentNo,'1' IsParent from Port_Dept where ParentNo='0'";
 

         DdlEmpSql = sqlDB;//No,Name没有的情况会报错
         DataTable BindBySQLDt = DBAccess.RunSQLReturnTable(BindBySQL);

         return GetTreeJsonByTable(BindBySQLDt, "NO", "NAME", "ParentNo", "0", "IsParent", "");

     }
     /// <summary>
     /// 按BindByEmp 方式
     /// </summary>
     public String BindByEmp()
     {

         String BindByEmpSql = "select No,Name,ParentNo,'1' IsParent  from Port_Dept   WHERE No IN (SELECT FK_Dept FROM " +
                                           "Port_Emp WHERE No in(SELECT FK_EMP FROM WF_NodeEmp WHERE FK_Node="+mySelector.getNodeID()+")) or ParentNo=0 union " +
                                           "select No,Name,FK_Dept as ParentNo,'0' IsParent  from Port_Emp  WHERE No in (SELECT FK_EMP " +
                                           "FROM WF_NodeEmp WHERE FK_Node="+mySelector.getNodeID()+")";
         DdlEmpSql = "select No,Name from Port_Emp  WHERE No in (SELECT FK_EMP " +
                                           "FROM WF_NodeEmp WHERE FK_Node="+mySelector.getNodeID()+")";
         DataTable BindByEmpDt = DBAccess.RunSQLReturnTable(BindByEmpSql);
         DataTable ParDt = DBAccess.RunSQLReturnTable("select No from Port_Dept where ParentNo='0'");
         for(DataRow r:BindByEmpDt.Rows)
         {
             if ("1".equals(r.get("IsParent")) && !"0".equals(r.get("ParentNo")))
             {
                 r.setValue("ParentNo",ParDt.Rows.get(0).get(0));
             }
         }
         return GetTreeJsonByTable(BindByEmpDt, "NO", "NAME", "ParentNo", "0", "IsParent", "");
     }
     public String DdlEmpSql = "";
     
 	private String initParameterValue(String param){
		return get_request().getParameter(param);
	}
     
     /// <summary>
     /// 返回值
     /// </summary>
     private void returnValue(String whichMet) throws Exception
     {
         
         

 		String method = "";
 		//返回值
 		String s_responsetext = "";

 		if (StringHelper.isNullOrEmpty(initParameterValue("method")))
 		{
 			return;
 		}

 		method = initParameterValue("method").toString();

 		if (method.equals("getTreeDateMet")) //获取数据
 		{
 				s_responsetext = getTreeDateMet(whichMet);
 		}
 		else if (method.equals("saveMet"))
 		{
 				saveMet();
 		}

 		if (StringHelper.isNullOrEmpty(s_responsetext))
 		{
 			s_responsetext = "";
 		}
 		s_responsetext = AppendJson(s_responsetext);
 		s_responsetext = DdlValue(s_responsetext, DdlEmpSql);
 		//组装ajax字符串格式,返回调用客户端 树型
 		printResult(s_responsetext);
     }
     
     private void printResult(String result){
 		get_response().setContentType("text/html; charset=utf-8");
 		PrintWriter out = null;
 		try {
 			out = get_response().getWriter();
 			out.write(result);
 			
 			out.flush();
 		} catch (IOException e) {
 			e.printStackTrace();
 		} finally{
 			if(out != null){
 				out.close();
 			}
 		}
 	}
     
     public String AppendJson(String json)
     {
         StringBuilder AppendJson = new StringBuilder();
         AppendJson.append(json);
         AppendJson.append(",CheId:");
         String alreadyHadEmps = "select No, Name from Port_Emp where No in( select FK_Emp from WF_SelectAccper " +
                                            "where FK_Node="+this.getToNode()+" and WorkID="+this.getWorkID()+")";
         DataTable dt = DBAccess.RunSQLReturnTable(alreadyHadEmps);
         AppendJson.append("[{\"id\":\"CheId\",\"iconCls\":\"icon-save\",\"text\":\"已选人员\",\"children\":[");
         for (int i = 0; i < dt.Rows.size(); i++)
         {
             AppendJson.append("{\"id\":\"" + dt.Rows.get(i).get(0) + "\",iconCls:\"icon-user\"" + ",\"text\":\"" + dt.Rows.get(i).get(1) + "\"");
             if (i == dt.Rows.size() - 1)
             {
                 AppendJson.append("}");
                 break;
             }
             AppendJson.append("},");
         }
         //AppendJson.Append("]}]}");

         AppendJson.append("]}]");

         AppendJson.insert(0, "{tt:");
         return AppendJson.toString();
     }
     public String DdlValue(String StrJson, String Str)
     {
         StringBuilder SBuilder = new StringBuilder();
         SBuilder.append(StrJson);
         DataTable dt = DBAccess.RunSQLReturnTable(Str);

         SBuilder.append(",ddl:[");
         for (int i = 0; i < dt.Rows.size(); i++)
         {
             if (i == 0)
             {
                 SBuilder.append("{\"id\":\"" + dt.Rows.get(i).get("No") + "\",\"text\":\"" + dt.Rows.get(i).get("Name") + "\",\"selected\":\"selected\"}");
             }
             else
             {
                 SBuilder.append("{\"id\":\"" + dt.Rows.get(i).get("No") + "\",\"text\":\"" + dt.Rows.get(i).get("Name") + "\"}");
             }
             if (i == dt.Rows.size() - 1)
             {
                 SBuilder.append("");
                 continue;
             }
             SBuilder.append(",");
         }
         SBuilder.append("]}");
         return SBuilder.toString();
     }
     public String getTreeDateMet(String Met) throws Exception
     {
    	 if(Met.equals("BindByEmp")){
    		 return BindByEmp();
    	 }
    	 else if(Met.equals("BindByDept")){
    		 return BindByDept();
    	 }
    	 else if(Met.equals("BindByStation")){
    		 return BindByStation();
    	 }
    	 else if(Met.equals("BindBySQL")){
    		 return BindBySQL();
    	 }
    	 else{
    		 return "";
    	 }
    	 
     }
     public String BindByDept()
     {

         String BindByDeptSql = "SELECT  No,Name,ParentNo,'1' IsParent  FROM Port_Dept WHERE No IN (SELECT " +
                                              "FK_Dept FROM WF_NodeDept WHERE FK_Node="+mySelector.getNodeID()+") or ParentNo=0 union SELECT No,Name,FK_Dept " +
                                              "as ParentNo,'0' IsParent FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node="+mySelector.getNodeID()+")";

         DdlEmpSql = "SELECT No,Name FROM Port_Emp WHERE FK_Dept IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node="+mySelector.getNodeID()+")";


         DataTable BindByDeptDt = DBAccess.RunSQLReturnTable(BindByDeptSql);
         DataTable ParDt = DBAccess.RunSQLReturnTable("select No from Port_Dept where ParentNo='0'");
         for(DataRow r:BindByDeptDt.Rows)
         {
             if (r.get("IsParent").toString().equals("1") && !r.get("ParentNo").toString().equals("0"))
             {
                 r.setValue("ParentNo",ParDt.Rows.get(0).get(0));
             }
         }
         return GetTreeJsonByTable(BindByDeptDt, "NO", "NAME", "ParentNo", "0", "IsParent", "");
     }
     /// <summary>
     /// 按table方式.
     /// </summary>
     public void BindBySQL_Table(DataTable dtGroup, DataTable dtObj)
     {
         int col = 4;
         this.Pub1.append(AddTable("style='border:0px;width:100%'"));
         for(DataRow drGroup:dtGroup.Rows)
         {
             String ctlIDs = "";
             String groupNo = drGroup.get(0).toString();

             //增加全部选择.
             this.Pub1.append(AddTR());
             CheckBox cbx = new CheckBox();
             cbx.setId("CBs_" + drGroup.get(0).toString());
             cbx.setText(drGroup.get(1).toString());
             this.Pub1.append(AddTDTitle("align=left", cbx.toString()));
             this.Pub1.append(AddTREnd());

             this.Pub1.append(AddTR());
             this.Pub1.append(AddTDBegin("nowarp=false"));

             this.Pub1.append(AddTable("style='border:0px;width:100%'"));
             int colIdx = -1;
             for(DataRow drObj:dtObj.Rows)
             {
                 String no = drObj.get(0).toString();
                 String name = drObj.get(1).toString();
                 String group = drObj.get(2).toString();
                 if (group.trim() != groupNo.trim())
                     continue;

                 colIdx++;
                 if (colIdx == 0)
                     this.Pub1.append(AddTR());

                 CheckBox cb = new CheckBox();
                 cb.setId("CB_" + no);
                 ctlIDs += cb.getId() + ",";
                 cb.addAttr("onclick","isChange=true;");
                 cb.setText(name);
                 cb.setChecked(false);
                 if (cb.getChecked())
                     cb.setText("<font color=green>" + cb.getText() + "</font>");
                 this.Pub1.append(AddTD(cb));
                 if (col - 1 == colIdx)
                 {
                     this.Pub1.append(AddTREnd());
                     colIdx = -1;
                 }
             }
             cbx.addAttr("onclick","SetSelected(this,'" + ctlIDs + "')");

             if (colIdx != -1)
             {
                 while (colIdx != col - 1)
                 {
                     colIdx++;
                     this.Pub1.append(AddTD());
                 }
                 this.Pub1.append(AddTREnd());
             }
             this.Pub1.append(AddTableEnd());
             this.Pub1.append(AddTDEnd());
             this.Pub1.append(AddTREnd());
         }
         this.Pub1.append(AddTableEnd());

         this.BindEnd();
     }

     public void BindBySQL_Tree(DataTable dtGroup, DataTable dtDB)
     {
     }

     public String BindByStation() throws Exception
     {
         return GetTreeJsonByTable(this.GetTable(), "No", "Name", "ParentNo", "0", "IsParent", "");
     }
     /// <summary>
     /// 处理绑定结束
     /// </summary>
     public void BindEnd()
     {
         Button btn = new Button();
         if (this.getIsWinOpen() == 1)
         {
             btn.setText("确定并关闭");
             btn.setId("Btn_Save");
             btn.setCssClass("Btn");
             btn.setType("submit");
//             btn.Click += new EventHandler(btn_Save_Click);
             this.Pub1.append(btn);
         }
         else
         {
             btn = new Button();
             btn.setText("确定并发送");
             btn.setId("Btn_Save");
             btn.setCssClass("Btn");
             btn.setText("submit");
//             btn.Click += new EventHandler(btn_Save_Click);
             this.Pub1.append(btn);

             btn = new Button();
             btn.setText("取消并返回");
             btn.setId("Btn_Cancel");
             btn.setCssClass("Btn");
//             btn.Click += new EventHandler(btn_Save_Click);
             this.Pub1.append(btn);
         }

         CheckBox mycb = new CheckBox();
         mycb.setId("CB_IsSetNextTime");
         mycb.setText("以后发送都按照本次设置计算");
         this.Pub1.append(mycb);


     }
     //保存
     public void saveMet()
     {
         String getSaveNo = this.get_request().getParameter("getSaveNo");

         //此处做判断,删除checked的部门数据
         String[] getSaveNoArray = getSaveNo.split(",");
         List<String> getSaveNoList = new ArrayList<String>();

         for (int i = 0; i < getSaveNoArray.length; i++)
         {
             getSaveNoList.add(getSaveNoArray[i]);
         }

         getSaveNo = null;
         String ziFu = ",";
         for (int i = 0; i < getSaveNoList.size(); i++)
         {
             if (i == getSaveNoList.size() - 1)
             {
                 ziFu = null;
             }
             getSaveNo += (getSaveNoList.get(i) + ziFu);
         }

         //设置人员.
         BP.WF.Dev2Interface.WorkOpt_SetAccepter(this.getToNode(), this.getWorkID(), this.getFID(), getSaveNo, false);





         if (this.getIsWinOpen() == 0)
         {
             /*如果是 MyFlow.aspx 调用的, 就要调用发送逻辑. */
             //this.DoSend();
             return;
         }


         if (this.get_request().getParameter("IsEUI") == null)
         {
             this.WinClose();
         }
         else
         {
             PubClass.ResponseWriteScript("window.parent.$('windowIfrem').window('close');");

         }

     }


     public void DoSend() throws Exception
     {
         // 以下代码是从 MyFlow.aspx Send 方法copy 过来的，需要保持业务逻辑的一致性，所以代码需要保持一致.

         Node nd = new Node(this.getFK_Node());
         Work wk = nd.getHisWork();
         wk.setOID(this.getWorkID());
         wk.Retrieve();

         WorkNode firstwn = new WorkNode(wk, nd);
         String msg = "";
         try
         {
             msg = firstwn.NodeSend().ToMsgOfHtml();
         }
         catch (Exception exSend)
         {
             this.Pub1.append(AddFieldSetGreen("错误"));
             this.Pub1.append(exSend.getMessage().replace("@@", "@").replace("@", "<BR>@"));
             this.Pub1.append(AddFieldSetEnd());
             return;
         }

         //#region 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
         try
         {
             //处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
             BP.WF.Glo.DealBuinessAfterSendWork(this.getFK_Flow(), this.getWorkID(), this.getDoFunc(), getWorkIDs(), this.getCFlowNo(), 0, null);
         }
         catch (Exception ex)
         {
             this.ToMsg(msg, ex.getMessage());
             return;
         }
         //#endregion 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.


         /*处理转向问题.*/
         TurnToDeal ttd=firstwn.getHisNode().getHisTurnToDeal();
         if(ttd==TurnToDeal.SpecUrl){
        	 String myurl = firstwn.getHisNode().getTurnToDealDoc();
             if (myurl.contains("&") == false)
                 myurl += "?1=1";
             Attrs myattrs = firstwn.getHisWork().getEnMap().getAttrs();
             Work hisWK = firstwn.getHisWork();
             for(Attr attr:myattrs)
             {
                 if (myurl.contains("@") == false)
                     break;
                 myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
             }
             if (myurl.contains("@"))
                 throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);

             myurl += "&FromFlow=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&PWorkID=" + this.getWorkID() + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
             this.get_response().sendRedirect(myurl);
         }
         else if(ttd==TurnToDeal.TurnToByCond){
        	 TurnTos tts = new TurnTos(this.getFK_Flow());
             if (tts.size() == 0)
                 throw new Exception("@您没有设置节点完成后的转向条件。");
             for (TurnTo tt: tts.ToJavaList())
             {
                 tt.HisWork=firstwn.getHisWork();
                 if (tt.getIsPassed() == true)
                 {
                     String url = tt.getTurnToURL();
                     if (url.contains("&") == false)
                         url += "?1=1";
                     Attrs attrs = firstwn.getHisWork().getEnMap().getAttrs();
                     Work hisWK1 = firstwn.getHisWork();
                     for(Attr attr:attrs)
                     {
                         if (url.contains("@") == false)
                             break;
                         url = url.replace("@" + attr.getKey(), hisWK1.GetValStrByKey(attr.getKey()));
                     }
                     if (url.contains("@"))
                         throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);

                     url += "&PFlowNo=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&PWorkID=" + this.getWorkID() + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
                     this.get_response().sendRedirect(url);
                     return;
                 }
             }
//#warning 为上海修改了如果找不到路径就让它按系统的信息提示。
             this.ToMsg(msg, "info");
         }
         else{

             this.ToMsg(msg, "info");
         }
         
         return;
     }

     public void ToMsg(String msg, String type)
     {
    		get_request().setAttribute("info", msg);
    		//this.Application["info" + WebUser.getNo()] = msg;

    		try {
    			BP.WF.Glo.setSessionMsg(msg);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		try {
    			get_response().sendRedirect("./../MyFlowInfo.jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Type=" + type + "&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID());
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
     }
     /// <summary>
     /// 根据DataTable生成Json树结构
     /// </summary>
     /// <param name="tabel">数据源</param>
     /// <param name="idCol">ID列</param>
     /// <param name="txtCol">Text列</param>
     /// <param name="rela">关系字段</param>
     /// <param name="pId">父ID</param>
     ///<returns>easyui tree json格式</returns>
     StringBuilder treeResult = new StringBuilder();
     StringBuilder treesb = new StringBuilder();
     public String GetTreeJsonByTable(DataTable tabel, String idCol, String txtCol, String rela, Object pId, String IsParent, String CheckedString)
     {
    	 String treeJson = "";
 		treeResult.append(treesb.toString());

 		treesb = new StringBuilder();
 		if (tabel.Rows.size() > 0)
 		{
 			treesb.append("[");
 			Map<String, Object> filer = new HashMap<String, Object>();
 			filer.put(rela, pId);
 			
 			
 			if (pId.toString().equals(""))
 			{
 				//filer = String.format("%1$s is null", rela);
 				filer.put(rela, null);
 			}
 			else
 			{
 				//filer = String.format("%1$s='%2$s'", rela, pId);
 				filer.put(rela, pId);
 			}
 			try {
 				List<DataRow> rows = tabel.Select(filer);
 				//DataRow[] rows = tabel.Select(filer);
 				if (rows.size() > 0) //修改
 				{
 				
 					for (DataRow row : rows)
 					{
 						String deptNo = (String) (row.getValue(idCol) instanceof String ? row.getValue(idCol) : null);
 						HashMap<String, Object> filerMap = new HashMap<String, Object>();
 						filerMap.put(rela, row.getValue(idCol));
 						
 						if (treeResult.length() == 0)
 						{
 							treesb.append("{\"id\":\"" + row.getValue(idCol) + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}" + ",\"checked\":" + CheckedString.contains("," + row.getValue(idCol) + ",") + ",\"state\":\"open\"");
 						}
 						else if (tabel.Select(filerMap).size() > 0)
 						{
 							treesb.append("{\"id\":\"" + row.getValue(idCol) + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}" + ",\"checked\":" + CheckedString.contains("," + row.getValue(idCol) + ",")  + ",\"state\":\"open\"");
 						}
 						else
 						{
 							treesb.append("{\"id\":\"" + row.getValue(idCol) + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}" + ",\"checked\":" + CheckedString.contains("," + row.getValue(idCol) + ",") );
 						}


 						if (tabel.Select(filerMap).size() > 0)
 						{
 							treesb.append(",\"children\":");
 							GetTreeJsonByTable(tabel, idCol, txtCol, rela, row.getValue(idCol), IsParent, CheckedString);
 							treeResult.append(treesb.toString());
 							treesb = new StringBuilder();
 						}
 						treeResult.append(treesb.toString());
 						treesb = new StringBuilder();
 						treesb.append("},");
 					}
 					treesb = treesb.deleteCharAt(treesb.length() - 1);
 				}
 				treesb.append("]");
 				treeResult.append(treesb.toString());
 				treeJson = treeResult.toString();
 				treesb = new StringBuilder();
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 		}
 		return treeJson;
     }
}
