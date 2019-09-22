package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;

/** 
 实体版本号
*/
public class EnVer extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性

	/** 
	 实体类
	*/
	public final String getNo()
	{
		return this.GetValStrByKey(EnVerAttr.No);
	}
	public final void setNo(String value)
	{
		this.SetValByKey(EnVerAttr.No, value);
	}

	/** 
	 实体类名称
	*/
	public final String getName()
	{
		return this.GetValStrByKey(EnVerAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(EnVerAttr.Name, value);
	}

	/** 
	 版本号
	*/
	public final int getEVer()
	{
		return this.GetValIntByKey(EnVerAttr.EVer);
	}
	public final void setEVer(int value)
	{
		this.SetValByKey(EnVerAttr.EVer, value);
	}
	/** 
	 修改人
	*/
	public final String getRec()
	{
		return this.GetValStrByKey(EnVerAttr.Rec);
	}
	public final void setRec(String value)
	{
		this.SetValByKey(EnVerAttr.Rec, value);
	}

	/** 
	 修改日期
	*/
	public final String getRDT()
	{
		return this.GetValStrByKey(EnVerAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(EnVerAttr.RDT, value);
	}

	/** 
	 主键值
	*/
	public final String getPKValue()
	{
		return this.GetValStrByKey(EnVerAttr.PKValue);
	}
	public final void setPKValue(String value)
	{
		this.SetValByKey(EnVerAttr.PKValue, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 实体版本号
	*/
	public EnVer()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写方法
	@Override
	public UAC getHisUAC()
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	protected boolean beforeInsert()
	{
		this.SetValByKey("MyPK", this.getNo() + "_" + this.getPKValue());
		return super.beforeInsert();
	}
}