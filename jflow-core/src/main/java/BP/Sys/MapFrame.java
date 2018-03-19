package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.WF.DotNetToJavaStringHelper;

/** 
 框架
 
*/
public class MapFrame extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsDelete = true;
			uac.IsUpdate = true;
		}
		return uac;
	}

	/** 
	 是否自适应大小
	 
	*/
	public final boolean getIsAutoSize()
	{
		return this.GetValBooleanByKey(MapFrameAttr.IsAutoSize);
	}
	public final void setIsAutoSize(boolean value)
	{
		this.SetValByKey(MapFrameAttr.IsAutoSize, value);
	}
	
	public final int getRowIdx()
	{
		return 0;
		 
	}
	
	
	/** 
	 名称
	 
	*/
	public final String getName()
	{
		return this.GetValStrByKey(MapFrameAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(MapFrameAttr.Name, value);
	}
	public final String getNoOfObj()
	{
		return this.GetValStrByKey(FrmEleAttr.EleID);
	}
	/** 
	 连接
	 
	*/
	public final String getURL()
	{
		String s= this.GetValStrByKey(MapFrameAttr.URL);
		if (DotNetToJavaStringHelper.isNullOrEmpty(s))
		{
			return "http://ccflow.org";
		}
		return s;
	}
	public final void setURL(String value)
	{
		this.SetValByKey(MapFrameAttr.URL, value);
	}
	/** 
	 高度
	 
	*/
	public final String getH()
	{
		return this.GetValStrByKey(MapFrameAttr.H, "700px");

	}
	public final void setH(String value)
	{
		this.SetValByKey(MapFrameAttr.H, value);
	}
	/** 
	 宽度
	 
	*/
	public final String getW()
	{
		return this.GetValStrByKey(MapFrameAttr.W, "100%");
	}
	public final void setW(String value)
	{
		this.SetValByKey(MapFrameAttr.W, value);
	}
	public boolean IsUse = false;
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(MapFrameAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapFrameAttr.FK_MapData, value);
	}

  
	/** 
	 框架
	 
	*/
	public MapFrame()
	{
	}
	/** 
	 框架
	 
	 @param no
	*/
	public MapFrame(String mypk)
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
		Map map = new Map("Sys_MapFrame", "框架");//Sys_MapFrame
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(MapFrameAttr.FK_MapData, null, "表单ID", true, true, 0, 100, 20);
		map.AddTBString(MapFrameAttr.Name, null, "名称", true, false, 0, 200, 20,true);
		map.AddTBString(MapFrameAttr.URL, null, "URL", true, false, 0, 3000, 20, true);

		map.AddTBString(FrmEleAttr.Y, null, "Y", true, false, 0, 20, 20);
		map.AddTBString(FrmEleAttr.X, null, "x", true, false, 0, 20, 20);

		map.AddTBString(FrmEleAttr.W, null, "宽度", true, false, 0, 20, 20);
		map.AddTBString(FrmEleAttr.H, null, "高度", true, false, 0, 20, 20);

		map.AddBoolean(MapFrameAttr.IsAutoSize, true, "是否自动设置大小", false, false);

		map.AddTBString(FrmEleAttr.EleType, null, "类型", false, false, 0, 50, 20, true);

		   // map.AddTBInt(MapFrameAttr.RowIdx, 99, "位置", false, false);
		   // map.AddTBInt(MapFrameAttr.GroupID, 0, "GroupID", false, false);

		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", false, false, 0, 128, 20);

		this.set_enMap(  map);
		return this.get_enMap();
	}

	/** 
	 插入之后增加一个分组.
	 
	*/
	@Override
	protected void afterInsert()
	{
		GroupField gf = new GroupField();
		gf.setEnName(  this.getFK_MapData());
		gf.setCtrlID( this.getMyPK());
		gf.setCtrlType("Frame");
		gf.setLab(this.getName());
		gf.setIdx(0);
		gf.Insert(); //插入.

		super.afterInsert();
	}
	/** 
	 删除之后的操作
	 
	*/
	@Override
	protected void afterDelete()
	{
		GroupField gf = new GroupField();
		gf.Delete(GroupFieldAttr.CtrlID, this.getMyPK());

		super.afterDelete();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}