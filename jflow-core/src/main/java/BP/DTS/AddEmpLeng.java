package BP.DTS;

import BP.DA.DBUrlType;
import BP.DA.Log;
import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entity;
import BP.En.Map;

public class AddEmpLeng extends DataIOEn
{
	public AddEmpLeng()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "为操作员编号长度生级";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	
	@Override
	public void Do()
	{
		String sql = "";
		String sql2 = "";
		
		java.util.ArrayList al = ClassFactory.GetObjects("BP.En.Entity");
		for (Object obj : al)
		{
			Entity en = (Entity) ((obj instanceof Entity) ? obj : null);
			Map map = en.getEnMap();
			
			try
			{
				if (map.getIsView())
				{
					continue;
				}
			} catch (java.lang.Exception e)
			{
			}
			
			String table = en.getEnMap().getPhysicsTable();
			for (Attr attr : map.getAttrs())
			{
				if (attr.getKey().indexOf("Text") != -1)
				{
					continue;
				}
				
				if (attr.getKey().equals("Rec")
						|| attr.getKey().equals("FK_Emp")
						|| attr.getUIBindKey().equals("BP.Port.Emps"))
				{
					sql += "\n update " + table + " set " + attr.getKey()
							+ "='01'||" + attr.getKey() + " WHERE length("
							+ attr.getKey() + ")=6;";
				} else if (attr.getKey().equals("Checker"))
				{
					sql2 += "\n update " + table + " set " + attr.getKey()
							+ "='01'||" + attr.getKey() + " WHERE length("
							+ attr.getKey() + ")=6;";
				}
			}
		}
		Log.DebugWriteInfo(sql);
		Log.DebugWriteInfo("===========================" + sql2);
	}
}