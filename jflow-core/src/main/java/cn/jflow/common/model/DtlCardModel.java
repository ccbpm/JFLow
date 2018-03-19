package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.*;
import BP.Sys.*;
import BP.Sys.MapData;
import BP.Sys.MapDtl;

public class DtlCardModel extends EnModel {
	public StringBuffer UCEn1;
	public DtlCardModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		UCEn1 = new StringBuffer();
	}

	public final int getaddRowNum() {
			try {
				String addRowNum=getParameter("addRowNum");
				String IsCut=getParameter("IsCut");
				int i = Integer.parseInt(addRowNum);
				if (IsCut == null) {
					return i;
				}
				else {
					return i;
				}
			}
			catch (java.lang.Exception e) {
				return 0;
			}
		}

	public final String getRefPKVal() {
		String str = getParameter("RefPKVal");
		if (str == null) {
			return "1";
		}
		return str;
	}

	public final boolean getIsReadonly() {
		String isReadonly=getParameter("IsReadonly");
		if(isReadonly!=null){
			if (isReadonly.equals("1")) {
				return true;
			}
		}
		return false;
	}

	public final void Page_Load() {

		// /#start 查询出来从表.
		String ensName=getParameter("EnsName");
		MapDtl mdtl = new MapDtl(ensName);
		GEDtls dtls = new GEDtls(ensName);
		QueryObject qo = null;
		try {
			qo = new QueryObject(dtls);
			switch (mdtl.getDtlOpenType()) {
			//case DtlOpenType.ForEmp:
			case ForEmp:
				qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
				break;
			case ForWorkID:
				qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
				break;
			case ForFID:
				qo.AddWhere(GEDtlAttr.FID, this.getRefPKVal());
				break;
			}
			qo.DoQuery();
		} catch (RuntimeException ex) {
			dtls.getGetNewEntity().CheckPhysicsTable();
			throw ex;
		}

		// /#start 初始化空白行
		if (this.getIsReadonly() == false) {
			mdtl.setRowsOfList(mdtl.getRowsOfList() + this.getaddRowNum());
			int num = dtls.size();
			if (mdtl.getIsInsert()) {
				int dtlCount = dtls.size();
				for (int i = 0; i < mdtl.getRowsOfList() - dtlCount; i++) {
					BP.Sys.GEDtl dt = new GEDtl(ensName);
					dt.ResetDefaultVal();
					dt.setOID(i);
					dtls.AddEntity(dt);
				}

				if (num == mdtl.getRowsOfList()) {
					BP.Sys.GEDtl dt1 = new GEDtl(ensName);
					dt1.ResetDefaultVal();
					dt1.setOID(mdtl.getRowsOfList() + 1);
					dtls.AddEntity(dt1);
				}
			}
		}
		// /#end 初始化空白行

		MapData md = new MapData(mdtl.getNo());
		UCEn1.setLength(0);

		this.UCEn1.append("\t\n<div class=\"easyui-tabs\" fit=\"true\" border=\"false\" style='width:"
						+ md.getFrmW()
						+ "px;height:"
						+ md.getFrmH()
						+ "px;' data-options=\"tools:'#tab-tools'\">"); 
		

		// /#start 输出标签.
		int idx = 0;
		int dtlsNum = dtls.size();

		for (GEDtl dtl : dtls.ToJavaList()) {
			idx++;
			this.UCEn1.append("\t\n<div id=" + idx + " title='第" + idx
					+ "条' style='overflow: auto;'>");
			String src = "";
			src = "FrmDtl.jsp?FK_MapData=" + ensName + "&WorkID="
					+ this.getRefPKVal() + "&OID=" + dtl.getOID() + "&IsReadonly="
					+ this.getIsReadonly();
			this.UCEn1.append("\t\n<iframe id='IF" + idx
					+ "' Onblur=\"SaveDtlData('" + idx
					+ "');\" frameborder='0' style='width:" + md.getFrmW()
					+ "px;height:" + md.getFrmH() + "px;' src=\"" + src
					+ "\"></iframe>");
			this.UCEn1.append("\t\n</div>");
		}
		this.UCEn1.append("\t\n </div>");
		if (this.getIsReadonly() == false && mdtl.getIsInsert()) {
			int addNum = getaddRowNum() + 1;
			int cutNum = getaddRowNum() - 1;

			this.UCEn1.append("\t\n<div id=\"tab-tools\">");
			if (cutNum >= 0) {
				this.UCEn1.append("\t\n<a href='DtlCard.jsp?EnsName="
								+ ensName
								+ "&RefPKVal="
								+ this.getRefPKVal()
								+ "&addRowNum="
								+ cutNum
								+ "' class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-reload'\">移除</a>");
				this.UCEn1.append("\t\n<a href='DtlCard.jsp?EnsName="
								+ ensName
								+ "&RefPKVal="
								+ this.getRefPKVal()
								+ "&addRowNum="
								+ addNum
								+ "' class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'icon-reload'\">插入</a>");
			} else {
				this.UCEn1.append("\t\n<a href='DtlCard.jsp?EnsName="
						+ ensName + "&RefPKVal=" + this.getRefPKVal()
						+ "&addRowNum=" + addNum + "' >插入</a>");
			}
			this.UCEn1.append("\t\n</div>");
		}
		// /#end 输出标签.
	}

}
