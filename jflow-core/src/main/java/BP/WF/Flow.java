package BP.WF;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Depositary;
import BP.DA.Log;
import BP.DA.Paras;
import BP.DA.XmlWriteMode;
import BP.En.Attr;
import BP.En.EditType;
import BP.En.Entity;
import BP.En.FieldTypeS;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.UAC;
import BP.En.UIContralType;
import BP.Port.Emp;
import BP.Sys.AttachmentUploadType;
import BP.Sys.EventListOfNode;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBs;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmEle;
import BP.Sys.FrmEles;
import BP.Sys.FrmEvent;
import BP.Sys.FrmEvents;
import BP.Sys.FrmImg;
import BP.Sys.FrmImgAths;
import BP.Sys.FrmImgs;
import BP.Sys.FrmLab;
import BP.Sys.FrmLabs;
import BP.Sys.FrmLine;
import BP.Sys.FrmLines;
import BP.Sys.FrmLink;
import BP.Sys.FrmLinks;
import BP.Sys.FrmRB;
import BP.Sys.FrmRBs;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.GEEntity;
import BP.Sys.GroupField;
import BP.Sys.GroupFieldAttr;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Sys.MapDtl;
import BP.Sys.MapDtlAttr;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.MapFrames;
import BP.Sys.OSModel;
import BP.Sys.PubClass;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnumMainAttr;
import BP.Sys.SysEnumMains;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.DateUtils;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.WF.Data.GERpt;
import BP.WF.Data.GERptAttr;
import BP.WF.Data.NDXRptBaseAttr;
import BP.WF.Template.BillTemplate;
import BP.WF.Template.BillTemplates;
import BP.WF.Template.CC;
import BP.WF.Template.CCDept;
import BP.WF.Template.CCDeptAttr;
import BP.WF.Template.CCDepts;
import BP.WF.Template.CCEmp;
import BP.WF.Template.CCEmpAttr;
import BP.WF.Template.CCEmps;
import BP.WF.Template.CCStation;
import BP.WF.Template.Cond;
import BP.WF.Template.CondModel;
import BP.WF.Template.CondType;
import BP.WF.Template.Conds;
import BP.WF.Template.ConnDataFrom;
import BP.WF.Template.DTSField;
import BP.WF.Template.DataStoreModel;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.Directions;
import BP.WF.Template.DraftRole;
import BP.WF.Template.FindWorkerRole;
import BP.WF.Template.FlowAttr;
import BP.WF.Template.FlowDTSTime;
import BP.WF.Template.FlowDTSWay;
import BP.WF.Template.FlowDeptDataRightCtrlType;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSorts;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.FrmWorkCheck;
import BP.WF.Template.LabNote;
import BP.WF.Template.LabNotes;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeDept;
import BP.WF.Template.NodeDeptAttr;
import BP.WF.Template.NodeDepts;
import BP.WF.Template.NodeEmp;
import BP.WF.Template.NodeEmpAttr;
import BP.WF.Template.NodeEmps;
import BP.WF.Template.NodeExt;
import BP.WF.Template.NodeExts;
import BP.WF.Template.NodeReturn;
import BP.WF.Template.NodeReturnAttr;
import BP.WF.Template.NodeReturns;
import BP.WF.Template.NodeStation;
import BP.WF.Template.NodeStationAttr;
import BP.WF.Template.NodeStations;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;
import BP.WF.Template.NodeYGFlow;
import BP.WF.Template.NodeYGFlows;
import BP.WF.Template.Selector;
import BP.WF.Template.SelectorModel;
import BP.WF.Template.StartGuideWay;
import BP.WF.Template.TimelineRole;
import BP.WF.Template.TurnTo;
import BP.Web.GuestUser;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;

/** 
 流程
 记录了流程的信息．
 流程的编号，名称，建立时间．
*/
public class Flow extends BP.En.EntityNoName
{

	   /// <summary>
    /// (当前节点为子流程时)是否检查所有子流程完成后父流程自动发送
    /// </summary>
    public SubFlowOver SubFlowOver()
    {
           return SubFlowOver.forValue( this.GetValIntByKey(FlowAttr.IsAutoSendSubFlowOver));
    }
		
	/** 
	 最大值x
	*/
	public final int getMaxX()
	{
		int i = this.GetParaInt("MaxX");
		if (i == 0)
		{
			this.GenerMaxXY();
		}
		else
		{
			return i;
		}
		return this.GetParaInt("MaxX");
	}
	public final void setMaxX(int value)
	{
		this.SetPara("MaxX", value);
	}
	/** 
	 最大值Y
	*/
	public final int getMaxY()
	{
		int i = this.GetParaInt("MaxY");
		if (i == 0)
		{
			this.GenerMaxXY();
		}
		else
		{
			return i;
		}

		return this.GetParaInt("MaxY");
	}
	public final void setMaxY(int value)
	{
		this.SetPara("MaxY", value);
	}
	private void GenerMaxXY()
	{
		//int x1 = DBAccess.RunSQLReturnValInt("SELECT MAX(X) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "'", 0);
		//int x2 = DBAccess.RunSQLReturnValInt("SELECT MAX(X) FROM WF_NodeLabelNode WHERE FK_Flow='" + this.getNo() + "'", 0);
		//this.MaxY = DBAccess.RunSQLReturnValInt("SELECT MAX(Y) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "'", 0);
	}

    ///#endregion 参数属性.
	///#region 业务数据表同步属性.
	/** 
	 同步方式
	*/
	public final FlowDTSWay getDTSWay()
	{
		return FlowDTSWay.forValue(this.GetValIntByKey(FlowAttr.DTSWay));
	}
	public final void setDTSWay(FlowDTSWay value)
	{
		this.SetValByKey(FlowAttr.DTSWay, value.getValue());
	}
	public final FlowDTSTime getDTSTime()
	{
		return FlowDTSTime.forValue(this.GetValIntByKey(FlowAttr.DTSTime));
	}
	public final void setDTSTime(FlowDTSTime value)
	{
		this.SetValByKey(FlowAttr.DTSTime, value.getValue());
	}
	public final DTSField getDTSField()
	{
		return DTSField.forValue(this.GetValIntByKey(FlowAttr.DTSField));
	}
	public final void setDTSField(DTSField value)
	{
		this.SetValByKey(FlowAttr.DTSField, value.getValue());
	}
	/** 
	 数据源
	 
	*/
	public final String getDTSDBSrc()
	{
		String str = this.GetValStringByKey(FlowAttr.DTSDBSrc);
		if (StringHelper.isNullOrEmpty(str))
		{
			return "local";
		}
		return str;
	}
	public final void setDTSDBSrc(String value)
	{
		this.SetValByKey(FlowAttr.DTSDBSrc, value);
	}
	/** 
	 业务表
	 
	*/
	public final String getDTSBTable()
	{
		return this.GetValStringByKey(FlowAttr.DTSBTable);
	}
	public final void setDTSBTable(String value)
	{
		this.SetValByKey(FlowAttr.DTSBTable, value);
	}
	public final String getDTSBTablePK()
	{
		return this.GetValStringByKey(FlowAttr.DTSBTablePK);
	}
	public final void setDTSBTablePK(String value)
	{
		this.SetValByKey(FlowAttr.DTSBTablePK, value);
	}
	/** 
	 要同步的节点s
	 
	*/
	public final String getDTSSpecNodes()
	{
		return this.GetValStringByKey(FlowAttr.DTSSpecNodes);
	}
	public final void setDTSSpecNodes(String value)
	{
		this.SetValByKey(FlowAttr.DTSSpecNodes, value);
	}
	/** 
	 同步的字段对应关系.
	 
	*/
	public final String getDTSFields()
	{
		return this.GetValStringByKey(FlowAttr.DTSFields);
	}
	public final void setDTSFields(String value)
	{
		this.SetValByKey(FlowAttr.DTSFields, value);
	}

	/** 
	 流程图数据
	 
	*/
	public final String getFlowJson()
	{
		try {
			return this.GetBigTextFromDB("FlowJson");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public final void setFlowJson(String value)
	{
		try {
			this.SaveBigTxtToDB("FlowJson", value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*****************************H5流程列表使用，不做其他用处*********************************************/
	public final int getSta0(){
		return this.GetValIntByKey(FlowAttr.Sta0);
	}
	public final void setSta0(String value) {
		this.SetValByKey(FlowAttr.Sta0, value);
	}
	
	public final int getSta1(){
		return this.GetValIntByKey(FlowAttr.Sta1);
	}
	public final void setSta1(String value) {
		this.SetValByKey(FlowAttr.Sta1, value);
	}
	
	public final int getSta2(){
		return this.GetValIntByKey(FlowAttr.Sta2);
	}
	public final void setSta2(String value) {
		this.SetValByKey(FlowAttr.Sta2, value);
	}
/**************************************************************************/
	/** 
	 设计类型
	*/
	public final int getDType()
	{
		return this.GetValIntByKey(FlowAttr.DType);
	}
	public final void setDType(int value)
	{
		this.SetValByKey(FlowAttr.DType, value);
	}
	 
	/** 
	 流程事件实体
	*/
	public final String getFlowEventEntity()
	{
		return this.GetValStringByKey(FlowAttr.FlowEventEntity);
	}
	public final void setFlowEventEntity(String value)
	{
		this.SetValByKey(FlowAttr.FlowEventEntity, value);
	}
	/** 
	 流程标记
	 
	*/
	public final String getFlowMark()
	{
		String str = this.GetValStringByKey(FlowAttr.FlowMark);
		if (str.equals(""))
		{
			return this.getNo();
		}
		return str;
	}
	public final void setFlowMark(String value)
	{
		this.SetValByKey(FlowAttr.FlowMark, value);
	}

	/** 
	 节点图形类型
	 
	*/
	public final int getChartType()
	{
		return this.GetValIntByKey(FlowAttr.ChartType);
	}
	public final void setChartType(int value)
	{
		this.SetValByKey(FlowAttr.ChartType, value);
	}

	/** 
	 发起限制.
	 
	*/
	public final StartLimitRole getStartLimitRole()
	{
		return StartLimitRole.forValue(this.GetValIntByKey(FlowAttr.StartLimitRole));
	}
	public final void setStartLimitRole(StartLimitRole value)
	{
		this.SetValByKey(FlowAttr.StartLimitRole, value.getValue());
	}
	///// <summary>
	///// 发起限制文本
	///// </summary>
	//public string StartLimitRoleText
	//{
	//    get
	//    {
	//        return  this.GetValRefTextByKey(FlowAttr.StartLimitRole);
	//    }
	//}
	/** 
	 发起内容
	*/
	public final String getStartLimitPara()
	{
		return this.GetValStringByKey(FlowAttr.StartLimitPara);
	}
	public final void setStartLimitPara(String value)
	{
		this.SetValByKey(FlowAttr.StartLimitPara, value);
	}
	public final String getStartLimitAlert()
	{
		String s = this.GetValStringByKey(FlowAttr.StartLimitAlert);
		if (s.equals(""))
		{
			return "您已经启动过该流程，不能重复启动。";
		}
		return s;
	}
	public final void setStartLimitAlert(String value)
	{
		this.SetValByKey(FlowAttr.StartLimitAlert, value);
	}
	/** 
	 限制触发时间
	*/
	public final StartLimitWhen getStartLimitWhen()
	{
		return StartLimitWhen.forValue(this.GetValIntByKey(FlowAttr.StartLimitWhen));
	}
	public final void setStartLimitWhen(StartLimitWhen value)
	{
		this.SetValByKey(FlowAttr.StartLimitWhen, value.getValue());
	}

	/** 
	 发起导航方式
	 
	*/
	public final StartGuideWay getStartGuideWay()
	{
		return StartGuideWay.forValue(this.GetValIntByKey(FlowAttr.StartGuideWay));
	}
	public final void setStartGuideWay(StartGuideWay value)
	{
		this.SetValByKey(FlowAttr.StartGuideWay, value.getValue());
	}
	/** 
	 右侧的超链接
	 
	*/
	public final String getStartGuideLink()
	{
		return this.GetValStringByKey(FlowAttr.StartGuideLink);
	}
	public final void setStartGuideLink(String value)
	{
		this.SetValByKey(FlowAttr.StartGuideLink, value);
	}
	/** 
	 标签
	 
	*/
	public final String getStartGuideLab()
	{
		return this.GetValStringByKey(FlowAttr.StartGuideLab);
	}
	public final void setStartGuideLab(String value)
	{
		this.SetValByKey(FlowAttr.StartGuideLab, value);
	}
	/** 
	 前置导航参数1
	*/
	public final String getStartGuidePara1()
	{
		String str = this.GetValStringByKey(FlowAttr.StartGuidePara1);
		return str.replace("~", "'");
	}
	
	public final void setStartGuidePara1(String value)
	{
		this.SetValByKey(FlowAttr.StartGuidePara1, value);
	}


	/** 
	 流程发起参数2
	*/
	public final String getStartGuidePara2()
	{
		String str = this.GetValStringByKey(FlowAttr.StartGuidePara2);
		str = str.replace("~", "'");
		if (StringHelper.isNullOrEmpty(str))
		{
			if (this.getStartGuideWay() == BP.WF.Template.StartGuideWay.ByHistoryUrl)
			{
			}
		}
		return str;
	}
	public final void setStartGuidePara2(String value)
	{
		this.SetValByKey(FlowAttr.StartGuidePara2, value);
	}
	/** 
	 流程发起参数3
	*/
	public final String getStartGuidePara3()
	{
		return this.GetValStrByKey(FlowAttr.StartGuidePara3);
	}
	public final void setStartGuidePara3(String value)
	{
		this.SetValByKey(FlowAttr.StartGuidePara3, value);
	}
	/** 
	 是否启用数据重置按钮？
	*/
	public final boolean getIsResetData()
	{
		return this.GetValBooleanByKey(FlowAttr.IsResetData);
	}
	/** 
	 是否启用导入历史数据按钮?
	*/
	public final boolean getIsImpHistory()
	{
		return this.GetValBooleanByKey(FlowAttr.IsImpHistory);
	}
	/** 
	 是否自动装载上一笔数据?
	*/
	public final boolean getIsLoadPriData()
	{
		return this.GetValBooleanByKey(FlowAttr.IsLoadPriData);
	}

	/** 
	 流程删除规则
	*/
	
	public final int getFlowDeleteRole()
    {
         return this.GetValIntByKey(FlowAttr.FlowDeleteRole);
    }
	/** 
	 草稿规则
	*/
	public final DraftRole getDraftRole()
	{
		// return (DraftRole) this.GetValIntByKey(FlowAttr.Draft);
		return DraftRole.forValue(this.GetValIntByKey(FlowAttr.Draft));
	}
	public final void setDraftRole(DraftRole value)
	{
		this.SetValByKey(FlowAttr.Draft, value.getValue());
	}
	public String Tag = null;
	/** 
	 运行类型
	*/
	public final FlowRunWay getHisFlowRunWay()
	{
		// return (FlowRunWay) this.GetValIntByKey(FlowAttr.FlowRunWay);
		return FlowRunWay.forValue(this.GetValIntByKey(FlowAttr.FlowRunWay));
	}
	public final void setHisFlowRunWay(FlowRunWay value)
	{
		this.SetValByKey(FlowAttr.FlowRunWay, value.getValue());
	}
	/** 
	 运行对象
	*/
	public final String getRunObj()
	{
		return this.GetValStrByKey(FlowAttr.RunObj);
	}
	public final void setRunObj(String value)
	{
		this.SetValByKey(FlowAttr.RunObj, value);
	}
	/** 
	 时间点规则
	*/
	public final TimelineRole getHisTimelineRole()
	{
		return TimelineRole.forValue(this.GetValIntByKey(FlowAttr.TimelineRole));
	}
	/** 
	 流程部门数据查询权限控制方式
	*/
	public final FlowDeptDataRightCtrlType getHisFlowDeptDataRightCtrlType()
	{
		return FlowDeptDataRightCtrlType.forValue(this.GetValIntByKey(FlowAttr.DRCtrlType));
	}
	public final void setHisFlowDeptDataRightCtrlType(FlowDeptDataRightCtrlType value)
	{
		this.SetValByKey(FlowAttr.DRCtrlType, value);
	}
	/** 
	 流程应用类型
	*/
	public final FlowAppType getFlowAppType()
	{
		return FlowAppType.forValue(this.GetValIntByKey(FlowAttr.FlowAppType));
	}
	public final void setFlowAppType(FlowAppType value)
	{
		this.SetValByKey(FlowAttr.FlowAppType, value.getValue());
	}
	  

	/** 
	 流程备注的表达式	 
	*/
	public final String getFlowNoteExp()
	{
		return this.GetValStrByKey(FlowAttr.FlowNoteExp);
	}
	public final void setFlowNoteExp(String value)
	{
		this.SetValByKey(FlowAttr.FlowNoteExp, value);
	}

	/** 
	 创建新工作web方式调用的
	 @return 
	 * @throws Exception 
	*/
	public final Work NewWork() throws Exception
	{
		return NewWork(WebUser.getNo());
	}
	/** 
	 创建新工作.web方式调用的
	 @param empNo 人员编号
	 @return 
	 * @throws Exception 
	*/
	public final Work NewWork(String empNo) throws Exception
	{
		Emp emp = new Emp(empNo);
		return NewWork(emp, null);
	}
	/** 
	 产生一个开始节点的新工作
	 @param emp 发起人
	 @param paras 参数集合,如果是CS调用，要发起子流程，要从其他table里copy数据,就不能从request里面取,可以传递为null.
	 @return 返回的Work.
	 * @throws Exception 
	*/
	public final Work NewWork(Emp emp, java.util.Hashtable paras) throws Exception
	{
		// 检查是否可以发起该流程？
		if (Glo.CheckIsCanStartFlow_InitStartFlow(this) == false)
		{
			throw new RuntimeException("@您违反了该流程的【" + this.getStartLimitRole() + "】限制规则。" + this.getStartLimitAlert());
		}

		//如果是bs系统.
		if (paras == null)
		{
			paras = new java.util.Hashtable();
		}
		if (BP.Sys.SystemConfig.getIsBSsystem() == true)
		{
			Enumeration enu = BP.Sys.Glo.getRequest().getParameterNames();
			while (enu.hasMoreElements())
			{
				// 判断是否有内容，hasNext()
				String key = (String) enu.nextElement();
				paras.put(key, BP.Sys.Glo.getRequest().getParameter(key));
			}
		}

		//开始节点.
		BP.WF.Node nd = new BP.WF.Node(this.getStartNodeID());

		//从草稿里看看是否有新工作？
		StartWork wk = (StartWork)nd.getHisWork();
		wk.ResetDefaultVal();

		String dbstr = SystemConfig.getAppCenterDBVarStr();

		Paras ps = new Paras();
		GERpt rpt = this.getHisGERpt();

		//是否新创建的WorkID
		boolean IsNewWorkID = false;
		//如果要启用草稿,就创建一个新的WorkID .
		if (this.getDraftRole() != BP.WF.Template.DraftRole.None && nd.getIsStartNode())
		{
			IsNewWorkID = true;
		}

		try
		{
			//从报表里查询该数据是否存在？
			if (this.getIsGuestFlow() == true && StringHelper.isNullOrEmpty(GuestUser.getNo()) == false)
			{
				//是客户参与的流程，并且具有客户登陆的信息。
				ps.SQL = "SELECT OID,FlowEndNode FROM " + this.getPTable() + " WHERE GuestNo=" + dbstr + "GuestNo AND WFState=" + dbstr + "WFState ";
				ps.Add(GERptAttr.GuestNo, GuestUser.getNo());
				ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() > 0 && IsNewWorkID == false)
				{
					wk.setOID(Long.parseLong(dt.Rows.get(0).getValue(0).toString()));
					int nodeID = Integer.parseInt(dt.Rows.get(0).getValue(1).toString());
					if (nodeID != this.getStartNodeID())
					{
						String error = "@这里出现了blank的状态下流程运行到其它的节点上去了的情况。";
						Log.DefaultLogWriteLineError(error);
						throw new RuntimeException(error);
					}
				}
			}
			else
			{

				ps.SQL = "SELECT WorkID,FK_Node FROM WF_GenerWorkFlow WHERE WFState=0 AND Starter=" + dbstr + "FlowStarter AND FK_Flow=" + dbstr + "FK_Flow ";
				ps.Add(GERptAttr.FlowStarter, emp.getNo());
				ps.Add(GenerWorkFlowAttr.FK_Flow, this.getNo());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);

				//如果没有启用草稿，并且存在草稿就取第一条 by dgq 5.28
				if (dt.Rows.size() >0)
				{
					wk.setOID(Long.parseLong(dt.Rows.get(0).getValue(0).toString()));
					wk.RetrieveFromDBSources();
					int nodeID = Integer.parseInt(dt.Rows.get(0).getValue(1).toString());
					if (nodeID != this.getStartNodeID())
					{
						String error = "@这里出现了blank的状态下流程运行到其它的节点上去了的情况，当前停留节点:" + nodeID;
						Log.DefaultLogWriteLineError(error);
						//     throw new Exception(error);
					}
				}
			}

			//启用草稿或空白就创建WorkID
			if (wk.getOID() == 0)
			{
				// 说明没有空白,就创建一个空白..
				wk.ResetDefaultVal();
				wk.setRec(WebUser.getNo());

				wk.SetValByKey(StartWorkAttr.RecText, emp.getName());
				wk.SetValByKey(StartWorkAttr.Emps, emp.getNo());

				wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.getCurrentDataTime());
				wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.getCurrentDataTime());
				wk.SetValByKey(GERptAttr.WFState, WFState.Blank.getValue());

				wk.setOID(DBAccess.GenerOID("WorkID")); //这里产生WorkID ,这是唯一产生WorkID的地方.

				//把尽量可能的流程字段放入，否则会出现冲掉流程字段属性.
				wk.SetValByKey(GERptAttr.FK_NY, BP.DA.DataType.getCurrentYearMonth());
				wk.SetValByKey(GERptAttr.FK_Dept, emp.getFK_Dept());
				wk.setFID(0);
				wk.DirectInsert();

				//设置参数.
				for (Object k : paras.keySet())
				{
					rpt.SetValByKey(k.toString(), paras.get(k));
				}

				if (this.getPTable().equals(wk.getEnMap().getPhysicsTable()))
				{
					//如果开始节点表与流程报表相等.
					rpt.setOID(wk.getOID());
					rpt.RetrieveFromDBSources();
					rpt.setFID(0);
					rpt.setFlowStartRDT(BP.DA.DataType.getCurrentDataTime());
					rpt.setMyNum(0);
					rpt.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, wk));
					//WebUser.getNo() + "," + BP.Web.WebUser.Name + "在" + DataType.CurrentDataCNOfShort + "发起.";
					rpt.setWFState(WFState.Blank);
					rpt.setFlowStarter(emp.getNo());
					rpt.setFK_NY(DataType.getCurrentYearMonth());
					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
					{
						rpt.setFlowEmps("@" + emp.getName());
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.getNo());
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.getNo() + "," + emp.getName());
					}

					rpt.setFlowEnderRDT(BP.DA.DataType.getCurrentDataTime());
					rpt.setFK_Dept(emp.getFK_Dept());
					rpt.setFlowEnder(emp.getNo());
					rpt.setFlowEndNode(this.getStartNodeID());
					rpt.setWFState(WFState.Blank);
					rpt.setFID(0);
					rpt.DirectUpdate();
				}
				else
				{
					rpt.setOID(wk.getOID());
					rpt.setFID(0);
					rpt.setFlowStartRDT(BP.DA.DataType.getCurrentDataTime());
					rpt.setFlowEnderRDT(BP.DA.DataType.getCurrentDataTime());
					rpt.setMyNum(0);

					rpt.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, wk));
					// rpt.Title = WebUser.getNo() + "," + BP.Web.WebUser.Name + "在" + DataType.CurrentDataCNOfShort + "发起.";

					rpt.setWFState(WFState.Blank);
					rpt.setFlowStarter(emp.getNo());

					rpt.setFlowEndNode(this.getStartNodeID());
					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
					{
						rpt.setFlowEmps("@" + emp.getName());
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.getNo());
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.getNo() + "," + emp.getName());
					}

					rpt.setFK_NY(DataType.getCurrentYearMonth());
					rpt.setFK_Dept(emp.getFK_Dept());
					rpt.setFlowEnder(emp.getNo());
					rpt.InsertAsOID(wk.getOID());
				}

				//调用 OnCreateWorkID的方法.  add by zhoupeng 2016.12.4 for LIMS.
				this.DoFlowEventEntity(EventListOfNode.FlowOnCreateWorkID, nd, wk, null, null, null);

			}
		}
		catch (RuntimeException ex)
		{
			wk.CheckPhysicsTable();

			//检查报表.
			this.CheckRpt();
			throw new RuntimeException("@创建工作失败：有可能是您在设计表单时候，新增加的控件，没有预览导致的，请您刷新一次应该可以解决，技术信息：" + ex.getStackTrace() + " @ 技术信息:" + ex.getMessage());
		}

		//在创建WorkID的时候调用的事件.
		this.DoFlowEventEntity(EventListOfNode.CreateWorkID, nd, wk, null);


			///#region copy数据.
		// 记录这个id ,不让其它在复制时间被修改。
		long newOID = wk.getOID();
		if (IsNewWorkID == true)
		{
			// 处理传递过来的参数。
			int i = 0;
			for (Object k : paras.keySet())
			{
				i++;
				wk.SetValByKey(k.toString(), paras.get(k).toString());
			}

			if (i >= 3)
			{
				wk.setOID(newOID);
				wk.DirectUpdate();
			}
		}

		if (paras.containsKey(StartFlowParaNameList.IsDeleteDraft) && paras.get(StartFlowParaNameList.IsDeleteDraft).toString().equals("1"))
		{
			//是否要删除Draft 
			long oid = wk.getOID();
			try
			{
				//wk.ResetDefaultValAllAttr();
				wk.DirectUpdate();
			}
			catch (RuntimeException ex)
			{
				wk.Update();
				BP.DA.Log.DebugWriteError("创建新工作错误，但是屏蔽了异常,请检查默认值的问题：" + ex.getMessage());
			}

			MapDtls dtls = wk.getHisMapDtls();
			for (MapDtl dtl : dtls.ToJavaList())
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + oid);
			}

			//删除附件数据。
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + wk.getNodeID() + "' AND RefPKVal='" + wk.getOID() + "'");
			wk.setOID(newOID);
		}
			///#region 处理开始节点, 如果传递过来 FromTableName 就是要从这个表里copy数据。
		if (paras.containsKey("FromTableName"))
		{
			String tableName = paras.get("FromTableName").toString();
			String tablePK = paras.get("FromTablePK").toString();
			String tablePKVal = paras.get("FromTablePKVal").toString();

			DataTable dt = DBAccess.RunSQLReturnTable("SELECT * FROM " + tableName + " WHERE " + tablePK + "='" + tablePKVal + "'");
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@利用table传递数据错误，没有找到指定的行数据，无法为用户填充数据。");
			}

			String innerKeys = ",OID,RDT,CDT,FID,WFState,";
			for (DataColumn dc : dt.Columns)
			{
				if (innerKeys.contains("," + dc.ColumnName + ","))
				{
					continue;
				}

				wk.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName).toString());
				rpt.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName).toString());
			}
			rpt.Update();
		}
		// 获取特殊标记变量.
		String PFlowNo = null;
		String PNodeIDStr = null;
		String PWorkIDStr = null;
		String PFIDStr = null;

		String CopyFormWorkID = null;
		if (paras.containsKey("CopyFormWorkID") == true)
		{
			CopyFormWorkID = paras.get("CopyFormWorkID").toString();
			PFlowNo = this.getNo();
			PNodeIDStr = paras.get("CopyFormNode").toString();
			PWorkIDStr = CopyFormWorkID;
			PFIDStr = "0";
		}

		if (paras.containsKey("PNodeID") == true)
		{
			PFlowNo = paras.get("PFlowNo").toString();
			PNodeIDStr = paras.get("PNodeID").toString();
			PWorkIDStr = paras.get("PWorkID").toString();
			PFIDStr = "0";
			if (paras.containsKey("PFID") == true)
			{
				PFIDStr = paras.get("PFID").toString(); //父流程.
			}
		}
			///#region  判断是否装载上一条数据.
		if (this.getIsLoadPriData() == true && this.getStartGuideWay() == BP.WF.Template.StartGuideWay.None)
		{
			// 如果需要从上一个流程实例上copy数据. 
			String sql = "SELECT OID FROM " + this.getPTable() + " WHERE FlowStarter='" + WebUser.getNo() + "' AND OID!=" + wk.getOID() + " ORDER BY OID DESC";
			String workidPri = DBAccess.RunSQLReturnStringIsNull(sql, "0");
			if (workidPri.equals("0"))
			{
				//说明没有第一笔数据.
			}
			else
			{
				PFlowNo = this.getNo();
				PNodeIDStr = Integer.parseInt(this.getNo()) + "01";
				PWorkIDStr = workidPri;
				PFIDStr = "0";
				CopyFormWorkID = workidPri;
			}
		}
			///#region 处理流程之间的数据传递1。
		if (StringHelper.isNullOrEmpty(PNodeIDStr) == false && StringHelper.isNullOrEmpty(PWorkIDStr) == false)
		{
			long PWorkID = Long.parseLong(PWorkIDStr);
			long PNodeID = 0;
			if (CopyFormWorkID != null)
			{
				PNodeID = Long.parseLong(PNodeIDStr);
			}
				///#region copy 首先从父流程的NDxxxRpt copy.
			long pWorkIDReal = 0;
			Flow pFlow = new Flow(PFlowNo);
			String pOID = "";
			if (StringHelper.isNullOrEmpty(PFIDStr) == true || PFIDStr.equals("0"))
			{
				pOID = (new Long(PWorkID)).toString();
			}
			else
			{
				pOID = PFIDStr;
			}

			String sql = "SELECT * FROM " + pFlow.getPTable() + " WHERE OID=" + pOID;
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() != 1)
			{
				throw new RuntimeException("@不应该查询不到父流程的数据, 可能的情况之一,请确认该父流程的调用节点是子线程，但是没有把子线程的FID参数传递进来。");
			}

			wk.Copy(dt.Rows.get(0));
			rpt.Copy(dt.Rows.get(0));
				///#region 从调用的节点上copy.
			BP.WF.Node fromNd = new BP.WF.Node(Integer.parseInt(PNodeIDStr));
			Work wkFrom = fromNd.getHisWork();
			wkFrom.setOID(PWorkID);
			if (wkFrom.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("@父流程的工作ID不正确，没有查询到数据" + PWorkID);
			}
			for (Object k : paras.keySet())
			{
				wk.SetValByKey(k.toString(), paras.get(k));
				rpt.SetValByKey(k.toString(), paras.get(k));
			}
			wk.setOID(newOID);
			rpt.setOID(newOID);
			// 在执行copy后，有可能这两个字段会被冲掉。
			if (CopyFormWorkID != null)
			{
				//如果不是 执行的从已经完成的流程copy.

				wk.SetValByKey(StartWorkAttr.PFlowNo, PFlowNo);
				wk.SetValByKey(StartWorkAttr.PNodeID, PNodeID);
				wk.SetValByKey(StartWorkAttr.PWorkID, PWorkID);

				rpt.SetValByKey(GERptAttr.PFlowNo, PFlowNo);
				rpt.SetValByKey(GERptAttr.PNodeID, PNodeID);
				rpt.SetValByKey(GERptAttr.PWorkID, PWorkID);

				//忘记了增加这句话.
				rpt.SetValByKey(GERptAttr.PEmp, WebUser.getNo());

				//要处理单据编号 BillNo .
				if (!this.getBillNoFormat().equals(""))
				{
					rpt.SetValByKey(GERptAttr.BillNo, BP.WF.WorkFlowBuessRole.GenerBillNo(this.getBillNoFormat(), rpt.getOID(), rpt, this.getPTable()));

					//设置单据编号.
					wk.SetValByKey(GERptAttr.BillNo, rpt.getBillNo());
				}

				rpt.SetValByKey(GERptAttr.FID, 0);
				rpt.SetValByKey(GERptAttr.FlowStartRDT, BP.DA.DataType.getCurrentDataTime());
				rpt.SetValByKey(GERptAttr.FlowEnderRDT, BP.DA.DataType.getCurrentDataTime());
				rpt.SetValByKey(GERptAttr.MyNum, 0);
				rpt.SetValByKey(GERptAttr.WFState, WFState.Blank.getValue());
				rpt.SetValByKey(GERptAttr.FlowStarter, emp.getNo());
				rpt.SetValByKey(GERptAttr.FlowEnder, emp.getNo());
				rpt.SetValByKey(GERptAttr.FlowEndNode, this.getStartNodeID());
				rpt.SetValByKey(GERptAttr.FK_Dept, emp.getFK_Dept());
				rpt.SetValByKey(GERptAttr.FK_NY, DataType.getCurrentYearMonth());

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getName());
				}

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getNo());
				}

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getNo() + "," + emp.getName());
				}

			}

			if (rpt.getEnMap().getPhysicsTable() != wk.getEnMap().getPhysicsTable())
			{
				wk.Update(); //更新工作节点数据.
			}
			rpt.Update(); // 更新流程数据表.

				///#endregion 特殊赋值.


				///#region 复制其他数据..
			//复制明细。
			MapDtls dtls = wk.getHisMapDtls();
			if (dtls.size() > 0)
			{
				MapDtls dtlsFrom = wkFrom.getHisMapDtls();
				int idx = 0;
				if (dtlsFrom.size() == dtls.size())
				{
					for (MapDtl dtl : dtls.ToJavaList())
					{
						if (dtl.getIsCopyNDData() == false)
						{
							continue;
						}

						//new 一个实例.
						GEDtl dtlData = new GEDtl(dtl.getNo());

						//检查该明细表是否有数据，如果没有数据，就copy过来，如果有，就说明已经copy过了。
						//  sql = "SELECT COUNT(OID) FROM "+dtlData.getEnMap().getPhysicsTable()+" WHERE RefPK="+wk.OID;

						//删除以前的数据.
						sql = "DELETE FROM " + dtlData.getEnMap().getPhysicsTable() + " WHERE RefPK=" + wk.getOID();
						DBAccess.RunSQL(sql);


						MapDtl dtlFrom = (MapDtl)((dtlsFrom.get(idx) instanceof MapDtl) ? dtlsFrom.get(idx) : null);

						GEDtls dtlsFromData = new GEDtls(dtlFrom.getNo());
						dtlsFromData.Retrieve(GEDtlAttr.RefPK, PWorkID);
						for (GEDtl geDtlFromData : dtlsFromData.ToJavaList())
						{
							dtlData.Copy(geDtlFromData);
							//dtlsFromData.Retrieve(GEDtlAttr.RefPK, PWorkID);
							dtlData.setRefPK(wk.getOID()+"");
							if (PFlowNo.equals(this.getNo()))
							{
								dtlData.InsertAsNew();
							}
							else
							{
                                if (this.getStartLimitRole() == BP.WF.StartLimitRole.OnlyOneSubFlow)
								{
                                    dtlData.SaveAsOID((int)geDtlFromData.getOID()); //为子流程的时候，仅仅允许被调用1次.
								}
								else
								{
									dtlData.InsertAsNew();
								}
							}
						}
					}
				}
			}

			//复制附件数据。
			if (wk.getHisFrmAttachments().size() > 0)
			{
				if (wkFrom.getHisFrmAttachments().size() > 0)
				{
					int toNodeID = wk.getNodeID();

					//删除数据。
					DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + toNodeID + "' AND RefPKVal='" + wk.getOID() + "'");
					FrmAttachmentDBs athDBs = new FrmAttachmentDBs("ND" + PNodeIDStr, (new Long(PWorkID)).toString());

					for (FrmAttachmentDB athDB : athDBs.ToJavaList())
					{
						FrmAttachmentDB athDB_N = new FrmAttachmentDB();
						athDB_N.Copy(athDB);
						athDB_N.setFK_MapData("ND" + toNodeID);
						athDB_N.setRefPKVal(String.valueOf(wk.getOID()));
						athDB_N.setFK_FrmAttachment(athDB_N.getFK_FrmAttachment().replace("ND" + PNodeIDStr, "ND" + toNodeID));

						if (athDB_N.getHisAttachmentUploadType() == AttachmentUploadType.Single)
						{
							/*如果是单附件.*/
							athDB_N.setMyPK(athDB_N.getFK_FrmAttachment() + "_" + wk.getOID());
							if (athDB_N.getIsExits() == true)
							{
								continue; //说明上一个节点或者子线程已经copy过了, 但是还有子线程向合流点传递数据的可能，所以不能用break.
							}
							athDB_N.Insert();
						}
						else
						{
							athDB_N.setMyPK(athDB_N.getUploadGUID() + "_" + athDB_N.getFK_MapData() + "_" + wk.getOID());
							athDB_N.Insert();
						}
					}
				}
			}
			//求出来被copy的节点有多少个独立表单.
			FrmNodes fnsFrom = new FrmNodes(fromNd.getNodeID());
			if (fnsFrom.size() != 0)
			{
				//求当前节点表单的绑定的表单.
				FrmNodes fns = new FrmNodes(nd.getNodeID());
				if (fns.size() != 0)
				{
					//开始遍历当前绑定的表单.
					for (FrmNode fn : fns.ToJavaList())
					{
						for (FrmNode fnFrom : fnsFrom.ToJavaList())
						{
							if (!fn.getFK_Frm().equals(fnFrom.getFK_Frm()))
							{
								continue;
							}

							BP.Sys.GEEntity geEnFrom = new GEEntity(fnFrom.getFK_Frm());
							geEnFrom.setOID(PWorkID);
							if (geEnFrom.RetrieveFromDBSources() == 0)
							{
								continue;
							}

							//执行数据copy , 复制到本身. 
							geEnFrom.CopyToOID(wk.getOID());
						}
					}
				}
			}
		}
		//生成单据编号.
		if (this.getBillNoFormat().length() > 3)
			if (this.getBillNoFormat().length() > 3)
			{
				Object tempVar3 = this.getBillNoFormat();
				String billNoFormat = (String)((tempVar3 instanceof String) ? tempVar3 : null);

				if (billNoFormat.contains("@"))
				{
					for (Object str : paras.keySet())
					{
						billNoFormat = billNoFormat.replace("@" + str, paras.get(str).toString());
					}
				}

				//生成单据编号.
				rpt.setBillNo(BP.WF.Glo.GenerBillNo(billNoFormat, rpt.getOID(), rpt, this.getPTable()));
				//rpt.Update(GERptAttr.BillNo, rpt.BillNo);
				if (wk.getRow().containsKey(GERptAttr.BillNo) == true)
				{
					wk.SetValByKey(NDXRptBaseAttr.BillNo, rpt.getBillNo());
					// wk.Update(GERptAttr.BillNo, rpt.BillNo);
				}
				rpt.Update();
			}

			///#endregion 处理单据编号.
			///#region 处理流程之间的数据传递2, 如果是直接要跳转到指定的节点上去.
		if (paras.containsKey("JumpToNode") == true)
		{
			wk.setRec(WebUser.getNo());
			wk.SetValByKey(StartWorkAttr.RDT, BP.DA.DataType.getCurrentDataTime());
			wk.SetValByKey(StartWorkAttr.CDT, BP.DA.DataType.getCurrentDataTime());
			wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
			wk.setFK_Dept(emp.getFK_Dept());
			wk.SetValByKey("FK_DeptName", emp.getFK_DeptText());
			wk.SetValByKey("FK_DeptText", emp.getFK_DeptText());
			wk.setFID(0);
			wk.SetValByKey(StartWorkAttr.RecText, emp.getName());

			int jumpNodeID = Integer.parseInt(paras.get("JumpToNode").toString());
			Node jumpNode = new Node(jumpNodeID);

			String jumpToEmp = paras.get("JumpToEmp").toString();
			if (StringHelper.isNullOrEmpty(jumpToEmp))
			{
				jumpToEmp = emp.getNo();
			}

			WorkNode wn = new WorkNode(wk, nd);
			wn.NodeSend(jumpNode, jumpToEmp);

			WorkFlow wf = new WorkFlow(this, wk.getOID(), wk.getFID());

			BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(rpt.getOID());
			rpt.setWFState(WFState.Runing);
			rpt.Update();

			return wf.GetCurrentWorkNode().getHisWork();
		}

			///#endregion 处理流程之间的数据传递。


			///#region 最后整理wk数据.
		wk.setRec(emp.getNo());
		wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.getCurrentDataTime());
		wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.getCurrentDataTime());
		wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
		wk.setFK_Dept(emp.getFK_Dept());
		wk.SetValByKey("FK_DeptName", emp.getFK_DeptText());
		wk.SetValByKey("FK_DeptText", emp.getFK_DeptText());

		wk.SetValByKey(NDXRptBaseAttr.BillNo, rpt.getBillNo());
		wk.setFID(0);
		wk.SetValByKey(StartWorkAttr.RecText, emp.getName());
		if (wk.getIsExits() == false)
		{
			wk.DirectInsert();
		}
		else
		{
			wk.Update();
		}

		///#endregion 最后整理参数.


		///#region 给generworkflow初始化数据. add 2015-08-06
		GenerWorkFlow mygwf = new GenerWorkFlow();
		mygwf.setWorkID(wk.getOID());
		
		if (mygwf.RetrieveFromDBSources() == 0)
		{
			mygwf.setFK_Flow(this.getNo());
			mygwf.setFK_FlowSort(this.getFK_FlowSort());
			mygwf.setSysType(this.getSysType());
			mygwf.setFK_Node(nd.getNodeID());
			mygwf.setWorkID(wk.getOID());
			mygwf.setWFState(WFState.Blank);
			mygwf.setFlowName(this.getName());
			mygwf.setRDT(BP.DA.DataType.getCurrentDataTime());
			mygwf.Insert();
		}
		mygwf.setStarter(WebUser.getNo());
		mygwf.setStarterName(WebUser.getName());
		mygwf.setFK_Dept(BP.Web.WebUser.getFK_Dept());
		mygwf.setDeptName(BP.Web.WebUser.getFK_DeptName());
		mygwf.setBillNo(rpt.getBillNo());
		mygwf.setTitle(rpt.getTitle());

		if (mygwf.getTitle().contains("@") == true)
		{
			mygwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, rpt));
		}
		if (StringHelper.isNullOrEmpty(PNodeIDStr) == false && StringHelper.isNullOrEmpty(PWorkIDStr) == false)
		{
			if (StringHelper.isNullOrEmpty(PFIDStr) == false)
			{
				mygwf.setPFID(Long.parseLong(PFIDStr));
			}
			mygwf.setPEmp(rpt.getPEmp());
			mygwf.setPFlowNo(rpt.getPFlowNo());
			mygwf.setPNodeID(rpt.getPNodeID());
			mygwf.setPWorkID(rpt.getPWorkID());
		}
		mygwf.Update();
			///#endregion 给 generworkflow 初始化数据.

		return wk;
	}


		///#endregion 创建新工作.


		///#region 初始化一个工作.
	/** 
	 初始化一个工作
	 
	 @param workid
	 @param fk_node
	 @return 
	 * @throws Exception 
	*/
	public final Work GenerWork(long workid, Node nd, boolean isPostBack) throws Exception
	{
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		if (wk.RetrieveFromDBSources() == 0)
		{
//                
//                 * 2012-10-15 偶然发现一次工作丢失情况, WF_GenerWorkerlist WF_GenerWorkFlow 都有这笔数据，没有查明丢失原因。 stone.
//                 * 用如下代码自动修复，但是会遇到数据copy不完全的问题。
//                 * 

///#warning 2011-10-15 偶然发现一次工作丢失情况.

			String fk_mapData = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
			GERpt rpt = new GERpt(fk_mapData);
			rpt.setOID(Integer.parseInt((new Long(workid)).toString()));
			if (rpt.RetrieveFromDBSources() >= 1)
			{
				//  查询到报表数据.  
				wk.Copy(rpt);
				wk.setRec(WebUser.getNo());
				wk.InsertAsOID(workid);
			}
			else
			{
				//  没有查询到报表数据.  


///#warning 这里不应该出现的异常信息.

				String msg = "@不应该出现的异常.";
				msg += "@在为节点NodeID=" + nd.getNodeID() + " workid:" + workid + " 获取数据时.";
				msg += "@获取它的Rpt表数据时，不应该查询不到。";
				msg += "@GERpt 信息: table:" + rpt.getEnMap().getPhysicsTable() + "   OID=" + rpt.getOID();

				String sql = "SELECT count(*) FROM " + rpt.getEnMap().getPhysicsTable() + " WHERE OID=" + workid;
				int num = DBAccess.RunSQLReturnValInt(sql);

				msg += " @SQL:" + sql;
				msg += " ReturnNum:" + num;
				if (num == 0)
				{
					msg += "已经用sql可以查询出来，但是不应该用类查询不出来.";
				}
				else
				{
					//如果可以用sql 查询出来.
					num = rpt.RetrieveFromDBSources();
					msg += "@从rpt.RetrieveFromDBSources = " + num;
				}

				Log.DefaultLogWriteLineError(msg);

				MapData md = new MapData("ND" + Integer.parseInt(nd.getFK_Flow()) + "01");
				sql = "SELECT * FROM " + md.getPTable() + " WHERE OID=" + workid;
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 1)
				{
					rpt.Copy(dt.Rows.get(0));
					try
					{
						rpt.setFlowStarter(dt.Rows.get(0).getValue(StartWorkAttr.Rec).toString());
						rpt.setFlowStartRDT(dt.Rows.get(0).getValue(StartWorkAttr.RDT).toString());
						rpt.setFK_Dept(dt.Rows.get(0).getValue(StartWorkAttr.FK_Dept).toString());
					}
					catch (java.lang.Exception e)
					{
					}

					rpt.setOID(Integer.parseInt((new Long(workid)).toString()));
					try
					{
						rpt.InsertAsOID(rpt.getOID());
					}
					catch (RuntimeException ex)
					{
						Log.DefaultLogWriteLineError("@不应该出插入不进去 rpt:" + rpt.getEnMap().getPhysicsTable() + " workid=" + workid);
						rpt.RetrieveFromDBSources();
					}
				}
				else
				{
					Log.DefaultLogWriteLineError("@没有找到开始节点的数据, NodeID:" + nd.getNodeID() + " workid:" + workid);
					throw new RuntimeException("@没有找到开始节点的数据, NodeID:" + nd.getNodeID() + " workid:" + workid + " SQL:" + sql);
				}


///#warning 不应该出现的工作丢失.
				Log.DefaultLogWriteLineError("@工作[" + nd.getNodeID() + " : " + wk.getEnDesc() + "], 报表数据WorkID=" + workid + " 丢失, 没有从NDxxxRpt里找到记录,请联系管理员。");

				wk.Copy(rpt);
				wk.setRec(WebUser.getNo());
				wk.ResetDefaultVal();
				wk.Insert();
			}
		}


			///#region 判断是否有删除草稿的需求.
		if (SystemConfig.getIsBSsystem() == true && isPostBack == false && nd.getIsStartNode() && BP.Sys.Glo.getRequest().getParameter("IsDeleteDraft").equals("1"))
		{

			//需要删除草稿.
			//是否要删除Draft 
			String title = wk.GetValStringByKey("Title");
			wk.ResetDefaultValAllAttr();
			wk.setOID(workid);
			wk.SetValByKey(GenerWorkFlowAttr.Title, title);
			wk.DirectUpdate();

			MapDtls dtls = wk.getHisMapDtls();
			for (MapDtl dtl : dtls.ToJavaList())
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + wk.getOID());
			}

			//删除附件数据。
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + wk.getNodeID() + "' AND RefPKVal='" + wk.getOID() + "'");

		}
		// 设置当前的人员把记录人。
		wk.setRec(WebUser.getNo());
		wk.setRecText(WebUser.getName());
		wk.setRec(WebUser.getNo());
		wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.getCurrentDataTime());
		wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.getCurrentDataTime());
		wk.SetValByKey(GERptAttr.WFState, WFState.Runing);
		wk.SetValByKey("FK_Dept", WebUser.getFK_Dept());
		wk.SetValByKey("FK_DeptName", WebUser.getFK_DeptName());
		wk.SetValByKey("FK_DeptText", WebUser.getFK_DeptName());
		wk.setFID(0);
		wk.SetValByKey("RecText", WebUser.getName());

		//处理单据编号.
		if (nd.getIsStartNode())
		{
			try
			{
				String billNo = wk.GetValStringByKey(NDXRptBaseAttr.BillNo);
				if (StringHelper.isNullOrEmpty(billNo) && nd.getHisFlow().getBillNoFormat().length() > 2)
				{
					//让他自动生成编号
					wk.SetValByKey(NDXRptBaseAttr.BillNo, BP.WF.WorkFlowBuessRole.GenerBillNo(nd.getHisFlow().getBillNoFormat(), wk.getOID(), wk, nd.getHisFlow().getPTable()));
				}
			}
			catch (java.lang.Exception e2)
			{
				// 可能是没有billNo这个字段,也不需要处理它.
			}
		}

		return wk;
	}
		///#region 其他通用方法.
	public final String DoBTableDTS() throws Exception
	{
		if (this.getDTSWay() == FlowDTSWay.None)
		{
			return "执行失败，您没有设置同步方式。";
		}

		String info = "";
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.FK_Flow, this.getNo());
		for (GenerWorkFlow gwf : gwfs.ToJavaList())
		{
			GERpt rpt = this.getHisGERpt();
			rpt.setOID(gwf.getWorkID());
			rpt.RetrieveFromDBSources();

			info += "@开始同步:" + gwf.getTitle() + ",WorkID=" + gwf.getWorkID();
			if (gwf.getWFSta() == WFSta.Complete)
			{
				info += this.DoBTableDTS(rpt, new Node(gwf.getFK_Node()), true);
			}
			else
			{
				info += this.DoBTableDTS(rpt, new Node(gwf.getFK_Node()), false);
			}
		}
		return info;
	}
	/** 
	 同步当前的流程数据到业务数据表里.
	 
	 @param rpt 流程报表
	 @param currNode 当前节点ID
	 @param isStopFlow 流程是否结束
	 @return 返回同步结果.
	 * @throws Exception 
	*/
	public final String DoBTableDTS(GERpt rpt, Node currNode, boolean isStopFlow) throws Exception
	{
		boolean isActiveSave = false;
		// 判断是否符合流程数据同步条件.
		switch (this.getDTSTime())
		{
			case AllNodeSend:
				isActiveSave = true;
				break;
			case SpecNodeSend:
				if (this.getDTSSpecNodes().contains((new Integer(currNode.getNodeID())).toString()) == true)
				{
					isActiveSave = true;
				}
				break;
			case WhenFlowOver:
				if (isStopFlow)
				{
					isActiveSave = true;
				}
				break;
			default:
				break;
		}
		if (isActiveSave == false)
		{
			return "";
		}
			///#region qinfaliang, 编写同步的业务逻辑,执行错误就抛出异常.

		String[] dtsArray = this.getDTSFields().split("[@]", -1);

		String[] lcArr = dtsArray[0].split("[,]", -1); //取出对应的主表字段
		String[] ywArr = dtsArray[1].split("[,]", -1); //取出对应的业务表字段


		String sql = "SELECT " + dtsArray[0] + " FROM " + this.getPTable().toUpperCase() + " WHERE OID=" + rpt.getOID();
		DataTable lcDt = DBAccess.RunSQLReturnTable(sql);
		if (lcDt.Rows.size() == 0) //没有记录就return掉
		{
			return "";
		}

		BP.Sys.SFDBSrc src = new BP.Sys.SFDBSrc(this.getDTSDBSrc());
		sql = "SELECT " + dtsArray[1] + " FROM " + this.getDTSBTable().toUpperCase();

		DataTable ywDt = src.RunSQLReturnTable(sql);

		String values = "";
		String upVal = "";


		for (int i = 0; i < lcArr.length; i++)
		{
			switch (src.getDBSrcType())
			{
				case Localhost:
					switch (SystemConfig.getAppCenterDBType())
					{
						case MSSQL:
							break;
						case Oracle:
							if (ywDt.Columns.get(ywArr[i]).DataType == DateUtils.class)
							{
								if (!StringHelper.isNullOrEmpty(lcDt.Rows.get(0).getValue(lcArr[i].toString()).toString()))
								{
									values += "to_date('" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "','YYYY-MM-DD'),";
								}
								else
								{
									values += "'',";
								}
								continue;
							}
							values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
							continue;
						case MySQL:
							break;
						case Informix:
							break;
						default:
							throw new RuntimeException("没有涉及到的连接测试类型...");
					}
					break;
				case SQLServer:
					break;
				case MySQL:
					break;
				case Oracle:
					if (ywDt.Columns.get(ywArr[i]).DataType == DateUtils.class)
					{
						if (!StringHelper.isNullOrEmpty(lcDt.Rows.get(0).getValue(lcArr[i].toString()).toString()))
						{
							values += "to_date('" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "','YYYY-MM-DD'),";
						}
						else
						{
							values += "'',";
						}
						continue;
					}
					values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
					continue;
				default:
					throw new RuntimeException("暂时不支您所使用的数据库类型!");
			}
			values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
			//获取除主键之外的其他值
			if (i > 0)
			{
				upVal = upVal + ywArr[i] + "='" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
			}
		}

		values = values.substring(0, values.length() - 1);
		upVal = upVal.substring(0, upVal.length() - 1);

		//查询对应的业务表中是否存在这条记录
		sql = "SELECT * FROM " + this.getDTSBTable().toUpperCase() + " WHERE " + getDTSBTablePK() + "='" + lcDt.Rows.get(0).getValue(lcArr[0].toString()) + "'";
		DataTable dt = src.RunSQLReturnTable(sql);
		//如果存在，执行更新，如果不存在，执行插入
		if (dt.Rows.size() > 0)
		{

			sql = "UPDATE " + this.getDTSBTable().toUpperCase() + " SET " + upVal + " WHERE " + getDTSBTablePK() + "='" + lcDt.Rows.get(0).getValue(lcArr[0].toString()) + "'";
		}
		else
		{
			sql = "INSERT INTO " + this.getDTSBTable().toUpperCase() + "(" + dtsArray[1] + ") VALUES(" + values + ")";
		}

		try
		{
			src.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage());
		}
		return "同步成功.";
	}
	/** 
	 自动发起
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoAutoStartIt() throws Exception
	{
		switch (this.getHisFlowRunWay())
		{
			case SpecEmp: //指定人员按时运行。
				String RunObj = this.getRunObj();
				String FK_Emp = RunObj.substring(0, RunObj.indexOf('@'));
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.setNo(FK_Emp);
				if (emp.RetrieveFromDBSources() == 0)
				{
					return "启动自动启动流程错误：发起人(" + FK_Emp + ")不存在。";
				}

				BP.Web.WebUser.SignInOfGener(emp);
				String info_send = BP.WF.Dev2Interface.Node_StartWork(this.getNo(), null, null, 0, null, 0, null).ToMsgOfText();
				if ( ! WebUser.getNo().equals("admin"))
				{
					emp = new BP.Port.Emp();
					emp.setNo("admin");
					emp.Retrieve();
					BP.Web.WebUser.SignInOfGener(emp);
					return info_send;
				}
				return info_send;
			case DataModel: //按数据集合驱动的模式执行。
				break;
			default:
				return "@该流程您没有设置为自动启动的流程类型。";
		}

		String msg = "";
		BP.Sys.MapExt me = new MapExt();
		me.setMyPK("ND" + Integer.parseInt(this.getNo()) + "01_" + MapExtXmlList.StartFlow);
		int i = me.RetrieveFromDBSources();
		if (i == 0)
		{
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + this.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return "没有为流程(" + this.getName() + ")的开始节点设置发起数据,请参考说明书解决.";
		}
		if (StringHelper.isNullOrEmpty(me.getTag()))
		{
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + this.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return "没有为流程(" + this.getName() + ")的开始节点设置发起数据,请参考说明书解决.";
		}

		// 获取从表数据.
		DataSet ds = new DataSet();
		String[] dtlSQLs = me.getTag1().split("[*]", -1);
		for (String sql : dtlSQLs)
		{
			if (StringHelper.isNullOrEmpty(sql))
			{
				continue;
			}

			String[] tempStrs = sql.split("[=]", -1);
			String dtlName = tempStrs[0];
			DataTable dtlTable = DBAccess.RunSQLReturnTable(sql.replace(dtlName + "=", ""));
			dtlTable.TableName = dtlName;
			ds.Tables.add(dtlTable);
		}


			///#region 检查数据源是否正确.
		String errMsg = "";
		// 获取主表数据.
		DataTable dtMain = DBAccess.RunSQLReturnTable(me.getTag());
		if (dtMain.Rows.size() == 0)
		{
			return "流程(" + this.getName() + ")此时无任务,查询语句:" + me.getTag().replace("'", "”");
		}

		msg += "@查询到(" + dtMain.Rows.size() + ")条任务.";

		if (dtMain.Columns.contains("Starter") == false)
		{
			errMsg += "@配值的主表中没有Starter列.";
		}

		if (dtMain.Columns.contains("MainPK") == false)
		{
			errMsg += "@配值的主表中没有MainPK列.";
		}

		if (errMsg.length() > 2)
		{
			return "流程(" + this.getName() + ")的开始节点设置发起数据,不完整." + errMsg;
		}

			///#region 处理流程发起.

		String fk_mapdata = "ND" + Integer.parseInt(this.getNo()) + "01";

		MapData md = new MapData(fk_mapdata);
		int idx = 0;
		for (DataRow dr : dtMain.Rows)
		{
			idx++;

			String mainPK = dr.getValue("MainPK").toString();
			String sql = "SELECT OID FROM " + md.getPTable() + " WHERE MainPK='" + mainPK + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0)
			{
				msg += "@" + this.getName() + ",第" + idx + "条,此任务在之前已经完成。";
				continue; //说明已经调度过了
			}

			String starter = dr.getValue("Starter").toString();
			if (!starter.equals(WebUser.getNo()))
			{
				BP.Web.WebUser.Exit();
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.setNo(starter);
				if (emp.RetrieveFromDBSources() == 0)
				{
					msg += "@" + this.getName() + ",第" + idx + "条,设置的发起人员:" + emp.getNo() + "不存在.";
					msg += "@数据驱动方式发起流程(" + this.getName() + ")设置的发起人员:" + emp.getNo() + "不存在。";
					continue;
				}
				WebUser.SignInOfGener(emp);
			}


				///#region  给值.
			Work wk = this.NewWork();
			for (DataColumn dc : dtMain.Columns)
			{
				wk.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
			}

			if (ds.Tables.size() != 0)
			{
				// MapData md = new MapData(nodeTable);
				MapDtls dtls = md.getMapDtls(); // new MapDtls(nodeTable);
				for (MapDtl dtl : dtls.ToJavaList())
				{
					for (DataTable dt : ds.Tables)
					{
						if (dt.TableName != dtl.getNo())
						{
							continue;
						}

						//删除原来的数据。
						GEDtl dtlEn = dtl.getHisGEDtl();
						dtlEn.Delete(GEDtlAttr.RefPK, (new Long(wk.getOID())).toString());

						// 执行数据插入。
						for (DataRow drDtl : dt.Rows)
						{
							if (!drDtl.getValue("RefMainPK").toString().equals(mainPK))
							{
								continue;
							}

							dtlEn = dtl.getHisGEDtl();
							for (DataColumn dc : dt.Columns)
							{
								dtlEn.SetValByKey(dc.ColumnName, drDtl.getValue(dc.ColumnName).toString());
							}

							dtlEn.setRefPK(String.valueOf(wk.getOID()));
							dtlEn.setOID(0);
							dtlEn.Insert();
						}
					}
				}
			}
			// 处理发送信息.
			Node nd = this.getHisStartNode();
			try
			{
				WorkNode wn = new WorkNode(wk, nd);
				String infoSend = wn.NodeSend().ToMsgOfHtml();
				BP.DA.Log.DefaultLogWriteLineInfo(msg);
				msg += "@" + this.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "已完成.\r\n" + infoSend;
				//this.SetText("@第（" + idx + "）条任务，" + WebUser.getNo() + " - " + WebUser.Name + "已经完成。\r\n" + msg);
			}
			catch (RuntimeException ex)
			{
				msg += "@" + this.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "发起时出现错误.\r\n" + ex.getMessage();
			}
			msg += "<hr>";
		}
		return msg;

			///#endregion 处理流程发起.
	}

	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsUpdate = true;
		}
		return uac;
	}
	/** 
	 修补流程数据视图
	 
	 @return 
	*/
	public static String RepareV_FlowData_View()
	{
		return null;
		//string err = "";
		//Flows fls = new Flows();
		//fls.RetrieveAllFromDBSource();

		//if (fls.Count == 0)
		//    return null;

		//string sql = "";
		//sql = "CREATE VIEW V_FlowData (FK_FlowSort,FK_Flow,OID,FID,Title,WFState,CDT,FlowStarter,FlowStartRDT,FK_Dept,FK_NY,FlowDaySpan,FlowEmps,FlowEnder,FlowEnderRDT,FlowEndNode,MyNum, PWorkID,PFlowNo,BillNo,ProjNo) ";
		////     sql += "\t\n /*  WorkFlow Data " + DateTime.Now.ToString("yyyy-MM-dd") + " */ ";
		//sql += " AS ";
		//foreach (Flow fl in fls)
		//{
		//    if (fl.IsCanStart == false)
		//        continue;


		//    string mysql = "\t\n SELECT '" + fl.FK_FlowSort + "' AS FK_FlowSort,'" + fl.getNo() + "' AS FK_Flow,OID,FID,Title,WFState,CDT,FlowStarter,FlowStartRDT,FK_Dept,FK_NY,FlowDaySpan,FlowEmps,FlowEnder,FlowEnderRDT,FlowEndNode,1 as MyNum,PWorkID,PFlowNo,BillNo,ProjNo FROM " + fl.PTable + " WHERE WFState >1 ";
		//    try
		//    {
		//        DBAccess.RunSQLReturnTable(mysql);
		//    }
		//    catch (Exception ex)
		//    {
		//        continue;
		//        try
		//        {
		//            fl.DoCheck();
		//            DBAccess.RunSQLReturnTable(mysql);
		//        }
		//        catch (Exception ex1)
		//        {
		//            err += ex1.Message;
		//            continue;
		//        }
		//    }

		//    if (fls.Count == 1)
		//        break;

		//    sql += mysql;
		//    sql += "\t\n UNION ";
		//}
		//if (sql.Contains("SELECT") == false)
		//    return null;

		//if (fls.Count > 1)
		//    sql = sql.substing(0, sql.Length - 6);

		//if (sql.Length > 20)
		//{
		//    #region 删除 V_FlowData
		//    try
		//    {
		//        DBAccess.RunSQL("DROP VIEW V_FlowData");
		//    }
		//    catch
		//    {
		//        try
		//        {
		//            DBAccess.RunSQL("DROP table V_FlowData");
		//        }
		//        catch
		//        {
		//        }
		//    }
		//    #endregion 删除 V_FlowData

		//    #region 创建视图.
		//    try
		//    {
		//        DBAccess.RunSQL(sql);
		//    }
		//    catch
		//    {
		//    }
		//    #endregion 创建视图.

		//}
		//return null;
	}
	/** 
	 校验流程
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoCheck() throws Exception
	{

			///#region 检查独立表单
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
		String frms = "";
		String err = "";
		for (FrmNode item : fns.ToJavaList())
		{
			if (frms.contains(item.getFK_Frm() + ","))
			{
				continue;
			}
			frms += item.getFK_Frm() + ",";
			try
			{
				MapData md = new MapData(item.getFK_Frm());
				md.RepairMap();
				Entity en = md.getHisEn();
				en.CheckPhysicsTable();
			}
			catch (RuntimeException ex)
			{
				err += "@节点绑定的表单:" + item.getFK_Frm() + ",已经被删除了.异常信息." + ex.getMessage();
			}
		}
		try
		{
			// 设置流程名称.
			DBAccess.RunSQL("UPDATE WF_Node SET FlowName = (SELECT Name FROM WF_Flow WHERE NO=WF_Node.FK_Flow)");

			//删除垃圾,非法数据.
			String sqls = "DELETE FROM Sys_FrmSln where fk_mapdata not in (select no from sys_mapdata)";
			sqls += "@ DELETE FROM WF_Direction WHERE Node=ToNode";
			DBAccess.RunSQLs(sqls);

			//更新计算数据.
			this.setNumOfBill(DBAccess.RunSQLReturnValInt("SELECT count(*) FROM WF_BillTemplate WHERE NodeID IN (select NodeID from WF_Flow WHERE no='" + this.getNo() + "')"));
			this.setNumOfDtl(DBAccess.RunSQLReturnValInt("SELECT count(*) FROM Sys_MapDtl WHERE FK_MapData='ND" + Integer.parseInt(this.getNo()) + "Rpt'"));
			this.DirectUpdate();

			String msg = "@  =======  关于《" + this.getName() + " 》流程检查报告  ============";
			msg += "@信息输出分为三种: 信息  警告  错误. 如果遇到输出的错误，则必须要去修改或者设置.";
			msg += "@流程检查目前还不能覆盖100%的错误,需要手工的运行一次才能确保流程设计的正确性.";

			Nodes nds = new Nodes(this.getNo());

			//单据模版.
			BillTemplates bks = new BillTemplates(this.getNo());

			//条件集合.
			Conds conds = new Conds(this.getNo());


				///#region 对节点进行检查
			//节点表单字段数据类型检查--begin---------
			msg += CheckFormFields();
			//表单字段数据类型检查-------End-----

			for (Node nd : nds.ToJavaList())
			{
				//设置它的位置类型.
				nd.SetValByKey(NodeAttr.NodePosType, nd.GetHisNodePosType().getValue());

				msg += "@信息: -------- 开始检查节点ID:(" + nd.getNodeID() + ")名称:(" + nd.getName() + ")信息 -------------";


					///#region 修复数节点表单数据库.
				msg += "@信息:开始补充&修复节点必要的字段";
				try
				{
					nd.RepareMap();
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@修复节点表必要字段时出现错误:" + nd.getName() + " - " + ex.getMessage());
				}

				msg += "@信息:开始修复节点物理表.";
			//	DBAccess.RunSQL("UPDATE Sys_MapData SET Name='" + nd.getName() + "' WHERE No='ND" + nd.getNodeID() + "'");
				try
				{
					nd.getHisWork().CheckPhysicsTable();
				}
				catch (RuntimeException ex)
				{
					msg += "@检查节点表字段时出现错误:" + "NodeID" + nd.getNodeID() + " Table:" + nd.getHisWork().getEnMap().getPhysicsTable() + " Name:" + nd.getName() + " , 节点类型NodeWorkTypeText=" + nd.getNodeWorkTypeText() + "出现错误.@err=" + ex.getMessage();
				}

				// 从表检查。
				MapDtls dtls = new BP.Sys.MapDtls("ND" + nd.getNodeID());
				for (MapDtl dtl : dtls.ToJavaList())
				{
					msg += "@检查明细表:" + dtl.getName();
					try
					{
						dtl.getHisGEDtl().CheckPhysicsTable();
					}
					catch (RuntimeException ex)
					{
						msg += "@检查明细表时间出现错误" + ex.getMessage();
					}
				}

				MapAttrs mattrs = new MapAttrs("ND" + nd.getNodeID());

				msg += "@信息:开始对节点的访问规则进行检查.";

				switch (nd.getHisDeliveryWay())
				{
					case ByStation:
						if (nd.getNodeStations().size() == 0)
						{
							msg += "@错误:您设置了该节点的访问规则是按岗位，但是您没有为节点绑定岗位。";
						}
						break;
					case ByDept:
						if (nd.getNodeDepts().size() == 0)
						{
							msg += "@错误:您设置了该节点的访问规则是按部门，但是您没有为节点绑定部门。";
						}
						break;
					case ByBindEmp:
						if (nd.getNodeEmps().size() == 0)
						{
							msg += "@错误:您设置了该节点的访问规则是按人员，但是您没有为节点绑定人员。";
						}
						break;
					case BySpecNodeEmp: //按指定的岗位计算.
					case BySpecNodeEmpStation: //按指定的岗位计算.
						if (nd.getDeliveryParas().trim().length() == 0)
						{
							msg += "@错误:您设置了该节点的访问规则是按指定的岗位计算，但是您没有设置节点编号.</font>";
						}
						else
						{
							if (DataType.IsNumStr(nd.getDeliveryParas()) == false)
							{
								msg += "@错误:您没有设置指定岗位的节点编号，目前设置的为{" + nd.getDeliveryParas() + "}";
							}
						}
						break;
					case ByDeptAndStation: //按部门与岗位的交集计算.
						String mysql = "";
						//added by liuxc,2015.6.30.
						//区别集成与BPM模式
						if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne)
						{
							mysql = "SELECT No FROM Port_Emp WHERE No IN (SELECT No FK_Emp FROM Port_Emp WHERE FK_Dept IN ( SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + nd.getNodeID() + "))AND No IN (SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " WHERE FK_Station IN ( SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + nd.getNodeID() + " )) ORDER BY No ";
						}
						else
						{
							mysql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept" + "             AND wnd.FK_Node = " + nd.getNodeID() + "        INNER JOIN WF_NodeStation wns" + "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node =" + nd.getNodeID() + " ORDER BY" + "        pdes.FK_Emp";
						}

						DataTable mydt = DBAccess.RunSQLReturnTable(mysql);
						if (mydt.Rows.size() == 0)
						{
							msg += "@错误:按照岗位与部门的交集计算错误，没有人员集合{" + mysql + "}";
						}
						break;
					case BySQL:
					case BySQLAsSubThreadEmpsAndData:
						if (nd.getDeliveryParas().trim().length() == 0)
						{
							msg += "@错误:您设置了该节点的访问规则是按SQL查询，但是您没有在节点属性里设置查询sql，此sql的要求是查询必须包含No,Name两个列，sql表达式里支持@+字段变量，详细参考开发手册.";
						}
						else
						{
							try
							{
								String sql = nd.getDeliveryParas();
								for (MapAttr item : mattrs.ToJavaList())
								{
									if (item.getIsNum())
									{
										sql = sql.replace("@" + item.getKeyOfEn(), "0");
									}
									else
									{
										sql = sql.replace("@" + item.getKeyOfEn(), "'0'");
									}
								}

								sql = sql.replace("WebUser.No", "'ss'");
								sql = sql.replace("@WebUser.Name", "'ss'");
								sql = sql.replace("@WebUser.FK_Dept", "'ss'");
								sql = sql.replace("@WebUser.FK_DeptName", "'ss'");

								sql = sql.replace("''''", "''"); //出现双引号的问题.

								if (sql.contains("@"))
								{
									throw new RuntimeException("您编写的sql变量填写不正确，实际执行中，没有被完全替换下来" + sql);
								}

								DataTable testDB = null;
								try
								{
									testDB = DBAccess.RunSQLReturnTable(sql);
								}
								catch (RuntimeException ex)
								{
									msg += "@错误:您设置了该节点的访问规则是按SQL查询,执行此语句错误." + ex.getMessage();
								}

								if (testDB.Columns.contains("no") == false || testDB.Columns.contains("name") == false)
								{
									msg += "@错误:您设置了该节点的访问规则是按SQL查询，设置的sql不符合规则，此sql的要求是查询必须包含No,Name两个列，sql表达式里支持@+字段变量，详细参考开发手册.";
								}
							}
							catch (RuntimeException ex)
							{
								msg += ex.getMessage();
							}
						}
						break;
					case ByPreviousNodeFormEmpsField:
						//去rpt表中，查询是否有这个字段
						String str = (nd.getNodeID() + "").substring(0, (nd.getNodeID()+"").length() - 2);
						MapAttrs rptAttrs = new BP.Sys.MapAttrs();
						rptAttrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + str + "Rpt", MapAttrAttr.KeyOfEn);
						if (rptAttrs.Contains(BP.Sys.MapAttrAttr.KeyOfEn, nd.getDeliveryParas()) == false)
						{
							//检查节点字段是否有FK_Emp字段
							msg += "@错误:您设置了该节点的访问规则是[06.按上一节点表单指定的字段值作为本步骤的接受人]，但是您没有在节点属性的[访问规则设置内容]里设置指定的表单字段，详细参考开发手册.";
						}
						//if (mattrs.Contains(BP.Sys.MapAttrAttr.KeyOfEn, "FK_Emp") == false)
						//{
						//    /*检查节点字段是否有FK_Emp字段*/
						//    msg += "@错误:您设置了该节点的访问规则是按指定节点表单人员，但是您没有在节点表单中增加FK_Emp字段，详细参考开发手册 .";
						//}
						break;
					case BySelected: // 由上一步发送人员选择 
						if (nd.getIsStartNode())
						{
							msg += "@错误:开始节点不能设置指定的选择人员访问规则。";
							break;
						}
						break;
					case ByPreviousNodeEmp: // 由上一步发送人员选择 
						if (nd.getIsStartNode())
						{
							msg += "@错误:节点访问规则设置错误:开始节点，不允许设置与上一节点的工作人员相同.";
							break;
						}
						break;
					default:
						break;
				}
				msg += "@对节点的访问规则进行检查完成....";
					///#region 检查节点完成条件，方向条件的定义.
				//设置它没有流程完成条件.
				nd.setIsCCFlow(false);

				if (conds.size() != 0)
				{
					msg += "@信息:开始检查(" + nd.getName() + ")方向条件:";
					for (Cond cond : conds.ToJavaList())
					{
						if (cond.getFK_Node() == nd.getNodeID() && cond.getHisCondType() == CondType.Flow)
						{
							nd.setIsCCFlow(true);
							nd.Update();
						}

						Node ndOfCond = new Node();
						ndOfCond.setNodeID(ndOfCond.getNodeID());
						if (ndOfCond.RetrieveFromDBSources() == 0)
						{
							continue;
						}

						try
						{
							if (cond.getAttrKey().length() < 2)
							{
								continue;
							}
							if (ndOfCond.getHisWork().getEnMap().getAttrs().Contains(cond.getAttrKey()) == false)
							{
								throw new RuntimeException("@错误:属性:" + cond.getAttrKey() + " , " + cond.getAttrName() + " 不存在。");
							}
						}
						catch (RuntimeException ex)
						{
							msg += "@错误:" + ex.getMessage();
							ndOfCond.Delete();
						}
						msg += cond.getAttrKey() + cond.getAttrName() + cond.getOperatorValue() + "、";
					}
					msg += "@(" + nd.getName() + ")方向条件检查完成.....";
				}
			}
			
			
			  //#region 执行一次保存. @于庆海翻译.  增加了此部分.
							NodeExts nes = new NodeExts();
							nes.Retrieve(NodeAttr.FK_Flow, this.getNo());
							for (NodeExt item : nes.ToJavaList())
							{
								item.Update(); // 调用里面的业务逻辑执行检查.
							}
			
			
			msg += "@流程的基础信息: ------ ";
			msg += "@编号:  " + this.getNo() + " 名称:" + this.getName() + " , 存储表:" + this.getPTable();

			msg += "@信息:开始检查节点流程报表.";
			this.DoCheck_CheckRpt(this.getHisNodes());
			msg += "@信息:开始检查节点的焦点字段";

			//获得gerpt字段.
			GERpt rpt = this.getHisGERpt();
			for (Node nd : nds.ToJavaList())
			{
				if (nd.getFocusField().trim().equals(""))
				{
					Work wk = nd.getHisWork();
					String attrKey = "";
					for (Attr attr : wk.getEnMap().getAttrs())
					{
						if (attr.getUIVisible() == true && attr.getUIIsDoc() && attr.getUIIsReadonly() == false)
						{
							attrKey = attr.getDesc() + ":@" + attr.getKey();
						}
					}
					if (attrKey.equals(""))
					{
						msg += "@警告:节点ID:" + nd.getNodeID() + " 名称:" + nd.getName() + "属性里没有设置焦点字段，会导致信息写入轨迹表空白，为了能够保证流程轨迹是可读的请设置焦点字段.";
					}
					else
					{
						msg += "@信息:节点ID:" + nd.getNodeID() + " 名称:" + nd.getName() + "属性里没有设置焦点字段，会导致信息写入轨迹表空白，为了能够保证流程轨迹是可读的系统自动设置了焦点字段为" + attrKey + ".";
						nd.setFocusField(attrKey);
						nd.DirectUpdate();
					}
					continue;
				}

				Object tempVar = nd.getFocusField();
				String strs = (String)((tempVar instanceof String) ? tempVar : null);
				strs = Glo.DealExp(strs, rpt, "err");
				if (strs.contains("@") == true)
				{
					msg += "@错误:焦点字段（" + nd.getFocusField() + "）在节点(step:" + nd.getStep() + " 名称:" + nd.getName() + ")属性里的设置已无效，表单里不存在该字段.";
				}
				else
				{
					msg += "@提示:节点的(" + nd.getNodeID() + "," + nd.getName() + ")焦点字段（" + nd.getFocusField() + "）设置完整检查通过.";
				}

				if (this.getIsMD5())
				{
					if (nd.getHisWork().getEnMap().getAttrs().Contains(WorkAttr.MD5) == false)
					{
						nd.RepareMap();
					}
				}
			}
			msg += "@信息:检查节点的焦点字段完成.";
				///#region 检查质量考核点.
			msg += "@信息:开始检查质量考核点";
			for (Node nd : nds.ToJavaList())
			{
				if (nd.getIsEval())
				{
					//如果是质量考核点，检查节点表单是否具别质量考核的特别字段？
					String sql = "SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='ND" + nd.getNodeID() + "' AND KeyOfEn IN ('EvalEmpNo','EvalEmpName','EvalEmpCent')";
					if (DBAccess.RunSQLReturnValInt(sql) != 3)
					{
						msg += "@信息:您设置了节点(" + nd.getNodeID() + "," + nd.getName() + ")为质量考核节点，但是您没有在该节点表单中设置必要的节点考核字段.";
					}
				}
			}
			msg += "@检查质量考核点完成.";

				///#endregion

			msg += "@流程报表检查完成...";

			// 检查流程.
			Node.CheckFlow(this);

			//生成 V001 视图. del by stone 2016.03.27.
			// CheckRptViewDel(nds);
			return msg;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@检查流程错误:" + ex.getMessage() + " @" + ex.getStackTrace());
		}
	}
	/** 
	 节点表单字段数据类型检查，名字相同的字段出现类型不同的处理方法：依照不同于NDxxRpt表中同名字段类型为基准
	 
	 @return 检查结果
	 * @throws Exception 
	*/
	private String CheckFormFields() throws Exception
	{
		StringBuilder errorAppend = new StringBuilder();
		errorAppend.append("@信息: -------- 流程节点表单的字段类型检查: ------ ");
		try
		{
			Nodes nds = new Nodes(this.getNo());
			String fk_mapdatas = "'ND" + Integer.parseInt(this.getNo()) + "Rpt'";
			for (Node nd : nds.ToJavaList())
			{
				fk_mapdatas += ",'ND" + nd.getNodeID() + "'";
			}

			//筛选出类型不同的字段
			String checkSQL = "SELECT   AA.KEYOFEN, COUNT(*) AS MYNUM FROM (" + "  SELECT A.KEYOFEN,  MYDATATYPE,  COUNT(*) AS MYNUM" + "  FROM SYS_MAPATTR A WHERE FK_MAPDATA IN (" + fk_mapdatas + ") GROUP BY KEYOFEN, MYDATATYPE" + ")  AA GROUP BY  AA.KEYOFEN HAVING COUNT(*) > 1";
			DataTable dt_Fields = DBAccess.RunSQLReturnTable(checkSQL);
			for (DataRow row : dt_Fields.Rows)
			{
				String keyOfEn = row.getValue("KEYOFEN").toString();
				String myNum = row.getValue("MYNUM").toString();
				int iMyNum = 0;
				
				//cn.jflow.common.tangible.RefObject<Integer> tempRef_iMyNum = new cn.jflow.common.tangible.RefObject<Integer>(iMyNum);
				//cn.jflow.common.tangible.TryParseHelper.tryParseInt(myNum, tempRef_iMyNum);
				
				iMyNum =0;// tempRef_iMyNum.argValue;

				//存在2种以上数据类型，有手动进行调整
				if (iMyNum > 2)
				{
					errorAppend.append("@错误：字段名" + keyOfEn + "在此流程表(" + fk_mapdatas + ")中存在2种以上数据类型(如：int，float,varchar,datetime)，请手动修改。");
					return errorAppend.toString();
				}

				//存在2种数据类型，以不同于NDxxRpt字段类型为主
				MapAttr baseMapAttr = new MapAttr();
				MapAttr rptMapAttr = new MapAttr("ND" + Integer.parseInt(this.getNo()) + "Rpt", keyOfEn);

				//Rpt表中不存在此字段
				if (rptMapAttr == null || rptMapAttr.getMyPK().equals(""))
				{
					this.DoCheck_CheckRpt(this.getHisNodes());
					rptMapAttr = new MapAttr("ND" + Integer.parseInt(this.getNo()) + "Rpt", keyOfEn);
					this.getHisGERpt().CheckPhysicsTable();
				}

				//Rpt表中不存在此字段,直接结束
				if (rptMapAttr == null || rptMapAttr.getMyPK().equals(""))
				{
					continue;
				}

				for (Node nd : nds.ToJavaList())
				{
					MapAttr ndMapAttr = new MapAttr("ND" + nd.getNodeID(), keyOfEn);
					if (ndMapAttr == null || ndMapAttr.getMyPK().equals(""))
					{
						continue;
					}

					//找出与NDxxRpt表中字段数据类型不同的表单
					if (rptMapAttr.getMyDataType() != ndMapAttr.getMyDataType())
					{
						baseMapAttr = ndMapAttr;
						break;
					}
				}
				errorAppend.append("@基础表" + baseMapAttr.getFK_MapData() + "，字段" + keyOfEn + "数据类型为：" + baseMapAttr.getMyDataTypeStr());
				//根据基础属性类修改数据类型不同的表单
				for (Node nd : nds.ToJavaList())
				{
					MapAttr ndMapAttr = new MapAttr("ND" + nd.getNodeID(), keyOfEn);
					//不包含此字段的进行返回,类型相同的进行返回
					if (ndMapAttr == null || ndMapAttr.getMyPK().equals("") || baseMapAttr.getMyPK() == ndMapAttr.getMyPK() || baseMapAttr.getMyDataType() == ndMapAttr.getMyDataType())
					{
						continue;
					}
					ndMapAttr.setName(baseMapAttr.getName());
					ndMapAttr.setMyDataType(baseMapAttr.getMyDataType());
					ndMapAttr.setUIWidth(baseMapAttr.getUIWidth());
					ndMapAttr.setUIHeight(baseMapAttr.getUIHeight());
					ndMapAttr.setMinLen(baseMapAttr.getMinLen());
					ndMapAttr.setMaxLen(baseMapAttr.getMaxLen());
					if (ndMapAttr.Update() > 0)
					{
						errorAppend.append("@修改了" + "ND" + nd.getNodeID() + " 表，字段" + keyOfEn + "修改为：" + baseMapAttr.getMyDataTypeStr());
					}
					else
					{
						errorAppend.append("@错误:修改" + "ND" + nd.getNodeID() + " 表，字段" + keyOfEn + "修改为：" + baseMapAttr.getMyDataTypeStr() + "失败。");
					}
				}
				//修改NDxxRpt
				rptMapAttr.setName(baseMapAttr.getName());
				rptMapAttr.setMyDataType(baseMapAttr.getMyDataType());
				rptMapAttr.setUIWidth(baseMapAttr.getUIWidth());
				rptMapAttr.setUIHeight(baseMapAttr.getUIHeight());
				rptMapAttr.setMinLen(baseMapAttr.getMinLen());
				rptMapAttr.setMaxLen(baseMapAttr.getMaxLen());
				if (rptMapAttr.Update() > 0)
				{
					errorAppend.append("@修改了" + "ND" + Integer.parseInt(this.getNo()) + "Rpt 表，字段" + keyOfEn + "修改为：" + baseMapAttr.getMyDataTypeStr());
				}
				else
				{
					errorAppend.append("@错误:修改" + "ND" + Integer.parseInt(this.getNo()) + "Rpt 表，字段" + keyOfEn + "修改为：" + baseMapAttr.getMyDataTypeStr() + "失败。");
				}
			}
		}
		catch (RuntimeException ex)
		{
			errorAppend.append("@错误:" + ex.getMessage());
		}
		return errorAppend.toString();
	}
		///#region 产生数据模板。
	private static String PathFlowDesc;
	static
	{
		PathFlowDesc = SystemConfig.getPathOfDataUser() + "FlowDesc\\";
	}
	/** 
	 生成流程模板
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GenerFlowXmlTemplete() throws Exception
	{
		String name = this.getName();
		name = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(name);

		String path = this.getNo() + "." + name;
		path = PathFlowDesc + path + "\\";
		this.DoExpFlowXmlTemplete(path);

		// name = path + name + "." + this.Ver.replace(":", "_") + ".xml";

		name = path + name + ".xml";
		return name;
	}
	/** 
	 生成流程模板
	 @param path
	 @return 
	 * @throws Exception 
	*/
	public final DataSet DoExpFlowXmlTemplete(String path) throws Exception
	{
		File file = new File(path);
		if (!file.exists())
		{
			try
			{
				file.mkdirs();
			} catch (Exception e)
			{
				
				e.printStackTrace();
			}
		}
		
		DataSet ds = GetFlow(path);
		if (ds != null)
		{
			//String filePath = path + this.getName() + "_" + this.getVer().replace(":", "_") + ".xml";
			String filePath = path + this.getName() + ".xml";
			try
			{
				ds.WriteXml(filePath, XmlWriteMode.WriteSchema, ds);
			} catch (Exception e)
			{
				
				e.printStackTrace();
			}
		}
		return ds; 
	}

	//xml文件是否正在操作中
	private static boolean isXmlLocked;
	/** 
	 备份当前流程到用户xml文件
	 用户每次保存时调用
	 捕获异常写入日志,备份失败不影响正常保存
	*/
	public final void WriteToXml(DataSet ds)
	{
		String path = PathFlowDesc + this.getNo() + "." + this.getName() + "/";
		String xmlName = path + "Flow" + ".xml";
		File file = new File(path);
		File filexmlName = new File(xmlName);
		if (!StringHelper.isNullOrEmpty(path))
		{
			if (!file.exists())
			{
				try
				{
					file.mkdirs();
				} catch (Exception e)
				{
					
					e.printStackTrace();
				}
			} else if (filexmlName.exists())
			{
				long modifiedTime = filexmlName.lastModified();
				Date date = new Date(modifiedTime);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String dd = sdf.format(date);
				String xmlNameOld = path + "Flow" + dd + ".xml";
				File filexmlNameOld = new File(xmlNameOld);
				if (filexmlNameOld.exists())
				{
					filexmlNameOld.delete();
				}
				FileAccess.Move(filexmlName, xmlNameOld);
				// File.move(xmlName, xmlNameOld);
			}
		}
		
		try
		{
			if (!StringHelper.isNullOrEmpty(xmlName) && null != ds)
			{
				ds.WriteXml(xmlName, XmlWriteMode.WriteSchema, ds);
			}
		} catch (RuntimeException e)
		{
			isXmlLocked = false;
			BP.DA.Log.DefaultLogWriteLineError("流程模板文件备份错误:" + e.getMessage());
		} catch (Exception e)
		{
			
			e.printStackTrace();
		}
	}
	 
	 public DataSet GetFlow(String path) throws Exception
     {
         // 把所有的数据都存储在这里。
         DataSet ds = new DataSet();

         // 流程信息。
         String sql = "SELECT * FROM WF_Flow WHERE No='" + this.getNo() + "'";

         Flow fl = new Flow(this.getNo());
         DataTable dtFlow = fl.ToDataTableField("WF_Flow");
         dtFlow.TableName = "WF_Flow";
         ds.Tables.add(dtFlow);

         // 节点信息
         Nodes nds = new Nodes(this.getNo());
         DataTable dtNodes = nds.ToDataTableField("WF_Node");
         ds.Tables.add(dtNodes);

         // 单据模版. 
         BillTemplates tmps = new BillTemplates(this.getNo());
         String pks = "";
         for (BillTemplate tmp : tmps.ToJavaList())
         {
             try
             {
                 if (path != null)
                	 FileAccess.Copy(SystemConfig.getPathOfDataUser() + "CyclostyleFile/" + tmp.getNo() + ".rtf", path + "/" + tmp.getNo() + ".rtf");
             }catch(java.lang.Exception e )
             {
                 pks += "@" + tmp.getPKVal();
                 tmp.Delete();
             }
         }
         tmps.remove(pks);
         ds.Tables.add(tmps.ToDataTableField("WF_BillTemplate"));



         String sqlin = "SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo() + "'";

         // 条件信息
         Conds cds = new Conds(this.getNo());
         ds.Tables.add(cds.ToDataTableField("WF_Cond") );


         //// 转向规则.
         //sql = "SELECT * FROM WF_TurnTo WHERE FK_Flow='" + this.No + "'";
         //dt = DBAccess.RunSQLReturnTable(sql);
         //dt.TableName = "WF_TurnTo";
         //ds.Tables.Add(dt);

         // 节点与表单绑定.
         FrmNodes fns = new FrmNodes();
         fns.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
         ds.Tables.add(fns.ToDataTableField("WF_FrmNode"));


         // 表单方案.
         FrmFields ffs = new FrmFields();
         ffs.Retrieve(FrmFieldAttr.FK_Flow, this.getNo());
         ds.Tables.add(ffs.ToDataTableField("Sys_FrmSln"));

         // 方向
         Directions dirs = new Directions();
         dirs.Retrieve(DirectionAttr.FK_Flow, this.getNo());
         ds.Tables.add(dirs.ToDataTableField("WF_Direction"));

         // 流程标签.
         LabNotes labs = new LabNotes(this.getNo());
         ds.Tables.add(labs.ToDataTableField("WF_LabNote"));

         // 可退回的节点。
         NodeReturns nrs = new NodeReturns();
         nrs.RetrieveInSQL(NodeReturnAttr.FK_Node, sqlin);
         ds.Tables.add(nrs.ToDataTableField("WF_NodeReturn"));

          

         // 工具栏。
         NodeToolbars tools = new NodeToolbars();
         tools.RetrieveInSQL(NodeToolbarAttr.FK_Node, sqlin);
         ds.Tables.add(tools.ToDataTableField("WF_NodeToolbar"));

          
         // 节点与部门。
         NodeDepts ndepts = new NodeDepts();
         ndepts.RetrieveInSQL(NodeDeptAttr.FK_Node, sqlin);
         ds.Tables.add(ndepts.ToDataTableField("WF_NodeDept"));
          

         // 节点与岗位权限。
         NodeStations nss = new NodeStations();
         nss.RetrieveInSQL(NodeStationAttr.FK_Node, sqlin);
         ds.Tables.add(nss.ToDataTableField("WF_NodeStation"));
          
         // 节点与人员。
         NodeEmps nes = new NodeEmps();
         nes.RetrieveInSQL(NodeEmpAttr.FK_Node, sqlin);
         ds.Tables.add(nes.ToDataTableField("WF_NodeEmp"));
           

         // 抄送人员。
         CCEmps ces = new CCEmps();
         ces.RetrieveInSQL(CCEmpAttr.FK_Node, sqlin);
         ds.Tables.add(ces.ToDataTableField("WF_CCEmp"));
          

         // 抄送部门。
         CCDepts cdds = new CCDepts();
         cdds.RetrieveInSQL(CCDeptAttr.FK_Node, sqlin);
         ds.Tables.add(cdds.ToDataTableField("WF_CCDept"));
       

         // 延续子流程。
         NodeYGFlows fls = new NodeYGFlows();
         fls.RetrieveInSQL(CCDeptAttr.FK_Node, sqlin);
         ds.Tables.add(fls.ToDataTableField("WF_NodeSubFlow"));

         //表单信息，包含从表.
         sql = "SELECT No FROM Sys_MapData WHERE " + Glo.MapDataLikeKey(this.getNo(), "No");
         MapDatas mds = new MapDatas();
         mds.RetrieveInSQL(MapDataAttr.No, sql);
         ds.Tables.add(mds.ToDataTableField("Sys_MapData"));
          

         // Sys_MapAttr.
         sql = "SELECT MyPK FROM Sys_MapAttr WHERE  " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         MapAttrs attrs = new MapAttrs();
         attrs.RetrieveInSQL(MapAttrAttr.MyPK, sql);
         ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));
          

         // Sys_EnumMain
         sql = "SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + ")";
         SysEnumMains ses = new SysEnumMains();
         ses.RetrieveInSQL(SysEnumMainAttr.No, sql);
         ds.Tables.add(ses.ToDataTableField("Sys_EnumMain"));
          

         // Sys_Enum
         sql = "SELECT MyPK FROM Sys_Enum WHERE EnumKey IN ( SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ) )";
         SysEnums sesDtl = new SysEnums();
         sesDtl.RetrieveInSQL("MyPK", sql);
         ds.Tables.add(sesDtl.ToDataTableField("Sys_Enum"));
           

         // Sys_MapDtl
         sql = "SELECT No FROM Sys_MapDtl WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         MapDtls mdtls = new MapDtls();
         mdtls.RetrieveInSQL(sql);
         ds.Tables.add(mdtls.ToDataTableField("Sys_MapDtl"));


         // Sys_MapExt
         sql = "SELECT MyPK FROM Sys_MapExt WHERE  " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         MapExts mexts = new MapExts();
         mexts.RetrieveInSQL(sql);
         ds.Tables.add(mexts.ToDataTableField("Sys_MapExt"));

          

         // Sys_GroupField
         sql = "SELECT OID FROM Sys_GroupField WHERE   " + Glo.MapDataLikeKey(this.getNo(), "FrmID"); // +" " + Glo.MapDataLikeKey(this.No, "EnName");
         GroupFields gfs = new GroupFields();
         gfs.RetrieveInSQL(sql);
         ds.Tables.add(gfs.ToDataTableField("Sys_GroupField"));
          

         // Sys_MapFrame
         sql = "SELECT MyPK FROM Sys_MapFrame WHERE" + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         MapFrames mfs = new MapFrames();
         mfs.RetrieveInSQL("MyPK",sql);
         ds.Tables.add(mfs.ToDataTableField("Sys_MapFrame"));

        

         // Sys_FrmLine.
         sql = "SELECT MyPK FROM Sys_FrmLine WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         FrmLines frmls = new FrmLines();
         frmls.RetrieveInSQL(sql);
         ds.Tables.add(frmls.ToDataTableField("Sys_FrmLine"));
        

         // Sys_FrmLab.
         sql = "SELECT MyPK FROM Sys_FrmLab WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         FrmLabs frmlabs = new FrmLabs();
         frmlabs.RetrieveInSQL(sql);
         ds.Tables.add(frmlabs.ToDataTableField("Sys_FrmLab"));

         

         // Sys_FrmEle.
         sql = "SELECT MyPK FROM Sys_FrmEle WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");

         FrmEles frmEles = new FrmEles();
         frmEles.RetrieveInSQL(sql);
         ds.Tables.add(frmEles.ToDataTableField("Sys_FrmEle"));

         // Sys_FrmLink.
         sql = "SELECT MyPK FROM Sys_FrmLink WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         FrmLinks frmLinks = new FrmLinks();
         frmLinks.RetrieveInSQL(sql);
         ds.Tables.add(frmLinks.ToDataTableField("Sys_FrmLink"));

         // Sys_FrmRB.
         sql = "SELECT MyPK FROM Sys_FrmRB WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         FrmRBs frmRBs = new FrmRBs();
         frmRBs.RetrieveInSQL(sql);
         ds.Tables.add(frmRBs.ToDataTableField("Sys_FrmRB"));


         // Sys_FrmImgAth.
         sql = "SELECT MyPK FROM Sys_FrmImgAth WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         FrmImgAths frmIs = new FrmImgAths();
         frmIs.RetrieveInSQL(sql);
         ds.Tables.add(frmIs.ToDataTableField("Sys_FrmImgAth"));

         // Sys_FrmImg.
         sql = "SELECT MyPK FROM Sys_FrmImg WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         FrmImgs frmImgs = new FrmImgs();
         frmImgs.RetrieveInSQL(sql);
         ds.Tables.add(frmImgs.ToDataTableField("Sys_FrmImg"));


         // Sys_FrmAttachment.
         sql = "SELECT MyPK FROM Sys_FrmAttachment WHERE FK_Node=0 AND " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         FrmAttachments frmaths = new FrmAttachments();
         frmaths.RetrieveInSQL(sql);
         ds.Tables.add(frmaths.ToDataTableField("Sys_FrmAttachment"));

         // Sys_FrmEvent.
         sql = "SELECT MyPK FROM Sys_FrmEvent WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
         FrmEvents frmevens = new FrmEvents();
         frmevens.RetrieveInSQL(sql);
         ds.Tables.add(frmevens.ToDataTableField("Sys_FrmEvent"));
         return ds;
     }

	public final DataSet GetFlow2017(String path) throws Exception
	{
		// 把所有的数据都存储在这里。
		DataSet ds = new DataSet();

		// 流程信息。
		String sql = "SELECT * FROM WF_Flow WHERE No='" + this.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Flow";
		ds.Tables.add(dt);

		// 节点信息
		sql = "SELECT * FROM WF_Node WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Node";
		ds.Tables.add(dt);

		// 单据模版. 
		BillTemplates tmps = new BillTemplates(this.getNo());
		String pks = "";
		for (BillTemplate tmp : tmps.ToJavaList())
		{
			try
			{
				if (path != null)
				{
					// System.IO.File.Copy(SystemConfig.getPathOfDataUser()
					// + "\\CyclostyleFile\\" + tmp.getNo() + ".rtf", path
					// + "\\" + tmp.getNo() + ".rtf", true);
					FileAccess.Copy(SystemConfig.getPathOfDataUser() + "CyclostyleFile/" + tmp.getNo() + ".rtf", path + "/" + tmp.getNo() + ".rtf");
				}
			}
			catch (java.lang.Exception e)
			{
				pks += "@" + tmp.getPKVal();
				tmp.Delete();
			}
		}
		tmps.remove(pks);
		ds.Tables.add(tmps.ToDataTableField("WF_BillTemplate"));

		String sqlin = "SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo() + "'";
		// 独立表单树
		sql = "SELECT * FROM WF_FlowFormTree WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_FlowFormTree";
		ds.Tables.add(dt);

		//// 独立表单
		//sql = "SELECT * FROM WF_FlowForm WHERE FK_Flow='" + this.getNo() + "'";
		//dt = DBAccess.RunSQLReturnTable(sql);
		//dt.TableName = "WF_FlowForm";
		//ds.Tables.Add(dt);

		//// 节点表单权限
		//sql = "SELECT * FROM WF_NodeForm WHERE FK_Node IN (" + sqlin + ")";
		//dt = DBAccess.RunSQLReturnTable(sql);
		//dt.TableName = "WF_NodeForm";
		//ds.Tables.Add(dt);

		// 条件信息
		sql = "SELECT * FROM WF_Cond WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Cond";
		ds.Tables.add(dt);

		// 转向规则.
		sql = "SELECT * FROM WF_TurnTo WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_TurnTo";
		ds.Tables.add(dt);

		// 节点与表单绑定.
		sql = "SELECT * FROM WF_FrmNode WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_FrmNode";
		ds.Tables.add(dt);

		// 表单方案.
		sql = "SELECT * FROM Sys_FrmSln WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmSln";
		ds.Tables.add(dt);

		// 方向
		sql = "SELECT * FROM WF_Direction WHERE Node IN (" + sqlin + ") OR ToNode In (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Direction";
		ds.Tables.add(dt);

		//// 应用设置 FAppSet
		//sql = "SELECT * FROM WF_FAppSet WHERE FK_Flow='" + this.getNo() + "'";
		//dt = DBAccess.RunSQLReturnTable(sql);
		//dt.TableName = "WF_FAppSet";
		//ds.Tables.Add(dt);

		// 流程标签.
		LabNotes labs = new LabNotes(this.getNo());
		ds.Tables.add(labs.ToDataTableField("WF_LabNote"));

	 

		// 可退回的节点。
		sql = "SELECT * FROM WF_NodeReturn WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeReturn";
		ds.Tables.add(dt);

		// 工具栏。
		sql = "SELECT * FROM WF_NodeToolbar WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeToolbar";
		ds.Tables.add(dt);

		// 节点与部门。
		sql = "SELECT * FROM WF_NodeDept WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeDept";
		ds.Tables.add(dt);


		// 节点与岗位权限。
		sql = "SELECT * FROM WF_NodeStation WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeStation";
		ds.Tables.add(dt);

		// 节点与人员。
		sql = "SELECT * FROM WF_NodeEmp WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeEmp";
		ds.Tables.add(dt);

		// 抄送人员。
		sql = "SELECT * FROM WF_CCEmp WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCEmp";
		ds.Tables.add(dt);

		// 抄送部门。
		sql = "SELECT * FROM WF_CCDept WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCDept";
		ds.Tables.add(dt);

		// 抄送部门。
		sql = "SELECT * FROM WF_CCStation WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCStation";
		ds.Tables.add(dt);
		
		  // 延续子流程。
        sql = "SELECT * FROM WF_NodeSubFlow WHERE FK_Node IN (" + sqlin + ")";
        dt = DBAccess.RunSQLReturnTable(sql);
        dt.TableName = "WF_NodeSubFlow";
        ds.Tables.add(dt);

		//// 流程报表。
		//WFRpts rpts = new WFRpts(this.getNo());
		//// rpts.SaveToXml(path + "WFRpts.xml");
		//ds.Tables.Add(rpts.ToDataTableField("WF_Rpt"));

		//// 流程报表属性
		//RptAttrs rptAttrs = new RptAttrs();
		//rptAttrs.RetrieveAll();
		//ds.Tables.Add(rptAttrs.ToDataTableField("RptAttrs"));

		//// 流程报表访问权限。
		//RptStations rptStations = new RptStations(this.getNo());
		//rptStations.RetrieveAll();
		////  rptStations.SaveToXml(path + "RptStations.xml");
		//ds.Tables.Add(rptStations.ToDataTableField("RptStations"));

		//// 流程报表人员访问权限。
		//RptEmps rptEmps = new RptEmps(this.getNo());
		//rptEmps.RetrieveAll();

		// rptEmps.SaveToXml(path + "RptEmps.xml");
		// ds.Tables.Add(rptEmps.ToDataTableField("RptEmps"));

		int flowID = Integer.parseInt(this.getNo());
		sql = "SELECT * FROM Sys_MapData WHERE " + Glo.MapDataLikeKey(this.getNo(), "No");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		ds.Tables.add(dt);


		// Sys_MapAttr.
		sql = "SELECT * FROM Sys_MapAttr WHERE  " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ORDER BY FK_MapData,Idx";
		//sql = "SELECT * FROM Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + "  ORDER BY FK_MapData,Idx";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		ds.Tables.add(dt);

		// Sys_EnumMain
		//sql = "SELECT * FROM Sys_EnumMain WHERE No IN (SELECT KeyOfEn from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") +")";
		sql = "SELECT * FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_EnumMain";
		ds.Tables.add(dt);

		// Sys_Enum
		sql = "SELECT * FROM Sys_Enum WHERE EnumKey IN ( SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ) )";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_Enum";
		ds.Tables.add(dt);

		// Sys_MapDtl
		sql = "SELECT * FROM Sys_MapDtl WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapDtl";
		ds.Tables.add(dt);

		// Sys_MapExt
		//sql = "SELECT * FROM Sys_MapExt WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		sql = "SELECT * FROM Sys_MapExt WHERE  " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData"); // +Glo.MapDataLikeKey(this.getNo(), "FK_MapData");

		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		// Sys_GroupField
		sql = "SELECT * FROM Sys_GroupField WHERE   " + Glo.MapDataLikeKey(this.getNo(), "FrmID"); // +" " + Glo.MapDataLikeKey(this.getNo(), "EnName");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_GroupField";
		ds.Tables.add(dt);

		// Sys_MapFrame
		sql = "SELECT * FROM Sys_MapFrame WHERE" + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapFrame";
		ds.Tables.add(dt);

		// Sys_MapM2M
		//sql = "SELECT * FROM Sys_MapM2M WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		//dt = DBAccess.RunSQLReturnTable(sql);
		//dt.TableName = "Sys_MapM2M";
		//ds.Tables.add(dt);

		// Sys_FrmLine.
		sql = "SELECT * FROM Sys_FrmLine WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLine";
		ds.Tables.add(dt);

		// Sys_FrmLab.
		sql = "SELECT * FROM Sys_FrmLab WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLab";
		ds.Tables.add(dt);

		// Sys_FrmEle.
		sql = "SELECT * FROM Sys_FrmEle WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmEle";
		ds.Tables.add(dt);

		// Sys_FrmLink.
		sql = "SELECT * FROM Sys_FrmLink WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLink";
		ds.Tables.add(dt);

		// Sys_FrmRB.
		sql = "SELECT * FROM Sys_FrmRB WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmRB";
		ds.Tables.add(dt);

		// Sys_FrmImgAth.
		sql = "SELECT * FROM Sys_FrmImgAth WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmImgAth";
		ds.Tables.add(dt);

		// Sys_FrmImg.
		sql = "SELECT * FROM Sys_FrmImg WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmImg";
		ds.Tables.add(dt);

		// Sys_FrmAttachment.
		sql = "SELECT * FROM Sys_FrmAttachment WHERE FK_Node=0 AND " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmAttachment";
		ds.Tables.add(dt);

		// Sys_FrmEvent.
		sql = "SELECT * FROM Sys_FrmEvent WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmEvent";
		ds.Tables.add(dt);
		return ds;
	}


		///#endregion 产生数据模板。


		///#region 其他公用方法1
	/** 
	 重新设置Rpt表
	 * @throws Exception 
	 
	*/
	public final void CheckRptOfReset() throws Exception
	{
		String fk_mapData = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "'";
		DBAccess.RunSQL(sql);

		sql = "DELETE FROM Sys_MapData WHERE No='" + fk_mapData + "'";
		DBAccess.RunSQL(sql);
		this.DoCheck_CheckRpt(this.getHisNodes());
	}
	/** 
	 重新装载
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoReloadRptData() throws Exception
	{
		this.DoCheck_CheckRpt(this.getHisNodes());

		// 检查报表数据是否丢失。

		if (this.getHisDataStoreModel() != DataStoreModel.ByCCFlow)
		{
			return "@流程" + this.getNo() + this.getName() + "的数据存储非轨迹模式不能重新生成.";
		}

		DBAccess.RunSQL("DELETE FROM " + this.getPTable());

		String sql = "SELECT OID FROM ND" + Integer.parseInt(this.getNo()) + "01 WHERE  OID NOT IN (SELECT OID FROM  " + this.getPTable() + " ) ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		this.CheckRptData(this.getHisNodes(), dt);

		return "@共有:" + dt.Rows.size() + "条(" + this.getName() + ")数据被装载成功。";
	}
	/** 
	 检查与修复报表数据
	 
	 @param nds
	 @param dt
	 * @throws Exception 
	*/
	private String CheckRptData(Nodes nds, DataTable dt) throws Exception
	{
		GERpt rpt = new GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
		String err = "";
		for (DataRow dr : dt.Rows)
		{
			rpt.ResetDefaultVal();
			int oid = Integer.parseInt(dr.getValue(0).toString());
			rpt.SetValByKey("OID", oid);
			Work startWork = null;
			Work endWK = null;
			String flowEmps = "";
			for (Node nd : nds.ToJavaList())
			{
				try
				{
					Work wk = nd.getHisWork();
					wk.setOID(oid);
					if (wk.RetrieveFromDBSources() == 0)
					{
						continue;
					}

					rpt.Copy(wk);
					if (nd.getNodeID() == Integer.parseInt(this.getNo() + "01"))
					{
						startWork = wk;
					}

					try
					{
						if (flowEmps.contains("@" + wk.getRec() + ","))
						{
							continue;
						}

						flowEmps += "@" + wk.getRec() + "," + wk.getRecOfEmp().getName();
					}
					catch (java.lang.Exception e)
					{
					}
					endWK = wk;
				}
				catch (RuntimeException ex)
				{
					err += ex.getMessage();
				}
			}

			if (startWork == null || endWK == null)
			{
				continue;
			}

			rpt.SetValByKey("OID", oid);
			rpt.setFK_NY(startWork.GetValStrByKey("RDT").substring(0, 7));
			rpt.setFK_Dept(startWork.GetValStrByKey("FK_Dept"));
			if (StringHelper.isNullOrEmpty(rpt.getFK_Dept()))
			{
				String fk_dept = DBAccess.RunSQLReturnString("SELECT FK_Dept FROM Port_Emp WHERE No='" + startWork.getRec() + "'");
				rpt.setFK_Dept(fk_dept);

				startWork.SetValByKey("FK_Dept", fk_dept);
				startWork.Update();
			}
			rpt.setTitle(startWork.GetValStrByKey("Title"));
			String wfState = DBAccess.RunSQLReturnStringIsNull("SELECT WFState FROM WF_GenerWorkFlow WHERE WorkID=" + oid, "1");
			rpt.setWFState(WFState.forValue(Integer.parseInt(wfState)));
			rpt.setFlowStarter(startWork.getRec());
			rpt.setFlowStartRDT(startWork.getRDT());
			rpt.setFID(startWork.GetValIntByKey("FID"));
			rpt.setFlowEmps(flowEmps);
			rpt.setFlowEnder(endWK.getRec());
			rpt.setFlowEnderRDT(endWK.getRDT());
			rpt.setFlowEndNode(endWK.getNodeID());
			rpt.setMyNum(1);

			//修复标题字段。
			WorkNode wn = new WorkNode(startWork, this.getHisStartNode());
			rpt.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, startWork));
			try
			{
				/*TimeSpan ts = endWK.getRDT_DateTime() - startWork.getRDT_DateTime();
				rpt.setFlowDaySpan(ts.Days);*/
				int day = (int) ((endWK.getRDT_DateTime().getTime() - startWork.getRDT_DateTime().getTime()) / (1000 * 60 * 60 * 24));
				rpt.setFlowDaySpan(day);
			}
			catch (java.lang.Exception e2)
			{
			}
			rpt.InsertAsOID(rpt.getOID());
		} // 结束循环。
		return err;
	}
	/** 
	 生成明细报表信息
	 
	 @param nds
	 * @throws Exception 
	*/
	private void CheckRptDtl(Nodes nds) throws Exception
	{
		MapDtls dtlsDtl = new MapDtls();
		dtlsDtl.Retrieve(MapDtlAttr.FK_MapData, "ND" + Integer.parseInt(this.getNo()) + "Rpt");
		for (MapDtl dtl : dtlsDtl.ToJavaList())
		{
			dtl.Delete();
		}

		//  dtlsDtl.Delete(MapDtlAttr.FK_MapData, "ND" + int.Parse(this.getNo()) + "Rpt");
		for (Node nd : nds.ToJavaList())
		{
			if (nd.getIsEndNode() == false)
			{
				continue;
			}

			// 取出来从表.
			MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
			if (dtls.size() == 0)
			{
				continue;
			}

			String rpt = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
			int i = 0;
			for (MapDtl dtl : dtls.ToJavaList())
			{
				i++;
				String rptDtlNo = "ND" + Integer.parseInt(this.getNo()) + "RptDtl" + (new Integer(i)).toString();
				MapDtl rtpDtl = new MapDtl();
				rtpDtl.setNo(rptDtlNo);
				if (rtpDtl.RetrieveFromDBSources() == 0)
				{
					rtpDtl.Copy(dtl);
					rtpDtl.setNo(rptDtlNo);
					rtpDtl.setFK_MapData(rpt);
					rtpDtl.setPTable(rptDtlNo);
					rtpDtl.Insert();
				}

				MapAttrs attrsRptDtl = new MapAttrs(rptDtlNo);
				MapAttrs attrs = new MapAttrs(dtl.getNo());
				for (MapAttr attr : attrs.ToJavaList())
				{
					if (attrsRptDtl.Contains(MapAttrAttr.KeyOfEn, attr.getKeyOfEn()) == true)
					{
						continue;
					}

					MapAttr attrN = new MapAttr();
					attrN.Copy(attr);
					attrN.setFK_MapData(rptDtlNo);
					String keyOfEn = attr.getKeyOfEn();

//					switch (attr.KeyOfEn)
					if (keyOfEn.equals("FK_NY")) {
						attrN.setUIVisible(true);
						attrN.setIdx(100);
						attrN.setUIWidth(60);
					} else if (keyOfEn.equals("RDT")) {
						attrN.setUIVisible(true);
						attrN.setIdx(100);
						attrN.setUIWidth(60);
					} else if (keyOfEn.equals("Rec")) {
						attrN.setUIVisible(true);
						attrN.setIdx(100);
						attrN.setUIWidth(60);
					}
					attrN.Save();
				}
				GEDtl geDtl = new GEDtl(rptDtlNo);
				geDtl.CheckPhysicsTable();
			}
		}
	}
	 
	/** 
	 检查数据报表.
	 @param nds
	 * @throws Exception 
	*/
	private void DoCheck_CheckRpt(Nodes nds) throws Exception
	{
		String fk_mapData = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		String flowId = String.valueOf(Integer.parseInt(this.getNo()));

		// 处理track表.
		Track.CreateOrRepairTrackTable(flowId);


			///#region 插入字段。
		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
				 sql = "SELECT distinct  KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' " + SystemConfig.getAppCenterDBAddStringStr() + " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "' AND Sys_MapAttr.FK_MapData = 'ND' || CAST(WF_Node.NodeID AS VARCHAR(20)))";
					break;
			case MSSQL:
			
	            sql = "SELECT distinct  KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' + cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "' AND Sys_MapAttr.FK_MapData = 'ND' + CAST(WF_Node.NodeID AS VARCHAR(20)))";
				break;
			case Informix:
				//sql = "SELECT distinct  KeyOfEn FROM Sys_MapAttr WHERE FK_MapData IN ( SELECT 'ND' " + SystemConfig.getAppCenterDBAddStringStr() + " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
				sql = "SELECT distinct KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' " + SystemConfig.getAppCenterDBAddStringStr() + " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "' AND Sys_MapAttr.FK_MapData = 'ND' || CAST(WF_Node.NodeID AS VARCHAR(20)))";
				break;
			case MySQL:
				//sql = "SELECT DISTINCT KeyOfEn FROM Sys_MapAttr  WHERE FK_MapData IN (SELECT X.No FROM ( SELECT CONCAT('ND',NodeID) AS No FROM WF_Node WHERE FK_Flow='" + this.getNo() + "') AS X )";
				sql = "SELECT DISTINCT KeyOfEn FROM Sys_MapAttr  WHERE  exists  (SELECT X.No FROM ( SELECT CONCAT('ND',NodeID) AS No FROM WF_Node WHERE FK_Flow='" + this.getNo() + "') AS X where sys_mapattr.fk_mapdata=x.no)";
				break;
			default:
				//sql = "SELECT distinct  KeyOfEn FROM Sys_MapAttr WHERE FK_MapData IN ( SELECT 'ND' " + SystemConfig.getAppCenterDBAddStringStr() + " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
				sql = "SELECT distinct KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' " + SystemConfig.getAppCenterDBAddStringStr() + " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "' AND Sys_MapAttr.FK_MapData = 'ND' || CAST(WF_Node.NodeID AS VARCHAR(20)))";
				break;
		}

		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT A.* FROM (" + sql + ") AS A ";
			String sql3 = "DELETE FROM Sys_MapAttr WHERE KeyOfEn NOT IN (" + sql + ") AND FK_MapData='" + fk_mapData + "' ";
			DBAccess.RunSQL(sql3); // 删除不存在的字段.
		}
		else
		{
			String sql2 = "DELETE FROM Sys_MapAttr WHERE KeyOfEn NOT IN (" + sql + ") AND FK_MapData='" + fk_mapData + "' ";
			DBAccess.RunSQL(sql2); // 删除不存在的字段.
		}

		// 补充上没有字段。
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
				//sunxd ORACLE数据库语句增加别名
				sql = "SELECT MyPK \"MyPK\", KeyOfEn \"KeyOfEn\" FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' " + SystemConfig.getAppCenterDBAddStringStr() + " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "' AND Sys_MapAttr.FK_MapData = 'ND' || CAST(WF_Node.NodeID AS VARCHAR(20)))";
				break;
			case MySQL:
				sql = "SELECT MyPK, KeyOfEn FROM Sys_MapAttr WHERE EXISTS (SELECT X.No FROM ( SELECT CONCAT('ND',NodeID) AS No FROM WF_Node WHERE FK_Flow='" + this.getNo() + "') AS X where Sys_MapAttr.FK_MapData = X.No)";
				break;
			default:
				sql = "SELECT MyPK, KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND'+cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo() + "' AND Sys_MapAttr.FK_MapData = 'ND' + CAST(WF_Node.NodeID AS VARCHAR(20)))";
				break;
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		//sunxd ORACLE数据库语句增加别名
		sql = "SELECT KeyOfEn \"KeyOfEn\" FROM Sys_MapAttr WHERE FK_MapData='ND" + flowId + "Rpt'";
		DataTable dtExits = DBAccess.RunSQLReturnTable(sql);
		String pks = "@";
		for (DataRow dr : dtExits.Rows)
		{
			pks += dr.getValue(0) + "@";
		}

		for (DataRow dr : dt.Rows)
		{
			String mypk = dr.getValue("MyPK").toString();
			if (pks.contains("@" + dr.getValue("KeyOfEn").toString() + "@"))
			{
				continue;
			}

			pks += dr.getValue("KeyOfEn").toString() + "@";

			BP.Sys.MapAttr ma = new BP.Sys.MapAttr(mypk);
			ma.setMyPK("ND" + flowId + "Rpt_" + ma.getKeyOfEn());
			ma.setFK_MapData("ND" + flowId + "Rpt");
			ma.setUIIsEnable(false);

			if (ma.getDefValReal().contains("@"))
			{
				//如果是一个有变量的参数.
				ma.setDefVal("");
			}

			try
			{
				ma.Insert();
			}
			catch (java.lang.Exception e)
			{
			}
		}

		MapAttrs attrs = new MapAttrs(fk_mapData);

		// 创建mapData.
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo("ND" + flowId + "Rpt");
		if (md.RetrieveFromDBSources() == 0)
		{
			md.setName(this.getName());
			md.setPTable(this.getPTable());
			md.Insert();
		}
		else
		{
			md.setName(this.getName());
			md.setPTable(this.getPTable());
			md.Update();
		}
		int groupID = 0;
		for (MapAttr attr : attrs.ToJavaList())
		{
			String keyOfEn = attr.getKeyOfEn();
			if (keyOfEn.equals(StartWorkAttr.FK_Dept)) {
				// attr.UIBindKey = "BP.Port.Depts";
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(true);
				attr.setGroupID(groupID); // gfs[0].GetValIntByKey("OID");
				attr.setUIIsEnable(false);
				attr.setDefVal("");
				attr.setMaxLen(100);
				attr.Update();
			} else if (keyOfEn.equals("FK_NY")) {
				// attr.UIBindKey = "BP.Pub.NYs";
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(true);
				attr.setUIIsEnable(false);
				attr.setGroupID(groupID);
				attr.Update();
			}
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.Title) == false)
		{
			// 标题 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.Title); // "FlowEmps";
			attr.setName("标题");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(400);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.OID) == false)
		{
			// WorkID 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("OID");
			attr.setName("WorkID");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(EditType.Readonly);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.WFState) == false)
		{
			// 流程状态 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.WFState);
			attr.setName("流程状态");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIBindKey(GERptAttr.WFState);
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.Enum);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.WFSta) == false)
		{
			// 流程状态Ext 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.WFSta);
			attr.setName("状态");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIBindKey(GERptAttr.WFSta);
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.Enum);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowEmps) == false)
		{
			// 参与人 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEmps); // "FlowEmps";
			attr.setName("参与人");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowStarter) == false)
		{
			// 发起人 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStarter);
			attr.setName("发起人");
			attr.setMyDataType(DataType.AppString);
			
			attr.setUIBindKey("BP.Port.Emps");
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);
			
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(20);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowStartRDT) == false)
		{
			// MyNum 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStartRDT); // "FlowStartRDT";
			attr.setName("发起时间");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowEnder) == false)
		{
			// 发起人 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnder);
			attr.setName("结束人");
			attr.setMyDataType(DataType.AppString);
			attr.setUIBindKey("BP.Port.Emps");
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(20);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowEnderRDT) == false)
		{
			// MyNum 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnderRDT); // "FlowStartRDT";
			attr.setName("结束时间");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowEndNode) == false)
		{
			// 结束节点 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEndNode);
			attr.setName("结束节点");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowDaySpan) == false)
		{
			// FlowDaySpan 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowDaySpan); // "FlowStartRDT";
			attr.setName("跨度(天)");
			attr.setMyDataType(DataType.AppMoney);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PFlowNo) == false)
		{
			// 父流程 流程编号 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PFlowNo);
			attr.setName("父流程流程编号"); // 父流程流程编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(3);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PNodeID) == false)
		{
			// 父流程WorkID 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PNodeID);
			attr.setName("父流程启动的节点");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PWorkID) == false)
		{
			// 父流程WorkID 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PWorkID);
			attr.setName("父流程WorkID");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PEmp) == false)
		{
			// 调起子流程的人员 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PEmp);
			attr.setName("调起子流程的人员");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(32);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.BillNo) == false)
		{
			// 父流程 流程编号 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.BillNo);
			attr.setName("单据编号"); // 单据编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(100);
			attr.setIdx(-100);
			attr.Insert();
		}


		if (attrs.Contains(md.getNo() + "_MyNum") == false)
		{
			// MyNum 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("MyNum");
			attr.setName("条");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("1");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.AtPara) == false)
		{
			// 父流程 流程编号 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.AtPara);
			attr.setName("参数"); // 单据编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(4000);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.GUID) == false)
		{
			// 父流程 流程编号 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.GUID);
			attr.setName("GUID"); // 单据编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(32);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PrjNo) == false)
		{
			// 项目编号 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PrjNo);
			attr.setName("项目编号"); // 项目编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(100);
			attr.setIdx(-100);
			attr.Insert();
		}
		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PrjName) == false)
		{
			// 项目名称 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PrjName);
			attr.setName("项目名称");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(100);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowNote) == false)
		{
			// 流程信息 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowNote);
			attr.setName("流程信息"); //父流程流程编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(500);
			attr.setIdx(-100);
			attr.Insert();
		}
			///#region 为流程字段设置分组。
		try
		{
			String flowInfo = "流程信息";
			GroupField flowGF = new GroupField();
			int num = flowGF.Retrieve(GroupFieldAttr.EnName, fk_mapData, GroupFieldAttr.Lab, "流程信息");
			if (num == 0)
			{
				flowGF = new GroupField();
				flowGF.setLab(flowInfo);
				flowGF.setEnName(fk_mapData);
				flowGF.setIdx(-1);
				flowGF.Insert();
			}
			sql = "UPDATE Sys_MapAttr SET GroupID=" + flowGF.getOID() + " WHERE  FK_MapData='" + fk_mapData + "'  AND KeyOfEn IN('" + GERptAttr.PFlowNo + "','" + GERptAttr.PWorkID + "','" + GERptAttr.MyNum + "','" + GERptAttr.FK_Dept + "','" + GERptAttr.FK_NY + "','" + GERptAttr.FlowDaySpan + "','" + GERptAttr.FlowEmps + "','" + GERptAttr.FlowEnder + "','" + GERptAttr.FlowEnderRDT + "','" + GERptAttr.FlowEndNode + "','" + GERptAttr.FlowStarter + "','" + GERptAttr.FlowStartRDT + "','" + GERptAttr.WFState + "')";
			DBAccess.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
			Log.DefaultLogWriteLineError(ex.getMessage());
		}
			///#region 尾后处理.
		GERpt gerpt = this.getHisGERpt();
		gerpt.CheckPhysicsTable(); //让报表重新生成.

		DBAccess.RunSQL("DELETE FROM Sys_GroupField WHERE FrmID='" + fk_mapData + "' AND OID NOT IN (SELECT GroupID FROM Sys_MapAttr WHERE FK_MapData = '" + fk_mapData + "')");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name='活动时间' WHERE FK_MapData='ND" + flowId + "Rpt' AND KeyOfEn='CDT'");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name='参与者' WHERE FK_MapData='ND" + flowId + "Rpt' AND KeyOfEn='Emps'");
			///#region 处理报表.
		String mapRpt = "ND" + Integer.parseInt(this.getNo()) + "MyRpt";
		MapData mapData = new MapData();
		mapData.setNo(mapRpt);
		if (mapData.RetrieveFromDBSources() == 0)
		{
			mapData.setNo(mapRpt);
			mapData.setPTable(this.getPTable());
			mapData.setName(this.getName() + "报表");
			mapData.setNote("默认.");

			//默认的查询字段.
			mapData.Insert();

			BP.WF.Rpt.MapRpt rpt = new BP.WF.Rpt.MapRpt();
			rpt.setNo(mapRpt);
			rpt.RetrieveFromDBSources();
			rpt.setFK_Flow(this.getNo());
			rpt.ResetIt();
			rpt.Update();
		}

		if (!this.getPTable().equals(mapData.getPTable()))
		{
			mapData.setPTable(this.getPTable());
			mapData.Update();
		}

		//补充基础字段.
		attrs = new MapAttrs(mapData.getNo());
			///#region 补充上流程字段到NDxxxRpt.
		for (MapAttr attr : attrs.ToJavaList())
		{
			String keyOfEn = attr.getKeyOfEn();
			if (keyOfEn.equals(StartWorkAttr.FK_Dept)) {
				attr.setUIBindKey("BP.Port.Depts");
				attr.setUIContralType(UIContralType.DDL);
				attr.setLGType(FieldTypeS.FK);
				attr.setUIVisible(true);
				attr.setGroupID(groupID); // gfs[0].GetValIntByKey("OID");
				attr.setUIIsEnable(false);
				attr.setDefVal("");
				attr.setMaxLen(100);
				attr.Update();
			} else if (keyOfEn.equals("FK_NY")) {
				attr.setUIBindKey("BP.Pub.NYs");
				attr.setUIContralType(UIContralType.DDL);
				attr.setLGType(FieldTypeS.FK);
				attr.setUIVisible(true);
				attr.setGroupID(groupID); // gfs[0].GetValIntByKey("OID");
				attr.setUIIsEnable(false);
				attr.Update();
			}
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.OID) == false)
		{
			// WorkID 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("OID");
			attr.setName("WorkID");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(BP.En.EditType.Readonly);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.WFSta) == false)
		{
			// 流程状态Ext 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.WFSta);
			attr.setName("状态");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIBindKey(GERptAttr.WFSta);
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.Enum);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowEmps) == false)
		{
			// 参与人 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEmps); // "FlowEmps");
			attr.setName("参与人");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowStarter) == false)
		{
			// 发起人 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStarter);
			attr.setName("发起人");
			attr.setMyDataType(DataType.AppString);

			attr.setUIBindKey("BP.Port.Emps");
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);

			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(20);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowStartRDT) == false)
		{
			// MyNum 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStartRDT); // "FlowStartRDT");
			attr.setName("发起时间");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowEnder) == false)
		{
			// 发起人 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnder);
			attr.setName("结束人");
			attr.setMyDataType(DataType.AppString);
			attr.setUIBindKey("BP.Port.Emps");
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(20);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowEnderRDT) == false)
		{
			// MyNum 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnderRDT); // "FlowStartRDT");
			attr.setName("结束时间");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowEndNode) == false)
		{
			// 结束节点 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEndNode);
			attr.setName("结束节点");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowDaySpan) == false)
		{
			// FlowDaySpan 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowDaySpan); // "FlowStartRDT");
			attr.setName("跨度(天)");
			attr.setMyDataType(DataType.AppMoney);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.setDefVal("0");
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PFlowNo) == false)
		{
			// 父流程 流程编号 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PFlowNo);
			attr.setName("父流程编号"); //  父流程流程编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(3);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PNodeID) == false)
		{
			// 父流程WorkID 
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PNodeID);
			attr.setName("父流程启动的节点");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PWorkID) == false)
		{
			// 父流程WorkID 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PWorkID);
			attr.setName("父流程WorkID");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PEmp) == false)
		{
			// 调起子流程的人员 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PEmp);
			attr.setName("调起子流程的人员");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(32);
			attr.setIdx(-100);
			attr.Insert();
		}


		//if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.CWorkID) == false)
		//{
		//    /* 延续流程WorkID */
		//    MapAttr attr = new BP.Sys.MapAttr();
		//    attr.FK_MapData = md.No;
		//    attr.HisEditType = EditType.UnDel;
		//    attr.KeyOfEn = GERptAttr.CWorkID;
		//    attr.Name = "延续流程WorkID";
		//    attr.MyDataType = DataType.AppInt;
		//    attr.DefVal = "0";
		//    attr.UIContralType = UIContralType.TB;
		//    attr.LGType = FieldTypeS.Normal;
		//    attr.UIVisible = true;
		//    attr.UIIsEnable = false;
		//    attr.UIIsLine = false;
		//    attr.HisEditType = EditType.UnDel;
		//    attr.Idx = -101;
		//    attr.Insert();
		//}

		//if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.CFlowNo) == false)
		//{
		//    /* 延续流程编号 */
		//    MapAttr attr = new BP.Sys.MapAttr();
		//    attr.FK_MapData = md.No;
		//    attr.HisEditType = EditType.UnDel;
		//    attr.KeyOfEn = GERptAttr.CFlowNo;
		//    attr.Name = "延续流程编号";
		//    attr.MyDataType = DataType.AppString;
		//    attr.UIContralType = UIContralType.TB;
		//    attr.LGType = FieldTypeS.Normal;
		//    attr.UIVisible = true;
		//    attr.UIIsEnable = false;
		//    attr.UIIsLine = true;
		//    attr.MinLen = 0;
		//    attr.MaxLen = 3;
		//    attr.Idx = -100;
		//    attr.Insert();
		//}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.BillNo) == false)
		{
			// 单据编号 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.BillNo);
			attr.setName("单据编号"); //  单据编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(100);
			attr.setIdx(-100);
			attr.Insert();
		}


		if (attrs.Contains(mapData.getNo() + "_MyNum") == false)
		{
			// MyNum 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("MyNum");
			attr.setName("条");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("1");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.AtPara) == false)
		{
			// 父流程 流程编号 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.AtPara);
			attr.setName("参数"); // 单据编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(4000);
			attr.setIdx(-100);
			attr.Insert();
		}


		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.GUID) == false)
		{
			// 父流程 流程编号 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.GUID);
			attr.setName("GUID"); // 单据编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(32);
			attr.setIdx(-100);
			attr.Insert();
		}

		//if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PrjNo) == false)
		//{
		//    /* 项目编号 */
		//    MapAttr attr = new BP.Sys.MapAttr();
		//    attr.FK_MapData = md.No;
		//    attr.HisEditType = EditType.UnDel;
		//    attr.KeyOfEn = GERptAttr.PrjNo;
		//    attr.Name = "项目编号"; //  项目编号
		//    attr.MyDataType = DataType.AppString;
		//    attr.UIContralType = UIContralType.TB;
		//    attr.LGType = FieldTypeS.Normal;
		//    attr.UIVisible = true;
		//    attr.UIIsEnable = false;
		//    attr.UIIsLine = false;
		//    attr.MinLen = 0;
		//    attr.MaxLen = 100;
		//    attr.Idx = -100;
		//    attr.Insert();
		//}
		//if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PrjName) == false)
		//{
		//    /* 项目名称 */
		//    MapAttr attr = new BP.Sys.MapAttr();
		//    attr.FK_MapData = md.No;
		//    attr.HisEditType = EditType.UnDel;
		//    attr.KeyOfEn = GERptAttr.PrjName;
		//    attr.Name = "项目名称"; //  项目名称
		//    attr.MyDataType = DataType.AppString;
		//    attr.UIContralType = UIContralType.TB;
		//    attr.LGType = FieldTypeS.Normal;
		//    attr.UIVisible = true;
		//    attr.UIIsEnable = false;
		//    attr.UIIsLine = false;
		//    attr.MinLen = 0;
		//    attr.MaxLen = 100;
		//    attr.Idx = -100;
		//    attr.Insert();
		//}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowNote) == false)
		{
			// 流程信息 
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowNote);
			attr.setName("流程信息"); //  父流程流程编号
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(500);
			attr.setIdx(-100);
			attr.Insert();
		}
	}
	
	/** 
	 执行运动事件
	 @param doType 事件类型
	 @param currNode 当前节点
	 @param en 实体
	 @param atPara 参数
	 @param objs 发送对象，可选
	 @return 执行结果
	 * @throws Exception 
	*/
	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara) throws Exception
	{
		return DoFlowEventEntity(doType, currNode, en, atPara, null,"");
	}
	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, SendReturnObjs objs) throws Exception
	{
		return DoFlowEventEntity(doType, currNode, en, atPara, objs, null, null);
	}
	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, SendReturnObjs objs, Node jumpToNode, String jumpToEmps) throws Exception
	{
		if (currNode == null)
		{
			return null;
		}

		String str = null;
		if (this.getFEventEntity() != null)
		{
			this.getFEventEntity().SendReturnObjs = objs;
			str = this.getFEventEntity().DoIt(doType, currNode, en, atPara, jumpToNode, jumpToEmps);
		}

		//FrmEvents fes = currNode.getMapData().getFrmEvents();
        FrmEvents fes = currNode.getFrmEvents();

		if (str == null)
		{
			str = fes.DoEventNode(doType, en, atPara);
		}

		if (!(doType.equals(EventListOfNode.WorkArrive)
				|| doType.equals(EventListOfNode.SendSuccess)
				|| doType.equals(EventListOfNode.ShitAfter)
				|| doType.equals(EventListOfNode.ReturnAfter)
				|| doType.equals(EventListOfNode.UndoneAfter)
				|| doType.equals(EventListOfNode.AskerReAfter))) {
			return str;
		}

		//执行消息的发送.
		PushMsgs pms = currNode.getHisPushMsgs();
		String msgAlert = ""; //生成的提示信息.
		for (PushMsg item : pms.ToJavaList())
		{
			if (!item.getFK_Event().equals(doType))
			{
				continue;
			}

			if (item.getSMSPushWay() == 0 && item.getMailPushWay() == 0)
			{
				continue; // 如果都没有消息设置，就放过.
			}

			//执行发送消息.
			msgAlert += item.DoSendMessage(currNode, en, atPara, objs, jumpToNode, jumpToEmps);
		}
		return str + msgAlert;
	}
	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, Node jumpToNode, String jumpToEmp) throws Exception
	{
		String str = this.DoFlowEventEntity(doType, currNode, en, atPara, null, jumpToNode, jumpToEmp);
		return str;
	}
	private BP.WF.FlowEventBase _FDEventEntity = null;
	/** 
	 节点实体类，没有就返回为空.
	*/
	private BP.WF.FlowEventBase getFEventEntity()
	{
		if (_FDEventEntity == null && !this.getFlowMark().equals("") && !this.getFlowEventEntity().equals(""))
		{
			_FDEventEntity = BP.WF.Glo.GetFlowEventEntityByEnName(this.getFlowEventEntity());
		}
		return _FDEventEntity;
	}
	/** 
	 是否是MD5加密流程
	*/
	public final boolean getIsMD5()
	{
		return this.GetValBooleanByKey(FlowAttr.IsMD5);
	}
	public final void setIsMD5(boolean value)
	{
		this.SetValByKey(FlowAttr.IsMD5, value);
	}
	/** 
	 是否有单据
	*/
	public final int getNumOfBill()
	{
		return this.GetValIntByKey(FlowAttr.NumOfBill);
	}
	public final void setNumOfBill(int value)
	{
		this.SetValByKey(FlowAttr.NumOfBill, value);
	}
	/** 
	 标题生成规则
	*/
	public final String getTitleRole()
	{
		return this.GetValStringByKey(FlowAttr.TitleRole);
	}
	public final void setTitleRole(String value)
	{
		this.SetValByKey(FlowAttr.TitleRole, value);
	}
	/** 
	 明细表
	*/
	public final int getNumOfDtl()
	{
		return this.GetValIntByKey(FlowAttr.NumOfDtl);
	}
	public final void setNumOfDtl(int value)
	{
		this.SetValByKey(FlowAttr.NumOfDtl, value);
	}
	public final int getAvgDay()
	{
		return this.GetValIntByKey(FlowAttr.AvgDay);
	}
	public final void setAvgDay(java.math.BigDecimal value)
	{
		this.SetValByKey(FlowAttr.AvgDay, value);
	}
	public final int getStartNodeID()
	{
		return Integer.parseInt(this.getNo() + "01");
			//return this.GetValIntByKey(FlowAttr.StartNodeID);
	}
	/** 
	 add 2013-01-01.
	 业务主表(默认为NDxxRpt)
	 
	*/
	public final String getPTable()
	{
		String s = this.GetValStringByKey(FlowAttr.PTable);
		if (StringHelper.isNullOrEmpty(s))
		{
			//sunxd 20170714
			//Integer.parseInt()中对像为空时Integer.parseInt()会报错
			//Integer.parseInt()中对像作了空值转换
			s = "ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Rpt";
		}
		return s;
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(FlowAttr.PTable, value);
	}
	/** 
	 历史记录显示字段.
	 
	*/
	public final String getHistoryFields()
	{
		String strs = this.GetValStringByKey(FlowAttr.HistoryFields);
		if (StringHelper.isNullOrEmpty(strs))
		{
			strs = "WFState,Title,FlowStartRDT,FlowEndNode";
		}

		return strs;
	}
	/** 
	 是否启用？
	 
	*/
	public final boolean getIsGuestFlow()
	{
		return this.GetValBooleanByKey(FlowAttr.IsGuestFlow);
	}
	public final void setIsGuestFlow(boolean value)
	{
		this.SetValByKey(FlowAttr.IsGuestFlow, value);
	}
	/** 
	 是否可以独立启动
	 
	*/
	public final boolean getIsCanStart()
	{
		return this.GetValBooleanByKey(FlowAttr.IsCanStart);
	}
	public final void setIsCanStart(boolean value)
	{
		this.SetValByKey(FlowAttr.IsCanStart, value);
	}
	/** 
	 是否可以批量发起
	 
	*/
	public final boolean getIsBatchStart()
	{
		return this.GetValBooleanByKey(FlowAttr.IsBatchStart);
	}
	public final void setIsBatchStart(boolean value)
	{
		this.SetValByKey(FlowAttr.IsBatchStart, value);
	}
	/** 
	 是否自动计算未来的处理人
	 
	*/
	public final boolean getIsFullSA()
	{
		return this.GetValBooleanByKey(FlowAttr.IsFullSA);
	}
	public final void setIsFullSA(boolean value)
	{
		this.SetValByKey(FlowAttr.IsFullSA, value);
	}
	/** 
	 批量发起字段
	 
	*/
	public final String getBatchStartFields()
	{
		return this.GetValStringByKey(FlowAttr.BatchStartFields);
	}
	public final void setBatchStartFields(String value)
	{
		this.SetValByKey(FlowAttr.BatchStartFields, value);
	}
	/** 
	 单据格式
	 
	*/
	public final String getBillNoFormat()
	{
		return this.GetValStringByKey(FlowAttr.BillNoFormat);
	}
	public final void setBillNoFormat(String value)
	{
		this.SetValByKey(FlowAttr.BillNoFormat, value);
	}
	/** 
	 流程类别
	 
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStringByKey(FlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{
		this.SetValByKey(FlowAttr.FK_FlowSort, value);
	}
	/** 
	 系统类别
	 
	*/
	public final String getSysType()
	{
		return this.GetValStringByKey(FlowAttr.SysType);
	}
	public final void setSysType(String value)
	{
		this.SetValByKey(FlowAttr.SysType, value);
	}
	/** 
	 参数
	 
	*/
	public final String getParas()
	{
		return this.GetValStringByKey(FlowAttr.Paras);
	}
	public final void setParas(String value)
	{
		this.SetValByKey(FlowAttr.Paras, value);
	}
	/** 
	 流程类别名称
	 * @throws Exception 
	 
	*/
	public final String getFK_FlowSortText() throws Exception
	{
		FlowSort fs = new FlowSort(this.getFK_FlowSort());
		return fs.getName();
			//return this.GetValRefTextByKey(FlowAttr.FK_FlowSort);
	}

	/** 
	 版本号
	 
	*/
	public final String getVer()
	{
		return this.GetValStringByKey(FlowAttr.Ver);
	}
	public final void setVer(String value)
	{
		this.SetValByKey(FlowAttr.Ver, value);
	}

		///#endregion


		///#region 计算属性
	/** 
	 流程类型(大的类型)
	 
	*/
	public final int getFlowType_del()
	{
		return this.GetValIntByKey(FlowAttr.FlowType);
	}
	 public final SubFlowOver getSubFlowOver()
     {
        
             return SubFlowOver.forValue(this.GetValIntByKey(FlowAttr.IsAutoSendSubFlowOver));
         
     }
	
	 
	public final String getNote()
	{
		String s = this.GetValStringByKey("Note");
		if (s.length() == 0)
		{
			return "无";
		}
		return s;
	}
	public final String getNoteHtml()
	{
		if (this.getNote().equals("无") || this.getNote().equals(""))
		{
			return "流程设计人员没有编写此流程的帮助信息，请打开设计器-》打开此流程-》设计画布上点击右键-》流程属性-》填写流程帮助信息。";
		}
		else
		{
			return this.getNote();
		}
	}
	/** 
	 是否多线程自动流程
	 
	*/
	public final boolean getIsMutiLineWorkFlow_del()
	{
		return false;
//                
//                if (this.FlowType==2 || this.FlowType==1 )
//                    return true;
//                else
//                    return false;
//                    
	}

		///#endregion


		
	/** 
	 应用类型
	*/
	public final FlowAppType getHisFlowAppType()
	{
		return FlowAppType.forValue(this.GetValIntByKey(FlowAttr.FlowAppType));
	}
	public final void setHisFlowAppType(FlowAppType value)
	{
		this.SetValByKey(FlowAttr.FlowAppType, value.getValue());
	}
	/** 
	 数据存储模式
	 
	*/
	public final DataStoreModel getHisDataStoreModel()
	{
		return DataStoreModel.forValue(this.GetValIntByKey(FlowAttr.DataStoreModel));
	}
	public final void setHisDataStoreModel(DataStoreModel value)
	{
		this.SetValByKey(FlowAttr.DataStoreModel, value.getValue());
	}
	/** 
	 节点
	 
	*/
	public Nodes _HisNodes = null;
	/** 
	 他的节点集合.
	 * @throws Exception 
	 
	*/
	public final Nodes getHisNodes() throws Exception
	{
		if (this._HisNodes == null)
		{
			_HisNodes = new Nodes(this.getNo());
		}
		return _HisNodes;
	}
	public final void setHisNodes(Nodes value)
	{
		_HisNodes = value;
	}
	/** 
	 他的 Start 节点
	 * @throws Exception 
	 
	*/
	public final Node getHisStartNode() throws Exception
	{

		for (Node nd : this.getHisNodes().ToJavaList())
		{
			if (nd.getIsStartNode())
			{
				return nd;
			}
		}
		throw new RuntimeException("@没有找到他的开始节点,工作流程[" + this.getName() + "]定义错误.");
	}
	/** 
	 他的事务类别
	 * @throws Exception 
	 
	*/
	public final FlowSort getHisFlowSort() throws Exception
	{
		return new FlowSort(this.getFK_FlowSort());
	}
	/** 
	 flow data 数据
	 * @throws Exception 
	 
	*/
	public final BP.WF.Data.GERpt getHisGERpt() throws Exception
	{
		try
		{
			BP.WF.Data.GERpt wk = new BP.WF.Data.GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
			return wk;
		}
		catch (java.lang.Exception e)
		{
			this.DoCheck();
			BP.WF.Data.GERpt wk1 = new BP.WF.Data.GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
			return wk1;
		}
	}

		///#endregion


		

	/** 
	 流程
	 
	*/
	public Flow()
	{
	}
	/** 
	 流程
	 
	 @param _No 编号
	*/
	/*
	 * 向上移动
	 */
	public final void DoUp()
	{
		this.DoOrderUp(FlowAttr.FK_FlowSort, this.getFK_FlowSort(), FlowAttr.Idx);
	}
	/*
	 * 向下移动
	 */
	public final void DoDown()
	{
		this.DoOrderDown(FlowAttr.FK_FlowSort, this.getFK_FlowSort(), FlowAttr.Idx);
	}
	public Flow(String _No) throws Exception
	{
		this.setNo(_No);
		if (SystemConfig.getIsDebug())
		{
			int i = this.RetrieveFromDBSources();
			if (i == 0)
			{
				throw new RuntimeException("流程编号不存在");
			}
		}
		else
		{
			this.Retrieve();
		}
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		//获得事件实体.
		this.setFlowEventEntity(BP.WF.Glo.GetFlowEventEntityStringByFlowMark(this.getFlowMark(), this.getNo()));

		DBAccess.RunSQL("UPDATE WF_Node SET FlowName='" + this.getName() + "' WHERE FK_Flow='" + this.getNo() + "'");
		DBAccess.RunSQL("UPDATE Sys_MapData SET  Name='" + this.getName() + "' WHERE No='" + this.getPTable() + "'");
		return super.beforeUpdateInsertAction();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Flow", "流程");
		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("3");

		map.AddTBStringPK(FlowAttr.No, null, "编号", true, true, 1, 10, 3);
		map.AddTBString(FlowAttr.Name, null, "名称", true, false, 0, 500, 10);

		map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "流程类别", new FlowSorts(), false);
		map.AddTBString(FlowAttr.SysType, null, "系统类别", true, false, 0, 3, 10);

		map.AddTBInt(FlowAttr.FlowRunWay, 0, "运行方式", false, false);

			//  map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "流程类别", new FlowSorts(), false);
			//map.AddDDLSysEnum(FlowAttr.FlowRunWay, (int)FlowRunWay.HandWork, "运行方式", false,
			//    false, FlowAttr.FlowRunWay,
			//    "@0=手工启动@1=指定人员按时启动@2=数据集按时启动@3=触发式启动");

		map.AddTBString(FlowAttr.RunObj, null, "运行内容", true, false, 0, 3000, 10);
		map.AddTBString(FlowAttr.Note, null, "备注", true, false, 0, 100, 10);
		map.AddTBString(FlowAttr.RunSQL, null, "流程结束执行后执行的SQL", true, false, 0, 2000, 10);

		map.AddTBInt(FlowAttr.NumOfBill, 0, "是否有单据", false, false);
		map.AddTBInt(FlowAttr.NumOfDtl, 0, "NumOfDtl", false, false);
		map.AddTBInt(FlowAttr.FlowAppType, 0, "流程类型", false, false);
		map.AddTBInt(FlowAttr.ChartType, 1, "节点图形类型", false, false);

			// map.AddBoolean(FlowAttr.IsOK, true, "是否启用", true, true);
		map.AddBoolean(FlowAttr.IsCanStart, true, "可以独立启动否？", true, true, true);
	    map.AddTBInt(FlowAttr.IsStartInMobile, 1, "是否可以在手机里发起？", true, true);
		 
		map.AddTBDecimal(FlowAttr.AvgDay, 0, "平均运行用天", false, false);


		map.AddTBInt(FlowAttr.IsFullSA, 0, "是否自动计算未来的处理人？(启用后,ccflow就会为已知道的节点填充处理人到WF_SelectAccper)", false, false);
		map.AddTBInt(FlowAttr.IsMD5, 0, "IsMD5", false, false);
		map.AddTBInt(FlowAttr.Idx, 0, "显示顺序号(在发起列表中)", true, false);
		map.AddTBInt(FlowAttr.TimelineRole, 0, "时效性规则", true, false);
		map.AddTBString(FlowAttr.Paras, null, "参数", false, false, 0, 400, 10);

			// add 2013-01-01. 
		map.AddTBString(FlowAttr.PTable, null, "流程数据存储主表", true, false, 0, 30, 10);


			// 草稿规则 "@0=无(不设草稿)@1=保存到待办@2=保存到草稿箱"
		map.AddTBInt(FlowAttr.Draft, 0, "草稿规则", true, false);

			// add 2013-01-01.
		map.AddTBInt(FlowAttr.DataStoreModel, 0, "数据存储模式", true, false);


			// add 2013-02-05.
		map.AddTBString(FlowAttr.TitleRole, null, "标题生成规则", true, false, 0, 150, 10, true);

			// add 2013-02-14 
		map.AddTBString(FlowAttr.FlowMark, null, "流程标记", true, false, 0, 150, 10);
		map.AddTBString(FlowAttr.FlowEventEntity, null, "FlowEventEntity", true, false, 0, 100, 10, true);
		map.AddTBString(FlowAttr.HistoryFields, null, "历史查看字段", true, false, 0, 500, 10, true);
		map.AddTBInt(FlowAttr.IsGuestFlow, 0, "是否是客户参与流程？", true, false);
		map.AddTBString(FlowAttr.BillNoFormat, null, "单据编号格式", true, false, 0, 50, 10, true);
		map.AddTBString(FlowAttr.FlowNoteExp, null, "备注表达式", true, false, 0, 500, 10, true);

			//部门权限控制类型,此属性在报表中控制的.
		map.AddTBInt(FlowAttr.DRCtrlType, 0, "部门查询权限控制方式", true, false);


			///#region 流程启动限制
		map.AddTBInt(FlowAttr.StartLimitRole, 0, "启动限制规则", true, false);
		map.AddTBString(FlowAttr.StartLimitPara, null, "规则内容", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartLimitAlert, null, "限制提示", true, false, 0, 500, 10, false);
		map.AddTBInt(FlowAttr.StartLimitWhen, 0, "提示时间", true, false);

			///#endregion 流程启动限制


			///#region 导航方式。
		map.AddTBInt(FlowAttr.StartGuideWay, 0, "前置导航方式", false, false);
		map.AddTBString(FlowAttr.StartGuideLink, null, "右侧的连接", true, false, 0, 200, 10, true);
		map.AddTBString(FlowAttr.StartGuideLab, null, "连接标签", true, false, 0, 200, 10, true);

		map.AddTBString(FlowAttr.StartGuidePara1, null, "参数1", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartGuidePara2, null, "参数2", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartGuidePara3, null, "参数3", true, false, 0, 500, 10, true);
		map.AddTBInt(FlowAttr.IsResetData, 0, "是否启用数据重置按钮？", true, false);
			//    map.AddTBInt(FlowAttr.IsImpHistory, 0, "是否启用导入历史数据按钮？", true, false);
		map.AddTBInt(FlowAttr.IsLoadPriData, 0, "是否导入上一个数据？", true, false);
		map.AddTBInt(FlowAttr.IsDBTemplate, 0, "加载模板？", true, false);

		 
 
			//批量发起 add 2013-12-27. 
		map.AddTBInt(FlowAttr.IsBatchStart, 0, "是否可以批量发起", true, false);
		map.AddTBString(FlowAttr.BatchStartFields, null, "批量发起字段(用逗号分开)", true, false, 0, 500, 10, true);

			// map.AddTBInt(FlowAttr.IsEnableTaskPool, 0, "是否启用共享任务池", true, false);
			//map.AddDDLSysEnum(FlowAttr.TimelineRole, (int)TimelineRole.ByNodeSet, "时效性规则",
			// true, true, FlowAttr.TimelineRole, "@0=按节点(由节点属性来定义)@1=按发起人(开始节点SysSDTOfFlow字段计算)");

		map.AddTBInt(FlowAttr.IsAutoSendSubFlowOver, 0, "(当前节点为子流程时)是否检查所有子流程完成后父流程自动发送", true, true);
		map.AddTBString(FlowAttr.Ver, null, "版本号", true, true, 0, 20, 10);

			//设计类型 .
		map.AddTBInt(FlowAttr.DType, 0, "设计类型0=ccbpm,1=bpmn", true, false);

		map.AddTBInt(FlowAttr.FlowDeleteRole, 0, "流程实例删除规则", true, false);





			//参数.
		map.AddTBAtParas(1000);



			///#region 数据同步方案
			//数据同步方式.
		map.AddTBInt(FlowAttr.DTSWay, FlowDTSWay.None.getValue(), "同步方式", true, true);
		map.AddTBString(FlowAttr.DTSDBSrc, null, "数据源", true, false, 0, 200, 100, false);
		map.AddTBString(FlowAttr.DTSBTable, null, "业务表名", true, false, 0, 200, 100, false);
		map.AddTBString(FlowAttr.DTSBTablePK, null, "业务表主键", false, false, 0, 32, 10);

		map.AddTBInt(FlowAttr.DTSTime, FlowDTSTime.AllNodeSend.getValue(), "执行同步时间点", true, true);
		map.AddTBString(FlowAttr.DTSSpecNodes, null, "指定的节点ID", true, false, 0, 200, 100, false);
		
		//map.AddTBInt(FlowAttr.DTSField, getDTSField().SameNames.getValue(), "要同步的字段计算方式", true, true);
		map.AddTBInt(FlowAttr.DTSField, 0, "要同步的字段计算方式", true, true);
		
	//	map.AddTBString(FlowAttr.DTSFields, null, "要同步的字段s,中间用逗号分开.", false, false, 0, 2000, 100, false);

			///#endregion 数据同步方案

			// map.AddSearchAttr(FlowAttr.FK_FlowSort);
			// map.AddSearchAttr(FlowAttr.FlowRunWay);

		RefMethod rm = new RefMethod();
		rm.Title = "设计检查报告"; // "设计检查报告";
		rm.ToolTip = "检查流程设计的问题。";
		rm.Icon = SystemConfig.getCCFlowWebPath() + "WF/Img/Btn/Confirm.gif";
		rm.ClassMethodName = this.toString() + ".DoCheck";
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);

			//   rm = new RefMethod();
			//rm.Title = this.ToE("ViewDef", "视图定义"); //"视图定义";
			//rm.Icon = "/WF/Img/Btn/View.gif";
			//rm.ClassMethodName = this.ToString() + ".DoDRpt";
			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "报表运行"; // "报表运行";
		rm.Icon = SystemConfig.getCCFlowWebPath() + "WF/Img/Btn/View.gif";
		rm.ClassMethodName = this.toString() + ".DoOpenRpt()";
			//rm.Icon = "/WF/Img/Btn/Table.gif";
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = this.ToE("FlowDataOut", "数据转出定义");  //"数据转出定义";
			////  rm.Icon = "/WF/Img/Btn/Table.gif";
			//rm.ToolTip = "在流程完成时间，流程数据转储存到其它系统中应用。";
			//rm.ClassMethodName = this.ToString() + ".DoExp";
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "删除数据";
			//rm.Warning = "您确定要执行删除流程数据吗？";
			//rm.ToolTip = "清除历史流程数据。";
			//rm.ClassMethodName = this.ToString() + ".DoExp";
			//map.AddRefMethod(rm);

			//map.AttrsOfOneVSM.Add(new FlowStations(), new Stations(), FlowStationAttr.FK_Flow,
			//    FlowStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "抄送岗位");

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region  公共方法
	/** 
	 设计数据转出
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoExp() throws Exception
	{
		this.DoCheck();
		return Glo.getCCFlowAppPath() + "WF/Admin/Exp.jsp?CondType=0&FK_Flow=" + this.getNo();
		//PubClass.WinOpen(Glo.CCFlowAppPath + "WF/Admin/Exp.jsp?CondType=0&FK_Flow=" + this.getNo(), "单据", "cdsn", 800, 500, 210, 300);
		//return null;
	}
	/** 
	 定义报表
	 @return 
	 * @throws Exception 
	*/
	public final String DoDRpt() throws Exception
	{
		this.DoCheck();
		PubClass.WinOpen(ContextHolderUtils.getResponse(),Glo.getCCFlowAppPath() + "WF/Admin/WFRpt.jsp?CondType=0&FK_Flow=" + this.getNo(), "单据", "cdsn", 800, 500, 210, 300);
		return null;
	}
	/** 
	 运行报表
	 
	 @return 
	*/
	public final String DoOpenRpt()
	{
		return null;
	}
	public final String DoDelData() throws Exception
	{

			///#region 删除独立表单的数据.
		String mysql = "SELECT OID FROM " + this.getPTable();
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
		String strs = "";
		for (FrmNode nd : fns.ToJavaList())
		{
			if (strs.contains("@" + nd.getFK_Frm()) == true)
			{
				continue;
			}

			strs += "@" + nd.getFK_Frm() + "@";
			try
			{
				MapData md = new MapData(nd.getFK_Frm());
				DBAccess.RunSQL("DELETE FROM " + md.getPTable() + " WHERE OID in (" + mysql + ")");
			}
			catch (java.lang.Exception e)
			{
			}
		}
			///#endregion 删除独立表单的数据.
		String sql = "  where FK_Node in (SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo() + "')";
		String sql1 = " where NodeID in (SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo() + "')";
		// DBAccess.RunSQL("DELETE FROM WF_CHOfFlow WHERE FK_Flow='" + this.getNo() + "'");
		DBAccess.RunSQL("DELETE FROM WF_Bill WHERE FK_Flow='" + this.getNo() + "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE FK_Flow='" + this.getNo() + "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo() + "'");

		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo() + "'");

		String sqlIn = " WHERE ReturnNode IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork " + sqlIn);
		DBAccess.RunSQL("DELETE FROM WF_GenerFH WHERE FK_Flow='" + this.getNo() + "'");
		DBAccess.RunSQL("DELETE FROM WF_SelectAccper " + sql);
		DBAccess.RunSQL("DELETE FROM WF_TransferCustom " + sql);
		// DBAccess.RunSQL("DELETE FROM WF_FileManager " + sql);
		DBAccess.RunSQL("DELETE FROM WF_RememberMe " + sql);

		//sunxd 20170714
		//Integer.parseInt()中对像为空时Integer.parseInt()会报错
		//Integer.parseInt()中对像作了空值转换
		if (DBAccess.IsExitsObject("ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Track"))
		{
			DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(this.getNo()) + "Track ");
		}

		if (DBAccess.IsExitsObject(this.getPTable()))
		{
			DBAccess.RunSQL("DELETE FROM " + this.getPTable());
		}

		DBAccess.RunSQL("DELETE FROM WF_CH WHERE FK_Flow='" + this.getNo() + "'");
		//DBAccess.RunSQL("DELETE FROM Sys_MapExt WHERE FK_MapData LIKE 'ND"+int.Parse(this.getNo())+"%'" );

		//删除节点数据。
		Nodes nds = new Nodes(this.getNo());
		for (Node nd : nds.ToJavaList())
		{
			try
			{
				Work wk = nd.getHisWork();
				DBAccess.RunSQL("DELETE FROM " + wk.getEnMap().getPhysicsTable());
			}
			catch (java.lang.Exception e2)
			{
			}

			MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
			for (MapDtl dtl : dtls.ToJavaList())
			{
				try
				{
					DBAccess.RunSQL("DELETE FROM " + dtl.getPTable());
				}
				catch (java.lang.Exception e3)
				{
				}
			}
		}
		//sunxd 20170714
		//Integer.parseInt()中对像为空时Integer.parseInt()会报错
		//Integer.parseInt()中对像作了空值转换
		MapDtls mydtls = new MapDtls("ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Rpt");
		for (MapDtl dtl : mydtls.ToJavaList())
		{
			try
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable());
			}
			catch (java.lang.Exception e4)
			{
			}
		}
		return "删除成功...";
	}
	/** 
	 装载流程模板
	 @param fk_flowSort 流程类别
	 @param path 流程名称
	 @return 
	 * @throws Exception 
	*/
	public static Flow DoLoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model) throws Exception
	{
		return DoLoadFlowTemplate(fk_flowSort, path, model, "");
	}
	public static Flow DoLoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model, String SpecialFlowNo) throws Exception
	{
		
		try
		{
		java.io.File info = new java.io.File(path);
		DataSet ds = new DataSet();
		ds.readXml(path);

		int isFlow = 0;
		for (DataTable dts : ds.Tables)
		{
			if (dts.TableName.equals("WF_Flow"))
			{
				isFlow = 1;
				break;
			}
		}
		if (isFlow == 0)
		{
			throw new RuntimeException("导入错误，非流程模版文件。");
		}
		
		DataTable dtFlow = ds.getHashTables().get("WF_Flow");
		Flow fl = new Flow();
		String oldFlowNo = dtFlow.Rows.get(0).getValue("No").toString();
		String oldFlowName = dtFlow.Rows.get(0).getValue("Name").toString();
		
		int oldFlowID = Integer.parseInt("".equals(oldFlowNo) ? "0" : oldFlowNo);
		//String timeKey = new java.util.Date().toString("yyMMddhhmmss");
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmss");
		String timeKey = format.format(new Date());
		
	 


			///#region 根据不同的流程模式，设置生成不同的流程编号.
		switch (model)
		{
			case AsNewFlow: //做为一个新流程. 
				//SpecialFlowNo = 0; // 如果不加会提示，编码已存在的bug
				fl.setNo(fl.getGenerNewNo());
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
				break;
			case AsTempleteFlowNo: //用流程模版中的编号
				fl.setNo(oldFlowNo);
				if (fl.getIsExits())
				{
					throw new RuntimeException("导入错误:流程模版(" + oldFlowName + ")中的编号(" + oldFlowNo + ")在系统中已经存在,流程名称为:" + dtFlow.Rows.get(0).getValue("name").toString());
				}
				else
				{
					fl.setNo(oldFlowNo);
					fl.DoDelData();
					fl.DoDelete(); //删除可能存在的垃圾.
				}
				break;
			case OvrewaiteCurrFlowNo: //覆盖当前的流程.
				fl.setNo(oldFlowNo);
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
				break;
			case AsSpecFlowNo:
				if (SpecialFlowNo.length() <= 0)
				{
					throw new RuntimeException("@您是按照指定的流程编号导入的，但是您没有传入正确的流程编号。");
				}
				String newFlowNo = StringUtils.leftPad(String.valueOf(SpecialFlowNo),3,'0');
				fl.setNo(newFlowNo);
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
				break;
			default:
				throw new RuntimeException("@没有判断");
		}

			///#endregion 根据不同的流程模式，设置生成不同的流程编号.

		// string timeKey = fl.getNo();
		int idx = 0;
		String infoErr = "";
		String infoTable = "";
		//sunxd 20170714
		//Integer.parseInt()中对像为空时Integer.parseInt()会报错
		//Integer.parseInt()中对像作了空值转换
		int flowID = fl.getNo().equals("") ? 0 : Integer.parseInt(fl.getNo());


			///#region 处理流程表数据
		for (DataColumn dc : dtFlow.Columns)
		{
			String val = (String)((dtFlow.Rows.get(0).getValue(dc.ColumnName) instanceof String) ? dtFlow.Rows.get(0).getValue(dc.ColumnName) : null);

			if (dc.ColumnName.toLowerCase().equals("no") || dc.ColumnName.toLowerCase().equals("fk_flowsort"))
			{
					continue;
			}
			else if (dc.ColumnName.toLowerCase().equals("name"))
			{
					// val = "复制:" + val + "_" + DateTime.Now.ToString("MM月dd日HH时mm分");
			}
			else
			{
			}
			fl.SetValByKey(dc.ColumnName, val);
		}
		fl.setFK_FlowSort(fk_flowSort);
		fl.Insert();

			///#endregion 处理流程表数据


			///#region 处理OID 插入重复的问题 Sys_GroupField, Sys_MapAttr.
		DataTable mydtGF = ds.getHashTables().get("Sys_GroupField");
		DataTable myDTAttr = ds.getHashTables().get("Sys_MapAttr");
		DataTable myDTAth = ds.getHashTables().get("Sys_FrmAttachment");
		DataTable myDTDtl = ds.getHashTables().get("Sys_MapDtl");
		DataTable myDFrm = ds.getHashTables().get("Sys_MapFrame");
		//DataTable myDM2M = ds.getHashTables().get("Sys_MapM2M");
		if (mydtGF != null)
		{
			for (DataRow dr : mydtGF.Rows)
			{
				GroupField gf = new GroupField();
				for (DataColumn dc : mydtGF.Columns)
				{
					String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
					gf.SetValByKey(dc.ColumnName, val);
				}
				int oldID = (int)gf.getOID();
				gf.setOID(DBAccess.GenerOID());
				dr.setValue("OID", gf.getOID());
				// 属性。
				if (myDTAttr != null && myDTAttr.Columns.contains("GroupID"))
				{
					for (DataRow dr1 : myDTAttr.Rows)
					{
						if (dr1.getValue("GroupID") == null)
						{
							dr1.setValue("GroupID", 0);
						}

						if (dr1.getValue("GroupID").toString().equals(String.valueOf(oldID)))
						{
							dr1.setValue("GroupID", gf.getOID());
						}
					}
				}

				if (myDTAth != null && myDTAth.Columns.contains("GroupID"))
				{
					// 附件。
					for (DataRow dr1 : myDTAth.Rows)
					{
						if (dr1.getValue("GroupID") == null)
						{
							dr1.setValue("GroupID", 0);
						}

						if (dr1.getValue("GroupID").toString().equals(String.valueOf(oldID)))
						{
							dr1.setValue("GroupID", gf.getOID());
						}
					}
				}

				if (myDTDtl != null && myDTDtl.Columns.contains("GroupID"))
				{
					// 从表。
					for (DataRow dr1 : myDTDtl.Rows)
					{
						if (dr1.getValue("GroupID") == null)
						{
							dr1.setValue("GroupID", 0);
						}

						if (dr1.getValue("GroupID").toString().equals(String.valueOf(oldID)))
						{
							dr1.setValue("GroupID", gf.getOID());
						}
					}
				}

				if (myDFrm != null && myDFrm.Columns.contains("GroupID"))
				{
					// frm.
					for (DataRow dr1 : myDFrm.Rows)
					{
						if (dr1.getValue("GroupID") == null)
						{
							dr1.setValue("GroupID", 0);
						}

						if (dr1.getValue("GroupID").toString().equals(String.valueOf(oldID)))
						{
							dr1.setValue("GroupID", gf.getOID());
						}
					}
				}

				 
			}
		}

			///#endregion 处理OID 插入重复的问题。 Sys_GroupField ， Sys_MapAttr.

		int timeKeyIdx = 0;
		for (DataTable dt : ds.Tables)
		{
			timeKeyIdx++;
			timeKey = timeKey + (new Integer(timeKeyIdx)).toString();

			infoTable = "@导入:" + dt.TableName + " 出现异常。";
			
			
			if ( dt.TableName.equals("WF_FlowForm")== true )  
			{
				for (DataRow dr : dt.Rows)
                {
					   NodeYGFlow yg = new NodeYGFlow();
	                    for (DataColumn dc : dt.Columns)
	                    {
	                        String val = (String) dr.getValue(dc.ColumnName) ;
	                        if (val == null)
	                            continue;
	                        
	                        String colName=dc.ColumnName.toLowerCase();
	                        
	                        if (colName.equals("nodeid") || colName.equals("fk_node"))
	                        {
	                        	 if (val.length() == 3)
	                                    val = flowID + val.substring(1);
	                                else if (val.length() == 4)
	                                    val = flowID + val.substring(2);
	                                else if (val.length() == 5)
	                                    val = flowID + val.substring(3);
	                        }
	                        
	                        if (colName.equals("fk_flow"))
	                        {
	                        	 val = fl.getNo();
	                        }                        
	                      
	                        yg.SetValByKey(dc.ColumnName, val);
	                    }
	                    yg.Insert();
                }
			}
			
			
//			switch (dt.TableName)
//ORIGINAL LINE: case "WF_Flow":
			if (dt.TableName.equals("WF_Flow")) //模版文件。
			{
					continue;
			}
//ORIGINAL LINE: case "WF_FlowFormTree":
			else if (dt.TableName.equals("WF_FlowFormTree")) //独立表单目录 add 2013-12-03
			{
					//foreach (DataRow dr in dt.Rows)
					//{
					//    FlowForm cd = new FlowForm();
					//    foreach (DataColumn dc in dt.Columns)
					//    {
					//        string val = dr.getValue(dc.ColumnName) as string;
					//        if (val == null)
					//            continue;
					//        switch (dc.ColumnName.ToLower())
					//        {
					//            case "fk_flow":
					//                val = fl.getNo();
					//                break;
					//            default:
					//                val = val.replace("ND" + oldFlowID, "ND" + flowID);
					//                break;
					//        }
					//        cd.SetValByKey(dc.ColumnName, val);
					//    }
					//    cd.Insert();
					//}
			}
//ORIGINAL LINE: case "WF_FlowForm":
			else if (dt.TableName.equals("WF_FlowForm")) //独立表单。 add 2013-12-03
			{
					//foreach (DataRow dr in dt.Rows)
					//{
					//    FlowForm cd = new FlowForm();
					//    foreach (DataColumn dc in dt.Columns)
					//    {
					//        string val = dr.getValue(dc.ColumnName) as string;
					//        if (val == null)
					//            continue;
					//        switch (dc.ColumnName.ToLower())
					//        {
					//            case "fk_flow":
					//                val = fl.getNo();
					//                break;
					//            default:
					//                val = val.replace("ND" + oldFlowID, "ND" + flowID);
					//                break;
					//        }
					//        cd.SetValByKey(dc.ColumnName, val);
					//    }
					//    cd.Insert();
					//}
			}
//ORIGINAL LINE: case "WF_NodeForm":
			else if (dt.TableName.equals("WF_NodeForm")) //节点表单权限。 2013-12-03
			{
					for (DataRow dr : dt.Rows)
					{
						NodeToolbar cd = new NodeToolbar();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
//ORIGINAL LINE: case "tonodeid":
							if (dc.ColumnName.toLowerCase().equals("tonodeid") || dc.ColumnName.toLowerCase().equals("fk_node") || dc.ColumnName.toLowerCase().equals("nodeid"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("fk_flow"))
							{
									val = fl.getNo();
							}
							else
							{
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.Insert();
					}
			}
			else if (dt.TableName.equals("Sys_FrmSln")) //表单字段权限。 2013-12-03
			{
					for (DataRow dr : dt.Rows)
					{
						FrmField cd = new FrmField();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
//ORIGINAL LINE: case "tonodeid":
							if (dc.ColumnName.toLowerCase().equals("tonodeid") || dc.ColumnName.toLowerCase().equals("fk_node") || dc.ColumnName.toLowerCase().equals("nodeid"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("fk_flow"))
							{
									val = fl.getNo();
							}
							else
							{
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.Insert();
					}
			}
			else if (dt.TableName.equals("WF_NodeToolbar")) //工具栏。
			{
					for (DataRow dr : dt.Rows)
					{
						NodeToolbar cd = new NodeToolbar();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
//ORIGINAL LINE: case "tonodeid":
							if (dc.ColumnName.toLowerCase().equals("tonodeid") || dc.ColumnName.toLowerCase().equals("fk_node") || dc.ColumnName.toLowerCase().equals("nodeid"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("fk_flow"))
							{
									val = fl.getNo();
							}
							else
							{
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.setOID(DBAccess.GenerOID());
						cd.DirectInsert();
					}
			}
			else if (dt.TableName.equals("WF_BillTemplate"))
			{
					continue; //因为省掉了 打印模板的处理。
					/*for (DataRow dr : dt.Rows)
					{
						BillTemplate bt = new BillTemplate();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_flow"))
							{
									val = (new Integer(flowID)).toString();
							}
							else if (dc.ColumnName.toLowerCase().equals("nodeid") || dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							bt.SetValByKey(dc.ColumnName, val);
						}
						int i = 0;
						String no = bt.getNo();
						while (bt.getIsExits())
						{
							bt.setNo(no + (new Integer(i)).toString());
							i++;
						}

						try
						{
							File.Copy(info.getDirectoryName() + "\\" + no + ".rtf", BP.Sys.SystemConfig.getPathOfWebApp() + "\\DataUser\\CyclostyleFile\\" + bt.getNo() + ".rtf", true);
						}
						catch (RuntimeException ex)
						{
							// infoErr += "@恢复单据模板时出现错误：" + ex.Message + ",有可能是您在复制流程模板时没有复制同目录下的单据模板文件。";
						}
						bt.Insert();
					}*/
			}
			else if (dt.TableName.equals("WF_FrmNode")) //Conds.xml。
			{
					DBAccess.RunSQL("DELETE FROM WF_FrmNode WHERE FK_Flow='" + fl.getNo()+ "'");
					for (DataRow dr : dt.Rows)
					{
						FrmNode fn = new FrmNode();
						fn.setFK_Flow(fl.getNo());
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("fk_flow"))
							{
									val = fl.getNo();
							}
							else
							{
							}
							fn.SetValByKey(dc.ColumnName, val);
						}
						// 开始插入。
						fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node());
						fn.Insert();
					}
			}
			else if (dt.TableName.equals("WF_FindWorkerRole")) //找人规则
			{
					for (DataRow dr : dt.Rows)
					{
						FindWorkerRole en = new FindWorkerRole();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node") || dc.ColumnName.toLowerCase().equals("nodeid"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("fk_flow"))
							{
									val = fl.getNo();
							}
							else
							{
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							en.SetValByKey(dc.ColumnName, val);
						}

						//插入.
						en.DirectInsert();
					}
			}
			else if (dt.TableName.equals("WF_Cond")) //Conds.xml。
			{
					for (DataRow dr : dt.Rows)
					{
						Cond cd = new Cond();
						cd.setFK_Flow(fl.getNo());
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							if (dc.ColumnName.toLowerCase().equals("tonodeid") || dc.ColumnName.toLowerCase().equals("fk_node") || dc.ColumnName.toLowerCase().equals("nodeid"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("fk_flow"))
							{
									val = fl.getNo();
							}
							else
							{
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							cd.SetValByKey(dc.ColumnName, val);
						}

						cd.setFK_Flow(fl.getNo());

						//  return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.Stas.ToString();
						// ，开始插入。 
						if (cd.getMyPK().contains("Stas"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.Stas.toString());
						}
						else if (cd.getMyPK().contains("Dept"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.Depts.toString());
						}
						else if (cd.getMyPK().contains("Paras"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.Paras.toString());
						}
						else if (cd.getMyPK().contains("Url"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.Url.toString());
						}
						else if (cd.getMyPK().contains("SQL"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.SQL);
						}
						else
						{
							cd.setMyPK(DBAccess.GenerOID() + DateUtils.getCurrentDate("yyMMddHHmmss"));
						}
						cd.DirectInsert();
					}
			}else if (dt.TableName.equals("WF_CCDept")) //抄送到部门。
			{
					for (DataRow dr : dt.Rows)
					{
						CCDept cd = new CCDept();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							cd.SetValByKey(dc.ColumnName, val);
						}

						//开始插入。
						try
						{
							cd.Insert();
						}
						catch (java.lang.Exception e)
						{
							cd.Update();
						}
					}
			}
			else if (dt.TableName.equals("WF_NodeReturn")) //可退回的节点。
			{
					for (DataRow dr : dt.Rows)
					{
						NodeReturn cd = new NodeReturn();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node") || dc.ColumnName.toLowerCase().equals("returnn"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							cd.SetValByKey(dc.ColumnName, val);
						}

						//开始插入。
						try
						{
							cd.Insert();
						}
						catch (java.lang.Exception e2)
						{
							cd.Update();
						}
					}
			}
//ORIGINAL LINE: case "WF_Direction":
			else if (dt.TableName.equals("WF_Direction")) //方向。
			{
					for (DataRow dr : dt.Rows)
					{
						Direction dir = new Direction();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
//ORIGINAL LINE: case "node":
							if (dc.ColumnName.toLowerCase().equals("node") || dc.ColumnName.toLowerCase().equals("tonode"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							dir.SetValByKey(dc.ColumnName, val);
						}
						dir.setFK_Flow(fl.getNo());
						dir.Insert();
					}
			}
			else if (dt.TableName.equals("WF_TurnTo")) //转向规则.
			{
					for (DataRow dr : dt.Rows)
					{
						TurnTo fs = new TurnTo();

						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							fs.SetValByKey(dc.ColumnName, val);
						}
						fs.setFK_Flow(fl.getNo());
						fs.Save();
					}
			}			 
			else if (dt.TableName.equals("WF_LabNote")) //LabNotes.xml。
			{
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						LabNote ln = new LabNote();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							ln.SetValByKey(dc.ColumnName, val);
						}
						idx++;
						ln.setFK_Flow(fl.getNo());
						ln.setMyPK(ln.getFK_Flow() + "_" + ln.getX() + "_" + ln.getY() + "_" + idx);
						ln.DirectInsert();
					}
			}
			else if (dt.TableName.equals("WF_NodeDept")) //FAppSets.xml。
			{
					for (DataRow dr : dt.Rows)
					{
						NodeDept dir = new NodeDept();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
 
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							dir.SetValByKey(dc.ColumnName, val);
						}
						dir.Insert();
					}
			}
			else if (dt.TableName.equals("WF_Node")) //导入节点信息.
			{
					for (DataRow dr : dt.Rows)
					{
						BP.WF.Template.NodeSheet nd = new BP.WF.Template.NodeSheet();

						BP.WF.Template.CC cc = new CC(); // 抄送相关的信息.
						BP.WF.Template.FrmWorkCheck fwc = new FrmWorkCheck();

						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							if (dc.ColumnName.toLowerCase().equals("nodefrmid"))
							{
									if (val.length() == 5)
									{
										val = "ND" + flowID + val.substring(3);
									}
									else if (val.length() == 6)
									{
										val = "ND" + flowID + val.substring(4);
									}
									else if (val.length() == 7)
									{
										val = "ND" + flowID + val.substring(5);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("nodeid"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("fk_flow")
									|| dc.ColumnName.toLowerCase().equals("fk_flowsort"))
							{
									continue;
							}
							else if (dc.ColumnName.toLowerCase().equals("showsheets")
									|| dc.ColumnName.toLowerCase().equals("histonds") 
									|| dc.ColumnName.toLowerCase().equals("groupstands"))
							{
									String key = "@" + flowID;
									val = val.replace(key, "@");
							}
							else
							{
							}
							nd.SetValByKey(dc.ColumnName, val);
							cc.SetValByKey(dc.ColumnName, val);
							fwc.SetValByKey(dc.ColumnName, val);
						}

						nd.setFK_Flow(fl.getNo());
						nd.setFlowName(fl.getName());
						try
						//@于庆海
						{
							if (nd.getEnMap().getAttrs().Contains("OfficePrintEnable")){
							if (nd.GetValStringByKey("OfficePrintEnable").equals("打印"))
							{
								nd.SetValByKey("OfficePrintEnable", 0);
							}
							}
							nd.DirectInsert();

							//把抄送的信息也导入里面去.
							cc.DirectUpdate();
							fwc.DirectUpdate();
							DBAccess.RunSQL("DELETE FROM Sys_MapAttr WHERE FK_MapData='ND" + nd.getNodeID() + "'");
						}
						catch (RuntimeException ex)
						{
							cc.CheckPhysicsTable();
							fwc.CheckPhysicsTable();

							throw new RuntimeException("@导入节点:FlowName:" + nd.getFlowName() + " nodeID: " + nd.getNodeID() + " , " + nd.getName() + " 错误:" + ex.getMessage());
						}

						//删除mapdata.
					}
					 // 执行update 触发其他的业务逻辑。
					for (DataRow dr : dt.Rows)
					{
						Node nd = new Node();
						//sunxd 20170714
						//Integer.parseInt()中对像为空时Integer.parseInt()会报错
						//Integer.parseInt()中对像作了空值转换
						nd.setNodeID(dr.getValue(NodeAttr.NodeID).toString().equals("") ? 0 : Integer.parseInt(dr.getValue(NodeAttr.NodeID).toString()));
						nd.RetrieveFromDBSources();
						nd.setFK_Flow(fl.getNo());
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("nodefrmid"))
							{
									if (val.length() == 5)
									{
										val = "ND" + flowID + val.substring(3);
									}
									else if (val.length() == 6)
									{
										val = "ND" + flowID + val.substring(4);
									}
									else if (val.length() == 7)
									{
										val = "ND" + flowID + val.substring(5);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("nodeid"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else if (dc.ColumnName.toLowerCase().equals("fk_flow") || dc.ColumnName.toLowerCase().equals("fk_flowsort"))
							{
									continue;
							}
							else if (dc.ColumnName.toLowerCase().equals("showsheets") || dc.ColumnName.toLowerCase().equals("histonds") || dc.ColumnName.toLowerCase().equals("groupstands"))
							{
									String key = "@" + flowID;
									val = val.replace(key, "@");
							}
							else
							{
							}
							nd.SetValByKey(dc.ColumnName, val);
						}
						nd.setFK_Flow(fl.getNo());
						nd.setFlowName(fl.getName());
						nd.DirectUpdate();
					}
			}
			else if (dt.TableName.equals("WF_NodeStation")) //FAppSets.xml。
			{
					DBAccess.RunSQL("DELETE FROM WF_NodeStation WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + fl.getNo() + "')");
					for (DataRow dr : dt.Rows)
					{
						NodeStation ns = new NodeStation();
						for (DataColumn dc : dr.columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							ns.SetValByKey(dc.ColumnName, val);
						}
						ns.Insert();
					}
			}
			 
			else if (dt.TableName.equals("Sys_Enum")) //RptEmps.xml。
			{
					for (DataRow dr : dt.Rows)
					{
						SysEnum se = new SysEnum();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
							}
							else
							{
							}
							se.SetValByKey(dc.ColumnName, val);
						}
						se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
						if (se.getIsExits())
						{
							continue;
						}
						se.Insert();
					}
			}
			else if (dt.TableName.equals("Sys_EnumMain")) //RptEmps.xml。
			{
					for (DataRow dr : dt.Rows)
					{
						SysEnumMain sem = new SysEnumMain();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							sem.SetValByKey(dc.ColumnName, val);
						}
						if (sem.getIsExits())
						{
							continue;
						}
						sem.Insert();
					}
			}
			else if (dt.TableName.equals("Sys_MapAttr")) //RptEmps.xml。
			{
					for (DataRow dr : dt.Rows)
					{
						MapAttr ma = new MapAttr();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) + "";

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_mapdata") || dc.ColumnName.toLowerCase().equals("keyofen") || dc.ColumnName.toLowerCase().equals("autofulldoc"))
							{
									if (val == null)
									{
										continue;
									}

									val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							else
							{
							}
							ma.SetValByKey(dc.ColumnName, val);
						}
						boolean b = ma.IsExit(MapAttrAttr.FK_MapData, ma.getFK_MapData(), MapAttrAttr.KeyOfEn, ma.getKeyOfEn());

						ma.setMyPK(ma.getFK_MapData() + "_" + ma.getKeyOfEn());
						if (b == true)
						{
							ma.DirectUpdate();
						}
						else
						{
							ma.DirectInsert();
						}
					}
			}
			else if (dt.TableName.equals("Sys_MapData")) //RptEmps.xml。
			{
					for (DataRow dr : dt.Rows)
					{
						MapData md = new MapData();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + Integer.parseInt(fl.getNo()));
							md.SetValByKey(dc.ColumnName, val);
						}
						md.Save();
					}
			}
			else if (dt.TableName.equals("Sys_MapDtl")) //RptEmps.xml。
			{
					for (DataRow dr : dt.Rows)
					{
						MapDtl md = new MapDtl();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							md.SetValByKey(dc.ColumnName, val);
						}
						md.Save();
					}
			}
			else if (dt.TableName.equals("Sys_MapExt"))
			{
					for (DataRow dr : dt.Rows)
					{
						MapExt md = new MapExt();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							md.SetValByKey(dc.ColumnName, val);
						}

						//调整他的PK.
						//md.InitPK();
						md.Save(); //执行保存.
					}
			}
			else if (dt.TableName.equals("Sys_FrmLine"))
			{
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLine en = new FrmLine();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}
						
						en.setMyPK( DBAccess.GenerGUID());
						// DBAccess.GenerOIDByGUID(); "LIE" + timeKey + "_" + idx;
						//if (en.IsExitGenerPK())
						//    continue;
						en.Insert();
					}
			}
			else if (dt.TableName.equals("Sys_FrmEle"))
			{
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmEle en = new FrmEle();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}
						en.Insert();
					}
			}
			else if (dt.TableName.equals("Sys_FrmImg"))
			{
					idx = 0;
					timeKey = DateUtils.getCurrentDate("yyyyMMddHHmmss");
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImg en = new FrmImg();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

					 
							en.setMyPK( DBAccess.GenerGUID());
							en.Insert();
						 
					}
			}
			else if (dt.TableName.equals("Sys_FrmLab"))
			{
					idx = 0;
					timeKey = DateUtils.getCurrentDate("yyyyMMddHHmmss");
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLab en = new FrmLab();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						//en.MyPK = Guid.NewGuid().ToString();
						// 出现重复的
					 
							en.setMyPK( DBAccess.GenerGUID());
							en.Insert();
						 
					}
			}
			else if (dt.TableName.equals("Sys_FrmLink"))
			{
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLink en = new FrmLink();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val);
						}
						en.setMyPK(UUID.randomUUID().toString());
						//en.MyPK = "LK" + timeKey + "_" + idx;
						en.Insert();
					}
			}
			else if (dt.TableName.equals("Sys_FrmAttachment"))
			{
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmAttachment en = new FrmAttachment();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.setMyPK(en.getFK_MapData() + "_" + en.getNoOfObj());
						en.Save();
					}
			}
			else if (dt.TableName.equals("Sys_FrmEvent")) //事件.
			{
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmEvent en = new FrmEvent();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						//解决保存错误问题. 
						try
						{
							en.Insert();
						}
						catch (java.lang.Exception e5)
						{
							en.Update();
						}
					}
			}
			 
			else if (dt.TableName.equals("Sys_FrmRB")) //Sys_FrmRB.
			{
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmRB en = new FrmRB();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}
						en.Insert();
					}
			}
			else if (dt.TableName.equals("WF_NodeEmp")) //FAppSets.xml。
			{
					for (DataRow dr : dt.Rows)
					{
						NodeEmp ne = new NodeEmp();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}


//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Insert();
					}
			}
			else if (dt.TableName.equals("Sys_GroupField"))
			{
					for (DataRow dr : dt.Rows)
					{
						GroupField gf = new GroupField();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("enname") || dc.ColumnName.toLowerCase().equals("keyofen"))
							{
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							else if (dc.ColumnName.toLowerCase().equals("ctrlid")) //升级傻瓜表单的时候,新增加的字段 add by zhoupeng 2016.11.21
							{
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							else
							{
							}
							gf.SetValByKey(dc.ColumnName, val);
						}
						int oid = DBAccess.GenerOID();
						DBAccess.RunSQL("UPDATE Sys_MapAttr SET GroupID=" + oid + " WHERE FK_MapData='" + gf.getEnName() + "' AND GroupID=" + gf.getOID());
						gf.InsertAsOID(oid);
					}
			}
			else if (dt.TableName.equals("WF_CCEmp")) // 抄送.
			{
					for (DataRow dr : dt.Rows)
					{
						CCEmp ne = new CCEmp();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}


//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Insert();
					}
			}
			else if (dt.TableName.equals("WF_CCStation")) // 抄送.
			{
					String mysql = " DELETE FROM WF_CCStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + flowID + "')";
					DBAccess.RunSQL(mysql);
					for (DataRow dr : dt.Rows)
					{
						CCStation ne = new CCStation();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.getValue(dc.ColumnName) instanceof String) ? dr.getValue(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}


//							switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node"))
							{
									if (val.length() == 3)
									{
										val = flowID + val.substring(1);
									}
									else if (val.length() == 4)
									{
										val = flowID + val.substring(2);
									}
									else if (val.length() == 5)
									{
										val = flowID + val.substring(3);
									}
							}
							else
							{
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Save();
					}
			}
			else
			{
					// infoErr += "Error:" + dt.TableName;
			}
		}


			///#region 处理数据完整性。
		DBAccess.RunSQL("UPDATE WF_Cond SET FK_Node=NodeID WHERE FK_Node=0");
		DBAccess.RunSQL("UPDATE WF_Cond SET ToNodeID=NodeID WHERE ToNodeID=0");

		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE NodeID NOT IN (SELECT NodeID FROM WF_Node)");
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE ToNodeID NOT IN (SELECT NodeID FROM WF_Node) ");
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE FK_Node NOT IN (SELECT NodeID FROM WF_Node) AND FK_Node > 0");

			///#endregion

		if (infoErr.equals(""))
		{
			infoTable = "";
			return fl; // "完全成功。";
		}

		infoErr = "@执行["+path+"]期间出现如下非致命的错误：\t\r" + infoErr + "@ " + infoTable;
		throw new RuntimeException(infoErr);
		}catch(Exception ex)
		{
			String err = "@执行["+path+"]期间出现如下致命的错误："+ex.getMessage();
			throw new RuntimeException(err); 
		}

	}
	public final Node DoNewNode(int x, int y) throws Exception
	{
		Node nd = new Node();
		int idx = this.getHisNodes().size();
		if (idx == 0)
		{
			idx++;
		}

		while (true)
		{
			String strID = this.getNo() + StringHelper.padLeft(String.valueOf(idx), 2, '0');
			nd.setNodeID(Integer.parseInt(strID));
			if (!nd.getIsExits())
			{
				break;
			}
			idx++;
		}

		nd.setHisNodeWorkType(NodeWorkType.Work);
		nd.setName("节点" + idx);
		nd.setHisNodePosType(NodePosType.Mid);
		nd.setFK_Flow(this.getNo());
		nd.setFlowName(this.getName());
		nd.setX(x);
		nd.setY(y);
		nd.setStep(idx);

		//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
		nd.setCondModel(CondModel.SendButtonSileSelect); //默认的发送方向.
		nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
		nd.setFormType(NodeFormType.FoolForm); //表单类型.
		
		
		//为创建节点设置默认值 @于庆海. 
		String file= SystemConfig.getPathOfDataUser()+"XML/DefaultNewNodeAttr.xml";
		if ((new java.io.File(file)).isFile())
		{
			DataSet ds = new DataSet();
			ds.readXml(file);

			DataTable dt = ds.Tables.get(0);
			for (DataColumn dc : dt.Columns)
			{
				nd.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
			}
		}
		
		nd.Insert();
		nd.CreateMap();

		//通用的人员选择器.
		BP.WF.Template.Selector select = new Selector(nd.getNodeID());
		select.setSelectorModel(SelectorModel.GenerUserSelecter);
		select.Update();

		//设置审核组件的高度
		DBAccess.RunSQL("UPDATE WF_Node SET FWC_H=300,FTC_H=300 WHERE NodeID='" + nd.getNodeID() + "'");

		// 周朋@于庆海需要翻译.
		CreatePushMsg(nd);
		return nd;
	}
	 public final void CreatePushMsg(Node nd) throws Exception
		{
			// 周朋@于庆海需要翻译.
			if (SystemConfig.getIsEnableCCIM() == false)
			{
				return;
			}

			//创建发送短消息,为默认的消息.
			BP.WF.Template.PushMsg pm = new BP.WF.Template.PushMsg();
			int i = pm.Retrieve(PushMsgAttr.FK_Event, EventListOfNode.SendSuccess, PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Flow, nd.getFK_Flow());
			if (i == 0)
			{
				pm.setFK_Event(EventListOfNode.SendSuccess);
				pm.setFK_Node(nd.getNodeID());
				pm.setFK_Flow(this.getNo());

				pm.setSMSPushWay(1); // 发送短消息.
				pm.setMailPushWay(0); //不发送邮件消息.
				pm.setMyPK(DBAccess.GenerGUID());
				pm.Insert();
			}

			//设置退回消息提醒.
			i = pm.Retrieve(PushMsgAttr.FK_Event, EventListOfNode.ReturnAfter, PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Flow, nd.getFK_Flow());
			if (i == 0)
			{
				pm.setFK_Event(EventListOfNode.ReturnAfter);
				pm.setFK_Node(nd.getNodeID());
				pm.setFK_Flow(this.getNo());

				pm.setSMSPushWay(1); // 发送短消息.
				pm.setMailPushWay(0); //不发送邮件消息.
				pm.setMyPK(DBAccess.GenerGUID());
				pm.Insert();
			}
		}
	/** 
	 执行新建
	 @param flowSort 类别
	 @param flowName 流程名称
	 @param model 数据存储模式
	 @param pTable 数据存储物理表
	 @param FlowMark 流程标记
	 * @throws Exception 
	*/
	public final String DoNewFlow(String flowSort, String flowName, DataStoreModel model, String pTable, String FlowMark) throws Exception
	{
		try
		{
			//检查参数的完整性.
			if (StringHelper.isNullOrEmpty(pTable) == false && pTable.length() >= 1)
			{
				String c = pTable.substring(0, 1);
				if (DataType.IsNumStr(c) == true)
				{
					throw new RuntimeException("@非法的流程数据表(" + pTable + "),它会导致ccflow不能创建该表.");
				}
			}

			this.setName(flowName);
			if (StringHelper.isNullOrWhiteSpace(this.getName()))
			{
				this.setName("新建流程" + this.getNo()); //新建流程.
			}

			this.setNo(this.GenerNewNoByKey(FlowAttr.No));
			this.setHisDataStoreModel(model);
			this.setPTable(pTable);
			this.setFK_FlowSort(flowSort);
			this.setFlowMark(FlowMark);

			if (StringHelper.isNullOrEmpty(FlowMark) == false)
			{
				if (this.IsExit(FlowAttr.FlowMark, FlowMark))
				{
					throw new RuntimeException("@该流程标示:" + FlowMark + "已经存在于系统中.");
				}
			}

			//给初始值
			//this.Paras = "@StartNodeX=10@StartNodeY=15@EndNodeX=40@EndNodeY=10";
			this.setParas("@StartNodeX=200@StartNodeY=50@EndNodeX=200@EndNodeY=350");
			this.Save();


				///#region 删除有可能存在的历史数据.
			Flow fl = new Flow(this.getNo());
			fl.DoDelData();
			fl.DoDelete();

			this.Save();

				///#endregion 删除有可能存在的历史数据.

			Node nd = new Node();
			nd.setNodeID(Integer.parseInt(this.getNo() + "01"));
			nd.setName("开始节点"); // "开始节点";
			nd.setStep(1);
			nd.setFK_Flow(this.getNo());
			nd.setFlowName(this.getName());
			nd.setHisNodePosType(NodePosType.Start);
			nd.setHisNodeWorkType(NodeWorkType.StartWork);
			nd.setX(200);
			nd.setY(150);
			nd.setICON("前台");
			


			//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
			nd.setCondModel(CondModel.SendButtonSileSelect); //默认的发送方向.
			nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
			nd.setFormType(NodeFormType.FoolForm); //表单类型.

			nd.Insert();

			nd.CreateMap();
			nd.getHisWork().CheckPhysicsTable();

			// 周朋@于庆海需要翻译.
            CreatePushMsg(nd);
			//通用的人员选择器.
			BP.WF.Template.Selector select = new Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();



			nd = new Node();
			nd.setNodeID(Integer.parseInt(this.getNo() + "02"));
			nd.setName("节点2"); // "结束节点";
			nd.setStep(2);
			nd.setFK_Flow(this.getNo());
			nd.setFlowName(this.getName());
			nd.setHisNodePosType(NodePosType.End);
			nd.setHisNodeWorkType(NodeWorkType.Work);
			nd.setX(200);
			nd.setY(250);
			nd.setICON("审核");

			//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
			nd.setCondModel(CondModel.SendButtonSileSelect); //默认的发送方向.
			nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
			nd.setFormType(NodeFormType.FoolForm); //表单类型.
			
            //为创建节点设置默认值 @于庆海. 
			String fileNewNode = SystemConfig.getPathOfDataUser() + "XML/DefaultNewNodeAttr.xml";
			if ((new java.io.File(fileNewNode)).isFile())
			{
				DataSet ds_NodeDef = new DataSet();
				ds_NodeDef.readXml(fileNewNode);

				DataTable dt = ds_NodeDef.Tables.get(0);
				for (DataColumn dc : dt.Columns)
				{
					nd.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
				}
			}

			nd.Insert();
			nd.CreateMap();
			nd.getHisWork().CheckPhysicsTable();
			 // 周朋@于庆海需要翻译.
            CreatePushMsg(nd);

			//通用的人员选择器.
			select = new Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();


			BP.Sys.MapData md = new BP.Sys.MapData();
			md.setNo("ND" + Integer.parseInt(this.getNo()) + "Rpt");
			md.setName(this.getName());
			md.Save();

			// 装载模版.
			String file = BP.Sys.SystemConfig.getPathOfDataUser() + "XML/TempleteSheetOfStartNode.xml";
			if ((new java.io.File(file)).isFile())
			{
				//如果存在开始节点表单模版
				DataSet ds = new DataSet();
				ds.readXml(file);

				String nodeID = "ND" + Integer.parseInt(this.getNo() + "01");
				BP.Sys.MapData.ImpMapData(nodeID, ds);
			}
			else
			{

					///#region 生成CCForm 的装饰.
				FrmImg img = new FrmImg();
				img.setMyPK("Img" + DateUtils.getCurrentDate("yyMMddhhmmss") + WebUser.getNo());
				img.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				img.setX((float) 577.26);
				img.setY((float) 3.45);
				
				img.setW((float) 137);
				img.setH((float) 40);

				img.setImgURL("/ccform;component/Img/LogoBig.png");
				img.setLinkURL("http://ccflow.org");
				img.setLinkTarget("_blank");
				img.Insert();

				FrmLab lab = new FrmLab();

				lab = new FrmLab();
				lab.setMyPK("Lab"
						+ DataType.dateToStr(new Date(), "yyMMddhhmmss")
						+ WebUser.getNo() + 2);
				lab.setText("发起人");
				lab.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				lab.setX((float) 106.48);
				lab.setY((float) 96.08);
				lab.setFontSize(11);
				lab.setFontColor("black");
				lab.setFontName("Portable User Interface");
				lab.setFontStyle("Normal");
				lab.setFontWeight("normal");
				lab.Insert();

				lab = new FrmLab();
				lab.setMyPK("Lab"
						+ DataType.dateToStr(new Date(), "yyMMddhhmmss")
						+ WebUser.getNo() + 3);
				lab.setText("发起时间");
				lab.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				lab.setX((float) 307.64);
				lab.setY((float) 95.17);
				
				lab.setFontSize(11);
				lab.setFontColor("black");
				lab.setFontName("Portable User Interface");
				lab.setFontStyle("Normal");
				lab.setFontWeight("normal");
				lab.Insert();

				lab = new FrmLab();
				lab.setMyPK("Lab"
						+ DataType.dateToStr(new Date(), "yyMMddhhmmss")
						+ WebUser.getNo() + 4);
				lab.setText("新建节点(请修改标题)");
				lab.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				
				lab.setX((float) 294.67);
				lab.setY((float) 8.27);
				
				lab.setFontSize(23);
				lab.setFontColor("Blue");
				lab.setFontName("Portable User Interface");
				lab.setFontStyle("Normal");
				lab.setFontWeight("normal");
				lab.Insert();

				lab = new FrmLab();
				lab.setMyPK("Lab" + DataType.dateToStr(new Date(), "yyMMddhhmmss") + WebUser.getNo() + 5);
				lab.setText("说明:以上内容是ccflow自动产生的，您可以修改/删除它。@为了更方便您的设计您可以到http://ccflow.org官网下载表单模板.");
				lab.setText(lab.getText() + "@因为当前技术问题与silverlight开发工具使用特别说明如下:@");
				lab.setText(lab.getText() + "@1,改变控件位置: ");
				lab.setText(lab.getText() + "@  所有的控件都支持 wasd, 做为方向键用来移动控件的位置， 部分控件支持方向键. ");
				lab.setText(lab.getText() + "@2, 增加textbox, 从表, dropdownlistbox, 的宽度 shift+ -> 方向键增加宽度 shift + <- 减小宽度.");
				lab.setText(lab.getText() + "@3, 保存 windows键 + s.  删除 delete.  复制 ctrl+c   粘帖: ctrl+v.");
				lab.setText(lab.getText() + "@4, 支持全选，批量移动， 批量放大缩小字体., 批量改变线的宽度.");
				lab.setText(lab.getText() + "@5, 改变线的长度： 选择线，点绿色的圆点，拖拉它。.");
				lab.setText(lab.getText() + "@6, 放大或者缩小　label 的字体 , 选择一个多个label , 按 A+ 或者　A－　按钮.");
				lab.setText(lab.getText() + "@7, 改变线或者标签的颜色， 选择操作对象，点工具栏上的调色板.");

				lab.setX((float) 168.24);
				lab.setY((float) 163.7);
				lab.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				lab.setFontSize(11);
				lab.setFontColor("Red");
				lab.setFontName("Portable User Interface");
				lab.setFontStyle("Normal");
				lab.setFontWeight("normal");
				lab.Insert();


				String key = "L" + DateUtils.getCurrentDate("yyMMddhhmmss") + WebUser.getNo();
				FrmLine line = new FrmLine();
				line.setMyPK(key + "_1");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 281.82);
				line.setY1((float) 81.82);
				line.setX2((float) 281.82);
				line.setY2((float) 121.82);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();

				line.setMyPK(key + "_2");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 360);
				line.setY1((float) 80.91);
				line.setX2((float) 360);
				line.setY2((float) 120.91);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();

				line.setMyPK(key + "_3");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 158.82);
				line.setY1((float) 41.82);
				line.setX2((float) 158.82);
				line.setY2((float) 482.73);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();

				line.setMyPK(key + "_4");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 81.55);
				line.setY1((float) 80);
				line.setX2((float) 718.82);
				line.setY2((float) 80);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();


				line.setMyPK(key + "_5");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 81.82);
				line.setY1((float) 40);
				line.setX2((float) 81.82);
				line.setY2((float) 480.91);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();

				line.setMyPK(key + "_6");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 81.82);
				line.setY1((float) 481.82);
				line.setX2((float) 720);
				line.setY2((float) 481.82);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();
				
				line.setMyPK(key + "_7");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 83.36);
				line.setY1((float) 40.91);
				line.setX2((float) 717.91);
				line.setY2((float) 40.91);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();
				
				line.setMyPK(key + "_8");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 83.36);
				line.setY1((float) 120.91);
				line.setX2((float) 717.91);
				line.setY2((float) 120.91);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();
				
				line.setMyPK(key + "_9");
				line.setFK_MapData("ND" + Integer.parseInt(this.getNo() + "01"));
				line.setX1((float) 719.09);
				line.setY1((float) 40);
				line.setX2((float) 719.09);
				line.setY2((float) 482.73);
				line.setBorderWidth((float) 2);
				line.setBorderColor("Black");
				line.Insert();
			}

			//写入权限.
			WritToGPM(flowSort);

			this.DoCheck_CheckRpt(this.getHisNodes());
			//  Flow.RepareV_FlowData_View();
			return this.getNo();
		}
		catch (RuntimeException ex)
		{
			/**删除垃圾数据.
			*/
			this.DoDelete();

			//提示错误.
			throw new RuntimeException("创建流程错误:" + ex.getMessage());
		}
	}

	/** 
	 写入权限
	 @param flowSort
	*/
	public final void WritToGPM(String flowSort)
	{

		if (Glo.getOSModel() == OSModel.OneMore)
		{
 
		}
		//return;
		/*if (Glo.getOSModel() == OSModel.OneMore)
		{
			String sql = "";

			try
			{
				sql = "DELETE FROM GPM_Menu WHERE FK_App='" + SystemConfig.getSysNo() + "' AND Flag='Flow" + this.getNo() + "'";
				DBAccess.RunSQL(sql);
			}
			catch (java.lang.Exception e)
			{
			}

			// 开始组织发起流程的数据.
			// 取得该流程的目录编号.
			sql = "SELECT No FROM GPM_Menu WHERE Flag='FlowSort" + flowSort + "' AND FK_App='" + BP.Sys.SystemConfig.getSysNo() + "'";
			String parentNoOfMenu = DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (parentNoOfMenu == null)
			{
				throw new RuntimeException("@没有找到该流程的(" + BP.Sys.SystemConfig.getSysNo() + ")目录在GPM系统中,请重新新建此目录。");
			}

			// 取得该功能的主键编号.
			String treeNo = String.valueOf(DBAccess.GenerOID("BP.GPM.Menu"));

			// 插入流程名称.
			String url = SystemConfig.getSysNo() + "WF/MyFlow.htm?FK_Flow=" + this.getNo() + "&FK_Node=" + Integer.parseInt(this.getNo()) + "01";

			sql = "INSERT INTO GPM_Menu(No,Name,ParentNo,IsDir,MenuType,FK_App,IsEnable,Flag,Url)";
			sql += " VALUES('{0}','{1}','{2}',{3},{4},'{5}',{6},'{7}','{8}')";
			sql = String.format(sql, treeNo, this.getName(), parentNoOfMenu, 0, 4, SystemConfig.getSysNo(), 1, "Flow" + this.getNo(), url);
			DBAccess.RunSQL(sql);
		}*/

			///#endregion
	}
	/** 
	 检查报表
	 * @throws Exception 
	 
	*/
	public final void CheckRpt() throws Exception
	{
		this.DoCheck_CheckRpt(this.getHisNodes());
	}
	/** 
	 更新之前做检查
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		this.setVer(BP.DA.DataType.getCurrentDataTimess());
		Node.CheckFlow(this);
		return super.beforeUpdate();
	}

	/** 
	 更新版本号
	 
	*/
	public static void UpdateVer(String flowNo)
	{
		String sql = "UPDATE WF_Flow SET VER='" + BP.DA.DataType.getCurrentDataTimess() + "' WHERE No='" + flowNo + "'";
		DBAccess.RunSQL(sql);
	}
	public final void DoDelete() throws Exception
	{
		//删除流程数据.
		this.DoDelData();

		String sql = "";
		//sql = " DELETE FROM WF_chofflow WHERE FK_Flow='" + this.getNo() + "'";
		sql += "@ DELETE  FROM WF_GenerWorkerlist WHERE FK_Flow='" + this.getNo() + "'";
		sql += "@ DELETE FROM  WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo() + "'";
		sql += "@ DELETE FROM  WF_Cond WHERE FK_Flow='" + this.getNo() + "'";

		//删除消息配置. 周朋@于庆海.
		sql += "@ DELETE FROM WF_PushMsg WHERE FK_Flow='" + this.getNo() + "'";
		// 删除岗位节点。
		sql += "@ DELETE  FROM  WF_NodeStation WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		// 删除方向。
		sql += "@ DELETE FROM  WF_Direction WHERE FK_Flow='" + this.getNo() + "'";

		//删除节点绑定信息.
		sql += "@ DELETE FROM WF_FrmNode  WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		sql += "@ DELETE FROM WF_NodeEmp  WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		sql += "@ DELETE FROM WF_CCEmp WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		sql += "@ DELETE FROM WF_CCDept WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		sql += "@ DELETE FROM WF_CCStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		sql += "@ DELETE FROM WF_NodeReturn WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		sql += "@ DELETE FROM WF_NodeDept WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		sql += "@ DELETE FROM WF_NodeStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		sql += "@ DELETE FROM WF_NodeEmp WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		sql += "@ DELETE FROM WF_NodeToolbar WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		sql += "@ DELETE FROM WF_SelectAccper WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		sql += "@ DELETE FROM WF_TurnTo WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";

 
		// 删除d2d数据.
		//  sql += "@GO DELETE WF_M2M WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		//// 删除配置.
		//sql += "@ DELETE FROM WF_FAppSet WHERE NodeID IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
 
		//// 外部程序设置
		//sql += "@ DELETE FROM WF_FAppSet WHERE  NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		//删除单据.
		sql += "@ DELETE FROM WF_BillTemplate WHERE  NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		//删除权限控制.
		sql += "@ DELETE FROM Sys_FrmSln WHERE FK_Flow='" + this.getNo() + "'";
		//考核表
		sql += "@ DELETE FROM WF_CH WHERE FK_Flow='" + this.getNo() + "'";
		//删除抄送
		sql += "@ DELETE FROM WF_CCList WHERE FK_Flow='" + this.getNo() + "'";
		Nodes nds = new Nodes(this.getNo());
		for (Node nd : nds.ToJavaList())
		{
			// 删除节点所有相关的东西.
			//sql += "@ DELETE  FROM Sys_MapM2M WHERE FK_MapData='ND" + nd.getNodeID() + "'";
			nd.Delete();
		}

		sql += "@ DELETE  FROM WF_Node WHERE FK_Flow='" + this.getNo() + "'";
		sql += "@ DELETE  FROM  WF_LabNote WHERE FK_Flow='" + this.getNo() + "'";

		//删除分组信息
		sql += "@ DELETE FROM Sys_GroupField WHERE FrmID NOT IN(SELECT NO FROM Sys_MapData)";


			///#region 删除流程报表,删除轨迹
		MapData md = new MapData();
		//sunxd 20170714
		//Integer.parseInt()中对像为空时Integer.parseInt()会报错
		//Integer.parseInt()中对像作了空值转换
		md.setNo("ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Rpt");
		md.Delete();

		//删除视图.
		if (DBAccess.IsExitsObject("V_" + this.getNo()))
		{
			DBAccess.RunSQL("DROP VIEW V_" + this.getNo());
		}

		//删除轨迹.
		//sunxd 20170714
		//Integer.parseInt()中对像为空时Integer.parseInt()会报错
		//Integer.parseInt()中对像作了空值转换
		if (DBAccess.IsExitsObject("ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Track") == true)
		{
			DBAccess.RunSQL("DROP TABLE ND" + Integer.parseInt(this.getNo()) + "Track ");
		}


			///#endregion 删除流程报表,删除轨迹.

		// 执行录制的sql scripts.
		DBAccess.RunSQLs(sql);
		this.Delete(); //删除需要移除缓存.

		// Flow.RepareV_FlowData_View();

		////删除权限管理
		//if (BP.WF.Glo.OSModel == OSModel.OneMore)
		//{
		//    try
		//    {
		//        DBAccess.RunSQL("DELETE FROM GPM_Menu WHERE Flag='Flow" + this.getNo() + "' AND FK_App='" + SystemConfig.getSysNo() + "'");
		//    }
		//    catch
		//    {
		//    }
		//}
	}
	/** 是否启用数据模版？
	 
	*/
	public final boolean getIsDBTemplate()
	{
		return this.GetValBooleanByKey(FlowAttr.IsDBTemplate);
	}

		///#endregion
}