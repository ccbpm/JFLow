package BP.WF;

import BP.En.Entity;
import BP.Sys.EventListOfNode;

public class OverrideClass {
	
	 
    //重写该方法实现，自己的业务逻辑.
	public static void FEE(String doType, Node currNode, Entity en, String atPara, SendReturnObjs objs, Node jumpToNode,
			String jumpToEmps) throws Exception {
		
		if (1==1)
			return;
		
		 //事件标志
		String sendWhen= EventListOfNode.SendWhen; //发送 前.
		if (doType.equals( EventListOfNode.SendWhen)==true )
		{
			  String flowNo= currNode.getFK_Flow();     
			  
			  if (flowNo.equals("001"))
			  {
			     /*输入自己的业务代码.*/
			  }
			   
		}
		 
          //获得workid.
          int workid= Integer.parseInt(en.getPKVal().toString());
        
		
	      //获得当前节点的相关参数.		
          String flowNo= currNode.getFK_Flow(); //当前流程的编号 比如: "001"
          String flowName=currNode.getFlowName(); //当前节点的名称.
          int nodeID= currNode.getNodeID(); // 当前节点的ID.
          String nodeName= currNode.getName(); // 当前节点的名称.
          
          //当前登录的相关信息
          String no=BP.Web.WebUser.getNo();  //当前操作员的编号
          String name=BP.Web.WebUser.getName(); //当前操作员的名称.
          String deptNo=BP.Web.WebUser.getFK_Dept(); //当前操作员的部门编号
          String deptName=BP.Web.WebUser.getFK_DeptName(); //当前操作员的部门名称
           
		
		// TODO Auto-generated method stub
		
	}
	

}
