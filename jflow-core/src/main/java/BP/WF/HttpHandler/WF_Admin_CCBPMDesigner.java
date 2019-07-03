package BP.WF.HttpHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Sys.FrmTree;
import BP.Sys.FrmTreeAttr;
import BP.Sys.MapData;
import BP.Sys.OSModel;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Port.AdminEmp;
import BP.WF.Template.DataStoreModel;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSortAttr;
import BP.WF.Template.FlowSorts;
import BP.WF.Template.LabNote;
import BP.WF.Template.LabNoteAttr;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.WorkflowDefintionManager;
import BP.WF.XML.AdminMenu;
import BP.WF.XML.AdminMenuGroup;
import BP.WF.XML.AdminMenuGroups;
import BP.WF.XML.AdminMenus;
import BP.Web.WebUser;


/** 
 初始化函数
 
*/
public class WF_Admin_CCBPMDesigner extends WebContralBase
{
	 
	/**
	 * 构造函数
	 */
	public WF_Admin_CCBPMDesigner()
	{
	
	}
	
	
	/** 
	 流程信息.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Flows_Init() throws Exception
	{
		DataSet ds = new DataSet();

		FlowSorts sorts = new FlowSorts();
		sorts.RetrieveAll();

		//把类别数据放入.
		DataTable dt = sorts.ToDataTableField();
		dt.TableName = "Sorts";
		ds.Tables.add(dt);

		Flows fls = new Flows();
		fls.RetrieveAll();

		dt = fls.ToDataTableField();
		dt.TableName = "Flows";

		dt.Columns.Add("NumOfRuning", Integer.class); // 耗时分析.
		dt.Columns.Add("NumOfComplete", Integer.class);
		dt.Columns.Add("NumOfEtc", Integer.class);
		dt.Columns.Add("NumOfOverTime", Integer.class);

		for (DataRow dr : dt.Rows)
		{
			String no = dr.getValue("No").toString();
			dr.setValue("NumOfRuning",DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE FK_Flow='" + no + "' AND WFSta=1"));
			dr.setValue("NumOfComplete",DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE FK_Flow='" + no + "' AND WFSta=1"));
			dr.setValue("NumOfEtc", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE FK_Flow='" + no + "' AND WFSta=1"));
			dr.setValue("NumOfOverTime", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE FK_Flow='" + no + "' AND WFSta=1"));
		}
		ds.Tables.add(dt);
		return BP.Tools.Json.ToJson(ds);
	}
	 /// <summary>
    /// 执行流程设计图的保存.
    /// </summary>
    /// <returns></returns>
    public String Designer_Save()
    {
        String sql = "";
        try
        {
            //保存节点位置. @101,2,30@102,3,1
            String[] nodes = this.GetRequestVal("Nodes").split("@");
            for (String item : nodes)
            {
            	if(item==null||item.length() ==0)
                	continue;
                String[] strs = item.split(",");
                
                sql = "UPDATE WF_Node SET X=" + strs[1] + ",Y=" + strs[2] + " WHERE NodeID=" + strs[0];
                DBAccess.RunSQL(sql);
            }

            //保存方向.
            String[] dirs = this.GetRequestVal("Dirs").split("@");
            for (String item : dirs)
            {
            	if(item==null||item.length() ==0)
                	continue;
                String[] strs = item.split(",");
                String sql1="delete from WF_Direction where MyPK='"+strs[0]+"'";
                DBAccess.RunSQL(sql1);
                sql = "insert into WF_Direction(MyPK,FK_Flow,Node,ToNode,IsCanBack) values('" + strs[0] + "','" + strs[1] + "','" + strs[2] + "','" + strs[3] + "',"+"0)";
                DBAccess.RunSQL(sql);
            }

            //保存label位置.
            String[] labs = this.GetRequestVal("Labs").split("@");
            for (String item : labs)
            {
            	if(item==null||item.length() ==0)
                	continue;
                String[] strs = item.split(",");
                sql = "UPDATE WF_LabNote SET X=" + strs[1] + ",Y=" + strs[2] + " WHERE MyPK='" + strs[0]+"'";
                DBAccess.RunSQL(sql);
            }

            Flow fl = new Flow(this.getFK_Flow());
            fl.DoCheck();

            return "保存成功.";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	public String DoDefaultMethod()
	{
		return "err@没有判断的标记:" + this.getDoType();
	}
	
	/// <summary>
    /// 下载流程模版
    /// </summary>
    /// <returns></returns>
    public String ExpFlowTemplete() throws Exception
    {
        Flow flow = new Flow(this.getFK_Flow());
        String fileXml = flow.GenerFlowXmlTemplete();
        String docs = DataType.ReadTextFile(fileXml);
        return docs;
    }
    
    public String DownFormTemplete() throws Exception
    {
        DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet_AllEleInfo(this.getFK_MapData());
        String file = BP.Sys.SystemConfig.getPathOfTemp() + this.getFK_MapData() + ".xml";
        //表单导出方式修改
        ds.WriteXml(file,null,ds);
        String docs = DataType.ReadTextFile(file);
        return docs;
    }
	/** 
	 * 使管理员登录
	 * @return 
	 * @throws Exception 
	*/
	public final String LetLogin() throws Exception
	{
		 //System.out.println(StringHelper.isNullOrEmpty(WebUser.getNo()));
		return  LetAdminLogin( this.GetRequestVal("userNo"), true) ;	
	}
	
	
	/** 
	 * 获得枚举列表的JSON.
	 * @return 
	*/
	public final String Logout()
	{
		BP.WF.Dev2Interface.Port_SigOut();
		return "";
	}
	
	
	/** 
	 根据部门、岗位获取人员列表
	 @return 
	 * @throws Exception 
	*/
	public final String GetEmpsByStationTable() throws Exception
	{
		String deptid = this.GetRequestVal("DeptNo");
		String stid = this.GetRequestVal("StationNo");

		if (StringHelper.isNullOrEmpty(deptid) || StringHelper.isNullOrEmpty(stid))
		{
			return "[]";
		}

		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);

		if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
		{
			BP.GPM.DeptEmp de = null;
			BP.Port.Emp emp = null;
			BP.WF.Port.EmpStations ess = new BP.WF.Port.EmpStations(stid);

			BP.GPM.DeptEmps des = new BP.GPM.DeptEmps();
			des.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, deptid);

			BP.Port.Emps emps = new BP.Port.Emps();
			emps.RetrieveAll();

			for (BP.WF.Port.EmpStation es : ess.ToJavaList())
			{
				Object tempVar = des.GetEntityByKey(BP.GPM.DeptEmpAttr.FK_Emp, es.getFK_Emp());
				de = (BP.GPM.DeptEmp)((tempVar instanceof BP.GPM.DeptEmp) ? tempVar : null);

				if (de == null)
				{
					continue;
				}

				Object tempVar2 = emps.GetEntityByKey(es.getFK_Emp());
				emp = (BP.Port.Emp)((tempVar2 instanceof BP.Port.Emp) ? tempVar2 : null);

				dt.Rows.AddDatas(emp.getNo(), deptid + "|" + stid, emp.getName(), "EMP");
			}
		}
		else
		{
			BP.GPM.Emp emp = null;
			BP.GPM.Emps emps = new BP.GPM.Emps();
			emps.RetrieveAll();

			BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
			dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, deptid, BP.GPM.DeptEmpStationAttr.FK_Station, stid);

			for (BP.GPM.DeptEmpStation des : dess.ToJavaList())
			{
				Object tempVar3 = emps.GetEntityByKey(des.getFK_Emp());
				emp = (BP.GPM.Emp)((tempVar3 instanceof BP.GPM.Emp) ? tempVar3 : null);

				dt.Rows.AddDatas(emp.getNo(), deptid + "|" + stid, emp.getName(), "EMP");
			}
		}

		
		return BP.Tools.Json.ToJson(dt);
		
		 
	}
	//组织结构
	public final String GetStructureTreeRootTable() throws Exception
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);
		
		String parentrootid = getRequest().getParameter("parentrootid");
		String newRootId = "";

		if ( !"admin".equals(WebUser.getNo()))
		{
			BP.WF.Port.AdminEmp aemp = new AdminEmp();
			aemp.setNo(WebUser.getNo());

			if (aemp.RetrieveFromDBSources() != 0 && aemp.getUserType() == 1 && !StringHelper.isNullOrEmpty(aemp.getRootOfDept()))
			{
				newRootId = aemp.getRootOfDept();
			}
		}

		if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
		{
			BP.WF.Port.Dept dept = new BP.WF.Port.Dept();

			if (!StringHelper.isNullOrEmpty(newRootId))
			{
				if (dept.Retrieve(BP.WF.Port.DeptAttr.No, newRootId) == 0)
				{
					dept.setNo("-1");
					dept.setName("无部门");
					dept.setParentNo("");
				}
			}
			else
			{
				if (dept.Retrieve(BP.WF.Port.DeptAttr.ParentNo, parentrootid) == 0)
				{
					dept.setNo("-1");
					dept.setName("无部门");
					dept.setParentNo("");
				}
			}
			
			DataRow dr=dt.NewRow();
			dr.setValue(0, dept.getNo());
			dr.setValue(1, dept.getParentNo());
			dr.setValue(2, dept.getName());
			dr.setValue(3, "DEPT");

			dt.Rows.add(dr);  
		}
		else
		{
			BP.GPM.Dept dept = new BP.GPM.Dept();

			if (!StringHelper.isNullOrEmpty(newRootId))
			{
				if (dept.Retrieve(BP.GPM.DeptAttr.No, newRootId) == 0)
				{
					dept.setNo("-1");
					dept.setName("无部门");
					dept.setParentNo("");
				}
			}
			else
			{
				if (dept.Retrieve(BP.GPM.DeptAttr.ParentNo, parentrootid) == 0)
				{
					dept.setNo("-1");
					dept.setName("无部门");
					dept.setParentNo("");
				}
			}
			
			
			DataRow dr=dt.NewRow();
			dr.setValue(0, dept.getNo());
			dr.setValue(1, dept.getParentNo());
			dr.setValue(2, dept.getName());
			dr.setValue(3, "DEPT");

			dt.Rows.add(dr);  
			

			//dt.Rows.AddRow(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");
		}

		return BP.Tools.Json.ToJson(dt);
	}

	/** 
	 获取指定部门下一级子部门及岗位列表
	 @return 
	 * @throws Exception 
	*/
	public final String GetSubDeptsTable() throws Exception
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);

		String rootid = getRequest().getParameter("rootid");

		 
			BP.GPM.Depts depts = new BP.GPM.Depts();
			depts.Retrieve(BP.GPM.DeptAttr.ParentNo, rootid);
			BP.GPM.Stations sts = new BP.GPM.Stations();
			sts.RetrieveAll();
			BP.GPM.DeptStations dss = new BP.GPM.DeptStations();
			dss.Retrieve(BP.GPM.DeptStationAttr.FK_Dept, rootid);
			BP.GPM.DeptEmps des = new BP.GPM.DeptEmps();
			des.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, rootid);
			BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
			dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, rootid);
			BP.GPM.Station stt = null;
			BP.GPM.Emp emp = null;
			java.util.ArrayList<String> inemps = new java.util.ArrayList<String>();

			for (BP.GPM.Dept dept : depts.ToJavaList())
			{
				//增加部门
				dt.Rows.AddDatas(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");
			}

			//增加部门岗位
			for (BP.GPM.DeptStation ds : dss.ToJavaList())
			{
				Object tempVar = sts.GetEntityByKey(ds.getFK_Station());
				stt = (BP.GPM.Station)((tempVar instanceof BP.GPM.Station) ? tempVar : null);

				if (stt == null)
				{
					continue;
				}

				dt.Rows.AddDatas(ds.getFK_Station(), rootid, stt.getName(), "STATION");
			}

			//增加没有岗位的人员
			for (BP.GPM.DeptEmp de : des.ToJavaList())
			{
				if (dess.GetEntityByKey(BP.GPM.DeptEmpStationAttr.FK_Emp, de.getFK_Emp()) == null)
				{
					if (inemps.contains(de.getFK_Emp()))
					{
						continue;
					}

					inemps.add(de.getFK_Emp());
				}
			}

			for (String inemp : inemps)
			{
				emp = new BP.GPM.Emp(inemp);
				dt.Rows.AddDatas(emp.getNo(), rootid, emp.getName(), "EMP");
			}
		

		return BP.Tools.Json.ToJson(dt);
	}

	/** 
	 初始化登录界面.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Default_Init() throws Exception
	{
		
	
		
		
		//让admin登录
		if (DataType.IsNullOrEmpty(BP.Web.WebUser.getNoOfRel())==true)
		{
			 
			 String userNo = this.GetRequestVal("UserNo");            
             String sid = this.GetRequestVal("SID");
             
             BP.WF.Dev2Interface.Port_Login(userNo, sid);
             
		   return "url@Login.htm?DoType=Logout";
		}

		if (BP.Web.WebUser.getIsAdmin() == false)
		{
			return "url@Login.htm?DoType=Logout";
		}
			

		//如果没有流程表，就执行安装.
			 
		if (BP.DA.DBAccess.IsExitsObject("WF_Flow") == false)
		{
			return "url@../DBInstall.htm";
		}

		java.util.Hashtable ht = new java.util.Hashtable();
	 
			ht.put("OSModel", "1");
	 
		

        //把系统信息加入里面去.
        ht.put("SysNo", SystemConfig.getSysNo());
        ht.put("SysName", SystemConfig.getSysName());
        
        //集成的平台.
        ht.put("RunOnPlant", SystemConfig.getRunOnPlant());


		try
		{
			// 执行升级
			String str;
			str = BP.WF.Glo.UpdataCCFlowVer();
			if (str == null)
			{
				str = "";
			}
			ht.put("Msg", str);
			
		}
		catch (Exception ex)
		{
			return "err@" + ex.getMessage();
		}

		//生成Json.
		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
	//流程设计器登陆前台，转向规则
	public final String Login_Redirect()
	{
		if (SystemConfig.getCustomerNo().equals("TianYe"))
		{
			return "../../../BPM/pages/login.html";
		}

		return "../../AppClassic/Login.htm?DoType=Logout";
	}

	/** 
	 初始化登录界面.
	 @return 
	 * @throws Exception 
	*/
	public final String Login_Init() throws Exception
	{
 
  
		 if (DBAccess.TestIsConnection() == false)
             return "err@数据库连接配置错误 ";

         if (DBAccess.IsExitsObject("Port_Emp") == false )       		 
             return "url@../DBInstall.htm"; 
         

         try
         {
             // 执行升级
             String str = BP.WF.Glo.UpdataCCFlowVer();
             if (str == null)
                 str = "ccbpm 准备完毕,欢迎登录.";
             return str;
         }
         catch (Exception ex)
         {
             return "err@升级失败请联系管理员,或者反馈给ccbpm. 失败原因:" + ex.getMessage();
         }
	}

	public String Login_Submit() throws Exception
	{
		
		 
		 BP.Port.Emp emp = new BP.Port.Emp();
         emp.setNo( this.GetValFromFrmByKey("TB_No"));

         if (emp.RetrieveFromDBSources() == 0)
             return "err@用户名或密码错误.";
                  
         if ("admin".equals(emp.getNo())==false)
         {
             //检查是否是管理员？
             AdminEmp adminEmp = new AdminEmp();
             adminEmp.setNo(emp.getNo());
             if (adminEmp.RetrieveFromDBSources() == 0)
                 return "err@您非管理员用户，不能登录.";

             if (adminEmp.getIsAdmin() == false)
                 return "err@您非管理员用户或已被禁用，不能登录.";
         }
         
         
         //判断集成的平台.
         String plant=BP.Sys.SystemConfig.getRunOnPlant();
         
         if (plant.equals("jeesite")==true)
         {
        	 
         }
          

         String pass = this.GetValFromFrmByKey("TB_PW");
         if (emp.CheckPass(pass) == false)
             return "err@用户名或密码错误.";

         //让其登录.
         BP.WF.Dev2Interface.Port_Login(emp.getNo());
         
         return "url@Default.htm?SID=" + emp.getSID() + "&UserNo=" + emp.getNo();
          
	}

 
	/** 
	 保存流程图信息
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SaveOneFlow() throws Exception
	{
		//流程图格式.
		String diagram = GetValFromFrmByKey("diagram");
		/*//检查该数据是否可以被转换为Json对象？
		//JsonData flowJsonData = JsonMapper.ToObject(diagram);
		Object json = Json.ToJson(diagram);
		if (json.IsObject == false)
		{
			return "err@参数diagram不能转换为json对象.";
		}*/

		//流程图.
		String png = GetValFromFrmByKey("png");
		// 流程编号.
		String flowNo = GetValFromFrmByKey("FlowNo");
		//节点到节点关系
		String direction = GetValFromFrmByKey("direction");

		//直接保存流程图信息
		BP.WF.Flow fl = new BP.WF.Flow(flowNo);
		//修改版本
		fl.setDType(fl.getDType() == BP.WF.CCBPM_DType.BPMN.getValue() ? BP.WF.CCBPM_DType.BPMN.getValue() : BP.WF.CCBPM_DType.CCBPM.getValue());
	 
		fl.Update();

		//节点方向
		String[] dir_Nodes = direction.split("[@]", -1);
		Direction drToNode = new Direction();
		drToNode.Delete(DirectionAttr.FK_Flow, flowNo);
		for (String item : dir_Nodes)
		{
			if (DotNetToJavaStringHelper.isNullOrEmpty(item))
			{
				continue;
			}

			String[] nodes = item.split("[:]", -1);
			if (nodes.length == 2)
			{
				drToNode = new Direction();
				drToNode.setFK_Flow(flowNo);
				drToNode.setNode(Integer.parseInt(nodes[0]));
				drToNode.setToNode(Integer.parseInt(nodes[1]));
				drToNode.Insert();
			}
		}
		//保存节点坐标及标签
		//清空标签
		LabNote labelNode = new LabNote();
		labelNode.Delete(LabNoteAttr.FK_Flow, flowNo);
		
		 // JsonData flowJsonData = JsonMapper.ToObject(diagram);
		  JSONObject flowJsonData = JSONObject.fromObject(diagram);
		//jackjson
		/* if (flowJsonData.getIsObject() == true)
         {*/
		JSONArray flow_Nodes = flowJsonData.getJSONObject("s").getJSONArray("figures");
		for (int iNode = 0, jNode = flow_Nodes.size(); iNode < jNode; iNode++)
		{
			JSONObject figure = (JSONObject) flow_Nodes.get(iNode);
			//不存在不进行处理，继续循环
			if (figure == null || figure.get("CCBPM_Shape") == null)
			{
				continue;
			}
			if (figure.get("CCBPM_Shape").equals("Node"))
			{
				//节点坐标处理
				BP.WF.Node node = new BP.WF.Node();
				node.RetrieveByAttr(NodeAttr.NodeID, figure.get("CCBPM_OID"));
				if (!DotNetToJavaStringHelper.isNullOrEmpty(node.getName()) && figure.getJSONArray("rotationCoords").size() > 0)
				{
					JSONObject rotationCoord = (JSONObject) figure.getJSONArray("rotationCoords").get(0);
					node.setX((int)Float.parseFloat(rotationCoord.optString("x")));
					node.setY((int)Float.parseFloat(rotationCoord.optString("y")));
					//增加名称的URIdecode
					try {
						node.setName(URLDecoder.decode(node.getName(), "utf-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					node.DirectUpdate();
				}
			}
			else if (figure.get("CCBPM_Shape").toString().equals("Text"))
			{
				//流程标签处理.
				JSONObject primitives = (JSONObject) figure.getJSONArray("primitives").get(0);
				JSONObject vector = (JSONObject) primitives.getJSONArray("vector").get(0);
				labelNode = new LabNote();
				labelNode.setFK_Flow(flowNo);
				labelNode.setName(primitives.get("str").toString());
				labelNode.setX((int)Float.parseFloat(vector.optString("x")));
				labelNode.setY((int)Float.parseFloat(vector.optString("y")));
				labelNode.Insert();
			}
		}
		 /*return "true";
       }*/
		 return "true";
	}
 
	/** 
	 获取流程所有元素
	 @return json data
	 * @throws Exception 
	*/
	public final String Flow_AllElements_ResponseJson() throws Exception
	{
		BP.WF.Flow flow = new BP.WF.Flow();
		flow.setNo(this.getFK_Flow());
		flow.RetrieveFromDBSources();

		//获取所有节点
		String sqls = "SELECT NODEID,NAME,X,Y,RUNMODEL FROM WF_NODE WHERE FK_FLOW='" + this.getFK_Flow() + "';" + "\r\n" + "SELECT NODE,TONODE FROM WF_DIRECTION WHERE FK_FLOW='" + this.getFK_Flow() + "';" + "\r\n" + "SELECT MYPK,NAME,X,Y FROM WF_LABNOTE WHERE FK_FLOW='" + this.getFK_Flow() + "';";

		DataSet ds = DBAccess.RunSQLReturnDataSet(sqls);

		ds.Tables.get(0).TableName = "Nodes";
        ds.Tables.get(1).TableName = "Direction";
        ds.Tables.get(2).TableName = "LabNote";

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 创建流程节点并返回编号
	 @return 
	 * @throws Exception 
	*/
	public final String CreateNode() throws Exception
	{
		try
		{
			String FK_Flow = this.GetValFromFrmByKey("FK_Flow");
			String figureName = this.GetValFromFrmByKey("FigureName");
			String x = this.GetValFromFrmByKey("x");
			String y = this.GetValFromFrmByKey("y");
			
			int iX = 30;
			int iY = 30;
			if (!DotNetToJavaStringHelper.isNullOrEmpty(x))
			{
				iX = (int)Double.parseDouble(x);
			}
			if (!DotNetToJavaStringHelper.isNullOrEmpty(y))
			{
				iY = (int)Double.parseDouble(y);
			}

			int nodeId = BP.WF.Template.TemplateGlo.NewNode(FK_Flow, iX, iY);

			BP.WF.Node node = new BP.WF.Node(nodeId);
			node.setHisRunModel(Node_GetRunModelByFigureName(figureName));
			node.Update();

			java.util.Hashtable ht = new java.util.Hashtable();
			ht.put("NodeID", node.getNodeID());
			ht.put("Name", node.getName());

			return BP.Tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 gen
	 @param figureName
	 @return 
	*/
	public final BP.WF.RunModel Node_GetRunModelByFigureName(String figureName)
	{
		BP.WF.RunModel runModel = BP.WF.RunModel.Ordinary;
		if (figureName.equals("NodeOrdinary"))
		{
				runModel = BP.WF.RunModel.Ordinary;
		}
		else if (figureName.equals("NodeFL"))
		{
				runModel = BP.WF.RunModel.FL;
		}
		else if (figureName.equals("NodeHL"))
		{
				runModel = BP.WF.RunModel.HL;
		}
		else if (figureName.equals("NodeFHL"))
		{
				runModel = BP.WF.RunModel.FHL;
		}
		else if (figureName.equals("NodeSubThread"))
		{
				runModel = BP.WF.RunModel.SubThread;
		}
		else
		{
				runModel = BP.WF.RunModel.Ordinary;
		}
		return runModel;
	}
	/** 
	 根据节点编号删除流程节点
	 @return 执行结果
	 * @throws Exception 
	*/
	public final String DeleteNode() throws Exception
	{
		try
		{
			BP.WF.Node node = new BP.WF.Node();
			node.setNodeID(this.getFK_Node());
			if (node.RetrieveFromDBSources() == 0)
			{
				return "err@删除失败,没有删除到数据，估计该节点已经别删除了.";
			}

			if (node.getIsStartNode() == true)
			{
				return "err@开始节点不允许被删除。";
			}

			node.Delete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 修改节点名称
	 @return 
	 * @throws Exception 
	*/
	public final String Node_EditNodeName() throws Exception
	{
		String FK_Node = this.GetValFromFrmByKey("NodeID");
		String NodeName = this.GetValFromFrmByKey("NodeName");

		BP.WF.Node node = new BP.WF.Node();
		node.setNodeID (Integer.parseInt(FK_Node));
		int iResult = node.RetrieveFromDBSources();
		if (iResult > 0)
		{
			node.setName(NodeName);
			node.Update();
			return "@修改成功.";
		}

		return "err@修改节点失败，请确认该节点是否存在？";
	}
	/** 
	 修改节点运行模式
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Node_ChangeRunModel() throws Exception
	{
		String runModel = GetValFromFrmByKey("RunModel");
		BP.WF.Node node = new BP.WF.Node(this.getFK_Node());
		//节点运行模式
		if (runModel.equals("NodeOrdinary"))
		{
				node.setHisRunModel(BP.WF.RunModel.Ordinary);
		}
		else if (runModel.equals("NodeFL"))
		{
				node.setHisRunModel(BP.WF.RunModel.FL);
		}
		else if (runModel.equals("NodeHL"))
		{
				node.setHisRunModel(BP.WF.RunModel.HL);
		}
		else if (runModel.equals("NodeFHL"))
		{
				node.setHisRunModel(BP.WF.RunModel.FHL);
		}
		else if (runModel.equals("NodeSubThread"))
		{
				node.setHisRunModel(BP.WF.RunModel.SubThread);
		}
		node.Update();

		return "设置成功.";
	}

	/** 
	 获取用户信息
	 @return 
	 * @throws Exception 
	*/
	public final String GetWebUserInfo() throws Exception
	{
		if(StringHelper.isNullOrEmpty(WebUser.getNo()))
		{
			return "err@当前用户没有登录，请登录后再试。";
		}

		java.util.Hashtable ht = new java.util.Hashtable();

		BP.Port.Emp emp = new BP.Port.Emp(WebUser.getNo());

		ht.put("No", emp.getNo());
		ht.put("Name", emp.getName());
		ht.put("FK_Dept", emp.getFK_Dept());
		ht.put("SID", emp.getSID());


		if ("admin".equals(WebUser.getNo()))
		{
			ht.put("IsAdmin", "1");
			ht.put("RootOfDept", "0");
			ht.put("RootOfFlow", "F0");
			ht.put("RootOfForm", "");
		}
		else
		{
			BP.WF.Port.AdminEmp aemp = new AdminEmp();
			aemp.setNo(WebUser.getNo());

			if (aemp.RetrieveFromDBSources() == 0)
			{
				ht.put("RootOfDept", "-9999");
				ht.put("RootOfFlow", "-9999");
				ht.put("RootOfForm", "-9999");
			}
			else
			{
				ht.put("RootOfDept", aemp.getRootOfDept());
				ht.put("RootOfFlow", "F" + aemp.getRootOfFlow());
				ht.put("RootOfForm", aemp.getRootOfForm());
			}
		}

		return BP.Tools.Json.ToJsonEntityModel(ht);

	}
	
		/**
		 * 获取设计器 - 系统维护菜单数据
         * 系统维护管理员菜单 @于庆海 需要翻译
		 * @return
		 * @throws Exception 
		 */
	   public String GetTreeJson_AdminMenu() throws Exception
       {
		   //查询全部
           AdminMenuGroups groups = new AdminMenuGroups();
           groups.RetrieveAll();
		   
		   AdminMenus menus = new AdminMenus();
			menus.RetrieveAll();
			 // 定义容器.
			AdminMenus newMenus = new AdminMenus();
			
			
			for (AdminMenuGroup menu : groups.ToJavaList())
            {
                //是否可以使用？
                if (menu.IsCanUse(WebUser.getNo()) == false)
                    continue;

                AdminMenu newMenu = new AdminMenu();
                newMenu.setNo(menu.getNo());
                newMenu.setName(menu.getName());
                newMenu.setGroupNo("0");
                newMenu.setFor(menu.getFor());
                newMenu.setUrl("");
                newMenus.Add(newMenu);
            }
			
			for (AdminMenu  menu: menus.ToJavaList())
			{
				//是否可以使用？
				if (menu.IsCanUse(WebUser.getNo()) == false)
					continue;
				//进行返回
				newMenus.Add(menu);
			}
			//添加默认，无权限
			if (newMenus.size() == 0)
			{
				AdminMenu menu = new AdminMenu();
				menu.setNo("1");
				menu.setParentNo("AdminMenu");
				menu.setName("无权限");
				menu.setUrl("");
				newMenus.Add(menu);
			}
			DataTable dt = newMenus.ToDataTable();
            return BP.Tools.Json.ToJson(newMenus.ToDataTable());
       }

	private StringBuilder sbJson = new StringBuilder();
	/** 
	 获取流程树数据
	 
	 @return 返回结果Json,流程树
	 * @throws Exception 
	*/
	public final String GetFlowTreeTable() throws Exception
	{
		String sql = "SELECT * FROM (SELECT 'F'+No NO,'F'+ParentNo PARENTNO, NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" + "                           union " + "\r\n" + "                           SELECT NO, 'F'+FK_FlowSort as PARENTNO,(NO + '.' + NAME) NAME,IDX,0 ISPARENT,'FLOW' TTYPE,DTYPE FROM WF_Flow) A  ORDER BY IDX";

		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			sql = "SELECT * FROM (SELECT 'F'||No NO,'F'||ParentNo PARENTNO,NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" + "                        union " + "\r\n" + "                        SELECT NO, 'F'||FK_FlowSort as PARENTNO,NO||'.'||NAME NAME,IDX,0 ISPARENT,'FLOW' TTYPE,DTYPE FROM WF_Flow) A  ORDER BY IDX";
		}
		else if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT * FROM (SELECT CONCAT('F', No) NO, CONCAT('F', ParentNo) PARENTNO, NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" + "                           union " + "\r\n" + "                           SELECT NO, CONCAT('F', FK_FlowSort) PARENTNO, CONCAT(NO, '.', NAME) NAME,IDX,0 ISPARENT,'FLOW' TTYPE,DTYPE FROM WF_Flow) A  ORDER BY IDX";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		//判断是否为空，如果为空，则创建一个流程根结点，added by liuxc,2016-01-24
		if (dt.Rows.size() == 0)
		{
			FlowSort fs = new FlowSort();
			fs.setNo("99");
			fs.setParentNo("0");
			fs.setName("流程树");
			fs.Insert();

			dt.Rows.AddDatas("F99", "F0", "流程树", 0, 1, "FLOWTYPE", -1);
		}
		else
		{
			List<DataRow> drs = dt.select("NAME='流程树'");
            if (drs.size() > 0 && (!"F0".equals(drs.get(0).getValue("PARENTNO"))))
                drs.get(0).setValue("PARENTNO", "F0");
		}



		if ( ! "admin".equals(WebUser.getNo()))
		{
			BP.WF.Port.AdminEmp aemp = new AdminEmp();
			aemp.setNo(WebUser.getNo());
			
			if (aemp.RetrieveFromDBSources() == 0)
			{
					return "err@登录帐号错误.";
			}

			if (aemp.getIsAdmin() == false)
			{
				return "err@非管理员用户.";
			}

			DataRow rootRow = dt.select("PARENTNO='F0'").get(0);
			DataRow newRootRow = dt.select("NO='F" + aemp.getRootOfFlow() + "'").get(0);

			newRootRow.setValue("PARENTNO", "F0");
			DataTable newDt = dt.clone();
			newDt.Rows.AddRow(newRootRow);
			if(newRootRow.size()!=0 && !aemp.getRootOfFlow().equals("99"))
				newDt.Rows.remove(dt.select("NAME='流程树'").get(0));

			GenerChildRows(dt, newDt, newRootRow);
			dt = newDt;
		}

		return BP.Tools.Json.DataTableToJson(dt, false);
	}

	public final void GenerChildRows(DataTable dt, DataTable newDt, DataRow parentRow)
	{
		 List<DataRow> rows = dt.select("ParentNo='" + parentRow.getValue("NO") + "'");
		for(DataRow r : rows)
		{
			newDt.Rows.AddRow(r);

			GenerChildRows(dt, newDt, r);
		}
	}
	
	public final void GenerChildRowsX(DataTable dt, DataTable newDt, DataRow parentRow)
	{
		 List<DataRow> rows = dt.select("ParentNo='" + parentRow.getValue("No") + "'");
		for(DataRow r : rows)
		{
			newDt.Rows.AddRow(r);

			GenerChildRowsX(dt, newDt, r);
		}
	}

	public final String GetBindingFormsTable()
	{
		String fk_flow = GetValFromFrmByKey("fk_flow");
		if (StringHelper.isNullOrEmpty(fk_flow))
		{
			return "[]";
		}

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT wfn.FK_Frm NO,");
		sql.append("       smd.NAME,");
		sql.append("       NULL PARENTNO,");
		sql.append("       'FORM' TTYPE,");
		sql.append("       -1 DTYPE,");
		sql.append("       0 ISPARENT");
		sql.append("FROM   WF_FrmNode wfn");
		sql.append("       INNER JOIN Sys_MapData smd");
		sql.append("            ON  smd.No = wfn.FK_Frm");
		sql.append("WHERE  wfn.FK_Flow = '{0}'");
		sql.append("       AND wfn.FK_Node = (");
		sql.append("               SELECT wn.NodeID");
		sql.append("               FROM   WF_Node wn");
		sql.append("               WHERE  wn.FK_Flow = '{0}' AND wn.NodePosType = 0");
		sql.append("           )");

		DataTable dt = DBAccess.RunSQLReturnTable(String.format(sql.toString(), fk_flow));
		return BP.Tools.Json.DataTableToJson(dt, false);
	}

	public final String GetFormTreeTable() throws Exception
	{

		 // #region 检查数据是否符合规范.
          String rootNo =DBAccess.RunSQLReturnStringIsNull("SELECT No FROM Sys_FormTree WHERE ParentNo='' OR ParentNo IS NULL", null);
          if (DataType.IsNullOrEmpty(rootNo)==false)
          {
              //删除垃圾数据.
              DBAccess.RunSQL(String.format("DELETE FROM Sys_FormTree WHERE No='{0}'", rootNo));
          }
          //检查根目录是否符合规范.
          FrmTree ft = new FrmTree();
         int i=  ft.Retrieve(FrmTreeAttr.ParentNo, "0");          
          if (i== 0)
          {
              ft.setName("表单库");
              ft.setParentNo("0");
              ft.Insert();
          }


         // #endregion 检查数据是否符合规范.

          //组织数据源.
          String sqls = "";
          if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
          {
              sqls = "SELECT No \"No\", ParentNo \"ParentNo\",Name \"Name\", Idx \"Idx\", 1 \"IsParent\", 'FORMTYPE' \"TType\" FROM Sys_FormTree ORDER BY Idx ASC ; ";
              sqls += "SELECT No \"No\", FK_FormTree as \"ParentNo\", Name \"Name\",Idx \"Idx\", 0 \"IsParent\", 'FORM' \"TType\" FROM Sys_MapData  WHERE AppType=0 AND FK_FormTree IN (SELECT No FROM Sys_FormTree) ORDER BY Idx ASC";
          }
          else
          {
              sqls = "SELECT No,ParentNo,Name, Idx, 1 IsParent, 'FORMTYPE' TType FROM Sys_FormTree ORDER BY Idx ASC ; ";
              sqls += "SELECT No, FK_FormTree as ParentNo,Name,Idx,0 IsParent, 'FORM' TType FROM Sys_MapData  WHERE AppType=0 AND FK_FormTree IN (SELECT No FROM Sys_FormTree) ORDER BY Idx ASC";
          }
          
         // String sqls = "SELECT No,ParentNo,Name, Idx, 1 IsParent, 'FORMTYPE' TType FROM Sys_FormTree ORDER BY Idx ASC ; ";
          //sqls += "SELECT No, FK_FormTree as ParentNo,Name,Idx,0 IsParent, 'FORM' TType FROM Sys_MapData  WHERE AppType=0 AND FK_FormTree IN (SELECT No FROM Sys_FormTree) ORDER BY Idx ASC";
          
          
          DataSet ds = DBAccess.RunSQLReturnDataSet(sqls);

          //获得表单数据.
          DataTable dtSort = ds.Tables.get(0); //类别表.
          DataTable dtForm = ds.Tables.get(1).copy(); //表单表,这个是最终返回的数据.
          

          //增加顶级目录.
          DataRow[] rowsOfSort = dtSort.Select("ParentNo='0'");
          DataRow drFormRoot = dtForm.NewRow();
          drFormRoot.setValue("No", rowsOfSort[0].getValue("No"));
          drFormRoot.setValue("ParentNo", "0");
          drFormRoot.setValue("Name", rowsOfSort[0].getValue("Name"));
          drFormRoot.setValue("Idx", rowsOfSort[0].getValue("Idx"));
          drFormRoot.setValue("IsParent", rowsOfSort[0].getValue("IsParent"));
          drFormRoot.setValue("TType", rowsOfSort[0].getValue("TType"));
          dtForm.Rows.add(drFormRoot); 
         //增加顶级类别..
          
          //把类别数据组装到form数据里.
          for (DataRow dr : dtSort.Rows)
          {
              DataRow drForm = dtForm.NewRow();
              drForm.setValue("No", dr.getValue("No"));
              drForm.setValue("ParentNo", dr.getValue("ParentNo")); 
              drForm.setValue("Name", dr.getValue("Name")); 
              drForm.setValue("Idx", dr.getValue("Idx")); 
              drForm.setValue("IsParent", dr.getValue("IsParent")); 
              drForm.setValue("TType", dr.getValue("TType")); 
              dtForm.Rows.add(drForm); //类别.
          }
          for(DataRow row : ds.Tables.get(1).Rows)
          {
              dtForm.Rows.add(row);
          }

          if (WebUser.getNo().equals("admin")==false)
          {
              AdminEmp aemp = new AdminEmp();
              aemp.setNo(WebUser.getNo());
              aemp.RetrieveFromDBSources();

              if (aemp.getUserType() != 1)
                  return "err@您[" + WebUser.getNo() + "]已经不是二级管理员了.";
              if (aemp.getRootOfForm().equals("") )
                  return "err@没有给二级管理员[" + WebUser.getNo() + "]设置表单树的权限...";

              DataRow[] rootRows = dtForm.Select("No='" + aemp.getRootOfForm() + "'");
              DataRow newRootRow = rootRows[0];

              newRootRow.setValue("ParentNo", "0");
              DataTable newDt = dtForm.copy();
              newDt.Rows.AddRow(newRootRow);

              GenerChildRows(dtForm, newDt, newRootRow);
              dtForm = newDt;
          }
          
          String str= BP.Tools.Json.ToJson(dtForm);
         // BP.DA.DataType.WriteFile("C:\\TreeJflow.txt", str);
          return str; 
	}
	 

	public final String GetStructureTreeTable() throws Exception
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);

		if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
		{
			BP.WF.Port.Depts depts = new BP.WF.Port.Depts();
			depts.RetrieveAll();
			BP.WF.Port.Stations sts = new BP.WF.Port.Stations();
			sts.RetrieveAll();
			BP.WF.Port.Emps emps = new BP.WF.Port.Emps();
			emps.RetrieveAll(BP.WF.Port.EmpAttr.Name);
			BP.WF.Port.EmpStations empsts = new BP.WF.Port.EmpStations();
			empsts.RetrieveAll();
			BP.GPM.DeptEmps empdetps = new BP.GPM.DeptEmps();
			empdetps.RetrieveAll();

			//部门人员
			java.util.HashMap<String, java.util.ArrayList<String>> des = new java.util.HashMap<String, java.util.ArrayList<String>>();
			//岗位人员
			java.util.HashMap<String, java.util.ArrayList<String>> ses = new java.util.HashMap<String, java.util.ArrayList<String>>();
			//部门岗位
			java.util.HashMap<String, java.util.ArrayList<String>> dss = new java.util.HashMap<String, java.util.ArrayList<String>>();
			BP.WF.Port.Station stt = null;
			BP.WF.Port.Emp empt = null;

			for (BP.WF.Port.Dept dept : depts.ToJavaList())
			{
				//增加部门
				dt.Rows.AddDatas(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");
				des.put(dept.getNo(), new java.util.ArrayList<String>());
				dss.put(dept.getNo(), new java.util.ArrayList<String>());

				//获取部门下的岗位
				empdetps.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, dept.getNo());
				for (BP.GPM.DeptEmp empdept : empdetps.ToJavaList())
				{
					des.get(dept.getNo()).add(empdept.getFK_Emp());
					//判断该人员拥有的岗位
					empsts.Retrieve(BP.WF.Port.EmpStationAttr.FK_Emp, empdept.getFK_Emp());
					for (BP.WF.Port.EmpStation es : empsts.ToJavaList())
					{
						if (ses.containsKey(es.getFK_Station()))
						{
							if (ses.get(es.getFK_Station()).contains(es.getFK_Emp()) == false)
							{
								ses.get(es.getFK_Station()).add(es.getFK_Emp());
							}
						}
						else
						{
							ses.put(es.getFK_Station(), new java.util.ArrayList<String>(java.util.Arrays.asList(new String[] { es.getFK_Emp() })));
						}

						//增加部门的岗位
						if (dss.get(dept.getNo()).contains(es.getFK_Station()) == false)
						{
							Object tempVar = sts.GetEntityByKey(es.getFK_Station());
							stt = (BP.WF.Port.Station)((tempVar instanceof BP.WF.Port.Station) ? tempVar : null);

							if (stt == null)
							{
								continue;
							}

							dss.get(dept.getNo()).add(es.getFK_Station());
							dt.Rows.AddDatas(dept.getNo() + "|" + es.getFK_Station(), dept.getNo(), stt.getName(), "STATION");
						}
					}
				}
			}

			for (java.util.Map.Entry<String, java.util.ArrayList<String>> ds : dss.entrySet())
			{
				for (String st : ds.getValue())
				{
					for (String emp : ses.get(st))
					{
						Object tempVar2 = emps.GetEntityByKey(emp);
						empt = (BP.WF.Port.Emp)((tempVar2 instanceof BP.WF.Port.Emp) ? tempVar2 : null);

						if (empt == null)
						{
							continue;
						}

						dt.Rows.AddDatas(ds.getKey() + "|" + st + "|" + emp, ds.getKey() + "|" + st, empt.getName(), "EMP");
					}
				}
			}
		}
		else
		{
			BP.GPM.Depts depts = new BP.GPM.Depts();
			depts.RetrieveAll();
			BP.GPM.Stations sts = new BP.GPM.Stations();
			sts.RetrieveAll();
			BP.GPM.Emps emps = new BP.GPM.Emps();
			emps.RetrieveAll(BP.WF.Port.EmpAttr.Name);
			BP.GPM.DeptStations dss = new BP.GPM.DeptStations();
			dss.RetrieveAll();
			BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
			dess.RetrieveAll();
			BP.GPM.Station stt = null;
			BP.GPM.Emp empt = null;

			for (BP.GPM.Dept dept : depts.ToJavaList())
			{
				//增加部门
				dt.Rows.AddDatas(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");

				//增加部门岗位
				dss.Retrieve(BP.GPM.DeptStationAttr.FK_Dept, dept.getNo());
				for (BP.GPM.DeptStation ds : dss.ToJavaList())
				{
					Object tempVar3 = sts.GetEntityByKey(ds.getFK_Station());
					stt = (BP.GPM.Station)((tempVar3 instanceof BP.GPM.Station) ? tempVar3 : null);

					if (stt == null)
					{
						continue;
					}

					dt.Rows.AddDatas(dept.getNo() + "|" + ds.getFK_Station(), dept.getNo(), stt.getName(), "STATION");

					//增加部门岗位人员
					dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, dept.getNo(), BP.GPM.DeptEmpStationAttr.FK_Station, ds.getFK_Station());

					for (BP.GPM.DeptEmpStation des : dess.ToJavaList())
					{
						Object tempVar4 = emps.GetEntityByKey(des.getFK_Emp());
						empt = (BP.GPM.Emp)((tempVar4 instanceof BP.GPM.Emp) ? tempVar4 : null);

						if (empt == null)
						{
							continue;
						}

						dt.Rows.AddDatas(dept.getNo() + "|" + ds.getFK_Station() + "|" + des.getFK_Emp(), dept.getNo() + "|" + ds.getFK_Station(), empt.getName(), "EMP");
					}
				}
			}
		}

		return BP.Tools.Json.DataTableToJson(dt, false);
	}
	/** 
	 根据DataTable生成Json树结构
	 
	*/
	public final String GetTreeJsonByTable(DataTable tabel, Object pId, String rela, String idCol, String txtCol, String IsParent, String sChecked, String[] attrFields)
	{
		String treeJson = "";

		if (tabel.Rows.size() > 0)
		{
			sbJson.append("[");
			String filer = "";
			if (pId.toString().equals(""))
			{
				filer = String.format("%1$s is null or %1$s='-1' or %1$s='0' or %1$s='F0'", rela);
			}
			else
			{
				filer = String.format("%1$s='%2$s'", rela, pId);
			}
			
			List<DataRow> rows = tabel.select(filer);//tabel.select(filer, idCol);
			if (rows.size() > 0)
			{
				for (int i = 0; i < rows.size(); i++)
				{
					DataRow row = rows.get(i);


					String jNo = (String)((row.getValue(idCol)instanceof String) ? row.getValue(idCol) : null);
					String jText = (String)((row.getValue(txtCol) instanceof String) ? row.getValue(txtCol) : null);
					if (jText.length() > 25)
					{
						jText = jText.substring(0, 25) + "<img src='../Scripts/easyUI/themes/icons/add2.png' onclick='moreText(" + jNo + ")'/>";
					}

					String jIsParent = row.getValue(IsParent).toString();
					String jState = (new String("1")).equals(jIsParent) ? "open" : "closed";
					jState = (new String("open")).equals(jState) && i == 0 ? "open" : "closed";

					List<DataRow> rowChild = tabel.select(String.format("%1$s='%2$s'", rela, jNo));
					String tmp = "{\"id\":\"" + jNo + "\",\"text\":\"" + jText;

					//增加自定义attributes列，added by liuxc,2015-10-6
					String attrs = "";
					if (attrFields != null && attrFields.length > 0)
					{
						for (String field : attrFields)
						{
							if (!tabel.Columns.contains(field))
							{
								continue;
							}
							if (DotNetToJavaStringHelper.isNullOrEmpty(row.getValue(field).toString()))
							{
								attrs += ",\"" + field + "\":\"\"";
								continue;
							}
							 attrs += ",\"" + field + "\":" + 
							(tabel.Columns.get(field).DataType == 
							String.class ? String.format("\"%1$s\"", 
									row.getValue(field)) : row.getValue(field));
						}
					}

					if ((new String("0")).equals(pId.toString()) || row.getValue(rela).toString().equals("F0"))
					{
						tmp += "\",\"attributes\":{\"IsParent\":\"" + jIsParent + "\",\"IsRoot\":\"1\"" + attrs + "}";
					}
					else
					{
						tmp += "\",\"attributes\":{\"IsParent\":\"" + jIsParent + "\"" + attrs + "}";
					}

					if (rowChild.size() > 0)
					{
						tmp += ",\"checked\":" + String.valueOf(sChecked.contains("," + jNo + ",")).toLowerCase() + ",\"state\":\"" + jState + "\"";
					}
					else
					{
						tmp += ",\"checked\":" + String.valueOf(sChecked.contains("," + jNo + ",")).toLowerCase();
					}

					sbJson.append(tmp);
					if (rowChild.size() > 0)
					{
						sbJson.append(",\"children\":");
						GetTreeJsonByTable(tabel, jNo, rela, idCol, txtCol, IsParent, sChecked, attrFields);
					}

					sbJson.append("},");
				}
				sbJson = sbJson.deleteCharAt(sbJson.length() - 1);
			}
			sbJson.append("]");
			treeJson = sbJson.toString();
		}
		return treeJson;
	}

	/** 
	 删除流程
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DelFlow() throws Exception
	{
		return WorkflowDefintionManager.DeleteFlowTemplete(this.getFK_Flow());
	}
	public final String NewFlow()
	{
		try
		{
			String[] ps = this.GetRequestVal("paras").split("[,]", -1);
			if (ps.length != 6)			
				throw new RuntimeException("@创建流程参数错误");
			

			String fk_floSort = ps[0]; //类别编号.
			fk_floSort = fk_floSort.replace("F", ""); //传入的编号多出F符号，需要替换掉

			String flowName = ps[1]; // 流程名称.
			DataStoreModel dataSaveModel = DataStoreModel.forValue(Integer
					.parseInt(ps[2])); // 数据保存方式。
			String pTable = ps[3]; // 物理表名。
			String flowMark = ps[4]; // 流程标记.
			String flowVer = ps[5]; // 流程版本

			String flowNo = BP.WF.Template.TemplateGlo.NewFlow(fk_floSort, flowName, dataSaveModel, pTable, flowMark, flowVer);
			
			  //清空WF_Emp 的StartFlows
            DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");
            
            return flowNo;

		}
		catch (Exception ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String DelNode() throws Exception
	{
		try
		{
			BP.WF.Node nd = new BP.WF.Node();
			nd.setNodeID(this.getFK_Node());
			nd.Delete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String GetFlowSorts() throws Exception
	{
		FlowSorts flowSorts = new FlowSorts();
		flowSorts.RetrieveAll(FlowSortAttr.Idx);

		BP.WF.Port.AdminEmp emp = new AdminEmp(BP.Web.WebUser.getNo());

		return BP.Tools.Entitis2Json.ConvertEntitis2GenerTree(flowSorts, emp.getRootOfFlow());
	}
	/** 
	 删除流程类别.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DelFlowSort() throws Exception
	{
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");

		FlowSort fs = new FlowSort();
		fs.setNo(fk_flowSort);

		//检查是否有子流程？
		String sql = "SELECT COUNT(*) FROM WF_Flow WHERE FK_FlowSort='" + fk_flowSort + "'";
		if (DBAccess.RunSQLReturnValInt(sql) != 0)
		{
			return "err@该目录下有流程，您不能删除。";
		}

		//检查是否有子目录？
		sql = "SELECT COUNT(*) FROM WF_FlowSort WHERE ParentNo='" + fk_flowSort + "'";
		if (DBAccess.RunSQLReturnValInt(sql) != 0)
		{
			return "err@该目录下有子目录，您不能删除。";
		}

		fs.Delete();

		return "删除成功.";
	}
	/** 
	 新建同级流程类别
	 @于庆海对照需要翻译
	 @return 
	 * @throws Exception 
	*/
	public final String NewSameLevelFlowSort() throws Exception
	{
		FlowSort fs = null;
		fs = new FlowSort(this.getNo().replace("F", "")); //传入的编号多出F符号，需要替换掉
		
		String orgNo = fs.getOrgNo();  //记录原来的组织结构编号. @于庆海对照需要翻译
		
		String sameNodeNo = fs.DoCreateSameLevelNode().getNo();
		fs = new FlowSort(sameNodeNo);
		fs.setName(this.getName());
		fs.setOrgNo(orgNo); // 组织结构编号. @于庆海对照需要翻译
		fs.Update();
		return "F" + fs.getNo();
	}
	/** 
	 新建下级类别.
	 @return 
	 * @throws Exception 
	*/
	public final String NewSubFlowSort() throws Exception
	{
		FlowSort fsSub = new FlowSort(this.getNo().replace("F", "")); //传入的编号多出F符号，需要替换掉
		String orgNo = fsSub.getOrgNo(); //记录原来的组织结构编号. @于庆海对照需要翻译
		String subNodeNo = fsSub.DoCreateSubNode().getNo();
		FlowSort subFlowSort = new FlowSort(subNodeNo);
		subFlowSort.setName(this.getName());
		subFlowSort.setOrgNo(orgNo); // 组织结构编号. @于庆海对照需要翻译
		subFlowSort.Update();
		return "F" + subFlowSort.getNo();
	}

	public final String EditFlowSort() throws Exception
	{
		String flowNo = this.getNo().replace("F", "");
		FlowSort fs = new FlowSort(flowNo); //传入的编号多出F符号，需要替换掉
		fs.setNo(flowNo);
		fs.RetrieveFromDBSources();
		fs.setName(this.getName());
		fs.Update();
		return fs.getNo();
	}

	/** 
	 让admin 登陆
	 
	 @param lang 当前的语言
	 @return 成功则为空，有异常时返回异常信息
	 * @throws Exception 
	*/
	public final String LetAdminLogin(String empNo, boolean islogin) throws Exception
	{
		try
		{
			if (islogin)
			{
				BP.Port.Emp emp = new BP.Port.Emp(empNo);
				WebUser.SignInOfGener(emp);
			}
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
		return "@登录成功.";
	}

	/** 流程检查.
	 
	 @return 
	 * @throws Exception 
	 */
	public final String FlowCheck_Init() throws Exception
	{
		BP.WF.Flow fl = new BP.WF.Flow(this.getFK_Flow());
		String info = fl.DoCheck().replace("@", "<BR>@");
		info = info.replace("@错误", "<font color=red><b>@错误</b></font>");
		info = info.replace("@警告", "<font color=yellow><b>@警告</b></font>");
		info = info.replace("@信息", "<font color=black><b>@信息</b></font>");
		return info;
	}
	/*
	 * 流程上移
	 */
	public final String MoveUpFlow() throws Exception
	{
		Flow flow = new Flow(getFK_Flow());
		flow.DoUp();
		return flow.getNo();
	}
	/*
	 * 流程下移
	 */
	public final String MoveDownFlow() throws Exception
	{
		Flow flow = new Flow(this.getFK_Flow());
		flow.DoDown();
		return flow.getNo();
	}
	/*
	 * 上移流程类别
	 */
	  public final String MoveUpFlowSort() throws Exception
			{
				String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");
				FlowSort fsSub = new FlowSort(fk_flowSort); //传入的编号多出F符号，需要替换掉
				fsSub.DoUp();
				return "F" + fsSub.getNo();
			}
	  /*
	   * 下移流程类别
	   */
	  public final String MoveDownFlowSort() throws Exception
			{
				String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");
				FlowSort fsSub = new FlowSort(fk_flowSort); //传入的编号多出F符号，需要替换掉
				fsSub.DoDown();
				return "F" + fsSub.getNo();
			}
	  
	  
			/** 
			 表单树 - 编辑表单类别
			 
			 @return 
			 * @throws Exception 
			*/
			public final String CCForm_EditCCFormSort() throws Exception
			{
				SysFormTree formTree = new SysFormTree(this.getNo());
				formTree.setName(this.getName());
				formTree.Update();
				return this.getNo();
			}
			/** 
			 表单树 - 删除表单类别
			 
			 @return 
			 * @throws Exception 
			*/
			public final String CCForm_DelFormSort() throws Exception
			{
				SysFormTree formTree = new SysFormTree(this.getNo());

				//检查是否有子类别？
				String sql = "SELECT COUNT(*) FROM Sys_FormTree WHERE ParentNo='" + this.getNo() + "'";
				if (DBAccess.RunSQLReturnValInt(sql) != 0)
				{
					return "err@该目录下有子类别，您不能删除。";
				}

				//检查是否有表单？
				sql = "SELECT COUNT(*) FROM Sys_MapData WHERE FK_FormTree='" + this.getNo() + "'";
				if (DBAccess.RunSQLReturnValInt(sql) != 0)
				{
					return "err@该目录下有表单，您不能删除。";
				}

				formTree.Delete();
				return "删除成功";
			}
			/** 
			 表单树-上移表单类别
			 
			 @return 
			 * @throws Exception 
	*/
			public final String CCForm_MoveUpCCFormSort() throws Exception
			{
				SysFormTree formTree = new SysFormTree(this.getNo());
				formTree.DoUp();
				return formTree.getNo();
			}
			/** 
			 表单树-下移表单类别
			 
			 @return 
			 * @throws Exception 
			*/
			public final String CCForm_MoveDownCCFormSort() throws Exception
			{
				SysFormTree formTree = new SysFormTree(this.getNo());
				formTree.DoDown();
				return formTree.getNo();
			}
	  
	  /** 
		 表单树-上移表单
		 
		 @return 
	 * @throws Exception 
*/
		public final String CCForm_MoveUpCCFormTree() throws Exception
		{
			MapData mapData = new MapData(this.getFK_MapData());
			mapData.DoUp();
			return mapData.getNo();
		}
		/** 
		 表单树-下移表单
		 
		 @return 
		 * @throws Exception 
		*/
		public final String CCForm_MoveDownCCFormTree() throws Exception
		{
			MapData mapData = new MapData(this.getFK_MapData());
			mapData.DoOrderDown();
			return mapData.getNo();
		}

		/** 
		 表单树 - 删除表单
		 
		 @return 
		 * @throws Exception 
		*/
		public final String CCForm_DeleteCCFormMapData() throws Exception
		{
			MapData mapData = new MapData(this.getFK_MapData());
			mapData.Delete();
			return mapData.getNo();
		}
		
		public String Login_InitInfo() throws Exception
        {
			 
			
            Hashtable ht = new Hashtable();
            ht.put("SysNo", SystemConfig.getSysNo()==null?"":SystemConfig.getSysNo());
            ht.put("SysName", SystemConfig.getSysName()==null?"":SystemConfig.getSysName());

            return BP.Tools.Json.ToJson(ht);
        }
}
  