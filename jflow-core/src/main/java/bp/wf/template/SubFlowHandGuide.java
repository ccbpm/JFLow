package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/**
 * 手工启动子流程.
 */
public class SubFlowHandGuide extends EntityMyPK {

	/// 基本属性
	/**
	 * UI界面上的访问控制
	 */
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}

	/**
	 * 主流程编号
	 */
	public final String getFK_Flow() throws Exception {
		return this.GetValStringByKey(SubFlowAutoAttr.FK_Flow);
	}

	public final void setFK_Flow(String value) throws Exception {
		SetValByKey(SubFlowAutoAttr.FK_Flow, value);
	}

	/**
	 * 流程编号
	 */
	public final String getSubFlowNo() throws Exception {
		return this.GetValStringByKey(SubFlowHandAttr.SubFlowNo);
	}

	public final void setSubFlowNo(String value) throws Exception {
		SetValByKey(SubFlowHandAttr.SubFlowNo, value);
	}

	/**
	 * 流程名称
	 */
	public final String getSubFlowName() throws Exception {
		return this.GetValStringByKey(SubFlowHandAttr.SubFlowName);
	}

	/**
	 * 条件表达式.
	 */
	public final String getCondExp() throws Exception {
		return this.GetValStringByKey(SubFlowHandAttr.CondExp);
	}

	public final void setCondExp(String value) throws Exception {
		SetValByKey(SubFlowHandAttr.CondExp, value);
	}

	/**
	 * 仅仅可以启动一次?
	 */
	public final boolean getStartOnceOnly() throws Exception {
		return this.GetValBooleanByKey(SubFlowYanXuAttr.StartOnceOnly);
	}

	/**
	 * 该流程启动的子流程运行结束后才可以再次启动
	 */
	public final boolean getCompleteReStart() throws Exception {
		return this.GetValBooleanByKey(SubFlowAutoAttr.CompleteReStart);
	}

	/**
	 * 表达式类型
	 */
	public final ConnDataFrom getExpType() throws Exception {
		return ConnDataFrom.forValue(this.GetValIntByKey(SubFlowHandAttr.ExpType));
	}

	public final void setExpType(ConnDataFrom value) throws Exception {
		SetValByKey(SubFlowHandAttr.ExpType, value.getValue());
	}

	public final String getFK_Node() throws Exception {
		return this.GetValStringByKey(SubFlowHandAttr.FK_Node);
	}

	public final void setFK_Node(String value) throws Exception {
		SetValByKey(SubFlowHandAttr.FK_Node, value);
	}

	/**
	 * 指定的流程结束后,才能启动该子流程(请在文本框配置子流程).
	 * 
	 * @throws Exception
	 */
	public final boolean getIsEnableSpecFlowOver() throws Exception {
		boolean val = this.GetValBooleanByKey(SubFlowAutoAttr.IsEnableSpecFlowOver);
		if (val == false) {
			return false;
		}

		if (this.getSpecFlowOver().length() > 2) {
			return true;
		}
		return false;
	}

	public final String getSpecFlowOver() throws Exception {
		return this.GetValStringByKey(SubFlowYanXuAttr.SpecFlowOver);
	}

	public final String getSpecFlowStart() throws Exception {
		return this.GetValStringByKey(SubFlowYanXuAttr.SpecFlowStart);
	}

	/**
	 * 自动发起的子流程发送方式
	 * 
	 * @throws Exception
	 */
	public final int getSendModel() throws Exception {
		return this.GetValIntByKey(SubFlowAutoAttr.SendModel);
	}

	/**
	 * 指定的流程启动后,才能启动该子流程(请在文本框配置子流程).
	 * 
	 * @throws Exception
	 */
	public final boolean getIsEnableSpecFlowStart() throws Exception {
		boolean val = this.GetValBooleanByKey(SubFlowAutoAttr.IsEnableSpecFlowStart);
		if (val == false) {
			return false;
		}

		if (this.getSpecFlowStart().length() > 2) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是树形结构
	 * @return
	 * @throws Exception
	 */
	public boolean getIsTreeConstruct()throws Exception{
		return this.GetValBooleanByKey(SubFlowHandGuideAttr.IsTreeConstruct);
	}


	/// 构造函数
	/**
	 * 手工启动子流程
	 */
	public SubFlowHandGuide() {
	}

	public SubFlowHandGuide(String subFlowMyPK) throws Exception {
		this.setMyPK(subFlowMyPK );
		this.Retrieve();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap() throws Exception {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("WF_NodeSubFlow", "手动启动子流程");

		map.AddMyPK();

		map.AddTBString(SubFlowAttr.FK_Flow, null, "主流程编号", true, true, 0, 5, 100);
		map.AddTBInt(SubFlowHandAttr.FK_Node, 0, "节点", false, true);

		map.AddTBString(SubFlowYanXuAttr.SubFlowNo, null, "子流程编号", false, true, 0, 10, 150, false);
		map.AddTBString(SubFlowYanXuAttr.SubFlowName, null, "子流程名称", false, true, 0, 200, 150, false);
		map.AddTBString(SubFlowHandGuideAttr.ParentNo, "0", "父节点的值", true, false, 0, 20, 100, true);

		map.AddTBString(SubFlowHandGuideAttr.SubFlowGuideSQL, null, "子流程前置导航配置SQL", true, false, 0, 200, 150, true);
		String msg = "发起前置导航的实体列表SQL, 必须包含No,Name两个列,与流程发起前置导航相同.";
		msg += "\t\n比如：SELECT No,Name FROM Port_Emp ";
		msg += "\t\nSQL配置支持ccbpm表达式.";
		map.SetHelperAlert(SubFlowHandGuideAttr.SubFlowGuideSQL, msg);

		map.AddTBString(SubFlowHandGuideAttr.SubFlowGuideGroup, null, "分组的SQL", true, false, 0, 200, 150, true);

		map.AddTBString(SubFlowHandGuideAttr.SubFlowGuideEnNoFiled, null, "实体No字段", true, false, 0, 40, 150);
		map.AddTBString(SubFlowHandGuideAttr.SubFlowGuideEnNameFiled, null, "实体Name字段", true, false, 0, 40, 150);

		//@0=单条手工启动, 1=按照简单数据源批量启动. @2=分组数据源批量启动. @3=树形结构批量启动.
		map.AddTBInt(SubFlowHandAttr.SubFlowStartModel, 0, "启动模式", false, false);

		//@0=表格模式, 1=列表模式.
		map.AddTBInt(SubFlowHandAttr.SubFlowShowModel, 0, "展现模式", false, false);
		//  map.Add(SubFlowHandAttr.IsTreeConstruct, 0, "是否是树结构", false, true);

		//批量发送后，是否隐藏父流程的待办. @yln.
		map.AddBoolean(SubFlowHandGuideAttr.SubFlowHidTodolist, false, "发送后是否隐藏父流程待办",false,false);


		this.set_enMap(map);
		return this.get_enMap();
	}

	public String getSubFlowGuideEnNoFiled() throws Exception {
		return this.GetValStringByKey(SubFlowHandGuideAttr.SubFlowGuideEnNoFiled);
		 
	}
	public String getSubFlowGuideEnNameFiled() throws Exception {
		return this.GetValStringByKey(SubFlowHandGuideAttr.SubFlowGuideEnNameFiled);
		 
	}
	public String getParentNo() throws Exception{
		return this.GetValStringByKey(SubFlowHandGuideAttr.ParentNo);
	}

	public String getSubFlowGuideSQL() throws Exception{
		return this.GetValStringByKey(SubFlowHandGuideAttr.SubFlowGuideSQL);
	}

	public boolean getSubFlowHidTodolist()throws Exception{
		return this.GetValBooleanByKey(SubFlowHandGuideAttr.SubFlowHidTodolist);
	}

	@Override
	public boolean beforeUpdateInsertAction()throws Exception{

		/*if(this.getIsTreeConstruct() == true
				&& DataType.IsNullOrEmpty(this.getParentNo())==true ){
			throw new Exception("请配置父节点的编号");
		}*/
		return super.beforeUpdateInsertAction();
	}

}