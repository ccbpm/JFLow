package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_Admin_Multilingual extends DirectoryPageBase
{


	/** 
	 构造函数
	*/
	public WF_Admin_Multilingual()
	{
	}
	/** 
	 获得使用的语言.
	 
	 @return 使用的语言
	*/
	public final String GetLangue()
	{
		Hashtable ht = new Hashtable();

		if (SystemConfig.IsMultilingual == true)
		{
			ht.put("IsMultilingual", "1");
		}
		else
		{
			ht.put("IsMultilingual", "0");
		}

		ht.put("Langue", SystemConfig.Langue);
		return BP.Tools.Json.ToJson(ht);
	}



		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + HttpContextHelper.RequestRawUrl);
	}

		///#endregion 执行父类的重写方法.


		///#region ccform
	/** 
	 表单的配置
	 
	 @return 
	*/
	public final String CCForm_Init()
	{
		return "";
	}

		///#endregion xxx 界面方法.

}