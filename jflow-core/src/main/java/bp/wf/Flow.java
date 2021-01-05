package bp.wf;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.sys.*;
import bp.tools.FileAccess;
import bp.port.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.template.*;
import bp.wf.data.*;
import bp.web.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.math.*;

/** 
 流程
 记录了流程的信息．
 流程的编号，名称，建立时间．
*/
public class Flow extends EntityNoName
{

		///参数属性.
	/** 
	 最大值x
	 * @throws Exception 
	*/
	public final int getMaxX() throws Exception
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
	public final void setMaxX(int value) throws Exception
	{
		this.SetPara("MaxX", value);
	}
	/** 
	 最大值Y
	*/
	public final int getMaxY()throws Exception
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
	public final void setMaxY(int value) throws Exception
	{
		this.SetPara("MaxY", value);
	}
	private void GenerMaxXY()
	{
		
	}
	/** 
	 待办的业务字段 2019-09-25 by zhoupeng
	*/
	public final String getBuessFields()throws Exception
	{
		return this.GetParaString(FlowAttr.BuessFields);
	}
	public final void setBuessFields(String value) throws Exception
	{
		this.SetPara(FlowAttr.BuessFields, value);
	}

		/// 参数属性.


		///业务数据表同步属性.
	/** 
	 同步方式
	*/
	public final DataDTSWay getDTSWay()throws Exception
	{
		return DataDTSWay.forValue(this.GetValIntByKey(FlowAttr.DataDTSWay));
	}
	public final void setDTSWay(DataDTSWay value)throws Exception
	{
		this.SetValByKey(FlowAttr.DataDTSWay, value.getValue());
	}
	public final FlowDTSTime getDTSTime()throws Exception
	{
		return FlowDTSTime.forValue(this.GetValIntByKey(FlowAttr.DTSTime));
	}
	public final void setDTSTime(FlowDTSTime value)throws Exception
	{
		this.SetValByKey(FlowAttr.DTSTime, value.getValue());
	}

	/** 
	 数据源
	*/
	public final String getDTSDBSrc()throws Exception
	{
		String str = this.GetParaString(FlowAttr.DTSDBSrc);
		if (DataType.IsNullOrEmpty(str))
		{
			return "local";
		}
		return str;
	}
	public final void setDTSDBSrc(String value) throws Exception
	{
		this.SetPara(FlowAttr.DTSDBSrc, value);
	}
	/** 
	 业务表
	*/
	public final String getDTSBTable()throws Exception
	{
		return this.GetParaString(FlowAttr.DTSBTable);
	}
	public final void setDTSBTable(String value) throws Exception
	{
		this.SetPara(FlowAttr.DTSBTable, value);
	}
	public final String getDTSBTablePK()throws Exception
	{
		return this.GetParaString(FlowAttr.DTSBTablePK);
	}
	public final void setDTSBTablePK(String value) throws Exception
	{
		this.SetPara(FlowAttr.DTSBTablePK, value);
	}
	/** 
	 要同步的节点s
	*/
	public final String getDTSSpecNodes()throws Exception
	{
		return this.GetParaString(FlowAttr.DTSSpecNodes);
	}
	public final void setDTSSpecNodes(String value) throws Exception
	{
		this.SetPara(FlowAttr.DTSSpecNodes, value);
	}
	/** 
	 同步的字段对应关系.
	*/
	public final String getDTSFields()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.DTSFields);
	}
	public final void setDTSFields(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.DTSFields, value);
	}

		/// 业务数据表同步属性.


		///基础属性.
	/** 
	 消息推送.
	*/
	public final PushMsgs getPushMsgs()throws Exception
	{
		Entities ens = this.GetEntitiesAttrFromAutoNumCash(new PushMsgs(), PushMsgAttr.FK_Flow, this.getNo());
		return ens instanceof PushMsgs ? (PushMsgs)ens : null;
	}
	/** 
	 流程事件实体
	*/
	public final String getFlowEventEntity()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.FlowEventEntity);
	}
	public final void setFlowEventEntity(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.FlowEventEntity, value);
	}
	/** 
	 流程表单类型
	*/
	public final FlowFrmModel getFlowFrmModel()throws Exception
	{
		return FlowFrmModel.forValue(this.GetValIntByKey(FlowAttr.FlowFrmModel));
	}
	public final void setFlowFrmModel(FlowFrmModel value)throws Exception
	{
		this.SetValByKey(FlowAttr.FlowFrmModel, value.getValue());
	}
	/** 
	 url
	*/
	public final String getFrmUrl()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.FrmUrl);
	}
	public final void setFrmUrl(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.FrmUrl, value);
	}

	/** 
	 流程标记
	*/
	public final String getFlowMark()throws Exception
	{
		String str = this.GetValStringByKey(FlowAttr.FlowMark);
		if (str.equals(""))
		{
			return this.getNo();
		}
		return str;
	}
	public final void setFlowMark(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.FlowMark, value);
	}
	public final String getOrgNo()throws Exception
	{
		String str = this.GetValStringByKey(FlowAttr.OrgNo);
		return str;
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.OrgNo, value);
	}
	/** 
	 节点图形类型
	*/
	public final int getChartType()throws Exception
	{
		return this.GetValIntByKey(FlowAttr.ChartType);
	}
	public final void setChartType(int value) throws Exception
	{
		this.SetValByKey(FlowAttr.ChartType, value);
	}

		///


		///发起限制.
	/** 
	 发起限制.
	*/
	public final StartLimitRole getStartLimitRole()throws Exception
	{
		return StartLimitRole.forValue(this.GetValIntByKey(FlowAttr.StartLimitRole));
	}
	public final void setStartLimitRole(StartLimitRole value)throws Exception
	{
		this.SetValByKey(FlowAttr.StartLimitRole, value.getValue());
	}
	/** 
	 发起内容
	*/
	public final String getStartLimitPara()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.StartLimitPara);
	}
	public final void setStartLimitPara(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.StartLimitPara, value);
	}
	public final String getStartLimitAlert()throws Exception
	{
		String s = this.GetValStringByKey(FlowAttr.StartLimitAlert);
		if (s.equals(""))
		{
			return "您已经启动过该流程，不能重复启动。";
		}
		return s;
	}
	public final void setStartLimitAlert(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.StartLimitAlert, value);
	}
	/** 
	 限制触发时间
	*/
	public final StartLimitWhen getStartLimitWhen()throws Exception
	{
		return StartLimitWhen.forValue(this.GetValIntByKey(FlowAttr.StartLimitWhen));
	}
	public final void setStartLimitWhen(StartLimitWhen value)throws Exception
	{
		this.SetValByKey(FlowAttr.StartLimitWhen, value.getValue());
	}

		/// 发起限制.


		///导航模式
	/** 
	 发起导航方式
	*/
	public final StartGuideWay getStartGuideWay()throws Exception
	{
		return StartGuideWay.forValue(this.GetValIntByKey(FlowAttr.StartGuideWay));
	}
	public final void setStartGuideWay(StartGuideWay value)throws Exception
	{
		this.SetValByKey(FlowAttr.StartGuideWay, value.getValue());
	}
	/** 
	 右侧的超链接
	*/
	public final String getStartGuideLink()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.StartGuideLink);
	}
	public final void setStartGuideLink(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.StartGuideLink, value);
	}
	/** 
	 标签
	*/
	public final String getStartGuideLab()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.StartGuideLab);
	}
	public final void setStartGuideLab(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.StartGuideLab, value);
	}
	/** 
	 前置导航参数1
	*/
	public final String getStartGuidePara1()throws Exception
	{
		if (this.getStartGuideWay() == StartGuideWay.BySelfUrl)
		{
			String str = this.GetValStringByKey(FlowAttr.StartGuidePara1);
			if (str.contains("?") == false)
			{
				str = str + "?1=2";
			}
			return str.replace("~", "'");
		}
		else
		{
			return this.GetValStringByKey(FlowAttr.StartGuidePara1);
		}
	}
	public final void setStartGuidePara1(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.StartGuidePara1, value);
	}


	/** 
	 流程发起参数2
	*/
	public final String getStartGuidePara2()throws Exception
	{
		String str = this.GetValStringByKey(FlowAttr.StartGuidePara2);
		str = str.replace("~", "'");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			if (this.getStartGuideWay() == bp.wf.template.StartGuideWay.ByHistoryUrl)
			{
			}
		}
		return str;
	}
	public final void setStartGuidePara2(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.StartGuidePara2, value);
	}
	/** 
	 流程发起参数3
	*/
	public final String getStartGuidePara3()throws Exception
	{
		return this.GetValStrByKey(FlowAttr.StartGuidePara3);
	}
	public final void setStartGuidePara3(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.StartGuidePara3, value);
	}
	/** 
	 是否启用数据重置按钮？
	*/
	public final boolean getIsResetData()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsResetData);
	}
	/** 
	 是否启用导入历史数据按钮?
	*/
	public final boolean getIsImpHistory()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsImpHistory);
	}
	/** 
	 是否自动装载上一笔数据?
	*/
	public final boolean getIsLoadPriData()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsLoadPriData);
	}

		///


		///其他属性
	/** 
	 流程删除规则
	*/
	public final FlowDeleteRole getFlowDeleteRole()throws Exception
	{
		return FlowDeleteRole.forValue(this.GetValIntByKey(FlowAttr.FlowDeleteRole));
	}
	/** 
	 草稿规则
	*/
	public final DraftRole getDraftRole()throws Exception
	{
		return DraftRole.forValue(this.GetValIntByKey(FlowAttr.Draft));
	}
	public final void setDraftRole(DraftRole value)throws Exception
	{
		this.SetValByKey(FlowAttr.Draft, value.getValue());
	}
	public String Tag = null;
	/** 
	 运行类型
	*/
	public final FlowRunWay getHisFlowRunWay()throws Exception
	{
		return FlowRunWay.forValue(this.GetValIntByKey(FlowAttr.FlowRunWay));
	}
	public final void setHisFlowRunWay(FlowRunWay value)throws Exception
	{
		this.SetValByKey(FlowAttr.FlowRunWay, value.getValue());
	}
	/** 
	 运行对象
	*/
	public final String getRunObj()throws Exception
	{
		return this.GetValStrByKey(FlowAttr.RunObj);
	}
	public final void setRunObj(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.RunObj, value);
	}
	/** 
	 时间点规则
	*/
	public final SDTOfFlowRole getSDTOfFlowRole()throws Exception
	{
		return SDTOfFlowRole.forValue(this.GetValIntByKey(FlowAttr.SDTOfFlowRole));
	}
	/** 
	 按照SQL来计算流程完成时间
	*/
	public final String getSDTOfFlowRoleSQL()throws Exception
	{
		return this.GetValStrByKey(FlowAttr.SDTOfFlowRoleSQL);
	}
	/** 
	 流程部门数据查询权限控制方式
	*/
	public final FlowDeptDataRightCtrlType getHisFlowDeptDataRightCtrlType()throws Exception
	{
		return FlowDeptDataRightCtrlType.forValue(this.GetValIntByKey(FlowAttr.DRCtrlType));
	}
	public final void setHisFlowDeptDataRightCtrlType(FlowDeptDataRightCtrlType value)throws Exception
	{
		this.SetValByKey(FlowAttr.DRCtrlType, value);
	}
	/** 
	 流程应用类型
	*/
	public final FlowAppType getFlowAppType()throws Exception
	{
		return FlowAppType.forValue(this.GetValIntByKey(FlowAttr.FlowAppType));
	}
	public final void setFlowAppType(FlowAppType value)throws Exception
	{
		this.SetValByKey(FlowAttr.FlowAppType, value.getValue());
	}
	/** 
	 流程备注的表达式
	*/
	public final String getFlowNoteExp()throws Exception
	{
		return this.GetValStrByKey(FlowAttr.FlowNoteExp);
	}
	public final void setFlowNoteExp(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.FlowNoteExp, value);
	}
	/** 
	 是否可以在手机里启用
	*/
	public final boolean getIsStartInMobile()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsStartInMobile);
	}
	public final void setIsStartInMobile(boolean value) throws Exception
	{
		this.SetValByKey(FlowAttr.IsStartInMobile, value);
	}

		/// 业务处理


		///创建新工作.
	/** 
	 创建新工作web方式调用的
	 
	 @return 
	*/
	public final Work NewWork()throws Exception
	{
		return NewWork(WebUser.getNo());
	}
	/** 
	 创建新工作.web方式调用的
	 
	 @param empNo 人员编号
	 @return 
	*/
	public final Work NewWork(String empNo)throws Exception
	{
		Emp emp = new Emp(empNo);
		return NewWork(emp, null);
	}
	/** 
	 产生一个开始节点的新工作
	 
	 @param emp 发起人
	 @param paras 参数集合,如果是CS调用，要发起子流程，要从其他table里copy数据,就不能从request里面取,可以传递为null.
	 @return 返回的Work.
	*/
	public final Work NewWork(Emp emp, Hashtable paras)throws Exception
	{
		// 检查是否可以发起该流程？
		if (Glo.CheckIsCanStartFlow_InitStartFlow(this) == false)
		{
			throw new RuntimeException("err@您违反了该流程的【" + this.getStartLimitRole() + "】限制规则。" + this.getStartLimitAlert());
		}

		//如果是bs系统.
		if (paras == null)
		{
			paras = new Hashtable();
		}
		if (SystemConfig.getIsBSsystem() == true)
		{
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String k = (String) enu.nextElement();
				if (k == null|| k.equals("OID") || k.equals("WorkID"))
				{
					continue;
				}
				if (paras.containsKey(k))
				{
					paras.put(k, ContextHolderUtils.getRequest().getParameter(k)); // BP.Sys.Glo.Request.QueryString[k];
				}
				else
				{
					paras.put(k, ContextHolderUtils.getRequest().getParameter(k));
				}
			}
			
			
		}

		//开始节点.
		bp.wf.Node nd = new bp.wf.Node(this.getStartNodeID());

		//从草稿里看看是否有新工作？
		Work wk = nd.getHisWork();
		try
		{
			wk.ResetDefaultVal();
		}
		catch (RuntimeException ex)
		{
			wk.ResetDefaultVal();
		}

		String dbstr = SystemConfig.getAppCenterDBVarStr();

		Paras ps = new Paras();
		GERpt rpt = this.getHisGERpt();

		//是否新创建的WorkID
		boolean IsNewWorkID = false;
		/*如果要启用草稿,就创建一个新的WorkID .*/
		if (this.getDraftRole() != DraftRole.None && nd.getIsStartNode())
		{
			IsNewWorkID = true;
		}

		String errInfo = "";
		try
		{
			//从报表里查询该数据是否存在？
			if (this.getGuestFlowRole() == true && DataType.IsNullOrEmpty(GuestUser.getNo()) == false)
			{
				/*是客户参与的流程，并且具有客户登陆的信息。*/
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

				//如果没有启用草稿，并且存在草稿就取第一条
				if (dt.Rows.size() > 0)
				{
					wk.setOID(Long.parseLong(dt.Rows.get(0).getValue(0).toString()));
					wk.RetrieveFromDBSources();
					int nodeID = Integer.parseInt(dt.Rows.get(0).getValue(1).toString());
					if (nodeID != this.getStartNodeID())
					{
						String error = "err@这里出现了blank的状态下流程运行到其它的节点上去了的情况，当前停留节点:" + nodeID;
						Log.DefaultLogWriteLineError(error);
					}
				}
			}

			//启用草稿或空白就创建WorkID
			if (wk.getOID() == 0)
			{
				/* 说明没有空白,就创建一个空白..*/
				wk.ResetDefaultVal();
				wk.setRec(WebUser.getNo());

				//   wk.SetValByKey(GERptAttr.RecText, emp.getName());
				//  wk.SetValByKey(GERptAttr.Emps, emp.getNo());

				//  wk.SetValByKey(WorkAttr.RDT, DataType.getCurrentDataTime());
				// wk.SetValByKey(WorkAttr.CDT, DataType.getCurrentDataTime());

				wk.SetValByKey(GERptAttr.WFState, WFState.Blank.getValue());

				wk.setOID(DBAccess.GenerOID("WorkID")); //这里产生WorkID ,这是唯一产生WorkID的地方.

				//把尽量可能的流程字段放入，否则会出现冲掉流程字段属性.
				wk.SetValByKey(GERptAttr.FK_NY, bp.da.DataType.getCurrentYearMonth());
				wk.SetValByKey(GERptAttr.FK_Dept, emp.getFK_Dept());
				wk.setFID(0);

				try
				{
					wk.DirectInsert();
				}
				catch (RuntimeException ex)
				{
					wk.CheckPhysicsTable();
					//wk.DirectInsert();
				}

				//设置参数.
				for (Object k : paras.keySet())
				{
					if(k == null)
						continue;
					rpt.SetValByKey(k.toString(), paras.get(k));
				}

				if (this.getPTable().equals(wk.getEnMap().getPhysicsTable()))
				{
					/*如果开始节点表与流程报表相等.*/
					rpt.setOID(wk.getOID());
					rpt.RetrieveFromDBSources();
					rpt.setFID(0);
					rpt.setFlowStartRDT(DataType.getCurrentDataTime());
					rpt.setTitle(bp.wf.WorkFlowBuessRole.GenerTitle(this, wk));
					rpt.setWFState(WFState.Blank);
					rpt.setFlowStarter(emp.getNo());
					rpt.setFK_NY(DataType.getCurrentYearMonth());
					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
					{
						rpt.setFlowEmps("@" + emp.getName() + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.getNo() + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.getNo() + "," + emp.getName() + "@");
					}

					rpt.setFlowEnderRDT(DataType.getCurrentDataTime());
					rpt.setFlowStartRDT(DataType.getCurrentDataTime());

					rpt.setFK_Dept(emp.getFK_Dept());
					rpt.setFlowEnder(emp.getNo());
					rpt.setFlowEndNode(this.getStartNodeID());
					rpt.setFlowStarter(emp.getNo());
					rpt.setWFState(WFState.Blank);
					rpt.setFID(0);
					rpt.DirectUpdate();
				}
				else
				{
					rpt.setOID(wk.getOID());
					rpt.setFID(0);
					rpt.setFlowStartRDT(DataType.getCurrentDataTime());
					rpt.setFlowEnderRDT(DataType.getCurrentDataTime());

					rpt.setTitle(bp.wf.WorkFlowBuessRole.GenerTitle(this, wk));
					// rpt.Title = WebUser.getNo() + "," + WebUser.getName() + "在" + DataType.getCurrentDate()CNOfShort + "发起.";

					rpt.setWFState(WFState.Blank);
					rpt.setFlowStarter(emp.getNo());

					rpt.setFlowEndNode(this.getStartNodeID());
					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
					{
						rpt.setFlowEmps("@" + emp.getName() + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.getNo() + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.getNo() + "," + emp.getName() + "@");
					}

					rpt.setFK_NY(DataType.getCurrentYearMonth());
					rpt.setFK_Dept(emp.getFK_Dept());
					rpt.setFlowEnder(emp.getNo());
					rpt.setFlowStarter(emp.getNo());
					rpt.InsertAsOID(wk.getOID());
				}

				//调用 OnCreateWorkID的方法.  add by zhoupeng 2016.12.4 for LIMS.
				ExecEvent.DoFlow(EventListFlow.FlowOnCreateWorkID, wk, nd, null);
			}

			if (wk.getOID() != 0)
			{
				rpt.setOID(wk.getOID());
				rpt.RetrieveFromDBSources();

				rpt.setFID(0);
				rpt.setFlowStartRDT(DataType.getCurrentDataTime());
				rpt.setFlowEnderRDT(DataType.getCurrentDataTime());

				//在发送的时候有更新.
				//   rpt.DirectUpdate();
			}
		}
		catch (RuntimeException ex)
		{
			wk.CheckPhysicsTable();

			//检查报表.
			this.CheckRpt();
			throw new RuntimeException("@创建工作失败：请您刷新一次，如果问题仍然存在请反馈给管理员，技术信息：" + ex.getStackTrace() + " @ 技术信息:" + ex.getMessage());
		}

		//在创建WorkID的时候调用的事件.
		ExecEvent.DoFlow(EventListFlow.FlowOnCreateWorkID, wk, nd, null);


			///copy数据.
		// 记录这个id ,不让其它在复制时间被修改。
		long newOID = wk.getOID();
		if (IsNewWorkID == true)
		{
			// 处理传递过来的参数。
			int i = 0;

			String expKeys = "OID,DoType,HttpHandlerName,DoMethod,t,";
			for (Object k : paras.keySet())
			{
				if(k==null)
					continue;
				if (expKeys.indexOf("," + k.toString()+ ",") != -1)
				{
					continue;
				}

				String str = paras.get(k) instanceof String ? (String)paras.get(k) : null;
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}
				i++;
				wk.SetValByKey(k.toString(), str);
			}

			if (i >= 3)
			{
				wk.DirectUpdate();
			}
		}

			/// copy数据.


			///处理删除草稿的需求。
		if (paras.containsKey(StartFlowParaNameList.IsDeleteDraft) && paras.get(StartFlowParaNameList.IsDeleteDraft).toString().equals("1"))
		{
			/*是否要删除Draft */
			long oid = wk.getOID();
			try
			{
				//wk.ResetDefaultValAllAttr();
				wk.DirectUpdate();
			}
			catch (RuntimeException ex)
			{
				wk.Update();
				bp.da.Log.DebugWriteError("创建新工作错误，但是屏蔽了异常,请检查默认值的问题：" + ex.getMessage());
			}

			MapDtls dtls = wk.getHisMapDtls();
			for (MapDtl dtl : dtls.ToJavaList())
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + oid + "'");
			}

			//删除附件数据。
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + wk.getNodeID() + "' AND RefPKVal='" + wk.getOID() + "'");
			wk.setOID(newOID);
		}

			/// 处理删除草稿的需求。


			///处理开始节点, 如果传递过来 FromTableName 就是要从这个表里copy数据。
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

			String innerKeys = ",OID,RDT,CDT,FID,WFState,WorkID,WORKID,WFSTATE";
			for (DataColumn dc : dt.Columns)
			{
				if (innerKeys.contains("," + dc.ColumnName.toUpperCase() + ","))
				{
					continue;
				}

				wk.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName).toString());
				rpt.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName).toString());
			}
			rpt.Update();
		}

			/// 处理开始节点, 如果传递过来 FromTableName 就是要从这个表里copy数据。


			///获取特殊标记变量
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

		if (paras.containsKey("PWorkID") == true && paras.containsKey("PNodeID") == true && Long.parseLong(paras.get("PWorkID").toString()) != 0 && Integer.parseInt(paras.get("PNodeID").toString()) != 0)
		{
			if (paras.get("PFlowNo") == null)
			{
				int nodeId = Integer.parseInt(paras.get("PNodeID").toString());
				Node node = new Node(nodeId);
				PFlowNo = node.getFK_Flow();

			}
			else
			{
				PFlowNo = paras.get("PFlowNo").toString();
			}

			PNodeIDStr = paras.get("PNodeID").toString();
			PWorkIDStr = paras.get("PWorkID").toString();
			PFIDStr = "0";
			if (paras.containsKey("PFID") == true)
			{
				PFIDStr = paras.get("PFID").toString(); //父流程.
			}
		}

			/// 获取特殊标记变量


			/// 判断是否装载上一条数据.
		if (this.getIsLoadPriData() == true && this.getStartGuideWay() == bp.wf.template.StartGuideWay.None)
		{
			/* 如果需要从上一个流程实例上copy数据. */
			String sql = "SELECT OID FROM " + this.getPTable() + " WHERE FlowStarter='" + WebUser.getNo() + "' AND OID!=" + wk.getOID() + " ORDER BY OID DESC";
			String workidPri = DBAccess.RunSQLReturnStringIsNull(sql, "0");
			if (workidPri.equals("0"))
			{
				/*说明没有第一笔数据.*/
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

			///  判断是否装载上一条数据.


			///处理流程之间的数据传递1。
		if (DataType.IsNullOrEmpty(PNodeIDStr) == false && DataType.IsNullOrEmpty(PWorkIDStr) == false)
		{
			long PWorkID = Long.parseLong(PWorkIDStr);
			long PNodeID = 0;
			if (CopyFormWorkID != null)
			{
				PNodeID = Long.parseLong(PNodeIDStr);
			}

			/* 如果是从另外的一个流程上传递过来的，就考虑另外的流程数据。*/


				///copy 首先从父流程的NDxxxRpt copy.
			long pWorkIDReal = 0;
			Flow pFlow = new Flow(PFlowNo);
			String pOID = "";
			if (DataType.IsNullOrEmpty(PFIDStr) == true || PFIDStr.equals("0"))
			{
				pOID = String.valueOf(PWorkID);
			}
			else
			{
				pOID = PFIDStr;
			}

			String sql = "SELECT * FROM " + pFlow.getPTable() + " WHERE OID=" + pOID;
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() != 1)
			{
				throw new RuntimeException("@不应该查询不到父流程的数据[" + sql + "], 可能的情况之一,请确认该父流程的调用节点是子线程，但是没有把子线程的FID参数传递进来。");
			}

			wk.Copy(dt.Rows.get(0));
			rpt.Copy(dt.Rows.get(0));

			//设置单号为空.
			wk.SetValByKey("BillNo", "");
			rpt.setBillNo("");

				/// copy 首先从父流程的NDxxxRpt copy.


				///从调用的节点上copy.
			bp.wf.Node fromNd = new bp.wf.Node(Integer.parseInt(PNodeIDStr));
			Work wkFrom = fromNd.getHisWork();
			wkFrom.setOID(PWorkID);
			if (wkFrom.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("@父流程的工作ID不正确，没有查询到数据" + PWorkID);
			}
			//wk.Copy(wkFrom);
			//rpt.Copy(wkFrom);

				/// 从调用的节点上copy.


				///获取web变量.
			for (Object k : paras.keySet())
			{
				if(k==null)
					continue;
				if (k.toString().equals("OID"))
				{
					continue;
				}

				wk.SetValByKey(k.toString(), paras.get(k));
				rpt.SetValByKey(k.toString(), paras.get(k));
			}

				/// 获取web变量.


				///特殊赋值.
			wk.setOID(newOID);
			rpt.setOID(newOID);

			// 在执行copy后，有可能这两个字段会被冲掉。
			if (CopyFormWorkID != null)
			{
				/*如果不是 执行的从已经完成的流程copy.*/

				wk.SetValByKey(GERptAttr.PFlowNo, PFlowNo);
				wk.SetValByKey(GERptAttr.PNodeID, PNodeID);
				wk.SetValByKey(GERptAttr.PWorkID, PWorkID);

				rpt.SetValByKey(GERptAttr.PFlowNo, PFlowNo);
				rpt.SetValByKey(GERptAttr.PNodeID, PNodeID);
				rpt.SetValByKey(GERptAttr.PWorkID, PWorkID);

				//忘记了增加这句话.
				rpt.SetValByKey(GERptAttr.PEmp, WebUser.getNo());

				//要处理单据编号 BillNo .
				if (!this.getBillNoFormat().equals(""))
				{
					rpt.SetValByKey(GERptAttr.BillNo, bp.wf.WorkFlowBuessRole.GenerBillNo(this.getBillNoFormat(), rpt.getOID(), rpt, this.getPTable()));

					//设置单据编号.
					wk.SetValByKey(GERptAttr.BillNo, rpt.getBillNo());
				}

				rpt.SetValByKey(GERptAttr.FID, 0);
				rpt.SetValByKey(GERptAttr.FlowStartRDT, DataType.getCurrentDataTime());
				rpt.SetValByKey(GERptAttr.FlowEnderRDT, DataType.getCurrentDataTime());
				rpt.SetValByKey(GERptAttr.WFState, WFState.Blank.getValue());
				rpt.SetValByKey(GERptAttr.FlowStarter, emp.getNo());
				rpt.SetValByKey(GERptAttr.FlowEnder, emp.getNo());
				rpt.SetValByKey(GERptAttr.FlowEndNode, this.getStartNodeID());
				rpt.SetValByKey(GERptAttr.FK_Dept, emp.getFK_Dept());
				rpt.SetValByKey(GERptAttr.FK_NY, DataType.getCurrentYearMonth());

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getName() + "@");
				}

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getNo() + "@");
				}

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getNo() + "," + emp.getName() + "@");
				}

			}

			if (rpt.getEnMap().getPhysicsTable() != wk.getEnMap().getPhysicsTable())
			{
				wk.Update(); //更新工作节点数据.
			}
			rpt.Update(); // 更新流程数据表.

				/// 特殊赋值.


				///复制其他数据..
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

						//删除以前的数据.
						try
						{
							sql = "DELETE FROM " + dtlData.getEnMap().getPhysicsTable() + " WHERE RefPK=" + wk.getOID();
							DBAccess.RunSQL(sql);
						}
						catch (RuntimeException ex)
						{

						}



						MapDtl dtlFrom = dtlsFrom.get(idx) instanceof MapDtl ? (MapDtl)dtlsFrom.get(idx) : null;

						GEDtls dtlsFromData = new GEDtls(dtlFrom.getNo());
						dtlsFromData.Retrieve(GEDtlAttr.RefPK, PWorkID);
						for (GEDtl geDtlFromData : dtlsFromData.ToJavaList())
						{
							//是否启用多附件
							FrmAttachmentDBs dbs = null;
							if (dtl.getIsEnableAthM() == true)
							{
								//根据从表的OID 获取附件信息
								dbs = new FrmAttachmentDBs();
								dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, geDtlFromData.getOID());
							}

							dtlData.Copy(geDtlFromData);
							dtlData.setRefPK(String.valueOf(wk.getOID()));
							dtlData.setFID(wk.getOID());
							if (this.getNo().equals(PFlowNo) == false && (this.getStartLimitRole() == StartLimitRole.OnlyOneSubFlow))
							{
								dtlData.SaveAsOID(geDtlFromData.getOID()); //为子流程的时候，仅仅允许被调用1次.
							}
							else
							{
								dtlData.InsertAsNew();
								if (dbs != null && dbs.size() != 0)
								{
									//复制附件信息
									FrmAttachmentDB newDB = new FrmAttachmentDB();
									for (FrmAttachmentDB db : dbs.ToJavaList())
									{
										newDB.Copy(db);
										newDB.setRefPKVal(String.valueOf(dtlData.getOID()));
										newDB.setFID(dtlData.getOID());
										newDB.setMyPK(DBAccess.GenerGUID());
										newDB.Insert();
									}
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
					FrmAttachmentDBs athDBs = new FrmAttachmentDBs("ND" + PNodeIDStr, String.valueOf(PWorkID));

					for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
						FrmAttachmentDB athDB_N = new FrmAttachmentDB();
						athDB_N.Copy(athDB);
						athDB_N.setFK_MapData("ND" + toNodeID);
						athDB_N.setRefPKVal(String.valueOf(wk.getOID()));
						athDB_N.setFK_FrmAttachment(
								athDB_N.getFK_FrmAttachment().replace("ND" + PNodeIDStr, "ND" + toNodeID));

						if (athDB_N.getHisAttachmentUploadType() == AttachmentUploadType.Single) {
							/* 如果是单附件. */
							athDB_N.setMyPK(athDB_N.getFK_FrmAttachment() + "_" + wk.getOID());
							if (athDB_N.getIsExits() == true) {
								continue; // 说明上一个节点或者子线程已经copy过了,
											// 但是还有子线程向合流点传递数据的可能，所以不能用break.
							}
							athDB_N.Insert();
						} else {
							athDB_N.setMyPK(
									athDB_N.getUploadGUID() + "_" + athDB_N.getFK_MapData() + "_" + wk.getOID());
							athDB_N.Insert();
						}
					}
				}
			}

				/// 复制表单其他数据.


				///复制独立表单数据.
			//求出来被copy的节点有多少个独立表单.
			FrmNodes fnsFrom = new bp.wf.template.FrmNodes(fromNd.getNodeID());
			if (fnsFrom.size() != 0)
			{
				//求当前节点表单的绑定的表单.
				FrmNodes fns = new bp.wf.template.FrmNodes(nd.getNodeID());
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

							GEEntity geEnFrom = new GEEntity(fnFrom.getFK_Frm());
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

				/// 复制独立表单数据.

		}



			/// 处理流程之间的数据传递1。


			///处理单据编号.
		//生成单据编号.
		if (this.getBillNoFormat().length() > 3)
		{
			Object tempVar = this.getBillNoFormat();
			String billNoFormat = tempVar instanceof String ? (String)tempVar : null;
			String billNo = rpt.getBillNo();
			if (DataType.IsNullOrEmpty(billNo) == true)
			{
				//生成单据编号.
				rpt.setBillNo(bp.wf.WorkFlowBuessRole.GenerBillNo(billNoFormat, rpt.getOID(), rpt, this.getPTable()));
				if (wk.getRow().containsKey(GERptAttr.BillNo) == true)
				{
					wk.SetValByKey(NDXRptBaseAttr.BillNo, rpt.getBillNo());
				}
				rpt.Update();
			}
		}

			/// 处理单据编号.


			///处理流程之间的数据传递2, 如果是直接要跳转到指定的节点上去.
		if (paras.containsKey("JumpToNode") == true)
		{
			wk.setRec(WebUser.getNo());

			//wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
			//wk.FK_Dept = emp.FK_Dept;
			//wk.SetValByKey("FK_DeptName", emp.FK_DeptText);
			//wk.SetValByKey("FK_DeptText", emp.FK_DeptText);
			wk.setFID(0);
			// wk.SetValByKey(GERptAttr.RecText, emp.getName());

			int jumpNodeID = Integer.parseInt(paras.get("JumpToNode").toString());
			Node jumpNode = new Node(jumpNodeID);

			String jumpToEmp = paras.get("JumpToEmp").toString();
			if (DataType.IsNullOrEmpty(jumpToEmp))
			{
				jumpToEmp = emp.getNo();
			}

			WorkNode wn = new WorkNode(wk, nd);
			wn.NodeSend(jumpNode, jumpToEmp);

			WorkFlow wf = new WorkFlow(this, wk.getOID(), wk.getFID());

			bp.wf.GenerWorkFlow gwf = new GenerWorkFlow(rpt.getOID());
			rpt.setWFState(WFState.Runing);
			rpt.Update();

			return wf.GetCurrentWorkNode().getHisWork();
		}

			/// 处理流程之间的数据传递。


			///最后整理wk数据.
		wk.setRec(emp.getNo());
		//  wk.SetValByKey(WorkAttr.RDT, DataType.getCurrentDataTime());
		// wk.SetValByKey(WorkAttr.CDT, DataType.getCurrentDataTime());
		wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
		wk.SetValByKey("FK_Dept", emp.getFK_Dept());
		wk.SetValByKey("FK_DeptName", emp.getFK_DeptText());
		wk.SetValByKey("FK_DeptText", emp.getFK_DeptText());

		if (rpt.getEnMap().getAttrs().Contains("BillNo") == true)
		{
			wk.SetValByKey(NDXRptBaseAttr.BillNo, rpt.getBillNo());
		}

		wk.setFID(0);
		if (wk.getIsExits() == false)
		{
			wk.DirectInsert();
		}
		else
		{
			wk.Update();
		}

			/// 最后整理参数.


			///给generworkflow初始化数据. add 2015-08-06
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
			mygwf.Insert();
		}
		mygwf.setStarter(WebUser.getNo());
		mygwf.setStarterName(WebUser.getName());
		mygwf.setFK_Dept(WebUser.getFK_Dept());
		mygwf.setDeptName(WebUser.getFK_DeptName());
		mygwf.setRDT(DataType.getCurrentDataTime());
		mygwf.setTitle(rpt.getTitle());
		mygwf.setBillNo(rpt.getBillNo());
		if (mygwf.getTitle().contains("@") == true)
		{
			mygwf.setTitle(bp.wf.WorkFlowBuessRole.GenerTitle(this, rpt));
		}
		if (DataType.IsNullOrEmpty(PNodeIDStr) == false && DataType.IsNullOrEmpty(PWorkIDStr) == false)
		{
			if (DataType.IsNullOrEmpty(PFIDStr) == false)
			{
				mygwf.setPFID(Long.parseLong(PFIDStr));
			}
			mygwf.setPEmp(rpt.getPEmp());
			mygwf.setPFlowNo(rpt.getPFlowNo());
			mygwf.setPNodeID(rpt.getPNodeID());
			mygwf.setPWorkID(rpt.getPWorkID());
		}
		mygwf.DirectUpdate();

			/// 给 generworkflow 初始化数据.

		//更新domian.
		DBAccess.RunSQL("UPDATE wf_generworkflow  SET domain=(SELECT domain FROM wf_flowsort WHERE wf_flowsort.NO=wf_generworkflow.FK_FlowSort) where workid=" + wk.getOID());


		return wk;
	}

		/// 创建新工作.


		///其他通用方法.
	public final String DoBTableDTS()throws Exception
	{
		if (this.getDTSWay() == DataDTSWay.None)
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
				WorkNodePlus.DTSData(this, gwf, rpt, new Node(gwf.getFK_Node()), true);
			}
			else
			{
				WorkNodePlus.DTSData(this, gwf, rpt, new Node(gwf.getFK_Node()), false);
			}
			info += "同步成功.";
		}
		return info;
	}
	/** 
	 自动发起
	 
	 @return 
	*/
	public final String DoAutoStartIt()throws Exception
	{
		switch (this.getHisFlowRunWay())
		{
			case SpecEmp: //指定人员按时运行。
				String RunObj = this.getRunObj();
				String FK_Emp = RunObj.substring(0, RunObj.indexOf('@'));
				bp.port.Emp emp = new bp.port.Emp();
				emp.setNo(FK_Emp);
				if (emp.RetrieveFromDBSources() == 0)
				{
					return "启动自动启动流程错误：发起人(" + FK_Emp + ")不存在。";
				}

				WebUser.SignInOfGener(emp);
				String info_send = bp.wf.Dev2Interface.Node_StartWork(this.getNo(), null, null, 0, null, 0, null).ToMsgOfText();
				if (!WebUser.getNo().equals("admin"))
				{
					emp = new bp.port.Emp();
					emp.setNo("admin");
					emp.Retrieve();
					WebUser.SignInOfGener(emp);
					return info_send;
				}
				return info_send;
			case SelectSQLModel: //按数据集合驱动的模式执行。
				break;
			default:
				return "@该流程您没有设置为自动启动的流程类型。";
		}

		String msg = "";
		MapExt me = new MapExt();
		me.setMyPK("ND" + Integer.parseInt(this.getNo()) + "01_" + MapExtXmlList.StartFlow);
		int i = me.RetrieveFromDBSources();
		if (i == 0)
		{
			bp.da.Log.DefaultLogWriteLineError("没有为流程(" + this.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return "没有为流程(" + this.getName() + ")的开始节点设置发起数据,请参考说明书解决.";
		}
		if (DataType.IsNullOrEmpty(me.getTag()))
		{
			bp.da.Log.DefaultLogWriteLineError("没有为流程(" + this.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return "没有为流程(" + this.getName() + ")的开始节点设置发起数据,请参考说明书解决.";
		}

		// 获取从表数据.
		DataSet ds = new DataSet();
		String[] dtlSQLs = me.getTag1().split("[*]", -1);
		for (String sql : dtlSQLs)
		{
			if (DataType.IsNullOrEmpty(sql))
			{
				continue;
			}

			String[] tempStrs = sql.split("[=]", -1);
			String dtlName = tempStrs[0];
			DataTable dtlTable = DBAccess.RunSQLReturnTable(sql.replace(dtlName + "=", ""));
			dtlTable.TableName = dtlName;
			ds.Tables.add(dtlTable);
		}


			///检查数据源是否正确.
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

			/// 检查数据源是否正确.


			///处理流程发起.

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
				WebUser.Exit();
				bp.port.Emp emp = new bp.port.Emp();
				emp.setNo(starter);
				if (emp.RetrieveFromDBSources() == 0)
				{
					msg += "@" + this.getName() + ",第" + idx + "条,设置的发起人员:" + emp.getNo() + "不存在.";
					msg += "@数据驱动方式发起流程(" + this.getName() + ")设置的发起人员:" + emp.getNo() + "不存在。";
					continue;
				}
				WebUser.SignInOfGener(emp);
			}


				/// 给值.
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
						if (!dt.TableName.equals(dtl.getNo()))
						{
							continue;
						}

						//删除原来的数据。
						GEDtl dtlEn = dtl.getHisGEDtl();
						dtlEn.Delete(GEDtlAttr.RefPK, String.valueOf(wk.getOID()));

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

				///  给值.

			// 处理发送信息.
			Node nd = this.getHisStartNode();
			try
			{
				WorkNode wn = new WorkNode(wk, nd);
				String infoSend = wn.NodeSend().ToMsgOfHtml();
				bp.da.Log.DefaultLogWriteLineInfo(msg);
				msg += "@" + this.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "已完成.\r\n" + infoSend;
				//this.SetText("@第（" + idx + "）条任务，" + WebUser.getNo() + " - " + WebUser.getName() + "已经完成。\r\n" + msg);
			}
			catch (RuntimeException ex)
			{
				msg += "@" + this.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "发起时出现错误.\r\n" + ex.getMessage();
			}
			msg += "<hr>";
		}
		return msg;

			/// 处理流程发起.
	}

	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin") == true)
		{
			uac.IsUpdate = true;
		}
		return uac;
	}

	public final String ClearCash()throws Exception
	{
		bp.da.Cash.ClearCash();
		//清空流程中的缓存
		//获取改流程中的节点数据
		Nodes nds = new Nodes(this.getNo());
		for (Node nd : nds.ToJavaList())
		{
			//判断表单的类型
			if (nd.getHisFormType() == NodeFormType.FoolForm || nd.getHisFormType() == NodeFormType.FreeForm)
			{
				bp.sys.CCFormAPI.AfterFrmEditAction("ND" + nd.getNodeID());
			}
		}

		return "清除成功.";
	}


	/** 
	 执行检查2018
	 
	 @return 
	*/
	public final String DoCheck2018()throws Exception
	{
		FlowCheckError check = new FlowCheckError(this);
		check.DoCheck();

		return bp.tools.Json.ToJson(check.dt);
		//return "../../Admin/Testing/FlowCheckError.htm?FK_Flow=" + this.getNo()+ "&Lang=CH";
	}
	/** 
	 校验流程
	 
	 @return 
	*/
	public final String DoCheck()throws Exception
	{
		bp.da.Cash.ClearCash();

		//删除缓存数据.
		this.ClearAutoNumCash(false);

		Nodes nds = new Nodes(this.getNo());


			///检查独立表单
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
		String frms = "";
		String err = "";
		MapData md = new MapData();
		for (FrmNode item : fns.ToJavaList())
		{
			if (frms.contains(item.getFK_Frm() + ","))
			{
				continue;
			}
			frms += item.getFK_Frm() + ",";
			md.setNo(item.getFK_Frm());
			if (md.getIsExits() == false)
			{
				err += "@节点" + item.getFK_Node() + "绑定的表单:" + item.getFK_Frm() + ",已经被删除了.";
			}
		}

			///

		try
		{

				///对流程的设置做必要的检查.
			// 设置流程名称.
			DBAccess.RunSQL("UPDATE WF_Node SET FlowName = (SELECT Name FROM WF_Flow WHERE NO=WF_Node.FK_Flow) WHERE FK_Flow='" + this.getNo()+ "'");

			//设置单据编号只读格式.
			DBAccess.RunSQL("UPDATE Sys_MapAttr SET UIIsEnable=0 WHERE KeyOfEn='BillNo' AND UIIsEnable=1 ");

			//开始节点不能有会签.
			DBAccess.RunSQL("UPDATE WF_Node SET HuiQianRole=0 WHERE NodePosType=0 AND HuiQianRole !=0  AND FK_Flow='" + this.getNo()+ "'");

			//开始节点不能有退回.
			// DBAccess.RunSQL("UPDATE WF_Node SET ReturnRole=0 WHERE NodePosType=0 AND ReturnRole !=0");

				/// 对流程的设置做必要的检查.

			//删除垃圾,非法数据.
			String sqls = "DELETE FROM Sys_FrmSln WHERE FK_MapData NOT IN (SELECT No from Sys_MapData)";
			sqls = "";
			sqls += "@ DELETE FROM WF_Direction WHERE Node=ToNode";
			DBAccess.RunSQLs(sqls);

			//更新计算数据.
			//this.NumOfBill = DBAccess.RunSQLReturnValInt("SELECT count(*) FROM WF_BillTemplate WHERE NodeID IN (SELECT NodeID FROM WF_Flow WHERE No='" + this.getNo()+ "')");
			//this.NumOfDtl = DBAccess.RunSQLReturnValInt("SELECT count(*) FROM Sys_MapDtl WHERE FK_MapData='ND" + int.Parse(this.getNo()) + "Rpt'");
			//this.DirectUpdate();

			String msg = "@  =======  关于《" + this.getName() + " 》流程检查报告  ============";
			msg += "@信息输出分为三种: 信息  警告  错误. 如果遇到输出的错误，则必须要去修改或者设置.";
			msg += "@流程检查目前还不能覆盖100%的错误,需要手工的运行一次才能确保流程设计的正确性.";

			////条件集合.
			//Conds conds = new Conds(this.getNo());


				///对节点进行检查
			//节点表单字段数据类型检查--begin---------
			msg += CheckFormFields(nds);
			//表单字段数据类型检查-------End-----

			//获得字段用于校验sql.
			MapAttrs mattrs = new MapAttrs("ND" + Integer.parseInt(this.getNo()) + "Rpt");

			//查询所有的条件.
			Conds conds = new Conds();
			conds.Retrieve(CondAttr.FK_Flow, this.getNo());


			//删除垃圾数据.
			String sql = "DELETE FROM WF_Direction  WHERE Node NOT IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "') AND FK_Flow='" + this.getNo()+ "' ";
			DBAccess.RunSQL(sql);
			sql = "DELETE FROM WF_Direction  WHERE ToNode NOT IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "') AND FK_Flow='" + this.getNo()+ "' ";
			DBAccess.RunSQL(sql);

			for (Node nd : nds.ToJavaList())
			{
				//设置它的位置类型.
				// nd.RetrieveFromDBSources();
				nd.SetValByKey(NodeAttr.NodePosType, nd.GetHisNodePosType().getValue());

				msg += "@信息: -------- 开始检查节点ID:(" + nd.getNodeID() + ")名称:(" + nd.getName() + ")信息 -------------";


					///对节点的访问规则进行检查
				msg += "@信息:开始对节点的访问规则进行检查.";

				switch (nd.getHisDeliveryWay())
				{
					case ByStation:
					case FindSpecDeptEmpsInStationlist:
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
							msg += "@错误:您设置了该节点的访问规则是按指定的岗位计算，但是您没有设置节点编号.";
						}
						break;
					case ByDeptAndStation: //按部门与岗位的交集计算.
						String mysql = "";
						//added by liuxc,2015.6.30.

						mysql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept" + "             AND wnd.FK_Node = " + nd.getNodeID() + "        INNER JOIN WF_NodeStation wns" + "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node =" + nd.getNodeID() + " ORDER BY" + "        pdes.FK_Emp";


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
								sql = nd.getDeliveryParas();
								sql = Glo.DealExp(sql, this.getHisGERpt(), null);

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
						//去rpt表中，查询是否有这个字段.
						String str1 = String.valueOf(nd.getNodeID()).substring(0, String.valueOf(nd.getNodeID()).length() - 2);
						MapAttrs rptAttrs = new MapAttrs();
						rptAttrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + str1 + "Rpt", MapAttrAttr.KeyOfEn);

						if (rptAttrs.Contains(MapAttrAttr.KeyOfEn, nd.getDeliveryParas()) == false)
						{
							/*检查节点字段是否有FK_Emp字段*/
							msg += "@错误:您设置了该节点的访问规则是[06.按上一节点表单指定的字段值作为本步骤的接受人]，但是您没有在节点属性的[访问规则设置内容]里设置指定的表单字段，详细参考开发手册.";
						}
						//if (mattrs.Contains(MapAttrAttr.KeyOfEn, "FK_Emp") == false)
						//{
						//    /*检查节点字段是否有FK_Emp字段*/
						//    msg += "@错误:您设置了该节点的访问规则是按指定节点表单人员，但是您没有在节点表单中增加FK_Emp字段，详细参考开发手册 .";
						//}
						break;
					case BySelected: // 由上一步发送人员选择
						if (nd.getIsStartNode())
						{
							//msg += "@错误:开始节点不能设置指定的选择人员访问规则。";
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

					///


					///检查节点完成条件，方向条件的定义.
				if (conds.size() != 0)
				{
					msg += "@信息:开始检查(" + nd.getName() + ")方向条件:";
					for (Cond cond : conds.ToJavaList())
					{
						msg += cond.getAttrKey() + cond.getAttrName() + cond.getOperatorValue() + "、";
					}
					msg += "@(" + nd.getName() + ")方向条件检查完成.....";
				}

					/// 检查节点完成条件的定义.


					///如果是引用的表单库的表单，就要检查该表单是否有FID字段，没有就自动增加.
				if (nd.getHisFormType() == NodeFormType.RefOneFrmTree)
				{
					MapAttr mattr = new MapAttr();
					mattr.setMyPK(nd.getNodeFrmID() + "_FID");
					if (mattr.RetrieveFromDBSources() == 0)
					{
						mattr.setKeyOfEn("FID");
						mattr.setFK_MapData(nd.getNodeFrmID());
						mattr.setMyDataType(DataType.AppInt);
						mattr.setUIVisible( false);
						mattr.setName( "FID(自动增加)");
						mattr.Insert();

						GEEntity en = new GEEntity(nd.getNodeFrmID());
						en.CheckPhysicsTable();
					}
				}

					/// 如果是引用的表单库的表单，就要检查该表单是否有FID字段，没有就自动增加.

				//如果是子线城，子线程的表单必须是轨迹模式。
				if (nd.getHisRunModel() == RunModel.SubThread)
				{
					md = new MapData("ND" + nd.getNodeID());
					if (!md.getPTable().equals("ND" + nd.getNodeID()))
					{
						md.setPTable("ND" + nd.getNodeID());
						md.Update();
					}
				}
				nd.Update();
			}

				///


				///检查延续流程,子流程发起。
			SubFlowYanXus ygflows = new SubFlowYanXus();
			ygflows.Retrieve(SubFlowYanXuAttr.SubFlowNo, this.getNo(), SubFlowYanXuAttr.SubFlowType, SubFlowType.YanXuFlow.getValue());
			for (SubFlowYanXu flow : ygflows.ToJavaList())
			{
				Flow fl = new Flow(flow.getSubFlowNo());

				/* 如果当前为子流程的时候，允许节点自动运行下一步骤，就要确定下一步骤的节点，必须有确定的可以计算的接收人. */
				if (fl.getSubFlowOver() == SubFlowOver.SendParentFlowToNextStep)
				{
					Node nd = new Node(flow.getFK_Node());
					if (nd.getHisToNodes().size() > 1)
					{
						msg += "@当前节点[" + nd.getName() + "]的可以启动子流程或者延续流程.被启动的子流程设置了当子流程结束时让父流程自动运行到下一个节点，但是当前节点有分支，导致流程无法运行到下一个节点.";
					}

					if (nd.getHisToNodes().size() == 1)
					{
						Node toNode = nd.getHisToNodes().get(0) instanceof Node ? (Node)nd.getHisToNodes().get(0) : null;
						if (nd.getHisDeliveryWay() == DeliveryWay.BySelected)
						{
							msg += "@当前节点[" + nd.getName() + "]的可以启动子流程或者延续流程.被启动的子流程设置了当子流程结束时让父流程自动运行到下一个节点，但是当前节点有分支，导致流程无法运行到下一个节点.";
						}
					}

				}
			}

				/// 检查越轨流程,子流程发起。


			msg += "@流程的基础信息: ------ ";
			msg += "@编号:  " + this.getNo()+ " 名称:" + this.getName() + " , 存储表:" + this.getPTable();

			msg += "@信息:开始检查节点流程报表.";
			this.DoCheck_CheckRpt(this.getHisNodes());


				///检查焦点字段设置是否还有效
			msg += "@信息:开始检查节点的焦点字段";

			//获得gerpt字段.
			GERpt rpt = this.getHisGERpt();
			for (Attr attr : rpt.getEnMap().getAttrs())
			{
				rpt.SetValByKey(attr.getKey(), "0");
			}

			//处理焦点字段.
			for (Node nd : nds.ToJavaList())
			{
				if (nd.getFocusField().trim().equals(""))
				{
					Work wk = nd.getHisWork();
					String attrKey = "";
					for (Attr attr : wk.getEnMap().getAttrs())
					{
						if (attr.getUIVisible()== true && attr.getUIIsDoc() && attr.getUIIsReadonly() == false && (attr.getKey().contains("Check") || attr.getKey().contains("Note")))
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
				String strs = tempVar instanceof String ? (String)tempVar : null;
				strs = Glo.DealExp(strs, rpt, "err");
				if (strs.contains("@") == true)
				{
					// msg += "@错误:焦点字段（" + nd.FocusField + "）在节点(step:" + nd.Step + " 名称:" + nd.Name + ")属性里的设置已无效，表单里不存在该字段.";
					//删除节点属性中的焦点字段
					nd.setFocusField("");
					nd.Update();
				}
				else
				{
					msg += "@提示:节点的(" + nd.getNodeID() + "," + nd.getName() + ")焦点字段（" + nd.getFocusField() + "）设置完整检查通过.";
				}

				if (this.getIsMD5())
				{
					if (nd.getHisWork().getEnMap().getAttrs().Contains(WorkAttr.MD5) == false)
					{
						nd.RepareMap(this);
					}
				}
			}
			msg += "@信息:检查节点的焦点字段完成.";

				///


				///检查质量考核点.
			msg += "@信息:开始检查质量考核点";
			for (Node nd : nds.ToJavaList())
			{
				if (nd.getIsEval())
				{
					/*如果是质量考核点，检查节点表单是否具别质量考核的特别字段？*/
					sql = "SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='ND" + nd.getNodeID() + "' AND KeyOfEn IN ('EvalEmpNo','EvalEmpName','EvalEmpCent')";
					if (DBAccess.RunSQLReturnValInt(sql) != 3)
					{
						msg += "@信息:您设置了节点(" + nd.getNodeID() + "," + nd.getName() + ")为质量考核节点，但是您没有在该节点表单中设置必要的节点考核字段.";
					}
				}
			}
			msg += "@检查质量考核点完成.";

				///


				///检查如果是合流节点必须不能是由上一个节点指定接受人员.
			for (Node nd : nds.ToJavaList())
			{
				//如果是合流节点.
				if (nd.getHisNodeWorkType() == NodeWorkType.WorkHL || nd.getHisNodeWorkType() == NodeWorkType.WorkFHL)
				{
					if (nd.getHisDeliveryWay() == DeliveryWay.BySelected)
					{
						msg += "@错误:节点ID:" + nd.getNodeID() + " 名称:" + nd.getName() + "是合流或者分合流节点，但是该节点设置的接收人规则为由上一步指定，这是错误的，应该为自动计算而非每个子线程人为的选择.";
					}
				}
			}

				/// 检查如果是合流节点必须不能是由上一个节点指定接受人员。

			msg += "@流程报表检查完成...";

			// 检查流程， 处理计算字段.
			Node.CheckFlow(nds, this.getNo());

			//创建track.
			Track.CreateOrRepairTrackTable(this.getNo());
			return msg;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@检查流程错误:" + ex.getMessage() + " @" + ex.getStackTrace());
		}
	}
	/** 
	 节点表单字段数据类型检查，名字相同的字段出现类型不同的处理方法：
	 依照不同于NDxxRpt表中同名字段类型为基准
	 
	 @return 检查结果
	*/
	private String CheckFormFields(Nodes nds)throws Exception
	{
		StringBuilder errorAppend = new StringBuilder();
		errorAppend.append("@信息: -------- 流程节点表单的字段类型检查: ------ ");
		try
		{
			String fk_mapdatas = "'ND" + Integer.parseInt(this.getNo()) + "Rpt'";
			for (Node nd : nds.ToJavaList())
			{
				fk_mapdatas += ",'ND" + nd.getNodeID() + "'";
			}

			//筛选出类型不同的字段.
			String checkSQL = "SELECT   AA.KEYOFEN, COUNT(*) AS MYNUM FROM (" + "  SELECT A.KEYOFEN,  MYDATATYPE,  COUNT(*) AS MYNUM " + "  FROM SYS_MAPATTR A WHERE FK_MAPDATA IN (" + fk_mapdatas + ") GROUP BY KEYOFEN, MYDATATYPE" + ")  AA GROUP BY  AA.KEYOFEN HAVING COUNT(*) > 1";
			DataTable dt_Fields = DBAccess.RunSQLReturnTable(checkSQL);
			for (DataRow row : dt_Fields.Rows)
			{
				String keyOfEn = row.getValue("KEYOFEN").toString();
				String myNum = row.getValue("MYNUM").toString();
				int iMyNum = 0;
				if (DataType.IsNullOrEmpty(myNum) == false)
					iMyNum = Integer.parseInt(myNum);

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
					if (ndMapAttr == null || ndMapAttr.getMyPK().equals("") || baseMapAttr.getMyPK().equals(ndMapAttr.getMyPK()) || baseMapAttr.getMyDataType() == ndMapAttr.getMyDataType())
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

		/// 其他方法.


		///检查流程.


		/// 检查流程.


		///产生数据模板。
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
		name = bp.tools.StringExpressionCalculate.ReplaceBadCharOfFileName(name);

		String path = this.getNo()+ "." + name;
		path = PathFlowDesc + path + "/";

		this.DoExpFlowXmlTemplete(path);

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
		if ((new File(path)).isDirectory() == false)
		{
			(new File(path)).mkdirs();
		}

		DataSet ds;
		try
		{
			ds = GetFlow(path);
		}
		catch (RuntimeException e)
		{
			throw new RuntimeException("err@流程模板导出失败：" + e.getMessage());
		}

		if (ds != null)
		{
			String name = this.getName();
			name = bp.tools.StringExpressionCalculate.ReplaceBadCharOfFileName(name);
			name = name + ".xml";
			String filePath = path + name;
			ds.WriteXml(filePath, XmlWriteMode.WriteSchema, ds);
		}
		return ds;
	}

	//xml文件是否正在操作中
	private static boolean isXmlLocked;
	/** 
	 备份当前流程到用户xml文件
	 用户每次保存时调用
	 捕获异常写入日志,备份失败不影响正常保存
	 * @throws Exception 
	*/
	public final void WriteToXml() throws Exception
	{
		try
		{
			String name = this.getNo()+ "." + this.getName();
			name = bp.tools.StringExpressionCalculate.ReplaceBadCharOfFileName(name);
			String path = PathFlowDesc + name + "/";
			DataSet ds = GetFlow(path);
			if (ds == null)
			{
				return;
			}

			String directory = this.getNo()+ "." + this.getName();
			directory = bp.tools.StringExpressionCalculate.ReplaceBadCharOfFileName(directory);
			path = PathFlowDesc + directory + "/";
			String xmlName = path + "Flow" + ".xml";
			File filexmlName = new File(xmlName);
			if (!isXmlLocked)
			{
				if (!DataType.IsNullOrEmpty(path))
				{
					if (!(new File(path)).isDirectory())
					{
						(new File(path)).mkdirs();
					}
					else if (filexmlName.exists()) {
						long modifiedTime = filexmlName.lastModified();
						java.util.Date date = new java.util.Date(modifiedTime);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						String dd = sdf.format(date);
						String xmlNameOld = path + "Flow" + dd + ".xml";
						File filexmlNameOld = new File(xmlNameOld);
						if (filexmlNameOld.exists()) {
							filexmlNameOld.delete();
						}
						FileAccess.Move(filexmlName, xmlNameOld);
					}
				}

				if (DataType.IsNullOrEmpty(xmlName) == false)
				{
					ds.WriteXml(xmlName);
					isXmlLocked = false;
				}
			}
		}
		catch (RuntimeException e)
		{
			isXmlLocked = false;
			bp.da.Log.DefaultLogWriteLineError("流程模板文件备份错误:" + e.getMessage());
		}
	}
	public final DataSet GetFlow(String path) throws Exception
	{
		// 把所有的数据都存储在这里。
		DataSet ds = new DataSet();

		// 流程信息。
		String sql = "SELECT * FROM WF_Flow WHERE No='" + this.getNo()+ "'";

		Flow fl = new Flow(this.getNo());
		DataTable dtFlow = fl.ToDataTableField("WF_Flow");
		dtFlow.TableName = "WF_Flow";
		ds.Tables.add(dtFlow);

		// 节点信息
		Nodes nds = new Nodes(this.getNo());
		DataTable dtNodes = nds.ToDataTableField("WF_Node");
		ds.Tables.add(dtNodes);

		//节点属性
		NodeExts ndexts = new NodeExts(this.getNo());
		DataTable dtNodeExts = ndexts.ToDataTableField("WF_NodeExt");
		ds.Tables.add(dtNodeExts);

		//节点消息
		PushMsg pushMsg = new PushMsg();
		pushMsg.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
		ds.Tables.add(pushMsg.ToDataTableField("WF_PushMsg"));
		
		//接收人规则
		Selectors selectors = new Selectors(this.getNo());
		DataTable dtSelectors = selectors.ToDataTableField("WF_Selector");
		ds.Tables.add(dtSelectors);

		// 单据模版. 
		BillTemplates tmps = new BillTemplates(this.getNo());
		String pks = "";
		for (BillTemplate tmp : tmps.ToJavaList())
		{
			try
			{
				if (path != null)
				{
					Files.copy(Paths.get(SystemConfig.getPathOfDataUser() + "/CyclostyleFile/" + tmp.getNo() + ".rtf"), Paths.get(path + "/" + tmp.getNo() + ".rtf"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			catch (java.lang.Exception e)
			{
				pks += "@" + tmp.getPKVal();
				tmp.Delete();
			}
		}
		tmps.Remove(pks);
		ds.Tables.add(tmps.ToDataTableField("WF_BillTemplate"));


		String sqlin = "SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo()+ "'";

		// 条件信息
		Conds cds = new bp.wf.template.Conds(this.getNo());
		ds.Tables.add(cds.ToDataTableField("WF_Cond"));

		// 节点与表单绑定.
		FrmNodes fns = new bp.wf.template.FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
		ds.Tables.add(fns.ToDataTableField("WF_FrmNode"));


		// 表单方案.
		FrmFields ffs = new bp.wf.template.FrmFields();
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

		//子流程。
		SubFlows fls = new SubFlows();
		fls.RetrieveInSQL(CCDeptAttr.FK_Node, sqlin);
		ds.Tables.add(fls.ToDataTableField("WF_NodeSubFlow"));

		//表单信息，包含从表.
		sql = "SELECT No FROM Sys_MapData WHERE " + Glo.MapDataLikeKey(this.getNo(), "No");
		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL(MapDataAttr.No, sql);
		DataTable dt = mds.ToDataTableField("Sys_MapData");
		dt.Columns.Add("HtmlTemplateFile");
		for (DataRow dr : dt.Rows)
		{
			if (Integer.parseInt(dr.getValue("FrmType").toString()) == FrmType.Develop.getValue())
			{
				String htmlCode = DBAccess.GetBigTextFromDB("Sys_MapData", "No", dr.getValue("No").toString(), "HtmlTemplateFile");
				dr.setValue("HtmlTemplateFile", htmlCode);
			}

		}

		ds.Tables.add(dt);

		// Sys_MapAttr.
		sql = "SELECT MyPK FROM Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		sql += " UNION "; //增加多附件的扩展列.
		sql += "SELECT MyPK FROM Sys_MapAttr WHERE FK_MapData IN ( SELECT MyPK FROM Sys_FrmAttachment WHERE FK_Node=0 AND " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ) ";

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
		mfs.RetrieveInSQL("MyPK", sql);
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
		String sql = "SELECT * FROM WF_Flow WHERE No='" + this.getNo()+ "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Flow";
		ds.Tables.add(dt);

		// 节点信息
		sql = "SELECT * FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "'";
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
					Files.copy(Paths.get(SystemConfig.getPathOfDataUser() + "/CyclostyleFile/" + tmp.getNo() + ".rtf"), Paths.get(path + "/" + tmp.getNo() + ".rtf"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			catch (java.lang.Exception e)
			{
				pks += "@" + tmp.getPKVal();
				tmp.Delete();
			}
		}
		tmps.Remove(pks);
		ds.Tables.add(tmps.ToDataTableField("WF_BillTemplate"));

		String sqlin = "SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo()+ "'";


		// 条件信息
		sql = "SELECT * FROM WF_Cond WHERE FK_Flow='" + this.getNo()+ "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Cond";
		ds.Tables.add(dt);

		// 转向规则.
		//sql = "SELECT * FROM WF_TurnTo WHERE FK_Flow='" + this.getNo()+ "'";
		//dt = DBAccess.RunSQLReturnTable(sql);
		//dt.TableName = "WF_TurnTo";
		//ds.Tables.add(dt);

		// 节点与表单绑定.
		sql = "SELECT * FROM WF_FrmNode WHERE FK_Flow='" + this.getNo()+ "'";
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


		int flowID = Integer.parseInt(this.getNo());
		sql = "SELECT * FROM Sys_MapData WHERE " + Glo.MapDataLikeKey(this.getNo(), "No");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		ds.Tables.add(dt);


		// Sys_MapAttr.
		sql = "SELECT * FROM Sys_MapAttr WHERE  " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ORDER BY FK_MapData,Idx";
		//sql = "SELECT * FROM Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData") + "  ORDER BY FK_MapData,Idx";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		ds.Tables.add(dt);

		// Sys_EnumMain
		//sql = "SELECT * FROM Sys_EnumMain WHERE No IN (SELECT KeyOfEn from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData") +")";
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
		//sql = "SELECT * FROM Sys_MapExt WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		sql = "SELECT * FROM Sys_MapExt WHERE  " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData"); // +Glo.MapDataLikeKey(this.No, "FK_MapData");

		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		// Sys_GroupField
		sql = "SELECT * FROM Sys_GroupField WHERE   " + Glo.MapDataLikeKey(this.getNo(), "FrmID"); // +" " + Glo.MapDataLikeKey(this.No, "EnName");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_GroupField";
		ds.Tables.add(dt);

		// Sys_MapFrame
		sql = "SELECT * FROM Sys_MapFrame WHERE" + Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapFrame";
		ds.Tables.add(dt);

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


		/// 产生数据模板。


		///其他公用方法1
	/** 
	 重新设置Rpt表
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final void CheckRptOfReset() throws NumberFormatException, Exception
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
			return "@流程" + this.getNo()+ this.getName() + "的数据存储非轨迹模式不能重新生成.";
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
			String flowEmps = "@";
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
					if (nd.getNodeID() == Integer.parseInt(this.getNo()+ "01"))
					{
						startWork = wk;
					}

					try
					{
						if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
						{
							if (flowEmps.contains("@" + wk.getRecOfEmp().getName() + "@"))
							{
								continue;
							}
							flowEmps += wk.getRecOfEmp().getName() + "@";
						}

						if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDOnly)
						{
							if (flowEmps.contains("@" + wk.getRec() + "@"))
							{
								continue;
							}
							flowEmps += wk.getRec() + "@";
						}

						if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
						{
							if (flowEmps.contains("@" + wk.getRec() + ","))
							{
								continue;
							}
							flowEmps += wk.getRec() + "," + wk.getRecOfEmp().getName();
						}




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
			if (DataType.IsNullOrEmpty(rpt.getFK_Dept()))
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
			rpt.setFlowStartRDT(DBAccess.RunSQLReturnStringIsNull("SELECT WFState FROM WF_GenerWorkFlow WHERE WorkID=" + oid, "1"));
			rpt.setFID(startWork.GetValIntByKey("FID"));
			rpt.setFlowEmps(flowEmps);
			rpt.setFlowEnder(endWK.getRec());
			//rpt.FlowEnderRDT = endWK.RDT;
			rpt.setFlowEndNode(endWK.getNodeID());

			//修复标题字段。
			WorkNode wn = new WorkNode(startWork, this.getHisStartNode());
			rpt.setTitle(bp.wf.WorkFlowBuessRole.GenerTitle(this, startWork));
			//try
			//{
			//    TimeSpan ts = endWK.RDT_DateTime - startWork.RDT_DateTime;
			//    rpt.FlowDaySpan =  ts.Days;
			//}
			//catch
			//{
			//}
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
				String rptDtlNo = "ND" + Integer.parseInt(this.getNo()) + "RptDtl" + String.valueOf(i);
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
					switch (attr.getKeyOfEn())
					{
						case "FK_NY":
							attrN.setUIVisible( true);
							attrN.setIdx( 100);
							attrN.setUIWidth( 60);
							break;
						case "RDT":
							attrN.setUIVisible( true);
							attrN.setIdx( 100);
							attrN.setUIWidth( 60);
							break;
						case "Rec":
							attrN.setUIVisible( true);
							attrN.setIdx( 100);
							attrN.setUIWidth( 60);
							break;
						default:
							break;
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


		//生成该节点的 nds 比如  "'ND101','ND102','ND103'"
		String ndsstrs = "";
		for (bp.wf.Node nd : nds.ToJavaList())
		{
			ndsstrs += "'ND" + nd.getNodeID() + "',";
		}
		ndsstrs = ndsstrs.substring(0, ndsstrs.length() - 1);


			///插入字段。
		String sql = "SELECT distinct KeyOfEn FROM Sys_MapAttr WHERE FK_MapData IN (" + ndsstrs + ")";
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

		//所有节点表单字段的合集.
		sql = "SELECT MyPK, KeyOfEn,DefVal FROM Sys_MapAttr WHERE FK_MapData IN (" + ndsstrs + ")";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		//求已经存在的字段集合。
		sql = "SELECT KeyOfEn FROM Sys_MapAttr WHERE FK_MapData='ND" + flowId + "Rpt'";
		DataTable dtExits = DBAccess.RunSQLReturnTable(sql);
		String pks = "@";
		for (DataRow dr : dtExits.Rows)
		{
			pks += dr.getValue(0) + "@";
		}

		//查询出来已经有的映射.
		MapAttrs attrs = new MapAttrs(fk_mapData);

		//遍历 - 所有节点表单字段的合集
		for (DataRow dr : dt.Rows)
		{
			if (pks.contains("@" + dr.getValue("KeyOfEn").toString() + "@") == true)
			{
				continue;
			}

			String mypk = dr.getValue("MyPK").toString();

			pks += dr.getValue("KeyOfEn").toString() + "@";

			//找到这个属性.
			MapAttr ma = new MapAttr(mypk);
			ma.setMyPK("ND" + flowId + "Rpt_" + ma.getKeyOfEn());
			ma.setFK_MapData("ND" + flowId + "Rpt");
			ma.setUIIsEnable(false);

			if (ma.getDefValReal().contains("@"))
			{
				/*如果是一个有变量的参数.*/
				ma.setDefVal("");
			}

			//如果包含他,就说已经存在.
			if (attrs.Contains("MyPK", ma.getMyPK()) == true)
			{
				continue;
			}

			// 如果不存在.
			ma.Insert();
		}


		// 创建mapData.
		MapData md = new MapData();
		md.setNo("ND" + flowId + "Rpt");
		if (md.RetrieveFromDBSources() == 0)
		{
			md.setName( this.getName());
			md.setPTable(this.getPTable());
			md.Insert();
		}
		else
		{
			if (md.getName().equals(this.getName()) || md.getPTable().equals(this.getPTable()) == false)
			{
				md.Update();
			}
		}

			/// 插入字段。


			///补充上流程字段到NDxxxRpt.
		int groupID = 0;
		for (MapAttr attr : attrs.ToJavaList())
		{
			switch (attr.getKeyOfEn())
			{
				case GERptAttr.FK_Dept:
					attr.setUIContralType( UIContralType.TB);
					attr.setLGType( FieldTypeS.Normal);
					attr.setUIVisible( true);
					attr.setGroupID(groupID); // gfs[0].GetValIntByKey("OID");
					attr.setUIIsEnable(false);
					attr.setDefVal("");
					attr.setMaxLen( 100);
					attr.Update();
					break;
				case "FK_NY":
					//  attr.setUIBindKey( "BP.Pub.NYs";
					attr.setUIContralType( UIContralType.TB);
					attr.setLGType( FieldTypeS.Normal);
					attr.setUIVisible( true);
					attr.setUIIsEnable(false);
					attr.setGroupID(groupID);
					attr.Update();
					break;
				case "FK_Emp":
					break;
				default:
					break;
			}
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.Title) == false)
		{
			/* 标题 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.Title); // "FlowEmps";
			attr.setName( "标题");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen( 400);
			attr.setIdx( -1000);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.OID) == false)
		{
			/* WorkID */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("OID");
			attr.setName( "WorkID");
			attr.setMyDataType(bp.da.DataType.AppInt);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(EditType.Readonly);
			attr.Insert();
		}


		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FID) == false)
		{
			/* WorkID */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("FID");
			attr.setName( "FID");
			attr.setMyDataType(bp.da.DataType.AppInt);
			attr.setUIContralType( UIContralType.TB);
			attr.setLGType( FieldTypeS.Normal);
			attr.setUIVisible( false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(EditType.Readonly);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.WFState) == false)
		{
			/* 流程状态 */
			MapAttr attr = new MapAttr();
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
			/* 流程状态Ext */
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
			/* 参与人 */
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
			/* 发起人 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStarter);
			attr.setName("发起人");
			attr.setMyDataType(DataType.AppString);

			attr.setUIBindKey("bp.port.Emps");
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
			/* 发起时间 */
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
			/* 结束人 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnder);
			attr.setName("结束人");
			attr.setMyDataType(DataType.AppString);
			attr.setUIBindKey("bp.port.Emps");
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
			/* 结束时间 */
			MapAttr attr = new MapAttr();
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
			/* 结束节点 */
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

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowDaySpan) == false)
		{
			/* FlowDaySpan */
			MapAttr attr = new MapAttr();
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
			/* 父流程 流程编号 */
			MapAttr attr = new MapAttr();
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
			/* 父流程WorkID */
			MapAttr attr = new MapAttr();
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
			/* 父流程WorkID */
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

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PEmp) == false)
		{
			/* 调起子流程的人员 */
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

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.BillNo) == false)
		{
			/* 父流程 流程编号 */
			MapAttr attr = new MapAttr();
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




		if (attrs.Contains(md.getNo() + "_" + GERptAttr.AtPara) == false)
		{
			/* 父流程 流程编号 */
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

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.GUID) == false)
		{
			/* 父流程 流程编号 */
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

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PrjNo) == false)
		{
			/* 项目编号 */
			MapAttr attr = new MapAttr();
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
			/* 项目名称 */
			MapAttr attr = new MapAttr();
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
			/* 流程信息 */
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowNote);
			attr.setName("流程信息"); // 父流程流程编号
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

			/// 补充上流程字段。


			///为流程字段设置分组。
		try
		{
			String flowInfo = "流程信息";
			GroupField flowGF = new GroupField();
			int num = flowGF.Retrieve(GroupFieldAttr.FrmID, fk_mapData, GroupFieldAttr.Lab, "流程信息");
			if (num == 0)
			{
				flowGF = new GroupField();
				flowGF.setLab(flowInfo);
				flowGF.setFrmID(fk_mapData);
				flowGF.setIdx( -1);
				flowGF.Insert();
			}
			sql = "UPDATE Sys_MapAttr SET GroupID='" + flowGF.getOID() + "' WHERE  FK_MapData='" + fk_mapData + "'  AND KeyOfEn IN('" + GERptAttr.PFlowNo + "','" + GERptAttr.PWorkID + "','" + GERptAttr.FK_Dept + "','" + GERptAttr.FK_NY + "','" + GERptAttr.FlowDaySpan + "','" + GERptAttr.FlowEmps + "','" + GERptAttr.FlowEnder + "','" + GERptAttr.FlowEnderRDT + "','" + GERptAttr.FlowEndNode + "','" + GERptAttr.FlowStarter + "','" + GERptAttr.FlowStartRDT + "','" + GERptAttr.WFState + "')";
			DBAccess.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
			Log.DefaultLogWriteLineError(ex.getMessage());
		}

			/// 为流程字段设置分组


			///尾后处理.
		GERpt gerpt = this.getHisGERpt();
		gerpt.CheckPhysicsTable(); //让报表重新生成.

		DBAccess.RunSQL("DELETE FROM Sys_GroupField WHERE FrmID='" + fk_mapData + "' AND OID NOT IN (SELECT GroupID FROM Sys_MapAttr WHERE FK_MapData = '" + fk_mapData + "')");

		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name='活动时间' WHERE FK_MapData='ND" + flowId + "Rpt' AND KeyOfEn='CDT'");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name='参与者' WHERE FK_MapData='ND" + flowId + "Rpt' AND KeyOfEn='Emps'");

			/// 尾后处理.
	}

		/// 其他公用方法1


		///执行流程事件.
	private bp.wf.FlowEventBase _FDEventEntity = null;
	/** 
	 节点实体类，没有就返回为空.
	 * @throws Exception 
	*/
	public final bp.wf.FlowEventBase getFEventEntity() throws Exception
	{
		if (_FDEventEntity == null && DataType.IsNullOrEmpty(this.getFlowMark()) == false && DataType.IsNullOrEmpty(this.getFlowEventEntity()) == false)
		{
			_FDEventEntity = bp.wf.Glo.GetFlowEventEntityByEnName(this.getFlowEventEntity());
		}
		return _FDEventEntity;
	}

		/// 执行流程事件.


		///基本属性
	/** 
	 是否是MD5加密流程
	 * @throws Exception 
	*/
	public final boolean getIsMD5() throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsMD5);
	}
	public final void setIsMD5(boolean value) throws Exception
	{
		this.SetValByKey(FlowAttr.IsMD5, value);
	}
	/** 
	 是否有单据
	 * @throws Exception 
	*/
	public final int getNumOfBill() throws Exception
	{
		return this.GetValIntByKey(FlowAttr.NumOfBill);
	}
	public final void setNumOfBill(int value) throws Exception
	{
		this.SetValByKey(FlowAttr.NumOfBill, value);
	}
	/** 
	 标题生成规则
	*/
	public final String getTitleRole()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.TitleRole);
	}
	public final void setTitleRole(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.TitleRole, value);
	}
	/** 
	 明细表
	*/
	public final int getNumOfDtl()throws Exception
	{
		return this.GetValIntByKey(FlowAttr.NumOfDtl);
	}
	public final void setNumOfDtl(int value) throws Exception
	{
		this.SetValByKey(FlowAttr.NumOfDtl, value);
	}
	public final int getAvgDay()throws Exception
	{
		return this.GetValIntByKey(FlowAttr.AvgDay);
	}
	public final void setAvgDay(BigDecimal value) throws Exception
	{
		this.SetValByKey(FlowAttr.AvgDay, value);
	}
	public final int getStartNodeID()throws Exception
	{
		return Integer.parseInt(this.getNo()+ "01");
			//return this.GetValIntByKey(FlowAttr.StartNodeID);
	}
	/** 
	 add 2013-01-01.
	 业务主表(默认为NDxxRpt)
	*/
	public final String getPTable()throws Exception
	{
		String s = this.GetValStringByKey(FlowAttr.PTable);
		if (DataType.IsNullOrEmpty(s))
		{
			s = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		}
		return s;
	}
	public final void setPTable(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.PTable, value);
	}
	/** 
	 历史记录显示字段.
	*/
	public final String getHistoryFields()throws Exception
	{
		String strs = this.GetValStringByKey(FlowAttr.HistoryFields);
		if (DataType.IsNullOrEmpty(strs))
		{
			strs = "WFState,Title,FlowStartRDT,FlowEndNode";
		}

		return strs;
	}
	/** 
	 是否启用？
	*/
	public final boolean getGuestFlowRole()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.GuestFlowRole);
	}
	public final void setGuestFlowRole(boolean value) throws Exception
	{
		this.SetValByKey(FlowAttr.GuestFlowRole, value);
	}
	/** 
	 是否可以独立启动
	*/
	public final boolean getIsCanStart()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsCanStart);
	}
	public final void setIsCanStart(boolean value) throws Exception
	{
		this.SetValByKey(FlowAttr.IsCanStart, value);
	}
	/** 
	 是否可以批量发起
	*/
	public final boolean getIsBatchStart()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsBatchStart);
	}
	public final void setIsBatchStart(boolean value) throws Exception
	{
		this.SetValByKey(FlowAttr.IsBatchStart, value);
	}
	/** 
	 是否自动计算未来的处理人
	*/
	public final boolean getIsFullSA()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsFullSA);
	}
	public final void setIsFullSA(boolean value) throws Exception
	{
		this.SetValByKey(FlowAttr.IsFullSA, value);
	}
	/** 
	 批量发起字段
	*/
	public final String getBatchStartFields()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.BatchStartFields);
	}
	public final void setBatchStartFields(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.BatchStartFields, value);
	}
	/** 
	 单据格式
	*/
	public final String getBillNoFormat()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.BillNoFormat);
	}
	public final void setBillNoFormat(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.BillNoFormat, value);
	}
	/** 
	 流程类别
	*/
	public final String getFK_FlowSort()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.FK_FlowSort, value);
	}
	/** 
	 系统类别
	*/
	public final String getSysType()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.SysType);
	}
	public final void setSysType(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.SysType, value);
	}
	/** 
	 参数
	*/
	public final String getParas()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.Paras);
	}
	public final void setParas(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.Paras, value);
	}
	/** 
	 流程类别名称
	*/
	public final String getFK_FlowSortText()throws Exception
	{
		FlowSort fs = new FlowSort(this.getFK_FlowSort());
		return fs.getName();
			//return this.GetValRefTextByKey(FlowAttr.FK_FlowSort);
	}

	/** 
	 版本号
	*/
	public final String getVer()throws Exception
	{
		return this.GetValStringByKey(FlowAttr.Ver);
	}
	public final void setVer(String value) throws Exception
	{
		this.SetValByKey(FlowAttr.Ver, value);
	}

		///


		///计算属性
	/** 
	 流程类型(大的类型)
	*/
	public final int getFlowTypeDel()throws Exception
	{
		return this.GetValIntByKey(FlowAttr.FlowType);
	}
	/** 
	 (当前节点为子流程时)是否检查所有子流程完成后父流程自动发送
	*/
	public final SubFlowOver getSubFlowOver()throws Exception
	{
		return SubFlowOver.forValue(this.GetValIntByKey(FlowAttr.IsAutoSendSubFlowOver));
	}
	/** 
	 是否启用数据模版？
	*/
	public final boolean getIsDBTemplate()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsDBTemplate);
	}
	public final String getNote()throws Exception
	{
		String s = this.GetValStringByKey("Note");
		if (s.length() == 0)
		{
			return "无";
		}
		return s;
	}
	public final String getNoteHtml()throws Exception
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
	public final boolean getIsMutiLineWorkFlowDel()
	{
		return false;
			/*
			if (this.FlowType==2 || this.FlowType==1 )
			    return true;
			else
			    return false;
			    */
	}

		///


		///扩展属性
	/** 
	 应用类型
	*/
	public final FlowAppType getHisFlowAppType()throws Exception
	{
		return FlowAppType.forValue(this.GetValIntByKey(FlowAttr.FlowAppType));
	}
	public final void setHisFlowAppType(FlowAppType value)throws Exception
	{
		this.SetValByKey(FlowAttr.FlowAppType, value.getValue());
	}
	/** 
	 数据存储模式
	*/
	public final DataStoreModel getHisDataStoreModel()throws Exception
	{
		return DataStoreModel.forValue(this.GetValIntByKey(FlowAttr.DataStoreModel));
	}
	public final void setHisDataStoreModel(DataStoreModel value)throws Exception
	{
		this.SetValByKey(FlowAttr.DataStoreModel, value.getValue());
	}
	/** 
	 是否启用子流程运行结束后，主流程自动运行到下一节点
	*/
	public final boolean getIsToParentNextNode()throws Exception
	{
		return this.GetValBooleanByKey(NodeAttr.IsToParentNextNode);
	}
	public final void setIsToParentNextNode(boolean value) throws Exception
	{
		this.SetValByKey(NodeAttr.IsToParentNextNode, value);
	}
	/** 
	 节点
	*/
	private Nodes _HisNodes = null;
	/** 
	 他的节点集合.
	*/
	public final Nodes getHisNodes()throws Exception
	{
		_HisNodes = new Nodes(this.getNo());
		return _HisNodes;
	}
	public final void setHisNodes(Nodes value)
	{
		_HisNodes = value;
	}
	/** 
	 流程完成条件
	*/
	public final Conds getCondsOfFlowComplete()throws Exception
	{
		Entities ens = this.GetEntitiesAttrFromAutoNumCash(new Conds(), CondAttr.FK_Flow, this.getNo(), CondAttr.CondType, CondType.Flow.getValue());
		return ens instanceof Conds ? (Conds)ens : null;
	}
	/** 
	 事件:
	*/
	public final FrmEvents getFrmEvents()throws Exception
	{
		Entities ens = this.GetEntitiesAttrFromAutoNumCash(new FrmEvents(), FrmEventAttr.FK_Flow, this.getNo());
		return ens instanceof FrmEvents ? (FrmEvents)ens : null;
	}
	/** 
	 他的 Start 节点
	*/
	public final Node getHisStartNode()throws Exception
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
	*/
	public final FlowSort getHisFlowSort()throws Exception
	{
		return new FlowSort(this.getFK_FlowSort());
	}
	/** 
	 flow data 数据
	*/
	public final bp.wf.data.GERpt getHisGERpt()throws Exception
	{
		try
		{
			bp.wf.data.GERpt wk = new bp.wf.data.GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
			return wk;
		}
		catch (java.lang.Exception e)
		{
			this.DoCheck();
			bp.wf.data.GERpt wk1 = new bp.wf.data.GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
			return wk1;
		}
	}

	public final boolean getIsFrmEnable()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsFrmEnable);
	}

	public final boolean getIsTruckEnable()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsTruckEnable);
	}

	public final boolean getIsTimeBaseEnable()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsTimeBaseEnable);
	}

	public final boolean getIsTableEnable()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsTableEnable);
	}

	public final boolean getIsOPEnable()throws Exception
	{
		return this.GetValBooleanByKey(FlowAttr.IsOPEnable);
	}

		///


		///构造方法

	/** 
	 流程
	*/
	public Flow()throws Exception
	{
		this.setNo( "");
	}
	/** 
	 流程
	 
	 @param _No 编号
	*/
	public Flow(String _No)throws Exception
	{
		this.setNo( _No);
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
	protected boolean beforeUpdateInsertAction()throws Exception
	{
		//获得事件实体.
		if (DataType.IsNullOrEmpty(this.getFlowMark()) == true)
		{
			this.setFlowEventEntity(bp.wf.Glo.GetFlowEventEntityStringByFlowMark(this.getNo()));
		}
		else
		{
			this.setFlowEventEntity(bp.wf.Glo.GetFlowEventEntityStringByFlowMark(this.getFlowMark()));
		}

		DBAccess.RunSQL("UPDATE WF_Node SET FlowName='" + this.getName() + "' WHERE FK_Flow='" + this.getNo()+ "'");
		DBAccess.RunSQL("UPDATE Sys_MapData SET Name='" + this.getName() + "' WHERE No='" + this.getPTable() + "'");
		return super.beforeUpdateInsertAction();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Flow", "流程");

			//@sly 取消了缓存.
		map.setDepositaryOfEntity( Depositary.Application);
			//map.DepositaryOfEntity= Depositary.Application;
		map.setCodeStruct( "3");

		map.AddTBStringPK(FlowAttr.No, null, "编号", true, true, 1, 4, 3);
		map.AddTBString(FlowAttr.Name, null, "名称", true, false, 0, 200, 10);

		map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "流程类别", new FlowSorts(), false);
		map.AddTBString(FlowAttr.SysType, null, "系统类别", true, false, 0, 3, 10);

		map.AddTBInt(FlowAttr.FlowRunWay, 0, "运行方式", false, false);

			//  map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "流程类别", new FlowSorts(), false);
			//map.AddDDLSysEnum(FlowAttr.FlowRunWay, (int)FlowRunWay.HandWork, "运行方式", false,
			//    false, FlowAttr.FlowRunWay,
			//    "@0=手工启动@1=指定人员按时启动@2=数据集按时启动@3=触发式启动");

		map.AddTBString(FlowAttr.RunObj, null, "运行内容", true, false, 0, 500, 10);
		map.AddTBString(FlowAttr.Note, null, "备注", true, false, 0, 300, 10);
		map.AddTBString(FlowAttr.RunSQL, null, "流程结束执行后执行的SQL", true, false, 0, 500, 10);

		map.AddTBInt(FlowAttr.NumOfBill, 0, "是否有单据", false, false);
		map.AddTBInt(FlowAttr.NumOfDtl, 0, "NumOfDtl", false, false);
		map.AddTBInt(FlowAttr.FlowAppType, 0, "流程类型", false, false);
		map.AddTBInt(FlowAttr.ChartType, 1, "节点图形类型", false, false);

		map.AddTBInt(FlowAttr.IsFrmEnable, 1, "是否显示表单", true, true);
		map.AddTBInt(FlowAttr.IsTruckEnable, 1, "是否显示轨迹图", true, true);
		map.AddTBInt(FlowAttr.IsTimeBaseEnable, 1, "是否显示时间轴", true, true);
		map.AddTBInt(FlowAttr.IsTableEnable, 1, "是否显示时间表", true, true);
		map.AddTBInt(FlowAttr.IsOPEnable, 1, "是否显示操作", true, true);

			// map.AddBoolean(FlowAttr.IsOK, true, "是否启用", true, true);
		map.AddTBInt(FlowAttr.IsCanStart, 1, "可以独立启动否？", true, true);
		map.AddTBInt(FlowAttr.IsStartInMobile, 1, "是否可以在手机里发起？", true, true);

		map.AddTBDecimal(FlowAttr.AvgDay, new BigDecimal(0), "平均运行用天", false, false);

		map.AddTBInt(FlowAttr.IsFullSA, 0, "是否自动计算未来的处理人？(启用后,ccflow就会为已知道的节点填充处理人到WF_SelectAccper)", false, false);
		map.AddTBInt(FlowAttr.IsMD5, 0, "IsMD5", false, false);
		map.AddTBInt(FlowAttr.Idx, 0, "显示顺序号(在发起列表中)", true, false);

		map.AddTBInt(FlowAttr.SDTOfFlowRole, 0, "流程计划完成日期计算规则", true, false);
		map.AddTBString(FlowAttr.SDTOfFlowRoleSQL, null, "流程计划完成日期计算规则SQL", false, false, 0, 200, 10);

		map.AddTBString(FlowAttr.Paras, null, "参数", false, false, 0, 1000, 10);

			// add 2013-01-01. 
		map.AddTBString(FlowAttr.PTable, null, "流程数据存储主表", true, false, 0, 30, 10);

			// add 2019.11.07  @liuqiang.
		map.AddTBInt(FlowAttr.FlowFrmModel, 0, "流程表单类型", true, true);
		map.AddTBString(FlowAttr.FrmUrl, null, "表单Url", true, false, 0, 150, 10, true);

			// 草稿规则 "@0=无(不设草稿)@1=保存到待办@2=保存到草稿箱"
		map.AddTBInt(FlowAttr.Draft, 0, "草稿规则", true, false);

			// add 2013-01-01.
		map.AddTBInt(FlowAttr.DataStoreModel, 0, "数据存储模式", true, false);

			// add 2013-02-05.
		map.AddTBString(FlowAttr.TitleRole, null, "标题生成规则", true, false, 0, 90, 10, true);

			// add 2013-02-14 
		map.AddTBString(FlowAttr.FlowMark, null, "流程标记", true, false, 0, 50, 10);
		map.AddTBString(FlowAttr.FlowEventEntity, null, "FlowEventEntity", true, false, 0, 100, 10, true);
		map.AddTBString(FlowAttr.HistoryFields, null, "历史查看字段", true, false, 0, 120, 10, true);
		map.AddTBInt(FlowAttr.GuestFlowRole, 0, "是否是客户参与流程？", true, false);
		map.AddTBString(FlowAttr.BillNoFormat, null, "单据编号格式", true, false, 0, 50, 10, true);
		map.AddTBString(FlowAttr.FlowNoteExp, null, "备注表达式", true, false, 0, 90, 10, true);

			//部门权限控制类型,此属性在报表中控制的.
		map.AddTBInt(FlowAttr.DRCtrlType, 0, "部门查询权限控制方式", true, false);

			//运行主机. 这个流程运行在那个子系统的主机上.
		map.AddTBString(FlowAttr.HostRun, null, "运行主机(IP+端口)", true, false, 0, 40, 10, true);
		map.AddTBInt(FlowAttr.IsToParentNextNode, 0, "子流程运行到该节点时，让父流程自动运行到下一步", false, false);


			///流程启动限制
		map.AddTBInt(FlowAttr.StartLimitRole, 0, "启动限制规则", true, false);
		map.AddTBString(FlowAttr.StartLimitPara, null, "规则内容", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartLimitAlert, null, "限制提示", true, false, 0, 500, 10, false);
		map.AddTBInt(FlowAttr.StartLimitWhen, 0, "提示时间", true, false);

			/// 流程启动限制


			///导航方式。
		map.AddTBInt(FlowAttr.StartGuideWay, 0, "前置导航方式", false, false);
		map.AddTBString(FlowAttr.StartGuideLink, null, "右侧的连接", true, false, 0, 200, 10, true);
		map.AddTBString(FlowAttr.StartGuideLab, null, "连接标签", true, false, 0, 200, 10, true);

		map.AddTBString(FlowAttr.StartGuidePara1, null, "参数1", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartGuidePara2, null, "参数2", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartGuidePara3, null, "参数3", true, false, 0, 500, 10, true);
		map.AddTBInt(FlowAttr.IsResetData, 0, "是否启用数据重置按钮？", true, false);
			//    map.AddTBInt(FlowAttr.IsImpHistory, 0, "是否启用导入历史数据按钮？", true, false);
		map.AddTBInt(FlowAttr.IsLoadPriData, 0, "是否导入上一个数据？", true, false);

			/// 导航方式。

		map.AddTBInt(FlowAttr.IsDBTemplate, 0, "是否启用数据模版？", true, false);

			//批量发起 add 2013-12-27. 
		map.AddTBInt(FlowAttr.IsBatchStart, 0, "是否可以批量发起", true, false);
		map.AddTBString(FlowAttr.BatchStartFields, null, "批量发起字段(用逗号分开)", true, false, 0, 200, 10, true);
		map.AddTBInt(FlowAttr.IsAutoSendSubFlowOver, 0, "(当前节点为子流程时)是否检查所有子流程完成后父流程自动发送", true, true);

		map.AddTBString(FlowAttr.Ver, null, "版本号", true, true, 0, 20, 10);
			//设计类型 .
		map.AddTBInt(FlowAttr.FlowDeleteRole, 0, "流程实例删除规则", true, false);

		map.AddTBString(FlowAttr.OrgNo, null, "OrgNo", true, true, 0, 50, 10);

			//参数.
		map.AddTBAtParas(1000);


			///数据同步方案
			//数据同步方式.
		map.AddTBInt(FlowAttr.DataDTSWay, DataDTSWay.None.getValue(), "同步方式", true, true);
			//map.AddTBString(FlowAttr.DTSDBSrc, null, "数据源", true, false, 0, 200, 100, false);
			//map.AddTBString(FlowAttr.DTSBTable, null, "业务表名", true, false, 0, 200, 100, false);
			//map.AddTBString(FlowAttr.DTSBTablePK, null, "业务表主键", false, false, 0, 32, 10);

		map.AddTBInt(FlowAttr.DTSTime, FlowDTSTime.AllNodeSend.getValue(), "执行同步时间点", true, true);
		map.AddTBString(FlowAttr.DTSFields, null, "要同步的字段s,中间用逗号分开.", false, false, 0, 900, 100, false);

			/// 数据同步方案

			// map.AddSearchAttr(FlowAttr.FK_FlowSort);
			// map.AddSearchAttr(FlowAttr.FlowRunWay);

		RefMethod rm = new RefMethod();
		rm.Title = "设计检查报告"; // "设计检查报告";
		rm.ToolTip = "检查流程设计的问题。";
		rm.Icon = "../../WF/Img/Btn/Confirm.gif";
		rm.ClassMethodName = this.toString() + ".DoCheck";
		rm.GroupName = "流程维护";
		map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///


		/// 公共方法
	/** 
	 创建索引
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String CreateIndex() throws NumberFormatException, Exception
	{
		// 为track表创建索引.  FID, WorkID
		String ptable = "ND" + Integer.parseInt(this.getPTable()) + "Track";

		// DBAccess.CreatIndex(DBUrlType.AppCenterDSN, ptable, "my");

		return "流程[" + this.getNo()+ "." + this.getName() + "]索引创建成功.";
	}
	/** 
	 删除数据.
	 
	 @return 
	*/
	public final String DoDelData()throws Exception
	{

			///删除独立表单的数据.
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

			/// 删除独立表单的数据.

		String sql = "  WHERE FK_Node in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		String sql1 = " WHERE NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		// DBAccess.RunSQL("DELETE FROM WF_CHOfFlow WHERE FK_Flow='" + this.getNo()+ "'");

		DBAccess.RunSQL("DELETE FROM WF_Bill WHERE FK_Flow='" + this.getNo()+ "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE FK_Flow='" + this.getNo()+ "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo()+ "'");

		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo()+ "'");

		String sqlIn = " WHERE ReturnNode IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork " + sqlIn);
		DBAccess.RunSQL("DELETE FROM WF_SelectAccper " + sql);
		DBAccess.RunSQL("DELETE FROM WF_TransferCustom " + sql);
		// DBAccess.RunSQL("DELETE FROM WF_FileManager " + sql);
		DBAccess.RunSQL("DELETE FROM WF_RememberMe " + sql);

		if (DBAccess.IsExitsObject("ND" + Integer.parseInt(this.getNo()) + "Track"))
		{
			DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(this.getNo()) + "Track ");
		}

		if (DBAccess.IsExitsObject(this.getPTable()))
		{
			DBAccess.RunSQL("DELETE FROM " + this.getPTable());
		}

		DBAccess.RunSQL("DELETE FROM WF_CH WHERE FK_Flow='" + this.getNo()+ "'");
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
		MapDtls mydtls = new MapDtls("ND" + Integer.parseInt(this.getNo()) + "Rpt");
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
		File info = new File(path);
		DataSet ds = new DataSet();

		try
		{
			ds.readXml(path);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@导入流程路径:" + path + "出错：" + ex.getMessage());
		}


		boolean isFlow = false;
		for (DataTable dts : ds.Tables) {
			if (dts.TableName.equals("WF_Flow")) {
				isFlow = true;
				break;
			}
		}
		if (isFlow ==false) {
			throw new RuntimeException("导入错误，非流程模版文件。");
		}

		DataTable dtFlow = ds.GetTableByName("WF_Flow");
		Flow fl = new Flow();
		String oldFlowNo = dtFlow.Rows.get(0).getValue("No").toString();
		String oldFlowName = dtFlow.Rows.get(0).getValue("Name").toString();

		int oldFlowID = Integer.parseInt(oldFlowNo);
		int iOldFlowLength = String.valueOf(oldFlowID).length();
		String timeKey = DataType.getCurrentDateByFormart("yyMMddhhmmss");


			///根据不同的流程模式，设置生成不同的流程编号.
		switch (model)
		{
			case AsNewFlow: //做为一个新流程.
				fl.setNo(fl.getGenerNewNo());
				//  fl.DoDelData();
				//    fl.DoDelete(); /*删除可能存在的垃圾.*/
				fl.Insert();
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
					fl.Insert();

				}
				break;
			case OvrewaiteCurrFlowNo: //覆盖当前的流程.
				fl.setNo(oldFlowNo);
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
				fl.Insert();
				break;
			case AsSpecFlowNo:
				if (SpecialFlowNo.length() <= 0)
				{
					throw new RuntimeException("@您是按照指定的流程编号导入的，但是您没有传入正确的流程编号。");
				}

				fl.setNo(SpecialFlowNo.toString());
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
				fl.Insert();
				break;
			default:
				throw new RuntimeException("@没有判断");
		}

			/// 根据不同的流程模式，设置生成不同的流程编号.

		// string timeKey = fl.getNo();
		int idx = 0;
		String infoErr = "";
		String infoTable = "";
		int flowID = Integer.parseInt(fl.getNo());


			///处理流程表数据
		for (DataColumn dc : dtFlow.Columns)
		{
			String val = dtFlow.Rows.get(0).getValue(dc.ColumnName) instanceof String ? (String)dtFlow.Rows.get(0).getValue(dc.ColumnName) : null;
			switch (dc.ColumnName.toLowerCase())
			{
				case "no":
				case "fk_flowsort":
					continue;
				case "name":
					// val = "复制:" + val + "_" + DateTime.Now.ToString("MM月dd日HH时mm分");
					break;
				default:
					break;
			}
			fl.SetValByKey(dc.ColumnName, val);
		}
		fl.setFK_FlowSort(fk_flowSort);
		if (DBAccess.IsExitsObject(fl.getPTable()) == true)
		{
			fl.setPTable(null);
		}
		//修改成当前登陆人所在的组织
		fl.setOrgNo(WebUser.getOrgNo());
		fl.Update();
		//判断该流程是否是公文流程，存在BuessFields、FlowBuessType、FK_DocType=01
		Attrs attrs = fl.getEnMap().getAttrs();
		if (attrs.Contains("FlowBuessType") == true)
		{
			DBAccess.RunSQL("UPDATE WF_Flow Set BuessFields='" + fl.GetParaString("BuessFields") + "', FlowBuessType=" + fl.GetParaInt("FlowBuessType") + " ,FK_DocType='" + fl.GetParaString("FK_DocType") + "'");
		}


			/// 处理流程表数据


			///处理OID 插入重复的问题 Sys_GroupField, Sys_MapAttr.
		DataTable mydtGF = ds.GetTableByName("Sys_GroupField");
		DataTable myDTAttr = ds.GetTableByName("Sys_MapAttr");
		DataTable myDTAth = ds.GetTableByName("Sys_FrmAttachment");
		DataTable myDTDtl = ds.GetTableByName("Sys_MapDtl");
		DataTable myDFrm = ds.GetTableByName("Sys_MapFrame");

		//throw new Exception("@" + fl.getNo() + fl.Name + ", 缺少：Sys_GroupField");
		for (DataRow dr : mydtGF.Rows)
		{
			GroupField gf = new GroupField();
			for (DataColumn dc : mydtGF.Columns)
			{
				String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
				gf.SetValByKey(dc.ColumnName, val);
			}
			long oldID = gf.getOID();
			gf.setOID(DBAccess.GenerOID());
			dr.setValue("OID", gf.getOID()); //给他一个新的OID.

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

			// 附件。
			if (myDTAth != null && myDTAth.Columns.contains("GroupID"))
			{
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

			// frm.
			if (myDFrm != null && myDFrm.Columns.contains("GroupID"))
			{
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

			/// 处理OID 插入重复的问题。 Sys_GroupField ， Sys_MapAttr.

		int timeKeyIdx = 0;
		for (DataTable dt : ds.Tables)
		{
			timeKeyIdx++;
			timeKey = timeKey + String.valueOf(timeKeyIdx);

			infoTable = "@导入:" + dt.TableName + " 出现异常。";
			switch (dt.TableName)
			{
				case "WF_Flow": //模版文件。
					continue;
				case "WF_NodeSubFlow": //延续子流程.
					for (DataRow dr : dt.Rows)
					{
						SubFlowYanXu yg = new SubFlowYanXu();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "tonodeid":
								case "fk_node":
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_NodeSubFlow下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
									val = fl.getNo();
									break;
								default:
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
							}
							yg.SetValByKey(dc.ColumnName, val);
						}
						yg.Insert();
					}
					continue;
				case "WF_FlowForm": //独立表单。 add 2013-12-03
					//foreach (DataRow dr in dt.Rows)
					//{
					//    FlowForm cd = new FlowForm();
					//    foreach (DataColumn dc in dt.Columns)
					//    {
					//        string val = dr[dc.ColumnName] as string;
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
					break;
				case "WF_NodeForm": //节点表单权限。 2013-12-03
					for (DataRow dr : dt.Rows)
					{
						NodeToolbar cd = new NodeToolbar();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "tonodeid":
								case "fk_node":
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_NodeForm下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
									val = fl.getNo();
									break;
								default:
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.Insert();
					}
					break;
				case "Sys_FrmSln": //表单字段权限。 2013-12-03
					for (DataRow dr : dt.Rows)
					{
						FrmField cd = new FrmField();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "tonodeid":
								case "fk_node":
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点Sys_FrmSln下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
									val = fl.getNo();
									break;
								default:
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.Insert();
					}
					break;
				case "WF_NodeToolbar": //工具栏。
					for (DataRow dr : dt.Rows)
					{
						NodeToolbar cd = new NodeToolbar();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "tonodeid":
								case "fk_node":
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_NodeToolbar下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
									val = fl.getNo();
									break;
								default:
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.setOID(DBAccess.GenerOID());
						cd.DirectInsert();
					}
					break;
				case "WF_BillTemplate":
					continue; //因为省掉了 打印模板的处理。
					/*for (DataRow dr : dt.Rows)
					{
						BillTemplate bt = new BillTemplate();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_flow":
									val = String.valueOf(flowID);
									break;
								case "nodeid":
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_BillTemplate下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
							}
							bt.SetValByKey(dc.ColumnName, val);
						}
						int i = 0;
						String no = bt.getNo();
						while (bt.getIsExits())
						{
							bt.setNo(no + String.valueOf(i));
							i++;
						}

						try
						{
							Files.copy(Paths.get(info.getParent() + "\\" + no + ".rtf"), Paths.get(SystemConfig.getPathOfWebApp() + "\\DataUser\\CyclostyleFile\\" + bt.getNo() + ".rtf"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
						}
						catch (RuntimeException ex)
						{
							// infoErr += "@恢复单据模板时出现错误：" + ex.Message + ",有可能是您在复制流程模板时没有复制同目录下的单据模板文件。";
						}
						bt.Insert();
					}
					break;*/
				case "WF_FrmNode": //Conds.xml。
					DBAccess.RunSQL("DELETE FROM WF_FrmNode WHERE FK_Flow='" + fl.getNo() + "'");
					for (DataRow dr : dt.Rows)
					{
						FrmNode fn = new FrmNode();
						fn.setFK_Flow(fl.getNo());
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_FrmNode下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
									val = fl.getNo();
									break;
								default:
									break;
							}
							fn.SetValByKey(dc.ColumnName, val);
						}
						// 开始插入。
						fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node());
						fn.Insert();
					}
					break;
				case "WF_FindWorkerRole": //找人规则
					for (DataRow dr : dt.Rows)
					{
						FindWorkerRole en = new FindWorkerRole();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_FindWorkerRole下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
									val = fl.getNo();
									break;
								default:
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
							}
							en.SetValByKey(dc.ColumnName, val);
						}

						//插入.
						en.DirectInsert();
					}
					break;
				case "WF_Cond": //Conds.xml。
					for (DataRow dr : dt.Rows)
					{
						Cond cd = new Cond();
						cd.setFK_Flow(fl.getNo());
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							switch (dc.ColumnName.toLowerCase())
							{
								case "tonodeid":
								case "fk_node":
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_Cond下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
									val = fl.getNo();
									break;
								default:
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
							}
							cd.SetValByKey(dc.ColumnName, val);
						}

						cd.setFK_Flow(fl.getNo());

						//  return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.Stas.ToString();
						// ，开始插入。 
						if (cd.getMyPK().contains("Stas"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getCondType().toString() + "_" + ConnDataFrom.Stas.toString());
						}
						else if (cd.getMyPK().contains("Dept"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getCondType().toString() + "_" + ConnDataFrom.Depts.toString());
						}
						else if (cd.getMyPK().contains("Paras"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getCondType().toString() + "_" + ConnDataFrom.Paras.toString());
						}
						else if (cd.getMyPK().contains("Url"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getCondType().toString() + "_" + ConnDataFrom.Url.toString());
						}
						else if (cd.getMyPK().contains("SQL"))
						{
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getCondType().toString() + "_" + ConnDataFrom.SQL);
						}
						else
						{
							cd.setMyPK(String.valueOf(DBAccess.GenerOID()) + DataType.getCurrentDateByFormart("yyMMddHHmmss"));
						}
						cd.DirectInsert();
					}
					break;
				case "WF_CCDept": //抄送到部门。
					for (DataRow dr : dt.Rows)
					{
						CCDept cd = new CCDept();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_CCDept下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
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
					break;
				case "WF_NodeReturn": //可退回的节点。
					for (DataRow dr : dt.Rows)
					{
						NodeReturn cd = new NodeReturn();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
								case "returnto":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_NodeReturn下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
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
					break;
				case "WF_Direction": //方向。
					for (DataRow dr : dt.Rows)
					{
						Direction dir = new Direction();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "node":
								case "tonode":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_Direction下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
							}
							dir.SetValByKey(dc.ColumnName, val);
						}
						dir.setFK_Flow(fl.getNo());
						dir.Insert();
					}
					break;
				case "WF_LabNote": //LabNotes.xml。
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						LabNote ln = new LabNote();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
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
					break;
				case "WF_NodeDept": //FAppSets.xml。
					for (DataRow dr : dt.Rows)
					{
						NodeDept dp = new NodeDept();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_NodeDept下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
							}
							dp.SetValByKey(dc.ColumnName, val);
						}
						try
						{
							//如果部门不属于本组织的，就要删除.  @sly 
							if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
							{
								bp.wf.port.admin2.Dept dept = new bp.wf.port.admin2.Dept(dp.getFK_Dept());
								if (dept.getOrgNo().equals(WebUser.getOrgNo()) == false)
								{
									continue;
								}
							}
							dp.Insert();
						}
						catch (RuntimeException ex)
						{
						}
					}
					break;
				case "WF_Node": //导入节点信息.
					for (DataRow dr : dt.Rows)
					{
						bp.wf.template.NodeExt nd = new bp.wf.template.NodeExt();
						bp.wf.template.CC cc = new CC(); // 抄送相关的信息.
						//cc.CheckPhysicsTable();
						bp.wf.template.NodeWorkCheck fwc = new NodeWorkCheck();

						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							switch (dc.ColumnName.toLowerCase())
							{
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_Node下nodeid值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
								case "fk_flowsort":
									continue;
								case "showsheets":
								case "histonds":
								case "groupstands": //去除不必要的替换
									String key = "@" + flowID;
									val = val.replace(key, "@");
									break;
								default:
									break;
							}
							nd.SetValByKey(dc.ColumnName, val);
							cc.SetValByKey(dc.ColumnName, val);
							fwc.SetValByKey(dc.ColumnName, val);

						}

						nd.setFK_Flow(fl.getNo());
						nd.setFlowName(fl.getName());
						try
						{

							if (nd.getEnMap().getAttrs().Contains("OfficePrintEnable"))
							{
								if (nd.GetValStringByKey("OfficePrintEnable").equals("打印"))
								{
									nd.SetValByKey("OfficePrintEnable", 0);
								}
							}

							nd.DirectInsert();

							//把抄送的信息也导入里面去.
							cc.DirectUpdate();
							fwc.setFWCVer(1); //设置为2019版本. 2018版是1个节点1个人,仅仅显示1个意见.
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
						nd.setNodeID(Integer.parseInt(dr.getValue(NodeAttr.NodeID).toString()));
						nd.RetrieveFromDBSources();
						nd.setFK_Flow(fl.getNo());
						//获取表单类别
						String formType = dr.getValue(NodeAttr.FormType).toString();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "nodefrmid":
									//绑定表单库的表单11不需要替换表单编号
									if (formType.equals("11") == false)
									{
										int iFormTypeLength = iOldFlowLength + 2;
										if (val.length() > iFormTypeLength)
										{
											val = "ND" + flowID + val.substring(iFormTypeLength);
										}
									}
									break;
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_Node下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
								case "fk_flowsort":
									continue;
								case "showsheets":
								case "histonds":
								case "groupstands": //修复替换
									String key = "@" + flowID;
									val = val.replace(key, "@");
									break;
								default:
									break;
							}
							nd.SetValByKey(dc.ColumnName, val);
						}
						nd.setFK_Flow(fl.getNo());
						nd.setFlowName(fl.getName());
						nd.DirectUpdate();
					}
					break;
				case "WF_NodeExt":
					for (DataRow dr : dt.Rows)
					{
						bp.wf.template.NodeExt nd = new bp.wf.template.NodeExt();
						nd.setNodeID(Integer.parseInt(flowID + dr.getValue(NodeAttr.NodeID).toString().substring(iOldFlowLength)));
						nd.RetrieveFromDBSources();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							switch (dc.ColumnName.toLowerCase())
							{
								case "nodeid":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_Node下nodeid值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
								case "fk_flowsort":
									continue;
								case "showsheets":
								case "histonds":
								case "groupstands": //去除不必要的替换
									String key = "@" + flowID;
									val = val.replace(key, "@");
									break;
								default:
									break;
							}
							nd.SetValByKey(dc.ColumnName, val);
						}

						nd.DirectUpdate();
					}
					break;
				case "WF_Selector":
					for (DataRow dr : dt.Rows)
					{
						Selector selector = new Selector();
						for (DataColumn dc : dt.Columns)
						{

							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							if (dc.ColumnName.toLowerCase().equals("nodeid"))
							{
								if (val.length() < iOldFlowLength)
								{
									// 节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
									throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_Node下FK_Node值错误:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							}

							selector.SetValByKey(dc.ColumnName, val);
						}
						selector.DirectUpdate();
					}
					break;
				case "WF_NodeStation": //FAppSets.xml。
					DBAccess.RunSQL("DELETE FROM WF_NodeStation WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + fl.getNo() + "')");
					for (DataRow dr : dt.Rows)
					{
						NodeStation ns = new NodeStation();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_NodeStation下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
							}
							ns.SetValByKey(dc.ColumnName, val);
						}
						ns.Insert();
					}
					break;
				case "Sys_Enum": //RptEmps.xml。
					for (DataRow dr : dt.Rows)
					{
						SysEnum se = new SysEnum();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									break;
								default:
									break;
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
					break;
				case "Sys_EnumMain": //RptEmps.xml。
					for (DataRow dr : dt.Rows)
					{
						SysEnumMain sem = new SysEnumMain();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
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
					break;
				case "Sys_MapAttr": //RptEmps.xml。
					for (DataRow dr : dt.Rows)
					{
						MapAttr ma = new MapAttr();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_mapdata":
								case "keyofen":
								case "autofulldoc":
									if (val == null)
									{
										continue;
									}
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
								default:
									break;
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
					break;
				case "Sys_MapData": //RptEmps.xml。
					for (DataRow dr : dt.Rows)
					{
						MapData md = new MapData();
						String htmlCode = "";
						for (DataColumn dc : dt.Columns)
						{
							if (dc.ColumnName.equals("HtmlTemplateFile"))
							{
								htmlCode = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
								continue;
							}

							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + Integer.parseInt(fl.getNo()));
							md.SetValByKey(dc.ColumnName, val);
						}
						md.Save();

						//如果是开发者表单，赋值HtmlTemplateFile数据库的值并保存到DataUser下
						if (md.getHisFrmType() == FrmType.Develop)
						{
							//string htmlCode = DBAccess.GetBigTextFromDB("Sys_MapData", "No", "ND" + oldFlowID, "HtmlTemplateFile");
							if (DataType.IsNullOrEmpty(htmlCode) == false)
							{
								htmlCode = htmlCode.replace("ND" + oldFlowID, "ND" + Integer.parseInt(fl.getNo()));
								//保存到数据库，存储html文件
								//保存到DataUser/CCForm/HtmlTemplateFile/文件夹下
								String filePath = SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/";
								if ((new File(filePath)).isDirectory() == false)
								{
									(new File(filePath)).mkdirs();
								}
								filePath = filePath + md.getNo() + ".htm";
								//写入到html 中
								bp.da.DataType.WriteFile(filePath, htmlCode);
								// HtmlTemplateFile 保存到数据库中
								DBAccess.SaveBigTextToDB(htmlCode, "Sys_MapData", "No", md.getNo(), "HtmlTemplateFile");
							}
							else
							{
								//如果htmlCode是空的需要删除当前节点的html文件
								String filePath = SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/" + md.getNo() + ".htm";
								if ((new File(filePath)).isFile() == true)
								{
									(new File(filePath)).delete();
								}
								DBAccess.SaveBigTextToDB("", "Sys_MapData", "No", md.getNo(), "HtmlTemplateFile");
							}
						}
					}

					break;
				case "Sys_MapDtl": //RptEmps.xml。
					for (DataRow dr : dt.Rows)
					{
						MapDtl md = new MapDtl();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							md.SetValByKey(dc.ColumnName, val);
						}
						md.Save();
						md.IntMapAttrs(); //初始化他的字段属性.
					}
					break;
				case "Sys_MapExt":
					for (DataRow dr : dt.Rows)
					{
						MapExt md = new MapExt();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
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
					break;
				case "Sys_FrmLine":
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLine en = new FrmLine();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.setMyPK(DBAccess.GenerGUID());
						// DBAccess.GenerOIDByGUID(); "LIE" + timeKey + "_" + idx;
						//if (en.IsExitGenerPK())
						//    continue;
						en.Insert();
					}
					break;
				case "Sys_FrmImg":
					idx = 0;
					timeKey = DataType.getCurrentDateByFormart("yyyyMMddHHmmss");
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImg en = new FrmImg();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.setMyPK(DBAccess.GenerGUID());
					}
					break;
				case "Sys_FrmLab":
					idx = 0;
					timeKey = DataType.getCurrentDateByFormart("yyyyMMddHHmmss");
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLab en = new FrmLab();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.setMyPK(DBAccess.GenerGUID()); // "Lab" + timeKey + "_" + idx;
						en.Insert();
					}
					break;
				case "Sys_FrmLink":
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLink en = new FrmLink();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val);
						}
						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();
					}
					break;
				case "Sys_FrmAttachment":
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmAttachment en = new FrmAttachment();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
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
					break;
				case "Sys_FrmEvent": //事件.
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmEvent en = new FrmEvent();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
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
						catch (java.lang.Exception e3)
						{
							en.Update();
						}
					}
					break;
				case "Sys_FrmRB": //Sys_FrmRB.
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmRB en = new FrmRB();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}
						en.Insert();
					}
					break;
				case "Sys_MapFrame":
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						MapFrame en = new MapFrame();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}


							en.SetValByKey(dc.ColumnName, val.toString().replace("ND" + oldFlowID, "ND" + flowID));
						}
						en.DirectInsert();
					}
					break;
				case "WF_NodeEmp": //FAppSets.xml。
					for (DataRow dr : dt.Rows)
					{
						NodeEmp ne = new NodeEmp();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_NodeEmp下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Insert();
					}
					break;
				case "Sys_GroupField": //这里需要对比一下翻译.
					for (DataRow dr : dt.Rows)
					{
						GroupField gf = new GroupField();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) !=null ? dr.getValue(dc.ColumnName).toString() : null;
							if (val == null)
							{
								continue;
							}
							switch (dc.ColumnName.toLowerCase())
							{
								case "enname":
								case "keyofen":
								case "ctrlid": //升级傻瓜表单的时候,新增加的字段 add by zhoupeng 2016.11.21
								case "frmid": //升级傻瓜表单的时候,新增加的字段 add by zhoupeng 2016.11.21
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
								default:
									break;
							}
							gf.SetValByKey(dc.ColumnName, val);
						}
						gf.InsertAsOID(gf.getOID());

						/*
						string sql = "select * from Sys_GroupField where CtrlID = '" + gf.CtrlID + "' AND FrmID='" + gf.FrmID + "'";
						int count = DBAccess.RunSQLReturnCOUNT(sql);
						if (count > 0)
						{
						    DBAccess.RunSQL("delete from Sys_GroupField where CtrlID = '" + gf.CtrlID + "' AND FrmID='" + gf.FrmID + "'");
						}
						int oid = DBAccess.GenerOID();
						DBAccess.RunSQL("UPDATE Sys_MapAttr SET GroupID='" + oid + "' WHERE FK_MapData='" + gf.FrmID + "' AND GroupID='" + gf.getOID() + "'");
						gf.InsertAsOID(oid); */
					}
					break;
				case "WF_CCEmp": // 抄送.
					for (DataRow dr : dt.Rows)
					{
						CCEmp ne = new CCEmp();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) !=null ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_CCEmp下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Insert();
					}
					break;
				case "WF_CCStation": // 抄送.
					String mysql = " DELETE FROM WF_CCStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + flowID + "')";
					DBAccess.RunSQL(mysql);
					for (DataRow dr : dt.Rows)
					{
						CCStation ne = new CCStation();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_CCStation下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Save();
					}
					break;
					case "WF_PushMsg": // 发送消息
					for (DataRow dr : dt.Rows)
					{
						PushMsg pushMsg= new PushMsg();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) !=null ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.length() < iOldFlowLength)
									{
										//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点WF_PushMsg下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								case "fk_flow":
									val = fl.getNo();
									break;
								default:
									break;
							}
							pushMsg.SetValByKey(dc.ColumnName, val);
						}
						pushMsg.setMyPK(DBAccess.GenerGUID());
						pushMsg.Insert();
					}
					break;
				default:
					// infoErr += "Error:" + dt.TableName;
					break;
					//    throw new Exception("@unhandle named " + dt.TableName);
			}
		}


			///处理数据完整性。

		//@sly 删除.
		//DBAccess.RunSQL("UPDATE WF_Cond SET FK_Node=NodeID WHERE FK_Node=0");
		//DBAccess.RunSQL("UPDATE WF_Cond SET ToNodeID=NodeID WHERE ToNodeID=0");
		// DBAccess.RunSQL("DELETE FROM WF_Cond WHERE NodeID NOT IN (SELECT NodeID FROM WF_Node)");
		//  DBAccess.RunSQL("DELETE FROM WF_Cond WHERE ToNodeID NOT IN (SELECT NodeID FROM WF_Node) ");
		// DBAccess.RunSQL("DELETE FROM WF_Cond WHERE FK_Node NOT IN (SELECT NodeID FROM WF_Node) AND FK_Node > 0");

		//处理分组错误.
		Nodes nds = new Nodes(fl.getNo());
		for (Node nd : nds.ToJavaList())
		{
			MapFrmFool cols = new MapFrmFool("ND" + nd.getNodeID());
			cols.DoCheckFixFrmForUpdateVer();
		}

			///

		//处理OrgNo 的导入问题.
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.GroupInc)
		{
			fl.RetrieveFromDBSources();
			fl.setOrgNo(WebUser.getOrgNo());
			fl.DirectUpdate();
		}

		if (infoErr.equals(""))
		{
			infoTable = "";
			return fl; // "完全成功。";
		}

		infoErr = "@执行期间出现如下非致命的错误：\t\r" + infoErr + "@ " + infoTable;
		throw new RuntimeException(infoErr);
	}

	/** 
	 检查报表
	 * @throws Exception 
	*/
	public final void CheckRpt() throws Exception
	{
		this.DoCheck_CheckRpt(this.getHisNodes());
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(WebUser.getOrgNo());
		}

		return super.beforeInsert();
	}
	/** 
	 更新之前做检查
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		//删除缓存数据.
		this.ClearAutoNumCash(false);

		this.setVer(DataType.getCurrentDataTimess());
		return super.beforeUpdate();
	}
	/** 
	 更新版本号
	*/
	public static void UpdateVer(String flowNo)
	{
		String sql = "UPDATE WF_Flow SET Ver='" + DataType.getCurrentDataTimess() + "' WHERE No='" + flowNo + "'";
		DBAccess.RunSQL(sql);
	}
	/** 
	 删除功能.
	 
	 @return 
	*/
	
	public final String DoDelete() throws Exception
	{
		if (DataType.IsNullOrEmpty(this.getNo()) == true)
		{
			throw new RuntimeException("err@流程没有初始化，删除错误.");
		}

		//如果是广西计算中心，不知道为什么删除了.
		if (SystemConfig.getCustomerNo().equals("GXJSZX") == true)
		{
			throw new RuntimeException("err@目前暂时不支持[DoDelete - " + this.getNo()+ "]请立即联系管理员.");
		}

		//检查流程有没有版本管理？
		if (this.getFK_FlowSort().length() > 1)
		{
			String str = "SELECT * FROM WF_Flow WHERE PTable='" + this.getPTable() + "' AND FK_FlowSort='' ";
			DataTable dt = DBAccess.RunSQLReturnTable(str);
			if (dt.Rows.size() >= 1)
			{
				return "err@删除流程出错，该流程下有[" + dt.Rows.size() + "]个子版本您不能删除。";
			}
		}
		String sql = "";
		//sql = " DELETE FROM WF_chofflow WHERE FK_Flow='" + this.getNo()+ "'";
		sql += "@ DELETE FROM WF_GenerWorkerlist WHERE FK_Flow='" + this.getNo()+ "'";
		sql += "@ DELETE FROM  WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo()+ "'";
		sql += "@ DELETE FROM  WF_Cond WHERE RefFlowNo='" + this.getNo()+ "'";

		//删除消息配置.
		sql += "@ DELETE FROM WF_PushMsg WHERE FK_Flow='" + this.getNo()+ "'";

		// 删除岗位节点。
		sql += "@ DELETE FROM WF_NodeStation WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		// 删除方向。
		sql += "@ DELETE FROM WF_Direction WHERE FK_Flow='" + this.getNo()+ "'";

		//删除节点绑定信息.
		sql += "@ DELETE FROM WF_FrmNode WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		sql += "@ DELETE FROM WF_NodeEmp  WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		sql += "@ DELETE FROM WF_CCEmp WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		sql += "@ DELETE FROM WF_CCDept WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		sql += "@ DELETE FROM WF_CCStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		sql += "@ DELETE FROM WF_NodeReturn WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		sql += "@ DELETE FROM WF_NodeDept WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		sql += "@ DELETE FROM WF_NodeStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		sql += "@ DELETE FROM WF_NodeEmp WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		sql += "@ DELETE FROM WF_NodeToolbar WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		sql += "@ DELETE FROM WF_SelectAccper WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		//sql += "@ DELETE FROM WF_TurnTo WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		//删除侦听.
		// sql += "@ DELETE FROM WF_Listen WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		// 删除d2d数据.
		//  sql += "@GO DELETE WF_M2M WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		//// 删除配置.
		//sql += "@ DELETE FROM WF_FAppSet WHERE NodeID IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";


		//// 外部程序设置
		//sql += "@ DELETE FROM WF_FAppSet WHERE  NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";

		//删除单据.
		sql += "@ DELETE FROM WF_BillTemplate WHERE  NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "')";
		//删除权限控制.
		sql += "@ DELETE FROM Sys_FrmSln WHERE FK_Flow='" + this.getNo()+ "'";
		//考核表
		sql += "@ DELETE FROM WF_CH WHERE FK_Flow='" + this.getNo()+ "'";
		//删除抄送
		sql += "@ DELETE FROM WF_CCList WHERE FK_Flow='" + this.getNo()+ "'";
		Nodes nds = new Nodes(this.getNo());
		for (Node nd : nds.ToJavaList())
		{
			// 删除节点所有相关的东西.
			nd.Delete();
		}

		sql += "@ DELETE  FROM WF_Node WHERE FK_Flow='" + this.getNo()+ "'";
		sql += "@ DELETE  FROM WF_LabNote WHERE FK_Flow='" + this.getNo()+ "'";

		//删除分组信息
		sql += "@ DELETE FROM Sys_GroupField WHERE FrmID NOT IN(SELECT NO FROM Sys_MapData)";


			///删除流程报表,删除轨迹
		MapData md = new MapData();
		md.setNo("ND" + Integer.parseInt(this.getNo()) + "Rpt");
		md.Delete();

		//删除视图.
		if (DBAccess.IsExitsObject("V_" + this.getNo()))
		{
			DBAccess.RunSQL("DROP VIEW V_" + this.getNo());
		}

		//删除轨迹.
		if (DBAccess.IsExitsObject("ND" + Integer.parseInt(this.getNo()) + "Track") == true)
		{
			DBAccess.RunSQL("DROP TABLE ND" + Integer.parseInt(this.getNo()) + "Track ");
		}


			/// 删除流程报表,删除轨迹.

		// 执行录制的sql scripts.
		DBAccess.RunSQLs(sql);

		//清空WF_Emp中的StartFlow 
		DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");

		//删除数据的接口.
		DoDelData();

		this.Delete(); //删除需要移除缓存.

		return "执行成功.";
	}

	/** 
	 向上移动
	 * @throws Exception 
	*/
	public final void DoUp() throws Exception
	{
		this.DoOrderUp(FlowAttr.FK_FlowSort, this.getFK_FlowSort(), FlowAttr.Idx);
	}
	/** 
	 向下移动
	 * @throws Exception 
	*/
	public final void DoDown() throws Exception
	{
		this.DoOrderDown(FlowAttr.FK_FlowSort, this.getFK_FlowSort(), FlowAttr.Idx);
	}

		///


		///版本管理.
	/** 
	 创建新版本.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String VerCreateNew() throws Exception
	{
		try
		{
			//生成模版.
			String file = GenerFlowXmlTemplete();

			Flow newFlow = Flow.DoLoadFlowTemplate(this.getFK_FlowSort(), file, ImpFlowTempleteModel.AsNewFlow);

			newFlow.setPTable(this.getPTable());
			newFlow.setFK_FlowSort(""); //不能显示流程树上.
			newFlow.setName( this.getName());
			newFlow.setVer(DataType.getCurrentDataTime());
			newFlow.setIsCanStart(false); //不能发起.
			newFlow.DirectUpdate();
			return newFlow.getNo();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}

		//DataSet ds = this.GenerFlowXmlTemplete();
		//return "001";
	}
	/** 
	 设置当前的版本为新版本.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String VerSetCurrentVer() throws Exception
	{
		String sql = "SELECT FK_FlowSort,No FROM WF_Flow WHERE PTable='" + this.getPTable() + "' AND FK_FlowSort!='' ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return "err@没有找到主版本,请联系管理员.";
		}
		String flowSort = dt.Rows.get(0).getValue(0).toString();
		String oldFlowNo = dt.Rows.get(0).getValue(1).toString();
		sql = "UPDATE WF_Flow SET FK_FlowSort='',IsCanStart=0 WHERE PTable='" + this.getPTable() + "' ";
		DBAccess.RunSQL(sql);

		sql = "UPDATE WF_Flow SET FK_FlowSort='" + flowSort + "', IsCanStart=1 WHERE No='" + this.getNo()+ "' ";
		DBAccess.RunSQL(sql);

		//清缓存
		bp.da.Cash2019.DeleteRow("bp.wf.Flow", oldFlowNo);
		bp.da.Cash2019.DeleteRow("bp.wf.Flow", this.getNo());
		Flow flow = new Flow(oldFlowNo);
		flow = new Flow(this.getNo());


		return "info@设置成功.";
	}
	/** 
	 获得版本列表.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String VerGenerVerList() throws Exception
	{
		//if (this.FK_FlowSort.Equals("") == true)
		//    return "err@当前版本为分支版本，您无法管理，只有主版本才能管理。";

		DataTable dt = new DataTable();
		dt.Columns.Add("Ver"); //版本号
		dt.Columns.Add("No"); //内部编号
		dt.Columns.Add("Name"); //流程名称.
		dt.Columns.Add("IsRel"); //是否发布？
		dt.Columns.Add("NumOfRuning"); //运行中的流程.
		dt.Columns.Add("NumOfOK"); //运行完毕的流程.

		//如果业务表是空的，就更新它.
		String ptable = this.GetValStringByKey(FlowAttr.PTable);
		if (DataType.IsNullOrEmpty(ptable) == true)
		{
			this.SetValByKey(FlowAttr.PTable, this.getPTable());
			this.DirectUpdate();
		}

		//获得所有的版本.
		String sql = "SELECT No  FROM WF_Flow WHERE PTable='" + this.getPTable() + "'";
		Flows fls = new Flows();
		fls.RetrieveInSQL(sql);

		for (Flow item : fls.ToJavaList())
		{
			DataRow dr = dt.NewRow();
			dr.setValue("Ver", item.getVer());
			dr.setValue("No", item.getNo());
			dr.setValue("Name", item.getName());

			if (DataType.IsNullOrEmpty(item.getFK_FlowSort()) == true)
			{
				dr.setValue("IsRel", "0");
			}
			else
			{
				dr.setValue("IsRel", "1");
			}

			dr.setValue("NumOfRuning", DBAccess.RunSQLReturnValInt("SELECT COUNT(WORKID) FROM WF_GenerWorkFlow WHERE FK_FLOW='" + item.getNo() + "' AND WFState=2"));
			dr.setValue("NumOfOK", DBAccess.RunSQLReturnValInt("SELECT COUNT(WORKID) FROM WF_GenerWorkFlow WHERE FK_FLOW='" + item.getNo() + "' AND WFState=3"));
			dt.Rows.add(dr);
		}

		return bp.tools.Json.ToJson(dt);
	}

		/// 版本管理.

}