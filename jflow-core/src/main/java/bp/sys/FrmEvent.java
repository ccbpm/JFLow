package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.web.*;
import bp.difference.*;
import bp.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 事件
 节点的节点保存事件有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class FrmEvent extends EntityMyPK
{

		///#region 参数属性.
	/** 
	 名称
	*/
	public final String getMonthedDLL() throws Exception {
		return this.GetParaString(FrmEventAttr.MonthedDLL);

	}
	public final void setMonthedDLL(String value)throws Exception
	{this.SetPara(FrmEventAttr.MonthedDLL, value);
	}


	/** 
	 类名
	*/
	public final String getMonthedClass() throws Exception {
		return this.GetParaString(FrmEventAttr.MonthedClass);

	}
	public final void setMonthedClass(String value)throws Exception
	{this.SetPara(FrmEventAttr.MonthedClass, value);
	}
	/** 
	 方法名
	*/
	public final String getMonthedName() throws Exception {
		return this.GetParaString(FrmEventAttr.MonthedName);

	}
	public final void setMonthedName(String value)throws Exception
	{this.SetPara(FrmEventAttr.MonthedName, value);
	}
	/** 
	 方法参数.
	*/
	public final String getMonthedParas() throws Exception {
		return this.GetParaString(FrmEventAttr.MonthedParas);

	}
	public final void setMonthedParas(String value)throws Exception
	{this.SetPara(FrmEventAttr.MonthedParas, value);
	}

	public final String getFK_DBSrc() throws Exception
	{
		return this.GetValStringByKey(FrmEventAttr.FK_DBSrc);
	}

		///#endregion 参数属性.


		///#region 基本属性
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.IsAdjunct = false;
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}
	/** 
	 节点ID
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(FrmEventAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.FK_Node, value);
	}
	/** 
	 关联的流程编号
	*/
	public final String getRefFlowNo() throws Exception
	{
		return this.GetValStringByKey(FrmEventAttr.RefFlowNo);
	}
	public final void setRefFlowNo(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.RefFlowNo, value);
	}
	/** 
	 流程
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(FrmEventAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.FK_Flow, value);
	}
	/** 
	 节点
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStringByKey(FrmEventAttr.FK_MapData);
	}
	public final void setFKMapData(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.FK_MapData, value);
	}
	public final void setFK_MapData(String val)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.FK_MapData, val);
	}
	public final String getDoDoc() throws Exception
	{
		return this.GetValStringByKey(FrmEventAttr.DoDoc).replace("~", "'");
	}
	public final void setDoDoc(String value)throws Exception
	{String doc = value.replace("'", "~");
		this.SetValByKey(FrmEventAttr.DoDoc, doc);
	}
	/** 
	 执行成功提示
	*/
	public final String MsgOK(Entity en)
	{
		String val = this.GetValStringByKey(FrmEventAttr.MsgOK);
		if (val.trim().equals(""))
		{
			return "";
		}

		if (val.indexOf('@') == -1)
		{
			return val;
		}

		for (Attr attr : en.getEnMap().getAttrs())
		{
			val = val.replace("@" + attr.getKey(), en.GetValStringByKey(attr.getKey()));
		}
		return val;
	}
	public final String getMsgOKString() throws Exception
	{
		return this.GetValStringByKey(FrmEventAttr.MsgOK);
	}
	public final void setMsgOKString(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.MsgOK, value);
	}
	public final String getMsgErrorString() throws Exception
	{
		return this.GetValStringByKey(FrmEventAttr.MsgError);
	}
	public final void setMsgErrorString(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.MsgError, value);
	}
	/** 
	 错误或异常提示
	 
	 param en
	 @return 
	*/
	public final String MsgError(Entity en)
	{
		String val = this.GetValStringByKey(FrmEventAttr.MsgError);
		if (val.trim().equals(""))
		{
			return null;
		}

		if (val.indexOf('@') == -1)
		{
			return val;
		}

		for (Attr attr : en.getEnMap().getAttrs())
		{
			val = val.replace("@" + attr.getKey(), en.GetValStringByKey(attr.getKey()));
		}
		return val;
	}

	public final String getFK_Event() throws Exception
	{
		return this.GetValStringByKey(FrmEventAttr.FK_Event);
	}
	public final void setFK_Event(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.FK_Event, value);
	}
	/** 
	 执行类型
	*/
	public final EventDoType getHisDoType() throws Exception {
		return EventDoType.forValue(this.GetValIntByKey(FrmEventAttr.EventDoType));
	}
	public final void setHisDoType(EventDoType value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.EventDoType, value.getValue());
	}
	public final int getHisDoTypeInt() throws Exception
	{
		return this.GetValIntByKey(FrmEventAttr.EventDoType);
	}
	public final void setHisDoTypeInt(int value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.EventDoType, value);
	}

		///#endregion


		///#region 事件消息.
	/** 
	 消息控制类型.
	*/
	public final MsgCtrl getMsgCtrl() throws Exception {
		return MsgCtrl.forValue(this.GetValIntByKey(FrmEventAttr.MsgCtrl));
	}
	public final void setMsgCtrl(MsgCtrl value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.MsgCtrl, value.getValue());
	}
	/** 
	 是否手机推送？
	*/
	public final boolean getMobilePushEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmEventAttr.MobilePushEnable);
	}
	public final void setMobilePushEnable(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.MobilePushEnable, value);
	}
	public final boolean getMailEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmEventAttr.MailEnable);
	}
	public final void setMailEnable(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.MailEnable, value);
	}
	/** 
	 邮件标题
	*/
	public final String getMailTitle() throws Exception {
		String str = this.GetValStrByKey(FrmEventAttr.MailTitle);
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}
		switch (this.getFK_Event())
		{
			case EventListNode.SendSuccess:
				return "新工作@Title,发送人@WebUser.No,@WebUser.Name";
			case EventListNode.ShitAfter:
				return "移交来的新工作@Title,移交人@WebUser.No,@WebUser.Name";
			case EventListNode.ReturnAfter:
				return "被退回来@Title,退回人@WebUser.No,@WebUser.Name";
			case EventListNode.UndoneAfter:
				return "工作被撤销@Title,发送人@WebUser.No,@WebUser.Name";
			case EventListNode.AskerReAfter:
				return "加签新工作@Title,发送人@WebUser.No,@WebUser.Name";
			case EventListFlow.AfterFlowDel:
				return "工作流程被删除@Title,发送人@WebUser.No,@WebUser.Name";
			case EventListFlow.FlowOverAfter:
				return "流程结束@Title,发送人@WebUser.No,@WebUser.Name";
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
	}
	/** 
	 邮件标题
	*/
	public final String getMailTitleReal() throws Exception {
		String str = this.GetValStrByKey(FrmEventAttr.MailTitle);
		return str;
	}
	public final void setMailTitleReal(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.MailTitle, value);
	}
	/** 
	 邮件内容
	*/
	public final String getMailDocReal() throws Exception
	{
		return this.GetValStrByKey(FrmEventAttr.MailDoc);
	}
	public final void setMailDocReal(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.MailDoc, value);
	}
	/** 
	 邮件内容模版
	*/
	public final String getMailDoc() throws Exception {
		String str = this.GetValStrByKey(FrmEventAttr.MailDoc);
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}
		switch (this.getFK_Event())
		{
			case EventListNode.SendSuccess:
				str += "\t\n您好:";
				str += "\t\n    有新工作@Title需要您处理, 点击这里打开工作{Url} .";
				str += "\t\n致! ";
				str += "\t\n    @WebUser.No, @WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListNode.ReturnAfter:
				str += "\t\n您好:";
				str += "\t\n    工作@Title被退回来了, 点击这里打开工作{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListNode.ShitAfter:
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作@Title, 点击这里打开工作{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListNode.UndoneAfter:
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作@Title, 点击这里打开工作{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListNode.AskerReAfter: //加签.
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作@Title, 点击这里打开工作{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListFlow.AfterFlowDel: //流程删除
				str += "\t\n您好:";
				str += "\t\n    被删除的工作@Title.";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			case EventListFlow.FlowOverAfter: //流程结束
				str += "\t\n您好:";
				str += "\t\n    工作@Title已经结束，点击这里查看工作{Url}.";
				str += "\t\n 致! ";
				str += "\t\n    @WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
				break;
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
		return str;
	}
	/** 
	 是否启用短信发送
	*/
	public final boolean getSMSEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmEventAttr.SMSEnable);
	}
	public final void setSMSEnable(boolean value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.SMSEnable, value);
	}
	/** 
	 短信模版内容
	*/
	public final String getSMSDocReal() throws Exception {
		String str = this.GetValStrByKey(FrmEventAttr.SMSDoc);
		return str;
	}
	public final void setSMSDocReal(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.SMSDoc, value);
	}
	/** 
	 短信模版内容
	*/
	public final String getSMSDoc() throws Exception {
		String str = this.GetValStrByKey(FrmEventAttr.SMSDoc);
		if (DataType.IsNullOrEmpty(str) == false)
		{
			return str;
		}

		switch (this.getFK_Event())
		{
			case EventListNode.SendSuccess:
				str = "有新工作@Title需要您处理, 发送人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListNode.ReturnAfter:
				str = "工作@Title被退回,退回人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListNode.ShitAfter:
				str = "移交工作@Title,移交人:@WebUser.No, @WebUser.Name,打开{Url} .";
				break;
			case EventListNode.UndoneAfter:
				str = "工作撤销@Title,撤销人:@WebUser.No, @WebUser.Name,打开{Url}.";
				break;
			case EventListNode.AskerReAfter: //加签.
				str = "工作加签@Title,加签人:@WebUser.No, @WebUser.Name,打开{Url}.";
				break;
			default:
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
		return str;
	}
	public final void setSMSDoc(String value)  throws Exception
	 {
		this.SetValByKey(FrmEventAttr.SMSDoc, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 事件
	*/
	public FrmEvent()  {
	}
	public FrmEvent(String mypk)throws Exception
	{
		this.setMyPK(mypk);
		this.RetrieveFromDBSources();
	}
	public FrmEvent(String fk_mapdata, String fk_Event) throws Exception {
		this.setFK_Event(fk_Event);
		this.setFK_MapData(fk_mapdata);
		this.setMyPK(this.getFK_MapData() + "_" + this.getFK_Event());
		this.RetrieveFromDBSources();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FrmEvent", "外部自定义事件(表单,从表,流程,节点)");

		map.IndexField = FrmEventAttr.FK_MapData;
		map.AddMyPK();

			//0=表单事件,1=流程，2=节点事件.
		map.AddTBInt(FrmEventAttr.EventSource, 0, "事件类型", true, true);
		map.AddTBString(FrmEventAttr.FK_Event, null, "事件标记", true, true, 0, 400, 10);
		map.AddTBString(FrmEventAttr.RefFlowNo, null, "关联的流程编号", true, true, 0, 10, 10);

			//事件类型的主键.
		map.AddTBString(FrmEventAttr.FK_MapData, null, "表单ID(包含Dtl表)", true, true, 0, 100, 10);
		map.AddTBString(FrmEventAttr.FK_Flow, null, "流程编号", true, true, 0, 100, 10);
		map.AddTBInt(FrmEventAttr.FK_Node, 0, "节点ID", true, true);

			//执行内容. EventDoType 0=SQL,1=URL.... 
		map.AddTBInt(FrmEventAttr.EventDoType, 0, "事件执行类型", true, true);
		map.AddTBString(FrmEventAttr.FK_DBSrc, "local", "数据源", true, false, 0, 100, 20);
		map.AddTBString(FrmEventAttr.DoDoc, null, "执行内容", true, true, 0, 400, 10);
		map.AddTBString(FrmEventAttr.MsgOK, null, "成功执行提示", true, true, 0, 400, 10);
		map.AddTBString(FrmEventAttr.MsgError, null, "异常信息提示", true, true, 0, 400, 10);


			///#region 消息设置. 如下属性放入了节点参数信息了.
		map.AddDDLSysEnum(FrmEventAttr.MsgCtrl, 0, "消息发送控制", true, true, FrmEventAttr.MsgCtrl, "@0=不发送@1=按设置的下一步接受人自动发送（默认）@2=由本节点表单系统字段(IsSendEmail,IsSendSMS)来决定@3=由SDK开发者参数(IsSendEmail,IsSendSMS)来决定", true);

		map.AddBoolean(FrmEventAttr.MailEnable, true, "是否启用邮件发送？(如果启用就要设置邮件模版，支持ccflow表达式。)", true, true, true);
		map.AddTBString(FrmEventAttr.MailTitle, null, "邮件标题模版", true, false, 0, 200, 20, true);
		map.AddTBStringDoc(FrmEventAttr.MailDoc, null, "邮件内容模版", true, false, true);

			//是否启用手机短信？
		map.AddBoolean(FrmEventAttr.SMSEnable, false, "是否启用短信发送？(如果启用就要设置短信模版，支持ccflow表达式。)", true, true, true);
		map.AddTBStringDoc(FrmEventAttr.SMSDoc, null, "短信内容模版", true, false, true);
		map.AddBoolean(FrmEventAttr.MobilePushEnable, true, "是否推送到手机、pad端。", true, true, true);

			///#endregion 消息设置.

			//参数属性
		map.AddTBAtParas(4000);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		//设置关联的FlowNo编号,以方便流程删除与模版导入导出.
		if (DataType.IsNullOrEmpty(this.getFK_Flow()) == false)
		{
			this.setRefFlowNo(this.getFK_Flow());
		}

		if (this.getFK_Node() != 0)
		{
			this.setRefFlowNo(DBAccess.RunSQLReturnString("SELECT FK_Flow FROM WF_Node WHERE NodeID=" + this.getFK_Node()));
		}

		if (this.getFK_MapData().startsWith("ND") == true)
		{
			int nodeid = Integer.parseInt(this.getFK_MapData().replace("ND", ""));
			this.setRefFlowNo(DBAccess.RunSQLReturnString("SELECT FK_Flow FROM WF_Node WHERE NodeID=" + nodeid));
		}

		return super.beforeUpdateInsertAction();
	}

	@Override
	protected boolean beforeInsert() throws Exception {
		//在属性实体集合插入前，clear父实体的缓存.
		if (DataType.IsNullOrEmpty(this.getFK_MapData()) == false)
		{
			bp.sys.base.Glo.ClearMapDataAutoNum(this.getFK_MapData());
		}


		return super.beforeInsert();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception {
		super.afterInsertUpdateAction();
	}
	@Override
	protected void afterDelete() throws Exception {
		super.afterDelete();
	}
}