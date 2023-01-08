package bp.wf.template;

import bp.da.*;
import bp.difference.handler.CommonUtils;
import bp.en.Map;
import bp.port.DeptEmp;
import bp.sys.*;
import bp.en.*;
import bp.wf.Glo;
import bp.wf.data.*;
import bp.web.*;
import bp.difference.*;
import bp.wf.*;
import bp.wf.data.GERpt;

import java.util.*;

/** 
 条件
*/
public class Cond extends EntityMyPK
{

		///#region 参数属性.
	/** 
	 指定人员方式
	*/
	public final SpecOperWay getSpecOperWay() throws Exception {
		return SpecOperWay.forValue(this.GetParaInt(CondAttr.SpecOperWay, 0));
	}
	public final void setSpecOperWay(SpecOperWay value)throws Exception
	{this.SetPara(CondAttr.SpecOperWay, value.getValue());
	}
	/** 
	 指定人员参数
	*/
	public final String getSpecOperPara() throws Exception {
		return this.GetParaString(CondAttr.SpecOperPara);
	}
	public final void setSpecOperPara(String value)throws Exception
	{this.SetPara(CondAttr.SpecOperPara, value);
	}
	/** 
	 求指定的人员.
	*/
	public final String getSpecOper() throws Exception {
		SpecOperWay way = this.getSpecOperWay();
		if (way == SpecOperWay.CurrOper)
		{
			return WebUser.getNo();
		}


		if (way == SpecOperWay.SpecNodeOper)
		{
			String sql = "SELECT FK_Emp FROM WF_GenerWorkerlist WHERE FK_Node=" + this.getSpecOperPara() + " AND WorkID=" + this.getWorkID();
			String fk_emp = DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (fk_emp == null)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的节点[" + this.getSpecOperPara() + "]，作为处理人，但是该节点上没有人员。查询SQL:" + sql);
			}
			return fk_emp;
		}

		if (way == SpecOperWay.SpecSheetField)
		{
			if (this.en.getRow().containsKey(this.getSpecOperPara().replace("@", "")) == false)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的字段[" + this.getSpecOperPara() + "]作为处理人，但是该字段不存在。");
			}

			return this.en.GetValStringByKey(this.getSpecOperPara().replace("@", ""));
		}

		if (way == SpecOperWay.CurrOper)
		{
			if (this.en.getRow().containsKey(this.getSpecOperPara().replace("@", "")) == false)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的字段[" + this.getSpecOperPara() + "]作为处理人，但是该字段不存在。");
			}

			return this.en.GetValStringByKey(this.getSpecOperPara().replace("@", ""));
		}

		if (way == SpecOperWay.SpenEmpNo)
		{
			if (DataType.IsNullOrEmpty(this.getSpecOperPara()) == false)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的人员[" + this.getSpecOperPara() + "]作为处理人，但是人员参数没有设置。");
			}
			return this.getSpecOperPara();
		}

		throw new RuntimeException("@配置异常，没有判断的条件类型。");
	}

		///#endregion 参数属性.


		///#region 基本属性.
	public GERpt en = null;
	/** 
	 数据来源
	*/
	public final ConnDataFrom getHisDataFrom()  {
		return ConnDataFrom.forValue(this.GetValIntByKey(CondAttr.DataFrom));
	}
	public final void setHisDataFrom(ConnDataFrom value)  throws Exception
	 {
		this.SetValByKey(CondAttr.DataFrom, value.getValue());
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(CondAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(CondAttr.FK_Flow, value);
	}
	public final String getRefPKVal()
	{
		return this.GetValStringByKey(CondAttr.RefPKVal);
	}
	public final void setRefPKVal(String value)
	{
		this.SetValByKey(CondAttr.RefPKVal, value);
	}
	public final String getDataFromText()
	{
		return this.GetValStringByKey(CondAttr.DataFromText);
	}
	public final void setDataFromText(String value)
	{
		this.SetValByKey(CondAttr.DataFromText, value);
	}

	/** 
	 隶属流程编号，用于备份删除.
	*/
	public final String getRefFlowNo() throws Exception
	{
		return this.GetValStringByKey(CondAttr.RefFlowNo);
	}
	public final void setRefFlowNo(String value)  throws Exception
	 {
		this.SetValByKey(CondAttr.RefFlowNo, value);
	}
	/** 
	 数据源
	*/
	public final String getFK_DBSrc() throws Exception
	{
		return this.GetValStringByKey(CondAttr.FK_DBSrc);
	}
	public final void setFK_DBSrc(String value)  throws Exception
	 {
		this.SetValByKey(CondAttr.FK_DBSrc, value);
	}
	/** 
	 备注
	*/
	public final String getNote() throws Exception
	{
		return this.GetValStringByKey(CondAttr.Note);
	}
	public final void setNote(String value)  throws Exception
	 {
		this.SetValByKey(CondAttr.Note, value);
	}
	/** 
	 条件类型(表单条件，岗位条件，部门条件，开发者参数)
	*/
	public final CondType getCondType()  {
		return CondType.forValue(this.GetValIntByKey(CondAttr.CondType));
	}
	public final void setCondType(CondType value)  throws Exception
	 {
		this.SetValByKey(CondAttr.CondType, value.getValue());
	}
	public final int getCondTypeInt() throws Exception
	{
		return this.GetValIntByKey(CondAttr.CondType);
	}
	public final void setCondTypeInt(int value)  throws Exception
	 {
		this.SetValByKey(CondAttr.CondType, value);
	}
	/** 
	 节点ID
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(CondAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(CondAttr.FK_Node, value);
	}
	/** 
	 对方向条件有效
	*/
	public final int getToNodeID() throws Exception
	{
		return this.GetValIntByKey(CondAttr.ToNodeID);
	}
	public final void setToNodeID(int value)  throws Exception
	 {
		this.SetValByKey(CondAttr.ToNodeID, value);
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception {
		//设置他的主键。
		this.setMyPK(DBAccess.GenerGUID(0, null, null));
		return super.beforeInsert();
	}
	/** 
	 清除数据缓存
	*/
	@Override
	protected void afterInsertUpdateAction() throws Exception {
		Flow flow = new Flow(this.getFK_Flow());
		flow.ClearAutoNumCash(true);
		super.afterInsertUpdateAction();
	}
	@Override
	protected void afterDelete() throws Exception {
		Flow flow = new Flow(this.getFK_Flow());
		flow.ClearAutoNumCash(true);
		super.afterDelete();
	}

		///#region 实现基本的方方法
	/** 
	 属性
	*/
	public final String getFKAttr() throws Exception
	{
		return this.GetValStringByKey(CondAttr.FK_Attr);
	}
	public final void setFKAttr(String value)throws Exception
	{if (value == null)
		{
			throw new RuntimeException("FK_Attr不能设置为null");
		}

		value = value.trim();

		this.SetValByKey(CondAttr.FK_Attr, value);

		MapAttr attr = new MapAttr(value);

		if (attr.getLGType() == FieldTypeS.Enum)
		{
				/*是一个枚举类型的*/
			SysEnum se = new SysEnum(attr.getUIBindKey(), this.getOperatorValueInt());
			this.setOperatorValueT(se.getLab());
		}

		this.SetValByKey(CondAttr.AttrKey, attr.getKeyOfEn());
		this.SetValByKey(CondAttr.AttrName, attr.getName());

	}
	/** 
	 要运算的实体属性
	*/
	public final String getAttrKey()
	{
		return this.GetValStringByKey(CondAttr.AttrKey);
	}
	public final void setAttrKey(String value)  throws Exception
	 {
		this.SetValByKey(CondAttr.AttrKey, value);
	}
	/** 
	 属性名称
	*/
	public final String getAttrName() throws Exception
	{
		return this.GetValStringByKey(CondAttr.AttrName);
	}
	public final void setAttrName(String value)  throws Exception
	 {
		this.SetValByKey(CondAttr.AttrName, value);
	}
	/** 
	 Idx
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(CondAttr.Idx);
	}
	public final void setIdx(int value)  throws Exception
	 {
		this.SetValByKey(CondAttr.Idx, value);
	}
	/** 
	 操作的值
	*/
	public final String getOperatorValueT() throws Exception
	{
		return this.GetValStringByKey(CondAttr.OperatorValueT);
	}
	public final void setOperatorValueT(String value)  throws Exception
	 {
		this.SetValByKey(CondAttr.OperatorValueT, value);
	}
	/** 
	 运算符号
	*/
	public final String getFKOperator()  {
		String s = this.GetValStringByKey(CondAttr.FK_Operator);
		if (s == null || s.equals(""))
		{
			return "=";
		}
		return s;
	}
	public final void setFKOperator(String value)throws Exception
	{String val = "";

		switch (value)
	{case "dengyu":
				val = "=";
				break;
			case "dayu":
				val = ">";
				break;
			case "dayudengyu":
				val = ">=";
				break;
			case "xiaoyu":
				val = "<";
				break;
			case "xiaoyudengyu":
				val = "<=";
				break;
			case "budengyu":
				val = "!=";
				break;
			case "like":
				val = " LIKE ";
				break;
			default:
				break;
		}

		this.SetValByKey(CondAttr.FK_Operator, val);
	}
	/** 
	 运算值
	*/
	public final Object getOperatorValue()  {
		String s = this.GetValStringByKey(CondAttr.OperatorValue);
		s = s.replace("~", "'");

		if (s.contains("@") == true)
		{
			if (s.equals("@WebUser.No") == true)
			{
				return WebUser.getNo();
			}
			if (s.equals("@WebUser.Name") == true)
			{
				return WebUser.getName();
			}
			if (s.equals("@WebUser.FK_Dept") == true)
			{
				return WebUser.getFK_Dept();
			}
			if (s.equals("@WebUser.FK_DeptName") == true)
			{
				return WebUser.getFK_DeptName();
			}
		}

		return s;
	}
	public final void setOperatorValue(Object value)  throws Exception
	 {
		this.SetValByKey(CondAttr.OperatorValue, value instanceof String ? (String)value : null);
	}
	/** 
	 操作值Str
	*/
	public final String getOperatorValueStr() throws Exception {
		String sql = this.GetValStringByKey(CondAttr.OperatorValue);
		sql = sql.replace("~", "'");
		return sql;
	}
	/** 
	 操作值int
	*/
	public final int getOperatorValueInt() throws Exception
	{
		return this.GetValIntByKey(CondAttr.OperatorValue);
	}
	/** 
	 操作值boolen
	*/
	public final boolean getOperatorValueBool() throws Exception
	{
		return this.GetValBooleanByKey(CondAttr.OperatorValue);
	}
	private long _FID = 0;
	public final long getFID() throws Exception {
		return _FID;
	}
	public final void setFID(long value)throws Exception
	{_FID = value;
	}

	/** 
	 workid
	*/
	private long _WorkID = 0;
	public final long getWorkID()  {
		return _WorkID;
	}
	public final void setWorkID(long value)
	{_WorkID = value;
	}
	/** 
	 条件消息
	*/
	public String MsgOfCond = "";
	/** 
	 上移
	 
	 param fk_node 节点ID
	*/
	public final void DoUp(int fk_node)
	{
		int condtypeInt = this.getCondType().getValue();
		this.DoOrderUp(CondAttr.FK_Node, String.valueOf(fk_node), CondAttr.CondType, String.valueOf(condtypeInt), CondAttr.Idx);
	}
	/** 
	 下移
	 
	 param fk_node 节点ID
	*/
	public final void DoDown(int fk_node)
	{
		int condtypeInt = this.getCondType().getValue();
		this.DoOrderDown(CondAttr.FK_Node, String.valueOf(fk_node), CondAttr.CondType, String.valueOf(condtypeInt), CondAttr.Idx);
	}
	/** 
	 方向条件-下移
	*/
	public final void DoDown2020Cond() throws Exception {
		if (this.getCondType() == CondType.Dir)
		{
			this.DoOrderDown(CondAttr.FK_Node, this.getFK_Node(), CondAttr.ToNodeID, this.getToNodeID(), CondAttr.CondType, CondType.Dir.getValue(), CondAttr.Idx);
		}

		if (this.getCondType() == CondType.Flow)
		{
			this.DoOrderDown(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, CondType.Flow.getValue(), CondAttr.Idx);
		}

		if (this.getCondType() == CondType.SubFlow)
		{
			this.DoOrderDown(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, CondType.SubFlow.getValue(), CondAttr.Idx);
		}

		if (this.getCondType() == CondType.Node)
		{
			this.DoOrderDown(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, CondType.Node.getValue(), CondAttr.Idx);
		}
	}
	/** 
	 方向条件-上移
	*/
	public final void DoUp2020Cond() throws Exception {
		if (this.getCondType() == CondType.Dir)
		{
			this.DoOrderUp(CondAttr.FK_Node, this.getFK_Node(), CondAttr.ToNodeID, this.getToNodeID(), CondAttr.CondType, CondType.Dir.getValue(), CondAttr.Idx);
		}

		if (this.getCondType() == CondType.Flow)
		{
			this.DoOrderUp(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, CondType.Flow.getValue(), CondAttr.Idx);
		}

		if (this.getCondType() == CondType.SubFlow)
		{
			this.DoOrderUp(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, CondType.SubFlow.getValue(), CondAttr.Idx);
		}

		if (this.getCondType() == CondType.Node)
		{
			this.DoOrderUp(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, CondType.Node.getValue(), CondAttr.Idx);
		}
	}

		///#endregion


		///#region 构造方法
	/** 
	 条件
	*/
	public Cond() {
	}
	/** 
	 条件
	 
	 param mypk
	*/
	public Cond(String mypk)throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}

		///#endregion


		///#region 公共方法
	/** 
	 这个条件能不能通过
	*/
	public boolean isPassed() throws Exception {
		Node nd = new Node(this.getFK_Node());
		if (this.en == null)
		{

				///#region 实体不存在则进行重新初始化
			GERpt en = nd.getHisFlow().getHisGERpt();
			try
			{
				en.SetValByKey("OID", this.getWorkID());
				en.Retrieve();
				en.ResetDefaultVal(null, null, 0);
				this.en = en;
			}
			catch (RuntimeException ex)
			{
					//this.Delete();
				return false;
					//throw new Exception("@在取得判断条件实体[" + nd.EnDesc + "], 出现错误:" + ex.Message + "@错误原因是定义流程的判断条件出现错误,可能是你选择的判断条件工作类是当前工作节点的下一步工作造成,取不到该实体的实例.");
			} catch (Exception e) {
				e.printStackTrace();
			}

				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.Stas)
		{

				///#region 按岗位控制
			String strs = this.getOperatorValue().toString();
			strs += this.getOperatorValueT().toString();

			String strs1 = "";

			bp.port.DeptEmpStations sts = new bp.port.DeptEmpStations();
			sts.Retrieve("FK_Emp", this.getSpecOper(), null);
			for (bp.port.DeptEmpStation st : sts.ToJavaList())
			{
				if (strs.contains("@" + st.getFK_Station() + "@"))
				{
					this.MsgOfCond = "@以岗位判断方向，条件为true：岗位集合" + strs + "，操作员(" + WebUser.getNo() + ")岗位:" + st.getFK_Station() + st.getFK_StationT();
					return true;
				}
				strs1 += st.getFK_Station() + "-" + st.getFK_StationT();
			}


			this.MsgOfCond = "@以岗位判断方向，条件为false：岗位集合" + strs + "，操作员(" + WebUser.getNo() + ")岗位:" + strs1;
			return false;

				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.Depts)
		{

				///#region 按部门控制
			String strs = this.getOperatorValue().toString();
			strs += this.getOperatorValueT().toString();

			bp.port.DeptEmps sts = new bp.port.DeptEmps();

			sts.Retrieve(bp.port.DeptEmpAttr.FK_Emp, this.getSpecOper(), null);

				//@于庆海.
			bp.port.Emp emp = new bp.port.Emp(this.getSpecOper());
			emp.setUserID(this.getSpecOper());
			if (emp.RetrieveFromDBSources() == 1)
			{
				bp.port.DeptEmp de = new bp.port.DeptEmp();
				de.setFK_Dept(emp.getFK_Dept());
				sts.AddEntity(de);
			}


			String strs1 = "";
			for (DeptEmp st : sts.ToJavaList())
			{
				if (strs.contains("@" + st.getFK_Dept() + "@"))
				{
					this.MsgOfCond = "@以岗位判断方向，条件为true：部门集合" + strs + "，操作员(" + WebUser.getNo() + ")部门:" + st.getFK_Dept();
					return true;
				}
				strs1 += st.getFK_Dept();
			}

			this.MsgOfCond = "@以部门判断方向，条件为false：部门集合" + strs + "，操作员(" + WebUser.getNo() + ")部门:" + strs1;
			return false;


				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.SQL)
		{

				///#region 按SQL 计算
				//this.MsgOfCond = "@以表单值判断方向，值 " + en.EnDesc + "." + this.AttrKey + " (" + en.GetValStringByKey(this.AttrKey) + ") 操作符:(" + this.FK_Operator + ") 判断值:(" + this.OperatorValue.ToString() + ")";
			String sql = this.getOperatorValueStr();
			sql = sql.replace("~", "'");
			sql = sql.replace("@WebUser.No", WebUser.getNo());
			sql = sql.replace("@WebUser.Name", WebUser.getName());
			sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
			sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

			sql = sql.replace("@WebUser.OrgNo", WebUser.getOrgNo());
				//获取参数值
				//System.Collections.Specialized.NameValueCollection urlParams = this.GetRequestVal; // System.Web.HttpContext.Current.Request.Form;
//			for (Object key : CommonUtils.getRequest().getParameterMap().keySet())
//			{
//					//循环使用数组
//				if (DataType.IsNullOrEmpty(key) == false && sql.contains((CharSequence) key) == true)
//				{
//					//sql = sql.replace("@" + key, this.GetRequestVal(key));
//				}
//					//sql = sql.Replace("@" + key, urlParams[key]);
//			}

			if (en.getIsOIDEntity() == true)
			{
				sql = sql.replace("@WorkID", en.GetValStrByKey("OID"));
				sql = sql.replace("@OID", en.GetValStrByKey("OID"));
			}

			if (sql.contains("@") == true)
			{
					/* 如果包含 @ */
				for (Attr attr : this.en.getEnMap().getAttrs())
				{
					sql = sql.replace("@" + attr.getKey(), en.GetValStrByKey(attr.getKey()));
				}
			}

			int result = 0;
				//如果是本地数据源
			if (DataType.IsNullOrEmpty(this.getFK_DBSrc()) == true || this.getFK_DBSrc().equals("local"))
			{
				result = DBAccess.RunSQLReturnValInt(sql, -1);
			}
			else
			{
				SFDBSrc dbSrc = new SFDBSrc(this.getFK_DBSrc());
				result = dbSrc.RunSQLReturnInt(sql, 0);
			}
			if (result <= 0)
			{
				return false;
			}

			return true;

				///#endregion 按SQL 计算

		}

		if (this.getHisDataFrom() == ConnDataFrom.SQLTemplate)
		{

				///#region 按SQLTemplate 计算
				//this.MsgOfCond = "@以表单值判断方向，值 " + en.EnDesc + "." + this.AttrKey + " (" + en.GetValStringByKey(this.AttrKey) + ") 操作符:(" + this.FK_Operator + ") 判断值:(" + this.OperatorValue.ToString() + ")";
			String fk_sqlTemplate = this.getOperatorValueStr();
			SQLTemplate sqltemplate = new SQLTemplate();
			sqltemplate.setNo(fk_sqlTemplate);
			if (sqltemplate.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("@配置的SQLTemplate编号为[" + sqltemplate + "]被删除了,判断条件丢失.");
			}

			String sql = sqltemplate.getDocs();
			sql = sql.replace("~", "'");
			sql = sql.replace("@WebUser.No", WebUser.getNo());
			sql = sql.replace("@WebUser.Name", WebUser.getName());
			sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

			if (en.getIsOIDEntity() == true)
			{
				sql = sql.replace("@WorkID", en.GetValStrByKey("OID"));
				sql = sql.replace("@OID", en.GetValStrByKey("OID"));
			}

			if (sql.contains("@") == true)
			{
					/* 如果包含 @ */
				for (Attr attr : this.en.getEnMap().getAttrs())
				{
					sql = sql.replace("@" + attr.getKey(), en.GetValStrByKey(attr.getKey()));
				}
			}

			int result = 0;
				//如果是本地数据源
			if (DataType.IsNullOrEmpty(this.getFK_DBSrc()) == true || this.getFK_DBSrc().equals("local"))
			{
				result = DBAccess.RunSQLReturnValInt(sql, -1);
			}
			else
			{
				SFDBSrc dbSrc = new SFDBSrc(this.getFK_DBSrc());
				result = dbSrc.RunSQLReturnInt(sql, 0);
			}
			if (result <= 0)
			{
				return false;
			}

			if (result >= 1)
			{
				return true;
			}

			throw new RuntimeException("@您设置的sql返回值，不符合农芯BPM的要求，必须是0或大于等于1。");

				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.Url)
		{

				///#region URL 参数计算
			String url = this.getOperatorValueStr();
			if (url.contains("?") == false)
			{
				url = url + "?1=2";
			}

			url = url.replace("@SDKFromServHost", (CharSequence) SystemConfig.getAppSettings().get("SDKFromServHost"));
			url = Glo.DealExp(url, this.en, "");


				///#region 加入必要的参数.
			if (url.contains("&FK_Flow") == false)
			{
				url += "&FK_Flow=" + this.getFK_Flow();
			}
			if (url.contains("&FK_Node") == false)
			{
				url += "&FK_Node=" + this.getFK_Node();
			}

			if (url.contains("&WorkID") == false)
			{
				url += "&WorkID=" + this.getWorkID();
			}

			if (url.contains("&FID") == false)
			{
				url += "&FID=" + this.getFID();
			}

			if (url.contains("&Token") == false)
			{
				url += "&Token=" + WebUser.getToken();
			}

			if (url.contains("&UserNo") == false)
			{
				url += "&UserNo=" + WebUser.getNo();
			}



				///#endregion 加入必要的参数.


				///#region 对url进行处理.
			if (SystemConfig.getIsBSsystem())
			{
					/*是bs系统，并且是url参数执行类型.*/
				String myurl = ContextHolderUtils.getRequest().getRequestURI(); // BP.Sys.Base.Glo.Request.RawUrl;
				if (myurl.indexOf('?') != -1)
				{
					myurl = myurl.substring(myurl.indexOf('?'));
				}

				myurl = myurl.replace("?", "&");
				String[] paras = myurl.split("[&]", -1);
				for (String s : paras)
				{
					String[] strs = s.split("[=]", -1);

						//如果已经有了这个参数.
					if (url.contains(strs[0] + "=") == true)
					{
						continue;
					}

					if (url.contains(s))
					{
						continue;
					}
					url += "&" + s;
				}
				url = url.replace("&?", "&");
			}

				//替换特殊的变量.
			url = url.replace("&?", "&");

			if (SystemConfig.getIsBSsystem() == false)
			{
					/*非bs模式下调用,比如在cs模式下调用它,它就取不到参数. */
			}


			if (url.contains("http") == false)
			{
					/*如果没有绝对路径 */
				if (SystemConfig.getIsBSsystem())
				{
						/*在cs模式下自动获取*/
					String host = bp.sys.Glo.getRequest().getRemoteHost();; //BP.Sys.Base.Glo.Request.Url.Host;
					if (url.contains("@AppPath"))
					{
						url = url.replace("@AppPath", "http://" + host + bp.sys.Glo.getRequest().getRemoteAddr()); //bp.sys.Glo.Request.ApplicationPath

					}
					else //BP.Sys.Base.Glo.Request.Url.Authority
					{
						//url = "http://" + HttpContextHelper.RequestUrlAuthority + url;
						url = "http://" + bp.sys.Glo.getRequest().getRemoteHost() + url;
					}
				}

				if (SystemConfig.getIsBSsystem() == false)
				{
						/*在cs模式下它的baseurl 从web.config中获取.*/
					String cfgBaseUrl = (String) SystemConfig.getAppSettings().get("HostURL");
					if (DataType.IsNullOrEmpty(cfgBaseUrl))
					{
						String err = "调用url失败:没有在web.config中配置BaseUrl,导致url事件不能被执行.";
						Log.DebugWriteError(err);
						throw new RuntimeException(err);
					}
					url = cfgBaseUrl + url;
				}
			}

				///#endregion 对url进行处理.


				///#region 求url的值
			try
			{
				url = url.replace("'", "");
					// url = url.Replace("//", "/");
					// url = url.Replace("//", "/");
				//System.Text.Encoding encode = System.Text.Encoding.GetEncoding("gb2312");
				//String text = DataType.ReadURLContext(url, 8000, encode, null);
				String text = DataType.ReadURLContext(url, 8000);

				if (text == null)
				{
						//throw new Exception("@流程设计的方向条件错误，执行的URL错误:" + url + ", 返回为null, 请检查设置是否正确。");
					return false;
				}

				if (DataType.IsNullOrEmpty(text) == true)
				{
						// throw new Exception("@错误，没有接收到返回值.");
					return false;
				}

				if (DataType.IsNumStr(text) == false)
				{
						//throw new Exception("@错误，不符合约定的格式，必须是数字类型。");
					return false;
				}
				try
				{
					float f = Float.parseFloat(text);
					if (f > 0)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				catch (RuntimeException ex)
				{
					return false;
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@判断url方向出现错误:" + ex.getMessage() + ",执行url错误:" + url);
			}

				///#endregion 对url进行处理.


				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.WebApi)
		{

				///#region WebApi接口
				//返回值
			String postData = "";
			String apiUrl = this.getOperatorValueStr();
			if (apiUrl.contains("@WebApiHost")) //可以替换配置文件中配置的webapi地址
			{
				apiUrl = apiUrl.replace("@WebApiHost", (CharSequence) SystemConfig.getAppSettings().get("WebApiHost"));
			}

				//如果有参数
			if (apiUrl.contains("?"))
			{
					//api接口地址
				String apiHost = apiUrl.split("[?]", -1)[0];
					//api参数
				String apiParams = apiUrl.split("[?]", -1)[1];
					//参数替换
				apiParams = Glo.DealExp(apiParams, nd.getHisWork());
					//执行POST
				//postData = HttpClientUtil.doPost(apiHost, apiParams);

				if (postData.equals("true"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
//			else
//			{ //如果没有参数，执行GET
//				postData = Glo.HttpGet(apiUrl);
//				if (postData.equals("true"))
//				{
//					return true;
//				}
//				else
//				{
//					return false;
//				}
//			}

				///#endregion WebApi接口
		}

			///#region 审核组件的立场
		if (this.getHisDataFrom() == ConnDataFrom.WorkCheck)
		{
				//获取当前节点的审核组件信息
			String tag = Dev2Interface.GetCheckTag(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(), WebUser.getNo());
			if (tag.contains("@FWCView=" + this.getOperatorValue()) == true)
			{
				return true;
			}
			return false;
		}

			///#endregion 审核组件的立场

		if (this.getHisDataFrom() == ConnDataFrom.Paras)
		{
			Hashtable ht = en.getRow();
			return Glo.CondExpPara(this.getOperatorValueStr(), ht, en.getOID());
		}

			//从节点表单里判断.
		if (this.getHisDataFrom() == ConnDataFrom.NodeForm)
		{
			if (en.getEnMap().getAttrs().contains(this.getAttrKey()) == false)
			{
				throw new RuntimeException("err@判断条件方向出现错误：实体：" + nd.getEnDesc() + " 属性" + this.getAttrKey() + "已经被删除方向条件判断失败.");
			}

			this.MsgOfCond = "@以表单值判断方向，值 " + en.getEnDesc() + "." + this.getAttrKey() + " (" + en.GetValStringByKey(this.getAttrKey()) + ") 操作符:(" + this.getFKOperator() + ") 判断值:(" + this.getOperatorValue().toString() + ")";
			return CheckIsPass(en);
		}

			//从独立表单里判断.
		if (this.getHisDataFrom() == ConnDataFrom.StandAloneFrm)
		{
			MapAttr attr = new MapAttr(this.getFKAttr());
			attr.setMyPK(this.getFKAttr());
			if (attr.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("err@到达【" + this.getToNodeID() + "】方向条件设置错误,原来做方向条件的字段:" + this.getFKAttr() + ",已经不存在了.");
			}

			GEEntity myen = new GEEntity(attr.getFK_MapData(), en.getOID());
			return CheckIsPass(myen);
		}
		return false;
	}
	private boolean CheckIsPass(Entity en) throws Exception {
		MapAttr attr = new MapAttr(this.getFKAttr());
		attr.setMyPK(this.getFKAttr());
		if(attr.RetrieveFromDBSources()==0)
			throw new RuntimeException("err@到达【" + this.getToNodeID() + "】方向条件设置错误,原来做方向条件的字段:" + this.getFKAttr() + ",已经不存在了.");
		try
		{
			switch (this.getFKOperator().trim().toLowerCase())
			{
				case "<>":
				case "!=":
				case "budingyu":
				case "budengyu": //不等于.
					if(attr.getIsNum()==true){
						if(en.GetValDoubleByKey(this.getAttrKey())!=Double.parseDouble(this.getOperatorValue().toString()))
							return true;
						else
							return false;
					}

					if (en.GetValStringByKey(this.getAttrKey()).equals(this.getOperatorValue().toString()) == false)
					{
						return true;
					}
					else
					{
						return false;
					}
				case "=": // 如果是 =
				case "dengyu":
					if(attr.getIsNum()==true){
						if(en.GetValDoubleByKey(this.getAttrKey())==Double.parseDouble(this.getOperatorValue().toString()))
							return true;
						else
							return false;
					}
					if (en.GetValStringByKey(this.getAttrKey()).equals(this.getOperatorValue().toString().replace("\"", "")) == true)
					{
						return true;
					}
					else
					{
						return false;
					}
				case ">":
				case "dayu":
					if (en.GetValDoubleByKey(this.getAttrKey()) > Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}

				//if (en.GetValDoubleByKey(this.AttrKey) > Double.Parse(this.OperatorValue.ToString()))
				//    return true;
				//else
				//    return false;
				case ">=":
				case "dayudengyu":
					if (en.GetValDoubleByKey(this.getAttrKey()) >= Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}
				case "<":
				case "xiaoyu":
					if (en.GetValDoubleByKey(this.getAttrKey()) < Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}
				//if (en.GetValDoubleByKey(this.AttrKey) < Double.Parse(this.OperatorValue.ToString()))
				//    return true;
				//else
				//    return false;
				case "<=":
				case "xiaoyudengyu":
					if (en.GetValDoubleByKey(this.getAttrKey()) <= Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}
				case "like":
				case "baohan":
					if (en.GetValStringByKey(this.getAttrKey()).indexOf(this.getOperatorValue().toString()) == -1)
					{
						return false;
					}
					else
					{
						return true;
					}
				default:
					throw new RuntimeException("@没有找到操作符号(" + this.getFKOperator().trim().toLowerCase() + ").");
			}
		}
		catch (RuntimeException ex)
		{
			Node nd23 = new Node(this.getFK_Node());
			throw new RuntimeException("@判断条件:Node=[" + this.getFK_Node() + "," + nd23.getEnDesc() + "], 出现错误。@" + ex.getMessage() + "。有可能您设置了非法的条件判断方式。");
		}
	}
	/** 
	 属性
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_Cond", "条件");

		map.AddMyPK(true);

			//用于整体流程的删除，导入，导出.
		map.AddTBString(CondAttr.RefFlowNo, null, "流程编号", true, true, 0, 5, 20);

			//@0=节点完成条件@1=流程条件@2=方向条件@3=启动子流程
		map.AddTBInt(CondAttr.CondType, 0, "条件类型", true, true);

			//@0=NodeForm表单数据,1=StandAloneFrm独立表单,2=Stas岗位数据,3=Depts,4=按sql计算.
			//5,按sql模版计算.6,按参数,7=按Url @=100条件表达式.
		map.AddTBInt(CondAttr.DataFrom, 0, "条件数据来源0表单,1岗位(对方向条件有效)", true, true);
		map.AddTBString(CondAttr.FK_Flow, null, "流程", true, true, 0, 4, 20);

			//对于启动子流程规则有效.
		map.AddTBString(CondAttr.SubFlowNo, null, "子流程编号", true, true, 0, 5, 20);

		map.AddTBInt(CondAttr.FK_Node, 0, "节点ID(对方向条件有效)", true, true);
		map.AddTBInt(CondAttr.ToNodeID, 0, "ToNodeID（对方向条件有效）", true, true);

		map.AddTBString(CondAttr.FK_Attr, null, "属性", true, true, 0, 80, 20);
		map.AddTBString(CondAttr.AttrKey, null, "属性键", true, true, 0, 60, 20);
		map.AddTBString(CondAttr.AttrName, null, "中文名称", true, true, 0, 500, 20);
		map.AddTBString(CondAttr.FK_Operator, "=", "运算符号", true, true, 0, 60, 20);
		map.AddTBString(CondAttr.OperatorValue, "", "要运算的值", true, true, 0, 4000, 20);
		map.AddTBString(CondAttr.OperatorValueT, "", "要运算的值T", true, true, 0, 4000, 20);

		map.AddTBString(CondAttr.Note, null, "备注", true, true, 0, 500, 20);
		map.AddTBInt(CondAttr.Idx, 1, "优先级", true, true);
		map.AddTBString(CondAttr.FK_DBSrc, "local", "SQL的数据来源", false, true, 0, 50, 20);
			//参数 for wangrui add 2015.10.6. 条件为station,depts模式的时候，需要指定人员。
		map.AddTBAtParas(2000);

		map.AddTBInt(CondAttr.Idx, 0, "Idx", true, true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {

		if (DataType.IsNullOrEmpty(this.getRefFlowNo()) == true)
		{
			if (this.getCondType() == CondType.Dir || this.getCondType() == CondType.Node || this.getCondType() == CondType.SubFlow)
			{
				Node nd = new Node(this.getFK_Node());
				this.setRefFlowNo(nd.getFK_Flow());
			}

			if (this.getCondType() == CondType.Flow)
			{
				this.setRefFlowNo(this.getFK_Flow());
				if (DataType.IsNullOrEmpty(this.getRefFlowNo()) == true)
				{
					throw new RuntimeException("err@流程完成条件设置错误，没有给FK_Flow赋值。");
				}
			}
			//for vue版本数据格式.增加一个主从表的标记字段.
			if (this.getCondType() == CondType.Dir)
				this.setRefPKVal(this.getFK_Flow() + '_' + this.getFK_Node() + '_' + this.getToNodeID());
		}

		return super.beforeUpdateInsertAction();
	}
}