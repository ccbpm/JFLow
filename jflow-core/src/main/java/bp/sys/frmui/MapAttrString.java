package bp.sys.frmui;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;

/** 
 实体属性
*/
public class MapAttrString extends EntityMyPK
{
	private static final long serialVersionUID = 1L;

	///文本字段参数属性.
	public final boolean getIsSupperText() throws Exception
	{
		return this.GetValBooleanByKey(MapAttrAttr.IsSupperText);
	}
	public final void setIsSupperText(boolean value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.IsSupperText, value);
	}
	public final boolean getIsRichText()throws Exception
	{
		return this.GetValBooleanByKey(MapAttrAttr.IsRichText);
	}
	public final void setIsRichText(boolean value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.IsRichText, value);
	}
	/** 
	 表单ID
	*/
	public final String getFK_MapData()throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}
	/** 
	 最大长度
	*/
	public final int getMaxLen()throws Exception
	{
		return this.GetValIntByKey(MapAttrAttr.MaxLen);
	}
	public final void setMaxLen(int value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.MaxLen, value);
	}

	/** 
	 字段
	*/
	public final String getKeyOfEn()throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}
	/** 
	 控件类型
	*/
	public final UIContralType getUIContralType()throws Exception
	{
		return UIContralType.forValue(this.GetValIntByKey(MapAttrAttr.UIContralType));
	}
	public final void setUIContralType(UIContralType value)throws Exception
	{
		this.SetValByKey(MapAttrAttr.UIContralType, value.getValue());
	}
	/** 
	 是否可见
	*/
	public final boolean getUIVisible()throws Exception
	{
		return this.GetValBooleanByKey(MapAttrAttr.UIVisible);
	}
	public final void setUIVisible(boolean value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.UIVisible, value);
	}
	/** 
	 是否可编辑
	*/
	public final boolean getUIIsEnable()throws Exception
	{
		return this.GetValBooleanByKey(MapAttrAttr.UIIsEnable);
	}
	public final void setUIIsEnable(boolean value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.UIIsEnable, value);
	}

		///


		///构造方法
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 实体属性
	*/
	public MapAttrString()
	{
	}
	/** 
	 实体属性
	*/
	public MapAttrString(String myPK)throws Exception
	{
		this.setMyPK(myPK);
		this.Retrieve();

	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_MapAttr", "文本字段");


			///基本字段信息.
		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);
		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20, true);

		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

			//默认值.
		String sql = "SELECT No,Name FROM Sys_GloVar WHERE GroupKey='DefVal'";
			//显示的分组.
		map.AddDDLSQL("ExtDefVal", "0", "系统默认值", sql, true);

		map.AddTBString(MapAttrAttr.DefVal, null, "默认值表达式", true, false, 0, 400, 20, true);
		map.SetHelperAlert(MapAttrAttr.DefVal, "可以编辑的字段设置默认值后，保存时候按照编辑字段计算.");

		map.AddTBInt(MapAttrAttr.MinLen, 0, "最小长度", true, false);
		map.AddTBInt(MapAttrAttr.MaxLen, 50, "最大长度", true, false);
		map.SetHelperAlert(MapAttrAttr.MaxLen, "定义该字段的字节长度.");


		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.SetHelperAlert(MapAttrAttr.UIWidth, "对自由表单,从表有效,显示文本框的宽度.");

		map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, false);
			//map.AddTBInt(MapAttrAttr.UIContralType, 0, "控件", true, false); 
			//map.AddDDLSysEnum(MapAttrAttr.UIContralType, 0, "控件", true, false, MapAttrAttr.UIContralType);
			//map.AddTBFloat("ExtRows", 1, "文本框行数(决定高度)", true, false);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
		map.SetHelperAlert(MapAttrAttr.UIVisible, "对于不可见的字段可以在隐藏功能的栏目里找到这些字段进行编辑或者删除.");

		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		map.SetHelperAlert(MapAttrAttr.UIIsEnable, "不可编辑,让该字段设置为只读.");

		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
		map.AddBoolean(MapAttrAttr.IsRichText, false, "是否富文本？", true, true);
		map.SetHelperAlert(MapAttrAttr.IsRichText, "以html编辑器呈现或者编写字段.");
		map.AddBoolean(MapAttrAttr.IsSecret, false, "是否保密？", true, true);

		map.AddBoolean(MapAttrAttr.IsSupperText, false, "是否大块文本？(是否该字段存放的超长字节字段)", true, true, true);
		map.SetHelperAlert(MapAttrAttr.IsSupperText, "大块文本存储字节比较长，超过4000个字符.");

		map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 400, 20, true);
		map.SetHelperAlert(MapAttrAttr.Tip, "在文本框输入的时候显示在文本框背景的提示文字,也就是文本框的 placeholder 的值.");
			//CCS样式
		map.AddDDLSQL(MapAttrAttr.CSS, "0", "自定义样式", MapAttrString.getSQLOfCSSAttr(), true);


			/// 基本字段信息.


			///傻瓜表单
			//单元格数量 2013-07-24 增加
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "TextBox单元格数量", true, true, "ColSpanAttrDT", "@0=跨0个单元格@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨5个单元格@6=跨6个单元格");
		map.SetHelperAlert(MapAttrAttr.ColSpan, "对于傻瓜表单有效: 标识该字段TextBox横跨的宽度,占的单元格数量.");

			//文本占单元格数量
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "Label单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨6个单元格@6=跨6个单元格");
		map.SetHelperAlert(MapAttrAttr.TextColSpan, "对于傻瓜表单有效: 标识该字段Lable，标签横跨的宽度,占的单元格数量.");


			//文本跨行
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);

			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);

		map.AddDDLSysEnum(MapAttrAttr.IsSigan, 0, "签名模式", true, true, MapAttrAttr.IsSigan, "@0=无@1=图片签名@2=山东CA@3=广东CA@4=图片盖章");
		map.SetHelperAlert(MapAttrAttr.IsSigan, "图片签名,需要的是当前是只读的并且默认值为@WebUser.No,其他签名需要个性化的编写数字签章的集成代码.");

		map.AddTBInt(MapAttrAttr.Idx, 0, "顺序号", true, false);
		map.SetHelperAlert(MapAttrAttr.Idx, "对傻瓜表单有效:用于调整字段在同一个分组中的顺序.");

			/// 傻瓜表单


			///基本功能.
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "正则表达式";
		rm.ClassMethodName = this.toString() + ".DoRegularExpression()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "文本框自动完成";
		rm.ClassMethodName = this.toString() + ".DoTBFullCtrl2019()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "事件绑函数";
		rm.ClassMethodName = this.toString() + ".BindFunction()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "快速录入";
		rm.ClassMethodName = this.toString() + ".DoFastEnter()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "全局默认值";
		rm.ClassMethodName = this.toString() + ".DoDefVal()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		  //  map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "字段重命名";
		rm.ClassMethodName = this.toString() + ".DoRenameField()";
		rm.getHisAttrs().AddTBString("key1", "@KeyOfEn", "字段重命名为?", true, false, 0, 100, 100);
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "如果是节点表单，系统就会把该流程上的所有同名的字段都会重命名，包括NDxxxRpt表单。";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "Pop返回值";
		rm.ClassMethodName = this.toString() + ".DoPop2019()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "常用字段";
			//rm.ClassMethodName = this.ToString() + ".DoGeneralField()";
			//rm.refMethodType = RefMethodType.RightFrameOpen;
			//map.AddRefMethod(rm);

			/// 基本功能.


			///输入多选.
		rm = new RefMethod();
		rm.GroupName = "输入内容多选";
		rm.Title = "小范围多选(combox)";
		rm.ClassMethodName = this.toString() + ".DoMultipleChoiceSmall()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "输入内容多选";
		rm.Title = "搜索多选";
		rm.ClassMethodName = this.toString() + ".DoMultipleChoiceSearch()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "输入内容多选";
		rm.Title = "高级快速录入";
		rm.ClassMethodName = this.toString() + ".DoFastInput()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

			/// 输入多选


			///高级设置.
		rm = new RefMethod();
		rm.Title = "批处理";
		rm.ClassMethodName = this.toString() + ".DoEleBatch()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);

			//sly 翻译过去。
		rm = new RefMethod();
		rm.Title = "转化为签批组件";
		rm.ClassMethodName = this.toString() + ".DoSetCheck()";
		rm.Warning = "您确定要设置为签批组件吗？";
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "转化为评论组件";
			//rm.ClassMethodName = this.ToString() + ".DoSetFlowBBS()";
			//rm.Warning = "您确定要设置为评论组件吗？";
		   // rm.GroupName = "高级设置";
			//map.AddRefMethod(rm);


			/// 执行的方法.

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 字段分组查询语句
	*/
	public static String getSQLOfGroupAttr()
	{
		return "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'  AND (CtrlType IS NULL OR CtrlType='')  ";
	}
	/** 
	 字段自定义样式查询
	*/
	public static String getSQLOfCSSAttr()
	{
		return "SELECT No,Name FROM Sys_GloVar WHERE GroupKey='CSS' OR GroupKey='css' ";
	}
	/** 
	 删除
	*/
	@Override
	protected void afterDelete()throws Exception
	{
		//如果是附件字段删除附件属性
		//MapAttr attr = new MapAttr(this.MyPK);
		if (this.getUIContralType() == UIContralType.AthShow)
		{
			FrmAttachment ath = new FrmAttachment();
			ath.Delete(FrmAttachmentAttr.MyPK, this.getMyPK());
		}
		//删除可能存在的关联属性.
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + this.getFK_MapData() + "' AND KeyOfEn='" + this.getKeyOfEn() + "T'";
		DBAccess.RunSQL(sql);

		//删除相关的图片信息.
		if (DBAccess.IsExitsTableCol("Sys_FrmImg", "KeyOfEn") == true)
		{
			sql = "DELETE FROM Sys_FrmImg WHERE FK_MapData='" + this.getFK_MapData() + "' AND KeyOfEn='" + this.getKeyOfEn() + "T'";
		}
		DBAccess.RunSQL(sql);

		//删除相对应的rpt表中的字段
		if (this.getFK_MapData().contains("ND") == true)
		{
			String fk_mapData = this.getFK_MapData().substring(0, this.getFK_MapData().length() - 2) + "Rpt";
			sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND( KeyOfEn='" + this.getKeyOfEn() + "T' OR KeyOfEn='" + this.getKeyOfEn() + "')";
			DBAccess.RunSQL(sql);
		}

		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterDelete();
	}


	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		mapAttr.RetrieveFromDBSources();
		mapAttr.Update();

		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}


		///


		///基本功能.

	public final String DoRenameField(String newField) throws Exception
	{
		String sql = "";
		if (this.getFK_MapData().indexOf("ND") == 0)
		{
			String strs = this.getFK_MapData().replace("ND", "");
			strs = strs.substring(0, strs.length() - 2);

			String rptTable = "ND" + strs + "Rpt";
			MapDatas mds = new MapDatas();
			mds.Retrieve(MapDataAttr.PTable, rptTable);

			for (MapData item : mds.ToJavaList())
			{
				sql = "UPDATE Sys_MapAttr SET KeyOfEn='" + newField + "',  MyPK='" + item.getNo() + "_" + newField + "' WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + item.getNo() + "'";
				DBAccess.RunSQL(sql);
			}
		}
		else
		{
			sql = "UPDATE Sys_MapAttr SET KeyOfEn='" + newField + "', MyPK='" + this.getFK_MapData() + "_" + newField + "'  WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + this.getFK_MapData() + "'";
			DBAccess.RunSQL(sql);
		}

		return "重名称成功,如果是自由表单，请关闭表单设计器重新打开.";
	}
	/** 
	 绑定函数
	 
	 @return 
	*/
	public final String BindFunction()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 快速录入
	 
	 @return 
	*/
	public final String DoFastEnter()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/FastInput.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 快速录入
	 
	 @return 
	*/
	public final String DoPop2019()throws Exception
	{
		return "../../Admin/FoolFormDesigner/Pop/Default.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 设置常用字段
	 
	 @return 
	*/
	public final String DoGeneralField()throws Exception
	{
		return "../../Admin/FoolFormDesigner/General/GeneralField.htm?FrmID=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 全局默认值
	 
	 @return 
	*/
	public final String DoDefVal()throws Exception
	{
		return "../../Admin/FoolFormDesigner/DefVal.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}

		///


		///方法执行 Pop自动完成.
	/** 
	 简单列表模式
	 
	 @return 
	*/
	public final String DoPopFullCtrl()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/PopFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + this.getMyPK();
	}
	/** 
	 多条件查询列表模式
	 
	 @return 
	*/
	public final String DoPopFullCtrlAdv()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/PopFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + this.getMyPK();
	}

		/// 方法执行 Pop填充自动完成.


		///方法执行.
	/** 
	 设置签批组件
	 
	 @return 执行结果
	*/
	public final String DoSetCheck()throws Exception
	{
		this.setUIContralType(UIContralType.SignCheck);
		this.setUIIsEnable(false);
		this.setUIVisible(false);
		this.Update();
		return "设置成功,当前文本框已经是签批组件了,请关闭掉当前的窗口.";
	}

	public final String DoSetFlowBBS()throws Exception
	{
		MapAttrs mapAttrs = new MapAttrs();
		mapAttrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.UIContralType, getUIContralType().FlowBBS.getValue());
		if (mapAttrs.size() == 0)
		{
			this.setUIContralType(UIContralType.FlowBBS);
			this.setUIIsEnable(false);
			this.setUIVisible(false);
			this.Update();
			return "设置成功,当前文本框已经是评论组件了,请关闭掉当前的窗口.";
		}

		return "表单中只能存在一个评论组件，表单" + this.getFK_MapData() + "已经存在评论组件不能再增加";
	}
	/** 
	 批处理
	 
	 @return 
	*/
	public final String DoEleBatch()throws Exception
	{
		//return "../../Admin/FoolFormDesigner/EleBatch.aspx?EleType=MapAttr&KeyOfEn=" + this.KeyOfEn + "&FType=1&MyPK=" + this.MyPK + "&FK_MapData=" + this.FK_MapData;
		return "../../Admin/FoolFormDesigner/EleBatch.htm?EleType=MapAttr&KeyOfEn=" + this.getKeyOfEn() + "&FType=1&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}
	/** 
	 小范围多选
	 
	 @return 
	*/
	public final String DoMultipleChoiceSmall()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/MultipleChoiceSmall.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";
	}
	/** 
	 大范围多选
	 
	 @return 
	*/
	public final String DoMultipleChoiceSearch()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/MultipleChoiceSearch.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";
	}

	public final String DoFastInput()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/MultipleInputSearch.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";

	}
	/** 
	 超链接
	 
	 @return 
	*/
	public final String DoLink()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/Link.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK() + "&FK_MapExt=Link_" + this.getFK_MapData() + "_" + this.getKeyOfEn();
	}
	/** 
	 设置开窗返回值
	 
	 @return 
	*/
	public final String DoPopVal()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/PopVal.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK() + "&FK_MapExt=PopVal_" + this.getFK_MapData() + "_" + this.getKeyOfEn();
	}
	/** 
	 正则表达式
	 
	 @return 
	*/
	public final String DoRegularExpression()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpression.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}

	/** 
	 文本框自动完成
	 
	 @return 
	*/
	public final String DoTBFullCtrl2019()throws Exception
	{
		return "../../Admin/FoolFormDesigner/TBFullCtrl/Default.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + this.getMyPK();
	}

	/** 
	 设置级联
	 
	 @return 
	*/
	public final String DoInputCheck()throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/InputCheck.htm?FK_MapData=" + this.getFK_MapData() + "&OperAttrKey=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK() + "&DoType=New&ExtType=InputCheck";
	}
	/** 
	 扩展控件
	 
	 @return 
	*/
	public final String DoEditFExtContral()throws Exception
	{
		return "../../Admin/FoolFormDesigner/EditFExtContral.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}
	/** 
	 扩展控件2019
	 
	 @return 
	*/
	public final String DoEditFExtContral2019()throws Exception
	{
		return "../../Admin/FoolFormDesigner/EditFExtContral/Default.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}

		/// 方法执行.


		///重载.
	@Override
	protected boolean beforeUpdateInsertAction()throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//高度.
		//  attr.UIHeightInt = this.GetValIntByKey("ExtRows") * 23;

		attr.setIsRichText(this.GetValBooleanByKey(MapAttrAttr.IsRichText)); //是否是富文本？
		attr.setIsSupperText(this.GetValIntByKey(MapAttrAttr.IsSupperText)); //是否是大块文本？

		if (attr.getIsRichText() || attr.getIsSupperText() == 1)
		{
			attr.setMaxLen(4000);
			this.SetValByKey(MapAttrAttr.MaxLen, 4000);
		}



			///自动扩展字段长度. 需要翻译.
		if (attr.getMaxLen() < this.getMaxLen())
		{
			attr.setMaxLen(this.getMaxLen());

			String sql = "";
			MapData md = new MapData();
			md.setNo(this.getFK_MapData());
			if (md.RetrieveFromDBSources() == 1)
			{
				if (DBAccess.IsExitsTableCol(md.getPTable(), this.getKeyOfEn()) == true)
				{
					if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
					{
						sql = "ALTER TABLE " + md.getPTable() + " ALTER column " + this.getKeyOfEn() + " NVARCHAR(" + attr.getMaxLen() + ")";
					}

					if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
					{
						sql = "ALTER table " + md.getPTable() + " modify " + attr.getField() + " NVARCHAR(" + attr.getMaxLen() + ")";
					}

					if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.DM)
					{
						sql = "ALTER table " + md.getPTable() + " modify " + attr.getField() + " NVARCHAR2(" + attr.getMaxLen() + ")";
					}

					if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
					{
						sql = "ALTER table " + md.getPTable() + " alter " + attr.getField() + " type character varying(" + attr.getMaxLen() + ")";
					}

					DBAccess.RunSQL(sql); //如果是oracle如果有nvarchar与varchar类型，就会出错.
				}
			}
		}

			/// 自动扩展字段长度.


		//默认值.
		String defval = this.GetValStrByKey("ExtDefVal");
		if (defval.equals("") || defval.equals("0"))
		{
			String defVal = this.GetValStrByKey("DefVal");
			if (defval.contains("@") == true)
			{
				this.SetValByKey("DefVal", "");
			}
		}
		else
		{
			this.SetValByKey("DefVal", this.GetValByKey("ExtDefVal"));
		}

		//执行保存.
		attr.Save();

		if (this.GetValStrByKey("GroupID").equals("无"))
		{
			this.SetValByKey("GroupID", "0");
		}

		return super.beforeUpdateInsertAction();
	}

		///
}