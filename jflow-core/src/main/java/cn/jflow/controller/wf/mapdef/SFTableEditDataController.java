package cn.jflow.controller.wf.mapdef;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.QueryObject;
import BP.Sys.GENoName;
import BP.Sys.GENoNames;
import BP.WF.Glo;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/MapDef")
public class SFTableEditDataController extends BaseController {
	public String getRefNo(HttpServletRequest request)
	{
		String s = request.getParameter("FK_SFTable");
		return s;
	}
	@RequestMapping(value = "/btn_Click3", method = RequestMethod.POST)
	public void btn_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		//批量保存数据。
		GENoNames ens = new GENoNames(this.getRefNo(), "sdsd");
		QueryObject qo = new QueryObject(ens);
		qo.DoQuery("No", 10, this.getPageIdx(), false);
		for (GENoName myen : ens.ToJavaList())
		{
			String no = myen.getNo();
			String name1 = request.getParameter("TB_" + myen.getNo());
			if (name1.equals(""))
			{
				continue;
			}
			BP.DA.DBAccess.RunSQL("update " + this.getRefNo() + " set Name='" + name1 + "' WHERE no='" + no + "'");
		}


		GENoName en = new GENoName(this.getRefNo(), "sd");
		String name = request.getParameter("TB_Name");
		if (name.length() > 0)
		{
			en.setName(name);
			en.setNo(en.getGenerNewNo());
			//en.setNo(PingYinUtil.getFirstSpell(name).toUpperCase());
			en.Insert();
			try {
				response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Admin/FoolFormDesigner/SFTableEditData.jsp?RefNo=" + this.getRefNo() + "&FK_SFTable=" + this.getRefNo() + "&PageIdx=" + this.getPageIdx());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

}
