package BP.Sys;

import BP.DA.DBAccess;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;

/**
 * 通用实体
 */
public class GEEntity extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造函数	
	/** 
	 设置或者获取主键值.
	*/
	public final long getOID()
	{
		return this.GetValInt64ByKey("OID");
	}
	public final void setOID(long value)
	{
		this.SetValByKey("OID", value);
	}
	
	@Override
	public String getPK()	
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
	 * 主键
	 */
	public String FK_MapData = null;
	
	/**
	 * 通用实体
	 */
	public GEEntity()
	{
	}
	
	/**
	 * 通用实体
	 * 
	 * @param nodeid
	 *            节点ID
	 */
	public GEEntity(String fk_mapdata)
	{
		//this.getRow().clear();
		this.FK_MapData = fk_mapdata;
		
	}
	
	/**
	 * 通用实体
	 * 
	 * @param nodeid
	 *            节点ID
	 * @param _oid
	 *            OID
	 * @throws Exception 
	 */
	public GEEntity(String fk_mapdata, Object pk) throws Exception
	{
		 
		this.FK_MapData = fk_mapdata;
		this.setPKVal(pk);
		this.Retrieve();
	}
	
	// Map
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		// 注释掉，这里不需要再缓存了，因为有 BP.Sys.MapData.GenerHisMap 里缓存，
		// 否则会造成 流程查询 功能，设置列后无效问题
//		if (this.get_enMap() != null)
//		{
//			return this.get_enMap();
//		}
		
		if (this.FK_MapData == null)
		{
			throw new RuntimeException("没有给" + this.FK_MapData
					+ "值，您不能获取它的Map。");
		}
		 
		
		try {
			this.set_enMap(BP.Sys.MapData.GenerHisMap(this.FK_MapData));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.get_enMap();
	}
	
	/**
	 * GEEntitys
	 */
	@Override
	public Entities getGetNewEntities()
	{
		if (this.FK_MapData == null)
		{
			return new GEEntitys();
		}
		return new GEEntitys(this.FK_MapData);
	}
	
	private java.util.ArrayList _Dtls = null;
	
	public final java.util.ArrayList getDtls()
	{
		if (_Dtls == null)
		{
			_Dtls = new java.util.ArrayList();
		}
		return _Dtls;
	}
	
	/** 
	 * 把当前实体的数据copy到指定的主键数据表里.
	 * @param oid 指定的主键
	 * @throws Exception 
	 */
	public final void CopyToOID(long oid) throws Exception
	{
		//实例化历史数据表单entity.
		long oidOID = this.getOID();
		this.setOID(oid);
		this.Save();

		//复制从表数据.
		MapDtls dtls = new MapDtls(this.FK_MapData);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			//删除旧的数据.
			BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + this.getOID());

			GEDtls ensDtl = new GEDtls(dtl.getNo());
			ensDtl.Retrieve(GEDtlAttr.RefPK, oidOID);
			for (GEDtl enDtl : ensDtl.ToJavaList())
			{
				enDtl.setRefPK(String.valueOf(this.getOID()));
				enDtl.Insert();
			}
		}

		//复制附件数据.
		FrmAttachments aths = new FrmAttachments(this.FK_MapData);
		for (FrmAttachment ath : aths.ToJavaList())
		{
			//删除可能存在的新oid数据。
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='" + this.FK_MapData + "' AND RefPKVal='" + this.getOID() + "'");

			//找出旧数据.
			FrmAttachmentDBs athDBs = new FrmAttachmentDBs(this.FK_MapData, String.valueOf(oidOID));
			for (FrmAttachmentDB athDB : athDBs.ToJavaList())
			{
				FrmAttachmentDB athDB_N = new FrmAttachmentDB();
				athDB_N.Copy(athDB);

				athDB_N.setFK_MapData(this.FK_MapData);
				athDB_N.setRefPKVal(String.valueOf(this.getOID()));

				if (athDB_N.getHisAttachmentUploadType() == AttachmentUploadType.Single)
				{
					/*如果是单附件.*/
					athDB_N.setMyPK(athDB_N.getFK_FrmAttachment() + "_" + this.getOID());
					if (athDB_N.getIsExits() == true)
					{
						continue; //说明上一个节点或者子线程已经copy过了, 但是还有子线程向合流点传递数据的可能，所以不能用break.
					}

					athDB_N.Insert();
				}
				else
				{
					athDB_N.setMyPK(DBAccess.GenerGUID());
					athDB_N.Insert();
				}
			}
		}

	}
}