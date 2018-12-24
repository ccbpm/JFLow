package BP.FlowEvent;

// 
import java.math.BigDecimal;

import BP.WF.FlowEventBase;

/** 
省汇总流程001
 
*/
public class F001 extends FlowEventBase
{
	
private String getOID;


	/** 
	 重写流程标记
	 
	*/
	@Override
	public String getFlowMark()
	{
		return "001";
	}

	/** 
	 报销流程事件
	 
	*/
	public F001()
	{
	}


	/** 
	 重写发送前事件
	 
	 @return 
	 * @throws com.sun.star.uno.Exception 
	*/
	@Override
	public String SendWhen() throws Exception 
	{
		
		//if (1==1)
		//return "sssssssssssssss";
		
		 

		// 当前的节点, 其他的变量请从 this.HisNode .
//		int nodeID = this.HisNode.NodeID; 
		int nodeID = this.HisNode.getNodeID(); // int类型的ID.
		String nodeName = this.HisNode.getName(); // 当前节点名称.
		 
		

		switch (nodeID)
		{
			case 101: //当是第1个节点的时候.
				break;
			default:
				break;
		}
		return super.SendWhen();
	}

	/** 
	 保存后执行的事件
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public String SaveAfter() throws Exception 
	{
		switch (this.HisNode.getNodeID())
		{
			case 101:
			try {
				this.ND101_SaveAfter();
			} catch (Exception e) {
				e.printStackTrace();
			}
				break;
			default:
				break;
		}
		return super.SaveAfter();
	}
	/** 
	 节点保存后事件
	 方法命名规则为:ND+节点名_事件名.
	 * @throws Exception 
	 
	*/
	public final void ND101_SaveAfter() throws Exception
	{

		//求出明细表的合计.
		float hj = BP.DA.DBAccess.RunSQLReturnValFloat("SELECT SUM(XiaoJi) as Num FROM ND101Dtl1 WHERE RefPK=" + this.getOID, 0);

		//更新合计小写 , 把合计转化成大写.
		String sql = "UPDATE ND101 SET DaXie='" + BP.DA.DataType.ParseFloatToCash(hj) + "',HeJi="+hj+"  WHERE OID=" + this.getOID;
		BP.DA.DBAccess.RunSQL(sql);

		//if (1 == 2)
		//    throw new Exception("@执行错误xxxxxx.");
		//如果你要向用户提示执行成功的信息，就给他赋值，否则就不必赋值。
		//this.SucessInfo = "执行成功提示.";
	}
	/** 
	 发送成功事件，发送成功时，把流程的待办写入其他系统里.
	 
	 @return 返回执行结果，如果返回null就不提示。
	 * @throws Exception 
	*/
	@Override
	public String SendSuccess() throws Exception
	{
		try
		{
			// 组织必要的变量.
			long workid = this.getWorkID(); // 工作id.
			String flowNo = this.HisNode.getFK_Flow(); // 流程编号.
			int currNodeID = this.SendReturnObjs.getVarCurrNodeID(); //当前节点id
			int toNodeID = this.SendReturnObjs.getVarToNodeID(); // 到达节点id.
			String toNodeName = this.SendReturnObjs.getVarToNodeName(); // 到达节点名称。
			String acceptersID = this.SendReturnObjs.getVarAcceptersID(); // 接受人员id, 多个人员会用 逗号分看 ,比如 zhangsan,lisi。
			String acceptersName = this.SendReturnObjs.getVarAcceptersName(); // 接受人员名称，多个人员会用逗号分开比如:张三,李四.
			//   求实际金额.
			BigDecimal sjje = new BigDecimal("0");
			//直接执行
			if(currNodeID==1807){
			   String sql="UPDATE ND18RPT set  ShiJiHuaFeiJinE = (select YuSuanJinE from ND18Rpt where OID= '"+workid+"') where OID= "+workid;
			    BP.DA.DBAccess.RunSQL(sql);
			}
			// 分解执行的情况.
	        if(currNodeID==1808  ){
		    // BP.WF.Entity.GenerWorkFlows ens =new  BP.WF.Entity.GenerWorkFlows();
		    //ens.Retrieve(BP.WF.Entity.GenerWorkFlowAttr.PWorkID, workid );
			    String sql="SELECT SUM(ShiJiHuaFeiChengBen) FROM ND19RPT WHERE PWorkID="+workid;
			    sjje=BP.DA.DBAccess.RunSQLReturnValDecimal(sql, sjje, 2);
			    sql="UPDATE ND18RPT SET ShiJiHuaFeiJinE="+sjje+" where oid="+workid;
			    BP.DA.DBAccess.RunSQL(sql);
			}
			

			//执行向其他系统写入待办.
//                
//                 * 在这里需要编写你的业务逻辑，根据上面组织的变量.
//                 * 
//                 

			//返回.
			return super.SendSuccess();
		}
		catch(RuntimeException ex)
		{
			throw new RuntimeException("向其他系统写入待办失败，详细信息："+ex.getMessage());
		}
	}
}