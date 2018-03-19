package cn.jflow.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;

@Controller
@RequestMapping(value = "/WF/TBFullCtrlDtl")
public class TBFullCtrlDtlController {

	// /#region 属性。
	
	public String FK_MapData="";

	public String OperAttrKey="";

	public String MyPK="";
	
	public String RefNo="";

	public String Lab = null;

	@RequestMapping(value = "/Btn_Save_Click", method = RequestMethod.POST)
	public void Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response,String TB_SQL) {
		MyPK=request.getParameter("MyPK");
		
		MapExt myme = new MapExt(this.MyPK);
		MapDtls dtls = new MapDtls(myme.getFK_MapData());


		String info = "";
		String error = "";
		for (MapDtl dtl : MapDtls.convertMapDtls(dtls))
		{

			if (TB_SQL.trim().equals(""))
			{
				continue;
			}

			info += "$" + dtl.getNo() + ":" + TB_SQL;
		}

		if (!error.equals(""))
		{

			 throw new RuntimeException("设置错误，请更正:<br/>"+error+"");
		}
		myme.setTag1(info);
		myme.Update();

	}
}
