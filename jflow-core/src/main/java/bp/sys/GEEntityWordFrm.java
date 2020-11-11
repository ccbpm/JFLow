package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.*;
import java.util.*;

/** 
 通用实体
*/
public class GEEntityWordFrm extends Entity
{

///属性。
	public final int getOID() throws Exception
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
	  public final String getLastEditer() throws Exception
	  {
		 return this.GetValStringByKey(GEEntityWordFrmAttr.LastEditer);
	  }
	  public final void setLastEditer(String value) throws Exception
	  {
		  this.SetValByKey(GEEntityWordFrmAttr.LastEditer, value);
	  }
	/** 
	 记录时间
	*/
	   public final String getRDT() throws Exception
	   {
		  return this.GetValStringByKey(GEEntityWordFrmAttr.RDT);
	   }
	   public final void setRDT(String value) throws Exception
	   {
		   this.SetValByKey(GEEntityWordFrmAttr.RDT, value);
	   }

	/** 
	 文件路径
	*/
	   public final String getFilePath() throws Exception
	   {
		   return this.GetValStringByKey(GEEntityWordFrmAttr.FilePath);
	   }
	   public final void setFilePath(String value) throws Exception
	   {
		   this.SetValByKey(GEEntityWordFrmAttr.FilePath, value);
	   }

/// 属性。



		///构造函数
	@Override
	public String getPK()
	{
		return "OID";
	}
	@Override
	public String getPKField()
	{
		return "OID";
	}
	@Override
	public String toString()
	{
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
	 
	 @param nodeid 节点ID
	*/
	public GEEntityWordFrm(String fk_mapdata)
	{
		this.FK_MapData=fk_mapdata;
	}
	/** 
	 通用实体
	 
	 @param nodeid 节点ID
	 @param _oid OID
	 * @throws Exception 
	*/
	public GEEntityWordFrm(String fk_mapdata, Object pk) throws Exception
	{
		this.FK_MapData=fk_mapdata;
		this.setPKVal(pk);
		this.Retrieve();
	}

		///


		///Map
	/** 
	 重写基类方法
	 * @throws Exception 
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		if (this.FK_MapData == null)
		{
			throw new RuntimeException("没有给" + this.FK_MapData + "值，您不能获取它的Map。");
		}

		this.set_enMap(bp.sys.MapData.GenerHisMap(this.FK_MapData));
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

		///

	private ArrayList _Dtls = null;
	public final ArrayList getDtls()
	{
		if (_Dtls == null)
		{
			_Dtls = new ArrayList();
		}
		return _Dtls;
	}
}