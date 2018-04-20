package BP.WF.HttpHandler;

import java.util.Hashtable;

import org.apache.http.protocol.HttpContext;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.UIContralType;
import BP.Sys.MapData;
import BP.Sys.SFTable;
import BP.Sys.SysEnumMain;
import BP.Tools.StringHelper;
import BP.WF.HttpHandler.Base.WebContralBase;
/** 
 初始化函数
*/
public class WF_Admin_CCFormDesigner_DialogCtr extends WebContralBase
{
	/** 
	 初始化函数
	 @param mycontext
	*/
	public WF_Admin_CCFormDesigner_DialogCtr(HttpContext mycontext)
	{
		this.context = mycontext;
	}

	public WF_Admin_CCFormDesigner_DialogCtr() {
	}

	
	/** 
	 获取隐藏字段
	 
	 @return 
	 * @throws Exception 
*/
	public final String Hiddenfielddata() throws Exception
	{
		return BP.Sys.CCFormAPI.DB_Hiddenfielddata(this.getFK_MapData());
	}
	
	
	
	
	
	/** 
	 * 默认执行的方法
	 @return 
	*/
	public final String PublicNoName_InitFieldVal()
	{
		String sql = "";
		Hashtable ht = new Hashtable();
		String ctrlType = getRequest().getParameter("CtrlType");
		if(StringHelper.isNullOrEmpty(ctrlType)){
			ctrlType = this.GetRequestVal("action");
		}
		int num = 1;
		if("Dtl".equals(ctrlType))
		{
			sql = "SELECT COUNT(*) FROM Sys_MapDtl WHERE FK_MapData='" + this.getFK_MapData() + "'";
			num = DBAccess.RunSQLReturnValInt(sql) + 1;
			ht.put("No", this.getFK_MapData() + "Dtl" + num);
			ht.put("Name", "从表" + num);
		}else if("AthMulti".equals(ctrlType))
		{
			sql = "SELECT COUNT(*) FROM Sys_FrmAttachment WHERE FK_MapData='" + this.getFK_MapData() + "'";
			num = DBAccess.RunSQLReturnValInt(sql) + 1;
			ht.put("No", "AthMulti" + num);
			ht.put("Name", "多附件" + num);
		}else if("AthSingle".equals(ctrlType))
		{
			sql = "SELECT COUNT(*) FROM Sys_FrmAttachment WHERE FK_MapData='" + this.getFK_MapData() + "'";
			num = DBAccess.RunSQLReturnValInt(sql) + 1;
			ht.put("No", "AthSingle" + num);
			ht.put("Name", "单附件" + num);
		}else if("AthImg".equals(ctrlType))
		{
			sql = "SELECT COUNT(*) FROM Sys_FrmImgAth WHERE FK_MapData='" + this.getFK_MapData() + "'";
			num = DBAccess.RunSQLReturnValInt(sql) + 1;
			ht.put("No", "AthImg" + num);
			ht.put("Name", "图片附件" + num);
		}else if("HandSiganture".equals(ctrlType))
		{
			sql = "SELECT COUNT(*) FROM Sys_FrmEle WHERE FK_MapData='" + this.getFK_MapData() + "' AND EleType='" + ctrlType + "'";
			num = DBAccess.RunSQLReturnValInt(sql) + 1;
			ht.put("No", "HandSiganture" + num);
			ht.put("Name", "签字板" + num);
		}else if("iFrame".equals(ctrlType))
		{
			sql = "SELECT COUNT(*) FROM Sys_FrmEle WHERE FK_MapData='" + this.getFK_MapData() + "' AND EleType='" + ctrlType + "'";
			num = DBAccess.RunSQLReturnValInt(sql) + 1;
			ht.put("No", "iFrame" + num);
			ht.put("Name", "框架" + num);
		}else if("Fieldset".equals(ctrlType))
		{
			sql = "SELECT COUNT(*) FROM Sys_FrmEle WHERE FK_MapData='" + this.getFK_MapData() + "' AND EleType='" + ctrlType + "'";
			num = DBAccess.RunSQLReturnValInt(sql) + 1;
			ht.put("No", "Fieldset" + num);
			ht.put("Name", "分组" + num);
		}
		else {
			ht.put("No", ctrlType +1);
			ht.put("Name", ctrlType+1);
		}
		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
		///#region 枚举界面.
	public final String FrmTable_GetSFTableList() throws Exception
	{
		int pageNumber = this.GetRequestValInt("pageNumber");
		if (pageNumber == 0)
		{
			pageNumber = 1;
		}

		int pageSize = this.GetRequestValInt("pageSize");
		if (pageSize == 0)
		{
			pageSize = 9999;
		}

		return BP.Sys.CCFormAPI.DB_SFTableList(pageNumber, pageSize);
	}
	public final String FrmEnumeration_Init() throws Exception
	{
		  WF_Admin_FoolFormDesigner wf = new  WF_Admin_FoolFormDesigner();
          return wf.SysEnumList_Init(); 
	}
	/** 
	 默认执行的方法
	 @return 
	 * @throws Exception 
	*/
	@Override
	public String DoDefaultMethod() throws Exception
	{
		String sql = "";
		if("NewSFTableField".equals(getDoType()))//创建一个SFTable字段.
		{
			//调用接口,执行保存.
			try {
				BP.Sys.CCFormAPI.SaveFieldSFTable(this.getFK_MapData(), this.getKeyOfEn(), this.GetRequestVal("Name"), this.GetRequestVal("UIBindKey"), this.GetRequestValFloat("x"), this.GetRequestValFloat("y"),0);
			} catch (Exception e) {
				return "保存外部字段失败";
			}
			return "执行成功.";
		}else if("FrmTable_DelSFTable".equals(getDoType()))
		{
			SFTable sfDel = new SFTable();
			sfDel.setNo(this.GetRequestVal("FK_SFTable"));
			sfDel.Delete();
			return "删除成功.";
		}
		//找不不到标记就抛出异常.
		return "err@标记[" + this.getDoType() + "]，没有找到.";
	}
	
	
	 ///#endregion 执行父类的重写方法.

			public final String EnumerationNew_GetEnum() throws Exception
			{
				String enumKey = this.GetRequestVal("EnumKey");

				SysEnumMain sem = new SysEnumMain(enumKey);
				java.util.Hashtable ht = new java.util.Hashtable();
				ht.put("No", sem.getNo());
				ht.put("Name", sem.getName());
				ht.put("CfgVal", sem.getCfgVal());

				return BP.Tools.Json.ToJsonEntityModel(ht);
			}
	
	
	public String FrmEnumeration_NewEnumField() throws Exception 
	{
		//创建一个字段. 对应 FigureCreateCommand.js  里的方法.
		UIContralType ctrl = UIContralType.RadioBtn;
		String ctrlDoType = GetRequestVal("ctrlDoType");
		if (ctrlDoType.equals("DDL"))
		{
			ctrl = UIContralType.DDL;
		}
		else
		{
			ctrl = UIContralType.RadioBtn;
		}

		String fk_mapdata = this.GetRequestVal("FK_MapData");
		String keyOfEn = this.GetRequestVal("KeyOfEn");
		String fieldDesc = this.GetRequestVal("Name");
		String enumKeyOfBind = this.GetRequestVal("UIBindKey"); //要绑定的enumKey.
		float x = Float.parseFloat(this.GetRequestVal("x"));
		float y = Float.parseFloat(this.GetRequestVal("y"));

		BP.Sys.CCFormAPI.NewEnumField(fk_mapdata, keyOfEn, fieldDesc, enumKeyOfBind, ctrl, x, y,0);
		return "绑定成功.";
	}
	public String FrmEnumeration_SaveEnum() throws Exception
	{
		String enumName = this.GetRequestVal("EnumName");
		String enumKey1 = this.GetRequestVal("EnumKey");
		String cfgVal = this.GetRequestVal("Vals");

		//调用接口执行保存.
		return BP.Sys.CCFormAPI.SaveEnum(enumKey1, enumName, cfgVal, false,null);
	}
	public String FrmEnumeration_NewEnum() throws Exception
	{
		String newnEumName = this.GetRequestVal("EnumName");
		String newEnumKey1 = this.GetRequestVal("EnumKey");
		String newCfgVal = this.GetRequestVal("Vals");

		//调用接口执行保存.
		return BP.Sys.CCFormAPI.SaveEnum(newEnumKey1, newnEumName, newCfgVal, true,null);
	}
	public String FrmEnumeration_DelEnum()
	{
		String sql = "";
		//删除空数据.
		BP.DA.DBAccess.RunSQL("DELETE FROM Sys_MapAttr WHERE FK_MapData IS NULL OR FK_MapData='' ");

		//获得要删除的枚举值.
		String enumKey = this.GetRequestVal("EnumKey");

		// 检查这个物理表是否被使用.
		sql = "SELECT  FK_MapData,KeyOfEn,Name FROM Sys_MapAttr WHERE UIBindKey='" + enumKey + "'";
		DataTable dtEnum = DBAccess.RunSQLReturnTable(sql);
		String msgDelEnum = "";
		for (DataRow dr : dtEnum.Rows)
		{
			msgDelEnum += "\n 表单编号:" + dr.getValue("FK_MapData") + " , 字段:" + dr.getValue("KeyOfEn") + ", 名称:" + dr.getValue("Name");
		}

		if (!msgDelEnum.equals(""))
		{
			return "error:该枚举已经被如下字段所引用，您不能删除它。" + msgDelEnum;
		}

		sql = "DELETE FROM Sys_EnumMain WHERE No='" + enumKey + "'";
		sql += "@DELETE FROM Sys_Enum WHERE EnumKey='" + enumKey + "' ";
		DBAccess.RunSQLs(sql);
		return "执行成功.";
	}
	public String FrmTextBox_ParseStringToPinyin()
	{
		String name = getRequest().getParameter("name");
		String flag = getRequest().getParameter("flag");

		if (flag.equals("true"))
		{
			 return BP.Sys.CCFormAPI.ParseStringToPinyinField(name, true);			
		}

		return BP.Sys.CCFormAPI.ParseStringToPinyinField(name, false);
	}

	// / <summary>
	// / 获得表单对应的物理表特定的数据类型字段
	// / </summary>
	// / <returns></returns>
	public String FrmTextBoxChoseOneField_Init() throws Exception {
		DataTable mydt = MapData.GetFieldsOfPTableMode2(this.getFK_MapData());
		mydt.TableName = "dt";
		return BP.Tools.Json.ToJson(mydt);
	}

}
