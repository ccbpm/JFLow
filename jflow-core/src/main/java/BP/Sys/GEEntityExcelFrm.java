package BP.Sys;

import BP.En.*;
import java.util.*;

/** 
 excel表单实体
*/
public class GEEntityExcelFrm extends EntityOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性。
	/** 
	 最后修改人
	*/
	public final String getLastEditer()
	{
		return this.GetValStringByKey(GEEntityExcelFrmAttr.LastEditer);
	}
	public final void setLastEditer(String value)
	{
		this.SetValByKey(GEEntityExcelFrmAttr.LastEditer, value);
	}
	/** 
	 记录时间
	*/
	public final String getRDT()
	{
		return this.GetValStringByKey(GEEntityExcelFrmAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(GEEntityExcelFrmAttr.RDT, value);
	}
	/** 
	 文件路径
	*/
	public final String getFilePath()
	{
		return this.GetValStringByKey(GEEntityExcelFrmAttr.FilePath);
	}
	public final void setFilePath(String value)
	{
		this.SetValByKey(GEEntityExcelFrmAttr.FilePath, value);
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
	public GEEntityExcelFrm()
	{
	}
	/** 
	 通用实体
	 
	 @param nodeid 节点ID
	*/
	public GEEntityExcelFrm(String fk_mapdata)
	{
		this.FK_MapData = fk_mapdata;
	}
	/** 
	 通用实体
	 
	 @param nodeid 节点ID
	 @param _oid OID
	*/
	public GEEntityExcelFrm(String fk_mapdata, int oid)
	{
		this.FK_MapData = fk_mapdata;
		this.setOID(oid);
		int i = this.RetrieveFromDBSources();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Map
	/** 
	 重写基类方法=
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
			return new GEEntityExcelFrms();
		}
		return new GEEntityExcelFrms(this.FK_MapData);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他属性.
	private ArrayList _Dtls = null;
	public final ArrayList getDtls()
	{
		if (_Dtls == null)
		{
			_Dtls = new ArrayList();
		}
		return _Dtls;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 其他属性.
}