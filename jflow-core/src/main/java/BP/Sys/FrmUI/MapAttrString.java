package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

/** 
 实体属性
*/
public class MapAttrString extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 文本字段参数属性.
	public final boolean getIsSupperText()
	{
		return this.GetValBooleanByKey(MapAttrAttr.IsSupperText);
	}
	public final void setIsSupperText(boolean value)
	{
		this.SetValByKey(MapAttrAttr.IsSupperText, value);
	}
	public final boolean getIsRichText()
	{
		return this.GetValBooleanByKey(MapAttrAttr.IsRichText);
	}
	public final void setIsRichText(boolean value)
	{
		this.SetValByKey(MapAttrAttr.IsRichText, value);
	}
	/** 
	 表单ID
	*/
	public final String getFK_MapData()
	{
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}
	/** 
	 字段
	*/
	public final String getKeyOfEn()
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)
	{
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
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
	 实体属性
	*/
	public MapAttrString()
	{
	}
	/** 
	 实体属性
	*/
	public MapAttrString(String myPK)
	{
		this.setMyPK(myPK);
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

		Map map = new Map("Sys_MapAttr", "文本字段");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本字段信息.
		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20, true);

		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

			//默认值.
		String sql = "SELECT No,Name FROM Sys_GloVar WHERE GroupKey='DefVal'";
			//显示的分组.
		map.AddDDLSQL("ExtDefVal", "0", "系统默认值", sql, true);

		map.AddTBString(MapAttrAttr.DefVal, null, "默认值表达式", true, false, 0, 400, 20, true);

		map.AddTBInt(MapAttrAttr.MinLen, 0, "最小长度", true, false);
		map.AddTBInt(MapAttrAttr.MaxLen, 50, "最大长度", true, false);

		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, false);

			//map.AddTBFloat("ExtRows", 1, "文本框行数(决定高度)", true, false);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
		map.AddBoolean(MapAttrAttr.IsRichText, false, "是否富文本？", true, true);
		map.AddBoolean(MapAttrAttr.IsSupperText, false, "是否大块文本？(是否该字段存放的超长字节字段)", true, true, true);
		map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 400, 20, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本字段信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 傻瓜表单
			//单元格数量 2013-07-24 增加
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrDT", "@0=跨0个单元格@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨4个单元格@6=跨4个单元格");

			//文本占单元格数量
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨4个单元格@6=跨4个单元格");

			//文本跨行
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);

			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);

		map.AddDDLSysEnum(MapAttrAttr.IsSigan, 0, "签名模式", true, true, MapAttrAttr.IsSigan, "@0=无@1=图片签名@2=山东CA@3=广东CA@4=图片盖章");

		map.AddTBInt(MapAttrAttr.Idx, 0, "顺序号", true, false); //@李国文
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 傻瓜表单

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本功能.
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "正则表达式";
		rm.ClassMethodName = this.toString() + ".DoRegularExpression()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "文本框自动完成";
		rm.ClassMethodName = this.toString() + ".DoTBFullCtrl2019()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "脚本验证";
		rm.ClassMethodName = this.toString() + ".DoInputCheck()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "事件绑函数";
		rm.ClassMethodName = this.toString() + ".BindFunction()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "超链接";
		rm.ClassMethodName = this.toString() + ".DoLink()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "快速录入";
		rm.ClassMethodName = this.toString() + ".DoFastEnter()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "全局默认值";
		rm.ClassMethodName = this.toString() + ".DoDefVal()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "字段重命名";
		rm.ClassMethodName = this.toString() + ".DoRenameField()";
		rm.getHisAttrs().AddTBString("key1", "@KeyOfEn", "字段重命名为?", true, false, 0, 100, 100);
		rm.RefMethodType = RefMethodType.Func;
		rm.Warning = "如果是节点表单，系统就会把该流程上的所有同名的字段都会重命名，包括NDxxxRpt表单。";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "Pop返回值";
		rm.ClassMethodName = this.toString() + ".DoPop2019()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本功能.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 输入多选.
		rm = new RefMethod();
		rm.GroupName = "输入内容多选";
		rm.Title = "小范围多选(combox)";
		rm.ClassMethodName = this.toString() + ".DoMultipleChoiceSmall()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "输入内容多选";
		rm.Title = "搜索多选";
		rm.ClassMethodName = this.toString() + ".DoMultipleChoiceSearch()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 输入多选

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region Pop 自动填充.
			//rm = new RefMethod();
			//rm.GroupName = "Pop自动填充";
			//rm.Title = "简单列表模式";
			//rm.ClassMethodName = this.ToString() + ".DoPopFullCtrl()";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.GroupName = "Pop自动填充";
			//rm.Title = "多条件查询列表模式";
			//rm.ClassMethodName = this.ToString() + ".DoPopFullCtrlAdv()";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion Pop 自动填充.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 高级设置.
		rm = new RefMethod();
		rm.Title = "扩展控件";
		rm.ClassMethodName = this.toString() + ".DoEditFExtContral()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "扩展控件2019";
		rm.ClassMethodName = this.toString() + ".DoEditFExtContral2019()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批处理";
		rm.ClassMethodName = this.toString() + ".DoEleBatch()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 执行的方法.

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 字段分组查询语句
	*/
	public static String getSQLOfGroupAttr()
	{
		return "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'  AND (CtrlType IS NULL OR CtrlType='')  ";
	}
	/** 
	 删除
	*/
	@Override
	protected void afterDelete()
	{
		//删除可能存在的关联属性.
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + this.getFK_MapData() + "' AND KeyOfEn='" + this.getKeyOfEn() + "T'";
		DBAccess.RunSQL(sql);

		//删除相关的图片信息.
		if (DBAccess.IsExitsTableCol("Sys_FrmImg", "KeyOfEn") == true)
		{
			sql = "DELETE FROM Sys_FrmImg WHERE FK_MapData='" + this.getFK_MapData() + "' AND KeyOfEn='" + this.getKeyOfEn() + "T'";
		}
		DBAccess.RunSQL(sql);

		//删除相对应的rpt表中的字段
		if (this.getFK_MapData().contains("ND") == true)
		{
			String fk_mapData = this.getFK_MapData().substring(0, this.getFK_MapData().length() - 2) + "Rpt";
			sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND( KeyOfEn='" + this.getKeyOfEn() + "T' OR KeyOfEn='" + this.getKeyOfEn() + "')";
			DBAccess.RunSQL(sql);
		}

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterDelete();
	}


	@Override
	protected void afterInsertUpdateAction()
	{
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		mapAttr.RetrieveFromDBSources();
		mapAttr.Update();

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本功能.

	public final String DoRenameField(String newField)
	{
		String sql = "";
		if (this.getFK_MapData().indexOf("ND") == 0)
		{
			String strs = this.getFK_MapData().replace("ND", "");
			strs = strs.substring(0, strs.length() - 2);

			String rptTable = "ND" + strs + "Rpt";
			MapDatas mds = new MapDatas();
			mds.Retrieve(MapDataAttr.PTable, rptTable);

			for (MapData item : mds)
			{
				sql = "UPDATE Sys_MapAttr SET KeyOfEn='" + newField + "',  MyPK='" + newField + "_" + item.getNo() + " WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + item.getNo() + "'";
				DBAccess.RunSQL(sql);
			}
		}
		else
		{
			sql = "UPDATE Sys_MapAttr SET KeyOfEn='" + newField + "', MyPK='" + newField + "_" + this.getFK_MapData() + "  WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + this.getFK_MapData() + "'";
			DBAccess.RunSQL(sql);
		}

		return "重名称成功,如果是自由表单，请关闭表单设计器重新打开.";
	}
	/** 
	 绑定函数
	 
	 @return 
	*/
	public final String BindFunction()
	{
		return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 快速录入
	 
	 @return 
	*/
	public final String DoFastEnter()
	{
		return "../../Admin/FoolFormDesigner/MapExt/FastInput.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 快速录入
	 
	 @return 
	*/
	public final String DoPop2019()
	{
		return "../../Admin/FoolFormDesigner/Pop/Default.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 全局默认值
	 
	 @return 
	*/
	public final String DoDefVal()
	{
		return "../../Admin/FoolFormDesigner/DefVal.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法执行 Pop自动完成.
	/** 
	 简单列表模式
	 
	 @return 
	*/
	public final String DoPopFullCtrl()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PopFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + HttpUtility.UrlEncode(this.getMyPK());
	}
	/** 
	 多条件查询列表模式
	 
	 @return 
	*/
	public final String DoPopFullCtrlAdv()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PopFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + HttpUtility.UrlEncode(this.getMyPK());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法执行 Pop填充自动完成.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法执行.
	/** 
	 批处理
	 
	 @return 
	*/
	public final String DoEleBatch()
	{
		//return "../../Admin/FoolFormDesigner/EleBatch.aspx?EleType=MapAttr&KeyOfEn=" + this.KeyOfEn + "&FType=1&MyPK=" + this.MyPK + "&FK_MapData=" + this.FK_MapData;
		return "../../Admin/FoolFormDesigner/EleBatch.htm?EleType=MapAttr&KeyOfEn=" + this.getKeyOfEn() + "&FType=1&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}

	/** 
	 小范围多选
	 
	 @return 
	*/
	public final String DoMultipleChoiceSmall()
	{
		return "../../Admin/FoolFormDesigner/MapExt/MultipleChoiceSmall.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";
	}
	/** 
	 大范围多选
	 
	 @return 
	*/
	public final String DoMultipleChoiceSearch()
	{
		return "../../Admin/FoolFormDesigner/MapExt/MultipleChoiceSearch.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";
	}
	/** 
	 超链接
	 
	 @return 
	*/
	public final String DoLink()
	{
		return "../../Admin/FoolFormDesigner/MapExt/Link.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + HttpUtility.UrlEncode(this.getMyPK()) + "&FK_MapExt=Link_" + this.getFK_MapData() + "_" + this.getKeyOfEn();
	}
	/** 
	 设置开窗返回值
	 
	 @return 
	*/
	public final String DoPopVal()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PopVal.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + HttpUtility.UrlEncode(this.getMyPK()) + "&FK_MapExt=PopVal_" + this.getFK_MapData() + "_" + this.getKeyOfEn();
	}
	/** 
	 正则表达式
	 
	 @return 
	*/
	public final String DoRegularExpression()
	{
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpression.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + HttpUtility.UrlEncode(this.getMyPK());
	}

	/** 
	 文本框自动完成
	 
	 @return 
	*/
	public final String DoTBFullCtrl2019()
	{
		return "../../Admin/FoolFormDesigner/TBFullCtrl/Default.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + HttpUtility.UrlEncode(this.getMyPK());
	}

	/** 
	 设置级联
	 
	 @return 
	*/
	public final String DoInputCheck()
	{
		return "../../Admin/FoolFormDesigner/MapExt/InputCheck.htm?FK_MapData=" + this.getFK_MapData() + "&OperAttrKey=" + this.getKeyOfEn() + "&RefNo=" + HttpUtility.UrlEncode(this.getMyPK()) + "&DoType=New&ExtType=InputCheck";
	}
	/** 
	 扩展控件
	 
	 @return 
	*/
	public final String DoEditFExtContral()
	{
		return "../../Admin/FoolFormDesigner/EditFExtContral.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + HttpUtility.UrlEncode(this.getMyPK());
	}
	/** 
	 扩展控件2019
	 
	 @return 
	*/
	public final String DoEditFExtContral2019()
	{
		return "../../Admin/FoolFormDesigner/EditFExtContral/Default.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + HttpUtility.UrlEncode(this.getMyPK());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法执行.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重载.
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//高度.
		//  attr.UIHeightInt = this.GetValIntByKey("ExtRows") * 23;

		attr.setIsRichText(this.GetValBooleanByKey(MapAttrAttr.IsRichText)); //是否是富文本？
		attr.setIsSupperText(this.GetValBooleanByKey(MapAttrAttr.IsSupperText)); //是否是大块文本？

		if (attr.getIsRichText() || attr.getIsSupperText())
		{
			attr.setMaxLen(4000);
			this.SetValByKey(MapAttrAttr.MaxLen, 4000);
		}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 自动扩展字段长度.  @杜. 需要翻译.
		if (attr.getMaxLen() < 4000)
		{
			String sql = "";
			MapData md = new MapData();
			md.setNo(this.getFK_MapData());
			if (md.RetrieveFromDBSources() == 1)
			{
				if (DBAccess.IsExitsTableCol(md.getPTable(), this.getKeyOfEn()) == true)
				{
					if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
					{
						sql = "ALTER TABLE " + md.getPTable() + " ALTER column " + this.getKeyOfEn() + " NVARCHAR(" + attr.getMaxLen() + ")";
					}

					if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
					{
						sql = "alter table " + md.getPTable() + " modify " + attr.getField() + " NVARCHAR(" + attr.getMaxLen() + ")";
					}

					if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
					{
						sql = "alter table " + md.getPTable() + " modify " + attr.getField() + " NVARCHAR2(" + attr.getMaxLen() + ")";
					}

					if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
					{
						sql = "alter table " + md.getPTable() + " alter " + attr.getField() + " type character varying(" + attr.getMaxLen() + ")";
					}

					DBAccess.RunSQL(sql); //如果是oracle如果有nvarchar与varchar类型，就会出错.
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 自动扩展字段长度.


		//默认值.
		String defval = this.GetValStrByKey("ExtDefVal");
		if (defval.equals("") || defval.equals("0"))
		{
			String defVal = this.GetValStrByKey("DefVal");
			if (defval.contains("@") == true)
			{
				this.SetValByKey("DefVal", "");
			}
		}
		else
		{
			this.SetValByKey("DefVal", this.GetValByKey("ExtDefVal"));
		}

		//执行保存.
		attr.Save();

		if (this.GetValStrByKey("GroupID").equals("无"))
		{
			this.SetValByKey("GroupID", "0");
		}

		return super.beforeUpdateInsertAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}