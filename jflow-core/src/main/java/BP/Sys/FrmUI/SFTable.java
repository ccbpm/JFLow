package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

/** 
 用户自定义表
*/
public class SFTable extends EntityNoName
{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 用户自定义表
	*/
	public SFTable()
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
		Map map = new Map("Sys_SFTable", "字典表");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(SFTableAttr.No, null, "表英文名称", true, false, 1, 200, 20);
		map.AddTBString(SFTableAttr.Name, null, "表中文名称", true, false, 0, 200, 20);

		map.AddDDLSysEnum(SFTableAttr.SrcType, 0, "数据表类型", true, false, SFTableAttr.SrcType, "@0=本地的类@1=创建表@2=表或视图@3=SQL查询表@4=WebServices");

		map.AddDDLSysEnum(SFTableAttr.CodeStruct, 0, "字典表类型", true, false, SFTableAttr.CodeStruct);

		map.AddTBString(SFTableAttr.FK_Val, null, "默认创建的字段名", true, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.TableDesc, null, "表描述", true, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.DefVal, null, "默认值", true, false, 0, 200, 20);

			//数据源.
		map.AddDDLEntities(SFTableAttr.FK_SFDBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);
		map.AddTBString(SFTableAttr.SrcTable, null, "表/视图", true, false, 0, 200, 20);

		map.AddTBString(SFTableAttr.ColumnValue, null, "显示的值(编号列/默认为No)", true, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.ColumnText, null, "显示的文字(名称列默认为Name)", true, false, 0, 200, 20);


		map.AddTBString(SFTableAttr.ParentValue, null, "父级值(父级列)", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.SelectStatement, null, "查询语句", false, false, 0, 1000, 600, true);

		map.AddTBDateTime(SFTableAttr.RDT, null, "加入日期", false, false);

		RefMethod rm = new RefMethod();
		rm.Title = "查看数据";
		rm.ClassMethodName = this.toString() + ".DoEdit";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.IsForEns = false;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 更新的操作
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdate()
	{
		BP.Sys.SFTable sf = new BP.Sys.SFTable(this.getNo());
		sf.Copy(this);
		sf.Update();

		return super.beforeUpdate();
	}

	@Override
	protected void afterInsertUpdateAction()
	{
		BP.Sys.SFTable sftable = new BP.Sys.SFTable();
		sftable.setNo(this.getNo());
		sftable.RetrieveFromDBSources();
		sftable.Update();

		super.afterInsertUpdateAction();
	}


	/** 
	 编辑数据
	 
	 @return 
	*/
	public final String DoEdit()
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Admin/FoolFormDesigner/SFTableEditData.htm?FK_SFTable=" + this.getNo();
	}
	/** 
	 执行删除.
	 
	 @return 
	*/
	@Override
	protected boolean beforeDelete()
	{
		BP.Sys.SFTable sf = new Sys.SFTable(this.getNo());
		sf.Delete();
		return super.beforeDelete();
	}
	@Override
	protected boolean beforeInsert()
	{
		//利用这个时间串进行排序.
		this.SetValByKey("RDT", DataType.getCurrentDataTime());
		return super.beforeInsert();
	}
}