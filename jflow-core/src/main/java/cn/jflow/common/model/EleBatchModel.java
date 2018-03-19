package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapData;
import BP.Sys.MapDatas;

public class EleBatchModel extends BaseModel {
	public EleBatchModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public StringBuilder Pub1 = new StringBuilder();
	public StringBuilder Left = new StringBuilder();

	public final String getKeyOfEn() {
		return get_request().getParameter("KeyOfEn");
	}

	public final String getEleType() {
		return get_request().getParameter("EleType");
	}

	public final String getFK_Flow() {
		String str = this.getFK_MapData();
		str = str.replace("ND", "");
		str=StringUtils.leftPad(str,5, "0");
//		str = str.PadLeft(5, '0');
		str = str.substring(0, 3);
		return str;
	}

	
	// /#endregion 属性.

	public void init() {
		if (!this.getFK_MapData().substring(0, 2).equals("ND")) {
			this.Pub1.append(AddFieldSetRed("错误", "err:只有节点表单才可以执行 "));
			return;
		}

		
		// /#region 菜单
		this.Left.append(AddHR());
		this.Left.append(AddUL());
		if (this.getDoType().equals("Copy")) {
			this.Left.append(AddLiB(
					"EleBatch.jsp?EleType=" + this.getEleType()
							+ "&FK_MapData=" + this.getFK_MapData()
							+ "&KeyOfEn=" + this.getKeyOfEn() + "&DoType=Copy",
					"批量复制"));
		} else {
			this.Left.append(AddLi("EleBatch.jsp?EleType=" + this.getEleType()
					+ "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn="
					+ this.getKeyOfEn() + "&DoType=Copy", "批量复制"));
		}

		if (this.getDoType().equals("Update")) {
			this.Left.append(AddLiB(
					"EleBatch.jsp?EleType=" + this.getEleType()
							+ "&FK_MapData=" + this.getFK_MapData()
							+ "&KeyOfEn=" + this.getKeyOfEn()
							+ "&DoType=Update", "批量更新"));
		} else {
			this.Left.append(AddLi("EleBatch.jsp?EleType=" + this.getEleType()
					+ "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn="
					+ this.getKeyOfEn() + "&DoType=Update", "批量更新"));
		}

		if (this.getDoType().equals("Delete")) {
			this.Left.append(AddLiB(
					"EleBatch.jsp?EleType=" + this.getEleType()
							+ "&FK_MapData=" + this.getFK_MapData()
							+ "&KeyOfEn=" + this.getKeyOfEn()
							+ "&DoType=Delete", "批量删除"));
		} else {
			this.Left.append(AddLi("EleBatch.jsp?EleType=" + this.getEleType()
					+ "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn="
					+ this.getKeyOfEn() + "&DoType=Delete", "批量删除"));
		}
		this.Left.append(AddULEnd());
		
		// /#endregion

		if (this.getDoType().equals("")) {
			this.Pub1.append(AddFieldSet("表单元素的批量处理",
					"仅用节点表单，它包括如下几种处理方式<BR>1，批量更新元素属性。<BR>2，批量增加。<BR>3、批量删除。"));
			return;
		}

		// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
		// string member and was converted to Java 'if-else' logic:
		// switch (this.EleType)
		// ORIGINAL LINE: case "MapAttr":
		if (this.getEleType().equals("MapAttr")) {
			this.MapAttr();
		} else {
		}
	}

	public MapDatas getGetMDs() {
		String sql = "SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getFK_Flow() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String nds = "";
		for (DataRow dr : dt.Rows) {
			nds += ",'ND" + dr.getValue(0).toString() + "'";
		}

		sql = "SELECT No FROM Sys_MapData WHERE No IN (" + nds.substring(1)
				+ ")";
		dt = DBAccess.RunSQLReturnTable(sql);

		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL(sql);

		return mds;
	}

	public String getLabel() {
		// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
		// string member and was converted to Java 'if-else' logic:
		// switch (this.DoType)
		// ORIGINAL LINE: case "Copy":
		if (this.getDoType().equals("Copy")) {
			return "复制";
		}
		// ORIGINAL LINE: case "Update":
		else if (this.getDoType().equals("Update")) {
			return "更新";
		}
		// ORIGINAL LINE: case "Delete":
		else if (this.getDoType().equals("Delete")) {
			return "删除";
		} else {
			return "";
		}
	}

	/** 
 
 
*/
	public void MapAttr() {
		MapDatas mds = this.getGetMDs();

		this.Pub1.append(AddTable());
		this.Pub1.append(AddCaptionLeftTX1("批量:" + this.getLabel()));

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("表单ID"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("操作"));
		this.Pub1.append(AddTREnd());

		for (MapData md : mds.ToJavaList()) {
			// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
			// string member and was converted to Java 'if-else' logic:
			// switch (this.DoType)
			// ORIGINAL LINE: case "Copy":
			if (this.getDoType().equals("Copy")) {
				if (md.getMapAttrs().Contains(MapAttrAttr.KeyOfEn,
						this.getKeyOfEn()) == true) {
					continue;
				}
			}
			// ORIGINAL LINE: case "Update":
			else if (this.getDoType().equals("Update")) {
				if (md.getMapAttrs().Contains(MapAttrAttr.KeyOfEn,
						this.getKeyOfEn()) == false) {
					continue;
				}
				if (this.getFK_MapData().equals(md.getNo())) {
					continue;
				}

			}
			// ORIGINAL LINE: case "Delete":
			else if (this.getDoType().equals("Delete")) {
				if (md.getMapAttrs().Contains(MapAttrAttr.KeyOfEn,
						this.getKeyOfEn()) == false) {
					continue;
				}
			} else {
			}

			this.Pub1.append(AddTR());
			CheckBox cb = new CheckBox();
			cb.setId("CB_" + md.getNo());
			cb.setName("CB_" + md.getNo());
			cb.setText(md.getName());
			this.Pub1.append(AddTD(cb));
			this.Pub1.append(AddTD(md.getName()));
			this.Pub1
					.append(AddTD("<a href=''>预览自由表单</a> - <a href=''>设计自由表单</a>"));
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());

		this.Pub1.append(AddHR());
		Button btn = new Button();
		btn.setId("Btn");
		btn.setName("Btn");
		btn.setText("执行批量[" + this.getLabel() + "]操作");
		
		// event wireups:
		// btn.Click += new EventHandler(btn_Click);
		btn.addAttr("onclick", "btn_Click('Btn')");
		this.Pub1.append(btn);
	}
}
