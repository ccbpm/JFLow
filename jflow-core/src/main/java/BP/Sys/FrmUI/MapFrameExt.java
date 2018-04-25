package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;

/** 
 框架
 
*/
public class MapFrameExt extends EntityMyPK
{
  
		///#region 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		uac.IsDelete = false;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 框架
	 
	*/
	public MapFrameExt()
	{

	}
	/** 
	 框架
	 
	 @param mypk
	 * @throws Exception 
	*/
	public MapFrameExt(String mypk) throws Exception
	{
		this.setMyPK( mypk);
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

		Map map = new Map("Sys_MapFrame", "框架");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

 

        map.AddMyPK();
        map.AddTBString(MapFrameAttr.FK_MapData, null, "表单ID", true, true, 0, 100, 20);
        map.AddTBString(MapFrameAttr.Name, null, "名称", true, false, 0, 200, 20, true);

        map.AddTBString(MapFrameAttr.URL, null, "URL", true, false, 0, 3000, 20, true);

        map.AddDDLSysEnum(MapFrameAttr.UrlSrcType, 0, "URL来源", true, true, MapFrameAttr.UrlSrcType, "@0=自定义@1=表单库");
        //显示的分组.
        map.AddDDLSQL(MapFrameAttr.FrmID, "0", "表单表单","SELECT No, Name FROM Sys_Mapdata  WHERE  FrmType=3 ", true);

        map.AddTBString(FrmEleAttr.Y, null, "Y", true, false, 0, 20, 20);
        map.AddTBString(FrmEleAttr.X, null, "x", true, false, 0, 20, 20);

        map.AddTBString(FrmEleAttr.W, null, "宽度", true, false, 0, 20, 20);
        map.AddTBString(FrmEleAttr.H, null, "高度", true, false, 0, 20, 20);

        map.AddBoolean(MapFrameAttr.IsAutoSize, true, "是否自动设置大小", false, false);

        map.AddTBString(FrmEleAttr.EleType, null, "类型", false, false, 0, 50, 20, true);

        // map.AddTBInt(MapFrameAttr.RowIdx, 99, "位置", false, false);
        // map.AddTBInt(MapFrameAttr.GroupID, 0, "GroupID", false, false);

        map.AddTBString(FrmBtnAttr.GUID, null, "GUID", false, false, 0, 128, 20);
        
		

		this.set_enMap( map);
		return map;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}