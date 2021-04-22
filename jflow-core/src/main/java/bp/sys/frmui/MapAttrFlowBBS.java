package bp.sys.frmui;

import bp.en.*;
import bp.en.Map;
import bp.sys.*;;

/** 
 评论(抄送)组件
*/
public class MapAttrFlowBBS extends EntityMyPK
{
	private static final long serialVersionUID = 1L;

	///文本字段参数属性.
	/** 
	 表单ID
	 * @throws Exception 
	*/
	public final String getFK_MapData() throws Exception
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
	public MapAttrFlowBBS()
	{
	}
	/** 
	 实体属性
	*/
	public MapAttrFlowBBS(String myPK)throws Exception
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

		Map map = new Map("Sys_MapAttr", "评论(抄送)组件");



			///基本字段信息.
		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);
		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20, true);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20, true);

		map.AddTBInt(MapAttrAttr.MinLen, 0, "最小长度", true, false);
		map.AddTBInt(MapAttrAttr.MaxLen, 50, "最大长度", true, false);
		map.SetHelperAlert(MapAttrAttr.MaxLen, "定义该字段的字节长度.");


		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.SetHelperAlert(MapAttrAttr.UIWidth, "对自由表单,从表有效,显示文本框的宽度.");

		map.AddTBInt(MapAttrAttr.UIContralType, 0, "控件", false, false);

			/**map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
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
			*/
		map.AddDDLSQL(MapAttrAttr.CSSCtrl, "0", "自定义样式", MapAttrString.getSQLOfCSSAttr(), true);

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


		map.AddTBInt(MapAttrAttr.Idx, 0, "顺序号", true, false);
		map.SetHelperAlert(MapAttrAttr.Idx, "对傻瓜表单有效:用于调整字段在同一个分组中的顺序.");


			/// 傻瓜表单

		this.set_enMap(map);
		return this.get_enMap();
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


		///重载.
	@Override
	protected boolean beforeUpdateInsertAction()throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//强制设置为评论组件.
		this.setUIContralType(UIContralType.FlowBBS);

		if (this.GetValStrByKey("GroupID").equals("无"))
		{
			this.SetValByKey("GroupID", "0");
		}

		return super.beforeUpdateInsertAction();
	}

		///
}