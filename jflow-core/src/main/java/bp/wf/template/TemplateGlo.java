package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.tools.DateUtils;
import bp.web.*;
import bp.wf.*;
import bp.en.*;
import bp.wf.Glo;
import bp.difference.*;
import bp.wf.template.sflow.*;
import bp.wf.template.ccen.*;
import bp.wf.template.frm.*;

import java.io.*;
import java.nio.file.*;
import java.util.Date;

/** 
 流程模版的操作
*/
public class TemplateGlo
{
	/** 
	 装载流程模板
	 
	 param fk_flowSort 流程类别
	 param path 流程名称
	 @return 
	*/

	public static Flow LoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model, String SpecialFlowNo) throws Exception {
		return LoadFlowTemplate(fk_flowSort, path, model, SpecialFlowNo, null);
	}

	public static Flow LoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model) throws Exception {
		return LoadFlowTemplate(fk_flowSort, path, model, "", null);
	}

	public static Flow LoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model, String SpecialFlowNo, String flowName) throws Exception {
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


		if (ds.contains("WF_Flow") == false)
		{
			throw new RuntimeException("导入错误，非流程模版文件" + path + "。");
		}

		DataTable dtFlow = ds.GetTableByName("WF_Flow");
		Flow fl = new Flow();
		String oldFlowNo = dtFlow.Rows.get(0).getValue("No").toString();
		String oldFlowName = dtFlow.Rows.get(0).getValue("Name").toString();

		int oldFlowID = Integer.parseInt(oldFlowNo);
		int iOldFlowLength = String.valueOf(oldFlowID).length();
		String timeKey = DateUtils.format(new Date(),"yyMMddhhmmss");


			///#region 根据不同的流程模式，设置生成不同的流程编号.
		switch (model)
		{
			case AsNewFlow: //做为一个新流程.
				fl.setNo(fl.getGenerNewNo());
				fl.DoDelData();
				fl.DoDelete(); //删除可能存在的垃圾.
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

			///#endregion 根据不同的流程模式，设置生成不同的流程编号.

		// string timeKey = fl.No;
		int idx = 0;
		String infoErr = "";
		String infoTable = "";
		int flowID = Integer.parseInt(fl.getNo());


			///#region 处理流程表数据
		for (DataColumn dc : dtFlow.Columns)
		{
			String val = dtFlow.Rows.get(0).getValue(dc.ColumnName) instanceof String ? (String)dtFlow.Rows.get(0).getValue(dc.ColumnName) : null;
			switch (dc.ColumnName.toLowerCase())
			{
				case "no":
				case "fk_flowsort":
					continue;
				case "name":
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
		if (DataType.IsNullOrEmpty(flowName) == false)
		{
			fl.setName(flowName);
		}
		fl.Update();
		//判断该流程是否是公文流程，存在BuessFields、FlowBuessType、FK_DocType=01
		Attrs attrs = fl.getEnMap().getAttrs();
		if (attrs.contains("FlowBuessType") == true)
		{
			DBAccess.RunSQL("UPDATE WF_Flow Set BuessFields='" + fl.GetParaString("BuessFields") + "', FlowBuessType=" + fl.GetParaInt("FlowBuessType", 0) + " ,FK_DocType='" + fl.GetParaString("FK_DocType") + "'");
		}


			///#endregion 处理流程表数据


			///#region 处理OID 插入重复的问题 Sys_GroupField, Sys_MapAttr.
		DataTable mydtGF = ds.GetTableByName("Sys_GroupField");
		DataTable myDTAttr = ds.GetTableByName("Sys_MapAttr");
		DataTable myDTAth = ds.GetTableByName("Sys_FrmAttachment");
		DataTable myDTDtl = ds.GetTableByName("Sys_MapDtl");
		DataTable myDFrm = ds.GetTableByName("Sys_MapFrame");

		for (DataRow dr : mydtGF.Rows)
		{
			GroupField gf = new GroupField();
			for (DataColumn dc : mydtGF.Columns)
			{
				String val = dr.getValue(dc.ColumnName)!=null? dr.getValue(dc.ColumnName).toString(): null;
				if (val == null)
					continue;
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
			int oldID = Math.toIntExact(gf.getOID());
			gf.setOID(DBAccess.GenerOID());
			gf.DirectInsert();
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
						SubFlow yg = new SubFlow();
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
				case "Sys_FrmPrintTemplate":
					for (DataRow dr : dt.Rows)
					{
						FrmPrintTemplate bt = new FrmPrintTemplate();
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
										throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点Sys_FrmPrintTemplate下FK_Node值错误:" + val);
									}
									val = flowID + val.substring(iOldFlowLength);
									break;
								default:
									break;
							}
							bt.SetValByKey(dc.ColumnName, val);
						}

							bt.setMyPK(DBAccess.GenerGUID(0, null, null));

						try
						{
							Files.copy(Paths.get(info.getParent() + "/" + bt.getMyPK() + ".rtf"), Paths.get(SystemConfig.getPathOfWebApp() + "/DataUser/CyclostyleFile/" + bt.getMyPK() + ".rtf"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
						}
						catch (RuntimeException ex)
						{
							// infoErr += "@恢复单据模板时出现错误：" + ex.Message + ",有可能是您在复制流程模板时没有复制同目录下的单据模板文件。";
						}
						bt.Insert();
					}
					break;
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
						fn.setMyPK(fn.getFKFrm() + "_" + fn.getFK_Node());
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

						cd.setFK_Flow(fl.getNo()); //@hongyan.
						cd.setMyPK(DBAccess.GenerGUID(0, null, null));
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
						ln.setMyPK(DBAccess.GenerGUID(0, null, null));
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
							//如果部门不属于本组织的，就要删除.  
							if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
							{
								bp.wf.port.admin2group.Dept dept = new bp.wf.port.admin2group.Dept(dp.getFK_Dept());
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
						NodeExt nd = new NodeExt();
						CC cc = new CC(); //抄送相关的信息.
						NodeWorkCheck fwc = new NodeWorkCheck();

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
								case "atpara":
									if(DataType.IsNullOrEmpty(val)==false){
										AtPara para  = new AtPara(val);
										String sf = para.GetValStrByKey("ShenFenVal");
										if(DataType.IsNullOrEmpty(sf)==false){
											sf = sf.replace(String.valueOf(oldFlowID),String.valueOf(flowID));
											para.SetVal("ShenFenVal",sf);
											val = para.GenerAtParaStrs();
										}
									}
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

							if (nd.getEnMap().getAttrs().contains("OfficePrintEnable"))
							{
								if (nd.GetValStringByKey("OfficePrintEnable").equals("打印"))
								{
									nd.SetValByKey("OfficePrintEnable", 0);
								}
							}

							try
							{
								nd.DirectInsert();
							}
							catch (RuntimeException ex)
							{
								// var i2 = 11; 
							}

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
								case "atpara":
									if(DataType.IsNullOrEmpty(val)==false){
										AtPara para  = new AtPara(val);
										String sf = para.GetValStrByKey("ShenFenVal");
										if(DataType.IsNullOrEmpty(sf)==false){
											sf = sf.replace(String.valueOf(oldFlowID),String.valueOf(flowID));
											para.SetVal("ShenFenVal",sf);
											val = para.GenerAtParaStrs();
										}
									}
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

					//更新 GroupStaNDs . HisToND 
					String sql = "UPDATE WF_Node SET GroupStaNDs=Replace(GroupStaNDs,'@" + Integer.parseInt(oldFlowNo) + "','@" + Integer.parseInt(fl.getNo()) + "'), HisToNDs=Replace(HisToNDs,'@" + Integer.parseInt(oldFlowNo) + "','@" + Integer.parseInt(fl.getNo()) + "') WHERE FK_Flow='" + fl.getNo() + "'";
					DBAccess.RunSQL(sql);
					break;
				case "WF_NodeExt":
					for (DataRow dr : dt.Rows)
					{
						NodeExt nd = new NodeExt();
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
								case "atpara":
									if(DataType.IsNullOrEmpty(val)==false){
										AtPara para  = new AtPara(val);
										String sf = para.GetValStrByKey("ShenFenVal");
										if(DataType.IsNullOrEmpty(sf)==false){
											sf = sf.replace(String.valueOf(oldFlowID),String.valueOf(flowID));
											para.SetVal("ShenFenVal",sf);
											val = para.GenerAtParaStrs();
										}
									}
									break;
								default:
									break;
							}
							nd.SetValByKey(dc.ColumnName, val);
						}
						nd.setFK_Flow(fl.getNo());
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
				case "Sys_Enums":
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
						// se.setMyPK(se.EnumKey + "_" + se.Lang + "_" + se.getIntKey();

						//设置orgNo.
						se.setOrgNo(WebUser.getOrgNo());
						se.ResetPK();

						if (se.getIsExits() == true)
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
								DataType.WriteFile(filePath, htmlCode);
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
				case "Sys_FrmImg":
					idx = 0;
					timeKey = DateUtils.format(new Date(),"yyMMddhhmmss");
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

						//设置主键.
						en.setMyPK(en.getFK_MapData() + "_" + en.getKeyOfEn());
						en.Save(); //执行保存.
					}
					break;
				case "Sys_FrmImgAth": //图片附件.
					idx = 0;
					timeKey = DateUtils.format(new Date(),"yyMMddhhmmss");
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImgAth en = new FrmImgAth();
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
						en.setMyPK(en.getFK_MapData() + "_" + en.getCtrlID());
						en.Save();
						//  en.setMyPK(Guid.NewGuid().ToString());
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

							switch (dc.ColumnName.toLowerCase())
							{
								case "fk_node":
									if (val.equals("0") == false)
									{
										if (val.length() < iOldFlowLength)
										{
											//节点编号长度小于流程编号长度则为异常数据，异常数据不进行处理
											throw new RuntimeException("@导入模板名称：" + oldFlowName + "；节点Sys_FrmEvent下FK_Node值错误:" + val);
										}
										val = flowID + val.substring(iOldFlowLength);
									}
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

						//解决保存错误问题. 
						en.setMyPK(DBAccess.GenerGUID(0, null, null));
						en.Insert();
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
				case "Sys_GroupField":
					/*for (DataRow dr : dt.Rows)
					{
						GroupField gf = new GroupField();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName)!=null? dr.getValue(dc.ColumnName).toString(): null;
							if (val == null)
								continue;
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
						if(gf.getCtrlID().contains("_AthMDtl")==true)
							continue;
						gf.InsertAsOID(gf.getOID());
					}*/
					break;
				case "WF_NodeCC":
					for (DataRow dr : dt.Rows)
					{
						CC cc = new CC();
						cc.setNodeID(Integer.parseInt(flowID + dr.getValue(NodeAttr.NodeID).toString().substring(iOldFlowLength)));
						cc.RetrieveFromDBSources();
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

							cc.SetValByKey(dc.ColumnName, val);
						}
						cc.SetValByKey("FK_Flow", fl.getNo());
						cc.DirectUpdate();
					}
					break;
				case "WF_CCEmp": // 抄送.
					for (DataRow dr : dt.Rows)
					{
						CCEmp ne = new CCEmp();
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
				default:
					// infoErr += "Error:" + dt.TableName;
					break;
					//    throw new Exception("@unhandle named " + dt.TableName);
			}
		}


			///#region 处理数据完整性。


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

			///#endregion

		//处理OrgNo 的导入问题.
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			fl.RetrieveFromDBSources();
			fl.setOrgNo(WebUser.getOrgNo());
			fl.DirectUpdate();
		}




		if (infoErr.equals(""))
		{
			infoTable = "";


			//写入日志.
			bp.sys.base.Glo.WriteUserLog("导入流程模板：" + fl.getName() + " - " + fl.getNo(), "通用操作");

			//创建track.
			Track.CreateOrRepairTrackTable(fl.getNo());
			return fl; // "完全成功。";
		}

		infoErr = "@执行期间出现如下非致命的错误：\t\r" + infoErr + "@ " + infoTable;
		throw new RuntimeException(infoErr);
	}


	public static Node NewNode(String flowNo, int x, int y) throws Exception {
		return NewNode(flowNo, x, y, null);
	}

//ORIGINAL LINE: public static Node NewNode(string flowNo, int x, int y, string icon = null)
	public static Node NewNode(String flowNo, int x, int y, String icon) throws Exception {
		Flow flow = new Flow(flowNo);

		Node nd = new Node();
		int idx = DBAccess.RunSQLReturnValInt("SELECT COUNT(NodeID) FROM WF_Node WHERE FK_Flow='" + flowNo + "'", 0);
		if (idx == 0)
		{
			idx++;
		}

		int nodeID = 0;
		//设置节点ID.
		while (true)
		{
			String strID = flowNo + StringHelper.padLeft(String.valueOf(idx), 2, '0');
			nd.setNodeID(Integer.parseInt(strID));
			if (nd.getIsExits() == false)
			{
				break;
			}
			idx++;
		}

		if (nd.getNodeID() > Integer.parseInt(flowNo + "99"))
		{
			throw new RuntimeException("流程最大节点编号不可以超过100");
		}

		nodeID = nd.getNodeID();

		//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
		nd.setCondModel(DirCondModel.ByDDLSelected); //默认的发送方向.
		nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
		nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.


		//如果是极简模式.
		if (flow.getFlowDevModel() == FlowDevModel.JiJian)
		{
			nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.
			nd.setNodeFrmID("ND" + Integer.parseInt(flow.getNo()) + "01");
			nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);
			nd.DirectUpdate();
			FrmNode fn = new FrmNode();
			fn.setFKFrm(nd.getNodeFrmID());
			fn.setEnableFWC(FrmWorkCheckSta.Enable);
			fn.setFK_Node(nd.getNodeID());
			fn.setFK_Flow(flowNo);
			fn.setFrmSln(FrmSln.Readonly);
			fn.setMyPK(fn.getFKFrm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
			//执行保存.
			fn.Save();
			MapData md = new MapData(nd.getNodeFrmID());
			md.setHisFrmType( FrmType.FoolForm);
			md.Update();
		}

		//如果是累加.
		if (flow.getFlowDevModel() == FlowDevModel.FoolTruck)
		{
			nd.setFormType(NodeFormType.FoolTruck); //设置为傻瓜表单.
			nd.setNodeFrmID("ND" + nodeID);
			nd.setFrmWorkCheckSta(FrmWorkCheckSta.Disable);
			nd.DirectUpdate();


		}

		//如果是绑定表单库的表单
		if (flow.getFlowDevModel() == FlowDevModel.RefOneFrmTree)
		{
			nd.setFormType(NodeFormType.RefOneFrmTree); //设置为傻瓜表单.
			nd.setNodeFrmID(flow.getFrmUrl());
			nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);
			nd.DirectUpdate();
			FrmNode fn = new FrmNode();
			fn.setFKFrm(nd.getNodeFrmID());
			fn.setEnableFWC(FrmWorkCheckSta.Enable);
			fn.setFK_Node(nd.getNodeID());
			fn.setFK_Flow(flowNo);
			fn.setFrmSln(FrmSln.Readonly);
			fn.setMyPK(fn.getFKFrm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
			//执行保存.
			fn.Save();

		}
		//如果是Self类型的表单的类型
		if (flow.getFlowDevModel() == FlowDevModel.SDKFrm)
		{
			nd.setHisFormType(NodeFormType.SDKForm);
			nd.setFormUrl(flow.getFrmUrl());
			nd.DirectUpdate();

		}
		//如果是Self类型的表单的类型
		if (flow.getFlowDevModel() == FlowDevModel.SelfFrm)
		{
			nd.setHisFormType(NodeFormType.SelfForm);
			nd.setFormUrl(flow.getFrmUrl());
			nd.DirectUpdate();

		}

		nd.setFK_Flow(flowNo);

		nd.Insert();

		//为创建节点设置默认值  @sly 部分方法
		String file = SystemConfig.getPathOfDataUser() + "XML/DefaultNewNodeAttr.xml";
		DataSet ds = new DataSet();
		if (1==2 &&(new File(file)).isFile() == true)
		{
			ds.readXml(file);

			NodeExt ndExt = new NodeExt(nd.getNodeID());
			DataTable dt = ds.Tables.get(0);
			for (DataColumn dc : dt.Columns)
			{
				nd.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
				ndExt.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
			}

			ndExt.setFK_Flow(flowNo);
			ndExt.setNodeID(nodeID);
			ndExt.DirectUpdate();
		}
		nd.setFWCVer(1); //设置为2019版本. 2018版是1个节点1个人,仅仅显示1个意见.
		nd.setNodeID(nodeID);

		nd.setHisDeliveryWay(DeliveryWay.BySelected);

		nd.setX(x);
		nd.setY(y);
		nd.setICON(icon);
		nd.setStep(idx);

		//节点类型.
		nd.setHisNodeWorkType(NodeWorkType.Work);
		nd.setName("New Node " + idx);
		nd.setHisNodePosType(NodePosType.Mid);
		nd.setFK_Flow(flow.getNo());
		nd.setFlowName(flow.getName());

		//设置审核意见的默认值.
		nd.SetValByKey(NodeWorkCheckAttr.FWCDefInfo, Glo.getDefValWFNodeFWCDefInfo());

		nd.Update(); //执行更新. @sly
		nd.CreateMap();

		//通用的人员选择器.
		Selector select = new Selector(nd.getNodeID());
		select.setSelectorModel(SelectorModel.GenerUserSelecter);
		select.Update();

		//设置默认值。
		int state = 0;
		if (flow.getFlowDevModel() == FlowDevModel.JiJian)
		{
			state = 1;
		}

		//设置审核组件的高度.
		DBAccess.RunSQL("UPDATE WF_Node SET FWC_H=300,FTC_H=300," + NodeAttr.FWCSta + "=" + state + " WHERE NodeID='" + nd.getNodeID() + "'");

		//创建默认的推送消息.
		CreatePushMsg(nd);


		//写入日志.
		bp.sys.base.Glo.WriteUserLog("创建节点：" + nd.getName() + " - " + nd.getNodeID(), "通用操作");

		return nd;
	}
	private static void CreatePushMsg(Node nd) throws Exception {
		/*创建发送短消息,为默认的消息.*/
		PushMsg pm = new PushMsg();
		int i = pm.Retrieve(PushMsgAttr.FK_Event, EventListNode.SendSuccess, PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Flow, nd.getFK_Flow());
		if (i == 0)
		{
			pm.setFKEvent(EventListNode.SendSuccess);
			pm.setFK_Node(nd.getNodeID());
			pm.setFK_Flow(nd.getFK_Flow());

			pm.setSMSPushWay(1); // 发送短消息.
			pm.setSMSPushModel("Email");
			pm.setMyPK(DBAccess.GenerGUID(0, null, null));
			pm.Insert();
		}

		//设置退回消息提醒.
		i = pm.Retrieve(PushMsgAttr.FK_Event, EventListNode.ReturnAfter, PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Flow, nd.getFK_Flow());
		if (i == 0)
		{
			pm.setFKEvent(EventListNode.ReturnAfter);
			pm.setFK_Node(nd.getNodeID());
			pm.setFK_Flow(nd.getFK_Flow());

			pm.setSMSPushWay(1); // 发送短消息.
			pm.setMailPushWay(0); //不发送邮件消息.
			pm.setMyPK(DBAccess.GenerGUID(0, null, null));
			pm.Insert();
		}
	}

	/** 
	 检查当前人员是否有修改该流程模版的权限.
	 
	 param flowNo
	 @return 
	*/
	public static boolean CheckPower(String flowNo)
	{
        return true;
		/**if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.GroupInc)
		{
			return true;
		}

		//return true;

		if (WebUser.getNo().equals("admin") == true)
		{
			return true;
		}

		String sql = "SELECT DesignerNo FROM WF_Flow WHERE No='" + flowNo + "'";
		String empNo = DBAccess.RunSQLReturnStringIsNull(sql, null);
		if (DataType.IsNullOrEmpty(empNo) == true)
		{
			return true;
		}

		if (empNo.equals(WebUser.getNo()) == false)
		{
			throw new RuntimeException("err@您没有权限对该流程修改.");
		}

		return true;**/
	}

	/** 
	 创建一个流程模版
	 
	 param flowSort 流程类别
	 param flowName 名称
	 param dsm 存储方式
	 param ptable 物理量
	 param flowMark 标记
	 @return 创建的流程编号
	*/
	public static String NewFlow(String flowSort, String flowName, DataStoreModel dsm, String ptable, String flowMark)throws Exception
	{
		//定义一个变量.
		Flow flow = new Flow();
		try
		{
			//检查参数的完整性.
			if (DataType.IsNullOrEmpty(ptable) == false && ptable.length() >= 1)
			{
				String c = ptable.substring(0, 1);
				if (DataType.IsNumStr(c) == true)
				{
					throw new RuntimeException("@非法的流程数据表(" + ptable + "),它会导致ccflow不能创建该表.");
				}
			}

			flow.setHisDataStoreModel(dsm);
			flow.setPTable(ptable);
			flow.setFK_FlowSort(flowSort);
			flow.setFlowMark(flowMark);

			if (DataType.IsNullOrEmpty(flowMark) == false)
			{
				if (flow.IsExit(FlowAttr.FlowMark, flowMark))
				{
					throw new RuntimeException("@该流程标示:" + flowMark + "已经存在于系统中.");
				}
			}

			/*给初始值*/
			//this.Paras = "@StartNodeX=10@StartNodeY=15@EndNodeX=40@EndNodeY=10";
			flow.setParas("@StartNodeX=200@StartNodeY=50@EndNodeX=200@EndNodeY=350");

			flow.setNo(flow.GenerNewNoByKey(FlowAttr.No, null));
			flow.setName(flowName);
			if (DataType.IsNullOrEmpty(flow.getName()))
			{
				flow.setName("新建流程" + flow.getNo()); //新建流程.
			}

			if (flow.getIsExits() == true)
			{
				throw new RuntimeException("err@系统出现自动生成的流程编号重复.");
			}

			if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
			{
				flow.setOrgNo(WebUser.getOrgNo()); //隶属组织
			}
			flow.setPTable("ND" + Integer.parseInt(flow.getNo()) + "Rpt");

			//设置创建人，创建日期.
			flow.SetValByKey(FlowAttr.CreateDate, DataType.getCurrentDateTime());
			flow.SetValByKey(FlowAttr.Creater, bp.web.WebUser.getNo());
			flow.SetValByKey("Icon", "icon-people");
			flow.Insert();

			//如果是集团模式下.
			/*if (Glo.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
			{*/
				// 记录创建人.
				FlowExt fe = new FlowExt(flow.getNo());
				fe.setDesignerNo(WebUser.getNo());
				fe.setDesignerName(WebUser.getName());
				fe.setDesignTime(DataType.getCurrentDateTime());
				fe.DirectUpdate();
			//}


			Node nd = new Node();
			nd.setNodeID(Integer.parseInt(flow.getNo() + "01"));
			nd.setName("Start Node"); //  "开始节点";
			nd.setStep(1);
			nd.setFK_Flow(flow.getNo());
			nd.setFlowName(flow.getName());
			nd.setHisNodePosType(NodePosType.Start);
			nd.setHisNodeWorkType(NodeWorkType.StartWork);
			nd.setX(200);
			nd.setY(150);
			nd.setNodePosType(NodePosType.Start);
			nd.setICON("前台");

			//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
			nd.setCondModel(DirCondModel.ByDDLSelected); //默认的发送方向.
			nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
			nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.

			//如果是集团模式.   
			if (Glo.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
			{
				if (DataType.IsNullOrEmpty(WebUser.getOrgNo()) == true)
				{
					throw new RuntimeException("err@登录信息丢失了组织信息,请重新登录.");
				}

				nd.setHisDeliveryWay(DeliveryWay.BySelectedOrgs);

				//把本组织加入进去.
				FlowOrg fo = new FlowOrg();
				fo.Delete(FlowOrgAttr.FlowNo, nd.getFK_Flow());
				fo.setFlowNo(nd.getFK_Flow());
				fo.setOrgNo(WebUser.getOrgNo());
				fo.Insert();
			}

			nd.Insert();
			nd.CreateMap();

			//为开始节点增加一个删除按钮. @李国文.
			String sql = "UPDATE WF_Node SET DelEnable=1 WHERE NodeID=" + nd.getNodeID();
			DBAccess.RunSQL(sql);

			//nd.HisWork.CheckPhysicsTable();  去掉，检查的时候会执行.
			CreatePushMsg(nd);

			//通用的人员选择器.
			Selector select = new Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();

			nd = new Node();

			//为创建节点设置默认值 
			String fileNewNode = SystemConfig.getPathOfDataUser() + "XML/DefaultNewNodeAttr.xml";
			if ((new File(fileNewNode)).isFile() == true)
			{
				DataSet myds = new DataSet();
				myds.readXml(fileNewNode);
				DataTable dt = myds.Tables.get(0);
				for (DataColumn dc : dt.Columns)
				{
					nd.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
				}
			}
			else
			{
				nd.setHisNodePosType(NodePosType.Mid);
				nd.setHisNodeWorkType(NodeWorkType.Work);
				nd.setX(200);
				nd.setY(250);
				nd.setICON("审核");
				nd.setNodePosType(NodePosType.End);

				//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
				nd.setCondModel(DirCondModel.ByDDLSelected); //默认的发送方向.
				nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
				nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.
			}

			nd.setNodeID(Integer.parseInt(flow.getNo() + "02"));
			nd.setName("Node 2"); // "结束节点";
			nd.setStep(2);
			nd.setFK_Flow(flow.getNo());
			nd.setFlowName(flow.getName());
			nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
			nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.
			nd.setCondModel(DirCondModel.ByDDLSelected);

			nd.setX(200);
			nd.setY(250);

			//设置审核意见的默认值.
			nd.SetValByKey(NodeWorkCheckAttr.FWCDefInfo, Glo.getDefValWFNodeFWCDefInfo());

			nd.Insert();
			nd.CreateMap();
			//nd.HisWork.CheckPhysicsTable(); //去掉，检查的时候会执行.
			CreatePushMsg(nd);

			//通用的人员选择器.
			select = new Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();

			MapData md = new MapData();
			md.setNo("ND" + Integer.parseInt(flow.getNo()) + "Rpt");
			md.setName( flow.getName());
			md.Save();

			// 装载模版.
			String file = SystemConfig.getPathOfDataUser() + "XML/TempleteSheetOfStartNode.xml";
			if ((new File(file)).isFile() == false && SystemConfig.getIsJarRun()==false)
			{
				//throw new RuntimeException("@开始节点表单模版丢失" + file);
				/*如果存在开始节点表单模版*/
				DataSet ds = new DataSet();
				ds.readXml(file);

				String nodeID = "ND" + Integer.parseInt(flow.getNo() + "01");
				bp.sys.MapData.ImpMapData(nodeID, ds);
			}


			/*如果存在开始节点表单模版*/
			DataSet ds = new DataSet();
			ds.readXml(file);

			String nodeID = "ND" + Integer.parseInt(flow.getNo() + "01");
			MapData.ImpMapData(nodeID, ds);

			//创建track.
			Track.CreateOrRepairTrackTable(flow.getNo());

		}
		catch (RuntimeException ex)
		{
			/**删除垃圾数据.
			*/
			flow.DoDelete();
			//提示错误.
			throw new RuntimeException("err@创建流程错误:" + ex.getMessage());
		}


		FlowExt flowExt = new FlowExt(flow.getNo());
		flowExt.setDesignerNo(WebUser.getNo());
		flowExt.setDesignerName(WebUser.getName());
		flowExt.setDesignTime(DateUtils.format(new Date(),"yyMMddhhmmss"));
		flowExt.DirectSave();

		//创建连线
		Direction drToNode = new Direction();
		drToNode.setFK_Flow(flow.getNo());
		drToNode.setNode(Integer.parseInt(Integer.parseInt(flow.getNo()) + "01"));
		drToNode.setToNode(Integer.parseInt(Integer.parseInt(flow.getNo()) + "02"));
		drToNode.Save();

		//增加方向.
		Node mynd = new Node(drToNode.getNode());
		mynd.setHisToNDs(String.valueOf(drToNode.getToNode()));
		mynd.Update();


		//设置流程的默认值.
		for (String key :SystemConfig.getAppSettings().keySet())
		{
			if (key.toString().contains("NewFlowDefVal") == false)
			{
				continue;
			}

			String val = SystemConfig.GetValByKey(key, "");

			//设置值.
			flow.SetValByKey(key.replace("NewFlowDefVal_",""), val);

		}

		//执行一次流程检查, 为了节省效率，把检查去掉了.
		flow.DoCheck();


		//写入日志.
		bp.sys.base.Glo.WriteUserLog("创建流程：" + flow.getName() + " - " + flow.getNo(), "通用操作");

		return flow.getNo();
	}
	/** 
	 删除节点.
	 
	 param nodeid
	*/
	public static void DeleteNode(int nodeid) throws Exception {
		Node nd = new Node(nodeid);
		nd.Delete();
	}
}