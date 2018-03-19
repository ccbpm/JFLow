package cn.jflow.model.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;

public class HidAttrModel extends BaseModel {

	public StringBuilder pub;

	private void appendPub(String str) {
		pub.append(str);
	}

	public HidAttrModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		pub = new StringBuilder();
	}

	public void pageLoad() {
		appendPub(AddTable(" width='80%' "));
		appendPub(AddCaptionLeft("隐藏字段"));
		appendPub(AddTR());
		appendPub(AddTH("IDX"));
		appendPub(AddTH("字段"));
		appendPub(AddTH("名称(点击编辑)"));
		appendPub(AddTH("类型"));
		appendPub(AddTREnd());
		MapAttrs mattrs = new MapAttrs(this.getFK_MapData());
		// GroupFields gfs = new GroupFields(this.getFK_MapData());
		// String msg = "";
		int idx = 0;
		for (MapAttr attr : mattrs.ToJavaList()) {
			if (attr.getUIVisible()) {
				continue;
			}
			String keyOfEn = attr.getKeyOfEn();
			if (keyOfEn.equals("BatchID") || keyOfEn.equals("OID")
					|| keyOfEn.equals("FID") || keyOfEn.equals("FK_NY")
					|| keyOfEn.equals("RefPK") || keyOfEn.equals("Emps")
					|| keyOfEn.equals("FK_Dept") || keyOfEn.equals("WFState")
					|| keyOfEn.equals("RDT") || keyOfEn.equals("MyNum")
					|| keyOfEn.equals("Rec") || keyOfEn.equals("CDT")) {
				continue;
			} else {
			}
			appendPub(AddTR());
			appendPub(AddTDIdx(idx++));
			appendPub(AddTD(keyOfEn));
			
			switch (attr.getLGType()) {
			case Normal:
				appendPub(AddTD("<a href=\"javascript:Edit('"
						+ attr.getFK_MapData() + "','" + attr.getMyPK() + "','"
						+ attr.getMyDataType() + "');\">" + attr.getName()
						+ "</a>"));
				break;
			case Enum:
				appendPub(AddTD("<a href=\"javascript:EditEnum('"
						+ attr.getFK_MapData() + "','" + attr.getMyPK() + "','"
						+ attr.getMyDataType() + "');\">" + attr.getName()
						+ "</a>"));
				break;
			case FK:
				appendPub(AddTD("<a href=\"javascript:EditTable('"
						+ attr.getFK_MapData() + "','" + attr.getMyPK() + "','"
						+ attr.getMyDataType() + "');\">" + attr.getName()
						+ "</a>"));
				break;
			default:
				break;
			}
			appendPub(AddTD(attr.getLGType().toString()));
			appendPub(AddTREnd());
		}
		appendPub(AddTableEndWithHR());
	}

}
