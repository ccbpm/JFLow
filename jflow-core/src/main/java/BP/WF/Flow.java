package BP.WF;

import BP.DA.*;
import BP.Sys.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.Data.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.math.*;

/** 
 流程
 记录了流程的信息．
 流程的编号，名称，建立时间．
*/
public class Flow extends BP.En.EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 参数属性.
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
		//int x1 = DBAccess.RunSQLReturnValInt("SELECT MAX(X) FROM WF_Node WHERE FK_Flow='" + this.No + "'", 0);
		//int x2 = DBAccess.RunSQLReturnValInt("SELECT MAX(X) FROM WF_NodeLabelNode WHERE FK_Flow='" + this.No + "'", 0);
		//this.MaxY = DBAccess.RunSQLReturnValInt("SELECT MAX(Y) FROM WF_Node WHERE FK_Flow='" + this.No + "'", 0);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 参数属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		if (DataType.IsNullOrEmpty(str))
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 业务数据表同步属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基础属性.
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
			return this.No;
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 发起限制.
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 发起限制.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 导航模式
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
		if (this.getStartGuideWay() == Template.StartGuideWay.BySelfUrl)
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
		if (DataType.IsNullOrEmpty(str) == null)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他属性
	/** 
	 流程删除规则
	*/
	public final FlowDeleteRole getFlowDeleteRole()
	{
		return FlowDeleteRole.forValue(this.GetValIntByKey(FlowAttr.FlowDeleteRole));
	}
	/** 
	 草稿规则
	*/
	public final DraftRole getDraftRole()
	{
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
	public final SDTOfFlowRole getSDTOfFlowRole()
	{
		return SDTOfFlowRole.forValue(this.GetValIntByKey(FlowAttr.SDTOfFlowRole));
	}
	/** 
	 按照SQL来计算流程完成时间
	*/
	public final String getSDTOfFlowRoleSQL()
	{
		return this.GetValStrByKey(FlowAttr.SDTOfFlowRoleSQL);
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
	 是否可以在手机里启用
	*/
	public final boolean getIsStartInMobile()
	{
		return this.GetValBooleanByKey(FlowAttr.IsStartInMobile);
	}
	public final void setIsStartInMobile(boolean value)
	{
		this.SetValByKey(FlowAttr.IsStartInMobile, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 业务处理

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 创建新工作.
	/** 
	 创建新工作web方式调用的
	 
	 @return 
	*/
	public final Work NewWork()
	{
		return NewWork(WebUser.No);
	}
	/** 
	 创建新工作.web方式调用的
	 
	 @param empNo 人员编号
	 @return 
	*/
	public final Work NewWork(String empNo)
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
	public final Work NewWork(Emp emp, Hashtable paras)
	{
		// 检查是否可以发起该流程？
		if (Glo.CheckIsCanStartFlow_InitStartFlow(this) == false)
		{
			throw new RuntimeException("@您违反了该流程的【" + this.getStartLimitRole() + "】限制规则。" + this.getStartLimitAlert());
		}

		//如果是bs系统.
		if (paras == null)
		{
			paras = new Hashtable();
		}
		if (BP.Sys.SystemConfig.IsBSsystem == true)
		{
			for (String k : HttpContextHelper.RequestParamKeys)
			{
				if (k.equals("OID") || k.equals("WorkID") || k == null)
				{
					continue;
				}

				if (paras.containsKey(k))
				{
					paras.put(k, HttpContextHelper.RequestParams(k)); // BP.Sys.Glo.Request.QueryString[k];
				}
				else
				{
					paras.put(k, HttpContextHelper.RequestParams(k));
				}
			}
		}

		//开始节点.
		BP.WF.Node nd = new BP.WF.Node(this.getStartNodeID());

		//从草稿里看看是否有新工作？
		StartWork wk = (StartWork)nd.getHisWork();
		try
		{
			wk.ResetDefaultVal();
		}
		catch (RuntimeException ex)
		{
			wk.ResetDefaultVal();
		}

		String dbstr = SystemConfig.AppCenterDBVarStr;

		Paras ps = new Paras();
		GERpt rpt = this.getHisGERpt();

		//是否新创建的WorkID
		boolean IsNewWorkID = false;
		/*如果要启用草稿,就创建一个新的WorkID .*/
		if (this.getDraftRole() != Template.DraftRole.None && nd.getIsStartNode())
		{
			IsNewWorkID = true;
		}

		try
		{
			//从报表里查询该数据是否存在？
			if (this.getIsGuestFlow() == true && DataType.IsNullOrEmpty(GuestUser.No) == false)
			{
				/*是客户参与的流程，并且具有客户登陆的信息。*/
				ps.SQL = "SELECT OID,FlowEndNode FROM " + this.getPTable() + " WHERE GuestNo=" + dbstr + "GuestNo AND WFState=" + dbstr + "WFState ";
				ps.Add(GERptAttr.GuestNo, GuestUser.No);
				ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() > 0 && IsNewWorkID == false)
				{
					wk.setOID(Long.parseLong(dt.Rows[0][0].toString()));
					int nodeID = Integer.parseInt(dt.Rows[0][1].toString());
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
				ps.Add(GERptAttr.FlowStarter, emp.No);
				ps.Add(GenerWorkFlowAttr.FK_Flow, this.No);
				DataTable dt = DBAccess.RunSQLReturnTable(ps);

				//如果没有启用草稿，并且存在草稿就取第一条 by dgq 5.28
				if (dt.Rows.size() > 0)
				{
					wk.setOID(Long.parseLong(dt.Rows[0][0].toString()));
					wk.RetrieveFromDBSources();
					int nodeID = Integer.parseInt(dt.Rows[0][1].toString());
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
				/* 说明没有空白,就创建一个空白..*/
				wk.ResetDefaultVal();
				wk.setRec(WebUser.No);

				wk.SetValByKey(StartWorkAttr.RecText, emp.Name);
				wk.SetValByKey(StartWorkAttr.Emps, emp.No);

				wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.CurrentDataTime);
				wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.CurrentDataTime);
				wk.SetValByKey(GERptAttr.WFState, WFState.Blank.getValue());

				wk.setOID(DBAccess.GenerOID("WorkID")); //这里产生WorkID ,这是唯一产生WorkID的地方.

				//把尽量可能的流程字段放入，否则会出现冲掉流程字段属性.
				wk.SetValByKey(GERptAttr.FK_NY, BP.DA.DataType.CurrentYearMonth);
				wk.SetValByKey(GERptAttr.FK_Dept, emp.FK_Dept);
				wk.setFID(0);

				try
				{
					wk.DirectInsert();
				}
				catch (java.lang.Exception e)
				{
					wk.CheckPhysicsTable();
					//    wk.DirectInsert();
				}

				//设置参数.
				for (String k : paras.keySet())
				{
					rpt.SetValByKey(k, paras.get(k));
				}

				if (this.getPTable().equals(wk.EnMap.PhysicsTable))
				{
					/*如果开始节点表与流程报表相等.*/
					rpt.setOID(wk.getOID());
					rpt.RetrieveFromDBSources();
					rpt.setFID(0);
					rpt.setFlowStartRDT(BP.DA.DataType.CurrentDataTime);
					rpt.setMyNum(0);
					rpt.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, wk));
					//WebUser.No + "," + BP.Web.WebUser.Name + "在" + DataType.CurrentDataCNOfShort + "发起.";
					rpt.setWFState(WFState.Blank);
					rpt.setFlowStarter(emp.No);
					rpt.setFK_NY(DataType.CurrentYearMonth);
					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
					{
						rpt.setFlowEmps("@" + emp.Name + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.No + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.No + "," + emp.Name + "@");
					}

					rpt.setFlowEnderRDT(BP.DA.DataType.CurrentDataTime);
					rpt.setFlowStartRDT(BP.DA.DataType.CurrentDataTime);

					rpt.setFK_Dept(emp.FK_Dept);
					rpt.setFlowEnder(emp.No);
					rpt.setFlowEndNode(this.getStartNodeID());
					rpt.setFlowStarter(emp.No);
					rpt.setWFState(WFState.Blank);
					rpt.setFID(0);
					rpt.DirectUpdate();
				}
				else
				{
					rpt.setOID(wk.getOID());
					rpt.setFID(0);
					rpt.setFlowStartRDT(BP.DA.DataType.CurrentDataTime);
					rpt.setFlowEnderRDT(BP.DA.DataType.CurrentDataTime);
					rpt.setMyNum(0);

					rpt.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, wk));
					// rpt.Title = WebUser.No + "," + BP.Web.WebUser.Name + "在" + DataType.CurrentDataCNOfShort + "发起.";

					rpt.setWFState(WFState.Blank);
					rpt.setFlowStarter(emp.No);

					rpt.setFlowEndNode(this.getStartNodeID());
					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
					{
						rpt.setFlowEmps("@" + emp.Name + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.No + "@");
					}

					if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
					{
						rpt.setFlowEmps("@" + emp.No + "," + emp.Name + "@");
					}

					rpt.setFK_NY(DataType.CurrentYearMonth);
					rpt.setFK_Dept(emp.FK_Dept);
					rpt.setFlowEnder(emp.No);
					rpt.setFlowStarter(emp.No);
					rpt.InsertAsOID(wk.getOID());
				}

				//调用 OnCreateWorkID的方法.  add by zhoupeng 2016.12.4 for LIMS.
				this.DoFlowEventEntity(EventListOfNode.FlowOnCreateWorkID, nd, wk, null);

			}

			if (wk.getOID() != 0)
			{
				rpt.setOID(wk.getOID());
				rpt.RetrieveFromDBSources();

				rpt.setFID(0);
				rpt.setFlowStartRDT(BP.DA.DataType.CurrentDataTime);
				rpt.setFlowEnderRDT(BP.DA.DataType.CurrentDataTime);
				rpt.setMyNum(1);

				//在发送的时候有更新.
				//   rpt.DirectUpdate();
			}
		}
		catch (RuntimeException ex)
		{
			wk.CheckPhysicsTable();

			//检查报表.
			this.CheckRpt();
			throw new RuntimeException("@创建工作失败：请您刷新一次，如果问题仍然存在请反馈给管理员，技术信息：" + ex.StackTrace + " @ 技术信息:" + ex.getMessage());
		}

		//在创建WorkID的时候调用的事件.
		this.DoFlowEventEntity(EventListOfNode.CreateWorkID, nd, wk, null);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region copy数据.
		// 记录这个id ,不让其它在复制时间被修改。
		long newOID = wk.getOID();
		if (IsNewWorkID == true)
		{
			// 处理传递过来的参数。
			int i = 0;

			String expKeys = "OID,DoType,HttpHandlerName,DoMethod,t,";
			for (String k : paras.keySet())
			{
				if (expKeys.indexOf("," + k + ",") != -1)
				{
					continue;
				}

				String str = paras.get(k) instanceof String ? (String)paras.get(k) : null;
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}
				i++;
				wk.SetValByKey(k, str);
			}

			if (i >= 3)
			{
				wk.DirectUpdate();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion copy数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理删除草稿的需求。
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
				BP.DA.Log.DebugWriteError("创建新工作错误，但是屏蔽了异常,请检查默认值的问题：" + ex.getMessage());
			}

			MapDtls dtls = wk.getHisMapDtls();
			for (MapDtl dtl : dtls)
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.PTable + " WHERE RefPK='" + oid + "'");
			}

			//删除附件数据。
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + wk.getNodeID() + "' AND RefPKVal='" + wk.getOID() + "'");
			wk.setOID(newOID);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理删除草稿的需求。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

			String innerKeys = ",OID,RDT,CDT,FID,WFState,WorkID,WORKID,WFSTATE";
			for (DataColumn dc : dt.Columns)
			{
				if (innerKeys.contains("," + dc.ColumnName.toUpperCase() + ","))
				{
					continue;
				}

				wk.SetValByKey(dc.ColumnName, dt.Rows[0][dc.ColumnName].toString());
				rpt.SetValByKey(dc.ColumnName, dt.Rows[0][dc.ColumnName].toString());
			}
			rpt.Update();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理开始节点, 如果传递过来 FromTableName 就是要从这个表里copy数据。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 获取特殊标记变量
		// 获取特殊标记变量.
		String PFlowNo = null;
		String PNodeIDStr = null;
		String PWorkIDStr = null;
		String PFIDStr = null;

		String CopyFormWorkID = null;
		if (paras.containsKey("CopyFormWorkID") == true)
		{
			CopyFormWorkID = paras.get("CopyFormWorkID").toString();
			PFlowNo = this.No;
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 获取特殊标记变量

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  判断是否装载上一条数据.
		if (this.getIsLoadPriData() == true && this.getStartGuideWay() == BP.WF.Template.StartGuideWay.None)
		{
			/* 如果需要从上一个流程实例上copy数据. */
			String sql = "SELECT OID FROM " + this.getPTable() + " WHERE FlowStarter='" + WebUser.No + "' AND OID!=" + wk.getOID() + " ORDER BY OID DESC";
			String workidPri = DBAccess.RunSQLReturnStringIsNull(sql, "0");
			if (workidPri.equals("0"))
			{
				/*说明没有第一笔数据.*/
			}
			else
			{
				PFlowNo = this.No;
				PNodeIDStr = Integer.parseInt(this.No) + "01";
				PWorkIDStr = workidPri;
				PFIDStr = "0";
				CopyFormWorkID = workidPri;
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion  判断是否装载上一条数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理流程之间的数据传递1。
		if (DataType.IsNullOrEmpty(PNodeIDStr) == false && DataType.IsNullOrEmpty(PWorkIDStr) == false)
		{
			long PWorkID = Long.parseLong(PWorkIDStr);
			long PNodeID = 0;
			if (CopyFormWorkID != null)
			{
				PNodeID = Long.parseLong(PNodeIDStr);
			}

			/* 如果是从另外的一个流程上传递过来的，就考虑另外的流程数据。*/

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region copy 首先从父流程的NDxxxRpt copy.
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

			wk.Copy(dt.Rows[0]);
			rpt.Copy(dt.Rows[0]);

			//设置单号为空.
			wk.SetValByKey("BillNo", "");
			rpt.setBillNo("");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion copy 首先从父流程的NDxxxRpt copy.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 从调用的节点上copy.
			BP.WF.Node fromNd = new BP.WF.Node(Integer.parseInt(PNodeIDStr));
			Work wkFrom = fromNd.getHisWork();
			wkFrom.setOID(PWorkID);
			if (wkFrom.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("@父流程的工作ID不正确，没有查询到数据" + PWorkID);
			}
			//wk.Copy(wkFrom);
			//rpt.Copy(wkFrom);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 从调用的节点上copy.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 获取web变量.
			for (String k : paras.keySet())
			{
				if (k.equals("OID"))
				{
					continue;
				}

				wk.SetValByKey(k, paras.get(k));
				rpt.SetValByKey(k, paras.get(k));
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 获取web变量.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 特殊赋值.
			wk.setOID(newOID);
			rpt.setOID(newOID);

			// 在执行copy后，有可能这两个字段会被冲掉。
			if (CopyFormWorkID != null)
			{
				/*如果不是 执行的从已经完成的流程copy.*/

				wk.SetValByKey(StartWorkAttr.PFlowNo, PFlowNo);
				wk.SetValByKey(StartWorkAttr.PNodeID, PNodeID);
				wk.SetValByKey(StartWorkAttr.PWorkID, PWorkID);

				rpt.SetValByKey(GERptAttr.PFlowNo, PFlowNo);
				rpt.SetValByKey(GERptAttr.PNodeID, PNodeID);
				rpt.SetValByKey(GERptAttr.PWorkID, PWorkID);

				//忘记了增加这句话.
				rpt.SetValByKey(GERptAttr.PEmp, WebUser.No);

				//要处理单据编号 BillNo .
				if (!this.getBillNoFormat().equals(""))
				{
					rpt.SetValByKey(GERptAttr.BillNo, BP.WF.WorkFlowBuessRole.GenerBillNo(this.getBillNoFormat(), rpt.getOID(), rpt, this.getPTable()));

					//设置单据编号.
					wk.SetValByKey(GERptAttr.BillNo, rpt.getBillNo());
				}

				rpt.SetValByKey(GERptAttr.FID, 0);
				rpt.SetValByKey(GERptAttr.FlowStartRDT, BP.DA.DataType.CurrentDataTime);
				rpt.SetValByKey(GERptAttr.FlowEnderRDT, BP.DA.DataType.CurrentDataTime);
				rpt.SetValByKey(GERptAttr.MyNum, 0);
				rpt.SetValByKey(GERptAttr.WFState, WFState.Blank.getValue());
				rpt.SetValByKey(GERptAttr.FlowStarter, emp.No);
				rpt.SetValByKey(GERptAttr.FlowEnder, emp.No);
				rpt.SetValByKey(GERptAttr.FlowEndNode, this.getStartNodeID());
				rpt.SetValByKey(GERptAttr.FK_Dept, emp.FK_Dept);
				rpt.SetValByKey(GERptAttr.FK_NY, DataType.CurrentYearMonth);

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.Name + "@");
				}

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.No + "@");
				}

				if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
				{
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.No + "," + emp.Name + "@");
				}

			}

			if (rpt.getEnMap().PhysicsTable != wk.EnMap.PhysicsTable)
			{
				wk.Update(); //更新工作节点数据.
			}
			rpt.Update(); // 更新流程数据表.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 特殊赋值.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 复制其他数据..
			//复制明细。
			MapDtls dtls = wk.getHisMapDtls();
			if (dtls.size() > 0)
			{
				MapDtls dtlsFrom = wkFrom.getHisMapDtls();
				int idx = 0;
				if (dtlsFrom.size() == dtls.size())
				{
					for (MapDtl dtl : dtls)
					{
						if (dtl.IsCopyNDData == false)
						{
							continue;
						}

						//new 一个实例.
						GEDtl dtlData = new GEDtl(dtl.No);

						//删除以前的数据.
						sql = "DELETE FROM " + dtlData.EnMap.PhysicsTable + " WHERE RefPK=" + wk.getOID();
						DBAccess.RunSQL(sql);


						MapDtl dtlFrom = dtlsFrom[idx] instanceof MapDtl ? (MapDtl)dtlsFrom[idx] : null;

						GEDtls dtlsFromData = new GEDtls(dtlFrom.No);
						dtlsFromData.Retrieve(GEDtlAttr.RefPK, PWorkID);
						for (GEDtl geDtlFromData : dtlsFromData)
						{
							//是否启用多附件
							FrmAttachmentDBs dbs = null;
							if (dtl.IsEnableAthM == true)
							{
								//根据从表的OID 获取附件信息
								dbs = new FrmAttachmentDBs();
								dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, geDtlFromData.OID);
							}

							dtlData.Copy(geDtlFromData);
							dtlData.RefPK = String.valueOf(wk.getOID());
							dtlData.FID = wk.getOID();
							if (this.No.equals(PFlowNo) == false && (this.getStartLimitRole() == WF.StartLimitRole.OnlyOneSubFlow))
							{
								dtlData.SaveAsOID(geDtlFromData.OID); //为子流程的时候，仅仅允许被调用1次.
							}
							else
							{
								dtlData.InsertAsNew();
								if (dbs != null && dbs.size() != 0)
								{
									//复制附件信息
									FrmAttachmentDB newDB = new FrmAttachmentDB();
									for (FrmAttachmentDB db : dbs)
									{
										newDB.Copy(db);
										newDB.RefPKVal = dtlData.OID.toString();
										newDB.FID = dtlData.OID;
										newDB.MyPK = BP.DA.DBAccess.GenerGUID();
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

					for (FrmAttachmentDB athDB : athDBs)
					{
						FrmAttachmentDB athDB_N = new FrmAttachmentDB();
						athDB_N.Copy(athDB);
						athDB_N.FK_MapData = "ND" + toNodeID;
						athDB_N.RefPKVal = String.valueOf(wk.getOID());
						athDB_N.FK_FrmAttachment = athDB_N.FK_FrmAttachment.Replace("ND" + PNodeIDStr, "ND" + toNodeID);

						if (athDB_N.HisAttachmentUploadType == AttachmentUploadType.Single)
						{
							/*如果是单附件.*/
							athDB_N.MyPK = athDB_N.FK_FrmAttachment + "_" + wk.getOID();
							if (athDB_N.IsExits == true)
							{
								continue; //说明上一个节点或者子线程已经copy过了, 但是还有子线程向合流点传递数据的可能，所以不能用break.
							}
							athDB_N.Insert();
						}
						else
						{
							athDB_N.MyPK = athDB_N.UploadGUID + "_" + athDB_N.FK_MapData + "_" + wk.getOID();
							athDB_N.Insert();
						}
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 复制表单其他数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 复制独立表单数据.
			//求出来被copy的节点有多少个独立表单.
			FrmNodes fnsFrom = new BP.WF.Template.FrmNodes(fromNd.getNodeID());
			if (fnsFrom.size() != 0)
			{
				//求当前节点表单的绑定的表单.
				FrmNodes fns = new BP.WF.Template.FrmNodes(nd.getNodeID());
				if (fns.size() != 0)
				{
					//开始遍历当前绑定的表单.
					for (FrmNode fn : fns)
					{
						for (FrmNode fnFrom : fnsFrom)
						{
							if (!fn.getFK_Frm().equals(fnFrom.getFK_Frm()))
							{
								continue;
							}

							BP.Sys.GEEntity geEnFrom = new GEEntity(fnFrom.getFK_Frm());
							geEnFrom.OID = PWorkID;
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 复制独立表单数据.

		}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理流程之间的数据传递1。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理单据编号.
		//生成单据编号.
		if (this.getBillNoFormat().length() > 3)
		{
			Object tempVar = this.getBillNoFormat().Clone();
			String billNoFormat = tempVar instanceof String ? (String)tempVar : null;
			String billNo = rpt.getBillNo();
			if (DataType.IsNullOrEmpty(billNo) == true)
			{
				//生成单据编号.
				rpt.setBillNo(BP.WF.WorkFlowBuessRole.GenerBillNo(billNoFormat, rpt.getOID(), rpt, this.getPTable()));
				if (wk.Row.ContainsKey(GERptAttr.BillNo) == true)
				{
					wk.SetValByKey(NDXRptBaseAttr.BillNo, rpt.getBillNo());
				}
				rpt.Update();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理单据编号.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理流程之间的数据传递2, 如果是直接要跳转到指定的节点上去.
		if (paras.containsKey("JumpToNode") == true)
		{
			wk.setRec(WebUser.No);
			wk.SetValByKey(StartWorkAttr.RDT, BP.DA.DataType.CurrentDataTime);
			wk.SetValByKey(StartWorkAttr.CDT, BP.DA.DataType.CurrentDataTime);
			wk.SetValByKey("FK_NY", DataType.CurrentYearMonth);
			wk.setFK_Dept(emp.FK_Dept);
			wk.SetValByKey("FK_DeptName", emp.FK_DeptText);
			wk.SetValByKey("FK_DeptText", emp.FK_DeptText);
			wk.setFID(0);
			wk.SetValByKey(StartWorkAttr.RecText, emp.Name);

			int jumpNodeID = Integer.parseInt(paras.get("JumpToNode").toString());
			Node jumpNode = new Node(jumpNodeID);

			String jumpToEmp = paras.get("JumpToEmp").toString();
			if (DataType.IsNullOrEmpty(jumpToEmp))
			{
				jumpToEmp = emp.No;
			}

			WorkNode wn = new WorkNode(wk, nd);
			wn.NodeSend(jumpNode, jumpToEmp);

			WorkFlow wf = new WorkFlow(this, wk.getOID(), wk.getFID());

			BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(rpt.getOID());
			rpt.setWFState(WFState.Runing);
			rpt.Update();

			return wf.GetCurrentWorkNode().getHisWork();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理流程之间的数据传递。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 最后整理wk数据.
		wk.setRec(emp.No);
		wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.CurrentDataTime);
		wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.CurrentDataTime);
		wk.SetValByKey("FK_NY", DataType.CurrentYearMonth);
		wk.setFK_Dept(emp.FK_Dept);
		wk.SetValByKey("FK_DeptName", emp.FK_DeptText);
		wk.SetValByKey("FK_DeptText", emp.FK_DeptText);

		if (rpt.getEnMap().Attrs.Contains("BillNo") == true)
		{
			wk.SetValByKey(NDXRptBaseAttr.BillNo, rpt.getBillNo());
		}

		wk.setFID(0);
		wk.SetValByKey(StartWorkAttr.RecText, emp.Name);
		if (wk.IsExits == false)
		{
			wk.DirectInsert();
		}
		else
		{
			wk.Update();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 最后整理参数.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 给generworkflow初始化数据. add 2015-08-06
		GenerWorkFlow mygwf = new GenerWorkFlow();
		mygwf.setWorkID(wk.getOID());
		if (mygwf.RetrieveFromDBSources() == 0)
		{
			mygwf.setFK_Flow(this.No);
			mygwf.setFK_FlowSort(this.getFK_FlowSort());
			mygwf.setSysType(this.getSysType());
			mygwf.setFK_Node(nd.getNodeID());
			mygwf.setWorkID(wk.getOID());
			mygwf.setWFState(WFState.Blank);
			mygwf.setFlowName(this.Name);
			mygwf.Insert();
		}
		mygwf.setStarter(WebUser.No);
		mygwf.setStarterName(WebUser.Name);
		mygwf.setFK_Dept(BP.Web.WebUser.FK_Dept);
		mygwf.setDeptName(BP.Web.WebUser.FK_DeptName);
		mygwf.setRDT(BP.DA.DataType.CurrentDataTime);
		mygwf.setTitle(rpt.getTitle());
		mygwf.setBillNo(rpt.getBillNo());
		if (mygwf.getTitle().contains("@") == true)
		{
			mygwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, rpt));
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 给 generworkflow 初始化数据.

		return wk;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 创建新工作.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 初始化一个工作.
	/** 
	 初始化一个工作
	 
	 @param workid
	 @param fk_node
	 @return 
	*/
	public final Work GenerWork(long workid, Node nd, boolean isPostBack)
	{
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		if (wk.RetrieveFromDBSources() == 0)
		{
			/*
			 * 2012-10-15 偶然发现一次工作丢失情况, WF_GenerWorkerlist WF_GenerWorkFlow 都有这笔数据，没有查明丢失原因。 stone.
			 * 用如下代码自动修复，但是会遇到数据copy不完全的问题。
			 * */
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#warning 2011-10-15 偶然发现一次工作丢失情况.

			String fk_mapData = "ND" + Integer.parseInt(this.No) + "Rpt";
			GERpt rpt = new GERpt(fk_mapData);
			rpt.setOID(Integer.parseInt(String.valueOf(workid)));
			if (rpt.RetrieveFromDBSources() >= 1)
			{
				/*  查询到报表数据.  */
				wk.Copy(rpt);
				wk.setRec(WebUser.No);
				wk.InsertAsOID(workid);
			}
			else
			{
				/*  没有查询到报表数据.  */

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#warning 这里不应该出现的异常信息.

				String msg = "@不应该出现的异常.";
				msg += "@在为节点NodeID=" + nd.getNodeID() + " workid:" + workid + " 获取数据时.";
				msg += "@获取它的Rpt表数据时，不应该查询不到。";
				msg += "@GERpt 信息: table:" + rpt.getEnMap().PhysicsTable + "   OID=" + rpt.getOID();

				String sql = "SELECT count(*) FROM " + rpt.getEnMap().PhysicsTable + " WHERE OID=" + workid;
				int num = DBAccess.RunSQLReturnValInt(sql);

				msg += " @SQL:" + sql;
				msg += " ReturnNum:" + num;
				if (num == 0)
				{
					msg += "已经用sql可以查询出来，但是不应该用类查询不出来.";
				}
				else
				{
					/*如果可以用sql 查询出来.*/
					num = rpt.RetrieveFromDBSources();
					msg += "@从rpt.RetrieveFromDBSources = " + num;
				}

				Log.DefaultLogWriteLineError(msg);

				MapData md = new MapData("ND" + Integer.parseInt(nd.getFK_Flow()) + "01");
				sql = "SELECT * FROM " + md.PTable + " WHERE OID=" + workid;
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 1)
				{
					rpt.Copy(dt.Rows[0]);
					try
					{
						rpt.setFlowStarter(dt.Rows[0][StartWorkAttr.Rec].toString());
						rpt.setFlowStartRDT(dt.Rows[0][StartWorkAttr.RDT].toString());
						rpt.setFK_Dept(dt.Rows[0][StartWorkAttr.FK_Dept].toString());
					}
					catch (java.lang.Exception e)
					{
					}

					rpt.setOID(Integer.parseInt(String.valueOf(workid)));
					try
					{
						rpt.InsertAsOID(rpt.getOID());
					}
					catch (RuntimeException ex)
					{
						Log.DefaultLogWriteLineError("@不应该出插入不进去 rpt:" + rpt.getEnMap().PhysicsTable + " workid=" + workid);
						rpt.RetrieveFromDBSources();
					}
				}
				else
				{
					Log.DefaultLogWriteLineError("@没有找到开始节点的数据, NodeID:" + nd.getNodeID() + " workid:" + workid);
					throw new RuntimeException("@没有找到开始节点的数据, NodeID:" + nd.getNodeID() + " workid:" + workid + " SQL:" + sql);
				}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#warning 不应该出现的工作丢失.
				Log.DefaultLogWriteLineError("@工作[" + nd.getNodeID() + " : " + wk.EnDesc + "], 报表数据WorkID=" + workid + " 丢失, 没有从NDxxxRpt里找到记录,请联系管理员。");

				wk.Copy(rpt);
				wk.setRec(WebUser.No);
				wk.ResetDefaultVal();
				wk.Insert();
			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 判断是否有删除草稿的需求.
		if (SystemConfig.IsBSsystem == true && isPostBack == false && nd.getIsStartNode() && HttpContextHelper.RequestParams("IsDeleteDraft").equals("1"))
		{

			/*需要删除草稿.*/
			/*是否要删除Draft */
			String title = wk.GetValStringByKey("Title");
			wk.ResetDefaultValAllAttr();
			wk.setOID(workid);
			wk.SetValByKey(GenerWorkFlowAttr.Title, title);
			wk.DirectUpdate();

			MapDtls dtls = wk.getHisMapDtls();
			for (MapDtl dtl : dtls)
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.PTable + " WHERE RefPK=" + wk.getOID());
			}

			//删除附件数据。
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + wk.getNodeID() + "' AND RefPKVal='" + wk.getOID() + "'");

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion


		// 设置当前的人员把记录人。
		wk.setRec(WebUser.No);
		wk.setRecText(WebUser.Name);
		wk.setRec(WebUser.No);
		wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.CurrentDataTime);
		wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.CurrentDataTime);
		wk.SetValByKey(GERptAttr.WFState, WFState.Runing);
		wk.SetValByKey("FK_Dept", WebUser.FK_Dept);
		wk.SetValByKey("FK_DeptName", WebUser.FK_DeptName);
		wk.SetValByKey("FK_DeptText", WebUser.FK_DeptName);
		wk.setFID(0);
		wk.SetValByKey("RecText", WebUser.Name);

		//处理单据编号.
		if (nd.getIsStartNode())
		{
			try
			{
				String billNo = wk.GetValStringByKey(NDXRptBaseAttr.BillNo);
				if (DataType.IsNullOrEmpty(billNo) && nd.getHisFlow().getBillNoFormat().length() > 2)
				{
					/*让他自动生成编号*/
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 初始化一个工作

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他通用方法.
	public final String DoBTableDTS()
	{
		if (this.getDTSWay() == FlowDTSWay.None)
		{
			return "执行失败，您没有设置同步方式。";
		}

		String info = "";
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.FK_Flow, this.No);
		for (GenerWorkFlow gwf : gwfs)
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
	*/
	public final String DoBTableDTS(GERpt rpt, Node currNode, boolean isStopFlow)
	{
		boolean isActiveSave = false;
		// 判断是否符合流程数据同步条件.
		switch (this.getDTSTime())
		{
			case AllNodeSend:
				isActiveSave = true;
				break;
			case SpecNodeSend:
				if (this.getDTSSpecNodes().contains(String.valueOf(currNode.getNodeID())) == true)
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
			switch (src.DBSrcType)
			{
				case Sys.DBSrcType.Localhost:
					switch (SystemConfig.AppCenterDBType)
					{
						case DBType.MSSQL:
							break;
						case DBType.Oracle:
							if (ywDt.Columns[ywArr[i]].DataType == LocalDateTime.class)
							{
								if (!DataType.IsNullOrEmpty(lcDt.Rows[0][lcArr[i].toString()].toString()))
								{
									values += "to_date('" + lcDt.Rows[0][lcArr[i].toString()] + "','YYYY-MM-DD'),";
								}
								else
								{
									values += "'',";
								}
								continue;
							}
							values += "'" + lcDt.Rows[0][lcArr[i].toString()] + "',";
							continue;
							break;
						case DBType.MySQL:
							break;
						case DBType.Informix:
							break;
						default:
							throw new RuntimeException("没有涉及到的连接测试类型...");
					}
					break;
				case Sys.DBSrcType.SQLServer:
					break;
				case Sys.DBSrcType.MySQL:
					break;
				case Sys.DBSrcType.Oracle:
					if (ywDt.Columns[ywArr[i]].DataType == LocalDateTime.class)
					{
						if (!DataType.IsNullOrEmpty(lcDt.Rows[0][lcArr[i].toString()].toString()))
						{
							values += "to_date('" + lcDt.Rows[0][lcArr[i].toString()] + "','YYYY-MM-DD'),";
						}
						else
						{
							values += "'',";
						}
						continue;
					}
					values += "'" + lcDt.Rows[0][lcArr[i].toString()] + "',";
					continue;
				default:
					throw new RuntimeException("暂时不支您所使用的数据库类型!");
			}
			values += "'" + lcDt.Rows[0][lcArr[i].toString()] + "',";
			//获取除主键之外的其他值
			if (i > 0)
			{
				upVal = upVal + ywArr[i] + "='" + lcDt.Rows[0][lcArr[i].toString()] + "',";
			}
		}

		values = values.substring(0, values.length() - 1);
		upVal = upVal.substring(0, upVal.length() - 1);

		//查询对应的业务表中是否存在这条记录
		sql = "SELECT * FROM " + this.getDTSBTable().toUpperCase() + " WHERE " + getDTSBTablePK() + "='" + lcDt.Rows[0][getDTSBTablePK()] + "'";
		DataTable dt = src.RunSQLReturnTable(sql);
		//如果存在，执行更新，如果不存在，执行插入
		if (dt.Rows.size() > 0)
		{

			sql = "UPDATE " + this.getDTSBTable().toUpperCase() + " SET " + upVal + " WHERE " + getDTSBTablePK() + "='" + lcDt.Rows[0][getDTSBTablePK()] + "'";
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion qinfaliang, 编写同步的业务逻辑,执行错误就抛出异常.

		return "同步成功.";
	}
	/** 
	 自动发起
	 
	 @return 
	*/
	public final String DoAutoStartIt()
	{
		switch (this.getHisFlowRunWay())
		{
			case SpecEmp: //指定人员按时运行。
				String RunObj = this.getRunObj();
				String FK_Emp = RunObj.substring(0, RunObj.indexOf('@'));
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.No = FK_Emp;
				if (emp.RetrieveFromDBSources() == 0)
				{
					return "启动自动启动流程错误：发起人(" + FK_Emp + ")不存在。";
				}

				BP.Web.WebUser.SignInOfGener(emp);
				String info_send = BP.WF.Dev2Interface.Node_StartWork(this.No, null, null, 0, null, 0, null).ToMsgOfText();
				if (!WebUser.No.equals("admin"))
				{
					emp = new BP.Port.Emp();
					emp.No = "admin";
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
		me.MyPK = "ND" + Integer.parseInt(this.No) + "01_" + MapExtXmlList.StartFlow;
		int i = me.RetrieveFromDBSources();
		if (i == 0)
		{
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + this.Name + ")的开始节点设置发起数据,请参考说明书解决.");
			return "没有为流程(" + this.Name + ")的开始节点设置发起数据,请参考说明书解决.";
		}
		if (DataType.IsNullOrEmpty(me.Tag))
		{
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + this.Name + ")的开始节点设置发起数据,请参考说明书解决.");
			return "没有为流程(" + this.Name + ")的开始节点设置发起数据,请参考说明书解决.";
		}

		// 获取从表数据.
		DataSet ds = new DataSet();
		String[] dtlSQLs = me.Tag1.split("[*]", -1);
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
			ds.Tables.Add(dtlTable);
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查数据源是否正确.
		String errMsg = "";
		// 获取主表数据.
		DataTable dtMain = DBAccess.RunSQLReturnTable(me.Tag);
		if (dtMain.Rows.size() == 0)
		{
			return "流程(" + this.Name + ")此时无任务,查询语句:" + me.Tag.Replace("'", "”");
		}

		msg += "@查询到(" + dtMain.Rows.size() + ")条任务.";

		if (dtMain.Columns.Contains("Starter") == false)
		{
			errMsg += "@配值的主表中没有Starter列.";
		}

		if (dtMain.Columns.Contains("MainPK") == false)
		{
			errMsg += "@配值的主表中没有MainPK列.";
		}

		if (errMsg.length() > 2)
		{
			return "流程(" + this.Name + ")的开始节点设置发起数据,不完整." + errMsg;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 检查数据源是否正确.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理流程发起.

		String fk_mapdata = "ND" + Integer.parseInt(this.No) + "01";

		MapData md = new MapData(fk_mapdata);
		int idx = 0;
		for (DataRow dr : dtMain.Rows)
		{
			idx++;

			String mainPK = dr.get("MainPK").toString();
			String sql = "SELECT OID FROM " + md.PTable + " WHERE MainPK='" + mainPK + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0)
			{
				msg += "@" + this.Name + ",第" + idx + "条,此任务在之前已经完成。";
				continue; //说明已经调度过了
			}

			String starter = dr.get("Starter").toString();
			if (!starter.equals(WebUser.No))
			{
				BP.Web.WebUser.Exit();
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.No = starter;
				if (emp.RetrieveFromDBSources() == 0)
				{
					msg += "@" + this.Name + ",第" + idx + "条,设置的发起人员:" + emp.No + "不存在.";
					msg += "@数据驱动方式发起流程(" + this.Name + ")设置的发起人员:" + emp.No + "不存在。";
					continue;
				}
				WebUser.SignInOfGener(emp);
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region  给值.
			Work wk = this.NewWork();
			for (DataColumn dc : dtMain.Columns)
			{
				wk.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
			}

			if (ds.Tables.size() != 0)
			{
				// MapData md = new MapData(nodeTable);
				MapDtls dtls = md.MapDtls; // new MapDtls(nodeTable);
				for (MapDtl dtl : dtls)
				{
					for (DataTable dt : ds.Tables)
					{
						if (!dt.TableName.equals(dtl.No))
						{
							continue;
						}

						//删除原来的数据。
						GEDtl dtlEn = dtl.HisGEDtl;
						dtlEn.Delete(GEDtlAttr.RefPK, String.valueOf(wk.getOID()));

						// 执行数据插入。
						for (DataRow drDtl : dt.Rows)
						{
							if (!drDtl.get("RefMainPK").toString().equals(mainPK))
							{
								continue;
							}

							dtlEn = dtl.HisGEDtl;
							for (DataColumn dc : dt.Columns)
							{
								dtlEn.SetValByKey(dc.ColumnName, drDtl.get(dc.ColumnName).toString());
							}

							dtlEn.RefPK = String.valueOf(wk.getOID());
							dtlEn.OID = 0;
							dtlEn.Insert();
						}
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  给值.

			// 处理发送信息.
			Node nd = this.getHisStartNode();
			try
			{
				WorkNode wn = new WorkNode(wk, nd);
				String infoSend = wn.NodeSend().ToMsgOfHtml();
				BP.DA.Log.DefaultLogWriteLineInfo(msg);
				msg += "@" + this.Name + ",第" + idx + "条,发起人员:" + WebUser.No + "-" + WebUser.Name + "已完成.\r\n" + infoSend;
				//this.SetText("@第（" + idx + "）条任务，" + WebUser.No + " - " + WebUser.Name + "已经完成。\r\n" + msg);
			}
			catch (RuntimeException ex)
			{
				msg += "@" + this.Name + ",第" + idx + "条,发起人员:" + WebUser.No + "-" + WebUser.Name + "发起时出现错误.\r\n" + ex.getMessage();
			}
			msg += "<hr>";
		}
		return msg;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理流程发起.
	}

	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.No.equals("admin"))
		{
			uac.IsUpdate = true;
		}
		return uac;
	}

	public final String ClearCash()
	{
		BP.DA.Cash.ClearCash();
		//清空流程中的缓存
		//获取改流程中的节点数据
		Nodes nds = new Nodes(this.No);
		for (Node nd : nds)
		{
			//判断表单的类型
			if (nd.getHisFormType() == NodeFormType.FoolForm || nd.getHisFormType() == NodeFormType.FreeForm)
			{
				BP.Sys.CCFormAPI.AfterFrmEditAction("ND" + nd.getNodeID());
			}
		}

		return "清除成功.";
	}


	/** 
	 执行检查2018
	 
	 @return 
	*/
	public final String DoCheck2018()
	{
		FlowCheckError check = new FlowCheckError(this);
		check.DoCheck();

		return BP.Tools.Json.ToJson(check.dt);
		//return "../../Admin/Testing/FlowCheckError.htm?FK_Flow=" + this.No + "&Lang=CH";
	}
	/** 
	 校验流程
	 
	 @return 
	*/
	public final String DoCheck()
	{

		BP.DA.Cash.ClearCash();

		Nodes nds = new Nodes(this.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查独立表单
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.No);
		String frms = "";
		String err = "";
		for (FrmNode item : fns)
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
				Entity en = md.HisEn;
				en.CheckPhysicsTable();
			}
			catch (RuntimeException ex)
			{
				err += "@节点绑定的表单:" + item.getFK_Frm() + ",已经被删除了.异常信息." + ex.getMessage();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查消息推送。
		PushMsgs pus = new PushMsgs();
		pus.Retrieve(PushMsgAttr.FK_Flow, this.No);
		for (Node nd : nds)
		{
			//创建默认信息.
			CreatePushMsg(nd);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 检查消息推送。

		try
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 对流程的设置做必要的检查.
			// 设置流程名称.
			DBAccess.RunSQL("UPDATE WF_Node SET FlowName = (SELECT Name FROM WF_Flow WHERE NO=WF_Node.FK_Flow)");

			//设置单据编号只读格式.
			DBAccess.RunSQL("UPDATE Sys_MapAttr SET UIIsEnable=0 WHERE KeyOfEn='BillNo' AND UIIsEnable=1");

			//开始节点不能有会签.
			DBAccess.RunSQL("UPDATE WF_Node SET HuiQianRole=0 WHERE NodePosType=0 AND HuiQianRole !=0");

			//开始节点不能有退回.
			DBAccess.RunSQL("UPDATE WF_Node SET ReturnRole=0 WHERE NodePosType=0 AND ReturnRole !=0");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 对流程的设置做必要的检查.

			//删除垃圾,非法数据.
			String sqls = "DELETE FROM Sys_FrmSln WHERE FK_MapData not in (select No from Sys_MapData)";
			sqls += "@ DELETE FROM WF_Direction WHERE Node=ToNode";
			DBAccess.RunSQLs(sqls);

			//更新计算数据.
			this.setNumOfBill(DBAccess.RunSQLReturnValInt("SELECT count(*) FROM WF_BillTemplate WHERE NodeID IN (SELECT NodeID FROM WF_Flow WHERE No='" + this.No + "')"));
			this.setNumOfDtl(DBAccess.RunSQLReturnValInt("SELECT count(*) FROM Sys_MapDtl WHERE FK_MapData='ND" + Integer.parseInt(this.No) + "Rpt'"));
			this.DirectUpdate();

			String msg = "@  =======  关于《" + this.Name + " 》流程检查报告  ============";
			msg += "@信息输出分为三种: 信息  警告  错误. 如果遇到输出的错误，则必须要去修改或者设置.";
			msg += "@流程检查目前还不能覆盖100%的错误,需要手工的运行一次才能确保流程设计的正确性.";


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查是否是数据合并模式?
			if (this.getHisDataStoreModel() == Template.DataStoreModel.SpecTable)
			{
				for (Node nd : nds)
				{
					MapData md = new MapData();
					md.No = "ND" + nd.getNodeID();
					if (md.RetrieveFromDBSources() == 1)
					{
						if (!this.getPTable().equals(md.PTable))
						{
							md.PTable = this.getPTable();
							md.Update();
						}
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 检查是否是数据合并模式?

			//单据模版.
			BillTemplates bks = new BillTemplates(this.No);

			//条件集合.
			Conds conds = new Conds(this.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 对节点进行检查
			//节点表单字段数据类型检查--begin---------
			msg += CheckFormFields();
			//表单字段数据类型检查-------End-----

			for (Node nd : nds)
			{
				//设置它的位置类型.
				nd.RetrieveFromDBSources();
				nd.SetValByKey(NodeAttr.NodePosType, nd.GetHisNodePosType().getValue());

				msg += "@信息: -------- 开始检查节点ID:(" + nd.getNodeID() + ")名称:(" + nd.getName() + ")信息 -------------";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 修复数节点表单数据库.
				msg += "@信息:开始补充&修复节点必要的字段";
				try
				{
					nd.RepareMap(this);
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@修复节点表必要字段时出现错误:" + nd.getName() + " - " + ex.getMessage());
				}

				msg += "@信息:开始修复节点物理表.";
				try
				{
					nd.getHisWork().CheckPhysicsTable();
				}
				catch (RuntimeException ex)
				{
					msg += "@检查节点表字段时出现错误:" + "NodeID" + nd.getNodeID() + " Table:" + nd.getHisWork().EnMap.PhysicsTable + " Name:" + nd.getName() + " , 节点类型NodeWorkTypeText=" + nd.getNodeWorkTypeText() + "出现错误.@err=" + ex.getMessage();
				}

				// 从表检查。
				Sys.MapDtls dtls = new BP.Sys.MapDtls("ND" + nd.getNodeID());
				for (Sys.MapDtl dtl : dtls)
				{
					msg += "@检查明细表:" + dtl.Name;
					try
					{
						dtl.HisGEDtl.CheckPhysicsTable();
					}
					catch (RuntimeException ex)
					{
						msg += "@检查明细表时间出现错误" + ex.getMessage();
					}
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 修复数节点表单数据库.

				MapAttrs mattrs = new MapAttrs("ND" + nd.getNodeID());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 对节点的访问规则进行检查

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

						mysql = "SELECT pdes.FK_Emp AS No"
								+ " FROM   Port_DeptEmpStation pdes"
								+ "        INNER JOIN WF_NodeDept wnd"
								+ "             ON  wnd.FK_Dept = pdes.FK_Dept"
								+ "             AND wnd.FK_Node = " + nd.getNodeID() + "        INNER JOIN WF_NodeStation wns"
								+ "             ON  wns.FK_Station = pdes.FK_Station"
								+ "             AND wnd.FK_Node =" + nd.getNodeID() + " ORDER BY"
								+ "        pdes.FK_Emp";


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
								for (MapAttr item : mattrs)
								{
									sql = sql.replace("@" + item.KeyOfEn, "0");
								}

								sql = sql.replace("@WebUser.No", "ss");
								sql = sql.replace("@WebUser.Name", "ss");
								sql = sql.replace("@WebUser.FK_DeptName", "ss");
								sql = sql.replace("@WebUser.FK_Dept", "ss");


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

								if (testDB.Columns.Contains("no") == false || testDB.Columns.Contains("name") == false)
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
						MapAttrs rptAttrs = new BP.Sys.MapAttrs();
						rptAttrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + str1 + "Rpt", MapAttrAttr.KeyOfEn);

						if (rptAttrs.Contains(BP.Sys.MapAttrAttr.KeyOfEn, nd.getDeliveryParas()) == false)
						{
							/*检查节点字段是否有FK_Emp字段*/
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 检查节点完成条件，方向条件的定义.
				//设置它没有流程完成条件.
				nd.setIsCCFlow(false);

				if (conds.size() != 0)
				{
					msg += "@信息:开始检查(" + nd.getName() + ")方向条件:";
					for (Cond cond : conds)
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
							if (ndOfCond.getHisWork().EnMap.Attrs.Contains(cond.getAttrKey()) == false)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 检查节点完成条件的定义.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 如果是引用的表单库的表单，就要检查该表单是否有FID字段，没有就自动增加.
				if (nd.getHisFormType() == NodeFormType.RefOneFrmTree)
				{
					MapAttr mattr = new MapAttr();
					mattr.MyPK = nd.getNodeFrmID() + "_FID";
					if (mattr.RetrieveFromDBSources() == 0)
					{
						mattr.KeyOfEn = "FID";
						mattr.FK_MapData = nd.getNodeFrmID();
						mattr.MyDataType = DataType.AppInt;
						mattr.UIVisible = false;
						mattr.Name = "FID(自动增加)";
						mattr.Insert();

						GEEntity en = new GEEntity(nd.getNodeFrmID());
						en.CheckPhysicsTable();
					}
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 如果是引用的表单库的表单，就要检查该表单是否有FID字段，没有就自动增加.

				//@李国文. 如果是子线城，子线程的表单必须是轨迹模式。
				if (nd.getHisRunModel() == RunModel.SubThread)
				{
					MapData md = new MapData("ND" + nd.getNodeID());
					if (!md.PTable.equals("ND" + nd.getNodeID()))
					{
						md.PTable = "ND" + nd.getNodeID();
						md.Update();
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 执行一次保存.
			NodeExts nes = new NodeExts();
			nes.Retrieve(NodeAttr.FK_Flow, this.No);
			for (NodeExt item : nes)
			{
				item.Update(); // 调用里面的业务逻辑执行检查.
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查越轨流程,子流程发起。
			SubFlowYanXus ygflows = new SubFlowYanXus();
			ygflows.Retrieve(SubFlowYanXuAttr.SubFlowNo, this.No, SubFlowYanXuAttr.SubFlowType, SubFlowType.YanXuFlow.getValue());
			for (SubFlowYanXu flow : ygflows)
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
						Node toNode = nd.getHisToNodes()[0] instanceof Node ? (Node)nd.getHisToNodes()[0] : null;
						if (nd.getHisDeliveryWay() == DeliveryWay.BySelected)
						{
							msg += "@当前节点[" + nd.getName() + "]的可以启动子流程或者延续流程.被启动的子流程设置了当子流程结束时让父流程自动运行到下一个节点，但是当前节点有分支，导致流程无法运行到下一个节点.";
						}
					}

				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 检查越轨流程,子流程发起。


			msg += "@流程的基础信息: ------ ";
			msg += "@编号:  " + this.No + " 名称:" + this.Name + " , 存储表:" + this.getPTable();

			msg += "@信息:开始检查节点流程报表.";
			this.DoCheck_CheckRpt(this.getHisNodes());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查焦点字段设置是否还有效
			msg += "@信息:开始检查节点的焦点字段";

			//获得gerpt字段.
			GERpt rpt = this.getHisGERpt();

			for (Attr attr : rpt.getEnMap().Attrs)
			{
				rpt.SetValByKey(attr.Key, "0");
			}

			for (Node nd : nds)
			{
				if (nd.getFocusField().trim().equals(""))
				{
					Work wk = nd.getHisWork();
					String attrKey = "";
					for (Attr attr : wk.EnMap.Attrs)
					{
						if (attr.UIVisible == true && attr.UIIsDoc && attr.UIIsReadonly == false)
						{
							attrKey = attr.Desc + ":@" + attr.Key;
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

				Object tempVar = nd.getFocusField().Clone();
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
					if (nd.getHisWork().EnMap.Attrs.Contains(WorkAttr.MD5) == false)
					{
						nd.RepareMap(this);
					}
				}
			}
			msg += "@信息:检查节点的焦点字段完成.";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查质量考核点.
			msg += "@信息:开始检查质量考核点";
			for (Node nd : nds)
			{
				if (nd.getIsEval())
				{
					/*如果是质量考核点，检查节点表单是否具别质量考核的特别字段？*/
					String sql = "SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='ND" + nd.getNodeID() + "' AND KeyOfEn IN ('EvalEmpNo','EvalEmpName','EvalEmpCent')";
					if (DBAccess.RunSQLReturnValInt(sql) != 3)
					{
						msg += "@信息:您设置了节点(" + nd.getNodeID() + "," + nd.getName() + ")为质量考核节点，但是您没有在该节点表单中设置必要的节点考核字段.";
					}
				}
			}
			msg += "@检查质量考核点完成.";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查如果是合流节点必须不能是由上一个节点指定接受人员。 @dudongliang 需要翻译.
			for (Node nd : nds)
			{
				//如果是合流节点.
				if (nd.getHisNodeWorkType() == NodeWorkType.WorkHL || nd.getHisNodeWorkType() == NodeWorkType.WorkFHL)
				{
					if (nd.getHisDeliveryWay() == DeliveryWay.BySelected)
					{
						msg += "@错误:节点ID:" + nd.getNodeID() + " 名称:" + nd.getName() + "是合流或者分合流节点，但是该节点设置的接收人规则为由上一步指定，这是错误的，应该为自动计算而非每个子线程人为的选择.";
					}
				}

				//子线程节点
				if (nd.getHisNodeWorkType() == NodeWorkType.SubThreadWork)
				{
					if (nd.getCondModel() == CondModel.ByUserSelected)
					{
						Nodes toNodes = nd.getHisToNodes();
						if (toNodes.size() == 1)
						{
							//msg += "@错误:节点ID:" + nd.NodeID + " 名称:" + nd.Name + " 错误当前节点为子线程，但是该节点的到达.";
						}
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 检查如果是合流节点必须不能是由上一个节点指定接受人员。


			msg += "@流程报表检查完成...";

			// 检查流程.
			Node.CheckFlow(this);

			//一直没有找到设置3列，自动回到四列的情况.
			DBAccess.RunSQL("UPDATE Sys_MapAttr SET ColSpan=3 WHERE  UIHeight<=23 AND ColSpan=4");


			//创建track.
			Track.CreateOrRepairTrackTable(this.No);

			//生成 V001 视图. del by stone 2016.03.27.
			// CheckRptViewDel(nds);
			return msg;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@检查流程错误:" + ex.getMessage() + " @" + ex.StackTrace);
		}
	}
	/** 
	 节点表单字段数据类型检查，名字相同的字段出现类型不同的处理方法：依照不同于NDxxRpt表中同名字段类型为基准
	 
	 @return 检查结果
	*/
	private String CheckFormFields()
	{
		StringBuilder errorAppend = new StringBuilder();
		errorAppend.append("@信息: -------- 流程节点表单的字段类型检查: ------ ");
		try
		{
			Nodes nds = new Nodes(this.No);
			String fk_mapdatas = "'ND" + Integer.parseInt(this.No) + "Rpt'";
			for (Node nd : nds)
			{
				fk_mapdatas += ",'ND" + nd.getNodeID() + "'";
			}

			//筛选出类型不同的字段.
			String checkSQL = "SELECT   AA.KEYOFEN, COUNT(*) AS MYNUM FROM ("
								+ "  SELECT A.KEYOFEN,  MYDATATYPE,  COUNT(*) AS MYNUM "
								+ "  FROM SYS_MAPATTR A WHERE FK_MAPDATA IN (" + fk_mapdatas + ") GROUP BY KEYOFEN, MYDATATYPE"
								+ ")  AA GROUP BY  AA.KEYOFEN HAVING COUNT(*) > 1";
			DataTable dt_Fields = DBAccess.RunSQLReturnTable(checkSQL);
			for (DataRow row : dt_Fields.Rows)
			{
				String keyOfEn = row.get("KEYOFEN").toString();
				String myNum = row.get("MYNUM").toString();
				int iMyNum = 0;
				tangible.OutObject<Integer> tempOut_iMyNum = new tangible.OutObject<Integer>();
				tangible.TryParseHelper.tryParseInt(myNum, tempOut_iMyNum);
			iMyNum = tempOut_iMyNum.argValue;

				//存在2种以上数据类型，有手动进行调整
				if (iMyNum > 2)
				{
					errorAppend.append("@错误：字段名" + keyOfEn + "在此流程表(" + fk_mapdatas + ")中存在2种以上数据类型(如：int，float,varchar,datetime)，请手动修改。");
					return errorAppend.toString();
				}

				//存在2种数据类型，以不同于NDxxRpt字段类型为主
				MapAttr baseMapAttr = new MapAttr();
				MapAttr rptMapAttr = new MapAttr("ND" + Integer.parseInt(this.No) + "Rpt", keyOfEn);

				//Rpt表中不存在此字段
				if (rptMapAttr == null || rptMapAttr.MyPK.equals(""))
				{
					this.DoCheck_CheckRpt(this.getHisNodes());
					rptMapAttr = new MapAttr("ND" + Integer.parseInt(this.No) + "Rpt", keyOfEn);
					this.getHisGERpt().CheckPhysicsTable();
				}

				//Rpt表中不存在此字段,直接结束
				if (rptMapAttr == null || rptMapAttr.MyPK.equals(""))
				{
					continue;
				}

				for (Node nd : nds)
				{
					MapAttr ndMapAttr = new MapAttr("ND" + nd.getNodeID(), keyOfEn);
					if (ndMapAttr == null || ndMapAttr.MyPK.equals(""))
					{
						continue;
					}

					//找出与NDxxRpt表中字段数据类型不同的表单
					if (rptMapAttr.MyDataType != ndMapAttr.MyDataType)
					{
						baseMapAttr = ndMapAttr;
						break;
					}
				}
				errorAppend.append("@基础表" + baseMapAttr.FK_MapData + "，字段" + keyOfEn + "数据类型为：" + baseMapAttr.MyDataTypeStr);
				//根据基础属性类修改数据类型不同的表单
				for (Node nd : nds)
				{
					MapAttr ndMapAttr = new MapAttr("ND" + nd.getNodeID(), keyOfEn);
					//不包含此字段的进行返回,类型相同的进行返回
					if (ndMapAttr == null || ndMapAttr.MyPK.equals("") || baseMapAttr.MyPK == ndMapAttr.MyPK || baseMapAttr.MyDataType == ndMapAttr.MyDataType)
					{
						continue;
					}

					ndMapAttr.Name = baseMapAttr.Name;
					ndMapAttr.MyDataType = baseMapAttr.MyDataType;
					ndMapAttr.UIWidth = baseMapAttr.UIWidth;
					ndMapAttr.UIHeight = baseMapAttr.UIHeight;
					ndMapAttr.MinLen = baseMapAttr.MinLen;
					ndMapAttr.MaxLen = baseMapAttr.MaxLen;
					if (ndMapAttr.Update() > 0)
					{
						errorAppend.append("@修改了" + "ND" + nd.getNodeID() + " 表，字段" + keyOfEn + "修改为：" + baseMapAttr.MyDataTypeStr);
					}
					else
					{
						errorAppend.append("@错误:修改" + "ND" + nd.getNodeID() + " 表，字段" + keyOfEn + "修改为：" + baseMapAttr.MyDataTypeStr + "失败。");
					}
				}
				//修改NDxxRpt
				rptMapAttr.Name = baseMapAttr.Name;
				rptMapAttr.MyDataType = baseMapAttr.MyDataType;
				rptMapAttr.UIWidth = baseMapAttr.UIWidth;
				rptMapAttr.UIHeight = baseMapAttr.UIHeight;
				rptMapAttr.MinLen = baseMapAttr.MinLen;
				rptMapAttr.MaxLen = baseMapAttr.MaxLen;
				if (rptMapAttr.Update() > 0)
				{
					errorAppend.append("@修改了" + "ND" + Integer.parseInt(this.No) + "Rpt 表，字段" + keyOfEn + "修改为：" + baseMapAttr.MyDataTypeStr);
				}
				else
				{
					errorAppend.append("@错误:修改" + "ND" + Integer.parseInt(this.No) + "Rpt 表，字段" + keyOfEn + "修改为：" + baseMapAttr.MyDataTypeStr + "失败。");
				}
			}
		}
		catch (RuntimeException ex)
		{
			errorAppend.append("@错误:" + ex.getMessage());
		}
		return errorAppend.toString();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 其他方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 检查流程.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 检查流程.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 产生数据模板。
	private static String PathFlowDesc;
	static
	{
		PathFlowDesc = SystemConfig.PathOfDataUser + "FlowDesc\\";
	}
	/** 
	 生成流程模板
	 
	 @return 
	*/
	public final String GenerFlowXmlTemplete()
	{
		String name = this.Name;
		name = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(name);

		String path = this.No + "." + name;
		path = PathFlowDesc + path + "\\";

		this.DoExpFlowXmlTemplete(path);

		// name = path + name + "." + this.Ver.Replace(":", "_") + ".xml";

		name = path + name + ".xml";
		return name;
	}
	/** 
	 生成流程模板
	 
	 @param path
	 @return 
	*/
	public final DataSet DoExpFlowXmlTemplete(String path)
	{
		if ((new File(path)).isDirectory() == false)
		{
			(new File(path)).mkdirs();
		}

		DataSet ds = GetFlow(path);
		if (ds != null)
		{
			String name = this.Name;
			name = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(name);
			// name = name + "." + this.Ver.Replace(":", "_") + ".xml";
			name = name + ".xml";
			String filePath = path + name;
			ds.WriteXml(filePath);
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
	public final void WriteToXml()
	{
		try
		{
			String name = this.No + "." + this.Name;
			name = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(name);
			String path = PathFlowDesc + name + "\\";
			DataSet ds = GetFlow(path);
			if (ds == null)
			{
				return;
			}

			String directory = this.No + "." + this.Name;
			directory = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(directory);
			path = PathFlowDesc + directory + "\\";
			String xmlName = path + "Flow" + ".xml";

			if (!isXmlLocked)
			{
				if (!DataType.IsNullOrEmpty(path))
				{
					if (!(new File(path)).isDirectory())
					{
						(new File(path)).mkdirs();
					}
					else if ((new File(xmlName)).isFile())
					{
						LocalDateTime time = File.GetLastWriteTime(xmlName);
						String xmlNameOld = path + "Flow" + time.toString("@yyyyMMddHHmmss") + ".xml";

						isXmlLocked = true;
						if ((new File(xmlNameOld)).isFile())
						{
							(new File(xmlNameOld)).delete();
						}
						Files.move(Paths.get(xmlName), Paths.get(xmlNameOld));
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
			BP.DA.Log.DefaultLogWriteLineError("流程模板文件备份错误:" + e.getMessage());
		}
	}
	public final DataSet GetFlow(String path)
	{
		// 把所有的数据都存储在这里。
		DataSet ds = new DataSet();

		// 流程信息。
		String sql = "SELECT * FROM WF_Flow WHERE No='" + this.No + "'";

		Flow fl = new Flow(this.No);
		DataTable dtFlow = fl.ToDataTableField("WF_Flow");
		dtFlow.TableName = "WF_Flow";
		ds.Tables.Add(dtFlow);

		// 节点信息
		Nodes nds = new Nodes(this.No);
		DataTable dtNodes = nds.ToDataTableField("WF_Node");
		ds.Tables.Add(dtNodes);

		//节点属性
		NodeExts ndexts = new NodeExts(this.No);
		DataTable dtNodeExts = ndexts.ToDataTableField("WF_NodeExt");
		ds.Tables.Add(dtNodeExts);

		//接收人规则
		Selectors selectors = new Selectors(this.No);
		DataTable dtSelectors = selectors.ToDataTableField("WF_Selector");
		ds.Tables.Add(dtSelectors);

		// 单据模版. 
		BillTemplates tmps = new BillTemplates(this.No);
		String pks = "";
		for (BillTemplate tmp : tmps)
		{
			try
			{
				if (path != null)
				{
					Files.copy(Paths.get(SystemConfig.PathOfDataUser + "\\CyclostyleFile\\" + tmp.getNo() + ".rtf"), Paths.get(path + "\\" + tmp.getNo() + ".rtf"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			catch (java.lang.Exception e)
			{
				pks += "@" + tmp.PKVal;
				tmp.Delete();
			}
		}
		tmps.Remove(pks);
		ds.Tables.Add(tmps.ToDataTableField("WF_BillTemplate"));


		String sqlin = "SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.No + "'";

		// 条件信息
		Conds cds = new BP.WF.Template.Conds(this.No);
		ds.Tables.Add(cds.ToDataTableField("WF_Cond"));

		// 节点与表单绑定.
		FrmNodes fns = new BP.WF.Template.FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.No);
		ds.Tables.Add(fns.ToDataTableField("WF_FrmNode"));


		// 表单方案.
		FrmFields ffs = new BP.WF.Template.FrmFields();
		ffs.Retrieve(FrmFieldAttr.FK_Flow, this.No);
		ds.Tables.Add(ffs.ToDataTableField("Sys_FrmSln"));

		// 方向
		Directions dirs = new Directions();
		dirs.Retrieve(DirectionAttr.FK_Flow, this.No);
		ds.Tables.Add(dirs.ToDataTableField("WF_Direction"));

		// 流程标签.
		LabNotes labs = new LabNotes(this.No);
		ds.Tables.Add(labs.ToDataTableField("WF_LabNote"));

		// 可退回的节点。
		NodeReturns nrs = new NodeReturns();
		nrs.RetrieveInSQL(NodeReturnAttr.FK_Node, sqlin);
		ds.Tables.Add(nrs.ToDataTableField("WF_NodeReturn"));


		// 工具栏。
		NodeToolbars tools = new NodeToolbars();
		tools.RetrieveInSQL(NodeToolbarAttr.FK_Node, sqlin);
		ds.Tables.Add(tools.ToDataTableField("WF_NodeToolbar"));


		// 节点与部门。
		NodeDepts ndepts = new NodeDepts();
		ndepts.RetrieveInSQL(NodeDeptAttr.FK_Node, sqlin);
		ds.Tables.Add(ndepts.ToDataTableField("WF_NodeDept"));


		// 节点与岗位权限。
		NodeStations nss = new NodeStations();
		nss.RetrieveInSQL(NodeStationAttr.FK_Node, sqlin);
		ds.Tables.Add(nss.ToDataTableField("WF_NodeStation"));

		// 节点与人员。
		NodeEmps nes = new NodeEmps();
		nes.RetrieveInSQL(NodeEmpAttr.FK_Node, sqlin);
		ds.Tables.Add(nes.ToDataTableField("WF_NodeEmp"));

		// 抄送人员。
		CCEmps ces = new CCEmps();
		ces.RetrieveInSQL(CCEmpAttr.FK_Node, sqlin);
		ds.Tables.Add(ces.ToDataTableField("WF_CCEmp"));

		// 抄送部门。
		CCDepts cdds = new CCDepts();
		cdds.RetrieveInSQL(CCDeptAttr.FK_Node, sqlin);
		ds.Tables.Add(cdds.ToDataTableField("WF_CCDept"));

		//子流程。
		SubFlows fls = new SubFlows();
		fls.RetrieveInSQL(CCDeptAttr.FK_Node, sqlin);
		ds.Tables.Add(fls.ToDataTableField("WF_NodeSubFlow"));

		//表单信息，包含从表.
		sql = "SELECT No FROM Sys_MapData WHERE " + Glo.MapDataLikeKey(this.No, "No");
		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL(MapDataAttr.No, sql);
		ds.Tables.Add(mds.ToDataTableField("Sys_MapData"));

		// Sys_MapAttr.
		sql = "SELECT MyPK FROM Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		sql += " UNION "; //增加多附件的扩展列.
		sql += "SELECT MyPK FROM Sys_MapAttr WHERE FK_MapData IN ( SELECT MyPK FROM Sys_FrmAttachment WHERE FK_Node=0 AND " + Glo.MapDataLikeKey(this.No, "FK_MapData") + " ) ";

		MapAttrs attrs = new MapAttrs();
		attrs.RetrieveInSQL(MapAttrAttr.MyPK, sql);
		ds.Tables.Add(attrs.ToDataTableField("Sys_MapAttr"));


		// Sys_EnumMain
		sql = "SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData") + ")";
		SysEnumMains ses = new SysEnumMains();
		ses.RetrieveInSQL(SysEnumMainAttr.No, sql);
		ds.Tables.Add(ses.ToDataTableField("Sys_EnumMain"));


		// Sys_Enum
		sql = "SELECT MyPK FROM Sys_Enum WHERE EnumKey IN ( SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData") + " ) )";
		SysEnums sesDtl = new SysEnums();
		sesDtl.RetrieveInSQL("MyPK", sql);
		ds.Tables.Add(sesDtl.ToDataTableField("Sys_Enum"));


		// Sys_MapDtl
		sql = "SELECT No FROM Sys_MapDtl WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		MapDtls mdtls = new MapDtls();
		mdtls.RetrieveInSQL(sql);
		ds.Tables.Add(mdtls.ToDataTableField("Sys_MapDtl"));


		// Sys_MapExt
		sql = "SELECT MyPK FROM Sys_MapExt WHERE  " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		MapExts mexts = new MapExts();
		mexts.RetrieveInSQL(sql);
		ds.Tables.Add(mexts.ToDataTableField("Sys_MapExt"));



		// Sys_GroupField
		sql = "SELECT OID FROM Sys_GroupField WHERE   " + Glo.MapDataLikeKey(this.No, "FrmID"); // +" " + Glo.MapDataLikeKey(this.No, "EnName");
		GroupFields gfs = new GroupFields();
		gfs.RetrieveInSQL(sql);
		ds.Tables.Add(gfs.ToDataTableField("Sys_GroupField"));


		// Sys_MapFrame
		sql = "SELECT MyPK FROM Sys_MapFrame WHERE" + Glo.MapDataLikeKey(this.No, "FK_MapData");
		MapFrames mfs = new MapFrames();
		mfs.RetrieveInSQL("MyPK", sql);
		ds.Tables.Add(mfs.ToDataTableField("Sys_MapFrame"));



		// Sys_FrmLine.
		sql = "SELECT MyPK FROM Sys_FrmLine WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		FrmLines frmls = new FrmLines();
		frmls.RetrieveInSQL(sql);
		ds.Tables.Add(frmls.ToDataTableField("Sys_FrmLine"));


		// Sys_FrmLab.
		sql = "SELECT MyPK FROM Sys_FrmLab WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		FrmLabs frmlabs = new FrmLabs();
		frmlabs.RetrieveInSQL(sql);
		ds.Tables.Add(frmlabs.ToDataTableField("Sys_FrmLab"));



		// Sys_FrmEle.
		sql = "SELECT MyPK FROM Sys_FrmEle WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");

		FrmEles frmEles = new FrmEles();
		frmEles.RetrieveInSQL(sql);
		ds.Tables.Add(frmEles.ToDataTableField("Sys_FrmEle"));

		// Sys_FrmLink.
		sql = "SELECT MyPK FROM Sys_FrmLink WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		FrmLinks frmLinks = new FrmLinks();
		frmLinks.RetrieveInSQL(sql);
		ds.Tables.Add(frmLinks.ToDataTableField("Sys_FrmLink"));

		// Sys_FrmRB.
		sql = "SELECT MyPK FROM Sys_FrmRB WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		FrmRBs frmRBs = new FrmRBs();
		frmRBs.RetrieveInSQL(sql);
		ds.Tables.Add(frmRBs.ToDataTableField("Sys_FrmRB"));

		// Sys_FrmImgAth.
		sql = "SELECT MyPK FROM Sys_FrmImgAth WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		FrmImgAths frmIs = new FrmImgAths();
		frmIs.RetrieveInSQL(sql);
		ds.Tables.Add(frmIs.ToDataTableField("Sys_FrmImgAth"));

		// Sys_FrmImg.
		sql = "SELECT MyPK FROM Sys_FrmImg WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		FrmImgs frmImgs = new FrmImgs();
		frmImgs.RetrieveInSQL(sql);
		ds.Tables.Add(frmImgs.ToDataTableField("Sys_FrmImg"));


		// Sys_FrmAttachment.
		sql = "SELECT MyPK FROM Sys_FrmAttachment WHERE FK_Node=0 AND " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		FrmAttachments frmaths = new FrmAttachments();
		frmaths.RetrieveInSQL(sql);
		ds.Tables.Add(frmaths.ToDataTableField("Sys_FrmAttachment"));

		// Sys_FrmEvent.
		sql = "SELECT MyPK FROM Sys_FrmEvent WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		FrmEvents frmevens = new FrmEvents();
		frmevens.RetrieveInSQL(sql);
		ds.Tables.Add(frmevens.ToDataTableField("Sys_FrmEvent"));
		return ds;
	}
	public final DataSet GetFlow2017(String path)
	{
		// 把所有的数据都存储在这里。
		DataSet ds = new DataSet();

		// 流程信息。
		String sql = "SELECT * FROM WF_Flow WHERE No='" + this.No + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Flow";
		ds.Tables.Add(dt);

		// 节点信息
		sql = "SELECT * FROM WF_Node WHERE FK_Flow='" + this.No + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Node";
		ds.Tables.Add(dt);

		// 单据模版. 
		BillTemplates tmps = new BillTemplates(this.No);
		String pks = "";
		for (BillTemplate tmp : tmps)
		{
			try
			{
				if (path != null)
				{
					Files.copy(Paths.get(SystemConfig.PathOfDataUser + "\\CyclostyleFile\\" + tmp.getNo() + ".rtf"), Paths.get(path + "\\" + tmp.getNo() + ".rtf"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			catch (java.lang.Exception e)
			{
				pks += "@" + tmp.PKVal;
				tmp.Delete();
			}
		}
		tmps.Remove(pks);
		ds.Tables.Add(tmps.ToDataTableField("WF_BillTemplate"));

		String sqlin = "SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.No + "'";


		// 条件信息
		sql = "SELECT * FROM WF_Cond WHERE FK_Flow='" + this.No + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Cond";
		ds.Tables.Add(dt);

		// 转向规则.
		//sql = "SELECT * FROM WF_TurnTo WHERE FK_Flow='" + this.No + "'";
		//dt = DBAccess.RunSQLReturnTable(sql);
		//dt.TableName = "WF_TurnTo";
		//ds.Tables.Add(dt);

		// 节点与表单绑定.
		sql = "SELECT * FROM WF_FrmNode WHERE FK_Flow='" + this.No + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_FrmNode";
		ds.Tables.Add(dt);

		// 表单方案.
		sql = "SELECT * FROM Sys_FrmSln WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmSln";
		ds.Tables.Add(dt);

		// 方向
		sql = "SELECT * FROM WF_Direction WHERE Node IN (" + sqlin + ") OR ToNode In (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Direction";
		ds.Tables.Add(dt);

		// 流程标签.
		LabNotes labs = new LabNotes(this.No);
		ds.Tables.Add(labs.ToDataTableField("WF_LabNote"));


		// 可退回的节点。
		sql = "SELECT * FROM WF_NodeReturn WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeReturn";
		ds.Tables.Add(dt);

		// 工具栏。
		sql = "SELECT * FROM WF_NodeToolbar WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeToolbar";
		ds.Tables.Add(dt);

		// 节点与部门。
		sql = "SELECT * FROM WF_NodeDept WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeDept";
		ds.Tables.Add(dt);


		// 节点与岗位权限。
		sql = "SELECT * FROM WF_NodeStation WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeStation";
		ds.Tables.Add(dt);

		// 节点与人员。
		sql = "SELECT * FROM WF_NodeEmp WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeEmp";
		ds.Tables.Add(dt);

		// 抄送人员。
		sql = "SELECT * FROM WF_CCEmp WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCEmp";
		ds.Tables.Add(dt);

		// 抄送部门。
		sql = "SELECT * FROM WF_CCDept WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCDept";
		ds.Tables.Add(dt);

		// 抄送部门。
		sql = "SELECT * FROM WF_CCStation WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCStation";
		ds.Tables.Add(dt);

		// 延续子流程。
		sql = "SELECT * FROM WF_NodeSubFlow WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeSubFlow";
		ds.Tables.Add(dt);


		int flowID = Integer.parseInt(this.No);
		sql = "SELECT * FROM Sys_MapData WHERE " + Glo.MapDataLikeKey(this.No, "No");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		ds.Tables.Add(dt);


		// Sys_MapAttr.
		sql = "SELECT * FROM Sys_MapAttr WHERE  " + Glo.MapDataLikeKey(this.No, "FK_MapData") + " ORDER BY FK_MapData,Idx";
		//sql = "SELECT * FROM Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData") + "  ORDER BY FK_MapData,Idx";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		ds.Tables.Add(dt);

		// Sys_EnumMain
		//sql = "SELECT * FROM Sys_EnumMain WHERE No IN (SELECT KeyOfEn from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData") +")";
		sql = "SELECT * FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData") + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_EnumMain";
		ds.Tables.Add(dt);

		// Sys_Enum
		sql = "SELECT * FROM Sys_Enum WHERE EnumKey IN ( SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData") + " ) )";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_Enum";
		ds.Tables.Add(dt);

		// Sys_MapDtl
		sql = "SELECT * FROM Sys_MapDtl WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapDtl";
		ds.Tables.Add(dt);

		// Sys_MapExt
		//sql = "SELECT * FROM Sys_MapExt WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		sql = "SELECT * FROM Sys_MapExt WHERE  " + Glo.MapDataLikeKey(this.No, "FK_MapData"); // +Glo.MapDataLikeKey(this.No, "FK_MapData");

		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		ds.Tables.Add(dt);

		// Sys_GroupField
		sql = "SELECT * FROM Sys_GroupField WHERE   " + Glo.MapDataLikeKey(this.No, "FrmID"); // +" " + Glo.MapDataLikeKey(this.No, "EnName");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_GroupField";
		ds.Tables.Add(dt);

		// Sys_MapFrame
		sql = "SELECT * FROM Sys_MapFrame WHERE" + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapFrame";
		ds.Tables.Add(dt);

		// Sys_FrmLine.
		sql = "SELECT * FROM Sys_FrmLine WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLine";
		ds.Tables.Add(dt);

		// Sys_FrmLab.
		sql = "SELECT * FROM Sys_FrmLab WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLab";
		ds.Tables.Add(dt);

		// Sys_FrmEle.
		sql = "SELECT * FROM Sys_FrmEle WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmEle";
		ds.Tables.Add(dt);

		// Sys_FrmLink.
		sql = "SELECT * FROM Sys_FrmLink WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLink";
		ds.Tables.Add(dt);

		// Sys_FrmRB.
		sql = "SELECT * FROM Sys_FrmRB WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmRB";
		ds.Tables.Add(dt);

		// Sys_FrmImgAth.
		sql = "SELECT * FROM Sys_FrmImgAth WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmImgAth";
		ds.Tables.Add(dt);

		// Sys_FrmImg.
		sql = "SELECT * FROM Sys_FrmImg WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmImg";
		ds.Tables.Add(dt);

		// Sys_FrmAttachment.
		sql = "SELECT * FROM Sys_FrmAttachment WHERE FK_Node=0 AND " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmAttachment";
		ds.Tables.Add(dt);

		// Sys_FrmEvent.
		sql = "SELECT * FROM Sys_FrmEvent WHERE " + Glo.MapDataLikeKey(this.No, "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmEvent";
		ds.Tables.Add(dt);
		return ds;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 产生数据模板。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他公用方法1
	/** 
	 重新设置Rpt表
	*/
	public final void CheckRptOfReset()
	{
		String fk_mapData = "ND" + Integer.parseInt(this.No) + "Rpt";
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "'";
		DBAccess.RunSQL(sql);

		sql = "DELETE FROM Sys_MapData WHERE No='" + fk_mapData + "'";
		DBAccess.RunSQL(sql);
		this.DoCheck_CheckRpt(this.getHisNodes());
	}
	/** 
	 重新装载
	 
	 @return 
	*/
	public final String DoReloadRptData()
	{
		this.DoCheck_CheckRpt(this.getHisNodes());

		// 检查报表数据是否丢失。

		if (this.getHisDataStoreModel() != DataStoreModel.ByCCFlow)
		{
			return "@流程" + this.No + this.Name + "的数据存储非轨迹模式不能重新生成.";
		}

		DBAccess.RunSQL("DELETE FROM " + this.getPTable());

		String sql = "SELECT OID FROM ND" + Integer.parseInt(this.No) + "01 WHERE  OID NOT IN (SELECT OID FROM  " + this.getPTable() + " ) ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		this.CheckRptData(this.getHisNodes(), dt);

		return "@共有:" + dt.Rows.size() + "条(" + this.Name + ")数据被装载成功。";
	}
	/** 
	 检查与修复报表数据
	 
	 @param nds
	 @param dt
	*/
	private String CheckRptData(Nodes nds, DataTable dt)
	{
		GERpt rpt = new GERpt("ND" + Integer.parseInt(this.No) + "Rpt");
		String err = "";
		for (DataRow dr : dt.Rows)
		{
			rpt.ResetDefaultVal();
			int oid = Integer.parseInt(dr.get(0).toString());
			rpt.SetValByKey("OID", oid);
			Work startWork = null;
			Work endWK = null;
			String flowEmps = "@";
			for (Node nd : nds)
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
					if (nd.getNodeID() == Integer.parseInt(this.No + "01"))
					{
						startWork = wk;
					}

					try
					{
						if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName)
						{
							if (flowEmps.contains("@" + wk.getRecOfEmp().Name + "@"))
							{
								continue;
							}
							flowEmps += wk.getRecOfEmp().Name + "@";
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
							flowEmps += wk.getRec() + "," + wk.getRecOfEmp().Name;
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
				TimeSpan ts = endWK.getRDT_DateTime() - startWork.getRDT_DateTime();
				rpt.setFlowDaySpan(ts.Days);
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
	*/
	private void CheckRptDtl(Nodes nds)
	{
		MapDtls dtlsDtl = new MapDtls();
		dtlsDtl.Retrieve(MapDtlAttr.FK_MapData, "ND" + Integer.parseInt(this.No) + "Rpt");
		for (MapDtl dtl : dtlsDtl)
		{
			dtl.Delete();
		}

		//  dtlsDtl.Delete(MapDtlAttr.FK_MapData, "ND" + int.Parse(this.No) + "Rpt");
		for (Node nd : nds)
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

			String rpt = "ND" + Integer.parseInt(this.No) + "Rpt";
			int i = 0;
			for (MapDtl dtl : dtls)
			{
				i++;
				String rptDtlNo = "ND" + Integer.parseInt(this.No) + "RptDtl" + String.valueOf(i);
				MapDtl rtpDtl = new MapDtl();
				rtpDtl.No = rptDtlNo;
				if (rtpDtl.RetrieveFromDBSources() == 0)
				{
					rtpDtl.Copy(dtl);
					rtpDtl.No = rptDtlNo;
					rtpDtl.FK_MapData = rpt;
					rtpDtl.PTable = rptDtlNo;
					rtpDtl.Insert();
				}

				MapAttrs attrsRptDtl = new MapAttrs(rptDtlNo);
				MapAttrs attrs = new MapAttrs(dtl.No);
				for (MapAttr attr : attrs)
				{
					if (attrsRptDtl.Contains(MapAttrAttr.KeyOfEn, attr.KeyOfEn) == true)
					{
						continue;
					}

					MapAttr attrN = new MapAttr();
					attrN.Copy(attr);
					attrN.FK_MapData = rptDtlNo;
					switch (attr.KeyOfEn)
					{
						case "FK_NY":
							attrN.UIVisible = true;
							attrN.Idx = 100;
							attrN.UIWidth = 60;
							break;
						case "RDT":
							attrN.UIVisible = true;
							attrN.Idx = 100;
							attrN.UIWidth = 60;
							break;
						case "Rec":
							attrN.UIVisible = true;
							attrN.Idx = 100;
							attrN.UIWidth = 60;
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
	*/
	private void DoCheck_CheckRpt(Nodes nds)
	{
		String fk_mapData = "ND" + Integer.parseInt(this.No) + "Rpt";
		String flowId = Integer.parseInt(this.No).toString();

		//生成该节点的 nds 比如  "'ND101','ND102','ND103'"
		String ndsstrs = "";
		for (BP.WF.Node nd : nds)
		{
			ndsstrs += "'ND" + nd.getNodeID() + "',";
		}
		ndsstrs = ndsstrs.substring(0, ndsstrs.length() - 1);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 插入字段。
		String sql = "SELECT distinct KeyOfEn FROM Sys_MapAttr WHERE FK_MapData IN (" + ndsstrs + ")";
		if (SystemConfig.AppCenterDBType == DBType.MySQL)
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
			pks += dr.get(0) + "@";
		}

		//遍历 - 所有节点表单字段的合集
		for (DataRow dr : dt.Rows)
		{
			if (pks.contains("@" + dr.get("KeyOfEn").toString() + "@") == true)
			{
				continue;
			}

			String mypk = dr.get("MyPK").toString();

			pks += dr.get("KeyOfEn").toString() + "@";

			//找到这个属性.
			BP.Sys.MapAttr ma = new BP.Sys.MapAttr(mypk);

			ma.MyPK = "ND" + flowId + "Rpt_" + ma.KeyOfEn;
			ma.FK_MapData = "ND" + flowId + "Rpt";
			ma.UIIsEnable = false;

			if (ma.DefValReal.Contains("@"))
			{
				/*如果是一个有变量的参数.*/
				ma.DefVal = "";
			}

			// 如果不存在.
			if (ma.IsExits == false)
			{
				ma.Insert();
			}
		}

		MapAttrs attrs = new MapAttrs(fk_mapData);

		// 创建mapData.
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.No = "ND" + flowId + "Rpt";
		if (md.RetrieveFromDBSources() == 0)
		{
			md.Name = this.Name;
			md.PTable = this.getPTable();
			md.Insert();
		}
		else
		{
			md.Name = this.Name;
			md.PTable = this.getPTable();
			md.Update();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 插入字段。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 补充上流程字段到NDxxxRpt.
		int groupID = 0;
		for (MapAttr attr : attrs)
		{
			switch (attr.KeyOfEn)
			{
				case StartWorkAttr.FK_Dept:
					attr.UIContralType = UIContralType.TB;
					attr.LGType = FieldTypeS.Normal;
					attr.UIVisible = true;
					attr.GroupID = groupID; // gfs[0].GetValIntByKey("OID");
					attr.UIIsEnable = false;
					attr.DefVal = "";
					attr.MaxLen = 100;
					attr.Update();
					break;
				case "FK_NY":
					//  attr.UIBindKey = "BP.Pub.NYs";
					attr.UIContralType = UIContralType.TB;
					attr.LGType = FieldTypeS.Normal;
					attr.UIVisible = true;
					attr.UIIsEnable = false;
					attr.GroupID = groupID;
					attr.Update();
					break;
				case "FK_Emp":
					break;
				default:
					break;
			}
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.Title) == false)
		{
			/* 标题 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.Title; // "FlowEmps";
			attr.Name = "标题";
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = true;
			attr.MinLen = 0;
			attr.MaxLen = 400;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.OID) == false)
		{
			/* WorkID */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.KeyOfEn = "OID";
			attr.Name = "WorkID";
			attr.MyDataType = BP.DA.DataType.AppInt;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.DefVal = "0";
			attr.HisEditType = BP.En.EditType.Readonly;
			attr.Insert();
		}


		if (attrs.Contains(md.No + "_" + GERptAttr.FID) == false)
		{
			/* WorkID */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.KeyOfEn = "FID";
			attr.Name = "FID";
			attr.MyDataType = BP.DA.DataType.AppInt;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.DefVal = "0";
			attr.HisEditType = BP.En.EditType.Readonly;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.WFState) == false)
		{
			/* 流程状态 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.WFState;
			attr.Name = "流程状态";
			attr.MyDataType = DataType.AppInt;
			attr.UIBindKey = GERptAttr.WFState;
			attr.UIContralType = UIContralType.DDL;
			attr.LGType = FieldTypeS.Enum;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.MinLen = 0;
			attr.MaxLen = 1000;
			attr.Idx = -1;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.WFSta) == false)
		{
			/* 流程状态Ext */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.WFSta;
			attr.Name = "状态";
			attr.MyDataType = DataType.AppInt;
			attr.UIBindKey = GERptAttr.WFSta;
			attr.UIContralType = UIContralType.DDL;
			attr.LGType = FieldTypeS.Enum;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.MinLen = 0;
			attr.MaxLen = 1000;
			attr.Idx = -1;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.FlowEmps) == false)
		{
			/* 参与人 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.FlowEmps; // "FlowEmps";
			attr.Name = "参与人";
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = true;
			attr.MinLen = 0;
			attr.MaxLen = 1000;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.FlowStarter) == false)
		{
			/* 发起人 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.FlowStarter;
			attr.Name = "发起人";
			attr.MyDataType = DataType.AppString;

			//attr.UIBindKey = "BP.Port.Emps";
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;

			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.MinLen = 0;
			attr.MaxLen = 32;
			attr.Idx = -1;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.FlowStartRDT) == false)
		{
			/* MyNum */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.FlowStartRDT; // "FlowStartRDT";
			attr.Name = "发起时间";
			attr.MyDataType = DataType.AppDateTime;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.Idx = -101;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.FlowEnder) == false)
		{
			/* 发起人 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.FlowEnder;
			attr.Name = "结束人";
			attr.MyDataType = DataType.AppString;
			// attr.UIBindKey = "BP.Port.Emps";
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.MinLen = 0;
			attr.MaxLen = 32;
			attr.Idx = -1;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.FlowEnderRDT) == false)
		{
			/* MyNum */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.FlowEnderRDT; // "FlowStartRDT";
			attr.Name = "结束时间";
			attr.MyDataType = DataType.AppDateTime;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.Idx = -101;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.FlowEndNode) == false)
		{
			/* 结束节点 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.FlowEndNode;
			attr.Name = "结束节点";
			attr.MyDataType = DataType.AppInt;
			attr.DefVal = "0";
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.HisEditType = EditType.UnDel;
			attr.Idx = -101;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.FlowDaySpan) == false)
		{
			/* FlowDaySpan */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.FlowDaySpan; // "FlowStartRDT";
			attr.Name = "跨度(天)";
			attr.MyDataType = DataType.AppFloat;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.UIIsLine = false;
			attr.Idx = -101;
			attr.DefVal = "0";
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.PFlowNo) == false)
		{
			/* 父流程 流程编号 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.PFlowNo;
			attr.Name = "父流程编号"; //  父流程流程编号
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = true;
			attr.MinLen = 0;
			attr.MaxLen = 3;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.PNodeID) == false)
		{
			/* 父流程WorkID */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.PNodeID;
			attr.Name = "父流程启动的节点";
			attr.MyDataType = DataType.AppInt;
			attr.DefVal = "0";
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.HisEditType = EditType.UnDel;
			attr.Idx = -101;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.PWorkID) == false)
		{
			/* 父流程WorkID */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.PWorkID;
			attr.Name = "父流程WorkID";
			attr.MyDataType = DataType.AppInt;
			attr.DefVal = "0";
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.HisEditType = EditType.UnDel;
			attr.Idx = -101;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.PEmp) == false)
		{
			/* 调起子流程的人员 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.PEmp;
			attr.Name = "调起子流程的人员";
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = true;
			attr.MinLen = 0;
			attr.MaxLen = 32;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.BillNo) == false)
		{
			/* 父流程 流程编号 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.BillNo;
			attr.Name = "单据编号"; //  单据编号
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.MinLen = 0;
			attr.MaxLen = 100;
			attr.Idx = -100;
			attr.Insert();
		}


		if (attrs.Contains(md.No + "_MyNum") == false)
		{
			/* MyNum */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = "MyNum";
			attr.Name = "条";
			attr.MyDataType = DataType.AppInt;
			attr.DefVal = "1";
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.HisEditType = EditType.UnDel;
			attr.Idx = -101;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.AtPara) == false)
		{
			/* 父流程 流程编号 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.AtPara;
			attr.Name = "参数"; // 单据编号
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.MinLen = 0;
			attr.MaxLen = 4000;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.GUID) == false)
		{
			/* 父流程 流程编号 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.GUID;
			attr.Name = "GUID"; // 单据编号
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = false;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.MinLen = 0;
			attr.MaxLen = 32;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.PrjNo) == false)
		{
			/* 项目编号 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.PrjNo;
			attr.Name = "项目编号"; //  项目编号
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.MinLen = 0;
			attr.MaxLen = 100;
			attr.Idx = -100;
			attr.Insert();
		}
		if (attrs.Contains(md.No + "_" + GERptAttr.PrjName) == false)
		{
			/* 项目名称 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.PrjName;
			attr.Name = "项目名称"; //  项目名称
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = false;
			attr.MinLen = 0;
			attr.MaxLen = 100;
			attr.Idx = -100;
			attr.Insert();
		}

		if (attrs.Contains(md.No + "_" + GERptAttr.FlowNote) == false)
		{
			/* 流程信息 */
			MapAttr attr = new BP.Sys.MapAttr();
			attr.FK_MapData = md.No;
			attr.HisEditType = EditType.UnDel;
			attr.KeyOfEn = GERptAttr.FlowNote;
			attr.Name = "流程信息"; //  父流程流程编号
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.LGType = FieldTypeS.Normal;
			attr.UIVisible = true;
			attr.UIIsEnable = false;
			attr.UIIsLine = true;
			attr.MinLen = 0;
			attr.MaxLen = 500;
			attr.Idx = -100;
			attr.Insert();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 补充上流程字段。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 为流程字段设置分组。
		try
		{
			String flowInfo = "流程信息";
			GroupField flowGF = new GroupField();
			int num = flowGF.Retrieve(GroupFieldAttr.FrmID, fk_mapData, GroupFieldAttr.Lab, "流程信息");
			if (num == 0)
			{
				flowGF = new GroupField();
				flowGF.Lab = flowInfo;
				flowGF.FrmID = fk_mapData;
				flowGF.Idx = -1;
				flowGF.Insert();
			}
			sql = "UPDATE Sys_MapAttr SET GroupID='" + flowGF.OID + "' WHERE  FK_MapData='" + fk_mapData + "'  AND KeyOfEn IN('" + GERptAttr.PFlowNo + "','" + GERptAttr.PWorkID + "','" + GERptAttr.MyNum + "','" + GERptAttr.FK_Dept + "','" + GERptAttr.FK_NY + "','" + GERptAttr.FlowDaySpan + "','" + GERptAttr.FlowEmps + "','" + GERptAttr.FlowEnder + "','" + GERptAttr.FlowEnderRDT + "','" + GERptAttr.FlowEndNode + "','" + GERptAttr.FlowStarter + "','" + GERptAttr.FlowStartRDT + "','" + GERptAttr.WFState + "')";
			DBAccess.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
			Log.DefaultLogWriteLineError(ex.getMessage());
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 为流程字段设置分组

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 尾后处理.
		GERpt gerpt = this.getHisGERpt();
		gerpt.CheckPhysicsTable(); //让报表重新生成.

		DBAccess.RunSQL("DELETE FROM Sys_GroupField WHERE FrmID='" + fk_mapData + "' AND OID NOT IN (SELECT GroupID FROM Sys_MapAttr WHERE FK_MapData = '" + fk_mapData + "')");

		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name='活动时间' WHERE FK_MapData='ND" + flowId + "Rpt' AND KeyOfEn='CDT'");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name='参与者' WHERE FK_MapData='ND" + flowId + "Rpt' AND KeyOfEn='Emps'");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 尾后处理.
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 其他公用方法1

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行流程事件.
	/** 
	 执行运动事件
	 
	 @param doType 事件类型
	 @param currNode 当前节点
	 @param en 实体
	 @param atPara 参数
	 @param objs 发送对象，可选
	 @return 执行结果
	*/

	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, SendReturnObjs objs, int toNodeID)
	{
		return DoFlowEventEntity(doType, currNode, en, atPara, objs, toNodeID, null);
	}

	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, SendReturnObjs objs)
	{
		return DoFlowEventEntity(doType, currNode, en, atPara, objs, 0, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string DoFlowEventEntity(string doType, Node currNode, Entity en, string atPara, SendReturnObjs objs, int toNodeID = 0, string toEmps = null)
	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, SendReturnObjs objs, int toNodeID, String toEmps)
	{
		if (currNode == null)
		{
			return null;
		}

		String str = null;
		if (this.getFEventEntity() == null)
		{
			/* 如果是发送成功了, 并且在没有任何设置的情况下，就执行默认的方法. */
			//if (doType == EventListOfNode.SendSuccess)
			//{
			//    CCInterface.PortalInterfaceSoapClient soap = BP.WF.Glo.GetPortalInterfaceSoapClient();
			//    soap.SendSuccess(currNode.FK_Flow, currNode.NodeID, Int64.Parse(en.PKVal.ToString()), BP.Web.WebUser.No, BP.Web.WebUser.Name);
			//}

			//if (doType == EventListOfNode.SendWhen)
			//{
			//    /*如果是发送成功了, 并且在没有任何设置的情况下，就执行默认的方法.*/
			//    CCInterface.PortalInterfaceSoapClient soap = BP.WF.Glo.GetPortalInterfaceSoapClient();
			//    soap.SendWhen(currNode.FK_Flow, currNode.NodeID, Int64.Parse(en.PKVal.ToString()), BP.Web.WebUser.No, BP.Web.WebUser.Name);
			//}

			//if (doType == EventListOfNode.FlowOverBefore)
			//{
			//    /*如果是发送成功了, 并且在没有任何设置的情况下，就执行默认的方法.*/
			//    CCInterface.PortalInterfaceSoapClient soap = BP.WF.Glo.GetPortalInterfaceSoapClient();
			//    soap.FlowOverBefore(currNode.FK_Flow, currNode.NodeID, Int64.Parse(en.PKVal.ToString()), BP.Web.WebUser.No, BP.Web.WebUser.Name);
			//}
		}

		if (this.getFEventEntity() != null)
		{
			this.getFEventEntity().SendReturnObjs = objs;
			str = this.getFEventEntity().DoIt(doType, currNode, en, atPara, toNodeID, toEmps);
		}

		FrmEvents fes = currNode.getFrmEvents();
		//增加对流程事件的支持，流程事件时，FrmEvent.FK_MapData=FK_Flow，added by liuxc,2017-05-20
		switch (doType)
		{
			case EventListOfNode.FlowOverAfter:
			case EventListOfNode.FlowOverBefore:
			case EventListOfNode.AfterFlowDel:
			case EventListOfNode.BeforeFlowDel:
				if (fes.GetEntityByKey(FrmEventAttr.FK_Event, doType) == null)
				{
					FrmEvents flowEvents = new FrmEvents();
					flowEvents.Retrieve(FrmEventAttr.FK_MapData, this.No);
					fes.AddEntities(flowEvents);
				}
				break;
			default:
				break;
		}

		// 2019-08-27 取消节点事件 zl
		if (str == null)
		{
			str = fes.DoEventNode(doType, en, atPara);
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理消息推送, edit  by zhoupeng for dengzhou gov project. 2016.05.01
		//有一些事件没有消息，直接 return ;
		switch (doType)
		{
			case EventListOfNode.WorkArrive:
			case EventListOfNode.SendSuccess:
			case EventListOfNode.ShitAfter:
			case EventListOfNode.ReturnAfter:
			case EventListOfNode.UndoneAfter:
			case EventListOfNode.AskerReAfter:
			case EventListOfNode.FlowOverAfter: //流程结束后.
				break;
			default:
				return str;
		}

		//执行消息的发送.
		PushMsgs pms = currNode.getHisPushMsgs();
		if (doType.equals(EventListOfNode.UndoneAfter))
		{
			AtPara ap = new AtPara(atPara);
			if (toNodeID == 0)
			{
				toNodeID = ap.GetValIntByKey("ToNode");
			}
			if (toNodeID == 0)
			{
				return str;
			}

			Node toNode = new Node(toNodeID);
			pms = toNode.getHisPushMsgs();
		}
		String msgAlert = ""; //生成的提示信息.
		for (PushMsg item : pms)
		{
			if (!item.getFK_Event().equals(doType))
			{
				continue;
			}

			if (item.getSMSPushWay() == 0)
			{
				continue; // 如果都没有消息设置，就放过.
			}

			//执行发送消息.
			msgAlert += item.DoSendMessage(currNode, en, atPara, objs);
		}
		return str + msgAlert;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理消息推送.

		return str;
	}
	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara)
	{
		String str = this.DoFlowEventEntity(doType, currNode, en, atPara, null);
		return BP.DA.DataType.PraseGB2312_To_utf8(str);
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行流程事件.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
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
	public final BigDecimal getAvgDay()
	{
		return this.GetValIntByKey(FlowAttr.AvgDay);
	}
	public final void setAvgDay(BigDecimal value)
	{
		this.SetValByKey(FlowAttr.AvgDay, value);
	}
	public final int getStartNodeID()
	{
		return Integer.parseInt(this.No + "01");
			//return this.GetValIntByKey(FlowAttr.StartNodeID);
	}
	/** 
	 add 2013-01-01.
	 业务主表(默认为NDxxRpt)
	*/
	public final String getPTable()
	{
		String s = this.GetValStringByKey(FlowAttr.PTable);
		if (DataType.IsNullOrEmpty(s))
		{
			s = "ND" + Integer.parseInt(this.No) + "Rpt";
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
		if (DataType.IsNullOrEmpty(strs))
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
	*/
	public final String getFK_FlowSortText()
	{
		FlowSort fs = new FlowSort(this.getFK_FlowSort());
		return fs.Name;
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 计算属性
	/** 
	 流程类型(大的类型)
	*/
	public final int getFlowType_del()
	{
		return this.GetValIntByKey(FlowAttr.FlowType);
	}
	/** 
	 (当前节点为子流程时)是否检查所有子流程完成后父流程自动发送
	*/
	public final SubFlowOver getSubFlowOver()
	{
		return SubFlowOver.forValue(this.GetValIntByKey(FlowAttr.IsAutoSendSubFlowOver));
	}
	/** 
	 是否启用数据模版？
	*/
	public final boolean getIsDBTemplate()
	{
		return this.GetValBooleanByKey(FlowAttr.IsDBTemplate);
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
			/*
			if (this.FlowType==2 || this.FlowType==1 )
			    return true;
			else
			    return false;
			    */
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 扩展属性
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
	 是否启用子流程运行结束后，主流程自动运行到下一节点
	*/
	public final boolean getIsToParentNextNode()
	{
		return this.GetValBooleanByKey(NodeAttr.IsToParentNextNode);
	}
	public final void setIsToParentNextNode(boolean value)
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
	public final Nodes getHisNodes()
	{
		_HisNodes = new Nodes(this.No);
		return _HisNodes;
	}
	public final void setHisNodes(Nodes value)
	{
		_HisNodes = value;
	}
	/** 
	 他的 Start 节点
	*/
	public final Node getHisStartNode()
	{

		for (Node nd : this.getHisNodes())
		{
			if (nd.getIsStartNode())
			{
				return nd;
			}
		}
		throw new RuntimeException("@没有找到他的开始节点,工作流程[" + this.Name + "]定义错误.");
	}
	/** 
	 他的事务类别
	*/
	public final FlowSort getHisFlowSort()
	{
		return new FlowSort(this.getFK_FlowSort());
	}
	/** 
	 flow data 数据
	*/
	public final BP.WF.Data.GERpt getHisGERpt()
	{
		try
		{
			BP.WF.Data.GERpt wk = new BP.WF.Data.GERpt("ND" + Integer.parseInt(this.No) + "Rpt");
			return wk;
		}
		catch (java.lang.Exception e)
		{
			this.DoCheck();
			BP.WF.Data.GERpt wk1 = new BP.WF.Data.GERpt("ND" + Integer.parseInt(this.No) + "Rpt");
			return wk1;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法

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
	public Flow(String _No)
	{
		this.No = _No;
		if (SystemConfig.IsDebug)
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
	protected boolean beforeUpdateInsertAction()
	{
		//获得事件实体.
		if (DataType.IsNullOrEmpty(this.getFlowMark()) == true)
		{
			this.setFlowEventEntity(BP.WF.Glo.GetFlowEventEntityStringByFlowMark(this.No));
		}
		else
		{
			this.setFlowEventEntity(BP.WF.Glo.GetFlowEventEntityStringByFlowMark(this.getFlowMark()));
		}

		DBAccess.RunSQL("UPDATE WF_Node SET FlowName='" + this.Name + "' WHERE FK_Flow='" + this.No + "'");
		DBAccess.RunSQL("UPDATE Sys_MapData SET  Name='" + this.Name + "' WHERE No='" + this.getPTable() + "'");
		return super.beforeUpdateInsertAction();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_Flow", "流程");
		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("3");

		map.AddTBStringPK(FlowAttr.No, null, "编号", true, true, 1, 5, 3);
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

			// map.AddBoolean(FlowAttr.IsOK, true, "是否启用", true, true);
		map.AddTBInt(FlowAttr.IsCanStart, 1, "可以独立启动否？", true, true);
		map.AddTBInt(FlowAttr.IsStartInMobile, 1, "是否可以在手机里发起？", true, true);

		map.AddTBDecimal(FlowAttr.AvgDay, 0, "平均运行用天", false, false);

		map.AddTBInt(FlowAttr.IsFullSA, 0, "是否自动计算未来的处理人？(启用后,ccflow就会为已知道的节点填充处理人到WF_SelectAccper)", false, false);
		map.AddTBInt(FlowAttr.IsMD5, 0, "IsMD5", false, false);
		map.AddTBInt(FlowAttr.Idx, 0, "显示顺序号(在发起列表中)", true, false);

		map.AddTBInt(FlowAttr.SDTOfFlowRole, 0, "流程计划完成日期计算规则", true, false);
		map.AddTBString(FlowAttr.SDTOfFlowRoleSQL, null, "流程计划完成日期计算规则SQL", false, false, 0, 200, 10);

		map.AddTBString(FlowAttr.Paras, null, "参数", false, false, 0, 1000, 10);

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
		map.AddTBString(FlowAttr.FlowNoteExp, null, "备注表达式", true, false, 0, 200, 10, true);

			//部门权限控制类型,此属性在报表中控制的.
		map.AddTBInt(FlowAttr.DRCtrlType, 0, "部门查询权限控制方式", true, false);

			//运行主机. 这个流程运行在那个子系统的主机上.
		map.AddTBString(FlowAttr.HostRun, null, "运行主机(IP+端口)", true, false, 0, 40, 10, true);
		map.AddTBInt(FlowAttr.IsToParentNextNode, 0, "子流程运行到该节点时，让父流程自动运行到下一步", false, false);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 流程启动限制
		map.AddTBInt(FlowAttr.StartLimitRole, 0, "启动限制规则", true, false);
		map.AddTBString(FlowAttr.StartLimitPara, null, "规则内容", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartLimitAlert, null, "限制提示", true, false, 0, 500, 10, false);
		map.AddTBInt(FlowAttr.StartLimitWhen, 0, "提示时间", true, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 流程启动限制

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 导航方式。

		map.AddTBInt(FlowAttr.IsDBTemplate, 0, "是否启用数据模版？", true, false);

			//批量发起 add 2013-12-27. 
		map.AddTBInt(FlowAttr.IsBatchStart, 0, "是否可以批量发起", true, false);
		map.AddTBString(FlowAttr.BatchStartFields, null, "批量发起字段(用逗号分开)", true, false, 0, 300, 10, true);

			// map.AddTBInt(FlowAttr.IsEnableTaskPool, 0, "是否启用共享任务池", true, false);
			//map.AddDDLSysEnum(FlowAttr.TimelineRole, (int)TimelineRole.ByNodeSet, "时效性规则",
			// true, true, FlowAttr.TimelineRole, "@0=按节点(由节点属性来定义)@1=按发起人(开始节点SysSDTOfFlow字段计算)");

		map.AddTBInt(FlowAttr.IsAutoSendSubFlowOver, 0, "(当前节点为子流程时)是否检查所有子流程完成后父流程自动发送", true, true);

		map.AddTBString(FlowAttr.Ver, null, "版本号", true, true, 0, 20, 10);
			//设计类型 .
		map.AddTBInt(FlowAttr.FlowDeleteRole, 0, "流程实例删除规则", true, false);

			//参数.
		map.AddTBAtParas(1000);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 数据同步方案
			//数据同步方式.
		map.AddTBInt(FlowAttr.DTSWay, FlowDTSWay.None.getValue(), "同步方式", true, true);
		map.AddTBString(FlowAttr.DTSDBSrc, null, "数据源", true, false, 0, 200, 100, false);
		map.AddTBString(FlowAttr.DTSBTable, null, "业务表名", true, false, 0, 200, 100, false);
		map.AddTBString(FlowAttr.DTSBTablePK, null, "业务表主键", false, false, 0, 32, 10);

		map.AddTBInt(FlowAttr.DTSTime, FlowDTSTime.AllNodeSend.getValue(), "执行同步时间点", true, true);
		map.AddTBString(FlowAttr.DTSSpecNodes, null, "指定的节点ID", true, false, 0, 200, 100, false);
		map.AddTBInt(FlowAttr.DTSField, getDTSField().SameNames.getValue(), "要同步的字段计算方式", true, true);
		map.AddTBString(FlowAttr.DTSFields, null, "要同步的字段s,中间用逗号分开.", false, false, 0, 900, 100, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 数据同步方案

			// map.AddSearchAttr(FlowAttr.FK_FlowSort);
			// map.AddSearchAttr(FlowAttr.FlowRunWay);

		RefMethod rm = new RefMethod();
		rm.Title = "设计检查报告"; // "设计检查报告";
		rm.ToolTip = "检查流程设计的问题。";
		rm.Icon = "../../WF/Img/Btn/Confirm.gif";
		rm.ClassMethodName = this.toString() + ".DoCheck";
		rm.GroupName = "流程维护";
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

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region  公共方法
	/** 
	 创建索引
	 
	 @return 
	*/
	public final String CreateIndex()
	{
		// 为track表创建索引.  FID, WorkID
		String ptable = "ND" + Integer.parseInt(this.getPTable()) + "Track";

		// DBAccess.CreatIndex(DBUrlType.AppCenterDSN, ptable, "my");

		return "流程[" + this.No + "." + this.Name + "]索引创建成功.";
	}
	/** 
	 删除数据.
	 
	 @return 
	*/
	public final String DoDelData()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 删除独立表单的数据.
		String mysql = "SELECT OID FROM " + this.getPTable();
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.No);
		String strs = "";
		for (FrmNode nd : fns)
		{
			if (strs.contains("@" + nd.getFK_Frm()) == true)
			{
				continue;
			}

			strs += "@" + nd.getFK_Frm() + "@";
			try
			{
				MapData md = new MapData(nd.getFK_Frm());
				DBAccess.RunSQL("DELETE FROM " + md.PTable + " WHERE OID in (" + mysql + ")");
			}
			catch (java.lang.Exception e)
			{
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 删除独立表单的数据.

		String sql = "  WHERE FK_Node in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		String sql1 = " WHERE NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		// DBAccess.RunSQL("DELETE FROM WF_CHOfFlow WHERE FK_Flow='" + this.No + "'");

		DBAccess.RunSQL("DELETE FROM WF_Bill WHERE FK_Flow='" + this.No + "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE FK_Flow='" + this.No + "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.No + "'");

		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.No + "'");

		String sqlIn = " WHERE ReturnNode IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork " + sqlIn);
		DBAccess.RunSQL("DELETE FROM WF_SelectAccper " + sql);
		DBAccess.RunSQL("DELETE FROM WF_TransferCustom " + sql);
		// DBAccess.RunSQL("DELETE FROM WF_FileManager " + sql);
		DBAccess.RunSQL("DELETE FROM WF_RememberMe " + sql);

		if (DBAccess.IsExitsObject("ND" + Integer.parseInt(this.No) + "Track"))
		{
			DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(this.No) + "Track ");
		}

		if (DBAccess.IsExitsObject(this.getPTable()))
		{
			DBAccess.RunSQL("DELETE FROM " + this.getPTable());
		}

		DBAccess.RunSQL("DELETE FROM WF_CH WHERE FK_Flow='" + this.No + "'");
		//DBAccess.RunSQL("DELETE FROM Sys_MapExt WHERE FK_MapData LIKE 'ND"+int.Parse(this.No)+"%'" );

		//删除节点数据。
		Nodes nds = new Nodes(this.No);
		for (Node nd : nds)
		{
			try
			{
				Work wk = nd.getHisWork();
				DBAccess.RunSQL("DELETE FROM " + wk.EnMap.PhysicsTable);
			}
			catch (java.lang.Exception e2)
			{
			}

			MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
			for (MapDtl dtl : dtls)
			{
				try
				{
					DBAccess.RunSQL("DELETE FROM " + dtl.PTable);
				}
				catch (java.lang.Exception e3)
				{
				}
			}
		}
		MapDtls mydtls = new MapDtls("ND" + Integer.parseInt(this.No) + "Rpt");
		for (MapDtl dtl : mydtls)
		{
			try
			{
				DBAccess.RunSQL("DELETE FROM " + dtl.PTable);
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
	*/

	public static Flow DoLoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model)
	{
		return DoLoadFlowTemplate(fk_flowSort, path, model, "");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static Flow DoLoadFlowTemplate(string fk_flowSort, string path, ImpFlowTempleteModel model, string SpecialFlowNo = "")
	public static Flow DoLoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model, String SpecialFlowNo)
	{
		File info = new File(path);
		DataSet ds = new DataSet();

		try
		{
			ds.ReadXml(path);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@导入流程路径:" + path + "出错：" + ex.getMessage());
		}


		if (ds.Tables.Contains("WF_Flow") == false)
		{
			throw new RuntimeException("导入错误，非流程模版文件" + path + "。");
		}

		DataTable dtFlow = ds.Tables["WF_Flow"];
		Flow fl = new Flow();
		String oldFlowNo = dtFlow.Rows[0]["No"].toString();
		String oldFlowName = dtFlow.Rows[0]["Name"].toString();

		int oldFlowID = Integer.parseInt(oldFlowNo);
		int iOldFlowLength = String.valueOf(oldFlowID).length();
		String timeKey = LocalDateTime.now().toString("yyMMddhhmmss");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 根据不同的流程模式，设置生成不同的流程编号.
		switch (model)
		{
			case AsNewFlow: //做为一个新流程.
				fl.No = fl.GenerNewNo;
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
				break;
			case AsTempleteFlowNo: //用流程模版中的编号
				fl.No = oldFlowNo;
				if (fl.IsExits)
				{
					throw new RuntimeException("导入错误:流程模版(" + oldFlowName + ")中的编号(" + oldFlowNo + ")在系统中已经存在,流程名称为:" + dtFlow.Rows[0]["name"].toString());
				}
				else
				{
					fl.No = oldFlowNo;
					fl.DoDelData();
					fl.DoDelete(); //删除可能存在的垃圾.
				}
				break;
			case OvrewaiteCurrFlowNo: //覆盖当前的流程.
				fl.No = oldFlowNo;
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
				break;
			case AsSpecFlowNo:
				if (SpecialFlowNo.length() <= 0)
				{
					throw new RuntimeException("@您是按照指定的流程编号导入的，但是您没有传入正确的流程编号。");
				}

				fl.No = SpecialFlowNo.toString();
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
				break;
			default:
				throw new RuntimeException("@没有判断");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 根据不同的流程模式，设置生成不同的流程编号.

		// string timeKey = fl.No;
		int idx = 0;
		String infoErr = "";
		String infoTable = "";
		int flowID = Integer.parseInt(fl.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理流程表数据
		for (DataColumn dc : dtFlow.Columns)
		{
			String val = dtFlow.Rows[0][dc.ColumnName] instanceof String ? (String)dtFlow.Rows[0][dc.ColumnName] : null;
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
		fl.Insert();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理流程表数据

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理OID 插入重复的问题 Sys_GroupField, Sys_MapAttr.
		DataTable mydtGF = ds.Tables["Sys_GroupField"];
		DataTable myDTAttr = ds.Tables["Sys_MapAttr"];
		DataTable myDTAth = ds.Tables["Sys_FrmAttachment"];
		DataTable myDTDtl = ds.Tables["Sys_MapDtl"];
		DataTable myDFrm = ds.Tables["Sys_MapFrame"];
		if (mydtGF != null)
		{
			//throw new Exception("@" + fl.No + fl.Name + ", 缺少：Sys_GroupField");
			for (DataRow dr : mydtGF.Rows)
			{
				Sys.GroupField gf = new Sys.GroupField();
				for (DataColumn dc : mydtGF.Columns)
				{
					String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
					gf.SetValByKey(dc.ColumnName, val);
				}
				int oldID = gf.OID;
				gf.OID = DBAccess.GenerOID();
				dr.set("OID", gf.OID);

				// 属性。
				if (myDTAttr != null && myDTAttr.Columns.Contains("GroupID"))
				{
					for (DataRow dr1 : myDTAttr.Rows)
					{
						if (dr1.get("GroupID") == null)
						{
							dr1.set("GroupID", 0);
						}

						if (dr1.get("GroupID").toString().equals(String.valueOf(oldID)))
						{
							dr1.set("GroupID", gf.OID);
						}
					}
				}

				if (myDTAth != null && myDTAth.Columns.Contains("GroupID"))
				{
					// 附件。
					for (DataRow dr1 : myDTAth.Rows)
					{
						if (dr1.get("GroupID") == null)
						{
							dr1.set("GroupID", 0);
						}

						if (dr1.get("GroupID").toString().equals(String.valueOf(oldID)))
						{
							dr1.set("GroupID", gf.OID);
						}
					}
				}

				if (myDTDtl != null && myDTDtl.Columns.Contains("GroupID"))
				{
					// 从表。
					for (DataRow dr1 : myDTDtl.Rows)
					{
						if (dr1.get("GroupID") == null)
						{
							dr1.set("GroupID", 0);
						}

						if (dr1.get("GroupID").toString().equals(String.valueOf(oldID)))
						{
							dr1.set("GroupID", gf.OID);
						}
					}
				}

				if (myDFrm != null && myDFrm.Columns.Contains("GroupID"))
				{
					// frm.
					for (DataRow dr1 : myDFrm.Rows)
					{
						if (dr1.get("GroupID") == null)
						{
							dr1.set("GroupID", 0);
						}

						if (dr1.get("GroupID").toString().equals(String.valueOf(oldID)))
						{
							dr1.set("GroupID", gf.OID);
						}
					}
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理OID 插入重复的问题。 Sys_GroupField ， Sys_MapAttr.

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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
									val = fl.No;
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
					//                val = fl.No;
					//                break;
					//            default:
					//                val = val.Replace("ND" + oldFlowID, "ND" + flowID);
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
									val = fl.No;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
									val = fl.No;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
									val = fl.No;
									break;
								default:
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.OID = DBAccess.GenerOID();
						cd.DirectInsert();
					}
					break;
				case "WF_BillTemplate":
					continue; //因为省掉了 打印模板的处理。
					for (DataRow dr : dt.Rows)
					{
						BillTemplate bt = new BillTemplate();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
						while (bt.IsExits)
						{
							bt.setNo(no + String.valueOf(i));
							i++;
						}

						try
						{
							Files.copy(Paths.get(info.getParent() + "\\" + no + ".rtf"), Paths.get(BP.Sys.SystemConfig.PathOfWebApp + "\\DataUser\\CyclostyleFile\\" + bt.getNo() + ".rtf"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
						}
						catch (RuntimeException ex)
						{
							// infoErr += "@恢复单据模板时出现错误：" + ex.Message + ",有可能是您在复制流程模板时没有复制同目录下的单据模板文件。";
						}
						bt.Insert();
					}
					break;
				case "WF_FrmNode": //Conds.xml。
					DBAccess.RunSQL("DELETE FROM WF_FrmNode WHERE FK_Flow='" + fl.No + "'");
					for (DataRow dr : dt.Rows)
					{
						FrmNode fn = new FrmNode();
						fn.setFK_Flow(fl.No);
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
									val = fl.No;
									break;
								default:
									break;
							}
							fn.SetValByKey(dc.ColumnName, val);
						}
						// 开始插入。
						fn.MyPK = fn.getFK_Frm() + "_" + fn.getFK_Node();
						fn.Insert();
					}
					break;
				case "WF_FindWorkerRole": //找人规则
					for (DataRow dr : dt.Rows)
					{
						FindWorkerRole en = new FindWorkerRole();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
									val = fl.No;
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
						cd.setFK_Flow(fl.No);
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
									val = fl.No;
									break;
								default:
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
									break;
							}
							cd.SetValByKey(dc.ColumnName, val);
						}

						cd.setFK_Flow(fl.No);

						//  return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.Stas.ToString();
						// ，开始插入。 
						if (cd.MyPK.Contains("Stas"))
						{
							cd.MyPK = cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.Stas.toString();
						}
						else if (cd.MyPK.Contains("Dept"))
						{
							cd.MyPK = cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.Depts.toString();
						}
						else if (cd.MyPK.Contains("Paras"))
						{
							cd.MyPK = cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.Paras.toString();
						}
						else if (cd.MyPK.Contains("Url"))
						{
							cd.MyPK = cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.Url.toString();
						}
						else if (cd.MyPK.Contains("SQL"))
						{
							cd.MyPK = cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString() + "_" + ConnDataFrom.SQL;
						}
						else
						{
							cd.MyPK = DBAccess.GenerOID().toString() + LocalDateTime.now().toString("yyMMddHHmmss");
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
						dir.setFK_Flow(fl.No);
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							ln.SetValByKey(dc.ColumnName, val);
						}
						idx++;
						ln.setFK_Flow(fl.No);
						ln.MyPK = ln.getFK_Flow() + "_" + ln.getX() + "_" + ln.getY() + "_" + idx;
						ln.DirectInsert();
					}
					break;
				case "WF_NodeDept": //FAppSets.xml。
					for (DataRow dr : dt.Rows)
					{
						NodeDept dir = new NodeDept();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
							dir.SetValByKey(dc.ColumnName, val);
						}
						dir.Insert();
					}
					break;
				case "WF_Node": //导入节点信息.
					for (DataRow dr : dt.Rows)
					{
						BP.WF.Template.NodeExt nd = new BP.WF.Template.NodeExt();
						BP.WF.Template.CC cc = new CC(); // 抄送相关的信息.
						//cc.CheckPhysicsTable();
						BP.WF.Template.FrmWorkCheck fwc = new FrmWorkCheck();

						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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

						nd.setFK_Flow(fl.No);
						nd.setFlowName(fl.Name);
						try
						{

							if (nd.getEnMap().Attrs.Contains("OfficePrintEnable"))
							{
								if (nd.GetValStringByKey("OfficePrintEnable").equals("打印"))
								{
									nd.SetValByKey("OfficePrintEnable", 0);
								}
							}

							nd.DirectInsert();

							//把抄送的信息也导入里面去.
							cc.DirectUpdate();
							fwc.setFWCVer(1);
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
						nd.setNodeID(Integer.parseInt(dr.get(NodeAttr.NodeID).toString()));
						nd.RetrieveFromDBSources();
						nd.setFK_Flow(fl.No);
						//获取表单类别
						String formType = dr.get(NodeAttr.FormType).toString();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
						nd.setFK_Flow(fl.No);
						nd.setFlowName(fl.Name);
						nd.DirectUpdate();
					}
					break;
				case "WF_NodeExt":
					for (DataRow dr : dt.Rows)
					{
						BP.WF.Template.NodeExt nd = new BP.WF.Template.NodeExt();
						nd.setNodeID(Integer.parseInt(flowID + dr.get(NodeAttr.NodeID).toString().substring(iOldFlowLength)));
						nd.RetrieveFromDBSources();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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

							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
					DBAccess.RunSQL("DELETE FROM WF_NodeStation WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + fl.No + "')");
					for (DataRow dr : dt.Rows)
					{
						NodeStation ns = new NodeStation();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
						Sys.SysEnum se = new Sys.SysEnum();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									break;
								default:
									break;
							}
							se.SetValByKey(dc.ColumnName, val);
						}
						se.MyPK = se.EnumKey + "_" + se.Lang + "_" + se.IntKey;
						if (se.IsExits)
						{
							continue;
						}
						se.Insert();
					}
					break;
				case "Sys_EnumMain": //RptEmps.xml。
					for (DataRow dr : dt.Rows)
					{
						Sys.SysEnumMain sem = new Sys.SysEnumMain();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							sem.SetValByKey(dc.ColumnName, val);
						}
						if (sem.IsExits)
						{
							continue;
						}
						sem.Insert();
					}
					break;
				case "Sys_MapAttr": //RptEmps.xml。
					for (DataRow dr : dt.Rows)
					{
						Sys.MapAttr ma = new Sys.MapAttr();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
						boolean b = ma.IsExit(Sys.MapAttrAttr.FK_MapData, ma.FK_MapData, Sys.MapAttrAttr.KeyOfEn, ma.KeyOfEn);

						ma.MyPK = ma.FK_MapData + "_" + ma.KeyOfEn;
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
						Sys.MapData md = new Sys.MapData();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + Integer.parseInt(fl.No));
							md.SetValByKey(dc.ColumnName, val);
						}
						md.Save();
					}
					break;
				case "Sys_MapDtl": //RptEmps.xml。
					for (DataRow dr : dt.Rows)
					{
						Sys.MapDtl md = new Sys.MapDtl();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
						Sys.MapExt md = new Sys.MapExt();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.MyPK = UUID.NewGuid().toString();
						// DBAccess.GenerOIDByGUID(); "LIE" + timeKey + "_" + idx;
						//if (en.IsExitGenerPK())
						//    continue;
						en.Insert();
					}
					break;
				case "Sys_FrmEle":
					idx = 0;
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmEle en = new FrmEle();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
				case "Sys_FrmImg":
					idx = 0;
					timeKey = LocalDateTime.now().toString("yyyyMMddHHmmss");
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImg en = new FrmImg();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.MyPK = UUID.NewGuid().toString();
					}
					break;
				case "Sys_FrmLab":
					idx = 0;
					timeKey = LocalDateTime.now().toString("yyyyMMddHHmmss");
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLab en = new FrmLab();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.MyPK = BP.DA.DBAccess.GenerGUID(); // "Lab" + timeKey + "_" + idx;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val);
						}
						en.MyPK = UUID.NewGuid().toString();
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.MyPK = en.FK_MapData + "_" + en.NoOfObj;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
							Object val = dr.get(dc.ColumnName) instanceof Object ? (Object)dr.get(dc.ColumnName) : null;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
				case "Sys_GroupField":
					for (DataRow dr : dt.Rows)
					{
						Sys.GroupField gf = new Sys.GroupField();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
						int oid = DBAccess.GenerOID();
						DBAccess.RunSQL("UPDATE Sys_MapAttr SET GroupID='" + oid + "' WHERE FK_MapData='" + gf.FrmID + "' AND GroupID='" + gf.OID + "'");
						gf.InsertAsOID(oid);
					}
					break;
				case "WF_CCEmp": // 抄送.
					for (DataRow dr : dt.Rows)
					{
						CCEmp ne = new CCEmp();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
							String val = dr.get(dc.ColumnName) instanceof String ? (String)dr.get(dc.ColumnName) : null;
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
				default:
					// infoErr += "Error:" + dt.TableName;
					break;
					//    throw new Exception("@unhandle named " + dt.TableName);
			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理数据完整性。
		DBAccess.RunSQL("UPDATE WF_Cond SET FK_Node=NodeID WHERE FK_Node=0");
		DBAccess.RunSQL("UPDATE WF_Cond SET ToNodeID=NodeID WHERE ToNodeID=0");

		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE NodeID NOT IN (SELECT NodeID FROM WF_Node)");
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE ToNodeID NOT IN (SELECT NodeID FROM WF_Node) ");
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE FK_Node NOT IN (SELECT NodeID FROM WF_Node) AND FK_Node > 0");

		//处理分组错误.
		Nodes nds = new Nodes(fl.No);
		for (Node nd : nds)
		{
			MapFrmFool cols = new MapFrmFool("ND" + nd.getNodeID());
			cols.DoCheckFixFrmForUpdateVer();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion


		if (infoErr.equals(""))
		{
			infoTable = "";
			return fl; // "完全成功。";
		}

		infoErr = "@执行期间出现如下非致命的错误：\t\r" + infoErr + "@ " + infoTable;
		throw new RuntimeException(infoErr);
	}

	public final Node DoNewNode(int x, int y)
	{
		return DoNewNode(x, y, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public Node DoNewNode(int x, int y, string icon = null)
	public final Node DoNewNode(int x, int y, String icon)
	{
		Node nd = new Node();
		int idx = this.getHisNodes().size();
		if (idx == 0)
		{
			idx++;
		}

		while (true)
		{
			String strID = this.No + tangible.StringHelper.padLeft(String.valueOf(idx), 2, '0');
			nd.setNodeID(Integer.parseInt(strID));
			if (nd.IsExits == false)
			{
				break;
			}
			idx++;
		}

		nd.setHisNodeWorkType(NodeWorkType.Work);
		nd.setName("New Node " + idx);
		nd.setHisNodePosType(NodePosType.Mid);
		nd.setFK_Flow(this.No);
		nd.setFlowName(this.Name);
		nd.setX(x);
		nd.setY(y);
		nd.setICON(icon);
		nd.setStep(idx);

		//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
		nd.setCondModel(CondModel.ByLineCond); //默认的发送方向.
		nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
		nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.

		//为创建节点设置默认值 @于庆海. 
		String file = SystemConfig.PathOfDataUser + "\\XML\\DefaultNewNodeAttr.xml";
		if ((new File(file)).isFile() == true)
		{
			DataSet ds = new DataSet();
			ds.ReadXml(file);

			DataTable dt = ds.Tables[0];
			for (DataColumn dc : dt.Columns)
			{
				nd.SetValByKey(dc.ColumnName, dt.Rows[0][dc.ColumnName]);
			}
		}
		nd.setFWCVer(1);
		nd.Insert();
		nd.CreateMap();

		//通用的人员选择器.
		BP.WF.Template.Selector select = new BP.WF.Template.Selector(nd.getNodeID());
		select.setSelectorModel(SelectorModel.GenerUserSelecter);
		select.Update();

		//设置审核组件的高度
		DBAccess.RunSQL("UPDATE WF_Node SET FWC_H=300,FTC_H=300 WHERE NodeID='" + nd.getNodeID() + "'");

		CreatePushMsg(nd);

		return nd;
	}
	public final void CreatePushMsg(Node nd)
	{
		/*创建发送短消息,为默认的消息.*/
		BP.WF.Template.PushMsg pm = new BP.WF.Template.PushMsg();
		int i = pm.Retrieve(PushMsgAttr.FK_Event, EventListOfNode.SendSuccess, PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Flow, nd.getFK_Flow());
		if (i == 0)
		{
			pm.setFK_Event(EventListOfNode.SendSuccess);
			pm.setFK_Node(nd.getNodeID());
			pm.setFK_Flow(this.No);

			pm.setSMSPushWay(1); // 发送短消息.
			pm.setMailPushWay(0); //不发送邮件消息.
			pm.MyPK = DBAccess.GenerGUID();
			pm.Insert();
		}

		//设置退回消息提醒.
		i = pm.Retrieve(PushMsgAttr.FK_Event, EventListOfNode.ReturnAfter, PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Flow, nd.getFK_Flow());
		if (i == 0)
		{
			pm.setFK_Event(EventListOfNode.ReturnAfter);
			pm.setFK_Node(nd.getNodeID());
			pm.setFK_Flow(this.No);

			pm.setSMSPushWay(1); // 发送短消息.
			pm.setMailPushWay(0); //不发送邮件消息.
			pm.MyPK = DBAccess.GenerGUID();
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
	*/
	public final String DoNewFlow(String flowSort, String flowName, DataStoreModel model, String pTable, String FlowMark)
	{
		try
		{
			//检查参数的完整性.
			if (DataType.IsNullOrEmpty(pTable) == false && pTable.length() >= 1)
			{
				String c = pTable.substring(0, 1);
				if (DataType.IsNumStr(c) == true)
				{
					throw new RuntimeException("@非法的流程数据表(" + pTable + "),它会导致ccflow不能创建该表.");
				}
			}

			this.Name = flowName;
			if (tangible.StringHelper.isNullOrWhiteSpace(this.Name))
			{
				this.Name = "新建流程" + this.No; //新建流程.
			}

			this.No = this.GenerNewNoByKey(FlowAttr.No);
			this.setHisDataStoreModel(model);
			this.setPTable(pTable);
			this.setFK_FlowSort(flowSort);
			this.setFlowMark(FlowMark);

			if (DataType.IsNullOrEmpty(FlowMark) == false)
			{
				if (this.IsExit(FlowAttr.FlowMark, FlowMark))
				{
					throw new RuntimeException("@该流程标示:" + FlowMark + "已经存在于系统中.");
				}
			}

			/*给初始值*/
			//this.Paras = "@StartNodeX=10@StartNodeY=15@EndNodeX=40@EndNodeY=10";
			this.setParas("@StartNodeX=200@StartNodeY=50@EndNodeX=200@EndNodeY=350");

			this.Save();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 删除有可能存在的历史数据.
			Flow fl = new Flow(this.No);
			fl.DoDelData();
			fl.DoDelete();
			this.Save();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 删除有可能存在的历史数据.

			Node nd = new Node();
			nd.setNodeID(Integer.parseInt(this.No + "01"));
			nd.setName("Start Node"); //  "开始节点";
			nd.setStep(1);
			nd.setFK_Flow(this.No);
			nd.setFlowName(this.Name);
			nd.setHisNodePosType(NodePosType.Start);
			nd.setHisNodeWorkType(NodeWorkType.StartWork);
			nd.setX(200);
			nd.setY(150);
			nd.setNodePosType(NodePosType.Start);
			nd.setICON("前台");


			//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
			nd.setCondModel(CondModel.SendButtonSileSelect); //默认的发送方向.
			nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
			nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.
			nd.Insert();
			nd.CreateMap();

			//为开始节点增加一个删除按钮. @李国文.
			String sql = "UPDATE WF_Node SET DelEnable=1 WHERE NodeID=" + nd.getNodeID();
			BP.DA.DBAccess.RunSQL(sql);



			//nd.HisWork.CheckPhysicsTable();  去掉，检查的时候会执行.
			CreatePushMsg(nd);


			//通用的人员选择器.
			BP.WF.Template.Selector select = new BP.WF.Template.Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();

			nd = new Node();
			nd.setNodeID(Integer.parseInt(this.No + "02"));
			nd.setName("Node 2"); // "结束节点";
			nd.setStep(2);
			nd.setFK_Flow(this.No);
			nd.setFlowName(this.Name);
			nd.setHisNodePosType(NodePosType.Mid);
			nd.setHisNodeWorkType(NodeWorkType.Work);
			nd.setX(200);
			nd.setY(250);
			nd.setICON("审核");
			nd.setNodePosType(NodePosType.End);

			//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
			nd.setCondModel(CondModel.SendButtonSileSelect); //默认的发送方向.
			nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
			nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.

			//为创建节点设置默认值 @于庆海. 
			String fileNewNode = SystemConfig.PathOfDataUser + "\\XML\\DefaultNewNodeAttr.xml";
			if ((new File(fileNewNode)).isFile() == true)
			{
				DataSet ds_NodeDef = new DataSet();
				ds_NodeDef.ReadXml(fileNewNode);

				DataTable dt = ds_NodeDef.Tables[0];
				for (DataColumn dc : dt.Columns)
				{
					nd.SetValByKey(dc.ColumnName, dt.Rows[0][dc.ColumnName]);
				}
			}
			nd.Insert();
			nd.CreateMap();
			//nd.HisWork.CheckPhysicsTable(); //去掉，检查的时候会执行.


			CreatePushMsg(nd);

			//通用的人员选择器.
			select = new BP.WF.Template.Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();

			BP.Sys.MapData md = new BP.Sys.MapData();
			md.No = "ND" + Integer.parseInt(this.No) + "Rpt";
			md.Name = this.Name;
			md.Save();

			// 装载模版.
			String file = BP.Sys.SystemConfig.PathOfDataUser + "XML\\TempleteSheetOfStartNode.xml";
			if ((new File(file)).isFile() == false)
			{
				throw new RuntimeException("@开始节点表单模版丢失" + file);
			}

			/*如果存在开始节点表单模版*/
			DataSet ds = new DataSet();
			ds.ReadXml(file);

			String nodeID = "ND" + Integer.parseInt(this.No + "01");
			BP.Sys.MapData.ImpMapData(nodeID, ds);

			//创建track.
			Track.CreateOrRepairTrackTable(this.No);


			return this.No;
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
	 检查报表
	*/
	public final void CheckRpt()
	{
		this.DoCheck_CheckRpt(this.getHisNodes());
	}
	/** 
	 更新之前做检查
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdate()
	{
		this.setVer(BP.DA.DataType.CurrentDataTimess);
		Node.CheckFlow(this);
		return super.beforeUpdate();
	}
	/** 
	 更新版本号
	*/
	public static void UpdateVer(String flowNo)
	{
		String sql = "UPDATE WF_Flow SET Ver='" + BP.DA.DataType.CurrentDataTimess + "' WHERE No='" + flowNo + "'";
		DBAccess.RunSQL(sql);
	}
	public final String DoDelete()
	{
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

		//删除流程数据.
		this.DoDelData();

		String sql = "";
		//sql = " DELETE FROM WF_chofflow WHERE FK_Flow='" + this.No + "'";
		sql += "@ DELETE FROM WF_GenerWorkerlist WHERE FK_Flow='" + this.No + "'";
		sql += "@ DELETE FROM  WF_GenerWorkFlow WHERE FK_Flow='" + this.No + "'";
		sql += "@ DELETE FROM  WF_Cond WHERE FK_Flow='" + this.No + "'";


		//删除消息配置.
		sql += "@ DELETE FROM WF_PushMsg WHERE FK_Flow='" + this.No + "'";

		// 删除岗位节点。
		sql += "@ DELETE FROM WF_NodeStation WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		// 删除方向。
		sql += "@ DELETE FROM WF_Direction WHERE FK_Flow='" + this.No + "'";

		//删除节点绑定信息.
		sql += "@ DELETE FROM WF_FrmNode WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		sql += "@ DELETE FROM WF_NodeEmp  WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		sql += "@ DELETE FROM WF_CCEmp WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		sql += "@ DELETE FROM WF_CCDept WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		sql += "@ DELETE FROM WF_CCStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		sql += "@ DELETE FROM WF_NodeReturn WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		sql += "@ DELETE FROM WF_NodeDept WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		sql += "@ DELETE FROM WF_NodeStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		sql += "@ DELETE FROM WF_NodeEmp WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		sql += "@ DELETE FROM WF_NodeToolbar WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		sql += "@ DELETE FROM WF_SelectAccper WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		//sql += "@ DELETE FROM WF_TurnTo WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		//删除侦听.
		// sql += "@ DELETE FROM WF_Listen WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		// 删除d2d数据.
		//  sql += "@GO DELETE WF_M2M WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		//// 删除配置.
		//sql += "@ DELETE FROM WF_FAppSet WHERE NodeID IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";


		//// 外部程序设置
		//sql += "@ DELETE FROM WF_FAppSet WHERE  NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";

		//删除单据.
		sql += "@ DELETE FROM WF_BillTemplate WHERE  NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.No + "')";
		//删除权限控制.
		sql += "@ DELETE FROM Sys_FrmSln WHERE FK_Flow='" + this.No + "'";
		//考核表
		sql += "@ DELETE FROM WF_CH WHERE FK_Flow='" + this.No + "'";
		//删除抄送
		sql += "@ DELETE FROM WF_CCList WHERE FK_Flow='" + this.No + "'";
		Nodes nds = new Nodes(this.No);
		for (Node nd : nds)
		{
			// 删除节点所有相关的东西.
			nd.Delete();
		}

		sql += "@ DELETE  FROM WF_Node WHERE FK_Flow='" + this.No + "'";
		sql += "@ DELETE  FROM WF_LabNote WHERE FK_Flow='" + this.No + "'";

		//删除分组信息
		sql += "@ DELETE FROM Sys_GroupField WHERE FrmID NOT IN(SELECT NO FROM Sys_MapData)";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 删除流程报表,删除轨迹
		MapData md = new MapData();
		md.No = "ND" + Integer.parseInt(this.No) + "Rpt";
		md.Delete();

		//删除视图.
		if (DBAccess.IsExitsObject("V_" + this.No))
		{
			DBAccess.RunSQL("DROP VIEW V_" + this.No);
		}

		//删除轨迹.
		if (DBAccess.IsExitsObject("ND" + Integer.parseInt(this.No) + "Track") == true)
		{
			DBAccess.RunSQL("DROP TABLE ND" + Integer.parseInt(this.No) + "Track ");
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 删除流程报表,删除轨迹.

		// 执行录制的sql scripts.
		DBAccess.RunSQLs(sql);

		//清空WF_Emp中的StartFlow 
		DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");

		this.Delete(); //删除需要移除缓存.

		return "执行成功.";
	}

	/** 
	 向上移动
	*/
	public final void DoUp()
	{
		this.DoOrderUp(FlowAttr.FK_FlowSort, this.getFK_FlowSort(), FlowAttr.Idx);
	}
	/** 
	 向下移动
	*/
	public final void DoDown()
	{
		this.DoOrderDown(FlowAttr.FK_FlowSort, this.getFK_FlowSort(), FlowAttr.Idx);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 版本管理.
	/** 
	 创建新版本.
	 
	 @return 
	*/
	public final String VerCreateNew()
	{
		try
		{
			//生成模版.
			String file = GenerFlowXmlTemplete();

			Flow newFlow = Flow.DoLoadFlowTemplate(this.getFK_FlowSort(), file, ImpFlowTempleteModel.AsNewFlow);

			newFlow.setPTable(this.getPTable());
			newFlow.setFK_FlowSort(""); //不能显示流程树上.
			newFlow.Name = this.Name;
			newFlow.setVer(DataType.CurrentDataTime);
			newFlow.setIsCanStart(false); //不能发起.
			newFlow.DirectUpdate();
			return newFlow.No;
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
	*/
	public final String VerSetCurrentVer()
	{
		String sql = "SELECT FK_FlowSort,No FROM WF_Flow WHERE PTable='" + this.getPTable() + "' AND FK_FlowSort!='' ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			return "err@没有找到主版本,请联系管理员.";
		}
		String flowSort = dt.Rows[0][0].toString();
		String oldFlowNo = dt.Rows[0][1].toString();
		sql = "UPDATE WF_Flow SET FK_FlowSort='',IsCanStart=0 WHERE PTable='" + this.getPTable() + "' ";
		DBAccess.RunSQL(sql);

		sql = "UPDATE WF_Flow SET FK_FlowSort='" + flowSort + "', IsCanStart=1 WHERE No='" + this.No + "' ";
		DBAccess.RunSQL(sql);

		//清缓存
		BP.DA.Cash2019.DeleteRow("BP.WF.Flow", oldFlowNo);
		BP.DA.Cash2019.DeleteRow("BP.WF.Flow", this.No);
		Flow flow = new Flow(oldFlowNo);
		flow = new Flow(this.No);


		return "info@设置成功.";
	}
	/** 
	 获得版本列表.
	 
	 @return 
	*/
	public final String VerGenerVerList()
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
		// string sql = "SELECT No,Name,Ver,FK_FlowSort FROM WF_Flow WHERE PTable='"+this.PTable+"'";
		String sql = "SELECT No  FROM WF_Flow WHERE PTable='" + this.getPTable() + "'";
		Flows fls = new Flows();
		fls.RetrieveInSQL(sql);

		for (Flow item : fls)
		{
			DataRow dr = dt.NewRow();
			dr.set("Ver", item.getVer());
			dr.set("No", item.No);
			dr.set("Name", item.Name);

			if (DataType.IsNullOrEmpty(item.getFK_FlowSort()) == true)
			{
				dr.set("IsRel", "0");
			}
			else
			{
				dr.set("IsRel", "1");
			}

			dr.set("NumOfRuning", DBAccess.RunSQLReturnValInt("SELECT COUNT(WORKID) FROM WF_GenerWorkFlow WHERE FK_FLOW='" + item.No + "' AND WFState=2"));
			dr.set("NumOfOK", DBAccess.RunSQLReturnValInt("SELECT COUNT(WORKID) FROM WF_GenerWorkFlow WHERE FK_FLOW='" + item.No + "' AND WFState=3"));
			dt.Rows.Add(dr);
		}

		return BP.Tools.Json.ToJson(dt);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 版本管理.

}