package BP.WF;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Depositary;
import BP.DA.Log;
import BP.DA.Paras;
import BP.DA.XmlWriteMode;
import BP.En.Attr;
import BP.En.EditType;
import BP.En.Entity;
import BP.En.FieldTypeS;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.UAC;
import BP.En.UIContralType;
import BP.Port.Emp;
import BP.Sys.AttachmentUploadType;
import BP.Sys.EventListOfNode;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBs;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmEle;
import BP.Sys.FrmEles;
import BP.Sys.FrmEvent;
import BP.Sys.FrmEvents;
import BP.Sys.FrmImg;
import BP.Sys.FrmImgAths;
import BP.Sys.FrmImgs;
import BP.Sys.FrmLab;
import BP.Sys.FrmLabs;
import BP.Sys.FrmLine;
import BP.Sys.FrmLines;
import BP.Sys.FrmLink;
import BP.Sys.FrmLinks;
import BP.Sys.FrmRB;
import BP.Sys.FrmRBs;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.GEEntity;
import BP.Sys.Glo;
import BP.Sys.GroupField;
import BP.Sys.GroupFieldAttr;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Sys.MapDtl;
import BP.Sys.MapDtlAttr;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.MapFrame;
import BP.Sys.MapFrames;
import BP.Sys.OSModel;
import BP.Sys.PubClass;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnumMainAttr;
import BP.Sys.SysEnumMains;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.DateUtils;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.WF.Data.GERpt;
import BP.WF.Data.GERptAttr;
import BP.WF.Data.NDXRptBaseAttr;
import BP.WF.Template.BillTemplate;
import BP.WF.Template.BillTemplates;
import BP.WF.Template.CC;
import BP.WF.Template.CCDept;
import BP.WF.Template.CCDeptAttr;
import BP.WF.Template.CCDepts;
import BP.WF.Template.CCEmp;
import BP.WF.Template.CCEmpAttr;
import BP.WF.Template.CCEmps;
import BP.WF.Template.CCStation;
import BP.WF.Template.Cond;
import BP.WF.Template.CondModel;
import BP.WF.Template.CondType;
import BP.WF.Template.Conds;
import BP.WF.Template.ConnDataFrom;
import BP.WF.Template.DTSField;
import BP.WF.Template.DataStoreModel;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.Directions;
import BP.WF.Template.DraftRole;
import BP.WF.Template.FindWorkerRole;
import BP.WF.Template.FlowAttr;
import BP.WF.Template.FlowDTSTime;
import BP.WF.Template.FlowDTSWay;
import BP.WF.Template.FlowDeptDataRightCtrlType;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSorts;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.FrmWorkCheck;
import BP.WF.Template.LabNote;
import BP.WF.Template.LabNotes;
import BP.WF.Template.MapFrmFool;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeDept;
import BP.WF.Template.NodeDeptAttr;
import BP.WF.Template.NodeDepts;
import BP.WF.Template.NodeEmp;
import BP.WF.Template.NodeEmpAttr;
import BP.WF.Template.NodeEmps;
import BP.WF.Template.NodeExt;
import BP.WF.Template.NodeExts;
import BP.WF.Template.NodeReturn;
import BP.WF.Template.NodeReturnAttr;
import BP.WF.Template.NodeReturns;
import BP.WF.Template.NodeStation;
import BP.WF.Template.NodeStationAttr;
import BP.WF.Template.NodeStations;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;
import BP.WF.Template.NodeYGFlow;
import BP.WF.Template.NodeYGFlows;
import BP.WF.Template.Selector;
import BP.WF.Template.SelectorModel;
import BP.WF.Template.Selectors;
import BP.WF.Template.StartGuideWay;
import BP.WF.Template.TimelineRole;
import BP.WF.Template.TurnTo;
import BP.Web.GuestUser;
import BP.Web.WebUser;
import BP.Tools.ContextHolderUtils;

/**
 * 娴佺▼ 璁板綍浜嗘祦绋嬬殑淇℃伅锛� 娴佺▼鐨勭紪鍙凤紝鍚嶇О锛屽缓绔嬫椂闂达紟
 */
public class Flow extends BP.En.EntityNoName {

	/// <summary>
	/// (褰撳墠鑺傜偣涓哄瓙娴佺▼鏃�)鏄惁妫�鏌ユ墍鏈夊瓙娴佺▼瀹屾垚鍚庣埗娴佺▼鑷姩鍙戦��
	/// </summary>
	public SubFlowOver SubFlowOver() {
		return SubFlowOver.forValue(this.GetValIntByKey(FlowAttr.IsAutoSendSubFlowOver));
	}

	/**
	 * 鏈�澶у�紉
	 */
	public final int getMaxX() {
		int i = this.GetParaInt("MaxX");
		if (i == 0) {
			this.GenerMaxXY();
		} else {
			return i;
		}
		return this.GetParaInt("MaxX");
	}

	public final void setMaxX(int value) {
		this.SetPara("MaxX", value);
	}

	/**
	 * 鏈�澶у�糦
	 */
	public final int getMaxY() {
		int i = this.GetParaInt("MaxY");
		if (i == 0) {
			this.GenerMaxXY();
		} else {
			return i;
		}

		return this.GetParaInt("MaxY");
	}

	public final void setMaxY(int value) {
		this.SetPara("MaxY", value);
	}

	private void GenerMaxXY() {
		// int x1 = DBAccess.RunSQLReturnValInt("SELECT MAX(X) FROM WF_Node
		// WHERE FK_Flow='" + this.getNo() + "'", 0);
		// int x2 = DBAccess.RunSQLReturnValInt("SELECT MAX(X) FROM
		// WF_NodeLabelNode WHERE FK_Flow='" + this.getNo() + "'", 0);
		// this.MaxY = DBAccess.RunSQLReturnValInt("SELECT MAX(Y) FROM WF_Node
		// WHERE FK_Flow='" + this.getNo() + "'", 0);
	}

	/// #endregion 鍙傛暟灞炴��.
	/// #region 涓氬姟鏁版嵁琛ㄥ悓姝ュ睘鎬�.
	/**
	 * 鍚屾鏂瑰紡
	 */
	public final FlowDTSWay getDTSWay() {
		return FlowDTSWay.forValue(this.GetValIntByKey(FlowAttr.DTSWay));
	}

	public final void setDTSWay(FlowDTSWay value) {
		this.SetValByKey(FlowAttr.DTSWay, value.getValue());
	}

	public final FlowDTSTime getDTSTime() {
		return FlowDTSTime.forValue(this.GetValIntByKey(FlowAttr.DTSTime));
	}

	public final void setDTSTime(FlowDTSTime value) {
		this.SetValByKey(FlowAttr.DTSTime, value.getValue());
	}

	public final DTSField getDTSField() {
		return DTSField.forValue(this.GetValIntByKey(FlowAttr.DTSField));
	}

	public final void setDTSField(DTSField value) {
		this.SetValByKey(FlowAttr.DTSField, value.getValue());
	}

	/**
	 * 鏁版嵁婧�
	 * 
	 */
	public final String getDTSDBSrc() {
		String str = this.GetValStringByKey(FlowAttr.DTSDBSrc);
		if (StringHelper.isNullOrEmpty(str)) {
			return "local";
		}
		return str;
	}

	public final void setDTSDBSrc(String value) {
		this.SetValByKey(FlowAttr.DTSDBSrc, value);
	}

	/**
	 * 涓氬姟琛�
	 * 
	 */
	public final String getDTSBTable() {
		return this.GetValStringByKey(FlowAttr.DTSBTable);
	}

	public final void setDTSBTable(String value) {
		this.SetValByKey(FlowAttr.DTSBTable, value);
	}

	public final String getDTSBTablePK() {
		return this.GetValStringByKey(FlowAttr.DTSBTablePK);
	}

	public final void setDTSBTablePK(String value) {
		this.SetValByKey(FlowAttr.DTSBTablePK, value);
	}

	/**
	 * 瑕佸悓姝ョ殑鑺傜偣s
	 * 
	 */
	public final String getDTSSpecNodes() {
		return this.GetValStringByKey(FlowAttr.DTSSpecNodes);
	}

	public final void setDTSSpecNodes(String value) {
		this.SetValByKey(FlowAttr.DTSSpecNodes, value);
	}

	/**
	 * 鍚屾鐨勫瓧娈靛搴斿叧绯�.
	 * 
	 */
	public final String getDTSFields() {
		return this.GetValStringByKey(FlowAttr.DTSFields);
	}

	public final void setDTSFields(String value) {
		this.SetValByKey(FlowAttr.DTSFields, value);
	}

	/***************************** H5娴佺▼鍒楄〃浣跨敤锛屼笉鍋氬叾浠栫敤澶� *********************************************/
	public final int getSta0() {
		return this.GetValIntByKey(FlowAttr.Sta0);
	}

	public final void setSta0(String value) {
		this.SetValByKey(FlowAttr.Sta0, value);
	}

	public final int getSta1() {
		return this.GetValIntByKey(FlowAttr.Sta1);
	}

	public final void setSta1(String value) {
		this.SetValByKey(FlowAttr.Sta1, value);
	}

	public final int getSta2() {
		return this.GetValIntByKey(FlowAttr.Sta2);
	}

	public final void setSta2(String value) {
		this.SetValByKey(FlowAttr.Sta2, value);
	}

	/**************************************************************************/
	/**
	 * 璁捐绫诲瀷
	 */
	public final int getDType() {
		return this.GetValIntByKey(FlowAttr.DType);
	}

	public final void setDType(int value) {
		this.SetValByKey(FlowAttr.DType, value);
	}

	/**
	 * 娴佺▼浜嬩欢瀹炰綋
	 */
	public final String getFlowEventEntity() {
		return this.GetValStringByKey(FlowAttr.FlowEventEntity);
	}

	public final void setFlowEventEntity(String value) {
		this.SetValByKey(FlowAttr.FlowEventEntity, value);
	}

	/**
	 * 娴佺▼鏍囪
	 * 
	 */
	public final String getFlowMark() {
		String str = this.GetValStringByKey(FlowAttr.FlowMark);
		if (str.equals("")) {
			return this.getNo();
		}
		return str;
	}

	public final void setFlowMark(String value) {
		this.SetValByKey(FlowAttr.FlowMark, value);
	}

	/**
	 * 鑺傜偣鍥惧舰绫诲瀷
	 * 
	 */
	public final int getChartType() {
		return this.GetValIntByKey(FlowAttr.ChartType);
	}

	public final void setChartType(int value) {
		this.SetValByKey(FlowAttr.ChartType, value);
	}

	/**
	 * 鍙戣捣闄愬埗.
	 * 
	 */
	public final StartLimitRole getStartLimitRole() {
		return StartLimitRole.forValue(this.GetValIntByKey(FlowAttr.StartLimitRole));
	}

	public final void setStartLimitRole(StartLimitRole value) {
		this.SetValByKey(FlowAttr.StartLimitRole, value.getValue());
	}

	///// <summary>
	///// 鍙戣捣闄愬埗鏂囨湰
	///// </summary>
	// public string StartLimitRoleText
	// {
	// get
	// {
	// return this.GetValRefTextByKey(FlowAttr.StartLimitRole);
	// }
	// }
	/**
	 * 鍙戣捣鍐呭
	 */
	public final String getStartLimitPara() {
		return this.GetValStringByKey(FlowAttr.StartLimitPara);
	}

	public final void setStartLimitPara(String value) {
		this.SetValByKey(FlowAttr.StartLimitPara, value);
	}

	public final String getStartLimitAlert() {
		String s = this.GetValStringByKey(FlowAttr.StartLimitAlert);
		if (s.equals("")) {
			return "鎮ㄥ凡缁忓惎鍔ㄨ繃璇ユ祦绋嬶紝涓嶈兘閲嶅鍚姩銆�";
		}
		return s;
	}

	public final void setStartLimitAlert(String value) {
		this.SetValByKey(FlowAttr.StartLimitAlert, value);
	}

	/**
	 * 闄愬埗瑙﹀彂鏃堕棿
	 */
	public final StartLimitWhen getStartLimitWhen() {
		return StartLimitWhen.forValue(this.GetValIntByKey(FlowAttr.StartLimitWhen));
	}

	public final void setStartLimitWhen(StartLimitWhen value) {
		this.SetValByKey(FlowAttr.StartLimitWhen, value.getValue());
	}

	/**
	 * 鍙戣捣瀵艰埅鏂瑰紡
	 * 
	 */
	public final StartGuideWay getStartGuideWay() {
		return StartGuideWay.forValue(this.GetValIntByKey(FlowAttr.StartGuideWay));
	}

	public final void setStartGuideWay(StartGuideWay value) {
		this.SetValByKey(FlowAttr.StartGuideWay, value.getValue());
	}

	/**
	 * 鍙充晶鐨勮秴閾炬帴
	 * 
	 */
	public final String getStartGuideLink() {
		return this.GetValStringByKey(FlowAttr.StartGuideLink);
	}

	public final void setStartGuideLink(String value) {
		this.SetValByKey(FlowAttr.StartGuideLink, value);
	}

	/**
	 * 鏍囩
	 * 
	 */
	public final String getStartGuideLab() {
		return this.GetValStringByKey(FlowAttr.StartGuideLab);
	}

	public final void setStartGuideLab(String value) {
		this.SetValByKey(FlowAttr.StartGuideLab, value);
	}

	/**
	 * 鍓嶇疆瀵艰埅鍙傛暟1
	 */
	public final String getStartGuidePara1() {
		String str = this.GetValStringByKey(FlowAttr.StartGuidePara1);
		return str.replace("~", "'");
	}

	public final void setStartGuidePara1(String value) {
		this.SetValByKey(FlowAttr.StartGuidePara1, value);
	}

	/**
	 * 娴佺▼鍙戣捣鍙傛暟2
	 */
	public final String getStartGuidePara2() {
		String str = this.GetValStringByKey(FlowAttr.StartGuidePara2);
		str = str.replace("~", "'");
		if (StringHelper.isNullOrEmpty(str)) {
			if (this.getStartGuideWay() == BP.WF.Template.StartGuideWay.ByHistoryUrl) {
			}
		}
		return str;
	}

	public final void setStartGuidePara2(String value) {
		this.SetValByKey(FlowAttr.StartGuidePara2, value);
	}

	/**
	 * 娴佺▼鍙戣捣鍙傛暟3
	 */
	public final String getStartGuidePara3() {
		return this.GetValStrByKey(FlowAttr.StartGuidePara3);
	}

	public final void setStartGuidePara3(String value) {
		this.SetValByKey(FlowAttr.StartGuidePara3, value);
	}

	/**
	 * 鏄惁鍚敤鏁版嵁閲嶇疆鎸夐挳锛�
	 */
	public final boolean getIsResetData() {
		return this.GetValBooleanByKey(FlowAttr.IsResetData);
	}

	/**
	 * 鏄惁鍚敤瀵煎叆鍘嗗彶鏁版嵁鎸夐挳?
	 */
	public final boolean getIsImpHistory() {
		return this.GetValBooleanByKey(FlowAttr.IsImpHistory);
	}

	/**
	 * 鏄惁鑷姩瑁呰浇涓婁竴绗旀暟鎹�?
	 */
	public final boolean getIsLoadPriData() {
		return this.GetValBooleanByKey(FlowAttr.IsLoadPriData);
	}

	/**
	 * 娴佺▼鍒犻櫎瑙勫垯
	 */

	public final int getFlowDeleteRole() {
		return this.GetValIntByKey(FlowAttr.FlowDeleteRole);
	}

	/**
	 * 鑽夌瑙勫垯
	 */
	public final DraftRole getDraftRole() {
		// return (DraftRole) this.GetValIntByKey(FlowAttr.Draft);
		return DraftRole.forValue(this.GetValIntByKey(FlowAttr.Draft));
	}

	public final void setDraftRole(DraftRole value) {
		this.SetValByKey(FlowAttr.Draft, value.getValue());
	}

	public String Tag = null;

	/**
	 * 杩愯绫诲瀷
	 */
	public final FlowRunWay getHisFlowRunWay() {
		// return (FlowRunWay) this.GetValIntByKey(FlowAttr.FlowRunWay);
		return FlowRunWay.forValue(this.GetValIntByKey(FlowAttr.FlowRunWay));
	}

	public final void setHisFlowRunWay(FlowRunWay value) {
		this.SetValByKey(FlowAttr.FlowRunWay, value.getValue());
	}

	/**
	 * 杩愯瀵硅薄
	 */
	public final String getRunObj() {
		return this.GetValStrByKey(FlowAttr.RunObj);
	}

	public final void setRunObj(String value) {
		this.SetValByKey(FlowAttr.RunObj, value);
	}

	/**
	 * 鏃堕棿鐐硅鍒�
	 */
	public final TimelineRole getHisTimelineRole() {
		return TimelineRole.forValue(this.GetValIntByKey(FlowAttr.TimelineRole));
	}

	/**
	 * 娴佺▼閮ㄩ棬鏁版嵁鏌ヨ鏉冮檺鎺у埗鏂瑰紡
	 */
	public final FlowDeptDataRightCtrlType getHisFlowDeptDataRightCtrlType() {
		return FlowDeptDataRightCtrlType.forValue(this.GetValIntByKey(FlowAttr.DRCtrlType));
	}

	public final void setHisFlowDeptDataRightCtrlType(FlowDeptDataRightCtrlType value) {
		this.SetValByKey(FlowAttr.DRCtrlType, value);
	}

	/**
	 * 娴佺▼搴旂敤绫诲瀷
	 */
	public final FlowAppType getFlowAppType() {
		return FlowAppType.forValue(this.GetValIntByKey(FlowAttr.FlowAppType));
	}

	public final void setFlowAppType(FlowAppType value) {
		this.SetValByKey(FlowAttr.FlowAppType, value.getValue());
	}

	/**
	 * 娴佺▼澶囨敞鐨勮〃杈惧紡
	 */
	public final String getFlowNoteExp() {
		return this.GetValStrByKey(FlowAttr.FlowNoteExp);
	}

	public final void setFlowNoteExp(String value) {
		this.SetValByKey(FlowAttr.FlowNoteExp, value);
	}

	/**
	 * 鍒涘缓鏂板伐浣渨eb鏂瑰紡璋冪敤鐨�
	 * 
	 * @return
	 * @throws Exception
	 */
	public final Work NewWork() throws Exception {
		return NewWork(WebUser.getNo());
	}

	/**
	 * 鍒涘缓鏂板伐浣�.web鏂瑰紡璋冪敤鐨�
	 * 
	 * @param empNo
	 *            浜哄憳缂栧彿
	 * @return
	 * @throws Exception
	 */
	public final Work NewWork(String empNo) throws Exception {
		Emp emp = new Emp(empNo);
		return NewWork(emp, null);
	}

	/**
	 * 浜х敓涓�涓紑濮嬭妭鐐圭殑鏂板伐浣�
	 * 
	 * @param emp
	 *            鍙戣捣浜�
	 * @param paras
	 *            鍙傛暟闆嗗悎,濡傛灉鏄疌S璋冪敤锛岃鍙戣捣瀛愭祦绋嬶紝瑕佷粠鍏朵粬table閲宑opy鏁版嵁,灏变笉鑳戒粠request閲岄潰鍙�,鍙互浼犻�掍负null.
	 * @return 杩斿洖鐨刉ork.
	 * @throws Exception
	 */
	public final Work NewWork(Emp emp, java.util.Hashtable paras) throws Exception {
		// 妫�鏌ユ槸鍚﹀彲浠ュ彂璧疯娴佺▼锛�
		if (BP.WF.Glo.CheckIsCanStartFlow_InitStartFlow(this) == false)
			throw new RuntimeException("@鎮ㄨ繚鍙嶄簡璇ユ祦绋嬬殑銆�" + this.getStartLimitRole() + "銆戦檺鍒惰鍒欍��" + this.getStartLimitAlert());

		// 濡傛灉鏄痓s绯荤粺.
		if (paras == null) {
			paras = new java.util.Hashtable();
		}
		if (BP.Sys.SystemConfig.getIsBSsystem() == true) {
			if (BP.Sys.Glo.getRequest() != null) {
				Enumeration enu = BP.Sys.Glo.getRequest().getParameterNames();
				while (enu.hasMoreElements()) {
					// 鍒ゆ柇鏄惁鏈夊唴瀹癸紝hasNext()
					String key = (String) enu.nextElement();
					if (key == "OID" || key == "WorkID" || key == null)
						continue;
					if (paras.containsKey(key))
						paras.remove(key);

					paras.put(key, BP.Sys.Glo.getRequest().getParameter(key));
				}
			}

		}

		// 寮�濮嬭妭鐐�.
		BP.WF.Node nd = new BP.WF.Node(this.getStartNodeID());

		// 浠庤崏绋块噷鐪嬬湅鏄惁鏈夋柊宸ヤ綔锛�
		StartWork wk = (StartWork) nd.getHisWork();
		wk.ResetDefaultVal();

		String dbstr = SystemConfig.getAppCenterDBVarStr();

		Paras ps = new Paras();
		GERpt rpt = this.getHisGERpt();

		// 鏄惁鏂板垱寤虹殑WorkID
		boolean IsNewWorkID = false;
		// 濡傛灉瑕佸惎鐢ㄨ崏绋�,灏卞垱寤轰竴涓柊鐨刉orkID .
		if (this.getDraftRole() != BP.WF.Template.DraftRole.None && nd.getIsStartNode()) {
			IsNewWorkID = true;
		}

		try {
			// 浠庢姤琛ㄩ噷鏌ヨ璇ユ暟鎹槸鍚﹀瓨鍦紵
			if (this.getIsGuestFlow() == true && StringHelper.isNullOrEmpty(GuestUser.getNo()) == false) {
				// 鏄鎴峰弬涓庣殑娴佺▼锛屽苟涓斿叿鏈夊鎴风櫥闄嗙殑淇℃伅銆�
				ps.SQL = "SELECT OID,FlowEndNode FROM " + this.getPTable() + " WHERE GuestNo=" + dbstr
						+ "GuestNo AND WFState=" + dbstr + "WFState ";
				ps.Add(GERptAttr.GuestNo, GuestUser.getNo());
				ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() > 0 && IsNewWorkID == false) {
					wk.setOID(Long.parseLong(dt.Rows.get(0).getValue(0).toString()));
					int nodeID = Integer.parseInt(dt.Rows.get(0).getValue(1).toString());
					if (nodeID != this.getStartNodeID()) {
						String error = "@杩欓噷鍑虹幇浜哹lank鐨勭姸鎬佷笅娴佺▼杩愯鍒板叾瀹冪殑鑺傜偣涓婂幓浜嗙殑鎯呭喌銆�";
						Log.DefaultLogWriteLineError(error);
						throw new RuntimeException(error);
					}
				}
			} else {

				ps.SQL = "SELECT WorkID,FK_Node FROM WF_GenerWorkFlow WHERE WFState=0 AND Starter=" + dbstr
						+ "FlowStarter AND FK_Flow=" + dbstr + "FK_Flow ";
				ps.Add(GERptAttr.FlowStarter, emp.getNo());
				ps.Add(GenerWorkFlowAttr.FK_Flow, this.getNo());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);

				// 濡傛灉娌℃湁鍚敤鑽夌锛屽苟涓斿瓨鍦ㄨ崏绋垮氨鍙栫涓�鏉� by dgq 5.28
				if (dt.Rows.size() > 0) {
					wk.setOID(Long.parseLong(dt.Rows.get(0).getValue(0).toString()));
					wk.RetrieveFromDBSources();
					int nodeID = Integer.parseInt(dt.Rows.get(0).getValue(1).toString());
					if (nodeID != this.getStartNodeID()) {
						String error = "@杩欓噷鍑虹幇浜哹lank鐨勭姸鎬佷笅娴佺▼杩愯鍒板叾瀹冪殑鑺傜偣涓婂幓浜嗙殑鎯呭喌锛屽綋鍓嶅仠鐣欒妭鐐�:" + nodeID;
						Log.DefaultLogWriteLineError(error);
						// throw new Exception(error);
					}
				}
			}

			// 鍚敤鑽夌鎴栫┖鐧藉氨鍒涘缓WorkID
			if (wk.getOID() == 0) {
				// 璇存槑娌℃湁绌虹櫧,灏卞垱寤轰竴涓┖鐧�..
				wk.ResetDefaultVal();
				wk.setRec(WebUser.getNo());

				wk.SetValByKey(StartWorkAttr.RecText, emp.getName());
				wk.SetValByKey(StartWorkAttr.Emps, emp.getNo());

				wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.getCurrentDataTime());
				wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.getCurrentDataTime());
				wk.SetValByKey(GERptAttr.WFState, WFState.Blank.getValue());

				wk.setOID(DBAccess.GenerOID("WorkID")); // 杩欓噷浜х敓WorkID
														// ,杩欐槸鍞竴浜х敓WorkID鐨勫湴鏂�.

				// 鎶婂敖閲忓彲鑳界殑娴佺▼瀛楁鏀惧叆锛屽惁鍒欎細鍑虹幇鍐叉帀娴佺▼瀛楁灞炴��.
				wk.SetValByKey(GERptAttr.FK_NY, BP.DA.DataType.getCurrentYearMonth());
				wk.SetValByKey(GERptAttr.FK_Dept, emp.getFK_Dept());
				wk.setFID(0);

				try {
					wk.DirectInsert();
				} catch (Exception ex) {
					wk.CheckPhysicsTable();

					// wk.DirectInsert();
				}

				// 璁剧疆鍙傛暟.
				for (Object k : paras.keySet()) {
					rpt.SetValByKey(k.toString(), paras.get(k));
				}

				if (this.getPTable().equals(wk.getEnMap().getPhysicsTable())) {
					// 濡傛灉寮�濮嬭妭鐐硅〃涓庢祦绋嬫姤琛ㄧ浉绛�.
					rpt.setOID(wk.getOID());
					rpt.RetrieveFromDBSources();
					rpt.setFID(0);
					rpt.setFlowStartRDT(BP.DA.DataType.getCurrentDataTime());
					rpt.setMyNum(0);
					rpt.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, wk));
					// WebUser.getNo() + "," + BP.Web.WebUser.Name + "鍦�" +
					// DataType.CurrentDataCNOfShort + "鍙戣捣.";
					rpt.setWFState(WFState.Blank);
					rpt.setFlowStarter(emp.getNo());
					rpt.setFK_NY(DataType.getCurrentYearMonth());
					if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly) {
						rpt.setFlowEmps("@" + emp.getName());
					}

					if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName) {
						rpt.setFlowEmps("@" + emp.getNo());
					}

					if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName) {
						rpt.setFlowEmps("@" + emp.getNo() + "," + emp.getName());
					}

					rpt.setFlowEnderRDT(BP.DA.DataType.getCurrentDataTime());
					rpt.setFK_Dept(emp.getFK_Dept());
					rpt.setFlowEnder(emp.getNo());
					rpt.setFlowEndNode(this.getStartNodeID());
					rpt.setWFState(WFState.Blank);
					rpt.setFID(0);
					rpt.DirectUpdate();
				} else {
					rpt.setOID(wk.getOID());
					rpt.setFID(0);
					rpt.setFlowStartRDT(BP.DA.DataType.getCurrentDataTime());
					rpt.setFlowEnderRDT(BP.DA.DataType.getCurrentDataTime());
					rpt.setMyNum(0);

					rpt.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, wk));
					// rpt.Title = WebUser.getNo() + "," + BP.Web.WebUser.Name +
					// "鍦�" + DataType.CurrentDataCNOfShort + "鍙戣捣.";

					rpt.setWFState(WFState.Blank);
					rpt.setFlowStarter(emp.getNo());

					rpt.setFlowEndNode(this.getStartNodeID());
					if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly) {
						rpt.setFlowEmps("@" + emp.getName());
					}

					if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName) {
						rpt.setFlowEmps("@" + emp.getNo());
					}

					if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName) {
						rpt.setFlowEmps("@" + emp.getNo() + "," + emp.getName());
					}

					rpt.setFK_NY(DataType.getCurrentYearMonth());
					rpt.setFK_Dept(emp.getFK_Dept());
					rpt.setFlowEnder(emp.getNo());
					rpt.InsertAsOID(wk.getOID());
				}

				// 璋冪敤 OnCreateWorkID鐨勬柟娉�. add by zhoupeng 2016.12.4 for LIMS.
				this.DoFlowEventEntity(EventListOfNode.FlowOnCreateWorkID, nd, wk, null, null, null);

			} else {
				rpt.setOID(wk.getOID());
				rpt.RetrieveFromDBSources();

				rpt.setFID(0);
				rpt.setFlowStartRDT(BP.DA.DataType.getCurrentData());
				rpt.setFlowEnderRDT(BP.DA.DataType.getCurrentData());
				rpt.setMyNum(1);
			}
		} catch (RuntimeException ex) {
			wk.CheckPhysicsTable();

			// 妫�鏌ユ姤琛�.
			this.CheckRpt();
			throw new RuntimeException("@鍒涘缓宸ヤ綔澶辫触锛氭湁鍙兘鏄偍鍦ㄨ璁¤〃鍗曟椂鍊欙紝鏂板鍔犵殑鎺т欢锛屾病鏈夐瑙堝鑷寸殑锛岃鎮ㄥ埛鏂颁竴娆″簲璇ュ彲浠ヨВ鍐筹紝鎶�鏈俊鎭細" + ex.getMessage()
					+ " @ 鎶�鏈俊鎭�:" + ex.getStackTrace());
		}

		// 鍦ㄥ垱寤篧orkID鐨勬椂鍊欒皟鐢ㄧ殑浜嬩欢.
		this.DoFlowEventEntity(EventListOfNode.CreateWorkID, nd, wk, null);

		/// #region copy鏁版嵁.
		// 璁板綍杩欎釜id ,涓嶈鍏跺畠鍦ㄥ鍒舵椂闂磋淇敼銆�
		long newOID = wk.getOID();
		if (IsNewWorkID == true) {
			// 澶勭悊浼犻�掕繃鏉ョ殑鍙傛暟銆�
			int i = 0;
			for (Object k : paras.keySet()) {
				i++;
				wk.SetValByKey(k.toString(), paras.get(k).toString());
			}

			if (i >= 3) {
				wk.setOID(newOID);
				wk.DirectUpdate();
			}
		}

		if (paras.containsKey(StartFlowParaNameList.IsDeleteDraft)
				&& paras.get(StartFlowParaNameList.IsDeleteDraft).toString().equals("1")) {
			// 鏄惁瑕佸垹闄raft
			long oid = wk.getOID();
			try {
				// wk.ResetDefaultValAllAttr();
				wk.DirectUpdate();
			} catch (RuntimeException ex) {
				wk.Update();
				BP.DA.Log.DebugWriteError("鍒涘缓鏂板伐浣滈敊璇紝浣嗘槸灞忚斀浜嗗紓甯�,璇锋鏌ラ粯璁ゅ�肩殑闂锛�" + ex.getMessage());
			}

			MapDtls dtls = wk.getHisMapDtls();
			for (MapDtl dtl : dtls.ToJavaList()) {
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + oid);
			}

			// 鍒犻櫎闄勪欢鏁版嵁銆�
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + wk.getNodeID() + "' AND RefPKVal='"
					+ wk.getOID() + "'");
			wk.setOID(newOID);
		}
		/// #region 澶勭悊寮�濮嬭妭鐐�, 濡傛灉浼犻�掕繃鏉� FromTableName 灏辨槸瑕佷粠杩欎釜琛ㄩ噷copy鏁版嵁銆�
		if (paras.containsKey("FromTableName")) {
			String tableName = paras.get("FromTableName").toString();
			String tablePK = paras.get("FromTablePK").toString();
			String tablePKVal = paras.get("FromTablePKVal").toString();

			DataTable dt = DBAccess
					.RunSQLReturnTable("SELECT * FROM " + tableName + " WHERE " + tablePK + "='" + tablePKVal + "'");
			if (dt.Rows.size() == 0) {
				throw new RuntimeException("@鍒╃敤table浼犻�掓暟鎹敊璇紝娌℃湁鎵惧埌鎸囧畾鐨勮鏁版嵁锛屾棤娉曚负鐢ㄦ埛濉厖鏁版嵁銆�");
			}

			String innerKeys = ",OID,RDT,CDT,FID,WFState,";
			for (DataColumn dc : dt.Columns) {
				if (innerKeys.contains("," + dc.ColumnName + ",")) {
					continue;
				}

				wk.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName).toString());
				rpt.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName).toString());
			}
			rpt.Update();
		}
		// 鑾峰彇鐗规畩鏍囪鍙橀噺.
		String PFlowNo = null;
		String PNodeIDStr = null;
		String PWorkIDStr = null;
		String PFIDStr = null;

		String CopyFormWorkID = null;
		if (paras.containsKey("CopyFormWorkID") == true) {
			CopyFormWorkID = paras.get("CopyFormWorkID").toString();
			PFlowNo = this.getNo();
			PNodeIDStr = paras.get("CopyFormNode").toString();
			PWorkIDStr = CopyFormWorkID;
			PFIDStr = "0";
		}

		if (paras.containsKey("PNodeID") == true) {
			PFlowNo = paras.get("PFlowNo").toString();
			PNodeIDStr = paras.get("PNodeID").toString();
			if (paras.containsKey("PWorkID") == true)
				PWorkIDStr = paras.get("PWorkID").toString();
			PFIDStr = "0";
			if (paras.containsKey("PFID") == true) {
				PFIDStr = paras.get("PFID").toString(); // 鐖舵祦绋�.
			}
		}
		/// #region 鍒ゆ柇鏄惁瑁呰浇涓婁竴鏉℃暟鎹�.
		if (this.getIsLoadPriData() == true && this.getStartGuideWay() == BP.WF.Template.StartGuideWay.None) {
			// 濡傛灉闇�瑕佷粠涓婁竴涓祦绋嬪疄渚嬩笂copy鏁版嵁.
			String sql = "SELECT OID FROM " + this.getPTable() + " WHERE FlowStarter='" + WebUser.getNo()
					+ "' AND OID!=" + wk.getOID() + " ORDER BY OID DESC";
			String workidPri = DBAccess.RunSQLReturnStringIsNull(sql, "0");
			if (workidPri.equals("0")) {
				// 璇存槑娌℃湁绗竴绗旀暟鎹�.
			} else {
				PFlowNo = this.getNo();
				PNodeIDStr = Integer.parseInt(this.getNo()) + "01";
				PWorkIDStr = workidPri;
				PFIDStr = "0";
				CopyFormWorkID = workidPri;
			}
		}
		/// #region 澶勭悊娴佺▼涔嬮棿鐨勬暟鎹紶閫�1銆�
		if (StringHelper.isNullOrEmpty(PNodeIDStr) == false && StringHelper.isNullOrEmpty(PWorkIDStr) == false) {
			long PWorkID = Long.parseLong(PWorkIDStr);
			long PNodeID = 0;
			if (CopyFormWorkID != null) {
				PNodeID = Long.parseLong(PNodeIDStr);
			}
			/// #region copy 棣栧厛浠庣埗娴佺▼鐨凬DxxxRpt copy.
			long pWorkIDReal = 0;
			Flow pFlow = new Flow(PFlowNo);
			String pOID = "";
			if (StringHelper.isNullOrEmpty(PFIDStr) == true || PFIDStr.equals("0")) {
				pOID = (new Long(PWorkID)).toString();
			} else {
				pOID = PFIDStr;
			}

			String sql = "SELECT * FROM " + pFlow.getPTable() + " WHERE OID=" + pOID;
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() != 1) {
				throw new RuntimeException("@涓嶅簲璇ユ煡璇笉鍒扮埗娴佺▼鐨勬暟鎹�, 鍙兘鐨勬儏鍐典箣涓�,璇风‘璁よ鐖舵祦绋嬬殑璋冪敤鑺傜偣鏄瓙绾跨▼锛屼絾鏄病鏈夋妸瀛愮嚎绋嬬殑FID鍙傛暟浼犻�掕繘鏉ャ��");
			}

			wk.Copy(dt.Rows.get(0));
			rpt.Copy(dt.Rows.get(0));
			/// #region 浠庤皟鐢ㄧ殑鑺傜偣涓奵opy.
			BP.WF.Node fromNd = new BP.WF.Node(Integer.parseInt(PNodeIDStr));
			Work wkFrom = fromNd.getHisWork();
			wkFrom.setOID(PWorkID);
			if (wkFrom.RetrieveFromDBSources() == 0) {
				throw new RuntimeException("@鐖舵祦绋嬬殑宸ヤ綔ID涓嶆纭紝娌℃湁鏌ヨ鍒版暟鎹�" + PWorkID);
			}
			for (Object k : paras.keySet()) {
				wk.SetValByKey(k.toString(), paras.get(k));
				rpt.SetValByKey(k.toString(), paras.get(k));
			}
			wk.setOID(newOID);
			rpt.setOID(newOID);
			// 鍦ㄦ墽琛宑opy鍚庯紝鏈夊彲鑳借繖涓や釜瀛楁浼氳鍐叉帀銆�
			if (CopyFormWorkID != null) {
				// 濡傛灉涓嶆槸 鎵ц鐨勪粠宸茬粡瀹屾垚鐨勬祦绋媍opy.

				wk.SetValByKey(StartWorkAttr.PFlowNo, PFlowNo);
				wk.SetValByKey(StartWorkAttr.PNodeID, PNodeID);
				wk.SetValByKey(StartWorkAttr.PWorkID, PWorkID);

				rpt.SetValByKey(GERptAttr.PFlowNo, PFlowNo);
				rpt.SetValByKey(GERptAttr.PNodeID, PNodeID);
				rpt.SetValByKey(GERptAttr.PWorkID, PWorkID);

				// 蹇樿浜嗗鍔犺繖鍙ヨ瘽.
				rpt.SetValByKey(GERptAttr.PEmp, WebUser.getNo());

				// 瑕佸鐞嗗崟鎹紪鍙� BillNo .
				if (!this.getBillNoFormat().equals("")) {
					rpt.SetValByKey(GERptAttr.BillNo, BP.WF.WorkFlowBuessRole.GenerBillNo(this.getBillNoFormat(),
							rpt.getOID(), rpt, this.getPTable()));

					// 璁剧疆鍗曟嵁缂栧彿.
					wk.SetValByKey(GERptAttr.BillNo, rpt.getBillNo());
				}

				rpt.SetValByKey(GERptAttr.FID, 0);
				rpt.SetValByKey(GERptAttr.FlowStartRDT, BP.DA.DataType.getCurrentDataTime());
				rpt.SetValByKey(GERptAttr.FlowEnderRDT, BP.DA.DataType.getCurrentDataTime());
				rpt.SetValByKey(GERptAttr.MyNum, 0);
				rpt.SetValByKey(GERptAttr.WFState, WFState.Blank.getValue());
				rpt.SetValByKey(GERptAttr.FlowStarter, emp.getNo());
				rpt.SetValByKey(GERptAttr.FlowEnder, emp.getNo());
				rpt.SetValByKey(GERptAttr.FlowEndNode, this.getStartNodeID());
				rpt.SetValByKey(GERptAttr.FK_Dept, emp.getFK_Dept());
				rpt.SetValByKey(GERptAttr.FK_NY, DataType.getCurrentYearMonth());

				if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly) {
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getName());
				}

				if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName) {
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getNo());
				}

				if (BP.WF.Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName) {
					rpt.SetValByKey(GERptAttr.FlowEmps, "@" + emp.getNo() + "," + emp.getName());
				}

			}

			if (rpt.getEnMap().getPhysicsTable() != wk.getEnMap().getPhysicsTable()) {
				wk.Update(); // 鏇存柊宸ヤ綔鑺傜偣鏁版嵁.
			}
			rpt.Update(); // 鏇存柊娴佺▼鏁版嵁琛�.

			/// #endregion 鐗规畩璧嬪��.

			/// #region 澶嶅埗鍏朵粬鏁版嵁..
			// 澶嶅埗鏄庣粏銆�
			MapDtls dtls = wk.getHisMapDtls();
			if (dtls.size() > 0) {
				MapDtls dtlsFrom = wkFrom.getHisMapDtls();
				int idx = 0;
				if (dtlsFrom.size() == dtls.size()) {
					for (MapDtl dtl : dtls.ToJavaList()) {
						if (dtl.getIsCopyNDData() == false) {
							continue;
						}

						// new 涓�涓疄渚�.
						GEDtl dtlData = new GEDtl(dtl.getNo());

						// 妫�鏌ヨ鏄庣粏琛ㄦ槸鍚︽湁鏁版嵁锛屽鏋滄病鏈夋暟鎹紝灏眂opy杩囨潵锛屽鏋滄湁锛屽氨璇存槑宸茬粡copy杩囦簡銆�
						// sql = "SELECT COUNT(OID) FROM
						// "+dtlData.getEnMap().getPhysicsTable()+" WHERE
						// RefPK="+wk.OID;

						// 鍒犻櫎浠ュ墠鐨勬暟鎹�.
						sql = "DELETE FROM " + dtlData.getEnMap().getPhysicsTable() + " WHERE RefPK=" + wk.getOID();
						DBAccess.RunSQL(sql);

						MapDtl dtlFrom = (MapDtl) ((dtlsFrom.get(idx) instanceof MapDtl) ? dtlsFrom.get(idx) : null);

						GEDtls dtlsFromData = new GEDtls(dtlFrom.getNo());
						dtlsFromData.Retrieve(GEDtlAttr.RefPK, PWorkID);
						for (GEDtl geDtlFromData : dtlsFromData.ToJavaList()) {
							dtlData.Copy(geDtlFromData);
							// dtlsFromData.Retrieve(GEDtlAttr.RefPK, PWorkID);
							dtlData.setRefPK(wk.getOID() + "");
							if (PFlowNo.equals(this.getNo())) {
								dtlData.InsertAsNew();
							} else {
								if (this.getStartLimitRole() == BP.WF.StartLimitRole.OnlyOneSubFlow) {
									dtlData.SaveAsOID((int) geDtlFromData.getOID()); // 涓哄瓙娴佺▼鐨勬椂鍊欙紝浠呬粎鍏佽琚皟鐢�1娆�.
								} else {
									dtlData.InsertAsNew();
								}
							}
						}
					}
				}
			}

			// 澶嶅埗闄勪欢鏁版嵁銆�
			if (wk.getHisFrmAttachments().size() > 0) {
				if (wkFrom.getHisFrmAttachments().size() > 0) {
					int toNodeID = wk.getNodeID();

					// 鍒犻櫎鏁版嵁銆�
					DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + toNodeID
							+ "' AND RefPKVal='" + wk.getOID() + "'");
					FrmAttachmentDBs athDBs = new FrmAttachmentDBs("ND" + PNodeIDStr, (new Long(PWorkID)).toString());

					for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
						FrmAttachmentDB athDB_N = new FrmAttachmentDB();
						athDB_N.Copy(athDB);
						athDB_N.setFK_MapData("ND" + toNodeID);
						athDB_N.setRefPKVal(String.valueOf(wk.getOID()));
						athDB_N.setFK_FrmAttachment(
								athDB_N.getFK_FrmAttachment().replace("ND" + PNodeIDStr, "ND" + toNodeID));

						if (athDB_N.getHisAttachmentUploadType() == AttachmentUploadType.Single) {
							/* 濡傛灉鏄崟闄勪欢. */
							athDB_N.setMyPK(athDB_N.getFK_FrmAttachment() + "_" + wk.getOID());
							if (athDB_N.getIsExits() == true) {
								continue; // 璇存槑涓婁竴涓妭鐐规垨鑰呭瓙绾跨▼宸茬粡copy杩囦簡,
											// 浣嗘槸杩樻湁瀛愮嚎绋嬪悜鍚堟祦鐐逛紶閫掓暟鎹殑鍙兘锛屾墍浠ヤ笉鑳界敤break.
							}
							athDB_N.Insert();
						} else {
							athDB_N.setMyPK(
									athDB_N.getUploadGUID() + "_" + athDB_N.getFK_MapData() + "_" + wk.getOID());
							athDB_N.Insert();
						}
					}
				}
			}
			// 姹傚嚭鏉ヨcopy鐨勮妭鐐规湁澶氬皯涓嫭绔嬭〃鍗�.
			FrmNodes fnsFrom = new FrmNodes(fromNd.getNodeID());
			if (fnsFrom.size() != 0) {
				// 姹傚綋鍓嶈妭鐐硅〃鍗曠殑缁戝畾鐨勮〃鍗�.
				FrmNodes fns = new FrmNodes(nd.getNodeID());
				if (fns.size() != 0) {
					// 寮�濮嬮亶鍘嗗綋鍓嶇粦瀹氱殑琛ㄥ崟.
					for (FrmNode fn : fns.ToJavaList()) {
						for (FrmNode fnFrom : fnsFrom.ToJavaList()) {
							if (!fn.getFK_Frm().equals(fnFrom.getFK_Frm())) {
								continue;
							}

							BP.Sys.GEEntity geEnFrom = new GEEntity(fnFrom.getFK_Frm());
							geEnFrom.setOID(PWorkID);
							if (geEnFrom.RetrieveFromDBSources() == 0) {
								continue;
							}

							// 鎵ц鏁版嵁copy , 澶嶅埗鍒版湰韬�.
							geEnFrom.CopyToOID(wk.getOID());
						}
					}
				}
			}
		}
		// 鐢熸垚鍗曟嵁缂栧彿.
		if (this.getBillNoFormat().length() > 3)
			if (this.getBillNoFormat().length() > 3) {
				Object tempVar3 = this.getBillNoFormat();
				String billNoFormat = (String) ((tempVar3 instanceof String) ? tempVar3 : null);

				if (billNoFormat.contains("@")) {
					for (Object str : paras.keySet()) {
						billNoFormat = billNoFormat.replace("@" + str, paras.get(str).toString());
					}
				}

				// 鐢熸垚鍗曟嵁缂栧彿.
				rpt.setBillNo(BP.WF.Glo.GenerBillNo(billNoFormat, rpt.getOID(), rpt, this.getPTable()));
				// rpt.Update(GERptAttr.BillNo, rpt.BillNo);
				if (wk.getRow().containsKey(GERptAttr.BillNo) == true) {
					wk.SetValByKey(NDXRptBaseAttr.BillNo, rpt.getBillNo());
					// wk.Update(GERptAttr.BillNo, rpt.BillNo);
				}
				rpt.Update();
			}

		/// #endregion 澶勭悊鍗曟嵁缂栧彿.
		/// #region 澶勭悊娴佺▼涔嬮棿鐨勬暟鎹紶閫�2, 濡傛灉鏄洿鎺ヨ璺宠浆鍒版寚瀹氱殑鑺傜偣涓婂幓.
		if (paras.containsKey("JumpToNode") == true) {
			wk.setRec(WebUser.getNo());
			wk.SetValByKey(StartWorkAttr.RDT, BP.DA.DataType.getCurrentDataTime());
			wk.SetValByKey(StartWorkAttr.CDT, BP.DA.DataType.getCurrentDataTime());
			wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
			wk.setFK_Dept(emp.getFK_Dept());
			wk.SetValByKey("FK_DeptName", emp.getFK_DeptText());
			wk.SetValByKey("FK_DeptText", emp.getFK_DeptText());
			wk.setFID(0);
			wk.SetValByKey(StartWorkAttr.RecText, emp.getName());

			int jumpNodeID = Integer.parseInt(paras.get("JumpToNode").toString());
			Node jumpNode = new Node(jumpNodeID);

			String jumpToEmp = paras.get("JumpToEmp").toString();
			if (StringHelper.isNullOrEmpty(jumpToEmp)) {
				jumpToEmp = emp.getNo();
			}

			WorkNode wn = new WorkNode(wk, nd);
			wn.NodeSend(jumpNode, jumpToEmp);

			WorkFlow wf = new WorkFlow(this, wk.getOID(), wk.getFID());

			BP.WF.GenerWorkFlow gwf = new GenerWorkFlow(rpt.getOID());
			rpt.setWFState(WFState.Runing);
			rpt.Update();

			return wf.GetCurrentWorkNode().getHisWork();
		}

		/// #endregion 澶勭悊娴佺▼涔嬮棿鐨勬暟鎹紶閫掋��

		/// #region 鏈�鍚庢暣鐞唚k鏁版嵁.
		wk.setRec(emp.getNo());
		wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.getCurrentDataTime());
		wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.getCurrentDataTime());
		wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
		wk.setFK_Dept(emp.getFK_Dept());
		wk.SetValByKey("FK_DeptName", emp.getFK_DeptText());
		wk.SetValByKey("FK_DeptText", emp.getFK_DeptText());

		wk.SetValByKey(NDXRptBaseAttr.BillNo, rpt.getBillNo());
		wk.setFID(0);
		wk.SetValByKey(StartWorkAttr.RecText, emp.getName());

		int i = wk.Update();
		if (i == 0)
			wk.DirectInsert();

		/// #endregion 鏈�鍚庢暣鐞嗗弬鏁�.

		/// #region 缁檊enerworkflow鍒濆鍖栨暟鎹�. add 2015-08-06
		GenerWorkFlow mygwf = new GenerWorkFlow();
		mygwf.setWorkID(wk.getOID());

		if (mygwf.RetrieveFromDBSources() == 0) {
			mygwf.setFK_Flow(this.getNo());
			mygwf.setFK_FlowSort(this.getFK_FlowSort());
			mygwf.setSysType(this.getSysType());
			mygwf.setFK_Node(nd.getNodeID());
			mygwf.setWorkID(wk.getOID());
			mygwf.setWFState(WFState.Blank);
			mygwf.setFlowName(this.getName());
			mygwf.setRDT(BP.DA.DataType.getCurrentDataTime());
			mygwf.Insert();
		}
		mygwf.setStarter(WebUser.getNo());
		mygwf.setStarterName(WebUser.getName());
		mygwf.setFK_Dept(BP.Web.WebUser.getFK_Dept());
		mygwf.setDeptName(BP.Web.WebUser.getFK_DeptName());
		mygwf.setBillNo(rpt.getBillNo());
		mygwf.setTitle(rpt.getTitle());

		if (mygwf.getTitle().contains("@") == true) {
			mygwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, rpt));
		}
		if (StringHelper.isNullOrEmpty(PNodeIDStr) == false && StringHelper.isNullOrEmpty(PWorkIDStr) == false) {
			if (StringHelper.isNullOrEmpty(PFIDStr) == false) {
				mygwf.setPFID(Long.parseLong(PFIDStr));
			}
			mygwf.setPEmp(rpt.getPEmp());
			mygwf.setPFlowNo(rpt.getPFlowNo());
			mygwf.setPNodeID(rpt.getPNodeID());
			mygwf.setPWorkID(rpt.getPWorkID());
		}
		mygwf.Update();
		/// #endregion 缁� generworkflow 鍒濆鍖栨暟鎹�.

		return wk;
	}

	/// #endregion 鍒涘缓鏂板伐浣�.

	/// #region 鍒濆鍖栦竴涓伐浣�.
	/**
	 * 鍒濆鍖栦竴涓伐浣�
	 * 
	 * @param workid
	 * @param fk_node
	 * @return
	 * @throws Exception
	 */
	public final Work GenerWork(long workid, Node nd, boolean isPostBack) throws Exception {
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		if (wk.RetrieveFromDBSources() == 0) {
			//
			// * 2012-10-15 鍋剁劧鍙戠幇涓�娆″伐浣滀涪澶辨儏鍐�, WF_GenerWorkerlist WF_GenerWorkFlow
			// 閮芥湁杩欑瑪鏁版嵁锛屾病鏈夋煡鏄庝涪澶卞師鍥犮�� stone.
			// * 鐢ㄥ涓嬩唬鐮佽嚜鍔ㄤ慨澶嶏紝浣嗘槸浼氶亣鍒版暟鎹甤opy涓嶅畬鍏ㄧ殑闂銆�
			// *

			/// #warning 2011-10-15 鍋剁劧鍙戠幇涓�娆″伐浣滀涪澶辨儏鍐�.

			String fk_mapData = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
			GERpt rpt = new GERpt(fk_mapData);
			rpt.setOID(Integer.parseInt((new Long(workid)).toString()));
			if (rpt.RetrieveFromDBSources() >= 1) {
				// 鏌ヨ鍒版姤琛ㄦ暟鎹�.
				wk.Copy(rpt);
				wk.setRec(WebUser.getNo());
				wk.InsertAsOID(workid);
			} else {
				// 娌℃湁鏌ヨ鍒版姤琛ㄦ暟鎹�.

				/// #warning 杩欓噷涓嶅簲璇ュ嚭鐜扮殑寮傚父淇℃伅.

				String msg = "@涓嶅簲璇ュ嚭鐜扮殑寮傚父.";
				msg += "@鍦ㄤ负鑺傜偣NodeID=" + nd.getNodeID() + " workid:" + workid + " 鑾峰彇鏁版嵁鏃�.";
				msg += "@鑾峰彇瀹冪殑Rpt琛ㄦ暟鎹椂锛屼笉搴旇鏌ヨ涓嶅埌銆�";
				msg += "@GERpt 淇℃伅: table:" + rpt.getEnMap().getPhysicsTable() + "   OID=" + rpt.getOID();

				String sql = "SELECT count(*) FROM " + rpt.getEnMap().getPhysicsTable() + " WHERE OID=" + workid;
				int num = DBAccess.RunSQLReturnValInt(sql);

				msg += " @SQL:" + sql;
				msg += " ReturnNum:" + num;
				if (num == 0) {
					msg += "宸茬粡鐢╯ql鍙互鏌ヨ鍑烘潵锛屼絾鏄笉搴旇鐢ㄧ被鏌ヨ涓嶅嚭鏉�.";
				} else {
					// 濡傛灉鍙互鐢╯ql 鏌ヨ鍑烘潵.
					num = rpt.RetrieveFromDBSources();
					msg += "@浠巖pt.RetrieveFromDBSources = " + num;
				}

				Log.DefaultLogWriteLineError(msg);

				MapData md = new MapData("ND" + Integer.parseInt(nd.getFK_Flow()) + "01");
				sql = "SELECT * FROM " + md.getPTable() + " WHERE OID=" + workid;
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 1) {
					rpt.Copy(dt.Rows.get(0));
					try {
						rpt.setFlowStarter(dt.Rows.get(0).getValue(StartWorkAttr.Rec).toString());
						rpt.setFlowStartRDT(dt.Rows.get(0).getValue(StartWorkAttr.RDT).toString());
						rpt.setFK_Dept(dt.Rows.get(0).getValue(StartWorkAttr.FK_Dept).toString());
					} catch (java.lang.Exception e) {
					}

					rpt.setOID(Integer.parseInt((new Long(workid)).toString()));
					try {
						rpt.InsertAsOID(rpt.getOID());
					} catch (RuntimeException ex) {
						Log.DefaultLogWriteLineError(
								"@涓嶅簲璇ュ嚭鎻掑叆涓嶈繘鍘� rpt:" + rpt.getEnMap().getPhysicsTable() + " workid=" + workid);
						rpt.RetrieveFromDBSources();
					}
				} else {
					Log.DefaultLogWriteLineError("@娌℃湁鎵惧埌寮�濮嬭妭鐐圭殑鏁版嵁, NodeID:" + nd.getNodeID() + " workid:" + workid);
					throw new RuntimeException(
							"@娌℃湁鎵惧埌寮�濮嬭妭鐐圭殑鏁版嵁, NodeID:" + nd.getNodeID() + " workid:" + workid + " SQL:" + sql);
				}

				/// #warning 涓嶅簲璇ュ嚭鐜扮殑宸ヤ綔涓㈠け.
				Log.DefaultLogWriteLineError("@宸ヤ綔[" + nd.getNodeID() + " : " + wk.getEnDesc() + "], 鎶ヨ〃鏁版嵁WorkID="
						+ workid + " 涓㈠け, 娌℃湁浠嶯DxxxRpt閲屾壘鍒拌褰�,璇疯仈绯荤鐞嗗憳銆�");

				wk.Copy(rpt);
				wk.setRec(WebUser.getNo());
				wk.ResetDefaultVal();
				wk.Insert();
			}
		}

		/// #region 鍒ゆ柇鏄惁鏈夊垹闄よ崏绋跨殑闇�姹�.
		if (SystemConfig.getIsBSsystem() == true && isPostBack == false && nd.getIsStartNode()
				&& BP.Sys.Glo.getRequest() != null
				&& BP.Sys.Glo.getRequest().getParameter("IsDeleteDraft").equals("1")) {

			// 闇�瑕佸垹闄よ崏绋�.
			// 鏄惁瑕佸垹闄raft
			String title = wk.GetValStringByKey("Title");
			wk.ResetDefaultValAllAttr();
			wk.setOID(workid);
			wk.SetValByKey(GenerWorkFlowAttr.Title, title);
			wk.DirectUpdate();

			MapDtls dtls = wk.getHisMapDtls();
			for (MapDtl dtl : dtls.ToJavaList()) {
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + wk.getOID());
			}

			// 鍒犻櫎闄勪欢鏁版嵁銆�
			DBAccess.RunSQL("DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData='ND" + wk.getNodeID() + "' AND RefPKVal='"
					+ wk.getOID() + "'");

		}
		// 璁剧疆褰撳墠鐨勪汉鍛樻妸璁板綍浜恒��
		wk.setRec(WebUser.getNo());
		wk.setRecText(WebUser.getName());
		wk.setRec(WebUser.getNo());
		wk.SetValByKey(WorkAttr.RDT, BP.DA.DataType.getCurrentDataTime());
		wk.SetValByKey(WorkAttr.CDT, BP.DA.DataType.getCurrentDataTime());
		wk.SetValByKey(GERptAttr.WFState, WFState.Runing);
		wk.SetValByKey("FK_Dept", WebUser.getFK_Dept());
		wk.SetValByKey("FK_DeptName", WebUser.getFK_DeptName());
		wk.SetValByKey("FK_DeptText", WebUser.getFK_DeptName());
		wk.setFID(0);
		wk.SetValByKey("RecText", WebUser.getName());

		// 澶勭悊鍗曟嵁缂栧彿.
		if (nd.getIsStartNode()) {
			try {
				String billNo = wk.GetValStringByKey(NDXRptBaseAttr.BillNo);
				if (StringHelper.isNullOrEmpty(billNo) && nd.getHisFlow().getBillNoFormat().length() > 2) {
					// 璁╀粬鑷姩鐢熸垚缂栧彿
					wk.SetValByKey(NDXRptBaseAttr.BillNo, BP.WF.WorkFlowBuessRole.GenerBillNo(
							nd.getHisFlow().getBillNoFormat(), wk.getOID(), wk, nd.getHisFlow().getPTable()));
				}
			} catch (java.lang.Exception e2) {
				// 鍙兘鏄病鏈塨illNo杩欎釜瀛楁,涔熶笉闇�瑕佸鐞嗗畠.
			}
		}

		return wk;
	}

	/// #region 鍏朵粬閫氱敤鏂规硶.
	public final String DoBTableDTS() throws Exception {
		if (this.getDTSWay() == FlowDTSWay.None) {
			return "鎵ц澶辫触锛屾偍娌℃湁璁剧疆鍚屾鏂瑰紡銆�";
		}

		String info = "";
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.FK_Flow, this.getNo());
		for (GenerWorkFlow gwf : gwfs.ToJavaList()) {
			GERpt rpt = this.getHisGERpt();
			rpt.setOID(gwf.getWorkID());
			rpt.RetrieveFromDBSources();

			info += "@寮�濮嬪悓姝�:" + gwf.getTitle() + ",WorkID=" + gwf.getWorkID();
			if (gwf.getWFSta() == WFSta.Complete) {
				info += this.DoBTableDTS(rpt, new Node(gwf.getFK_Node()), true);
			} else {
				info += this.DoBTableDTS(rpt, new Node(gwf.getFK_Node()), false);
			}
		}
		return info;
	}

	/**
	 * 鍚屾褰撳墠鐨勬祦绋嬫暟鎹埌涓氬姟鏁版嵁琛ㄩ噷.
	 * 
	 * @param rpt
	 *            娴佺▼鎶ヨ〃
	 * @param currNode
	 *            褰撳墠鑺傜偣ID
	 * @param isStopFlow
	 *            娴佺▼鏄惁缁撴潫
	 * @return 杩斿洖鍚屾缁撴灉.
	 * @throws Exception
	 */
	public final String DoBTableDTS(GERpt rpt, Node currNode, boolean isStopFlow) throws Exception {
		boolean isActiveSave = false;
		// 鍒ゆ柇鏄惁绗﹀悎娴佺▼鏁版嵁鍚屾鏉′欢.
		switch (this.getDTSTime()) {
		case AllNodeSend:
			isActiveSave = true;
			break;
		case SpecNodeSend:
			if (this.getDTSSpecNodes().contains((new Integer(currNode.getNodeID())).toString()) == true) {
				isActiveSave = true;
			}
			break;
		case WhenFlowOver:
			if (isStopFlow) {
				isActiveSave = true;
			}
			break;
		default:
			break;
		}
		if (isActiveSave == false) {
			return "";
		}
		/// #region qinfaliang, 缂栧啓鍚屾鐨勪笟鍔￠�昏緫,鎵ц閿欒灏辨姏鍑哄紓甯�.

		String[] dtsArray = this.getDTSFields().split("[@]", -1);

		String[] lcArr = dtsArray[0].split("[,]", -1); // 鍙栧嚭瀵瑰簲鐨勪富琛ㄥ瓧娈�
		String[] ywArr = dtsArray[1].split("[,]", -1); // 鍙栧嚭瀵瑰簲鐨勪笟鍔¤〃瀛楁

		String sql = "SELECT " + dtsArray[0] + " FROM " + this.getPTable().toUpperCase() + " WHERE OID=" + rpt.getOID();
		DataTable lcDt = DBAccess.RunSQLReturnTable(sql);
		if (lcDt.Rows.size() == 0) // 娌℃湁璁板綍灏眗eturn鎺�
		{
			return "";
		}

		BP.Sys.SFDBSrc src = new BP.Sys.SFDBSrc(this.getDTSDBSrc());
		sql = "SELECT " + dtsArray[1] + " FROM " + this.getDTSBTable().toUpperCase();

		DataTable ywDt = src.RunSQLReturnTable(sql);

		String values = "";
		String upVal = "";

		for (int i = 0; i < lcArr.length; i++) {
			switch (src.getDBSrcType()) {
			case Localhost:
				switch (SystemConfig.getAppCenterDBType()) {
				case MSSQL:
					break;
				case Oracle:
					if (ywDt.Columns.get(ywArr[i]).DataType == DateUtils.class) {
						if (!StringHelper.isNullOrEmpty(lcDt.Rows.get(0).getValue(lcArr[i].toString()).toString())) {
							values += "to_date('" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "','YYYY-MM-DD'),";
						} else {
							values += "'',";
						}
						continue;
					}
					values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
					continue;
				case MySQL:
					break;
				case Informix:
					break;
				default:
					throw new RuntimeException("娌℃湁娑夊強鍒扮殑杩炴帴娴嬭瘯绫诲瀷...");
				}
				break;
			case SQLServer:
				break;
			case MySQL:
				break;
			case Oracle:
				if (ywDt.Columns.get(ywArr[i]).DataType == DateUtils.class) {
					if (!StringHelper.isNullOrEmpty(lcDt.Rows.get(0).getValue(lcArr[i].toString()).toString())) {
						values += "to_date('" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "','YYYY-MM-DD'),";
					} else {
						values += "'',";
					}
					continue;
				}
				values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
				continue;
			default:
				throw new RuntimeException("鏆傛椂涓嶆敮鎮ㄦ墍浣跨敤鐨勬暟鎹簱绫诲瀷!");
			}
			values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
			// 鑾峰彇闄や富閿箣澶栫殑鍏朵粬鍊�
			if (i > 0) {
				upVal = upVal + ywArr[i] + "='" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
			}
		}

		values = values.substring(0, values.length() - 1);
		upVal = upVal.substring(0, upVal.length() - 1);

		// 鏌ヨ瀵瑰簲鐨勪笟鍔¤〃涓槸鍚﹀瓨鍦ㄨ繖鏉¤褰�
		sql = "SELECT * FROM " + this.getDTSBTable().toUpperCase() + " WHERE " + getDTSBTablePK() + "='"
				+ lcDt.Rows.get(0).getValue(lcArr[0].toString()) + "'";
		DataTable dt = src.RunSQLReturnTable(sql);
		// 濡傛灉瀛樺湪锛屾墽琛屾洿鏂帮紝濡傛灉涓嶅瓨鍦紝鎵ц鎻掑叆
		if (dt.Rows.size() > 0) {

			sql = "UPDATE " + this.getDTSBTable().toUpperCase() + " SET " + upVal + " WHERE " + getDTSBTablePK() + "='"
					+ lcDt.Rows.get(0).getValue(lcArr[0].toString()) + "'";
		} else {
			sql = "INSERT INTO " + this.getDTSBTable().toUpperCase() + "(" + dtsArray[1] + ") VALUES(" + values + ")";
		}

		try {
			src.RunSQL(sql);
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		return "鍚屾鎴愬姛.";
	}

	/**
	 * 鑷姩鍙戣捣
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DoAutoStartIt() throws Exception {
		switch (this.getHisFlowRunWay()) {
		case SpecEmp: // 鎸囧畾浜哄憳鎸夋椂杩愯銆�
			String RunObj = this.getRunObj();
			String FK_Emp = RunObj.substring(0, RunObj.indexOf('@'));
			BP.Port.Emp emp = new BP.Port.Emp();
			emp.setNo(FK_Emp);
			if (emp.RetrieveFromDBSources() == 0) {
				return "鍚姩鑷姩鍚姩娴佺▼閿欒锛氬彂璧蜂汉(" + FK_Emp + ")涓嶅瓨鍦ㄣ��";
			}

			BP.Web.WebUser.SignInOfGener(emp);
			String info_send = BP.WF.Dev2Interface.Node_StartWork(this.getNo(), null, null, 0, null, 0, null)
					.ToMsgOfText();
			if (!WebUser.getNo().equals("admin")) {
				emp = new BP.Port.Emp();
				emp.setNo("admin");
				emp.Retrieve();
				BP.Web.WebUser.SignInOfGener(emp);
				return info_send;
			}
			return info_send;
		case DataModel: // 鎸夋暟鎹泦鍚堥┍鍔ㄧ殑妯″紡鎵ц銆�
			break;
		default:
			return "@璇ユ祦绋嬫偍娌℃湁璁剧疆涓鸿嚜鍔ㄥ惎鍔ㄧ殑娴佺▼绫诲瀷銆�";
		}

		String msg = "";
		BP.Sys.MapExt me = new MapExt();
		me.setMyPK("ND" + Integer.parseInt(this.getNo()) + "01_" + MapExtXmlList.StartFlow);
		int i = me.RetrieveFromDBSources();
		if (i == 0) {
			BP.DA.Log.DefaultLogWriteLineError("娌℃湁涓烘祦绋�(" + this.getName() + ")鐨勫紑濮嬭妭鐐硅缃彂璧锋暟鎹�,璇峰弬鑰冭鏄庝功瑙ｅ喅.");
			return "娌℃湁涓烘祦绋�(" + this.getName() + ")鐨勫紑濮嬭妭鐐硅缃彂璧锋暟鎹�,璇峰弬鑰冭鏄庝功瑙ｅ喅.";
		}
		if (StringHelper.isNullOrEmpty(me.getTag())) {
			BP.DA.Log.DefaultLogWriteLineError("娌℃湁涓烘祦绋�(" + this.getName() + ")鐨勫紑濮嬭妭鐐硅缃彂璧锋暟鎹�,璇峰弬鑰冭鏄庝功瑙ｅ喅.");
			return "娌℃湁涓烘祦绋�(" + this.getName() + ")鐨勫紑濮嬭妭鐐硅缃彂璧锋暟鎹�,璇峰弬鑰冭鏄庝功瑙ｅ喅.";
		}

		// 鑾峰彇浠庤〃鏁版嵁.
		DataSet ds = new DataSet();
		String[] dtlSQLs = me.getTag1().split("[*]", -1);
		for (String sql : dtlSQLs) {
			if (StringHelper.isNullOrEmpty(sql)) {
				continue;
			}

			String[] tempStrs = sql.split("[=]", -1);
			String dtlName = tempStrs[0];
			DataTable dtlTable = DBAccess.RunSQLReturnTable(sql.replace(dtlName + "=", ""));
			dtlTable.TableName = dtlName;
			ds.Tables.add(dtlTable);
		}

		/// #region 妫�鏌ユ暟鎹簮鏄惁姝ｇ‘.
		String errMsg = "";
		// 鑾峰彇涓昏〃鏁版嵁.
		DataTable dtMain = DBAccess.RunSQLReturnTable(me.getTag());
		if (dtMain.Rows.size() == 0) {
			return "娴佺▼(" + this.getName() + ")姝ゆ椂鏃犱换鍔�,鏌ヨ璇彞:" + me.getTag().replace("'", "鈥�");
		}

		msg += "@鏌ヨ鍒�(" + dtMain.Rows.size() + ")鏉′换鍔�.";

		if (dtMain.Columns.contains("Starter") == false) {
			errMsg += "@閰嶅�肩殑涓昏〃涓病鏈塖tarter鍒�.";
		}

		if (dtMain.Columns.contains("MainPK") == false) {
			errMsg += "@閰嶅�肩殑涓昏〃涓病鏈塎ainPK鍒�.";
		}

		if (errMsg.length() > 2) {
			return "娴佺▼(" + this.getName() + ")鐨勫紑濮嬭妭鐐硅缃彂璧锋暟鎹�,涓嶅畬鏁�." + errMsg;
		}

		/// #region 澶勭悊娴佺▼鍙戣捣.

		String fk_mapdata = "ND" + Integer.parseInt(this.getNo()) + "01";

		MapData md = new MapData(fk_mapdata);
		int idx = 0;
		for (DataRow dr : dtMain.Rows) {
			idx++;

			String mainPK = dr.getValue("MainPK").toString();
			String sql = "SELECT OID FROM " + md.getPTable() + " WHERE MainPK='" + mainPK + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0) {
				msg += "@" + this.getName() + ",绗�" + idx + "鏉�,姝や换鍔″湪涔嬪墠宸茬粡瀹屾垚銆�";
				continue; // 璇存槑宸茬粡璋冨害杩囦簡
			}

			String starter = dr.getValue("Starter").toString();
			if (!starter.equals(WebUser.getNo())) {
				BP.Web.WebUser.Exit();
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.setNo(starter);
				if (emp.RetrieveFromDBSources() == 0) {
					msg += "@" + this.getName() + ",绗�" + idx + "鏉�,璁剧疆鐨勫彂璧蜂汉鍛�:" + emp.getNo() + "涓嶅瓨鍦�.";
					msg += "@鏁版嵁椹卞姩鏂瑰紡鍙戣捣娴佺▼(" + this.getName() + ")璁剧疆鐨勫彂璧蜂汉鍛�:" + emp.getNo() + "涓嶅瓨鍦ㄣ��";
					continue;
				}
				WebUser.SignInOfGener(emp);
			}

			/// #region 缁欏��.
			Work wk = this.NewWork();
			for (DataColumn dc : dtMain.Columns) {
				wk.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
			}

			if (ds.Tables.size() != 0) {
				// MapData md = new MapData(nodeTable);
				MapDtls dtls = md.getMapDtls(); // new MapDtls(nodeTable);
				for (MapDtl dtl : dtls.ToJavaList()) {
					for (DataTable dt : ds.Tables) {
						if (dt.TableName != dtl.getNo()) {
							continue;
						}

						// 鍒犻櫎鍘熸潵鐨勬暟鎹��
						GEDtl dtlEn = dtl.getHisGEDtl();
						dtlEn.Delete(GEDtlAttr.RefPK, (new Long(wk.getOID())).toString());

						// 鎵ц鏁版嵁鎻掑叆銆�
						for (DataRow drDtl : dt.Rows) {
							if (!drDtl.getValue("RefMainPK").toString().equals(mainPK)) {
								continue;
							}

							dtlEn = dtl.getHisGEDtl();
							for (DataColumn dc : dt.Columns) {
								dtlEn.SetValByKey(dc.ColumnName, drDtl.getValue(dc.ColumnName).toString());
							}

							dtlEn.setRefPK(String.valueOf(wk.getOID()));
							dtlEn.setOID(0);
							dtlEn.Insert();
						}
					}
				}
			}
			// 澶勭悊鍙戦�佷俊鎭�.
			Node nd = this.getHisStartNode();
			try {
				WorkNode wn = new WorkNode(wk, nd);
				String infoSend = wn.NodeSend().ToMsgOfHtml();
				BP.DA.Log.DefaultLogWriteLineInfo(msg);
				msg += "@" + this.getName() + ",绗�" + idx + "鏉�,鍙戣捣浜哄憳:" + WebUser.getNo() + "-" + WebUser.getName()
						+ "宸插畬鎴�.\r\n" + infoSend;
				// this.SetText("@绗紙" + idx + "锛夋潯浠诲姟锛�" + WebUser.getNo() + " - "
				// + WebUser.Name + "宸茬粡瀹屾垚銆俓r\n" + msg);
			} catch (RuntimeException ex) {
				msg += "@" + this.getName() + ",绗�" + idx + "鏉�,鍙戣捣浜哄憳:" + WebUser.getNo() + "-" + WebUser.getName()
						+ "鍙戣捣鏃跺嚭鐜伴敊璇�.\r\n" + ex.getMessage();
			}
			msg += "<hr>";
		}
		return msg;

		/// #endregion 澶勭悊娴佺▼鍙戣捣.
	}

	/**
	 * UI鐣岄潰涓婄殑璁块棶鎺у埗
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin")) {
			uac.IsUpdate = true;
		}
		return uac;
	}

	/**
	 * 淇ˉ娴佺▼鏁版嵁瑙嗗浘
	 * 
	 * @return
	 */
	public static String RepareV_FlowData_View() {
		return null;
	
	}

	public final String ClearCash() {
		BP.DA.Cash.ClearCash();
		return "娓呴櫎鎴愬姛.";
	}

	/**
	 * 鏍￠獙娴佺▼
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DoCheck() throws Exception {

		BP.DA.Cash.ClearCash();

		/// #region 妫�鏌ョ嫭绔嬭〃鍗�
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
		String frms = "";
		String err = "";
		for (FrmNode item : fns.ToJavaList()) {
			if (frms.contains(item.getFK_Frm() + ","))
				continue;

			frms += item.getFK_Frm() + ",";
			try {
				MapData md = new MapData(item.getFK_Frm());
				md.RepairMap();
				Entity en = md.getHisEn();
				en.CheckPhysicsTable();
			} catch (RuntimeException ex) {
				err += "@鑺傜偣缁戝畾鐨勮〃鍗�:" + item.getFK_Frm() + ",宸茬粡琚垹闄や簡.寮傚父淇℃伅." + ex.getMessage();
			}
		}

		try {
			// 璁剧疆娴佺▼鍚嶇О.
			DBAccess.RunSQL("UPDATE WF_Node SET FlowName = (SELECT Name FROM WF_Flow WHERE NO=WF_Node.FK_Flow)");
			
		    BP.WF.Node node = new BP.WF.Node();
		    node.CheckPhysicsTable();
			// 鍒犻櫎鍨冨溇,闈炴硶鏁版嵁.
			String sqls = "DELETE FROM Sys_FrmSln where fk_mapdata not in (select no from sys_mapdata)";
			sqls += "@ DELETE FROM WF_Direction WHERE Node=ToNode";
			DBAccess.RunSQLs(sqls);

			// 鏇存柊璁＄畻鏁版嵁.
			this.setNumOfBill(DBAccess.RunSQLReturnValInt(
					"SELECT count(*) FROM WF_BillTemplate WHERE NodeID IN (select NodeID from WF_Flow WHERE no='"
							+ this.getNo() + "')"));
			this.setNumOfDtl(DBAccess.RunSQLReturnValInt(
					"SELECT count(*) FROM Sys_MapDtl WHERE FK_MapData='ND" + Integer.parseInt(this.getNo()) + "Rpt'"));
			this.DirectUpdate();

			String msg = "@  =======  鍏充簬銆�" + this.getName() + " 銆嬫祦绋嬫鏌ユ姤鍛�  ============";
			msg += "@淇℃伅杈撳嚭鍒嗕负涓夌: 淇℃伅  璀﹀憡  閿欒. 濡傛灉閬囧埌杈撳嚭鐨勯敊璇紝鍒欏繀椤昏鍘讳慨鏀规垨鑰呰缃�.";
			msg += "@娴佺▼妫�鏌ョ洰鍓嶈繕涓嶈兘瑕嗙洊100%鐨勯敊璇�,闇�瑕佹墜宸ョ殑杩愯涓�娆℃墠鑳界‘淇濇祦绋嬭璁＄殑姝ｇ‘鎬�.";

			Nodes nds = new Nodes(this.getNo());

			// 鍗曟嵁妯＄増.
			BillTemplates bks = new BillTemplates(this.getNo());

			// 鏉′欢闆嗗悎.
			Conds conds = new Conds(this.getNo());

			/// #region 瀵硅妭鐐硅繘琛屾鏌�
			// 鑺傜偣琛ㄥ崟瀛楁鏁版嵁绫诲瀷妫�鏌�--begin---------
			msg += CheckFormFields();
			// 琛ㄥ崟瀛楁鏁版嵁绫诲瀷妫�鏌�-------End-----

			for (Node nd : nds.ToJavaList()) {
				// 璁剧疆瀹冪殑浣嶇疆绫诲瀷.
				nd.SetValByKey(NodeAttr.NodePosType, nd.GetHisNodePosType().getValue());

				msg += "@淇℃伅: -------- 寮�濮嬫鏌ヨ妭鐐笽D:(" + nd.getNodeID() + ")鍚嶇О:(" + nd.getName() + ")淇℃伅 -------------";

				/// #region 淇鏁拌妭鐐硅〃鍗曟暟鎹簱.
				msg += "@淇℃伅:寮�濮嬭ˉ鍏�&淇鑺傜偣蹇呰鐨勫瓧娈�";
				try {
					nd.RepareMap();
				} catch (RuntimeException ex) {
					throw new RuntimeException("@淇鑺傜偣琛ㄥ繀瑕佸瓧娈垫椂鍑虹幇閿欒:" + nd.getName() + " - " + ex.getMessage());
				}

				msg += "@淇℃伅:寮�濮嬩慨澶嶈妭鐐圭墿鐞嗚〃.";
				try {
					nd.getHisWork().CheckPhysicsTable();
				} catch (RuntimeException ex) {
					msg += "@妫�鏌ヨ妭鐐硅〃瀛楁鏃跺嚭鐜伴敊璇�:" + "NodeID" + nd.getNodeID() + " Table:"
							+ nd.getHisWork().getEnMap().getPhysicsTable() + " Name:" + nd.getName()
							+ " , 鑺傜偣绫诲瀷NodeWorkTypeText=" + nd.getNodeWorkTypeText() + "鍑虹幇閿欒.@err=" + ex.getMessage();
				}

				// 浠庤〃妫�鏌ャ��
				MapDtls dtls = new BP.Sys.MapDtls("ND" + nd.getNodeID());
				for (MapDtl dtl : dtls.ToJavaList()) {
					msg += "@妫�鏌ユ槑缁嗚〃:" + dtl.getName();
					try {
						dtl.getHisGEDtl().CheckPhysicsTable();
					} catch (RuntimeException ex) {
						msg += "@妫�鏌ユ槑缁嗚〃鏃堕棿鍑虹幇閿欒" + ex.getMessage();
					}
				}

				MapAttrs mattrs = new MapAttrs("ND" + nd.getNodeID());

				msg += "@淇℃伅:寮�濮嬪鑺傜偣鐨勮闂鍒欒繘琛屾鏌�.";

				switch (nd.getHisDeliveryWay()) {
				case ByStation:
					if (nd.getNodeStations().size() == 0) {
						msg += "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄寜宀椾綅锛屼絾鏄偍娌℃湁涓鸿妭鐐圭粦瀹氬矖浣嶃��";
					}
					break;
				case ByDept:
					if (nd.getNodeDepts().size() == 0) {
						msg += "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄寜閮ㄩ棬锛屼絾鏄偍娌℃湁涓鸿妭鐐圭粦瀹氶儴闂ㄣ��";
					}
					break;
				case ByBindEmp:
					if (nd.getNodeEmps().size() == 0) {
						msg += "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄寜浜哄憳锛屼絾鏄偍娌℃湁涓鸿妭鐐圭粦瀹氫汉鍛樸��";
					}
					break;
				case BySpecNodeEmp: // 鎸夋寚瀹氱殑宀椾綅璁＄畻.
				case BySpecNodeEmpStation: // 鎸夋寚瀹氱殑宀椾綅璁＄畻.
					if (nd.getDeliveryParas().trim().length() == 0) {
						msg += "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄寜鎸囧畾鐨勫矖浣嶈绠楋紝浣嗘槸鎮ㄦ病鏈夎缃妭鐐圭紪鍙�.</font>";
					} else {
						String[] deliveryParas = nd.getDeliveryParas().split(",");
						for(String str : deliveryParas){
							if (DataType.IsNumStr(str) == false) {
								msg += "@閿欒:鎮ㄨ缃寚瀹氬矖浣嶇殑鑺傜偣缂栧彿鏍煎紡涓嶆纭紝鐩墠璁剧疆鐨勪负{" + nd.getDeliveryParas() + "}";
							}
						}
					}
					break;
				case ByDeptAndStation: // 鎸夐儴闂ㄤ笌宀椾綅鐨勪氦闆嗚绠�.
					String mysql = "";
					// added by liuxc,2015.6.30.
					// 鍖哄埆闆嗘垚涓嶣PM妯″紡
					if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne) {
						mysql = "SELECT No FROM Port_Emp WHERE No IN (SELECT No FK_Emp FROM Port_Emp WHERE FK_Dept IN ( SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node="
								+ nd.getNodeID() + "))AND No IN (SELECT FK_Emp FROM " + BP.WF.Glo.getEmpStation()
								+ " WHERE FK_Station IN ( SELECT FK_Station FROM WF_NodeStation WHERE FK_Node="
								+ nd.getNodeID() + " )) ORDER BY No ";
					} else {
						mysql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes"
								+ "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept"
								+ "             AND wnd.FK_Node = " + nd.getNodeID()
								+ "        INNER JOIN WF_NodeStation wns"
								+ "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node ="
								+ nd.getNodeID() + " ORDER BY" + "        pdes.FK_Emp";
					}

					DataTable mydt = DBAccess.RunSQLReturnTable(mysql);
					if (mydt.Rows.size() == 0) {
						msg += "@閿欒:鎸夌収宀椾綅涓庨儴闂ㄧ殑浜ら泦璁＄畻閿欒锛屾病鏈変汉鍛橀泦鍚坽" + mysql + "}";
					}
					break;
				case BySQL:
				case BySQLAsSubThreadEmpsAndData:
					if (nd.getDeliveryParas().trim().length() == 0) {
						msg += "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄寜SQL鏌ヨ锛屼絾鏄偍娌℃湁鍦ㄨ妭鐐瑰睘鎬ч噷璁剧疆鏌ヨsql锛屾sql鐨勮姹傛槸鏌ヨ蹇呴』鍖呭惈No,Name涓や釜鍒楋紝sql琛ㄨ揪寮忛噷鏀寔@+瀛楁鍙橀噺锛岃缁嗗弬鑰冨紑鍙戞墜鍐�.";
					} else {
						try {
							String sql = nd.getDeliveryParas();
							for (MapAttr item : mattrs.ToJavaList()) {
								if (item.getIsNum()) {
									sql = sql.replace("@" + item.getKeyOfEn(), "0");
								} else {
									sql = sql.replace("@" + item.getKeyOfEn(), "'0'");
								}
							}

							sql = sql.replace("WebUser.No", "'ss'");
							sql = sql.replace("@WebUser.Name", "'ss'");
							sql = sql.replace("@WebUser.FK_Dept", "'ss'");
							sql = sql.replace("@WebUser.FK_DeptName", "'ss'");

							sql = sql.replace("''''", "''"); // 鍑虹幇鍙屽紩鍙风殑闂.

							if (sql.contains("@")) {
								throw new RuntimeException("鎮ㄧ紪鍐欑殑sql鍙橀噺濉啓涓嶆纭紝瀹為檯鎵ц涓紝娌℃湁琚畬鍏ㄦ浛鎹笅鏉�" + sql);
							}

							DataTable testDB = null;
							try {
								testDB = DBAccess.RunSQLReturnTable(sql);
							} catch (RuntimeException ex) {
								msg += "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄寜SQL鏌ヨ,鎵ц姝よ鍙ラ敊璇�." + ex.getMessage();
							}

							if (testDB.Columns.contains("no") == false || testDB.Columns.contains("name") == false) {
								msg += "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄寜SQL鏌ヨ锛岃缃殑sql涓嶇鍚堣鍒欙紝姝ql鐨勮姹傛槸鏌ヨ蹇呴』鍖呭惈No,Name涓や釜鍒楋紝sql琛ㄨ揪寮忛噷鏀寔@+瀛楁鍙橀噺锛岃缁嗗弬鑰冨紑鍙戞墜鍐�.";
							}
						} catch (RuntimeException ex) {
							msg += ex.getMessage();
						}
					}
					break;
				case ByPreviousNodeFormEmpsField:
					// 鍘籸pt琛ㄤ腑锛屾煡璇㈡槸鍚︽湁杩欎釜瀛楁
					String str = (nd.getNodeID() + "").substring(0, (nd.getNodeID() + "").length() - 2);
					MapAttrs rptAttrs = new BP.Sys.MapAttrs();
					rptAttrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + str + "Rpt", MapAttrAttr.KeyOfEn);
					if (rptAttrs.Contains(BP.Sys.MapAttrAttr.KeyOfEn, nd.getDeliveryParas()) == false) {
						// 妫�鏌ヨ妭鐐瑰瓧娈垫槸鍚︽湁FK_Emp瀛楁
						msg += "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄痆06.鎸変笂涓�鑺傜偣琛ㄥ崟鎸囧畾鐨勫瓧娈靛�间綔涓烘湰姝ラ鐨勬帴鍙椾汉]锛屼絾鏄偍娌℃湁鍦ㄨ妭鐐瑰睘鎬х殑[璁块棶瑙勫垯璁剧疆鍐呭]閲岃缃寚瀹氱殑琛ㄥ崟瀛楁锛岃缁嗗弬鑰冨紑鍙戞墜鍐�.";
					}
					// if (mattrs.Contains(BP.Sys.MapAttrAttr.KeyOfEn, "FK_Emp")
					// == false)
					// {
					// /*妫�鏌ヨ妭鐐瑰瓧娈垫槸鍚︽湁FK_Emp瀛楁*/
					// msg +=
					// "@閿欒:鎮ㄨ缃簡璇ヨ妭鐐圭殑璁块棶瑙勫垯鏄寜鎸囧畾鑺傜偣琛ㄥ崟浜哄憳锛屼絾鏄偍娌℃湁鍦ㄨ妭鐐硅〃鍗曚腑澧炲姞FK_Emp瀛楁锛岃缁嗗弬鑰冨紑鍙戞墜鍐�
					// .";
					// }
					break;
				case BySelected: // 鐢变笂涓�姝ュ彂閫佷汉鍛橀�夋嫨
					if (nd.getIsStartNode()) {
						// msg += "@閿欒:寮�濮嬭妭鐐逛笉鑳借缃寚瀹氱殑閫夋嫨浜哄憳璁块棶瑙勫垯銆�";
						break;
					}
					break;
				case ByPreviousNodeEmp: // 鐢变笂涓�姝ュ彂閫佷汉鍛橀�夋嫨
					if (nd.getIsStartNode()) {
						msg += "@閿欒:鑺傜偣璁块棶瑙勫垯璁剧疆閿欒:寮�濮嬭妭鐐癸紝涓嶅厑璁歌缃笌涓婁竴鑺傜偣鐨勫伐浣滀汉鍛樼浉鍚�.";
						break;
					}
					break;
				default:
					break;
				}
				msg += "@瀵硅妭鐐圭殑璁块棶瑙勫垯杩涜妫�鏌ュ畬鎴�....";
				/// #region 妫�鏌ヨ妭鐐瑰畬鎴愭潯浠讹紝鏂瑰悜鏉′欢鐨勫畾涔�.
				// 璁剧疆瀹冩病鏈夋祦绋嬪畬鎴愭潯浠�.
				nd.setIsCCFlow(false);

				if (conds.size() != 0) {
					msg += "@淇℃伅:寮�濮嬫鏌�(" + nd.getName() + ")鏂瑰悜鏉′欢:";
					for (Cond cond : conds.ToJavaList()) {
						if (cond.getFK_Node() == nd.getNodeID() && cond.getHisCondType() == CondType.Flow) {
							nd.setIsCCFlow(true);
							nd.Update();
						}

						Node ndOfCond = new Node();
						ndOfCond.setNodeID(ndOfCond.getNodeID());
						if (ndOfCond.RetrieveFromDBSources() == 0) {
							continue;
						}

						try {
							if (cond.getAttrKey().length() < 2) {
								continue;
							}
							if (ndOfCond.getHisWork().getEnMap().getAttrs().Contains(cond.getAttrKey()) == false) {
								throw new RuntimeException(
										"@閿欒:灞炴��:" + cond.getAttrKey() + " , " + cond.getAttrName() + " 涓嶅瓨鍦ㄣ��");
							}
						} catch (RuntimeException ex) {
							msg += "@閿欒:" + ex.getMessage();
							ndOfCond.Delete();
						}
						msg += cond.getAttrKey() + cond.getAttrName() + cond.getOperatorValue() + "銆�";
					}
					msg += "@(" + nd.getName() + ")鏂瑰悜鏉′欢妫�鏌ュ畬鎴�.....";
				}
				
				//#region 濡傛灉鏄紩鐢ㄧ殑琛ㄥ崟搴撶殑琛ㄥ崟锛屽氨瑕佹鏌ヨ琛ㄥ崟鏄惁鏈塅ID瀛楁锛屾病鏈夊氨鑷姩澧炲姞.
                if (nd.getHisFormType() == NodeFormType.RefOneFrmTree)
                {
                    MapAttr mattr = new MapAttr();
                    mattr.setMyPK(nd.getNodeFrmID() + "_FID");
                    if (mattr.RetrieveFromDBSources() == 0)
                    {
                        mattr.setKeyOfEn("FID");
                        mattr.setFK_MapData(nd.getNodeFrmID());
                        mattr.setMyDataType(DataType.AppInt);
                        mattr.setUIVisible(false);
                        mattr.setName("FID(鑷姩澧炲姞)");
                        mattr.Insert();

                        GEEntity en = new GEEntity(nd.getNodeFrmID());
                        en.CheckPhysicsTable();
                    }
                }
                //#endregion 濡傛灉鏄紩鐢ㄧ殑琛ㄥ崟搴撶殑琛ㄥ崟锛屽氨瑕佹鏌ヨ琛ㄥ崟鏄惁鏈塅ID瀛楁锛屾病鏈夊氨鑷姩澧炲姞.
			}
			
			
			// #region 鎵ц涓�娆′繚瀛�. @浜庡簡娴风炕璇�. 澧炲姞浜嗘閮ㄥ垎.
			NodeExts nes = new NodeExts();
			nes.Retrieve(NodeAttr.FK_Flow, this.getNo());
			for (NodeExt item : nes.ToJavaList()) {
				item.Update(); // 璋冪敤閲岄潰鐨勪笟鍔￠�昏緫鎵ц妫�鏌�.
			}

			msg += "@娴佺▼鐨勫熀纭�淇℃伅: ------ ";
			msg += "@缂栧彿:  " + this.getNo() + " 鍚嶇О:" + this.getName() + " , 瀛樺偍琛�:" + this.getPTable();

			msg += "@淇℃伅:寮�濮嬫鏌ヨ妭鐐规祦绋嬫姤琛�.";
			this.DoCheck_CheckRpt(this.getHisNodes());
			msg += "@淇℃伅:寮�濮嬫鏌ヨ妭鐐圭殑鐒︾偣瀛楁";

			// 鑾峰緱gerpt瀛楁.
			GERpt rpt = this.getHisGERpt();
			for(Attr attr : rpt.getEnMap().getAttrs())
            {
                  rpt.SetValByKey(attr.getKey(), "0");
            }
			  
			for (Node nd : nds.ToJavaList()) {
				if (nd.getFocusField().trim().equals("")) {
					Work wk = nd.getHisWork();
					String attrKey = "";
					for (Attr attr : wk.getEnMap().getAttrs()) {
						if (attr.getUIVisible() == true && attr.getUIIsDoc() && attr.getUIIsReadonly() == false) {
							attrKey = attr.getDesc() + ":@" + attr.getKey();
						}
					}
					if (attrKey.equals("")) {
						msg += "@璀﹀憡:鑺傜偣ID:" + nd.getNodeID() + " 鍚嶇О:" + nd.getName()
								+ "灞炴�ч噷娌℃湁璁剧疆鐒︾偣瀛楁锛屼細瀵艰嚧淇℃伅鍐欏叆杞ㄨ抗琛ㄧ┖鐧斤紝涓轰簡鑳藉淇濊瘉娴佺▼杞ㄨ抗鏄彲璇荤殑璇疯缃劍鐐瑰瓧娈�.";
					} else {
						msg += "@淇℃伅:鑺傜偣ID:" + nd.getNodeID() + " 鍚嶇О:" + nd.getName()
								+ "灞炴�ч噷娌℃湁璁剧疆鐒︾偣瀛楁锛屼細瀵艰嚧淇℃伅鍐欏叆杞ㄨ抗琛ㄧ┖鐧斤紝涓轰簡鑳藉淇濊瘉娴佺▼杞ㄨ抗鏄彲璇荤殑绯荤粺鑷姩璁剧疆浜嗙劍鐐瑰瓧娈典负" + attrKey + ".";
						nd.setFocusField(attrKey);
						nd.DirectUpdate();
					}
					continue;
				}

				Object tempVar = nd.getFocusField();
				String strs = (String) ((tempVar instanceof String) ? tempVar : null);
				strs = BP.WF.Glo.DealExp(strs, rpt, "err");
				if (strs.contains("@") == true) {
					msg += "@閿欒:鐒︾偣瀛楁锛�" + nd.getFocusField() + "锛夊湪鑺傜偣(step:" + nd.getStep() + " 鍚嶇О:" + nd.getName()
							+ ")灞炴�ч噷鐨勮缃凡鏃犳晥锛岃〃鍗曢噷涓嶅瓨鍦ㄨ瀛楁.";
				} else {
					msg += "@鎻愮ず:鑺傜偣鐨�(" + nd.getNodeID() + "," + nd.getName() + ")鐒︾偣瀛楁锛�" + nd.getFocusField()
							+ "锛夎缃畬鏁存鏌ラ�氳繃.";
				}

				if (this.getIsMD5()) {
					if (nd.getHisWork().getEnMap().getAttrs().Contains(WorkAttr.MD5) == false) {
						nd.RepareMap();
					}
				}
			}
			msg += "@淇℃伅:妫�鏌ヨ妭鐐圭殑鐒︾偣瀛楁瀹屾垚.";
			/// #region 妫�鏌ヨ川閲忚�冩牳鐐�.
			msg += "@淇℃伅:寮�濮嬫鏌ヨ川閲忚�冩牳鐐�";
			for (Node nd : nds.ToJavaList()) {
				if (nd.getIsEval()) {
					// 濡傛灉鏄川閲忚�冩牳鐐癸紝妫�鏌ヨ妭鐐硅〃鍗曟槸鍚﹀叿鍒川閲忚�冩牳鐨勭壒鍒瓧娈碉紵
					String sql = "SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='ND" + nd.getNodeID()
							+ "' AND KeyOfEn IN ('EvalEmpNo','EvalEmpName','EvalEmpCent')";
					if (DBAccess.RunSQLReturnValInt(sql) != 3) {
						msg += "@淇℃伅:鎮ㄨ缃簡鑺傜偣(" + nd.getNodeID() + "," + nd.getName()
								+ ")涓鸿川閲忚�冩牳鑺傜偣锛屼絾鏄偍娌℃湁鍦ㄨ鑺傜偣琛ㄥ崟涓缃繀瑕佺殑鑺傜偣鑰冩牳瀛楁.";
					}
				}
			}
			msg += "@妫�鏌ヨ川閲忚�冩牳鐐瑰畬鎴�.";

			/// #endregion

			msg += "@娴佺▼鎶ヨ〃妫�鏌ュ畬鎴�...";

			// #region 妫�鏌ュ鏋滄槸鍚堟祦鑺傜偣蹇呴』涓嶈兘鏄敱涓婁竴涓妭鐐规寚瀹氭帴鍙椾汉鍛樸�� @dudongliang 闇�瑕佺炕璇�.
			for (Node nd : nds.ToJavaList()) {
				// 濡傛灉鏄悎娴佽妭鐐�.
				if (nd.getHisNodeWorkType() == NodeWorkType.WorkHL || nd.getHisNodeWorkType() == NodeWorkType.WorkFHL) {
					if (nd.getHisDeliveryWay() == DeliveryWay.BySelected)
						msg += "@閿欒:鑺傜偣ID:" + nd.getNodeID() + " 鍚嶇О:" + nd.getName()
								+ "鏄悎娴佹垨鑰呭垎鍚堟祦鑺傜偣锛屼絾鏄鑺傜偣璁剧疆鐨勬帴鏀朵汉瑙勫垯涓虹敱涓婁竴姝ユ寚瀹氾紝杩欐槸閿欒鐨勶紝搴旇涓鸿嚜鍔ㄨ绠楄�岄潪姣忎釜瀛愮嚎绋嬩汉涓虹殑閫夋嫨.";
				}

				// 瀛愮嚎绋嬭妭鐐�
				if (nd.getHisNodeWorkType() == NodeWorkType.SubThreadWork) {
					if (nd.getCondModel() == CondModel.ByUserSelected) {
						Nodes toNodes = nd.getHisToNodes();
						if (toNodes.size() == 1) {
						}
					}
				}
			}
			// #endregion 妫�鏌ュ鏋滄槸鍚堟祦鑺傜偣蹇呴』涓嶈兘鏄敱涓婁竴涓妭鐐规寚瀹氭帴鍙椾汉鍛樸��

			// 妫�鏌ユ祦绋�.
			Node.CheckFlow(this);

			return msg;
		} catch (RuntimeException ex) {
			throw new RuntimeException("@妫�鏌ユ祦绋嬮敊璇�:" + ex.getMessage() + " @" + ex.getStackTrace());
		}
	}

	/**
	 * 鑺傜偣琛ㄥ崟瀛楁鏁版嵁绫诲瀷妫�鏌ワ紝鍚嶅瓧鐩稿悓鐨勫瓧娈靛嚭鐜扮被鍨嬩笉鍚岀殑澶勭悊鏂规硶锛氫緷鐓т笉鍚屼簬NDxxRpt琛ㄤ腑鍚屽悕瀛楁绫诲瀷涓哄熀鍑�
	 * 
	 * @return 妫�鏌ョ粨鏋�
	 * @throws Exception
	 */
	private String CheckFormFields() throws Exception {
		StringBuilder errorAppend = new StringBuilder();
		errorAppend.append("@淇℃伅: -------- 娴佺▼鑺傜偣琛ㄥ崟鐨勫瓧娈电被鍨嬫鏌�: ------ ");
		try {
			Nodes nds = new Nodes(this.getNo());
			String fk_mapdatas = "'ND" + Integer.parseInt(this.getNo()) + "Rpt'";
			for (Node nd : nds.ToJavaList()) {
				fk_mapdatas += ",'ND" + nd.getNodeID() + "'";
			}

			// 绛涢�夊嚭绫诲瀷涓嶅悓鐨勫瓧娈�
			String checkSQL = "SELECT   AA.KEYOFEN, COUNT(*) AS MYNUM FROM ("
					+ "  SELECT A.KEYOFEN,  MYDATATYPE,  COUNT(*) AS MYNUM"
					+ "  FROM SYS_MAPATTR A WHERE FK_MAPDATA IN (" + fk_mapdatas + ") GROUP BY KEYOFEN, MYDATATYPE"
					+ ")  AA GROUP BY  AA.KEYOFEN HAVING COUNT(*) > 1";
			DataTable dt_Fields = DBAccess.RunSQLReturnTable(checkSQL);
			for (DataRow row : dt_Fields.Rows) {
				String keyOfEn = row.getValue("KEYOFEN").toString();
				String myNum = row.getValue("MYNUM").toString();
				int iMyNum = 0;

				// cn.jflow.common.tangible.RefObject<Integer> tempRef_iMyNum =
				// new cn.jflow.common.tangible.RefObject<Integer>(iMyNum);
				// cn.jflow.common.tangible.TryParseHelper.tryParseInt(myNum,
				// tempRef_iMyNum);

				iMyNum = 0;// tempRef_iMyNum.argValue;

				// 瀛樺湪2绉嶄互涓婃暟鎹被鍨嬶紝鏈夋墜鍔ㄨ繘琛岃皟鏁�
				if (iMyNum > 2) {
					errorAppend.append("@閿欒锛氬瓧娈靛悕" + keyOfEn + "鍦ㄦ娴佺▼琛�(" + fk_mapdatas
							+ ")涓瓨鍦�2绉嶄互涓婃暟鎹被鍨�(濡傦細int锛宖loat,varchar,datetime)锛岃鎵嬪姩淇敼銆�");
					return errorAppend.toString();
				}

				// 瀛樺湪2绉嶆暟鎹被鍨嬶紝浠ヤ笉鍚屼簬NDxxRpt瀛楁绫诲瀷涓轰富
				MapAttr baseMapAttr = new MapAttr();
				MapAttr rptMapAttr = new MapAttr("ND" + Integer.parseInt(this.getNo()) + "Rpt", keyOfEn);

				// Rpt琛ㄤ腑涓嶅瓨鍦ㄦ瀛楁
				if (rptMapAttr == null || rptMapAttr.getMyPK().equals("")) {
					this.DoCheck_CheckRpt(this.getHisNodes());
					rptMapAttr = new MapAttr("ND" + Integer.parseInt(this.getNo()) + "Rpt", keyOfEn);
					this.getHisGERpt().CheckPhysicsTable();
				}

				// Rpt琛ㄤ腑涓嶅瓨鍦ㄦ瀛楁,鐩存帴缁撴潫
				if (rptMapAttr == null || rptMapAttr.getMyPK().equals("")) {
					continue;
				}

				for (Node nd : nds.ToJavaList()) {
					MapAttr ndMapAttr = new MapAttr("ND" + nd.getNodeID(), keyOfEn);
					if (ndMapAttr == null || ndMapAttr.getMyPK().equals("")) {
						continue;
					}

					// 鎵惧嚭涓嶯DxxRpt琛ㄤ腑瀛楁鏁版嵁绫诲瀷涓嶅悓鐨勮〃鍗�
					if (rptMapAttr.getMyDataType() != ndMapAttr.getMyDataType()) {
						baseMapAttr = ndMapAttr;
						break;
					}
				}
				errorAppend.append("@鍩虹琛�" + baseMapAttr.getFK_MapData() + "锛屽瓧娈�" + keyOfEn + "鏁版嵁绫诲瀷涓猴細"
						+ baseMapAttr.getMyDataTypeStr());
				// 鏍规嵁鍩虹灞炴�х被淇敼鏁版嵁绫诲瀷涓嶅悓鐨勮〃鍗�
				for (Node nd : nds.ToJavaList()) {
					MapAttr ndMapAttr = new MapAttr("ND" + nd.getNodeID(), keyOfEn);
					// 涓嶅寘鍚瀛楁鐨勮繘琛岃繑鍥�,绫诲瀷鐩稿悓鐨勮繘琛岃繑鍥�
					if (ndMapAttr == null || ndMapAttr.getMyPK().equals("")
							|| baseMapAttr.getMyPK() == ndMapAttr.getMyPK()
							|| baseMapAttr.getMyDataType() == ndMapAttr.getMyDataType()) {
						continue;
					}
					ndMapAttr.setName(baseMapAttr.getName());
					ndMapAttr.setMyDataType(baseMapAttr.getMyDataType());
					ndMapAttr.setUIWidth(baseMapAttr.getUIWidth());
					ndMapAttr.setUIHeight(baseMapAttr.getUIHeight());
					ndMapAttr.setMinLen(baseMapAttr.getMinLen());
					ndMapAttr.setMaxLen(baseMapAttr.getMaxLen());
					if (ndMapAttr.Update() > 0) {
						errorAppend.append("@淇敼浜�" + "ND" + nd.getNodeID() + " 琛紝瀛楁" + keyOfEn + "淇敼涓猴細"
								+ baseMapAttr.getMyDataTypeStr());
					} else {
						errorAppend.append("@閿欒:淇敼" + "ND" + nd.getNodeID() + " 琛紝瀛楁" + keyOfEn + "淇敼涓猴細"
								+ baseMapAttr.getMyDataTypeStr() + "澶辫触銆�");
					}
				}
				// 淇敼NDxxRpt
				rptMapAttr.setName(baseMapAttr.getName());
				rptMapAttr.setMyDataType(baseMapAttr.getMyDataType());
				rptMapAttr.setUIWidth(baseMapAttr.getUIWidth());
				rptMapAttr.setUIHeight(baseMapAttr.getUIHeight());
				rptMapAttr.setMinLen(baseMapAttr.getMinLen());
				rptMapAttr.setMaxLen(baseMapAttr.getMaxLen());
				if (rptMapAttr.Update() > 0) {
					errorAppend.append("@淇敼浜�" + "ND" + Integer.parseInt(this.getNo()) + "Rpt 琛紝瀛楁" + keyOfEn + "淇敼涓猴細"
							+ baseMapAttr.getMyDataTypeStr());
				} else {
					errorAppend.append("@閿欒:淇敼" + "ND" + Integer.parseInt(this.getNo()) + "Rpt 琛紝瀛楁" + keyOfEn + "淇敼涓猴細"
							+ baseMapAttr.getMyDataTypeStr() + "澶辫触銆�");
				}
			}
		} catch (RuntimeException ex) {
			errorAppend.append("@閿欒:" + ex.getMessage());
		}
		return errorAppend.toString();
	}

	/// #region 浜х敓鏁版嵁妯℃澘銆�
	private static String PathFlowDesc;
	static {
		PathFlowDesc = SystemConfig.getPathOfDataUser() + "FlowDesc/";
	}

	/**
	 * 鐢熸垚娴佺▼妯℃澘
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String GenerFlowXmlTemplete() throws Exception {
		String name = this.getName();
		name = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(name);

		String path = this.getNo() + "." + name;
		path = PathFlowDesc + path + "\\";
		this.DoExpFlowXmlTemplete(path);

		// name = path + name + "." + this.Ver.replace(":", "_") + ".xml";

		name = path + name + ".xml";
		return name;
	}

	/**
	 * 鐢熸垚娴佺▼妯℃澘
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public final DataSet DoExpFlowXmlTemplete(String path) throws Exception {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		DataSet ds = GetFlow(path);
		if (ds != null) {
			// String filePath = path + this.getName() + "_" +
			// this.getVer().replace(":", "_") + ".xml";
			String filePath = path + this.getName() + ".xml";
			try {
				ds.WriteXml(filePath, XmlWriteMode.WriteSchema, ds);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return ds;
	}

	// xml鏂囦欢鏄惁姝ｅ湪鎿嶄綔涓�
	private static boolean isXmlLocked;

	/**
	 * 澶囦唤褰撳墠娴佺▼鍒扮敤鎴穢ml鏂囦欢 鐢ㄦ埛姣忔淇濆瓨鏃惰皟鐢� 鎹曡幏寮傚父鍐欏叆鏃ュ織,澶囦唤澶辫触涓嶅奖鍝嶆甯镐繚瀛�
	 */
	public final void WriteToXml(DataSet ds) {
		String path = PathFlowDesc + this.getNo() + "." + this.getName() + "/";
		String xmlName = path + "Flow" + ".xml";
		File file = new File(path);
		File filexmlName = new File(xmlName);
		if (!StringHelper.isNullOrEmpty(path)) {
			if (!file.exists()) {
				try {
					file.mkdirs();
				} catch (Exception e) {

					e.printStackTrace();
				}
			} else if (filexmlName.exists()) {
				long modifiedTime = filexmlName.lastModified();
				Date date = new Date(modifiedTime);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String dd = sdf.format(date);
				String xmlNameOld = path + "Flow" + dd + ".xml";
				File filexmlNameOld = new File(xmlNameOld);
				if (filexmlNameOld.exists()) {
					filexmlNameOld.delete();
				}
				FileAccess.Move(filexmlName, xmlNameOld);
				// File.move(xmlName, xmlNameOld);
			}
		}

		try {
			if (!StringHelper.isNullOrEmpty(xmlName) && null != ds) {
				ds.WriteXml(xmlName, XmlWriteMode.WriteSchema, ds);
			}
		} catch (RuntimeException e) {
			isXmlLocked = false;
			BP.DA.Log.DefaultLogWriteLineError("娴佺▼妯℃澘鏂囦欢澶囦唤閿欒:" + e.getMessage());
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public DataSet GetFlow(String path) throws Exception {
		// 鎶婃墍鏈夌殑鏁版嵁閮藉瓨鍌ㄥ湪杩欓噷銆�
		DataSet ds = new DataSet();

		// 娴佺▼淇℃伅銆�
		String sql = "SELECT * FROM WF_Flow WHERE No='" + this.getNo() + "'";

		Flow fl = new Flow(this.getNo());
		DataTable dtFlow = fl.ToDataTableField("WF_Flow");
		dtFlow.TableName = "WF_Flow";
		ds.Tables.add(dtFlow);

		// 鑺傜偣淇℃伅
		Nodes nds = new Nodes(this.getNo());
		DataTable dtNodes = nds.ToDataTableField("WF_Node");
		ds.Tables.add(dtNodes);
		
		//鑺傜偣灞炴��
        NodeExts ndexts = new NodeExts(this.getNo());
        DataTable dtNodeExts = ndexts.ToDataTableField("WF_NodeExt");
        ds.Tables.add(dtNodeExts);
		
		//鎺ユ敹浜鸿鍒�
        Selectors selectors = new Selectors(this.getNo());
		DataTable dtSelectors = selectors.ToDataTableField("WF_Selector");
		ds.Tables.add(dtSelectors);

		// 鍗曟嵁妯＄増.
		BillTemplates tmps = new BillTemplates(this.getNo());
		String pks = "";
		for (BillTemplate tmp : tmps.ToJavaList()) {
			try {
				if (path != null)
					FileAccess.Copy(SystemConfig.getPathOfDataUser() + "CyclostyleFile/" + tmp.getNo() + ".rtf",
							path + "/" + tmp.getNo() + ".rtf");
			} catch (java.lang.Exception e) {
				pks += "@" + tmp.getPKVal();
				tmp.Delete();
			}
		}
		tmps.remove(pks);
		ds.Tables.add(tmps.ToDataTableField("WF_BillTemplate"));

		String sqlin = "SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo() + "'";

		// 鏉′欢淇℃伅
		Conds cds = new Conds(this.getNo());
		ds.Tables.add(cds.ToDataTableField("WF_Cond"));

		//// 杞悜瑙勫垯.
		// sql = "SELECT * FROM WF_TurnTo WHERE FK_Flow='" + this.No + "'";
		// dt = DBAccess.RunSQLReturnTable(sql);
		// dt.TableName = "WF_TurnTo";
		// ds.Tables.Add(dt);

		// 鑺傜偣涓庤〃鍗曠粦瀹�.
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
		ds.Tables.add(fns.ToDataTableField("WF_FrmNode"));

		// 琛ㄥ崟鏂规.
		FrmFields ffs = new FrmFields();
		ffs.Retrieve(FrmFieldAttr.FK_Flow, this.getNo());
		ds.Tables.add(ffs.ToDataTableField("Sys_FrmSln"));

		// 鏂瑰悜
		Directions dirs = new Directions();
		dirs.Retrieve(DirectionAttr.FK_Flow, this.getNo());
		ds.Tables.add(dirs.ToDataTableField("WF_Direction"));

		// 娴佺▼鏍囩.
		LabNotes labs = new LabNotes(this.getNo());
		ds.Tables.add(labs.ToDataTableField("WF_LabNote"));

		// 鍙��鍥炵殑鑺傜偣銆�
		NodeReturns nrs = new NodeReturns();
		nrs.RetrieveInSQL(NodeReturnAttr.FK_Node, sqlin);
		ds.Tables.add(nrs.ToDataTableField("WF_NodeReturn"));

		// 宸ュ叿鏍忋��
		NodeToolbars tools = new NodeToolbars();
		tools.RetrieveInSQL(NodeToolbarAttr.FK_Node, sqlin);
		ds.Tables.add(tools.ToDataTableField("WF_NodeToolbar"));

		// 鑺傜偣涓庨儴闂ㄣ��
		NodeDepts ndepts = new NodeDepts();
		ndepts.RetrieveInSQL(NodeDeptAttr.FK_Node, sqlin);
		ds.Tables.add(ndepts.ToDataTableField("WF_NodeDept"));

		// 鑺傜偣涓庡矖浣嶆潈闄愩��
		NodeStations nss = new NodeStations();
		nss.RetrieveInSQL(NodeStationAttr.FK_Node, sqlin);
		ds.Tables.add(nss.ToDataTableField("WF_NodeStation"));

		// 鑺傜偣涓庝汉鍛樸��
		NodeEmps nes = new NodeEmps();
		nes.RetrieveInSQL(NodeEmpAttr.FK_Node, sqlin);
		ds.Tables.add(nes.ToDataTableField("WF_NodeEmp"));

		// 鎶勯�佷汉鍛樸��
		CCEmps ces = new CCEmps();
		ces.RetrieveInSQL(CCEmpAttr.FK_Node, sqlin);
		ds.Tables.add(ces.ToDataTableField("WF_CCEmp"));

		// 鎶勯�侀儴闂ㄣ��
		CCDepts cdds = new CCDepts();
		cdds.RetrieveInSQL(CCDeptAttr.FK_Node, sqlin);
		ds.Tables.add(cdds.ToDataTableField("WF_CCDept"));

		// 寤剁画瀛愭祦绋嬨��
		NodeYGFlows fls = new NodeYGFlows();
		fls.RetrieveInSQL(CCDeptAttr.FK_Node, sqlin);
		ds.Tables.add(fls.ToDataTableField("WF_NodeSubFlow"));

		// 琛ㄥ崟淇℃伅锛屽寘鍚粠琛�.
		sql = "SELECT No FROM Sys_MapData WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "No");
		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL(MapDataAttr.No, sql);
		ds.Tables.add(mds.ToDataTableField("Sys_MapData"));

		// Sys_MapAttr.
		sql = "SELECT MyPK FROM Sys_MapAttr WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		sql += " UNION "; // 澧炲姞澶氶檮浠剁殑鎵╁睍鍒�.
		sql += "SELECT MyPK FROM Sys_MapAttr WHERE FK_MapData IN ( SELECT MyPK FROM Sys_FrmAttachment WHERE FK_Node=0 AND "
				+ BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ) ";

		// sql = "SELECT MyPK FROM Sys_MapAttr WHERE " +
		// Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		MapAttrs attrs = new MapAttrs();
		attrs.RetrieveInSQL(MapAttrAttr.MyPK, sql);
		ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));

		// Sys_EnumMain
		sql = "SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE "
				+ BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + ")";
		SysEnumMains ses = new SysEnumMains();
		ses.RetrieveInSQL(SysEnumMainAttr.No, sql);
		ds.Tables.add(ses.ToDataTableField("Sys_EnumMain"));

		// Sys_Enum
		sql = "SELECT MyPK FROM Sys_Enum WHERE EnumKey IN ( SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE "
				+ BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ) )";
		SysEnums sesDtl = new SysEnums();
		sesDtl.RetrieveInSQL("MyPK", sql);
		ds.Tables.add(sesDtl.ToDataTableField("Sys_Enum"));

		// Sys_MapDtl
		sql = "SELECT No FROM Sys_MapDtl WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		MapDtls mdtls = new MapDtls();
		mdtls.RetrieveInSQL(sql);
		ds.Tables.add(mdtls.ToDataTableField("Sys_MapDtl"));

		// Sys_MapExt
		sql = "SELECT MyPK FROM Sys_MapExt WHERE  " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		MapExts mexts = new MapExts();
		mexts.RetrieveInSQL(sql);
		ds.Tables.add(mexts.ToDataTableField("Sys_MapExt"));

		// Sys_GroupField
		sql = "SELECT OID FROM Sys_GroupField WHERE   " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FrmID"); // +"
																											// "
																											// +
																											// Glo.MapDataLikeKey(this.No,
																											// "EnName");
		GroupFields gfs = new GroupFields();
		gfs.RetrieveInSQL(sql);
		ds.Tables.add(gfs.ToDataTableField("Sys_GroupField"));

		// Sys_MapFrame
		sql = "SELECT MyPK FROM Sys_MapFrame WHERE" + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		MapFrames mfs = new MapFrames();
		mfs.RetrieveInSQL("MyPK", sql);
		ds.Tables.add(mfs.ToDataTableField("Sys_MapFrame"));

		// Sys_FrmLine.
		sql = "SELECT MyPK FROM Sys_FrmLine WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		FrmLines frmls = new FrmLines();
		frmls.RetrieveInSQL(sql);
		ds.Tables.add(frmls.ToDataTableField("Sys_FrmLine"));

		// Sys_FrmLab.
		sql = "SELECT MyPK FROM Sys_FrmLab WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		FrmLabs frmlabs = new FrmLabs();
		frmlabs.RetrieveInSQL(sql);
		ds.Tables.add(frmlabs.ToDataTableField("Sys_FrmLab"));

		// Sys_FrmEle.
		sql = "SELECT MyPK FROM Sys_FrmEle WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");

		FrmEles frmEles = new FrmEles();
		frmEles.RetrieveInSQL(sql);
		ds.Tables.add(frmEles.ToDataTableField("Sys_FrmEle"));

		// Sys_FrmLink.
		sql = "SELECT MyPK FROM Sys_FrmLink WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		FrmLinks frmLinks = new FrmLinks();
		frmLinks.RetrieveInSQL(sql);
		ds.Tables.add(frmLinks.ToDataTableField("Sys_FrmLink"));

		// Sys_FrmRB.
		sql = "SELECT MyPK FROM Sys_FrmRB WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		FrmRBs frmRBs = new FrmRBs();
		frmRBs.RetrieveInSQL(sql);
		ds.Tables.add(frmRBs.ToDataTableField("Sys_FrmRB"));

		// Sys_FrmImgAth.
		sql = "SELECT MyPK FROM Sys_FrmImgAth WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		FrmImgAths frmIs = new FrmImgAths();
		frmIs.RetrieveInSQL(sql);
		ds.Tables.add(frmIs.ToDataTableField("Sys_FrmImgAth"));

		// Sys_FrmImg.
		sql = "SELECT MyPK FROM Sys_FrmImg WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		FrmImgs frmImgs = new FrmImgs();
		frmImgs.RetrieveInSQL(sql);
		ds.Tables.add(frmImgs.ToDataTableField("Sys_FrmImg"));

		// Sys_FrmAttachment.
		sql = "SELECT MyPK FROM Sys_FrmAttachment WHERE FK_Node=0 AND "
				+ BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		FrmAttachments frmaths = new FrmAttachments();
		frmaths.RetrieveInSQL(sql);
		ds.Tables.add(frmaths.ToDataTableField("Sys_FrmAttachment"));

		// Sys_FrmEvent.
		sql = "SELECT MyPK FROM Sys_FrmEvent WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		FrmEvents frmevens = new FrmEvents();
		frmevens.RetrieveInSQL(sql);
		ds.Tables.add(frmevens.ToDataTableField("Sys_FrmEvent"));
		return ds;
	}

	public final DataSet GetFlow2017(String path) throws Exception {
		// 鎶婃墍鏈夌殑鏁版嵁閮藉瓨鍌ㄥ湪杩欓噷銆�
		DataSet ds = new DataSet();

		// 娴佺▼淇℃伅銆�
		String sql = "SELECT * FROM WF_Flow WHERE No='" + this.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Flow";
		ds.Tables.add(dt);

		// 鑺傜偣淇℃伅
		sql = "SELECT * FROM WF_Node WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Node";
		ds.Tables.add(dt);

		// 鍗曟嵁妯＄増.
		BillTemplates tmps = new BillTemplates(this.getNo());
		String pks = "";
		for (BillTemplate tmp : tmps.ToJavaList()) {
			try {
				if (path != null) {
					// System.IO.File.Copy(SystemConfig.getPathOfDataUser()
					// + "\\CyclostyleFile\\" + tmp.getNo() + ".rtf", path
					// + "\\" + tmp.getNo() + ".rtf", true);
					FileAccess.Copy(SystemConfig.getPathOfDataUser() + "CyclostyleFile/" + tmp.getNo() + ".rtf",
							path + "/" + tmp.getNo() + ".rtf");
				}
			} catch (java.lang.Exception e) {
				pks += "@" + tmp.getPKVal();
				tmp.Delete();
			}
		}
		tmps.remove(pks);
		ds.Tables.add(tmps.ToDataTableField("WF_BillTemplate"));

		String sqlin = "SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo() + "'";
		// 鐙珛琛ㄥ崟鏍�
		sql = "SELECT * FROM WF_FlowFormTree WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_FlowFormTree";
		ds.Tables.add(dt);

		//// 鐙珛琛ㄥ崟
		// sql = "SELECT * FROM WF_FlowForm WHERE FK_Flow='" + this.getNo() +
		//// "'";
		// dt = DBAccess.RunSQLReturnTable(sql);
		// dt.TableName = "WF_FlowForm";
		// ds.Tables.Add(dt);

		//// 鑺傜偣琛ㄥ崟鏉冮檺
		// sql = "SELECT * FROM WF_NodeForm WHERE FK_Node IN (" + sqlin + ")";
		// dt = DBAccess.RunSQLReturnTable(sql);
		// dt.TableName = "WF_NodeForm";
		// ds.Tables.Add(dt);

		// 鏉′欢淇℃伅
		sql = "SELECT * FROM WF_Cond WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Cond";
		ds.Tables.add(dt);

		// 杞悜瑙勫垯.
		sql = "SELECT * FROM WF_TurnTo WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_TurnTo";
		ds.Tables.add(dt);

		// 鑺傜偣涓庤〃鍗曠粦瀹�.
		sql = "SELECT * FROM WF_FrmNode WHERE FK_Flow='" + this.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_FrmNode";
		ds.Tables.add(dt);

		// 琛ㄥ崟鏂规.
		sql = "SELECT * FROM Sys_FrmSln WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmSln";
		ds.Tables.add(dt);

		// 鏂瑰悜
		sql = "SELECT * FROM WF_Direction WHERE Node IN (" + sqlin + ") OR ToNode In (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Direction";
		ds.Tables.add(dt);

		//// 搴旂敤璁剧疆 FAppSet
		// sql = "SELECT * FROM WF_FAppSet WHERE FK_Flow='" + this.getNo() +
		//// "'";
		// dt = DBAccess.RunSQLReturnTable(sql);
		// dt.TableName = "WF_FAppSet";
		// ds.Tables.Add(dt);

		// 娴佺▼鏍囩.
		LabNotes labs = new LabNotes(this.getNo());
		ds.Tables.add(labs.ToDataTableField("WF_LabNote"));

		// 鍙��鍥炵殑鑺傜偣銆�
		sql = "SELECT * FROM WF_NodeReturn WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeReturn";
		ds.Tables.add(dt);

		// 宸ュ叿鏍忋��
		sql = "SELECT * FROM WF_NodeToolbar WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeToolbar";
		ds.Tables.add(dt);

		// 鑺傜偣涓庨儴闂ㄣ��
		sql = "SELECT * FROM WF_NodeDept WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeDept";
		ds.Tables.add(dt);

		// 鑺傜偣涓庡矖浣嶆潈闄愩��
		sql = "SELECT * FROM WF_NodeStation WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeStation";
		ds.Tables.add(dt);

		// 鑺傜偣涓庝汉鍛樸��
		sql = "SELECT * FROM WF_NodeEmp WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeEmp";
		ds.Tables.add(dt);

		// 鎶勯�佷汉鍛樸��
		sql = "SELECT * FROM WF_CCEmp WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCEmp";
		ds.Tables.add(dt);

		// 鎶勯�侀儴闂ㄣ��
		sql = "SELECT * FROM WF_CCDept WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCDept";
		ds.Tables.add(dt);

		// 鎶勯�侀儴闂ㄣ��
		sql = "SELECT * FROM WF_CCStation WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_CCStation";
		ds.Tables.add(dt);

		// 寤剁画瀛愭祦绋嬨��
		sql = "SELECT * FROM WF_NodeSubFlow WHERE FK_Node IN (" + sqlin + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_NodeSubFlow";
		ds.Tables.add(dt);

		//// 娴佺▼鎶ヨ〃銆�
		// WFRpts rpts = new WFRpts(this.getNo());
		//// rpts.SaveToXml(path + "WFRpts.xml");
		// ds.Tables.Add(rpts.ToDataTableField("WF_Rpt"));

		//// 娴佺▼鎶ヨ〃灞炴��
		// RptAttrs rptAttrs = new RptAttrs();
		// rptAttrs.RetrieveAll();
		// ds.Tables.Add(rptAttrs.ToDataTableField("RptAttrs"));

		//// 娴佺▼鎶ヨ〃璁块棶鏉冮檺銆�
		// RptStations rptStations = new RptStations(this.getNo());
		// rptStations.RetrieveAll();
		//// rptStations.SaveToXml(path + "RptStations.xml");
		// ds.Tables.Add(rptStations.ToDataTableField("RptStations"));

		//// 娴佺▼鎶ヨ〃浜哄憳璁块棶鏉冮檺銆�
		// RptEmps rptEmps = new RptEmps(this.getNo());
		// rptEmps.RetrieveAll();

		// rptEmps.SaveToXml(path + "RptEmps.xml");
		// ds.Tables.Add(rptEmps.ToDataTableField("RptEmps"));

		int flowID = Integer.parseInt(this.getNo());
		sql = "SELECT * FROM Sys_MapData WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "No");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		ds.Tables.add(dt);

		// Sys_MapAttr.
		sql = "SELECT * FROM Sys_MapAttr WHERE  " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData")
				+ " ORDER BY FK_MapData,Idx";
		// sql = "SELECT * FROM Sys_MapAttr WHERE " +
		// Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ORDER BY
		// FK_MapData,Idx";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		ds.Tables.add(dt);

		// Sys_EnumMain
		// sql = "SELECT * FROM Sys_EnumMain WHERE No IN (SELECT KeyOfEn from
		// Sys_MapAttr WHERE " + Glo.MapDataLikeKey(this.getNo(), "FK_MapData")
		// +")";
		sql = "SELECT * FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE "
				+ BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_EnumMain";
		ds.Tables.add(dt);

		// Sys_Enum
		sql = "SELECT * FROM Sys_Enum WHERE EnumKey IN ( SELECT No FROM Sys_EnumMain WHERE No IN (SELECT UIBindKey from Sys_MapAttr WHERE "
				+ BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData") + " ) )";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_Enum";
		ds.Tables.add(dt);

		// Sys_MapDtl
		sql = "SELECT * FROM Sys_MapDtl WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapDtl";
		ds.Tables.add(dt);

		// Sys_MapExt
		// sql = "SELECT * FROM Sys_MapExt WHERE " +
		// Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		sql = "SELECT * FROM Sys_MapExt WHERE  " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData"); // +Glo.MapDataLikeKey(this.getNo(),
																											// "FK_MapData");

		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		// Sys_GroupField
		sql = "SELECT * FROM Sys_GroupField WHERE   " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FrmID"); // +"
																											// "
																											// +
																											// Glo.MapDataLikeKey(this.getNo(),
																											// "EnName");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_GroupField";
		ds.Tables.add(dt);

		// Sys_MapFrame
		sql = "SELECT * FROM Sys_MapFrame WHERE" + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapFrame";
		ds.Tables.add(dt);

		// Sys_MapM2M
		// sql = "SELECT * FROM Sys_MapM2M WHERE " +
		// Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		// dt = DBAccess.RunSQLReturnTable(sql);
		// dt.TableName = "Sys_MapM2M";
		// ds.Tables.add(dt);

		// Sys_FrmLine.
		sql = "SELECT * FROM Sys_FrmLine WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLine";
		ds.Tables.add(dt);

		// Sys_FrmLab.
		sql = "SELECT * FROM Sys_FrmLab WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLab";
		ds.Tables.add(dt);

		// Sys_FrmEle.
		sql = "SELECT * FROM Sys_FrmEle WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmEle";
		ds.Tables.add(dt);

		// Sys_FrmLink.
		sql = "SELECT * FROM Sys_FrmLink WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmLink";
		ds.Tables.add(dt);

		// Sys_FrmRB.
		sql = "SELECT * FROM Sys_FrmRB WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmRB";
		ds.Tables.add(dt);

		// Sys_FrmImgAth.
		sql = "SELECT * FROM Sys_FrmImgAth WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmImgAth";
		ds.Tables.add(dt);

		// Sys_FrmImg.
		sql = "SELECT * FROM Sys_FrmImg WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmImg";
		ds.Tables.add(dt);

		// Sys_FrmAttachment.
		sql = "SELECT * FROM Sys_FrmAttachment WHERE FK_Node=0 AND "
				+ BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmAttachment";
		ds.Tables.add(dt);

		// Sys_FrmEvent.
		sql = "SELECT * FROM Sys_FrmEvent WHERE " + BP.WF.Glo.MapDataLikeKey(this.getNo(), "FK_MapData");
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FrmEvent";
		ds.Tables.add(dt);
		return ds;
	}

	/// #endregion 浜х敓鏁版嵁妯℃澘銆�

	/// #region 鍏朵粬鍏敤鏂规硶1
	/**
	 * 閲嶆柊璁剧疆Rpt琛�
	 * 
	 * @throws Exception
	 * 
	 */
	public final void CheckRptOfReset() throws Exception {
		String fk_mapData = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "'";
		DBAccess.RunSQL(sql);

		sql = "DELETE FROM Sys_MapData WHERE No='" + fk_mapData + "'";
		DBAccess.RunSQL(sql);
		this.DoCheck_CheckRpt(this.getHisNodes());
	}

	/**
	 * 閲嶆柊瑁呰浇
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DoReloadRptData() throws Exception {
		this.DoCheck_CheckRpt(this.getHisNodes());

		// 妫�鏌ユ姤琛ㄦ暟鎹槸鍚︿涪澶便��

		if (this.getHisDataStoreModel() != DataStoreModel.ByCCFlow) {
			return "@娴佺▼" + this.getNo() + this.getName() + "鐨勬暟鎹瓨鍌ㄩ潪杞ㄨ抗妯″紡涓嶈兘閲嶆柊鐢熸垚.";
		}

		DBAccess.RunSQL("DELETE FROM " + this.getPTable());

		String sql = "SELECT OID FROM ND" + Integer.parseInt(this.getNo()) + "01 WHERE  OID NOT IN (SELECT OID FROM  "
				+ this.getPTable() + " ) ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		this.CheckRptData(this.getHisNodes(), dt);

		return "@鍏辨湁:" + dt.Rows.size() + "鏉�(" + this.getName() + ")鏁版嵁琚杞芥垚鍔熴��";
	}

	/**
	 * 妫�鏌ヤ笌淇鎶ヨ〃鏁版嵁
	 * 
	 * @param nds
	 * @param dt
	 * @throws Exception
	 */
	private String CheckRptData(Nodes nds, DataTable dt) throws Exception {
		GERpt rpt = new GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
		String err = "";
		for (DataRow dr : dt.Rows) {
			rpt.ResetDefaultVal();
			int oid = Integer.parseInt(dr.getValue(0).toString());
			rpt.SetValByKey("OID", oid);
			Work startWork = null;
			Work endWK = null;
			String flowEmps = "";
			for (Node nd : nds.ToJavaList()) {
				try {
					Work wk = nd.getHisWork();
					wk.setOID(oid);
					if (wk.RetrieveFromDBSources() == 0) {
						continue;
					}

					rpt.Copy(wk);
					if (nd.getNodeID() == Integer.parseInt(this.getNo() + "01")) {
						startWork = wk;
					}

					try {
						if (flowEmps.contains("@" + wk.getRec() + ",")) {
							continue;
						}

						flowEmps += "@" + wk.getRec() + "," + wk.getRecOfEmp().getName();
					} catch (java.lang.Exception e) {
					}
					endWK = wk;
				} catch (RuntimeException ex) {
					err += ex.getMessage();
				}
			}

			if (startWork == null || endWK == null) {
				continue;
			}

			rpt.SetValByKey("OID", oid);
			rpt.setFK_NY(startWork.GetValStrByKey("RDT").substring(0, 7));
			rpt.setFK_Dept(startWork.GetValStrByKey("FK_Dept"));
			if (StringHelper.isNullOrEmpty(rpt.getFK_Dept())) {
				String fk_dept = DBAccess
						.RunSQLReturnString("SELECT FK_Dept FROM Port_Emp WHERE No='" + startWork.getRec() + "'");
				rpt.setFK_Dept(fk_dept);

				startWork.SetValByKey("FK_Dept", fk_dept);
				startWork.Update();
			}
			rpt.setTitle(startWork.GetValStrByKey("Title"));
			String wfState = DBAccess
					.RunSQLReturnStringIsNull("SELECT WFState FROM WF_GenerWorkFlow WHERE WorkID=" + oid, "1");
			rpt.setWFState(WFState.forValue(Integer.parseInt(wfState)));
			rpt.setFlowStarter(startWork.getRec());
			rpt.setFlowStartRDT(startWork.getRDT());
			rpt.setFID(startWork.GetValIntByKey("FID"));
			rpt.setFlowEmps(flowEmps);
			rpt.setFlowEnder(endWK.getRec());
			rpt.setFlowEnderRDT(endWK.getRDT());
			rpt.setFlowEndNode(endWK.getNodeID());
			rpt.setMyNum(1);

			// 淇鏍囬瀛楁銆�
			WorkNode wn = new WorkNode(startWork, this.getHisStartNode());
			rpt.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this, startWork));
			try {
				/*
				 * TimeSpan ts = endWK.getRDT_DateTime() -
				 * startWork.getRDT_DateTime(); rpt.setFlowDaySpan(ts.Days);
				 */
				int day = (int) ((endWK.getRDT_DateTime().getTime() - startWork.getRDT_DateTime().getTime())
						/ (1000 * 60 * 60 * 24));
				rpt.setFlowDaySpan(day);
			} catch (java.lang.Exception e2) {
			}
			rpt.InsertAsOID(rpt.getOID());
		} // 缁撴潫寰幆銆�
		return err;
	}

	/**
	 * 鐢熸垚鏄庣粏鎶ヨ〃淇℃伅
	 * 
	 * @param nds
	 * @throws Exception
	 */
	private void CheckRptDtl(Nodes nds) throws Exception {
		MapDtls dtlsDtl = new MapDtls();
		dtlsDtl.Retrieve(MapDtlAttr.FK_MapData, "ND" + Integer.parseInt(this.getNo()) + "Rpt");
		for (MapDtl dtl : dtlsDtl.ToJavaList()) {
			dtl.Delete();
		}

		// dtlsDtl.Delete(MapDtlAttr.FK_MapData, "ND" + int.Parse(this.getNo())
		// + "Rpt");
		for (Node nd : nds.ToJavaList()) {
			if (nd.getIsEndNode() == false) {
				continue;
			}

			// 鍙栧嚭鏉ヤ粠琛�.
			MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
			if (dtls.size() == 0) {
				continue;
			}

			String rpt = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
			int i = 0;
			for (MapDtl dtl : dtls.ToJavaList()) {
				i++;
				String rptDtlNo = "ND" + Integer.parseInt(this.getNo()) + "RptDtl" + (new Integer(i)).toString();
				MapDtl rtpDtl = new MapDtl();
				rtpDtl.setNo(rptDtlNo);
				if (rtpDtl.RetrieveFromDBSources() == 0) {
					rtpDtl.Copy(dtl);
					rtpDtl.setNo(rptDtlNo);
					rtpDtl.setFK_MapData(rpt);
					rtpDtl.setPTable(rptDtlNo);
					rtpDtl.Insert();
				}

				MapAttrs attrsRptDtl = new MapAttrs(rptDtlNo);
				MapAttrs attrs = new MapAttrs(dtl.getNo());
				for (MapAttr attr : attrs.ToJavaList()) {
					if (attrsRptDtl.Contains(MapAttrAttr.KeyOfEn, attr.getKeyOfEn()) == true) {
						continue;
					}

					MapAttr attrN = new MapAttr();
					attrN.Copy(attr);
					attrN.setFK_MapData(rptDtlNo);
					String keyOfEn = attr.getKeyOfEn();

					// switch (attr.KeyOfEn)
					if (keyOfEn.equals("FK_NY")) {
						attrN.setUIVisible(true);
						attrN.setIdx(100);
						attrN.setUIWidth(60);
					} else if (keyOfEn.equals("RDT")) {
						attrN.setUIVisible(true);
						attrN.setIdx(100);
						attrN.setUIWidth(60);
					} else if (keyOfEn.equals("Rec")) {
						attrN.setUIVisible(true);
						attrN.setIdx(100);
						attrN.setUIWidth(60);
					}
					attrN.Save();
				}
				GEDtl geDtl = new GEDtl(rptDtlNo);
				geDtl.CheckPhysicsTable();
			}
		}
	}

	/**
	 * 妫�鏌ユ暟鎹姤琛�.
	 * 
	 * @param nds
	 * @throws Exception
	 */
	private void DoCheck_CheckRpt(Nodes nds) throws Exception {
		String fk_mapData = "ND" + Integer.parseInt(this.getNo()) + "Rpt";
		String flowId = String.valueOf(Integer.parseInt(this.getNo()));

		// 澶勭悊track琛�.
		Track.CreateOrRepairTrackTable(flowId);

		/// #region 鎻掑叆瀛楁銆�
		String sql = "";
		switch (SystemConfig.getAppCenterDBType()) {
		case Oracle:
			sql = "SELECT distinct  KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' "
					+ SystemConfig.getAppCenterDBAddStringStr()
					+ " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo()
					+ "' AND Sys_MapAttr.FK_MapData = 'ND' || CAST(WF_Node.NodeID AS VARCHAR(20)))";
			break;
		case MSSQL:

			sql = "SELECT distinct  KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' + cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='"
					+ this.getNo() + "' AND Sys_MapAttr.FK_MapData = 'ND' + CAST(WF_Node.NodeID AS VARCHAR(20)))";
			break;
		case Informix:
			// sql = "SELECT distinct KeyOfEn FROM Sys_MapAttr WHERE FK_MapData
			// IN ( SELECT 'ND' " + SystemConfig.getAppCenterDBAddStringStr() +
			// " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" +
			// this.getNo() + "')";
			sql = "SELECT distinct KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' "
					+ SystemConfig.getAppCenterDBAddStringStr()
					+ " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo()
					+ "' AND Sys_MapAttr.FK_MapData = 'ND' || CAST(WF_Node.NodeID AS VARCHAR(20)))";
			break;
		case MySQL:
			// sql = "SELECT DISTINCT KeyOfEn FROM Sys_MapAttr WHERE FK_MapData
			// IN (SELECT X.No FROM ( SELECT CONCAT('ND',NodeID) AS No FROM
			// WF_Node WHERE FK_Flow='" + this.getNo() + "') AS X )";
			sql = "SELECT DISTINCT KeyOfEn FROM Sys_MapAttr  WHERE  exists  (SELECT X.No FROM ( SELECT CONCAT('ND',NodeID) AS No FROM WF_Node WHERE FK_Flow='"
					+ this.getNo() + "') AS X where sys_mapattr.fk_mapdata=x.no)";
			break;
		default:
			// sql = "SELECT distinct KeyOfEn FROM Sys_MapAttr WHERE FK_MapData
			// IN ( SELECT 'ND' " + SystemConfig.getAppCenterDBAddStringStr() +
			// " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" +
			// this.getNo() + "')";
			sql = "SELECT distinct KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' "
					+ SystemConfig.getAppCenterDBAddStringStr()
					+ " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo()
					+ "' AND Sys_MapAttr.FK_MapData = 'ND' || CAST(WF_Node.NodeID AS VARCHAR(20)))";
			break;
		}

		if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
			sql = "SELECT A.* FROM (" + sql + ") AS A ";
			String sql3 = "DELETE FROM Sys_MapAttr WHERE KeyOfEn NOT IN (" + sql + ") AND FK_MapData='" + fk_mapData
					+ "' ";
			DBAccess.RunSQL(sql3); // 鍒犻櫎涓嶅瓨鍦ㄧ殑瀛楁.
		} else {
			String sql2 = "DELETE FROM Sys_MapAttr WHERE KeyOfEn NOT IN (" + sql + ") AND FK_MapData='" + fk_mapData
					+ "' ";
			DBAccess.RunSQL(sql2); // 鍒犻櫎涓嶅瓨鍦ㄧ殑瀛楁.
		}

		// 琛ュ厖涓婃病鏈夊瓧娈点��
		switch (SystemConfig.getAppCenterDBType()) {
		case Oracle:
			// sunxd ORACLE鏁版嵁搴撹鍙ュ鍔犲埆鍚�
			sql = "SELECT MyPK \"MyPK\", KeyOfEn \"KeyOfEn\" FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND' "
					+ SystemConfig.getAppCenterDBAddStringStr()
					+ " cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + this.getNo()
					+ "' AND Sys_MapAttr.FK_MapData = 'ND' || CAST(WF_Node.NodeID AS VARCHAR(20)))";
			break;
		case MySQL:
			sql = "SELECT MyPK, KeyOfEn FROM Sys_MapAttr WHERE EXISTS (SELECT X.No FROM ( SELECT CONCAT('ND',NodeID) AS No FROM WF_Node WHERE FK_Flow='"
					+ this.getNo() + "') AS X where Sys_MapAttr.FK_MapData = X.No)";
			break;
		default:
			sql = "SELECT MyPK, KeyOfEn FROM Sys_MapAttr WHERE EXISTS ( SELECT 'ND'+cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='"
					+ this.getNo() + "' AND Sys_MapAttr.FK_MapData = 'ND' + CAST(WF_Node.NodeID AS VARCHAR(20)))";
			break;
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		// sunxd ORACLE鏁版嵁搴撹鍙ュ鍔犲埆鍚�
		sql = "SELECT KeyOfEn \"KeyOfEn\" FROM Sys_MapAttr WHERE FK_MapData='ND" + flowId + "Rpt'";
		DataTable dtExits = DBAccess.RunSQLReturnTable(sql);
		String pks = "@";
		for (DataRow dr : dtExits.Rows) {
			pks += dr.getValue(0) + "@";
		}

		for (DataRow dr : dt.Rows) {
			String mypk = dr.getValue("MyPK").toString();
			if (pks.contains("@" + dr.getValue("KeyOfEn").toString() + "@")) {
				continue;
			}

			pks += dr.getValue("KeyOfEn").toString() + "@";

			BP.Sys.MapAttr ma = new BP.Sys.MapAttr(mypk);
			ma.setMyPK("ND" + flowId + "Rpt_" + ma.getKeyOfEn());
			ma.setFK_MapData("ND" + flowId + "Rpt");
			ma.setUIIsEnable(false);

			if (ma.getDefValReal().contains("@")) {
				// 濡傛灉鏄竴涓湁鍙橀噺鐨勫弬鏁�.
				ma.setDefVal("");
			}

			try {
				ma.Insert();
			} catch (java.lang.Exception e) {
			}
		}

		MapAttrs attrs = new MapAttrs(fk_mapData);

		// 鍒涘缓mapData.
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo("ND" + flowId + "Rpt");
		if (md.RetrieveFromDBSources() == 0) {
			md.setName(this.getName());
			md.setPTable(this.getPTable());
			md.Insert();
		} else {
			md.setName(this.getName());
			md.setPTable(this.getPTable());
			md.Update();
		}
		int groupID = 0;
		for (MapAttr attr : attrs.ToJavaList()) {
			String keyOfEn = attr.getKeyOfEn();
			if (keyOfEn.equals(StartWorkAttr.FK_Dept)) {
				// attr.UIBindKey = "BP.Port.Depts";
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(true);
				attr.setGroupID(groupID); // gfs[0].GetValIntByKey("OID");
				attr.setUIIsEnable(false);
				attr.setDefVal("");
				attr.setMaxLen(100);
				attr.Update();
			} else if (keyOfEn.equals("FK_NY")) {
				// attr.UIBindKey = "BP.Pub.NYs";
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(true);
				attr.setUIIsEnable(false);
				attr.setGroupID(groupID);
				attr.Update();
			}
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.Title) == false) {
			// 鏍囬
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.Title); // "FlowEmps";
			attr.setName("鏍囬");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(400);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.OID) == false) {
			// WorkID
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("OID");
			attr.setName("WorkID");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(EditType.Readonly);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.WFState) == false) {
			// 娴佺▼鐘舵��
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.WFState);
			attr.setName("娴佺▼鐘舵��");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIBindKey(GERptAttr.WFState);
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.Enum);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.WFSta) == false) {
			// 娴佺▼鐘舵�丒xt
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.WFSta);
			attr.setName("鐘舵��");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIBindKey(GERptAttr.WFSta);
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.Enum);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowEmps) == false) {
			// 鍙備笌浜�
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEmps); // "FlowEmps";
			attr.setName("鍙備笌浜�");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowStarter) == false) {
			// 鍙戣捣浜�
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStarter);
			attr.setName("鍙戣捣浜�");
			attr.setMyDataType(DataType.AppString);

			attr.setUIBindKey("BP.Port.Emps");
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);

			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(20);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowStartRDT) == false) {
			// MyNum
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStartRDT); // "FlowStartRDT";
			attr.setName("鍙戣捣鏃堕棿");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowEnder) == false) {
			// 鍙戣捣浜�
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnder);
			attr.setName("缁撴潫浜�");
			attr.setMyDataType(DataType.AppString);
			attr.setUIBindKey("BP.Port.Emps");
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(20);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowEnderRDT) == false) {
			// MyNum
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnderRDT); // "FlowStartRDT";
			attr.setName("缁撴潫鏃堕棿");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowEndNode) == false) {
			// 缁撴潫鑺傜偣
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEndNode);
			attr.setName("缁撴潫鑺傜偣");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowDaySpan) == false) {
			// FlowDaySpan
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowDaySpan); // "FlowStartRDT";
			attr.setName("璺ㄥ害(澶�)");
			attr.setMyDataType(DataType.AppMoney);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PFlowNo) == false) {
			// 鐖舵祦绋� 娴佺▼缂栧彿
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PFlowNo);
			attr.setName("鐖舵祦绋嬫祦绋嬬紪鍙�"); // 鐖舵祦绋嬫祦绋嬬紪鍙�
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(3);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PNodeID) == false) {
			// 鐖舵祦绋媁orkID
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PNodeID);
			attr.setName("鐖舵祦绋嬪惎鍔ㄧ殑鑺傜偣");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PWorkID) == false) {
			// 鐖舵祦绋媁orkID
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PWorkID);
			attr.setName("鐖舵祦绋媁orkID");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PEmp) == false) {
			// 璋冭捣瀛愭祦绋嬬殑浜哄憳
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PEmp);
			attr.setName("璋冭捣瀛愭祦绋嬬殑浜哄憳");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(32);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.BillNo) == false) {
			// 鐖舵祦绋� 娴佺▼缂栧彿
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.BillNo);
			attr.setName("鍗曟嵁缂栧彿"); // 鍗曟嵁缂栧彿
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(100);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_MyNum") == false) {
			// MyNum
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("MyNum");
			attr.setName("鏉�");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("1");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.AtPara) == false) {
			// 鐖舵祦绋� 娴佺▼缂栧彿
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.AtPara);
			attr.setName("鍙傛暟"); // 鍗曟嵁缂栧彿
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(4000);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.GUID) == false) {
			// 鐖舵祦绋� 娴佺▼缂栧彿
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.GUID);
			attr.setName("GUID"); // 鍗曟嵁缂栧彿
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(32);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PrjNo) == false) {
			// 椤圭洰缂栧彿
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PrjNo);
			attr.setName("椤圭洰缂栧彿"); // 椤圭洰缂栧彿
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(100);
			attr.setIdx(-100);
			attr.Insert();
		}
		if (attrs.Contains(md.getNo() + "_" + GERptAttr.PrjName) == false) {
			// 椤圭洰鍚嶇О
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PrjName);
			attr.setName("椤圭洰鍚嶇О");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(100);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(md.getNo() + "_" + GERptAttr.FlowNote) == false) {
			// 娴佺▼淇℃伅
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowNote);
			attr.setName("娴佺▼淇℃伅"); // 鐖舵祦绋嬫祦绋嬬紪鍙�
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(500);
			attr.setIdx(-100);
			attr.Insert();
		}
		/// #region 涓烘祦绋嬪瓧娈佃缃垎缁勩��
		try {
			String flowInfo = "娴佺▼淇℃伅";
			GroupField flowGF = new GroupField();
			int num = flowGF.Retrieve(GroupFieldAttr.EnName, fk_mapData, GroupFieldAttr.Lab, "娴佺▼淇℃伅");
			if (num == 0) {
				flowGF = new GroupField();
				flowGF.setLab(flowInfo);
				flowGF.setFrmID(fk_mapData);
				flowGF.setIdx(-1);
				flowGF.Insert();
			}
			sql = "UPDATE Sys_MapAttr SET GroupID='" + flowGF.getOID() + "' WHERE  FK_MapData='" + fk_mapData
					+ "'  AND KeyOfEn IN('" + GERptAttr.PFlowNo + "','" + GERptAttr.PWorkID + "','" + GERptAttr.MyNum
					+ "','" + GERptAttr.FK_Dept + "','" + GERptAttr.FK_NY + "','" + GERptAttr.FlowDaySpan + "','"
					+ GERptAttr.FlowEmps + "','" + GERptAttr.FlowEnder + "','" + GERptAttr.FlowEnderRDT + "','"
					+ GERptAttr.FlowEndNode + "','" + GERptAttr.FlowStarter + "','" + GERptAttr.FlowStartRDT + "','"
					+ GERptAttr.WFState + "')";
			DBAccess.RunSQL(sql);
		} catch (RuntimeException ex) {
			Log.DefaultLogWriteLineError(ex.getMessage());
		}
		/// #region 灏惧悗澶勭悊.
		GERpt gerpt = this.getHisGERpt();
		gerpt.CheckPhysicsTable(); // 璁╂姤琛ㄩ噸鏂扮敓鎴�.

		DBAccess.RunSQL("DELETE FROM Sys_GroupField WHERE FrmID='" + fk_mapData
				+ "' AND OID NOT IN (SELECT GroupID FROM Sys_MapAttr WHERE FK_MapData = '" + fk_mapData + "')");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name='娲诲姩鏃堕棿' WHERE FK_MapData='ND" + flowId + "Rpt' AND KeyOfEn='CDT'");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name='鍙備笌鑰�' WHERE FK_MapData='ND" + flowId + "Rpt' AND KeyOfEn='Emps'");
		/// #region 澶勭悊鎶ヨ〃.
		String mapRpt = "ND" + Integer.parseInt(this.getNo()) + "MyRpt";
		MapData mapData = new MapData();
		mapData.setNo(mapRpt);
		if (mapData.RetrieveFromDBSources() == 0) {
			mapData.setNo(mapRpt);
			mapData.setPTable(this.getPTable());
			mapData.setName(this.getName() + "鎶ヨ〃");
			mapData.setNote("榛樿.");

			// 榛樿鐨勬煡璇㈠瓧娈�.
			mapData.Insert();

			BP.WF.Rpt.MapRpt rpt = new BP.WF.Rpt.MapRpt();
			rpt.setNo(mapRpt);
			rpt.RetrieveFromDBSources();
			rpt.setFK_Flow(this.getNo());
			rpt.ResetIt();
			rpt.Update();
		}

		if (!this.getPTable().equals(mapData.getPTable())) {
			mapData.setPTable(this.getPTable());
			mapData.Update();
		}

		// 琛ュ厖鍩虹瀛楁.
		attrs = new MapAttrs(mapData.getNo());
		/// #region 琛ュ厖涓婃祦绋嬪瓧娈靛埌NDxxxRpt.
		for (MapAttr attr : attrs.ToJavaList()) {
			String keyOfEn = attr.getKeyOfEn();
			if (keyOfEn.equals(StartWorkAttr.FK_Dept)) {
				attr.setUIBindKey("BP.Port.Depts");
				attr.setUIContralType(UIContralType.DDL);
				attr.setLGType(FieldTypeS.FK);
				attr.setUIVisible(true);
				attr.setGroupID(groupID); // gfs[0].GetValIntByKey("OID");
				attr.setUIIsEnable(false);
				attr.setDefVal("");
				attr.setMaxLen(100);
				attr.Update();
			} else if (keyOfEn.equals("FK_NY")) {
				attr.setUIBindKey("BP.Pub.NYs");
				attr.setUIContralType(UIContralType.DDL);
				attr.setLGType(FieldTypeS.FK);
				attr.setUIVisible(true);
				attr.setGroupID(groupID); // gfs[0].GetValIntByKey("OID");
				attr.setUIIsEnable(false);
				attr.Update();
			}
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.OID) == false) {
			// WorkID
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setKeyOfEn("OID");
			attr.setName("WorkID");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(BP.En.EditType.Readonly);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.WFSta) == false) {
			// 娴佺▼鐘舵�丒xt
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.WFSta);
			attr.setName("鐘舵��");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIBindKey(GERptAttr.WFSta);
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.Enum);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowEmps) == false) {
			// 鍙備笌浜�
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEmps); // "FlowEmps");
			attr.setName("鍙備笌浜�");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(1000);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowStarter) == false) {
			// 鍙戣捣浜�
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStarter);
			attr.setName("鍙戣捣浜�");
			attr.setMyDataType(DataType.AppString);

			attr.setUIBindKey("BP.Port.Emps");
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);

			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(20);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowStartRDT) == false) {
			// MyNum
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowStartRDT); // "FlowStartRDT");
			attr.setName("鍙戣捣鏃堕棿");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowEnder) == false) {
			// 鍙戣捣浜�
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnder);
			attr.setName("缁撴潫浜�");
			attr.setMyDataType(DataType.AppString);
			attr.setUIBindKey("BP.Port.Emps");
			attr.setUIContralType(UIContralType.DDL);
			attr.setLGType(FieldTypeS.FK);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setMinLen(0);
			attr.setMaxLen(20);
			attr.setIdx(-1);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowEnderRDT) == false) {
			// MyNum
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEnderRDT); // "FlowStartRDT");
			attr.setName("缁撴潫鏃堕棿");
			attr.setMyDataType(DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowEndNode) == false) {
			// 缁撴潫鑺傜偣
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowEndNode);
			attr.setName("缁撴潫鑺傜偣");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowDaySpan) == false) {
			// FlowDaySpan
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowDaySpan); // "FlowStartRDT");
			attr.setName("璺ㄥ害(澶�)");
			attr.setMyDataType(DataType.AppMoney);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setUIIsLine(false);
			attr.setIdx(-101);
			attr.setDefVal("0");
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PFlowNo) == false) {
			// 鐖舵祦绋� 娴佺▼缂栧彿
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PFlowNo);
			attr.setName("鐖舵祦绋嬬紪鍙�"); // 鐖舵祦绋嬫祦绋嬬紪鍙�
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(3);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PNodeID) == false) {
			// 鐖舵祦绋媁orkID
			MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PNodeID);
			attr.setName("鐖舵祦绋嬪惎鍔ㄧ殑鑺傜偣");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PWorkID) == false) {
			// 鐖舵祦绋媁orkID
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PWorkID);
			attr.setName("鐖舵祦绋媁orkID");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("0");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PEmp) == false) {
			// 璋冭捣瀛愭祦绋嬬殑浜哄憳
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.PEmp);
			attr.setName("璋冭捣瀛愭祦绋嬬殑浜哄憳");
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(32);
			attr.setIdx(-100);
			attr.Insert();
		}

		// if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.CWorkID) ==
		// false)
		// {
		// /* 寤剁画娴佺▼WorkID */
		// MapAttr attr = new BP.Sys.MapAttr();
		// attr.FK_MapData = md.No;
		// attr.HisEditType = EditType.UnDel;
		// attr.KeyOfEn = GERptAttr.CWorkID;
		// attr.Name = "寤剁画娴佺▼WorkID";
		// attr.MyDataType = DataType.AppInt;
		// attr.DefVal = "0";
		// attr.UIContralType = UIContralType.TB;
		// attr.LGType = FieldTypeS.Normal;
		// attr.UIVisible = true;
		// attr.UIIsEnable = false;
		// attr.UIIsLine = false;
		// attr.HisEditType = EditType.UnDel;
		// attr.Idx = -101;
		// attr.Insert();
		// }

		// if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.CFlowNo) ==
		// false)
		// {
		// /* 寤剁画娴佺▼缂栧彿 */
		// MapAttr attr = new BP.Sys.MapAttr();
		// attr.FK_MapData = md.No;
		// attr.HisEditType = EditType.UnDel;
		// attr.KeyOfEn = GERptAttr.CFlowNo;
		// attr.Name = "寤剁画娴佺▼缂栧彿";
		// attr.MyDataType = DataType.AppString;
		// attr.UIContralType = UIContralType.TB;
		// attr.LGType = FieldTypeS.Normal;
		// attr.UIVisible = true;
		// attr.UIIsEnable = false;
		// attr.UIIsLine = true;
		// attr.MinLen = 0;
		// attr.MaxLen = 3;
		// attr.Idx = -100;
		// attr.Insert();
		// }

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.BillNo) == false) {
			// 鍗曟嵁缂栧彿
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.BillNo);
			attr.setName("鍗曟嵁缂栧彿"); // 鍗曟嵁缂栧彿
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(100);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_MyNum") == false) {
			// MyNum
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn("MyNum");
			attr.setName("鏉�");
			attr.setMyDataType(DataType.AppInt);
			attr.setDefVal("1");
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setHisEditType(EditType.UnDel);
			attr.setIdx(-101);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.AtPara) == false) {
			// 鐖舵祦绋� 娴佺▼缂栧彿
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.AtPara);
			attr.setName("鍙傛暟"); // 鍗曟嵁缂栧彿
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(4000);
			attr.setIdx(-100);
			attr.Insert();
		}

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.GUID) == false) {
			// 鐖舵祦绋� 娴佺▼缂栧彿
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.GUID);
			attr.setName("GUID"); // 鍗曟嵁缂栧彿
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(false);
			attr.setMinLen(0);
			attr.setMaxLen(32);
			attr.setIdx(-100);
			attr.Insert();
		}

		// if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PrjNo) == false)
		// {
		// /* 椤圭洰缂栧彿 */
		// MapAttr attr = new BP.Sys.MapAttr();
		// attr.FK_MapData = md.No;
		// attr.HisEditType = EditType.UnDel;
		// attr.KeyOfEn = GERptAttr.PrjNo;
		// attr.Name = "椤圭洰缂栧彿"; // 椤圭洰缂栧彿
		// attr.MyDataType = DataType.AppString;
		// attr.UIContralType = UIContralType.TB;
		// attr.LGType = FieldTypeS.Normal;
		// attr.UIVisible = true;
		// attr.UIIsEnable = false;
		// attr.UIIsLine = false;
		// attr.MinLen = 0;
		// attr.MaxLen = 100;
		// attr.Idx = -100;
		// attr.Insert();
		// }
		// if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.PrjName) ==
		// false)
		// {
		// /* 椤圭洰鍚嶇О */
		// MapAttr attr = new BP.Sys.MapAttr();
		// attr.FK_MapData = md.No;
		// attr.HisEditType = EditType.UnDel;
		// attr.KeyOfEn = GERptAttr.PrjName;
		// attr.Name = "椤圭洰鍚嶇О"; // 椤圭洰鍚嶇О
		// attr.MyDataType = DataType.AppString;
		// attr.UIContralType = UIContralType.TB;
		// attr.LGType = FieldTypeS.Normal;
		// attr.UIVisible = true;
		// attr.UIIsEnable = false;
		// attr.UIIsLine = false;
		// attr.MinLen = 0;
		// attr.MaxLen = 100;
		// attr.Idx = -100;
		// attr.Insert();
		// }

		if (attrs.Contains(mapData.getNo() + "_" + GERptAttr.FlowNote) == false) {
			// 娴佺▼淇℃伅
			MapAttr attr = new MapAttr();
			attr.setFK_MapData(md.getNo());
			attr.setHisEditType(EditType.UnDel);
			attr.setKeyOfEn(GERptAttr.FlowNote);
			attr.setName("娴佺▼淇℃伅"); // 鐖舵祦绋嬫祦绋嬬紪鍙�
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(true);
			attr.setUIIsEnable(false);
			attr.setUIIsLine(true);
			attr.setMinLen(0);
			attr.setMaxLen(500);
			attr.setIdx(-100);
			attr.Insert();
		}
	}

	/**
	 * 鎵ц杩愬姩浜嬩欢
	 * 
	 * @param doType
	 *            浜嬩欢绫诲瀷
	 * @param currNode
	 *            褰撳墠鑺傜偣
	 * @param en
	 *            瀹炰綋
	 * @param atPara
	 *            鍙傛暟
	 * @param objs
	 *            鍙戦�佸璞★紝鍙��
	 * @return 鎵ц缁撴灉
	 * @throws Exception
	 */
	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara) throws Exception {
		return DoFlowEventEntity(doType, currNode, en, atPara, null, "");
	}

	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, SendReturnObjs objs)
			throws Exception {
		return DoFlowEventEntity(doType, currNode, en, atPara, objs, null, null);
	}

	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, SendReturnObjs objs,
			Node jumpToNode, String jumpToEmps) throws Exception {
		if (currNode == null)
			return null;

		// 璋冪敤閫氱敤鏂规硶.
		BP.WF.OverrideClass.FEE(doType, currNode, en, atPara, objs, jumpToNode, jumpToEmps);

		String str = null;
		if (this.getFEventEntity() != null) {
			this.getFEventEntity().SendReturnObjs = objs;
			str = this.getFEventEntity().DoIt(doType, currNode, en, atPara, jumpToNode, jumpToEmps);
		}

		// BP.WF.PortalInterface.SendToEmail(mypk, sender, sendToEmpNo, email,
		// title, maildoc)

		// FrmEvents fes = currNode.getMapData().getFrmEvents();
		FrmEvents fes = currNode.getFrmEvents();

		if (str == null) {
			str = fes.DoEventNode(doType, en, atPara);
		}

		if (!(doType.equals(EventListOfNode.WorkArrive) || doType.equals(EventListOfNode.SendSuccess)
				|| doType.equals(EventListOfNode.ShitAfter) || doType.equals(EventListOfNode.ReturnAfter)
				|| doType.equals(EventListOfNode.UndoneAfter) || doType.equals(EventListOfNode.AskerReAfter))) {
			return str;
		}

		// 鎵ц娑堟伅鐨勫彂閫�.
		PushMsgs pms = currNode.getHisPushMsgs();
		String msgAlert = ""; // 鐢熸垚鐨勬彁绀轰俊鎭�.
		for (PushMsg item : pms.ToJavaList()) {
			if (!item.getFK_Event().equals(doType))
				continue;

			if (item.getSMSPushWay() == 0 || item.getMailPushWay() == 0)
				continue; // 濡傛灉閮芥病鏈夋秷鎭缃紝灏辨斁杩�.

			// 鎵ц鍙戦�佹秷鎭�.

			if (doType.equals(EventListOfNode.WorkArrive))
				msgAlert += item.DoSendMessage(currNode, en, atPara, objs, jumpToNode, objs.getVarAcceptersID());
			else
				msgAlert += item.DoSendMessage(currNode, en, atPara, objs, jumpToNode, jumpToEmps);

		}
		return str + msgAlert;
	}

	public final String DoFlowEventEntity(String doType, Node currNode, Entity en, String atPara, Node jumpToNode,
			String jumpToEmp) throws Exception {
		String str = this.DoFlowEventEntity(doType, currNode, en, atPara, null, jumpToNode, jumpToEmp);
		return str;
	}

	private BP.WF.FlowEventBase _FDEventEntity = null;

	/**
	 * 鑺傜偣瀹炰綋绫伙紝娌℃湁灏辫繑鍥炰负绌�.
	 */
	private BP.WF.FlowEventBase getFEventEntity() {
		if (_FDEventEntity == null && !this.getFlowMark().equals("") && !this.getFlowEventEntity().equals("")) {
			_FDEventEntity = BP.WF.Glo.GetFlowEventEntityByEnName(this.getFlowEventEntity());
		}
		return _FDEventEntity;
	}

	/**
	 * 鏄惁鏄疢D5鍔犲瘑娴佺▼
	 */
	public final boolean getIsMD5() {
		return this.GetValBooleanByKey(FlowAttr.IsMD5);
	}

	public final void setIsMD5(boolean value) {
		this.SetValByKey(FlowAttr.IsMD5, value);
	}

	/**
	 * 鏄惁鏈夊崟鎹�
	 */
	public final int getNumOfBill() {
		return this.GetValIntByKey(FlowAttr.NumOfBill);
	}

	public final void setNumOfBill(int value) {
		this.SetValByKey(FlowAttr.NumOfBill, value);
	}

	/**
	 * 鏍囬鐢熸垚瑙勫垯
	 */
	public final String getTitleRole() {
		return this.GetValStringByKey(FlowAttr.TitleRole);
	}

	public final void setTitleRole(String value) {
		this.SetValByKey(FlowAttr.TitleRole, value);
	}

	/**
	 * 鏄庣粏琛�
	 */
	public final int getNumOfDtl() {
		return this.GetValIntByKey(FlowAttr.NumOfDtl);
	}

	public final void setNumOfDtl(int value) {
		this.SetValByKey(FlowAttr.NumOfDtl, value);
	}

	public final int getAvgDay() {
		return this.GetValIntByKey(FlowAttr.AvgDay);
	}

	public final void setAvgDay(java.math.BigDecimal value) {
		this.SetValByKey(FlowAttr.AvgDay, value);
	}

	public final int getStartNodeID() {
		return Integer.parseInt(this.getNo() + "01");
		// return this.GetValIntByKey(FlowAttr.StartNodeID);
	}

	/**
	 * add 2013-01-01. 涓氬姟涓昏〃(榛樿涓篘DxxRpt)
	 * 
	 */
	public final String getPTable() {
		String s = this.GetValStringByKey(FlowAttr.PTable);
		if (StringHelper.isNullOrEmpty(s)) {
			// sunxd 20170714
			// Integer.parseInt()涓鍍忎负绌烘椂Integer.parseInt()浼氭姤閿�
			// Integer.parseInt()涓鍍忎綔浜嗙┖鍊艰浆鎹�
			s = "ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Rpt";
		}
		return s;
	}

	public final void setPTable(String value) {
		this.SetValByKey(FlowAttr.PTable, value);
	}

	/**
	 * 鍘嗗彶璁板綍鏄剧ず瀛楁.
	 * 
	 */
	public final String getHistoryFields() {
		String strs = this.GetValStringByKey(FlowAttr.HistoryFields);
		if (StringHelper.isNullOrEmpty(strs)) {
			strs = "WFState,Title,FlowStartRDT,FlowEndNode";
		}

		return strs;
	}

	/**
	 * 鏄惁鍚敤锛�
	 * 
	 */
	public final boolean getIsGuestFlow() {
		return this.GetValBooleanByKey(FlowAttr.IsGuestFlow);
	}

	public final void setIsGuestFlow(boolean value) {
		this.SetValByKey(FlowAttr.IsGuestFlow, value);
	}

	/**
	 * 鏄惁鍙互鐙珛鍚姩
	 * 
	 */
	public final boolean getIsCanStart() {
		return this.GetValBooleanByKey(FlowAttr.IsCanStart);
	}

	public final void setIsCanStart(boolean value) {
		this.SetValByKey(FlowAttr.IsCanStart, value);
	}

	/**
	 * 鏄惁鍙互鎵归噺鍙戣捣
	 * 
	 */
	public final boolean getIsBatchStart() {
		return this.GetValBooleanByKey(FlowAttr.IsBatchStart);
	}

	public final void setIsBatchStart(boolean value) {
		this.SetValByKey(FlowAttr.IsBatchStart, value);
	}

	/**
	 * 鏄惁鑷姩璁＄畻鏈潵鐨勫鐞嗕汉
	 * 
	 */
	public final boolean getIsFullSA() {
		return this.GetValBooleanByKey(FlowAttr.IsFullSA);
	}

	public final void setIsFullSA(boolean value) {
		this.SetValByKey(FlowAttr.IsFullSA, value);
	}

	/**
	 * 鎵归噺鍙戣捣瀛楁
	 * 
	 */
	public final String getBatchStartFields() {
		return this.GetValStringByKey(FlowAttr.BatchStartFields);
	}

	public final void setBatchStartFields(String value) {
		this.SetValByKey(FlowAttr.BatchStartFields, value);
	}

	/**
	 * 鍗曟嵁鏍煎紡
	 * 
	 */
	public final String getBillNoFormat() {
		return this.GetValStringByKey(FlowAttr.BillNoFormat);
	}

	public final void setBillNoFormat(String value) {
		this.SetValByKey(FlowAttr.BillNoFormat, value);
	}

	/**
	 * 娴佺▼绫诲埆
	 * 
	 */
	public final String getFK_FlowSort() {
		return this.GetValStringByKey(FlowAttr.FK_FlowSort);
	}

	public final void setFK_FlowSort(String value) {
		this.SetValByKey(FlowAttr.FK_FlowSort, value);
	}

	/**
	 * 绯荤粺绫诲埆
	 * 
	 */
	public final String getSysType() {
		return this.GetValStringByKey(FlowAttr.SysType);
	}

	public final void setSysType(String value) {
		this.SetValByKey(FlowAttr.SysType, value);
	}

	/**
	 * 鍙傛暟
	 * 
	 */
	public final String getParas() {
		return this.GetValStringByKey(FlowAttr.Paras);
	}

	public final void setParas(String value) {
		this.SetValByKey(FlowAttr.Paras, value);
	}

	/**
	 * 娴佺▼绫诲埆鍚嶇О
	 * 
	 * @throws Exception
	 * 
	 */
	public final String getFK_FlowSortText() throws Exception {
		FlowSort fs = new FlowSort(this.getFK_FlowSort());
		return fs.getName();
		// return this.GetValRefTextByKey(FlowAttr.FK_FlowSort);
	}

	/**
	 * 鐗堟湰鍙�
	 * 
	 */
	public final String getVer() {
		return this.GetValStringByKey(FlowAttr.Ver);
	}

	public final void setVer(String value) {
		this.SetValByKey(FlowAttr.Ver, value);
	}

	/// #endregion

	/// #region 璁＄畻灞炴��
	/**
	 * 娴佺▼绫诲瀷(澶х殑绫诲瀷)
	 * 
	 */
	public final int getFlowType_del() {
		return this.GetValIntByKey(FlowAttr.FlowType);
	}

	public final SubFlowOver getSubFlowOver() {

		return SubFlowOver.forValue(this.GetValIntByKey(FlowAttr.IsAutoSendSubFlowOver));

	}

	public final String getNote() {
		String s = this.GetValStringByKey("Note");
		if (s.length() == 0) {
			return "鏃�";
		}
		return s;
	}

	public final String getNoteHtml() {
		if (this.getNote().equals("鏃�") || this.getNote().equals("")) {
			return "娴佺▼璁捐浜哄憳娌℃湁缂栧啓姝ゆ祦绋嬬殑甯姪淇℃伅锛岃鎵撳紑璁捐鍣�-銆嬫墦寮�姝ゆ祦绋�-銆嬭璁＄敾甯冧笂鐐瑰嚮鍙抽敭-銆嬫祦绋嬪睘鎬�-銆嬪～鍐欐祦绋嬪府鍔╀俊鎭��";
		} else {
			return this.getNote();
		}
	}

	/**
	 * 鏄惁澶氱嚎绋嬭嚜鍔ㄦ祦绋�
	 * 
	 */
	public final boolean getIsMutiLineWorkFlow_del() {
		return false;
		//
		// if (this.FlowType==2 || this.FlowType==1 )
		// return true;
		// else
		// return false;
		//
	}

	/// #endregion

	/**
	 * 搴旂敤绫诲瀷
	 */
	public final FlowAppType getHisFlowAppType() {
		return FlowAppType.forValue(this.GetValIntByKey(FlowAttr.FlowAppType));
	}

	public final void setHisFlowAppType(FlowAppType value) {
		this.SetValByKey(FlowAttr.FlowAppType, value.getValue());
	}

	/**
	 * 鏁版嵁瀛樺偍妯″紡
	 * 
	 */
	public final DataStoreModel getHisDataStoreModel() {
		return DataStoreModel.forValue(this.GetValIntByKey(FlowAttr.DataStoreModel));
	}

	public final void setHisDataStoreModel(DataStoreModel value) {
		this.SetValByKey(FlowAttr.DataStoreModel, value.getValue());
	}

	/**
	 * 鑺傜偣
	 * 
	 */
	private Nodes _HisNodes = null;

	/**
	 * 浠栫殑鑺傜偣闆嗗悎.
	 * 
	 * @throws Exception
	 * 
	 */
	public final Nodes getHisNodes() throws Exception {
		if (this._HisNodes == null) {
			_HisNodes = new Nodes(this.getNo());
		}
		return _HisNodes;
	}

	public final void setHisNodes(Nodes value) {
		_HisNodes = value;
	}

	/**
	 * 浠栫殑 Start 鑺傜偣
	 * 
	 * @throws Exception
	 * 
	 */
	public final Node getHisStartNode() throws Exception {

		for (Node nd : this.getHisNodes().ToJavaList()) {
			if (nd.getIsStartNode()) {
				return nd;
			}
		}
		throw new RuntimeException("@娌℃湁鎵惧埌浠栫殑寮�濮嬭妭鐐�,宸ヤ綔娴佺▼[" + this.getName() + "]瀹氫箟閿欒.");
	}

	/**
	 * 浠栫殑浜嬪姟绫诲埆
	 * 
	 * @throws Exception
	 * 
	 */
	public final FlowSort getHisFlowSort() throws Exception {
		return new FlowSort(this.getFK_FlowSort());
	}

	/**
	 * flow data 鏁版嵁
	 * 
	 * @throws Exception
	 * 
	 */
	public final BP.WF.Data.GERpt getHisGERpt() throws Exception {
		try {
			BP.WF.Data.GERpt wk = new BP.WF.Data.GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
			return wk;
		} catch (java.lang.Exception e) {
			this.DoCheck();
			BP.WF.Data.GERpt wk1 = new BP.WF.Data.GERpt("ND" + Integer.parseInt(this.getNo()) + "Rpt");
			return wk1;
		}
	}

	/// #endregion

	/**
	 * 娴佺▼
	 * 
	 */
	public Flow() {
	}

	/**
	 * 娴佺▼
	 * 
	 * @param _No
	 *            缂栧彿
	 */
	/*
	 * 鍚戜笂绉诲姩
	 */
	public final void DoUp() {
		this.DoOrderUp(FlowAttr.FK_FlowSort, this.getFK_FlowSort(), FlowAttr.Idx);
	}

	/*
	 * 鍚戜笅绉诲姩
	 */
	public final void DoDown() {
		this.DoOrderDown(FlowAttr.FK_FlowSort, this.getFK_FlowSort(), FlowAttr.Idx);
	}

	public Flow(String _No) throws Exception {
		this.setNo(_No);
		if (SystemConfig.getIsDebug()) {
			int i = this.RetrieveFromDBSources();
			if (i == 0) {
				throw new RuntimeException("娴佺▼缂栧彿涓嶅瓨鍦�");
			}
		} else {
			this.Retrieve();
		}
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		// 鑾峰緱浜嬩欢瀹炰綋.
		this.setFlowEventEntity(BP.WF.Glo.GetFlowEventEntityStringByFlowMark(this.getFlowMark(), this.getNo()));

		DBAccess.RunSQL("UPDATE WF_Node SET FlowName='" + this.getName() + "' WHERE FK_Flow='" + this.getNo() + "'");
		DBAccess.RunSQL("UPDATE Sys_MapData SET  Name='" + this.getName() + "' WHERE No='" + this.getPTable() + "'");
		return super.beforeUpdateInsertAction();
	}

	/**
	 * 閲嶅啓鍩虹被鏂规硶
	 */
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("WF_Flow", "娴佺▼");
		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("3");

		map.AddTBStringPK(FlowAttr.No, null, "缂栧彿", true, true, 1, 10, 3);
		map.AddTBString(FlowAttr.Name, null, "鍚嶇О", true, false, 0, 500, 10);

		map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "娴佺▼绫诲埆", new FlowSorts(), false);
		map.AddTBString(FlowAttr.SysType, null, "绯荤粺绫诲埆", true, false, 0, 3, 10);

		map.AddTBInt(FlowAttr.FlowRunWay, 0, "杩愯鏂瑰紡", false, false);

		// map.AddDDLEntities(FlowAttr.FK_FlowSort, "01", "娴佺▼绫诲埆", new
		// FlowSorts(), false);
		// map.AddDDLSysEnum(FlowAttr.FlowRunWay, (int)FlowRunWay.HandWork,
		// "杩愯鏂瑰紡", false,
		// false, FlowAttr.FlowRunWay,
		// "@0=鎵嬪伐鍚姩@1=鎸囧畾浜哄憳鎸夋椂鍚姩@2=鏁版嵁闆嗘寜鏃跺惎鍔ˊ3=瑙﹀彂寮忓惎鍔�");

		map.AddTBString(FlowAttr.RunObj, null, "杩愯鍐呭", true, false, 0, 3000, 10);
		map.AddTBString(FlowAttr.Note, null, "澶囨敞", true, false, 0, 100, 10);
		map.AddTBString(FlowAttr.RunSQL, null, "娴佺▼缁撴潫鎵ц鍚庢墽琛岀殑SQL", true, false, 0, 2000, 10);

		map.AddTBInt(FlowAttr.NumOfBill, 0, "鏄惁鏈夊崟鎹�", false, false);
		map.AddTBInt(FlowAttr.NumOfDtl, 0, "NumOfDtl", false, false);
		map.AddTBInt(FlowAttr.FlowAppType, 0, "娴佺▼绫诲瀷", false, false);
		map.AddTBInt(FlowAttr.ChartType, 1, "鑺傜偣鍥惧舰绫诲瀷", false, false);

		// map.AddBoolean(FlowAttr.IsOK, true, "鏄惁鍚敤", true, true);
		map.AddBoolean(FlowAttr.IsCanStart, true, "鍙互鐙珛鍚姩鍚︼紵", true, true, true);
		map.AddTBInt(FlowAttr.IsStartInMobile, 1, "鏄惁鍙互鍦ㄦ墜鏈洪噷鍙戣捣锛�", true, true);

		map.AddTBDecimal(FlowAttr.AvgDay, 0, "骞冲潎杩愯鐢ㄥぉ", false, false);

		map.AddTBInt(FlowAttr.IsFullSA, 0, "鏄惁鑷姩璁＄畻鏈潵鐨勫鐞嗕汉锛�(鍚敤鍚�,ccflow灏变細涓哄凡鐭ラ亾鐨勮妭鐐瑰～鍏呭鐞嗕汉鍒癢F_SelectAccper)", false, false);
		map.AddTBInt(FlowAttr.IsMD5, 0, "IsMD5", false, false);
		map.AddTBInt(FlowAttr.Idx, 0, "鏄剧ず椤哄簭鍙�(鍦ㄥ彂璧峰垪琛ㄤ腑)", true, false);
		map.AddTBInt(FlowAttr.TimelineRole, 0, "鏃舵晥鎬ц鍒�", true, false);
		map.AddTBString(FlowAttr.Paras, null, "鍙傛暟", false, false, 0, 400, 10);

		// add 2013-01-01.
		map.AddTBString(FlowAttr.PTable, null, "娴佺▼鏁版嵁瀛樺偍涓昏〃", true, false, 0, 30, 10);

		// 鑽夌瑙勫垯 "@0=鏃�(涓嶈鑽夌)@1=淇濆瓨鍒板緟鍔濦2=淇濆瓨鍒拌崏绋跨"
		map.AddTBInt(FlowAttr.Draft, 0, "鑽夌瑙勫垯", true, false);

		// add 2013-01-01.
		map.AddTBInt(FlowAttr.DataStoreModel, 0, "鏁版嵁瀛樺偍妯″紡", true, false);

		// add 2013-02-05.
		map.AddTBString(FlowAttr.TitleRole, null, "鏍囬鐢熸垚瑙勫垯", true, false, 0, 150, 10, true);

		// add 2013-02-14
		map.AddTBString(FlowAttr.FlowMark, null, "娴佺▼鏍囪", true, false, 0, 150, 10);
		map.AddTBString(FlowAttr.FlowEventEntity, null, "FlowEventEntity", true, false, 0, 100, 10, true);
		map.AddTBString(FlowAttr.HistoryFields, null, "鍘嗗彶鏌ョ湅瀛楁", true, false, 0, 500, 10, true);
		map.AddTBInt(FlowAttr.IsGuestFlow, 0, "鏄惁鏄鎴峰弬涓庢祦绋嬶紵", true, false);
		map.AddTBString(FlowAttr.BillNoFormat, null, "鍗曟嵁缂栧彿鏍煎紡", true, false, 0, 50, 10, true);
		map.AddTBString(FlowAttr.FlowNoteExp, null, "澶囨敞琛ㄨ揪寮�", true, false, 0, 500, 10, true);

		// 閮ㄩ棬鏉冮檺鎺у埗绫诲瀷,姝ゅ睘鎬у湪鎶ヨ〃涓帶鍒剁殑.
		map.AddTBInt(FlowAttr.DRCtrlType, 0, "閮ㄩ棬鏌ヨ鏉冮檺鎺у埗鏂瑰紡", true, false);

		/// #region 娴佺▼鍚姩闄愬埗
		map.AddTBInt(FlowAttr.StartLimitRole, 0, "鍚姩闄愬埗瑙勫垯", true, false);
		map.AddTBString(FlowAttr.StartLimitPara, null, "瑙勫垯鍐呭", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartLimitAlert, null, "闄愬埗鎻愮ず", true, false, 0, 500, 10, false);
		map.AddTBInt(FlowAttr.StartLimitWhen, 0, "鎻愮ず鏃堕棿", true, false);

		/// #endregion 娴佺▼鍚姩闄愬埗

		/// #region 瀵艰埅鏂瑰紡銆�
		map.AddTBInt(FlowAttr.StartGuideWay, 0, "鍓嶇疆瀵艰埅鏂瑰紡", false, false);
		map.AddTBString(FlowAttr.StartGuideLink, null, "鍙充晶鐨勮繛鎺�", true, false, 0, 200, 10, true);
		map.AddTBString(FlowAttr.StartGuideLab, null, "杩炴帴鏍囩", true, false, 0, 200, 10, true);

		map.AddTBString(FlowAttr.StartGuidePara1, null, "鍙傛暟1", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartGuidePara2, null, "鍙傛暟2", true, false, 0, 500, 10, true);
		map.AddTBString(FlowAttr.StartGuidePara3, null, "鍙傛暟3", true, false, 0, 500, 10, true);
		map.AddTBInt(FlowAttr.IsResetData, 0, "鏄惁鍚敤鏁版嵁閲嶇疆鎸夐挳锛�", true, false);
		// map.AddTBInt(FlowAttr.IsImpHistory, 0, "鏄惁鍚敤瀵煎叆鍘嗗彶鏁版嵁鎸夐挳锛�", true, false);
		map.AddTBInt(FlowAttr.IsLoadPriData, 0, "鏄惁瀵煎叆涓婁竴涓暟鎹紵", true, false);
		map.AddTBInt(FlowAttr.IsDBTemplate, 0, "鍔犺浇妯℃澘锛�", true, false);

		// 鎵归噺鍙戣捣 add 2013-12-27.
		map.AddTBInt(FlowAttr.IsBatchStart, 0, "鏄惁鍙互鎵归噺鍙戣捣", true, false);
		map.AddTBString(FlowAttr.BatchStartFields, null, "鎵归噺鍙戣捣瀛楁(鐢ㄩ�楀彿鍒嗗紑)", true, false, 0, 500, 10, true);

		// map.AddTBInt(FlowAttr.IsEnableTaskPool, 0, "鏄惁鍚敤鍏变韩浠诲姟姹�", true, false);
		// map.AddDDLSysEnum(FlowAttr.TimelineRole, (int)TimelineRole.ByNodeSet,
		// "鏃舵晥鎬ц鍒�",
		// true, true, FlowAttr.TimelineRole,
		// "@0=鎸夎妭鐐�(鐢辫妭鐐瑰睘鎬ф潵瀹氫箟)@1=鎸夊彂璧蜂汉(寮�濮嬭妭鐐筍ysSDTOfFlow瀛楁璁＄畻)");

		map.AddTBInt(FlowAttr.IsAutoSendSubFlowOver, 0, "(褰撳墠鑺傜偣涓哄瓙娴佺▼鏃�)鏄惁妫�鏌ユ墍鏈夊瓙娴佺▼瀹屾垚鍚庣埗娴佺▼鑷姩鍙戦��", true, true);
		map.AddTBString(FlowAttr.Ver, null, "鐗堟湰鍙�", true, true, 0, 20, 10);

		// 璁捐绫诲瀷 .
		map.AddTBInt(FlowAttr.DType, 0, "璁捐绫诲瀷0=ccbpm,1=bpmn", true, false);

		map.AddTBInt(FlowAttr.FlowDeleteRole, 0, "娴佺▼瀹炰緥鍒犻櫎瑙勫垯", true, false);

		// 鍙傛暟.
		map.AddTBAtParas(1000);

		/// #region 鏁版嵁鍚屾鏂规
		// 鏁版嵁鍚屾鏂瑰紡.
		map.AddTBInt(FlowAttr.DTSWay, FlowDTSWay.None.getValue(), "鍚屾鏂瑰紡", true, true);
		map.AddTBString(FlowAttr.DTSDBSrc, null, "鏁版嵁婧�", true, false, 0, 200, 100, false);
		map.AddTBString(FlowAttr.DTSBTable, null, "涓氬姟琛ㄥ悕", true, false, 0, 200, 100, false);
		map.AddTBString(FlowAttr.DTSBTablePK, null, "涓氬姟琛ㄤ富閿�", false, false, 0, 32, 10);

		map.AddTBInt(FlowAttr.DTSTime, FlowDTSTime.AllNodeSend.getValue(), "鎵ц鍚屾鏃堕棿鐐�", true, true);
		map.AddTBString(FlowAttr.DTSSpecNodes, null, "鎸囧畾鐨勮妭鐐笽D", true, false, 0, 200, 100, false);

		// map.AddTBInt(FlowAttr.DTSField, getDTSField().SameNames.getValue(),
		// "瑕佸悓姝ョ殑瀛楁璁＄畻鏂瑰紡", true, true);
		map.AddTBInt(FlowAttr.DTSField, 0, "瑕佸悓姝ョ殑瀛楁璁＄畻鏂瑰紡", true, true);

		// map.AddTBString(FlowAttr.DTSFields, null, "瑕佸悓姝ョ殑瀛楁s,涓棿鐢ㄩ�楀彿鍒嗗紑.", false,
		// false, 0, 2000, 100, false);

		/// #endregion 鏁版嵁鍚屾鏂规

		// map.AddSearchAttr(FlowAttr.FK_FlowSort);
		// map.AddSearchAttr(FlowAttr.FlowRunWay);

		RefMethod rm = new RefMethod();
		rm.Title = "璁捐妫�鏌ユ姤鍛�"; // "璁捐妫�鏌ユ姤鍛�";
		rm.ToolTip = "妫�鏌ユ祦绋嬭璁＄殑闂銆�";
		rm.Icon = SystemConfig.getCCFlowWebPath() + "WF/Img/Btn/Confirm.gif";
		rm.ClassMethodName = this.toString() + ".DoCheck";
		rm.GroupName = "娴佺▼缁存姢";
		map.AddRefMethod(rm);

		// rm = new RefMethod();
		// rm.Title = this.ToE("ViewDef", "瑙嗗浘瀹氫箟"); //"瑙嗗浘瀹氫箟";
		// rm.Icon = "/WF/Img/Btn/View.gif";
		// rm.ClassMethodName = this.ToString() + ".DoDRpt";
		// map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "鎶ヨ〃杩愯"; // "鎶ヨ〃杩愯";
		rm.Icon = SystemConfig.getCCFlowWebPath() + "WF/Img/Btn/View.gif";
		rm.ClassMethodName = this.toString() + ".DoOpenRpt()";
		// rm.Icon = "/WF/Img/Btn/Table.gif";
		map.AddRefMethod(rm);

		// rm = new RefMethod();
		// rm.Title = this.ToE("FlowDataOut", "鏁版嵁杞嚭瀹氫箟"); //"鏁版嵁杞嚭瀹氫箟";
		//// rm.Icon = "/WF/Img/Btn/Table.gif";
		// rm.ToolTip = "鍦ㄦ祦绋嬪畬鎴愭椂闂达紝娴佺▼鏁版嵁杞偍瀛樺埌鍏跺畠绯荤粺涓簲鐢ㄣ��";
		// rm.ClassMethodName = this.ToString() + ".DoExp";
		// map.AddRefMethod(rm);

		// rm = new RefMethod();
		// rm.Title = "鍒犻櫎鏁版嵁";
		// rm.Warning = "鎮ㄧ‘瀹氳鎵ц鍒犻櫎娴佺▼鏁版嵁鍚楋紵";
		// rm.ToolTip = "娓呴櫎鍘嗗彶娴佺▼鏁版嵁銆�";
		// rm.ClassMethodName = this.ToString() + ".DoExp";
		// map.AddRefMethod(rm);

		// map.AttrsOfOneVSM.Add(new FlowStations(), new Stations(),
		// FlowStationAttr.FK_Flow,
		// FlowStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "鎶勯�佸矖浣�");

		this.set_enMap(map);
		return this.get_enMap();
	}

	/// #endregion

	/// #region 鍏叡鏂规硶
	/**
	 * 璁捐鏁版嵁杞嚭
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DoExp() throws Exception {
		this.DoCheck();
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/Exp.jsp?CondType=0&FK_Flow=" + this.getNo();
	}

	/**
	 * 瀹氫箟鎶ヨ〃
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DoDRpt() throws Exception {
		this.DoCheck();
		PubClass.WinOpen(ContextHolderUtils.getResponse(),
				BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/WFRpt.jsp?CondType=0&FK_Flow=" + this.getNo(), "鍗曟嵁", "cdsn",
				800, 500, 210, 300);
		return null;
	}

	/**
	 * 杩愯鎶ヨ〃
	 * 
	 * @return
	 */
	public final String DoOpenRpt() {
		return null;
	}

	public final String DoDelData() throws Exception {

		/// #region 鍒犻櫎鐙珛琛ㄥ崟鐨勬暟鎹�.
		String mysql = "SELECT OID FROM " + this.getPTable();
		FrmNodes fns = new FrmNodes();
		fns.Retrieve(FrmNodeAttr.FK_Flow, this.getNo());
		String strs = "";
		for (FrmNode nd : fns.ToJavaList()) {
			if (strs.contains("@" + nd.getFK_Frm()) == true) {
				continue;
			}

			strs += "@" + nd.getFK_Frm() + "@";
			try {
				MapData md = new MapData(nd.getFK_Frm());
				DBAccess.RunSQL("DELETE FROM " + md.getPTable() + " WHERE OID in (" + mysql + ")");
			} catch (java.lang.Exception e) {
			}
		}
		/// #endregion 鍒犻櫎鐙珛琛ㄥ崟鐨勬暟鎹�.
		String sql = "  where FK_Node in (SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo() + "')";
		String sql1 = " where NodeID in (SELECT NodeID FROM WF_Node WHERE fk_flow='" + this.getNo() + "')";
		// DBAccess.RunSQL("DELETE FROM WF_CHOfFlow WHERE FK_Flow='" +
		// this.getNo() + "'");
		DBAccess.RunSQL("DELETE FROM WF_Bill WHERE FK_Flow='" + this.getNo() + "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE FK_Flow='" + this.getNo() + "'");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo() + "'");

		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo() + "'");

		String sqlIn = " WHERE ReturnNode IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork " + sqlIn);

		DBAccess.RunSQL("DELETE FROM WF_SelectAccper " + sql);
		DBAccess.RunSQL("DELETE FROM WF_TransferCustom " + sql);
		// DBAccess.RunSQL("DELETE FROM WF_FileManager " + sql);
		DBAccess.RunSQL("DELETE FROM WF_RememberMe " + sql);

		// sunxd 20170714
		// Integer.parseInt()涓鍍忎负绌烘椂Integer.parseInt()浼氭姤閿�
		// Integer.parseInt()涓鍍忎綔浜嗙┖鍊艰浆鎹�
		if (DBAccess.IsExitsObject("ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Track")) {
			DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(this.getNo()) + "Track ");
		}

		if (DBAccess.IsExitsTabPK(this.getPTable())) {
			DBAccess.RunSQL("DELETE FROM " + this.getPTable());
		}

		DBAccess.RunSQL("DELETE FROM WF_CH WHERE FK_Flow='" + this.getNo() + "'");
		// DBAccess.RunSQL("DELETE FROM Sys_MapExt WHERE FK_MapData LIKE
		// 'ND"+int.Parse(this.getNo())+"%'" );

		// 鍒犻櫎鑺傜偣鏁版嵁銆�
		Nodes nds = new Nodes(this.getNo());
		for (Node nd : nds.ToJavaList()) {
			try {
				Work wk = nd.getHisWork();
				DBAccess.RunSQL("DELETE FROM " + wk.getEnMap().getPhysicsTable());
			} catch (java.lang.Exception e2) {
			}

			MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
			for (MapDtl dtl : dtls.ToJavaList()) {
				try {
					DBAccess.RunSQL("DELETE FROM " + dtl.getPTable());
				} catch (java.lang.Exception e3) {
				}
			}
		}
		// sunxd 20170714
		// Integer.parseInt()涓鍍忎负绌烘椂Integer.parseInt()浼氭姤閿�
		// Integer.parseInt()涓鍍忎綔浜嗙┖鍊艰浆鎹�
		MapDtls mydtls = new MapDtls("ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Rpt");
		for (MapDtl dtl : mydtls.ToJavaList()) {
			try {
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable());
			} catch (java.lang.Exception e4) {
			}
		}
		return "鍒犻櫎鎴愬姛...";
	}

	/**
	 * 瑁呰浇娴佺▼妯℃澘
	 * 
	 * @param fk_flowSort
	 *            娴佺▼绫诲埆
	 * @param path
	 *            娴佺▼鍚嶇О
	 * @return
	 * @throws Exception
	 */
	public static Flow DoLoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model)
			throws Exception {
		return DoLoadFlowTemplate(fk_flowSort, path, model, "");
	}

	public static Flow DoLoadFlowTemplate(String fk_flowSort, String path, ImpFlowTempleteModel model,
			String SpecialFlowNo) throws Exception {

		try {

			java.io.File info = new java.io.File(path);
			DataSet ds = new DataSet();
			ds.readXml(path);

			int isFlow = 0;
			for (DataTable dts : ds.Tables) {
				if (dts.TableName.equals("WF_Flow")) {
					isFlow = 1;
					break;
				}
			}
			if (isFlow == 0) {
				throw new RuntimeException("瀵煎叆閿欒锛岄潪娴佺▼妯＄増鏂囦欢銆�");
			}

			DataTable dtFlow = ds.getHashTables().get("WF_Flow");
			Flow fl = new Flow();
			String oldFlowNo = dtFlow.Rows.get(0).getValue("No").toString();
			String oldFlowName = dtFlow.Rows.get(0).getValue("Name").toString();

			int oldFlowID = Integer.parseInt("".equals(oldFlowNo) ? "0" : oldFlowNo);
			int iOldFlowLength = String.valueOf(oldFlowID).length();
			// String timeKey = new java.util.Date().toString("yyMMddhhmmss");
			SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmss");
			String timeKey = format.format(new Date());

			/// #region 鏍规嵁涓嶅悓鐨勬祦绋嬫ā寮忥紝璁剧疆鐢熸垚涓嶅悓鐨勬祦绋嬬紪鍙�.
			switch (model) {
			case AsNewFlow: // 鍋氫负涓�涓柊娴佺▼.
				// SpecialFlowNo = 0; // 濡傛灉涓嶅姞浼氭彁绀猴紝缂栫爜宸插瓨鍦ㄧ殑bug
				fl.setNo(fl.getGenerNewNo());
				fl.DoDelData();
				fl.DoDelete(); // 鍒犻櫎鍙兘瀛樺湪鐨勫瀮鍦�.
				break;
			case AsTempleteFlowNo: // 鐢ㄦ祦绋嬫ā鐗堜腑鐨勭紪鍙�
				fl.setNo(oldFlowNo);
				if (fl.getIsExits()) {
					throw new RuntimeException("瀵煎叆閿欒:娴佺▼妯＄増(" + oldFlowName + ")涓殑缂栧彿(" + oldFlowNo + ")鍦ㄧ郴缁熶腑宸茬粡瀛樺湪,娴佺▼鍚嶇О涓�:"
							+ dtFlow.Rows.get(0).getValue("name").toString());
				} else {
					fl.setNo(oldFlowNo);
					fl.DoDelData();
					fl.DoDelete(); // 鍒犻櫎鍙兘瀛樺湪鐨勫瀮鍦�.
				}
				break;
			case OvrewaiteCurrFlowNo: // 瑕嗙洊褰撳墠鐨勬祦绋�.
				fl.setNo(oldFlowNo);
				fl.DoDelData();
				fl.DoDelete(); // 鍒犻櫎鍙兘瀛樺湪鐨勫瀮鍦�.
				break;
			case AsSpecFlowNo:
				if (SpecialFlowNo.length() <= 0) {
					throw new RuntimeException("@鎮ㄦ槸鎸夌収鎸囧畾鐨勬祦绋嬬紪鍙峰鍏ョ殑锛屼絾鏄偍娌℃湁浼犲叆姝ｇ‘鐨勬祦绋嬬紪鍙枫��");
				}
				String newFlowNo = StringUtils.leftPad(String.valueOf(SpecialFlowNo), 3, '0');
				fl.setNo(newFlowNo);
				fl.DoDelData();
				fl.DoDelete(); // 鍒犻櫎鍙兘瀛樺湪鐨勫瀮鍦�.
				break;
			default:
				throw new RuntimeException("@娌℃湁鍒ゆ柇");
			}

			/// #endregion 鏍规嵁涓嶅悓鐨勬祦绋嬫ā寮忥紝璁剧疆鐢熸垚涓嶅悓鐨勬祦绋嬬紪鍙�.

			// string timeKey = fl.getNo();
			int idx = 0;
			String infoErr = "";
			String infoTable = "";
			// sunxd 20170714
			// Integer.parseInt()涓鍍忎负绌烘椂Integer.parseInt()浼氭姤閿�
			// Integer.parseInt()涓鍍忎綔浜嗙┖鍊艰浆鎹�
			int flowID = fl.getNo().equals("") ? 0 : Integer.parseInt(fl.getNo());

			/// #region 澶勭悊娴佺▼琛ㄦ暟鎹�
			for (DataColumn dc : dtFlow.Columns) {
				String val = (String) ((dtFlow.Rows.get(0).getValue(dc.ColumnName) instanceof String)
						? dtFlow.Rows.get(0).getValue(dc.ColumnName) : null);

				if (dc.ColumnName.toLowerCase().equals("no") || dc.ColumnName.toLowerCase().equals("fk_flowsort")) {
					continue;
				} else if (dc.ColumnName.toLowerCase().equals("name")) {
					// val = "澶嶅埗:" + val + "_" +
					// DateTime.Now.ToString("MM鏈坉d鏃H鏃秏m鍒�");
				} else {
				}
				fl.SetValByKey(dc.ColumnName, val);
			}
			fl.setFK_FlowSort(fk_flowSort);
			fl.Insert();

			/// #endregion 澶勭悊娴佺▼琛ㄦ暟鎹�

			/// #region 澶勭悊OID 鎻掑叆閲嶅鐨勯棶棰� Sys_GroupField, Sys_MapAttr.
			DataTable mydtGF = ds.getHashTables().get("Sys_GroupField");
			DataTable myDTAttr = ds.getHashTables().get("Sys_MapAttr");
			DataTable myDTAth = ds.getHashTables().get("Sys_FrmAttachment");
			DataTable myDTDtl = ds.getHashTables().get("Sys_MapDtl");
			DataTable myDFrm = ds.getHashTables().get("Sys_MapFrame");
			// DataTable myDM2M = ds.getHashTables().get("Sys_MapM2M");
			if (mydtGF != null) {
				for (DataRow dr : mydtGF.Rows) {
					GroupField gf = new GroupField();
					for (DataColumn dc : mydtGF.Columns) {
						String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
								? dr.getValue(dc.ColumnName) : null);
						gf.SetValByKey(dc.ColumnName, val);
					}
					int oldID = (int) gf.getOID();
					gf.setOID(DBAccess.GenerOID());
					dr.setValue("OID", gf.getOID());
					// 灞炴�с��
					if (myDTAttr != null && myDTAttr.Columns.contains("GroupID")) {
						for (DataRow dr1 : myDTAttr.Rows) {
							if (dr1.getValue("GroupID") == null) {
								dr1.setValue("GroupID", 0);
							}

							if (dr1.getValue("GroupID").toString().equals(String.valueOf(oldID))) {
								dr1.setValue("GroupID", gf.getOID());
							}
						}
					}

					if (myDTAth != null && myDTAth.Columns.contains("GroupID")) {
						// 闄勪欢銆�
						for (DataRow dr1 : myDTAth.Rows) {
							if (dr1.getValue("GroupID") == null) {
								dr1.setValue("GroupID", 0);
							}

							if (dr1.getValue("GroupID").toString().equals(String.valueOf(oldID))) {
								dr1.setValue("GroupID", gf.getOID());
							}
						}
					}

					if (myDTDtl != null && myDTDtl.Columns.contains("GroupID")) {
						// 浠庤〃銆�
						for (DataRow dr1 : myDTDtl.Rows) {
							if (dr1.getValue("GroupID") == null) {
								dr1.setValue("GroupID", 0);
							}

							if (dr1.getValue("GroupID").toString().equals(String.valueOf(oldID))) {
								dr1.setValue("GroupID", gf.getOID());
							}
						}
					}

					if (myDFrm != null && myDFrm.Columns.contains("GroupID")) {
						// frm.
						for (DataRow dr1 : myDFrm.Rows) {
							if (dr1.getValue("GroupID") == null) {
								dr1.setValue("GroupID", 0);
							}

							if (dr1.getValue("GroupID").toString().equals(String.valueOf(oldID))) {
								dr1.setValue("GroupID", gf.getOID());
							}
						}
					}

				}
			}

			/// #endregion 澶勭悊OID 鎻掑叆閲嶅鐨勯棶棰樸�� Sys_GroupField 锛� Sys_MapAttr.

			int timeKeyIdx = 0;
			for (DataTable dt : ds.Tables) {
				timeKeyIdx++;
				timeKey = timeKey + (new Integer(timeKeyIdx)).toString();

				infoTable = "@瀵煎叆:" + dt.TableName + " 鍑虹幇寮傚父銆�";

				if (dt.TableName.equals("WF_FlowForm") == true) {
					for (DataRow dr : dt.Rows) {
						NodeYGFlow yg = new NodeYGFlow();
						for (DataColumn dc : dt.Columns) {
							String val = (String) dr.getValue(dc.ColumnName);
							if (val == null)
								continue;

							String colName = dc.ColumnName.toLowerCase();

							if (colName.equals("nodeid") || colName.equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筃odeYGFlow涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							}

							if (colName.equals("fk_flow")) {
								val = fl.getNo();
							}

							yg.SetValByKey(dc.ColumnName, val);
						}
						yg.Insert();
					}
				}

				if (dt.TableName.equals("WF_Flow")) // 妯＄増鏂囦欢銆�
				{
					continue;
				}
				else if (dt.TableName.equals("WF_FlowFormTree")) // 鐙珛琛ㄥ崟鐩綍 add
																	// 2013-12-03
				{
					continue;
				}
				else if (dt.TableName.equals("WF_FlowForm")) // 鐙珛琛ㄥ崟銆� add
																// 2013-12-03
				{
					continue;
				}
				else if (dt.TableName.equals("WF_NodeForm")) // 鑺傜偣琛ㄥ崟鏉冮檺銆�
																// 2013-12-03
				{
					for (DataRow dr : dt.Rows) {
						NodeToolbar cd = new NodeToolbar();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							// ORIGINAL LINE: case "tonodeid":
							if (dc.ColumnName.toLowerCase().equals("tonodeid")
									|| dc.ColumnName.toLowerCase().equals("fk_node")
									|| dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_NodeForm涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")) {
								val = fl.getNo();
							} else {
								val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.Insert();
					}
				} else if (dt.TableName.equals("Sys_FrmSln")) // 琛ㄥ崟瀛楁鏉冮檺銆�
																// 2013-12-03
				{
					for (DataRow dr : dt.Rows) {
						FrmField cd = new FrmField();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							// ORIGINAL LINE: case "tonodeid":
							if (dc.ColumnName.toLowerCase().equals("tonodeid")
									|| dc.ColumnName.toLowerCase().equals("fk_node")
									|| dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筍ys_FrmSln涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")) {
								val = fl.getNo();
							} else {
								val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.Insert();
					}
				} else if (dt.TableName.equals("WF_NodeToolbar")) // 宸ュ叿鏍忋��
				{
					for (DataRow dr : dt.Rows) {
						NodeToolbar cd = new NodeToolbar();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							// ORIGINAL LINE: case "tonodeid":
							if (dc.ColumnName.toLowerCase().equals("tonodeid")
									|| dc.ColumnName.toLowerCase().equals("fk_node")
									|| dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_NodeToolbar涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")) {
								val = fl.getNo();
							} else {
								val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							cd.SetValByKey(dc.ColumnName, val);
						}
						cd.setOID(DBAccess.GenerOID());
						cd.DirectInsert();
					}
				} else if (dt.TableName.equals("WF_BillTemplate")) {
					continue; // 鍥犱负鐪佹帀浜� 鎵撳嵃妯℃澘鐨勫鐞嗐��
					
				} else if (dt.TableName.equals("WF_FrmNode")) // Conds.xml銆�
				{
					DBAccess.RunSQL("DELETE FROM WF_FrmNode WHERE FK_Flow='" + fl.getNo() + "'");
					for (DataRow dr : dt.Rows) {
						FrmNode fn = new FrmNode();
						fn.setFK_Flow(fl.getNo());
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_FrmNode涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")) {
								val = fl.getNo();
							} else {
							}
							fn.SetValByKey(dc.ColumnName, val);
						}
						// 寮�濮嬫彃鍏ャ��
						fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node());
						fn.Insert();
					}
				} else if (dt.TableName.equals("WF_FindWorkerRole")) // 鎵句汉瑙勫垯
				{
					for (DataRow dr : dt.Rows) {
						FindWorkerRole en = new FindWorkerRole();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")
									|| dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_FindWorkerRole涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")) {
								val = fl.getNo();
							} else {
								val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							en.SetValByKey(dc.ColumnName, val);
						}

						// 鎻掑叆.
						en.DirectInsert();
					}
				} else if (dt.TableName.equals("WF_Cond")) // Conds.xml銆�
				{
					for (DataRow dr : dt.Rows) {
						Cond cd = new Cond();
						cd.setFK_Flow(fl.getNo());
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}
							if (dc.ColumnName.toLowerCase().equals("tonodeid")
									|| dc.ColumnName.toLowerCase().equals("fk_node")
									|| dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_Cond涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")) {
								val = fl.getNo();
							} else {
								val = val.replace("ND" + oldFlowID, "ND" + flowID);
							}
							cd.SetValByKey(dc.ColumnName, val);
						}

						cd.setFK_Flow(fl.getNo());

						// return this.FK_MainNode + "_" + this.ToNodeID + "_" +
						// this.HisCondType.ToString() + "_" +
						// ConnDataFrom.Stas.ToString();
						// 锛屽紑濮嬫彃鍏ャ��
						if (cd.getMyPK().contains("Stas")) {
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString()
									+ "_" + ConnDataFrom.Stas.toString());
						} else if (cd.getMyPK().contains("Dept")) {
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString()
									+ "_" + ConnDataFrom.Depts.toString());
						} else if (cd.getMyPK().contains("Paras")) {
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString()
									+ "_" + ConnDataFrom.Paras.toString());
						} else if (cd.getMyPK().contains("Url")) {
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString()
									+ "_" + ConnDataFrom.Url.toString());
						} else if (cd.getMyPK().contains("SQL")) {
							cd.setMyPK(cd.getFK_Node() + "_" + cd.getToNodeID() + "_" + cd.getHisCondType().toString()
									+ "_" + ConnDataFrom.SQL);
						} else {
							cd.setMyPK(DBAccess.GenerOID() + DateUtils.getCurrentDate("yyMMddHHmmss"));
						}
						cd.DirectInsert();
					}
				} else if (dt.TableName.equals("WF_CCDept")) // 鎶勯�佸埌閮ㄩ棬銆�
				{
					for (DataRow dr : dt.Rows) {
						CCDept cd = new CCDept();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_CCDept涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							cd.SetValByKey(dc.ColumnName, val);
						}

						// 寮�濮嬫彃鍏ャ��
						try {
							cd.Insert();
						} catch (java.lang.Exception e) {
							cd.Update();
						}
					}
				} else if (dt.TableName.equals("WF_NodeReturn")) // 鍙��鍥炵殑鑺傜偣銆�
				{
					for (DataRow dr : dt.Rows) {
						NodeReturn cd = new NodeReturn();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")
									|| dc.ColumnName.toLowerCase().equals("returnto")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_NodeReturn涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							cd.SetValByKey(dc.ColumnName, val);
						}

						// 寮�濮嬫彃鍏ャ��
						try {
							cd.Insert();
						} catch (java.lang.Exception e2) {
							cd.Update();
						}
					}
				}
				// ORIGINAL LINE: case "WF_Direction":
				else if (dt.TableName.equals("WF_Direction")) // 鏂瑰悜銆�
				{
					for (DataRow dr : dt.Rows) {
						Direction dir = new Direction();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							// ORIGINAL LINE: case "node":
							if (dc.ColumnName.toLowerCase().equals("node")
									|| dc.ColumnName.toLowerCase().equals("tonode")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_Direction涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							dir.SetValByKey(dc.ColumnName, val);
						}
						dir.setFK_Flow(fl.getNo());
						dir.Insert();
					}
				} else if (dt.TableName.equals("WF_TurnTo")) // 杞悜瑙勫垯.
				{
					for (DataRow dr : dt.Rows) {
						TurnTo fs = new TurnTo();

						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_TurnTo涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							fs.SetValByKey(dc.ColumnName, val);
						}
						fs.setFK_Flow(fl.getNo());
						fs.Save();
					}
				} else if (dt.TableName.equals("WF_LabNote")) // LabNotes.xml銆�
				{
					idx = 0;
					for (DataRow dr : dt.Rows) {
						LabNote ln = new LabNote();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}
							ln.SetValByKey(dc.ColumnName, val);
						}
						idx++;
						ln.setFK_Flow(fl.getNo());
						ln.setMyPK(ln.getFK_Flow() + "_" + ln.getX() + "_" + ln.getY() + "_" + idx);
						ln.DirectInsert();
					}
				} else if (dt.TableName.equals("WF_NodeDept")) // FAppSets.xml銆�
				{
					for (DataRow dr : dt.Rows) {
						NodeDept dir = new NodeDept();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_NodeDept涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							dir.SetValByKey(dc.ColumnName, val);
						}
						dir.Insert();
					}
				} else if (dt.TableName.equals("WF_Node")) // 瀵煎叆鑺傜偣淇℃伅.
				{
					for (DataRow dr : dt.Rows) {
						BP.WF.Template.NodeSheet nd = new BP.WF.Template.NodeSheet();

						BP.WF.Template.CC cc = new CC(); // 鎶勯�佺浉鍏崇殑淇℃伅.
						BP.WF.Template.FrmWorkCheck fwc = new FrmWorkCheck();

						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}
							if (dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_Node涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")
									|| dc.ColumnName.toLowerCase().equals("fk_flowsort")) {
								continue;
							} else if (dc.ColumnName.toLowerCase().equals("showsheets")
									|| dc.ColumnName.toLowerCase().equals("histonds")
									|| dc.ColumnName.toLowerCase().equals("groupstands")) {
								String key = "@" + flowID;
								val = val.replace(key, "@");
							} else {
							}
							nd.SetValByKey(dc.ColumnName, val);
							cc.SetValByKey(dc.ColumnName, val);
							fwc.SetValByKey(dc.ColumnName, val);
						}

						nd.setFK_Flow(fl.getNo());
						nd.setFlowName(fl.getName());
						try
						// @浜庡簡娴�
						{
							if (nd.getEnMap().getAttrs().Contains("OfficePrintEnable")) {
								if (nd.GetValStringByKey("OfficePrintEnable").equals("鎵撳嵃")) {
									nd.SetValByKey("OfficePrintEnable", 0);
								}
							}
							nd.DirectInsert();

							// 鎶婃妱閫佺殑淇℃伅涔熷鍏ラ噷闈㈠幓.
							cc.DirectUpdate();
							fwc.DirectUpdate();
							DBAccess.RunSQL("DELETE FROM Sys_MapAttr WHERE FK_MapData='ND" + nd.getNodeID() + "'");
						} catch (RuntimeException ex) {
							cc.CheckPhysicsTable();
							fwc.CheckPhysicsTable();

							throw new RuntimeException("@瀵煎叆鑺傜偣:FlowName:" + nd.getFlowName() + " nodeID: "
									+ nd.getNodeID() + " , " + nd.getName() + " 閿欒:" + ex.getMessage());
						}

						// 鍒犻櫎mapdata.
					}
					// 鎵цupdate 瑙﹀彂鍏朵粬鐨勪笟鍔￠�昏緫銆�
					for (DataRow dr : dt.Rows) {
						Node nd = new Node();
						// sunxd 20170714
						// Integer.parseInt()涓鍍忎负绌烘椂Integer.parseInt()浼氭姤閿�
						// Integer.parseInt()涓鍍忎綔浜嗙┖鍊艰浆鎹�
						nd.setNodeID(dr.getValue(NodeAttr.NodeID).toString().equals("") ? 0
								: Integer.parseInt(dr.getValue(NodeAttr.NodeID).toString()));
						nd.RetrieveFromDBSources();
						nd.setFK_Flow(fl.getNo());

						// 鑾峰彇琛ㄥ崟绫诲埆
						String formType = dr.getValue(NodeAttr.FormType).toString();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("nodefrmid")) {
								// 缁戝畾琛ㄥ崟搴撶殑琛ㄥ崟11涓嶉渶瑕佹浛鎹㈣〃鍗曠紪鍙�
								if (formType.equals("11") == false) {
									int iFormTypeLength = iOldFlowLength + 2;
									if (val.length() > iFormTypeLength) {
										val = "ND" + flowID + val.substring(iFormTypeLength);
									}
								}
							} else if (dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_Node涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")
									|| dc.ColumnName.toLowerCase().equals("fk_flowsort")) {
								continue;
							} else if (dc.ColumnName.toLowerCase().equals("showsheets")
									|| dc.ColumnName.toLowerCase().equals("histonds")
									|| dc.ColumnName.toLowerCase().equals("groupstands")) {
								String key = "@" + flowID;
								val = val.replace(key, "@");
							} else {
							}
							nd.SetValByKey(dc.ColumnName, val);
						}
						nd.setFK_Flow(fl.getNo());
						nd.setFlowName(fl.getName());
						nd.DirectUpdate();
					}
				}else if(dt.TableName.equals("WF_NodeExt")){
					for(DataRow dr : dt.Rows)
                    {
                        BP.WF.Template.NodeExt nd = new BP.WF.Template.NodeExt();
                        nd.setNodeID (Integer.parseInt(flowID + dr.getValue(NodeAttr.NodeID).toString().substring(iOldFlowLength)));
                        nd.RetrieveFromDBSources();
                        for(DataColumn dc: dt.Columns)
                        {
                        	String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
                        	if (val == null) {
								continue;
							}
							if (dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_Node涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else if (dc.ColumnName.toLowerCase().equals("fk_flow")
									|| dc.ColumnName.toLowerCase().equals("fk_flowsort")) {
								continue;
							} else if (dc.ColumnName.toLowerCase().equals("showsheets")
									|| dc.ColumnName.toLowerCase().equals("histonds")
									|| dc.ColumnName.toLowerCase().equals("groupstands")) {
								String key = "@" + flowID;
								val = val.replace(key, "@");
							} else {
							}
                            nd.SetValByKey(dc.ColumnName, val);
                        }

                        nd.DirectUpdate();
                    }
				}
				else if(dt.TableName.equals("WF_Selector")){ 
					for (DataRow dr : dt.Rows) {
						Selector selector = new Selector();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}
							if (dc.ColumnName.toLowerCase().equals("nodeid")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_Node涓婩K_Node鍊奸敊璇�:" + val);
								}
							   val = flowID + val.substring(iOldFlowLength);
						   }
					       
							selector.SetValByKey(dc.ColumnName, val);
						}
						selector.DirectUpdate();
					}
				}
				else if (dt.TableName.equals("WF_NodeStation")) // FAppSets.xml銆�
				{
					DBAccess.RunSQL(
							"DELETE FROM WF_NodeStation WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
									+ fl.getNo() + "')");
					for (DataRow dr : dt.Rows) {
						NodeStation ns = new NodeStation();
						for (DataColumn dc : dr.columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_NodeStation涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							ns.SetValByKey(dc.ColumnName, val);
						}
						ns.Insert();
					}
				}

				else if (dt.TableName.equals("Sys_Enum")) // RptEmps.xml銆�
				{
					for (DataRow dr : dt.Rows) {
						SysEnum se = new SysEnum();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
							} else {
							}
							se.SetValByKey(dc.ColumnName, val);
						}
						se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
						if (se.getIsExits()) {
							continue;
						}
						se.Insert();
					}
				} else if (dt.TableName.equals("Sys_EnumMain")) // RptEmps.xml銆�
				{
					for (DataRow dr : dt.Rows) {
						SysEnumMain sem = new SysEnumMain();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}
							sem.SetValByKey(dc.ColumnName, val);
						}
						if (sem.getIsExits()) {
							continue;
						}
						sem.Insert();
					}
				}  else if (dt.TableName.equals("Sys_MapData")) // RptEmps.xml銆�
				{
					for (DataRow dr : dt.Rows) {
						MapData md = new MapData();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + Integer.parseInt(fl.getNo()));
							md.SetValByKey(dc.ColumnName, val);
						}
						md.Save();
					}
				} else if (dt.TableName.equals("Sys_MapDtl")) // RptEmps.xml銆�
				{
					for (DataRow dr : dt.Rows) {
						MapDtl md = new MapDtl();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							md.SetValByKey(dc.ColumnName, val);
						}
						md.Save();
						md.IntMapAttrs(); // 鍒濆鍖栦粬鐨勫瓧娈靛睘鎬�.

					}
				} else if (dt.TableName.equals("Sys_MapExt")) {
					for (DataRow dr : dt.Rows) {
						MapExt md = new MapExt();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							md.SetValByKey(dc.ColumnName, val);
						}

						// 璋冩暣浠栫殑PK.
						// md.InitPK();
						md.Save(); // 鎵ц淇濆瓨.
					}
				} else if (dt.TableName.equals("Sys_FrmLine")) {
					idx = 0;
					for (DataRow dr : dt.Rows) {
						idx++;
						FrmLine en = new FrmLine();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.setMyPK(DBAccess.GenerGUID());
						
						en.Insert();
					}
				} else if (dt.TableName.equals("Sys_FrmEle")) {
					idx = 0;
					for (DataRow dr : dt.Rows) {
						idx++;
						FrmEle en = new FrmEle();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}
						en.Insert();
					}
				} else if (dt.TableName.equals("Sys_FrmImg")) {
					idx = 0;
					timeKey = DateUtils.getCurrentDate("yyyyMMddHHmmss");
					for (DataRow dr : dt.Rows) {
						idx++;
						FrmImg en = new FrmImg();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}
							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();

					}
				} else if (dt.TableName.equals("Sys_FrmLab")) {
					idx = 0;
					timeKey = DateUtils.getCurrentDate("yyyyMMddHHmmss");
					for (DataRow dr : dt.Rows) {
						idx++;
						FrmLab en = new FrmLab();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						// en.MyPK = Guid.NewGuid().ToString();
						// 鍑虹幇閲嶅鐨�

						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();

					}
				} else if (dt.TableName.equals("Sys_FrmLink")) {
					idx = 0;
					for (DataRow dr : dt.Rows) {
						idx++;
						FrmLink en = new FrmLink();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							if (val == null) {
								continue;
							}

							en.SetValByKey(dc.ColumnName, val);
						}
						en.setMyPK(UUID.randomUUID().toString());
						// en.MyPK = "LK" + timeKey + "_" + idx;
						en.Insert();
					}
				} else if (dt.TableName.equals("Sys_FrmAttachment")) {
					idx = 0;
					for (DataRow dr : dt.Rows) {
						idx++;
						FrmAttachment en = new FrmAttachment();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						en.setMyPK(en.getFK_MapData() + "_" + en.getNoOfObj());
						en.Save();
					}
				} else if (dt.TableName.equals("Sys_FrmEvent")) // 浜嬩欢.
				{
					idx = 0;
					for (DataRow dr : dt.Rows) {
						idx++;
						FrmEvent en = new FrmEvent();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}

						// 瑙ｅ喅淇濆瓨閿欒闂.
						try {
							en.Insert();
						} catch (java.lang.Exception e5) {
							en.Update();
						}
					}
				}

				else if (dt.TableName.equals("Sys_FrmRB")) // Sys_FrmRB.
				{
					idx = 0;
					for (DataRow dr : dt.Rows) {
						idx++;
						FrmRB en = new FrmRB();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							val = val.replace("ND" + oldFlowID, "ND" + flowID);
							en.SetValByKey(dc.ColumnName, val);
						}
						en.Insert();
					}
					
				}
				else if (dt.TableName.equals("Sys_MapFrame"))
				{
						for (DataRow dr : dt.Rows)
						{
							idx++;
							MapFrame en = new MapFrame();
							for (DataColumn dc : dt.Columns)
							{
								String val = (String)((dr.get(dc.ColumnName) instanceof String) ? dr.get(dc.ColumnName) : null);
								if (val == null)
								{
									continue;
								}

								val = val.replace("ND" + oldFlowID, "ND" + flowID);
								en.SetValByKey(dc.ColumnName,val );
							}
							en.DirectInsert();
						}
				}
				else if (dt.TableName.equals("WF_NodeEmp")) // FAppSets.xml銆�
				{
					for (DataRow dr : dt.Rows) {
						NodeEmp ne = new NodeEmp();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_NodeEmp涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Insert();
					}
				} else if (dt.TableName.equals("Sys_GroupField")) {
					//鑾峰彇Sys_MapAttr //琚佷附濞�
					if(ds.hashTables.containsKey("Sys_MapAttr") == true){
						DataTable MapAttrdt = ds.GetTableByName("Sys_MapAttr");
						for (DataRow dr : MapAttrdt.Rows) {
							MapAttr ma = new MapAttr();
							for (DataColumn dc : MapAttrdt.Columns) {
								String val = dr.getValue(dc.ColumnName) + "";
	
								// switch (dc.ColumnName.ToLower())
								if (dc.ColumnName.toLowerCase().equals("fk_mapdata")
										|| dc.ColumnName.toLowerCase().equals("keyofen")
										|| dc.ColumnName.toLowerCase().equals("autofulldoc")) {
									if (val == null) {
										continue;
									}
	
									val = val.replace("ND" + oldFlowID, "ND" + flowID);
								} else {
								}
								ma.SetValByKey(dc.ColumnName, val);
							}
							boolean b = ma.IsExit(MapAttrAttr.FK_MapData, ma.getFK_MapData(), MapAttrAttr.KeyOfEn,
									ma.getKeyOfEn());
	
							ma.setMyPK(ma.getFK_MapData() + "_" + ma.getKeyOfEn());
							if (b == true) {
								ma.DirectUpdate();
							} else {
								ma.DirectInsert();
							}
						}
					}
					for (DataRow dr : dt.Rows) {
						GroupField gf = new GroupField();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("enname")
									|| dc.ColumnName.toLowerCase().equals("keyofen")) {
								val = val.replace("ND" + oldFlowID, "ND" + flowID);
							} else if (dc.ColumnName.toLowerCase().equals("ctrlid")) // 鍗囩骇鍌荤摐琛ㄥ崟鐨勬椂鍊�,鏂板鍔犵殑瀛楁
																						// add
																						// by
																						// zhoupeng
																						// 2016.11.21
							{
								val = val.replace("ND" + oldFlowID, "ND" + flowID);
							} else if (dc.ColumnName.toLowerCase().equals("frmid")) // 鍗囩骇鍌荤摐琛ㄥ崟鐨勬椂鍊�,鏂板鍔犵殑瀛楁
																					// add
																					// by
																					// zhoupeng
																					// 2016.11.21
							{
								val = val.replace("ND" + oldFlowID, "ND" + flowID);
							} else {
							}
							gf.SetValByKey(dc.ColumnName, val);
						}
						int oid = DBAccess.GenerOID();
						DBAccess.RunSQL("UPDATE Sys_MapAttr SET GroupID='" + oid + "' WHERE FK_MapData='" + gf.getFrmID()
								+ "' AND GroupID=" + dr.getValue("OID"));
						gf.InsertAsOID(oid);
						
					}
					
				} else if (dt.TableName.equals("WF_CCEmp")) // 鎶勯��.
				{
					for (DataRow dr : dt.Rows) {
						CCEmp ne = new CCEmp();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_CCEmp涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Insert();
					}
				} else if (dt.TableName.equals("WF_CCStation")) // 鎶勯��.
				{
					String mysql = " DELETE FROM WF_CCStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
							+ flowID + "')";
					DBAccess.RunSQL(mysql);
					for (DataRow dr : dt.Rows) {
						CCStation ne = new CCStation();
						for (DataColumn dc : dt.Columns) {
							String val = (String) ((dr.getValue(dc.ColumnName) instanceof String)
									? dr.getValue(dc.ColumnName) : null);
							if (val == null) {
								continue;
							}

							// switch (dc.ColumnName.ToLower())
							if (dc.ColumnName.toLowerCase().equals("fk_node")) {
								if (val.length() < iOldFlowLength) {
									// 鑺傜偣缂栧彿闀垮害灏忎簬娴佺▼缂栧彿闀垮害鍒欎负寮傚父鏁版嵁锛屽紓甯告暟鎹笉杩涜澶勭悊
									throw new RuntimeException(
											"@瀵煎叆妯℃澘鍚嶇О锛�" + oldFlowName + "锛涜妭鐐筗F_CCStation涓婩K_Node鍊奸敊璇�:" + val);
								}
								val = flowID + val.substring(iOldFlowLength);
							} else {
							}
							ne.SetValByKey(dc.ColumnName, val);
						}
						ne.Save();
					}
				} else {
					// infoErr += "Error:" + dt.TableName;
				}
			}

			/// #region 澶勭悊鏁版嵁瀹屾暣鎬с��
			DBAccess.RunSQL("UPDATE WF_Cond SET FK_Node=NodeID WHERE FK_Node=0");
			DBAccess.RunSQL("UPDATE WF_Cond SET ToNodeID=NodeID WHERE ToNodeID=0");

			DBAccess.RunSQL("DELETE FROM WF_Cond WHERE NodeID NOT IN (SELECT NodeID FROM WF_Node)");
			DBAccess.RunSQL("DELETE FROM WF_Cond WHERE ToNodeID NOT IN (SELECT NodeID FROM WF_Node) ");
			DBAccess.RunSQL("DELETE FROM WF_Cond WHERE FK_Node NOT IN (SELECT NodeID FROM WF_Node) AND FK_Node > 0");

			// 澶勭悊鍒嗙粍閿欒.
			Nodes nds = new Nodes(fl.getNo());
			for (Node nd : nds.ToJavaList()) {
				MapFrmFool cols = new MapFrmFool("ND" + nd.getNodeID());
				cols.DoCheckFixFrmForUpdateVer();
			}

			/// #endregion

			if (infoErr.equals("")) {
				infoTable = "";
				return fl; // "瀹屽叏鎴愬姛銆�";
			}

			infoErr = "@鎵ц[" + path + "]鏈熼棿鍑虹幇濡備笅闈炶嚧鍛界殑閿欒锛歕t\r" + infoErr + "@ " + infoTable;
			throw new RuntimeException(infoErr);
		} catch (Exception ex) {
			String err = "@鎵ц[" + path + "]鏈熼棿鍑虹幇濡備笅鑷村懡鐨勯敊璇細" + ex.getMessage();
			throw new RuntimeException(err);
		}

	}

	public final Node DoNewNode(int x, int y) throws Exception {
		Node nd = new Node();
		int idx = this.getHisNodes().size();
		if (idx == 0) {
			idx++;
		}

		while (true) {
			String strID = this.getNo() + StringHelper.padLeft(String.valueOf(idx), 2, '0');
			nd.setNodeID(Integer.parseInt(strID));
			if (!nd.getIsExits()) {
				break;
			}
			idx++;
		}

		nd.setHisNodeWorkType(NodeWorkType.Work);
		nd.setName("鑺傜偣" + idx);
		nd.setHisNodePosType(NodePosType.Mid);
		nd.setFK_Flow(this.getNo());
		nd.setFlowName(this.getName());
		nd.setX(x);
		nd.setY(y);
		nd.setStep(idx);

		// 澧炲姞浜嗕袱涓粯璁ゅ�煎�� . 2016.11.15. 鐩殑鏄鍒涘缓鐨勮妭鐐癸紝灏卞彲浠ヤ娇鐢�.
		nd.setCondModel(CondModel.SendButtonSileSelect); // 榛樿鐨勫彂閫佹柟鍚�.
		nd.setHisDeliveryWay(DeliveryWay.BySelected); // 涓婁竴姝ュ彂閫佷汉鏉ラ�夋嫨.
		nd.setFormType(NodeFormType.FoolForm); // 琛ㄥ崟绫诲瀷.

		// 涓哄垱寤鸿妭鐐硅缃粯璁ゅ�� @浜庡簡娴�.
		String file = SystemConfig.getPathOfDataUser() + "XML/DefaultNewNodeAttr.xml";
		if ((new java.io.File(file)).isFile()) {
			DataSet ds = new DataSet();
			ds.readXml(file);

			DataTable dt = ds.Tables.get(0);
			for (DataColumn dc : dt.Columns) {
				nd.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
			}
		}

		nd.Insert();
		nd.CreateMap();

		// 閫氱敤鐨勪汉鍛橀�夋嫨鍣�.
		BP.WF.Template.Selector select = new Selector(nd.getNodeID());
		select.setSelectorModel(SelectorModel.GenerUserSelecter);
		select.Update();

		// 璁剧疆瀹℃牳缁勪欢鐨勯珮搴�
		DBAccess.RunSQL("UPDATE WF_Node SET FWC_H=300,FTC_H=300 WHERE NodeID='" + nd.getNodeID() + "'");

		// 鍛ㄦ湅@浜庡簡娴烽渶瑕佺炕璇�.
		CreatePushMsg(nd);
		return nd;
	}

	public final void CreatePushMsg(Node nd) throws Exception {
		// 鍛ㄦ湅@浜庡簡娴烽渶瑕佺炕璇�.
		if (SystemConfig.getIsEnableCCIM() == false) {
			return;
		}

		// 鍒涘缓鍙戦�佺煭娑堟伅,涓洪粯璁ょ殑娑堟伅.
		BP.WF.Template.PushMsg pm = new BP.WF.Template.PushMsg();
		int i = pm.Retrieve(PushMsgAttr.FK_Event, EventListOfNode.SendSuccess, PushMsgAttr.FK_Node, nd.getNodeID(),
				PushMsgAttr.FK_Flow, nd.getFK_Flow());
		if (i == 0) {
			pm.setFK_Event(EventListOfNode.SendSuccess);
			pm.setFK_Node(nd.getNodeID());
			pm.setFK_Flow(this.getNo());

			pm.setSMSPushWay(1); // 鍙戦�佺煭娑堟伅.
			pm.setMailPushWay(0); // 涓嶅彂閫侀偖浠舵秷鎭�.
			pm.setMyPK(DBAccess.GenerGUID());
			pm.Insert();
		}

		// 璁剧疆閫�鍥炴秷鎭彁閱�.
		i = pm.Retrieve(PushMsgAttr.FK_Event, EventListOfNode.ReturnAfter, PushMsgAttr.FK_Node, nd.getNodeID(),
				PushMsgAttr.FK_Flow, nd.getFK_Flow());
		if (i == 0) {
			pm.setFK_Event(EventListOfNode.ReturnAfter);
			pm.setFK_Node(nd.getNodeID());
			pm.setFK_Flow(this.getNo());

			pm.setSMSPushWay(1); // 鍙戦�佺煭娑堟伅.
			pm.setMailPushWay(0); // 涓嶅彂閫侀偖浠舵秷鎭�.
			pm.setMyPK(DBAccess.GenerGUID());
			pm.Insert();
		}
	}

	/**
	 * 鎵ц鏂板缓
	 * 
	 * @param flowSort
	 *            绫诲埆
	 * @param flowName
	 *            娴佺▼鍚嶇О
	 * @param model
	 *            鏁版嵁瀛樺偍妯″紡
	 * @param pTable
	 *            鏁版嵁瀛樺偍鐗╃悊琛�
	 * @param FlowMark
	 *            娴佺▼鏍囪
	 * @throws Exception
	 */
	public final String DoNewFlow(String flowSort, String flowName, DataStoreModel model, String pTable,
			String FlowMark) throws Exception {
		try {
			// 妫�鏌ュ弬鏁扮殑瀹屾暣鎬�.
			if (StringHelper.isNullOrEmpty(pTable) == false && pTable.length() >= 1) {
				String c = pTable.substring(0, 1);
				if (DataType.IsNumStr(c) == true) {
					throw new RuntimeException("@闈炴硶鐨勬祦绋嬫暟鎹〃(" + pTable + "),瀹冧細瀵艰嚧ccflow涓嶈兘鍒涘缓璇ヨ〃.");
				}
			}

			this.setName(flowName);
			if (StringHelper.isNullOrWhiteSpace(this.getName())) {
				this.setName("鏂板缓娴佺▼" + this.getNo()); // 鏂板缓娴佺▼.
			}

			this.setNo(this.GenerNewNoByKey(FlowAttr.No));
			this.setHisDataStoreModel(model);
			this.setPTable(pTable);
			this.setFK_FlowSort(flowSort);
			this.setFlowMark(FlowMark);

			if (StringHelper.isNullOrEmpty(FlowMark) == false) {
				if (this.IsExit(FlowAttr.FlowMark, FlowMark)) {
					throw new RuntimeException("@璇ユ祦绋嬫爣绀�:" + FlowMark + "宸茬粡瀛樺湪浜庣郴缁熶腑.");
				}
			}

			// 缁欏垵濮嬪��
			// this.Paras =
			// "@StartNodeX=10@StartNodeY=15@EndNodeX=40@EndNodeY=10";
			this.setParas("@StartNodeX=200@StartNodeY=50@EndNodeX=200@EndNodeY=350");
			this.Save();

			/// #region 鍒犻櫎鏈夊彲鑳藉瓨鍦ㄧ殑鍘嗗彶鏁版嵁.
			Flow fl = new Flow(this.getNo());
			fl.DoDelData();
			fl.DoDelete();

			fl.setVer("鍘熷鐗�");

			this.Save();

			/// #endregion 鍒犻櫎鏈夊彲鑳藉瓨鍦ㄧ殑鍘嗗彶鏁版嵁.

			Node nd = new Node();
			nd.setNodeID(Integer.parseInt(this.getNo() + "01"));
			nd.setName("寮�濮嬭妭鐐�"); // "寮�濮嬭妭鐐�";
			nd.setStep(1);
			nd.setFK_Flow(this.getNo());
			nd.setFlowName(this.getName());
			nd.setHisNodePosType(NodePosType.Start);
			nd.setHisNodeWorkType(NodeWorkType.StartWork);
			nd.setX(200);
			nd.setY(150);
			nd.setICON("鍓嶅彴");

			// 澧炲姞浜嗕袱涓粯璁ゅ�煎�� . 2016.11.15. 鐩殑鏄鍒涘缓鐨勮妭鐐癸紝灏卞彲浠ヤ娇鐢�.
			nd.setCondModel(CondModel.SendButtonSileSelect); // 榛樿鐨勫彂閫佹柟鍚�.
			nd.setHisDeliveryWay(DeliveryWay.BySelected); // 涓婁竴姝ュ彂閫佷汉鏉ラ�夋嫨.
			nd.setFormType(NodeFormType.FoolForm); // 琛ㄥ崟绫诲瀷.
			nd.Insert();
			nd.CreateMap();
			// nd.getHisWork().CheckPhysicsTable();

			// 鍛ㄦ湅@浜庡簡娴烽渶瑕佺炕璇�.
			CreatePushMsg(nd);
			// 閫氱敤鐨勪汉鍛橀�夋嫨鍣�.
			BP.WF.Template.Selector select = new Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();

			nd = new Node();
			nd.setNodeID(Integer.parseInt(this.getNo() + "02"));
			nd.setName("鑺傜偣2"); // "缁撴潫鑺傜偣";
			nd.setStep(2);
			nd.setFK_Flow(this.getNo());
			nd.setFlowName(this.getName());
			nd.setHisNodePosType(NodePosType.End);
			nd.setHisNodeWorkType(NodeWorkType.Work);
			nd.setX(200);
			nd.setY(250);
			nd.setICON("瀹℃牳");

			// 澧炲姞浜嗕袱涓粯璁ゅ�煎�� . 2016.11.15. 鐩殑鏄鍒涘缓鐨勮妭鐐癸紝灏卞彲浠ヤ娇鐢�.
			nd.setCondModel(CondModel.SendButtonSileSelect); // 榛樿鐨勫彂閫佹柟鍚�.
			nd.setHisDeliveryWay(DeliveryWay.BySelected); // 涓婁竴姝ュ彂閫佷汉鏉ラ�夋嫨.
			nd.setFormType(NodeFormType.FoolForm); // 琛ㄥ崟绫诲瀷.

			// 涓哄垱寤鸿妭鐐硅缃粯璁ゅ�� @浜庡簡娴�.
			String fileNewNode = SystemConfig.getPathOfDataUser() + "XML/DefaultNewNodeAttr.xml";
			if ((new java.io.File(fileNewNode)).isFile()) {
				DataSet ds_NodeDef = new DataSet();
				ds_NodeDef.readXml(fileNewNode);

				DataTable dt = ds_NodeDef.Tables.get(0);
				for (DataColumn dc : dt.Columns) {
					nd.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
				}
			}

			nd.Insert();
			nd.CreateMap();
			// nd.getHisWork().CheckPhysicsTable();
			// 鍛ㄦ湅@浜庡簡娴烽渶瑕佺炕璇�.
			CreatePushMsg(nd);

			// 閫氱敤鐨勪汉鍛橀�夋嫨鍣�.
			select = new Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();

			BP.Sys.MapData md = new BP.Sys.MapData();
			md.setNo("ND" + Integer.parseInt(this.getNo()) + "Rpt");
			md.setName(this.getName());
			md.Save();

			// 瑁呰浇妯＄増.
			String file = BP.Sys.SystemConfig.getPathOfDataUser() + "XML/TempleteSheetOfStartNode.xml";
			if ((new java.io.File(file)).isFile() && 1 == 2) {
				// 濡傛灉瀛樺湪寮�濮嬭妭鐐硅〃鍗曟ā鐗�
				DataSet ds = new DataSet();
				ds.readXml(file);

				String nodeID = "ND" + Integer.parseInt(this.getNo() + "01");
				BP.Sys.MapData.ImpMapData(nodeID, ds);
			}

			// this.DoCheck_CheckRpt(this.getHisNodes());
			// Flow.RepareV_FlowData_View();
			return this.getNo();
		} catch (RuntimeException ex) {
			/**
			 * 鍒犻櫎鍨冨溇鏁版嵁.
			 */
			this.DoDelete();

			// 鎻愮ず閿欒.
			throw new RuntimeException("鍒涘缓娴佺▼閿欒:" + ex.getMessage());
		}
	}

	/**
	 * 鍐欏叆鏉冮檺
	 * 
	 * @param flowSort
	 */
	public final void WritToGPM(String flowSort) {

		if (BP.WF.Glo.getOSModel() == OSModel.OneMore) {

		}
		// return;
		/*
		 * if (Glo.getOSModel() == OSModel.OneMore) { String sql = "";
		 * 
		 * try { sql = "DELETE FROM GPM_Menu WHERE FK_App='" +
		 * SystemConfig.getSysNo() + "' AND Flag='Flow" + this.getNo() + "'";
		 * DBAccess.RunSQL(sql); } catch (java.lang.Exception e) { }
		 * 
		 * // 寮�濮嬬粍缁囧彂璧锋祦绋嬬殑鏁版嵁. // 鍙栧緱璇ユ祦绋嬬殑鐩綍缂栧彿. sql =
		 * "SELECT No FROM GPM_Menu WHERE Flag='FlowSort" + flowSort +
		 * "' AND FK_App='" + BP.Sys.SystemConfig.getSysNo() + "'"; String
		 * parentNoOfMenu = DBAccess.RunSQLReturnStringIsNull(sql, null); if
		 * (parentNoOfMenu == null) { throw new RuntimeException("@娌℃湁鎵惧埌璇ユ祦绋嬬殑(" +
		 * BP.Sys.SystemConfig.getSysNo() + ")鐩綍鍦℅PM绯荤粺涓�,璇烽噸鏂版柊寤烘鐩綍銆�"); }
		 * 
		 * // 鍙栧緱璇ュ姛鑳界殑涓婚敭缂栧彿. String treeNo =
		 * String.valueOf(DBAccess.GenerOID("BP.GPM.Menu"));
		 * 
		 * // 鎻掑叆娴佺▼鍚嶇О. String url = SystemConfig.getSysNo() +
		 * "WF/MyFlow.htm?FK_Flow=" + this.getNo() + "&FK_Node=" +
		 * Integer.parseInt(this.getNo()) + "01";
		 * 
		 * sql =
		 * "INSERT INTO GPM_Menu(No,Name,ParentNo,IsDir,MenuType,FK_App,IsEnable,Flag,Url)"
		 * ; sql += " VALUES('{0}','{1}','{2}',{3},{4},'{5}',{6},'{7}','{8}')";
		 * sql = String.format(sql, treeNo, this.getName(), parentNoOfMenu, 0,
		 * 4, SystemConfig.getSysNo(), 1, "Flow" + this.getNo(), url);
		 * DBAccess.RunSQL(sql); }
		 */

		/// #endregion
	}

	/**
	 * 妫�鏌ユ姤琛�
	 * 
	 * @throws Exception
	 * 
	 */
	public final void CheckRpt() throws Exception {
		this.DoCheck_CheckRpt(this.getHisNodes());
	}

	/**
	 * 鏇存柊涔嬪墠鍋氭鏌�
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean beforeUpdate() throws Exception {
		this.setVer(BP.DA.DataType.getCurrentDataTimess());
		Node.CheckFlow(this);
		return super.beforeUpdate();
	}

	/**
	 * 鏇存柊鐗堟湰鍙�
	 * 
	 */
	public static void UpdateVer(String flowNo) {
		String sql = "UPDATE WF_Flow SET VER='" + BP.DA.DataType.getCurrentDataTimess() + "' WHERE No='" + flowNo + "'";
		DBAccess.RunSQL(sql);
	}

	// region 鐗堟湰绠＄悊
	/**
	 * 鍒涘缓鏂扮増鏈�
	 * 
	 * @return
	 */
	public String VerCreateNew() {
		try {
			// 鐢熸垚妯℃澘
			String file = GenerFlowXmlTemplete();
			Flow newFlow = Flow.DoLoadFlowTemplate(this.getFK_FlowSort(), file, ImpFlowTempleteModel.AsNewFlow);
			newFlow.setPTable(this.getPTable());
			newFlow.setFK_FlowSort(""); // 涓嶈兘鏄剧ず鍦ㄦ祦绋嬫爲涓�
			newFlow.setName(this.getName());
			newFlow.setVer(DataType.getCurrentDataTime());
			newFlow.setIsCanStart(false); // 涓嶈兘鍙戣捣
			newFlow.DirectUpdate();
			return newFlow.getNo();
		} catch (Exception e) {
			return "err@" + e.getMessage();
		}
	}

	/**
	 * 璁剧疆褰撳墠鐨勭増鏈负鏂扮増鏈�
	 * 
	 * @return
	 */
	public String VerSetCurrentVer() {
		String sql = "SELECT FK_FlowSort FROM WF_Flow WHERE PTable='" + this.getPTable() + "' AND FK_FlowSort!='' ";
		String flowSort = DBAccess.RunSQLReturnStringIsNull(sql, "");
		if (DataType.IsNullOrEmpty(flowSort))
			return "err@娌℃湁鎵惧埌涓荤増鏈�,璇疯仈绯荤鐞嗗憳.";
		sql = "UPDATE WF_Flow SET FK_FlowSort ='',IsCanStart=0 WHERE PTable='" + this.getPTable() + "' ";
		DBAccess.RunSQL(sql);

		sql = "UPDATE WF_Flow SET FK_FlowSort ='" + flowSort + "',IsCanStart=1 WHERE No='" + this.getNo() + "' ";
		DBAccess.RunSQL(sql);
		return "info@璁剧疆鎴愬姛";
	}

	/**
	 * 鑾峰緱鐗堟湰鍒楄〃
	 * 
	 * @return
	 */
	public String VerGenerVerList() {
		// if (this.FK_FlowSort.Equals("") == true)
		// return "err@褰撳墠鐗堟湰涓哄垎鏀増鏈紝鎮ㄦ棤娉曠鐞嗭紝鍙湁涓荤増鏈墠鑳界鐞嗐��";

		DataTable dt = new DataTable();
		dt.Columns.Add("Ver");
		dt.Columns.Add("No");
		dt.Columns.Add("Name");
		dt.Columns.Add("IsRel");
		dt.Columns.Add("NumOfRuning");
		dt.Columns.Add("NumOfOK");
		try {

			String ptable = this.GetValStringByKey(FlowAttr.PTable);
			if (DataType.IsNullOrEmpty(ptable)) {
				this.SetValByKey(FlowAttr.PTable, this.getPTable());
				this.DirectUpdate();
			}

			String sql = "SELECT No FROM WF_Flow WHERE PTable='" + this.getPTable() + "' ";
			Flows fls = new Flows();
			fls.RetrieveInSQL(sql);
			for (Flow item : fls.ToJavaList()) {
				DataRow dr = dt.NewRow();
				dr.put("Ver", item.getVer());
				dr.put("No", item.getNo());
				dr.put("Name", item.getName());
				if (DataType.IsNullOrEmpty(item.getFK_FlowSort())) {
					dr.put("IsRel", "0");
				} else {
					dr.put("IsRel", "1");
				}
				dr.put("NumOfRuning",
						DBAccess.RunSQLReturnValInt("SELECT COUNT(WORKID) FROM WF_GenerWorkFlow WHERE FK_FLOW='"
								+ item.getNo() + "' AND WFState=2"));
				dr.put("NumOfOK",
						DBAccess.RunSQLReturnValInt("SELECT COUNT(WORKID) FROM WF_GenerWorkFlow WHERE FK_FLOW='"
								+ item.getNo() + "' AND WFState=3"));
				dt.Rows.add(dr);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "err@鐗堟湰鍒楄〃鑾峰彇澶辫触 " + e.getMessage();
		}
		return BP.Tools.Json.ToJson(dt);
	}

	// endregion 鐗堟湰绠＄悊

	public final String DoDelete() throws Exception {

		// 妫�鏌ユ祦绋嬫湁娌℃湁鐗堟湰绠＄悊锛�
		if (this.getFK_FlowSort().length() > 1) {
			String str = "SELECT * FROM WF_Flow WHERE PTable='" + this.getPTable() + "' AND FK_FlowSort='' ";
			DataTable dt = DBAccess.RunSQLReturnTable(str);
			if (dt.Rows.size() >= 1)
				return "err@鍒犻櫎娴佺▼鍑洪敊锛岃娴佺▼涓嬫湁[" + dt.Rows.size() + "]涓瓙鐗堟湰鎮ㄤ笉鑳藉垹闄ゃ��";
		}

		// 鍒犻櫎娴佺▼鏁版嵁.
		this.DoDelData();

		String sql = "";
		// sql = " DELETE FROM WF_chofflow WHERE FK_Flow='" + this.getNo() +
		// "'";
		sql += "@ DELETE  FROM WF_GenerWorkerlist WHERE FK_Flow='" + this.getNo() + "'";
		sql += "@ DELETE FROM  WF_GenerWorkFlow WHERE FK_Flow='" + this.getNo() + "'";
		sql += "@ DELETE FROM  WF_Cond WHERE FK_Flow='" + this.getNo() + "'";

		// 鍒犻櫎娑堟伅閰嶇疆. 鍛ㄦ湅@浜庡簡娴�.
		sql += "@ DELETE FROM WF_PushMsg WHERE FK_Flow='" + this.getNo() + "'";
		// 鍒犻櫎宀椾綅鑺傜偣銆�
		sql += "@ DELETE  FROM  WF_NodeStation WHERE FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getNo() + "')";

		// 鍒犻櫎鏂瑰悜銆�
		sql += "@ DELETE FROM  WF_Direction WHERE FK_Flow='" + this.getNo() + "'";

		// 鍒犻櫎鑺傜偣缁戝畾淇℃伅.
		sql += "@ DELETE FROM WF_FrmNode  WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()
				+ "')";

		sql += "@ DELETE FROM WF_NodeEmp  WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()
				+ "')";
		sql += "@ DELETE FROM WF_CCEmp WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()
				+ "')";
		sql += "@ DELETE FROM WF_CCDept WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()
				+ "')";
		sql += "@ DELETE FROM WF_CCStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getNo() + "')";

		sql += "@ DELETE FROM WF_NodeReturn WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getNo() + "')";

		sql += "@ DELETE FROM WF_NodeDept WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()
				+ "')";
		sql += "@ DELETE FROM WF_NodeStation WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getNo() + "')";
		sql += "@ DELETE FROM WF_NodeEmp WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='" + this.getNo()
				+ "')";

		sql += "@ DELETE FROM WF_NodeToolbar WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getNo() + "')";
		sql += "@ DELETE FROM WF_SelectAccper WHERE   FK_Node IN (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getNo() + "')";
		// sql += "@ DELETE FROM WF_TurnTo WHERE FK_Node IN (SELECT NodeID FROM
		// WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		// 鍒犻櫎d2d鏁版嵁.
		// sql += "@GO DELETE WF_M2M WHERE FK_Node IN (SELECT NodeID FROM
		// WF_Node WHERE FK_Flow='" + this.getNo() + "')";
		//// 鍒犻櫎閰嶇疆.
		// sql += "@ DELETE FROM WF_FAppSet WHERE NodeID IN (SELECT NodeID FROM
		// WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		//// 澶栭儴绋嬪簭璁剧疆
		// sql += "@ DELETE FROM WF_FAppSet WHERE NodeID in (SELECT NodeID FROM
		//// WF_Node WHERE FK_Flow='" + this.getNo() + "')";

		// 鍒犻櫎鍗曟嵁.
		sql += "@ DELETE FROM WF_BillTemplate WHERE  NodeID in (SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getNo() + "')";
		// 鍒犻櫎鏉冮檺鎺у埗.
		sql += "@ DELETE FROM Sys_FrmSln WHERE FK_Flow='" + this.getNo() + "'";
		// 鑰冩牳琛�
		sql += "@ DELETE FROM WF_CH WHERE FK_Flow='" + this.getNo() + "'";
		// 鍒犻櫎鎶勯��
		sql += "@ DELETE FROM WF_CCList WHERE FK_Flow='" + this.getNo() + "'";
		Nodes nds = new Nodes(this.getNo());
		for (Node nd : nds.ToJavaList()) {
			// 鍒犻櫎鑺傜偣鎵�鏈夌浉鍏崇殑涓滆タ.
			// sql += "@ DELETE FROM Sys_MapM2M WHERE FK_MapData='ND" +
			// nd.getNodeID() + "'";
			nd.Delete();
		}

		sql += "@ DELETE  FROM WF_Node WHERE FK_Flow='" + this.getNo() + "'";
		sql += "@ DELETE  FROM  WF_LabNote WHERE FK_Flow='" + this.getNo() + "'";

		// 鍒犻櫎鍒嗙粍淇℃伅
		sql += "@ DELETE FROM Sys_GroupField WHERE FrmID NOT IN(SELECT NO FROM Sys_MapData)";

		/// #region 鍒犻櫎娴佺▼鎶ヨ〃,鍒犻櫎杞ㄨ抗
		MapData md = new MapData();
		// sunxd 20170714
		// Integer.parseInt()涓鍍忎负绌烘椂Integer.parseInt()浼氭姤閿�
		// Integer.parseInt()涓鍍忎綔浜嗙┖鍊艰浆鎹�
		String mdNo = "ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Rpt";
		if (DBAccess.IsExitsTabPK(mdNo)) {
			md.setNo(mdNo);
			md.Delete();
		}

		// 鍒犻櫎瑙嗗浘.
		if (DBAccess.IsExitsObject("V_" + this.getNo())) {
			DBAccess.RunSQL("DROP VIEW V_" + this.getNo());
		}

		// 鍒犻櫎杞ㄨ抗.
		// sunxd 20170714
		// Integer.parseInt()涓鍍忎负绌烘椂Integer.parseInt()浼氭姤閿�
		// Integer.parseInt()涓鍍忎綔浜嗙┖鍊艰浆鎹�
		if (DBAccess.IsExitsObject(
				"ND" + (this.getNo().equals("") ? "" : Integer.parseInt(this.getNo())) + "Track") == true) {
			DBAccess.RunSQL("DROP TABLE ND" + Integer.parseInt(this.getNo()) + "Track ");
		}

		/// #endregion 鍒犻櫎娴佺▼鎶ヨ〃,鍒犻櫎杞ㄨ抗.

		// 鎵ц褰曞埗鐨剆ql scripts.
		DBAccess.RunSQLs(sql);
		this.Delete(); // 鍒犻櫎闇�瑕佺Щ闄ょ紦瀛�.

		// Flow.RepareV_FlowData_View();

		//// 鍒犻櫎鏉冮檺绠＄悊
		// if (BP.WF.Glo.OSModel == OSModel.OneMore)
		// {
		// try
		// {
		// DBAccess.RunSQL("DELETE FROM GPM_Menu WHERE Flag='Flow" +
		//// this.getNo() + "' AND FK_App='" + SystemConfig.getSysNo() + "'");
		// }
		// catch
		// {
		// }
		// }

		return "info@鍒犻櫎鎴愬姛.";
	}

	/**
	 * 鏄惁鍚敤鏁版嵁妯＄増锛�
	 * 
	 */
	public final boolean getIsDBTemplate() {
		return this.GetValBooleanByKey(FlowAttr.IsDBTemplate);
	}

	/// #endregion
}