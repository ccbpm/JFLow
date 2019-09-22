package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
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
public class FrmNodeExt extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
	public final String getFK_Frm()
	{
		return this.GetValStrByKey(FrmNodeAttr.FK_Frm);
	}
	public final int getFK_Node()
	{
		return this.GetValIntByKey(FrmNodeAttr.FK_Node);
	}
	/** 
	 @李国文 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(FrmNodeAttr.FK_Flow);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 节点表单
	*/
	public FrmNodeExt()
	{
	}
	/** 
	 节点表单
	 
	 @param mypk
	*/
	public FrmNodeExt(String mypk)
	{
		this.MyPK = mypk;
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_FrmNode", "节点表单");

		map.AddMyPK();

		map.AddDDLEntities(FrmNodeAttr.FK_Frm, null, "表单", new MapDatas(), false);
		map.AddTBString(FrmNodeAttr.FK_Flow, null, "流程编号", true, true, 0, 4, 20);
		map.AddTBInt(FrmNodeAttr.FK_Node, 0, "节点ID", true, true);

		map.AddBoolean(FrmNodeAttr.IsPrint, false, "是否可以打印", true, true);
		map.AddBoolean(FrmNodeAttr.IsEnableLoadData, false, "是否启用装载填充事件", true, true);

		map.AddBoolean(FrmNodeAttr.IsCloseEtcFrm, false, "打开时是否关闭其它的页面？", true, true,true);
		map.SetHelperAlert(FrmNodeAttr.IsCloseEtcFrm,"默认为不关闭,当该表单以tab标签也打开时,是否关闭其它的tab页?");

		map.AddDDLSysEnum(FrmNodeAttr.WhoIsPK, 0, "谁是主键?", true, true);
		map.SetHelperAlert(FrmNodeAttr.WhoIsPK, "用来控制谁是表单事例的主键的方案，对于父子流程如果子流程需要在看到父流程的表单，就需要设置ParentID是主键。");

		map.AddDDLSysEnum(FrmNodeAttr.FrmSln, 0, "控制方案", true, true, FrmNodeAttr.FrmSln, "@0=默认方案@1=只读方案@2=自定义方案");
		map.SetHelperAlert(FrmNodeAttr.FrmSln, "控制该表单数据元素权限的方案，如果是自定义方案，就要设置每个表单元素的权限.");


		map.AddBoolean(FrmNodeAttr.IsEnableFWC, false, "是否启用审核组件？", true, true, true);
		map.SetHelperAlert(FrmNodeAttr.IsEnableFWC, "控制该表单是否启用审核组件？如果启用了就显示在该表单上;显示审核组件的前提是启用了节点表单的审核组件，审核组件的状态也是根据节点审核组件的状态决定的");

			//map.AddDDLSysEnum( BP.WF.Template.FrmWorkCheckAttr.FWCSta, 0, "审核组件(是否启用审核组件？)", true, true);

			//显示的
		map.AddTBInt(FrmNodeAttr.Idx, 0, "顺序号", true, false);
		map.SetHelperAlert(FrmNodeAttr.Idx, "在表单树上显示的顺序,可以通过列表调整.");

			//add 2016.3.25.
		map.AddBoolean(FrmNodeAttr.Is1ToN, false, "是否1变N？(分流节点有效)", true, true, true);
		map.AddTBString(FrmNodeAttr.HuiZong, null, "汇总的数据表名", true, false, 0, 300, 20);
		map.SetHelperAlert(FrmNodeAttr.HuiZong, "子线程要汇总的数据表，对当前节点是子线程节点有效。");

			//模版文件，对于office表单有效.
		map.AddTBString(FrmNodeAttr.TempleteFile, null, "模版文件", true, false, 0, 500, 20);

			//是否显示
		map.AddTBString(FrmNodeAttr.GuanJianZiDuan, null, "关键字段", true, false, 0, 20, 20);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 表单启用规则. @袁丽娜
		map.AddDDLSysEnum(FrmNodeAttr.FrmEnableRole, 0, "启用规则", false, false, FrmNodeAttr.FrmEnableRole, "@0=始终启用@1=有数据时启用@2=有参数时启用@3=按表单的字段表达式@4=按SQL表达式@5=不启用@6=按岗位@7=按部门");

		map.SetHelperAlert(FrmNodeAttr.FrmEnableRole, "用来控制该表单是否显示的规则.");


		map.AddTBStringDoc(FrmNodeAttr.FrmEnableExp, null, "启用的表达式", false, false, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 表单启用规则.

		RefMethod rm = new RefMethod();
			//@袁丽娜
		rm.Title = "启用规则";
		rm.ClassMethodName = this.toString() + ".DoEnableRole()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "字段权限";
		rm.ClassMethodName = this.toString() + ".DoFields()";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "从表权限";
		rm.ClassMethodName = this.toString() + ".DoDtls()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "附件权限";
		rm.ClassMethodName = this.toString() + ".DoAths()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "图片附件权限";
		rm.ClassMethodName = this.toString() + ".DoImgAths()";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "从其他节点Copy权限设置";
		rm.ClassMethodName = this.toString() + ".DoCopyFromNode()";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "改变表单类型";
		rm.ClassMethodName = this.toString() + ".DoChangeFrmType()";
		rm.HisAttrs.AddDDLSysEnum("FrmType", 0, "修改表单类型", true, true);
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "表单启用规则";
			//rm.ClassMethodName = this.ToString() + ".DoFrmEnableRole()";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//map.AddRefMethod(rm);


		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 改变表单类型
	 
	 @param val 要改变的类型
	 @return 
	*/
	public final String DoChangeFrmType(int val)
	{
		MapData md = new MapData(this.getFK_Frm());
		String str = "原来的是:" + md.HisFrmTypeText + "类型，";
		md.HisFrmTypeInt = val;
		str += "现在修改为：" + md.HisFrmTypeText + "类型";
		md.Update();

		return str;
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 表单元素权限.
	public final String DoDtls()
	{
		return "../../Admin/Sln/Dtls.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}
	public final String DoFields()
	{
		return "../../Admin/Sln/Fields.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}
	public final String DoAths()
	{
		return "../../Admin/Sln/Aths.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}

	public final String DoImgAths()
	{
		return "../../Admin/Sln/ImgAths.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}

	public final String DoCopyFromNode()
	{
		return "../../Admin/Sln/Aths.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}
	public final String DoEnableRole()
	{
		return "../../Admin/AttrNode/BindFrmsNodeEnableRole.htm?MyPK=" + this.MyPK;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 表单元素权限.

}