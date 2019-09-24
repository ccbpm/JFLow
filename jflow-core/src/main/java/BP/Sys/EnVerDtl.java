package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 部门岗位对应 的摘要说明。
*/
public class EnVerDtl extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;

	}
	/** 
	 实体名称
	 * @throws Exception 
	*/
	public final String getEnName() throws Exception
	{
		return this.GetValStringByKey(EnVerDtlAttr.EnName);
	}
	public final void setEnName(String value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.EnName, value);
	}
	/** 
	 字段
	 * @throws Exception 
	*/
	public final String getAttrKey() throws Exception
	{
		return this.GetValStringByKey(EnVerDtlAttr.AttrKey);
	}

	public final void setAttrKey(String value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.AttrKey, value);
	}
	/** 
	 版本主表PK
	 * @throws Exception 
	*/
	public final String getEnVerPK() throws Exception
	{
		return this.GetValStringByKey(EnVerDtlAttr.EnVerPK);
	}

	public final void setEnVerPK(String value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.EnVerPK, value);
	}
	/** 
	字段名
	 * @throws Exception 
	*/
	public final String getAttrName() throws Exception
	{
		return this.GetValStringByKey(EnVerDtlAttr.AttrName);
	}
	public final void setAttrName(String value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.AttrName, value);
	}



	/** 
	 旧值
	 * @throws Exception 
	*/
	public final String getOldVal() throws Exception
	{
		return this.GetValStringByKey(EnVerDtlAttr.OldVal);
	}
	public final void setOldVal(String value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.OldVal, value);
	}

	/** 
	 新值
	 * @throws Exception 
	*/
	public final String getNewVal() throws Exception
	{
		return this.GetValStringByKey(EnVerDtlAttr.NewVal);
	}
	public final void setNewVal(String value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.NewVal, value);
	}

	/** 
	 版本号
	 * @throws Exception 
	*/
	public final int getEnVer() throws Exception
	{
		return this.GetValIntByKey(EnVerDtlAttr.EnVer);
	}
	public final void setEnVer(int value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.EnVer, value);
	}


	public final String getRDT() throws Exception
	{
		return this.GetValStringByKey(EnVerDtlAttr.RDT);
	}
	public final void setRDT(String value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.RDT, value);
	}
	public final String getRec() throws Exception
	{
		return this.GetValStringByKey(EnVerDtlAttr.Rec);
	}
	public final void setRec(String value) throws Exception
	{
		SetValByKey(EnVerDtlAttr.Rec, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 扩展属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 工作部门岗位对应
	*/
	public EnVerDtl()
	{
	}

	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_EnVerDtl", "实体修改明细");
		map.Java_SetEnType(EnType.Dot2Dot); //实体类型，admin 系统管理员表，PowerAble 权限管理表,也是用户表,你要想把它加入权限管理里面请在这里设置。。

		map.IndexField = EnVerDtlAttr.EnName;



		map.AddMyPK();
		map.AddTBString(EnVerDtlAttr.EnName, null, "实体名", true, false, 0, 200, 30);
		map.AddTBString(EnVerDtlAttr.EnVerPK, null, "版本主表PK", false, false, 0, 100, 100);
		map.AddTBString(EnVerDtlAttr.AttrKey, null, "字段", false, false, 0, 100, 1);
		map.AddTBString(EnVerDtlAttr.AttrName, null, "字段名", true, false, 0, 200, 30);
		map.AddTBString(EnVerDtlAttr.OldVal, null, "旧值", true, false, 0, 100, 30);
		map.AddTBString(EnVerDtlAttr.NewVal, null, "新值", true, false, 0, 100, 30);
			//map.AddTBString(EnVerDtlAttr.EnNo, null, "选中行编号", true, false, 0, 100, 30);
		map.AddTBInt(EnVerDtlAttr.EnVer, 1, "版本号(日期)", true, false);

		map.AddTBDateTime(EnVerDtlAttr.RDT, null, "日期", true, false);
		map.AddTBString(EnVerDtlAttr.Rec, null, "版本号", true, false, 0, 100, 30);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}