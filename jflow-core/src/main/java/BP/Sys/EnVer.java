package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.*;
import java.util.*;

/** 
 实体版本号
*/
public class EnVer extends EntityMyPK
{

		///#region 属性

	/** 
	 实体类
	 * @throws Exception 
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
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.Name, value);
	}

	/** 
	 版本号
	 * @throws Exception 
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
	 * @throws Exception 
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
	 * @throws Exception 
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
	 * @throws Exception 
	*/
	public final String getPKValue() throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.PKValue);
	}
	public final void setPKValue(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.PKValue, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 实体版本号
	*/
	public EnVer()
	{
	}

		///#endregion


		///#region 重写方法
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
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map();
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //连接到的那个数据库上. (默认的是: AppCenterDSN )
		map.setPhysicsTable("Sys_EnVer");
		map.Java_SetEnType(EnType.Admin);
		map.setEnDesc("实体版本号"); //  实体的描述.
		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.
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

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.SetValByKey("MyPK", this.getNo() + "_" + this.getPKValue());
		return super.beforeInsert();
	}
}