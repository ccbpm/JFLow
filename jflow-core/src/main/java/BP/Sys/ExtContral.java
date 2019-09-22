package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 扩展控件
*/
public class ExtContral extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本-属性
	/** 
	 表单ID
	*/
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}
	/** 
	 字段名
	*/
	public final String getKeyOfEn()
	{
		return this.GetValStrByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)
	{
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}
	/** 
	 控件类型
	*/
	public final UIContralType getUIContralType()
	{
		return UIContralType.forValue(this.GetValIntByKey(MapAttrAttr.UIContralType));
	}
	public final void setUIContralType(UIContralType value)
	{
		this.SetValByKey(MapAttrAttr.UIContralType, value.getValue());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 附件属性
	/** 
	 关联的字段.
	*/
	public final String getAthRefObj()
	{
		return this.GetParaString("AthRefObj");
	}
	public final void setAthRefObj(String value)
	{
		this.SetPara("AthRefObj", value);
	}
	/** 
	 显示方式
	*/
	public final AthShowModel getAthShowModel()
	{
		return AthShowModel.forValue(this.GetParaInt("AthShowModel"));
	}
	public final void setAthShowModel(AthShowModel value)
	{
		this.SetPara("AthShowModel", value.getValue());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 附件属性



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 扩展控件
	*/
	public ExtContral()
	{
	}
	public ExtContral(String fk_mapdata, String keyofEn)
	{
		this.setMyPK(fk_mapdata + "_" + keyofEn);
		this.Retrieve();
	}
	public ExtContral(String mypk)
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
		Map map = new Map("Sys_MapAttr", "扩展控件");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", true, true, 1, 100, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "属性", true, true, 1, 200, 20);

		map.AddTBString(MapAttrAttr.Name, null, "描述", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.DefVal, null, "默认值", false, false, 0, 4000, 20);

		map.AddTBInt(MapAttrAttr.UIContralType, 0, "控件", true, false);
		map.AddTBInt(MapAttrAttr.MyDataType, 0, "数据类型", true, false);

		map.AddDDLSysEnum(MapAttrAttr.LGType, 0, "逻辑类型", true, false, MapAttrAttr.LGType, "@0=普通@1=枚举@2=外键@3=打开系统页面");

		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, false);

		map.AddTBInt(MapAttrAttr.MinLen, 0, "最小长度", true, false);
		map.AddTBInt(MapAttrAttr.MaxLen, 300, "最大长度", true, false);

		map.AddTBString(MapAttrAttr.UIBindKey, null, "绑定的信息", true, false, 0, 100, 20);
		map.AddTBString(MapAttrAttr.UIRefKey, null, "绑定的Key", true, false, 0, 30, 20);
		map.AddTBString(MapAttrAttr.UIRefKeyText, null, "绑定的Text", true, false, 0, 30, 20);


		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否启用", true, true);
		map.AddBoolean(MapAttrAttr.UIIsLine, false, "是否单独栏显示", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填字段", true, true);


		map.AddTBFloat(MapAttrAttr.X, 5, "X", true, false);
		map.AddTBFloat(MapAttrAttr.Y, 5, "Y", false, false);


		map.AddTBString(MapAttrAttr.Tag, null, "标识（存放临时数据）", true, false, 0, 100, 20);
		map.AddTBInt(MapAttrAttr.EditType, 0, "编辑类型", true, false);

			//单元格数量。2013-07-24 增加。
		map.AddTBString(MapAttrAttr.ColSpan, "1", "单元格数量", true, false, 0, 3, 3);
		 //   map.AddTBInt(MapAttrAttr.ColSpan, 1, "单元格数量", true, false);

		map.AddTBInt(MapAttrAttr.Idx, 0, "序号", true, false);
		map.AddTBString(MapAttrAttr.GUID, null, "GUID", true, false, 0, 128, 20);

			//参数属性.
		map.AddTBAtParas(4000);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}