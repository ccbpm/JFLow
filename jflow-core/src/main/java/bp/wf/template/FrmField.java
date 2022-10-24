package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.sys.*;

//表单字段方案
public class FrmField extends EntityMyPK
{

		///#region 基本属性
	/** 
	 元素类型.
	*/
	public final String getEleType()
	{
		return this.GetValStringByKey(FrmFieldAttr.EleType);
	}
	public final void setEleType(String value)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.EleType, value);
	}
	/** 
	 正则表达式
	*/
	public final String getRegularExp() throws Exception
	{
		return this.GetValStringByKey(FrmFieldAttr.RegularExp);
	}
	public final void setRegularExp(String value)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.RegularExp, value);
	}
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(FrmFieldAttr.Name);
	}
	public final void setName(String value)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.Name, value);
	}
	/** 
	 是否为空
	*/
	public final boolean isNotNull() throws Exception
	{
		return this.GetValBooleanByKey(FrmFieldAttr.IsNotNull);
	}
	public final void setNotNull(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.IsNotNull, value);
	}
	/** 
	 是否写入流程数据表
	*/
	public final boolean isWriteToFlowTable() throws Exception
	{
		return this.GetValBooleanByKey(FrmFieldAttr.IsWriteToFlowTable);
	}
	public final void setWriteToFlowTable(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.IsWriteToFlowTable, value);
	}

	/** 
	 表单ID
	*/
	public final String getFK_MapData()
	{
		return this.GetValStringByKey(FrmFieldAttr.FK_MapData);
	}
	public final void setFKMapData(String value)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.FK_MapData, value);
	}
	public final void setFK_MapData(String val)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.FK_MapData, val);
	}
	/** 
	 字段
	*/
	public final String getKeyOfEn()
	{
		return this.GetValStringByKey(FrmFieldAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)
	 {
		this.SetValByKey(FrmFieldAttr.KeyOfEn, value);
	}


	/** 
	 流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(FrmFieldAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.FK_Flow, value);
	}
	/** 
	 解决方案
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(FrmFieldAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(FrmFieldAttr.FK_Node, value);
	}
	/** 
	 是否可见
	*/
	public final boolean getUIVisible() throws Exception
	{
		return this.GetValBooleanByKey(MapAttrAttr.UIVisible);
	}
	public final void setUIVisible(boolean value)
	 {
		this.SetValByKey(MapAttrAttr.UIVisible, value);
	}
	/** 
	 是否可用
	*/
	public final boolean getUIIsEnable() throws Exception
	{
		return this.GetValBooleanByKey(MapAttrAttr.UIIsEnable);
	}
	public final void setUIIsEnable(boolean value)
	 {
		this.SetValByKey(MapAttrAttr.UIIsEnable, value);
	}


	public final String getDefVal() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.DefVal);
	}
	public final void setDefVal(String value)  throws Exception
	 {
		this.SetValByKey(MapAttrAttr.DefVal, value);
	}
	/** 
	 是否是数字签名?
	*/
	public final boolean isSigan() throws Exception
	{
		return this.GetValBooleanByKey(MapAttrAttr.IsSigan);
	}
	public final void setSigan(boolean value)  throws Exception
	 {
		this.SetValByKey(MapAttrAttr.IsSigan, value);
	}

	@Override
	public String InitMyPKVals()  {
		this.setMyPK(this.getFK_MapData() + "_" + this.getFK_Flow() + "_" + this.getFK_Node() + "_" + this.getKeyOfEn() + "_" + this.getEleType());
		return super.InitMyPKVals();
	}

		///#endregion


		///#region 构造方法
	/** 
	 表单字段方案
	*/
	public FrmField()  {
	}
	/** 
	 表单字段方案
	 
	 param
	*/
	public FrmField(String mypk)throws Exception
	{
		super(mypk);
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FrmSln", "表单字段方案");
		map.setCodeStruct("4");

		map.AddMyPK(true);

			//该表单对应的表单ID
		map.AddTBString(FrmFieldAttr.FK_Flow, null, "流程编号", true, false, 0, 4, 4);
		map.AddTBInt(FrmFieldAttr.FK_Node, 0, "节点", true, false);

		map.AddTBString(FrmFieldAttr.FK_MapData, null, "表单ID", true, false, 0, 100, 10);
		map.AddTBString(FrmFieldAttr.KeyOfEn, null, "字段", true, false, 0, 200, 20);
		map.AddTBString(FrmFieldAttr.Name, null, "字段名", true, false, 0, 500, 20);
		map.AddTBString(FrmFieldAttr.EleType, null, "类型", true, false, 0, 20, 20);

			//控制内容.
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可用", true, true);
		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见", true, true);
		map.AddBoolean(MapAttrAttr.IsSigan, false, "是否签名", true, true);

			// Add 2013-12-26.
		map.AddTBInt(FrmFieldAttr.IsNotNull, 0, "是否为空", true, false);
		map.AddTBString(FrmFieldAttr.RegularExp, null, "正则表达式", true, false, 0, 500, 20);

			// 是否写入流程表? 2014-01-26，如果是，则首先写入该节点的数据表，然后copy到流程数据表里
			// 在节点发送时有ccflow自动写入，写入目的就是为了
		map.AddTBInt(FrmFieldAttr.IsWriteToFlowTable, 0, "是否写入流程表", true, false);
		map.AddTBInt(FrmFieldAttr.IsWriteToGenerWorkFlow, 0, "是否写入流程注册表", true, false);

			//map.AddDDLSysEnum(FrmFieldAttr.IsWriteToFlowTable, 0, "写入规则", true, true, FrmFieldAttr.IsWriteToFlowTable,
			  //  "@0=不写入@1=写入流程数据表@2=写入流程注册表@3=写入全部");

		map.AddBoolean(MapAttrAttr.IsSigan, false, "是否签名", true, true);
		map.AddTBString(MapAttrAttr.DefVal, null, "默认值", true, false, 0, 200, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception {
		if (DataType.IsNullOrEmpty(this.getEleType()))
		{
			this.setEleType("Field");
		}

		if (this.getEleType().equals("Field") == true)
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getFK_Node() + "_" + this.getKeyOfEn());
		}


		return super.beforeInsert();
	}
}