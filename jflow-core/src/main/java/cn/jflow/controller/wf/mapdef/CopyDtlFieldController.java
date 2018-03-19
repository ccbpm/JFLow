package cn.jflow.controller.wf.mapdef;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;

@Controller
@RequestMapping("/WF/MapDef/")
public class CopyDtlFieldController {	
	
	@RequestMapping(value = "/copyBtn_Click", method = RequestMethod.POST)
	public void btn_Click(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String dt1=request.getParameter("dtl");
		String myPk=request.getParameter("mypk");
//		String pub1=request.getParameter("pub2");
		MapAttrs attrsFrom = new MapAttrs(dt1);
		MapAttrs attrs = new MapAttrs(myPk);
		for (MapAttr attr : attrsFrom.ToJavaList()) {
			String keyOfEn = attr.getKeyOfEn();
			if (keyOfEn.equals("OID")
					|| keyOfEn.equals("FID")
					|| keyOfEn.equals("WorkID")
					|| keyOfEn.equals("Rec")
					|| keyOfEn.equals("RDT")
					|| keyOfEn.equals("RefPK")) {
				continue;
			} else {
			}

			if (attrs.Contains(MapAttrAttr.KeyOfEn, keyOfEn)) {
				continue;
			}

			String checkBoxIsCheck = request.getParameter("CB_" +  attr.getMyPK());
			if(null == checkBoxIsCheck){
				continue;
			}

			MapAttr en = new MapAttr();
			en.Copy(attr);
			en.setFK_MapData(myPk);
			en.setGroupID(0);
			// en.Idx = 0;
			en.Insert();
		}
		PrintWriter out=response.getWriter();
		out.print("复制成功，您可以用调整从表的顺序。");
	}

}
