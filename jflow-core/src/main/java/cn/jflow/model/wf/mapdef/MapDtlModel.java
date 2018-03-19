package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.TextBox;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.AddAllLocation;
import BP.Sys.DtlModel;
import BP.Sys.GroupFieldAttr;
import BP.Sys.GroupFields;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtlAttr;
import BP.Tools.StringHelper;

public class MapDtlModel extends BaseModel{
	
	private StringBuilder pub;
	private String title;
	
	public StringBuilder getPub() {
		return pub;
	}
	
	public String getTitle() {
		return title;
	}
	
	private void appendPub(String str){
		pub.append(str);
	}

	public MapDtlModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		pub = new StringBuilder();
	}
	
		
		public final String getDoType()
		{
			String v = getParameter("DoType");
			if (v == null || v.equals(""))
			{
				v = "New";
			}
			return v;
		}
		public final String getFK_MapDtl()
		{
			return getParameter("FK_MapDtl");
		}

		public final void pageLoad()
		{
			MapData md = new MapData(this.getFK_MapData());
			title = md.getName() + " - 设计明细";
			if (StringHelper.isNullOrEmpty(this.getDoType()) || this.getDoType().equals("New")) {
				int num = BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(No) FROM Sys_MapDtl WHERE FK_MapData='" + this.getFK_MapData() + "'") + 1;
				MapDtl dtl1 = new MapDtl();
				dtl1.setName("从表" + num);
				dtl1.setNo(this.getFK_MapData() + "Dtl" + num);
				dtl1.setPTable(this.getFK_MapData() + "Dtl" + num);
				BindEdit(md, dtl1);
			}else if (this.getDoType().equals("Edit")) {
				MapDtl dtl = new MapDtl();
				if (this.getFK_MapDtl() == null)
				{
					dtl.setNo(this.getFK_MapData() + "Dtl");
				}
				else
				{
					dtl.setNo(this.getFK_MapDtl());
					dtl.Retrieve();
				}
				BindEdit(md, dtl);
			}
		}
	
		public final void BindEdit(MapData md, MapDtl dtl)
		{
			appendPub(AddTable());
			appendPub(AddTR());
			appendPub(AddTDTitle("setId"));
			appendPub(AddTDTitle("colspan=3", "基本设置"));
			appendPub(AddTREnd());

			int idx = 1;
			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("表英文名称"));
			TextBox tb = new TextBox();
			tb.setId("TB_No");
			tb.setText(dtl.getNo());
			if (this.getDoType().equals("Edit"))
			{
				tb.setEnabled(false);
			}
			appendPub(AddTD(tb));
			appendPub(AddTD());
			//appendPub(AddTD("英文名称全局唯一");
			appendPub(AddTREnd());


			appendPub(AddTR1());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("表中文名称"));
			tb = new TextBox();
			tb.setId("TB_Name");
			tb.setText(dtl.getName());
			appendPub(AddTD(tb));
			//appendPub(AddTD("XX 从表"));
			appendPub(AddTREnd());


			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("物理表名"));
			tb = new TextBox();
			tb.setId("TB_PTable");
			tb.setText(dtl.getPTable());

			appendPub(AddTD(tb));
			appendPub(AddTD());
			//appendPub(AddTD("存储数据的物理表名称");
			//  appendPub(AddTD("存储数据的物理表名称");
			appendPub(AddTREnd());

			DDL ddl = new DDL();

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("工作模式"));
			DDL workModelDDl = new DDL();
			////workModelDDl.Items.Add(new ListItem("普通", "0"));
			////workModelDDl.Items.Add(new ListItem("固定行", "1"));
			////workModelDDl.SelectedItem.Value = ((int) dtl.DtlModel)+"";
			workModelDDl.BindSysEnum(MapDtlAttr.Model, dtl.getDtlModel().getValue());
			workModelDDl.setId("DDL_Model");
			appendPub(AddTD(workModelDDl));
			appendPub(AddTD());
			appendPub(AddTREnd());


			if (dtl.getDtlModel() == DtlModel.FixRow)
			{
				tb = new TextBox();
				tb.setId("TB_" + MapDtlAttr.ImpFixTreeSql);
				tb.setText(dtl.getImpFixTreeSql());
				tb.setCols(80);

				appendPub(AddTR1());
				appendPub(AddTDIdx(idx++));
				appendPub(AddTD("树形结构数据源"));
				appendPub(AddTD("colspan=2", tb));
				appendPub(AddTREnd());

				tb = new TextBox();
				tb.setId("TB_" + MapDtlAttr.ImpFixDataSql);
				tb.setText(dtl.getImpFixDataSql());
				tb.setCols(80);

				appendPub(AddTR());
				appendPub(AddTDIdx(idx++));
				appendPub(AddTD("明细表数据源"));
				appendPub(AddTD("colspan=2", tb.toString()));
				appendPub(AddTREnd());


			}

			CheckBox cb = new CheckBox();

			appendPub(AddTR1());
			appendPub(AddTDIdx(idx++));
			cb.setId("CB_IsView");
			cb.setText("是否可见");
			cb.setChecked(dtl.getIsView());
			appendPub(AddTD(cb.toString()));

			cb = new CheckBox();
			cb.setId("CB_IsUpdate");
			cb.setText("是否可以修改行"); // "是否可以修改行";
			cb.setChecked(dtl.getIsUpdate());
			appendPub(AddTD(cb.toString()));

			cb = new CheckBox();
			cb.setId("CB_IsInsert");
			cb.setText("是否可以新增行"); // "是否可以新增行";
			cb.setChecked(dtl.getIsInsert());
			appendPub(AddTD(cb));
			appendPub(AddTREnd());


			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_IsDelete");
			cb.setText("是否可以删除行"); // "是否可以删除行";
			cb.setChecked(dtl.getIsDelete());
			appendPub(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_IsShowIdx");
			cb.setText("是否显示序号列"); //"是否显示序号列";
			cb.setChecked(dtl.getIsShowIdx());
			appendPub(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_IsShowSum");
			cb.setText("是否合计行"); // "是否合计行";
			cb.setChecked(dtl.getIsShowSum());
			appendPub(AddTD(cb));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_IsEnableAthM");
			cb.setText("是否启用多附件");
			cb.setChecked(dtl.getIsEnableAthM());
			appendPub(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_" + MapDtlAttr.IsEnableM2M);
			cb.setText("是否启用一对多");
			cb.setChecked(dtl.getIsEnableM2M());
			appendPub(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_" + MapDtlAttr.IsEnableM2MM);
			cb.setText("是否启用一对多多");
			cb.setChecked(dtl.getIsEnableM2MM());
			appendPub(AddTD(cb));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_" + MapDtlAttr.IsRowLock);
			cb.setText("是否启用锁定行（如果启用就需要增加IsRowLock一个隐藏的列，默认值为0。）？"); // "是否合计行";
			cb.setChecked(dtl.getIsRowLock());
			appendPub(AddTD("colspan=3", cb));
			appendPub(AddTREnd());


			appendPub(AddTR1());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_" + MapDtlAttr.IsShowTitle);
			cb.setText("是否显示标头(如果是多表表头的明细表，就不要显示表头了)?");

			cb.setChecked(dtl.getIsShowTitle());
			appendPub(AddTD("colspan=3", cb.toString()));
			appendPub(AddTREnd());

			 //为精英修改.
			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("增加记录方式"));
			ddl = new DDL();
			ddl.setId("DDL_DtlAddRecModel");
			ddl.BindSysEnum(MapDtlAttr.DtlAddRecModel, dtl.getDtlAddRecModel().getValue());
			appendPub(AddTD(ddl));
			appendPub(AddTD("满足不同的用户习惯"));
			appendPub(AddTREnd());
			
			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("初始化行数"));
			tb = new TextBox();
			tb.setId("TB_RowsOfList");
			tb.setCssClass("TBNum");
			tb.setText(String.valueOf(dtl.getRowsOfList()));
			appendPub(AddTD(tb));
			appendPub(AddTD(""));
			appendPub(AddTREnd());


			appendPub(AddTR1());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("显示格式"));
			ddl = new DDL();
			ddl.setId("DDL_DtlShowModel");
			ddl.BindSysEnum(MapDtlAttr.DtlShowModel, dtl.getHisDtlShowModel().getValue());
			appendPub(AddTD(ddl));
			appendPub(AddTD());
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("越出处理"));
			ddl = new DDL();
			ddl.setId("DDL_WhenOverSize");
			ddl.BindSysEnum(MapDtlAttr.WhenOverSize, dtl.getHisWhenOverSize().getValue());
			appendPub(AddTD(ddl));
			appendPub(AddTD());
			appendPub(AddTREnd());
			
			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("存盘方式"));
			ddl = new DDL();
			ddl.setId("DDL_DtlSaveModel");
			ddl.BindSysEnum(MapDtlAttr.DtlSaveModel, dtl.getDtlSaveModel().getValue());
			appendPub(AddTD(ddl));
			appendPub(AddTD("用于设置在明细表自动存盘还是，手动存盘."));
			appendPub(AddTREnd());

			appendPub(AddTRSum());
			appendPub(AddTDTitle("colspan=4", "与工作流相关设置"));
			appendPub(AddTREnd());
			
			appendPub(AddTR1());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("操作权限"));
			ddl = new DDL();
			ddl.BindSysEnum(MapDtlAttr.DtlOpenType, dtl.getDtlOpenType().getValue());
			ddl.setId("DDL_DtlOpenType");
			appendPub(AddTD(ddl));
			appendPub(AddTD());
			// appendPub(AddTD("用于从表的权限控制");
			appendPub(AddTREnd());

			appendPub(AddTR1());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_IsEnablePass");
			cb.setText("是否起用审核字段？"); // "是否合计行";
			cb.setChecked(dtl.getIsEnablePass());
			appendPub(AddTD(cb));

			String sql = "SELECT KeyOfEn as No, Name FROM Sys_MapAttr WHERE FK_MapData='" + dtl.getNo() + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				appendPub(AddTD());
				appendPub(AddTD());
			}
			else
			{
				appendPub(AddTDBegin("colspan=2"));
				cb = new CheckBox();
				cb.setId("CB_IsEnableGroupField");
				cb.setText("是否起用分组字段？"); // "是否合计行";
				cb.setChecked(dtl.getIsEnableGroupField());
				appendPub(cb.toString());

				ddl = new DDL();
				ddl.setId("DDL_GroupField");
				ddl.BindSQL(sql, "No", "Name", dtl.getGroupField());
				appendPub(ddl.toString());
				appendPub(AddTDEnd());
			}
			appendPub(AddTREnd());
			
			
			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_IsCopyNDData");
			cb.setText("是允许从上一个节点Copy数据");
			cb.setChecked(dtl.getIsCopyNDData());
			appendPub(AddTD(cb));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_IsHLDtl");
			cb.setText("是否是合流汇总从表(当前节点是合流节点有效)");
			cb.setChecked(dtl.getIsHLDtl());
			appendPub(AddTD("colspan=2", cb));
			appendPub(AddTREnd());
			
			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("子线程处理人字段"));
			sql = "SELECT KeyOfEn as No, Name FROM Sys_MapAttr WHERE FK_MapData='" + dtl.getNo() + "' AND  ( (MyDataType =1 and UIVisible=1 ) or (UIContralType=1))";
			dt = DBAccess.RunSQLReturnTable(sql);

			ddl = new DDL();
			ddl.setId("DDL_SubThreadWorker");
			ddl.BindSQL(sql, "No", "Name", dtl.getSubThreadWorker());
			appendPub(AddTD("colspan=2", ddl));
			appendPub(AddTDEnd());
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("子线程批次号字段"));
			DataRow dr = dt.NewRow();
			dr.setValue(0, "");
			dr.setValue(1, "无");
			dt.Rows.add(dr);

			ddl = new DDL();
			ddl.setId("DDL_SubThreadGroupMark");
			ddl.BindSQL(sql, "No", "Name", dtl.getSubThreadGroupMark());
			appendPub(AddTD("colspan=2", ddl));
			appendPub(AddTDEnd());
			appendPub(AddTREnd());
			
			
			
			///#region 明细表的数据导入导出.
			appendPub(AddTRSum());
			appendPub(AddTDTitle("colspan=4", "数据导入导出"));
			appendPub(AddTREnd());
			
			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_IsExp");
			cb.setText("是否可以导出？"); // "是否可以导出";
			cb.setChecked(dtl.getIsExp());
			appendPub(AddTD(cb.toString()));

			cb = new CheckBox();
			cb.setId("CB_IsImp");
			cb.setText("是否可以导入？"); // "是否可以导出";
			//cb.setChecked(dtl.getImpModel());
			appendPub(AddTD(cb));
			appendPub(AddTD());
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_" + MapDtlAttr.ImpModel);
			cb.setText("是否启用选择性导入(如果true就要配置数据源呈现的sql)？");
			//cb.setChecked(dtl.ImpModel());
			appendPub(AddTD("colspan=3", cb));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("初始化SQL"));
			tb = new TextBox();
			tb.setId("TB_" + MapDtlAttr.ImpSQLInit);
			tb.setText(dtl.getImpSQLInit());
			tb.setCols(80);
			appendPub(AddTD("colspan=2", tb));
			appendPub(AddTREnd());


			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("查询SQL"));
			tb = new TextBox();
			tb.setId("TB_" + MapDtlAttr.ImpSQLSearch);
			tb.setText(dtl.getImpSQLSearch());
			tb.setCols(80);
			appendPub(AddTD("colspan=2", tb.toString()));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("填充SQL"));
			tb = new TextBox();
			tb.setId("TB_" + MapDtlAttr.ImpSQLFull);
			tb.setText(dtl.getImpSQLFull());
			tb.setCols(80);
			appendPub(AddTD("colspan=2", tb));
			appendPub(AddTREnd());

		///#endregion 明细表的数据导入导出.


		///#region 超连接.
			appendPub(AddTRSum());
			appendPub(AddTDTitle("colspan=4", "表格右边列超连接配置"));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			cb = new CheckBox();
			cb.setId("CB_" + MapDtlAttr.IsEnableLink);
			cb.setText("是否启用超连接？");
			cb.setChecked(dtl.getIsEnableLink());
			appendPub(AddTD(cb.toString()));
			appendPub(AddTD("超连接标签"));

			tb = new TextBox();
			tb.setId("TB_" + MapDtlAttr.LinkLabel);
			tb.setText(dtl.getLinkLabel());
			appendPub(AddTD(tb.toString()));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("colspan=3", "连接URL"));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			tb = new TextBox();
			tb.setId("TB_" + MapDtlAttr.LinkUrl);
			tb.setText(dtl.getLinkUrl());
			tb.setCols(90);
			appendPub(AddTD("colspan=3", tb.toString()));
			appendPub(AddTREnd());


			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD("连接目标"));
			tb = new TextBox();
			tb.setId("TB_" + MapDtlAttr.LinkTarget);
			tb.setText(dtl.getLinkTarget());
			appendPub(AddTD("colspan=2", tb.toString()));
			appendPub(AddTREnd());
		///#endregion 超连接.


			GroupFields gfs = new GroupFields(md.getNo());
			if (gfs.size() > 1)
			{
				appendPub(AddTR1());
				appendPub(AddTDIdx(idx++));
				appendPub(AddTD("显示在分组"));
				ddl = new DDL();
				ddl.setId("DDL_GroupID");
				ddl.BindEntities(gfs, GroupFieldAttr.OID, GroupFieldAttr.Lab, false, AddAllLocation.None);
				ddl.SetSelectItem(dtl.getGroupID());
				appendPub(AddTD("colspan=2", ddl));
				appendPub(AddTREnd());
			}
			if (gfs.size() > 1)
			{
				appendPub(AddTR());
			}
			else
			{
				appendPub(AddTR1());
			}

			appendPub(AddTRSum());
			appendPub(AddTD(""));
			appendPub(AddTDBegin("colspan=3 align=center"));

			Button btn = new Button();
			btn.setId("Btn_Save");
			btn.setCssClass("Btn");
			btn.setText(" 保存 ");
			btn.attributes.put("onclick", "save_map_def_dtl();");
			appendPub(btn.toString());

			btn = new Button();
			btn.setId("Btn_SaveAndClose");
			btn.setCssClass("Btn");
			btn.setText(" 保存并关闭 ");
			btn.attributes.put("onclick", "save_cancel_map_def_dtl();");
			appendPub(btn.toString());

			if (this.getFK_MapDtl() != null)
			{

				btn = new Button();
				btn.setId("Btn_Del");
				btn.setCssClass("Btn");
				btn.setText("删除");
				btn.attributes.put("onclick", "del_map_def_dtl();");
				appendPub(btn.toString());


				btn = new Button();
				btn.setId("Btn_New");
				btn.setCssClass("Btn");
				btn.setText("新建"); 
				btn.attributes.put("onclick", "new_map_def_dtl();");
				appendPub(btn.toString());

				/*btn = new Button();
				btn.setId("Btn_MapExt");
				btn.setCssClass("Btn");
				btn.setText("扩展设置"); 
				btn.attributes.put("onclick", "mapext_map_def_dtl();");
				appendPub(btn.toString());*/

				/*if (dtl.getIsEnableAthM())
				{
					btn = new Button();
					btn.setCssClass("Btn");
					btn.setId("Btn_IsEnableAthM");
					btn.setText("附件属性"); 
					btn.attributes.put("onclick", "mapath_map_def_dtl();");
					appendPub(btn.toString());
				}*/
				if (dtl.getIsEnableAthM())
				{
					btn = new Button();
					btn.setCssClass("Btn");
					btn.setId("Btn_IsEnableAthM");
					btn.setText("附件属性"); // "删除";
					//btn.Click += new EventHandler(btn_MapAth_Click);
					btn.attributes.put("onclick", "mapath_map_def_dtl();");
					appendPub(btn.toString());
				}

			}
			appendPub(AddTDEnd());
			appendPub(AddTREnd());
			appendPub(AddTableEnd());
		}

}
