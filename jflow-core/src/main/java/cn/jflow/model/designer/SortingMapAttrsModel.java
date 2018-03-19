package cn.jflow.model.designer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataRowCollection;
import BP.DA.DataTable;
import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmBtn;
import BP.Sys.FrmBtns;
import BP.Sys.GroupCtrlType;
import BP.Sys.GroupField;
import BP.Sys.GroupFieldAttr;
import BP.Sys.GroupFields;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Tools.StringHelper;
import BP.WF.Nodes;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.ListItem;
 
/**
 * 手机表单中的字段排序功能
 */
public class SortingMapAttrsModel extends BaseModel {
	public SortingMapAttrsModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		this.request=request;
		this.response=response;
	}

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	public StringBuilder pub1 = new StringBuilder();

	// /#region 排序按钮控件ID拆解对象

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 排序按钮控件ID拆解对象
	private static class MoveBtnIds {
		/**
		 * 排序按钮控件ID拆解对象
		 * 
		 * @param btnId
		 *            排序按钮控件ID，由8部分组成，每部分由_连接，如：Btn_Group_Up_4_1_2_1_186
		 *            <p>
		 *            各部分含义：
		 *            </p>
		 *            <p>
		 *            1.控件命名前缀，此处为Btn
		 *            </p>
		 *            <p>
		 *            2.移动对象类型，Group[字段分组]或Attr[字段]
		 *            </p>
		 *            <p>
		 *            3.移动方向，Up[上移]或Down[下移]
		 *            </p>
		 *            <p>
		 *            4.所有参与排序的记录总数
		 *            </p>
		 *            <p>
		 *            5.当前记录所处索引号，从1开始
		 *            </p>
		 *            <p>
		 *            6.当前记录原记录的索引号，从1开始
		 *            </p>
		 *            <p>
		 *            7.如果是字段移动，此处为该字段所处分组的索引号；分组移动时，此处为0
		 *            </p>
		 *            <p>
		 *            8.当前记录的主键值
		 *            </p>
		 */
		public MoveBtnIds(String btnId) {
			setControlId(btnId);

			String[] ids = getControlId().split("[_]", -1);
			if (ids.length < 8) {
				setSuccess(false);
				return;
			}

			try {
				setCtrlPrefix(ids[0]);
				setMoveDirection(ids[2]);
				setObjectType(ids[1]);
				setAllCount(Integer.parseInt(ids[3]));
				setIdx(Integer.parseInt(ids[4]));
				setOldIdx(Integer.parseInt(ids[5]));
				setGroupIdx(Integer.parseInt(ids[6]));
				setKey(getControlId().substring(
						getCtrlPrefix().length()
								+ getObjectType().length()
								+ getMoveDirection().length()
								+ (new Integer(getAllCount())).toString()
										.length()
								+ (new Integer(getIdx())).toString().length()
								+ (new Integer(getOldIdx())).toString()
										.length()
								+ (new Integer(getGroupIdx())).toString()
										.length() + 7));
				setSuccess(true);
			} catch (java.lang.Exception e) {
				setSuccess(false);
			}
		}

		/**
		 * 获取控件ID
		 */
		private String privateControlId;

		public final String getControlId() {
			return privateControlId;
		}

		private void setControlId(String value) {
			privateControlId = value;
		}

		/**
		 * 获取控件前缀
		 */
		private String privateCtrlPrefix;

		public final String getCtrlPrefix() {
			return privateCtrlPrefix;
		}

		private void setCtrlPrefix(String value) {
			privateCtrlPrefix = value;
		}

		/**
		 * 获取排序移动类型，Group或Attr
		 */
		private String privateObjectType;

		public final String getObjectType() {
			return privateObjectType;
		}

		private void setObjectType(String value) {
			privateObjectType = value;
		}

		/**
		 * 获取移动方向，Down或Up
		 */
		private String privateMoveDirection;

		public final String getMoveDirection() {
			return privateMoveDirection;
		}

		private void setMoveDirection(String value) {
			privateMoveDirection = value;
		}

		/**
		 * 获取所有参与排序的记录总数
		 */
		private int privateAllCount;

		public final int getAllCount() {
			return privateAllCount;
		}

		private void setAllCount(int value) {
			privateAllCount = value;
		}

		/**
		 * 获取当前记录所处索引号，从1开始
		 */
		private int privateIdx;

		public final int getIdx() {
			return privateIdx;
		}

		private void setIdx(int value) {
			privateIdx = value;
		}

		/**
		 * 获取当前记录原记录的索引号，从1开始
		 */
		private int privateOldIdx;

		public final int getOldIdx() {
			return privateOldIdx;
		}

		private void setOldIdx(int value) {
			privateOldIdx = value;
		}

		/**
		 * 获取字段所处分组的索引号，字段排序时有效
		 */
		private int privateGroupIdx;

		public final int getGroupIdx() {
			return privateGroupIdx;
		}

		private void setGroupIdx(int value) {
			privateGroupIdx = value;
		}

		/**
		 * 获取当前记录的主键值
		 */
		private String privateKey;

		public final String getKey() {
			return privateKey;
		}

		private void setKey(String value) {
			privateKey = value;
		}

		/**
		 * 获取本对象是否转换成功
		 */
		private boolean privateSuccess;

		public final boolean getSuccess() {
			return privateSuccess;
		}

		private void setSuccess(boolean value) {
			privateSuccess = value;
		}
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion

	public final String getFK_MapData() {
		return request.getParameter("FK_MapData");
	}

	public final String getFK_Flow() {
		return request.getParameter("FK_Flow");
	}

	private MapDatas mapdatas;
	private MapAttrs attrs;
	private GroupFields groups;
	private MapDtls dtls;
	private FrmAttachments athMents;
	private FrmBtns btns;

	public void Page_Load() {
		
		// /#region 检验登录（只有admin能进行排序），与传参
		if (!"admin".equals(WebUser.getNo())) {
			BP.WF.Dev2Interface.Port_Login("admin");
			// //Response.Write("<h3>重新点击左侧超链接！</h3>");
			return;
		}
		if (StringHelper.isNullOrEmpty(getFK_MapData())) {
			// //response.("<h3>FK_MapData参数错误！</h3>");
			return;
		}		
		// /#endregion		
		// /#region 获取数据
		mapdatas = new MapDatas();
		QueryObject qo = new QueryObject(mapdatas);
		qo.AddWhere(MapDataAttr.No, "Like", getFK_MapData() + "%");
		qo.addOrderBy(MapDataAttr.Idx);
		qo.DoQuery();

		attrs = new MapAttrs();
		qo = new QueryObject(attrs);
		qo.AddWhere(MapAttrAttr.FK_MapData, getFK_MapData());
		qo.addAnd();
		qo.AddWhere(MapAttrAttr.UIVisible, true);
		qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
		qo.DoQuery();

		athMents = new FrmAttachments(this.getFK_MapData());
		dtls = new MapDtls(this.getFK_MapData());

		groups = new GroupFields();
		qo = new QueryObject(groups);
		qo.AddWhere(GroupFieldAttr.EnName, getFK_MapData());
		qo.addOrderBy(GroupFieldAttr.Idx);
		qo.DoQuery();
		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#endregion

		this.BindData();
	}

	/**
	 * 加载数据
	 */
	private void BindData() {
		Object tempVar = mapdatas.GetEntityByKey(getFK_MapData());
		MapData mapdata = (MapData) ((tempVar instanceof MapData) ? tempVar
				: null);
		DataTable dt_Attr = attrs.ToDataTableField("dtAttrs");
		DataTable dt_Dtls = dtls.ToDataTableField("dtDtls");
		DataTable dtGroups = groups.ToDataTableField("dtGroups");
		DataTable dtNoGroupAttrs = null;
		List<DataRow> rows_Attrs = null;
		LinkButton btn = null;
		DDL ddl = null;
		int idx_Attr = 1;
		int gidx = 1;
		GroupField group = null;

		if (mapdata != null) {
			// C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could not
			// resolve the named parameters in the following line:
			// ORIGINAL LINE: pub1.AddEasyUiPanelInfoBegin(mapdata.Name + "[" +
			// mapdata.No + "]字段排序", padding: 5);
			 
//			pub1.append(AddEasyUiPanelInfoBegin(mapdata.getName() + "["
//					+ mapdata.getNo() + "]字段排序", "",5));
			pub1.append(AddTable("class='Table' border='0' cellpadding='0' cellspacing='0' style='width:100%'"));

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region 标题行

			pub1.append(AddTR());
			pub1.append(AddTDGroupTitle("style='width:40px;text-align:center'",
					"序"));
			pub1.append(AddTDGroupTitle("style='width:100px;'", "字段名称"));
			pub1.append(AddTDGroupTitle("style='width:160px;'", "中文描述"));
			pub1.append(AddTDGroupTitle("style='width:160px;'", "字段分组"));
			pub1.append(AddTDGroupTitle("字段排序"));
			pub1.append(AddTDGroupTitle("是否显示"));
			pub1.append(AddTREnd());

			
			// /#endregion
			// 检索全部字段，查找出没有分组或分组信息不正确的字段，存入"无分组"集合
			dtNoGroupAttrs = dt_Attr.clone();

			for (DataRow dr : dt_Attr.Rows) {
				if (IsExistInDataRowArray(dtGroups.Rows, GroupFieldAttr.OID,
						dr.getValue(MapAttrAttr.GroupID)) == false) {
					dtNoGroupAttrs.Rows.Add(dr.getValue("ItemArray"));
				}
			}
			// 未分组明细表,自动创建一个
			for (MapDtl mapDtl : dtls.ToJavaList()) {
				if (GetGroupID(mapDtl.getNo(), groups) == 0) {
					group = new GroupField();
					group.setLab(mapDtl.getName());
					group.setEnName(mapDtl.getFK_MapData());
					group.setCtrlType(GroupCtrlType.Dtl);
					group.setCtrlID(mapDtl.getNo());
					group.Insert();

					groups.AddEntity(group);
				}
			}
			// 未分组多附件自动分配一个
			for (FrmAttachment athMent : athMents.ToJavaList()) {
				if (GetGroupID(athMent.getMyPK(), groups) == 0) {
					group = new GroupField();
					group.setLab(athMent.getName());
					group.setEnName(athMent.getFK_MapData());
					group.setCtrlType(GroupCtrlType.Ath);
					group.setCtrlID(athMent.getMyPK());
					group.Insert();

					groups.AddEntity(group);
				}
			}
			
			  //03、未分组按钮自动创建一个
			for (FrmBtn fbtn : btns.ToJavaList())
			{
				if (GetGroupID(fbtn.getMyPK(), groups) == 0)
				{
					group = new GroupField();
					group.setLab(fbtn.getText());
					group.setEnName(fbtn.getFK_MapData());
					group.setCtrlType(GroupCtrlType.Btn);
					group.setCtrlID(fbtn.getMyPK());
					group.Insert();

					fbtn.setGroupID((int)group.getOID());
					fbtn.Update();

					groups.AddEntity(group);
				}
			}
			

			for (DataRow drGrp : dtGroups.Rows) {
				// 分组中的字段
				rows_Attrs = dt_Attr.select(String.format("FK_MapData = '%1$s' AND GroupID = %2$s",
				getFK_MapData(), drGrp.getValue("OID")));

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 分组行

				pub1.append(AddTR());
				pub1.append(AddTDBegin("colspan='5' class='GroupTitle'"));

				if (gidx > 1) {
					btn = new LinkButton(false, "Btn_Group_Up_"
							+ dtGroups.Rows.size() + "_" + gidx + "_"
							+ drGrp.getValue("Idx") + "_0_"
							+ drGrp.getValue("OID"), "上移");
					btn.SetDataOption("iconCls", "'icon-up'");
					// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
					// C#-style event wireups:
					//btn.Click += btn_Click;
					btn.addAttr("onclick", "btn_Click");
					pub1.append(Add(btn));
				}

				if (gidx < dtGroups.Rows.size()) {
					btn = new LinkButton(false, "Btn_Group_Down_"
							+ dtGroups.Rows.size() + "_" + gidx + "_"
							+ drGrp.getValue("Idx") + "_0_"
							+ drGrp.getValue("OID"), "下移");
					btn.SetDataOption("iconCls", "'icon-down'");
					// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
					// C#-style event wireups:
					//btn.Click += btn_Click;
					btn.addAttr("onclick", "btn_Click");
					pub1.append(Add(btn));
				}

				pub1.append(AddSpace(1));
				pub1.append(Add("<a href=\"javascript:GroupField('"
						+ this.getFK_MapData() + "','" + drGrp.getValue("OID")
						+ "')\" >分组：" + drGrp.getValue("Lab") + "</a>"));

				pub1.append(AddTDEnd());
				pub1.append(AddTREnd());

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				idx_Attr = 1;

				for (DataRow row : rows_Attrs) {
					// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor
					// in Java:
					// /#region 字段行

					ddl = new DDL();
					ddl.setId("DDL_Group_" + drGrp.getValue(GroupFieldAttr.OID)
							+ "_" + row.getValue(MapAttrAttr.KeyOfEn));

					for (DataRow rowGroup : dtGroups.select("CTRLTYPE = ''")) {
						ddl.Items.add(new ListItem(rowGroup.getValue(
								GroupFieldAttr.Lab).toString(), rowGroup
								.getValue(GroupFieldAttr.OID).toString()));
					}

					// //ddl.setAutoPostBack(true);
					// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
					// C#-style event wireups:
					// //ddl.SelectedIndexChanged += ddl_SelectedIndexChanged;
					ddl.SetSelectItem(Integer.parseInt(drGrp.getValue(
							GroupFieldAttr.OID).toString()));// //

					pub1.append(AddTR());
					pub1.append(AddTD("style='text-align:center'",
							(new Integer(idx_Attr)).toString()));
					pub1.append(AddTD(String
							.format("<a href='javascript:ShowEditWindow(\"%1$s\",\"%2$s\")'>%3$s</a>",
									row.getValue(MapAttrAttr.Name),
									GenerateEditUrl(row),
									row.getValue(MapAttrAttr.KeyOfEn))));
					pub1.append(AddTD(row.getValue(MapAttrAttr.Name).toString()));
					pub1.append(AddTD(ddl));
					pub1.append(AddTDBegin());

					if (idx_Attr > 1) {
						btn = new LinkButton(false, "Btn_Attr_Up_"
								+ rows_Attrs.size() + "_" + idx_Attr + "_"
								+ row.getValue("Idx") + "_" + gidx + "_"
								+ row.getValue("KeyOfEn"), "上移");
						btn.SetDataOption("iconCls", "'icon-up'");
						// C# TO JAVA CONVERTER TODO TASK: Java has no
						// equivalent to C#-style event wireups:
						//btn.Click += btn_Click;
						btn.addAttr("onclick", "btn_Click");
						pub1.append(Add(btn));
					}

					if (idx_Attr < rows_Attrs.size()) {
						btn = new LinkButton(false, "Btn_Attr_Down_"
								+ rows_Attrs.size() + "_" + idx_Attr + "_"
								+ row.getValue("Idx") + "_" + gidx + "_"
								+ row.getValue("KeyOfEn"), "下移");
						btn.SetDataOption("iconCls", "'icon-down'");
						// C# TO JAVA CONVERTER TODO TASK: Java has no
						// equivalent to C#-style event wireups:
						//btn.Click += btn_Click;
						btn.addAttr("onclick", "btn_Click");
						pub1.append(Add(btn));
					}

					pub1.append(AddTDEnd());
					pub1.append(AddTREnd());

					// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor
					// in Java:
					// /#endregion

					idx_Attr++;
				}

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 多附件行
				java.util.ArrayList<FrmAttachment> groupOfAthMents = new java.util.ArrayList<FrmAttachment>();
				for (FrmAttachment athMent : athMents.ToJavaList()) {
					if (athMent.getIsVisable() == false) {
						continue;
					}
					if (!(new Integer(GetGroupID(athMent.getMyPK(), groups)))
							.toString()
							.equals(drGrp.getValue("OID").toString())) {
						continue;
					}
					groupOfAthMents.add(athMent);
				}
				// 此分组存在多附件
				if (groupOfAthMents.size() > 0) {
					GroupAddAthMent(groupOfAthMents);
				}
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 明细表行
				java.util.ArrayList<MapDtl> groupOfDtls = new java.util.ArrayList<MapDtl>();
				for (MapDtl mapDtl : dtls.ToJavaList()) {
					if (!(new Integer(GetGroupID(mapDtl.getNo(), groups)))
							.toString()
							.equals(drGrp.getValue("OID").toString())) {
						continue;
					}
					groupOfDtls.add(mapDtl);
				}
				// 此分组存在明细表
				if (groupOfDtls.size() > 0) {
					GroupAddDtl(groupOfDtls);
				}
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				// 如果此分组下没有字段，则显示无字段消息
				if (rows_Attrs.size() == 0 && groupOfAthMents.isEmpty()
						&& groupOfDtls.isEmpty()) {
					// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor
					// in Java:
					// /#region 该分组下面没有任何字段
					pub1.append(AddTR());
					pub1.append(AddTDBegin("colspan='5' style='color:red'"));
					pub1.append(AddSpace(1));
					pub1.append(Add("该分组下面没有任何字段"));
					pub1.append(AddTDEnd());
					pub1.append(AddTREnd());
					// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor
					// in Java:
					// /#endregion
				}
				gidx++;
			}

			// 如果含有未分组字段，则显示在下方
			if (dtNoGroupAttrs.Rows.size() > 0) {
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 分组行
				pub1.append(AddTR());
				pub1.append(AddTDBegin("colspan='5' class='GroupTitle'"));
				pub1.append(AddSpace(1));
				pub1.append(Add("未分组字段"));
				pub1.append(AddTDEnd());
				pub1.append(AddTREnd());
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				idx_Attr = 1;

				for (DataRow row : dtNoGroupAttrs.Rows) {
					// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor
					// in Java:
					// /#region 字段行

					ddl = new DDL();
					ddl.setId("DDL_Group_"
							+ (((row.getValue(MapAttrAttr.GroupID)) != null) ? row
									.getValue(MapAttrAttr.GroupID) : "") + "_"
							+ row.getValue(MapAttrAttr.KeyOfEn));
					ddl.Items.add(new ListItem("请选择分组", ""));

					for (DataRow rowGroup : dtGroups.Rows) {
						ddl.Items.add(new ListItem(rowGroup.getValue(
								GroupFieldAttr.Lab).toString(), rowGroup
								.getValue(GroupFieldAttr.OID).toString()));
					}

					// //ddl.AutoPostBack = true;
					// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
					// C#-style event wireups:
					// //ddl.SelectedIndexChanged += ddl_SelectedIndexChanged;

					pub1.append(AddTR());
					pub1.append(AddTD("style='text-align:center'",
							(new Integer(idx_Attr)).toString()));
					pub1.append(AddTD(String
							.format("<a href='javascript:ShowEditWindow(\"%1$s\",\"%2$s\")'>%3$s</a>",
									row.getValue(MapAttrAttr.Name),
									GenerateEditUrl(row),
									row.getValue(MapAttrAttr.KeyOfEn))));
					pub1.append(AddTD(row.getValue(MapAttrAttr.Name).toString()));
					pub1.append(AddTD(ddl));
					pub1.append(AddTD("&nbsp;"));
					pub1.append(AddTREnd());

					// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor
					// in Java:
					// /#endregion

					idx_Attr++;
				}
			}

			pub1.append(AddTableEnd());
			pub1.append(AddEasyUiPanelInfoEnd());
			pub1.append(AddBR());

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region //检测是否含有明细表，与分组不对应的明细表做排序
			if (dtls.size() < 0) {
				// C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could
				// not resolve the named parameters in the following line:
				// ORIGINAL LINE:
				// pub1.append(AddEasyUiPanelInfoBegin("未分组明细表排序", padding: 5);
				pub1.append(AddEasyUiPanelInfoBegin("未分组明细表排序", "",5));
				pub1.append(AddTable("class='Table' border='0' cellpadding='0' cellspacing='0' style='width:100%'"));

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 标题行

				pub1.append(AddTR());
				pub1.append(AddTDGroupTitle(
						"style='width:40px;text-align:center'", "序"));
				pub1.append(AddTDGroupTitle("style='width:100px;'", "明细表编号"));
				pub1.append(AddTDGroupTitle("style='width:160px;'", "中文名称"));
				pub1.append(AddTDGroupTitle("排序"));
				pub1.append(AddTREnd());

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				idx_Attr = 1;

				for (MapDtl dtl : dtls.ToJavaList()) {
					// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor
					// in Java:
					// /#region 明细表排序

					pub1.append(AddTR());
					pub1.append(AddTD("style='text-align:center'",
							(new Integer(idx_Attr)).toString()));
					pub1.append(AddTD("<a href=\"javascript:EditDtl('"
							+ this.getFK_MapData() + "','" + dtl.getNo()
							+ "')\" >" + dtl.getNo() + "</a>"));
					pub1.append(AddTD(dtl.getName()));
					pub1.append(AddTDBegin());

					if (idx_Attr > 1) {
						btn = new LinkButton(false, "Btn_Dtl_Up_" + dtls.size()
								+ "_" + idx_Attr + "_" + dtl.getRowIdx()
								+ "_0_" + dtl.getNo(), "上移");
						btn.SetDataOption("iconCls", "'icon-up'");
						// C# TO JAVA CONVERTER TODO TASK: Java has no
						// equivalent to C#-style event wireups:
						// //btn.Click += btn_Click;
						btn.addAttr("onclick", "btn_Click");
						pub1.append(Add(btn));
					}

					if (idx_Attr < dtls.size()) {
						btn = new LinkButton(false, "Btn_Dtl_Down_"
								+ dtls.size() + "_" + idx_Attr + "_"
								+ dtl.getRowIdx() + "_0_" + dtl.getNo(), "下移");
						btn.SetDataOption("iconCls", "'icon-down'");
						// C# TO JAVA CONVERTER TODO TASK: Java has no
						// equivalent to C#-style event wireups:
						//btn.Click += btn_Click;
						btn.addAttr("onclick", "btn_Click");
						pub1.append(Add(btn));
					}

					pub1.append(AddSpace(1));
					String tempVar2 = getFK_Flow();
					pub1.append(Add(String
							.format("<a href='%1$s' target='_self' class='easyui-linkbutton' data-options=\"iconCls:'icon-sheet'\">字段排序</a>",
									request.getPathInfo()
											+ "?FK_Flow="
											+ ((tempVar2 != null) ? tempVar2
													: "")
											+ "&FK_MapData="
											+ dtl.getNo()
											+ "&t="
											+ getCurentDate("yyyyMMddHHmmssffffff"))));

					pub1.append(AddTDEnd());
					pub1.append(AddTREnd());

					// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor
					// in Java:
					// /#endregion

					idx_Attr++;
				}

				pub1.append(AddTableEnd());
				pub1.append(AddEasyUiPanelInfoEnd());
				pub1.append(AddBR());
			}
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region //如果是明细表的字段排序，则增加"返回"按钮；否则增加"复制排序"按钮,2016-03-21

			MapDtl tdtl = new MapDtl();
			tdtl.setNo(getFK_MapData());
			if (tdtl.RetrieveFromDBSources() == 1) {
				String tempVar3 = getFK_Flow();
				pub1.append(Add(String
						.format("<a href='%1$s' target='_self' class='easyui-linkbutton' data-options=\"iconCls:'icon-back'\">返回</a>",
								request.getPathInfo() + "?FK_Flow="
										+ ((tempVar3 != null) ? tempVar3 : "")
										+ "&FK_MapData=" + tdtl.getFK_MapData()
										+ "&t="
										+ getCurentDate("yyyyMMddHHmmssffffff"))));
			} else {
				btn = new LinkButton(false, "Btn_ResetAttr_Idx", "重置顺序");
				btn.SetDataOption("iconCls", "'icon-reset'");
				// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
				// C#-style event wireups:
				//btn.Click += btnReSet_Click;
				btn.addAttr("onclick", "btn_Click");
				pub1.append(Add(btn));
				pub1.append(Add("<a href='javascript:void(0)' onclick=\"Form_View('"
						+ this.getFK_MapData()
						+ "','"
						+ this.getFK_Flow()
						+ "');\" class='easyui-linkbutton' data-options=\"iconCls:'icon-search'\">预览</a>"));
				pub1.append(Add("<a href='javascript:void(0)' onclick=\"$('#nodes').dialog('open');\" class='easyui-linkbutton' data-options=\"iconCls:'icon-copy'\">复制排序</a>"));
				pub1.append(Add("&nbsp;<a href='javascript:void(0)' onclick=\"GroupFieldNew('"
						+ this.getFK_MapData()
						+ "')\" class='easyui-linkbutton' data-options=\"iconCls:'icon-addfolder'\">新建分组</a>"));
				pub1.append(AddBR());
				pub1.append(AddBR());

				pub1.append(Add("<div id='nodes' class='easyui-dialog' data-options=\"title:'选择复制到节点（多选）:',closed:true,buttons:'#btns'\" style='width:280px;height:340px'>"));

				ListBox lb = new ListBox();
				// //lb.Style.add("width", "100%");
				// //lb.Style.Add("Height", "100%");
				lb.setSelectionMode(ListSelectionMode.Multiple);
				;
				// //lb.BorderStyle = BorderStyle.None;
				lb.setId("lbNodes");

				Nodes nodes = new Nodes();
				nodes.Retrieve(BP.WF.Template.NodeAttr.FK_Flow, getFK_Flow(),
						BP.WF.Template.NodeAttr.Step);

				if (nodes.size() == 0) {
					String nodeid = getFK_MapData().replace("ND", "");
					String flowno = "";

					if (nodeid.length() > 2) {
						flowno = nodeid.substring(0, nodeid.length() - 2);////.PadLeft(3, '0');
						nodes.Retrieve(BP.WF.Template.NodeAttr.FK_Flow, flowno,
								BP.WF.Template.NodeAttr.Step);
					}
				}

				ListItem item = null;

				for (BP.WF.Node node : nodes.ToJavaList()) {
					// //item = new ListItem(String.format("(%1$s)%2$s",
					// node.getNodeID(), node.getName()), node.getNodeID());

					if (("ND" + getFK_MapData()).equals(node.getNodeID())) {
						item.attributes.put("disabled", "disabled");
					}

					lb.Items.add(item);
				}

				pub1.append(Add(lb));
				pub1.append(AddDivEnd());

				pub1.append(Add("<div id='btns'>"));

				LinkButton lbtn = new LinkButton(false, "复制");// //NamesOfBtn.Copy,
																// "复制"
				// //lbtn.OnClientClick = "var v = $('#" + lb.ClientID +
				// "').val(); if(!v) { alert('请选择将此排序复制到的节点！'); return false; } else { $('#"
				// + hidCopyNodes.ClientID + "').val(v); return true; }";
				// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
				// C#-style event wireups:
				//lbtn.Click += new EventHandler(lbtn_Click);
				lbtn.addAttr("onclick", "lbtn_Click");
				pub1.append(Add(lbtn));
				lbtn = new LinkButton(false, "取消");// // NamesOfBtn.Cancel,
				// //lbtn.OnClientClick = "$('#nodes').dialog('close');";
				pub1.append(Add(lbtn));

				pub1.append(AddDivEnd());
			}
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion
		}
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 排序复制功能

	/*private void lbtn_Click() {
		if (StringUtil.isAllWhitespace(hidCopyNodes.getValue())) {
			return;
		}

		String[] nodeids = hidCopyNodes.getValue().split("[,]", -1);
		String tmd = null;
		GroupField group = null;
		MapDatas tmapdatas = null;
		MapAttrs tattrs = null, oattrs = null, tarattrs = null;
		GroupFields tgroups = null, ogroups = null, targroups = null;
		MapDtls tdtls = null;
		MapData tmapdata = null;
		MapAttr tattr = null;
		GroupField tgrp = null;
		MapDtl tdtl = null;
		int maxGrpIdx = 0; // 当前最大分组排序号
		int maxAttrIdx = 0; // 当前最大字段排序号
		int maxDtlIdx = 0; // 当前最大明细表排序号
		java.util.ArrayList<String> idxGrps = new java.util.ArrayList<String>(); // 复制过的分组名称集合
		java.util.ArrayList<String> idxAttrs = new java.util.ArrayList<String>(); // 复制过的字段编号集合
		java.util.ArrayList<String> idxDtls = new java.util.ArrayList<String>(); // 复制过的明细表编号集合

		for (String nodeid : nodeids) {
			tmd = "ND" + nodeid;

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region 获取数据
			tmapdatas = new MapDatas();
			QueryObject qo = new QueryObject(tmapdatas);
			qo.AddWhere(MapDataAttr.No, "Like", tmd + "%");
			qo.addOrderBy(MapDataAttr.Idx);
			qo.DoQuery();

			tattrs = new MapAttrs();
			qo = new QueryObject(tattrs);
			qo.AddWhere(MapAttrAttr.FK_MapData, tmd);
			qo.addAnd();
			qo.AddWhere(MapAttrAttr.UIVisible, true);
			qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
			qo.DoQuery();

			tgroups = new GroupFields();
			qo = new QueryObject(tgroups);
			qo.AddWhere(GroupFieldAttr.EnName, tmd);
			qo.addOrderBy(GroupFieldAttr.Idx);
			qo.DoQuery();

			tdtls = new MapDtls();
			qo = new QueryObject(tdtls);
			qo.AddWhere(MapDtlAttr.FK_MapData, tmd);
			qo.addAnd();
			qo.AddWhere(MapDtlAttr.IsView, true);
			// qo.addOrderBy(MapDtlAttr.RowIdx);
			qo.DoQuery();
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region 复制排序逻辑

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region //分组排序复制
			for (GroupField grp : groups.ToJavaList()) {
				// 通过分组名称来确定是同一个组，同一个组在不同的节点分组编号是不一样的
				Object tempVar = tgroups.GetEntityByKey(GroupFieldAttr.Lab,
						grp.getLab());
				tgrp = (GroupField) ((tempVar instanceof GroupField) ? tempVar
						: null);
				if (tgrp == null) {
					continue;
				}

				tgrp.setIdx(grp.getIdx());
				tgrp.DirectUpdate();

				maxGrpIdx = Math.max(grp.getIdx(), maxGrpIdx);
				idxGrps.add(grp.getLab());
			}

			for (GroupField grp : tgroups.ToJavaList()) {
				if (idxGrps.contains(grp.getPKVal())) {
					continue;
				}

				grp.setIdx(maxGrpIdx = maxGrpIdx + 1);
				;
				grp.DirectUpdate();
			}
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region //字段排序复制
			for (MapAttr attr : attrs.ToJavaList()) {
				// 排除主键
				if (attr.getIsPK() == true) {
					continue;
				}

				Object tempVar2 = tattrs.GetEntityByKey(MapAttrAttr.KeyOfEn,
						attr.getKeyOfEn());
				tattr = (MapAttr) ((tempVar2 instanceof MapAttr) ? tempVar2
						: null);
				if (tattr == null) {
					continue;
				}

				Object tempVar3 = groups.GetEntityByKey(GroupFieldAttr.OID,
						attr.getGroupID());
				group = (GroupField) ((tempVar3 instanceof GroupField) ? tempVar3
						: null);

				// 比对字段的分组是否一致，不一致则更新一致
				if (group == null) {
					// 源字段分组为空，则目标字段分组置为0
					tattr.setGroupID(0);
				} else {
					// 此处要判断目标节点中是否已经创建了这个源字段所属分组，如果没有创建，则要自动创建
					Object tempVar4 = tgroups.GetEntityByKey(
							GroupFieldAttr.Lab, group.getLab());
					tgrp = (GroupField) ((tempVar4 instanceof GroupField) ? tempVar4
							: null);

					if (tgrp == null) {
						tgrp = new GroupField();
						tgrp.setLab(group.getLab());
						tgrp.setEnName(tmd);
						tgrp.setIdx(group.getIdx());
						tgrp.Insert();
						tgroups.AddEntity(tgrp);

						tattr.setGroupID(new Long(tgrp.getOID()).intValue());
					} else {
						if (tgrp.getOID() != tattr.getGroupID()) {
							tattr.setGroupID(new Long(tgrp.getOID()).intValue());
						}
					}
				}

				tattr.setIDX(attr.getIDX());
				tattr.DirectUpdate();
				maxAttrIdx = Math.max(attr.getIDX(), maxAttrIdx);
				idxAttrs.add(attr.getKeyOfEn());
			}

			for (MapAttr attr : tattrs.ToJavaList()) {
				// 排除主键
				if (attr.getIsPK() == true) {
					continue;
				}
				if (idxAttrs.contains(attr.getKeyOfEn())) {
					continue;
				}

				attr.setIDX(maxAttrIdx = maxAttrIdx + 1);
				attr.DirectUpdate();
			}
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region //明细表排序复制
			String dtlIdx = "";
			GroupField tgroup = null;
			int groupidx = 0;
			int tgroupidx = 0;

			for (MapDtl dtl : dtls.ToJavaList()) {
				dtlIdx = dtl.getNo().replace(dtl.getFK_MapData() + "Dtl", "");
				Object tempVar5 = tdtls.GetEntityByKey(MapDtlAttr.No, tmd
						+ "Dtl" + dtlIdx);
				tdtl = (MapDtl) ((tempVar5 instanceof MapDtl) ? tempVar5 : null);

				if (tdtl == null) {
					continue;
				}

				// 判断目标明细表是否有分组，没有分组，则创建分组
				tgroup = GetGroup(tdtl.getNo(), tgroups);
				tgroupidx = tgroup == null ? 0 : tgroup.getIdx();
				group = GetGroup(dtl.getNo(), groups);
				groupidx = group == null ? 0 : group.getIdx();

				if (tgroup == null) {
					group = new GroupField();
					group.setLab(tdtl.getName());
					group.setEnName(tdtl.getFK_MapData());
					group.setCtrlType(GroupCtrlType.Dtl);
					group.setCtrlID(tdtl.getNo());
					group.setIdx(groupidx);
					group.Insert();

					tgroupidx = groupidx;
					tgroups.AddEntity(group);
				}

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 1.明细表排序
				if (tgroupidx != groupidx && group != null) {
					tgroup.setIdx(groupidx);
					tgroup.DirectUpdate();

					tgroupidx = groupidx;
					Object tempVar6 = tmapdatas.GetEntityByKey(MapDataAttr.No,
							tdtl.getNo());
					tmapdata = (MapData) ((tempVar6 instanceof MapData) ? tempVar6
							: null);
					if (tmapdata != null) {
						tmapdata.setIdx(tgroup.getIdx());
						tmapdata.DirectUpdate();
					}
				}

				maxDtlIdx = Math.max(tgroupidx, maxDtlIdx);
				idxDtls.add(dtl.getNo());
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 2.获取源节点明细表中的字段分组、字段信息
				oattrs = new MapAttrs();
				qo = new QueryObject(oattrs);
				qo.AddWhere(MapAttrAttr.FK_MapData, dtl.getNo());
				qo.addAnd();
				qo.AddWhere(MapAttrAttr.UIVisible, true);
				qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
				qo.DoQuery();

				ogroups = new GroupFields();
				qo = new QueryObject(ogroups);
				qo.AddWhere(GroupFieldAttr.EnName, dtl.getNo());
				qo.addOrderBy(GroupFieldAttr.Idx);
				qo.DoQuery();
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 3.获取目标节点明细表中的字段分组、字段信息
				tarattrs = new MapAttrs();
				qo = new QueryObject(tarattrs);
				qo.AddWhere(MapAttrAttr.FK_MapData, tdtl.getNo());
				qo.addAnd();
				qo.AddWhere(MapAttrAttr.UIVisible, true);
				qo.addOrderBy(MapAttrAttr.GroupID, MapAttrAttr.Idx);
				qo.DoQuery();

				targroups = new GroupFields();
				qo = new QueryObject(targroups);
				qo.AddWhere(GroupFieldAttr.EnName, tdtl.getNo());
				qo.addOrderBy(GroupFieldAttr.Idx);
				qo.DoQuery();
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 4.明细表字段分组排序
				maxGrpIdx = 0;
				idxGrps = new java.util.ArrayList<String>();

				for (GroupField grp : ogroups.ToJavaList()) {
					// 通过分组名称来确定是同一个组，同一个组在不同的节点分组编号是不一样的
					Object tempVar7 = targroups.GetEntityByKey(
							GroupFieldAttr.Lab, grp.getLab());
					tgrp = (GroupField) ((tempVar7 instanceof GroupField) ? tempVar7
							: null);
					if (tgrp == null) {
						continue;
					}

					tgrp.setIdx(grp.getIdx());
					tgrp.DirectUpdate();

					maxGrpIdx = Math.max(grp.getIdx(), maxGrpIdx);
					idxGrps.add(grp.getLab());
				}

				for (GroupField grp : targroups.ToJavaList()) {
					if (idxGrps.contains(grp.getLab())) {
						continue;
					}

					grp.setIdx(maxGrpIdx = maxGrpIdx + 1);
					grp.DirectUpdate();
				}
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion

				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#region 5.明细表字段排序
				maxAttrIdx = 0;
				idxAttrs = new java.util.ArrayList<String>();

				for (MapAttr attr : oattrs.ToJavaList()) {
					Object tempVar8 = tarattrs.GetEntityByKey(
							MapAttrAttr.KeyOfEn, attr.getKeyOfEn());
					tattr = (MapAttr) ((tempVar8 instanceof MapAttr) ? tempVar8
							: null);
					if (tattr == null) {
						continue;
					}

					Object tempVar9 = ogroups.GetEntityByKey(
							GroupFieldAttr.OID, attr.getGroupID());
					group = (GroupField) ((tempVar9 instanceof GroupField) ? tempVar9
							: null);

					// 比对字段的分组是否一致，不一致则更新一致
					if (group == null) {
						// 源字段分组为空，则目标字段分组置为0
						tattr.setGroupID(0);
					} else {
						// 此处要判断目标节点中是否已经创建了这个源字段所属分组，如果没有创建，则要自动创建
						Object tempVar10 = targroups.GetEntityByKey(
								GroupFieldAttr.Lab, group.getLab());
						tgrp = (GroupField) ((tempVar10 instanceof GroupField) ? tempVar10
								: null);

						if (tgrp == null) {
							tgrp = new GroupField();
							tgrp.setLab(group.getLab());
							tgrp.setEnName(tdtl.getNo());
							tgrp.setIdx(group.getIdx());
							tgrp.Insert();
							targroups.AddEntity(tgrp);

							tattr.setGroupID(new Long(tgrp.getOID()).intValue());
						} else {
							if (tgrp.getOID() != tattr.getGroupID()) {
								tattr.setGroupID(new Long(tgrp.getOID())
										.intValue());
							}
						}
					}

					tattr.setIDX(attr.getIDX());
					tattr.DirectUpdate();
					maxAttrIdx = Math.max(attr.getIDX(), maxAttrIdx);
					idxAttrs.add(attr.getKeyOfEn());
				}

				for (MapAttr attr : tarattrs.ToJavaList()) {
					if (idxAttrs.contains(attr.getKeyOfEn())) {
						continue;
					}

					attr.setIDX(maxAttrIdx = maxAttrIdx + 1);
					attr.DirectUpdate();
				}
				// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in
				// Java:
				// /#endregion
			}

			// 确定目标节点中，源节点没有的明细表的排序
			for (MapDtl dtl : tdtls.ToJavaList()) {
				if (idxDtls.contains(dtl.getNo())) {
					continue;
				}

				maxDtlIdx = maxDtlIdx + 1;
				tgroup = GetGroup(dtl.getNo(), tgroups);

				if (tgroup == null) {
					tgroup = new GroupField();
					tgroup.setLab(tdtl.getName());
					tgroup.setEnName(tdtl.getFK_MapData());
					tgroup.setCtrlType(GroupCtrlType.Dtl);
					tgroup.setCtrlID(tdtl.getNo());
					tgroup.setIdx(maxDtlIdx);
					tgroup.Insert();

					tgroups.AddEntity(group);
				}

				if (tgroup.getIdx() != maxDtlIdx) {
					tgroup.setIdx(maxDtlIdx);
					;
					tgroup.DirectUpdate();
				}

				Object tempVar11 = tmapdatas.GetEntityByKey(MapDataAttr.No,
						dtl.getNo());
				tmapdata = (MapData) ((tempVar11 instanceof MapData) ? tempVar11
						: null);
				if (tmapdata != null) {
					tmapdata.setIdx(maxDtlIdx);
					tmapdata.DirectUpdate();
				}
			}
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion
		}

		// 重新加载本页
		String tempVar12 = getFK_Flow();
		response.sendRedirect(request.getPathInfo() + "?FK_Flow="
				+ ((tempVar12 != null) ? tempVar12 : "") + "&FK_MapData="
				+ getFK_MapData() + "&t="
				+ getCurentDate("yyyyMMddHHmmssffffff"));
	}*/

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 字段分组调整

	/*private void ddl_SelectedIndexChanged() {
		DDL ddl = new DDL();////(DDL) ((sender instanceof DDL) ? sender : null);
		MapAttr attr = null;
		String key = null;
		int newGrpId = 0;
		DataTable dt = null;
		DataRow[] rows = null;
		String[] ids = ddl.getId().split("[_]", -1); // 如：DDL_Group_102_XingMing

		if (ids.length < 4) {
			return;
		}

		key = ddl.getId().substring(ids[0].length() + ids[1].length()
				+ ids[2].length() + 3);
		newGrpId = ddl.getSelectedItemIntVal();
		// 如果是明细表
		if (ids[1].equals("Dtl")) {
			// MapDtl mapDtl = dtls.GetEntityByKey(MapAttrAttr.FK_MapData,
			// FK_MapData, MapDtlAttr.No, key) as MapDtl;
			// if (mapDtl == null) return;

			// mapDtl.GroupID = newGrpId;
			// mapDtl.DirectUpdate();
		} else if (ids[1].equals("AthMent")) {
			// 多附件排序
			// FrmAttachment athMent =
			// athMents.GetEntityByKey(FrmAttachmentAttr.FK_MapData, FK_MapData,
			// FrmAttachmentAttr.NoOfObj, key) as FrmAttachment;
			// if (athMent == null) return;

			// athMent.GroupID = newGrpId;
			// athMent.DirectUpdate();
		} else {
			Object tempVar = attrs.GetEntityByKey(MapAttrAttr.FK_MapData,
					getFK_MapData(), MapAttrAttr.KeyOfEn, key);
			attr = (MapAttr) ((tempVar instanceof MapAttr) ? tempVar : null);

			if (attr == null) {
				return;
			}

			newGrpId = ddl.getSelectedItemIntVal();
			dt = attrs.ToDataTableField("dtAttrs");
			rows = dt.Select(String.format("FK_MapData = '%1$s' AND GroupID = %2$s", getFK_MapData(),
					newGrpId), "Idx DESC"));

			attr.setGroupID(newGrpId);
			attr.setIDX((rows.length > 0 ? rows[0].getValue("Idx") : 0) + 1);;
			attr.DirectUpdate();
		}
		// 重新加载本页
		String tempVar2 = getFK_Flow();
		response.sendRedirect(
				request.getPathInfo() + "?FK_Flow="
						+ ((tempVar2 != null) ? tempVar2 : "") + "&FK_MapData="
						+ getFK_MapData() + "&t="
						+ getCurentDate("yyyyMMddHHmmssffffff"));
	}*/

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 重置表单字段
	/*private void btnReSet_Click() {
		attrs = new MapAttrs();
		QueryObject qo = new QueryObject(attrs);
		qo.AddWhere(MapAttrAttr.FK_MapData, getFK_MapData());
		qo.addAnd();
		qo.AddWhere(MapAttrAttr.UIVisible, true);
		qo.addOrderBy(MapAttrAttr.Y, MapAttrAttr.X);
		qo.DoQuery();
		int rowIdx = 0;
		for (MapAttr mapAttr : attrs.ToJavaList()) {
			mapAttr.setIDX(rowIdx);
			mapAttr.DirectUpdate();
			rowIdx++;
		}
		// 重新加载本页
		String tempVar = getFK_Flow();
		try {
			response.sendRedirect(
					request.getPathInfo() + "?FK_Flow=" + ((tempVar != null) ? tempVar : "")
							+ "&FK_MapData=" + getFK_MapData() + "&t="
							+ getCurentDate("yyyyMMddHHmmssffffff"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 字段排序

//	private void btn_Click() {
//		LinkButton btn = new LinkButton();////(LinkButton) ((sender instanceof LinkButton) ? sender: null);
//		MoveBtnIds ids = new MoveBtnIds(btn.getId());
//		MoveBtnIds tids = null;
//		LinkButton tbtn = null;
//		GroupField group = null;
//		MapAttr attr = null;
//		MapData mapdata = null;
//		MapDtl dtl = null;
//		FrmAttachment athMent = null;
//		java.util.ArrayList<String> keys = new java.util.ArrayList<String>();
//		int targetIdx;
//
//		if (!ids.getSuccess()) {
//			return;
//		}
//
//		targetIdx = ids.getMoveDirection().equals("Up") ? ids.getIdx() - 1
//				: ids.getIdx() + 1;
//
//		// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
//		// string member and was converted to Java 'if-else' logic:
//		// switch (ids.ObjectType)
//		// ORIGINAL LINE: case "Group":
//		if (ids.getObjectType().equals("Group")) {
//			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//			// /#region 字段分组
//			// 检测所有的分组，判断原有idx与现在idx是否匹配，不匹配的都进行更新，第一次进行排序时，将原有的idx都更新一遍
//			for (Control ctrl : pub1.Controls) {
//				tbtn = (LinkButton) ((ctrl instanceof LinkButton) ? ctrl : null);
//				if (tbtn == null || !tbtn.getId().startsWith("Btn_Group_")) {
//					continue;
//				}
//
//				tids = new MoveBtnIds(ctrl.ID);
//				if (!tids.getSuccess() || keys.contains(tids.getKey())) {
//					continue;
//				}
//
//				keys.add(tids.getKey());
//
//				if (tids.getIdx() == targetIdx) {
//					// 受影响的组索引号更改为当前移动组的索引号
//					if (tids.getOldIdx() != ids.getIdx()) {
//						// 更新GroupField中的索引
//						Object tempVar = groups.GetEntityByKey(Integer
//								.parseInt(tids.getKey()));
//						group = (GroupField) ((tempVar instanceof GroupField) ? tempVar
//								: null);
//						group.setIdx(ids.getIdx());;
//						group.Update();
//					}
//				} else if (tids.getIdx() == ids.getIdx()) {
//					// 当前移动组的索引号改为受影响的组索引号
//					if (tids.getOldIdx() != targetIdx) {
//						// 更新GroupField中的索引
//						Object tempVar2 = groups.GetEntityByKey(Integer
//								.parseInt(tids.getKey()));
//						group = (GroupField) ((tempVar2 instanceof GroupField) ? tempVar2
//								: null);
//						group.setIdx(targetIdx);
//						group.Update();
//					}
//				} else {
//					// 检索其余未受影响的组，将与之对应的索引号不一样的均更新
//					if (tids.getOldIdx() != tids.getIdx()) {
//						// 更新GroupField中的索引
//						Object tempVar3 = groups.GetEntityByKey(Integer
//								.parseInt(tids.getKey()));
//						group = (GroupField) ((tempVar3 instanceof GroupField) ? tempVar3
//								: null);
//						group.Idx = tids.getIdx();
//						group.Update();
//					}
//				}
//			}
//			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//			// /#endregion
//		}
//		// ORIGINAL LINE: case "Attr":
//		else if (ids.getObjectType().equals("Attr")) {
//			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//			// /#region 字段
//			// 检测所有的字段，判断原有idx与现在idx是否匹配，不匹配的都进行更新，第一次进行排序时，将原有的idx都更新一遍
//			for (Control ctrl : pub1.Controls) {
//				tbtn = (LinkButton) ((ctrl instanceof LinkButton) ? ctrl : null);
//				if (tbtn == null || !tbtn.getId().startsWith("Btn_Attr_")) {
//					continue;
//				}
//
//				tids = new MoveBtnIds(ctrl.ID);
//				if (!tids.getSuccess() || keys.contains(tids.getKey())) {
//					continue;
//				}
//
//				keys.add(tids.getKey());
//
//				if (tids.getIdx() == targetIdx
//						&& tids.getGroupIdx() == ids.getGroupIdx()) {
//					// 受影响的字段索引号更改为当前移动字段的索引号
//					if (tids.getOldIdx() != ids.getIdx()) {
//						// 更新MapAttr中的索引
//						Object tempVar4 = attrs.GetEntityByKey(
//								MapAttrAttr.FK_MapData, getFK_MapData(),
//								MapAttrAttr.KeyOfEn, tids.getKey());
//						attr = (MapAttr) ((tempVar4 instanceof MapAttr) ? tempVar4
//								: null);
//						attr.setIDX(ids.getIdx());
//						attr.Update();
//					}
//				} else if (tids.getIdx() == ids.getIdx()
//						&& tids.getGroupIdx() == ids.getGroupIdx()) {
//					// 当前移动字段的索引号改为受影响的字段索引号
//					if (tids.getOldIdx() != targetIdx) {
//						// 更新MapAttr中的索引
//						Object tempVar5 = attrs.GetEntityByKey(
//								MapAttrAttr.FK_MapData, getFK_MapData(),
//								MapAttrAttr.KeyOfEn, tids.getKey());
//						attr = (MapAttr) ((tempVar5 instanceof MapAttr) ? tempVar5
//								: null);
//						attr.setIDX(targetIdx);
//						attr.Update();
//					}
//				} else {
//					// 检索其余未受影响的字段，将与之对应的索引号不一样的均更新
//					if (tids.getOldIdx() != tids.getIdx()) {
//						// 更新MapAttr中的索引
//						Object tempVar6 = attrs.GetEntityByKey(
//								MapAttrAttr.FK_MapData, getFK_MapData(),
//								MapAttrAttr.KeyOfEn, tids.getKey());
//						attr = (MapAttr) ((tempVar6 instanceof MapAttr) ? tempVar6
//								: null);
//						attr.setIDX(tids.getIdx());
//						attr.Update();
//					}
//				}
//			}
//			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//			// /#endregion
//		}
//		// ORIGINAL LINE: case "Dtl":
//		else if (ids.getObjectType().equals("Dtl")) {
//			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//			// /#region 明细表
//			// 检测所有的明细表，判断原有idx与现在idx是否匹配，不匹配的都进行更新，第一次进行排序时，将原有的idx都更新一遍
//			for (Control ctrl : pub1.Controls) {
//				tbtn = (LinkButton) ((ctrl instanceof LinkButton) ? ctrl : null);
//				if (tbtn == null || !tbtn.getId().startsWith("Btn_Dtl_")) {
//					continue;
//				}
//
//				tids = new MoveBtnIds(ctrl.ID);
//				if (!tids.getSuccess() || keys.contains(tids.getKey())) {
//					continue;
//				}
//
//				keys.add(tids.getKey());
//
//				if (tids.getIdx() == targetIdx) {
//					// 受影响的明细表索引号更改为当前移动明细表的索引号
//					if (tids.getOldIdx() != ids.getIdx()) {
//						// 更新MapDtl中的明细表索引
//						// dtl = dtls.GetEntityByKey(tids.Key) as MapDtl;
//						// dtl.RowIdx = ids.Idx;
//						// dtl.Update();
//						group = GetGroup(tids.getKey(), groups);
//						group.setIdx(ids.getIdx());
//						group.Update();
//
//						// 更新MapData中的索引
//						Object tempVar7 = mapdatas.GetEntityByKey(dtl.getNo());
//						mapdata = (MapData) ((tempVar7 instanceof MapData) ? tempVar7
//								: null);
//						mapdata.setIdx(ids.getIdx());
//						mapdata.Update();
//					}
//				} else if (tids.getIdx() == ids.getIdx()) {
//					// 当前移动明细表的索引号改为受影响的明细表索引号
//					if (tids.getOldIdx() != targetIdx) {
//						// 更新MapDtl中的明细表索引
//						// dtl = dtls.GetEntityByKey(tids.Key) as MapDtl;
//						// dtl.RowIdx = targetIdx;
//						// dtl.Update();
//						group = GetGroup(tids.getKey(), groups);
//						group.setIdx(targetIdx);
//						group.Update();
//
//						// 更新MapData中的索引
//						Object tempVar8 = mapdatas.GetEntityByKey(dtl.getNo());
//						mapdata = (MapData) ((tempVar8 instanceof MapData) ? tempVar8
//								: null);
//						mapdata.setIdx(targetIdx);
//						mapdata.Update();
//					}
//				} else {
//					// 检索其余未受影响的明细表，将与之对应的索引号不一样的均更新
//					if (tids.getOldIdx() != tids.getIdx()) {
//						// 更新MapDtl中的明细表索引
//						// dtl = dtls.GetEntityByKey(tids.Key) as MapDtl;
//						// dtl.RowIdx = tids.Idx;
//						// dtl.Update();
//						group = GetGroup(tids.getKey(), groups);
//						group.setIdx(tids.getIdx());
//						group.Update();
//
//						// 更新MapData中的索引
//						Object tempVar9 = mapdatas.GetEntityByKey(dtl.getNo());
//						mapdata = (MapData) ((tempVar9 instanceof MapData) ? tempVar9
//								: null);
//						mapdata.setIdx(tids.getIdx());
//						mapdata.Update();
//					}
//				}
//			}
//			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//			// /#endregion
//		}
//		// ORIGINAL LINE: case "AthMent":
//		else if (ids.getObjectType().equals("AthMent")) {
//			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//			// /#region 多附件
//			// 检测所有的多附件，判断原有idx与现在idx是否匹配，不匹配的都进行更新，第一次进行排序时，将原有的idx都更新一遍
//			for (Control ctrl : pub1.Controls) {
//				tbtn = (LinkButton) ((ctrl instanceof LinkButton) ? ctrl : null);
//				if (tbtn == null || !tbtn.getId().startsWith("Btn_AthMent")) {
//					continue;
//				}
//
//				tids = new MoveBtnIds(ctrl.ID);
//				if (!tids.getSuccess() || keys.contains(tids.getKey())) {
//					continue;
//				}
//
//				keys.add(tids.getKey());
//
//				Object tempVar10 = athMents.GetEntityByKey(
//						FrmAttachmentAttr.FK_MapData, getFK_MapData(),
//						FrmAttachmentAttr.NoOfObj, tids.getKey());
//				athMent = (FrmAttachment) ((tempVar10 instanceof FrmAttachment) ? tempVar10
//						: null);
//				if (athMent == null) {
//					continue;
//				}
//				if (tids.getIdx() == targetIdx) {
//					// 受影响的组索引号更改为当前移动组的索引号
//					if (tids.getOldIdx() != ids.getIdx()) {
//						// 更新FrmAttachment中的索引
//						athMent.setRowIdx(ids.getIdx());
//						athMent.Update();
//					}
//				} else if (tids.getIdx() == ids.getIdx()) {
//					// 当前移动组的索引号改为受影响的组索引号
//					if (tids.getOldIdx() != targetIdx) {
//						// 更新FrmAttachment中的索引
//						athMent.setRowIdx(targetIdx);
//						athMent.Update();
//					}
//				} else {
//					// 检索其余未受影响的组，将与之对应的索引号不一样的均更新
//					if (tids.getOldIdx() != tids.getIdx()) {
//						// 更新FrmAttachment中的索引
//						athMent.setRowIdx(tids.getIdx());
//						athMent.Update();
//					}
//				}
//			}
//			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//			// /#endregion
//		}
//
//		// 重新加载本页
//		String tempVar11 = getFK_Flow();
//		response.sendRedirect(
//				request.getPathInfo() + "?FK_Flow="
//						+ ((tempVar11 != null) ? tempVar11 : "")
//						+ "&FK_MapData=" + getFK_MapData() + "&t="
//						+ getCurentDate("yyyyMMddHHmmssffffff"));
//	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion

	/**
	 * 判断在DataRow数组中，是否存在指定列指定值的行
	 * 
	 * @param rows
	 *            DataRow数组
	 * @param field
	 *            指定列名
	 * @param value
	 *            指定值
	 * @return
	 */
	private boolean IsExistInDataRowArray(DataRowCollection rows, String field,
			Object value) {
		for (DataRow row : rows) {
			if (row.getValue(field).equals(value)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 根据MapAttr字段行数据，获取该字段的编辑链接
	 * 
	 * @param drAttr
	 *            MapAttr字段行数据
	 * @return
	 */
	private String GenerateEditUrl(DataRow drAttr) {
		
		return "ssss";
		
	/*	String url = "../FoolFormDesigner/";
		String a = drAttr.getValue(MapAttrAttr.LGType)+"";
		switch (FieldTypeS.forValue(Integer.parseInt(StringUtil.isAllWhitespace(drAttr.getValue(MapAttrAttr.LGType)+"")?0+"":drAttr.getValue(MapAttrAttr.LGType)+""))) {
		case Enum:
			url += "EditEnum.aspx?DoType=Edit&FK_MapData="
					+ drAttr.getValue(MapAttrAttr.FK_MapData) + "&MyPK="
					+ drAttr.getValue(MapAttrAttr.MyPK) + "&FType="
					+ drAttr.getValue(MapAttrAttr.MyDataType) + "&GroupField=0";
			break;
		case Normal:
			url += "EditF.aspx?DoType=Edit&FK_MapData="
					+ drAttr.getValue(MapAttrAttr.FK_MapData) + "&MyPK="
					+ drAttr.getValue(MapAttrAttr.MyPK) + "&FType="
					+ drAttr.getValue(MapAttrAttr.MyDataType) + "&GroupField=0";
			break;
		case FK:
			url += "EditTable.aspx?DoType=Edit&FK_MapData="
					+ drAttr.getValue(MapAttrAttr.FK_MapData) + "&MyPK="
					+ drAttr.getValue(MapAttrAttr.MyPK) + "&FType="
					+ drAttr.getValue(MapAttrAttr.MyDataType) + "&GroupField=0";
			break;
		default:
			url = "javascript:alert('未涉及的字段类型！');void(0);";
			break;
		}

		return url;*/
	}

	/**
	 * 给分组添加明细表
	 * 
	 * @param groupOfDtls
	 */
	private void GroupAddDtl(java.util.ArrayList<MapDtl> groupOfDtls) {
		pub1.append(AddTR());
		pub1.append(AddTDBegin("colspan='5'"));
		pub1.append(AddTable("class='Table' border='0' cellpadding='0' cellspacing='0' style='width:100%'"));

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#region 标题行
		pub1.append(AddTR());
		pub1.append(AddTDGroupTitle("style='width:40px;text-align:center'", "序"));
		pub1.append(AddTDGroupTitle("style='width:100px;'", "明细表编号"));
		pub1.append(AddTDGroupTitle("style='width:160px;'", "中文名称"));
		pub1.append(AddTDGroupTitle("style='width:120px;'", "字段分组"));
		pub1.append(AddTDGroupTitle("排序"));
		pub1.append(AddTREnd());

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#endregion

		int idx_Attr = 1;
		LinkButton btn = null;
		for (MapDtl dtl : groupOfDtls) {
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region 明细表排序

			pub1.append(AddTR());
			pub1.append(AddTD("style='text-align:center'", (new Integer(
					idx_Attr)).toString()));
			pub1.append(AddTD("<a href=\"javascript:EditDtl('"
					+ this.getFK_MapData() + "','" + dtl.getNo() + "')\" >" + dtl.getNo()
					+ "</a>"));
			pub1.append(AddTD(dtl.getName()));
			// DDL ddl = new DDL();
			// ddl.ID = "DDL_Dtl_" + dtl.GroupID + "_" + dtl.No;

			// foreach (GroupField groupField in groups)
			// ddl.Items.Add(new ListItem(groupField.Lab,
			// groupField.OID.ToString()));

			// ddl.AutoPostBack = true;
			// ddl.SelectedIndexChanged += ddl_SelectedIndexChanged;
			// ddl.SetSelectItem(dtl.GroupID);
			// pub1.append(AddTD(ddl);
			pub1.append(AddTD("&nbsp;"));

			pub1.append(AddTDBegin());

			if (idx_Attr > 1) {
				btn = new LinkButton(false, "Btn_Dtl_Up_" + dtls.size() + "_"
						+ idx_Attr + "_" + dtl.getRowIdx() + "_0_" + dtl.getNo(), "上移");
				btn.SetDataOption("iconCls", "'icon-up'");
				// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
				// C#-style event wireups:
				//btn.Click += btn_Click;
				btn.addAttr("onclick", "btn_Click()");
				pub1.append(Add(btn));
			}

			if (idx_Attr < groupOfDtls.size()) {
				btn = new LinkButton(false, "Btn_Dtl_Down_" + dtls.size() + "_"
						+ idx_Attr + "_" + dtl.getRowIdx() + "_0_" + dtl.getNo(), "下移");
				btn.SetDataOption("iconCls", "'icon-down'");
				// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
				// C#-style event wireups:
				//btn.Click += btn_Click;
				btn.addAttr("onclick", "btn_Click()");
				pub1.append(Add(btn));
			}

			pub1.append(AddSpace(1));
			String tempVar = getFK_Flow();
			pub1.append(Add(String
					.format("<a href='%1$s' target='_self' class='easyui-linkbutton' data-options=\"iconCls:'icon-sheet'\">字段排序</a>",
							request.getPathInfo()
									+ "?FK_Flow="
									+ ((tempVar != null) ? tempVar : "")
									+ "&FK_MapData="
									+ dtl.getNo()
									+ "&t="
									+ getCurentDate("yyyyMMddHHmmssffffff"))));

			pub1.append(AddTDEnd());
			pub1.append(AddTREnd());

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion

			idx_Attr++;
		}
		pub1.append(AddTableEnd());

		pub1.append(AddBR());
		pub1.append(AddTDEnd());
		pub1.append(AddTREnd());
	}

	/**
	 * 给分组添加多附件
	 * 
	 * @param groupOfDtls
	 */
	private void GroupAddAthMent(
			java.util.ArrayList<FrmAttachment> groupOfAthMents) {
		pub1.append(AddTR());
		pub1.append(AddTDBegin("colspan='5'"));
		pub1.append(AddTable("class='Table' border='0' cellpadding='0' cellspacing='0' style='width:100%'"));

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#region 标题行
		pub1.append(AddTR());
		pub1.append(AddTDGroupTitle("style='width:40px;text-align:center'", "序"));
		pub1.append(AddTDGroupTitle("style='width:100px;'", "多附件编号"));
		pub1.append(AddTDGroupTitle("style='width:160px;'", "中文名称"));
		pub1.append(AddTDGroupTitle("style='width:120px;'", "字段分组"));
		pub1.append(AddTDGroupTitle("排序"));
		pub1.append(AddTREnd());

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#endregion

		int idx_Attr = 1;
		LinkButton btn = null;// //LinkBtn
		for (FrmAttachment athMent : groupOfAthMents) {
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#region 多附件排序

			pub1.append(AddTR());
			pub1.append(AddTD("style='text-align:center'", (new Integer(
					idx_Attr)).toString()));
			pub1.append(AddTD("<a href=\"javascript:EditAthMent('"
					+ this.getFK_MapData() + "','" + athMent.getNoOfObj()
					+ "')\" >" + athMent.getNoOfObj() + "</a>"));
			pub1.append(AddTD(athMent.getName()));
			// DDL ddl = new DDL();
			// ddl.ID = "DDL_AthMent_" + athMent.GroupID + "_" +
			// athMent.NoOfObj;

			// foreach (GroupField groupField in groups)
			// ddl.Items.Add(new ListItem(groupField.Lab,
			// groupField.OID.ToString()));

			// ddl.AutoPostBack = true;
			// ddl.SelectedIndexChanged += ddl_SelectedIndexChanged;
			// ddl.SetSelectItem(athMent.GroupID);
			// pub1.append(AddTD(ddl);
			pub1.append(AddTD("&nbsp;"));

			pub1.append(AddTDBegin());

			if (idx_Attr > 1) {
				btn = new LinkButton(false, "Btn_AthMent_Up_"
						+ athMents.size()// //LInkBtn
						+ "_" + idx_Attr + "_" + athMent.getRowIdx() + "_0_"
						+ athMent.getNoOfObj(), "上移");
				btn.SetDataOption("iconCls", "'icon-up'");
				// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
				// C#-style event wireups:
				//btn.Click += btn_Click;
				btn.addAttr("onclick", "btn_Click()");
				pub1.append(Add(btn));
			}

			if (idx_Attr < groupOfAthMents.size()) {
				btn = new LinkButton(false, "Btn_AthMent_Down_" + athMents.size()
						+ "_" + idx_Attr + "_" + athMent.getRowIdx() + "_0_"
						+ athMent.getNoOfObj(), "下移");
				btn.SetDataOption("iconCls", "'icon-down'");
				// C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to
				// C#-style event wireups:
				//btn.Click += btn_Click;
				btn.addAttr("onclick", "btn_Click()");
				pub1.append(Add(btn));
			}
			pub1.append(AddTDEnd());
			pub1.append(AddTREnd());

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			// /#endregion

			idx_Attr++;
		}
		pub1.append(AddTableEnd());

		pub1.append(AddBR());
		pub1.append(AddTDEnd());
		pub1.append(AddTREnd());
	}

	private int GetGroupID(String ctrlID, GroupFields gfs) {
		Object tempVar = gfs.GetEntityByKey(GroupFieldAttr.CtrlID, ctrlID);
		GroupField gf = (GroupField) ((tempVar instanceof GroupField) ? tempVar
				: null);
		return (int) (gf == null ? 0 : gf.getOID());
	}

	private GroupField GetGroup(String ctrlID, GroupFields gfs) {
		Object tempVar = gfs.GetEntityByKey(GroupFieldAttr.CtrlID, ctrlID);
		return (GroupField) ((tempVar instanceof GroupField) ? tempVar : null);
	}

	private String getCurentDate(String formate) {
		SimpleDateFormat dateFormater = new SimpleDateFormat(formate);
		Date date = new Date();
		String dtaes = dateFormater.format(date);
		return dtaes;
	}
}
