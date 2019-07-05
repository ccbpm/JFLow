package BP.WF;

import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;

/**
 * ������Ӧ�ÿ�����Ӧ��,�Ǳ�׼Ӧ��.
 * 
 */
public class AppClass {
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #region ������.
	/**
	 * 进度条 - for 为中科曙光.
	 * 
	 * @param workid
	 * @return
	 * @throws Exception
	 */
	public static String JobSchedule(long workid) throws Exception {

		DataSet ds = BP.WF.Dev2Interface.DB_JobSchedule(workid);

		DataTable gwf = ds.GetTableByName("WF_GenerWorkFlow"); // 工作记录.
		DataTable nodes = ds.GetTableByName("WF_Node"); // 节点.
		DataTable dirs = ds.GetTableByName("WF_Direction"); // 连接线.
		DataTable tracks = ds.GetTableByName("Track"); // 历史记录.

		// 状态,
		int wfState = Integer.parseInt(gwf.Rows.get(0).getValue("WFState").toString());
		int currNode = Integer.parseInt(gwf.Rows.get(0).getValue("FK_Node").toString());
		// String currNode = gwf.Rows.get(0).getValue("FK_Node").toString();
		// //停留节点.
		if (wfState == 3)
		{
			 
			DataSet myds1=new DataSet();
			myds1.Tables.add(gwf);
			myds1.Tables.add(tracks);
			
			return BP.Tools.Json.ToJson( myds1);
			
			//return BP.Tools.Json.ToJson(tracks); // 如果流程结束了，所有的数据都在tracks里面.
		}

		// 把以后的为未完成的节点放入到track里面.
		for (int i = 0; i < 100; i++) {

			// #region 判断当前节点的类型.
			// 如果是 =0 就说明有分支，有分支就判断当前节点是否是分河流。
			RunModel model = RunModel.Ordinary;
			for (DataRow dr : nodes.Rows) {
				if (Integer.parseInt(dr.getValue("NodeID").toString()) == currNode) {
					model = RunModel.forValue(Integer.parseInt(dr.getValue("RunModel").toString()));
				}
			}

			// 分合流.
			if (model == RunModel.FL || model == RunModel.FHL) {

				Node nd = new Node(currNode);
				Nodes tonds = nd.getHisToNodes();

				for (Node tond : tonds.ToJavaList()) {
					DataRow mydr = tracks.NewRow();
					mydr.setValue("NodeName", tond.getName());
					mydr.setValue("FK_Node", tond.getNodeID());
					mydr.setValue("RunModel", tond.getHisRunModel().getValue());
					// mydr["NodeName"] = tond.Name;
					// mydr["FK_Node"] = tond.NodeID; //
					// nd["NodeID"].ToString();
					// mydr["RunModel"] = (int)tond.HisRunModel;
					tracks.Rows.add(mydr);

					// 设置当前节点.
					// currNode = tond.HisToNodes[0].GetValIntByKey("NodeID");
					currNode = tond.getNodeID();
				}
			}
			// #endregion 判断当前节点的类型.

			int nextNode = GetNextNodeID(currNode, dirs);
			if (nextNode == 0)
				break;

			for (DataRow nd : nodes.Rows) {
				if (Integer.parseInt(nd.getValue("NodeID").toString()) == nextNode) {

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
		
		
		DataSet myds=new DataSet();
		myds.Tables.add(gwf);
		myds.Tables.add(tracks);
		
		return BP.Tools.Json.ToJson( myds);

	}

	// 根据当前节点获得下一个节点.
	public static int GetNextNodeID(int nodeID, DataTable dirs) {
		int toNodeID = 0;
		for (DataRow dir : dirs.Rows) {
			if (Integer.parseInt(dir.getValue("Node").toString()) == nodeID) {
				toNodeID = Integer.parseInt(dir.getValue("ToNode").toString());
				break;
			}
		}

		int toNodeID2 = 0;

		for (DataRow dir11 : dirs.Rows) {
			if (Integer.parseInt(dir11.getValue("Node").toString()) == nodeID) {
				toNodeID2 = Integer.parseInt(dir11.getValue("ToNode").toString());
			}
		}

		// 两次去的不一致，就有分支，有分支就reutrn 0 .
		if (toNodeID2 == toNodeID)
			return toNodeID;
		return 0;
	}

	/**
	 * sdk表单加载的时候，要返回的数据. 1. 系统会处理一些业务,设置当前工作已经读取等等. 2. 会判断权限，当前人员是否可以打开当前的工作.
	 * 3. 增加了一些审核组件的数据信息. 4. WF_Node的 FWCSta 是审核组件的状态 0=禁用,1=启用,2=只读.
	 * 
	 * @param workid
	 *            工作ID
	 * @return 初始化的sdk表单页面信息
	 * @throws Exception
	 */
	public static String SDK_Page_Init(long workid) throws Exception {
		
		try {
			
			GenerWorkFlow gwf = new GenerWorkFlow(workid);

			// 加载接口.
			DataSet ds = new DataSet();
			ds = BP.WF.CCFlowAPI.GenerWorkNode(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID(),
					BP.Web.WebUser.getNo(), "");

			// 要保留的tables.
			// String tables = ",WF_GenerWorkFlow,WF_Node,AlertMsg,Track,";

			// 移除不要的数据.
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

			if (ds.Tables.contains("BP.Port.Depts") == true) {
				ds.Tables.remove("BP.Port.Depts");
			}

			// 获得审核信息.

			// 历史执行人.
			String sql = "SELECT C.Name AS DeptName, A.* FROM ND" + Integer.parseInt(gwf.getFK_Flow())
					+ "Track A, Port_Emp B, Port_Dept C WHERE A.WorkID=" + workid
					+ " AND (A.ActionType=22) AND (A.EmpFrom=B.No) AND (B.FK_Dept=C.No) ORDER BY A.RDT DESC";
			DataTable dtTrack = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dtTrack.TableName = "Track";
			ds.Tables.add(dtTrack);

			return BP.Tools.Json.ToJson(ds);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #endregion sdk��װ�ص�ʱ�򷵻ص�����.
}