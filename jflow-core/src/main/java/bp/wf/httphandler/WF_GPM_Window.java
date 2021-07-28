package bp.wf.httphandler;

import bp.da.DBAccess;
import bp.difference.handler.WebContralBase;

public class WF_GPM_Window  extends WebContralBase {
	 /** 
	 构造函数
*/
//C# TO JAVA CONVERTER WARNING: The following constructor is declared outside of its associated class:
//ORIGINAL LINE: public WF_GPM_Window()
	public WF_GPM_Window()
	{
	}
	public final String Default_Mover()
	{
		String[] ens = this.GetRequestVal("MyPKs").split("[,]", -1);
		for (int i = 0; i < ens.length; i++)
		{
			String enNo = ens[i];
			String sql = "UPDATE GPM_WindowTemplate SET Idx=" + i + " WHERE No='" + enNo + "'  ";
			DBAccess.RunSQL(sql);
			// BP.GPM.Home.Window en = new GPM.Home.Window(); 
		}
		return "移动成功..";
	}
}