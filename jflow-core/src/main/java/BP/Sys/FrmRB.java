package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 单选框
 
*/
public class FrmRB extends EntityMyPK
{

		
	/** 
	 提示
	 
	*/
	public final String getTip()
	{
		return this.GetValStringByKey(FrmRBAttr.Tip);
	}
	public final void setTip(String value)
	{
		this.SetValByKey(FrmRBAttr.Tip, value);
	}
	
	/** 
	 设置值
	 
	*/
	public final String getSetVal()
	{
		return this.GetValStringByKey(FrmRBAttr.SetVal);
	}
	public final void setSetVal(String value)
	{
		this.SetValByKey(FrmRBAttr.SetVal, value);
	}
	/** 
	 要执行的脚本
	 
	*/
	public final String getScript()
	{
		return this.GetValStringByKey(FrmRBAttr.Script);
	}
	public final void setScript(String value)
	{
		this.SetValByKey(FrmRBAttr.Script, value);
	}

	/** 
	 字段-配置信息
	 
	*/
	public final String getFieldsCfg()
	{
		return this.GetValStringByKey(FrmRBAttr.FieldsCfg);
	}
	public final void setFieldsCfg(String value)
	{
		this.SetValByKey(FrmRBAttr.FieldsCfg, value);
	}
	public final String getLab()
	{
		return this.GetValStringByKey(FrmRBAttr.Lab);
	}
	public final void setLab(String value)
	{
		this.SetValByKey(FrmRBAttr.Lab, value);
	}
	public final String getKeyOfEn()
	{
		return this.GetValStringByKey(FrmRBAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)
	{
		this.SetValByKey(FrmRBAttr.KeyOfEn, value);
	}
	public final int getIntKey()
	{
		return this.GetValIntByKey(FrmRBAttr.IntKey);
	}
	public final void setIntKey(int value)
	{
		this.SetValByKey(FrmRBAttr.IntKey, value);
	}
	/** 
	  Y
	 
	*/
	public final float getY()
	{
		return this.GetValFloatByKey(FrmRBAttr.Y);
	}
	public final void setY(float value)
	{
		this.SetValByKey(FrmRBAttr.Y, value);
	}
	public final float getX()
	{
		return this.GetValFloatByKey(FrmRBAttr.X);
	}
	public final void setX(float value)
	{
		this.SetValByKey(FrmRBAttr.X, value);
	}
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(FrmRBAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(FrmRBAttr.FK_MapData, value);
	}
	public final String getEnumKey()
	{
		return this.GetValStrByKey(FrmRBAttr.EnumKey);
	}
	public final void setEnumKey(String value)
	{
		this.SetValByKey(FrmRBAttr.EnumKey, value);
	}

		///#endregion


		
	/** 
	 单选框
	 
	*/
	public FrmRB()
	{
	}
	public FrmRB(String mypk) throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 EnMap
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_FrmRB", "单选框");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(FrmRBAttr.FK_MapData, null, "表单ID", true, false, 0, 100, 20);
		map.AddTBString(FrmRBAttr.KeyOfEn, null, "字段", true, false, 0, 30, 20);
		map.AddTBString(FrmRBAttr.EnumKey, null, "枚举值", true, false, 0, 30, 20);
		map.AddTBString(FrmRBAttr.Lab, null, "标签", true, false, 0, 90, 20);
		map.AddTBInt(FrmRBAttr.IntKey, 0, "IntKey", true, false);
		
        map.AddTBInt(MapAttrAttr.UIIsEnable, 0, "是否启用", true, false);


		map.AddTBFloat(FrmRBAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmRBAttr.Y, 5, "Y", false, false);

			//要执行的脚本.
		map.AddTBString(FrmRBAttr.Script, null, "要执行的脚本", true, false, 0, 4000, 20);
		map.AddTBString(FrmRBAttr.FieldsCfg, null, "配置信息@FieldName=Sta", true, false, 0, 4000, 20);

		map.AddTBString(FrmRBAttr.Tip, null, "选择后提示的信息", true, false, 0, 1000, 20);
		 map.AddTBString(FrmRBAttr.SetVal, null, "设置的值", true, false, 0, 200, 20);

		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, false, 0, 128, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn() + "_" + this.getIntKey());
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn() + "_" + this.getIntKey());
		return super.beforeUpdateInsertAction();
	}
}