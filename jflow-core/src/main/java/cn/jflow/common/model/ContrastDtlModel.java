package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;

public class ContrastDtlModel extends BaseModel {

	private String FK_Dept;
	public StringBuilder UCSys1;

	public ContrastDtlModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		UCSys1=new StringBuilder();
	}

	public final void Page_Load() {
		this.BindData();
	}

	public final String getFK_Dept() {
		return FK_Dept;
	}

	public final void setFK_Dept(String value) {
		String val = value;
		if (val.equals("all")) {
			return;
		}

		if (this.getFK_Dept() == null) {
			this.FK_Dept = value;
			return;
		}

		if (this.getFK_Dept().length() > val.length()) {
			return;
		}

		FK_Dept = value;
	}

	private String privateShowTitle;

	public final String getShowTitle() {
		return privateShowTitle;
	}

	public final void setShowTitle(String value) {
		privateShowTitle = value;
	}

	public final void BindData() {
		
		String ensname = getParameter("EnsName");
		Entities ens = ClassFactory.GetEns(ensname);
		Entity en = ens.getGetNewEntity();

		QueryObject qo = new QueryObject(ens);
		String url = BP.Sys.Glo.getRequest().getRemoteAddr();
		String[] strs = url.split("[&]", -1);
		// String[] strs1 = this.Request.RawUrl.split("[&]", -1);
		String[] strs1 = url.split("[&]", -1);
		int i = 0;
		for (String str : strs) {
			if (str.indexOf("EnsName") != -1) {
				continue;
			}

			String[] mykey = str.split("[=]", -1);
			String key = mykey[0];

			if (key.equals("OID") || key.equals("MyPK")) {
				continue;
			}

			if (key.equals("FK_Dept")) {
				this.setFK_Dept(mykey[1]);
				continue;
			}

			if (en.getEnMap().getAttrs().Contains(key) == false) {
				continue;
			}

			qo.AddWhere(mykey[0], mykey[1]);
			qo.addAnd();
		}

		if (this.getFK_Dept() != null && (getParameter("FK_Emp") == null || getParameter("FK_Emp").equals("all"))) {
			
			if (this.getFK_Dept().length() == 2) {
				qo.AddWhere("FK_Dept", " = ", "all");
				qo.addAnd();
			} else {
				
				if (this.getFK_Dept().length() == 8) {
					qo.AddWhere("FK_Dept", " = ", this.getFK_Dept());
				} else {
					qo.AddWhere("FK_Dept", " like ", this.getFK_Dept() + "%");
				}

				qo.addAnd();
			}
		}

		qo.AddHD();
		int num = qo.DoQuery();
	
		setShowTitle(ens.getGetNewEntity().getEnMap().getEnDesc() + "，数据：" + num + " 条");
		DataPanelDtl(UCSys1, ens, null);
	}
	
	
}
