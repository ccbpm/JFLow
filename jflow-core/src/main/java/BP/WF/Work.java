package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.Port.*;
import BP.Web.WebUser;
import java.util.Date;

/**
 * WorkBase 的摘要说明。 工作
 */
public abstract class Work extends Entity {
	/**
	 * 检查MD5值是否通过
	 * 
	 * @return true/false
	 * @throws Exception
	 */
	public final boolean IsPassCheckMD5() throws Exception {
		String md51 = this.GetValStringByKey(WorkAttr.MD5);
		String md52 = Glo.GenerMD5(this);
		if (!md51.equals(md52)) {
			return false;
		}
		return true;
	}

	/**
	 * 主键
	 */
	@Override
	public String getPK() {
		return "OID";
	}

	/**
	 * classID
	 * 
	 * @throws Exception
	 */
	@Override
	public String getClassID() throws Exception {
		return "ND" + this.getHisNode().getNodeID();
	}

	/**
	 * 流程ID
	 */
	public long getFID() throws Exception {
		if (this.getHisNode().getHisRunModel() != RunModel.SubThread) {
			return 0;
		}
		return this.GetValInt64ByKey(WorkAttr.FID);
	}

	public void setFID(long value) throws Exception {
		if (this.getHisNode().getHisRunModel() != RunModel.SubThread) {
			this.SetValByKey(WorkAttr.FID, 0);
		} else {
			this.SetValByKey(WorkAttr.FID, value);
		}
	}

	/**
	 * workid,如果是空的就返回 0 .
	 * 
	 * @throws Exception
	 */
	public long getOID() throws Exception {
		return this.GetValInt64ByKey(WorkAttr.OID);
	}

	public void setOID(long value) throws Exception {
		this.SetValByKey(WorkAttr.OID, value);
	}

	/**
	 * 完成时间
	 */
	public final String getCDT() throws Exception {
		String str = this.GetValStringByKey(WorkAttr.CDT);
		if (str.length() < 5) {
			this.SetValByKey(WorkAttr.CDT, DataType.getCurrentDataTime());
		}

		return this.GetValStringByKey(WorkAttr.CDT);
	}

	/**
	 * 人员emps
	 */
	public final String getEmps() throws Exception {
		return this.GetValStringByKey(WorkAttr.Emps);
	}

	public final void setEmps(String value) throws Exception {
		this.SetValByKey(WorkAttr.Emps, value);
	}

	public final int RetrieveFID() throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhereIn(WorkAttr.OID, "(" + this.getFID() + "," + this.getOID() + ")");
		int i = qo.DoQuery();
		if (i == 0) {

			this.CheckPhysicsTable();
			throw new RuntimeException("@节点[" + this.getEnDesc() + "]数据丢失：WorkID=" + this.getOID() + " FID="
					+ this.getFID() + " sql=" + qo.getSQL());

		}
		return i;
	}

	/**
	 * 记录时间
	 * 
	 * @throws Exception
	 */
	public final String getRDT() throws Exception {
		return this.GetValStringByKey(WorkAttr.RDT);
	}

	public final String getRDT_Date() {
		try {
			return DataType.dateToStr(DataType.ParseSysDate2DateTime(this.getRDT()), DataType.getSysDataFormat());
		} catch (java.lang.Exception e) {
			return DataType.getCurrentDate();
		}
	}

	public final Date getRDT_DateTime() {
		try {
			return DataType.ParseSysDate2DateTime(this.getRDT_Date());
		} catch (java.lang.Exception e) {
			return new Date();
		}
	}

	public final String getRecord_FK_NY() throws Exception {
		return this.getRDT().substring(0, 7);
	}

	/**
	 * 记录人
	 * 
	 * @throws Exception
	 */
	public final String getRec() throws Exception {
		String str = this.GetValStringByKey(WorkAttr.Rec);
		if (str.equals("")) {
			this.SetValByKey(WorkAttr.Rec, WebUser.getNo());
		}

		return this.GetValStringByKey(WorkAttr.Rec);
	}

	public final void setRec(String value) throws Exception {
		this.SetValByKey(WorkAttr.Rec, value);
	}

	/**
	 * 工作人员
	 * 
	 * @throws Exception
	 */
	public final Emp getRecOfEmp() throws Exception {
		return new Emp(this.getRec());
	}

	/**
	 * 记录人名称
	 * 
	 * @throws Exception
	 */
	public final String getRecText() throws Exception {
		try {
			return this.getHisRec().getName();
		} catch (java.lang.Exception e) {
			return this.getRec();
		}
	}

	public final void setRecText(String value) throws Exception {
		this.SetValByKey("RecText", value);
	}

	private Node _HisNode = null;

	/**
	 * 工作的节点.
	 * 
	 * @throws Exception
	 */
	public final Node getHisNode() throws Exception {
		if (this._HisNode == null) {
			this._HisNode = new Node(this.getNodeID());
		}
		return _HisNode;
	}

	public final void setHisNode(Node value) {
		_HisNode = value;
	}

	/**
	 * 从表.
	 * 
	 * @throws Exception
	 */
	public final MapDtls getHisMapDtls() throws Exception {
		return this.getHisNode().getMapData().getMapDtls();
	}

	/**
	 * 从表.
	 * 
	 * @throws Exception
	 */
	public final FrmAttachments getHisFrmAttachments() throws Exception {
		return this.getHisNode().getMapData().getFrmAttachments();
	}

	/**
	 * 跨度天数
	 * 
	 * @throws Exception
	 */
	public final int getSpanDays() throws Exception {
		if (this.getCDT().equals(this.getRDT())) {
			return 0;
		}
		return DataType.SpanDays(this.getRDT(), this.getCDT());
	}

	/**
	 * 得到从工作完成到现在的日期
	 * 
	 * @return
	 * @throws Exception
	 */
	public final int GetCDTimeLimits(String todata) throws Exception {
		return DataType.SpanDays(this.getCDT(), todata);
	}

	/**
	 * 他的记录人
	 * 
	 * @throws Exception
	 */
	public final Emp getHisRec() throws Exception {
		Object tempVar = this.GetValByKey("HisRec" + this.getRec());
		Emp emp = tempVar instanceof Emp ? (Emp) tempVar : null;
		if (emp == null) {
			emp = new Emp(this.getRec());
			this.SetValByKey("HisRec" + this.getRec(), emp);
		}
		return emp;
	}

	/**
	 * 工作
	 */
	protected Work() {
	}

	/**
	 * 工作
	 * 
	 * @param oid
	 *            WFOID
	 * @throws Exception
	 */
	protected Work(long oid) throws Exception {
		this.SetValByKey(EntityOIDAttr.OID, oid);
		this.Retrieve();
	}

	/**
	 * 按照指定的OID Insert.
	 * 
	 * @throws Exception
	 */
	public final void InsertAsOID(long oid) throws Exception {
		this.SetValByKey("OID", oid);
		this.RunSQL(SqlBuilder.Insert(this));
	}

	/**
	 * 按照指定的OID 保存
	 * 
	 * @param oid
	 * @throws Exception
	 */
	public final void SaveAsOID(long oid) throws Exception {
		this.SetValByKey("OID", oid);
		if (this.RetrieveNotSetValues().Rows.size() == 0) {
			this.InsertAsOID(oid);
		}
		this.Update();
	}

	/**
	 * 保存实体信息
	 * 
	 * @throws Exception
	 */

	public final int Save() throws Exception {
		if (this.getOID() <= 10) {
			throw new RuntimeException("@没有给WorkID赋值,不能保存.");
		}
		if (this.Update() == 0) {
			this.InsertAsOID(this.getOID());
			return 0;
		}
		return 1;
	}

	@Override
	public void Copy(DataRow dr) throws Exception {
		for (Attr attr : this.getEnMap().getAttrs()) {
			if (WorkAttr.CDT.equals(attr.getKey()) || WorkAttr.RDT.equals(attr.getKey())
					|| WorkAttr.Rec.equals(attr.getKey()) || WorkAttr.FID.equals(attr.getKey())
					|| WorkAttr.OID.equals(attr.getKey()) || attr.getKey().equals("No")
					|| attr.getKey().equals("Name")) {
				continue;
			}

			try {
				this.SetValByKey(attr.getKey(), dr.getValue(attr.getKey()));
			} catch (java.lang.Exception e) {
			}
		}
	}

	@Override
	public void Copy(Entity fromEn) throws Exception {
		if (fromEn == null) {
			return;
		}
		Attrs attrs = fromEn.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if (WorkAttr.CDT.equals(attr.getKey()) || WorkAttr.RDT.equals(attr.getKey())
					|| WorkAttr.Rec.equals(attr.getKey()) || WorkAttr.FID.equals(attr.getKey())
					|| WorkAttr.OID.equals(attr.getKey()) || WorkAttr.Emps.equals(attr.getKey())
					|| attr.getKey().equals("No") || attr.getKey().equals("Name")) {
				continue;
			}
			this.SetValByKey(attr.getKey(), fromEn.GetValByKey(attr.getKey()));
		}
	}

	/**
	 * 删除主表数据也要删除它的明细数据
	 * 
	 * @throws Exception
	 */
	@Override
	protected void afterDelete() throws Exception {
		super.afterDelete();
	}

	/**
	 * 更新之前
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean beforeUpdate() throws Exception {
		return super.beforeUpdate();
	}

	/**
	 * 直接的保存前要做的工作
	 * 
	 * @throws Exception
	 */
	public void BeforeSave() throws Exception {

		// 执行保存前的事件。
		this.getHisNode().getHisFlow().DoFlowEventEntity(EventListOfNode.SaveBefore, this.getHisNode(),
				this.getHisNode().getHisWork(), "@WorkID=" + this.getOID() + "@FID=" + this.getFID());
	}

	/**
	 * 直接的保存
	 * 
	 * @throws Exception
	 */
	public final void DirectSave() throws Exception {
		this.beforeUpdateInsertAction();
		if (this.DirectUpdate() == 0) {
			this.SetValByKey(WorkAttr.RDT, DateUtils.getCurrentDate("yyyy-MM-dd"));
			this.DirectInsert();
		}
	}

	public String NodeFrmID = "";
	protected int _nodeID = 0;

	public final int getNodeID() {
		if (_nodeID == 0) {
			throw new RuntimeException("您没有给_Node给值。");
		}
		return this._nodeID;
	}

	public final void setNodeID(int value) {
		if (this._nodeID != value) {
			this._nodeID = value;
			this.set_enMap(null);
		}
		this._nodeID = value;
	}

	/**
	 * 已经路过的节点
	 */
	public String HisPassedFrmIDs = "";
}