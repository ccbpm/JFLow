package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Data.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
 条件
*/
public class Cond extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 参数属性.
	/** 
	 指定人员方式
	*/
	public final SpecOperWay getSpecOperWay()
	{
		return SpecOperWay.forValue(this.GetParaInt(CondAttr.SpecOperWay));
	}
	public final void setSpecOperWay(SpecOperWay value)
	{
		this.SetPara(CondAttr.SpecOperWay, value.getValue());
	}
	/** 
	 指定人员参数
	*/
	public final String getSpecOperPara()
	{
		return this.GetParaString(CondAttr.SpecOperPara);
	}
	public final void setSpecOperPara(String value)
	{
		this.SetPara(CondAttr.SpecOperPara, value);
	}
	/** 
	 求指定的人员.
	*/
	public final String getSpecOper()
	{
		BP.WF.Template.SpecOperWay way = this.getSpecOperWay();
		if (way == Template.SpecOperWay.CurrOper)
		{
			return WebUser.getNo();
		}


		if (way == Template.SpecOperWay.SpecNodeOper)
		{
			String sql = "SELECT FK_Emp FROM WF_GenerWorkerlist WHERE FK_Node=" + this.getSpecOperPara() + " AND WorkID=" + this.getWorkID();
			String fk_emp = DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (fk_emp == null)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的节点[" + this.getSpecOperPara() + "]，作为处理人，但是该节点上没有人员。查询SQL:" + sql);
			}
			return fk_emp;
		}

		if (way == Template.SpecOperWay.SpecSheetField)
		{
			if (this.en.Row.ContainsKey(this.getSpecOperPara().replace("@", "")) == false)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的字段[" + this.getSpecOperPara() + "]作为处理人，但是该字段不存在。");
			}

			return this.en.GetValStringByKey(this.getSpecOperPara().replace("@", ""));
		}

		if (way == Template.SpecOperWay.CurrOper)
		{
			if (this.en.Row.ContainsKey(this.getSpecOperPara().replace("@", "")) == false)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的字段[" + this.getSpecOperPara() + "]作为处理人，但是该字段不存在。");
			}

			return this.en.GetValStringByKey(this.getSpecOperPara().replace("@", ""));
		}

		if (way == Template.SpecOperWay.SpenEmpNo)
		{
			if (DataType.IsNullOrEmpty(this.getSpecOperPara()) == false)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的人员[" + this.getSpecOperPara() + "]作为处理人，但是人员参数没有设置。");
			}
			return this.getSpecOperPara();
		}

		throw new RuntimeException("@配置异常，没有判断的条件类型。");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 参数属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性.
	public GERpt en = null;
	/** 
	 数据来源
	*/
	public final ConnDataFrom getHisDataFrom()
	{
		return ConnDataFrom.forValue(this.GetValIntByKey(CondAttr.DataFrom));
	}
	public final void setHisDataFrom(ConnDataFrom value)
	{
		this.SetValByKey(CondAttr.DataFrom, value.getValue());
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(CondAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(CondAttr.FK_Flow, value);
	}
	/** 
	 备注
	*/
	public final String getNote()
	{
		return this.GetValStringByKey(CondAttr.Note);
	}
	public final void setNote(String value)
	{
		this.SetValByKey(CondAttr.Note, value);
	}
	/** 
	 条件类型(表单条件，岗位条件，部门条件，开发者参数)
	*/
	public final CondType getHisCondType()
	{
		return CondType.forValue(this.GetValIntByKey(CondAttr.CondType));
	}
	public final void setHisCondType(CondType value)
	{
		this.SetValByKey(CondAttr.CondType, value.getValue());
	}
	/** 
	 要运算的节点
	*/
	public final Node getHisNode()
	{
		return new Node(this.getNodeID());
	}
	/** 
	 优先级
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(CondAttr.PRI);
	}
	public final void setPRI(int value)
	{
		this.SetValByKey(CondAttr.PRI, value);
	}
	/** 
	 MyPOID
	*/
	public final int getMyPOID()
	{
		return this.GetValIntByKey(CondAttr.MyPOID);
	}
	public final void setMyPOID(int value)
	{
		this.SetValByKey(CondAttr.MyPOID, value);
	}
	/** 
	 节点ID
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(CondAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(CondAttr.NodeID, value);
	}
	/** 
	 节点ID
	*/
	public final int getFK_Node()
	{
		int i = this.GetValIntByKey(CondAttr.FK_Node);
		if (i == 0)
		{
			return this.getNodeID();
		}
		return i;
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(CondAttr.FK_Node, value);
	}
	/** 
	 节点名称
	*/
	public final String getFK_NodeT()
	{
		Node nd = new Node(this.getFK_Node());
		return nd.getName();
	}
	/** 
	 对方向条件有效
	*/
	public final int getToNodeID()
	{
		return this.GetValIntByKey(CondAttr.ToNodeID);
	}
	public final void setToNodeID(int value)
	{
		this.SetValByKey(CondAttr.ToNodeID, value);
	}
	/** 
	 关系类型
	*/
	public final CondOrAnd getCondOrAnd()
	{
		return CondOrAnd.forValue(this.GetValIntByKey(CondAttr.CondOrAnd));
	}
	public final void setCondOrAnd(CondOrAnd value)
	{
		this.SetValByKey(CondAttr.CondOrAnd, value.getValue());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 在更新与插入之前要做得操作。
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.RunSQL("UPDATE WF_Node SET IsCCFlow=0");
		// this.RunSQL("UPDATE WF_Node SET IsCCNode=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond WHERE CondType=" + (int)CondType.Node + ")");
		this.RunSQL("UPDATE WF_Node SET IsCCFlow=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond WHERE CondType=" + CondType.Flow.getValue() + ")");

		this.setMyPOID(BP.DA.DBAccess.GenerOID());
		return super.beforeUpdateInsertAction();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 实现基本的方方法
	/** 
	 属性
	*/
	public final String getFK_Attr()
	{
		return this.GetValStringByKey(CondAttr.FK_Attr);
	}
	public final void setFK_Attr(String value)
	{
		if (value == null)
		{
			throw new RuntimeException("FK_Attr不能设置为null");
		}

		value = value.trim();

		this.SetValByKey(CondAttr.FK_Attr, value);

		BP.Sys.MapAttr attr = new BP.Sys.MapAttr(value);

		if (attr.LGType == FieldTypeS.Enum)
		{
				/*是一个枚举类型的*/
			SysEnum se = new SysEnum(attr.UIBindKey, this.getOperatorValueInt());
			this.setOperatorValueT(se.Lab);
		}

		this.SetValByKey(CondAttr.AttrKey, attr.KeyOfEn);
		this.SetValByKey(CondAttr.AttrName, attr.Name);

	}
	/** 
	 要运算的实体属性
	*/
	public final String getAttrKey()
	{
		return this.GetValStringByKey(CondAttr.AttrKey);
	}
	/** 
	 属性名称
	*/
	public final String getAttrName()
	{
		return this.GetValStringByKey(CondAttr.AttrName);
	}
	/** 
	 操作的值
	*/
	public final String getOperatorValueT()
	{
		return this.GetValStringByKey(CondAttr.OperatorValueT);
	}
	public final void setOperatorValueT(String value)
	{
		this.SetValByKey(CondAttr.OperatorValueT, value);
	}
	/** 
	 运算符号
	*/
	public final String getFK_Operator()
	{
		String s = this.GetValStringByKey(CondAttr.FK_Operator);
		if (s == null || s.equals(""))
		{
			return "=";
		}
		return s;
	}
	public final void setFK_Operator(String value)
	{
		String val = "";

		switch (value)
		{
			case "dengyu":
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
	public final Object getOperatorValue()
	{
		String s = this.GetValStringByKey(CondAttr.OperatorValue);
		s = s.replace("~", "'");
		return s;
	}
	public final void setOperatorValue(Object value)
	{
		this.SetValByKey(CondAttr.OperatorValue, value instanceof String ? (String)value : null);
	}
	/** 
	 操作值Str
	*/
	public final String getOperatorValueStr()
	{
		String sql = this.GetValStringByKey(CondAttr.OperatorValue);
		sql = sql.replace("~", "'");
		return sql;
	}
	/** 
	 操作值int
	*/
	public final int getOperatorValueInt()
	{
		return this.GetValIntByKey(CondAttr.OperatorValue);
	}
	/** 
	 操作值boolen
	*/
	public final boolean getOperatorValueBool()
	{
		return this.GetValBooleanByKey(CondAttr.OperatorValue);
	}
	private long _FID = 0;
	public final long getFID()
	{
		return _FID;
	}
	public final void setFID(long value)
	{
		_FID = value;
	}
	/** 
	 workid
	*/
	private long _WorkID = 0;
	public final long getWorkID()
	{
		return _WorkID;
	}
	public final void setWorkID(long value)
	{
		_WorkID = value;
	}
	/** 
	 条件消息
	*/
	public String MsgOfCond = "";
	/** 
	 上移
	 
	 @param fk_node 节点ID
	*/
	public final void DoUp(int fk_node)
	{
		int condtypeInt = this.getHisCondType().getValue();
		this.DoOrderUp(CondAttr.FK_Node, String.valueOf(fk_node), CondAttr.CondType, String.valueOf(condtypeInt), CondAttr.PRI);
	}
	/** 
	 下移
	 
	 @param fk_node 节点ID
	*/
	public final void DoDown(int fk_node)
	{
		int condtypeInt = this.getHisCondType().getValue();
		this.DoOrderDown(CondAttr.FK_Node, String.valueOf(fk_node), CondAttr.CondType, String.valueOf(condtypeInt), CondAttr.PRI);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 条件
	*/
	public Cond()
	{
	}
	/** 
	 条件
	 
	 @param mypk
	*/
	public Cond(String mypk)
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公共方法
	/** 
	 这个条件能不能通过
	*/
	public boolean getIsPassed()
	{
		Node nd = new Node(this.getFK_Node());
		if (this.en == null)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 实体不存在则进行重新初始化
			GERpt en = nd.getHisFlow().getHisGERpt();
			try
			{
				en.SetValByKey("OID", this.getWorkID());
				en.Retrieve();
				en.ResetDefaultVal();
				this.en = en;
			}
			catch (RuntimeException ex)
			{
					//this.Delete();
				return false;
					//throw new Exception("@在取得判断条件实体[" + nd.EnDesc + "], 出现错误:" + ex.Message + "@错误原因是定义流程的判断条件出现错误,可能是你选择的判断条件工作类是当前工作节点的下一步工作造成,取不到该实体的实例.");
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.Stas)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 按岗位控制
			String strs = this.getOperatorValue().toString();
			strs += this.getOperatorValueT().toString();

			String strs1 = "";
			if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneMore)
			{
				BP.GPM.DeptEmpStations sts = new BP.GPM.DeptEmpStations();
				sts.Retrieve("FK_Emp", this.getSpecOper());
				for (BP.GPM.DeptEmpStation st : sts)
				{
					if (strs.contains("@" + st.FK_Station + "@"))
					{
						this.MsgOfCond = "@以岗位判断方向，条件为true：岗位集合" + strs + "，操作员(" + WebUser.getNo() + ")岗位:" + st.FK_Station + st.FK_StationT;
						return true;
					}
					strs1 += st.FK_Station + "-" + st.FK_StationT;
				}
			}

			this.MsgOfCond = "@以岗位判断方向，条件为false：岗位集合" + strs + "，操作员(" + WebUser.getNo() + ")岗位:" + strs1;
			return false;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.Depts)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 按部门控制
			String strs = this.getOperatorValue().toString();
			strs += this.getOperatorValueT().toString();

			BP.GPM.DeptEmps sts = new BP.GPM.DeptEmps();
			if (SystemConfig.OSModel == OSModel.OneMore)
			{
				sts.Retrieve(BP.GPM.DeptEmpAttr.FK_Emp, this.getSpecOper());

					//@于庆海.
				BP.Port.Emp emp = new BP.Port.Emp(this.getSpecOper());
				emp.No = this.getSpecOper();
				if (emp.RetrieveFromDBSources() == 1)
				{
					BP.GPM.DeptEmp de = new GPM.DeptEmp();
					de.FK_Dept = emp.FK_Dept;
					sts.AddEntity(de);
				}
			}
			else
			{
				BP.Port.Emp emp = new BP.Port.Emp(this.getSpecOper());

				BP.GPM.DeptEmp myen = new BP.GPM.DeptEmp();
				myen.FK_Dept = emp.FK_Dept;
				myen.FK_Emp = emp.No;
				sts.AddEntity(myen);
			}

			String strs1 = "";
			for (BP.GPM.DeptEmp st : sts)
			{
				if (strs.contains("@" + st.FK_Dept + "@"))
				{
					this.MsgOfCond = "@以岗位判断方向，条件为true：部门集合" + strs + "，操作员(" + WebUser.getNo() + ")部门:" + st.FK_Dept;
					return true;
				}
				strs1 += st.FK_Dept;
			}

			this.MsgOfCond = "@以部门判断方向，条件为false：部门集合" + strs + "，操作员(" + WebUser.getNo() + ")部门:" + strs1;
			return false;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.SQL)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 按SQL 计算
				//this.MsgOfCond = "@以表单值判断方向，值 " + en.EnDesc + "." + this.AttrKey + " (" + en.GetValStringByKey(this.AttrKey) + ") 操作符:(" + this.FK_Operator + ") 判断值:(" + this.OperatorValue.ToString() + ")";
			String sql = this.getOperatorValueStr();
			sql = sql.replace("~", "'");
			sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
			sql = sql.replace("@WebUser.getName()", WebUser.getName());
			sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

				//获取参数值
				//System.Collections.Specialized.NameValueCollection urlParams = HttpContextHelper.RequestParams; // System.Web.HttpContext.Current.Request.Form;
			for (String key : HttpContextHelper.RequestParamKeys)
			{
					//循环使用数组
				if (DataType.IsNullOrEmpty(key) == false && sql.contains(key) == true)
				{
					sql = sql.replace("@" + key, HttpContextHelper.RequestParams(key));
				}
					//sql = sql.Replace("@" + key, urlParams[key]);
			}

			if (en.IsOIDEntity == true)
			{
				sql = sql.replace("@WorkID", en.GetValStrByKey("OID"));
				sql = sql.replace("@OID", en.GetValStrByKey("OID"));
			}

			if (sql.contains("@") == true)
			{
					/* 如果包含 @ */
				for (Attr attr : this.en.getEnMap().getAttrs())
				{
					sql = sql.replace("@" + attr.Key, en.GetValStrByKey(attr.getKey()));
				}
			}

			int result = DBAccess.RunSQLReturnValInt(sql, -1);
			if (result <= 0)
			{
				return false;
			}

			return true;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 按SQL 计算

		}

		if (this.getHisDataFrom() == ConnDataFrom.SQLTemplate)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 按SQLTemplate 计算
				//this.MsgOfCond = "@以表单值判断方向，值 " + en.EnDesc + "." + this.AttrKey + " (" + en.GetValStringByKey(this.AttrKey) + ") 操作符:(" + this.FK_Operator + ") 判断值:(" + this.OperatorValue.ToString() + ")";
			String fk_sqlTemplate = this.getOperatorValueStr();
			SQLTemplate sqltemplate = new SQLTemplate();
			sqltemplate.No = fk_sqlTemplate;
			if (sqltemplate.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("@配置的SQLTemplate编号为[" + sqltemplate + "]被删除了,判断条件丢失.");
			}

			String sql = sqltemplate.getDocs();
			sql = sql.replace("~", "'");
			sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
			sql = sql.replace("@WebUser.getName()", WebUser.getName());
			sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

			if (en.IsOIDEntity == true)
			{
				sql = sql.replace("@WorkID", en.GetValStrByKey("OID"));
				sql = sql.replace("@OID", en.GetValStrByKey("OID"));
			}

			if (sql.contains("@") == true)
			{
					/* 如果包含 @ */
				for (Attr attr : this.en.getEnMap().getAttrs())
				{
					sql = sql.replace("@" + attr.Key, en.GetValStrByKey(attr.getKey()));
				}
			}

			int result = DBAccess.RunSQLReturnValInt(sql, -1);
			if (result <= 0)
			{
				return false;
			}

			if (result >= 1)
			{
				return true;
			}

			throw new RuntimeException("@您设置的sql返回值，不符合ccflow的要求，必须是0或大于等于1。");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.Url)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region URL 参数计算
			String url = this.getOperatorValueStr();
			if (url.contains("?") == false)
			{
				url = url + "?1=2";
			}

			url = url.replace("@SDKFromServHost", BP.Sys.SystemConfig.AppSettings["SDKFromServHost"]);
			url = BP.WF.Glo.DealExp(url, this.en, "");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

			if (url.contains("&SID") == false)
			{
				url += "&SID=" + WebUser.SID;
			}

			if (url.contains("&UserNo") == false)
			{
				url += "&UserNo=" + WebUser.getNo();
			}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 加入必要的参数.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 对url进行处理.
			if (SystemConfig.IsBSsystem)
			{
					/*是bs系统，并且是url参数执行类型.*/
				String myurl = HttpContextHelper.RequestRawUrl; // BP.Sys.Glo.Request.RawUrl;
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

			if (SystemConfig.IsBSsystem == false)
			{
					/*非bs模式下调用,比如在cs模式下调用它,它就取不到参数. */
			}


			if (url.contains("http") == false)
			{
					/*如果没有绝对路径 */
				if (SystemConfig.IsBSsystem)
				{
						/*在cs模式下自动获取*/
					String host = HttpContextHelper.RequestUrlHost; //BP.Sys.Glo.Request.Url.Host;
					if (url.contains("@AppPath"))
					{
						url = url.replace("@AppPath", "http://" + host + HttpContextHelper.RequestApplicationPath); //BP.Sys.Glo.Request.ApplicationPath
					}
					else //BP.Sys.Glo.Request.Url.Authority
					{
						url = "http://" + HttpContextHelper.RequestUrlAuthority + url;
					}
				}

				if (SystemConfig.IsBSsystem == false)
				{
						/*在cs模式下它的baseurl 从web.config中获取.*/
					String cfgBaseUrl = SystemConfig.AppSettings["HostURL"];
					if (DataType.IsNullOrEmpty(cfgBaseUrl))
					{
						String err = "调用url失败:没有在web.config中配置BaseUrl,导致url事件不能被执行.";
						Log.DefaultLogWriteLineError(err);
						throw new RuntimeException(err);
					}
					url = cfgBaseUrl + url;
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 对url进行处理.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 求url的值
			try
			{
				url = url.replace("'", "");
					// url = url.Replace("//", "/");
					// url = url.Replace("//", "/");
				System.Text.Encoding encode = System.Text.Encoding.GetEncoding("gb2312");
				String text = DataType.ReadURLContext(url, 8000, encode);
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 对url进行处理.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}

		if (this.getHisDataFrom() == ConnDataFrom.Paras)
		{
			Hashtable ht = en.Row;
			return BP.WF.Glo.CondExpPara(this.getOperatorValueStr(), ht, en.getOID());
		}

			//从节点表单里判断.
		if (this.getHisDataFrom() == ConnDataFrom.NodeForm)
		{
			if (en.getEnMap().getAttrs().Contains(this.getAttrKey()) == false)
			{
				throw new RuntimeException("err@判断条件方向出现错误：实体：" + nd.EnDesc + " 属性" + this.getAttrKey() + "已经被删除方向条件判断失败.");
			}

			this.MsgOfCond = "@以表单值判断方向，值 " + en.EnDesc + "." + this.getAttrKey() + " (" + en.GetValStringByKey(this.getAttrKey()) + ") 操作符:(" + this.getFK_Operator() + ") 判断值:(" + this.getOperatorValue().toString() + ")";
			return CheckIsPass(en);
		}

			//从独立表单里判断.
		if (this.getHisDataFrom() == ConnDataFrom.StandAloneFrm)
		{
			MapAttr attr = new MapAttr(this.getFK_Attr());
			GEEntity myen = new GEEntity(attr.FK_MapData, en.getOID());
			return CheckIsPass(myen);
		}
		return false;
	}
	private boolean CheckIsPass(Entity en)
	{

		try
		{
			switch (this.getFK_Operator().trim().toLowerCase())
			{
				case "<>":
				case "!=":
				case "budingyu":
				case "budengyu": //不等于.
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
					if (en.GetValStringByKey(this.getAttrKey()).equals(this.getOperatorValue().toString()) == true)
					{
						return true;
					}
					else
					{
						return false;
					}
				case ">":
				case "dayu":
					if (en.GetValStringByKey(this.getAttrKey()).CompareTo(this.getOperatorValue().toString()) == 1)
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
					if (en.GetValStringByKey(this.getAttrKey()).CompareTo(this.getOperatorValue().toString()) == -1)
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
					throw new RuntimeException("@没有找到操作符号(" + this.getFK_Operator().trim().toLowerCase() + ").");
			}
		}
		catch (RuntimeException ex)
		{
			Node nd23 = new Node(this.getNodeID());
			throw new RuntimeException("@判断条件:Node=[" + this.getNodeID() + "," + nd23.EnDesc + "], 出现错误。@" + ex.getMessage() + "。有可能您设置了非法的条件判断方式。");
		}
	}
	/** 
	 属性
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_Cond", "条件");
		map.IndexField = CondAttr.NodeID;


		map.AddMyPK();

		map.AddTBInt(CondAttr.CondType, 0, "条件类型", true, true);
			//map.AddDDLSysEnum(CondAttr.CondType, 0, "条件类型", true, false, CondAttr.CondType,"@0=节点完成条件@1=流程完成条件@2=方向条件");
		map.AddTBInt(CondAttr.DataFrom, 0, "条件数据来源0表单,1岗位(对方向条件有效)", true, true);
		map.AddTBString(CondAttr.FK_Flow, null, "流程", true, true, 0, 60, 20);
		map.AddTBInt(CondAttr.NodeID, 0, "发生的事件MainNode", true, true);
		map.AddTBInt(CondAttr.FK_Node, 0, "节点ID", true, true);

		map.AddTBString(CondAttr.FK_Attr, null, "属性", true, true, 0, 80, 20);
		map.AddTBString(CondAttr.AttrKey, null, "属性键", true, true, 0, 60, 20);
		map.AddTBString(CondAttr.AttrName, null, "中文名称", true, true, 0, 500, 20);
		map.AddTBString(CondAttr.FK_Operator, "=", "运算符号", true, true, 0, 60, 20);
		map.AddTBString(CondAttr.OperatorValue, "", "要运算的值", true, true, 0, 4000, 20);
		map.AddTBString(CondAttr.OperatorValueT, "", "要运算的值T", true, true, 0, 4000, 20);
		map.AddTBInt(CondAttr.ToNodeID, 0, "ToNodeID（对方向条件有效）", true, true);
		map.AddDDLSysEnum(CondAttr.ConnJudgeWay, 0, "条件关系", true, false, CondAttr.ConnJudgeWay, "@0=or@1=and");

		map.AddTBInt(CondAttr.MyPOID, 0, "MyPOID", true, true);
		map.AddTBInt(CondAttr.PRI, 0, "计算优先级", true, true);
		map.AddTBInt(CondAttr.CondOrAnd, 0, "方向条件类型", true, true);

		map.AddTBString(CondAttr.Note, null, "备注", true, true, 0, 500, 20);

			//参数 for wangrui add 2015.10.6. 条件为station,depts模式的时候，需要指定人员。
		map.AddTBAtParas(2000);

			//      map.AddDDLSysEnum(NodeAttr.CondOrAnd, 0, "方向条件类型",
			//true, true, NodeAttr.CondOrAnd, "@0=And(条件集合中所有的都成立)@1=Or(条件集合中只有一个成立)");

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}