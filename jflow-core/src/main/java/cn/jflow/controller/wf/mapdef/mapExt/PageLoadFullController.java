package cn.jflow.controller.wf.mapdef.mapExt;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/mapdef/mapExt")
@Scope("request")
public class PageLoadFullController extends BaseController {

	@RequestMapping(value = "/btnSave", method = { RequestMethod.POST })
	public ModelAndView btnSave(HttpServletRequest request,
			HttpServletResponse response) {

		String FK_MapData = this.getParamter("FK_MapData");
		MapExt me = new MapExt();
		me.setMyPK(FK_MapData + "_" + MapExtXmlList.PageLoadFull);
		me.setFK_MapData(FK_MapData);
		me.setExtType(MapExtXmlList.PageLoadFull);
		me.RetrieveFromDBSources();

		String tbStr = getParamter("TB_" + MapExtAttr.Tag);

		me.setTag(tbStr);
		String sql = "";
		MapDtls dtls = new MapDtls(FK_MapData);

		tbStr = null;
		for (MapDtl dtl : dtls.ToJavaList()) {
			tbStr = getParamter("TB_" + dtl.getNo());
			sql += "*" + dtl.getNo() + "=" + tbStr;
		}
		me.setTag1(sql);

		me.setMyPK(FK_MapData + "_" + MapExtXmlList.PageLoadFull);

		String info = me.getTag1() + me.getTag();
		if (StringHelper.isNullOrEmpty(info)) {
			me.Delete();
		} else {
			me.Save();
		}
		
		String url="../../Admin/FoolFormDesigner/MapExt/PageLoadFull.jsp?s=34&FK_MapData="+FK_MapData+"&ExtType=PageLoadFull&RefNo=";
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:" + url);

		return mv;
	}
	
	@RequestMapping(value = "/scriptBtnSave", method = { RequestMethod.POST })
	public ModelAndView scriptBtnSave(HttpServletRequest request,
			HttpServletResponse response) {

		String txt = getParamter("TB_Doc").trim();
		String FK_MapData = this.getParamter("FK_MapData");
		
		if (StringHelper.isNullOrEmpty(txt)) {
			String path = SystemConfig.getPathOfDataUser() + "JSLibData/"
					+ FK_MapData + "_Self.js";
		
			File file = new File(path);

			if (file.isFile() && file.exists()) {
		     file.delete();
			}
		
		} else {
			String path = SystemConfig.getPathOfDataUser() + "JSLibData/"
					+ FK_MapData + "_Self.js";
			BP.DA.DataType.WriteFile(path, txt);
		}
		
		String url = "../../Admin/FoolFormDesigner/MapExt/InitScript.jsp?s=34&FK_MapData="
				+ FK_MapData + "&ExtType=PageLoadFull&RefNo=";
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:" + url);

		return mv;
	}
}
