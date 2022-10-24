package bp.sys;

import bp.en.*;
import java.util.*;

/** 
 通用实体
*/
public class GEEntityWordFrm extends Entity
{

///#region 属性。
	public final int getOID()throws Exception
	{
	   return this.GetValIntByKey(GEEntityWordFrmAttr.OID);
	}
	public final void setOID(int value) throws Exception
	{
		this.SetValByKey(GEEntityWordFrmAttr.OID, value);
	}
	/** 
	 最后修改人
	*/
	  public final String getLastEditer() throws Exception {
		 return this.GetValStringByKey(GEEntityWordFrmAttr.LastEditer);
	  }
	  public final void setLastEditer(String value)throws Exception
	  {
		  this.SetValByKey(GEEntityWordFrmAttr.LastEditer, value);
	  }
	/** 
	 记录时间
	*/
	   public final String getRDT()throws Exception
	   {
		  return this.GetValStringByKey(GEEntityWordFrmAttr.RDT);
	   }
	   public final void setRDT(String value)throws Exception
	   {
		   this.SetValByKey(GEEntityWordFrmAttr.RDT, value);
	   }

	/** 
	 文件路径
	*/
	   public final String getFilePath()throws Exception
	   {
		   return this.GetValStringByKey(GEEntityWordFrmAttr.FilePath);
	   }
	   public final void setFilePath(String value)throws Exception
	   {
		   this.SetValByKey(GEEntityWordFrmAttr.FilePath, value);
	   }

///#endregion 属性。



		///#region 构造函数
	@Override
	public String getPK()
	{
		return "OID";
	}

	public String getPK_Field()throws Exception
	{
		return "OID";
	}
	@Override
	public String toString()  {
		return this.FK_MapData;
	}
	@Override
	public String getClassID()
	{
		return this.FK_MapData;
	}
	/** 
	 主键
	*/
	public String FK_MapData = null;
	/** 
	 通用实体
	*/
	public GEEntityWordFrm()
	{
	}
	/** 
	 通用实体
	 
	 param fk_mapdata 节点ID
	*/
	public GEEntityWordFrm(String fk_mapdata)
	{
		this.FK_MapData=fk_mapdata;
	}
	/** 
	 通用实体
	 
	 param fk_mapdata
	 param pk
	*/
	public GEEntityWordFrm(String fk_mapdata, Object pk) throws Exception {
		this.FK_MapData=fk_mapdata;
		this.setPKVal(pk);
		this.Retrieve();
	}


	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		if (this.FK_MapData == null)
		{
			throw new RuntimeException("没有给" + this.FK_MapData + "值，您不能获取它的Map。");
		}

		try {
			this.set_enMap(MapData.GenerHisMap(this.FK_MapData));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.get_enMap();
	}
	/** 
	 GEEntitys
	*/
	@Override
	public Entities getGetNewEntities()
	{
		if (this.FK_MapData == null)
		{
			return new GEEntityWordFrms();
		}
		return new GEEntityWordFrms(this.FK_MapData);
	}

		///#endregion

	private ArrayList _Dtls = null;
	public final ArrayList getDtls()throws Exception
	{
		if (_Dtls == null)
		{
			_Dtls = new ArrayList();
		}
		return _Dtls;
	}
}