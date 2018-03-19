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
import BP.Tools.StringHelper;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/wf/activeDDL")
public class ActiveDDLController extends BaseController{

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
		String msg="";
		String myPk=request.getParameter("myPk");
		String RefNo=request.getParameter("RefNo");
		String FK_MapData=request.getParameter("FK_MapData");
		String TB_Doc=request.getParameter("TB_Doc");
		String DDL_Attr=request.getParameter("DDL_Attr");
		String ctl12=request.getParameter("ctl12");
		String RB=request.getParameter("RB");
		MapExt me = new MapExt();
		
		//保存当前选择前 删除掉之前的。保证数据库只有一个级联动
		if(!StringHelper.isNullOrEmpty(myPk))
		{
			me.Delete(MapExtAttr.FK_MapData,FK_MapData,MapExtAttr.ExtType,MapExtXmlList.ActiveDDL
					,MapExtAttr.AttrOfOper,RefNo);
		}
		
		me.setMyPK(myPk);
		if (me.getMyPK().length() > 2) {
			me.RetrieveFromDBSources();
		}
		//me = Copy(me);
		me.setExtType(MapExtXmlList.ActiveDDL);
		me.setDoc(TB_Doc);
		me.setAttrOfOper(RefNo);
		me.setAttrsOfActive(DDL_Attr);
		if (me.getAttrsOfActive() == me.getAttrOfOper()) {
			msg="两个项目不能相同.";
			return msg;
		}
		
		if(StringHelper.isNullOrEmpty(RB)){
			me.setDoWay(0);
		}else {
			me.setDoWay(Integer.parseInt(RB));
		}
		me.setDBSrc(ctl12);
		me.setFK_MapData(FK_MapData);
		try {
			me.setMyPK(FK_MapData+ "_" + me.getExtType() + "_" + me.getAttrOfOper() + "_" + me.getAttrsOfActive());
			if (me.getDoc().contains("No") == false || me.getDoc().contains("Name") == false) {
				msg="在您的SQL表达式里，必须有No,Name 还两个列，分别标识编码与名称。";
				return msg;
			}
			me.Save();
		}
		catch (RuntimeException ex) {
			msg=ex.getMessage();
			return msg;
		}

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
		me.Delete(MapExtAttr.FK_MapData, FK_MapData, MapExtAttr.ExtType, MapExtXmlList.ActiveDDL, MapExtAttr.AttrOfOper, RefNo);
		return "success";
	}
}
