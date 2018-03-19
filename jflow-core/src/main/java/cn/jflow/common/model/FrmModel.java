package cn.jflow.common.model;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Paras;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Sys.FrmType;
import BP.Sys.GEDtl;
import BP.Sys.GEEntity;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.SignType;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.Web.WebUser;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.TextBox;

public class FrmModel extends EnModel {

	private int OIDPKVal;

	public String TB_SealData_Value, TB_SealFile_Value;

	public Button Btn_Save, Btn_Print;
	
	public FrmModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	// /#region 属性
	public final boolean getIsSign() {
		String isSign = (String) SystemConfig.getAppSettings().get("IsSign");

		if (StringHelper.isNullOrEmpty(isSign) || isSign.equals("0")) {
			return false;
		} else {
			return true;
		}
	}

	public final int getOID() {
		String cworkid = get_request().getParameter("CWorkID");
		if (StringHelper.isNullOrEmpty(cworkid) == false
				&& Integer.parseInt(cworkid) != 0) {
			return Integer.parseInt(cworkid);
		}

		String oid = get_request().getParameter("WorkID");
		if (oid == null || oid.equals("")) {
			oid = get_request().getParameter("OID");
		}
		if (oid == null || oid.equals("")) {
			oid = "0";
		}
		return Integer.parseInt(oid);
	}

	/**
	 * 延续流程ID
	 */
	public final int getCWorkID() {
		try {
			return Integer.parseInt(get_request().getParameter("CWorkID"));
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	/**
	 * 父流程ID
	 */
	public final int getPWorkID() {
		try {
			return Integer.parseInt(get_request().getParameter("PWorkID"));
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	public final int getOIDPKVal() {

		return OIDPKVal;
	}

	public final void setOIDPKVal(int value) {
		OIDPKVal = value;
	}

	public final boolean getIsEdit() {
		String isEdit = get_request().getParameter("IsEdit");
		if (StringHelper.isNullOrEmpty(isEdit) || isEdit.equals("1")) {
			return true;
		} else if (isEdit.equals("0")) {
			return false;
		}
		return true;
	}

	public final String getSID() {
		return get_request().getParameter("PWorkID");
	}

	public final boolean getIsPrint() {
		if (get_request().getParameter("IsPrint").equals("1")) {
			return true;
		}
		return false;
	}

	private FrmNode _HisFrmNode = null;

	public final FrmNode getHisFrmNode() {
		if (_HisFrmNode == null) {
			_HisFrmNode = new FrmNode();
		}
		return _HisFrmNode;
	}

	public final void setHisFrmNode(FrmNode value) {
		_HisFrmNode = value;
	}

	private String _height = "";

	public final String getHeight() {
		return _height;
	}

	public final void setHeight(String value) {
		_height = value;
	}

	private String _width = "";

	public final String getWidth() {
		return _width;
	}

	public final void setWidth(String value) {
		_width = value;
	}

	private int  FK_Node;
	public final int getFK_Node() {
		try
		 {
			 String nodeid = getParameter("NodeID");
			 if (nodeid == null)
			 {
				 nodeid = getParameter("FK_Node");
			 }
			 return Integer.parseInt(nodeid);
		 }
		 catch (java.lang.Exception e)
		 {
			 if (this.getFK_Flow()==null || "".equals(this.getFK_Flow()))
			 {
				 return 0;
			 }
			 else
			 {
				 return Integer.parseInt(this.getFK_Flow());// 0; 有可能是流程调用独立表单。
			 }
		 }
	}

	public final void setFK_Node(int value) {
		this.FK_Node = value;
	}
	// /#endregion 属性

	public void loadModel() throws IOException {
		Btn_Save = new Button();
		Btn_Print = new Button();
		// /#region 属性
		String sealName = null;
		// /#endregion 属性
		// /#warning 没有缓存经常预览与设计不一致
		MapData md = new MapData();
		md.setNo(this.getFK_MapData());
		String isTest = StringHelper.isEmpty(getParameter("IsTest"), "");
		if (isTest.equals("1")) {
			md.RepairMap();
			BP.Sys.SystemConfig.DoClearCash_del();
		}

		 if (get_request().getParameter("IsLoadData")!=null&&get_request().getParameter("IsLoadData").equals("1"))
		 {
			 this.IsLoadData=true;
			 //setIsLoadData(true);
		 }

		if (md.RetrieveFromDBSources() == 0 && md.getName().length() > 3) {
			// 如果没有找到，就可能是dtl。
			if (md.getHisFrmType().equals(FrmType.Url) || md.getHisFrmType().equals(FrmType.Silverlight)) {
				String no = get_request().getParameter("NO");
				String urlParas = "OID=" + this.getOID() + "&NO=" + no
						+ "&WorkID=" + this.getWorkID() + "&FK_Node="
						+ this.getFK_Node() + "&UserNo=" + WebUser.getNo()
						+ "&SID=" + this.getSID();
				// 如果是URL.
				if (md.getUrl().contains("?") == true) {
					this.get_response().sendRedirect(
							md.getUrl() + "&" + urlParas);
				} else {
					this.get_response().sendRedirect(
							md.getUrl() + "?" + urlParas);
				}
				return;
			}

			// 没有找到此map.
			MapDtl dtl = new MapDtl(this.getFK_MapData());
			GEDtl dtlEn = dtl.getHisGEDtl();
			dtlEn.SetValByKey("OID", this.getFID());

			if (dtlEn.getEnMap().getAttrs().size() <= 0) {
				md.RepairMap();
				this.get_response().sendRedirect(
						get_request().getRequestURL() + "?"
								+ get_request().getQueryString());//??
				return;
			}

			int i = dtlEn.RetrieveFromDBSources();

			String[] paras = this.get_request().getQueryString().split("&");
			for (String str : paras) {
				if (StringHelper.isNullOrEmpty(str)
						|| str.contains("=") == false) {
					continue;
				}

				String[] kvs = str.split("=");
				dtlEn.SetValByKey(kvs[0], kvs[1]);
			}
			setWidth(md.getMaxRight() + md.getMaxLeft() * 2 + 10 + "");
			if (Float.parseFloat(getWidth()) < 500) {
				setWidth("900");
			}
			float maxEnd = md.getMaxEnd();
			float frmHeight = md.getFrmH();

			if (maxEnd > frmHeight) {
				setHeight(String.valueOf(maxEnd));
			} else {
				setHeight(String.valueOf(frmHeight));
			}
			
			 if (frmHeight <= 800)
			 {
				 setHeight("800");
			 }

			appendPub("<div id=divCCForm style='width:" + getWidth()
					+ "px;height:" + getHeight() + "px' >");
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			if (md.getHisFrmType() == FrmType.FreeFrm) {
				BindCCForm(dtlEn, this.getFK_MapData(), !this.getIsEdit(),
						0,this.IsLoadData);
				//BindCCForm(dtlEn, this.getFK_MapData(), !this.getIsEdit(), 0, this.IsLoadData);
			}

			if (md.getHisFrmType() == FrmType.FoolForm) {
				BindCCForm(dtlEn, this.getFK_MapData(), !this.getIsEdit(),0,this.IsLoadData);
			}

			this.AddJSEvent(dtlEn);
			appendPub("</div>");
		} else {
			// 如果没有找到，就可能是dtl。
			if (md.getHisFrmType().equals(FrmType.Url) || md.getHisFrmType().equals(FrmType.Silverlight)) {
				String no = get_request().getParameter("NO");
				String urlParas = "OID=" + this.getOID() + "&NO=" + no
						+ "&WorkID=" + this.getWorkID() + "&FK_Node="
						+ this.getFK_Node() + "&IsEdit="
						+ (new Boolean(this.getIsEdit())).toString()
						+ "&UserNo=" + WebUser.getNo() + "&SID="
						+ this.getSID();
				// 如果是URL.
				if (md.getUrl().contains("?") == true) {
					this.get_response().sendRedirect(
							md.getUrl() + "&" + urlParas);
				} else {
					this.get_response().sendRedirect(
							md.getUrl() + "?" + urlParas);
				}
				return;
			}

			if (md.getHisFrmType().equals(FrmType.WordFrm)) {
				String no = get_request().getParameter("NO");
				String urlParas = "OID=" + this.getOID() + "&NO=" + no
						+ "&WorkID=" + this.getWorkID() + "&FK_Node="
						+ this.getFK_Node() + "&UserNo=" + WebUser.getNo()
						+ "&SID=" + this.getSID() + "&FK_MapData="
						+ this.getFK_MapData() + "&OIDPKVal="
						+ this.getOIDPKVal() + "&FID=" + this.getFID()
						+ "&FK_Flow=" + this.getFK_Flow();
				// 如果是URL.
				String requestParas = this.get_request().getQueryString();
				String[] parasArrary = requestParas.split("&");
				for (String str : parasArrary) {
					if (StringHelper.isNullOrEmpty(str)
							|| str.contains("=") == false) {
						continue;
					}
					String[] kvs = str.split("=");
					if (urlParas.contains(kvs[0])) {
						continue;
					}
					urlParas += "&" + kvs[0] + "=" + kvs[1];
				}
				if (md.getUrl().contains("?") == true) {
					this.get_response().sendRedirect(
							"WordFrm.jsp?1=2" + "&" + urlParas);
				} else {
					this.get_response().sendRedirect(
							"WordFrm.jsp" + "?" + urlParas);
				}
				return;
			}
			
			if (md.getHisFrmType().equals(FrmType.ExcelFrm)) {
				this.get_response().sendRedirect(
						"FrmExcel.jsp?1=2" + "&"
								+ get_request().getQueryString());
				return;
			}
			
			

			GEEntity en = md.getHisGEEn();

			// /#region 求出pk 值.
			int pk = this.getOID();
			String nodeid = (new Integer(this.getFK_Node())).toString();
			if (!nodeid.equals("0")
					&& StringHelper.isNullOrEmpty(this.getFK_Flow()) == false) {
				// 说明是流程调用它， 就要判断谁是表单的PK.
				FrmNode fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(),
						this.getFK_MapData());
				switch (fn.getWhoIsPK()) {
				case FID:
					pk = (int) this.getFID();
					if (pk == 0) {
						throw new RuntimeException("@没有接收到参数FID");
					}
					break;
				case CWorkID: // 延续流程ID
					pk = this.getCWorkID();
					if (pk == 0) {
						throw new RuntimeException("@没有接收到参数CWorkID");
					}
					break;
				case PWorkID: // 父流程ID
					pk = this.getPWorkID();
					if (pk == 0) {
						throw new RuntimeException("@没有接收到参数PWorkID");
					}
					break;
				case OID:
				default:
					break;
				}

				// Node nd = new Node(int.Parse(nodeid));
				// if (nd.HisRunModel == RunModel.SubThread
				// && nd.HisSubThreadType == SubThreadType.UnSameSheet
				// && this.FK_MapData != "ND" + nd.NodeID)
				// {
				// /*如果是子线程, 并且是异表单节点.*/
				// pk = this.FID; //
				// 是子线程，并且是异表单的子线程，并且不是节点表单。这样设置是为了到合流点上能够按FID进行表单数据汇总.
				// }
			}
			en.SetValByKey("OID", pk);
			// /#endregion 求出pk 值.
			// String no = get_request().getParameter("NO");
			if (en.getEnMap().getAttrs().size() <= 0) {
				md.RepairMap(); // 让他刷新一下,重新进入.
				this.get_response().sendRedirect(
						get_request().getRequestURL()
								.append(get_request().getQueryString())
								.toString());
				return;
			}

			int i = en.RetrieveFromDBSources();

			if (i == 0) {
				en.ResetDefaultValAllAttr();
				try {
					en.DirectInsert();
				} catch (Exception e) {
					md.RepairMap();
					en.CheckPhysicsTable();
					e.printStackTrace();
				}
			}

			String[] paras = get_request().getQueryString().split("&");
			for (String str : paras) {
				if (StringHelper.isNullOrEmpty(str)
						|| str.contains("=") == false) {
					continue;
				}

				String[] kvs = str.split("=");
				if(kvs.length>1){
					en.SetValByKey(kvs[0], kvs[1]);
				}
				
			}

			if (en.toString().equals("0")) {
				en.SetValByKey("OID", pk);
			}
			this.setOIDPKVal(pk);

			// /#region 处理表单权限控制方案
			setWidth(md.getFrmW()+"");
			if (Float.parseFloat(getWidth()) < 900) {
				setWidth("900");
			}

			float maxEnd = md.getMaxEnd();
			float frmHeight = md.getFrmH();

			if (maxEnd > frmHeight) {
				setHeight(String.valueOf(maxEnd));
			} else {
				setHeight(String.valueOf(frmHeight));
			}
			appendPub("<div id=divCCForm style='width:" + getWidth()
					+ "px;height:" + getHeight() + "px' >");
			if (!StringHelper.isNullOrEmpty(nodeid)) {
				setFk_node(String.valueOf(this.getFK_Node()));
				// 处理表单权限控制方案
				this.setHisFrmNode(new FrmNode());
				int ii = this.getHisFrmNode().Retrieve(FrmNodeAttr.FK_Frm,
						this.getFK_MapData(), FrmNodeAttr.FK_Node,
						Integer.parseInt(nodeid));

				if (ii == 0 || this.getHisFrmNode().getFrmSln() == 0) {
					// 说明没有配置,或者方案编号为默认就不用处理,
					BindCCForm(en, this.getFK_MapData(), !this.getIsEdit(), 0,this.IsLoadData);
				} else {
					FrmFields fls = new FrmFields(this.getFK_MapData(), this
							.getHisFrmNode().getFrmSln());
					// fls.Retrieve(FrmFieldAttr.FK_MapData, this.FK_MapData,
					// FrmFieldAttr.FK_Node, this.HisFrmNode.FrmSln);

					// 求出集合.
					MapAttrs mattrs = new MapAttrs(this.getFK_MapData());
					for (FrmField item : fls.ToJavaList()) {
						for (MapAttr attr : mattrs.ToJavaList()) {
							if (!attr.getKeyOfEn().equalsIgnoreCase(
									item.getKeyOfEn())) {
								continue;
							}

							if (item.getIsSigan()) {
								item.setUIIsEnable(false);
							}
							if (attr.getSignType() == SignType.CA) {
								// long workId = this.getOID();
								FrmField keyOfEn = new FrmField();
								QueryObject info = new QueryObject(keyOfEn);
								info.AddWhere(FrmFieldAttr.FK_Node,
										this.getFK_Node());
								info.addAnd();
								info.AddWhere(FrmFieldAttr.FK_MapData,
										attr.getFK_MapData());
								info.addAnd();
								info.AddWhere(FrmFieldAttr.KeyOfEn,
										attr.getKeyOfEn());
								info.addAnd();
								info.AddWhere(MapAttrAttr.UIIsEnable, "1");
								if (info.DoQuery() > 0) {
									sealName = en.GetValStrByKey(attr
											.getKeyOfEn());
								}
							}
							// if (attr.UIContralType == UIContralType.DDL)
							// attr.UIIsEnable = item.UIIsEnable;
							// else
							attr.setUIIsEnable(item.getUIIsEnable());

							attr.setUIVisible(item.getUIVisible());
							attr.setIsSigan(item.getIsSigan());
							attr.setDefValReal(item.getDefVal());
						}
					}

					// /#region 设置默认值.
					boolean isHave = false;

					for (MapAttr attr : mattrs.ToJavaList()) {
						if (attr.getUIIsEnable()) {
							continue;
						}

						if (attr.getDefValReal().contains("@") == false) {
							continue;
						}

						en.SetValByKey(attr.getKeyOfEn(), attr.getDefVal());
						isHave = true;
					}
					if (isHave) {
						en.DirectUpdate(); // 让其直接更新.
					}
					// /#endregion 设置默认值.

					// 按照当前方案绑定表单.
					BindCCForm(en, md, mattrs, this.getFK_MapData(),
							!this.getIsEdit(), 800/*Integer.parseInt(getWidth())*/,IsLoadData);

					// /#region 检查必填项
					String scriptCheckFrm = "";
					scriptCheckFrm += "\t\n<script type='text/javascript' >";
					scriptCheckFrm += "\t\n function CheckFrmSlnIsNull(){ ";
					scriptCheckFrm += "\t\n var isPass = true;";
					scriptCheckFrm += "\t\n var alloweSave = true;";
					scriptCheckFrm += "\t\n var erroMsg = '提示信息:';";

					// 表单权限设置为必填项
					// 查询出来，需要不为空的
					Paras ps = new Paras();
					ps.SQL = "SELECT KeyOfEn, Name FROM Sys_FrmSln WHERE FK_MapData="
							+ ps.getDBStr()
							+ "FK_MapData AND FK_Node="
							+ ps.getDBStr()
							+ "FK_Node AND IsNotNull="
							+ ps.getDBStr() + "IsNotNull";
					ps.Add(FrmFieldAttr.FK_MapData, this.getFK_MapData());
					ps.Add(FrmFieldAttr.FK_Node, this.getFK_Node());
					ps.Add(FrmFieldAttr.IsNotNull, 1);
					// 获取当前所有控件
					HashMap<String, BaseWebControl> ctrlMap = HtmlUtils
							.httpParser(Pub.toString(), false);
					// 查询
					DataTable dtKeys = DBAccess.RunSQLReturnTable(ps);
					// 检查数据是否完整.
					for (DataRow dr : dtKeys.Rows) {
						String key = dr.getValue(0).toString();
						String name = dr.getValue(1).toString();

						TextBox TB_NotNull = (TextBox) ctrlMap.get("TB_" + key);
						if (TB_NotNull != null) {
							scriptCheckFrm += "\t\n try{  ";
							scriptCheckFrm += "\t\n var element = document.getElementById('"
									+ TB_NotNull.getId() + "');";
							// 验证输入的正则格式
							scriptCheckFrm += "\t\n if(element && element.readOnly == true) return;";
							scriptCheckFrm += "\t\n isPass = EleSubmitCheck(element,'.{1}','"
									+ name + "，不能为空。');";
							scriptCheckFrm += "\t\n  if(isPass == false){";
							scriptCheckFrm += "\t\n    alloweSave = false;";
							scriptCheckFrm += "\t\n    erroMsg += '" + name
									+ "，不能为空。';";
							scriptCheckFrm += "\t\n  }";
							scriptCheckFrm += "\t\n } catch(e) { ";
							scriptCheckFrm += "\t\n  alert(e.name  + e.message);  return false;";
							scriptCheckFrm += "\t\n } ";
						}
					}

					// scriptCheckFrm += "\t\n if(alloweSave == false){";
					// scriptCheckFrm += "\t\n     alert(erroMsg);";
					// scriptCheckFrm += "\t\n  } ";
					scriptCheckFrm += "\t\n return alloweSave; } ";
					scriptCheckFrm += "\t\n</script>";
					// /#endregion
					// 检查必填项
					appendPub(scriptCheckFrm);

				}
			} else {
				BindCCForm(en, this.getFK_MapData(), !this.getIsEdit(), 0,this.IsLoadData);
			}
			appendPub("</div>");
			// /#endregion

			// if (!IsPostBack)
			// {
			if (md.getIsHaveCA()) {
				// /#region 检查是否有ca签名.
				/*if (md.getIsHaveCA() == true) {
					if (StringHelper.isNullOrEmpty(sealName)) {
						sealName = WebUser.getNo();
					}
					String basePath = Glo.getIntallPath()
							+ "/DataUser/Siganture/" + getWorkID();
					File file = new File(basePath);
					if (!file.exists()) {
						file.mkdirs();
					}*/
					// String basePath = Server.MapPath("~/DataUser/Siganture/"
					// + getWorkID());
					//
					// if (!System.IO.Directory.Exists(basePath))
					// {
					// System.IO.Directory.CreateDirectory(basePath);
					// }

					// basePath = "C:\\";

					/*this.TB_SealFile_Value = basePath + "\\" + sealName
							+ ".jpg";
					// /#region 获取存储的 签名信息

					BP.Tools.WFSealData sealData = new BP.Tools.WFSealData();
					sealData.RetrieveByAttrAnd(BP.Tools.WFSealDataAttr.OID,
							getWorkID(), BP.Tools.WFSealDataAttr.FK_Node,
							getFK_Node());
					// sealData.RetrieveFromDBSources();
					if (!StringHelper.isNullOrEmpty(sealData.getSealData())) {
						this.TB_SealData_Value = sealData.getSealData();
					}*/
					// /#endregion

					// this.TB_SealData.Text = en.GetValStringByKey("SealData");
				/*}*/
				// /#endregion 检查是否有ca签名.
			}
			AddJSEvent(en);
		}

		get_request().getSession().setAttribute("Count", "");
		Btn_Save.setId("Btn_Save");
		Btn_Save.setName("Btn_Save");
		boolean isEdit = this.getHisFrmNode().getIsEdit();
		Btn_Save.setVisible(this.getHisFrmNode().getIsEdit());//false
		Btn_Save.setEnabled(isEdit);
		//Btn_Save.setBackColor();
		Btn_Save.addAttr("onclick", "Save();");
		
		Btn_Print.setId("Btn_Print");
		Btn_Print.setName("Btn_Print");
		Btn_Print.setText("打印");

		Node curNd = new Node();
		curNd.setNodeID(this.getFK_Node());
		curNd.RetrieveFromDBSources();

		if (curNd.getFormType() == NodeFormType.SheetTree) {
			Btn_Save.setVisible(true);
			Btn_Save.setEnabled(true);

			Btn_Print.setVisible(false);
			Btn_Print.setEnabled(false);

		} else {
			boolean isPrint = this.getHisFrmNode().getIsPrint();
			Btn_Print.setVisible(isPrint);
			Btn_Print.setEnabled(isPrint);
			Btn_Print
					.addAttr(
							"onclick",
							"window.open('Print.jsp?FK_Node="
									+ this.getFK_Node()
									+ "&FID="
									+ this.getFID()
									+ "&FK_MapData="
									+ this.getFK_MapData()
									+ "&WorkID="
									+ this.getOID()
									+ "', '', 'dialogHeight: 350px; dialogWidth:450px; center: yes; help: no'); return false;");
		}
	}

	public final void AddJSEvent(Entity en) {
		// Attrs attrs = en.getEnMap().getAttrs();
		// for (Attr attr : attrs)
		// {
		// if (attr.getUIIsReadonly() || attr.getUIVisible() == false)
		// {
		// continue;
		// }
		// if (attr.getIsFKorEnum())
		// {
		// Object ddl = this.UCEn1.GetDDLByID("DDL_" + attr.getKey());
		// }
		// }
	}

}
