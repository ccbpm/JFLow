package bp.wf;
import bp.da.DBAccess;
import bp.da.DataRow;
import bp.da.DataSet;
import bp.da.DataTable;
import bp.web.WebUser;
/** 
 第三方应用开发的应用,非标准应用.
*/
public class AppClass
{

		///进度条.
	/** 
	 进度条 - for 为中科曙光.
	 1. 处理进度条.
	 
	 @param workid
	 @return 
	 * @throws Exception 
	*/
	public static String JobSchedule(long workid) throws Exception
	{
		
		
		//bp.wf.httphandler.WF_Comm com=new bp.wf.httphandler.WF_Comm();
		//com.RichUploadFile();
		
		DataSet ds = bp.wf.Dev2Interface.DB_JobSchedule(workid);

		DataTable gwf = ds.GetTableByName("WF_GenerWorkFlow"); //工作记录.
		DataTable nodes = ds.GetTableByName("WF_Node"); //节点.
		DataTable dirs = ds.GetTableByName("WF_Direction"); //连接线.
		DataTable tracks = ds.GetTableByName("Track"); //历史记录.

		//状态,
		int wfState = Integer.parseInt(gwf.Rows.get(0).getValue("WFState").toString());
		int currNode = Integer.parseInt(gwf.Rows.get(0).getValue("FK_Node").toString());
		if (wfState == 3)
		{
			return bp.tools.Json.ToJson(tracks); //如果流程结束了，所有的数据都在tracks里面.
		}

		//把以后的为未完成的节点放入到track里面.
		for (int i = 0; i < 100; i++)
		{
 
				///判断当前节点的类型.
			// 如果是 =0 就说明有分支，有分支就判断当前节点是否是分河流。
			RunModel model = bp.wf.RunModel.forValue(0);
			for (DataRow dr : nodes.Rows)
			{
				if (Integer.parseInt(dr.getValue("NodeID").toString()) == currNode)
				{
					model = RunModel.forValue(Integer.parseInt(dr.getValue("RunModel").toString()));
				}
			}

			// 分合流.
			if (model == RunModel.FL || model == RunModel.FHL)
			{

				Node nd = new Node(currNode);
				Nodes tonds = nd.getHisToNodes();

				for (Node tond : tonds.ToJavaList())
				{
					DataRow mydr = tracks.NewRow();
					mydr.setValue("NodeName", tond.getName());
					mydr.setValue("FK_Node", tond.getNodeID()); // nd["NodeID"].ToString();
					mydr.setValue("RunModel", tond.getHisRunModel().getValue());
					tracks.Rows.add(mydr);

					//设置当前节点.
				  //  currNode = tond.HisToNodes[0].GetValIntByKey("NodeID");
					currNode = tond.getNodeID();
				}
			}

				/// 判断当前节点的类型.

			int nextNode = GetNextNodeID(currNode, dirs);
			if (nextNode == 0)
			{
				break;
			}

			for (DataRow nd : nodes.Rows)
			{
				if (Integer.parseInt(nd.getValue("NodeID").toString()) == nextNode)
				{
					DataRow mydr = tracks.NewRow();
					mydr.setValue("NodeName", nd.getValue("Name").toString());
					mydr.setValue("FK_Node", nd.getValue("NodeID").toString());
					mydr.setValue("RunModel", nd.getValue("RunModel").toString());
					tracks.Rows.add(mydr);
					break;
				}
			}
			currNode = nextNode;
		}


	   //去掉重复的节点.
		DataTable dtNew = new DataTable();
		dtNew.TableName = "Track";
		dtNew.Columns.Add("FK_Node"); //节点ID.
		dtNew.Columns.Add("NodeName"); //名称.
		dtNew.Columns.Add("RunModel"); //节点类型.
		dtNew.Columns.Add("EmpNo"); //人员编号.
		dtNew.Columns.Add("EmpName"); //名称
		dtNew.Columns.Add("DeptName"); //部门名称
		dtNew.Columns.Add("RDT"); //记录日期.
		dtNew.Columns.Add("SDT"); //应完成日期(可以不用.)
		dtNew.Columns.Add("IsPass"); //是否通过?

		String strs = "";
		for (DataRow dr : tracks.Rows)
		{
			String nodeID = dr.getValue("FK_Node").toString();
			String isPass = dr.getValue("IsPass").toString();
			if (strs.contains("," + nodeID) == true)
			{
				continue;
			}

			strs += "," + nodeID + ",";

			DataRow drNew = dtNew.NewRow();
			drNew.setValue("FK_Node", dr.getValue("FK_Node"));
			drNew.setValue("NodeName", dr.getValue("NodeName"));
			drNew.setValue("RunModel", dr.getValue("RunModel"));
			drNew.setValue("EmpNo", dr.getValue("EmpNo"));
			drNew.setValue("EmpName", dr.getValue("EmpName"));
			drNew.setValue("DeptName", dr.getValue("DeptName"));
			drNew.setValue("RDT", dr.getValue("RDT"));
			drNew.setValue("SDT", dr.getValue("SDT"));
			drNew.setValue("IsPass", dr.getValue("IsPass"));

			dtNew.Rows.add(drNew);
		}

		return bp.tools.Json.ToJson(dtNew);
	}
	//根据当前节点获得下一个节点.
	private static int GetNextNodeID(int nodeID, DataTable dirs)
	{
		int toNodeID = 0;
		for (DataRow dir : dirs.Rows)
		{
			if (Integer.parseInt(dir.getValue("Node").toString()) == nodeID)
			{
				toNodeID = Integer.parseInt(dir.getValue("ToNode").toString());
				break;
			}
		}
		int toNodeID2 = 0;
		for (DataRow dir : dirs.Rows)
		{
			if (Integer.parseInt(dir.getValue("Node").toString()) == nodeID)
			{
				toNodeID2 = Integer.parseInt(dir.getValue("ToNode").toString());
			}
		}

		//两次去的不一致，就有分支，有分支就reutrn 0 .
		if (toNodeID2 == toNodeID)
		{
			return toNodeID;
		}



		return 0;
	}

		/// 进度条.


		///sdk表单装载的时候返回的数据.
	/** 
	 sdk表单加载的时候，要返回的数据.
	 1. 系统会处理一些业务,设置当前工作已经读取等等.
	 2. 会判断权限，当前人员是否可以打开当前的工作.
	 3. 增加了一些审核组件的数据信息.
	 4. WF_Node的 FWCSta 是审核组件的状态  0=禁用,1=启用,2=只读.
	 
	 @param workid 工作ID
	 @return 初始化的sdk表单页面信息
	 * @throws Exception 
	*/
	public static String SDK_Page_Init(long workid) throws Exception
	{
		try
		{
			GenerWorkFlow gwf = new GenerWorkFlow(workid);

			Node nd = new Node(gwf.getFK_Node());

			//加载接口.
			DataSet ds = new DataSet();
			ds = bp.wf.CCFlowAPI.GenerWorkNode(gwf.getFK_Flow(), nd, gwf.getWorkID(), gwf.getFID(), WebUser.getNo(),gwf.getWorkID());

			//要保留的tables.
		   // string tables = ",WF_GenerWorkFlow,WF_Node,AlertMsg,Track,";

			//移除不要的数据.
			ds.Tables.remove("Sys_MapData");
			ds.Tables.remove("Sys_MapDtl");
			ds.Tables.remove("Sys_Enum");
			ds.Tables.remove("Sys_MapExt");
			ds.Tables.remove("Sys_FrmLine");
			ds.Tables.remove("Sys_FrmLink");
			ds.Tables.remove("Sys_FrmBtn");
			ds.Tables.remove("Sys_FrmLab");
			ds.Tables.remove("Sys_FrmImg");
			ds.Tables.remove("Sys_FrmRB");
			ds.Tables.remove("Sys_FrmEle");
			ds.Tables.remove("Sys_MapFrame");
			ds.Tables.remove("Sys_FrmAttachment");
			ds.Tables.remove("Sys_FrmImgAth");
			ds.Tables.remove("Sys_FrmImgAthDB");
			ds.Tables.remove("Sys_MapAttr");
			ds.Tables.remove("Sys_GroupField");
			ds.Tables.remove("WF_FrmNodeComponent");
			ds.Tables.remove("MainTable");
			ds.Tables.remove("UIBindKey");

			if (ds.GetTableByName("bp.port.Depts") !=null)
			{
				ds.Tables.remove("bp.port.Depts");
			}


			//获得审核信息.

			//历史执行人. 
			String sql = "SELECT C.Name AS DeptName,A.MyPK,A.ActionType,A.ActionTypeText,A.getFID(),A.WorkID,A.NDFrom,A.NDFromT,A.NDTo,A.NDToT,A.EmpFrom,A.EmpFromT,A.EmpTo,A.EmpToT,A.RDT,A.WorkTimeSpan,A.Msg,A.NodeData,A.Tag,A.Exer FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track A, Port_Emp B, Port_Dept C WHERE A.WorkID=" + workid + " AND (A.ActionType=" + ActionType.WorkCheck.getValue() + ") AND (A.EmpFrom=B.getNo()) AND (B.FK_Dept=C.getNo()) ORDER BY A.RDT DESC";
			DataTable dtTrack = DBAccess.RunSQLReturnTable(sql);
			dtTrack.TableName = "Track";
			ds.Tables.add(dtTrack);

			return bp.tools.Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

		/// sdk表单装载的时候返回的数据.
}