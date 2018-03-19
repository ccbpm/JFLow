package cn.jflow.common.model;

import java.io.Serializable;

public class TempObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer FK_Node; 
	private Long WorkID;
	private Long FID;
	private String FK_Flow;
	private String FK_Dept;
	private String Info;
	private String ToEmp;
	private String AskFor;
	
    private String WorkIDs;
	private String CFlowNo;
	private String DoFunc;
	
	private String FK_MapData;
	private Integer RowNum;
	private Integer ListNum;
	private String FormHtml;
	
	private String Sta;
	private String PageID;
	private String PageSmall;
	private String GroupBy; 
	
	private String TB_Emp;
	private String TB_Note;
	
	private String TB_SkipToEmp;
	private String DDL_SkipToNode;
	
	private String Ddl_Value;
	private String Ens_Name;
	private String Pk_Val;
	private String Attr_Key1;
	private String Attr_Key2;
	private String Ddl_type;
	
	
	private String loginName;
	private String loginPass;
	
	public Integer getFK_Node() {
		return FK_Node;
	}
	public void setFK_Node(Integer fK_Node) {
		FK_Node = fK_Node;
	}
	public Long getWorkID() {
		return WorkID;
	}
	public void setWorkID(Long workID) {
		WorkID = workID;
	}
	public Long getFID() {
		return FID;
	}
	public void setFID(Long fID) {
		FID = fID;
	}
	public String getFK_Flow() {
		return FK_Flow;
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
	public String getFK_Dept() {
		return FK_Dept;
	}
	public void setFK_Dept(String fK_Dept) {
		FK_Dept = fK_Dept;
	}
	public String getInfo() {
		return Info;
	}
	public void setInfo(String info) {
		Info = info;
	}
	public String getToEmp() {
		return ToEmp;
	}
	public void setToEmp(String toEmp) {
		ToEmp = toEmp;
	}
	public String getAskFor() {
		return AskFor;
	}
	public void setAskFor(String askFor) {
		AskFor = askFor;
	}
	public String getWorkIDs() {
		return WorkIDs;
	}
	public void setWorkIDs(String workIDs) {
		WorkIDs = workIDs;
	}
	public String getCFlowNo() {
		return CFlowNo;
	}
	public void setCFlowNo(String cFlowNo) {
		CFlowNo = cFlowNo;
	}
	public String getDoFunc() {
		return DoFunc;
	}
	public void setDoFunc(String doFunc) {
		DoFunc = doFunc;
	}
	public String getFK_MapData() {
		return FK_MapData;
	}
	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
	}
	public Integer getRowNum() {
		return RowNum;
	}
	public void setRowNum(Integer rowNum) {
		RowNum = rowNum;
	}
	public Integer getListNum() {
		return ListNum;
	}
	public void setListNum(Integer listNum) {
		ListNum = listNum;
	}
	public String getFormHtml() {
		return FormHtml;
	}
	public void setFormHtml(String formHtml) {
		FormHtml = formHtml;
	}
	public String getSta() {
		return Sta;
	}
	public void setSta(String sta) {
		Sta = sta;
	}
	public String getPageID() {
		return PageID;
	}
	public void setPageID(String pageID) {
		PageID = pageID;
	}
	public String getPageSmall() {
		return PageSmall;
	}
	public void setPageSmall(String pageSmall) {
		PageSmall = pageSmall;
	}
	public String getGroupBy() {
		return GroupBy;
	}
	public void setGroupBy(String groupBy) {
		GroupBy = groupBy;
	}
	public String RefOID;

	public String getRefOID() {
		return RefOID;
	}
	public void setRefOID(String refOID) {
		RefOID = refOID;
	}
	public String btnName;

	public String getBtnName() {
		return btnName;
	}
	public void setBtnName(String btnName) {
		this.btnName = btnName;
	}
	public String KeyOfEn;

	public String getKeyOfEn() {
		return KeyOfEn;
	}
	public void setKeyOfEn(String keyOfEn) {
		KeyOfEn = keyOfEn;
	}
	
	public String GroupField;

	public String getGroupField() {
		return GroupField;
	}
	public void setGroupField(String groupField) {
		GroupField = groupField;
	}
	public int FType;

	public int getFType() {
		return FType;
	}
	public void setFType(int fType) {
		FType = fType;
	}
	public String IDX;

	public String getIDX() {
		return IDX;
	}
	public void setIDX(String iDX) {
		IDX = iDX;
	}
	
	public String getTB_Emp() {
		return TB_Emp;
	}
	public void setTB_Emp(String tB_Emp) {
		TB_Emp = tB_Emp;
	}
	public String getTB_Note() {
		return TB_Note;
	}
	public void setTB_Note(String tB_Note) {
		TB_Note = tB_Note;
	}
	public String getTB_SkipToEmp() {
		return TB_SkipToEmp;
	}
	public void setTB_SkipToEmp(String tB_SkipToEmp) {
		TB_SkipToEmp = tB_SkipToEmp;
	}
	public String getDDL_SkipToNode() {
		return DDL_SkipToNode;
	}
	public void setDDL_SkipToNode(String dDL_SkipToNode) {
		DDL_SkipToNode = dDL_SkipToNode;
	}
	public String getDdl_Value() {
		return Ddl_Value;
	}
	public void setDdl_Value(String ddl_Value) {
		Ddl_Value = ddl_Value;
	}
	public String getEns_Name() {
		return Ens_Name;
	}
	public void setEns_Name(String ens_Name) {
		Ens_Name = ens_Name;
	}
	public String getPk_Val() {
		return Pk_Val;
	}
	public void setPk_Val(String pk_Val) {
		Pk_Val = pk_Val;
	}
	public String getAttr_Key1() {
		return Attr_Key1;
	}
	public void setAttr_Key1(String attr_Key1) {
		Attr_Key1 = attr_Key1;
	}
	public String getAttr_Key2() {
		return Attr_Key2;
	}
	public void setAttr_Key2(String attr_Key2) {
		Attr_Key2 = attr_Key2;
	}
	public String getDdl_type() {
		return Ddl_type;
	}
	public void setDdl_type(String ddl_type) {
		Ddl_type = ddl_type;
	}
	public String EleType;
	public String DoType;

	public String getEleType() {
		return EleType;
	}
	public void setEleType(String eleType) {
		EleType = eleType;
	}
	public String getDoType() {
		return DoType;
	}
	public void setDoType(String doType) {
		DoType = doType;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPass() {
		return loginPass;
	}
	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}
}
