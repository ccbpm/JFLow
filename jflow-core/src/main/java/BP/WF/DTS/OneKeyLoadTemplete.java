package BP.WF.DTS;

import java.io.File;

import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.Entity;
import BP.En.Method;
import BP.En.QueryObject;
import BP.Port.Dept;
import BP.Port.Depts;
import BP.Port.Emp;
import BP.Port.EmpStations;
import BP.Port.Emps;
import BP.Port.Station;
import BP.Port.Stations;
import BP.Sys.MapData;
import BP.Sys.MapDatas;
import BP.Sys.OSModel;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnumMains;
import BP.Sys.SysEnums;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.ImpFlowTempleteModel;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSorts;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.SysFormTrees;

/** 
 Method 的摘要说明
 
*/
public class OneKeyLoadTemplete extends Method
{
	/** 
	 不带有参数的方法
	 
	*/
	public OneKeyLoadTemplete()
	{
		this.Title = "一键恢复流程模版目录";
		this.Help = "此功能是一键备份流程的逆向操作.";
		this.Help += "@执行时请注意";
		this.Help += "@1,系统所有的流程数据、模版数据、组织解构数据、将会被删除。";
		this.Help += "@2,重新装载C:/CCFlowTemplete 的数据。";
		this.Help += "@3,此功能一般提供给ccflow的开发者用于不同的数据库之间的移植。";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	 * @throws Exception 
	 
	*/
	@Override
	public boolean getIsCanDo() throws Exception
	{
		if ( ! BP.Web.WebUser.getNo().equals("admin"))
		{
			return false;
		}

		return true;
	}
	@Override
	public Object Do() throws Exception
	{
		String msg = "";


			///#region 检查数据文件是否完整.
		String path = "C:/CCFlowTemplete";
		File file = new File(path);
		if (file.exists() == false)
		{
			msg += "@错误：约定的目录不存在服务器" + path + ",请把从ccflow备份的文件放入" + path;
		}

		//PortTables.
		file = new File( path + "/PortTables.xml");
		if (file.exists() == false)
		{
			msg += "@错误：约定的文件不存在，" + file;
		}

		//SysTables.
		file = new File( path + "/SysTables.xml");
		if (file.exists() == false)
		{
			msg += "@错误：约定的文件不存在，" + file;
		}

		//FlowTables.
		file = new File(path + "/FlowTables.xml");
		if (file.exists() == false)
		{
			msg += "@错误：约定的文件不存在，" + file;
		}

			///#endregion 检查数据文件是否完整.


			///#region 1 装载流程基础表数据.
		DataSet ds = new DataSet();
		ds.readXml(path + "/FlowTables.xml");

		//流程类别.
		FlowSorts sorts = new FlowSorts();
		try {
			sorts.ClearTable();
			DataTable dt = ds.hashTables.get("WF_FlowSort");
			// sorts = QueryObject.InitEntitiesByDataTable(sorts, dt, null) as FlowSorts;
			for (FlowSort item : sorts.ToJavaListFs())
			{
				item.DirectInsert(); //插入数据.
			}
			
			///#endregion 1 装载流程基础表数据.
			
			
			///#region 2 组织结构.
			ds = new DataSet();
			ds.readXml(path + "/PortTables.xml");
			
			//Port_Emp.
			Emps emps = new Emps();
			emps.ClearTable();
			dt = ds.hashTables.get("Port_Emp");
			Object tempVar = QueryObject.InitEntitiesByDataTable(emps, dt, null);
			emps = (Emps)((tempVar instanceof Emps) ? tempVar : null);
			for (Emp item : emps.ToJavaList())
			{
				item.DirectInsert(); //插入数据.
			}
			
			//Depts.
			Depts depts = new Depts();
			depts.ClearTable();
			dt = ds.hashTables.get("Port_Dept");
			Object tempVar2 = QueryObject.InitEntitiesByDataTable(depts, dt, null);
			depts = (Depts)((tempVar2 instanceof Depts) ? tempVar2 : null);
			for (Dept item : depts.ToJavaList())
			{
				item.DirectInsert(); //插入数据.
			}
			
			//Stations.
			Stations stas = new Stations();
			stas.ClearTable();
			dt = ds.hashTables.get("Port_Station");
			Object tempVar3 = QueryObject.InitEntitiesByDataTable(stas, dt, null);
			stas = (Stations)((tempVar3 instanceof Stations) ? tempVar3 : null);
			for (Station item : stas.ToJavaList())
			{
				item.DirectInsert(); //插入数据.
			}
			
			
			if (BP.Sys.SystemConfig.getOSModel() == OSModel.OneMore)
			{
				//EmpDepts.
				BP.GPM.DeptEmps eds = new BP.GPM.DeptEmps();
				eds.ClearTable();
				dt = ds.hashTables.get("Port_DeptEmp");
				Object tempVar4 = QueryObject.InitEntitiesByDataTable(eds, dt, null);
				eds = (BP.GPM.DeptEmps)((tempVar4 instanceof BP.GPM.DeptEmps) ? tempVar4 : null);
				for (BP.GPM.DeptEmp item : eds.ToJavaList())
				{
					item.DirectInsert(); //插入数据.
				}
			}
			
			//EmpStations.
			EmpStations ess = new EmpStations();
			ess.ClearTable();
			dt = ds.hashTables.get("Port_EmpStation");
			Object tempVar5 = QueryObject.InitEntitiesByDataTable(ess, dt, null);
			ess = (EmpStations)((tempVar5 instanceof EmpStations) ? tempVar5 : null);
			for (Object item : ess)
			{
				((Entity) item).DirectInsert(); //插入数据.
			}
			
			///#endregion 2 组织结构.
			
			
			///#region 3 恢复系统数据.
			ds = new DataSet();
			ds.readXml(path + "/SysTables.xml");
			
			//枚举Main.
			SysEnumMains sems = new SysEnumMains();
			sems.ClearTable();
			dt = ds.hashTables.get("Sys_EnumMain");
			Object tempVar6 = QueryObject.InitEntitiesByDataTable(sems, dt, null);
			sems = (SysEnumMains)((tempVar6 instanceof SysEnumMains) ? tempVar6 : null);
			for (SysEnumMain item : sems.ToJavaList())
			{
				item.DirectInsert(); //插入数据.
			}
			
			//枚举.
			SysEnums ses = new SysEnums();
			ses.ClearTable();
			dt = ds.hashTables.get("Sys_Enum");
			Object tempVar7 = QueryObject.InitEntitiesByDataTable(ses, dt, null);
			ses = (SysEnums)((tempVar7 instanceof SysEnums) ? tempVar7 : null);
			for (SysEnum item : ses.ToJavaList())
			{
				item.DirectInsert(); //插入数据.
			}
			
		 
			
			
			///#region 5.恢复表单数据.
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
			File [] dirs = dirInfo.listFiles();
			for (File item : dirs)
			{
				if (item.getName().contains(".svn"))
				{
					continue;
				}
				
				File[] fls = item.listFiles();
				if (fls.length == 0)
				{
					continue;
				}
				
				SysFormTree fs = new SysFormTree();
				fs.setNo(item.getName().substring(0, item.getName().indexOf('.')));
				fs.setName(item.getName().substring(item.getName().indexOf('.')));
				fs.setParentNo("0");
				fs.Insert();
				
				for (File f : fls)
				{
					try
					{
						msg += "@开始调度表单模板文件:" + f;
						if (!(f.getName().indexOf(".xml") >= 0))
						{
							continue;
						}
						ds = new DataSet();
						// ds.ReadXml(f);
						ds.readXml(f.getName());
						
						MapData md = MapData.ImpMapData(ds, false);
						md.setFK_FrmSort( fs.getNo());
						md.Update();
					}
					catch (RuntimeException ex)
					{
						msg += "@调度失败,文件:"+f+",异常信息:" + ex.getMessage();
					}
				}
			}
			
			///#endregion 5.恢复表单数据.
			
			
			///#region 6.恢复流程数据.
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
			fsRoot.setParentNo ( "0");
			fsRoot.DirectInsert();
			
			for (File dir : dirs)
			{
				if (dir.getName().contains(".svn"))
				{
					continue;
				}
				
				File [] fls = dir.listFiles();
				if (fls.length == 0)
				{
					continue;
				}
				
				FlowSort fs = new FlowSort();
				fs.setNo ( dir.getName().substring(0, dir.getName().indexOf('.')));
				fs.setName(dir.getName().substring(3));
				fs.setParentNo ( fsRoot.getNo());
				fs.Insert();
				
				for (File filePath : fls)
				{
					msg += "\t\n@开始调度流程模板文件:" + filePath;
					Flow myflow = BP.WF.Flow.DoLoadFlowTemplate(fs.getNo(), filePath.getPath(), ImpFlowTempleteModel.AsTempleteFlowNo);
					msg += "\t\n@流程:" + myflow.getName() + "装载成功。";
					myflow.setName(filePath.getName().replace(".xml", ""));
					if (myflow.getName().substring(2, 3).equals("."))
					{
						myflow.setName(myflow.getName().substring(3));
					}
					
					myflow.DirectUpdate();
				}
			}
		} catch (Exception e) {
			Log.DebugWriteError("OneKeyLoadTemplete Do "+e.getMessage());
			e.printStackTrace();
		}

		///#endregion 6.恢复流程数据.

		BP.DA.Log.DefaultLogWriteLineInfo(msg);

		//删除多余的空格.
		BP.WF.DTS.DeleteBlankGroupField dts = new DeleteBlankGroupField();
		dts.Do();

		//执行生成签名.
		GenerSiganture gs = new GenerSiganture();
		gs.Do();

		return msg;
	}
}