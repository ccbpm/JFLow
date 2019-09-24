package BP.Frm;

import java.io.IOException;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.Data.*;
import BP.WF.HttpHandler.*;

/** 
 页面功能实体
*/
public class WF_CCBill_Admin extends DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public WF_CCBill_Admin()
	{
	}
	/** 
	 获得js,sql内容.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String MethodDoc_GetScript() throws Exception
	{
		BP.Frm.MethodFunc en = new BP.Frm.MethodFunc(this.getMyPK());
		int type = this.GetRequestValInt("TypeOfFunc");
		if (type == 0)
		{
			return en.getMethodDoc_SQL();
		}

		if (type == 1)
		{
			return en.getMethodDoc_JavaScript();
		}

		if (type == 2)
		{
			return en.getMethodDoc_Url();
		}

		return "err@没有判断的类型.";
	}
	/** 
	 保存脚本
	 
	 @return 
	 * @throws Exception 
	*/
	public final String MethodDoc_SaveScript() throws Exception
	{
		BP.Frm.MethodFunc en = new BP.Frm.MethodFunc(this.getMyPK());

		int type = this.GetRequestValInt("TypeOfFunc");
		String doc = this.GetRequestVal("doc");

		//sql模式.
		if (type == 0)
		{
			en.setMethodDoc_SQL(doc);
		}

		//script.
		if (type == 1)
		{
			en.setMethodDoc_JavaScript(doc);

			//string path=SystemConfig.PathOfDataUser + "JSLibData\\Method\\" ;
			//if (System.IO.Directory.Exists(path) == false)
			//    System.IO.Directory.CreateDirectory(path);
			////写入文件.
			//string file = path + en.MyPK + ".js";
			//DataType.WriteFile(file, doc);
		}

		//url.
		if (type == 2)
		{
			en.setMethodDoc_Url(doc);
		}

		en.setMethodDocTypeOfFunc(type);
		en.Update();

		return "保存成功.";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		throw new RuntimeException("@标记[" + this.getDoType() + "]DoMethod=[" + this.GetRequestVal("DoMethod") + "]，没有找到. @RowURL:" + HttpContextHelper.getRequestRawUrl());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.
}