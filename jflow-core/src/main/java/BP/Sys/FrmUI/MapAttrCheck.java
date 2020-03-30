package BP.Sys.FrmUI;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.Depositary;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.*;

/** 
 实体属性
*/
public class MapAttrCheck extends EntityMyPK
{
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
	 * @throws Exception
	*/
	public final String getKeyOfEn() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}

	public final int getMaxLen() throws Exception
	{

		int i = this.GetValIntByKey(MapAttrAttr.MaxLen);
		return i;

	}
	public UIContralType getUIContralType() throws Exception{
		return UIContralType.forValue(this.GetValIntByKey(MapAttrAttr.UIContralType));
	}

	public void setUIContralType(UIContralType value) throws Exception{
		this.SetValByKey(MapAttrAttr.UIContralType,value);
	}

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
	public MapAttrCheck()
	{
	}
	/**
	 实体属性
	 * @throws Exception
	*/
	public MapAttrCheck(String myPK) throws Exception
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


		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);
		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20, true);

		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);



		map.AddTBInt(MapAttrAttr.MinLen, 0, "最小长度", true, false);
		map.AddTBInt(MapAttrAttr.MaxLen, 50, "最大长度", true, false);
		map.SetHelperAlert(MapAttrAttr.MaxLen, "定义该字段的字节长度.");


		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.SetHelperAlert(MapAttrAttr.UIWidth, "对自由表单,从表有效,显示文本框的宽度.");


		map.AddTBInt(MapAttrAttr.UIContralType, 0, "控件", true, false);

		map.AddDDLSQL(MapAttrAttr.CSS, "0", "自定义样式", MapAttrString.getSQLOfCSSAttr(), true);
        //#endregion 基本字段信息.

        //#region 傻瓜表单
		//单元格数量 2013-07-24 增加
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "TextBox单元格数量", true, true, "ColSpanAttrDT",
				"@0=跨0个单元格@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨5个单元格@6=跨6个单元格");
		map.SetHelperAlert(MapAttrAttr.ColSpan, "对于傻瓜表单有效: 标识该字段TextBox横跨的宽度,占的单元格数量.");

		//文本占单元格数量
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "Label单元格数量", true, true, "ColSpanAttrString",
				"@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨6个单元格@6=跨6个单元格");
		map.SetHelperAlert(MapAttrAttr.TextColSpan, "对于傻瓜表单有效: 标识该字段Lable，标签横跨的宽度,占的单元格数量.");


		//文本跨行
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);

		//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID,0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);


		map.AddTBInt(MapAttrAttr.Idx, 0, "顺序号", true, false);
		map.SetHelperAlert(MapAttrAttr.Idx, "对傻瓜表单有效:用于调整字段在同一个分组中的顺序.");

        //#endregion 傻瓜表单
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "字段重命名";
		rm.ClassMethodName = this.toString() + ".DoRenameField()";
		rm.getHisAttrs().AddTBString("key1", "@KeyOfEn", "字段重命名为?", true, false, 0, 100, 100);
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "如果是节点表单，系统就会把该流程上的所有同名的字段都会重命名，包括NDxxxRpt表单。";
		map.AddRefMethod(rm);

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
	/// <summary>
	/// 字段自定义样式查询
	/// </summary>
	public static String getSQLOfCSSAttr()
	{

			return "SELECT No,Name FROM Sys_GloVar WHERE GroupKey='CSS' OR GroupKey='css' ";

	}
	/**
	 删除
	 * @throws Exception
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		//删除相对应的rpt表中的字段
		if (this.getFK_MapData().contains("ND") == true)
		{
			String fk_mapData = this.getFK_MapData().substring(0, this.getFK_MapData().length() - 2) + "Rpt";
			String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND( KeyOfEn='" + this.getKeyOfEn() + "T' OR KeyOfEn='" + this.getKeyOfEn() + "')";
			DBAccess.RunSQL(sql);
		}

		//调用frmEditAction, 完成其他的操作.
		CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterDelete();
	}


	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		mapAttr.RetrieveFromDBSources();
		mapAttr.Update();

		//调用frmEditAction, 完成其他的操作.
		CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}


		///#endregion


		///#region 基本功能.

	public final String DoRenameField(String newField) throws Exception
	{
		String sql = "";
		if (this.getFK_MapData().indexOf("ND") == 0)
		{
			String strs = this.getFK_MapData().replace("ND", "");
			strs = strs.substring(0, strs.length() - 2);

			String rptTable = "ND" + strs + "Rpt";
			MapDatas mds = new MapDatas();
			mds.Retrieve(MapDataAttr.PTable, rptTable);

			for (MapData item : mds.ToJavaList())
			{
				sql = "UPDATE Sys_MapAttr SET KeyOfEn='" + newField + "',  MyPK='" + newField + "_" + item.getNo() + "' WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + item.getNo() + "'";
				DBAccess.RunSQL(sql);
			}
		}
		else
		{
			sql = "UPDATE Sys_MapAttr SET KeyOfEn='" + newField + "', MyPK='" + newField + "_" + this.getFK_MapData() + "'  WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + this.getFK_MapData() + "'";
			DBAccess.RunSQL(sql);
		}

		return "重名称成功,如果是自由表单，请关闭表单设计器重新打开.";
	}

		///#region 重载.
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();
		///#region 自动扩展字段长度.
		if (attr.getMaxLen() <   this.getMaxLen() )
		{
			this.SetValByKey(MapAttrAttr.MaxLen, this.getMaxLen());
			
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

		return true;
	}

}