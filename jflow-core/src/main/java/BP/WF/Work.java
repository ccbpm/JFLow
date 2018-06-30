package BP.WF;

import java.text.ParseException;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entity;
import BP.En.EntityOIDAttr;
import BP.En.FieldType;
import BP.En.QueryObject;
import BP.En.SqlBuilder;
import BP.Port.Emp;
import BP.Sys.EventListOfNode;
import BP.Sys.FrmAttachments;
import BP.Sys.MapDtls;
import BP.Sys.SystemConfig;
import BP.Tools.DateUtils;

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
	public final boolean IsPassCheckMD5()
	{
		String md51 = this.GetValStringByKey(WorkAttr.MD5);
		String md52 = Glo.GenerMD5(this);
		if (!md51.equals(md52))
		{
			return false;
		}
		return true;
	}


	//(必须的属性)
	@Override
	public String getPK()
	{
		return "OID";
	}
	/** 
	 classID
	*/
	@Override
	public String getClassID()
	{
		try {
			return "ND"+this.getHisNode().getNodeID();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/** 
	 流程ID
	 * @throws Exception 
	*/
	public long getFID() throws Exception
	{
		if (this.getHisNode().getHisRunModel() != RunModel.SubThread)
		{
			return 0;
		}
		return this.GetValInt64ByKey(WorkAttr.FID);
	}
	public void setFID(long value) throws Exception
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
	public long getOID()
	{
		return this.GetValInt64ByKey(WorkAttr.OID);
	}
	public void setOID(long value)
	{
		this.SetValByKey(WorkAttr.OID, value);
	}
	/** 
	 完成时间
	*/
	public final String getCDT()
	{
		String str = this.GetValStringByKey(WorkAttr.CDT);
		if (str.length() < 5)
		{
			this.SetValByKey(WorkAttr.CDT, DataType.getCurrentDataTime());
		}

		return this.GetValStringByKey(WorkAttr.CDT);
	}
	public final String getEmps()
	{
		return this.GetValStringByKey(WorkAttr.Emps);
	}
	public final void setEmps(String value)
	{
		this.SetValByKey(WorkAttr.Emps, value);
	}
	@Override
	public int RetrieveFromDBSources() throws Exception
	{
		try
		{
			return super.RetrieveFromDBSources();
		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}
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
	@Override
	public int Retrieve() throws Exception
	{
		try
		{
			return super.Retrieve();
		}
		catch (RuntimeException ex)
		{
			this.CheckPhysicsTable();
			throw ex;
		}
	}
	/** 
	 记录时间
	*/
	public final String getRDT()
	{
		return this.GetValStringByKey(WorkAttr.RDT);
	}
	public final String getRDT_Date()
	{
		try
		{
			return DataType.dateToStr(DataType.ParseSysDate2DateTime(this.getRDT()),DataType.getSysDataFormat());
		}
		catch (java.lang.Exception e)
		{
			return DataType.getCurrentData();
		}
	}
	public final java.util.Date getRDT_DateTime()
	{
		try
		{
			return DataType.ParseSysDate2DateTime(this.getRDT_Date());
		}
		catch (java.lang.Exception e)
		{
			return new java.util.Date();
		}
	}
	public final String getRecord_FK_NY()
	{
		return this.getRDT().substring(0, 7);
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
			this.SetValByKey(WorkAttr.Rec, BP.Web.WebUser.getNo());
		}

		return this.GetValStringByKey(WorkAttr.Rec);
	}
	public final void setRec(String value)
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
	/** 
	 记录人名称
	 * @throws Exception 
	*/
	public final String getRecText() throws Exception
	{
		try
		{
			return this.getHisRec().getName();
		}
		catch (java.lang.Exception e)
		{
			return this.getRec();
		}
	}
	public final void setRecText(String value)
	{
		this.SetValByKey("RecText", value);
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
		
	/** 
	 跨度天数
	 * @throws ParseException 
	*/
	public final int getSpanDays() throws ParseException
	{
		if (this.getCDT().equals(this.getRDT()))
		{
			return 0;
		}
		return DataType.SpanDays(this.getRDT(), this.getCDT());
	}
	/** 
	 得到从工作完成到现在的日期
	 
	 @return 
	 * @throws ParseException 
	*/
	public final int GetCDTimeLimits(String todata) throws ParseException
	{
		return DataType.SpanDays(this.getCDT(), todata);
	}
	/** 
	 他的记录人
	 * @throws Exception 
	 
	*/
	public final Emp getHisRec() throws Exception
	{
		  //  return new Emp(this.Rec);
		Object tempVar = this.GetValByKey("HisRec"+this.getRec());
		Emp emp = (Emp)((tempVar instanceof Emp) ? tempVar : null);
		if (emp == null)
		{
			emp = new Emp(this.getRec());
			this.SetValByKey("HisRec" + this.getRec(), emp);
		}
		return emp;
	}
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
	  
	/** 
	 产生要执行的url.
	 * @throws Exception 
	 
	*/
	public final String GenerNextUrl() throws Exception
	{
		String appName = BP.Sys.Glo.getRequest().getRemoteAddr();
		String ip = (String) SystemConfig.getAppSettings().get("CIP");
		if (ip == null || ip.equals(""))
		{
			throw new RuntimeException("@您没有设置CIP");
		}
		return "http://" + ip + "/" + appName + "/WF/Port.jsp?UserNo=" + BP.Web.WebUser.getNo() + "&DoWhat=DoNode&WorkID=" + this.getOID() + "&FK_Node=" + this.getHisNode().getNodeID() + "&Key=MyKey";
	}
	public final void DoAutoFull(Attr attr)
	{
		if (this.getOID() == 0)
		{
			return;
		}

		if (attr.AutoFullDoc == null || attr.AutoFullDoc.length() == 0)
		{
			return;
		}

		String objval = null;

		// 这个代码需要提纯到基类中去。
		switch (attr.autoFullWay)
		{
			case Way0:
				return;
			case Way1_JS:
				break;
			case Way2_SQL:
				String sql = attr.AutoFullDoc;
				Attrs attrs1 = this.getEnMap().getAttrs();
				for (Attr a1 : attrs1)
				{
					if (a1.getIsNum())
					{
						sql = sql.replace("@" + a1.getKey(), this.GetValStringByKey(a1.getKey()));
					}
					else
					{
						sql = sql.replace("@" + a1.getKey(), "'" + this.GetValStringByKey(a1.getKey()) + "'");
					}
				}

				objval = DBAccess.RunSQLReturnString(sql);
				break;
			case Way3_FK:
				try
				{
					String sqlfk = "SELECT @Field FROM @Table WHERE No=@AttrKey";
					String[] strsFK = attr.AutoFullDoc.split("[@]", -1);
					for (String str : strsFK)
					{
						if (str == null || str.length() == 0)
						{
							continue;
						}

						String[] ss = str.split("[=]", -1);
						if (ss[0].equals("AttrKey"))
						{
							sqlfk = sqlfk.replace('@' + ss[0], "'" + this.GetValStringByKey(ss[1]) + "'");
						}
						else
						{
							sqlfk = sqlfk.replace('@' + ss[0], ss[1]);
						}
					}
					sqlfk = sqlfk.replace("''", "'");

					objval = DBAccess.RunSQLReturnString(sqlfk);
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@在处理自动完成：外键[" + attr.getKey() + ";" + attr.getDesc() + "],时出现错误。异常信息：" + ex.getMessage());
				}
				break;
			case Way4_Dtl:
				String mysql = "SELECT @Way(@Field) FROM @Table WHERE RefPK='"+ this.getOID()+"'";
				String[] strs = attr.AutoFullDoc.split("[@]", -1);
				for (String str : strs)
				{
					if (str == null || str.length() == 0)
					{
						continue;
					}

					String[] ss = str.split("[=]", -1);
					mysql = mysql.replace('@' + ss[0], ss[1]);
				}
				objval = DBAccess.RunSQLReturnString(mysql);
				break;
			default:
				throw new RuntimeException("未涉及到的类型。");
		}
		if (objval == null)
		{
			return;
		}

		if (attr.getIsNum())
		{
			try
			{
				java.math.BigDecimal d = new java.math.BigDecimal(objval);
				this.SetValByKey(attr.getKey(), objval);
			}
			catch (java.lang.Exception e)
			{
			}
		}
		else
		{
			this.SetValByKey(attr.getKey(), objval);
		}
		return;
	}


		///#endregion


		///#region  重写基类的方法。
	/** 
	 按照指定的OID Insert.
	 
	*/
	public final void InsertAsOID(long oid)
	{
		this.SetValByKey("OID", oid);
		this.RunSQL(SqlBuilder.Insert(this));
	}
	/** 
	 按照指定的OID Insert. 区分大小写
	 
	*/
	public final void InsertAsOID_2017(long oid)
	{
		this.SetValByKey_2017("OID", oid);
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
	/** 
	 保存实体信息 区分大小写
	 * @throws Exception 
	 
	*/
	public final int Save_2017() throws Exception
	{
		if (this.getOID() <= 10)
		{
			throw new RuntimeException("@没有给WorkID赋值,不能保存.");
		}
		if (this.Update() == 0)
		{
			this.InsertAsOID_2017(this.getOID());
			return 0;
		}
		return 1;
	}
	@Override
	public void Copy(DataRow dr)
	{
		for (Attr attr : this.getEnMap().getAttrs())
		{
			if (WorkAttr.CDT.equals(attr.getKey()) || WorkAttr.RDT.equals(attr.getKey()) || WorkAttr.Rec.equals(attr.getKey()) || WorkAttr.FID.equals(attr.getKey()) || WorkAttr.OID.equals(attr.getKey()) || attr.getKey().equals("No") || attr.getKey().equals("Name"))
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
	public void Copy(Entity fromEn)
	{
		if (fromEn == null)
		{
			return;
		}
		Attrs attrs = fromEn.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (WorkAttr.CDT.equals(attr.getKey()) || WorkAttr.RDT.equals(attr.getKey()) || WorkAttr.Rec.equals(attr.getKey()) || WorkAttr.FID.equals(attr.getKey()) || WorkAttr.OID.equals(attr.getKey()) || WorkAttr.Emps.equals(attr.getKey()) || attr.getKey().equals("No") || attr.getKey().equals("Name"))
			{
				continue;
			}
			this.SetValByKey(attr.getKey(), fromEn.GetValByKey(attr.getKey()));
		}
	}
	/** 
	 删除主表数据也要删除它的明细数据
	 * @throws Exception 
	 
	*/
	@Override
	protected void afterDelete() throws Exception
	{

			///#warning 删除了明细，有可能造成其他的影响.
		//MapDtls dtls = this.HisNode.MapData.MapDtls;
		//foreach (MapDtl dtl in dtls)。
		//    DBAccess.RunSQL("DELETE FROM  " + dtl.PTable + " WHERE RefPK=" + this.OID);

		super.afterDelete();
	}

		///#endregion


		///#region  公共方法
	/** 
	 更新之前
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdate() throws Exception
	{

			///#region 特殊处理
		try
		{
			if (this.GetValStrByKey("WFState").equals("Runing"))
			{
				this.SetValByKey("WFState", WFState.Runing.getValue());
			}
		}
		catch (RuntimeException ex)
		{
		}

			///#endregion

		return super.beforeUpdate();
	}
	/** 
	 直接的保存前要做的工作
	 * @throws Exception 
	 
	*/
	public void BeforeSave() throws Exception
	{
		//执行自动计算.
		this.AutoFull();
		// 执行保存前的事件。

		this.getHisNode().getHisFlow().DoFlowEventEntity(EventListOfNode.SaveBefore, this.getHisNode(), this.getHisNode().getHisWork(), null);
	}
	/** 
	 直接的保存
	 * @throws Exception 
	 
	*/
	public final void DirectSave() throws Exception
	{
		this.beforeUpdateInsertAction();
		if (this.DirectUpdate() == 0)
		{
			this.SetValByKey(WorkAttr.RDT, DateUtils.getCurrentDate("yyyy-MM-dd"));
			this.DirectInsert();
		}
	}
	public String NodeFrmID = "";
	protected int _nodeID = 0;
	public String HisPassedFrmIDs;
	public final int getNodeID()
	{
		if (_nodeID == 0)
		{
			throw new RuntimeException("您没有给_Node给值。");
		}
		return this._nodeID;
	}
	public final void setNodeID(int value)
	{
		if (this._nodeID != value)
		{
			this._nodeID = value;
			this.set_enMap(null);
		}
		this._nodeID = value;
	}
}