package bp.wf.dts;

import bp.da.*;
import bp.port.*;
import bp.en.*;
import bp.sys.*;
import bp.wf.*;

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
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		FrmLines ens = new FrmLines();
		ens.RetrieveAllFromDBSource();
		String sql = "";
		for (FrmLine item : ens.ToJavaList())
		{
			sql = "DELETE FROM " + item.getEnMap().getPhysicsTable() + " WHERE FK_MapData='" + item.getFK_MapData() + "' AND  x1=" + item.getX1() + " and x2=" + item.getX2() + " and y1=" + item.getY1() + " and y2=" + item.getY2();
			DBAccess.RunSQL(sql);
			item.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID()));
			item.Insert();
		}

		FrmLabs labs = new FrmLabs();
		labs.RetrieveAllFromDBSource();
		for (FrmLab item : labs.ToJavaList())
		{
			sql = "DELETE FROM " + item.getEnMap().getPhysicsTable() + " WHERE FK_MapData='" + item.getFK_MapData() + "' and x=" + item.getX() + " and y=" + item.getY() + " and Lab='" + item.getLab() + "'";
			DBAccess.RunSQL(sql);
			item.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID()));
			item.Insert();
		}
		return "删除成功";
	}
}