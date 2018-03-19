package cn.jflow.controller.wf.admin.xap.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.jws.WebService;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.GPM.DeptDuty;
import BP.GPM.DeptEmp;
import BP.GPM.DeptEmpAttr;
import BP.GPM.DeptEmpStation;
import BP.GPM.DeptEmpStationAttr;
import BP.GPM.DeptEmps;
import BP.Sys.AppType;
import BP.Sys.MapData;
import BP.Sys.OSModel;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumMain;
import BP.Sys.SystemConfig;
import BP.Tools.FormatToJson;
import BP.Tools.PinYinF4jUtils;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.ImpFlowTempleteModel;
import BP.WF.Node;
import BP.WF.RunModel;
import BP.WF.Template.CondAttr;
import BP.WF.Template.Conds;
import BP.WF.Template.DataStoreModel;
import BP.WF.Template.Direction;
import BP.WF.Template.FlowSort;
import BP.WF.Template.LabNote;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.WorkflowDefintionManager;
import BP.Web.WebUser;

@WebService(endpointInterface = "cn.jflow.controller.wf.admin.xap.service.WSDesignerSoap")
public class WSDesignerSoapImpl implements WSDesignerSoap {

	public String GetFlowTree() {
		String sql = ""
				+ "\r\n"
				+ "SELECT No ,ParentNo,Name, Idx, 1 IsParent FROM WF_FlowSort"
				+ "\r\n"
				+ "union "
				+ "\r\n"
				+ "SELECT No, FK_FlowSort as ParentNo,Name,Idx,0 IsParent FROM WF_Flow "
				+ "\r\n" + "";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String sTmp = "";
		String s = "";
		if (dt != null && dt.Rows.size() > 0) {
			s = FlowDesignerUtils.GetTreeJsonByTable(dt, "0", null, null, null,
					null, null);
		}
		sTmp += s;

		return sTmp;
	}

	public String GetFormTree() {
		String sql = "SELECT No ,ParentNo,Name, Idx, 1 IsParent FROM Sys_FormTree"
				+ "\r\n"
				+ "union "
				+ "\r\n"
				+ "SELECT No, FK_FrmSort as ParentNo,Name,Idx,0 IsParent FROM Sys_MapData   where AppType=0 AND FK_FormTree IN (SELECT No FROM Sys_FormTree) "
				+ "\r\n" + "";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String s = "";

		String sTmp = "";

		if (dt != null && dt.Rows.size() > 0) {
			s = FlowDesignerUtils.GetTreeJsonByTable(dt, "0", null, null, null,
					null, null);
		}
		sTmp += s;

		return sTmp;
	}
	
	public String GetFlowTrackJsonData(String fk_flow, String workid) {
		return GetFlowTrackJsonData(fk_flow,workid,"0");
	}

	public String GetFlowTrackJsonData(String fk_flow, String workid,String fid) {
		DataSet ds = new DataSet();
		DataTable dt = null;
		String json = null;
		try {
			// 获取流程信息
			/*String sql = "SELECT NO,Name,Paras,ChartType FROM WF_Flow WHERE No='"
					+ fk_flow + "'";*/
			String sql = "SELECT Paras,ChartType FROM WF_Flow WHERE No='" + fk_flow + "'";

			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_FLOW";
			ds.Tables.add(dt);

			// 获取流程中的节点信息
			sql = "SELECT NodeID ID,Name,Icon,X,Y,NodePosType,RunModel,HisToNDs,TodolistModel FROM WF_Node WHERE FK_Flow='"
					+ fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_NODE";
			ds.Tables.add(dt);

			// 获取流程中的标签信息
			sql = "SELECT MyPK,Name,X,Y FROM WF_LabNote WHERE FK_Flow='"
					+ fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_LABNOTE";
			ds.Tables.add(dt);

			// 获取流程中的线段方向信息
			sql = "SELECT Node,ToNode,DirType,IsCanBack,Dots FROM WF_Direction WHERE FK_Flow='"
					+ fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_DIRECTION";
			ds.Tables.add(dt);

			if (!StringHelper.isNullOrEmpty(workid)) {
				// 获取工作轨迹信息
				String trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
				sql = "SELECT NDFrom, NDTo,ActionType,ActionTypeText,Msg,RDT,EmpFrom,EmpFromT,EmpToT,EMPTO FROM "
						+ trackTable
						+ " WHERE WorkID="
						+ workid
						+ "  OR FID="
						+ workid + " ORDER BY RDT ASC";
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "TRACK";
				ds.Tables.add(dt);
				
				 //获取预先计算的节点处理人，以及处理时间,added by liuxc,2016-4-15
                sql = "SELECT wsa.FK_Node,wsa.FK_Emp,wsa.EmpName,wsa.TimeLimit,wsa.TSpanHour,wsa.ADT,wsa.SDT FROM WF_SelectAccper wsa WHERE wsa.WorkID = " + workid;
                dt = DBAccess.RunSQLReturnTable(sql);
                dt.TableName = "POSSIBLE";
                ds.Tables.add(dt);

                //获取节点处理人数据，及处理/查看信息
                sql = "SELECT wgw.FK_Emp,wgw.FK_Node,wgw.FK_EmpText,wgw.RDT,wgw.IsRead,wgw.IsPass FROM WF_GenerWorkerlist wgw WHERE wgw.WorkID = " +
                      workid + (StringHelper.isNullOrEmpty(fid) || "0".equals(fid) ? (" OR FID=" + workid) : (" OR WorkID=" + fid + " OR FID=" + fid));
                dt = DBAccess.RunSQLReturnTable(sql);
                dt.TableName = "DISPOSE";
                ds.Tables.add(dt);
			}
			 else
             {
                 String trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
                 sql = "SELECT NDFrom, NDTo,ActionType,ActionTypeText,Msg,RDT,EmpFrom,EmpFromT,EmpToT,EmpTo FROM " + trackTable +
                       " WHERE WorkID=0 ORDER BY RDT ASC";
                 dt = DBAccess.RunSQLReturnTable(sql);
                 dt.TableName = "TRACK";
                 ds.Tables.add(dt);
             }
            /* for (int i = 0; i < ds.Tables.size(); i++)
             {
                 dt = ds.Tables.get(i);
                 dt.TableName = dt.TableName.toUpperCase();
                 for (int j = 0; j < dt.Columns.size(); j++)
                 {
                     dt.Columns.get(j).ColumnName = dt.Columns.get(j).ColumnName.toUpperCase();
                 }
             }*/

            // String re = new { success = true, msg = string.Empty, ds = ds };

			
			json = FormatToJson.ToJson(ds);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}

	public String GetFlowDesignerTree(Boolean[] paras) {
//		OSModel model = OSModel.OneOne;
		if (!FlowDesignerUtils.TreeRootCheck()) {
            String error = "@检查树结构出现如下问题,有可能出现的问题如下.";
            error += "\t\n 1, 流程树WF_FlowSort、表单树WF_FlowSort, 没有ParentNo =0 的根节点，请检查。";
            error += "\t\n 2, 也许您集成了ccbpm的组织结构，但是组织结构部门表里没有ParentNo =0 的根节点，请检查。";
			throw new RuntimeException(error);
		}
		
		String sql = "";
		DataSet myds = new DataSet();

		//加入流程类别.
		sql = "SELECT No,Name,ParentNo FROM WF_FlowSort ORDER BY No,Idx";
		DataTable dtFlowSort = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtFlowSort.TableName = "WF_FlowSort";
		myds.Tables.add(dtFlowSort);

		//加入流程.
		sql = "SELECT No,Name,FK_FlowSort FROM WF_Flow ORDER BY No,Idx";
		DataTable dtFlow = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtFlow.TableName = "WF_Flow";
		myds.Tables.add(dtFlow);


		//加入表单树.
		sql = "SELECT No,Name,ParentNo FROM Sys_FormTree ORDER BY Idx ASC,No ASC";
		DataTable dtFormTree = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtFormTree.TableName = "Sys_FormTree";
		myds.Tables.add(dtFormTree);

		//加入表单.
		sql = "SELECT a.No, a.Name, a.FK_FormTree FROM Sys_MapData a, Sys_FormTree b WHERE a.AppType=" 
				+ AppType.Application.getValue() + " AND a.FK_FormTree=b.No ORDER BY a.Idx ASC , a.No ASC";
		DataTable dtForm = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtForm.TableName = "Sys_MapData";
		myds.Tables.add(dtForm);

		// 装载组织结构.
//		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
//		{
//			var ws = DataType.GetPortalInterfaceSoapClientInstance();
//			DataTable dt = ws.GetDepts();
//			dt.TableName = "Port_Dept";
//			myds.Tables.add(dt);
//
//			DataTable dtEmp = ws.GetEmps();
//			dtEmp.TableName = "Port_Emp";
//			myds.Tables.add(dtEmp);
//		}
//		else
//		{
			//加入部门.
			sql = "SELECT No,Name,ParentNo FROM Port_Dept ORDER BY No,Idx";
			DataTable dtDept = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dtDept.TableName = "Port_Dept";
			myds.Tables.add(dtDept);

			//加入人员.
			sql = "SELECT No,Name,FK_Dept FROM Port_Emp ORDER BY No,Idx";
			DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dtEmp.TableName = "Port_Emp";
			myds.Tables.add(dtEmp);
//		}
		return DataSet.ConvertDataSetToXml(myds);

//		String result = "";
//		String sqls = "";
//		java.util.ArrayList<String> tableNames = new java.util.ArrayList<String>();
//		boolean isBegin = true;
//
//		// 是否加载流程树
//		if (paras.length > 0 && paras[0]) {
//			tableNames.add("WF_FlowSort");
//			tableNames.add("WF_Flow");
//
//			sqls += "@SELECT * FROM WF_FlowSort ORDER BY No,Idx";
//			sqls += "@SELECT No,Name,FK_FlowSort,Idx FROM WF_Flow ORDER BY FK_FlowSort,Idx,No";
//			isBegin = false;
//		}
//
//		// 是否加载表单树
//		if (paras.length > 1 && paras[1]) {
//			tableNames.add("Sys_FormTree");
//			tableNames.add("Sys_MapData");
//			if (!isBegin) {
//				sqls += "@";
//			}
//
//			isBegin = false;
//			sqls += "SELECT No,Name,ParentNo FROM Sys_FormTree ORDER BY Idx ASC,No ASC";
//			sqls += "@SELECT No,Name,FK_FormTree FROM Sys_MapData WHERE AppType="
//					+ AppType.Application.getValue()
//					+ " AND FK_FormTree IN (SELECT No FROM Sys_FormTree) ORDER BY Idx ASC ,No ASC";
//		}
//
//		// 是否加载组织结构树
//		if (paras.length > 2 && paras[2]) {
//			tableNames.add("Port_Dept");
//			tableNames.add("Port_Emp");
//
//			if (!isBegin) {
//				sqls += "@";
//			}
//
//			if (model == OSModel.OneMore) {
//				sqls += "SELECT No,Name,ParentNo,TreeNo FROM Port_Dept ORDER BY Idx ASC,No ASC";
//				sqls += "@SELECT  No,Name,FK_Dept  FROM Port_Emp ";
//			} else {
//				sqls += "SELECT No,Name,ParentNo FROM Port_Dept ORDER BY Idx ASC,No ASC";
//				sqls += "@SELECT  No,Name,FK_Dept  FROM Port_Emp ";
//			}
//		}
//
//		DataSet ds = FlowDesignerUtils.RunSQLReturnDataSet(sqls); // DBAccess.RunSQLReturnDataSet(sqls);
//		if (tableNames.size() == ds.Tables.size()) {
//			for (int i = 0; i < ds.Tables.size(); i++) {
//				ds.Tables.get(i).TableName = tableNames.get(i);
//			}
//		}
//		result = DataSet.ConvertDataSetToXml(ds);
//		return result;
	}

	public String GetWorkList(String fk_flow, String workid) {
		try {
			String sql = "";
			String table = "ND" + Integer.parseInt(fk_flow) + "Track";
			DataSet ds = new DataSet();
			sql = "SELECT NDFrom, NDTo,ActionType,Msg,RDT,EmpFrom,EmpFromT FROM "
					+ table
					+ " WHERE WorkID="
					+ workid
					+ "  OR FID="
					+ workid
					+ "ORDER BY NDFrom ASC,NDTo ASC";
			DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			mydt.TableName = "WF_Track";
			ds.Tables.add(mydt);
			return DataSet.ConvertDataSetToXml(ds);
			// return null;
		} catch (RuntimeException ex) {
			BP.DA.Log.DefaultLogWriteLineError("GetDTOfWorkList发生了错误 paras:"
					+ fk_flow + "\t" + workid + ex.getMessage());
			return null;
		}
	}

	/**
		 * 人员修改
		 * 
		 * @param empNo
		 *            人员编号
		 * @param deptNo
		 *            部门编号
		 * @param attrs
		 *            人员属性格式为@字段1=值1@字段2=值2
		 * @param stations
		 *            该人员在本部门下的岗位集合
		 */
		public String Emp_Edit(String empNo, String deptNo, String attrs,
				String stations) {
			// 更新 emp 信息.
			BP.GPM.Emp emp = new BP.GPM.Emp(empNo);
			String[] strs = attrs.split("[^]", -1);
			for (String str : strs) {
				if (StringHelper.isNullOrEmpty(str)) {
					continue;
				}
				String[] kv = str.split("[=]", -1);
				emp.SetValByKey(kv[0], kv[1]); // 设置值.
			}
			emp.Update();
	
			// 更新 empDept 信息.m
			BP.GPM.DeptEmp deptEmp = new BP.GPM.DeptEmp();
			deptEmp.setMyPK(deptNo + "_" + empNo);
			deptEmp.setFK_Emp(empNo);
			deptEmp.setFK_Dept(deptNo);
	
			int i = deptEmp.RetrieveFromDBSources();
			if (i == 0) {
				deptEmp.DirectInsert();
			}
	
			strs = attrs.split("[^]", -1);
			for (String str : strs) {
				if (StringHelper.isNullOrEmpty(str)) {
					continue;
				}
	
				String[] kv = str.split("[=]", -1);
				deptEmp.SetValByKey(kv[0], kv[1]); // 设置值.
			}
			deptEmp.setMyPK(deptEmp.getFK_Dept() + "_" + deptEmp.getFK_Emp());
			deptEmp.Update(); // 执行保存.
	
			// 更新岗位对应.
			BP.DA.DBAccess.RunSQL("DELETE Port_DeptEmpStation WHERE FK_Dept='"
					+ deptNo + "' AND FK_Emp='" + emp.getNo() + "' ");
			strs = stations.split("[,]", -1);
			for (String str : strs) {
				if (StringHelper.isNullOrEmpty(str)) {
					continue;
				}
	
				// 插入.
				BP.GPM.DeptEmpStation ds = new BP.GPM.DeptEmpStation();
				ds.setFK_Dept(deptNo);
				ds.setFK_Emp(emp.getNo());
				ds.setFK_Station(str);
				ds.Insert();
			}
			return "@修改成功";
		}

	/**
	 * 人员新增
	 * 
	 * @param empNo
	 *            人员编号
	 * @param deptNo
	 *            部门编号
	 * @param attrs
	 *            人员属性格式为@字段1=值1@字段2=值2
	 * @param stations
	 *            该人员在本部门下的岗位集合
	 */
	public String Emp_New(String empNo, String deptNo, String attrs,
			String stations) {
		// 更新 emp 信息.
		BP.GPM.Emp emp = new BP.GPM.Emp();
		emp.setNo(empNo);
		// if (emp.IsExits)
		// return "@Error:编号为[" + empNo + "]新信息已经存在";
		if (!emp.getIsExits()) {
			String[] strs = attrs.split("[^]", -1);
			for (String str : strs) {
				if (StringHelper.isNullOrEmpty(str)) {
					continue;
				}

				String[] kv = str.split("[=]", -1);
				emp.SetValByKey(kv[0], kv[1]); // 设置值.
			}
			emp.Insert();
		}
		// 执行编辑.
		this.Emp_Edit(empNo, deptNo, attrs, stations);

		return "@增加成功";
	}

	/**
	 * 删除人员
	 * 
	 * @param empNo
	 *            人员编号
	 * @param deptNo
	 *            部门编号
	 * @return
	 */
	public String Emp_Delete(String empNo, String deptNo) {
		// 从部门信息里删除它.
		DeptEmp de = new DeptEmp(deptNo, empNo);
		de.Delete();

		// 删除这笔数据.
		String sql = "DELETE Port_DeptEmpStation WHERE FK_Emp='" + empNo
				+ "' AND FK_Dept='" + deptNo + "'";
		BP.DA.DBAccess.RunSQL(sql);

		// 检查其它部门里是否有此数据.
		DeptEmps des = new DeptEmps();
		des.Retrieve(DeptEmpAttr.FK_Emp, empNo);
		if (des.size() != 0) {
			// 说明其它部门里还有此帐号的信息，所在不能删除主表的数据.
		} else {
			// 其它部门里没有此帐号的信息了，就删除主表的数据。
			BP.GPM.Emp emp = new BP.GPM.Emp();
			emp.setNo(empNo);
			emp.Delete();
		}
		return "@人员[" + empNo + "]删除成功";
	}

	/**
	 * 保存ens
	 * 
	 * @param vals
	 * @return
	 */
	public String SaveEn(String vals) {
		Entity en = null;
		try {
			AtPara ap = new AtPara(vals);
			String enName = ap.GetValStrByKey("EnName");
			String pk = ap.GetValStrByKey("PKVal");
			if (enName.equals("BP.Sys.MapData")) {
				enName = "BP.Sys.MapData";
			}
			en = ClassFactory.GetEn(enName);
			en.ResetDefaultVal();

			if (en == null) {
				throw new RuntimeException("Error:无效的类名:" + enName);
			}

			if (StringHelper.isNullOrEmpty(pk) == false) {
				en.setPKVal(pk);
				en.RetrieveFromDBSources();
			}
			Hashtable<String, String> table = ap.getHisHT();
			for (Map.Entry<String, String> entry : table.entrySet()) {
				if (entry.getKey().equals("PKVal")) {
					continue;
				}
				if (StringHelper.isNullOrEmpty(entry.getValue())) {
					continue;
				}
				en.SetValByKey(entry.getKey(),
						entry.getValue().replace('#', '@'));
			}
			en.Save();
			return (String) ((en.getPKVal() instanceof String) ? en.getPKVal()
					: null);
		} catch (RuntimeException ex) {
			if (en != null) {
				en.CheckPhysicsTable();
			}

			return "Error:" + ex.getMessage();
		}
	}

	public String SaveEnum(String enumKey, String enumLab, String cfg) {
		SysEnumMain sem = new SysEnumMain();
		sem.setNo(enumKey);
		if (sem.RetrieveFromDBSources() == 0) {
			sem.setName(enumLab);
			sem.setCfgVal(cfg);
			sem.setLang(WebUser.getSysLang());
			sem.Insert();
		} else {
			sem.setName(enumLab);
			sem.setCfgVal(cfg);
			sem.setLang(WebUser.getSysLang());
			sem.Update();
		}

		String[] strs = cfg.split("[@]", -1);
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}
			String[] kvs = str.split("[=]", -1);
			SysEnum se = new SysEnum();
			se.setEnumKey(enumKey);
			se.setLang(WebUser.getSysLang());
			se.setIntKey(Integer.parseInt(kvs[0]));
			se.setLab(kvs[1]);
			se.Insert();
		}
		return "save ok.";
	}

	/**
	 * 人员与部门进行关联
	 * 
	 * @param empNos
	 *            人员编号集合
	 * @param deptNo
	 *            部门编号
	 * @return
	 */
	public String Dept_Emp_Related(String empNos, String deptNo) {
		try {
			String[] emps = empNos.split("[^]", -1);
			for (String empNo : emps) {
				BP.GPM.DeptEmps empDepts = new BP.GPM.DeptEmps();
				QueryObject objInfo = new QueryObject(empDepts);
				objInfo.AddWhere("MyPK", deptNo + "_" + empNo);
				objInfo.DoQuery();

				BP.GPM.Emp emp = new BP.GPM.Emp(empNo);
				emp.setFK_Dept(deptNo);
				emp.Update();

				BP.GPM.DeptEmp empDept = new DeptEmp();
				empDept.setMyPK(deptNo + "_" + empNo);
				empDept.setFK_Duty(emp.getFK_Duty());
				empDept.setFK_Dept(deptNo);
				empDept.setFK_Emp(empNo);
				// 判断是否存在
				if (empDepts == null || empDepts.size() == 0) {
					empDept.Insert();
				} else {
					empDept.Update();
				}
			}
		} catch (RuntimeException ex) {
			return "err:" + ex.getMessage();
		}
		return "@关联成功";
	}

	/**
	 * 编辑部门属性.
	 * 
	 * @param deptNo
	 *            部门编号
	 * @param attrs
	 *            属性是@字段名=值
	 * @param stations
	 *            多个用逗号分开.
	 * @param dutys
	 *            多个职务用逗号分开.
	 */
	public void Dept_Edit(String deptNo, String attrs, String stations,
			String dutys) {
		// 更新dept 信息.
		BP.GPM.Dept dept = new BP.GPM.Dept(deptNo);
		String[] strs = attrs.split("[^]", -1);
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}

			String[] kv = str.split("[=]", -1);
			dept.SetValByKey(kv[0], kv[1]); // 设置值.
		}
		dept.Update();

		// 更新岗位对应.
		strs = stations.split("[,]", -1);
		BP.DA.DBAccess.RunSQL("DELETE Port_DeptStation WHERE FK_Dept='"
				+ dept.getNo() + "'");
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}

			BP.GPM.DeptStation ds = new BP.GPM.DeptStation();
			ds.setFK_Dept(dept.getNo());
			ds.setFK_Station(str);
			ds.Insert();
		}
		// 更新职务对应.
		strs = dutys.split("[,]", -1);
		BP.DA.DBAccess.RunSQL("DELETE Port_DeptDuty WHERE FK_Dept='"
				+ dept.getNo() + "'");
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}

			DeptDuty ds = new DeptDuty();
			ds.setFK_Dept(dept.getNo());
			ds.setFK_Duty(str);
			ds.Insert();
		}
	}

	/**
	 * 删除部门
	 * 
	 * @param deptNo
	 *            部门编号
	 * @param forceDel
	 *            强制删除
	 * @return 返回删除信息
	 */
	public String Dept_Delete(String deptNo, boolean forceDel) {
		// 获取部门信息
		BP.GPM.Dept dept = new BP.GPM.Dept(deptNo);
		if (dept.getParentNo().equals("0")) {
			return "@error:根节点不允许被删除，强制删除将无效.";
		}
	
		if (forceDel) // 如果为强制删除
		{
			// 部门与人员
			DeptEmp deptEmp = new DeptEmp();
			deptEmp.Delete(DeptEmpAttr.FK_Dept, deptNo);
			// 部门、人员与岗位
			DeptEmpStation deptEmpStation = new DeptEmpStation();
			deptEmpStation.Delete(DeptEmpStationAttr.FK_Dept, deptNo);
			// 子部门与人员
			BP.GPM.Depts cDepts = new BP.GPM.Depts(deptNo);
	
			for (BP.GPM.Dept item : cDepts.ToJavaList()) {
				// 部门与人员
				DeptEmp cdeptEmp = new DeptEmp();
				cdeptEmp.Delete(DeptEmpAttr.FK_Dept, item.getNo());
				// 部门、人员与岗位
				DeptEmpStation cDeptEmpStation = new DeptEmpStation();
				cDeptEmpStation
						.Delete(DeptEmpStationAttr.FK_Dept, item.getNo());
				// 删除子部门
				item.Delete();
			}
			// 执行删除.
			dept.Delete();
			return "@部门[" + dept.getName() + "]删除成功.";
		}
	
		// 子部门
		BP.GPM.Depts childDept = new BP.GPM.Depts(dept.getNo());
		if (childDept != null && childDept.size() > 0) {
			return "@error:当前部门有下级";
		}
	
		// 检查一下部门下是否有人员.
		String sql = "SELECT * FROM Port_DeptEmp WHERE FK_Dept='"
				+ dept.getNo() + "'";
		if (BP.DA.DBAccess.RunSQLReturnTable(sql).Rows.size() != 0) {
			return "@error:当前部门下有人员";
		}
	
		// 检查一下部门下是否有人员.
		sql = "SELECT * FROM Port_DeptEmpStation WHERE FK_Dept='"
				+ dept.getNo() + "'";
		if (BP.DA.DBAccess.RunSQLReturnTable(sql).Rows.size() != 0) {
			return "@error:当前部门下有人员岗位对应信息";
		}
	
		// 执行删除.
		dept.Delete();
	
		// 更新父节点目录
		BP.GPM.Dept deptParent = new BP.GPM.Dept(dept.getParentNo());
		if (deptParent != null) {
			if (deptParent.getHisSubDepts() != null
					&& deptParent.getHisSubDepts().size() == 0) {
				deptParent.setIsDir(false);
				deptParent.Update("IsDir", false);
			}
		}
		return "@部门[" + dept.getName() + "]删除成功.";
	}

	/**
	 * 增加同级部门
	 * 
	 * @param currDeptNo
	 *            当前部门编号
	 * @param attrs
	 *            新部门属性
	 * @param stations
	 *            新部门关联的岗位集合，用逗号分开.
	 * @param dutys
	 *            新部门关联的职务集合，用逗号分开.
	 * @return 新建部门的编号
	 */
	public String Dept_CreateSameLevel(String currDeptNo, String attrs,
			String stations, String dutys) {
		// 检查部门是否存在
		if (DeptName_Check(attrs)) {
			return "err:该部门已经存在。";
		}
		BP.GPM.Dept dept = new BP.GPM.Dept(currDeptNo);
		Object tempVar = dept.DoCreateSameLevelNode();
		BP.GPM.Dept newDept = (BP.GPM.Dept) ((tempVar instanceof BP.GPM.Dept) ? tempVar
				: null);
		newDept.setName("new dept");
		newDept.setFK_DeptType("");
		// 调用编辑部门，并保存它.
		Dept_Edit(newDept.getNo(), attrs, stations, dutys);
		return newDept.getNo();
	}

	/**
	 * 增加增加下级部门
	 * 
	 * @param currDeptNo
	 *            当前部门编号
	 * @param attrs
	 *            新部门属性
	 * @param stations
	 *            新部门关联的岗位集合，用逗号分开.
	 * @param dutys
	 *            新部门关联的职务集合，用逗号分开.
	 * @return 新建部门的编号
	 */
	public String Dept_CreateSubLevel(String currDeptNo, String attrs,
			String stations, String dutys) {
		// 检查部门是否存在
		if (DeptName_Check(attrs)) {
			return "err:该部门已经存在。";
		}
		BP.GPM.Dept dept = new BP.GPM.Dept(currDeptNo);
		Object tempVar = dept.DoCreateSubNode();
		BP.GPM.Dept newDept = (BP.GPM.Dept) ((tempVar instanceof BP.GPM.Dept) ? tempVar
				: null);
		newDept.setName("new dept");
		newDept.setFK_DeptType("");
	
		// 调用编辑部门，并保存它.
		this.Dept_Edit(newDept.getNo(), attrs, stations, dutys);
		return newDept.getNo();
	}

	/**
	 * 检查部门是否存在
	 * 
	 * @param attrs
	 *            部门名称
	 * @return
	 */
	public boolean DeptName_Check(String attrs) {
		boolean isHave = false;
		String repeatName = SystemConfig.getCS_AppSettings()
				.get("RepeatDeptName").toString();
		// 允许名称重复。
		if (repeatName == null || repeatName.equals("0")) {
			return false;
		}
	
		String[] strs = attrs.split("[^]", -1);
		String deptName = "系统管理";
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}
	
			String[] kv = str.split("[=]", -1);
			if (kv[0].equals("Name")) {
				deptName = kv[1];
			}
		}
		String sql = "SELECT COUNT(Name) NUM FROM Port_Dept WHERE Name='"
				+ deptName + "'";
		int rowCount = RunSQLReturnValInt(sql, null, null);
		if (rowCount > 0) {
			isHave = true;
		}
	
		return isHave;
	}

	/**
	 * 获取值
	 * 
	 * @param kev
	 * @return
	 */
	public String CfgKey(String kev, String UserNo, String SID) {
		if (kev.equals("SendEmailPass") || kev.equals("AppCenterDSN")
				|| kev.equals("FtpPass")) {
			throw new RuntimeException("@非法的访问");
		} else {
		}

		return BP.Sys.SystemConfig.getAppSettings().get("kev").toString();
	}

	// / <summary>
	// / 获得共享模版的目录名称
	// / </summary>
	// / <returns>用@符合分开的文件名.</returns>
	public String GetDirs(String dir, boolean FileOrDirecotry) {
		String ip = "online.ccflow.org";
		String userNo = "ccflowlover";
		String pass = "ccflowlover";

		java.util.ArrayList<String> listDir = new java.util.ArrayList<String>();
		String dirs = "";
		// try
		// {
		// FtpConnection conn = new FtpConnection(ip, userNo, pass);
		// java.util.ArrayList<Win32FindData> sts = getFiles(conn, dir);
		//
		// for (Win32FindData item : sts)
		// {
		// if (FileOrDirecotry)
		// {
		// if (item.FileAttributes == FileAttributes.Directory)
		// {
		// listDir.add(item.FileName);
		// }
		// }
		// else if (item.FileAttributes == FileAttributes.Normal)
		// {
		// String tmp = item.FileName;
		// tmp = tmp.substring(0, tmp.lastIndexOf('.'));
		//
		// if (!listDir.contains(tmp))
		// {
		// listDir.add(tmp);
		// }
		// }
		// }
		//
		// for (String item : listDir)
		// {
		// dirs += item + "@";
		// }
		// if (!StringHelper.isNullOrEmpty(dirs))
		// {
		// dirs = dirs.substring(0, dirs.lastIndexOf('@'));
		// }
		//
		// }
		// catch (RuntimeException e)
		// {
		// BP.DA.Log.DebugWriteError(e.toString());
		// }
		return dirs;
	}

	public int RunSQL(String sql, String UserNo, String SID) {
		/* 因sid验证取消，暂时注释掉该验证*/
		if (!(WebUser.CheckSID(UserNo, SID) && Glo.getIsAdmin(UserNo))){
			throw new RuntimeException("@权限已丢失,请重新登录.");
    	}
		if (!Glo.getIsAdmin(WebUser.getNo())){
			throw new RuntimeException("@权限已丢失,请重新登录.");
		}
		return BP.DA.DBAccess.RunSQL(sql);
	}

	/**
	 * 运行sqls
	 * 
	 * @param sqls
	 * @return
	 */
	public int RunSQLs(String sqls, String UserNo, String SID) {
		if (!(WebUser.CheckSID(UserNo, SID) && Glo.getIsAdmin(UserNo))){
			throw new RuntimeException("@权限已丢失,请重新登录.");
    	}
		if (StringHelper.isNullOrEmpty(sqls)) {
			return 0;
		}
		int i = 0;
		String[] strs = sqls.split("[@]", -1);
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}
			i += BP.DA.DBAccess.RunSQL(str);
		}
		return i;
	}

	/**
	 * 运行sql返回table.
	 * 
	 * @param sql
	 * @return
	 */
	public String RunSQLReturnTable(String sql, String UserNo, String SID) {
		/*sid不做验证 */
		if (!(WebUser.CheckSID(UserNo, SID) && Glo.getIsAdmin(UserNo))){
			//throw new RuntimeException("@权限已丢失,请重新登录.");
    	}
		DataSet ds = new DataSet();
		ds.Tables.add(BP.DA.DBAccess.RunSQLReturnTable(sql));
		return DataSet.ConvertDataSetToXml(ds);
	}

	/**
	 * 运行sql返回String.
	 * 
	 * @param sql
	 * @return
	 */
	public String RunSQLReturnString(String sql, String UserNo, String SID) {
		if (!(WebUser.CheckSID(UserNo, SID) && Glo.getIsAdmin(UserNo))){
			throw new RuntimeException("@权限已丢失,请重新登录.");
    	}
		return BP.DA.DBAccess.RunSQLReturnString(sql);
	}

	/**
	 * 运行sql返回int.
	 * 
	 * @param sql
	 * @return
	 */
	public int RunSQLReturnValInt(String sql, String UserNo, String SID) {
	
		if (!(WebUser.CheckSID(UserNo, SID) && Glo.getIsAdmin(UserNo))){
			throw new RuntimeException("@权限已丢失,请重新登录.");
    	}
		return BP.DA.DBAccess.RunSQLReturnValInt(sql);
	}

	public float RunSQLReturnValFloat(String sql,String UserNo, String SID) {
		if (!(WebUser.CheckSID(UserNo, SID) && Glo.getIsAdmin(UserNo))){
			throw new RuntimeException("@权限已丢失,请重新登录.");
    	}
		try {
			return BP.DA.DBAccess.RunSQLReturnValFloat(sql);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return -1;
	}

	public String RunSQLReturnTableS(String sqls, String UserNo, String SID) {
		if (!(WebUser.CheckSID(UserNo, SID) && Glo.getIsAdmin(UserNo))){
			//throw new RuntimeException("@权限已丢失,请重新登录.");
    	}
		// 验证用户未通过
		DataSet newDs = FlowDesignerUtils.RunSQLReturnDataSet(sqls);
		String ds = DataSet.ConvertDataSetToXml(newDs);
		return ds;
	}

	/**
	 * 检查根节点
	 * 
	 * @return
	 */
	public String Dept_CheckRootNode() {
		OSModel model = OSModel
				.forValue(Integer.parseInt(GetConfig("OSModel")));

		if (model == OSModel.OneMore) {
			BP.GPM.Depts rootDepts = new BP.GPM.Depts("0");
			if (rootDepts == null || rootDepts.size() == 0) {
				BP.GPM.Dept rootDept = new BP.GPM.Dept();
				rootDept.setName("集团总部");
				rootDept.setFK_DeptType("01");
				rootDept.setParentNo("0");
				rootDept.setIdx(0);
				rootDept.Insert();
			}
		} else if (model == OSModel.OneOne) {
			BP.Port.Depts rootDepts = new BP.Port.Depts("0");
			if (rootDepts == null || rootDepts.size() == 0) {
				BP.GPM.Dept rootDept = new BP.GPM.Dept();
				rootDept.setName("集团总部");
				rootDept.setFK_DeptType("01");
				rootDept.setParentNo("0");
				rootDept.setIdx(0);
				rootDept.Insert();
			}
		}

		return "true";
	}

	/**
	 * 拖动部门改变节点父编号
	 * 
	 * @param currDeptNo
	 *            拖动节点
	 * @param pDeptNo
	 *            拖动节点的父节点
	 * @return
	 */
	public String Dept_DragTarget(String currDeptNo, String pDeptNo) {
		try {
			BP.GPM.Dept currDept = new BP.GPM.Dept(currDeptNo);
			currDept.setParentNo(pDeptNo);
			currDept.Update();
		} catch (RuntimeException ex) {
			return "err:" + ex.getMessage();
		}
		return null;
	}

	/**
	 * 拖动部门进行排序
	 * 
	 * @param currDeptNo
	 *            拖动节点
	 * @param nextDeptNo
	 *            关系节点
	 * @param nextNodeNos
	 *            下面节点的编号集合
	 * @param isUpNode
	 *            是否拖动节点后的上面节点
	 * @return
	 */
	public String Dept_DragSort(String currDeptNo, String nextDeptNo,
			String nextNodeNos, boolean isUpNode) {
		try {
			BP.GPM.Dept currDept = new BP.GPM.Dept(currDeptNo);
			BP.GPM.Dept nextDept = new BP.GPM.Dept(nextDeptNo);
			if (isUpNode) // 如果关系节点为上面的节点
			{
				// 设置序号
				currDept.setIdx(nextDept.getIdx() + 1);
				currDept.Update();
			} else {
				// 交换序号
				currDept.setIdx(nextDept.getIdx());
				currDept.Update();
				// 下面节点全部下移
				int Idx = currDept.getIdx();
				String[] nodeNos = nextNodeNos.split("[,]", -1);
				for (String nodeNo : nodeNos) {
					if (StringHelper.isNullOrEmpty(nodeNo)) {
						continue;
					}
					Idx++;
					// 下面的节点下移
					nextDept = new BP.GPM.Dept(nodeNo);
					nextDept.setIdx(Idx);
					nextDept.Update();
				}
			}
		} catch (RuntimeException ex) {
			return "err:" + ex.getMessage();
		}
		return null;
	}

	/**
	 * 获得共享模版的目录名称
	 * 
	 * @return 用@符合分开的文件名.
	 */

	public final FtpFile GetDirectory() {
		// String FlowTemplate = DoPort.FlowTemplate;
		// String ip = "online.ccflow.org";
		// String userNo = "ccflowlover";
		// String pass = "ccflowlover";

		FtpFile Superfile = null;
		// FtpSupport.FtpConnection conn = null;
		// try
		// {
		// conn = new FtpSupport.FtpConnection(ip, userNo, pass);
		//
		// FtpFile tempVar = new FtpFile();
		// tempVar.Name = FlowTemplate;
		// tempVar.Path = FlowTemplate;
		// tempVar.java.lang.Class = FtpFile.FileType.Directory;
		// Superfile = tempVar;
		// Superfile.Subs = new java.util.ArrayList<FtpFile>();
		//
		// java.util.ArrayList<FtpSupport.Win32FindData> sts = getFiles(conn,
		// FlowTemplate);
		// for (FtpSupport.Win32FindData item : sts)
		// {
		//
		// FtpFile tempVar2 = new FtpFile();
		// tempVar2.Path = FlowTemplate + "\\" + item.FileName;
		// FtpFile file = tempVar2;
		// FtpFile tempVar3 = new FtpFile();
		// tempVar3.Name = Superfile.getName();
		// tempVar3.Path = Superfile.Path;
		// file.Super = tempVar3;
		//
		// Superfile.Subs.add(file);
		//
		// if (item.FileAttributes == FileAttributes.Directory)
		// {
		// file.setName(item.FileName);
		// file.Type = FtpFile.FileType.Directory;
		// }
		// else
		// {
		// file.Type = FtpFile.FileType.File;
		// String tmp = item.FileName;
		//
		// file.setName(tmp.substring(0, tmp.lastIndexOf('.')));
		// file.Ext = tmp.substring(tmp.lastIndexOf('.') + 1);
		//
		// }
		// }
		//
		// Superfile.SyncChildren();
		// for (FtpFile item : Superfile.Subs)
		// {
		// if (item.Type == FtpFile.FileType.Directory)
		// {
		// getSubFile(conn, item);
		// }
		// }
		//
		// conn.Close();
		// }
		// catch (RuntimeException e)
		// {
		// BP.DA.Log.DebugWriteError(e.toString());
		// }

		return Superfile;
	}

	// private void getSubFile(FtpSupport.FtpConnection conn, FtpFile Superfile)
	// {
	// Superfile.Subs = new java.util.ArrayList<FtpFile>();
	// String path = Superfile.Path;
	//
	// java.util.ArrayList<FtpSupport.Win32FindData> sts = getFiles(conn, path);
	// for (FtpSupport.Win32FindData item : sts)
	// {
	//
	// FtpFile tempVar = new FtpFile();
	// tempVar.Name = item.FileName;
	// tempVar.Path = path + "\\" + item.FileName;
	// FtpFile file = tempVar;
	// FtpFile tempVar2 = new FtpFile();
	// tempVar2.Name = Superfile.getName();
	// tempVar2.Path = Superfile.Path;
	// file.Super = tempVar2;
	//
	//
	// if (item.FileAttributes == FileAttributes.Directory)
	// {
	// file.Type = FtpFile.FileType.Directory;
	// Superfile.Subs.add(file);
	// }
	// else
	// {
	// file.Type = FtpFile.FileType.File;
	// String tmp = item.FileName;
	//
	// file.setName(tmp.substring(0, tmp.lastIndexOf('.')));
	// file.Ext = tmp.substring(tmp.lastIndexOf('.') + 1);
	//
	//
	// if (file.getName().contains("Flow"))
	// {
	// Superfile.CanViewAndDown = true;
	// Superfile.Type = FtpFile.FileType.File;
	// }
	// boolean flag = false;
	// typing in Java:
	// for (var f : Superfile.Subs)
	// {
	// if (f.getName().equals(file.getName()))
	// {
	// flag = true;
	// break;
	// }
	// }
	//
	// if (!flag)
	// {
	// Superfile.Subs.add(file);
	// }
	// ;
	// }
	// }
	//
	// Superfile.SyncChildren();
	//
	// for (FtpFile item : Superfile.Subs)
	// {
	// if (item.Type == FtpFile.FileType.Directory)
	// {
	// getSubFile(conn, item);
	// }
	// }
	// }

	// private java.util.ArrayList<FtpSupport.Win32FindData>
	// getFiles(FtpSupport.FtpConnection conn, String path)
	// {
	// java.util.ArrayList<FtpSupport.Win32FindData> sts = new
	// java.util.ArrayList<Win32FindData>();
	// try
	// {
	// String tmp = conn.GetCurrentDirectory();
	// conn.SetCurrentDirectory("/");
	// conn.SetCurrentDirectory(path); //设置当前目录.
	// FtpSupport.Win32FindData[] f = conn.FindFiles();
	//
	// for (FtpSupport.Win32FindData item : f)
	// {
	// if ((new String(".")).equals(item.FileName) || (new
	// String("..")).equals(item.FileName) || (new
	// String("")).equals(item.FileName))
	// {
	// continue;
	// }
	//
	// sts.add(item);
	// }
	// }
	// catch (RuntimeException e)
	// {
	// throw new RuntimeException("FTP服务器读取目录出错：" + e.getMessage(), e);
	// }
	// return sts;
	// }

	public byte[] FlowTemplateDown(String[] FlowFileName) {
		String path = FlowFileName[0], fileName = FlowFileName[1], fileType = FlowFileName[2], cmd = FlowFileName[3];

		if (StringHelper.isNullOrEmpty(path)
				|| StringHelper.isNullOrEmpty(fileName)
				|| StringHelper.isNullOrEmpty(fileType)
				|| StringHelper.isNullOrEmpty(cmd)) {
			throw new RuntimeException("FTP服务器文件读取参数出错!");
		}

		String ip = "online.ccflow.org", userNo = "ccflowlover", pass = "ccflowlover";
		// FtpConnection conn = new FtpConnection(ip, userNo, pass);

		byte[] bytes = null;
		try {
			bytes = new byte[] {};

			// conn.SetCurrentDirectory(path); //设置当前目录.
			// FtpStream fs = conn.OpenFile(fileName, GenericRights.Read);
			// if (null != fs)
			// {
			// System.IO.MemoryStream ms = new MemoryStream();
			// fs.CopyTo(ms);
			// bytes = new byte[ms.getLength()];
			// ms.Seek(0, SeekOrigin.Begin);
			// ms.Read(bytes, 0, bytes.length);
			// }
		} catch (RuntimeException e) {
			throw new RuntimeException("FTP服务器文件读取出错：" + e.getMessage(), e);
		}
		// conn.Close();

		if (null != bytes && 0 < bytes.length && fileType.equals("XML")) {
			if (cmd.equals("INSTALL")) { // 在线安装
				if (fileName.equals("Flow.xml")) {
					// 流程安装
					// path = this.FlowTemplateUpload(bytes, fileName);
					// bytes = System.Text.Encoding.UTF8.GetBytes(path);
				} else {
					// 表单安装
					// this.UploadfileCCForm(bytes, fileName, "");
				}
			} else if (cmd.equals("DOWN")) { // 保存到本机

				// HttpContext.Current.Response.BinaryWrite(bytes);
				// string xml = System.Text.Encoding.UTF8.GetString(bytes, 0,
				// bytes.Length);
				// HttpContext.Current.Response.Write(xml);

				// fileName = HttpUtility.UrlEncode(fileName);
				// HttpContext.Current.Response.Charset = "GB2312";
				// HttpContext.Current.Response.AppendHeader("Content-Disposition",
				// "attachment;filename=" + fileName);
				// HttpContext.Current.Response.ContentEncoding =
				// System.Text.Encoding.GetEncoding("GB2312");

				// HttpContext.Current.Response.Flush();
				// HttpContext.Current.Response.End();
				// HttpContext.Current.Response.Close();
			}
		}
		return bytes;
	}

	// public DataSet TurnXmlDataSet2SLDataSet(DataSet ds)
	// {
	// DataSet myds = new DataSet();
	// for (DataTable dtXml : ds.Tables)
	// {
	// DataTable dt = new DataTable(dtXml.TableName);
	// for (DataColumn dc : dtXml.Columns)
	// {
	// DataColumn mydc = new DataColumn(dc.ColumnName, String.class);
	// dt.Columns.Add(mydc);
	// }
	// for (DataRow dr : dtXml.Rows)
	// {
	// DataRow drNew = dt.NewRow();
	// for (DataColumn dc : dtXml.Columns)
	// {
	// drNew.setValue(dc.ColumnName,dr.getValue(dc.ColumnName));
	// }
	// dt.Rows.add(drNew);
	// }
	// myds.Tables.add(dt);
	// }
	// return myds;
	// }

	/**
	 * 上传文件.
	 * 
	 * @param FileByte
	 * @param fileName
	 * @return
	 */

	// public final String UploadFile(byte[] FileByte, String fileName)
	// {
	// String path =
	// System.Web.HttpContext.Current.Request.PhysicalApplicationPath;
	//
	// String filePath = path + "\\" + fileName;
	// if (System.IO.File.Exists(filePath))
	// {
	// System.IO.File.Delete(filePath);
	// }
	//
	// //这里使用绝对路径来索引
	// FileStream stream = new FileStream(filePath, FileMode.CreateNew);
	// stream.Write(FileByte, 0, FileByte.length);
	// stream.Close();
	//
	// DataSet ds = new DataSet();
	// ds.ReadXml(filePath);
	//
	// return Silverlight.DataSetConnector.Connector.ToXml(ds);
	// }

	/**
	 * 将中文转化成拼音.
	 * 
	 * @param name
	 * @return
	 */
	public String ParseStringToPinyin(String name) {
		try {
			if (name.length() > 0) {
				name = PinYinF4jUtils.spell(name);
			}
			return name;
		} catch (java.lang.Exception e) {
			return null;
		}
	}

	/**
	 * 获取自定义表
	 * 
	 * @param ensName
	 * @return
	 */
	public String RequestSFTable(String ensName) {
		if (StringHelper.isNullOrEmpty(ensName)) {
			throw new RuntimeException("@EnsName值为null.");
		}
		DataTable dt = new DataTable();
		DataSet ds = new DataSet();
		if (ensName.contains(".")) {
			Entities ens = BP.En.ClassFactory.GetEns(ensName);
			if (ens == null) {
				ens = BP.En.ClassFactory.GetEns(ensName);
			}

			ens.RetrieveAllFromDBSource();
			dt = ens.ToDataTableField();
			ds.Tables.add(dt);
		} else {
			String sql = "SELECT No,Name FROM " + ensName;
			ds.Tables.add(BP.DA.DBAccess.RunSQLReturnTable(sql));
		}
		return DataSet.ConvertDataSetToXml(ds);
		// return Silverlight.DataSetConnector.Connector.ToXml(ds);
	}

	/**
	 * 执行功能返回信息
	 * 
	 * @param doType
	 * @param v1
	 * @param v2
	 * @param v3
	 * @param v4
	 * @param v5
	 * @return
	 */
	public String DoType(String doType, String v1, String v2, String v3,
			String v4, String v5) {
		try {

			if (doType.equals("FrmTreeUp")) // 表单树
			{
				SysFormTree sft = new SysFormTree();
				sft.DoUp();
				return null;
			} else if (doType.equals("FrmTreeDown")) // 表单树
			{
				SysFormTree sft1 = new SysFormTree();
				sft1.DoDown();
				return null;
			} else if (doType.equals("FrmUp")) {
				MapData md1 = new MapData(v1);
				md1.DoOrderDown();
				return null;
			} else if (doType.equals("FrmDown")) {
				MapData md = new MapData(v1);
				md.DoOrderDown();
				return null;
			} else if (doType.equals("AdminLogin")) {
				try {
//					if (BP.Sys.SystemConfig.getIsDebug()) {
//						return null;
//					}
					BP.Port.Emp emp = new BP.Port.Emp();
					emp.setNo(v1);
					emp.setPass(v2);
					emp.RetrieveFromDBSources();
					if (v2.equals(emp.getPass())) {
						String sid = WebUser.SignInOfGener(emp, "CH", null, true, false);
						return sid;
					}
					return "error password.";
				} catch (RuntimeException ex) {
					return ex.getMessage();
				}
			} else if (doType.equals("DeleteFrmSort")) {
				SysFormTree fs = new SysFormTree();
				fs.setNo(v1);
				fs.Delete();
				SysFormTree ft = new SysFormTree();
				ft.setNo(v1);
				ft.Delete();
				return null;
			} else if (doType.equals("DeleteFrm") || doType.equals("DelFrm")) {
				MapData md4 = new MapData();
				md4.setNo(v1);
				md4.Delete();
				return null;
			} else if (doType.equals("InitDesignerXml")) {
				String path = BP.Sys.SystemConfig.getPathOfData()
						+ "Xml/Designer.xml";
				DataSet ds = new DataSet();
				ds.readXmlm(path);
				ds = FlowDesignerUtils.TurnXmlDataSet2SLDataSet(ds);
				return DataSet.ConvertDataSetToXml(ds);
				// return Silverlight.DataSetConnector.Connector.ToXml(ds);
			} else {
				throw new RuntimeException("没有判断的，功能编号" + doType);
			}
		} catch (RuntimeException ex) {
			BP.DA.Log.DefaultLogWriteLineError("执行错误，功能编号" + doType + " error:"
					+ ex.getMessage());
			throw new RuntimeException("执行错误，功能编号" + doType + " error:"
					+ ex.getMessage());
		}
	}

	public String FlowTemplateUpload(byte[] FileByte, String fileName)
			throws IOException {
		try {
			// 文件存放路径
			String filepath = BP.Sys.SystemConfig.getPathOfTemp() + "/"
					+ fileName;
			File fileps = new File(filepath);
			// 如果文件已经存在则删除
			if (fileps.exists()) {
				fileps.delete();
			}

			String xml = new String(FileByte, "UTF-8").trim();

			// 创建文件流实例，用于写入文件
			FileOutputStream stream = new FileOutputStream(filepath);
			// 写入文件
			stream.write(xml.getBytes());
			stream.flush();
			stream.close();

			return filepath;
		} catch (RuntimeException exception) {
			return "Error: Occured on upload the file. Error Message is :\n"
					+ exception.getMessage();
		}
	}

	private String DealPK(String pk, String fromMapdata, String toMapdata) {
		if (pk.contains("*" + fromMapdata)) {
			return pk.replace("*" + toMapdata, "*" + toMapdata);
		} else {
			return pk + "*" + toMapdata;
		}
	}

//	public void LetAdminLogin() {
//		BP.Port.Emp emp = new BP.Port.Emp("admin");
//		WebUser.SignInOfGener(emp);
//	}

	// public DataSet GetWorkList(String fk_flow, String workid)
	// {
	// try
	// {
	// String sql = "";
	// String table = "ND" + Integer.parseInt(fk_flow) + "Track";
	// DataSet ds = new DataSet();
	// sql = "SELECT NDFrom, NDTo,ActionType,Msg,RDT,EmpFrom,EmpFromT FROM " +
	// table + " WHERE WorkID=" + workid + "  OR FID=" + workid +
	// "ORDER BY NDFrom ASC,NDTo ASC";
	// DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(sql);
	// mydt.TableName = "WF_Track";
	// ds.Tables.add(mydt);
	// return ds;
	// }
	// catch (RuntimeException ex)
	// {
	// BP.DA.Log.DefaultLogWriteLineError("GetDTOfWorkList发生了错误 paras:" +
	// fk_flow + "\t" + workid + ex.getMessage());
	// return null;
	// }
	// }
	
	/**
	 * 让admin 登录
	 * 
	 * @param lang
	 *            当前的语言
	 * @return 成功则为空，有异常时返回异常信息
	 */
	public String LetAdminLogin(String lang, boolean islogin) {
		try {
			if (islogin) {
				BP.Port.Emp emp = new BP.Port.Emp("admin");
				WebUser.SignInOfGener(emp, lang, "admin", true, false);
			}
		} catch (RuntimeException exception) {
			return exception.getMessage();
		}
		return "";
	}

	public String[] GetNodeIconFile() {
		// String path = ContextHolderUtils.getRequest().getRealPath("/");
		// path += "ClientBin\\NodeIcon";
		//
		// File file = new File(path);
		// //FilenameFilter fileFilter = new FilenameFilter();
		//
		// String[] files = file.list(new ImageFilter());
		//
		// for (int i = 0; i < files.length; i++)
		// {
		// String item = files[i];
		// // item = item.substring(path.length(), item.length());
		// item = item.substring(0, item.indexOf('.'));
		// files[i] = item;
		// }
		//
		// return files;
		return null;
	}

	/**
	 * 根据workID获取工作列表 FK_Node 节点ID rdt 记录日期，也是工作接受日期。 sdt 应完成日期. FK_emp 操作员编号。
	 * EmpName 操作员名称.
	 * 
	 * @param workid
	 *            workid
	 * @return
	 */
	public String GetDTOfWorkList(String fk_flow, String workid) {
		// DataSet ds = GetWorkList(fk_flow, workid);
		DataSet ds = FlowDesignerUtils.GetWorkList(fk_flow, workid);
		return DataSet.ConvertDataSetToXml(ds);
		// return Connector.ToXml(ds);
	}

	// public DataSet GetWorkList(String fk_flow, String workid)
	// {
	// try
	// {
	// String sql = "";
	// String table = "ND" + Integer.parseInt(fk_flow) + "Track";
	// DataSet ds = new DataSet();
	// sql = "SELECT NDFrom, NDTo,ActionType,Msg,RDT,EmpFrom,EmpFromT FROM " +
	// table + " WHERE WorkID=" + workid + "  OR FID=" + workid +
	// "ORDER BY NDFrom ASC,NDTo ASC";
	// DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(sql);
	// mydt.TableName = "WF_Track";
	// ds.Tables.add(mydt);
	// return ds;
	// }
	// catch (RuntimeException ex)
	// {
	// BP.DA.Log.DefaultLogWriteLineError("GetDTOfWorkList发生了错误 paras:" +
	// fk_flow + "\t" + workid + ex.getMessage());
	// return null;
	// }
	// }

	/**
	 * 创建一个节点
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param x
	 * @param y
	 * @return 返回节点编号
	 */
	public int DoNewNode(boolean isLogin, String[] param) {
		LetAdminLogin("CH", isLogin);
	
		String fk_flow = param[0];
		if (StringHelper.isNullOrEmpty(fk_flow))
			return 0;
	
		String nodeName = param[1];
		String icon = param[2];
	
		int x = (int) Double.parseDouble(param[3]), y = (int) Double
				.parseDouble(param[4]), HisRunModel = Integer
				.parseInt(param[5]);
	
		Flow fl = new Flow(fk_flow);
		if (param.length == 7) {
			int type = Integer.parseInt(param[6]);
			fl.Retrieve();
			fl.setChartType(type);
			fl.Update();
		}
	
		try {
			Node nf = fl.DoNewNode(x, y);
			nf.setICON(icon);
			nf.setName(nodeName);
			nf.setHisRunModel(RunModel.forValue(HisRunModel));
			nf.Save();
			return nf.getNodeID();
		} catch (EnumConstantNotPresentException e) {
			return 0;
		}
	}

	/**
	 * @param isLogin
	 * @param param
	 *            fk_flow,nodeName,icon,x,y,HisRunModel
	 * @return
	 * @return 返回节点编号
	 */
	public int DoNewNode1(boolean isLogin, String... param) {
		LetAdminLogin("CH", isLogin);

		String fk_flow = param[0];
		if (StringHelper.isNullOrEmpty(fk_flow)) {
			return 0;
		}

		String nodeName = param[1];
		String icon = param[2];

		int x = (int) Double.parseDouble(param[3]), y = (int) Double
				.parseDouble(param[4]), HisRunModel = Integer
				.parseInt(param[5]);

		Flow fl = new Flow(fk_flow);
		try {
			Node nf = fl.DoNewNode(x, y);
			nf.setICON(icon);
			nf.setName(nodeName);
			nf.setHisRunModel(RunModel.forValue(HisRunModel));
			nf.Save();
			return nf.getNodeID();
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	@Deprecated
	public String GetFlowBySort(String sort) {
		DataSet ds = new DataSet();
		DataTable dt = BP.DA.DBAccess
				.RunSQLReturnTable("SELECT No,Name,FK_FlowSort FROM WF_Flow");
		ds.Tables.add(dt);
		return DataSet.ConvertDataSetToXml(ds);
		// return Silverlight.DataSetConnector.Connector.ToXml(ds);
	}

	/**
	 * 删除一个连接线
	 * 
	 * @param from
	 *            从节点
	 * @param to
	 *            到节点
	 * @return
	 * @throws Exception
	 */
	public boolean DoDropLine(int from, int to) throws Exception {
		if (!Glo.getIsAdmin(WebUser.getNo())){
			throw new RuntimeException("@权限已丢失,请重新登录.");
		}
		Direction dir = new Direction();
		dir.setNode(from);
		dir.setToNode(to);
		dir.Delete();
		Conds conds = new Conds();
		conds.RetrieveByAttr(CondAttr.FK_Node, dir.getNode());
		conds.Delete();
		return true;
	}

	/**
	 * 创建一个标签
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param x
	 * @param y
	 * @return 返回标签编号
	 */
	public String DoNewLabel(String fk_flow, int x, int y, String name,
			String lableId) {
		LabNote lab = new LabNote();
		lab.setFK_Flow(fk_flow);
		lab.setX(x);
		lab.setY(y);
		if (StringHelper.isNullOrEmpty(lableId)) {
			lab.setMyPK(String.valueOf(BP.DA.DBAccess.GenerOID()));
		} else {
			lab.setMyPK(lableId);
		}
		lab.setName(name);
		try {
			lab.Save();
		} catch (java.lang.Exception e) {
		}
		return lab.getMyPK();
	}

	public String Do(String doWhat, String para1, boolean isLogin)
			throws Exception {
		// 如果admin账户登录时有错误发生，则返回错误信息
		String result = LetAdminLogin("CH", isLogin);
		if (!StringHelper.isNullOrEmpty(result)) {
			return result;
		}
		if (doWhat.equals("GenerFlowTemplete")) {
			Flow temp = new Flow(para1);
			return null;
		} else if (doWhat.equals("NewSameLevelFrmSort")) {
			SysFormTree frmSort = null;
			try {
				String[] para = para1.split("[,]", -1);
				frmSort = new SysFormTree(para[0]);
				String sameNodeNo = frmSort.DoCreateSameLevelNode().getNo();
				frmSort = new SysFormTree(sameNodeNo);
				frmSort.setName(para[1]);
				frmSort.Update();
				return null;
			} catch (RuntimeException ex) {
				return "Do Method NewFormSort Branch has a error , para:\t"
						+ para1 + ex.getMessage();
			}
		} else if (doWhat.equals("NewSubLevelFrmSort")) {
			SysFormTree frmSortSub = null;
			try {
				String[] para = para1.split("[,]", -1);
				frmSortSub = new SysFormTree(para[0]);
				String sameNodeNo = frmSortSub.DoCreateSubNode().getNo();
				frmSortSub = new SysFormTree(sameNodeNo);
				frmSortSub.setName(para[1]);
				frmSortSub.Update();
				return null;
			} catch (RuntimeException ex) {
				return "Do Method NewSubLevelFrmSort Branch has a error , para:\t"
						+ para1 + ex.getMessage();
			}
		} else if (doWhat.equals("NewSameLevelFlowSort")) {
			FlowSort fs = null;
			try {
				String[] para = para1.split("[,]", -1);
				fs = new FlowSort(para[0]);
				String sameNodeNo = fs.DoCreateSameLevelNode().getNo();
				fs = new FlowSort(sameNodeNo);
				fs.setName(para[1]);
				fs.Update();
				return fs.getNo();
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method NewSameLevelFlowSort Branch has a error , para:\t"
								+ para1 + ex.getMessage());
				return null;
			}
		} else if (doWhat.equals("NewSubFlowSort")) {

			try {
				String[] para = para1.split("[,]", -1);
				FlowSort fsSub = new FlowSort(para[0]);
				String subNodeNo = fsSub.DoCreateSubNode().getNo();
				FlowSort subFlowSort = new FlowSort(subNodeNo);
				subFlowSort.setName(para[1]);
				subFlowSort.Update();
				return subFlowSort.getNo();
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method NewSubFlowSort Branch has a error , para:\t"
								+ ex.getMessage());
				return null;
			}
		} else if (doWhat.equals("EditFlowSort")) {
			FlowSort fs = null;
			try {
				String[] para = para1.split("[,]", -1);
				fs = new FlowSort(para[0]);
				fs.setName(para[1]);
				fs.Save();
				return fs.getNo();
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method EditFlowSort Branch has a error , para:\t"
								+ para1 + ex.getMessage());
				return null;
			}
		} else if (doWhat.equals("NewFlow")) {
			Flow fl = new Flow();
			try {
				String[] ps = para1.split("[,]", -1);
				if (ps.length != 5) {
					throw new RuntimeException("@创建流程参数错误");
				}

				String fk_floSort = ps[0];
				String flowName = ps[1];
				DataStoreModel dataSaveModel = DataStoreModel.forValue(Integer
						.parseInt(ps[2]));
				String pTable = ps[3];

				String FlowMark = ps[4];

				fl.DoNewFlow(fk_floSort, flowName, dataSaveModel, pTable,
						FlowMark);
				return fl.getNo() + ";" + fl.getName();
			} catch (RuntimeException ex) {
				ex.printStackTrace();
				BP.DA.Log.DefaultLogWriteLineError("Do Method NewFlow Branch has a error , para:\t"
								+ para1 + ex.getMessage());
				return ex.getMessage();
			}
		} else if (doWhat.equals("DelFlow")) // 删除流程.
		{
			return WorkflowDefintionManager.DeleteFlowTemplete(para1);
		} else if (doWhat.equals("DelLable")) {
			LabNote ln = new LabNote(para1);
			try {
				ln.Delete();
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method DelLable Branch has a error , para:\t"
								+ para1 + ex.getMessage());
			}
			return null;
		} else if (doWhat.equals("DelFlowSort")) {
			try {
				FlowSort delfs = new FlowSort(para1);
				delfs.Delete();
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method DelFlowSort Branch has a error , para:\t"
								+ para1 + ex.getMessage());
			}
			return null;
		} else if (doWhat.equals("NewNode")) {
			try {
				Flow fl11 = new Flow(para1);
				Node node = new Node();
				node.setFK_Flow("");
				node.setX(0);
				node.setY(0);
				node.Insert();
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method NewNode Branch has a error , para:\t"
								+ para1 + ex.getMessage());
			}

			return null;
		} else if (doWhat.equals("DelNode")) {
			try {
				if (!StringHelper.isNullOrEmpty(para1)) {
					Node delNode = new Node(Integer.parseInt(para1));
					delNode.Delete();
				}
			} catch (RuntimeException ex) {
				return "err:" + ex.getMessage();

				// BP.DA.Log.DefaultLogWriteLineError("Do Method DelNode Branch has a error , para:\t"
				// + para1 + ex.Message);
			}
			return null;
		} else if (doWhat.equals("NewLab")) {
			LabNote lab = new LabNote();
			try {
				lab.setFK_Flow(para1);
				lab.setMyPK(String.valueOf(DBAccess.GenerOID()));
				lab.Insert();

			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method NewLab Branch has a error , para:\t"
								+ para1 + ex.getMessage());
			}
			return lab.getMyPK();
		} else if (doWhat.equals("DelLab")) {
			try {
				LabNote dellab = new LabNote();
				dellab.setMyPK(para1);
				dellab.Delete();
			} catch (RuntimeException ex) {
				BP.DA.Log
						.DefaultLogWriteLineError("Do Method DelLab Branch has a error , para:\t"
								+ para1 + ex.getMessage());
			}
			return null;
		} else if (doWhat.equals("GetSettings")) {
			return (String) SystemConfig.getAppSettings().get(para1);
		} else if (doWhat.equals("SaveFlowFrm")) {
			Entity en = null;
			try {
				AtPara ap = new AtPara(para1);
				String enName = ap.GetValStrByKey("EnName");
				String pk = ap.GetValStrByKey("PKVal");
				en = ClassFactory.GetEn(enName);
				en.ResetDefaultVal();

				if (en == null) {
					throw new RuntimeException("无效的类名:" + enName);
				}

				if (StringHelper.isNullOrEmpty(pk) == false) {
					en.setPKVal(pk);
					en.RetrieveFromDBSources();
				}

				while (ap.getHisHT().elements().hasMoreElements()) {
					String key = (String) ap.getHisHT().elements()
							.nextElement();
					if (key.equals("PKVal")) {
						continue;
					}
					en.SetValByKey(key, ap.getHisHT().get(key).toString()
							.replace('^', '@'));
				}
				en.Save();
				return (String) ((en.getPKVal() instanceof String) ? en
						.getPKVal() : null);
			} catch (RuntimeException ex) {
				if (en != null) {
					en.CheckPhysicsTable();
				}
				return "Error:" + ex.getMessage();
			}
		} else {
			throw null;
		}
	}

	/**
	 * 保存流程
	 * 
	 * @param fk_flow
	 * @param nodes
	 * @param dirs
	 * @param labes
	 */
	public String FlowSave(String fk_flow, String nodes, String dirs,
			String labes) {
		LetAdminLogin("CH", true);
		return WorkflowDefintionManager.SaveFlow(fk_flow, nodes, dirs, labes);
	}

	public String UploadfileCCForm(byte[] FileByte, String fileName,
			String fk_frmSort) throws IOException {
		try {
			// 文件存放路径
			String filepath = BP.Sys.SystemConfig.getPathOfTemp() + "/"
					+ fileName;
			File file = new File(filepath);
			// 如果文件已经存在则删除
			if (file.exists()) {
				file.delete();
			}

			// 创建文件流实例，用于写入文件
			FileOutputStream stream = new FileOutputStream(filepath);

			// 写入文件
			stream.write(FileByte, 0, FileByte.length);
			stream.close();

			DataSet ds = new DataSet();
			ds.readXml(filepath);

			MapData md = MapData.ImpMapData(ds);
			md.setFK_FrmSort(fk_frmSort);
			md.Update();
			return null;
		} catch (RuntimeException exception) {
			return "Error: Occured on upload the file. Error Message is :\n"
					+ exception.getMessage();
		}

	}

	public String GetConfig(String key) {
		String tmp = BP.Sys.SystemConfig.getAppSettings().get(key).toString();
		return tmp;
	}

	/**
	 * @param FK_flowSort 流程类别编号
	 * @param Path 模板文件路径
	 * @param ImportModel
	 * @param Islogin
	 *            0,1,2,3
	 * @return
	 */
	public String FlowTemplateLoad(String FK_flowSort, String path,
			int ImportModel, int SpecialFlowNo) {
		try {
			String Path = path.replace("//", "/");
			ImpFlowTempleteModel model = ImpFlowTempleteModel
					.forValue(ImportModel);
			LetAdminLogin("CH", true);
			Flow flow = null;
			if (model == ImpFlowTempleteModel.AsSpecFlowNo) {
				if (SpecialFlowNo <= 0) {
					return "指定流程编号错误";
				}

				try {
					flow = Flow.DoLoadFlowTemplate(FK_flowSort, Path, model);
				} catch (Exception e) {
				Log.DebugWriteError("WSDesignerSoapOmpl FlowTemplateLoad() " +e);
				}
			} else {
				try {
					flow = Flow.DoLoadFlowTemplate(FK_flowSort, Path, model);
				} catch (Exception e) {
					Log.DebugWriteError("WSDesignerSoapOmpl FlowTemplateLoad方法 " +e);
				}
			}
			// 执行一下修复view.
			Flow.RepareV_FlowData_View();

			return "TRUE," + FK_flowSort + "," + flow.getNo() + ","
					+ flow.getName() + "";
		} catch (RuntimeException ex) {
			return ex.getMessage();
		}
	}
	
	/**
	 * 发送消息接口. 需要与web.config中 ShortMessageWriteTo 配置才能起作用。
	 * 发送短信接口(二次开发需要重写这个接口) 
	 * @param msgPK 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人(内部帐号，可以为空.)
	 * @param sendToEmpNo 发送给(内部帐号，可以为空.)
	 * @param tel 手机号码
	 * @param msgInfo 短消息
	 * @return 是否发送成功
	 */
	public final boolean SendToWebServices(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo)
	{
		return SendToWebServices(msgPK, sender, sendToEmpNo, tel, msgInfo);
	}
	
	/**
	 * 发送邮件
	 * @param mypk 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人
	 * @param sendToEmpNo 发送给 
	 * @param email 邮件地址
	 * @param title 标题
	 * @param maildoc 内容
	 * @return
	 */
	 public final boolean SendToEmail(String mypk, String sender, String sendToEmpNo, String email, String title, String maildoc)
	 {
		return SendToEmail(mypk, sender, sendToEmpNo, email, title, maildoc);
	 }
	 
	 /**
	 * 发送叮叮
	 * @param mypk 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人
	 * @param sendToEmpNo 发送给 
	 * @param tel 手机号码
	 * @param msgInfo 短消息
	 * @return
	 */
	 public final boolean SendToDingDing(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo)
	 {
		 return SendToDingDing(mypk, sender, sendToEmpNo, tel, msgInfo);
	 }

	 /**
	 * 发送微信消息
	 * @param mypk 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人
	 * @param sendToEmpNo 发送给 
	 * @param tel 手机号码
	 * @param msgInfo 短消息
	 * @return
	 */
	 public final boolean SendToWeiXin(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo)
	 {
		 return SendToWeiXin(mypk, sender, sendToEmpNo, tel, msgInfo);
	 }
}
