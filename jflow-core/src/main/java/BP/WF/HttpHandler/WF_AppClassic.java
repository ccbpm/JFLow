package BP.WF.HttpHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.http.protocol.HttpContext;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.EditType;
import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.SysEnumMain;
import BP.Sys.SystemConfig;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.Web.WebUser;

public class WF_AppClassic extends WebContralBase {
	/**
	 * 初始化数据
	 * 
	 * @param mycontext
	 */
	public WF_AppClassic(HttpContext mycontext) {
		this.context = mycontext;
	}

	public WF_AppClassic() {
	}

	// /#region 执行父类的重写方法.
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@Override
	protected String DoDefaultMethod() {
		// switch (this.DoType)
		// ORIGINAL LINE: case "DtlFieldUp":
		if (this.getDoType().equals("DtlFieldUp")) // 字段上移
		{
			return "执行成功.";
		} else {
		}

		// 找不不到标记就抛出异常.
		return "err@没有判断的执行标记:" + this.getDoType();
	}

	// /#endregion 执行父类的重写方法.

	// /#region xxx 界面 .

	// /#endregion xxx 界面方法.

	/**
	 * 初始化Home
	 * 
	 * @return
	 */
	public final String Home_Init() {
		AppACE page = new AppACE(context);
		return page.Home_Init();
	}

	public final String Index_Init() {
		AppACE page = new AppACE(context);
		return page.Index_Init();
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 登录界面.
	public final String Login_Init() {
		AppACE page = new AppACE(context);
		return page.Login_Init();
	}

	public final String Login_Submit() {
		AppACE page = new AppACE(context);
		try {
			return page.Login_Submit();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
