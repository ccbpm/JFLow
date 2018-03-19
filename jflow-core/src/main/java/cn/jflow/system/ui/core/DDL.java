package cn.jflow.system.ui.core;

import java.util.ArrayList;
import java.util.Calendar;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.En.AddAllLocation;
import BP.En.Attr;
import BP.En.DDLShowType;
import BP.En.Entities;
import BP.En.EntitiesNoName;
import BP.En.EntitiesOIDName;
import BP.En.EntitiesTree;
import BP.En.Entity;
import BP.En.EntityNoName;
import BP.En.EntityOIDName;
import BP.En.EntitySimpleTree;
import BP.En.EntityTree;
import BP.Sys.MapAttr;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Tools.StringHelper;
import BP.WF.Template.SysFormTrees;
import BP.Web.WebUser;
import BP.XML.XmlEn;
import BP.XML.XmlEns;
import cn.jflow.common.util.ContextHolderUtils;

/** 
 BPListBox 的摘要说明。
 
*/
public class DDL extends BaseWebControl
{
	private String ClientID;
	
	public final String getClientID() {
		return ClientID;
	}
	public final void setClientID(String clientID) {
		ClientID = clientID;
	}
	public ArrayList<ListItem> Items = new ArrayList<ListItem>();

//	/** 
//	 关联的Key .
//	 
//	*/
	public String EnsKey;
	public final String getSelfEnsRefKey()
	{
		return this.EnsKey;
	}
	public final void setSelfEnsRefKey(String value)
	{
		this.EnsKey=value;
	}
//	/** 
//	 关联的Text
//	 
//	*/
	private String SelfEnsRefKeyText;
	public final String getSelfEnsRefKeyText()
	{
		return this.SelfEnsRefKeyText;
	}
	public final void setSelfEnsRefKeyText(String value)
	{
		this.SelfEnsRefKeyText=value;
	}
	public final void BindThisYearMonth()
	{
		String year=DataType.getCurrentYear();
		int i = 0;
		this.Items.clear();
		while (i < 12)
		{
			i++;
			String val = year + "-" + StringHelper.stringFill(String.valueOf(i), 2, '0', true) ;
			this.Items.add(new ListItem(val, val));
		}

		int myyear= Integer.parseInt(year)+1;
		year =(new Integer(myyear)).toString();
		i = 0;
		while (i < 12)
		{
			i++;
			String val=year+"-"+StringHelper.stringFill(String.valueOf(i), 2, '0', true) ;
			this.Items.add(new ListItem(val,val));
		}
	}
	public final void BindEntities(Entities ens, String refkey, String reftext, boolean isShowKey)
	{
		this.Items.clear();
		for (Entity en : ens.ToJavaListEn())
		{
			if (isShowKey)
			{
				this.Items.add(new ListItem(en.GetValStringByKey(refkey) + " " + en.GetValStringByKey(reftext), en.GetValStringByKey(refkey)));
			}
			else
			{
				this.Items.add(new ListItem(en.GetValStringByKey(reftext), en.GetValStringByKey(refkey)));
			}
		}
	}
	public final void BindEntities(Entities ens, String refkey, String reftext, boolean isShowKey, AddAllLocation where)
	{
		this.Items.clear();
		if (where==AddAllLocation.Top || where==AddAllLocation.TopAndEnd)
		{
			ListItem li = new ListItem("-=全部=-","all");
			this.Items.add(li);
		}

		for (Entity en : ens.ToJavaListEn())
		{
			if (isShowKey)
			{
				this.Items.add(new ListItem(en.GetValStringByKey(refkey)+" "+en.GetValStringByKey(reftext), en.GetValStringByKey(refkey)));
			}
			else
			{
				this.Items.add(new ListItem(en.GetValStringByKey(reftext), en.GetValStringByKey(refkey)));
			}
		}

		if (where==AddAllLocation.End || where==AddAllLocation.TopAndEnd)
		{
			ListItem li = new ListItem("-=全部=-","all");
			this.Items.add(li);
			//this.Items.
		}
	}
	private DDLShowType _ShowType=DDLShowType.None;
	public final DDLShowType getSelfShowType()
	{
		return _ShowType;
	}
	public final void setSelfShowType(DDLShowType value)
	{
		this._ShowType=value;
	}
	/** 
	 BindBindSysEnum
	 
	 @param enumKey
	*/
	public final void BindSysEnum(String enumKey)
	{
		SelfBindSysEnum(enumKey);
	}
	public final void BindSysEnum(String enumKey, int selecVal)
	{
		this.setSelfDefaultVal(String.valueOf(selecVal));
		SelfBindSysEnum(enumKey);
	}
	public final void BindSysEnum(String key, boolean isShowKey, AddAllLocation alllocal)
	{
		SysEnums ens = new SysEnums(key);
		this.Items.clear();
		if (alllocal == AddAllLocation.TopAndEnd || alllocal == AddAllLocation.Top || alllocal == AddAllLocation.TopAndEndWithMVal)
		{
			this.Items.add(new ListItem(">>", "all"));
		}

		for (SysEnum en : ens.ToJavaList())
		{
			if (this.getSelfIsShowVal())
			{
				this.Items.add(new ListItem(en.getIntKey() + " " + en.getLab(), String.valueOf(en.getIntKey())));
			}
			else
			{
				ListItem li = new ListItem(en.getLab(), String.valueOf(en.getIntKey()));
				//li.Attributes.CssStyle.Add("style", "color:" + en.Style);
				//li.addAttr("color", en.Style);
				//li.addAttr("style", "color:" + en.Style);

				this.Items.add(li);
			}
		}

		if (alllocal == AddAllLocation.TopAndEndWithMVal && this.Items.size() >= 4)
		{
			ListItem liMvals = new ListItem("*多项组合..", "mvals");
			liMvals.addAttr("style", "color:green");
			liMvals.addAttr("color", "green");
			liMvals.addAttr("style", "color:green");
			this.Items.add(liMvals); //new ListItem("*指定选项...", "mvals"));
		}

//		if (alllocal == AddAllLocation.TopAndEnd || alllocal == AddAllLocation.End || alllocal == AddAllLocation.TopAndEndWithMVal)
//		{
//			this.Items.add(new ListItem("-=全部=-", "all"));
//		}
	}
	
	public final void BindTable(String key, String refKey, String refText, boolean isShowKey, AddAllLocation alllocal){
		
		this.Items.clear();
		if (alllocal == AddAllLocation.TopAndEnd
				|| alllocal == AddAllLocation.Top
				|| alllocal == AddAllLocation.TopAndEndWithMVal) {
			this.Items.add(new ListItem(">>", "all"));
		}
		
		Paras ps = new Paras();
		ps.SQL = "select * from " + key;
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		for(DataRow dr : dt.Rows){
			if (this.getSelfIsShowVal()) {
				this.Items.add(new ListItem(dr.getValue(refKey) + " " + dr.getValue(refText),
						dr.getValue(refKey).toString()));
			} else {
				ListItem li = new ListItem(dr.getValue(refText).toString(), dr.getValue(refKey).toString());
				this.Items.add(li);
			}
		}
		if (alllocal == AddAllLocation.TopAndEndWithMVal && this.Items.size() >= 4){
			ListItem liMvals = new ListItem("*多项组合..", "mvals");
			liMvals.addAttr("style", "color:green");
			liMvals.addAttr("color", "green");
			liMvals.addAttr("style", "color:green");
			this.Items.add(liMvals); //new ListItem("*指定选项...", "mvals"));
		}
	}
	
	
	
	public final void SelfBindSysEnum()
	{
		this.SelfBindSysEnum(this.getSelfBindKey().trim());
	}
	/** 
	 按照enum
	 
	*/
	public final void SelfBindSysEnum(String enumKey)
	{
		this.Items.clear();

		if (this.getEnabled())
		{
			SysEnums ens = new SysEnums(enumKey);
			for (SysEnum en : ens.ToJavaList())
			{
				if (this.getSelfIsShowVal())
				{
					this.Items.add(new ListItem(en.getIntKey()+" "+en.getLab(), String.valueOf(en.getIntKey())));
				}
				else
				{
					ListItem li = new ListItem(en.getLab(), String.valueOf(en.getIntKey()));
					//li.addAttr("color",en.Style);
					//li.Attributes.CssStyle.Add("color",en.Style);
					////li.addAttr("onclick","alert('hello')");
					this.Items.add(li);
				}
			}
			this.SetSelectItem(this.getSelfDefaultVal());
		}
		else
		{
			this.Items.add(new ListItem(this.getSelfDefaultText(),this.getSelfDefaultVal()));
		}
	}
	public final void SelfBind(String key)
	{
		this.setSelfBindKey(key);
		this.Items.clear();
		this.SelfBind();
	}
	public final void SelfBind()
	{
		switch (this.getSelfShowType())
		{
			case Boolean:
				this.Items.add(new ListItem("是", "1"));
				this.Items.add(new ListItem("否", "0"));
				break;
			case Gender:
				this.Items.add(new ListItem("男", "1"));
				this.Items.add(new ListItem("女", "0"));
				break;
			case SysEnum: /// 枚举类型
				SelfBindSysEnum();
				break;
			case Self: /// 枚举类型
				SelfBindSysEnum();
				break;
			case BindTable: /// 于Table Bind.
				this.SelfBindTable();
				break;
			case Ens: /// 于实体。
				this.SelfBindEns();
				break;
		}

		//if (this.SelfAddAllLocation == AddAllLocation.TopAndEnd)
		//    this.Items.add(new ListItem("-=全部=-", "all"));

		if (this.getSelfDefaultVal() != null && this.getSelfDefaultVal().length() > 0)
		{
			this.SetSelectItem(this.getSelfDefaultVal());
		}

		//			if (this.Enabled==false)		
		//				this.BackColor=Color.FromName("#E4E3E6");
	}
		///#endregion

	public final void BindSQL(String sql, String val, String text, String selecVal)
	{
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		this.Items.clear();
		for (DataRow dr : dt.Rows)
		{
		   ListItem li= new ListItem();
			li.setValue(dr.getValue(val).toString()); // as string;
			li.setText(dr.getValue(text).toString()); // as string;
			//if (li.Value==selecVal)
			//    li.setSelected(true);
			this.Items.add(li);
		}
		this.SetSelectItem(selecVal);
	}
	/** 
	 绑定atPara.
	 
	 @param AtPara
	*/
	public final void BindAtParas(String atParas)
	{
		String[] strs = atParas.split("[@]", -1);
		for (String str : strs)
		{
			if (StringHelper.isNullOrEmpty(str) == true)
			{
				continue;
			}
			String[] mystrs = str.split("[=]", -1);
			this.Items.add(new ListItem(mystrs[1], mystrs[0]));
		}
	}
	public final void Bind(EntitiesTree ens, String selecVal)
	{
		this.Items.clear();
		for (EntityTree en : ens.ToJavaListEnTree())
		{
			this.Items.add(new ListItem(en.getName(), en.getNo()));
		}

		for (ListItem li : this.Items)
		{
			if (selecVal.equals(li.getValue()))
			{
				li.setSelected(true);
			}
		}
	}
	public final void Bind1(SysFormTrees ens, String selecVal)
	{
		this.Items.clear();
		for (int i=0;i<ens.size();i++)
		{
			EntitySimpleTree  en = (EntitySimpleTree) ens.get(i);
			this.Items.add(new ListItem(en.getName(), en.getNo()));
		}
		
		for (ListItem li : this.Items)
		{
			if (selecVal.equals(li.getValue()))
			{
				li.setSelected(true);
			}
		}
	}
	/** 
	 绑定datatable.
	 
	 @param dt
	 @param val
	 @param text
	*/
	public final void Bind(DataTable dt, String val, String text)
	{
		if (this.Items.size() >= dt.Rows.size())
		{
			boolean isHave = false;
			for (ListItem li : this.Items)
			{
				isHave = false;
				for (DataRow dr : dt.Rows)
				{
					if (dr.getValue(val).toString().equals(li.getValue()))
					{
						isHave = true;
						break;
					}
				}
				//if (isHave==false)
				//    li.Attributes["display"] = "none";

				//else
				//    li.Attributes["visibility"] = "false";

				if (isHave)
				{
					li.addAttr("visibility", "true");
				}
				else
				{
					li.addAttr("visibility","false");
				}
			}
		}
		else
		{
			for (DataRow dr : dt.Rows)
			{
				String b = (String)dr.getValue(dr.columns.get(val));
				String a = (String)dr.getValue(dr.columns.get(text));
				this.Items.add(new ListItem((String)dr.getValue(dr.columns.get(text)),(String)dr.getValue(dr.columns.get(val))));
			}
		}
	}
	public final void SetSelectItemByIndex(int index)
	{
		for(ListItem li : this.Items)
		{
			li.setSelected(false);
		}
		int i=0;
		for(ListItem li : this.Items)
		{

			if (i==index)
			{
				li.setSelected(true);
			}
			i++;
		}

	}

		///#region 处理BindKey
	/** 
	 -==全部==- 显示位置。
	 		
	*/
	private AddAllLocation _SelfAddAllLocation=AddAllLocation.None;
	/** 
	 -==全部==- 显示位置。
	 		 
	*/
	public final AddAllLocation getSelfAddAllLocation()
	{
		return _SelfAddAllLocation;
	}
	public final void setSelfAddAllLocation(AddAllLocation value)
	{
		_SelfAddAllLocation=value;
	}
	/** 
	 要bind的key.
	 这里有3种情况。
	 1，枚举类型的。
	 2，Table类型的。
	 3，实体类型的。
	 只有对于2，3两种类型的SelfRefKey, SelfRefText.才有意义。
	 
	*/
	private String SelfBindKey;
	public final String getSelfBindKey()
	{
		return this.SelfBindKey;
	}
	public final void setSelfBindKey(String value)
	{
		this.SelfBindKey=value;
	}
	private String AppPath;
	public final String getAppPath()
	{
		return  this.AppPath;
	}
	public final void setAppPath(String value)
	{
		this.AppPath =value;
	}
	/** 
	 为attr , 设置的属性
	 
	*/
	private Entities HisFKEns=null;
	
	public Entities getHisFKEns() {
		return HisFKEns;
	}
	public void setHisFKEns(Entities hisFKEns) {
		HisFKEns = hisFKEns;
	}
	/** 
	 默认值
	 
	*/
	private String SelfDefaultVal;

	public final String getSelfDefaultVal()
	{
		return this.SelfDefaultVal;
	}
	public final void setSelfDefaultVal(String value)
	{
		this.SelfDefaultVal=value;
	}
	/** 
	 默认Text
	 
	*/
	private String SelfDefaultText;
	public final String getSelfDefaultText()
	{
		return this.SelfDefaultText;
	}
	public final void setSelfDefaultText(String value)
	{
		this.SelfDefaultText=value;
	}

	/** 
	 要不要显示 Bind 的值.
	 
	*/
	public final boolean getSelfIsShowVal()
	{
		try
		{
			return this.SelfIsShowVal;
		}
		catch (java.lang.Exception e)
		{
			return false;
		}
	}
	private boolean SelfIsShowVal;
	public final void setSelfIsShowVal(boolean value)
	{
		this.SelfIsShowVal=value;
	}
	/** 
	 用到了DDL 于 Ens 定义的帮定
	 
	*/
	private void SelfBindEns()
	{
		if (this.getSelfBindKey().equals(""))
		{
			throw new RuntimeException("@没有设定它的Key.");
		}

		/*if (this.getSelfAddAllLocation() == AddAllLocation.Top || this.getSelfAddAllLocation() == AddAllLocation.TopAndEnd)
		{
			this.Items.add(new ListItem("-=全部=-", "all"));
		}*/

		if (this.getEnabled() == true)
		{
			Entities ens = this.HisFKEns;
			ens.RetrieveAll();

			this.BindEntities(ens, this.getSelfEnsRefKey(), this.getSelfEnsRefKeyText());
		}
		else
		{
			this.Items.add(new ListItem(this.getSelfDefaultText(), this.getSelfDefaultVal()));
		}

		/*if (this.getSelfAddAllLocation() == AddAllLocation.End || this.getSelfAddAllLocation() == AddAllLocation.TopAndEnd)
		{
			this.Items.add(new ListItem("-=全部=-", "all"));
		}*/
	}
	/** 
	 DDLDataHelp 用到了DDL自定义的帮定。
	 
	*/
	private void SelfBindTable()
	{
		if (this.getSelfBindKey().equals("")) {
			return;
		}
		this.Items.clear();

		if (this.getEnabled()) {
			
			DataTable dt = DBAccess.RunSQLReturnTable(this.getSelfBindKey());
			for(DataRow dr : dt.Rows){
				if (this.getSelfIsShowVal()) {
					this.Items.add(new ListItem(dr.getValue(this.getSelfEnsRefKey()) + " " + dr.getValue(this.getSelfEnsRefKeyText()),
							dr.getValue(this.getSelfEnsRefKey()).toString()));
				} else {
					ListItem li = new ListItem(dr.getValue(this.getSelfEnsRefKeyText()).toString(), dr.getValue(this.getSelfEnsRefKey()).toString());
					this.Items.add(li);
				}
			}
			this.SetSelectItem(this.getSelfDefaultVal());
		} else {
			this.Items.add(new ListItem(this.getSelfDefaultText(), this
					.getSelfDefaultVal()));
		}
	}
	
	private void DealRightKey(String TBDataHelpKey) {

	}


	private void TBInit()
	{
		this.SelfBind();
		return;
		// 可以保留如下代码。 
		//if (this.Enabled)
		//{
		//    //url =appPath+"Comm/DataHelp.htm?"+appPath+"Comm/HelperOfTB.jsp?EnsName="+this.EnsName+"&AttrKey="+this.AttrKey ;
		//    //script=" if ( event.button != 2)  return; str="+this.ClientID+".value;str= window.showModalDialog('"+url+"&Key=\'+str, '','dialogHeight: 500px; dialogWidth:850px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); if ( str==undefined) return ; "+this.ClientID+".value=str ; ";
		//    //	this.Attributes["onmousedown"]=script;
		//    //string appPath =this.Page.Request.ApplicationPath;
		//    //string url=appPath+"Comm/DataHelp.htm?"+appPath+"Comm/HelperOfDDL.jsp?EnsName="+attr.getUIBindKey()+"&RefKey="+attr.getUIRefKeyValue()+"&RefText="+attr.getUIRefKeyText();
		//    //string script=" if ( event.button != 2 )  return; str="+this.ClientID+".DataTextField; str= window.showModalDialog('"+url+"&Key=\'+str , '','dialogHeight: 500px; dialogWidth:800px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); if ( str==undefined) return ;  "+this.ClientID+".DataTextField=str ; ";
		//    //string script=" if ( event.button != 2 )  return; window.showModalDialog('"+url+"' , '','dialogHeight: 500px; dialogWidth:800px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); SetDDLVal('"+this.ClientID+"',str ) ; ";

		//    if (this.SelfBindKey != null)
		//    {
		//        this.Attributes["onmousedown"] = " if ( event.button != 2 ) return; HalperOfDDL('" + this.AppPath + "','" + this.SelfBindKey + "','" + this.SelfEnsRefKey + "','" + this.SelfEnsRefKeyText + "','" + this.ClientID.ToString() + "' )";
		//    }
		//}
		//this.SelfBind();
	}


	/** 
	 new ddl
	 
	 @param attr 属性
	 @param DefaultVal DefaultVal
	 @param DefaultText DefaultText
	 @param enable enable
	*/
	public DDL(Attr attr, String DefaultVal, String DefaultText, boolean enable, String appPath)
	{
		this.setAppPath(appPath);
		this.setId("DDL_"+attr.getKey());
		//this.BorderStyle=BorderStyle.None;
		this.setCssClass("DDL"+WebUser.getStyle());

//		if(null != attr.UITag){
//			String[] s=attr.UITag.split("@");
//			for(String ss:s){
//				if(ss!=null && !ss.equals("")){
//					String[] str=ss.split("=");
//					this.Items.add(new ListItem(str[1],str[0]));
//				}
//			}
//		}
		
		this.SetSelectItem(DefaultVal);
		this.setSelfDefaultText(DefaultText);
		this.setSelfDefaultVal(DefaultVal);

		this.setEnabled(enable);
		this.setSelfShowType(attr.getUIDDLShowType());

		this.setSelfBindKey(attr.getUIBindKey());
		this.setSelfEnsRefKey(attr.getUIRefKeyValue());
		this.setSelfEnsRefKeyText(attr.getUIRefKeyText());
		this.HisFKEns = attr.getHisFKEns();
		this.setSelfIsShowVal(false); ///不让显示编号

		this.TBInit();
		//this.PreRender +=new System.EventHandler(this.DDLPreRender);

		//this.Init += new System.EventHandler(this.TBInit);
	}
	public final void LoadMapAttr(Attr attr)
	{
		this.setId("DDL_" + attr.getKey());
	   // this.AppPath = BP.Sys.SystemConfig.AppName;
		//this.BorderStyle = BorderStyle.None;

		this.setSelfDefaultText(attr.getUIRefKeyText());
		this.setSelfDefaultVal(attr.getUIRefKeyValue());

		this.setEnabled(attr.getUIIsReadonly());
		if (attr.getMyDataType() == DataType.AppInt)
		{
			this.setSelfShowType(DDLShowType.SysEnum);
		}
		else
		{
			this.setSelfShowType(DDLShowType.Ens);
		}

		this.HisFKEns = attr.getHisFKEns();

		this.setSelfBindKey(attr.getUIBindKey());
		this.setSelfEnsRefKey(attr.getUIRefKeyValue());
		this.setSelfEnsRefKeyText(attr.getUIRefKeyText());
		this.setSelfIsShowVal(false); ///不让显示编号
		
		TBInit();
		//this.Init += new System.EventHandler(this.TBInit);
	}



	public DDL(Attr attr, String DefaultVal, String DefaultText, boolean enable)
	{
		this.setId("DDL_"+attr.getKey());
		this.setAppPath(ContextHolderUtils.getRequest().getRequestURI());
		//this.BorderStyle=BorderStyle.None;
		this.setCssClass("DDL"+WebUser.getStyle());

		this.setSelfDefaultText(DefaultText);
		this.setSelfDefaultVal(DefaultVal);

		this.setEnabled(enable);
		this.setSelfShowType(attr.getUIDDLShowType());

		this.setSelfBindKey(attr.getUIBindKey());
		this.setSelfEnsRefKey(attr.getUIRefKeyValue());
		this.setSelfEnsRefKeyText(attr.getUIRefKeyText());
		this.HisFKEns = attr.getHisFKEns();

		this.setSelfIsShowVal(false); ///不让显示编号
		//this.PreRender +=new System.EventHandler(this.DDLPreRender);

		//this.Init += new System.EventHandler(this.TBInit);
	}
	public final void LoadMapAttr(MapAttr attr)
	{
		this.setId("DDL_" + attr.getKeyOfEn());
		// this.AppPath = BP.Sys.SystemConfig.AppName;
		//this.BorderStyle = BorderStyle.None;

		this.setSelfDefaultText(attr.getUIRefKeyText());
		this.setSelfDefaultVal(attr.getUIRefKey());

		this.setEnabled(attr.getUIIsEnable());
		if (attr.getMyDataType() == DataType.AppInt)
		{
			this.setSelfShowType(DDLShowType.SysEnum);
			//SysEnums ses = new SysEnums(attr.getUIBindKey());
		   // this.BindSysEnum(attr.getUIBindKey());
		}
		else
		{
			this.setSelfShowType(DDLShowType.Ens);
			this.HisFKEns = BP.En.ClassFactory.GetEns(attr.getUIBindKey());

			//this.HisFKEns.RetrieveAll();
			//this.BindEntities(this.HisFKEns, "No", "Name", false);
		}


	  //  SelfEnsRefKey
		this.setSelfBindKey(attr.getUIBindKey());
		this.setSelfEnsRefKey(attr.getUIRefKey());
		this.setSelfEnsRefKeyText(attr.getUIRefKeyText());
		this.setSelfIsShowVal(false); ///不让显示编号
									/**
									*/

		//this.Init += new System.EventHandler(this.TBInit);
	}


	public DDL(Attr attr, String defSelectVal)
	{
		this.setId("DDL_" + attr.getKey());
		this.setSelfShowType(attr.getUIDDLShowType());
		this.setSelfBindKey(attr.getUIBindKey());
		this.setSelfEnsRefKey(attr.getUIRefKeyValue());
		this.setSelfEnsRefKeyText(attr.getUIRefKeyText());
		this.setSelfDefaultVal(defSelectVal);
		this.HisFKEns = attr.getHisFKEns();


		// this.SelfAddAllLocation = AddAllLocation.Top;

		if (attr.getUIBindKey().equals("BP.Port.SJs"))
		{

		}
		else
		{
			this.setSelfAddAllLocation(AddAllLocation.Top);
		}
		this.setSelfIsShowVal(false); ///不让显示编号
	}

	public DDL()
	{
		//this.PreRender +=new System.EventHandler(this.DDLPreRender);

		//this.Init += new System.EventHandler(this.TBInit);
		//this.Load +=new System.EventHandler(this.TBPreRender);			 
	}
	public final void Bind(EntitiesNoName ens, String seleVal)
	{
		this.BindEntitiesNoName(ens, seleVal);
	}
	public final void Bind(XmlEns xmls, String key, String name)
	{
		for (XmlEn xml : xmls.ToJavaListXmlEns())
		{
			this.Items.add(new ListItem(xml.GetValStringByKey(name), xml.GetValStringByKey(key)));
		}
	}
	public final void Bind(Entities ens, String key, String name)
	{
		this.Items.clear();
		for (Entity en : ens.ToJavaListEn())
		{
			this.Items.add(new ListItem(en.GetValStringByKey(name), en.GetValStringByKey(key)));
		}
	}
	
	/**
	 * 绑定一个table,并设置选择的值.
	 * @param dt 数据源
	 * @param val 值列
	 * @param text 标签列
	 * @param selectVal 选中的值
	 * @return 返回是否选择成功
	 */
	public final boolean Bind(DataTable dt, String val, String text, String selectVal)
	{
		this.Items.clear();
		if (dt.Rows.size() == 0)
		{
			ListItem li = new ListItem("无", "无");
			this.Items.add(li);
			return false;
		}

		boolean isHave = false;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue(val).toString().equals(selectVal))
			{
				ListItem li = new ListItem(dr.getValue(text).toString(), dr.getValue(val).toString());
				li.setSelected(true);
				isHave = true;
				this.Items.add(li);
			}
			else
			{
				this.Items.add(new ListItem(dr.getValue(text).toString(), dr.getValue(val).toString()));
			}
		}
		return isHave;
	}

	public ListItem FindByValue(String all)
	{
		for(ListItem item : this.Items)
		{
			if(item.getValue().equals(all))
				return item;
		}
		return null;
	}

	protected void OnPreRender()
	{

		if (this.getSelfAddAllLocation() == AddAllLocation.None)
		{
			return;
		}

		if (this.FindByValue("all") == null)
		{
			return;
		}

		if (this.getSelfAddAllLocation() == AddAllLocation.Top || this.getSelfAddAllLocation() == AddAllLocation.TopAndEnd)
		{
			ListItem li = new ListItem("-=全部=-", "all");
			this.Items.add(li);
		}

		if (this.getSelfAddAllLocation() == AddAllLocation.TopAndEnd)
		{
			this.Items.add(new ListItem("-=全部=-", "all"));
		}
		//super.OnPreRender();
	}


		///#region 处理用户风格

	public final void Style3()
	{
		this.setCssClass("DDL3");
//            
//            //this.BorderColor=Color.Transparent;
//			this.BackColor=Color.FromName("#006699");
//			this.ForeColor=Color.White;		
//				 
	}
	public final void Style2()
	{
		this.setCssClass("DDL2");
//            
//			this.BorderColor=System.Drawing.Color.Transparent;
//			this.BackColor=Color.FromName("#DEBA84");
//			this.ForeColor=Color.Black;
//			
	}
	public final void Style1()
	{
		this.setCssClass("DDL1");
		//this.BorderColor=System.Drawing.Color.FromName("#DEBA84");
		//this.BackColor=Color.FromName("#DEBA84");
		//this.ForeColor=Color.Black;			 			 
	}
		///#endregion



	/** 
	 OID, No, Name 
	 
	 @param dt
	*/
	public final void BindWithOIDNoNameCol(DataTable dt)
	{
		this.Items.clear();
		for (DataRow dr : dt.Rows)
		{
			ListItem li = new ListItem(dr.getValue("No").toString()+" "+dr.getValue("Name").toString(),dr.getValue("OID").toString());
			this.Items.add(li);
		}
	}
	/** 
	 OID,   Name 
	 
	 @param dt
	*/
	public final void BindWithOIDNameCol(DataTable dt)
	{
		this.Items.clear();
		for (DataRow dr : dt.Rows)
		{
			ListItem li = new ListItem(dr.getValue("Name").toString(),dr.getValue("OID").toString());
			this.Items.add(li);
		}
	}
	/** 
	 OID,   Name 
	 
	 @param dt
	*/
	public final void BindWithOIDNameCol(DataTable dt, String title, String selectVal)
	{
		this.Items.clear();
		this.Items.add(new ListItem("-="+title+"(全部)=-","all"));
		for (DataRow dr : dt.Rows)
		{
			ListItem li = new ListItem(dr.getValue("Name").toString(),dr.getValue("OID").toString());
			this.Items.add(li);
		}
		this.Items.add(new ListItem("-="+title+"(全部)=-","all"));
		for(ListItem li : this.Items)
		{
			if (selectVal.equals(li.getValue()))
			{
				li.setSelected(true);
				break;
			}
		}
	}

	/** 
	 No ,Name  bind
	 
	 @param dt
	*/
	public final void BindWithNoNameCol(DataTable dt, String title)
	{
		this.Items.clear();
		this.Items.add(new ListItem("-="+title+"(全部)=-","all"));
		for (DataRow dr : dt.Rows)
		{
			ListItem li = new ListItem(dr.getValue("Name").toString().trim(),dr.getValue("No").toString().trim());
			this.Items.add(li);
		}
		this.Items.add(new ListItem("-="+title+"(全部)=-","all"));
	}
	/** 
	 No ,Name bind
	 
	 @param dt
	*/
	public final void BindWithNoNameCol(DataTable dt)
	{
		this.Items.clear();
		for (DataRow dr : dt.Rows)
		{
			ListItem li = new ListItem(dr.getValue("Name").toString().trim(),dr.getValue("No").toString().trim());
			this.Items.add(li);
		}
	}
	public final void BindWithNoNameCol(DataTable dt, String title, String selectNo)
	{
		this.Items.clear();
		BindWithNoNameCol(dt, title);
		for(ListItem li : this.Items)
		{
			if (li.getValue().equals(selectNo))
			{
				li.setSelected(true);
				break;
			}
		}
	}
	public final void BindMonth(int month)
	{
		this.Items.clear();
		int i = 0;
		String str="";
		while (i < 12)
		{
			i++;
			str=(new Integer(i)).toString();
			if (str.length()==1)
			{
				str="0"+str;
			}
			ListItem li = new ListItem((new Integer(i)).toString()+"月", str);

			if (i==month)
			{
				li.setSelected(true);
			}

			this.Items.add(li);
		}
	}
	public final void Bindhh(int hh)
	{
		this.Items.clear();
		int i = 7;
		String str="";
		while (i < 20)
		{
			i++;
			str=(new Integer(i)).toString();
			if (str.length()==1)
			{
				str="0"+str;
			}
			ListItem li = new ListItem((new Integer(i)).toString()+"时", str);

			if (i==hh)
			{
				li.setSelected(true);
			}
			this.Items.add(li);
		}
	}
	public final void BindNumFromTo(int from, int to)
	{
		this.Items.clear();
		int i = from;
		String str = "";
		while (i <= to)
		{
			i++;
			str = (new Integer(i)).toString();
			if (str.length() == 1)
			{
				str = "0" + str;
			}

			ListItem li = new ListItem((new Integer(i)).toString(), str);
			this.Items.add(li);
		}
	}
	public final void Bindmm()
	{
		this.Items.clear();
		int i = 0;
		String str="";
		while (i < 59)
		{
			i++;
			str=(new Integer(i)).toString();
			if (str.length()==1)
			{
				str="0"+str;
			}
			ListItem li = new ListItem((new Integer(i)).toString()+"分", str);

			this.Items.add(li);
		}
	}
	public final void BindQuector(int selectVal)
	{
		this.Items.clear();

		this.Items.add(new ListItem(":00","00"));
		this.Items.add(new ListItem(":15","15"));
		this.Items.add(new ListItem(":30","30"));
		this.Items.add(new ListItem(":45","45"));

		this.SetSelectItem(selectVal);
	}
	public final void BindWeek(int selectVal)
	{
		this.Items.clear();

		this.Items.add(new ListItem("周日","0"));
		this.Items.add(new ListItem("周一","1"));
		this.Items.add(new ListItem("周二","2"));
		this.Items.add(new ListItem("周三","3"));
		this.Items.add(new ListItem("周四","4"));
		this.Items.add(new ListItem("周五","5"));
		this.Items.add(new ListItem("周六","6"));

		this.SetSelectItem(selectVal);
	}
	public final void BindDay(int day)
	{
		this.Items.clear();
		int i = 0;
		String str="";

		while (i < 31)
		{
			i++;
			str=(new Integer(i)).toString();
			if (str.length()==1)
			{
				str="0"+str;
			}

			ListItem li = new ListItem((new Integer(i)).toString()+"日", str);
			if (i==day)
			{
				li.setSelected(true);
			}
			this.Items.add(li);
		}
	}
	/** 
	 bind month ，到当前的月份。
	 
	*/
	public final void BindMonthToThisMonth()
	{
		this.Items.clear();
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.MONTH) + 1;
		int i = 0;
		while (i < 12)
		{
			i++;
			ListItem li = new ListItem((new Integer(i)).toString()+"月", (new Integer(i)).toString());
			if (i==m)
			{
				this.Items.add(li);
				li.setSelected(true);
				break;
			}
			this.Items.add(li);
		}

	}
	/** 
	 bind day 。
	 
	*/
	public final void BindAppDaySelectedToday()
	{
		this.Items.clear();
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.DATE);
		int i = 0;
		while (i < 31)
		{
			i++;
			ListItem li = new ListItem((new Integer(i)).toString()+"日", (new Integer(i)).toString());
			if (i==m)
			{
				this.Items.add(li);
				li.setSelected(true);
				continue;
			}
			this.Items.add(li);
		}

	}
	/** 
	 季度
	 
	*/
	public final void BindQuarter()
	{
		this.Items.clear();
		this.Items.add(new ListItem("第一季度","1"));
		this.Items.add(new ListItem("第二季度","2"));
		this.Items.add(new ListItem("第三季度","3"));
		this.Items.add(new ListItem("第四季度","4"));

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		if (month < 4)
		{
			this.SetSelectItem("1");
		}
		else if (month >=4 && month < 7)
		{
			this.SetSelectItem("2");
		}
		else if (month >=8 && month < 10)
		{
			this.SetSelectItem("3");
		}
		else
		{
			this.SetSelectItem("4");
		}
	}
	public final void BindStrs(String[] strs)
	{
		int i = -1;
		for (String s : strs)
		{
			i++;
			ListItem li = new ListItem(s, (new Integer(i)).toString());
			this.Items.add(li);
		}
	}
	public final void BindAppDay()
	{
		int i = 1;
		while (i < 31)
		{
			i++;
			ListItem li = new ListItem((new Integer(i)).toString()+"日", (new Integer(i)).toString());
			this.Items.add(li);
		}
	}
	public final void BindAppDay(String selectedDay)
	{
		int i =1;
		int m = Integer.parseInt(selectedDay);
		String str="";
		while (i < 31)
		{
			i++;

			str=(new Integer(i)).toString();
			if (str.length()==1)
			{
				str="0"+str;
			}

			ListItem li = new ListItem((new Integer(i)).toString()+"日", str);
			if (i==m)
			{
				this.Items.add(li);
				continue;
			}
			this.Items.add(li);
		}
	}

	public final void BindMonthSelectCurrentMonth()
	{
		this.Items.clear();
		
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.MONTH) + 1;
		int i = 0;

		String str="";
		while (i < 12)
		{
			i++;
			str=(new Integer(i)).toString();
			if (str.length()==1)
			{
				str="0"+str;
			}

			ListItem li = new ListItem((new Integer(i)).toString()+"月", str);
			if (i==m)
			{
				li.setSelected(true);
			}
			this.Items.add(li);
		}
	}
	/** 
	 月份  1 ， 2， 
	 
	 @param selectM
	*/
	public final void BindMonth(String selectM)
	{
		this.Items.clear();
		int i = 0;
		String str="";

		int m = Integer.parseInt(selectM);
		while (i < 12)
		{
			i++;
			str=(new Integer(i)).toString();
			if (str.length()==1)
			{
				str="0"+str;
			}

			ListItem li = new ListItem((new Integer(i)).toString()+"月", str);
			if (i==m)
			{
				li.setSelected(true);
			}

			this.Items.add(li);
		}
	}

	/** 
	 bind 近三年, 默认值是当前的年
	 
	*/
	public final void BindYearMonth(int nearM)
	{
		java.util.Date dt = new java.util.Date();
		for (int i = 0; i <= nearM; i++)
		{
			java.text.SimpleDateFormat formatter1 = new java.text.SimpleDateFormat("yyyy年MM月");
			java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat("yyyy-MM");
			Calendar rightNow = Calendar.getInstance();
			this.Items.add(new ListItem(formatter1.format(dt), formatter2.format(dt)));
		    rightNow.add(Calendar.MONTH,-1);
		    dt = rightNow.getTime();
			i++;
		}
	}
	/** 
	 bind 近三年, 默认值是当前的年
	 
	*/
	public final void BindYear()
	{
		this.Items.clear();
		Calendar cal = Calendar.getInstance();
		int i1 = cal.get(Calendar.YEAR);
		int i2 = i1 - 1;
		int i3 = i1 - 2;
		int i4 = i1 - 3;
		int i5 = i1 - 4;

		this.Items.add(new ListItem((new Integer(i1)).toString() + "年", (new Integer(i1)).toString()));
		this.Items.add(new ListItem((new Integer(i2)).toString() + "年", (new Integer(i2)).toString()));
		this.Items.add(new ListItem((new Integer(i3)).toString() + "年", (new Integer(i3)).toString()));
		this.Items.add(new ListItem((new Integer(i4)).toString() + "年", (new Integer(i4)).toString()));
		this.Items.add(new ListItem((new Integer(i5)).toString() + "年", (new Integer(i5)).toString()));
	}

	/** 
	 bind 近三年。
	 
	 @param selectYear 选择的年度
	*/
	public final void BindYear(int selectYear)
	{
		this.BindYear();
		for (ListItem li : this.Items)
		{
			if (li.getValue().equals((new Integer(selectYear)).toString()))
			{
				li.setSelected(true);
			}
		}
	}
	/** 
	 bind  近两年的。
	 
	*/
	public final void BindYearNear2()
	{
		this.Items.clear();
	}
	public final void BindAppPrecent(int selectVal)
	{
		int i = 0;
		while (true)
		{
			this.Items.add(new ListItem((new Integer(i)).toString() + "%", (new Integer(i)).toString()));
			i++;
			if (i == 100)
			{
				break;
			}
		}
	}
	public ListItem getSelectedItem()
	{
		for(ListItem item : this.Items)
		{
			if(item.getSelected())
				return item;
		}
		return this.Items.size() > 0 ? this.Items.get(0) : null;
	}
	public final int getSelectedItemIntVal()
	{
		return Integer.parseInt(this.getSelectedItem().getValue());
	}
	public final String getSelectedItemStringVal()
	{
		return this.getSelectedItem() == null? null :this.getSelectedItem().getValue();
	}

		///#region bind 实体。

	public final void BindEntities(Entities cateEns, String cateKey, String cateText, Entities subEns, String subKey, String subText, String refKey)
	{
		this.Items.clear();
		for (Entity en : cateEns.ToJavaListEn())
		{
			ListItem li = new ListItem("==="+en.GetValStringByKey(cateText)+"=====", en.GetValStringByKey(cateKey));
			li.addAttr("background-color","Green");
			li.addAttr("color","Green");

			this.Items.add(li);
			for (Entity suben : subEns.ToJavaListEn())
			{
				if (suben.GetValStringByKey(refKey)==en.GetValStringByKey(cateKey))
				{
					this.Items.add(new ListItem("|-"+suben.GetValStringByKey(subText), suben.GetValStringByKey(subKey)));
				}
			}
		}
	}

	/** 
	 绑定生成一个有树结构的下拉菜单
	 
	 @param dtNodeSets 菜单记录数据所在的表
	 @param strParentColumn 表中用于标记父记录的字段
	 @param strRootValue 第一层记录的父记录值(通常设计为0或者-1或者Null)用来表示没有父记录
	 @param strIndexColumn 索引字段，也就是放在DropDownList的Value里面的字段
	 @param strTextColumn 显示文本字段，也就是放在DropDownList的Text里面的字段
	 @param drpBind 需要绑定的DropDownList
	 @param i 用来控制缩入量的值，请输入-1 
	*/
//	public static void MakeTree(DataTable dtNodeSets, String strParentColumn, String strRootValue, String strIndexColumn, String strTextColumn, DropDownList drpBind, int i)
//	{
//		//每向下一层，多一个缩入单位 
//		i++;
//
//		DataView dvNodeSets = new DataView(dtNodeSets);
//		dvNodeSets.RowFilter = strParentColumn + "=" + strRootValue;
//
//		String strPading = ""; //缩入字符
//
//		//通过i来控制缩入字符的长度，我这里设定的是一个全角的空格 
//		for (int j = 0; j < i; j++)
//		{
//			strPading += "　"; //如果要增加缩入的长度，改成两个全角的空格就可以了
//		}
//
//		for (DataRowView drv : dvNodeSets)
//		{
//			TreeNode tnNode = new TreeNode();
//			ListItem li = new ListItem(strPading + "├" + drv[strTextColumn].toString(), drv[strIndexColumn].toString());
//			drpBind.Items.Add(li);
//			MakeTree(dtNodeSets, strParentColumn, drv[strIndexColumn].toString(), strIndexColumn, strTextColumn, drpBind, i);
//		}
//		//递归结束，要回到上一层，所以缩入量减少一个单位 
//		i--;
//	}


	public final void BindEntities(Entities ens, String refKey, String refText)
	{

///#warning 去了在2009-03-09
		this.Items.clear();

		if (ens.size() == 0)
		{
			ens.RetrieveAll();
		}

///#warning 这里出错误。

		//EntitiesTree treeEns = ens as EntitiesTree;
		//if (treeEns != null)
		//{
		//    DataTable dt = ens.ToDataTableField();
		//    MakeTree(dt, "ParentNo", "0", "No", "Name", this, -1);
		//    return;
		//}

		//EntitiesSimpleTree treeSimpEns = ens as EntitiesSimpleTree;
		//if (treeSimpEns != null)
		//{
		//    DataTable dt = ens.ToDataTableField();
		//    MakeTree(dt, "ParentNo", "0", "No", "Name", this, -1);
		//    return;
		//}

		for (Entity en : ens.ToJavaListEn())
		{
			this.Items.add(new ListItem(en.GetValStringByKey(refText), en.GetValStringByKey(refKey)));
		}
	}
	public final void BindEntities(EntitiesOIDName ens)
	{
		this.Items.clear();
		for(Object en : ens)
		{
			this.Items.add(new ListItem(((EntityOIDName)en).getName(),
					String.valueOf(((EntityOIDName)en).getOID())));
		}
	}
	public final void BindEntities(EntitiesNoName ens, AddAllLocation where)
	{
		this.Items.clear();
		if (where== AddAllLocation.Top || where== AddAllLocation.TopAndEnd)
		{
			this.Items.add(new ListItem("全部", "all"));
		}

		for (Object en : ens)
		{
			this.Items.add(new ListItem(((EntityNoName)en).getName(), 
					((EntityNoName)en).getNo()));
		}

		if (where == AddAllLocation.End || where == AddAllLocation.TopAndEnd)
		{
			this.Items.add(new ListItem("全部", "all"));
		}
	}
	public final void BindEntities(EntitiesOIDName ens, AddAllLocation where)
	{
		this.Items.clear();
		if (where == AddAllLocation.Top || where == AddAllLocation.TopAndEnd)
		{
			this.Items.add(new ListItem("全部", "all"));
		}

		for(Object en : ens)
		{
			this.Items.add(new ListItem(((EntityOIDName)en).getName(),
					String.valueOf(((EntityOIDName)en).getOID())));
		}

		if (where == AddAllLocation.End || where == AddAllLocation.TopAndEnd)
		{
			this.Items.add(new ListItem("全部", "all"));
		}
	}
	public final synchronized void BindEntities(EntitiesNoName ens)
	{
		this.Items.clear();
		int size = ens.size();
		for (int i = 0; i < size; i++) {
			Object en = ens.get(i);
			this.Items.add(new ListItem(((EntityNoName)en).getName(), 
					((EntityNoName)en).getNo()));
		}
	}
	public final void BindEntitiesNoName(EntitiesNoName ens, boolean isShowKey)
	{
		this.Items.clear();
		for (EntityNoName en : ens.ToJavaListEnNo())
		{
			ListItem li = new ListItem();
			if (isShowKey)
			{
				li.setValue(en.getNo());
				li.setText(en.getNo()+" "+en.getName());
			}
			else
			{
				li.setValue(en.getNo());
				li.setText(en.getName());
			}
			this.Items.add(li);
		}
	}
	public final void BindEntitiesNoNameWithSelectAll(EntitiesNoName ens, boolean IsShowKey)
	{
		this.Items.clear();
		this.Items.add(new ListItem("-=请选择=-","all"));

		for (EntityNoName en : ens.ToJavaListEnNo())
		{
			if (IsShowKey)
			{
				this.Items.add(new ListItem(en.getNo()+en.getName(),en.getNo()));
			}
			else
			{
				this.Items.add(new ListItem(en.getName(),en.getNo()));
			}
		}
	}
	public final void BindEntitiesNoName(EntitiesNoName ens, String selecVal)
	{
		this.Items.clear();
		for (EntityNoName en : ens.ToJavaListEnNo())
		{
			this.Items.add(new ListItem(en.getName(),en.getNo()));
		}

		for (ListItem li : this.Items)
		{
			if (selecVal.equals(li.getValue()))
			{
				li.setSelected(true);
			}
		}
	}
		///#endregion

		///#region app
	/** 
	 
	 
	 @param ens
	*/
	public final void BindEntitiesOIDName(EntitiesOIDName ens)
	{
		this.Items.clear();
		for (EntityOIDName en : ens.ToJavaList())
		{
			this.Items.add(new ListItem(en.getName(),String.valueOf(en.getOID())));
		}
	}
	public final void BindEntitiesOIDName(EntitiesOIDName ens, int seleOID)
	{
		this.Items.clear();
		 this.BindEntitiesOIDName(ens);
		for(ListItem li : this.Items)
		{
			if ((new Integer(seleOID)).toString().equals(li.getValue()))
			{
				li.setSelected(true);
			}
		}
	}

	public final void BindEntitiesNoName(EntitiesNoName ens)
	{
		this.Items.clear();
		for (EntityNoName en : ens.ToJavaListEnNo())
		{
			this.Items.add(new ListItem(en.getName(), en.getNo()));
		}
	}



	public final void BindAppYesOrNo(int selectedVal)
	{
		this.Items.clear();

		this.Items.add(new ListItem("是", "1"));
		this.Items.add(new ListItem("否", "0"));

		for(ListItem li : this.Items)
		{
			if (li.getValue().equals((new Integer(selectedVal)).toString()))
			{
				li.setSelected(true);
				break;
			}
		}
	}


	public final void SetSelectItem(int val)
	{
		this.SetSelectItem((new Integer(val)).toString());
	}

	public static boolean SetSelectItem(ArrayList<ListItem> items,String val)
	{
		try
		{
			for (ListItem li : items)
			{
				li.setSelected(false);
			}

			for (ListItem li : items)
			{

				if (val.equals(li.getValue()))
				{
					li.setSelected(true);
					return true;
				}
				else
				{
					li.setSelected(false);
				}
			}

		}
		catch (java.lang.Exception e)
		{

		}
		return false;
	}
	public final void SetSelectItem(String val, Attr attr)
	{
		if (attr.getUIBindKey().equals("BP.Port.Depts"))
		{
			if (!val.contains(WebUser.getFK_Dept()))
			{
				return;
			}

			this.Items.clear();
			BP.Port.Dept detps = new BP.Port.Dept(val);

			ListItem li1 = new ListItem();
			li1.setText(detps.GetValStrByKey(attr.getUIRefKeyText()));
			li1.setValue(detps.GetValStrByKey(attr.getUIRefKeyValue()));
			this.Items.add(li1);
			return;
		}

		if (attr.getUIBindKey().equals("BP.Port.Units"))
		{
			//if (val.Contains(WebUser.FK_Unit) == false)
			//    return;

			//this.Items.clear();
			//BP.Port.Unit units = new BP.Port.Unit(val);

			//ListItem li1 = new ListItem();
			//li1.Text = units.GetValStrByKey(attr.getUIRefKeyText());
			//li1.Value = units.GetValStrByKey(attr.getUIRefKeyValue());
			//this.Items.add(li1);
			return;
		}

		//this.SetSelectItem(val);
		//return;

		if (this.SetSelectItem(val))
		{
			return;
		}

		return;

//                Entity en = attr.HisFKEn; // ClassFactory.GetEn(attr.getUIBindKey());
//                en.PKVal = val;
//                en.Retrieve();


///#warning edit: 2008-06-01  en.RetrieveFromDBSources();


//            ListItem li = new ListItem();
//            li.Text = en.GetValStrByKey(attr.getUIRefKeyText());
//            li.Value = en.GetValStrByKey(attr.getUIRefKeyValue());

//            if (this.Items.Contains(li))
//            {
//                this.SetSelectItem(val);
//                return;
//            }



//            ListItem liall = this.GetItemByText("请用更多...");
//            ListItem myall = this.GetItemByVal("all");
//            if (myall != null)
//            {
//                this.Items.clear();
//                this.Items.add(li);
//                this.Items.add(myall);

//                // this.Items.remove(liall);
//            }

//            //  this.Items.add(li);
//            this.SetSelectItem(val);
	}
	/** 
	 设置选择的值
	 
	 @param val
	*/
	public final boolean SetSelectItem(String val)
	{
		
		if (val == null){
			return false;
		}
		
		for (ListItem li : this.Items){
			li.setSelected(false);
		}
		
		boolean isHaveSelect = false;

		for (ListItem li : this.Items){
			if (val.equals(li.getValue())){
				li.setSelected(true);
				isHaveSelect = true;
			}else{
				li.setSelected(false);
			}
		}
		return isHaveSelect;
	}
	public final ListItem GetItemByVal(String val)
	{
		for (ListItem li : this.Items)
		{
			if (val.equals(li.getValue()))
			{
				return li;
			}
		}
		return null;
	}
	public final ListItem GetItemByText(String text)
	{
		for (ListItem li : this.Items)
		{
			if (text.equals(li.getText()))
			{
				return li;
			}
		}
		return null;
	}
	public final void ReomveItem(String val)
	{
		//int  i = -1 ;
		for(int ii = 0 ; ii < this.Items.size(); ii++)
		{
			if (val.equals(this.Items.get(ii).getValue()))
			{
				this.Items.remove(ii);
				break;
			}
		}
	}
	/** 
	违规次数
	 
	*/
	public final void BindAppWGCS(int selected)
	{
		this.Items.clear();
		for(int i = 0 ; i <=12 ; i ++)
		{
			ListItem li = new ListItem((new Integer(i)).toString(), (new Integer(i)).toString());
			if (i ==selected)
			{
				li.setSelected(true);
			}
			this.Items.add(li);
		}
	}
	/** 
	 降序/升序
	 
	*/
	public final void BindAppDESCandASC()
	{
		this.Items.clear();
		this.Items.add(new ListItem("降序","DESC"));
		this.Items.add(new ListItem("升序","ASC"));
	}
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("<select ");
		str.append(" name =  \""+this.getName()+"\" ");
		str.append(" id =  \""+this.getId()+"\" ");
		if(!this.getEnabled())
			str.append(" disabled = \"disabled\"");
		if(this.getReadOnly())
			str.append(" disabled = \"disabled\"");
		str.append(" class = \""+this.getCssClass()+"\"");
		//str.append(" style = \"border-style:None;\"");
		str.append(" style = \"min-width:90px;\"");
		str.append(this.buildAttributes()).append(">");
		str.append(this.buildOption()).append("</select>");
		return str.toString();
	}
	
	private String buildOption()
	{
		StringBuilder str = new StringBuilder();
		for(ListItem item : this.Items)
		{
			if(!item.getSelected())
			{
				str.append("<option value=\""+item.getValue()+"\">"+item.getText()+"</option>");
			}else
			{
				str.append("<option selected=\"selected\" value=\""+item.getValue()+"\">"+item.getText()+"</option>");
			}
		}
		return str.toString();
		
	}
	
}