package cn.jflow.controller.wf.admin.xap.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.ClassFactory;
import BP.En.Entity;
import BP.GPM.DeptDuty;
import BP.Sys.MapData;
import BP.Sys.OSModel;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumMain;
import BP.Sys.SystemConfig;
import BP.Tools.ImageFilter;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.ImpFlowTempleteModel;
import BP.WF.Node;
import BP.WF.RunModel;
import BP.WF.Template.CondAttr;
import BP.WF.Template.Conds;
import BP.WF.Template.Direction;
import BP.WF.Template.LabNote;
import BP.WF.Template.SysFormTree;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;

public class FlowDesignerUtils {

	
	public static DataSet RunSQLReturnDataSet(String sqls)
	{
		String[] strs = sqls.split("[@]", -1);
		DataSet ds = new DataSet();
		int i = 0;
		for (String sql : strs)
		{
			if (StringHelper.isNullOrEmpty(sql))
			{
				continue;
			}
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "DT" + i;
			ds.Tables.add(dt);
			i++;
		}
		return ds;
	}
	

	public static String GetTreeJsonByTable(DataTable tabel, Object pId, String rela, String idCol, String txtCol, String IsParent, String sChecked)
	{
		StringBuilder sbJson = new StringBuilder();
		if(null == rela){
			rela = "ParentNo";
		}
		if(null == idCol){
			idCol = "No";
		}
		if(null == txtCol){
			txtCol = "Name";
		}
		if(null == IsParent){
			IsParent = "IsParent";
		}
		if(null == sChecked){
			sChecked = "";
		}

		String treeJson = "";
		Map<String,Object> filterMap = new HashMap<String, Object>();
		if (tabel.Rows.size() > 0)
		{
			sbJson.append("[");
			String filer = "";
			if (pId.toString().equals(""))
			{
				filterMap.put(rela, "");
				//				filer = String.format("%1$s is null", rela);
			}
			else
			{
				filterMap.put(rela, pId);
				//				filer = String.format("%1$s='%2$s'", rela, pId);
			}


			java.util.List<DataRow> rows = new ArrayList<DataRow>();
			try {
				rows = tabel.Select(filterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (rows.size() > 0)
			{
				for (int i = 0; i < rows.size(); i++)
				{
					DataRow row = rows.get(i);


					String jNo = (String)((row.getValue(idCol) instanceof String) ? row.getValue(idCol) : null);
					String jText = (String)((row.getValue(txtCol) instanceof String) ? row.getValue(txtCol) : null);
					if (jText.length() > 25)
					{
						jText = jText.substring(0, 25) + "<img src='"+Glo.getCCFlowAppPath()+"WF/Scripts/easyUI/themes/icons/add2.png' onclick='moreText(" + jNo + ")'/>";
					}

					String jIsParent = row.getValue(IsParent).toString();
					String jState = (new String("1")).equals(jIsParent) ? "open" : "closed";
					jState = (new String("open")).equals(jState) && i == 0 ? "open" : "closed";

					filterMap = new HashMap<String, Object>();
					filterMap.put(rela, jNo);

					java.util.List<DataRow> rowChild = new ArrayList<DataRow>();
					try {
						rowChild = tabel.Select(filterMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String tmp = "{\"id\":\"" + jNo + "\",\"text\":\"" + jText;

					if ((new String("0")).equals(pId.toString()))
					{
						tmp += "\",\"attributes\":{\"IsParent\":\"" + jIsParent + "\",\"IsRoot\":\"1\"}";
					}
					else
					{
						tmp += "\",\"attributes\":{\"IsParent\":\"" + jIsParent + "\"}";
					}

					if (rowChild.size() > 0)
					{
						tmp+= ",\"checked\":" + sChecked.contains("," + jNo + ",") + ",\"state\":\"" + jState + "\"";
					}
					else
					{
						tmp+= ",\"checked\":" + sChecked.contains("," + jNo + ",");
					}

					sbJson.append(tmp);
					if (rowChild.size() > 0)
					{
						sbJson.append(",\"children\":");
						GetTreeJsonByTable(tabel, jNo, rela, idCol, txtCol, IsParent, sChecked);
					}

					sbJson.append("},");
				}
				//				sbJson = sbJson.remove(sbJson.getLength() - 1, 1);
				sbJson = sbJson.delete(sbJson.length() - 1, 1);
			}
			sbJson.append("]");
			treeJson = sbJson.toString();
		}
		return treeJson;
	}
	
	public static boolean TreeRootCheck()
	{
		OSModel model = OSModel.OneOne;
		try
		{
			// 流程树根节点校验
			String tmp = "SELECT Name FROM WF_FlowSort where ParentNo ='0'";
			tmp = DBAccess.RunSQLReturnString(tmp);
			if (StringHelper.isNullOrEmpty(tmp))
			{
				tmp = "INSERT INTO WF_FlowSort(No,Name,ParentNo,TreeNo,idx,IsDir) values('99','流程树',0,'',0,0)";
				DBAccess.RunSQL(tmp);
			}

			// 表单树根节点校验
			tmp = "SELECT Name FROM Sys_FormTree where ParentNo ='0'";
			tmp = DBAccess.RunSQLReturnString(tmp);
			if (StringHelper.isNullOrEmpty(tmp))
			{
				tmp = "INSERT INTO Sys_FormTree(No,Name,ParentNo,DBSRC,Idx,IsDir) values('99','表单树',0,'local',0,0)";
				DBAccess.RunSQL(tmp);
			}

			// 组织结构校验
			model = OSModel.forValue(Integer.parseInt(FlowDesignerUtils.GetConfig("OSModel")));
			if (model == OSModel.OneMore)
			{
				BP.GPM.Depts rootDepts = new BP.GPM.Depts("0");
				if (rootDepts == null || rootDepts.size() == 0)
				{
					BP.GPM.Dept rootDept = new BP.GPM.Dept();
					rootDept.setName("集团总部");
					rootDept.setParentNo("0");
					rootDept.setIdx(0);
					rootDept.Insert();
				}
			}
			else if (model == OSModel.OneOne)
			{
				BP.Port.Depts rootDepts = new BP.Port.Depts("0");
				if (rootDepts == null || rootDepts.size() == 0)
				{
					BP.GPM.Dept rootDept = new BP.GPM.Dept();
					rootDept.setName("集团总部");
					rootDept.setParentNo("0");
					rootDept.setIdx(0);
					rootDept.Insert();
				}
			}
			return true;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			throw new RuntimeException("流程树根节点检查错误"+e.getMessage(), e);
		}
	}
	
	
	public static DataSet GetWorkList(String fk_flow, String workid)
	{
		try
		{
			String sql = "";
			String table = "ND" + Integer.parseInt(fk_flow) + "Track";
			DataSet ds = new DataSet();
			sql = "SELECT NDFrom, NDTo,ActionType,Msg,RDT,EmpFrom,EmpFromT FROM " + table + " WHERE WorkID=" + workid + "  OR FID=" + workid + " ORDER BY NDFrom ASC,NDTo ASC";
			DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			mydt.TableName = "WF_Track";
			ds.Tables.add(mydt);
			return ds;
		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError("GetDTOfWorkList发生了错误 paras:" + fk_flow + "\t" + workid + ex.getMessage());
			return null;
		}
	}
	
	/** 
	 增加同级部门
	 @param currDeptNo 当前部门编号
	 @param attrs 新部门属性
	 @param stations 新部门关联的岗位集合，用逗号分开.
	 @param dutys 新部门关联的职务集合，用逗号分开.
	 @return 新建部门的编号
	*/
	public static String Dept_CreateSameLevel(String currDeptNo, String attrs, String stations, String dutys)
	{
		//检查部门是否存在
		if (DeptName_Check(attrs))
		{
			return "err:该部门已经存在。";
		}
		BP.GPM.Dept dept = new BP.GPM.Dept(currDeptNo);
		Object tempVar = dept.DoCreateSameLevelNode();
		BP.GPM.Dept newDept = (BP.GPM.Dept)((tempVar instanceof BP.GPM.Dept) ? tempVar : null);
		newDept.setName("new dept");
		newDept.setFK_DeptType("");

		//调用编辑部门，并保存它.
		Dept_Edit(newDept.getNo(), attrs, stations, dutys);
		return newDept.getNo();
	}
	
	/** 
	 检查部门是否存在
	 
	 @param attrs 部门名称
	 @return 
	*/
	public static boolean DeptName_Check(String attrs)
	{
		boolean isHave = false;
		String repeatName = SystemConfig.getCS_AppSettings().get("RepeatDeptName").toString();
		//允许名称重复。
		if (repeatName == null || repeatName.equals("0"))
		{
			return false;
		}

		String[] strs = attrs.split("[^]", -1);
		String deptName = "系统管理";
		for (String str : strs)
		{
			if (StringHelper.isNullOrEmpty(str))
			{
				continue;
			}

			String[] kv = str.split("[=]", -1);
			if (kv[0].equals("Name"))
			{
				deptName = kv[1];
			}
		}
		String sql = "SELECT COUNT(Name) NUM FROM Port_Dept WHERE Name='" + deptName + "'";
		int rowCount = RunSQLReturnValInt(sql);
		if (rowCount > 0)
		{
			isHave = true;
		}

		return isHave;
	}
	
	public static void Dept_Edit(String deptNo, String attrs, String stations, String dutys)
	{
		// 更新dept 信息.
		BP.GPM.Dept dept = new BP.GPM.Dept(deptNo);
		String[] strs = attrs.split("[^]", -1);
		for (String str : strs)
		{
			if (StringHelper.isNullOrEmpty(str))
			{
				continue;
			}

			String[] kv = str.split("[=]", -1);
			dept.SetValByKey(kv[0], kv[1]); //设置值.
		}
		dept.Update();

		//更新岗位对应.
		strs = stations.split("[,]", -1);
		BP.DA.DBAccess.RunSQL("DELETE Port_DeptStation WHERE FK_Dept='" + dept.getNo() + "'");
		for (String str : strs)
		{
			if (StringHelper.isNullOrEmpty(str))
			{
				continue;
			}

			BP.GPM.DeptStation ds = new BP.GPM.DeptStation();
			ds.setFK_Dept(dept.getNo());
			ds.setFK_Station(str);
			ds.Insert();
		}

		//更新职务对应.
		strs = dutys.split("[,]", -1);
		BP.DA.DBAccess.RunSQL("DELETE Port_DeptDuty WHERE FK_Dept='" + dept.getNo() + "'");
		for (String str : strs)
		{
			if (StringHelper.isNullOrEmpty(str))
			{
				continue;
			}

			DeptDuty ds = new DeptDuty();
			ds.setFK_Dept(dept.getNo());
			ds.setFK_Duty(str);
			ds.Insert();
		}
	}
	
	/** 
	 运行sql返回String.
	 @param sql
	 @return 
	*/
	public static int RunSQLReturnValInt(String sql)
	{
		return BP.DA.DBAccess.RunSQLReturnValInt(sql);
	}
	
	/** 
	 检查根节点
	 @return 
	*/
	public static String Dept_CheckRootNode()
	{
	   OSModel model= OSModel.valueOf(GetConfig("OSModel"));

		if (model == OSModel.OneMore)
		{
			BP.GPM.Depts rootDepts = new BP.GPM.Depts("0");
			if (rootDepts == null || rootDepts.size() == 0)
			{
				BP.GPM.Dept rootDept = new BP.GPM.Dept();
				rootDept.setName("集团总部");
				rootDept.setFK_DeptType("01");
				rootDept.setParentNo("0");
				rootDept.setIdx(0);
				rootDept.Insert();
			}
		}
		else if (model == OSModel.OneOne)
		{
			BP.Port.Depts rootDepts = new BP.Port.Depts("0");
			if (rootDepts == null || rootDepts.size() == 0)
			{
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
	
	public static String GetConfig(String key)
	{
		String tmp = BP.Sys.SystemConfig.getAppSettings().get(key).toString();

		return tmp;
	}
	
	public static String FlowTemplateUpload(byte[] FileByte, String fileName) throws IOException
	{
		try
		{
			//文件存放路径
			String filepath = BP.Sys.SystemConfig.getPathOfTemp() + "/" + fileName;
			File fileps = new File(filepath);
			//如果文件已经存在则删除
			if (fileps.exists())
			{
				fileps.delete();
			}
			//创建文件流实例，用于写入文件
			FileOutputStream stream = new FileOutputStream(filepath);
			//写入文件
			stream.write(FileByte, 0, FileByte.length);
			stream.flush();
			stream.close();

			//保存图片.

			return filepath;
		}
		catch (RuntimeException exception)
		{
			return "Error: Occured on upload the file. Error Message is :\n" + exception.getMessage();
		}
	}
	
	/** 
	 创建一个标签
	 @param fk_flow 流程编号
	 @param x
	 @param y
	 @return 返回标签编号
	*/
	public String DoNewLabel(String fk_flow, int x, int y, String name, String lableId)
	{
		LabNote lab = new LabNote();
		lab.setFK_Flow(fk_flow);
		lab.setX(x);
		lab.setY(y);
		if (StringHelper.isNullOrEmpty(lableId))
		{
			lab.setMyPK(String.valueOf(BP.DA.DBAccess.GenerOID()));
		}
		else
		{
			lab.setMyPK(lableId);
		}
		lab.setName(name);
		try
		{
			lab.Save();
		}
		catch (java.lang.Exception e)
		{
		}
		return lab.getMyPK();
	}
	
	
	/** 
	 删除一个连接线
	 @param from 从节点
	 @param to 到节点
	 @return 
	 * @throws Exception 
	*/
	public static boolean DoDropLine(int from, int to) throws Exception
	{
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
	 创建一个节点
	 @param fk_flow 流程编号
	 @param x
	 @param y
	 @return 返回节点编号
	*/
	public static int DoNewNode(String fk_flow, int x, int y, int HisRunModel, String nodeName, boolean isLogin)
	{
		LetAdminLogin("CH", isLogin);
		if (StringHelper.isNullOrEmpty(fk_flow))
		{
			return 0;
		}

		Flow fl = new Flow(fk_flow);
		try
		{
			Node nf = fl.DoNewNode(x, y);
			nf.setName(nodeName);
			nf.setHisRunModel(RunModel.forValue(HisRunModel));
			nf.Save();
			return nf.getNodeID();
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	
	public static String LetAdminLogin(String lang, boolean islogin)
	{
		try
		{
			if (islogin)
			{
				BP.Port.Emp emp = new BP.Port.Emp("admin");
				WebUser.SignInOfGener(emp, lang, "admin", true, false);
			}
		}
		catch (RuntimeException exception)
		{
			return exception.getMessage();
		}
		return "";
	}
	
	/** 
	 @param isLogin
	 @param param fk_flow,nodeName,icon,x,y,HisRunModel
	 @return 
	 @return 返回节点编号
	*/
	public static int DoNewNode1(boolean isLogin, String... param)
	{
		LetAdminLogin("CH", isLogin);

		String fk_flow = param[0];
		if (StringHelper.isNullOrEmpty(fk_flow))
		{
			return 0;
		}

		String nodeName = param[1];
		String icon = param[2];



		int x= (int)Double.parseDouble(param[3]), y = (int)Double.parseDouble(param[4]), HisRunModel=Integer.parseInt(param[5]);


		Flow fl = new Flow(fk_flow);
		try
		{
			Node nf = fl.DoNewNode(x, y);
			nf.setICON(icon);
			nf.setName(nodeName);
			nf.setHisRunModel(RunModel.forValue(HisRunModel));
			nf.Save();
			return nf.getNodeID();
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
	
	/** 
	 执行功能返回信息
	 @param doType
	 @param v1
	 @param v2
	 @param v3
	 @param v4
	 @param v5
	 @return 
	*/
	public static String DoType(String doType, String v1, String v2, String v3, String v4, String v5)
	{
		try
		{

			if (doType.equals("FrmTreeUp")) // 表单树
			{
					SysFormTree sft = new SysFormTree();
					sft.DoUp();
					return null;
			}
			else if (doType.equals("FrmTreeDown")) // 表单树
			{
					SysFormTree sft1 = new SysFormTree();
					sft1.DoDown();
					return null;
			}
			else if (doType.equals("FrmUp"))
			{
					MapData md1 = new MapData(v1);
					md1.DoOrderDown();
					return null;
			}
			else if (doType.equals("FrmDown"))
			{
					MapData md = new MapData(v1);
					md.DoOrderDown();
					return null;
			}
			else if (doType.equals("AdminLogin"))
			{
					try
					{
//						if (BP.Sys.SystemConfig.getIsDebug())
//						{
//							return null;
//						}

						BP.Port.Emp emp = new BP.Port.Emp();
						emp.setNo( v1);
						emp.RetrieveFromDBSources();
						if (v2.equals(emp.getPass()))
						{
							return null;
						}
						return "error password.";
					}
					catch (RuntimeException ex)
					{
						return ex.getMessage();
					}
			}
//ORIGINAL LINE: case "DeleteFrmSort":
			else if (doType.equals("DeleteFrmSort"))
			{
					SysFormTree fs = new SysFormTree();
					fs.setNo(v1);
					fs.Delete();
					SysFormTree ft = new SysFormTree();
					ft.setNo(v1);
					ft.Delete();
					return null;
			}
//ORIGINAL LINE: case "DeleteFrm":
			else if (doType.equals("DeleteFrm") || doType.equals("DelFrm"))
			{
					MapData md4 = new MapData();
					md4.setNo(v1);
					md4.Delete();
					return null;
			}
//ORIGINAL LINE: case "InitDesignerXml":
			else if (doType.equals("InitDesignerXml"))
			{
					String path = BP.Sys.SystemConfig.getPathOfData() + "Xml/Designer.xml";
					DataSet ds = new DataSet();
					ds.readXml(path);
//					ds = this.TurnXmlDataSet2SLDataSet(ds);
					return null;
					//return Silverlight.DataSetConnector.Connector.ToXml(ds);
			}
			else
			{
					throw new RuntimeException("没有判断的，功能编号" + doType);
			}
		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError("执行错误，功能编号" + doType + " error:" + ex.getMessage());
			throw new RuntimeException("执行错误，功能编号" + doType + " error:" + ex.getMessage());
		}
	}
	
	public static DataSet TurnXmlDataSet2SLDataSet(DataSet ds)
	{
		DataSet myds = new DataSet();
		for (DataTable dtXml : ds.Tables)
		{
			DataTable dt = new DataTable(dtXml.TableName);
			for (DataColumn dc : dtXml.Columns)
			{
				DataColumn mydc = new DataColumn(dc.ColumnName, String.class);
				dt.Columns.Add(mydc);
			}
			for (DataRow dr : dtXml.Rows)
			{
				DataRow drNew = dt.NewRow();
				for (DataColumn dc : dtXml.Columns)
				{
					drNew.setValue(dc.ColumnName,dr.getValue(dc.ColumnName));
				}
				dt.Rows.add(drNew);
			}
			myds.Tables.add(dt);
		}
		return myds;
	}
	
	public static String[] GetNodeIconFile()
	{
		// XAP/Admin/WF
		String path = ContextHolderUtils.getSession().getServletContext().getRealPath("../../../");
		path += "ClientBin/NodeIcon";

		File file = new File(path);
		//FilenameFilter fileFilter = new FilenameFilter();
		
		String[] files = file.list(new ImageFilter());

		for (int i = 0; i < files.length; i++)
		{
			String item = files[i];
			item = item.substring(path.length(), item.length());
			item = item.substring(item.lastIndexOf('/')+1, item.lastIndexOf('/')+1 + item.indexOf('.')-1);
			files[i] = item;
		}

		return files;
	}

	public static String SaveEnum(String enumKey, String enumLab, String cfg)
	{
		SysEnumMain sem = new SysEnumMain();
		sem.setNo(enumKey);
		if (sem.RetrieveFromDBSources() == 0)
		{
			sem.setName(enumLab);
			sem.setCfgVal(cfg);
			sem.setLang(WebUser.getSysLang());
			sem.Insert();
		}
		else
		{
			sem.setName(enumLab);
			sem.setCfgVal(cfg);
			sem.setLang(WebUser.getSysLang());
			sem.Update();
		}

		String[] strs = cfg.split("[@]", -1);
		for (String str : strs)
		{
			if (StringHelper.isNullOrEmpty(str))
			{
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
	 拖动部门改变节点父编号
	 @param currDeptNo 拖动节点
	 @param pDeptNo 拖动节点的父节点
	 @return 
	*/
	public static String Dept_DragTarget(String currDeptNo, String pDeptNo)
	{
		try
		{
			BP.GPM.Dept currDept = new BP.GPM.Dept(currDeptNo);
			currDept.setParentNo(pDeptNo);
			currDept.Update();
		}
		catch (RuntimeException ex)
		{
			return "err:" + ex.getMessage();
		}
		return null;
	}
	
	/** 
	 拖动部门进行排序
	 @param currDeptNo 拖动节点
	 @param nextDeptNo 关系节点
	 @param nextNodeNos 下面节点的编号集合
	 @param isUpNode 是否拖动节点后的上面节点
	 @return 
	*/
	public static String Dept_DragSort(String currDeptNo, String nextDeptNo, String nextNodeNos, boolean isUpNode)
	{
		try
		{
			BP.GPM.Dept currDept = new BP.GPM.Dept(currDeptNo);
			BP.GPM.Dept nextDept = new BP.GPM.Dept(nextDeptNo);
			if (isUpNode) //如果关系节点为上面的节点
			{
				//设置序号
				currDept.setIdx( nextDept.getIdx() + 1);
				currDept.Update();
			}
			else
			{
				//交换序号
				currDept.setIdx(nextDept.getIdx());
				currDept.Update();
				//下面节点全部下移
				int Idx = currDept.getIdx();
				String[] nodeNos = nextNodeNos.split("[,]", -1);
				for (String nodeNo : nodeNos)
				{
					if (StringHelper.isNullOrEmpty(nodeNo))
					{
						continue;
					}
					Idx++;
					//下面的节点下移
					nextDept = new BP.GPM.Dept(nodeNo);
					nextDept.setIdx(Idx);
					nextDept.Update();
				}
			}
		}
		catch (RuntimeException ex)
		{
			return "err:" + ex.getMessage();
		}
		return null;
	}
	
	public static String GetDirs(String dir, boolean FileOrDirecotry)
	{
		String ip = "online.ccflow.org";
		String userNo = "ccflowlover";
		String pass = "ccflowlover";

		java.util.ArrayList<String> listDir = new java.util.ArrayList<String>();
		String dirs = "";
//		try
//		{
//		   FtpConnection conn = new FtpConnection(ip, userNo, pass);
//		   java.util.ArrayList<Win32FindData> sts = getFiles(conn, dir);
//
//			for (Win32FindData item : sts)
//			{
//				if (FileOrDirecotry)
//				{
//					if (item.FileAttributes == FileAttributes.Directory)
//					{
//						listDir.add(item.FileName);
//					}
//				}
//				else if (item.FileAttributes == FileAttributes.Normal)
//				{
//					String tmp = item.FileName;
//					tmp = tmp.substring(0, tmp.lastIndexOf('.'));
//
//					if (!listDir.contains(tmp))
//					{
//						listDir.add(tmp);
//					}
//				}
//			}
//
//			for (String item : listDir)
//			{
//				dirs += item + "@";
//			}
//			if (!StringHelper.isNullOrEmpty(dirs))
//			{
//				dirs = dirs.substring(0, dirs.lastIndexOf('@'));
//			}
//
//		}
//		catch (RuntimeException e)
//		{
//			BP.DA.Log.DebugWriteError(e.toString());
//		}
		return dirs;
	}
	
	/** 
	 获取值
	 @param kev
	 @return 
	*/
	public static String CfgKey(String kev)
	{

		if (kev.equals("SendEmailPass") || kev.equals("AppCenterDSN") || kev.equals("FtpPass"))
		{
				throw new RuntimeException("@非法的访问");
		}
		else
		{
		}

		return BP.Sys.SystemConfig.getAppSettings().get("kev").toString();
	}
	
	public static int RunSQL(String sql)
	{
		return BP.DA.DBAccess.RunSQL(sql);
	}
	/** 
	 运行sqls
	 
	 @param sqls
	 @return 
	*/

	public static int RunSQLs(String sqls)
	{
		if (StringHelper.isNullOrEmpty(sqls))
		{
			return 0;
		}

		int i = 0;
		String[] strs = sqls.split("[@]", -1);
		for (String str : strs)
		{
			if (StringHelper.isNullOrEmpty(str))
			{
				continue;
			}
			i += BP.DA.DBAccess.RunSQL(str);
		}
		return i;
	}
	/** 
	 保存ens
	 
	 @param vals
	 @return 
	*/

	public static String SaveEn(String vals)
	{
		Entity en = null;
		try
		{
			AtPara ap = new AtPara(vals);
			String enName = ap.GetValStrByKey("EnName");
			String pk = ap.GetValStrByKey("PKVal");
			en = ClassFactory.GetEn(enName);
			en.ResetDefaultVal();

			if (en == null)
			{
				throw new RuntimeException("无效的类名:" + enName);
			}

			if (StringHelper.isNullOrEmpty(pk) == false)
			{
				en.setPKVal(pk);
				en.RetrieveFromDBSources();
			}

			while(ap.getHisHT().keys().hasMoreElements())
			//for (String key : )
			{
				String key2 = (String)ap.getHisHT().keys().nextElement();
				if (key2.equals("PKVal"))
				{
					continue;
				}
				en.SetValByKey(key2, ap.getHisHT().get(key2).toString().replace('#', '@'));
			}
			en.Save();
			return (String)((en.getPKVal() instanceof String) ? en.getPKVal() : null);
		}
		catch (RuntimeException ex)
		{
			if (en != null)
			{
				en.CheckPhysicsTable();
			}

			return "Error:" + ex.getMessage();
		}
	}
	/** 
	 运行sql返回table.
	 @param sql
	 @return 
	*/
	public static String RunSQLReturnTable(String sql)
	{
		DataSet ds = new DataSet();
		ds.Tables.add(BP.DA.DBAccess.RunSQLReturnTable(sql));
		return null;
	}
	
	/** 
	   运行sql返回String.
	 @param sql
	 @return 
	*/
	public static String RunSQLReturnString(String sql)
	{
		return BP.DA.DBAccess.RunSQLReturnString(sql);
	}
	
	/** 
	 运行sql返回float.
	 @param sql
	 @return 
	*/
	public static float RunSQLReturnValFloat(String sql)
	{
		try {
			return BP.DA.DBAccess.RunSQLReturnValFloat(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static String RunSQLReturnTableS(String sqls)
	{
		DataSet ds = RunSQLReturnDataSet(sqls);
		return DataSet.ConvertDataSetToXml(ds);
		//return Connector.ToXml(ds);
	}
	

	/** 
	 @param FK_flowSort 流程类别编号
	 @param Path 模板文件路径
	 @param ImportModel
	 @param Islogin 0,1,2,3
	 @return 
	*/
	public static String FlowTemplateLoad(String FK_flowSort, String Path, int ImportModel, int SpecialFlowNo)
	{
		try
		{
			ImpFlowTempleteModel model = ImpFlowTempleteModel.forValue(ImportModel);
			LetAdminLogin("CH", true);
			Flow flow = null;
			if (model == ImpFlowTempleteModel.AsSpecFlowNo)
			{
				if (SpecialFlowNo <= 0)
				{
					return "指定流程编号错误";
				}

				try {
					flow = Flow.DoLoadFlowTemplate(FK_flowSort, Path, model);
				} catch (Exception e) {
					Log.DebugWriteError("FlowDesignerUtils FlowTemplateLoad方法" +e);
				}
			}
			else
			{
				try {
					flow = Flow.DoLoadFlowTemplate(FK_flowSort, Path, model);
				} catch (Exception e) {
					Log.DebugWriteError("FlowDesignerUtils FlowTemplateLoad()" +e);
				}
			}
			//执行一下修复view.
			Flow.RepareV_FlowData_View();

			return String.format("TRUE,%1$s,%2$s,%3$s", FK_flowSort, flow.getNo(), flow.getName());
		}
		catch (RuntimeException ex)
		{
			return ex.getMessage();
		}
	}

	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}
