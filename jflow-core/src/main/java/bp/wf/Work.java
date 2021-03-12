package bp.wf;

import java.util.List;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.*;
import bp.web.WebUser;
import bp.port.*;
import bp.wf.xml.*;
import bp.wf.template.*;

/** 
 WorkBase 的摘要说明。
 工作
*/
public abstract class Work extends Entity
{
	/** 
	 检查MD5值是否通过
	 
	 @return true/false
	 * @throws Exception 
	*/
	public final boolean IsPassCheckMD5() throws Exception
	{
		String md51 = this.GetValStringByKey(WorkAttr.MD5);
		String md52 = Glo.GenerMD5(this);
		if (!md51.equals(md52))
		{
			return false;
		}
		return true;
	}


		///基本属性(必须的属性)
	/** 
	 主键
	*/
	@Override
	public String getPK()
	{
		return "OID";
	}
	/** 
	 classID
	*/
	@Override
	public String getClassID()throws Exception
	{
		return "ND" + this.getHisNode().getNodeID();
	}
	/** 
	 流程ID
	*/
	public long getFID()throws Exception
	{
		if (this.getHisNode().getHisRunModel() != RunModel.SubThread)
		{
			return 0;
		}

		return this.GetValInt64ByKey(WorkAttr.FID);

	}
	public void setFID(long value)throws Exception
	{
		if (this.getHisNode().getHisRunModel() != RunModel.SubThread)
		{
			this.SetValByKey(WorkAttr.FID, 0);
		}
		else
		{
			this.SetValByKey(WorkAttr.FID, value);
		}
	}
	/** 
	 workid,如果是空的就返回 0 . 
	*/
	public long getOID()throws Exception
	{
		return this.GetValInt64ByKey(WorkAttr.OID);
	}
	public void setOID(long value)throws Exception
	{
		this.SetValByKey(WorkAttr.OID, value);
	}
	/** 
	 人员emps
	*/
	public final String getEmps()throws Exception
	{
		return this.GetValStringByKey(WorkAttr.Emps);
	}
	public final void setEmps(String value) throws Exception
	{
		this.SetValByKey(WorkAttr.Emps, value);
	}
	public final int RetrieveFID() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhereIn(WorkAttr.OID, "(" + this.getFID() + "," + this.getOID() + ")");
		int i = qo.DoQuery();
		if (i == 0)
		{
			if (SystemConfig.getIsDebug() == false)
			{
				this.CheckPhysicsTable();
				throw new RuntimeException("@节点[" + this.getEnDesc() + "]数据丢失：WorkID=" + this.getOID() + " FID=" + this.getFID() + " sql=" + qo.getSQL());
			}
		}
		return i;
	}
	/** 
	 记录人
	 * @throws Exception 
	*/
	public final String getRec() throws Exception
	{
		String str = this.GetValStringByKey(WorkAttr.Rec);
		if (str.equals(""))
		{
			this.SetValByKey(WorkAttr.Rec, WebUser.getNo());
		}

		return this.GetValStringByKey(WorkAttr.Rec);
	}
	public final void setRec(String value) throws Exception
	{
		this.SetValByKey(WorkAttr.Rec, value);
	}
	/** 
	 工作人员
	 * @throws Exception 
	*/
	public final Emp getRecOfEmp() throws Exception
	{
		return new Emp(this.getRec());
	}

	private Node _HisNode = null;
	/** 
	 工作的节点.
	 * @throws Exception 
	*/
	public final Node getHisNode() throws Exception
	{
		if (this._HisNode == null)
		{
			this._HisNode = new Node(this.getNodeID());
		}
		return _HisNode;
	}
	public final void setHisNode(Node value)
	{
		_HisNode = value;
	}
	/** 
	 从表.
	 * @throws Exception 
	*/
	public final MapDtls getHisMapDtls() throws Exception
	{
		return this.getHisNode().getMapData().getMapDtls();
	}
	/** 
	 从表.
	 * @throws Exception 
	*/
	public final FrmAttachments getHisFrmAttachments() throws Exception
	{
		return this.getHisNode().getMapData().getFrmAttachments();
	}

		///




		///构造函数
	/** 
	 工作
	*/
	protected Work()
	{
	}
	/** 
	 工作
	 
	 @param oid WFOID		 
	 * @throws Exception 
	*/
	protected Work(long oid) throws Exception
	{
		this.SetValByKey(EntityOIDAttr.OID, oid);
		this.Retrieve();
	}

		///


		/// 重写基类的方法。
	/** 
	 按照指定的OID Insert.
	 * @throws Exception 
	*/
	public final void InsertAsOID(long oid) throws Exception
	{
		this.SetValByKey("OID", oid);
		this.RunSQL(SqlBuilder.Insert(this));
	}
	/** 
	 按照指定的OID 保存
	 
	 @param oid
	 * @throws Exception 
	*/
	public final void SaveAsOID(long oid) throws Exception
	{
		this.SetValByKey("OID", oid);
		if (this.RetrieveNotSetValues().Rows.size() == 0)
		{
			this.InsertAsOID(oid);
		}
		this.Update();
	}
	/** 
	 保存实体信息
	 * @throws Exception 
	*/
	public final int Save() throws Exception
	{
		if (this.getOID() <= 10)
		{
			throw new RuntimeException("@没有给WorkID赋值,不能保存.");
		}
		if (this.Update() == 0)
		{
			this.InsertAsOID(this.getOID());
			return 0;
		}
		return 1;
	}
	@Override
	public void Copy(DataRow dr) throws Exception
	{
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (WorkAttr.Rec.equals(attr.getKey()) || WorkAttr.FID.equals(attr.getKey()) || WorkAttr.OID.equals(attr.getKey()) || attr.getKey().equals("No") || attr.getKey().equals("Name"))
			{
				continue;
			}

			try
			{
				this.SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
	@Override
	public void Copy(Entity fromEn) throws Exception
	{
		if (fromEn == null)
		{
			return;
		}
		Attrs attrs = fromEn.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (WorkAttr.Rec.equals(attr.getKey()) || WorkAttr.FID.equals(attr.getKey()) || WorkAttr.OID.equals(attr.getKey()) || WorkAttr.Emps.equals(attr.getKey()) || attr.getKey().equals("No") || attr.getKey().equals("Name"))
			{
				continue;
			}
			this.SetValByKey(attr.getKey(), fromEn.GetValByKey(attr.getKey()));
		}
	}

		///


		/// 公共方法
	/** 
	 直接的保存
	*/
	@Override
	public final void DirectSave() throws Exception
	{
		this.beforeUpdateInsertAction();
		if (this.DirectUpdate() == 0)
		{
			this.DirectInsert();
		}
	}
	public String NodeFrmID = "";
	protected int _nodeID = 0;
	public final int getNodeID()  throws Exception
	{
		if (_nodeID == 0)
		{
			throw new RuntimeException("您没有给_Node给值。");
		}
		return this._nodeID;
	}
	public final void setNodeID(int value) throws Exception
	{
		if (this._nodeID != value)
		{
			this._nodeID = value;
			this.set_enMap(null);
		}
		this._nodeID = value;
	}
	/** 
	 已经路过的节点
	*/
	public String HisPassedFrmIDs = "";

	
}