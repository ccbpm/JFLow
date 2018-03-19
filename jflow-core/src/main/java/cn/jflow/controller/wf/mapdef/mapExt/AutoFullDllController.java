package cn.jflow.controller.wf.mapdef.mapExt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/wf/autoFullDll")
public class AutoFullDllController extends BaseController{

	/**
	 * @Description:自动填充保存
	 * @Title: btn_Save_Click
	 * @param object
	 * @param request
	 * @param response
	 * @author peixiaofeng
	 * @date 2016年5月24日
	 */
	@RequestMapping(value = "/btn_Save_Click", method = RequestMethod.POST)
	@ResponseBody
	public String btn_Save_Click(TempObject object,HttpServletRequest request, HttpServletResponse response) {
		String myPk=request.getParameter("myPk");
		String RefNo=request.getParameter("RefNo");
		String FK_MapData=request.getParameter("FK_MapData");
		String tbSql=request.getParameter("tbSql");
		String DDL_DBSrc=request.getParameter("DDL_DBSrc");
		MapExt me = new MapExt();
		me.Retrieve(MapExtAttr.FK_MapData, FK_MapData, MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, RefNo);
		me.setExtType( MapExtXmlList.AutoFullDLL);
		me.setDoc(tbSql);
		me.setAttrOfOper(RefNo);
		me.setFK_MapData(FK_MapData) ;
		//me.setMyPK(FK_MapData + "_"+me.getExtType()+ "_" +me.getAttrOfOper());
		me.setMyPK(me.getExtType()+FK_MapData+"_"+RefNo);
		me.setDBSrc(DDL_DBSrc);
		me.Save();
		return "success";
	}
	
	/**
	 * @Description: 删除操作
	 * @Title: Btn_Delete_Click
	 * @param object
	 * @param request
	 * @param response
	 * @author peixiaofeng
	 * @date 2016年5月24日
	 */
	@RequestMapping(value = "/Btn_Delete_Click", method = RequestMethod.POST)
	@ResponseBody
	public String Btn_Delete_Click(TempObject object,HttpServletRequest request, HttpServletResponse response) {
		String myPk=request.getParameter("myPk");
		String RefNo=request.getParameter("RefNo");
		String FK_MapData=request.getParameter("FK_MapData");
		MapExt me = new MapExt();
		me.Retrieve(MapExtAttr.FK_MapData, FK_MapData, MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, RefNo);
		me.setExtType( MapExtXmlList.AutoFullDLL);
		me.setDoc("");
		me.setAttrOfOper(RefNo);
		me.setFK_MapData(FK_MapData) ;
		//me.setMyPK(FK_MapData + "_"+me.getExtType()+ "_" +me.getAttrOfOper());
		me.setMyPK(me.getExtType()+FK_MapData+"_"+RefNo);
		me.setDBSrc("");
		me.Update();
		return "success";
	}
}
