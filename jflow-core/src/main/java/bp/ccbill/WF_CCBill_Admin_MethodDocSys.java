package bp.ccbill;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import java.io.*;

/** 
 页面功能实体
*/
public class WF_CCBill_Admin_MethodDocSys extends WebContralBase
{

		///#region 属性.
	public final String getGroupID() throws Exception {
		String str = this.GetRequestVal("GroupID");
		return str;
	}
	public final String getName() throws Exception {
		String str = this.GetRequestVal("Name");
		return str;
	}

		///#endregion 属性.

	/** 
	 构造函数
	*/
	public WF_CCBill_Admin_MethodDocSys() throws Exception {

	}
	/** 
	 移动方法.
	 
	 @return 
	*/
	public final String MethodDocSysParas_Mover() throws Exception {
		String[] ens = this.GetRequestVal("MyPKs").split("[,]", -1);
		for (int i = 0; i < ens.length; i++)
		{
			String enNo = ens[i];
			String sql = "UPDATE Sys_MapAttr SET Idx=" + i + " WHERE MyPK='" + enNo + "'";
			DBAccess.RunSQL(sql);
		}
		return "方法顺序移动成功..";
	}

	/** 
	 获得js,sql内容.
	 
	 @return 
	*/
	public final String MethodDocSys_GetScript() throws Exception {
		bp.ccbill.sys.Func en = new bp.ccbill.sys.Func(this.getNo());
		int type = this.GetRequestValInt("TypeOfFunc");
		if (type == 0)
		{
			return en.getMethodDocSQL();
		}

		if (type == 1)
		{
			return en.getMethodDocJavaScript();
		}

		if (type == 2)
		{
			return en.getMethodDocUrl();
		}

		return "err@没有判断的类型.";
	}
	/** 
	 保存脚本
	 
	 @return 
	*/
	public final String MethodDocSys_SaveScript() throws Exception {
		bp.ccbill.sys.Func en = new bp.ccbill.sys.Func(this.getNo());

		int type = this.GetRequestValInt("TypeOfFunc");
		String doc = this.GetRequestVal("doc");
		String funcstr = this.GetRequestVal("funcstr");
		//sql模式.
		if (type == 0)
		{
			doc = doc.replace("/#", "+");
			doc = doc.replace("/$", "-");
			en.setMethodDocSQL(doc);
		}

		//script.
		if (type == 1)
		{
			en.setMethodDocJavaScript(doc);

			String path = bp.difference.SystemConfig.getPathOfDataUser() + "JSLibData/Method/";
			if ((new File(path)).isDirectory() == false)
			{
				(new File(path)).mkdirs();
			}
			//写入文件.
			String file = path + en.getNo() + ".js";
			funcstr = funcstr.replace("/#", "+");
			funcstr = funcstr.replace("/$", "-");
			DataType.WriteFile(file, funcstr);
		}

		//url. 、 methond.
		if (type == 2 || type == 3)
		{
			en.setMethodDocUrl(this.GetRequestVal("Tag1"));
		}

		en.setMethodDocTypeOfFunc(type);
		en.DirectUpdate();

		return "保存成功.";
	}


}