package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.pub.*;
import bp.*;
import java.util.*;

/** 
 线
*/
public class FrmLine extends EntityMyPK
{

		///属性
	public final String getBorderColorHtml() throws Exception
	{
		return PubClass.ToHtmlColor(this.getBorderColor());
	}
	public final String getBorderColor() throws Exception
	{
		return this.GetValStringByKey("BorderColor");
	}
	public final void setBorderColor(String value) throws Exception
	{
		this.SetValByKey(FrmLineAttr.BorderColor, value);
	}
	public final float getBorderWidth() throws Exception
	{
		return this.GetValFloatByKey(FrmLineAttr.BorderWidth);
	}
	public final void setBorderWidth(float value) throws Exception
	{
		this.SetValByKey(FrmLineAttr.BorderWidth, value);
	}
	/** 
	 GUID
	*/
	public final String getGUID() throws Exception
	{
		return this.GetValStrByKey(FrmLineAttr.GUID);
	}
	public final void setGUID(String value) throws Exception
	{
		this.SetValByKey(FrmLineAttr.GUID, value);
	}
	/** 
	 Y1
	*/
	public final float getY1() throws Exception
	{
		return this.GetValFloatByKey(FrmLineAttr.Y1);
	}
	public final void setY1(float value) throws Exception
	{
		this.SetValByKey(FrmLineAttr.Y1, value);
	}
	/** 
	 X1
	*/
	public final float getX1() throws Exception
	{
		return this.GetValFloatByKey(FrmLineAttr.X1);
	}
	public final void setX1(float value) throws Exception
	{
		this.SetValByKey(FrmLineAttr.X1, value);
	}
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmLineAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(FrmLineAttr.FK_MapData, value);
	}
	public final float getY2() throws Exception
	{
		return this.GetValFloatByKey(FrmLineAttr.Y2);
	}
	public final void setY2(float value) throws Exception
	{
		this.SetValByKey(FrmLineAttr.Y2, value);
	}
	public final float getX2() throws Exception
	{
		return this.GetValFloatByKey(FrmLineAttr.X2);
	}
	public final void setX2(float value) throws Exception
	{
		this.SetValByKey(FrmLineAttr.X2, value);
	}

		///


		///构造方法
	/** 
	 线
	*/
	public FrmLine()
	{
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_FrmLine", "线");
		map.IndexField = FrmImgAthDBAttr.FK_MapData;


		map.AddMyPK();
		map.AddTBString(FrmLineAttr.FK_MapData, null, "主表", true, false, 0, 100, 20);

		map.AddTBFloat(FrmLineAttr.X1, 5, "X1", true, false);
		map.AddTBFloat(FrmLineAttr.Y1, 5, "Y1", false, false);

		map.AddTBFloat(FrmLineAttr.X2, 9, "X2", false, false);
		map.AddTBFloat(FrmLineAttr.Y2, 9, "Y2", false, false);

			//不再用的两个字段,但是还不能删除.
		map.AddTBFloat("X", 9, "X", false, false);
		map.AddTBFloat("Y", 9, "Y", false, false);

		map.AddTBFloat(FrmLineAttr.BorderWidth, 1, "宽度", false, false);
		map.AddTBString(FrmLineAttr.BorderColor, "black", "颜色", true, false, 0, 30, 20);

		map.AddTBString(FrmLineAttr.GUID, null, "初始的GUID", true, false, 0, 128, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	/** 
	 是否存在相同的数据?
	 
	 @return 
	*/
	public final boolean IsExitGenerPK() throws Exception
	{
		String sql = "SELECT COUNT(*) FROM " + this.get_enMap().getPhysicsTable() + " WHERE FK_MapData='" + this.getFK_MapData() + "' AND x1=" + this.getX1() + " and x2=" + this.getX2() + " and y1=" + this.getY1() + " and y2=" + this.getY2();
		if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
		{
			return false;
		}
		return true;
	}
}