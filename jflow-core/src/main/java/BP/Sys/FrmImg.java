package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Tools.StringHelper;

/** 
 图片
 
*/
public class FrmImg extends EntityMyPK
{
 
	/** 
	 中文名称
	 
	*/
	public final String getName()
	{
		return this.GetValStringByKey(FrmImgAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(FrmImgAttr.Name, value);
	}
	/**
	 * 中文对应的字段
	 * @return
	 */
	public final String getKeyOfEn()
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)
	{
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}
	/** 
	 英文名称
	 
	*/
	public final String getEnPK()
	{
		return this.GetValStringByKey(FrmImgAttr.EnPK);
	}
	public final void setEnPK(String value)
	{
		this.SetValByKey(FrmImgAttr.EnPK, value);
	}
	/** 
	 是否可以编辑
	 
	*/
	public final int getIsEdit()
	{
		return this.GetValIntByKey(FrmImgAttr.IsEdit);
	}
	public final void setIsEdit(int value)
	{
		this.SetValByKey(FrmImgAttr.IsEdit, (int)value);
	}
	/** 
	 应用类型
	 
	*/
	public final ImgAppType getHisImgAppType()
	{
		return ImgAppType.forValue(this.GetValIntByKey(FrmImgAttr.ImgAppType));
	}
	public final void setHisImgAppType(ImgAppType value)
	{
		this.SetValByKey(FrmImgAttr.ImgAppType, value.getValue());
	}
	/** 
	 数据来源
	 
	*/
	public final int getImgSrcType()
	{
		return this.GetValIntByKey(FrmImgAttr.ImgSrcType);
	}
	public final void setSrcType(int value)
	{
		this.SetValByKey(FrmImgAttr.ImgSrcType, value);
	}

	public final String getTag0()
	{
		return this.GetValStringByKey(FrmImgAttr.Tag0);
	}
	public final void setTag0(String value)
	{
		this.SetValByKey(FrmImgAttr.Tag0, value);
	}
	public final String getLinkTarget()
	{
		return this.GetValStringByKey(FrmImgAttr.LinkTarget);
	}
	public final void setLinkTarget(String value)
	{
		this.SetValByKey(FrmImgAttr.LinkTarget, value);
	}
	/** 
	 URL
	 
	*/
	public final String getLinkURL()
	{
		return this.GetValStringByKey(FrmImgAttr.LinkURL);
	}
	public final void setLinkURL(String value)
	{
		this.SetValByKey(FrmImgAttr.LinkURL, value);
	}
	public final String getImgPath()
	{
		String src = this.GetValStringByKey(FrmImgAttr.ImgPath);
		if (StringHelper.isNullOrEmpty(src))
		{
			src =  "DataUser/ICON/" + BP.Sys.SystemConfig.getCustomerNo() + "/LogBiger.png";
		}
		return src;
	}
	public final void setImgPath(String value)
	{
		this.SetValByKey(FrmImgAttr.ImgPath, value);
	}
	public final String getImgURL()
	{
		String src = this.GetValStringByKey(FrmImgAttr.ImgURL);
		if (StringHelper.isNullOrEmpty(src) || src.contains("component/Img"))
		{
			src =  "DataUser/ICON/" + BP.Sys.SystemConfig.getCustomerNo() + "/LogBiger.png";
		}
		return src;
	}
	public final void setImgURL(String value)
	{
		this.SetValByKey(FrmImgAttr.ImgURL, value);
	}
	/** 
	 Y
	 
	*/
	public final float getY()
	{
		return this.GetValFloatByKey(FrmImgAttr.Y);
	}
	public final void setY(float value)
	{
		this.SetValByKey(FrmImgAttr.Y, value);
	}
	/** 
	 X
	 
	*/
	public final float getX()
	{
		return this.GetValFloatByKey(FrmImgAttr.X);
	}
	public final void setX(float value)
	{
		this.SetValByKey(FrmImgAttr.X, value);
	}
	/** 
	 H
	 
	*/
	public final float getH()
	{
		return this.GetValFloatByKey(FrmImgAttr.H);
	}
	public final void setH(float value)
	{
		this.SetValByKey(FrmImgAttr.H, value);
	}
	/** 
	 W
	 
	*/
	public final float getW()
	{
		return this.GetValFloatByKey(FrmImgAttr.W);
	}
	public final void setW(float value)
	{
		this.SetValByKey(FrmImgAttr.W, value);
	}
	/** 
	 FK_MapData
	 
	*/
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(FrmImgAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(FrmImgAttr.FK_MapData, value);
	}

		///#endregion


		
	/** 
	 图片
	 
	*/
	public FrmImg()
	{
	}
	/** 
	 图片
	 
	 @param mypk
	 * @throws Exception 
	*/
	public FrmImg(String mypk) throws Exception
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

		   Map map = new Map("Sys_FrmImg", "图片");
           map.Java_SetDepositaryOfEntity(Depositary.None);
           map.Java_SetDepositaryOfMap( Depositary.Application);
           map.Java_SetEnType(EnType.Sys);
           map.AddMyPK();

           map.AddTBString(FrmImgAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
           map.AddTBString(MapAttrAttr.KeyOfEn, null, "对应字段", true, false, 1, 100, 20);

           map.AddTBInt(FrmImgAttr.ImgAppType, 0, "应用类型", false, false);
           
           map.AddTBFloat(FrmImgAttr.X, 5, "X", true, false);
           map.AddTBFloat(FrmImgAttr.Y, 5, "Y", false, false);

           map.AddTBFloat(FrmImgAttr.H, 200, "H", true, false);
           map.AddTBFloat(FrmImgAttr.W, 160, "W", false, false);

           map.AddTBString(FrmImgAttr.ImgURL, null, "ImgURL", true, false, 0, 200, 20);
           map.AddTBString(FrmImgAttr.ImgPath, null, "ImgPath", true, false, 0, 200, 20);
           
           map.AddTBString(FrmImgAttr.LinkURL, null, "LinkURL", true, false, 0, 200, 20);
           map.AddTBString(FrmImgAttr.LinkTarget, "_blank", "LinkTarget", true, false, 0, 200, 20);

           map.AddTBString(FrmImgAttr.GUID, null, "GUID", true, false, 0, 128, 20);

           //如果是 seal 就是岗位集合。
           map.AddTBString(FrmImgAttr.Tag0, null, "参数", true, false, 0, 500, 20);
           map.AddTBInt(FrmImgAttr.ImgSrcType, 0, "图片来源0=本地,1=URL", true, false);
           map.AddTBInt(FrmImgAttr.IsEdit, 0, "是否可以编辑", true, false);
           map.AddTBString(FrmImgAttr.Name, null, "中文名称", true, false, 0, 500, 20);
           map.AddTBString(FrmImgAttr.EnPK, null, "英文名称", true, false, 0, 500, 20);
           map.AddTBInt(MapAttrAttr.ColSpan, 0, "单元格数量", false, true);
           map.AddTBInt(MapAttrAttr.TextColSpan, 1, "文本单元格数量", false, true);
           map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", false, true);

           //显示的分组.
           map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组",BP.Sys.FrmUI.MapAttrString.SQLOfGroupAttr(), true);


		this.set_enMap(map);
		return this.get_enMap();
	}
	
	/**
	 * 生成MyPK
	 */
	@Override
	 protected  boolean beforeInsert() throws Exception
     {
         if(DataType.IsNullOrEmpty(this.getKeyOfEn()) == false)
             this.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn()) ;
         return super.beforeInsert();
     }
	

	/** 
	 是否存在相同的数据?
	 
	 @return 
	*/
	public final boolean IsExitGenerPK()
	{
		String sql = "SELECT COUNT(*) FROM " + this.getEnMap().getPhysicsTable() + " WHERE FK_MapData='" + this.getFK_MapData() + "' AND X=" + this.getX() + " AND Y=" + this.getY();
		if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
		{
			return false;
		}
		return true;
	}
	 
}