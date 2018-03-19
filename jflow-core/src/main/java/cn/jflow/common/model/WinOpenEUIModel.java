package cn.jflow.common.model;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.WFState;
import BP.WF.WorkFlow;
import BP.WF.Template.BtnLab;
import BP.WF.Template.WebOfficeWorkModel;

public class WinOpenEUIModel extends BaseModel {

	public WinOpenEUIModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}

	/** 
	 当前的流程编号
	 
	*/
	public final String getFK_Flow()
	{
		String s = get_request().getParameter("FK_Flow");
		if (StringHelper.isNullOrEmpty(s))
		{
			throw new RuntimeException("@流程编号参数错误...");
		}

		return BP.WF.Dev2Interface.TurnFlowMarkToFlowNo(s);
	}
	public final String getFromNode()
	{
		return get_request().getParameter("FromNode");
	}
	public final String getDoFunc()
	{
		return get_request().getParameter("DoFunc");
	}
	public final String getCFlowNo()
	{
		return get_request().getParameter("CFlowNo");
	}
	public final String getWorkIDs()
	{
		return get_request().getParameter("WorkIDs");
	}
	public final String getNos()
	{
		return get_request().getParameter("Nos");
	}



	private long _workid = 0;
	/** 
	 当前的工作ID
	 
	*/
	public final long getWorkID()
	{

		if (get_request().getParameter("WorkID") == null)
		{
			return _workid;
		}
		else
		{
			return Long.parseLong(get_request().getParameter("WorkID"));
		}

	}
	public final void setWorkID(long value)
	{
		_workid = value;
	}
	
	private int _FK_Node = 0;
	/** 
	 当前的 NodeID ,在开始时间,nodeID,是地一个,流程的开始节点ID.
	 
	*/
	public final int getFK_Node()
	{
		String fk_nodeReq = get_request().getParameter("FK_Node");
		if (StringHelper.isNullOrEmpty(fk_nodeReq))
		{
			fk_nodeReq = get_request().getParameter("NodeID");
		}

		if (StringHelper.isNullOrEmpty(fk_nodeReq) == false)
		{
			return Integer.parseInt(fk_nodeReq);
		}

		if (_FK_Node == 0)
		{
			if (get_request().getParameter("WorkID") != null)
			{
				String sql = "SELECT FK_Node from  WF_GenerWorkFlow where WorkID=" + this.getWorkID();
				_FK_Node = DBAccess.RunSQLReturnValInt(sql);
			}
			else
			{
				_FK_Node = Integer.parseInt(this.getFK_Flow() + "01");
			}
		}
		return _FK_Node;
	}
	
	public final int getPWorkID()
	{
		try
		{
			String s = get_request().getParameter("PWorkID");
			if (StringHelper.isNullOrEmpty(s) == true)
			{
				s = get_request().getParameter("PWorkID");
			}
			if (StringHelper.isNullOrEmpty(s) == true)
			{
				s = "0";
			}
			return Integer.parseInt(s);
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}

	private boolean isTab = false;

	public final boolean getIsTab()
	{
		return isTab;
	}
	public final void setIsTab(boolean value)
	{
		isTab = value;
	}

	private String officeTabName = "正文";

	public final String getOfficeTabName()
	{
		return officeTabName;
	}
	public final void setOfficeTabName(String value)
	{
		officeTabName = value;
	}

	private boolean _IsWorkdTab = false;
	public final void setIsWordTab(boolean value)
	{
		_IsWorkdTab = value;
	}
	public final boolean getIsWordTab()
	{
		return _IsWorkdTab;
	}

	private boolean _isReWord = false;
	//是否已完成
	public final boolean getIsReWord()
	{
		return _isReWord;
	}
	public final void setIsReWord(boolean value)
	{
		_isReWord = value;
	}

	/** 
	 是否公文标签置前模式
	 
	*/
	private boolean privateIsOfficeTabFront;
	public final boolean getIsOfficeTabFront()
	{
		return privateIsOfficeTabFront;
	}
	public final void setIsOfficeTabFront(boolean value)
	{
		privateIsOfficeTabFront = value;
	}

	public final String getDoType()
	{
		return get_request().getParameter("DoType");
	}

	public void Page_Load() {
		try {

			BtnLab btnLab = new BtnLab(getFK_Node());

			if (btnLab.getWebOfficeWorkModel() == WebOfficeWorkModel.FrmFirst
					|| btnLab.getWebOfficeWorkModel() == WebOfficeWorkModel.WordFirst) {
				setIsTab(true);
				setOfficeTabName(btnLab.getWebOfficeLab());
				if (getWorkID() == 0) {
					Flow currFlow = new Flow(this.getFK_Flow());
					setWorkID(currFlow.NewWork().getOID());
				}
				Node node = new Node(this.getFK_Node());
				if (!node.getIsStartNode()) {
					String path =get_request().getSession().getServletContext().getRealPath("/DataUser/OfficeFile/"+ this.getFK_Flow() + "/");
					File f =new File(path);  
					File[] files =f.listFiles();  
				    if(files.length>0){  
						for(File file:files){  
				    	  if(file.getName().startsWith((new Long(this.getWorkID())).toString())){
				    		  setIsWordTab(true);
								break;
				    	  }
				       }
				     }
					/*for (String file : System.IO.Directory.GetFiles(path)) {
						System.IO.FileInfo info = new System.IO.FileInfo(file);
						if (info.getName().startsWith(
								(new Long(this.getWorkID())).toString())) {
							setIsWordTab(true);
							break;
						}
					}*/
				} else {
					setIsWordTab(true);
				}
			}
			try {
				WorkFlow workflow = new WorkFlow(this.getFK_Flow(),this.getWorkID());
				boolean IsComplate = workflow.getHisGenerWorkFlow().getWFState() == WFState.Complete ? true: false;
				if (IsComplate) {
					if ("公文".equals(getOfficeTabName())||"XCBANK".equals(SystemConfig.getCustomerNo())) {
						setIsReWord(true);
					}
				}
			} catch (java.lang.Exception e) {
				setIsReWord(false);
			}
		} catch (RuntimeException ex) {
			BP.DA.Log.DefaultLogWriteLineError("错误",ex);
		}
	}
}
