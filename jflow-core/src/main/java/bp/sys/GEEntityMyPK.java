package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import java.util.*;

/** 
 通用OID实体
*/
public class GEEntityMyPK extends Entity
{

		///#region 构造函数
	/** 
	 设置或者获取主键值.
	*/
	public final String getMyPK()  throws Exception
	{
		return this.GetValStrByKey("MyPK");
	}
	public final void setMyPK(String value) throws Exception
	{
		this.SetValByKey("MyPK", value);
	}
	/** 
	 主键值
	*/
	@Override
	public String getPK()
	{
		return "MyPK";
	}
	/** 
	  主键字段
	*/
	@Override
	public String getPK_Field()throws Exception
	{
		return "MyPK";
	}
	/** 
	 转化为类.
	 
	 @return 
	*/
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
	 通用OID实体
	*/
	public GEEntityMyPK()
	{
	}
	/** 
	 通用OID实体
	 
	 param fk_mapdata
	*/
	public GEEntityMyPK(String fk_mapdata)
	{
		this.FK_MapData=fk_mapdata;
	}
	/** 
	 通用OID实体
	 
	 param fk_mapdata
	 param pk
	*/
	public GEEntityMyPK(String fk_mapdata, String pk) throws Exception {
		this.FK_MapData=fk_mapdata;
		this.setPKVal(pk);
		this.Retrieve();
	}

		///#endregion


		///#region 构造映射.
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
	 GEEntityMyPKs
	*/
	@Override
	public Entities getGetNewEntities()
	{
		if (this.FK_MapData == null)
		{
			return new GEEntityMyPKs();
		}
		return new GEEntityMyPKs(this.FK_MapData);
	}

		///#endregion

	/** 
	 从另外的一个实体来copy数据.
	 
	 param en
	*/
	public final void CopyFromFrm(GEEntityMyPK en) throws Exception {
		//先求出来旧的OID.
		String oldOID = this.getMyPK();

		//复制主表数据.
		this.Copy(en);
		this.Save();
		this.setMyPK(oldOID);

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
			DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + this.getMyPK());

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
				dtlEnBlank.setRefPK(this.getMyPK().toString());
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
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='" + this.FK_MapData + "' AND RefPKVal='" + this.getMyPK() + "'");

			for (FrmAttachment athFrom : athsFrom.ToJavaList())
			{
				if (!athFrom.getNoOfObj().equals(ath.getNoOfObj()))
				{
					continue;
				}

				FrmAttachmentDBs athDBsFrom = new FrmAttachmentDBs();
				athDBsFrom.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athFrom.getMyPK(), FrmAttachmentDBAttr.RefPKVal, en.getMyPK().toString());
				for (FrmAttachmentDB athDBFrom : athDBsFrom.ToJavaList())
				{
					athDBFrom.setMyPK(DBAccess.GenerGUID());
					athDBFrom.setFK_MapData(this.FK_MapData);
					athDBFrom.setFK_FrmAttachment(ath.getMyPK());
					athDBFrom.setRefPKVal(this.getMyPK().toString());
					athDBFrom.Insert();
				}

			}
		}
	}
	/** 
	 把当前实体的数据copy到指定的主键数据表里.
	 
	 param oid 指定的主键
	*/
	public final void CopyToOID(String oid) throws Exception {
		//实例化历史数据表单entity.
		String oidOID = this.getMyPK();
		this.setMyPK(oid);
		this.Save();

		//复制从表数据.
		MapDtls dtls = new MapDtls(this.FK_MapData);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			//删除旧的数据.
			DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + this.getMyPK());

			GEDtls ensDtl = new GEDtls(dtl.getNo());
			ensDtl.Retrieve(GEDtlAttr.RefPK, oidOID);
			for (GEDtl enDtl : ensDtl.ToJavaList())
			{
				enDtl.setRefPK(this.getMyPK().toString());
				enDtl.InsertAsNew();
			}
		}

		//复制附件数据.
		FrmAttachments aths = new FrmAttachments(this.FK_MapData);
		for (FrmAttachment ath : aths.ToJavaList())
		{
			//删除可能存在的新oid数据。
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='" + this.FK_MapData + "' AND RefPKVal='" + this.getMyPK() + "'");

			//找出旧数据.
			FrmAttachmentDBs athDBs = new FrmAttachmentDBs(this.FK_MapData, oidOID.toString());
			for (FrmAttachmentDB athDB : athDBs.ToJavaList())
			{
				FrmAttachmentDB athDB_N = new FrmAttachmentDB();
				athDB_N.Copy(athDB);

				athDB_N.setFK_MapData(this.FK_MapData);
				athDB_N.setRefPKVal(this.getMyPK().toString());

				if (athDB_N.getHisAttachmentUploadType() == AttachmentUploadType.Single)
				{
					/*如果是单附件.*/
					athDB_N.setMyPK(athDB_N.getFK_FrmAttachment() + "_" + this.getMyPK());
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
	public final ArrayList getDtls()throws Exception
	{
		if (_Dtls == null)
		{
			_Dtls = new ArrayList();
		}
		return _Dtls;
	}
}