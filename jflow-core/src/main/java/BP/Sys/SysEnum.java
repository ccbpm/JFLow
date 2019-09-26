package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.WebUser;

import java.util.*;

/** 
 SysEnum
*/
public class SysEnum extends EntityMyPK
{
	/** 
	 得到一个String By LabKey.
	 
	 @param EnumKey
	 @param intKey
	 @return 
	 * @throws Exception 
	*/
	public static String GetLabByPK(String EnumKey, int intKey) throws Exception
	{
		SysEnum en = new SysEnum(EnumKey, intKey);
		return en.getLab();
	}


		///#region 实现基本的方方法
	/** 
	 标签
	 * @throws Exception 
	*/
	public final String getLab() throws Exception
	{
	  return this.GetValStringByKey(SysEnumAttr.Lab);
	}
	public final void setLab(String value) throws Exception
	{
		this.SetValByKey(SysEnumAttr.Lab, value);
	}
	/** 
	 标签
	 * @throws Exception 
	*/
	public final String getLang() throws Exception
	{
		return this.GetValStringByKey(SysEnumAttr.Lang);
	}
	public final void setLang(String value) throws Exception
	{
		this.SetValByKey(SysEnumAttr.Lang, value);
	}
	/** 
	 Int val
	 * @throws Exception 
	*/
	public final int getIntKey() throws Exception
	{
		return this.GetValIntByKey(SysEnumAttr.IntKey);
	}
	public final void setIntKey(int value) throws Exception
	{
		this.SetValByKey(SysEnumAttr.IntKey, value);
	}
	/** 
	 EnumKey
	 * @throws Exception 
	*/
	public final String getEnumKey() throws Exception
	{
		return this.GetValStringByKey(SysEnumAttr.EnumKey);
	}
	public final void setEnumKey(String value) throws Exception
	{
		this.SetValByKey(SysEnumAttr.EnumKey, value);
	}
	///// <summary>
	///// 风格
	///// </summary>
	//public  string  Style
	//{
	//    get
	//    {
	//        return this.GetValStringByKey(SysEnumAttr.Style);
	//    }
	//    set
	//    {
	//        this.SetValByKey(SysEnumAttr.Style,value);
	//    }
	//}


		///#endregion


		///#region 构造方法
	/** 
	 SysEnum
	*/
	public SysEnum()
	{
	}
	/** 
	 税务编号
	 
	 @param _No 编号
	 * @throws Exception 
	*/
	public SysEnum(String enumKey, int val) throws Exception
	{
		this.setEnumKey(enumKey);
		this.setLang(WebUser.getSysLang());
		this.setIntKey(val);
		this.setMyPK(this.getEnumKey() + "_" + this.getLang() + "_" + this.getIntKey());
		int i = this.RetrieveFromDBSources();
		if (i == 0)
		{
			i = this.Retrieve(SysEnumAttr.EnumKey, enumKey, SysEnumAttr.Lang, WebUser.getSysLang(), SysEnumAttr.IntKey, this.getIntKey());
			SysEnums ses = new SysEnums();
			ses.Full(enumKey);
			if (i == 0)
			{
				//尝试注册系统的枚举的配置.
				BP.Sys.SysEnums myee = new SysEnums(enumKey);
				throw new RuntimeException("@ EnumKey=" + getEnumKey() + " Val=" + val + " Lang=" + WebUser.getSysLang() + " ...Error");
			}
		}
	}
	public SysEnum(String enumKey, String Lang, int val) throws Exception
	{
		this.setEnumKey(enumKey);
		this.setLang(Lang);
		this.setIntKey(val);
		this.setMyPK(this.getEnumKey() + "_" + this.getLang() + "_" + this.getIntKey());
		int i = this.RetrieveFromDBSources();
		if (i == 0)
		{
			i = this.Retrieve(SysEnumAttr.EnumKey, enumKey, SysEnumAttr.Lang, Lang, SysEnumAttr.IntKey, this.getIntKey());

			SysEnums ses = new SysEnums();
			ses.Full(enumKey);

			if (i == 0)
			{
				throw new RuntimeException("@ EnumKey=" + enumKey + " Val=" + val + " Lang=" + Lang + " Error");
			}
		}
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
		Map map = new Map("Sys_Enum", "枚举数据");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.AddMyPK();

		map.AddTBString(SysEnumAttr.Lab, null, "Lab", true, false, 1, 300, 8);
		map.AddTBString(SysEnumAttr.EnumKey, null, "EnumKey", true, false, 1, 100, 8);
		map.AddTBInt(SysEnumAttr.IntKey, 0, "Val", true, false);
		map.AddTBString(SysEnumAttr.Lang, "CH", "语言", true, false, 0, 10, 8);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (this.getLang() == null && this.getLang().equals(""))
		{
			this.setLang(WebUser.getSysLang());
		}

		this.setMyPK(this.getEnumKey() + "_" + this.getLang() + "_" + this.getIntKey());
		return super.beforeUpdateInsertAction();
	}

	/** 
	 枚举类型新增保存后在Frm_RB中增加新的枚举值
	 * @throws Exception 
	*/
	@Override
	protected void afterInsert() throws Exception
	{
		//获取引用枚举的表单
		String sql = " select  distinct(FK_MapData)from Sys_FrmRB where EnumKey='" + this.getEnumKey() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			super.afterInsert();
			return;
		}


		for (DataRow dr : dt.Rows)
		{
			if (DataType.IsNullOrEmpty(dr.get(0).toString()))
			{
				continue;
			}
			String fk_mapdata = dr.get(0).toString();
			String mypk = fk_mapdata + "_" + this.getEnumKey() + "_" + this.getIntKey();
			FrmRB frmrb = new FrmRB();
			if (frmrb.IsExit("MyPK", mypk) == true)
			{
				frmrb.setLab(this.getLab());
				frmrb.Update();
				continue;
			}
			//获取mapAttr 
			MapAttr mapAttr = new MapAttr(fk_mapdata + "_" + this.getEnumKey());
			int RBShowModel = mapAttr.GetParaInt("RBShowModel");
			FrmRB frmrb1 = new FrmRB(fk_mapdata + "_" + this.getEnumKey() + "_0");


			frmrb.setFK_MapData(fk_mapdata);
			frmrb.setKeyOfEn(this.getEnumKey());
			frmrb.setEnumKey(this.getEnumKey());
			frmrb.setLab(this.getLab());
			frmrb.setIntKey(this.getIntKey());
			if (RBShowModel == 0)
			{
				frmrb.setX(frmrb1.getX());
				frmrb.setY(frmrb1.getY() + 40);
			}
			if (RBShowModel == 3)
			{
				frmrb.setX(frmrb1.getX() + 100);
				frmrb.setY(frmrb1.getY());
			}
			frmrb.Insert();

		}

		 super.afterInsert();
	}
}