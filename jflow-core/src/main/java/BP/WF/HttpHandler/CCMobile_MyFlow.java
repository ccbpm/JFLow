package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.*;

/** 
 页面功能实体
 
*/
public class CCMobile_MyFlow extends WebContralBase
{
	/** 
	 获得工作节点
	 
	 @return 
	*/
	public final String GenerWorkNode()
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.GenerWorkNode();
	}
	/** 
	 获得toolbar
	 
	 @return 
	*/
	public final String InitToolBar()
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.InitToolBarForMobile();
	}
	public final String MyFlow_Init()
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.MyFlow_Init();
	}

	public final String MyFlow_StopFlow()
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.MyFlow_StopFlow();
	}
	public final String Save()
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.Save();
	}
	public final String Send()
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.Send();
	}

	public final String Focus()
    {
        BP.WF.Dev2Interface.Flow_Focus( this.getWorkID());
        return "设置成功.";
    }
	
	public String StartGuide_Init()
    {
        WF_MyFlow en = new WF_MyFlow(this.context);
        return en.StartGuide_Init();
    }
	
	public final String FrmGener_Init()
	{
		WF_CCForm ccfrm = new WF_CCForm(this.context);
		return ccfrm.FrmGener_Init();
	}
	public final String FrmGener_Save()
	{
		WF_CCForm ccfrm = new WF_CCForm(this.context);
		return ccfrm.FrmGener_Save();
	}

	public final String AttachmentUpload_Down()
	{
		WF_CCForm ccform = new WF_CCForm(this.context);
		return ccform.AttachmentUpload_Down();
	}
	

}