package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 重新生成标题 的摘要说明
*/
public class ClearRepLineLab extends Method
{
	/** 
	 不带有参数的方法
	*/
	public ClearRepLineLab()
	{
		this.Title = "清除重复的表单中的Line Lab 数据";
		this.Help = "由于表单模板以前的Bug导致的标签与线重复数据。";
		this.GroupName = "系统维护";

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
		FrmLines ens = new FrmLines();
		ens.RetrieveAllFromDBSource();
		String sql = "";
		for (FrmLine item : ens)
		{
			sql = "DELETE FROM " + item.EnMap.PhysicsTable + " WHERE FK_MapData='" + item.FK_MapData + "' AND  x1=" + item.X1 + " and x2=" + item.X2 + " and y1=" + item.Y1 + " and y2=" + item.Y2;
			DBAccess.RunSQL(sql);
			item.MyPK = BP.DA.DBAccess.GenerOIDByGUID().toString();
			item.Insert();
		}

		FrmLabs labs = new FrmLabs();
		labs.RetrieveAllFromDBSource();
		for (FrmLab item : labs)
		{
			sql = "DELETE FROM " + item.EnMap.PhysicsTable + " WHERE FK_MapData='" + item.FK_MapData + "' and x=" + item.X + " and y=" + item.Y + " and Text='" + item.Text + "'";
			DBAccess.RunSQL(sql);
			item.MyPK = BP.DA.DBAccess.GenerOIDByGUID().toString();
			item.Insert();
		}
		return "删除成功";
	}
}