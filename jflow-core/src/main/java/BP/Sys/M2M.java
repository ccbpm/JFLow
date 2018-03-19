package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
  M2M 数据存储
 
*/
public class M2M extends EntityMyPK
{
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(M2MAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(M2MAttr.FK_MapData, value);
	}
	/** 
	 选择数
	 
	*/
	public final int getNumSelected()
	{
		return this.GetValIntByKey(M2MAttr.NumSelected);
	}
	public final void setNumSelected(int value)
	{
		this.SetValByKey(M2MAttr.NumSelected, value);
	}
	public final long getEnOID()
	{
		return this.GetValInt64ByKey(M2MAttr.EnOID);
	}
	public final void setEnOID(long value)
	{
		this.SetValByKey(M2MAttr.EnOID, value);
	}
	/** 
	 操作对象对于m2mm有效
	 
	*/
	public final String getDtlObj()
	{
		return this.GetValStrByKey(M2MAttr.DtlObj);
	}
	public final void setDtlObj(String value)
	{
		this.SetValByKey(M2MAttr.DtlObj, value);
	}
	public final String getValsSQL()
	{
		return this.GetValStrByKey(M2MAttr.ValsSQL);
	}
	public final void setValsSQL(String value)
	{
		this.SetValByKey(M2MAttr.ValsSQL, value);
	}
	public final String getValsName()
	{
		return this.GetValStrByKey(M2MAttr.ValsName);
	}
	public final void setValsName(String value)
	{
		this.SetValByKey(M2MAttr.ValsName, value);
	}
	/** 
	 数据
	 
	*/
	public final String getVals()
	{
		return this.GetValStrByKey(M2MAttr.Doc);
	}
	public final void setVals(String value)
	{
		this.SetValByKey(M2MAttr.Doc, value);
	}
	public final String getM2MNo()
	{
		return this.GetValStrByKey(M2MAttr.M2MNo);
	}
	public final void setM2MNo(String value)
	{
		this.SetValByKey(M2MAttr.M2MNo, value);
	}

		
	/** 
	 M2M数据存储
	 
	*/
	public M2M()
	{
	}
	/** 
	 M2M数据存储
	 
	 @param fk_mapdata 表单ID
	 @param m2mNo
	 @param enOID
	 @param dtlObj
	*/
	public M2M(String fk_mapdata, String m2mNo, long enOID, String dtlObj)
	{
		this.setFK_MapData(fk_mapdata);
		this.setM2MNo(m2mNo);
		this.setEnOID(enOID);
		this.setDtlObj(dtlObj);
		this.InitMyPK();
		this.Retrieve();
	}
	/** 
	 M2M数据存储
	 
	*/
	public M2M(String fk_mapdata, String m2mNo, int enOID)
	{
		this.setFK_MapData(fk_mapdata);
		this.setM2MNo(m2mNo);
		this.setEnOID(enOID);
		this.InitMyPK();
		this.Retrieve();
	}

		///#endregion

	/** 
	 M2M数据存储
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_M2M", "M2M数据存储");
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddMyPK();
		map.AddTBString(M2MAttr.FK_MapData, null, "FK_MapData", true, true, 0, 100, 20);
		map.AddTBString(M2MAttr.M2MNo, null, "M2MNo", true, true, 0, 20, 20);

		map.AddTBInt(M2MAttr.EnOID, 0, "实体OID", true, false);
		map.AddTBString(M2MAttr.DtlObj, null, "DtlObj(对于m2mm有效)", true, true, 0, 20, 20);

		map.AddTBStringDoc();
		map.AddTBStringDoc(M2MAttr.ValsName, null, "ValsName", true, true);
		map.AddTBStringDoc(M2MAttr.ValsSQL, null, "ValsSQL", true, true);

		map.AddTBInt(M2MAttr.NumSelected, 0, "选择数", true, false);
		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, false, 0, 128, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 更新前
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.InitMyPK();
		return super.beforeUpdateInsertAction();
	}
	public final void InitMyPK()
	{
		this.setMyPK(this.getFK_MapData() + "_" + this.getM2MNo() + "_" + this.getEnOID() + "_" + this.getDtlObj());
	}
}