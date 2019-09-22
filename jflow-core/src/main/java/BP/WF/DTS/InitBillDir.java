package BP.WF.DTS;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.DTS.*;
import BP.WF.*;
import java.io.*;
import java.time.*;

public class InitBillDir extends DataIOEn
{
	/** 
	 流程时效考核
	*/
	public InitBillDir()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "<font color=green><b>创建单据目录(运行在每次更改单据文号或每年一天)</b></font>";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	/** 
	 创建单据目录
	*/
	@Override
	public void Do()
	{
		if (true) //此方法暂时排除，不需要创建目录。
		{
			return;
		}
		Depts Depts = new Depts();
		QueryObject qo = new QueryObject(Depts);
  //      qo.AddWhere("Grade", " < ", 4);
		qo.DoQuery();

		BillTemplates funcs = new BillTemplates();
		funcs.RetrieveAll();


		String path = BP.WF.Glo.getFlowFileBill();
		String year = LocalDateTime.now().getYear().toString();

		if ((new File(path)).isDirectory() == false)
		{
			(new File(path)).mkdirs();
		}

		if ((new File(path + "\\\\" + year)).isDirectory() == false)
		{
			(new File(path + "\\\\" + year)).mkdirs();
		}


		for (Dept Dept : Depts)
		{
			if ((new File(path + "\\\\" + year + "\\\\" + Dept.No)).isDirectory() == false)
			{
				(new File(path + "\\\\" + year + "\\\\" + Dept.No)).mkdirs();
			}

			for (BillTemplate func : funcs)
			{
				if ((new File(path + "\\\\" + year + "\\\\" + Dept.No + "\\\\" + func.getNo())).isDirectory() == false)
				{
					(new File(path + "\\\\" + year + "\\\\" + Dept.No + "\\\\" + func.getNo())).mkdirs();
				}
			}
		}
	}
}