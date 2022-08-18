package bp.wf;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import bp.port.*;
import bp.*;
import java.util.*;

/** 
 抄送
*/
public class CCList extends EntityMyPK
{

		///#region 属性
	/** 
	 状态
	*/
	public final CCSta getHisSta() throws Exception {
		return CCSta.forValue(this.GetValIntByKey(CCListAttr.Sta));
	}
	public final void setHisSta(CCSta value)throws Exception
	{//@sly 这里去掉了业务逻辑.
		if (value == CCSta.Read)
		{
			this.setReadDT(DataType.getCurrentDateTime());
		}
		this.SetValByKey(CCListAttr.Sta, value.getValue());
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {

		UAC uac = new UAC();
		if (!bp.web.WebUser.getNo().equals("admin"))
		{
			uac.IsView = false;
			return uac;
		}
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}
	/** 
	 域
	*/
	public final String getDomain() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.Domain);
	}
	public final void setDomain(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.Domain, value);
	}
	/** 
	 抄送给
	*/
	public final String getCCTo() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.CCTo);
	}
	public final void setCCTo(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.CCTo, value);
	}
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.OrgNo);
	}
	public final void setOrgNo(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.OrgNo, value);
	}
	/** 
	 抄送给Name
	*/
	public final String getCCToName() throws Exception {
		String s = this.GetValStringByKey(CCListAttr.CCToName);
		if (DataType.IsNullOrEmpty(s))
		{
			s = this.getCCTo();
		}
		return s;
	}
	public final void setCCToName(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.CCToName, value);
	}
	/** 
	 读取时间
	*/
	public final String getCDT() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.CDT);
	}
	public final void setCDT(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.CDT, value);
	}
	/** 
	 抄送人所在的节点编号
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(CCListAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.FK_Node, value);
	}

	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(CCListAttr.WorkID);
	}
	public final void setWorkID(long value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.WorkID, value);
	}
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(CCListAttr.FID);
	}
	public final void setFID(long value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.FID, value);
	}
	/** 
	 父流程工作ID
	*/
	public final long getPWorkID() throws Exception
	{
		return this.GetValInt64ByKey(CCListAttr.PWorkID);
	}
	public final void setPWorkID(long value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.PWorkID, value);
	}
	/** 
	 父流程编号
	*/
	public final String getPFlowNo() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.PFlowNo, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_FlowT() throws Exception
	{
		return this.GetValRefTextByKey(CCListAttr.FK_Flow);
	}
	public final String getFlowName() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.FlowName);
	}
	public final void setFlowName(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.FlowName, value);
	}
	public final String getNodeName() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.NodeName);
	}
	public final void setNodeName(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.NodeName, value);
	}
	/** 
	 抄送标题
	*/
	public final String getTitle() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.Title);
	}
	public final void setTitle(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.Title, value);
	}
	/** 
	 抄送内容
	*/
	public final String getDoc() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.Doc);
	}
	public final void setDoc(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.Doc, value);
	}
	public final String getDocHtml() throws Exception
	{
		return this.GetValHtmlStringByKey(CCListAttr.Doc);
	}
	/** 
	 抄送对象
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.FK_Flow, value);
	}
	public final String getRec() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.Rec);
	}
	public final void setRec(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.Rec, value);
	}
	/** 
	 读取日期
	*/
	public final String getReadDT() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.ReadDT);
	}
	public final void setReadDT(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.ReadDT, value);
	}
	/** 
	 写入日期
	*/
	public final String getRDT() throws Exception
	{
		return this.GetValStringByKey(CCListAttr.RDT);
	}
	public final void setRDT(String value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.RDT, value);
	}
	/** 
	 是否加入待办列表
	*/
	public final boolean getInEmpWorks() throws Exception
	{
		return this.GetValBooleanByKey(CCListAttr.InEmpWorks);
	}
	public final void setInEmpWorks(boolean value)  throws Exception
	 {
		this.SetValByKey(CCListAttr.InEmpWorks, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 CCList
	*/
	public CCList()  {
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
		Map map = new Map("WF_CCList", "抄送列表");

		map.AddMyPK(true); //组合主键 WorkID+"_"+FK_Node+"_"+FK_Emp

		map.AddTBString(CCListAttr.Title, null, "标题", true, true, 0, 500, 10, true);
		map.AddTBStringDoc();

			//状态  @0=抄送@1=已读@2=已回复@3=已删除
		map.AddTBInt(CCListAttr.Sta, 0, "状态", true, true);

		map.AddTBString(CCListAttr.FK_Flow, null, "流程编号", true, true, 0, 5, 10, true);
		map.AddTBString(CCListAttr.FlowName, null, "名称", true, true, 0, 200, 10, true);
		map.AddTBInt(CCListAttr.FK_Node, 0, "节点", true, true);
		map.AddTBString(CCListAttr.NodeName, null, "节点名称", true, true, 0, 500, 10, true);

		map.AddTBInt(CCListAttr.WorkID, 0, "工作ID", true, true);
		map.AddTBInt(CCListAttr.FID, 0, "FID", true, true);

		map.AddTBString(CCListAttr.Rec, null, "抄送人员", true, true, 0, 50, 10, true);
		map.AddTBDateTime(CCListAttr.RDT, null, "抄送日期", true, false);

		map.AddTBString(CCListAttr.CCTo, null, "抄送给", true, false, 0, 50, 10, true);
		map.AddTBString(CCListAttr.CCToName, null, "抄送给(人员名称)", true, false, 0, 50, 10, true);

			//map.AddTBString(CCListAttr.CCToDept, null, "抄送到部门", true, false, 0, 50, 10, true);
			//map.AddTBString(CCListAttr.CCToDeptName, null, "抄送给部门名称", true, false, 0, 600, 10, true);

		map.AddTBString(CCListAttr.OrgNo, null, "组织", true, false, 0, 50, 10, true);

		map.AddTBDateTime(CCListAttr.CDT, null, "打开时间", true, false);
		map.AddTBDateTime(CCListAttr.ReadDT, null, "阅读时间", true, false);


		map.AddTBString(CCListAttr.PFlowNo, null, "父流程编号", true, true, 0, 100, 10, true);
		map.AddTBInt(CCListAttr.PWorkID, 0, "父流程WorkID", true, true);
			//added by liuxc,2015.7.6，标识是否在待办列表里显示
		map.AddBoolean(CCListAttr.InEmpWorks, false, "是否加入待办列表", true, true);

			//add by zhoupeng  
		map.AddTBString(CCListAttr.Domain, null, "Domain", true, true, 0, 50, 10, true);
		map.AddTBString(CCListAttr.OrgNo, null, "OrgNo", true, true, 0, 50, 10, true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception {
		this.setOrgNo(bp.web.WebUser.getOrgNo());
		return super.beforeInsert();
	}
}