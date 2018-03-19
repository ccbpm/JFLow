package cn.jflow.controller.wf.ccform;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.MapDtl;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/WF/CCForm")
public class FrmDtlController {
	

	private HashMap<String, BaseWebControl> ctrlMap;
	
	@RequestMapping(value = "/FrmDtlSave", method = RequestMethod.POST)
	public void Btn_Save_Click(HttpServletRequest request,HttpServletResponse response) {

		try {
			//UCEn1=new EnModel(request , response);
			//ctrlMap = HtmlUtils.httpParser(ContextHolderUtils.getRequest().getParameter("BodyHtml"), true);
		//	String s=request.getParameter(name)
			ctrlMap = HtmlUtils.httpParser(request.getParameter("bodyHtml"), request);
			MapDtl dtl = new MapDtl(this.getFK_MapData(request));
			GEDtl dtlEn = dtl.getHisGEDtl();
			dtlEn.SetValByKey("OID", this.getOID(request));
			int i = dtlEn.RetrieveFromDBSources();
			Object tempVar =  BaseModel.Copy(request, dtlEn, null, dtlEn.getEnMap(), ctrlMap);
			dtlEn = (GEDtl)((tempVar instanceof GEDtl) ? tempVar : null);
	 		dtlEn.SetValByKey(GEDtlAttr.RefPK, getWorkID(request));

			if (i == 0) {
				dtlEn.setOID(0);
				//dtlEn.Save();
				dtlEn.Insert();
			}
			else {
				dtlEn.Update();
			}

			
			try {
				//  response.sendRedirect("FrmDtl.jsp?WorkID=" + dtlEn.getRefPK() + "&FK_MapData=" + this.getFK_MapData(request) + "&IsReadonly=" + this.getIsReadonly(request) + "&OID=" + dtlEn.getOID());
				PrintWriter out = response.getWriter();
				out.print("保存成功");
				//response.sendRedirect("FrmDtl.jsp?WorkID=" + dtlEn.getRefPK() + "&FK_MapData=" + this.getFK_MapData(request) + "&IsReadonly=" + this.getIsReadonly(request) + "&OID=" + dtlEn.getOID());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		catch (RuntimeException ex) {
			//this.UCEn1.AddMsgOfWarning("error:", ex.getMessage());
		}
		
		
	
	}
	
	public final String getFK_MapData(HttpServletRequest request) {
		String s =request.getParameter("FK_MapData");
		if (s == null) {
			return "ND101";
		}
		return s;
	}
	public final int getOID(HttpServletRequest request) {
		try {
			String oID=request.getParameter("OID");
			return Integer.parseInt(oID);
		}
		catch (java.lang.Exception e) {
			return 0;
		}
	}
	
	public  long getWorkID(HttpServletRequest request)
	{
		String str = request.getParameter("WorkID");
		if (StringHelper.isNullOrEmpty(str))
		{
			str = request.getParameter("OID");
		}

		if (StringHelper.isNullOrEmpty(str))
		{
			str = request.getParameter("PKVal");
		}

		return Long.parseLong(str);
	}
	public final boolean getIsReadonly(HttpServletRequest request) {
		String isReadonly=request.getParameter("IsReadonly");
		if (isReadonly.equals("1")) {
			return true;
		}
		return false;
	}
}
