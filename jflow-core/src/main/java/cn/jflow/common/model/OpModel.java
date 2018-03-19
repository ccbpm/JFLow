package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.En.QueryObject;
import BP.WF.FlowOpList;
import BP.WF.WorkFlow;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerListAttr;
import BP.WF.Entity.GenerWorkerLists;
import BP.WF.Entity.GetTask;
import BP.WF.WFState;
import BP.Web.WebUser;

public class OpModel extends BaseModel{
	
	public StringBuffer Pub2=null;
	private String basePath;
	private String DoType;
	private String FK_Node;
	private String FK_Flow;
	private int WorkID;
	private int FID;
	
	public OpModel(HttpServletRequest request, HttpServletResponse response,String basePath,String DoType,String FK_Node,String FK_Flow,int WorkID,int FID) {
		super(request, response);
		Pub2=new StringBuffer();
		this.basePath=basePath;
		this.DoType=DoType;
		this.FK_Node=FK_Node;
		this.FK_Flow=FK_Flow;
		this.WorkID=WorkID;
		this.FID=FID;
	}

	public void init(){
		try{
			if (this.getDoType().equals("Del")) {
				WorkFlow wf = new WorkFlow(getFK_Flow(), getWorkID());
				wf.DoDeleteWorkFlowByReal(true);
				this.winCloseWithMsg("流程已经被删除.");
			}else if (this.getDoType().equals("HungUp")) {
				WorkFlow wf1 = new WorkFlow(getFK_Flow(), getWorkID());
				this.winCloseWithMsg("流程已经被挂起.");
			}else if (this.getDoType().equals("UnHungUp")) {
				WorkFlow wf2 = new WorkFlow(getFK_Flow(), getWorkID());
				//  wf2.DoUnHungUp();
				this.winCloseWithMsg("流程已经被解除挂起.");
			}else if (this.getDoType().equals("ComeBack")) {
				WorkFlow wf3 = new WorkFlow(getFK_Flow(), getWorkID());
				wf3.DoComeBackWorkFlow("无");
				this.winCloseWithMsg("流程已经被回复启用.");
			}else if (this.getDoType().equals("Takeback")) { //取回审批.
	
			}
			else if (this.getDoType().equals("UnSend")) {
				// 转化成编号.
				String message = BP.WF.Dev2Interface.Flow_DoUnSend(getFK_Flow(), getWorkID());
				Pub2.append(this.AddEasyUiPanelInfo("提示", message));
			}
		}catch (RuntimeException ex) {
			Pub2.append(this.AddEasyUiPanelInfo("提示", "执行功能:" + getDoType() + ",出现错误:" + ex.getMessage()));
		}
		 int wfState = BP.DA.DBAccess.RunSQLReturnValInt("SELECT WFState FROM WF_GenerWorkFlow WHERE WorkID=" + WorkID, 1);
         WFState wfstateEnum = WFState.forValue(wfState);
         //this.Pub2.AddH3("您可执行的操作<hr>");
         if(wfstateEnum==WFState.Runing){
        	 this.FlowOverByCoercion(); /*删除流程.*/
             this.TackBackCheck(); /*取回审批*/
             this.Hurry(); /*催办*/
             this.UnSend(); /*撤销发送*/
         }
         else if(wfstateEnum==WFState.Complete){
        	 
         }
         else if(wfstateEnum==WFState.Delete){
        	 this.RollBack();
         }
         else if(wfstateEnum==WFState.HungUp){
        	 this.AddUnHungUp();
         }
         else{
        	 
         }

	}
	
			
    /// 取回审批
    public void TackBackCheck()
    {
        GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
        /* 判断是否有取回审批的权限。*/
        Pub2.append(this.AddEasyUiPanelInfoBegin("取回审批","icon-tip", 30));//.AddEasyUiPanelInfoBegin("取回审批");
        //Pub2.append(this.AddEasyUiPanelInfoBegin("取回审批", "collapse-panel-1"));
        String sql = "SELECT NodeID FROM WF_Node WHERE CheckNodes LIKE '%" + gwf.getFK_Node() + "%'";
        int myNode = DBAccess.RunSQLReturnValInt(sql, 0);
        if (myNode != 0)
        {
            GetTask gt = new GetTask(myNode);
            if (gt.Can_I_Do_It() == true)
            {
            	
            	Pub2.append("功能执行:<a href=\"javascript:Takeback('" + WorkID + "','" + FK_Flow + "','" + gwf.getFK_Node() + "','" + myNode + "')\" >点击执行取回审批流程</a>。");
                Pub2.append(this.AddBR("说明：如果被成功取回，ccflow就会把停留在别人工作节点上的工作发送到您的待办列表里。"));//.AddBR("说明：如果被成功取回，ccflow就会把停留在别人工作节点上的工作发送到您的待办列表里。");
            }
        }
        else
        {
        	Pub2.append("您没有此权限");
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    
    // 强制删除流程
    public void FlowOverByCoercion()
    {
        try {

            GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
            Pub2.append(this.AddEasyUiPanelInfoBegin("删除流程", "icon-tip", 30));//.AddEasyUiPanelInfoBegin("删除流程");
            //Pub2.append(this.AddEasyUiPanelInfoBegin("删除流程", "collapse-panel-2"));
			if (BP.WF.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.FK_Flow,this.WorkID, WebUser.getNo()) ==true){
				Pub2.append("功能执行:<a href=\"javascript:DeleteFlowInstance('" + FK_Flow + "','" + WorkID + "')\" >点击执行删除流程</a>。");
			    Pub2.append(this.AddBR("说明：如果执行流程将会被彻底的删除。"));//.AddBR();
			}
			else
			{
				Pub2.append("对不起，您没有删除该流程的权限.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    
    // 催办
    public void Hurry()
    {
        /*催办*/
        Pub2.append(this.AddEasyUiPanelInfoBegin("工作催办","icon-tip", 30));
        //Pub2.append(this.AddEasyUiPanelInfoBegin("工作催办", "collapse-panel-3"));
        Pub2.append("您没有此权限.");
        //this.Pub2.Add("您没有此权限.");

        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    
    
    
    // 撤销发送
    public void UnSend()
    {
        /*撤销发送*/
        Pub2.append(this.AddEasyUiPanelInfoBegin("撤销发送", "icon-tip",30));//.AddEasyUiPanelInfoBegin("撤销发送");
       // Pub2.append(this.AddEasyUiPanelInfoBegin("撤销发送", "collapse-panel-4"));

        //查询是否有权限撤销发送
        GenerWorkerLists workerlists = new GenerWorkerLists();

        QueryObject info = new QueryObject(workerlists);
       
        info.AddWhere(GenerWorkerListAttr.FK_Emp, WebUser.getNo());
        info.addAnd();
        info.AddWhere(GenerWorkerListAttr.IsPass, "1");
        info.addAnd();
        info.AddWhere(GenerWorkerListAttr.IsEnable, "1");
        info.addAnd();
        info.AddWhere(GenerWorkerListAttr.WorkID, this.WorkID);
        int count = info.DoQuery();
        if (count > 0)
        {
        	Pub2.append("<a href =\"javascript:UnSend('" + this.FK_Flow + "','" + this.WorkID + "','" + FID + "')\" >撤销发送</a>");
        }
        else
        {
        	Pub2.append("您没有此权限.");
        }

        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    
    
    
    
    // 恢复启用流程数据到结束节点
    public void RollBack()
    {
    	Pub2.append(this.AddEasyUiPanelInfoBegin("恢复启用流程数据到结束节点","icon-tip",30));
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("恢复启用流程数据到结束节点", "collapse-panel-5"));
        if ("admin".equals(WebUser.getNo()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('ComeBack','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行恢复流程</a>。");
            Pub2.append(this.AddBR("说明：如果被成功恢复，ccflow就会把待办工作发送给最后一个结束流程的工作人员。"));
        }
        else
        {
        	Pub2.append("您没有权限.");
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }

    // 取消挂起
    public void AddUnHungUp()
    {
    	Pub2.append(this.AddEasyUiPanelInfoBegin("取消挂起","icon-tip", 30));
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("取消挂起", "collapse-panel-6"));
        if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(FK_Flow, Integer.parseInt(FK_Node), WorkID, WebUser.getNo()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('UnHungUp','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行取消挂起流程</a>。");
            Pub2.append(this.AddBR("说明：解除流程挂起的状态。"));
        }
        else
        {
        	Pub2.append(this.AddBR("您没有此权限，或者当前不是挂起的状态。"));
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    /// 挂起
    public void AddHungUp()
    {
    	Pub2.append(this.AddEasyUiPanelInfoBegin("挂起", "icon-tip", 30));
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("挂起", "collapse-panel-7"));
        if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(FK_Flow, Integer.parseInt(FK_Node), WorkID, WebUser.getNo()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('" + FlowOpList.HungUp + "','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "','')\" >点击执行挂起流程</a>。");
            Pub2.append(this.AddBR("说明：对该流程执行挂起，挂起后可以解除挂起，挂起的时间不计算考核。"));
        }
        else
        {
        	Pub2.append("您没有此权限.");
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    /// 移交
    public void AddShift()
    {
    	Pub2.append(this.AddEasyUiPanelInfoBegin("移交","icon-tip", 30));
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("移交", "collapse-panel-8"));
        if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(FK_Flow, Integer.parseInt(FK_Node), WorkID, WebUser.getNo()));
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('" + FlowOpList.UnHungUp + "','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行取消挂起流程</a>。");
        	Pub2.append(this.AddBR("说明：解除流程挂起的状态。"));
        }
        if(!BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(FK_Flow, Integer.parseInt(FK_Node), WorkID, WebUser.getNo()))//else
        {
            Pub2.append(this.AddBR("您没有此权限，或者当前不是挂起的状态。"));//.AddBR("您没有此权限，或者当前不是挂起的状态。");
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }

    public void AddShiftByCoercion()
    {
    	Pub2.append(this.AddEasyUiPanelInfoBegin("强制移交", "icon-tip", 30));
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("强制移交", "collapse-panel-9"));
        if ("admin".equals(WebUser.getNo()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('" + FlowOpList.ShiftByCoercion + "','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行取消挂起流程</a>。");
           Pub2.append(this.AddBR("说明：解除流程挂起的状态。"));
        }
        else
        {
        	Pub2.append(this.AddBR("您没有此权限。"));
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
}
