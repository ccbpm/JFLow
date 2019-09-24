package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 通用实体
*/
public class GEEntityWordFrm extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#region 属性。
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
	 * @throws Exception 
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
	 * @throws Exception 
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
	 * @throws Exception 
	*/
	   public final String getFilePath() throws Exception
	   {
		   return this.GetValStringByKey(GEEntityWordFrmAttr.FilePath);
	   }
	   public final void setFilePath(String value) throws Exception
	   {
		   this.SetValByKey(GEEntityWordFrmAttr.FilePath, value);
	   }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#endregion 属性。


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
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
		this.FK_MapData = fk_mapdata;
	}
	/** 
	 通用实体
	 
	 @param nodeid 节点ID
	 @param _oid OID
	 * @throws Exception 
	*/
	public GEEntityWordFrm(String fk_mapdata, Object pk) throws Exception
	{
		this.FK_MapData = fk_mapdata;
		this.setPKVal(pk);
		this.Retrieve();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Map
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		if (this.FK_MapData == null)
		{
			throw new RuntimeException("没有给" + this.FK_MapData + "值，您不能获取它的Map。");
		}

		this.set_enMap(BP.Sys.MapData.GenerHisMap(this.FK_MapData));
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

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