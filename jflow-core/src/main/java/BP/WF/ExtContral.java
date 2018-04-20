package BP.WF;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.Entity;
import BP.En.EntityMyPKAttr;
import BP.En.Map;
import BP.En.UIContralType;
import BP.Sys.AthShowModel;
import BP.Sys.FrmBtnAttr;
import BP.Sys.MapAttrAttr;

public class ExtContral  extends Entity {
	/**
	 * 扩展控件
	 */
      public ExtContral()
      {
    	  
      }
	  public ExtContral(String fk_mapdata, String keyofEn) throws Exception
      {
          this.setMyPK(fk_mapdata + "_" + keyofEn);
          this.Retrieve();
      }
      public ExtContral(String mypk) throws Exception
      {
          this.setMyPK(mypk);
          this.Retrieve();
      }
      /**
       * 集合类名称
       */
      public final String getMyPK()
      { 
         return this.GetValStringByKey(EntityMyPKAttr.MyPK);
      }
      public final void setMyPK(String value)
      {
    	  this.SetValByKey(EntityMyPKAttr.MyPK, value);
      }
	@Override
	public Map getEnMap() {
		 if (this.get_enMap() != null)
		 {
			 return this.get_enMap();
		 }
         Map map = new Map("Sys_MapAttr");
         map.setDepositaryOfEntity(Depositary.None);
         map.setDepositaryOfMap(Depositary.Application);
         map.setEnDesc("扩展控件");
         map.setEnType(EnType.Sys);
         
         map.AddTBStringPK(MapAttrAttr.MyPK, null, "实体标识", true, true, 1, 200, 20);
        // map.AddMyPK();
         map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", true, true, 1, 100, 20);
         map.AddTBString(MapAttrAttr.KeyOfEn, null, "属性", true, true, 1, 200, 20);

         map.AddTBString(MapAttrAttr.Name, null, "描述", true, false, 0, 200, 20);
         map.AddTBString(MapAttrAttr.DefVal, null, "默认值", false, false, 0, 4000, 20);

         map.AddTBInt(MapAttrAttr.UIContralType, 0, "控件", true, false);
         map.AddTBInt(MapAttrAttr.MyDataType, 0, "数据类型", true, false);

         map.AddDDLSysEnum(MapAttrAttr.LGType, 0, "逻辑类型", true, false, MapAttrAttr.LGType,
             "@0=普通@1=枚举@2=外键@3=打开系统页面");

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
         map.AddTBInt(MapAttrAttr.ColSpan, 1, "单元格数量", true, false);

         map.AddTBInt(MapAttrAttr.Idx, 0, "序号", true, false);
         map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, false, 0, 128, 20);

         //参数属性.
         map.AddTBAtParas(4000); //

         this.set_enMap(map);
         return this.get_enMap();
     }
	/**
	 * 控件类型
	 */
	public final UIContralType getUIContralType()
	{
		return UIContralType.forValue(this.GetValIntByKey(MapAttrAttr.UIContralType));
	}
	public final void setUIContralType(UIContralType value)
	{
		this.SetValByKey(MapAttrAttr.UIContralType, value.getValue());
	}
	/**
	 * 关联的字段
	 */
      public String getAthRefObj()
      {   
        return this.GetParaString("AthRefObj");
      }
      public void setAthRefObj(String value)
      {   
         this.GetParaString("AthRefObj",value);
      }
    /**
     * 显示方式
     */
      public AthShowModel getAthShowModel()
      {
    	  return AthShowModel.forValue(this.GetParaInt("AthShowModel"));
      
      }
      public void setAthShowModel(AthShowModel value)
      {
    	  this.SetPara("AthShowModel",value.getValue());
    	 
      }

      }
	   






