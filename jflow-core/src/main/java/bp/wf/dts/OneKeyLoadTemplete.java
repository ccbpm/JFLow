package bp.wf.dts;

import bp.da.*;
import bp.port.*;
import bp.en.*;
import bp.sys.*;
import bp.web.WebUser;
import bp.wf.template.*;
import bp.wf.*;
import java.io.*;

/**
 Method 的摘要说明
 */
public class OneKeyLoadTemplete extends Method
{
	/**
	 不带有参数的方法
	 */
	public OneKeyLoadTemplete()throws Exception
	{
		this.Title = "一键恢复流程模版目录";
		this.Help = "此功能是一键备份流程的逆向操作.";
		this.Help += "@执行时请注意";
		this.Help += "@1,系统所有的流程数据、模版数据、组织解构数据、将会被删除。";
		this.Help += "@2,重新装载C:/CCFlowTemplete 的数据。";
		this.Help += "@3,此功能一般提供给ccflow的开发者用于不同的数据库之间的移植。";

		this.GroupName = "数据备份/恢复";


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
	 * @throws Exception
	 */
	@Override
	public boolean getIsCanDo() 
	{
		if (!WebUser.getNo().equals("admin"))
		{
			return false;
		}

		return true;
	}
	@Override
	public Object Do() throws Exception
	{
		String msg = "";


		///检查数据文件是否完整.
		String path = "C:/CCFlowTemplete";
		if ((new File(path)).isDirectory() == false)
		{
			msg += "@错误：约定的目录不存在服务器" + path + ",请把从ccflow备份的文件放入" + path;
		}

		//PortTables.
		String file = path + "/PortTables.xml";
		if ((new File(file)).isFile() == false)
		{
			msg += "@错误：约定的文件不存在，" + file;
		}

		//SysTables.
		file = path + "/SysTables.xml";
		if ((new File(file)).isFile() == false)
		{
			msg += "@错误：约定的文件不存在，" + file;
		}

		//FlowTables.
		file = path + "/FlowTables.xml";
		if ((new File(file)).isFile() == false)
		{
			msg += "@错误：约定的文件不存在，" + file;
		}

		/// 检查数据文件是否完整.


		///1 装载流程基础表数据.
		DataSet ds = new DataSet();
		ds.readXml(path + "/FlowTables.xml");

		//流程类别.
		FlowSorts sorts = new FlowSorts();
		sorts.ClearTable();
		DataTable dt = ds.GetTableByName("WF_FlowSort");
		for (FlowSort item : sorts.ToJavaList())
		{
			item.DirectInsert(); //插入数据.
		}

		/// 1 装载流程基础表数据.


		///2 组织结构.
		ds = new DataSet();
		ds.readXml(path + "/PortTables.xml");

		//Port_Emp.
		Emps emps = new Emps();
		emps.ClearTable();
		dt = ds.GetTableByName("Port_Emp");
		Object tempVar = QueryObject.InitEntitiesByDataTable(emps, dt, null);
		emps = tempVar instanceof Emps ? (Emps)tempVar : null;
		for (Emp item : emps.ToJavaList())
		{
			item.DirectInsert(); //插入数据.
		}

		//Depts.
		Depts depts = new Depts();
		depts.ClearTable();
		dt = ds.GetTableByName("Port_Dept");
		Object tempVar2 = QueryObject.InitEntitiesByDataTable(depts, dt, null);
		depts = tempVar2 instanceof Depts ? (Depts)tempVar2 : null;
		for (Dept item : depts.ToJavaList())
		{
			item.DirectInsert(); //插入数据.
		}

		//Stations.
		Stations stas = new Stations();
		stas.ClearTable();
		dt = ds.GetTableByName("Port_Station");
		Object tempVar3 = QueryObject.InitEntitiesByDataTable(stas, dt, null);
		stas = tempVar3 instanceof Stations ? (Stations)tempVar3 : null;
		for (Station item : stas.ToJavaList())
		{
			item.DirectInsert(); //插入数据.
		}

		//EmpDepts.
		DeptEmps eds = new DeptEmps();
		eds.ClearTable();
		dt = ds.GetTableByName("Port_DeptEmp");
		Object tempVar4 = QueryObject.InitEntitiesByDataTable(eds, dt, null);
		eds = tempVar4 instanceof DeptEmps ? (DeptEmps)tempVar4 : null;
		for (DeptEmp item : eds.ToJavaList())
		{
			item.DirectInsert(); //插入数据.
		}


		/// 2 组织结构.


		///3 恢复系统数据.
		ds = new DataSet();
		ds.readXml(path + "/SysTables.xml");

		//枚举Main.
		SysEnumMains sems = new SysEnumMains();
		sems.ClearTable();
		dt = ds.GetTableByName("Sys_EnumMain");
		Object tempVar5 = QueryObject.InitEntitiesByDataTable(sems, dt, null);
		sems = tempVar5 instanceof SysEnumMains ? (SysEnumMains)tempVar5 : null;
		for (SysEnumMain item : sems.ToJavaList())
		{
			item.DirectInsert(); //插入数据.
		}

		//枚举.
		SysEnums ses = new SysEnums();
		ses.ClearTable();
		dt = ds.GetTableByName("Sys_Enum");
		Object tempVar6 = QueryObject.InitEntitiesByDataTable(ses, dt, null);
		ses = tempVar6 instanceof SysEnums ? (SysEnums)tempVar6 : null;
		for (SysEnum item : ses.ToJavaList())
		{
			item.DirectInsert(); //插入数据.
		}

		////Sys_FormTree.
		//bp.sys.SysFormTrees sfts = new SysFormTrees();
		//sfts.ClearTable();
		//dt = ds.GetTableByName("Sys_FormTree"];
		//sfts = QueryObject.InitEntitiesByDataTable(sfts, dt, null) as SysFormTrees;
		//foreach (SysFormTree item in sfts)
		//{
		//    try
		//    {
		//       item.DirectInsert(); //插入数据.
		//    }
		//    catch
		//    {
		//    }
		//}

		/// 3 恢复系统数据.


		///4.备份表单相关数据.
		if (1 == 2)
		{
			String pathOfTables = path + "/SFTables";
			(new File(pathOfTables)).mkdirs();
			SFTables tabs = new SFTables();
			tabs.RetrieveAll();
			for (SFTable item : tabs.ToJavaList())
			{
				if (item.getNo().contains("."))
				{
					continue;
				}

				String sql = "SELECT * FROM " + item.getNo();
				ds = new DataSet();
				ds.Tables.add(DBAccess.RunSQLReturnTable(sql));
				ds.WriteXml(pathOfTables + "/" + item.getNo() + ".xml",XmlWriteMode.WriteSchema, ds);
			}
		}

		/// 4 备份表单相关数据.


		///5.恢复表单数据.
		//删除所有的流程数据.
		MapDatas mds = new MapDatas();
		mds.RetrieveAll();
		for (MapData fl : mds.ToJavaList())
		{
			//if (fl.FK_FormTree.Length > 1 || fl.FK_FrmSort.Length > 1)
			//    continue;
			fl.Delete(); //删除流程.
		}

		//清除数据.
		SysFormTrees fss = new SysFormTrees();
		fss.ClearTable();

		// 调度表单文件。
		String frmPath = path + "/Form";
		File dirInfo = new File(frmPath);
		File[] dirs = dirInfo.listFiles();
		for (File item : dirs)
		{
			if (item.getPath().contains(".svn"))
			{
				continue;
			}

			String[] fls = (new File(item.getPath())).list();
			if (fls.length == 0)
			{
				continue;
			}

			SysFormTree fs = new SysFormTree();
			fs.setNo(item.getName().substring(0, item.getName().indexOf('.')));
			fs.setName(item.getName().substring(item.getName().indexOf('.')));
			fs.setParentNo("0");
			fs.Insert();

			for (String f : fls)
			{
				try
				{
					msg += "@开始调度表单模板文件:" + f;
					File info = new File(f);
					String fileName = info.getName();
					int lastIndx = fileName.lastIndexOf('.');
					String ext = fileName.substring(lastIndx);
					if (!ext.equals(".xml"))
					{
						continue;
					}

					ds = new DataSet();
					ds.readXml(f);

					MapData md = MapData.ImpMapData(ds);
					md.setFK_FormTree(fs.getNo());
					md.Update();
				}
				catch (RuntimeException ex)
				{
					msg += "@调度失败,文件:" + f + ",异常信息:" + ex.getMessage();
				}
			}
		}
		/// 5.恢复表单数据.


		///6.恢复流程数据.
		//删除所有的流程数据.
		Flows flsEns = new Flows();
		flsEns.RetrieveAll();
		for (Flow fl : flsEns.ToJavaList())
		{
			fl.DoDelete(); //删除流程.
		}

		dirInfo = new File(path + "/Flow/");
		dirs = dirInfo.listFiles();

		//删除数据.
		FlowSorts fsRoots = new FlowSorts();
		fsRoots.ClearTable();

		//生成流程树.
		FlowSort fsRoot = new FlowSort();
		fsRoot.setNo("99");
		fsRoot.setName("流程树");
		fsRoot.setParentNo("0");
		fsRoot.DirectInsert();

		for (File dir : dirs)
		{
			if (dir.getPath().contains(".svn"))
			{
				continue;
			}

			String[] fls = (new File(dir.getPath())).list();
			if (fls.length == 0)
			{
				continue;
			}

			FlowSort fs = new FlowSort();
			fs.setNo(dir.getName().substring(0, dir.getName().indexOf('.')));
			fs.setName(dir.getName().substring(3));
			fs.setParentNo(fsRoot.getNo());
			fs.Insert();

			for (String filePath : fls)
			{
				msg += "\t\n@开始调度流程模板文件:" + filePath;
				Flow myflow = bp.wf.template.TemplateGlo.LoadFlowTemplate(fs.getNo(), filePath, ImpFlowTempleteModel.AsTempleteFlowNo);
				msg += "\t\n@流程:" + myflow.getName() + "装载成功。";

				File info = new File(filePath);
				myflow.setName(info.getName().replace(".xml", ""));
				if (myflow.getName().substring(2, 3).equals("."))
				{
					myflow.setName(myflow.getName().substring(3));
				}

				myflow.DirectUpdate();
			}
		}

		///#endregion 6.恢复流程数据.

		Log.DefaultLogWriteLineInfo(msg);

		//删除多余的空格.
		bp.wf.dts.DeleteBlankGroupField dts = new DeleteBlankGroupField();
		dts.Do();

		//执行生成签名.
		GenerSiganture gs = new GenerSiganture();
		gs.Do();

		return msg;
	}
}