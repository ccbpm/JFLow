package cn.jflow.controller.wf.mapdef;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.PubClass;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.RadioButton;


@Controller
@RequestMapping(value = "/WF/FrmReturnValTBFullCtrl")
@ResponseBody
public class FrmReturnValTBFullCtrlController extends BaseController{
	
	@RequestMapping(value = "/Btn_Save_Click", method = RequestMethod.POST)
	private void btn_Click()
	{
		BP.Sys.MapExt ext = new BP.Sys.MapExt(this.getFK_MapExt());
		String sql = ext.getTagOfSQL_autoFullTB();
		if (this.getVal() != null)
		{
			sql = sql.replace("@Key", this.getVal());
		}

		sql = sql.replace("$", "");

		String val = "";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			RadioButton rb = new RadioButton();//this.GetRadioButtonByID("RB_" + dr.getValue("No"));
			if (this.getVal().equals(dr.getValue("No").toString()))
			{
				val = dr.getValue("No").toString();
				WinClose(val);
				return;

			}
		}
		//this.WinClose();
		PubClass.WinClose();
	}
	
	private String getVal()
	{
		String s = getRequest().getParameter("CtrlVal");
		if (s==null || s.equals(""))
		{
			s = "";
		}else
		{
			if(s.contains("_"))
			{
				s = s.substring(s.indexOf("_")+1, s.length());
				return s;
			}
		}
		return s;
	}
	
	/** 
	 关闭窗口
	 
	*/
	protected void WinClose(String val)
    {
        String clientscript = "<script language='javascript'> window.returnValue = '" + val + "'; window.close(); </script>";
        try {
			ContextHolderUtils.getResponse().getWriter().write(clientscript);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
