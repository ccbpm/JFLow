package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.*;
import bp.sys.*;
import java.util.*;

/**
 * 按钮
 */
public class FrmBtn extends EntityMyPK {

	/// 属性
	public final String getFK_MapData() throws Exception {
		return this.GetValStrByKey(FrmBtnAttr.FK_MapData);
	}

	///

	/// 构造方法
	/**
	 * 按钮
	 */
	public FrmBtn() {
	}

	/**
	 * 按钮
	 * 
	 * @param mypk
	 */
	public FrmBtn(String mypk) throws Exception {
		this.setMyPK(mypk);
		this.Retrieve();
	}

	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap() throws Exception {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("Sys_MapAttr", "按钮");
		map.IndexField = MapAttrAttr.FK_MapData;

		map.AddMyPK(false);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", true, true, 0, 100, 20);
		map.AddTBString(MapAttrAttr.Name, null, "按钮标签(文字)", true, false, 0, 50, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "按钮ID", true, true, 0, 50, 20);

		map.AddDDLSysEnum(MapAttrAttr.UIIsEnable, 0, "事件类型", true, true, FrmBtnAttr.EventType,
				"@0=禁用@1=执行URL@2=执行CCFromRef.js");
		// map.AddBoolean(FrmBtnAttr.UIIsEnable, true, "是否可用", true, true);

		// 文本跨行
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);

		// 单元格数量 2013-07-24 增加.
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "TextBox单元格数量", true, true, "ColSpanAttrDT",
				"@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨5个单元格@6=跨6个单元格");
		map.SetHelperAlert(MapAttrAttr.ColSpan, "对于傻瓜表单有效: 标识该字段TextBox横跨的宽度,占的单元格数量.");

		// 文本占单元格数量
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "Label单元格数量", true, true, "ColSpanAttrString",
				"@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨6个单元格@6=跨6个单元格");
		map.SetHelperAlert(MapAttrAttr.TextColSpan, "对于傻瓜表单有效: 标识该字段Lable，标签横跨的宽度,占的单元格数量.");

		// map.AddTBString(FrmBtnAttr.UACContext, null, "控制内容", false, false, 0, 3900,
		// 20);
		// map.AddDDLSysEnum(FrmBtnAttr.EventType, 0, "事件类型", true, true,
		// FrmBtnAttr.EventType,
		// "@0=禁用@1=执行URL@2=执行CCFromRef.js");
		// 显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "所在分组",
				"SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'", true);

		map.AddTBStringDoc(MapAttrAttr.Tag, null, "事件内容", true, false);
		map.SetHelperAlert(MapAttrAttr.Tag, "可以写JS，js可以调用在DataUser下JSLab下xxx_Self.js 函数.");

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception {
		// 在属性实体集合插入前，clear父实体的缓存.
		bp.sys.Glo.ClearMapDataAutoNum(this.getFK_MapData());

		bp.sys.FrmBtn frmBtn = new bp.sys.FrmBtn();
		frmBtn.setMyPK(this.getMyPK());
		frmBtn.RetrieveFromDBSources();
		frmBtn.Update();

		// 调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

	/**
	 * 删除后清缓存
	 */
	@Override
	protected void afterDelete() throws Exception {
		// @sly
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		if (mapAttr.RetrieveFromDBSources() != 0) {
			mapAttr.Delete();
		}

		// 调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}

	///
}