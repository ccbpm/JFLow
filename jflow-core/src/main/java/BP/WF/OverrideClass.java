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
		
		
		//获得参数.
		
          String flowNo= currNode.getFK_Flow();          
          int nodeID= currNode.getNodeID();
          String no=BP.Web.WebUser.getNo();
          String name=BP.Web.WebUser.getName();
          
          //获得workid.
          int workid= Integer.parseInt(en.getPKVal().toString());
          
          
          
		
		// TODO Auto-generated method stub
		
	}
	

}
