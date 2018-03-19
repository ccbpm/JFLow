package BP.DTS;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Depositary;
import BP.DA.Log;
import BP.DA.LogType;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.XML.XmlEns;

public class DataToCash extends DataIOEn
{
	public DataToCash()
	{
		this.HisDoType = DoType.Especial;
		this.Title = "调度到数据到 cash 中去";
		// this.HisUserType = Web.UserType.SysAdmin;
		
		this.DefaultEveryMin = "00";
		this.DefaultEveryHH = "00";
		this.DefaultEveryDay = "00";
		this.DefaultEveryMonth = "00";
		this.Note = "";
	}
	
	@Override
	public void Do() throws Exception
	{
		Log.DebugWriteInfo("开始执行 DataToCahs ");
		String sql = "";
		String str = "";
		
		// 枚举类型放入cash.
		sql = "  SELECT DISTINCT ENUMKEY FROM SYS_ENUM ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			/*
			 * warning str = dr.getValue(0).toString();
			 */
			str = dr.getValue(0).toString();
			BP.Sys.SysEnums ses = new BP.Sys.SysEnums(str);
		}
		
		// 调度单据
		// if (SystemConfig.getSysNo() == SysNoList.WF)
		// {
		// Log.DefaultLogWriteLineInfo("单据模板");
		// sql = "SELECT URL FROM WF_NODEREFFUNC  ";
		// dt = DBAccess.RunSQLReturnTable(sql);
		// foreach (DataRow dr in dt.Rows)
		// {
		// try
		// {
		// str = Cash.GetBillStr(dr.getValue(0).ToString(),false);
		// }
		// catch (Exception ex)
		// {
		// Log.DefaultLogWriteLineInfo("@调入单据cash 出现错误：" + ex.Message);
		// }
		// }
		// }
		
		// 把类的数据放进cash.
		// entity 数据放进cash.
		/*
		 * warning java.util.ArrayList al =
		 * ClassFactory.GetObjects("BP.En.Entities"); for (Entities ens : al)
		 */
		java.util.ArrayList<Entities> al = ClassFactory
				.GetObjects("BP.En.Entities");
		for (Entities ens : al)
		{
			Depositary where;
			try
			{
				where = ens.getGetNewEntity().getEnMap()
						.getDepositaryOfEntity();
			} catch (RuntimeException ex)
			{
				Log.DefaultLogWriteLine(
						LogType.Info,
						"@在把数据放在内存时出现错误:" + ex.getMessage() + " cls="
								+ ens.toString());
				// 包含用户登录信息的map 都不取它。
				continue;
			}
			
			if (where == Depositary.None)
			{
				continue;
			}
			
			// try
			// {
			// ens.FlodInCash();
			// }
			// catch (Exception ex)
			// {
			// Log.DefaultLogWriteLine(LogType.Info, "@把数据放进 cash 中出现错误。@" +
			// ex.Message);
			// }
		}
		
		// 把xml 数据放进cash.
		/*
		 * warning al = ClassFactory.GetObjects("BP.XML.XmlEns"); for
		 * (BP.XML.XmlEns ens : al)
		 */
		java.util.ArrayList<XmlEns> als = ClassFactory
				.GetObjects("BP.XML.XmlEns");
		for (XmlEns ens : als)
		{
			try
			{
				dt = ens.GetTable();
				ens.RetrieveAll();
			} catch (RuntimeException ex)
			{
				Log.DefaultLogWriteLineError("@调度  " + ens.toString()
						+ "出现错误: " + ex.getMessage());
			}
		}
		
		Log.DefaultLogWriteLine(LogType.Info, "结束执行DataToCahs ");
	}
}