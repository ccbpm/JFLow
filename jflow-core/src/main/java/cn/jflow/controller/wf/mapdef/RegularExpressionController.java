package cn.jflow.controller.wf.mapdef;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.XML.RegularExpressionDtl;
import BP.Sys.XML.RegularExpressionDtls;

/**
 * 
 * @author yuqinghai
 *
 */
@Controller
@RequestMapping(value = "/WF/RegularExpression")
public class RegularExpressionController {

	
	/**
	 * Save Event
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public String fk_MapData;
	public String RefNo;
	public String OperAttrKey;
	public String event;
	public String ExtType;
	StringBuffer Pub1 = new StringBuffer();
	
	@ResponseBody
	@RequestMapping(value = "/ActionBtn_Click", method = RequestMethod.POST)
	public void btn_Click(HttpServletRequest request,
			HttpServletResponse response,String mapx) throws IOException {
		
		fk_MapData = request.getParameter("FK_MapData");
		RefNo = request.getParameter("RefNo");
		ExtType = MapExtXmlList.RegularExpression;
		
		String map2 = mapx.split("-")[0].substring(0, mapx.split("-")[0].length()-1);
		String map1 = mapx.split("-")[1].substring(0, mapx.split("-")[1].length()-1);
		String[] kv1 = map1.split(";");
		String[] kv2 = map2.split(";");
		for(String tem1:kv1){
			for(String tem2:kv2){
				if(tem1.split("@").length<2||tem1.split("@").length<2){
					continue;
				}
				//hm2.put(tem.split("@")[0], tem.split("@")[1]);
				if(tem1.split("@")[0].contains("onblur")&&tem2.split("@")[0].contains("onblur")){
					BindRegularExpressionEdit_ClickSave("onblur",tem1.split("@")[1],tem2.split("@")[1]);
				}else if(tem1.split("@")[0].contains("onchange")&&tem2.split("@")[0].contains("onchange")){
					BindRegularExpressionEdit_ClickSave("onchange",tem1.split("@")[1],tem2.split("@")[1]);
				}else if(tem1.split("@")[0].contains("onclick")&&tem2.split("@")[0].contains("onclick")){
					BindRegularExpressionEdit_ClickSave("onclick",tem1.split("@")[1],tem2.split("@")[1]);
				}else if(tem1.split("@")[0].contains("ondblclick")&&tem2.split("@")[0].contains("ondblclick")){
					BindRegularExpressionEdit_ClickSave("ondblclick",tem1.split("@")[1],tem2.split("@")[1]);
				}else if(tem1.split("@")[0].contains("onkeypress")&&tem2.split("@")[0].contains("onkeypress")){
					BindRegularExpressionEdit_ClickSave("onkeypress",tem1.split("@")[1],tem2.split("@")[1]);
				}else if(tem1.split("@")[0].contains("onkeyup")&&tem2.split("@")[0].contains("onkeyup")){
					BindRegularExpressionEdit_ClickSave("onkeyup",tem1.split("@")[1],tem2.split("@")[1]);
				}else if(tem1.split("@")[0].contains("onsubmit")&&tem2.split("@")[0].contains("onsubmit")){
					BindRegularExpressionEdit_ClickSave("onsubmit",tem1.split("@")[1],tem2.split("@")[1]);
				}
			}
		}
	}
	public final void BindRegularExpressionEdit_ClickSave(String myEvent,String doc ,String tag)
	{
		MapExt me = new MapExt();
		me.setFK_MapData(fk_MapData);
		me.setExtType(ExtType);
		me.setTag(myEvent);
		me.setAttrOfOper(this.RefNo);
		me.setMyPK(fk_MapData + "_" + this.RefNo + "_" + me.getExtType() + "_" + me.getTag());
		me.Delete();

		me.setDoc(doc);
		me.setTag1(tag);
		if (me.getDoc().trim().length() == 0)
		{
			return;
		}

		me.Insert();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/btn_SaveReTemplete_Click", method = RequestMethod.POST)
	public void btn_SaveReTemplete_Click(HttpServletRequest request,
			HttpServletResponse response,String LBReTemplete,String FK_MapData,String RefNo) throws IOException {
		/*Object tempVar = this.Pub1.getFindControl("LBReTemplete");
		ListBox lb = (ListBox)((tempVar instanceof ListBox) ? tempVar : null);
		if (lb == null && lb.SelectedItem.getValue() == null)
		{
			return;
		}*/
		if(LBReTemplete==null){
			return;
		}
		//String[] keys = LBReTemplete.split(",");
		fk_MapData = FK_MapData;
		this.RefNo = RefNo;
		ExtType = MapExtXmlList.RegularExpression;
		
		String newMyPk = "";
		RegularExpressionDtls reDtls = new RegularExpressionDtls();
		reDtls.RetrieveAll();

		//删除现有的逻辑.
		MapExts exts = new MapExts();
		exts.Delete(MapExtAttr.AttrOfOper, this.RefNo, MapExtAttr.ExtType, MapExtXmlList.RegularExpression);

		// 开始装载.
		for (RegularExpressionDtl dtl : RegularExpressionDtls.convertRegularExpressionDtls(reDtls))
		{
			if (!dtl.getItemNo().equals(LBReTemplete))
			{
				continue;
			}

			MapExt ext = new MapExt();
			ext.setMyPK(fk_MapData + "_" + RefNo + "_" + MapExtXmlList.RegularExpression + "_" + dtl.getForEvent());
			ext.setFK_MapData(fk_MapData);
			ext.setAttrOfOper(RefNo);
			ext.setDoc(dtl.getExp()); //表达公式.
			ext.setTag(dtl.getForEvent()); //时间.
			ext.setTag1(dtl.getMsg()); //消息
			ext.setExtType(MapExtXmlList.RegularExpression); // 表达公式.
			ext.Insert();
			newMyPk = ext.getMyPK();
		}
		//this.Response.Redirect("RegularExpression.aspx?FK_MapData=" + this.getFK_MapData() + "&RefNo="+this.RefNo+"&ExtType=" + this.getExtType() + "&MyPK=" + newMyPk + "&OperAttrKey=" + this.getOperAttrKey() + "&DoType=New", true);
	}

	
}
