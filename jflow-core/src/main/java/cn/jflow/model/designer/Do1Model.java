package cn.jflow.model.designer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataSet;
import BP.DA.DataType;
import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Sys.FieldGroupXml;
import BP.Sys.FieldGroupXmls;
import BP.Sys.FrmAttachment;
import BP.Sys.GroupField;
import BP.Sys.GroupFieldAttr;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapFrame;
import BP.Sys.MapM2M;
import BP.Sys.SFTable;
import BP.Sys.SFTables;
import BP.Sys.SrcType;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnumMains;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.DDL;

public class Do1Model extends BaseModel {
	private HttpServletRequest request;
	private HttpServletResponse response;
	public StringBuilder Pub1 = new StringBuilder();
	public StringBuilder Pub2 = new StringBuilder();
	public String Title;

	public Do1Model(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		this.request = request;
		this.response = response;
	}

	private String IDX;

	public String getIDX() {
		return request.getParameter("IDX");
	}

	public void setIDX(String iDX) {
		IDX = iDX;
	}

	public void init() {
		String doType = getDoType();
		try {
			if ("FWCShowError".equals(doType)) {
				//this.Response.Write("<h3>该表单非节点表单，所以无法编辑审核组件属性.</h3>");
				BP.Sys.PubClass.Alert("该表单非节点表单，所以无法编辑审核组件属性",response);
			}
			else if ("EditSFTable".equals(doType)) {
				BP.Sys.SFTable mysf1 = new SFTable(this.getRefNo());
				if (mysf1.getSrcType().getValue() == SrcType.TableOrView.getValue()) {
					try {
						response.sendRedirect("SFTable.jsp?RefNo=" + mysf1.getNo() + "&FromApp=SL");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if (mysf1.getSrcType().getValue() == SrcType.SQL.getValue()) {
					try {
						response.sendRedirect("SFSQL.jsp?RefNo=" + mysf1.getNo() + "&FromApp=SL");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if (mysf1.getSrcType().getValue() == SrcType.WebServices.getValue()) {
					try {
						response.sendRedirect("SFWS.jsp?RefNo=" + mysf1.getNo() + "&FromApp=SL");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				return;
			}

			
			
			if (doType.equals("DownTempFrm")) {
				MapData md = new MapData(getFK_MapData());
				DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo());
				String name = "ccflow表单模板." + md.getName() + "." + md.getNo()
						+ ".xml";
				/*
				 * warning String file = request.PhysicalApplicationPath +
				 * "\\Temp\\" + getFK_MapData() + ".xml";
				 */
				String file = request.getSession().getServletContext()
						.getRealPath("")
						+ "\\Temp\\" + getFK_MapData() + ".xml";
//				System.out.println("11111111111" + file);
				ds.WriteXml(file);
				try {
					response.sendRedirect("../../Temp/" + getFK_MapData()
							+ ".xml");
				} catch (IOException e) {
					e.printStackTrace();
				}
				WinClose();
				return;
			} else if (doType.equals("CCForm")) {
				// Application.Clear();
//				if (!WebUser.getNoOfRel().equals("admin")) {
//					Emp emp = new Emp("admin");
//					WebUser.SignInOfGener(emp);
//				}

				MapAttr mattr = new MapAttr();
				mattr.setMyPK(request.getParameter("MyPK"));
				int i = mattr.RetrieveFromDBSources();
				mattr.setKeyOfEn(request.getParameter("KeyOfEn"));
				mattr.setFK_MapData(request.getParameter("FK_MapData"));
				mattr.setMyDataType(Integer.parseInt(request
						.getParameter("DataType")));

				if (!StringHelper.isNullOrEmpty(request
						.getParameter("UIBindKey") + "")) {
					mattr.setUIBindKey(request.getParameter("UIBindKey"));
				}
				mattr.setUIContralType(UIContralType.forValue(Integer
						.parseInt(request.getParameter("UIContralType"))));
				mattr.setLGType(FieldTypeS.forValue(Integer.parseInt(request
						.getParameter("LGType"))));
				if (i == 0) {
					mattr.setName(request.getParameter("KeyName"));
					mattr.setUIIsEnable(true);
					mattr.setUIVisible(true);
					if (mattr.getLGType() == FieldTypeS.Enum) {
						mattr.setDefVal("0");
					}
					mattr.Insert();
				} else {
					mattr.Update();
				}

				switch (mattr.getLGType()) {
				case Enum:
					try {
						response.sendRedirect("EditEnum.jsp?MyPK="
								+ mattr.getMyPK() + "&RefNo="
								+ mattr.getMyPK());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				case Normal:
					try {
						response.sendRedirect("EditF.jsp?DoType=Edit&MyPK="
								+ mattr.getFK_MapData() + "&RefNo="
								+ mattr.getMyPK() + "&FType="
								+ mattr.getMyDataType() + "&GroupField=0");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				case FK:
					try {
						response.sendRedirect("EditTable.jsp?DoType=Edit&MyPK="
								+ mattr.getFK_MapData() + "&RefNo="
								+ mattr.getMyPK() + "&FType="
								+ mattr.getMyDataType() + "&GroupField=0");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				default:
					break;
				}
			} else if (doType.equals("DobackToF")) {
				MapAttr ma = new MapAttr(getRefNo());
				switch (ma.getLGType()) {
				case Normal:
					try {
						response.sendRedirect("EditF.jsp?RefNo=" + getRefNo());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				case FK:
					try {
						response.sendRedirect("EditTable.jsp?RefNo="
								+ getRefNo());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				case Enum:
					try {
						response.sendRedirect("EditEnum.jsp?RefNo="
								+ getRefNo());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				default:
					return;
				}

			} else if (doType.equals("AddEnum")) {
				SysEnumMain sem1 = new SysEnumMain(
						request.getParameter("EnumKey"));
				MapAttr attrAdd = new MapAttr();
				attrAdd.setKeyOfEn(sem1.getNo());
				if (attrAdd.IsExit(MapAttrAttr.FK_MapData, getMyPK(),
						MapAttrAttr.KeyOfEn, sem1.getNo())) {
					BP.Sys.PubClass.Alert("字段已经存在 [" + sem1.getNo() + "]。",
							response);
					WinClose();
					return;
				}

				attrAdd.setFK_MapData(getMyPK());
				attrAdd.setName(sem1.getName());
				attrAdd.setUIContralType(UIContralType.DDL);
				attrAdd.setUIBindKey(sem1.getNo());
				attrAdd.setMyDataType(DataType.AppInt);
				attrAdd.setLGType(FieldTypeS.Enum);
				attrAdd.setDefVal("0");
				attrAdd.setUIIsEnable(true);
				if (getIDX() == null || getIDX().equals("")) {
					MapAttrs attrs1 = new MapAttrs(getMyPK());
					attrAdd.setIdx(0);
				} else {
					if(!StringHelper.isNullOrEmpty(getIDX()))
					{
						attrAdd.setIdx(Integer.parseInt(getIDX()));
					}
				}
				attrAdd.Insert();
				try {
					response.sendRedirect("EditEnum.jsp?MyPK=" + getMyPK()
							+ "&RefNo=" + attrAdd.getMyPK());
				} catch (IOException e) {
					e.printStackTrace();
				}
				WinClose();
				return;
			} else if (doType.equals("DelEnum")) {
				String eKey = request.getParameter("EnumKey");
				SysEnumMain sem = new SysEnumMain();
				sem.setNo(eKey);
				sem.Delete();
				WinClose();
				return;

			} else if (doType.equals("AddSysEnum")) {
				AddFEnum();
			} else if (doType.equals("AddSFTable")) {
				AddSFTable();
			} else if (doType.equals("AddSFSQL")) {
				AddSFSQL();
			} else if (doType.equals("AddSFWebServeces")) {
				AddSFWS();
			} else if (doType.equals("AddSFTableAttr")) {
				SFTable sf = new SFTable(request.getParameter("RefNo"));
				try {
					response.sendRedirect("EditTable.jsp?MyPK=" + getMyPK()
							+ "&SFKey=" + sf.getNo());
				} catch (IOException e) {
					e.printStackTrace();
				}
				WinClose();
				return;
			} else if (doType.equals("AddFG")) // 执行一个插入列组的命令.
			{
				if (getRefNo().equals("IsPass")) {
					MapDtl dtl = new MapDtl(getFK_MapData());
					dtl.setIsEnablePass(true); // 更新是否启动审核分组字段.
					MapAttr attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("Check_Note");
					attr.setName("审核意见");
					attr.setMyDataType(DataType.AppString);
					attr.setUIContralType(UIContralType.TB);
					attr.setUIIsEnable(true);
					attr.setUIIsLine(true);
					attr.setMaxLen(4000);
					attr.setColSpan(4); // 默认为4列。
					attr.setIdx(1);
					attr.Insert();

					attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("Checker");
					attr.setName("审核人"); // "审核人";
					attr.setMyDataType(DataType.AppString);
					attr.setUIContralType(UIContralType.TB);
					attr.setMaxLen(50);
					attr.setMinLen(0);
					attr.setUIIsEnable(true);
					attr.setUIIsLine(false);
					attr.setDefVal("@WebUser.Name");
					attr.setUIIsEnable(false);
					attr.setIsSigan(true);
					attr.setIdx(2);
					attr.Insert();

					attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("IsPass");
					attr.setName("通过否?"); // "审核人";
					attr.setMyDataType(DataType.AppBoolean);
					attr.setUIContralType(UIContralType.CheckBok);
					attr.setUIIsEnable(true);
					attr.setUIIsLine(false);
					attr.setUIIsEnable(false);
					attr.setIsSigan(true);
					attr.setDefVal("1");
					attr.setIdx(2);
					attr.setDefVal("0");
					attr.Insert();

					attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("Check_RDT");
					attr.setName("审核日期"); // "审核日期";
					attr.setMyDataType(DataType.AppDateTime);
					attr.setUIContralType(UIContralType.TB);
					attr.setUIIsEnable(true);
					attr.setUIIsLine(false);
					attr.setDefVal("@RDT");
					attr.setUIIsEnable(false);
					attr.setIdx(3);
					attr.Insert();

					// 处理批次ID
					attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("BatchID");
					attr.setName("BatchID"); // ToE("IsPass", "是否通过");// "审核人";
					attr.setMyDataType(DataType.AppInt);
					attr.setUIIsEnable(false);
					attr.setUIIsLine(false);
					attr.setUIIsEnable(false);
					attr.setUIVisible(false);
					attr.setIdx(2);
					attr.setDefVal("0");
					attr.Insert();

					dtl.Update();
					WinClose();
					return;
				} else if (getRefNo().equals("Eval")) {
					MapAttr attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("EvalEmpNo");
					attr.setName("被评价人员编号");
					attr.setMyDataType(DataType.AppString);
					attr.setUIContralType(UIContralType.TB);
					attr.setMaxLen(50);
					attr.setMinLen(0);
					attr.setUIIsEnable(true);
					attr.setUIIsLine(false);
					attr.setUIIsEnable(false);
					attr.setIsSigan(true);
					attr.setIdx(1);
					attr.Insert();

					attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("EvalEmpName");
					attr.setName("被评价人员名称");
					attr.setMyDataType(DataType.AppString);
					attr.setUIContralType(UIContralType.TB);
					attr.setMaxLen(50);
					attr.setMinLen(0);
					attr.setUIIsEnable(true);
					attr.setUIIsLine(false);
					attr.setUIIsEnable(false);
					attr.setIsSigan(true);
					attr.setIdx(2);
					attr.Insert();

					attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("EvalCent");
					attr.setName("工作得分");
					attr.setMyDataType(DataType.AppFloat);
					attr.setUIContralType(UIContralType.TB);
					attr.setMaxLen(50);
					attr.setMinLen(0);
					attr.setUIIsEnable(true);
					attr.setUIIsLine(false);
					attr.setUIIsEnable(true);
					attr.setIdx(3);
					attr.Insert();

					attr = new MapAttr();
					attr.setFK_MapData(getFK_MapData());
					attr.setKeyOfEn("EvalNote");
					attr.setName("评价信息");
					attr.setMyDataType(DataType.AppString);
					attr.setUIContralType(UIContralType.TB);
					attr.setMaxLen(50);
					attr.setMinLen(0);
					attr.setUIIsEnable(true);
					attr.setUIIsEnable(true);
					attr.setIdx(4);
					attr.Insert();
					WinClose();
					return;
				} else {
				}
			} else if (doType.equals("AddFGroup")) {
				AddFGroup();
				return;
			} else if (doType.equals("AddF") || doType.equals("ChoseFType")) {
				AddF();
			} else if (doType.equals("Up")) {
				MapAttr attrU = new MapAttr(getRefNo());
				if (request.getParameter("IsDtl") != null) {
					attrU.DoDtlUp();
				} else {
					attrU.DoUp();
				}

				WinClose();
				return;
			} else if (doType.equals("Down")) {
				MapAttr attrD = new MapAttr(getRefNo());
				attrD.DoDown();
				WinClose();
				return;
			} else if(doType.equals("DownAttr")){
				MapAttr attrD = new MapAttr(getRefNo());
				attrD.DoDtlDown();
				WinClose();
				return;
			}else if (doType.equals("Jump")) {
				MapAttr attrFrom = new MapAttr(request.getParameter("FromID"));
				MapAttr attrTo = new MapAttr(request.getParameter("ToID"));
				attrFrom.DoJump(attrTo);
				WinClose();
				return;
			} else if (doType.equals("MoveTo")) {
				String toID = request.getParameter("ToID");
				int toGFID = Integer.parseInt(request.getParameter("ToGID"));
				int fromGID = Integer.parseInt(request.getParameter("FromGID"));
				String fromID = request.getParameter("FromID");
				MapAttr fromAttr = new MapAttr();
				fromAttr.setMyPK(fromID);
				fromAttr.Retrieve();
				if (toGFID == fromAttr.getGroupID()
						&& toID.equals(fromAttr.getMyPK())) {
					// 如果没有移动.
					WinClose();
					return;
				}
				if (toGFID != fromAttr.getGroupID()
						&& toID.equals(fromAttr.getMyPK())) {
					MapAttr toAttr = new MapAttr(toID);
					fromAttr.Update(MapAttrAttr.GroupID, toAttr.getGroupID(),
							MapAttrAttr.Idx, toAttr.getIdx());
					WinClose();
					return;
				}
				// response.sendRedirect(Request.RawUrl.Replace("MoveTo",
				// "Jump"), true);
				return;
			} else if (doType.equals("Edit")) {
				Edit();
			} else if (doType.equals("Del")) {
				MapAttr attrDel = new MapAttr();
				attrDel.setMyPK(getRefNo());
				attrDel.Delete();
				WinClose();
				return;
			} else if (doType.equals("GFDoUp")) {
				GroupField gf = new GroupField(getRefOID());
				gf.DoUp();
				gf.Retrieve();
				if (gf.getIdx() == 0) {
					WinClose();
					return;
				}
				int oidIdx = gf.getIdx();
				gf.setIdx(gf.getIdx() - 1);
				GroupField gfUp = new GroupField();
				if (gfUp.Retrieve(GroupFieldAttr.EnName, gf.getEnName(),
						GroupFieldAttr.Idx, gf.getIdx()) == 1) {
					gfUp.setIdx(oidIdx);
					gfUp.Update();
				}
				gf.Update();
				WinClose();
				return;
			} else if (doType.equals("GFDoDown")) {
				GroupField mygf = new GroupField(getRefOID());
				mygf.DoDown();
				mygf.Retrieve();
				int oidIdx1 = mygf.getIdx();
				mygf.setIdx(mygf.getIdx() + 1);
				GroupField gfDown = new GroupField();
				if (gfDown.Retrieve(GroupFieldAttr.EnName, mygf.getEnName(),
						GroupFieldAttr.Idx, mygf.getIdx()) == 1) {
					gfDown.setIdx(oidIdx1);
					gfDown.Update();
				}
				mygf.Update();
				WinClose();
				return;
			} else if (doType.equals("AthDoUp")) {
				FrmAttachment frmAth = new FrmAttachment(getMyPK());
				if (frmAth.getRowIdx() > 0) {
					frmAth.setRowIdx(frmAth.getRowIdx() - 1);
					frmAth.Update();
				}
				WinClose();
				return;
			} else if (doType.equals("AthDoDown")) {
				FrmAttachment frmAthD = new FrmAttachment(getMyPK());
				if (frmAthD.getRowIdx() < 10) {
					frmAthD.setRowIdx(frmAthD.getRowIdx() + 1);
					frmAthD.Update();
				}
				WinClose();
				return;
			} else if (doType.equals("DtlDoUp")) {
				MapDtl dtl1 = new MapDtl(getMyPK());
				if (dtl1.getRowIdx() > 0) {
					dtl1.setRowIdx(dtl1.getRowIdx() - 1);
					dtl1.Update();
				}
				WinClose();
				return;
			} else if (doType.equals("DtlDoDown")) {
				MapDtl dtl2 = new MapDtl(getMyPK());
				if (dtl2.getRowIdx() < 10) {
					dtl2.setRowIdx(dtl2.getRowIdx() + 1);
					dtl2.Update();
				}
				WinClose();
				return;
			} else if (doType.equals("M2MDoUp")) {
				MapM2M ddtl1 = new MapM2M(getMyPK());
				if (ddtl1.getRowIdx() > 0) {
					ddtl1.setRowIdx(ddtl1.getRowIdx() - 1);
					ddtl1.Update();
				}
				WinClose();
				return;
			} else if (doType.equals("M2MDoDown")) {
				MapM2M ddtl2 = new MapM2M(getMyPK());
				if (ddtl2.getRowIdx() < 10) {
					ddtl2.setRowIdx(ddtl2.getRowIdx() + 1);
					ddtl2.Update();
				}
				WinClose();
			}  
		} catch (RuntimeException ex) {
			/*
			 * warning Pub1.AddMsgOfWarning("错误:", ex.getMessage() + " <br>" +
			 * Request.RawUrl);
			 */
			Pub1.append(AddMsgOfWarning("错误:", ex.getMessage() + " <br>"
					+ request.getRequestURI()));
		}
	}

	public final void Edit() {
		MapAttr attr = new MapAttr(getRefNo());
		switch (attr.getMyDataType()) {
		case BP.DA.DataType.AppString:
			// Response.Redirect("EditF.jsp?RefOID="+this
			break;
		default:
			break;
		}
	}

	public final String getGroupField() {
		return request.getParameter("GroupField");
	}

	public final void AddF() {
		/*Title = "增加新字段向导";

		Pub1.append(AddFieldSet("新增普通字段"));
		Pub1.append(AddUL());
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK()
				+ "&FType=" + BP.DA.DataType.AppString + "&IDX=" + getIDX()
				+ "&GroupField=" + getGroupField()
				+ "'><b>字符型</b></a> - <font color=Note>如:姓名、地址、邮编、电话</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK()
				+ "&FType=" + BP.DA.DataType.AppInt + "&IDX=" + getIDX()
				+ "&GroupField=" + getGroupField()
				+ "'><b>整数型</b></a> - <font color=Note>如:年龄、个数。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK()
				+ "&FType=" + BP.DA.DataType.AppMoney + "&IDX=" + getIDX()
				+ "&GroupField=" + getGroupField()
				+ "'><b>金额型</b></a> - <font color=Note>如:单价、薪水。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK()
				+ "&FType=" + BP.DA.DataType.AppFloat + "&IDX=" + getIDX()
				+ "&GroupField=" + getGroupField()
				+ "'><b>浮点型</b></a> - <font color=Note>如：身高、体重、长度。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK()
				+ "&FType=" + BP.DA.DataType.AppDate + "&IDX=" + getIDX()
				+ "&GroupField=" + getGroupField()
				+ "'><b>日期型</b></a> - <font color=Note>如：出生日期、发生日期。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK()
				+ "&FType=" + BP.DA.DataType.AppDateTime + "&IDX=" + getIDX()
				+ "&GroupField=" + getGroupField()
				+ "'><b>日期时间型</b></a> - <font color=Note>如：发生日期时间</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK="
				+ getMyPK()
				+ "&FType="
				+ BP.DA.DataType.AppBoolean
				+ "&IDX="
				+ getIDX()
				+ "&GroupField="
				+ getGroupField()
				+ "'><b>Boole型(是/否)</b></a> - <font color=Note>如：是否完成、是否达标</font>"));
		Pub1.append(AddULEnd());
		Pub1.append(AddFieldSetEnd());

		Pub1.append(AddFieldSet("新增枚举字段(用来表示，状态、类型...的数据。)"));
		Pub1.append(AddUL());
		Pub1.append(AddLi("<a href='Do.jsp?DoType=AddSysEnum&MyPK="
				+ getMyPK() + "&IDX=" + getIDX() + "&GroupField="
				+ getGroupField()
				+ "'><b>枚举型</b></a> -  比如：性别:男/女。请假类型：事假/病假/婚假/产假/其它。"));
		Pub1.append(AddULEnd());
		Pub1.append(AddFieldSetEnd());

		Pub1.append(AddFieldSet("新增外键字段(字典表，通常只有编号名称两个列)"));
		Pub1.append(AddUL());
		Pub1.append(AddLi("<a href='Do.jsp?DoType=AddSFTable&MyPK="
				+ getMyPK() + "&FType=Class&IDX=" + getIDX() + "&GroupField="
				+ getGroupField() + "'><b>外键型</b></a> -  比如：岗位、税种、行业、经济性质。"));
		Pub1.append(AddULEnd());
		Pub1.append(AddFieldSetEnd());

		Pub1.append(AddFieldSet("<div onclick=\"javascript:HidShowSysField();\" >系统约定字段-隐藏/显示</div> "));
		String info = DataType.ReadTextFile2Html(BP.Sys.SystemConfig
				.getPathOfData() + "SysFields.txt");
		Pub1.append("<div id='SysField' style='display:none' >" + info
				+ "</div>");
		Pub1.append(AddFieldSetEnd());*/
		
		
		Pub1.append(AddFieldSet("新增普通字段"));
		Pub1.append(AddUL());
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK() + "&FType=" + BP.DA.DataType.AppString + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>字符型</b></a> - <font color=Note>如:姓名、地址、邮编、电话</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK() + "&FType=" + BP.DA.DataType.AppInt + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>整数型</b></a> - <font color=Note>如:年龄、个数。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK() + "&FType=" + BP.DA.DataType.AppMoney + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>金额型</b></a> - <font color=Note>如:单价、薪水。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK() + "&FType=" + BP.DA.DataType.AppFloat + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>浮点型</b></a> - <font color=Note>如：身高、体重、长度。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK() + "&FType=" + BP.DA.DataType.AppDouble + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>双精度</b></a> - <font color=Note>如：亿万、兆数值类型单位。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK() + "&FType=" + BP.DA.DataType.AppDate + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>日期型</b></a> - <font color=Note>如：出生日期、发生日期。</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK() + "&FType=" + BP.DA.DataType.AppDateTime + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>日期时间型</b></a> - <font color=Note>如：发生日期时间</font>"));
		Pub1.append(AddLi("<a href='EditF.jsp?DoType=Add&MyPK=" + getMyPK() + "&FType=" + BP.DA.DataType.AppBoolean + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>Boole型(是/否)</b></a> - <font color=Note>如：是否完成、是否达标</font>"));
		Pub1.append(AddULEnd());
		Pub1.append(AddFieldSetEnd());

		Pub1.append(AddFieldSet("新增枚举字段(用来表示，状态、类型...的数据。)"));
		Pub1.append(AddUL());
		Pub1.append(AddLi("<a href='Do.jsp?DoType=AddSysEnum&MyPK=" + getMyPK() + "&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>枚举型</b></a> -  比如：性别:男/女。请假类型：事假/病假/婚假/产假/其它。"));
		Pub1.append(AddULEnd());
		Pub1.append(AddFieldSetEnd());

		Pub1.append(AddFieldSet("新增下拉框(外键、外部表、WebServices)字段(通常只有编号名称两个列)"));
		Pub1.append(AddUL());
		Pub1.append(AddLi("<a href='Do.jsp?DoType=AddSFTable&MyPK=" + getMyPK() + "&FType=Class&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>外键型</b></a> -  比如：岗位、税种、行业、科目，本机上一个表组成一个下拉框。"));
		Pub1.append(AddLi("<a href='Do.jsp?DoType=AddSFSQL&MyPK=" + getMyPK() + "&FType=Class&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>外部表</b></a> -  比如：配置一个SQL通过数据库连接或获取的外部数据，组成一个下拉框。"));
		Pub1.append(AddLi("<a href='Do.jsp?DoType=AddSFWebServeces&MyPK=" + getMyPK() + "&FType=Class&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "'><b>WebServices</b></a> -  比如：通过调用Webservices接口获得数据，组成一个下拉框。"));
		Pub1.append(AddULEnd());
		Pub1.append(AddFieldSetEnd());

		Pub1.append(AddFieldSet("从已有表里导入字段"));
		Pub1.append(AddUL());
		Pub1.append(AddLi("<a href=\"javascript:WinOpen('ImpTableField.jsp?FK_MapData=" + getMyPK() + "&FType=Class&Idx=" + getIDX() + "&GroupField=" + this.getGroupField() + "');\" ><b>导入字段</b></a> &nbsp;&nbsp;从现有的表中导入字段,以提高开发的速度与字段拼写的正确性."));
		Pub1.append(AddULEnd());
		Pub1.append(AddFieldSetEnd());

		Pub1.append(AddFieldSet("<div onclick=\"javascript:HidShowSysFieldImp();\" >增加系统字段-隐藏/显示</div> "));
		String info = DataType.ReadTextFile2Html(BP.Sys.SystemConfig
				.getPathOfData() + "SysFields.txt");
		Pub1.append(Add("<div id='SysField' style='display:none' >" + info + "</div>"));
		Pub1.append(AddFieldSetEnd());
	}

	public final void AddFEnum() {
		Title = "增加新字段向导";
		Pub1.append(AddTable());
		Pub1.append(AddCaptionLeft("<a href='Do.jsp?DoType=AddF&MyPK="
				+ getMyPK() + "&IDX=" + getIDX()
				+ "'>增加新字段向导</a> - <a href='SysEnum.jsp?DoType=New&MyPK="
				+ getMyPK() + "&IDX=" + getIDX()
				+ "' ><img src='../Img/Btn/New.gif' />新建枚举</a>"));
		Pub1.append(AddTR());
		Pub1.append(AddTDTitle("IDX"));
		Pub1.append(AddTDTitle("编号(点击增加到表单)"));
		Pub1.append(AddTDTitle("名称"));
		Pub1.append(AddTDTitle("操作"));
		Pub1.append(AddTDTitle());
		Pub1.append(AddTREnd());

		SysEnumMains sems = new SysEnumMains();
		QueryObject qo = new QueryObject(sems);
		Pub2.append(BindPageIdx(Pub1, qo.GetCount(), pageSize, getPageIdx(),
				"Do.jsp?DoType=AddSysEnum&MyPK=" + getMyPK()
						+ "&IDX=&GroupField"));
		qo.DoQuery("No", pageSize, getPageIdx());

		boolean is1 = false;
		int idx = 0;
		for (SysEnumMain sem : sems.ToJavaList()) {
			DDL ddl = null;
			try {
				ddl = new DDL();
				ddl.BindSysEnum(sem.getNo());
			} catch (java.lang.Exception e) {
				sem.Delete();
			}
			idx++;
			Pub1.append(AddTR(is1));
			Pub1.append(AddTDIdx(idx));
			Pub1.append(AddTD("<a  href=\"javascript:AddEnum('" + getMyPK()
					+ "','" + getIDX() + "','" + sem.getNo() + "')\" >"
					+ sem.getNo() + "</a>"));
			Pub1.append(AddTD(sem.getName()));
			Pub1.append(AddTD("[<a href='SysEnum.jsp?DoType=Edit&MyPK="
					+ getMyPK() + "&IDX=" + getIDX() + "&RefNo=" + sem.getNo()
					+ "' >编辑</a>]"));
			Pub1.append(AddTD(ddl));
			Pub1.append(AddTREnd());
		}
		Pub1.append(AddTableEnd());
	}

	/**
	 * 增加分组.
	 */
	public final void AddFGroup() {
		Pub1.append(AddFieldSet("插入列组"));

		Pub1.append(AddUL());
		FieldGroupXmls xmls = new FieldGroupXmls();
		xmls.RetrieveAll();
		for (FieldGroupXml en : xmls.ToJavaList()) {
			Pub1.append(AddLi("<a href='Do.jsp?DoType=AddFG&RefNo="
					+ en.getNo() + "&FK_MapData=" + getFK_MapData() + "' >"
					+ en.getName() + "</a><br>" + en.getDesc()));
		}
		Pub1.append(AddULEnd());
		Pub1.append(AddFieldSetEnd());
	}

	private int pageSize = 10;

	public final void AddSFTable() {
		Title = "增加新字段向导";

		Pub1.append(AddTable());
		Pub1.append(AddCaption("<a href='Do.jsp?DoType=AddF&MyPK="
				+ getMyPK()
				+ "&IDX="
				+ getIDX()
				+ "'>增加新字段向导</a> - 增加外键字段 - <a href='SFTable.jsp?DoType=New&MyPK="
				+ getMyPK() + "&IDX=" + getIDX() + "' > 新建表</a>"));
		Pub1.append(AddTR());
		Pub1.append(AddTDTitle("IDX"));
		Pub1.append(AddTDTitle("编号(点击增加到表单)"));
		Pub1.append(AddTDTitle("名称"));
		Pub1.append(AddTDTitle("类别"));
		Pub1.append(AddTDTitle("描述/编辑"));
		Pub1.append(AddTDTitle("编辑数据"));
		Pub1.append(AddTREnd());

		SFTables ens = new SFTables();
		QueryObject qo = new QueryObject(ens);
		qo.AddWhere(BP.Sys.SFTableAttr.SrcType, SrcType.TableOrView);
		Pub2.append(BindPageIdx(Pub1, qo.GetCount(), pageSize, getPageIdx(),
				"Do.jsp?DoType=AddSFTable&MyPK=" + getMyPK()
						+ "&IDX=&GroupField"));
		qo.DoQuery("No", pageSize, getPageIdx());

		boolean is1 = false;
		int idx = 0;
		for (SFTable sem : ens.ToJavaList()) {
			idx++;
			// is1 = Pub1.AddTR(is1);
			Pub1.append(AddTR(is1));
			Pub1.append(AddTDIdx(idx));
			Pub1.append(AddTD("<a  href=\"javascript:AddSFTable('" + getMyPK()
					+ "','" + getIDX() + "','" + sem.getNo() + "')\" >"
					+ sem.getNo() + "</a>"));
			Pub1.append(AddTD(sem.getName()));

			if (sem.getIsClass()) {
				Pub1.append(AddTD("<a href=\"javascript:WinOpen('../Comm/Search.jsp?EnsName="
						+ sem.getNo()
						+ "','sg')\"  ><img src='../Img/Btn/Edit.gif' border=0/>"
						+ sem.getTableDesc() + "</a>"));
			} else {
				Pub1.append(AddTD("<a href=\"javascript:WinOpen('SFTable.jsp?DoType=Edit&MyPK="
						+ getMyPK()
						+ "&IDX="
						+ getIDX()
						+ "&RefNo="
						+ sem.getNo()
						+ "','sg')\"  ><img src='../Img/Btn/Edit.gif' border=0/>"
						+ sem.getTableDesc() + "</a>"));
			}

			if (sem.getNo().contains(".")) {
				Pub1.append(AddTD("&nbsp;"));
			} else {
				Pub1.append(AddTD("<a href=\"javascript:WinOpen('SFTableEditData.jsp?RefNo="
						+ sem.getNo() + "');\" >编辑</a>"));
			}
			Pub1.append(AddTREnd());
		}
		Pub1.append(AddTableEnd());
	}
	public final void AddSFSQL()
	{
		this.Title = "增加新字段向导";

		Pub1.append(AddTable());
		Pub1.append(AddCaption("<a href='Do.jsp?DoType=AddF&MyPK=" + getMyPK() + "&Idx=" + getIDX() + "'><img src='/WF/Img/Btn/Back.gif' />&nbsp;返回</a> - 外部表列表 - <a href='SFSQL.jsp?DoType=New&MyPK=" + getMyPK() + "&Idx=" + getIDX() + "' >新建外部表</a>"));
		Pub1.append(AddTR());
		Pub1.append(AddTDTitle("Idx"));
		Pub1.append(AddTDTitle("编号(点击增加到表单)"));
		Pub1.append(AddTDTitle("名称(点击名称编辑属性)"));
		Pub1.append(AddTDTitle("编码表类型"));
		Pub1.append(AddTDTitle("查看数据"));
		Pub1.append(AddTREnd());

		BP.Sys.SFTables ens = new SFTables();
		QueryObject qo = new QueryObject(ens);
		qo.AddWhere(BP.Sys.SFTableAttr.SrcType, SrcType.SQL);
		Pub2.append(BindPageIdx(Pub1, qo.GetCount(), pageSize, getPageIdx(), "Do.jsp?DoType=AddSFSQL&MyPK=" + getMyPK() + "&Idx=&GroupField"));
		qo.DoQuery("No", pageSize, getPageIdx());
		if (ens.size() == 0)
		{
			Pub1.append(AddTR());
			Pub1.append(AddTDBigDoc("colspan=5", "注册到ccform的表为空，点击上面的新建表，进入创建向导。"));
			Pub1.append(AddTREnd());
			Pub1.append(AddTableEnd());
			return;
		}

		int idx = 0;
		for (BP.Sys.SFTable sem : ens.ToJavaList())
		{
			idx++;
			Pub1.append(AddTR());
			Pub1.append(AddTDIdx(idx));
			Pub1.append(AddTD("<a  href=\"javascript:AddSFSQL('" + getMyPK() + "','" + getIDX() + "','" + sem.getNo() + "')\" >" + sem.getNo() + "</a>"));
			Pub1.append(AddTD("<a href=\"javascript:WinOpen('SFSQL.jsp?DoType=Edit&MyPK=" + getMyPK() + "&Idx=" + getIDX() + "&RefNo=" + sem.getNo() + "','sg')\"  ><img src='../Img/Btn/Edit.gif' border=0/>" + sem.getName() + "</a>"));

			Pub1.append(AddTD(sem.getCodeStructT())); //编码表类型.
			Pub1.append(AddTD("<a href=\"javascript:WinOpen('SFTableEditData.jsp?RefNo=" + sem.getNo() + "');\" >查看</a>"));
			Pub1.append(AddTREnd());
		}
		Pub1.append(AddTableEnd());
	}

	public final void AddSFWS()
	{
		this.Title = "增加新WebService接口向导";

		Pub1.append(AddTable());
		Pub1.append(AddCaption("<a href='Do.jsp?DoType=AddF&MyPK=" + getMyPK() + "&Idx=" + getIDX() + "'><img src='/WF/Img/Btn/Back.gif' />&nbsp;返回</a> - WebService接口列表 - <a href='SFWS.jsp?DoType=New&MyPK=" + getMyPK() + "&Idx=" + getIDX() + "' >新建WebService接口</a>"));
		Pub1.append(AddTR());
		Pub1.append(AddTDTitle("Idx"));
		Pub1.append(AddTDTitle("编号(点击增加到表单)"));
		Pub1.append(AddTDTitle("名称(点击名称编辑属性)"));
		Pub1.append(AddTDTitle("编码表类型"));
		Pub1.append(AddTDTitle("查看数据"));
		Pub1.append(AddTREnd());

		BP.Sys.SFTables ens = new SFTables();
		QueryObject qo = new QueryObject(ens);
		qo.AddWhere(BP.Sys.SFTableAttr.SrcType, SrcType.WebServices);
		Pub2.append(BindPageIdx(Pub1,qo.GetCount(), pageSize, getPageIdx(), "Do.jsp?DoType=AddSFWS&MyPK=" + getMyPK() + "&Idx=&GroupField"));
		qo.DoQuery("No", pageSize, getPageIdx());
		if (ens.size() == 0)
		{
			Pub1.append(AddTR());
			Pub1.append(AddTDBigDoc("colspan=5", "注册到ccform的WebService接口为空，点击上面的新建表，进入创建向导。"));
			Pub1.append(AddTREnd());
			Pub1.append(AddTableEnd());
			return;
		}

		int idx = 0;
		for (BP.Sys.SFTable sem : ens.ToJavaList())
		{
			idx++;
			Pub1.append(AddTR());
			Pub1.append(AddTDIdx(idx));
			Pub1.append(AddTD(sem.getNo()));
			Pub1.append(AddTD("<a href=\"javascript:WinOpen('SFWS.jsp?DoType=Edit&MyPK=" + getMyPK() + "&Idx=" + getIDX() + "&RefNo=" + sem.getNo() + "','sg')\"  ><img src='../Img/Btn/Edit.gif' border=0/>" + sem.getName() + "</a>"));

			Pub1.append(AddTD(sem.getCodeStructT())); //编码表类型.
			Pub1.append(AddTD("<a href=\"javascript:WinOpen('SFTableEditData.jsp?RefNo=" + sem.getNo() + "');\" >查看</a>"));
			Pub1.append(AddTREnd());
		}
		Pub1.append(AddTableEnd());
	}
}
