package bp.wf.httphandler;

import bp.*;
import bp.difference.handler.WebContralBase;
import bp.wf.*;

/** 
 初始化函数
*/
public class WF_Admin_CCBPMDesigner_App_OneFlow extends WebContralBase
{

	/** 
	 构造函数
	*/
	public WF_Admin_CCBPMDesigner_App_OneFlow() throws Exception {
	}


		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod() throws Exception {
		switch (this.getDoType())
		{

			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到.");
	}

		///#endregion 执行父类的重写方法.


		///#region xxx 界面 .

		///#endregion xxx 界面方法.

}