package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

/** 
 数值字段
*/
public class MapAttrNum extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 文本字段参数属性.
	/** 
	 表单ID
	 * @throws Exception 
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}
	/** 
	 字段
	*/
	public final String getKeyOfEn() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}
	/** 
	 绑定的枚举ID
	*/
	public final String getUIBindKey() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.UIBindKey);
	}
	public final void setUIBindKey(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.UIBindKey, value);
	}
	/** 
	 数据类型
	*/
	public final int getMyDataType() throws Exception
	{
		return this.GetValIntByKey(MapAttrAttr.MyDataType);
	}
	public final void setMyDataType(int value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.MyDataType, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 数值字段
	*/
	public MapAttrNum()
	{
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

		Map map = new Map("Sys_MapAttr", "数值字段");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.IndexField = MapAttrAttr.FK_MapData;


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本信息.
		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

		map.AddDDLSysEnum(MapAttrAttr.MyDataType, 2, "数据类型", true, false);

		map.AddTBString(MapAttrAttr.DefVal, "0", "默认值/小数位数", true, false, 0, 200, 20);

		String help = "给该字段设置默认值:\t\r";

		help += "\t\r 1. 如果是整形就设置一个整形的数字作为默认值.";
		help += "\t\r 2. 对于float,decimal数据类型，如果设置0.0000就是标识要保留4位小数,如果是1.0000 标识保留4位小数,默认值为1.";
		map.SetHelperAlert("DefVal", help);

		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, true);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);

		map.AddBoolean("ExtIsSum", false, "是否显示合计(对从表有效)", true, true);
		map.SetHelperAlert("ExtIsSum", "如果是从表，就需要显示该从表的合计,在从表的底部.");

		map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 400, 20, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 傻瓜表单。
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrDT", "@0=跨0个单元格@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

			//文本占单元格数量
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

			//文本跨行
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);
			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);
		map.AddTBInt(MapAttrAttr.Idx, 0, "顺序号", true, false); //@李国文


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 傻瓜表单。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 执行的方法.
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "自动计算";
		rm.ClassMethodName = this.toString() + ".DoAutoFull()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "对从表列自动计算";
		rm.ClassMethodName = this.toString() + ".DoAutoFullDtlField()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "正则表达式";
		rm.ClassMethodName = this.toString() + ".DoRegularExpression()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "脚本验证";
		rm.ClassMethodName = this.toString() + ".DoInputCheck()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "事件绑函数";
		rm.ClassMethodName = this.toString() + ".BindFunction()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 执行的方法.

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 默认值
	*/
	public final String getDefVal() throws Exception
	{
		return this.GetValStrByKey(MapAttrAttr.DefVal);
	}
	public final void setDefVal(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.DefVal, value);
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		//如果没默认值.
		if (this.getDefVal().equals(""))
		{
			this.setDefVal("0");
		}

		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//是否显示合计
		attr.setIsSum(this.GetValBooleanByKey("ExtIsSum"));

		//增加保留小数位数.
		//attr.SetPara("DecimalDigits", this.GetValIntByKey("DecimalDigits"));

		attr.Update();

		return super.beforeUpdateInsertAction();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		mapAttr.RetrieveFromDBSources();
		mapAttr.Update();

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

	/** 
	 删除后清缓存
	 * @throws Exception 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		//删除相对应的rpt表中的字段
		if (this.getFK_MapData().contains("ND") == true)
		{
			String fk_mapData = this.getFK_MapData().substring(0, this.getFK_MapData().length() - 2) + "Rpt";
			String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND KeyOfEn='" + this.getKeyOfEn() + "'";
			DBAccess.RunSQL(sql);
		}

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本功能.
	/** 
	 绑定函数
	 
	 @return 
	 * @throws Exception 
	*/
	public final String BindFunction() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法执行.
	public final String DoAutoFullDtlField() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/AutoFullDtlField.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 自动计算
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoAutoFull() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/AutoFull.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 设置开窗返回值
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoPopVal() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/PopVal.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}

	/** 
	 正则表达式
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoRegularExpression() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpressionNum.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}
	/** 
	 文本框自动完成
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoTBFullCtrl() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/TBFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}
	/** 
	 设置级联
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoInputCheck() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/InputCheck.htm?FK_MapData=" + this.getFK_MapData() + "&OperAttrKey=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK() + "&DoType=New&ExtType=InputCheck";
	}
	/** 
	 扩展控件
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoEditFExtContral() throws Exception
	{
		return "../../Admin/FoolFormDesigner/EditFExtContral.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法执行.
}