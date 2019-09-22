package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.io.*;
import java.time.*;

/** 
 Method 的摘要说明
*/
public class GenerTemplate extends Method
{
	/** 
	 不带有参数的方法
	*/
	public GenerTemplate()
	{
		this.Title = "生成流程模板与表单模板";
		this.Help = "把系统中的流程与表单转化成模板放在指定的目录下。";
		this.HisAttrs.AddTBString("Path", "C:\\ccflow.Template", "生成的路径", true, false, 1, 1900, 200);
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
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{
		String path = this.GetValStrByKey("Path") + "_" + LocalDateTime.now().toString("yy年MM月dd日HH时mm分");
		if ((new File(path)).isDirectory())
		{
			return "系统正在执行中，请稍后。";
		}

		(new File(path)).mkdirs();
		(new File(path + "\\Flow.流程模板")).mkdirs();
		(new File(path + "\\Frm.表单模板")).mkdirs();

		Flows fls = new Flows();
		fls.RetrieveAll();
		FlowSorts sorts = new FlowSorts();
		sorts.RetrieveAll();

		// 生成流程模板。
		for (FlowSort sort : sorts)
		{
			String pathDir = path + "\\Flow.流程模板\\" + sort.No + "." + sort.Name;
			(new File(pathDir)).mkdirs();
			for (Flow fl : fls)
			{
				fl.DoExpFlowXmlTemplete(pathDir);
			}
		}

		// 生成表单模板。
		for (FlowSort sort : sorts)
		{
			String pathDir = path + "\\Frm.表单模板\\" + sort.No + "." + sort.Name;
			(new File(pathDir)).mkdirs();
			for (Flow fl : fls)
			{
				String pathFlowDir = pathDir + "\\" + fl.No + "." + fl.Name;
				(new File(pathFlowDir)).mkdirs();
				Nodes nds = new Nodes(fl.No);
				for (Node nd : nds)
				{
					MapData md = new MapData("ND" + nd.getNodeID());
					System.Data.DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.No);
					ds.WriteXml(pathFlowDir + "\\" + nd.getNodeID() + "." + nd.getName() + ".Frm.xml");
				}
			}
		}

		// 独立表单模板.
		SysFormTrees frmSorts = new SysFormTrees();
		frmSorts.RetrieveAll();
		for (SysFormTree sort : frmSorts)
		{
			String pathDir = path + "\\Frm.表单模板\\" + sort.No + "." + sort.Name;
			(new File(pathDir)).mkdirs();

			MapDatas mds = new MapDatas();
			mds.Retrieve(MapDataAttr.FK_FrmSort, sort.No);
			for (MapData md : mds)
			{
				System.Data.DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.No);
				ds.WriteXml(pathDir + "\\" + md.No + "." + md.Name + ".Frm.xml");
			}
		}
		return "生成成功，请打开" + path + "。<br>如果您想共享出来请压缩后发送到template＠ccflow.org";
	}
}