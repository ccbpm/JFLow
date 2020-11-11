package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.web.*;
import bp.*;
import java.util.*;

/** 
 实体版本号
*/
public class EnVer extends EntityMyPK
{

		///属性

	/** 
	 实体类
	*/
	public final String getNo() throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.No);
	}
	public final void setNo(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.No, value);
	}

	/** 
	 实体类名称
	*/
	public final String getName() throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.getName());
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.Name, value);
	}

	/** 
	 版本号
	*/
	public final int getEVer() throws Exception
	{
		return this.GetValIntByKey(EnVerAttr.EVer);
	}
	public final void setEVer(int value) throws Exception
	{
		this.SetValByKey(EnVerAttr.EVer, value);
	}
	/** 
	 修改人
	*/
	public final String getRec() throws Exception 
	{
		return this.GetValStrByKey(EnVerAttr.Rec);
	}
	public final void setRec(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.Rec, value);
	}

	/** 
	 修改日期
	*/
	public final String getRDT() throws Exception 
	{
		return this.GetValStrByKey(EnVerAttr.RDT);
	}
	public final void setRDT(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.RDT, value);
	}

	/** 
	 主键值
	*/
	public final String getPKValue() throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.PKValue);
	}
	public final void setPKValue(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.PKValue, value);
	}

		///


		///构造函数
	/** 
	 实体版本号
	*/
	public EnVer()
	{
	}

		///


		///重写方法
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_EnVer", "实体版本号");
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //连接到的那个数据库上. (默认的是: AppCenterDSN )
		map.IndexField = EnVerAttr.EVer;

		map.AddMyPK();
		map.AddTBString(EnVerAttr.No, null, "实体类", true, false, 1, 50, 20);
		map.AddTBString(EnVerAttr.Name, null, "实体名", true, false, 0, 100, 30);
		map.AddTBString(EnVerAttr.PKValue, null, "主键值", true, true, 0, 300, 100);
		map.AddTBInt(EnVerAttr.EVer, 1, "版本号", true, true);
		map.AddTBString(EnVerAttr.Rec, null, "修改人", true, true, 0, 100, 30);
		map.AddTBDateTime(EnVerAttr.RDT, null, "修改日期", true, true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.SetValByKey("MyPK", this.getNo() + "_" + this.getPKValue());
		return super.beforeInsert();
	}
}