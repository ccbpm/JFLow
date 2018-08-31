package BP.WF.Template;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.Attr;
import BP.En.Entity;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.Sys.GEEntity;
import BP.Sys.MapAttr;
import BP.Sys.OSModel;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.Data.GERpt;

/** 
 条件
 
*/
public class Cond extends EntityMyPK
{

		
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
	 * @throws Exception 
	 
	*/
	public final String getSpecOper() throws Exception
	{
		SpecOperWay way = this.getSpecOperWay();
		if (way == SpecOperWay.CurrOper)
		{
			return BP.Web.WebUser.getNo();
		}


		if (way == SpecOperWay.SpecNodeOper)
		{
			String sql = "SELECT FK_Emp FROM WF_GenerWorkerlist WHERE FK_Node=" + this.getSpecOperPara() + " AND WorkID=" + this.getWorkID();
			String fk_emp = DBAccess.RunSQLReturnStringIsNull(sql, null);
			if (fk_emp == null)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的节点["+this.getSpecOperPara()+"]，作为处理人，但是该节点上没有人员。查询SQL:"+sql);
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
			if (StringHelper.isNullOrEmpty(this.getSpecOperPara())==false)
			{
				throw new RuntimeException("@您在配置方向条件时错误，求指定的人员的时候，按照指定的人员[" + this.getSpecOperPara() + "]作为处理人，但是人员参数没有设置。");
			}
			return this.getSpecOperPara();
		}

		throw new RuntimeException("@配置异常，没有判断的类型。");
	}

		///#endregion 参数属性.

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
	 * @throws Exception 
	 
	*/
	public final Node getHisNode() throws Exception
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
	/** 备注
	 
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
	 节点名称
	 * @throws Exception 
	 
	*/
	public final String getFK_NodeT() throws Exception
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

		///#endregion

	/** 
	 在更新与插入之前要做得操作。
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.RunSQL("UPDATE WF_Node SET IsCCFlow=0");
		// this.RunSQL("UPDATE WF_Node SET IsCCNode=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond WHERE CondType=" + (int)CondType.Node + ")");
		this.RunSQL("UPDATE WF_Node SET IsCCFlow=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond WHERE CondType=" + CondType.Flow.getValue() + ")");

		this.setMyPOID(BP.DA.DBAccess.GenerOID());
		return super.beforeUpdateInsertAction();
	}


		///#region 实现基本的方方法
	/** 
	 属性
	 
	*/
	public final String getFK_Attr()
	{
		return this.GetValStringByKey(CondAttr.FK_Attr);
	}
	public final void setFK_Attr(String value) throws Exception
	{
		if (value == null)
		{
			throw new RuntimeException("FK_Attr不能设置为null");
		}

		value = value.trim();

		this.SetValByKey(CondAttr.FK_Attr, value);

		BP.Sys.MapAttr attr = new BP.Sys.MapAttr(value);
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
			return "=";
		
		return s;
	}
	public final void setFK_Operator(String value)
	{
		this.SetValByKey(CondAttr.FK_Operator, value);
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
		this.SetValByKey(CondAttr.OperatorValue, (String)((value instanceof String) ? value : null));
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
		this.DoOrderUp(CondAttr.FK_Node, (new Integer(fk_node)).toString(), CondAttr.CondType, (new Integer(condtypeInt)).toString(), CondAttr.PRI);
	}
	/** 
	 下移
	 
	 @param fk_node 节点ID
	*/
	public final void DoDown(int fk_node)
	{
		int condtypeInt = this.getHisCondType().getValue();
		this.DoOrderDown(CondAttr.FK_Node, (new Integer(fk_node)).toString(), CondAttr.CondType, (new Integer(condtypeInt)).toString(), CondAttr.PRI);
	}

		///#endregion


		
	/** 
	 条件
	 
	*/
	public Cond()
	{
	}
	/** 
	 条件
	 
	 @param mypk
	 * @throws Exception 
	*/
	public Cond(String mypk) throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}

		///#endregion


		///#region 公共方法
	/** 
	 这个条件能不能通过
	 * @throws Exception 
	 
	*/
	public boolean getIsPassed() throws Exception
	{
		Node nd = new Node(this.getFK_Node());
		if (this.en == null)
		{
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
		}

		if (this.getHisDataFrom() == ConnDataFrom.Stas)
		{
			String strs = this.getOperatorValue().toString();
			String strs1 = "";
			 

			if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneMore)
			{
				BP.GPM.DeptEmpStations sts = new BP.GPM.DeptEmpStations();
				sts.Retrieve("FK_Emp", this.getSpecOper());
				for (BP.GPM.DeptEmpStation st : sts.ToJavaList())
				{
					if (strs.contains("@" + st.getFK_Station() + "@"))
					{
						this.MsgOfCond = "@以岗位判断方向，条件为true：岗位集合" + strs + "，操作员(" + BP.Web.WebUser.getNo() + ")岗位:" + st.getFK_Station() + st.getFK_StationT();
						return true;
					}
					strs1 += st.getFK_Station() + "-" + st.getFK_StationT();
				}
			}

			this.MsgOfCond = "@以岗位判断方向，条件为false：岗位集合" + strs + "，操作员(" + BP.Web.WebUser.getNo() + ")岗位:" + strs1;
			return false;
		}

		if (this.getHisDataFrom() == ConnDataFrom.Depts)
		{
			String strs = this.getOperatorValue().toString();

			BP.GPM.DeptEmps sts = new BP.GPM.DeptEmps();
			if (SystemConfig.getOSModel() == OSModel.OneMore)
			{
				sts.Retrieve(BP.GPM.DeptEmpAttr.FK_Emp, this.getSpecOper());
				
			     //@于庆海.
                BP.Port.Emp emp = new BP.Port.Emp(this.getSpecOper());
                emp.setNo(this.getSpecOper());
                if (emp.RetrieveFromDBSources() == 1)
                {
                    BP.GPM.DeptEmp de = new BP.GPM.DeptEmp();
                    de.setFK_Dept( emp.getFK_Dept());
                    sts.AddEntity(de);
                }
                
			}
			else
			{
				BP.Port.Emp emp = new BP.Port.Emp(this.getSpecOper());

				BP.GPM.DeptEmp myen = new BP.GPM.DeptEmp();
				myen.setFK_Dept(emp.getFK_Dept());
				myen.setFK_Emp(emp.getNo());
				sts.AddEntity(myen);
			}

			String strs1 = "";
			for (BP.GPM.DeptEmp st : sts.ToJavaList())
			{
				if (strs.contains("@" + st.getFK_Dept() + "@"))
				{
					this.MsgOfCond = "@以岗位判断方向，条件为true：部门集合" + strs + "，操作员(" + BP.Web.WebUser.getNo() + ")部门:" + st.getFK_Dept();
					return true;
				}
				strs1 += st.getFK_Dept();
			}

			this.MsgOfCond = "@以部门判断方向，条件为false：部门集合" + strs + "，操作员(" + BP.Web.WebUser.getNo() + ")部门:" + strs1;
			return false;
		}

		if (this.getHisDataFrom() == ConnDataFrom.SQL)
		{
				//this.MsgOfCond = "@以表单值判断方向，值 " + en.EnDesc + "." + this.AttrKey + " (" + en.GetValStringByKey(this.AttrKey) + ") 操作符:(" + this.FK_Operator + ") 判断值:(" + this.OperatorValue.ToString() + ")";
			String sql = this.getOperatorValueStr();
			sql = sql.replace("~", "'");
			sql = sql.replace("WebUser.No", BP.Web.WebUser.getNo());
			sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
			sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
			if (sql.contains("@") == true)
			{
					// 如果包含 @ 
				for (Attr attr : this.en.getEnMap().getAttrs())
				{
					sql = sql.replace("@" + attr.getKey(), en.GetValStrByKey(attr.getKey()));
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
		}

		if (this.getHisDataFrom() == ConnDataFrom.Url)
		{
			String url = this.getOperatorValueStr();
			if (url.contains("?") == false)
			{
				url = url + "?1=2";
			}

			url = url.replace("@SDKFromServHost", (String)BP.Sys.SystemConfig.getAppSettings().get("SDKFromServHost"));
			url = BP.WF.Glo.DealExp(url, this.en, "");


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
				url += "&SID=" + BP.Web.WebUser.getSID();
			}

			if (url.contains("&UserNo") == false)
			{
				url += "&UserNo=" + BP.Web.WebUser.getNo();
			}



				///#endregion 加入必要的参数.


				///#region 对url进行处理.
			if (SystemConfig.getIsBSsystem() && BP.Sys.Glo.getRequest()!=null)
			{
					//是bs系统，并且是url参数执行类型.
				String myurl = BP.Sys.Glo.getRequest().getRequestURI();
				if (myurl.indexOf('?') != -1)
				{
					myurl = myurl.substring(myurl.indexOf('?'));
				}

				myurl = myurl.replace("?","&");
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
					//非bs模式下调用,比如在cs模式下调用它,它就取不到参数. 
			}


			if (url.contains("http") == false)
			{
					//如果没有绝对路径 
				if (SystemConfig.getIsBSsystem()  && BP.Sys.Glo.getRequest()!=null)
				{
						//在cs模式下自动获取
					String host = BP.Sys.Glo.getRequest().getRemoteHost();
					if (url.contains("@AppPath"))
					{
						url = url.replace("@AppPath", "http://" + host + BP.Sys.Glo.getRequest().getRemoteAddr());
					}
					else
					{
						url = "http://" +  BP.Sys.Glo.getRequest().getRemoteHost()+ url;
					}
				}

				if (SystemConfig.getIsBSsystem() == false)
				{
						//在cs模式下它的baseurl 从web.config中获取.
					String cfgBaseUrl = (String) SystemConfig.getAppSettings().get("BaseUrl");
					if (StringHelper.isNullOrEmpty(cfgBaseUrl))
					{
						String err = "调用url失败:没有在web.config中配置BaseUrl,导致url事件不能被执行.";
						Log.DefaultLogWriteLineError(err);
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
					// url = url.replace("//", "/");
					// url = url.replace("//", "/");
				//System.Text.Encoding encode = System.Text.Encoding.GetEncoding("gb2312");
     			//String text = DataType.ReadURLContext(url, 8000, encode);
				String text = DataType.ReadURLContext(url, 8000);
				if (text == null)
						//throw new Exception("@流程设计的方向条件错误，执行的URL错误:" + url + ", 返回为null, 请检查设置是否正确。");
				{
					return false;
				}

				if (StringHelper.isNullOrEmpty(text) == true)
						// throw new Exception("@错误，没有接收到返回值.");
				{
					return false;
				}

				if (DataType.IsNumStr(text) == false)
						//throw new Exception("@错误，不符合约定的格式，必须是数字类型。");
				{
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
		}

		if (this.getHisDataFrom() == ConnDataFrom.Paras)
		{
				//this.MsgOfCond = "@以表单值判断方向，值 " + en.EnDesc + "." + this.AttrKey + " (" + en.GetValStringByKey(this.AttrKey) + ") 操作符:(" + this.FK_Operator + ") 判断值:(" + this.OperatorValue.ToString() + ")";
			String exp = this.getOperatorValueStr();
			String[] strs = exp.trim().split("[ ]", -1);

			String key = strs[0].trim();
			String oper = strs[1].trim();
			String val = strs[2].trim();
			val = val.replace("'", "");
			val = val.replace("%", "");
			val = val.replace("~", "");

			BP.En.Row row = this.en.getRow();

			String valPara = null;
			if (row.containsKey(key) == false)
			{
				try
				{
						//如果不包含指定的关键的key, 就到公共变量里去找. 
					if (BP.WF.Glo.getSendHTOfTemp().containsKey(key) == false)
					{
						throw new RuntimeException("@判断条件时错误,请确认参数是否拼写错误,没有找到对应的表达式:" + exp + " Key=(" + key + ") oper=(" + oper + ")Val=(" + val + ")");
					}
					valPara = BP.WF.Glo.getSendHTOfTemp().get(key).toString().trim();
				}
				catch (java.lang.Exception e)
				{
						//有可能是常量. 
					valPara = key;
				}
			}
			else
			{
				valPara = row.get(key).toString().trim();
			}


				///#region 开始执行判断.
			if (oper.equals("=") || oper.equals("dengyu"))
			{
				if (val.equals(valPara))
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			if (oper.toUpperCase().equals("LIKE"))
			{
				if (valPara.contains(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			if (oper.equals(">") || oper.equals("dayu"))
			{
				if (Float.parseFloat(valPara) > Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (oper.equals(">=") || oper.equals("dayudengyu"))
			{
				if (Float.parseFloat(valPara) >= Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (oper.equals("<") || oper.contains("xiaoyu"))
			{
				if (Float.parseFloat(valPara) < Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (oper.equals("<=") || oper.contains("xiaoyudengyu"))
			{
				if (Float.parseFloat(valPara) <= Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			if (oper.equals("!=") || oper.contains("budengyu"))
			{
				if (Float.parseFloat(valPara) != Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			throw new RuntimeException("@参数格式错误:" + exp + " Key=" + key + " oper=" + oper + " Val=" + val);

				///#endregion 开始执行判断.
				// throw new Exception("@判断条件时错误,没有找到对应的表达式:" + exp + " Key=(" + key + ") oper=(" + oper + ")Val=(" + val+")");
		} //参数.
		

        //从节点表单里判断.
        if (this.getHisDataFrom() == ConnDataFrom.NodeForm)
        {
            if (en.getEnMap().getAttrs().Contains(this.getAttrKey()) == false)            	
                throw new RuntimeException("err@判断条件方向出现错误：实体：" + en.getEnDesc() + " 属性" + this.getAttrKey() + "已经被删除方向条件判断失败.");

            this.MsgOfCond = "@以表单值判断方向，值 " + en.getEnDesc() + "." + this.getAttrKey() + " (" + en.GetValStringByKey(this.getAttrKey()) + ") 操作符:(" + this.getFK_Operator() + ") 判断值:(" + this.getOperatorValue().toString() + ")";
            return CheckIsPass(en); 
        }

        //从独立表单里判断.
        if (this.getHisDataFrom() == ConnDataFrom.StandAloneFrm)
        {
            MapAttr attr = new MapAttr(this.getFK_Attr());
            GEEntity myen = new GEEntity(attr.getFK_MapData(), en.getOID());
            return CheckIsPass(myen);
        }
        
        return false;		
	}
	
	public boolean CheckIsPass(Entity en) throws Exception
	{

		try
		{
			  
			String oper=this.getFK_Operator().trim().toLowerCase();

			if (oper.equals("<>") || oper.equals("budengyu") ||oper.equals("!="))
			{
					if (!this.getOperatorValue().toString().equals(en.GetValStringByKey(this.getAttrKey())))
					{
						return true;
					}
					else
					{
						return false;
					}
			}
			else if (oper.equals("=") || oper.equals("dengyu")) // 如果是 =
			{
					if (this.getOperatorValue().toString().equals(en.GetValStringByKey(this.getAttrKey())))
					{
						return true;
					}
					else
					{
						return false;
					}
			}
			else if (oper.equals(">") || oper.equals("dayu"))
			{
					if (en.GetValDoubleByKey(this.getAttrKey()) > Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}
			}
			else if (oper.equals(">=") || oper.equals("dayudengyu"))
			{
					if (en.GetValDoubleByKey(this.getAttrKey()) >= Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}
			}
			else if (oper.equals("<") || oper.equals("xiaoyu"))
			{
					if (en.GetValDoubleByKey(this.getAttrKey()) < Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}
			}
			else if (oper.equals("<=") || oper.equals("xiaoyudengyu"))
			{
					if (en.GetValDoubleByKey(this.getAttrKey()) <= Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}
			}
			/*else if (oper.equals("!=") || oper.equals("budengyu"))
			{
					if (en.GetValDoubleByKey(this.getAttrKey()) != Double.parseDouble(this.getOperatorValue().toString()))
					{
						return true;
					}
					else
					{
						return false;
					}
			}*/
			else if (oper.equals("like"))
			{
					if (en.GetValStringByKey(this.getAttrKey()).indexOf(this.getOperatorValue().toString()) == -1)
					{
						return false;
					}
					else
					{
						return true;
					}
			}
			else
			{
					throw new RuntimeException("@没有找到操作符号(" + this.getFK_Operator().trim().toLowerCase() + ").");
			}
		}
		catch (RuntimeException ex)
		{
			Node nd23 = new Node(this.getNodeID());
			throw new RuntimeException("@判断条件:Node=[" + this.getNodeID() + "," + nd23.getEnDesc() + "], 出现错误。@" + ex.getMessage() + "。有可能您设置了非法的条件判断方式。");
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
		Map map = new Map("WF_Cond", "流程条件");

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

			//参数 for wangrui add 2015.10.6. 条件为station,depts模式的时候，需要指定人员。
		map.AddTBAtParas(2000);

		map.AddTBString(CondAttr.Note, null, "备注", true, true, 0, 500, 20);

			//      map.AddDDLSysEnum(NodeAttr.CondOrAnd, 0, "方向条件类型",
			//true, true, NodeAttr.CondOrAnd, "@0=And(条件集合中所有的都成立)@1=Or(条件集合中只有一个成立)");


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}