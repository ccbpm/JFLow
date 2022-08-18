package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.wf.Glo;
import bp.wf.template.sflow.*;
import bp.wf.template.frm.*;
import bp.wf.*;


/** 
 节点表单
 节点的工作节点有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class FrmNode extends EntityMyPK
{

		///#region 关于节点与office表单的toolbar权限控制方案.


		///#endregion 关于节点与office表单的toolbar权限控制方案.


		///#region 基本属性
	public final String getFrmUrl() throws Exception {
		switch (this.getHisFrmType())
		{
			case FoolForm:
				return Glo.getCCFlowAppPath() + "/WF/CCForm/FrmFix";
			case Develop:
				return Glo.getCCFlowAppPath() + "/WF/CCForm/Frm";
			default:
				throw new RuntimeException("err,未处理。");
		}
	}
	private CCFrm _hisFrm = null;
	public final CCFrm getHisFrm() throws Exception {
		if (this._hisFrm == null)
		{
			this._hisFrm = new CCFrm(this.getFKFrm());
			this._hisFrm.HisFrmNode = this;
		}
		return this._hisFrm;
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 表单类型
	*/
	public final FrmType getHisFrmType() throws Exception {
		return FrmType.forValue(this.GetValIntByKey(FrmNodeAttr.FrmType));
	}
	public final void setHisFrmType(FrmType value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FrmType, value.getValue());
	}
	/** 
	 表单类型
	*/
	public final String getHisFrmTypeText() throws Exception {
		SysEnum se = new SysEnum(FrmNodeAttr.FrmType, this.getHisFrmType().getValue());
		return se.getLab();
			// return (BP.Sys.FrmType)this.GetValIntByKey(FrmNodeAttr.FrmType);
	}
	/** 
	 是否启用装载填充事件
	*/
	public final boolean isEnableLoadData() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsEnableLoadData);
	}
	public final void setEnableLoadData(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.IsEnableLoadData, value);
	}
	/** 
	 是否启用节点组件?
	*/
	public final FrmWorkCheckSta isEnableFWC() throws Exception {
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(FrmNodeAttr.IsEnableFWC));
	}
	public final void setEnableFWC(FrmWorkCheckSta value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.IsEnableFWC, value.getValue());
	}

	public final FrmSubFlowSta getSFSta() throws Exception {
		return FrmSubFlowSta.forValue(this.GetValIntByKey(FrmNodeAttr.SFSta));
	}
	public final void setSFSta(FrmSubFlowSta value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.SFSta, value.getValue());
	}
	/** 
	 是否执行1变n
	*/
	public final boolean getIs1ToN() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.Is1ToN);
	}
	public final void setIs1ToN(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.Is1ToN, value);
	}
	/** 
	 是否默认打开
	*/
	public final boolean isDefaultOpen() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsDefaultOpen);
	}
	public final void setDefaultOpen(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.IsDefaultOpen, value);
	}
	/** 
	节点
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(FrmNodeAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	 {
		this.SetValByKey(FrmNodeAttr.FK_Node, value);
	}
	/** 
	 顺序号
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(FrmNodeAttr.Idx);
	}
	public final void setIdx(int value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.Idx, value);
	}
	/** 
	 谁是主键？
	*/
	public final WhoIsPK getWhoIsPK() throws Exception {
		return WhoIsPK.forValue(this.GetValIntByKey(FrmNodeAttr.WhoIsPK));
	}
	public final void setWhoIsPK(WhoIsPK value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.WhoIsPK, value.getValue());
	}
	/** 
	 表单ID
	*/
	public final String getFKFrm() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Frm);
	}
	public final void setFKFrm(String value)
	 {
		this.SetValByKey(FrmNodeAttr.FK_Frm, value);
	}
	/** 
	 模版文件
	*/
	public final String getTempleteFile() throws Exception {
		String str = this.GetValStringByKey(FrmNodeAttr.TempleteFile);
		if (DataType.IsNullOrEmpty(str))
		{
			return this.getFKFrm() + ".xls";
		}
		return str;
	}
	public final void setTempleteFile(String value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.TempleteFile, value);
	}
	/** 
	 是否显示
	*/
	public final boolean isEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsEnable);
	}
	public final void setEnable(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.IsEnable, value);
	}

	/** 
	 显示的名字
	*/
	public final String getFrmNameShow() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FrmNameShow);
	}
	public final void setFrmNameShow(String value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FrmNameShow, value);
	}
	/** 
	 对应的解决方案
	 0=默认方案.节点编号 1=自定义方案, 1=不可编辑.
	*/
	public final FrmSln getFrmSln() throws Exception {
		if (this.GetValIntByKey(FrmNodeAttr.FrmSln) > 5)
		{
			return FrmSln.Self;
		}

		return FrmSln.forValue(this.GetValIntByKey(FrmNodeAttr.FrmSln));
	}
	public final void setFrmSln(FrmSln value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FrmSln, value.getValue());
	}
	/** 
	 启用规则
	*/
	public final int getFrmEnableRoleInt() throws Exception
	{
		return this.GetValIntByKey(FrmNodeAttr.FrmEnableRole);
	}
	public final void setFrmEnableRoleInt(int value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FrmEnableRole, value);
	}
	/** 
	 表单启用规则
	*/
	public final FrmEnableRole getFrmEnableRole() throws Exception {
		return FrmEnableRole.forValue(this.GetValIntByKey(FrmNodeAttr.FrmEnableRole));
	}
	public final void setFrmEnableRole(FrmEnableRole value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FrmEnableRole, value.getValue());
	}
	/** 
	 启用规则.
	*/
	public final String getFrmEnableRoleText() throws Exception {
		if (this.getFrmEnableRole() == FrmEnableRole.WhenHaveFrmPara && this.getFKFrm().equals("ND" + this.getFK_Node()))
		{
			return "不启用";
		}

		SysEnum se = new SysEnum(FrmNodeAttr.FrmEnableRole, this.getFrmEnableRoleInt());
		return se.getLab();
	}
	/** 
	 表单启动表达式
	*/
	public final String getFrmEnableExp() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FrmEnableExp);
	}
	public final void setFrmEnableExp(String value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FrmEnableExp, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.FK_Flow, value);
	}
	/** 
	 是否可以编辑？
	*/
	public final boolean isEdit() throws Exception {
		if (this.getFrmSln() == FrmSln.Readonly)
		{
			return false;
		}
		return true;
	}
	/** 
	 是否可以编辑？
	*/
	public final int isEditInt() throws Exception {
		if (this.isEdit())
		{
			return 1;
		}
		return 0;
	}
	/** 
	 是否可以打印
	*/
	public final boolean isPrint() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsPrint);
	}
	public final void setPrint(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.IsPrint, value);
	}
	/** 
	 是否可以打印
	*/
	public final int isPrintInt() throws Exception
	{
		return this.GetValIntByKey(FrmNodeAttr.IsPrint);
	}
	/** 
	 汇总
	*/
	public final String getHuiZong() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.HuiZong);
	}
	public final void setHuiZong(String value)  throws Exception
	 {
		this.SetValByKey(FrmNodeAttr.HuiZong, value);
	}
	/** 
	打开时是否关闭其它的页面？
	*/
	public final boolean isCloseEtcFrm() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsCloseEtcFrm);
	}
	public final int isCloseEtcFrmInt() throws Exception {
		if (this.isCloseEtcFrm())
		{
			return 1;
		}
		return 0;
	}
	public final String getCheckField() throws Exception
	{
		return this.GetValStringByKey(NodeWorkCheckAttr.CheckField);
	}
	public final void setCheckField(String value)  throws Exception
	 {
		this.SetValByKey(NodeWorkCheckAttr.CheckField, value);
	}
	/** 
	 单据编号字段
	*/
	public final String getBillNoField() throws Exception
	{
		return this.GetValStringByKey(NodeWorkCheckAttr.BillNoField);
	}
	public final void setBillNoField(String value)  throws Exception
	 {
		this.SetValByKey(NodeWorkCheckAttr.BillNoField, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 节点表单
	*/
	public FrmNode()  {
	}
	/** 
	 节点表单
	 
	 param mypk
	*/
	public FrmNode(String mypk)throws Exception
	{
		super(mypk);
	}
	/** 
	 节点表单
	 
	 param fk_node 节点
	 param fk_frm 表单
	*/
	public FrmNode(int fk_node, String fk_frm) throws Exception {
		//设置属性.
		this.setFK_Node(fk_node);
		this.setFKFrm(fk_frm);

		int i = this.Retrieve(FrmNodeAttr.FK_Node, fk_node, FrmNodeAttr.FK_Frm, fk_frm);

		if (i == 0)
		{
			this.setPrint(false);
			//不可以编辑.
			this.setFrmSln(FrmSln.Default);
			Node node = new Node(fk_node);
			if (node.getFrmWorkCheckSta() != FrmWorkCheckSta.Disable)
			{
				this.setEnableFWC(node.getFrmWorkCheckSta());
			}

			this.setFK_Flow(node.getFK_Flow());
			return;
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FrmNode", "节点表单");

		map.AddMyPK(true);
		map.AddTBString(FrmNodeAttr.FK_Frm, null, "表单ID", true, true, 1, 200, 200);
		map.AddTBInt(FrmNodeAttr.FK_Node, 0, "节点编号", true, false);
		map.AddTBString(FrmNodeAttr.FK_Flow, null, "流程编号", true, true, 1, 10, 20);
		map.AddTBString(FrmNodeAttr.FrmType, "0", "表单类型", true, true, 1, 20, 20);

			//菜单在本节点的权限控制.
			// map.AddTBInt(FrmNodeAttr.IsEdit, 1, "是否可以更新", true, false);
		map.AddTBInt(FrmNodeAttr.IsPrint, 0, "是否可以打印", true, false);
		map.AddTBInt(FrmNodeAttr.IsEnableLoadData, 0, "是否启用装载填充事件", true, false);
		map.AddTBInt(FrmNodeAttr.IsDefaultOpen, 0, "是否默认打开", true, false);
		map.AddTBInt(FrmNodeAttr.IsCloseEtcFrm, 0, "打开时是否关闭其它的页面？", true, false);
		map.AddTBInt(FrmNodeAttr.IsEnableFWC, 0, "是否启用审核组件？", true, false);
		map.AddTBInt(FrmNodeAttr.SFSta, 0, "是否启用父子流程组件？", true, false);

			//显示的
		map.AddTBInt(FrmNodeAttr.Idx, 0, "顺序号", true, false);
		map.AddTBInt(FrmNodeAttr.FrmSln, 0, "表单控制方案", true, false);

			// add 2014-01-26
		map.AddTBInt(FrmNodeAttr.WhoIsPK, 0, "谁是主键？", true, false);

			//add 2016.3.25.
		map.AddTBInt(FrmNodeAttr.Is1ToN, 0, "是否1变N？", true, false);
		map.AddTBString(FrmNodeAttr.HuiZong, null, "子线程要汇总的数据表", true, true, 0, 300, 20);
		map.AddTBInt(FrmNodeAttr.FrmEnableRole, 0, "表单启用规则", true, false);

		map.AddTBString(FrmNodeAttr.FrmEnableExp, null, "启用的表达式", true, true, 0, 900, 20);

			//模版文件，对于office表单有效.
		map.AddTBString(FrmNodeAttr.TempleteFile, null, "模版文件", true, true, 0, 500, 20);

			//是否显示
		map.AddTBInt(FrmNodeAttr.IsEnable, 1, "是否显示", true, false);

			// map.AddTBString(FrmNodeAttr.GuanJianZiDuan, null, "关键字段", true, true, 0, 20, 20);

			//@2019.09.30 by zhoupeng.
		map.AddTBString(FrmNodeAttr.FrmNameShow, null, "表单显示名字", true, false, 0, 100, 20);
			//  map.SetHelperAlert(FrmNodeAttr.FrmNameShow, "显示在表单树上的名字,默认为空,表示与表单的实际名字相同.多用于节点表单的名字在表单树上显示.");
			//签批字段不可见
		map.AddTBString(NodeWorkCheckAttr.CheckField, null, "签批字段", false, false, 0, 50, 10, false);
		map.AddTBString(NodeWorkCheckAttr.BillNoField, null, "单据编号字段", false, false, 0, 50, 10, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 方法.
	public final void DoUp() throws Exception {
		this.DoOrderUp(FrmNodeAttr.FK_Node, String.valueOf(this.getFK_Node()), FrmNodeAttr.Idx);
	}
	public final void DoDown() throws Exception {
		this.DoOrderDown(FrmNodeAttr.FK_Node, String.valueOf(this.getFK_Node()), FrmNodeAttr.Idx);
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		if (this.getFKFrm().length() == 0)
		{
			throw new RuntimeException("@表单编号为空");
		}

		if (this.getFK_Node() == 0)
		{
			throw new RuntimeException("@节点ID为空");
		}

		if (this.getFK_Flow().length() == 0)
		{
			throw new RuntimeException("@流程编号为空");
		}


		this.setMyPK(this.getFKFrm() + "_" + this.getFK_Node() + "_" + this.getFK_Flow());

		//获取表单的类型
		MapData mapData = new MapData();
		mapData.setNo(this.getFKFrm());
		if (mapData.RetrieveFromDBSources() == 1)
		{
			this.setHisFrmType(mapData.getHisFrmType());
		}
		else
		{
			this.setHisFrmType(FrmType.FoolForm);
		}

		return super.beforeUpdateInsertAction();
	}

		///#endregion 方法.

	@Override
	protected boolean beforeInsert() throws Exception {
		//如果不是开始节点，默认为只读方案.
		if (String.valueOf(this.getFK_Node()).endsWith("01") == false)
		{
			this.setFrmSln(FrmSln.Readonly);
		}

		return super.beforeInsert();
	}

}