package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 节点表单
 节点的工作节点有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class FrmNode extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于节点与office表单的toolbar权限控制方案.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 关于节点与office表单的toolbar权限控制方案.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	public final String getFrmUrl()
	{
		switch (this.getHisFrmType())
		{
			case FrmType.FoolForm:
				return Glo.getCCFlowAppPath() + "/WF/CCForm/FrmFix";
			case FrmType.FreeFrm:
				return Glo.getCCFlowAppPath() + "/WF/CCForm/Frm";
			default:
				throw new RuntimeException("err,未处理。");
		}
	}
	private Frm _hisFrm = null;
	public final Frm getHisFrm() throws Exception
	{
		if (this._hisFrm == null)
		{
			this._hisFrm = new Frm(this.getFK_Frm());
			this._hisFrm.HisFrmNode = this;
		}
		return this._hisFrm;
	}
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 表单类型
	 * @throws Exception 
	*/
	public final FrmType getHisFrmType() throws Exception
	{
		return FrmType.forValue(this.GetValIntByKey(FrmNodeAttr.FrmType));
	}
	public final void setHisFrmType(BP.Sys.FrmType value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.FrmType, value.getValue());
	}
	/** 
	 表单类型
	 * @throws Exception 
	*/
	public final String getHisFrmTypeText() throws Exception
	{
		SysEnum se = new SysEnum(FrmNodeAttr.FrmType, this.getHisFrmType().getValue());
		return se.getLab();
			// return (BP.Sys.FrmType)this.GetValIntByKey(FrmNodeAttr.FrmType);
	}
	/** 
	 是否启用装载填充事件
	 * @throws Exception 
	*/
	public final boolean getIsEnableLoadData() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsEnableLoadData);
	}
	public final void setIsEnableLoadData(boolean value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.IsEnableLoadData, value);
	}
	/** 
	 是否启用节点组件?
	*/
	public final boolean getIsEnableFWC() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsEnableFWC);
	}
	public final void setIsEnableFWC(boolean value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.IsEnableFWC, value);
	}
	/** 
	 是否执行1变n
	*/
	public final boolean getIs1ToN() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.Is1ToN);
	}
	public final void setIs1ToN(boolean value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.Is1ToN, value);
	}
	/** 
	 是否默认打开
	*/
	public final boolean getIsDefaultOpen() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsDefaultOpen);
	}
	public final void setIsDefaultOpen(boolean value) throws Exception
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
	public final void setFK_Node(int value) throws Exception
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
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.Idx, value);
	}
	/** 
	 谁是主键？
	*/
	public final WhoIsPK getWhoIsPK() throws Exception
	{
		return WhoIsPK.forValue(this.GetValIntByKey(FrmNodeAttr.WhoIsPK));
	}
	public final void setWhoIsPK(WhoIsPK value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.WhoIsPK, value.getValue());
	}
	/** 
	 表单ID
	*/
	public final String getFK_Frm() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Frm);
	}
	public final void setFK_Frm(String value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.FK_Frm, value);
	}
	/** 
	 模版文件
	*/
	public final String getTempleteFile() throws Exception
	{
		String str = this.GetValStringByKey(FrmNodeAttr.TempleteFile);
		if (DataType.IsNullOrEmpty(str))
		{
			return this.getFK_Frm() + ".xls";
		}
		return str;
	}
	public final void setTempleteFile(String value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.TempleteFile, value);
	}
	/** 
	 是否显示
	*/
	public final String getIsEnable() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.IsEnable);
	}
	public final void setIsEnable(String value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.IsEnable, value);
	}
	/** 
	 关键字段
	*/
	public final String getGuanJianZiDuan() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.GuanJianZiDuan);
	}
	public final void setGuanJianZiDuan(String value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.GuanJianZiDuan, value);
	}
	/** 
	 对应的解决方案
	 0=默认方案.节点编号 1=自定义方案, 1=不可编辑.
	 * @throws Exception 
	*/
	public final FrmSln getFrmSln() throws Exception
	{
		if (this.GetValIntByKey(FrmNodeAttr.FrmSln) > 5)
		{
			return FrmSln.Self;
		}

		return FrmSln.forValue(this.GetValIntByKey(FrmNodeAttr.FrmSln));
	}
	public final void setFrmSln(FrmSln value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.FrmSln, value.getValue());
	}
	/** 
	 启用规则
	 * @throws Exception 
	*/
	public final int getFrmEnableRoleInt() throws Exception
	{
		return this.GetValIntByKey(FrmNodeAttr.FrmEnableRole);
	}
	public final void setFrmEnableRoleInt(int value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.FrmEnableRole, value);
	}
	/** 
	 表单启用规则
	*/
	public final FrmEnableRole getFrmEnableRole() throws Exception
	{
		return FrmEnableRole.forValue(this.GetValIntByKey(FrmNodeAttr.FrmEnableRole));
	}
	public final void setFrmEnableRole(FrmEnableRole value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.FrmEnableRole, value.getValue());
	}
	/** 
	 启用规则.
	 * @throws Exception 
	*/
	public final String getFrmEnableRoleText() throws Exception 
	{
		if (this.getFrmEnableRole() == FrmEnableRole.WhenHaveFrmPara && this.getFK_Frm().equals("ND" + this.getFK_Node()))
		{
			return "不启用";
		}

		SysEnum se = new SysEnum(FrmNodeAttr.FrmEnableRole, this.getFrmEnableRoleInt());
		return se.getLab();
	}
	/** 
	 表单启动表达式
	 * @throws Exception 
	*/
	public final String getFrmEnableExp() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FrmEnableExp);
	}
	public final void setFrmEnableExp(String value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.FrmEnableExp, value);
	}
	/** 
	 流程编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.FK_Flow, value);
	}
	/** 
	 是否可以编辑？
	 * @throws Exception 
	*/
	public final boolean getIsEdit() throws Exception
	{
		if (this.getFrmSln() == FrmSln.Readonly)
		{
			return false;
		}
		return true;
	}
	/** 
	 是否可以编辑？
	 * @throws Exception 
	*/
	public final int getIsEditInt() throws Exception
	{
		if (this.getIsEdit())
		{
			return 1;
		}
		return 0;
	}
	/** 
	 是否可以打印
	 * @throws Exception 
	*/
	public final boolean getIsPrint() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsPrint);
	}
	public final void setIsPrint(boolean value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.IsPrint, value);
	}
	/** 
	 是否可以打印
	 * @throws Exception 
	*/
	public final int getIsPrintInt() throws Exception
	{
		return this.GetValIntByKey(FrmNodeAttr.IsPrint);
	}
	/** 
	 汇总
	 * @throws Exception 
	*/
	public final String getHuiZong() throws Exception
	{
		return this.GetValStringByKey(FrmNodeAttr.HuiZong);
	}
	public final void setHuiZong(String value) throws Exception
	{
		this.SetValByKey(FrmNodeAttr.HuiZong, value);
	}
	/** 
	打开时是否关闭其它的页面？
	 * @throws Exception 
	*/
	public final boolean getIsCloseEtcFrm() throws Exception
	{
		return this.GetValBooleanByKey(FrmNodeAttr.IsCloseEtcFrm);
	}
	public final int getIsCloseEtcFrmInt() throws Exception
	{
		if (this.getIsCloseEtcFrm())
		{
			return 1;
		}
		return 0;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 节点表单
	*/
	public FrmNode()
	{
	}
	/** 
	 节点表单
	 
	 @param mypk
	 * @throws Exception 
	*/
	public FrmNode(String mypk) throws Exception
	{
		super(mypk);
	}
	/** 
	 节点表单
	 
	 @param fk_node 节点
	 @param fk_frm 表单
	 * @throws Exception 
	*/
	public FrmNode(String fk_flow, int fk_node, String fk_frm) throws Exception
	{
		int i = this.Retrieve(FrmNodeAttr.FK_Node, fk_node, FrmNodeAttr.FK_Frm, fk_frm);
		if (i == 0)
		{
			this.setIsPrint(false);
			//不可以编辑.
			this.setFrmSln(FrmSln.Default);
			// this.IsEdit = false;
			return;
//			throw new RuntimeException("@表单关联信息已被删除。");
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FrmNode", "节点表单");

		map.AddMyPK();

		map.AddTBString(FrmNodeAttr.FK_Frm, null, "表单ID", true, true, 1, 200, 200);
		map.AddTBInt(FrmNodeAttr.FK_Node, 0, "节点编号", true, false);
		map.AddTBString(FrmNodeAttr.FK_Flow, null, "流程编号", true, true, 1, 20, 20);
		map.AddTBString(FrmNodeAttr.FrmType, "0", "表单类型", true, true, 1, 20, 20);

			//菜单在本节点的权限控制.
			// map.AddTBInt(FrmNodeAttr.IsEdit, 1, "是否可以更新", true, false);
		map.AddTBInt(FrmNodeAttr.IsPrint, 0, "是否可以打印", true, false);
		map.AddTBInt(FrmNodeAttr.IsEnableLoadData, 0, "是否启用装载填充事件", true, false);
		map.AddTBInt(FrmNodeAttr.IsDefaultOpen, 0, "是否默认打开", true, false);
		map.AddTBInt(FrmNodeAttr.IsCloseEtcFrm, 0, "打开时是否关闭其它的页面？", true, false);
		map.AddTBInt(FrmNodeAttr.IsEnableFWC, 0, "是否启用审核组件？", true, false);

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

		map.AddTBString(FrmNodeAttr.GuanJianZiDuan, null, "关键字段", true, true, 0, 20, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法.
	public final void DoUp() throws Exception
	{
		this.DoOrderUp(FrmNodeAttr.FK_Node, String.valueOf(this.getFK_Node()), FrmNodeAttr.Idx);
	}
	public final void DoDown() throws Exception
	{
		this.DoOrderDown(FrmNodeAttr.FK_Node, String.valueOf(this.getFK_Node()), FrmNodeAttr.Idx);
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (this.getFK_Frm().length() == 0)
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

		this.setMyPK(this.getFK_Frm() + "_" + this.getFK_Node() + "_" + this.getFK_Flow());

		//获取表单的类型
		MapData mapData = new MapData();
		mapData.setNo(this.getFK_Frm());
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法.

}