package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 通用OID实体
*/
public class GEEntity extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 设置或者获取主键值.
	 * @throws Exception 
	*/
	public final long getOID() throws Exception
	{
		return this.GetValInt64ByKey("OID");
	}
	public final void setOID(long value) throws Exception
	{
		this.SetValByKey("OID", value);
	}
	/** 
	 主键值
	*/
	@Override
	public String getPK()
	{
		return "OID";
	}
	/** 
	  主键字段
	*/
	@Override
	public String getPKField()
	{
		return "OID";
	}
	/** 
	 转化为类.
	 
	 @return 
	*/
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
	 通用OID实体
	*/
	public GEEntity()
	{
	}
	/** 
	 通用OID实体
	 
	 @param nodeid 节点ID
	*/
	public GEEntity(String fk_mapdata)
	{
		this.FK_MapData = fk_mapdata;
	}
	/** 
	 通用OID实体
	 
	 @param nodeid 节点ID
	 @param _oid OID
	 * @throws Exception 
	*/
	public GEEntity(String fk_mapdata, Object pk) throws Exception
	{
		this.FK_MapData = fk_mapdata;
		this.setPKVal(pk);
		this.Retrieve();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造映射.
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
			return new GEEntitys();
		}
		return new GEEntitys(this.FK_MapData);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 从另外的一个实体来copy数据.
	 
	 @param en
	 * @throws Exception 
	*/
	public final void CopyFromFrm(GEEntity en) throws Exception
	{
		//先求出来旧的OID.
		long oldOID = this.getOID();

		//复制主表数据.
		this.Copy(en);
		this.Save();
		this.setOID(oldOID);

		//复制从表数据.
		MapDtls dtls = new MapDtls(this.FK_MapData);

		//被copy的明细集合.
		MapDtls dtlsFrom = new MapDtls(en.FK_MapData);

		if (dtls.size() != dtls.size())
		{
			throw new RuntimeException("@复制的两个表单从表不一致...");
		}

		//序号.
		int i = 0;
		for (MapDtl dtl : dtls.ToJavaList())
		{
			//删除旧的数据.
			BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getOID() + "'");

			//求对应的Idx的，从表配置.
			MapDtl dtlFrom = dtlsFrom.get(i) instanceof MapDtl ? (MapDtl)dtlsFrom.get(i) : null;
			GEDtls ensDtlFrom = new GEDtls(dtlFrom.getNo());
			ensDtlFrom.Retrieve(GEDtlAttr.RefPK, oldOID);

			//创建一个实体.
			GEDtl dtlEnBlank = new GEDtl(dtl.getNo());

			// 遍历数据,执行copy.
			for (GEDtl enDtlFrom : ensDtlFrom.ToJavaList())
			{
				dtlEnBlank.Copy(enDtlFrom);
				dtlEnBlank.setRefPK(String.valueOf(this.getOID()));
				dtlEnBlank.SaveAsNew();
			}
			i++;
		}

		//复制附件数据.
		FrmAttachments aths = new FrmAttachments(this.FK_MapData);
		FrmAttachments athsFrom = new FrmAttachments(en.FK_MapData);
		for (FrmAttachment ath : aths.ToJavaList())
		{
			//删除数据,防止copy重复
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='" + this.FK_MapData + "' AND RefPKVal='" + this.getOID() + "'");

			for (FrmAttachment athFrom : athsFrom.ToJavaList())
			{
				if (!athFrom.getNoOfObj().equals(ath.getNoOfObj()))
				{
					continue;
				}

				FrmAttachmentDBs athDBsFrom = new FrmAttachmentDBs();
				athDBsFrom.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athFrom.getMyPK(), FrmAttachmentDBAttr.RefPKVal, String.valueOf(en.getOID()));
				for (FrmAttachmentDB athDBFrom : athDBsFrom.ToJavaList())
				{
					athDBFrom.setMyPK(BP.DA.DBAccess.GenerGUID());
					athDBFrom.setFK_FrmAttachment(ath.getMyPK());
					athDBFrom.setRefPKVal(String.valueOf(this.getOID()));
					athDBFrom.Insert();
				}

			}
		}
	}
	/** 
	 把当前实体的数据copy到指定的主键数据表里.
	 
	 @param oid 指定的主键
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
			BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getOID() + "'");

			GEDtls ensDtl = new GEDtls(dtl.getNo());

		 //   var typeVal = BP.Sys.Glo.GenerRealType( ensDtl.GetNewEntity.getEnMap().getAttrs(), GEDtlAttr.RefPK, this.OID);

			ensDtl.Retrieve(GEDtlAttr.RefPK, String.valueOf(this.getOID()));

			for (GEDtl enDtl : ensDtl.ToJavaList())
			{
				enDtl.setRefPK(String.valueOf(this.getOID()));
				enDtl.InsertAsNew();
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