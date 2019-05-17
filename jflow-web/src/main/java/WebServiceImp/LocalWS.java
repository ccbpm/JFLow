package WebServiceImp;

import java.util.Hashtable;

import javax.jws.WebService;

import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Entity;
import BP.GPM.Dev2Interface;
import BP.WF.ActionType;
import BP.WF.Flow;
import BP.WF.GenerWorkFlow;
import BP.WF.GenerWorkerList;
import BP.WF.GenerWorkerLists;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.WFState;
import BP.WF.WorkFlow;
import BP.WF.Data.GERpt;

import BP.WF.Template.CondModel;
import BP.WF.Template.Directions;
import BP.WF.Template.FlowExt;
import BP.WF.Template.Selector;
import BP.WF.XML.Tools;
import WebService.LocalWSI;

public class LocalWS implements LocalWSI{
	
	/**
	 * 寰呭姙
	 * @param userNo 鐢ㄦ埛缂栧彿
	 * @param sysNo 绯荤粺缂栧彿,涓虹┖鏃惰繑鍥炲钩鍙版墍鏈夋暟鎹�
	 * @return
	 */
	@Override
	public String DB_Todolist(String userNo, String sysNo) {
		try {
			BP.WF.Dev2Interface.Port_Login(userNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String sql = "";
         if (DataType.IsNullOrEmpty(sysNo) == true)
             sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp='" + userNo + "'";
         else
             sql = "SELECT * FROM WF_EmpWorks WHERE Domain='" + sysNo + "' AND FK_Emp='" + userNo + "'";

         DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
         return BP.Tools.Json.ToJson(dt);
	}
	
	/**
	 * 鑾峰緱鍦ㄩ��
	 * @param userNo 鐢ㄦ埛缂栧彿
	 * @param sysNo 绯荤粺缂栧彿,涓虹┖鏃惰繑鍥炲钩鍙版墍鏈夋暟鎹�
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String DB_Runing(String userNo, String sysNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		 DataTable dt = BP.WF.Dev2Interface.DB_GenerRuning(userNo, null, false);
         return BP.Tools.Json.ToJson(dt);
	}
	
	/**
	 * 鎴戝彲浠ュ彂璧风殑娴佺▼
	 * @param userNo 鐢ㄦ埛缂栧彿
	 * @param sysNo  绯荤粺缂栧彿,涓虹┖鏃惰繑鍥炲钩鍙版墍鏈夋暟鎹�
	 * @return 杩斿洖鎴戝彲浠ュ彂璧风殑娴佺▼鍒楄〃.
	 * @throws Exception 
	 */
	@Override
	public String DB_StarFlows(String userNo, String sysNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		DataTable dt= BP.WF.Dev2Interface.DB_StarFlows(userNo);
        return BP.Tools.Json.ToJson(dt);
	}
	
	/**
	 * 鎴戝彂璧风殑娴佺▼瀹炰緥
	 * @param userNo 鐢ㄦ埛缂栧彿
	 * @param sysNo 缁熺紪鍙�,涓虹┖鏃惰繑鍥炲钩鍙版墍鏈夋暟鎹�
	 * @param pageSize
	 * @param pageIdx
	 * @return
	 */
	@Override
	public String DB_MyStartFlowInstance(String userNo, String sysNo, int pageSize, int pageIdx) {
		try {
			BP.WF.Dev2Interface.Port_Login(userNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "";
        if (sysNo == null)
            sql = "SELECT * FROM WF_GenerWorkFlow WHERE Starter='" + userNo + "'";
        else
            sql = "SELECT * FROM WF_GenerWorkFlow WHERE Domain='" + sysNo + "' AND Starter='" + userNo + "'";

        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        return BP.Tools.Json.ToJson(dt);
	}

	/**
	 * 鍒涘缓WorkID
	 * @param flowNo 娴佺▼缂栧彿
	 * @param userNo 宸ヤ綔浜哄憳缂栧彿
	 * @return 涓�涓暱鏁村瀷鐨勫伐浣滄祦绋嬪疄渚�
	 * @throws Exception 
	 */
	@Override
	public long CreateWorkID(String flowNo, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		 return BP.WF.Dev2Interface.Node_CreateBlankWork(flowNo, userNo);
	}

	/**
	 * 鎵ц鍙戦��
	 * @param flowNo 娴佺殑绋嬫ā鐗圛D
	 * @param workid 宸ヤ綔ID
	 * @param ht 鍙傛暟锛屾垨鑰呰〃鍗曞瓧娈�.
	 * @param toNodeID 鍒拌揪鐨勮妭鐐笽D.濡傛灉璁╃郴缁熻嚜鍔ㄨ绠楀氨浼犲叆0
	 * @param toEmps 鍒拌揪鐨勪汉鍛業Ds,姣斿:zhangsan,lisi,wangwu. 濡傛灉涓篘ull灏辨爣璇嗚绯荤粺鑷姩璁＄畻
	 * @return 鍙戦�佺殑缁撴灉淇℃伅.
	 * @throws Exception 
	 */
	@Override
	public String SendWork(String flowNo, long workid, Hashtable ht, int toNodeID, String toEmps, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		BP.WF.SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(flowNo, workid, ht, toNodeID, toEmps);

        String msg = objs.ToMsgOfText();
        

        Hashtable myht = new Hashtable();
        myht.put("Message", msg);
        myht.put("IsStopFlow", objs.getIsStopFlow());

        if (objs.getIsStopFlow()==false)
        {
>
        myht.put("VarAcceptersID", objs.getVarAcceptersID());
        myht.put("VarAcceptersName", objs.getVarAcceptersName());
        myht.put("VarToNodeID", objs.getVarToNodeID());
        myht.put("VarToNodeName", objs.getVarToNodeName());
        }
        return BP.Tools.Json.ToJson(myht);
	}

	/**
	 * 淇濆瓨鍙傛暟
	 * @param workid 宸ヤ綔ID
	 * @param paras 鐢ㄤ簬鎺у埗娴佺▼杩愯浆鐨勫弬鏁帮紝姣斿鏂瑰悜鏉′欢. 鏍煎紡涓�:@JinE=1000@QingJaiTianShu=100
	 * @throws Exception 
	 */
	@Override
	public void SaveParas(long workid, String paras, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		BP.WF.Dev2Interface.Flow_SaveParas(workid, paras);
		
	}

	/**
	 * 鑾峰緱涓嬩竴涓妭鐐逛俊鎭�
	 * @param flowNo 娴佺▼缂栧彿
	 * @param workid 娴佺▼瀹炰緥
	 * @param paras 鏂瑰悜鏉′欢鎵�闇�瑕佺殑鍙傛暟锛屽彲浠ヤ负绌恒��
	 * @return 涓嬩竴涓妭鐐圭殑JSON.
	 * @throws Exception 
	 */
	@Override
	public String GenerNextStepNode(String flowNo, long workid, String paras, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		if (paras != null)
            BP.WF.Dev2Interface.Flow_SaveParas(workid, paras);

        int nodeID = BP.WF.Dev2Interface.Node_GetNextStepNode(flowNo, workid);
        BP.WF.Node nd = new BP.WF.Node(nodeID);

        //濡傛灉瀛楁 DeliveryWay = 4 灏辫〃绀哄埌杈剧殑鎺ョ偣鏄敱褰撳墠鑺傜偣鍙戦�佷汉閫夋嫨鎺ユ敹浜�.
        //鑷畾涔夊弬鏁扮殑瀛楁鏄� SelfParas, DeliveryWay 
        // CondModel = 鏂瑰悜鏉′欢璁＄畻瑙勫垯.
        return nd.ToJson();
	}

	/**
	 * 鑾峰緱涓嬩竴姝ヨ妭鐐圭殑鎺ユ敹浜�
	 * @param flowNo 娴佺▼ID
	 * @param toNodeID 鑺傜偣ID
	 * @param workid 宸ヤ綔浜嬩緥ID
	 * @return 杩斿洖涓や釜缁撴灉闆嗕竴涓槸鍒嗙粍鐨凞epts(No,Name)锛屽彟澶栦竴涓槸浜哄憳鐨凟mps(No, Name, FK_Dept),鎺ュ彈鍚庯紝鐢ㄤ簬鏋勯�犱汉鍛橀�夋嫨鍣�.
	 * @throws Exception 
	 */
	@Override
	public String GenerNextStepNodeEmps(String flowNo, int toNodeID, int workid, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		Selector select = new Selector(toNodeID);
        Node nd = new Node(toNodeID);

        GERpt rpt = new GERpt("ND" + Integer.parseInt(flowNo) + "Rpt", workid);
        DataSet ds = select.GenerDataSet(toNodeID, rpt);
        return BP.Tools.Json.ToJson(ds);
	}
	
	/**
	 * 灏嗚杈惧埌鐨勮妭鐐�
	 * @param workID 褰撳墠鑺傜偣ID
	 * @return 杩斿洖鑺傜偣闆嗗悎鐨刯son.
	 * @throws Exception 
	 */
	@Override
	public String WillReturnToNodes(int workID, String userNo) throws Exception {
		
		try
		{
			
		BP.WF.Dev2Interface.Port_Login(userNo);
		
		GenerWorkFlow gwf=new GenerWorkFlow(workID);
		
		DataTable dt=BP.WF.Dev2Interface.DB_GenerWillReturnNodes(gwf.getFK_Node(), workID, gwf.getFID()); 
        return BP.Tools.Json.ToJson(dt);
		}catch(Exception ex)
		{
		  return "err@"+ex.getMessage();
		}
	}

	/**
	 * 灏嗚杈惧埌鐨勮妭鐐�
	 * @param currNodeID 褰撳墠鑺傜偣ID
	 * @return 杩斿洖鑺傜偣闆嗗悎鐨刯son.
	 * @throws Exception 
	 */
	@Override
	public String WillToNodes(int currNodeID, String userNo) throws Exception {
		
		try
		{
		BP.WF.Dev2Interface.Port_Login(userNo);
		Node nd = new Node(currNodeID);
		
      //  if (nd.getCondModel() != CondModel.SendButtonSileSelect)
         //   return "err@褰撳墠鑺傜偣涓嶆槸鐢变笂涓�姝ユ搷浣滃憳閫夋嫨鐨�";

        Directions dirs = new Directions();
        Nodes nds = dirs.GetHisToNodes(currNodeID, false);
        return nds.ToJson();
		}catch(Exception ex)
		{
		  return "err@"+ex.getMessage();
		}
	}

	/**
	 * 鑾峰緱褰撳墠鑺傜偣淇℃伅.
	 * @param currNodeID  褰撳墠鑺傜偣ID
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String CurrNodeInfo(int currNodeID, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		Node nd = new Node(currNodeID);
         return nd.ToJson();
	}

	/**
	 * 鑾峰緱褰撳墠娴佺▼淇℃伅.
	 * @param flowNo 娴佺▼ID
	 * @return 褰撳墠鑺傜偣淇℃伅
	 * @throws Exception 
	 */
	@Override
	public String CurrFlowInfo(String flowNo, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		Flow fl = new Flow(flowNo);
          return fl.ToJson();
	}

	/**
	 * 鑾峰緱褰撳墠娴佺▼淇℃伅.
	 * @param workID 娴佺▼ID
	 * @return 褰撳墠鑺傜偣淇℃伅
	 * @throws Exception 
	 */
	@Override
	public String CurrGenerWorkFlowInfo(long workID, String userNo) throws Exception {
		BP.WF.Dev2Interface.Port_Login(userNo);
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
         return gwf.ToJson();
	}

	
	/**
	 * 閫�鍥�.
	 * @param workID 娴佺▼ID
	 * @param retunrnToNodeID 娴佺▼閫�鍥炵殑鑺傜偣ID
	 * @param returnMsg 閫�鍥炲師鍥�
	 * @return 閫�鍥炵粨鏋滀俊鎭�
	 * @throws Exception 
	 */
  @Override
   public String Node_ReturnWork(long workID, int returnToNodeID, String returnMsg, String userNo) throws Exception {
	  BP.WF.Dev2Interface.Port_Login(userNo);
	  GenerWorkFlow gwf=new GenerWorkFlow(workID);
      return BP.WF.Dev2Interface.Node_ReturnWork(gwf.getFK_Flow(), workID, gwf.getFID(), gwf.getFK_Node(), returnToNodeID,null, returnMsg,false);
	  
	
   }
  
  /**
	 * 鎵ц娴佺▼缁撴潫 璇存槑:寮哄埗娴佺▼缁撴潫.
	 * 
	 * @param flowNo
	 *            娴佺▼缂栧彿
	 * @param workID
	 *            宸ヤ綔ID
	 * @param msg
	 *            娴佺▼缁撴潫鍘熷洜
	 * @return 杩斿洖鎴愬姛鎵ц淇℃伅
	 * @throws Exception
	 */
  @Override
	public  String Flow_DoFlowOverQiangZhi(String flowNo, long workID, String msg, String userNo) throws Exception {
	  BP.WF.Dev2Interface.Port_Login(userNo);
		return BP.WF.Dev2Interface.Flow_DoFlowOver(flowNo, workID, msg);
		
  }

	@Override
	public void Port_Login(String userNo) throws Exception{
		
		BP.WF.Dev2Interface.Port_Login(userNo);
	}
	
	/**
	 * 鎵ц鎾ら攢鎿嶄綔
	 * @return
	 * @throws Exception 
	 */
	public String Runing_UnSend(String userNo,String flowNo, long workID, int unSendToNode,long fid) throws Exception{
		//闇�瑕佸厛鐧婚檰
		BP.WF.Dev2Interface.Port_Login(userNo);
		
		return BP.WF.Dev2Interface.Flow_DoUnSend(flowNo, workID,unSendToNode,fid);
	}
	
	/**
	 * 娴佺▼缁撴潫鍚庡洖婊�
	 * @param flowNo 娴佺▼缂栫爜
	 * @param workId 宸ヤ綔ID
	 * @param backToNodeID 鍥炴粴鍒扮殑鑺傜偣ID
	 * @param backMsg 鍥炴粴鍘熷洜
	 * @return 鍥炴粴淇℃伅
	 * @throws Exception 
	 */
	public String DoRebackFlowData(String flowNo,long workId,int backToNodeID,String backMsg, String userNo) throws Exception{
		BP.WF.Dev2Interface.Port_Login(userNo);
		FlowExt flow = new FlowExt(flowNo);
		return flow.DoRebackFlowData(workId, backToNodeID, backMsg);
	}
	/** 
	 获得当前流程信息.
	 
	 @param flowNo 流程ID.
	 @return 当前节点信息
	*/
    public String CurrFlowInfo(String flowNo) throws Exception
    {
        Flow fl = new Flow(flowNo);
        return fl.ToJson();
    }
    /** 
	 获得当前流程信息.
	 
	 @param flowNo 流程ID.
	 @return 当前节点信息
	*/
    public String CurrGenerWorkFlowInfo(long workID) throws Exception
    {
        GenerWorkFlow gwf = new GenerWorkFlow(workID);
        return gwf.ToJson();
    }
    
	/** 
	 获得工作进度-用于展示流程的进度图
	 
	 @param workID workID
	 @param userNo 用户编号
	 @return 返回待办
	*/
    public String WorkProgressBar(long  workID, String userNo) throws Exception
    {
    	String sql = "";
		DataSet ds = new DataSet();

		//流程控制主表, 可以得到流程状态，停留节点，当前的执行人.
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		
		DataTable dt1 = gwf.ToDataTableField("WF_GenerWorkFlow");
		dt1.TableName = "WF_GenerWorkFlow";
		ds.Tables.add(dt1);

		//节点信息.
		Nodes nds = new Nodes(gwf.getFK_Flow());
		DataTable dt2 = nds.ToDataTableField("WF_Node");
		ds.Tables.add(dt2);

		//方向。
		Directions dirs = new Directions(gwf.getFK_Flow());
		ds.Tables.add(dirs.ToDataTableField("WF_Direction"));


		DataTable dtHistory = new DataTable();
		dtHistory.TableName = "Track";
		dtHistory.Columns.Add("FK_Node");
		dtHistory.Columns.Add("NodeName");
		dtHistory.Columns.Add("EmpNo");
		dtHistory.Columns.Add("EmpName");
		dtHistory.Columns.Add("RDT"); //记录日期.
		dtHistory.Columns.Add("SDT"); //应完成日期.

		//执行人.
		if (gwf.getWFState() == WFState.Complete)
		{
			//历史执行人. 
			sql = "SELECT * FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track WHERE WorkID=" + workID + " AND (ActionType=1 OR ActionType=0)  ORDER BY RDT DESC";
			DataTable dtTrack = BP.DA.DBAccess.RunSQLReturnTable(sql);

			for (DataRow drTrack : dtTrack.Rows)
			{
				DataRow dr = dtHistory.NewRow();
				dr.setValue("FK_Node", drTrack.getValue("NDFrom"));
			   // dr["ActionType"] = drTrack["NDFrom"];
				dr.setValue("NodeName", drTrack.getValue("NDFromT"));
				dr.setValue("EmpNo", drTrack.getValue("EmpFrom"));
				dr.setValue("EmpName", drTrack.getValue("EmpFromT"));
				dr.setValue("RDT", drTrack.getValue("RDT"));
				dr.setValue("SDT", drTrack.getValue(""));
				dtHistory.Rows.add(dr);
			}
		}
		else
		{
			GenerWorkerLists gwls = new GenerWorkerLists(workID);
			for (GenerWorkerList gwl : gwls.ToJavaList())
			{
				DataRow dr = dtHistory.NewRow();
				    dr.setValue("FK_Node", gwl.getFK_Node());
					dr.setValue("NodeName",gwl.getFK_NodeText());
					dr.setValue("EmpNo",gwl.getFK_Emp());
					dr.setValue("EmpName",gwl.getFK_EmpText());
					dr.setValue("RDT",gwl.getRDT());
					dr.setValue("SDT",gwl.getSDT());
				
			}
		}

		ds.Tables.add(dtHistory);

		return BP.Tools.Json.ToJson(ds);

    }

	
}
