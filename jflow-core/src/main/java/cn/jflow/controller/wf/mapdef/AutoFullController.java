package cn.jflow.controller.wf.mapdef;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.En.Attr;
import BP.En.Attrs;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import cn.jflow.common.model.TempObject;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.RadioButton;

@Controller
@RequestMapping("/WF/MapDef/")
public class AutoFullController {
	
	@RequestMapping(value = "/save_data", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,String RefNo,String FK_MapData){
		String object= request.getParameter("formHtml");
		RefNo= request.getParameter("refNo");
		FK_MapData = request.getParameter("fK_MapData");
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(object, request);
		MapAttr mattrNew = new MapAttr(RefNo);
		PrintWriter out = null;
		try {
		 out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
        MapExt me = new MapExt();
        me.setMyPK(RefNo + "_AutoFull");
        me.RetrieveFromDBSources();
        me.setFK_MapData(FK_MapData);
        me.setAttrOfOper(mattrNew.getKeyOfEn());
        me.setExtType(MapExtXmlList.AutoFull);
        RadioButton rb=(RadioButton) controls.get("RB_Way_0");
        if (rb.getChecked())
        {
            me.setTag("0");
        }

        // JS 方式。
        RadioButton rb1=(RadioButton) controls.get("RB_Way_1");
        if (rb1.getChecked())
        {
            me.setTag("1");
            me.setDoc(request.getParameter("TB_JS"));

            /*检查字段是否填写正确.*/
            MapAttrs attrsofCheck = new MapAttrs(FK_MapData);
            String docC = me.getDoc();
            for (MapAttr attrC: attrsofCheck.ToJavaList())
            {
                if (attrC.getIsNum() == false)
                    continue;
                docC = docC.replace("@" + attrC.getKeyOfEn(), "");
                System.out.println(docC+"【1】"+"@" + attrC.getKeyOfEn());
                docC = docC.replace("@" + attrC.getName(), "");
                System.out.println(docC+"【2】"+"@" + attrC.getName());
            }

            if (docC.contains("@"))
            {
                out.print("您填写的表达公式不正确，导致一些数值类型的字段没有被正确的替换。" + docC);
                return;
            }
        }

        // 外键方式。
        RadioButton rb2=(RadioButton) controls.get("RB_Way_2");
        if (rb2.getChecked())
        {
            me.setTag("2");
            me.setDoc(request.getParameter("TB_SQL"));

            //mattr.HisAutoFull = AutoFullWay.Way2_SQL;
            //mattr.AutoFullDoc = this.Pub1.GetTextBoxByID("TB_SQL").getText(;
        }

        // 本表单中外键列。
        String doc = "";
        RadioButton rb3=(RadioButton) controls.get("RB_Way_3");
        if (rb3.getChecked())
        {
            me.setTag("3");

           // mattr.HisAutoFull = AutoFullWay.Way3_FK;
            MapData md = new MapData(FK_MapData);
            Attrs attrs = md.GenerHisMap().getHisFKAttrs();
            for(Attr attr:attrs)
            {
                if (attr.getIsRefAttr())
                    continue;
                RadioButton rb4=(RadioButton) controls.get("RB_FK_" + attr.getKey());
                if (rb4.getChecked() == false)
                    continue;
                // doc = " SELECT " + this.Pub1.GetDDLByID("DDL_" + attr.Key).SelectedValue + " FROM " + attr.HisFKEn.getEnMap().getPhysicsTable() + " WHERE NO=@" + attr.Key;
                DDL ddl=(DDL) controls.get("DDL_" + attr.getKey());
                doc = "@AttrKey=" + attr.getKey() + "@Field=" + ddl.getSelectedItemStringVal() + "@Table=" + attr.getHisFKEn().getEnMap().getPhysicsTable();
            }
            me.setDoc(doc);
        }

        // 本表单中从表列。
        RadioButton rb4=(RadioButton) controls.get("RB_Way_4");
        if (rb4.getChecked())
        {
            me.setTag("4");

            MapDtls dtls = new MapDtls(FK_MapData);
         //   mattr.HisAutoFull = AutoFullWay.Way4_Dtl;
            for (MapDtl dtl: dtls.ToJavaList())
            {
                try
                {
                    RadioButton rb5=(RadioButton) controls.get("RB_" + dtl.getNo());
                    if (rb5.getChecked() == false)
                        continue;
                }
                catch(Exception e)
                {
                    continue;
                }
                //  doc = "SELECT " + this.Pub1.GetDDLByID( "DDL_"+dtl.No + "_Way").SelectedValue + "(" + this.Pub1.GetDDLByID("DDL_"+dtl.No+"_F").SelectedValue + ") FROM " + dtl.No + " WHERE REFOID=@OID";
                DDL dd=(DDL) controls.get("DDL_" + dtl.getNo() + "_F");
                doc = "@Table=" + dtl.getNo() + "@Field=" + dd.getSelectedItemStringVal();
            }
            me.setDoc(doc);
        }

        try
        {
            me.Save();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return;
        }
   
		out.print("保存成功");
		
      //  BaseModel.Alert("保存成功");
        //Button btn = sender as Button;
//        if (btn.ID.Contains("Close"))
//        {
//            this.WinClose();
//            return;
//        }
//        else
//        {
//            this.Response.Redirect(this.Request.RawUrl, true);
//        }
	}
	
	
	
	/**
	 * @Description: 表单控件编辑页面---扩展设置保存功能（新的）
	 * @Title: btn_Save_Click
	 * @param object
	 * @param request
	 * @param response
	 * @author peixiaofeng
	 * @date 2016年5月23日
	 */
	@RequestMapping(value = "/btn_Save_Click", method = RequestMethod.POST)
	@ResponseBody
	public String btn_Save_Click(TempObject object,HttpServletRequest request, HttpServletResponse response) {
		String msg="";
		String RefNo= request.getParameter("RefNo");
		String FK_MapData = request.getParameter("fK_MapData");
		String Tag = request.getParameter("Tag");
		String Doc = request.getParameter("Doc");
		MapAttr mattrNew = new MapAttr(RefNo);

		MapExt me = new MapExt();
		me.setMyPK(RefNo + "_AutoFull");
		me.RetrieveFromDBSources();
		me.setFK_MapData(FK_MapData);
		me.setAttrOfOper(mattrNew.getKeyOfEn());
		me.setExtType(MapExtXmlList.AutoFull);
		
		me.setTag(Tag);
		me.setDoc(Doc);
		if("1".equals(Tag)){
			MapAttrs attrsofCheck = new MapAttrs(FK_MapData);
			String docC = Doc;
			for(int i=0;i<attrsofCheck.size();i++){
				MapAttr attrC =(MapAttr) attrsofCheck.get(i);
				if (attrC.getIsNum() == false) {
					continue;
				}	
				docC = docC.replace("@" + attrC.getKeyOfEn(), "");
				docC = docC.replace("@" + attrC.getName(), "");
			}
			
			if (docC.contains("@")) {
				msg="您填写的表达公式不正确，导致一些数值类型的字段没有被正确的替换。" + docC;
				return msg;
			}

		}
		me.Save();
		return "success";
	}
	
}
