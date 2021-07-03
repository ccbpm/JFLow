package bp.sys.frmui;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.*;
import bp.sys.*;
import java.util.*;

/** 
 枚举字段
*/
public class MapAttrEnum extends EntityMyPK
{

		///文本字段参数属性.
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
	public final String getKeyOfEn()throws Exception
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
	public final String getUIBindKey()throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.UIBindKey);
	}
	public final void setUIBindKey(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.UIBindKey, value);
	}
	/** 
	 控件类型
	*/
	public final UIContralType getUIContralType()throws Exception
	{
		return UIContralType.forValue(this.GetValIntByKey(MapAttrAttr.UIContralType));
	}
	public final void setUIContralType(UIContralType value)throws Exception
	{
		this.SetValByKey(MapAttrAttr.UIContralType, value.getValue());
	}

		///


		///构造方法
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 枚举字段
	*/
	public MapAttrEnum()
	{
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_MapAttr", "枚举字段");
		map.IndexField = MapAttrAttr.FK_MapData;



			///基本信息.
		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
			case MySQL:
				sql = "SELECT -1 AS No, '-无(不选择)-' as Name ";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql = "SELECT -1 AS No, '-无(不选择)-' as Name FROM DUAL ";
				break;

			case PostgreSQL:
			default:
				sql = "SELECT -1 AS No, '-无(不选择)-' as Name FROM Port_Emp WHERE 1=2 ";
				break;
		}
		sql += " union ";

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql += "SELECT  IntKey as No, Lab as Name FROM "+bp.wf.Glo.SysEnum()+" WHERE EnumKey='@UIBindKey'";
		}
		else
		{
			sql += "SELECT  IntKey as No, Lab as Name FROM "+bp.wf.Glo.SysEnum()+" WHERE EnumKey='@UIBindKey' ";
		}

			//默认值.
		map.AddDDLSQL(MapAttrAttr.DefVal, "0", "默认值（选中）", sql, true);

			//map.AddTBString(MapAttrAttr.DefVal, "0", "默认值", true, true, 0, 3000, 20);

		map.AddDDLSysEnum(MapAttrAttr.UIContralType, 0, "控件类型", true, true, "EnumUIContralType", "@1=下拉框@2=复选框@3=单选按钮");

		map.AddDDLSysEnum("RBShowModel", 0, "单选按钮的展现方式", true, true, "RBShowModel", "@0=竖向@3=横向");

			//map.AddDDLSysEnum(MapAttrAttr.LGType, 0, "逻辑类型", true, false, MapAttrAttr.LGType, 
			// "@0=普通@1=枚举@2=外键@3=打开系统页面");

		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, true);

		map.AddTBString(MapAttrAttr.UIBindKey, null, "枚举ID", true, true, 0, 100, 20);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见?", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑?", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);

			//CCS样式
		map.AddDDLSQL(MapAttrAttr.CSSCtrl, "0", "自定义样式", MapAttrString.getSQLOfCSSAttr(), true);

			/// 基本信息.


			///傻瓜表单。
			//单元格数量 2013-07-24 增加。
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrDT", "@0=跨0个单元格@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

			//文本占单元格数量
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

			//文本跨行
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);
			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);
		map.AddTBInt(MapAttrAttr.Idx, 0, "顺序号", true, false); //@李国文


			/// 傻瓜表单。


			///执行的方法.
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "级联下拉框";
		rm.ClassMethodName = this.toString() + ".DoActiveDDL()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "填充其他控件";
		rm.ClassMethodName = this.toString() + ".DoDDLFullCtrl2019()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "编辑枚举值";
		rm.ClassMethodName = this.toString() + ".DoSysEnum()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

	

		rm = new RefMethod();
		rm.Title = "事件绑函数";
		rm.ClassMethodName = this.toString() + ".BindFunction()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "选项联动控件";
		rm.ClassMethodName = this.toString() + ".DoRadioBtns()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		//rm.GroupName = "高级设置";
		map.AddRefMethod(rm);
			/// 执行的方法.

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 处理业务逻辑.
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{

		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//单选按钮的展现方式.
		attr.setRBShowModel(this.GetValIntByKey("RBShowModel"));

		if (this.getUIContralType() == UIContralType.DDL || this.getUIContralType() == UIContralType.RadioBtn)
		{
			attr.setMyDataType(DataType.AppInt);
		}
		else
		{
			attr.setMyDataType(DataType.AppString);
		}

		//执行保存.
		attr.Update();

		return super.beforeUpdateInsertAction();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		mapAttr.RetrieveFromDBSources();
		//@sly
		if (this.getUIContralType() == UIContralType.CheckBok)
		{
			mapAttr.setMyDataType(DataType.AppString);
			MapData mapData = new MapData(this.getFK_MapData());
			GEEntity en = new GEEntity(this.getFK_MapData());

			if (DBAccess.IsExitsTableCol(en.getEnMap().getPhysicsTable(), this.getKeyOfEn()) == true)
			{
				switch (SystemConfig.getAppCenterDBType())
				{
					case MSSQL:
						//先检查是否存在约束
						String sqlYueShu = "SELECT b.name, a.name FName from sysobjects b join syscolumns a on b.id = a.cdefault where a.id = object_id('" + en.getEnMap().getPhysicsTable() + "') ";
						DataTable dtYueShu = DBAccess.RunSQLReturnTable(sqlYueShu);
						for (DataRow dr : dtYueShu.Rows)
						{
							if (dr.getValue("FName").toString().toLowerCase().equals(this.getKeyOfEn().toLowerCase()))
							{
								DBAccess.RunSQL("ALTER TABLE " + en.getEnMap().getPhysicsTable() + " drop constraint " + dr.getValue(0).toString());
								break;
							}

						}
						this.RunSQL("alter table  " + en.getEnMap().getPhysicsTable() + " ALTER column " + this.getKeyOfEn() + " VARCHAR(20)");
						break;
					case Oracle:
					case KingBaseR3:
					case KingBaseR6:
						//判断数据库当前字段的类型
						String sql = "SELECT DATA_TYPE FROM ALL_TAB_COLUMNS WHERE upper(TABLE_NAME)='" + en.getEnMap().getPhysicsTable().toUpperCase() + "' AND UPPER(COLUMN_NAME)='" + this.getKeyOfEn().toUpperCase() + "' ";
						String val = DBAccess.RunSQLReturnString(sql);
						if (val == null)
						{
							Log.DefaultLogWriteLineError("@没有检测到字段eunm" + this.getKeyOfEn());
						}
						if (val.indexOf("NUMBER") != -1)
						{
							this.RunSQL("ALTER TABLE " + en.getEnMap().getPhysicsTable() + " RENAME COLUMN " + this.getKeyOfEn() + " TO " + this.getKeyOfEn() + "_tmp");

							/*增加一个和原字段名同名的字段name*/
							this.RunSQL("ALTER TABLE " + en.getEnMap().getPhysicsTable() + " ADD " + this.getKeyOfEn() + " varchar2(20)");

							/*将原字段name_tmp数据更新到增加的字段name*/
							this.RunSQL("UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + this.getKeyOfEn() + "= trim(" + this.getKeyOfEn() + "_tmp)");

							/*更新完，删除原字段name_tmp*/
							this.RunSQL("ALTER TABLE " + en.getEnMap().getPhysicsTable() + " DROP COLUMN " + this.getKeyOfEn() + "_tmp");

							//this.RunSQL(sql);
						}
						break;
					case MySQL:
						this.RunSQL("alter table  " + en.getEnMap().getPhysicsTable() + " modify " + this.getKeyOfEn() + " NVARCHAR(20)");
						break;
					case PostgreSQL:
						this.RunSQL("ALTER TABLE " + en.getEnMap().getPhysicsTable() + " ALTER column " + this.getKeyOfEn() + " type character varying(20)");
						break;
				}
			}

		}
		mapAttr.Update();


		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

		///

	@Override
	protected void afterDelete()throws Exception
	{
		//删除可能存在的数据.
		DBAccess.RunSQL("DELETE FROM Sys_FrmRB WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + this.getFK_MapData() + "'");
		//删除相对应的rpt表中的字段
		if (this.getFK_MapData().contains("ND") == true)
		{
			String fk_mapData = this.getFK_MapData().substring(0, this.getFK_MapData().length() - 2) + "Rpt";
			String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND KeyOfEn='" + this.getKeyOfEn() + "'";
			DBAccess.RunSQL(sql);
		}
		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}


		///基本功能.
	/** 
	 绑定函数
	 
	 @return 
	*/
	public final String BindFunction()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}

		///


		///方法执行.
	/** 
	 编辑枚举值
	 
	 @return 
	*/
	public final String DoSysEnum()throws Exception
	{
		return "../../Admin/CCFormDesigner/DialogCtr/EnumerationNew.htm?DoType=FrmEnumeration_SaveEnum&EnumKey=" + this.getUIBindKey();
	}

	public final String DoDDLFullCtrl2019()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/DDLFullCtrl2019.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	/** 
	 设置自动填充
	 
	 @return 
	*/
	public final String DoAutoFull()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/AutoFullDLL.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	/** 
	 高级设置
	 
	 @return 
	*/
	public final String DoRadioBtns()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/RadioBtns.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	/** 
	 设置级联
	 
	 @return 
	*/
	public final String DoActiveDDL()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/ActiveDDL.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}


		/// 方法执行.
}