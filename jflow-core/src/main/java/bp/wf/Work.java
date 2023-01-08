package bp.wf;

import bp.da.*;
import bp.en.*;
import bp.sys.*;

/** 
 WorkBase 的摘要说明。
 工作
*/
public abstract class Work extends Entity
{
	/** 
	 检查MD5值是否通过
	 
	 @return true/false
	*/
	public final boolean IsPassCheckMD5() throws Exception {
		String md51 = this.GetValStringByKey(WorkAttr.MD5);
		String md52 = Glo.GenerMD5(this);
		if (!md51.equals(md52))
		{
			return false;
		}
		return true;
	}


		///#region 基本属性(必须的属性)
	/** 
	 主键
	*/
	@Override
	public String getPK()  {
		return "OID";
	}
	/** 
	 classID
	*/
	@Override
	public String getClassID()  {
		return "ND" + this.getHisNode().getNodeID();
	}
	/** 
	 流程ID
	*/
	public long getFID() throws Exception {
		if (this.getHisNode().getHisRunModel() != RunModel.SubThread)
		{
			return 0;
		}

		return this.GetValInt64ByKey(WorkAttr.FID);

	}
	public void setFID(long value)throws Exception
	{if (this.getHisNode().getHisRunModel() != RunModel.SubThread)
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
	public long getOID()
	{
		return this.GetValInt64ByKey(WorkAttr.OID);
	}
	public void setOID(long value)  throws Exception
	 {
		this.SetValByKey(WorkAttr.OID, value);
	}
	/** 
	 人员emps
	*/
	public final String getEmps() throws Exception
	{
		return this.GetValStringByKey(WorkAttr.Emps);
	}
	public final void setEmps(String value)  throws Exception
	 {
		this.SetValByKey(WorkAttr.Emps, value);
	}
	public final int RetrieveFID() throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhereIn(WorkAttr.OID, "(" + this.getFID() + "," + this.getOID() + ")");
		int i = qo.DoQuery();
		if (i == 0)
		{
			if (!bp.difference.SystemConfig.getIsDebug())
			{
				this.CheckPhysicsTable();
				throw new RuntimeException("@节点[" + this.getEnDesc() + "]数据丢失：WorkID=" + this.getOID() + " FID=" + this.getFID() + " sql=" + qo.getSQL());
			}
		}
		return i;
	}
	/** 
	 记录人
	*/
	public final String getRec()  {
		String str = this.GetValStringByKey(WorkAttr.Rec);
		if (str.equals(""))
		{
			this.SetValByKey(WorkAttr.Rec, bp.web.WebUser.getNo());
		}

		return this.GetValStringByKey(WorkAttr.Rec);
	}
	public final void setRec(String value)
	 {
		this.SetValByKey(WorkAttr.Rec, value);
	}

	public final int getFrmVer()  {
		return this.GetParaInt("FrmVer", 0);
	}
	private Node _HisNode = null;
	/** 
	 工作的节点.
	*/
	public final Node getHisNode()  {
		if (this._HisNode == null)
		{
			try {
				this._HisNode = new Node(this.getNodeID());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _HisNode;
	}
	public final void setHisNode(Node value)
	{_HisNode = value;
	}
	/** 
	 从表.
	*/
	public final MapDtls getHisMapDtls() throws Exception {
		return this.getHisNode().getMapData().getMapDtls();
	}
	/** 
	 附件.
	*/
	public final FrmAttachments getHisFrmAttachments() throws Exception {
		return this.getHisNode().getMapData().getFrmAttachments();
	}

		///#endregion




		///#region 构造函数
	/** 
	 工作
	*/
	protected Work()  {
	}
	/** 
	 工作
	 
	 param oid WFOID
	*/
	protected Work(long oid)  throws Exception
	 {
		this.SetValByKey(EntityOIDAttr.OID, oid);
		this.Retrieve();
	}

		///#endregion


		///#region  重写基类的方法。
	/** 
	 按照指定的OID Insert.
	*/
	public final void InsertAsOID(long oid)  throws Exception
	 {
		this.SetValByKey("OID", oid);
		this.RunSQL(SqlBuilder.Insert(this));
	}
	/** 
	 按照指定的OID 保存
	 
	 param oid
	*/
	public final void SaveAsOID(long oid)  throws Exception
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
	*/
	public final int Save() throws Exception {
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
	public void Copy(DataRow dr) throws Exception {
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (attr.getKey().equals(WorkAttr.Rec) || attr.getKey().equals(WorkAttr.FID) || attr.getKey().equals(WorkAttr.OID)
					|| attr.getKey().equals("BillNo") || attr.getKey().equals("No") || attr.getKey().equals("Name"))
				continue;

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
	public void Copy(Entity fromEn) throws Exception {
		if (fromEn == null)
		{
			return;
		}
		Attrs attrs = fromEn.getEnMap().getAttrs();
		for (Attr attr : attrs.ToJavaList())
		{
			if (attr.getKey().equals(WorkAttr.Rec) || attr.getKey().equals(WorkAttr.FID) || attr.getKey().equals(WorkAttr.OID) || attr.getKey().equals(WorkAttr.Emps) || attr.getKey().equals("No") || attr.getKey().equals("Name"))
			{
				continue;
			}
			this.SetValByKey(attr.getKey(), fromEn.GetValByKey(attr.getKey()));
		}
	}

		///#endregion


		///#region  公共方法
	/** 
	 直接的保存
	*/
	public final void DirectSave() throws Exception {
		this.beforeUpdateInsertAction();
		if (this.DirectUpdate() == 0)
		{
			this.DirectInsert();
		}
	}
	public String NodeFrmID = "";
	protected int _nodeID = 0;
	public final int getNodeID()  {
		if (_nodeID == 0)
		{
			throw new RuntimeException("您没有给_Node给值。");
		}
		return this._nodeID;
	}
	public final void setNodeID(int value)
	{if (this._nodeID != value)
	{this._nodeID = value;
			this.set_enMap(null);
		}
		this._nodeID = value;
	}
	/** 
	 已经路过的节点
	*/
	public String HisPassedFrmIDs = "";


	///#endregion
}