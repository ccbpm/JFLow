package bp.wf.httphandler;

import bp.da.*;
import bp.*;
import bp.difference.handler.WebContralBase;
import bp.wf.*;

/** 
 页面功能实体
*/
public class WF_GPM_Window extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_GPM_Window() throws Exception {
	}
	public final String Default_Mover() throws Exception {
		String[] ens = this.GetRequestVal("MyPKs").split("[,]", -1);
		for (int i = 0; i < ens.length; i++)
		{
			String enNo = ens[i];
			String sql = "UPDATE GPM_WindowTemplate SET Idx=" + i + " WHERE No='" + enNo + "'  ";
			DBAccess.RunSQL(sql);
			// BP.CCFast.Portal.Window en = new BP.CCFast.Portal.Window(); 
		}
		return "移动成功..";
	}
	public final String Tabs_Default_Mover() throws Exception {
		String[] ens = this.GetRequestVal("MyPKs").split("[,]", -1);
		for (int i = 0; i < ens.length; i++)
		{
			String enNo = ens[i];
			String sql = "UPDATE GPM_MenuDtl SET Idx=" + i + " WHERE No='" + enNo + "'  ";
			DBAccess.RunSQL(sql);
			// BP.CCFast.Portal.Window en = new BP.CCFast.Portal.Window(); 
		}
		return "移动成功..";
	}

}