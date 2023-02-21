package bp.wf.dts;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.sys.*;
import bp.wf.*;

/**
 Method 的摘要说明
 */
public class AutoRunStratFlows extends Method
{
	/**
	 不带有参数的方法
	 */
	public AutoRunStratFlows()throws Exception
	{
		this.Title = "自动启动流程";
		this.Help = "在流程属性上配置的信息,自动发起流程,按照时间规则....";
		this.GroupName = "流程自动执行定时任务";

	}
	/**
	 设置执行变量

	 @return
	 */
	@Override
	public void Init()  {
	}
	/**
	 当前的操纵员是否可以执行这个方法
	 */
	@Override
	public boolean getIsCanDo() {
		return true;
	}
	/**
	 执行

	 @return 返回执行结果
	  * @throws Exception
	 */
	@Override
	public Object Do() throws Exception
	{
		String no = "admin";
		if(DataType.IsNullOrEmpty(WebUser.getNo())==false){
			no = WebUser.getNo();
		}
		bp.port.Emp empadmin = new bp.port.Emp(no);
		WebUser.SignInOfGener(empadmin);
		bp.wf.Flows fls = new Flows();
		fls.RetrieveAll();


		///自动启动流程
		for (bp.wf.Flow fl : fls.ToJavaList())
		{
			if (fl.getHisFlowRunWay() == bp.wf.FlowRunWay.HandWork)
			{
				continue;
			}

			if (DataType.getCurrentDateByFormart("HH:mm").equals(fl.Tag))
			{
				continue;
			}

			if (fl.getRunObj() == null || fl.getRunObj().equals(""))
			{
				String msg = "您设置自动运行流程错误，没有设置流程内容，流程编号：" + fl.getNo() + ",流程名称:" + fl.getName();
				bp.da.Log.DebugWriteError(msg);
				continue;
			}


			///判断当前时间是否可以运行它。
			String nowStr = DataType.getCurrentDateByFormart("yyyy-MM-dd,HH:mm");
			String[] strs = fl.getRunObj().split("[@]", -1); //破开时间串。
			boolean IsCanRun = false;
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str))
				{
					continue;
				}
				if (nowStr.contains(str))
				{
					IsCanRun = true;
				}
			}

			if (IsCanRun == false)
			{
				continue;
			}

			// 设置时间.
			fl.Tag = DataType.getCurrentDateByFormart("HH:mm");

			/// 判断当前时间是否可以运行它。

			// 以此用户进入.
			switch (fl.getHisFlowRunWay())
			{
				case SpecEmp: //指定人员按时运行。
					String RunObj = fl.getRunObj();
					String fk_emp = RunObj.substring(0, RunObj.indexOf('@'));

					bp.port.Emp emp = new bp.port.Emp();
					emp.setNo(fk_emp);
					if (emp.RetrieveFromDBSources() == 0)
					{
						bp.da.Log.DebugWriteError("启动自动启动流程错误：发起人(" + fk_emp + ")不存在。");
						continue;
					}

					try
					{
						//让 userNo 登录.
						bp.wf.Dev2Interface.Port_Login(emp.getNo());

						//创建空白工作, 发起开始节点.
						long workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

						//执行发送.
						SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);

						//string info_send= BP.WF.Dev2Interface.Node_StartWork(fl.getNo(),);
						bp.da.Log.DefaultLogWriteLineInfo("流程:" + fl.getNo() + fl.getName() + "的定时任务\t\n -------------- \t\n" + objs.ToMsgOfText());

					}
					catch (RuntimeException ex)
					{
						bp.da.Log.DebugWriteError("流程:" + fl.getNo() + fl.getName() + "自动发起错误:\t\n -------------- \t\n" + ex.getMessage());
					}
					continue;
				case SelectSQLModel: //按数据集合驱动的模式执行。
					this.SelectSQLModel(fl);
					continue;
				case WF_TaskTableInsertModel: //按数据集合驱动的模式执行。
					this.WF_TaskTableInsertModel(fl);
					continue;
				default:
					break;
			}
		}

		empadmin = new bp.port.Emp(no);
		WebUser.SignInOfGener(empadmin);


		/// 发送消息

		return "调度完成..";
	}
	/**
	 触发模式

	 param fl
	 */
	public final void WF_TaskTableInsertModel(bp.wf.Flow fl)
	{
	}

	public final void SelectSQLModel(bp.wf.Flow fl) throws Exception
	{

		///读取数据.
		bp.sys.MapExt me = new MapExt();
		me.setMyPK(MapExtXmlList.StartFlow + "_ND" + Integer.parseInt(fl.getNo()) + "01");
		int i = me.RetrieveFromDBSources();
		if (i == 0)
		{
			return;
		}

		if (DataType.IsNullOrEmpty(me.getTag()))
		{
			bp.da.Log.DefaultLogWriteLineError("没有为流程(" + fl.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return;
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

		/// 读取数据.


		///检查数据源是否正确.
		String errMsg = "";
		// 获取主表数据.
		DataTable dtMain = DBAccess.RunSQLReturnTable(me.getTag());
		if (dtMain.Rows.size() == 0)
		{
			bp.da.Log.DefaultLogWriteLineError("流程(" + fl.getName() + ")此时无任务.");
			return;
		}

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
			bp.da.Log.DefaultLogWriteLineError("流程(" + fl.getName() + ")的开始节点设置发起数据,不完整." + errMsg);
			return;
		}

		/// 检查数据源是否正确.


		///处理流程发起.
		String nodeTable = "ND" + Integer.parseInt(fl.getNo()) + "01";
		int idx = 0;
		for (DataRow dr : dtMain.Rows)
		{
			idx++;

			String mainPK = dr.getValue("MainPK").toString();
			String sql = "SELECT OID FROM " + nodeTable + " WHERE MainPK='" + mainPK + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0)
			{
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
					bp.da.Log.DefaultLogWriteLineInfo("@数据驱动方式发起流程(" + fl.getName() + ")设置的发起人员:" + emp.getNo() + "不存在。");
					continue;
				}
				WebUser.SignInOfGener(emp);
			}


			/// 给值.
			//System.Collections.Hashtable ht = new Hashtable();

			Work wk = fl.NewWork();

			String err = "";

			///检查用户拼写的sql是否正确？
			for (DataColumn dc : dtMain.Columns)
			{
				String f = dc.ColumnName.toLowerCase();
				switch (f)
				{
					case "starter":
					case "mainpk":
					case "refmainpk":
					case "tonode":
						break;
					default:
						boolean isHave = false;
						for (Attr attr : wk.getEnMap().getAttrs())
						{
							if (attr.getKey().toLowerCase().equals(f))
							{
								isHave = true;
								break;
							}
						}
						if (isHave == false)
						{
							err += " " + f + " ";
						}
						break;
				}
			}
			if (DataType.IsNullOrEmpty(err) == false)
			{
				throw new RuntimeException("您设置的字段:" + err + "不存在开始节点的表单中，设置的sql:" + me.getTag());
			}


			/// 检查用户拼写的sql是否正确？

			for (DataColumn dc : dtMain.Columns)
			{
				wk.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
			}

			if (ds.Tables.size() != 0)
			{
				MapDtls dtls = new MapDtls(nodeTable);
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
							dtlEn.setOID( 0);
							dtlEn.Insert();
						}
					}
				}
			}

			///  给值.


			int toNodeID = 0;
			try
			{
				toNodeID = Integer.parseInt(dr.getValue("ToNode").toString());
			}
			catch (java.lang.Exception e)
			{
				/*有可能在4.5以前的版本中没有tonode这个约定.*/
			}

			// 处理发送信息.
			//  Node nd =new Node();
			String msg = "";
			try
			{
				if (toNodeID == 0)
				{
					WorkNode wn = new WorkNode(wk, fl.getHisStartNode());
					msg = wn.NodeSend().ToMsgOfText();
				}

				if (toNodeID == fl.getStartNodeID())
				{
					/* 发起后让它停留在开始节点上，就是为开始节点创建一个待办。*/
					long workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo(), null, null, WebUser.getNo(), null);
					if (workID != wk.getOID())
					{
						throw new RuntimeException("@异常信息:不应该不一致的workid.");
					}
					else
					{
						wk.Update();
					}
					msg = "已经为(" + WebUser.getNo() + ") 创建了开始工作节点. ";
				}

				bp.da.Log.DefaultLogWriteLineInfo(msg);
			}
			catch (RuntimeException ex)
			{
				bp.da.Log.DefaultLogWriteLineWarning("@" + fl.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "发起时出现错误.\r\n" + ex.getMessage());
			}
		}

		/// 处理流程发起.
	}
}