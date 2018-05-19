package BP.Sys.FrmUI;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.WF.Glo;

/** 
数值字段
*/
public class MapAttrNum extends EntityMyPK
{
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
	public final String getDefVal()
	{
		return this.GetValStringByKey(MapAttrAttr.DefVal);
	}
	public final void setDefVal(String value)
	{
		this.SetValByKey(MapAttrAttr.DefVal, value);
	}
	/** 
	 绑定的枚举ID
	*/
	public final String getUIBindKey()
	{
		return this.GetValStringByKey(MapAttrAttr.UIBindKey);
	}
	public final void setUIBindKey(String value)
	{
		this.SetValByKey(MapAttrAttr.UIBindKey, value);
	}
	/** 
	 数据类型
	*/
	public final int getMyDataType()
	{
		return this.GetValIntByKey(MapAttrAttr.MyDataType);
	}
	public final void setMyDataType(int value)
	{
		this.SetValByKey(MapAttrAttr.MyDataType, value);
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

		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

		map.AddDDLSysEnum(MapAttrAttr.MyDataType, 2, "数据类型", true, false);
		//map.AddTBDecimal(MapAttrAttr.DefVal, 0, "默认值", true, false);
		map.AddTBString(MapAttrAttr.DefVal, null, "默认值", true, false, 0, 300, 20);
		if (Glo.Plant.equals("CCFlow"))
			map.AddTBDecimal(MapAttrAttr.DefVal, 0, "默认值", true, false);
		else
			map.AddTBString(MapAttrAttr.DefVal, null, "默认值", true, false, 0, 200, 20);
		
		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, true);


		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);

		map.AddBoolean("ExtIsSum", false, "是否显示合计(对从表有效)", true, true);

		map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 4000, 20, true);

		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrDT", "@1=跨1个单元格@3=跨3个单元格");

			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组",MapAttrString.SQLOfGroupAttr(), true);

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

		 

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (this.getDefVal()=="")
			this.setDefVal("0");
		
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//是否显示合计
		attr.setIsSum(this.GetValBooleanByKey("ExtIsSum"));
		attr.Update();

		return super.beforeUpdateInsertAction();
	}
	/** 
	 旧版本设置
	 
	 @return 
	*/
	public final String DoOldVer()
	{
		return Glo.getCCFlowAppPath() +"WF/Admin/FoolFormDesigner/EditF.htm?KeyOfEn=" + this.getKeyOfEn() + "&FType=" + this.getMyDataType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}

	
	
	  ///#region 方法执行.
			public final String DoAutoFullDtlField()
			{
				return "../../Admin/FoolFormDesigner/MapExt/AutoFullDtlField.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" +this.getKeyOfEn();
				//return "/WF/Admin/FoolFormDesigner/MapExt/AutoFull.aspx?FK_MapData=" + this.FK_MapData + "&KeyOfEn=" + KeyOfEnthis.KeyOfEn + "&MyPK=" + this.MyPK;
			}
	/** 
	 自动计算
	 
	 @return 
	*/
	public final String DoAutoFull()
	{
		return Glo.getCCFlowAppPath() +"WF/Admin/FoolFormDesigner/MapExt/AutoFull.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
		//return "/WF/Admin/FoolFormDesigner/MapExt/AutoFull.aspx?FK_MapData=" + this.FK_MapData + "&KeyOfEn=" + KeyOfEnthis.KeyOfEn + "&MyPK=" + this.getMyPK();
	}

  
	/** 
	 文本框自动完成
	 
	 @return 
	*/
	public final String DoTBFullCtrl()
	{
		return "/WF/Admin/FoolFormDesigner/MapExt/TBFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
		//return "/WF/Admin/FoolFormDesigner/MapExt/TBFullCtrl.htm?FK_MapData=" + this.FK_MapData + "&ExtType=AutoFull&KeyOfEn=" + this.KeyOfEn + "&RefNo=" + this.getMyPK();
	}

	/** 
	 设置级联
	 
	 @return 
	*/
	public final String DoInputCheck()
	{
		//  InputCheck.aspx?FK_MapData=ND101Dtl1&ExtType=InputCheck&RefNo=ND101Dtl1_ZhouQi&OperAttrKey=ZhouQi&DoType=New
		return Glo.getCCFlowAppPath()+"WF/Admin/FoolFormDesigner/MapExt/InputCheck.jsp?FK_MapData=" + this.getFK_MapData() + "&OperAttrKey=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK() + "&DoType=New&ExtType=InputCheck";

		//  return "/WF/Admin/FoolFormDesigner/MapExt/InputCheck.htm?FK_MapData=" + this.FK_MapData + "&KeyOfEn=" + this.KeyOfEn +"&RefNo="+this.getMyPK();
		//  return "/WF/Admin/FoolFormDesigner/MapExt/InputCheck.aspx?FK_MapData=" + this.FK_MapData + "&ExtType=AutoFull&KeyOfEn=" + this.KeyOfEn + "&RefNo=" + this.getMyPK();
	}
	
	 /// <summary>
    /// 绑定函数
    /// </summary>
    /// <returns></returns>
    public final String BindFunction()
    {
        return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
    }
	/** 
	 扩展控件
	 
	 @return 
	*/
	public final String DoEditFExtContral()
	{
		return Glo.getCCFlowAppPath() +"WF/Admin/FoolFormDesigner/EditFExtContral.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
		//  return "/WF/Admin/FoolFormDesigner/MapExt/InputCheck.aspx?FK_MapData=" + this.FK_MapData + "&ExtType=AutoFull&KeyOfEn=" + this.KeyOfEn + "&RefNo=" + this.getMyPK();
	}
}

