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

		///#region 属性
	/** 
	 实体类名称
	*/
	public final String getName()  throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.Name, value);
	}
	public final String getEnPKValue()  throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.EnPKValue);
	}
	public final void setEnPKValue(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.EnPKValue, value);
	}


	/** 
	 版本号
	*/
	public final int getVer()  throws Exception
	{
		return this.GetValIntByKey(EnVerAttr.EnVer);
	}
	public final void setVer(int value) throws Exception
	{
		this.SetValByKey(EnVerAttr.EnVer, value);
	}
	/** 
	 修改人
	*/
	public final String getRecNo()  throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.RecNo);
	}
	public final void setRecNo(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.RecNo, value);
	}
	public final String getRecName()  throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.RecName);
	}
	public final void setRecName(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.RecName, value);
	}
	public final String getMyNote()  throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.MyNote);
	}
	public final void setMyNote(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.MyNote, value);
	}
	public final String getFrmID()  throws Exception
	{
		return this.GetValStrByKey(EnVerAttr.FrmID);
	}
	public final void setFrmID(String value) throws Exception
	{
		this.SetValByKey(EnVerAttr.FrmID, value);
	}
	/** 
	 修改日期
	*/
	public final String getRDT()  throws Exception
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
	public final String getPKValue()  throws Exception
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
	public EnVer(String mypk) throws Exception 
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}


		///#endregion


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
	public bp.en.Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_EnVer", "实体版本号");
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //连接到的那个数据库上. (默认的是: AppCenterDSN )

		map.AddMyPK();

		map.AddTBString(EnVerAttr.FrmID, null, "实体类", true, false, 1, 50, 20);
		map.AddTBString(EnVerAttr.Name, null, "实体名", true, false, 0, 100, 30);
		map.AddTBString(EnVerAttr.EnPKValue, null, "主键值", true, true, 0, 40, 100);

		map.AddTBInt(EnVerAttr.EnVer, 0, "版本号", true, true);

		map.AddTBString(EnVerAttr.RecNo, null, "修改人账号", true, true, 0, 100, 30);
		map.AddTBString(EnVerAttr.RecName, null, "修改人名称", true, true, 0, 100, 30);

		map.AddTBString(EnVerAttr.MyNote, null, "备注", true, true, 0, 100, 30);
		map.AddTBDateTime(EnVerAttr.RDT, null, "创建日期", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception 
	{
		return super.beforeInsert();
	}
	@Override
	protected void afterDelete() throws Exception 
	{
		//删除数据.
		EnVerDtls dtls = new EnVerDtls();
		dtls.Delete(EnVerDtlAttr.RefPK, this.getMyPK());
		super.afterDelete();
	}
}