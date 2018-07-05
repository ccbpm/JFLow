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
public class CCMobile_WorkOpt extends WebContralBase
{
	
	/**
	 * 构造函数
	 */
	public CCMobile_WorkOpt()
	{
	
	}
	
	/** 
	 打包下载
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Packup_Init() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.Packup_Init();
	}
	/** 
	 选择接受人
	 
	 @return 
	 * @throws Exception 
	*/
	public final String HuiQian_SelectEmps() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.HuiQian_SelectEmps();
	}


    ///#region 审核组件.
	public final String WorkCheck_GetNewUploadedAths() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.WorkCheck_GetNewUploadedAths();
	}
	public final String WorkCheck_Init() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.WorkCheck_Init();
	}
	public final String WorkCheck_Save() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.WorkCheck_Save();
	}

		///#endregion 审核组件


		///#region 会签.
	public final String HuiQian_AddEmps() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.HuiQian_AddEmps();
	}
	public final String HuiQian_Delete() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.HuiQian_Delete();
	}
	public final String HuiQian_Init() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.HuiQian_Init();
	}
	public final String HuiQian_SaveAndClose() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.HuiQian_SaveAndClose();
	}

		///#endregion 会签


		///#region 接收人选择器(限定接受人范围的).
	public final String Accepter_Init() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.Accepter_Init();
	}
	public final String Accepter_Save() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.Accepter_Save();
	}
	public final String Accepter_Send() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.Accepter_Send();
	}

		///#endregion 接收人选择器(限定接受人范围的).


		///#region 接收人选择器(通用).
	public final String AccepterOfGener_Init() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.AccepterOfGener_Init();
	}
	public final String AccepterOfGener_AddEmps()
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.AccepterOfGener_AddEmps();
	}
	public final String AccepterOfGener_Delete() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.AccepterOfGener_Delete();
	}
	
	public final String AccepterOfGener_Send() throws Exception
	{
		    WF_WorkOpt en = new WF_WorkOpt(this.context);		 
		    return en.AccepterOfGener_Send(); 
	}

		///#endregion 接收人选择器(通用).
 
		///#region 选择人员(通用).
	/** 
	 将要去掉.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SelectEmps() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.SelectEmps_Init();
	}
	public final String SelectEmps_Init() throws Exception
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.SelectEmps_Init();
	}

		///#endregion 选择人员(通用).



		///#region 退回.
	public final String Return_Init()
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.Return_Init();
	}
	//执行退回.
	public final String DoReturnWork()
	{
		WF_WorkOpt en = new WF_WorkOpt(this.context);
		return en.DoReturnWork();
	}

		///#endregion 退回.


		///#region xxx 界面 .

		///#endregion xxx 界面方法.
}