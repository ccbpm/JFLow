package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.io.*;

/** 
 Method 的摘要说明
*/
public class LoadTemplete extends Method
{
	/** 
	 不带有参数的方法
	*/
	public LoadTemplete()
	{
		this.Title = "装载流程演示模板";
		this.Help = "为了帮助各位爱好者学习与掌握ccflow, 特提供一些流程模板与表单模板以方便学习。";
		this.Help += "@这些模板的位于" + SystemConfig.PathOfData + "\\FlowDemo\\";
		this.GroupName = "流程维护";

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
	*/
	@Override
	public boolean getIsCanDo()
	{
		if (BP.Web.WebUser.No.equals("admin"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public Object Do()
	{
		String msg = "";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理表单.
		// 调度表单文件。
		SysFormTrees fss = new SysFormTrees();
		fss.ClearTable();

		//创建root.
		SysFormTree root = new SysFormTree();
		root.No = "1";
		root.Name = "表单库";
		root.setParentNo("0");
		root.Insert();

		String frmPath = SystemConfig.PathOfWebApp + "\\SDKFlowDemo\\FlowDemo\\Form\\";
		File dirInfo = new File(frmPath);
		File[] dirs = dirInfo.GetDirectories();
		int i = 0;
		for (File item : dirs)
		{
			if (item.getPath().contains(".svn"))
			{
				continue;
			}

			String[] fls = (new File(item.getPath())).list(File::isFile);
			if (fls.length == 0)
			{
				continue;
			}

			SysFormTree fs = new SysFormTree();
			fs.No = item.getName().substring(0, 2);
			fs.Name = item.getName().substring(3);
			fs.setParentNo("1");
			fs.setIdx(i++);
			fs.Insert();

			for (String f : fls)
			{
				File info = new File(f);
				if (!info.Extension.equals(".xml"))
				{
					continue;
				}

				msg += "@开始调度表单模板文件:" + f;
				BP.DA.Log.DefaultLogWriteLineInfo("@开始调度表单模板文件:" + f);

				DataSet ds = new DataSet();
				ds.ReadXml(f);

				try
				{
					MapData md = MapData.ImpMapData(ds);
					md.FK_FrmSort = fs.No;
					md.FK_FormTree = fs.No;
					md.AppType = "0";
					md.Update();
				}
				catch (RuntimeException ex)
				{
					BP.DA.Log.DefaultLogWriteLineInfo("@装载表单模版文件:" + f + "出现错误," + ex.getMessage() + " <br> " + ex.StackTrace);

					throw new RuntimeException("@装载模版文件:" + f + "出现错误," + ex.getMessage() + " <br> " + ex.StackTrace);
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理表单.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理流程.
		FlowSorts sorts = new FlowSorts();
		sorts.ClearTable();
		dirInfo = new File(SystemConfig.PathOfWebApp + "\\SDKFlowDemo\\FlowDemo\\Flow\\");
		dirs = dirInfo.GetDirectories();

		FlowSort fsRoot = new FlowSort();
		fsRoot.No = "99";
		fsRoot.Name = "流程树";
		fsRoot.ParentNo = "0";
		fsRoot.DirectInsert();

		for (File dir : dirs)
		{
			if (dir.getPath().contains(".svn"))
			{
				continue;
			}

			String[] fls = (new File(dir.getPath())).list(File::isFile);
			if (fls.length == 0)
			{
				continue;
			}

			FlowSort fs = new FlowSort();
			fs.No = dir.getName().substring(0, 3);
			fs.Name = dir.getName().substring(3);
			fs.ParentNo = fsRoot.No;
			fs.Insert();

			for (String filePath : fls)
			{
				msg += "\t\n@开始调度流程模板文件:" + filePath;
				BP.DA.Log.DefaultLogWriteLineInfo("@开始调度流程模板文件:" + filePath);

				Flow myflow = BP.WF.Flow.DoLoadFlowTemplate(fs.No, filePath, ImpFlowTempleteModel.AsTempleteFlowNo);
				msg += "\t\n@流程:[" + myflow.Name + "]装载成功。";

				File info = new File(filePath);
				myflow.Name = info.getName().replace(".xml", "");
				if (myflow.Name.substring(2, 3).equals("."))
				{
					myflow.Name = myflow.Name.substring(3);
				}
				myflow.DirectUpdate();
			}


			//调度它的下一级目录.
			File dirSubInfo = new File(SystemConfig.PathOfWebApp + "\\SDKFlowDemo\\FlowDemo\\Flow\\" + dir.getName());
			File[] myDirs = dirSubInfo.GetDirectories();
			for (File mydir : myDirs)
			{
				if (mydir.getPath().contains(".svn"))
				{
					continue;
				}

				String[] myfls = (new File(mydir.getPath())).list(File::isFile);
				if (myfls.length == 0)
				{
					continue;
				}

				// 流程类别.
				Object tempVar = fs.DoCreateSubNode();
				FlowSort subFlowSort = tempVar instanceof FlowSort ? (FlowSort)tempVar : null;
				subFlowSort.Name = mydir.getName().substring(3);
				subFlowSort.Update();

				for (String filePath : myfls)
				{
					msg += "\t\n@开始调度流程模板文件:" + filePath;
					BP.DA.Log.DefaultLogWriteLineInfo("@开始调度流程模板文件:" + filePath);

					Flow myflow = BP.WF.Flow.DoLoadFlowTemplate(subFlowSort.No, filePath, ImpFlowTempleteModel.AsTempleteFlowNo);
					msg += "\t\n@流程:" + myflow.Name + "装载成功。";

					File info = new File(filePath);
					myflow.Name = info.getName().replace(".xml", "");
					if (myflow.Name.substring(2, 3).equals("."))
					{
						myflow.Name = myflow.Name.substring(3);
					}
					myflow.DirectUpdate();
				}
			}
		}

		//执行流程检查.
		Flows flsEns = new Flows();
		flsEns.RetrieveAll();
		for (Flow fl : flsEns)
		{
			fl.DoCheck();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理流程.



		BP.DA.Log.DefaultLogWriteLineInfo(msg);

		//删除多余的空格.
		BP.WF.DTS.DeleteBlankGroupField dts = new DeleteBlankGroupField();
		dts.Do();

		return msg;
	}
}