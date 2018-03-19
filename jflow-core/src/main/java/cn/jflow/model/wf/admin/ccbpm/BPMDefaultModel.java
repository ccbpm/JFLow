package cn.jflow.model.wf.admin.ccbpm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;

public class BPMDefaultModel extends BaseModel{

	public BPMDefaultModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}
	
	public void page_load(HttpServletRequest request,HttpServletResponse response){
		//让admin登录
		if (StringHelper.isNullOrEmpty(BP.Web.WebUser.getNo()) || ! BP.Web.WebUser.getNo().equals("admin")) {
			//Response.Redirect("Login.jsp?DoType=Logout");
			return;
		}
		
		// 执行升级
		String str;
		try {
			str = BP.WF.Glo.UpdataCCFlowVer();
			if (str != null) {
				if ("0".equals(str)) {
					BP.Sys.PubClass.Alert("系统升级错误，请查看日志文件\\DataUser/\\log",response);
				}
				else {
					BP.Sys.PubClass.Alert("系统成功升级到:" + str + " ，系统升级不会破坏现有的数据。",response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


}
