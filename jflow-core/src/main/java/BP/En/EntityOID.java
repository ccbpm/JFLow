package BP.En;

import BP.DA.DBAccess;
import BP.DA.Paras;

/**
 * OID实体,只有一个实体这个实体只有一个主键属性。
 */
public abstract class EntityOID extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 属性
	/**
	 * 是否是自动增长列
	 */
	public boolean getIsInnKey() {
		return false;
	}

	/**
	 * 主键
	 */
	@Override
	public String getPK() {
		return "OID";
	}

	/**
	 * OID, 如果是空的就返回 0 .
	 */
	public long getOID() {
		try {
			return this.GetValIntByKey(EntityOIDAttr.OID);
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	public final void setOID(int value) {
		this.SetValByKey(EntityOIDAttr.OID, value);
	}

	// 构造函数
	/**
	 * 构造一个空实例
	 */
	protected EntityOID() {
	}

	/**
	 * 根据OID构造实体
	 * 
	 * @param oid
	 *            oid
	 * @throws Exception
	 */
	protected EntityOID(int oid) throws Exception {
		this.SetValByKey(EntityOIDAttr.OID, oid);
		this.Retrieve();
	}

	// override 方法
	@Override
	public int DirectInsert() {
		try {
			this.setOID(DBAccess.GenerOID());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			return super.DirectInsert();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public final void InsertAsNew() throws Exception {
		this.setOID(0);
		this.Insert();
	}

	@Override
	public boolean getIsExits() throws Exception {
		if (this.getOID() == 0)
			return false;

		return super.getIsExits();
	}

	/**
	 * 删除之前的操作。
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean beforeDelete() throws Exception {
		if (super.beforeDelete() == false) {
			return false;
		}
		try {
			if (this.getOID() < 0) {
				throw new RuntimeException("@实体[" + this.getEnDesc() + "]没有被实例化，不能Delete().");
			}
			return true;
		} catch (RuntimeException ex) {
			throw new RuntimeException("@[" + this.getEnDesc() + "].beforeDelete err:" + ex.getMessage());
		}
	}

	/**
	 * beforeInsert 之前的操作。
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean beforeInsert() throws Exception {
		if (this.getOID() > 0) {
			throw new RuntimeException("@[" + this.getEnDesc() + "], 实体已经被实例化 oid=[" + this.getOID() + "]，不能Insert.");
		}

		if (this.getIsInnKey()) {
			this.setOID(-1);
		} else {
			try {
				this.setOID(BP.DA.DBAccess.GenerOID());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return super.beforeInsert();

	}

	/**
	 * beforeUpdate
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean beforeUpdate() throws Exception {
		if (!super.beforeUpdate()) {
			return false;
		}

		//
		// if (this.OID <= 0 )
		// throw new Exception("@实体["+this.EnDesc+"]没有被实例化，不能Update().");
		//
		return true;
	}

	protected String getSerialKey() {
		return "OID";
	}

	// public 方法
	/**
	 * 作为一个新的实体保存。
	 */
	public final void SaveAsNew() {
		try {
			this.setOID(DBAccess.GenerOIDByKey32(this.getSerialKey()));
			this.RunSQL(SqlBuilder.Insert(this));
		} catch (RuntimeException ex) {
			try {
				this.CheckPhysicsTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 按照指定的OID Insert.
	 * 
	 * @throws Exception
	 */
	public final void InsertAsOID(int oid) throws Exception {
		this.SetValByKey("OID", oid);
		try {
			this.RunSQL(SqlBuilder.Insert(this));
		} catch (RuntimeException ex) {
			this.CheckPhysicsTable();
			throw ex;
		}
	}

	public final void InsertAsOID(long oid) {
		this.SetValByKey("OID", oid);
		try {
			this.RunSQL(SqlBuilder.Insert(this));
		} catch (Exception ex) {
			try {
				this.CheckPhysicsTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 按照指定的OID 保存
	 * 
	 * @param oid
	 * @throws Exception
	 */
	public final void SaveAsOID(int oid) throws Exception {
		this.SetValByKey("OID", oid);
		if (!this.getIsExits()) {
			try {
				this.InsertAsOID(oid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.Update();
	}
}