package BP.WF.Template;

import BP.En.EntityMyPK;
import BP.En.Map;
import BP.WF.Work;

/**
 * 条件
 * 
 */
public class TurnTo extends EntityMyPK {
	/**
	 * 流程
	 */
	public final String getFK_Flow() {
		return this.GetValStringByKey(TurnToAttr.FK_Flow);
	}

	public final void setFK_Flow(String value) {
		this.SetValByKey(TurnToAttr.FK_Flow, value);
	}

	/**
	 * 节点
	 * 
	 */
	public final int getFK_Node() {
		return this.GetValIntByKey(TurnToAttr.FK_Node);
	}

	public final void setFK_Node(int value) {
		this.SetValByKey(TurnToAttr.FK_Node, value);
	}

	/**
	 * 条件类型
	 * 
	 */
	public final TurnToType getHisTurnToType() {
		return TurnToType.forValue(this.GetValIntByKey(TurnToAttr.TurnToType));
	}

	public final void setHisTurnToType(TurnToType value) {
		this.SetValByKey(TurnToAttr.TurnToType, value.getValue());
	}

	/**
	 * 转向URL
	 * 
	 */
	public final String getTurnToURL() {
		return this.GetValStringByKey(TurnToAttr.TurnToURL);
	}

	public final void setTurnToURL(String value) {
		this.SetValByKey(TurnToAttr.TurnToURL, value);
	}

	/// #region 实现基本的方方法
	/**
	 * 属性
	 * 
	 */
	public final String getFK_Attr() {
		return this.GetValStringByKey(TurnToAttr.FK_Attr);
	}

	public final void setFK_Attr(String value) throws Exception {
		if (value == null) {
			throw new RuntimeException("FK_Attr不能设置为null");
		}

		value = value.trim();
		this.SetValByKey(TurnToAttr.FK_Attr, value);
		BP.Sys.MapAttr attr = new BP.Sys.MapAttr(value);
		this.SetValByKey(TurnToAttr.AttrKey, attr.getKeyOfEn());
		this.SetValByKey(TurnToAttr.AttrT, attr.getName());
	}

	/**
	 * 属性Key
	 * 
	 */
	public final String getAttrKey() {
		return this.GetValStringByKey(TurnToAttr.AttrKey);
	}

	/**
	 * 属性Text
	 * 
	 */
	public final String getAttrT() {
		return this.GetValStringByKey(TurnToAttr.AttrT);
	}

	/**
	 * 操作的值
	 * 
	 */
	public final String getOperatorValueT() {
		return this.GetValStringByKey(TurnToAttr.OperatorValueT);
	}

	public final void setOperatorValueT(String value) {
		this.SetValByKey(TurnToAttr.OperatorValueT, value);
	}

	/**
	 * 运算符号
	 * 
	 */
	public final String getFK_Operator() {
		String s = this.GetValStringByKey(TurnToAttr.FK_Operator);
		if (s == null || s.equals("")) {
			return "=";
		}
		return s;
	}

	public final void setFK_Operator(String value) {
		this.SetValByKey(TurnToAttr.FK_Operator, value);
	}

	/**
	 * 操作符描述
	 * 
	 */
	public final String getFK_OperatorExt() {
		String s = this.getFK_Operator().toLowerCase();

		// switch (s)
		// ORIGINAL LINE: case "=":
		if (s.equals("=")) {
			return "dengyu";
		}
		// ORIGINAL LINE: case ">":
		else if (s.equals(">")) {
			return "dayu";
		}
		// ORIGINAL LINE: case ">=":
		else if (s.equals(">=")) {
			return "dayudengyu";
		}
		// ORIGINAL LINE: case "<":
		else if (s.equals("<")) {
			return "xiaoyu";
		}
		// ORIGINAL LINE: case "<=":
		else if (s.equals("<=")) {
			return "xiaoyudengyu";
		}
		// ORIGINAL LINE: case "!=":
		else if (s.equals("!=")) {
			return "budengyu";
		}
		// ORIGINAL LINE: case "like":
		else if (s.equals("like")) {
			return "like";
		} else {
			return s;
		}
	}

	/**
	 * 运算值
	 * 
	 */
	public final Object getOperatorValue() {
		return this.GetValStringByKey(TurnToAttr.OperatorValue);
	}

	public final void setOperatorValue(Object value) {
		this.SetValByKey(TurnToAttr.OperatorValue, (String) ((value instanceof String) ? value : null));
	}

	/**
	 * 操作值str
	 * 
	 */
	public final String getOperatorValueStr() {
		return this.GetValStringByKey(TurnToAttr.OperatorValue);
	}

	/**
	 * 操作值int
	 * 
	 */
	public final int getOperatorValueInt() {
		return this.GetValIntByKey(TurnToAttr.OperatorValue);
	}

	/**
	 * 操作值boolen
	 * 
	 */
	public final boolean getOperatorValueBool() {
		return this.GetValBooleanByKey(TurnToAttr.OperatorValue);
	}

	/**
	 * workid
	 * 
	 */
	public long WorkID = 0;
	/**
	 * 转向消息
	 * 
	 */
	public String MsgOfTurnTo = "";

	/// #endregion

	/**
	 * 条件
	 * 
	 */
	public TurnTo() {
	}

	/**
	 * 条件
	 * 
	 * @param mypk
	 *            PK
	 * @throws Exception
	 */
	public TurnTo(String mypk) throws Exception {
		this.setMyPK(mypk);
		this.Retrieve();
	}

	/// #endregion

	/// #region 公共方法
	/**
	 * 它的工作
	 * 
	 */
	public Work HisWork = null;

	/**
	 * 这个条件能不能通过
	 * 
	 * @throws Exception
	 * 
	 */
	public boolean getIsPassed() throws Exception {

		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
		attr.setMyPK(this.getFK_Attr());
		attr.RetrieveFromDBSources();

		if (this.HisWork.getEnMap().getAttrs().Contains(attr.getKeyOfEn()) == false)
			throw new RuntimeException(
					"判断条件方向出现错误：实体：" + this.HisWork.getEnDesc() + " 属性" + this.getFK_Attr() + "已经不存在.");

		this.MsgOfTurnTo = "@以表单值判断方向，值 " + this.HisWork.getEnDesc() + "." + this.getFK_Attr() + " ("
				+ this.HisWork.GetValStringByKey(attr.getKeyOfEn()) + ") 操作符:(" + this.getFK_Operator() + ") 判断值:("
				+ this.getOperatorValue().toString() + ")";

		String oper = this.getFK_Operator().trim().toLowerCase();

		// switch (this.FK_Operator.Trim().ToLower())
		// ORIGINAL LINE: case "=":
		if (oper.equals("=") || oper.equals("dengyu")) // 如果是 =
		{
			if (this.getOperatorValue().toString().equals(this.HisWork.GetValStringByKey(attr.getKeyOfEn()))) {
				return true;
			} else {
				return false;
			}

		}
		// ORIGINAL LINE: case ">":
		else if (oper.equals(">") || oper.equals("dayu")) {
			if (this.HisWork.GetValDoubleByKey(attr.getKeyOfEn()) > Double
					.parseDouble(this.getOperatorValue().toString())) {
				return true;
			} else {
				return false;
			}

		}
		// ORIGINAL LINE: case ">=":
		else if (oper.equals(">=") || oper.equals("dayudengyu")) {
			if (this.HisWork.GetValDoubleByKey(attr.getKeyOfEn()) >= Double
					.parseDouble(this.getOperatorValue().toString())) {
				return true;
			} else {
				return false;
			}

		}
		// ORIGINAL LINE: case "<":
		else if (oper.equals("<") || oper.equals("xiaoyu")) {
			if (this.HisWork.GetValDoubleByKey(attr.getKeyOfEn()) < Double
					.parseDouble(this.getOperatorValue().toString())) {
				return true;
			} else {
				return false;
			}

		}
		// ORIGINAL LINE: case "<=":
		else if (oper.equals("<=") || oper.equals("xiaoyudengyu")) {
			if (this.HisWork.GetValDoubleByKey(attr.getKeyOfEn()) <= Double
					.parseDouble(this.getOperatorValue().toString())) {
				return true;
			} else {
				return false;
			}
		}
		// ORIGINAL LINE: case "!=":
		else if (oper.equals("!=") || oper.equals("budengyu")) {
			if (this.HisWork.GetValDoubleByKey(attr.getKeyOfEn()) != Double
					.parseDouble(this.getOperatorValue().toString())) {
				return true;
			} else {
				return false;
			}
		}
		// ORIGINAL LINE: case "like":
		else if (oper.equals("like")) {
			if (this.HisWork.GetValStringByKey(attr.getKeyOfEn()).indexOf(this.getOperatorValue().toString()) == -1) {
				return false;
			} else {
				return true;
			}
		} else {
			throw new RuntimeException("@没有找到操作符号..");
		}
	}

	/**
	 * 属性
	 * 
	 */
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("WF_TurnTo", "转向条件");

		map.AddMyPK();
		map.AddTBInt(TurnToAttr.TurnToType, 0, "条件类型", true, true);
		map.AddTBString(TurnToAttr.FK_Flow, null, "流程", true, true, 0, 60, 20);
		map.AddTBInt(TurnToAttr.FK_Node, 0, "节点ID", true, true);

		map.AddTBString(TurnToAttr.FK_Attr, null, "属性外键Sys_MapAttr", true, true, 0, 80, 20);
		map.AddTBString(TurnToAttr.AttrKey, null, "键值", true, true, 0, 80, 20);
		map.AddTBString(TurnToAttr.AttrT, null, "属性名称", true, true, 0, 80, 20);

		map.AddTBString(TurnToAttr.FK_Operator, "=", "运算符号", true, true, 0, 60, 20);

		map.AddTBString(TurnToAttr.OperatorValue, "", "要运算的值", true, true, 0, 60, 20);
		map.AddTBString(TurnToAttr.OperatorValueT, "", "要运算的值T", true, true, 0, 60, 20);

		map.AddTBString(TurnToAttr.TurnToURL, null, "要转向的URL", true, true, 0, 700, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/// #endregion
}