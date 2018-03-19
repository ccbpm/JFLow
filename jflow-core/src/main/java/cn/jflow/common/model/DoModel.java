package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.util.ContextHolderUtils;
import BP.DA.DataSet;
import BP.DA.DataType;
import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Sys.SFTable;
import BP.Sys.SysEnumMain;
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
import BP.Tools.StringHelper;
import BP.Web.WebUser;

public class DoModel extends BaseModel{

	private String DoType;
	
	private String FK_MapData;
	
	private String IDX;
	
	HttpServletRequest request;
	
	HttpServletResponse response;
	
	private String MyPK;
	
	private String GroupField;
	
	private String RefNo;
	
	private int RefOID;
	
	StringBuffer Pub1=null;
	
	public DoModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		
	}
	
	public void init(){
		try
        {
			if(DoType.equals("DownTempFrm")){
				MapData md = new MapData(this.FK_MapData);
                DataSet ds = md.GenerHisDataSet();
                String name = "ccflow表单模板." + md.getName() + "." + md.getNo() + ".xml";
                String file = ContextHolderUtils.getRequest().getRealPath("/") + "Temp/" + this.FK_MapData + ".xml";
                ds.WriteXml(file);
                response.sendRedirect("../../Temp/" + this.FK_MapData + ".xml");
//                this.WinClose();
			}
			if(DoType.equals("CCForm")){
//				this.Application.Clear();
//                if (WebUser.getNoOfRel() != "admin")
//                {
//                    BP.Port.Emp emp = new BP.Port.Emp("admin");
//                    WebUser.SignInOfGener(emp);
//                }

                MapAttr mattr = new MapAttr();
                mattr.setMyPK(request.getParameter("MyPK"));
                int i = mattr.RetrieveFromDBSources();
                mattr.setKeyOfEn(request.getParameter("KeyOfEn"));
                mattr.setFK_MapData(request.getParameter("FK_MapData"));
                mattr.setMyDataType(Integer.parseInt(request.getParameter("DataType")));

                if (!StringHelper.isNullOrEmpty(request.getParameter("UIBindKey") + ""))
                    mattr.setUIBindKey(request.getParameter("UIBindKey"));
               
//                mattr.setUIContralType((UIContralType)request.getParameter("UIContralType"));
//                mattr.setLGType((BP.En.FieldTypeS)int.Parse(this.Request.QueryString["LGType"]));
                if (i == 0)
                {
//                    mattr.setName(value); = System.Web.HttpUtility.UrlDecode(this.Request.QueryString["KeyName"],System.Text.Encoding.GetEncoding("GB2312"));
                    mattr.setUIIsEnable(true);
                    mattr.setUIVisible(true);
                    if (mattr.getLGType() == FieldTypeS.Enum)
                        mattr.setDefVal("0");
                    mattr.Insert();
                }
                else
                {
                    mattr.Update();
                }
                
                
                if(mattr.getLGType().equals(BP.En.FieldTypeS.Enum)){
                	 response.sendRedirect("EditEnum.jsp?MyPK=" + mattr.getFK_MapData() + "&RefNo=" + mattr.getMyPK());
                     return;
                }
                else if(mattr.getLGType().equals(BP.En.FieldTypeS.Normal)){
                	   response.sendRedirect("EditF.jsp?DoType=Edit&MyPK=" + mattr.getFK_MapData() + "&RefNo=" + mattr.getMyPK() + "&FType=" + mattr.getMyDataType() + "&GroupField=0");
                       return;
                }
                else if(mattr.getLGType().equals(BP.En.FieldTypeS.FK)){
                	response.sendRedirect("EditTable.jsp?DoType=Edit&MyPK=" + mattr.getFK_MapData() + "&RefNo=" + mattr.getMyPK() + "&FType=" + mattr.getMyDataType() + "&GroupField=0");
                    return;
                }
                else{
                	
                }
             
			}
			else if(DoType.equals("DobackToF")){
//				 MapAttr ma = new MapAttr(this.RefNo);
//                 switch (ma.LGType)
//                 {
//                     case FieldTypeS.Normal:
//                         this.Response.Redirect("EditF.aspx?RefNo=" + this.RefNo, true);
//                         return;
//                     case FieldTypeS.FK:
//                         this.Response.Redirect("EditTable.aspx?RefNo=" + this.RefNo, true);
//                         return;
//                     case FieldTypeS.Enum:
//                         this.Response.Redirect("EditEnum.aspx?RefNo=" + this.RefNo, true);
//                         return;
//                     default:
//                         return;
//                 }

			}
			else if(DoType.equals("AddEnum")){
				SysEnumMain sem1 = new SysEnumMain(request.getParameter("EnumKey"));
                MapAttr attrAdd = new MapAttr();
                attrAdd.setKeyOfEn(sem1.getNo());
                if (attrAdd.IsExit(MapAttrAttr.FK_MapData, this.MyPK, MapAttrAttr.KeyOfEn, sem1.getNo()))
                {
                    BP.Sys.PubClass.Alert("字段已经存在 [" + sem1.getNo() + "]。",response);
//                    BP.Sys.PubClass.WinClose();
                    return;
                }

                attrAdd.setFK_MapData(this.MyPK);
                attrAdd.setName(sem1.getName());
                attrAdd.setUIContralType(UIContralType.DDL);
                attrAdd.setUIBindKey(sem1.getNo());
                attrAdd.setMyDataType(BP.DA.DataType.AppInt);
                attrAdd.setLGType(FieldTypeS.Enum);
                attrAdd.setDefVal("0");
                attrAdd.setUIIsEnable(true);
                if (this.IDX == null || "".equals(this.IDX))
                {
                    MapAttrs attrs1 = new MapAttrs(this.MyPK);
                    attrAdd.setIdx(0);
                }
                else
                {
                    attrAdd.setIdx(Integer.parseInt(this.IDX));
                }
                attrAdd.Insert();
                response.sendRedirect("EditEnum.jsp?MyPK=" + this.MyPK + "&RefNo=" + attrAdd.getMyPK());
//                this.WinClose();
                return;
			}
			else if(DoType.equals("DelEnum")){
				 String eKey = request.getParameter("EnumKey");
                 SysEnumMain sem = new SysEnumMain();
                 sem.setNo(eKey);
                 sem.Delete();
//                 this.WinClose();
                 return;

			}
			else if(DoType.equals("AddSysEnum")){
				this.AddFEnum();
			}
			else if(DoType.equals("AddSFTable")){
				this.AddSFTable();
			}
			else if(DoType.equals("AddSFTableAttr")){
				 SFTable sf = new SFTable(request.getParameter("RefNo"));
                 response.sendRedirect("EditTable.jsp?MyPK=" + this.MyPK + "&SFKey=" + sf.getNo());
//                 this.WinClose();
                 return;
			}
			else if(DoType.equals("AddFG")){
				
				if(RefNo.equals("IsPass")){
					 MapDtl dtl = new MapDtl(this.FK_MapData);
                     dtl.setIsEnablePass(true); /*更新是否启动审核分组字段.*/
                     MapAttr attr = new MapAttr();
                     attr.setFK_MapData(this.FK_MapData);
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
                     attr.setFK_MapData(this.FK_MapData);
                     attr.setKeyOfEn("Checker");
                     attr.setName("审核人");// "审核人";
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
                     attr.setFK_MapData(this.FK_MapData);
                     attr.setKeyOfEn("IsPass");
                     attr.setName("通过否?");// "审核人";
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
                     attr.setFK_MapData(this.FK_MapData);
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

                     /* 处理批次ID*/
                     attr = new MapAttr();
                     attr.setFK_MapData(this.FK_MapData);
                     attr.setKeyOfEn("BatchID");
                     attr.setName("BatchID");// this.ToE("IsPass", "是否通过");// "审核人";
                     attr.setMyDataType(DataType.AppInt);
                     attr.setUIIsEnable(false);
                     attr.setUIIsLine(false);
                     attr.setUIIsEnable(false);
                     attr.setUIVisible(false);
                     attr.setIdx(2);
                     attr.setDefVal("0");
                     attr.Insert();

                     dtl.Update();
//                     this.WinClose();
                     return;
				}
				else if(RefNo.equals("Eval")){
					MapAttr attr = new MapAttr();
                    attr.setFK_MapData(this.FK_MapData);
                    attr.setKeyOfEn("EvalEmpNo");
                    attr.setName("被评价人员编号");
                    attr.setMyDataType(DataType.AppString);
                    attr.setUIContralType(UIContralType.TB);
                    attr.setMaxLen(50);
                    attr.setMinLen( 0);
                    attr.setUIIsEnable(true);
                    attr.setUIIsLine(false);
                    attr.setUIIsEnable(false);
                    attr.setIsSigan(true);
                    attr.setIdx(1);
                    attr.Insert();

                    attr = new MapAttr();
                    attr.setFK_MapData(this.FK_MapData);
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
                    attr.setFK_MapData(this.FK_MapData);
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
                    attr.setFK_MapData(this.FK_MapData);
                    attr.setKeyOfEn("EvalNote");
                    attr.setName("评价信息");
                    attr.setMyDataType(DataType.AppString);
                    attr.setUIContralType(UIContralType.TB);
                    attr.setMaxLen(50);
                    attr.setMinLen(0);
                    attr.setUIIsEnable(true);
                    attr.setIdx(4);
                    attr.Insert();
//                    this.WinClose();
                    return;
				}
				else{
					
				}
			}
			else if(DoType.equals("AddFGroup")){
				 this.AddFGroup();
				 return;
			}
			else if(DoType.equals("AddF")){
				
			}
			else if(DoType.equals("ChodeFType")){
				this.AddF();
			}
			else if(DoType.equals("Up")){
			    MapAttr attrU = new MapAttr(this.RefNo);
                if (request.getParameter("IsDtl") != null)
                    attrU.DoDtlUp();
                else
                    attrU.DoUp();

//                this.WinClose();
			}
			else if(DoType.equals("Down")){
				 MapAttr attrD = new MapAttr(this.RefNo);
                 if (request.getParameter("IsDtl") == null)
                     attrD.DoDown();
                 else
                     attrD.DoDtlDown();
//                 this.WinClose();
			}
			else if(DoType.equals("Jump")){
				 MapAttr attrFrom = new MapAttr(request.getParameter("FromID"));
                 MapAttr attrTo = new MapAttr(request.getParameter("ToID"));
                 attrFrom.DoJump(attrTo);
//                 this.WinClose();
			}
			else if(DoType.equals("MoveTo")){
				 String toID = request.getParameter("ToID");
                 int toGFID = Integer.parseInt(request.getParameter("ToGID"));
                 int fromGID = Integer.parseInt(request.getParameter("FromGID"));
                 String fromID = request.getParameter("FromID");
                 MapAttr fromAttr = new MapAttr();
                 fromAttr.setMyPK(fromID);
                 fromAttr.Retrieve();
                 if (toGFID == fromAttr.getGroupID() && fromAttr.getMyPK() == toID)
                 {
                     /* 如果没有移动. */
//                     this.WinClose();
                     return;
                 }
                 if (toGFID != fromAttr.getGroupID() && fromAttr.getMyPK() == toID)
                 {
                     MapAttr toAttr = new MapAttr(toID);
                     fromAttr.Update(MapAttrAttr.GroupID, toAttr.getGroupID(), MapAttrAttr.Idx, toAttr.getIdx());
//                     this.WinClose();
                     return;
                 }
//                 response.sendRedirect(this.Request.RawUrl.Replace("MoveTo", "Jump"), true);
                 return;
			}
			else if(DoType.equals("Edit")){
				Edit();
			}
			else if(DoType.equals("Del")){
				 MapAttr attrDel = new MapAttr();
                 attrDel.setMyPK(this.RefNo);
                 attrDel.Delete();
//                 this.WinClose();
			}
			else if(DoType.equals("GFDoUp")){
				GroupField gf = new GroupField(this.RefOID);
                gf.DoUp();
                gf.Retrieve();
                if (gf.getIdx() == 0)
                {
//                    this.WinClose();
                    return;
                }
                int oidIdx = gf.getIdx();
                gf.setIdx(gf.getIdx() - 1);
                GroupField gfUp = new GroupField();
                if (gfUp.Retrieve(GroupFieldAttr.EnName, gf.getEnName(), GroupFieldAttr.Idx, gf.getIdx()) == 1)
                {
                    gfUp.setIdx(oidIdx);
                    gfUp.Update();
                }
                gf.Update();
//                this.WinClose();
			}
			else if(DoType.equals("GFDoDown")){
				GroupField mygf = new GroupField(this.RefOID);
                mygf.DoDown();
                mygf.Retrieve();
                int oidIdx1 = mygf.getIdx();
                mygf.setIdx(mygf.getIdx() + 1);
                GroupField gfDown = new GroupField();
                if (gfDown.Retrieve(GroupFieldAttr.EnName, mygf.getEnName(), GroupFieldAttr.Idx, mygf.getIdx()) == 1)
                {
                    gfDown.setIdx(oidIdx1);
                    gfDown.Update();
                }
                mygf.Update();
//                this.WinClose();
			}
			else if(DoType.equals("AthDoUp")){
				 FrmAttachment frmAth = new FrmAttachment(this.MyPK);
                 if (frmAth.getRowIdx() > 0)
                 {
                     frmAth.setRowIdx(frmAth.getRowIdx() - 1);
                     frmAth.Update();
                 }
//                 this.WinClose();
			}
			else if(DoType.equals("AthDoDown")){
				FrmAttachment frmAthD = new FrmAttachment(this.MyPK);
                if (frmAthD.getRowIdx() < 10)
                {
                    frmAthD.setRowIdx(frmAthD.getRowIdx() + 1);
                    frmAthD.Update();
                }
//                this.WinClose();
			}
			else if(DoType.equals("DtlDoUp")){
				MapDtl dtl1 = new MapDtl(this.MyPK);
                if (dtl1.getRowIdx() > 0)
                {
                    dtl1.setRowIdx(dtl1.getRowIdx() - 1);
                    dtl1.Update();
                }
//                this.WinClose();
			}
			else if(DoType.equals("DtlDoDown")){
				MapDtl dtl2 = new MapDtl(this.MyPK);
                if (dtl2.getRowIdx() < 10)
                {
                    dtl2.setRowIdx(dtl2.getRowIdx()+ 1);
                    dtl2.Update();
                }
//                this.WinClose();
			}
			else if(DoType.equals("M2MDoUp")){
				MapM2M ddtl1 = new MapM2M(this.MyPK);
                if (ddtl1.getRowIdx() > 0)
                {
                    ddtl1.setRowIdx(ddtl1.getRowIdx() - 1);
                    ddtl1.Update();
                }
//                this.WinClose();
			}
			else if(DoType.equals("M2MDoDown")){
				 MapM2M ddtl2 = new MapM2M(this.MyPK);
                 if (ddtl2.getRowIdx() < 10)
                 {
                     ddtl2.setRowIdx(ddtl2.getRowIdx() + 1);
                     ddtl2.Update();
                 }
//                 this.WinClose();
			}
			 
			 
        }
        catch (Exception ex)
        {
//            this.Pub1.AddMsgOfWarning("错误:", ex.getMessage() + " <br>" + this.Request.RawUrl);
        }
	}
	
	 public void Edit()
     { 
     }
   
     public void AddF()
     {
//         this.Title = "增加新字段向导";
//
//         this.Pub1.AddFieldSet("新增普通字段");
//         this.Pub1.AddUL();
//         this.Pub1.AddLi("<a href='EditF.aspx?DoType=Add&MyPK=" + this.MyPK + "&FType=" + BP.DA.DataType.AppString + "&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>字符型</b></a> - <font color=Note>如:姓名、地址、邮编、电话</font>");
//         this.Pub1.AddLi("<a href='EditF.aspx?DoType=Add&MyPK=" + this.MyPK + "&FType=" + BP.DA.DataType.AppInt + "&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>整数型</b></a> - <font color=Note>如:年龄、个数。</font>");
//         this.Pub1.AddLi("<a href='EditF.aspx?DoType=Add&MyPK=" + this.MyPK + "&FType=" + BP.DA.DataType.AppMoney + "&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>金额型</b></a> - <font color=Note>如:单价、薪水。</font>");
//         this.Pub1.AddLi("<a href='EditF.aspx?DoType=Add&MyPK=" + this.MyPK + "&FType=" + BP.DA.DataType.AppFloat + "&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>浮点型</b></a> - <font color=Note>如：身高、体重、长度。</font>");
//         this.Pub1.AddLi("<a href='EditF.aspx?DoType=Add&MyPK=" + this.MyPK + "&FType=" + BP.DA.DataType.AppDate + "&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>日期型</b></a> - <font color=Note>如：出生日期、发生日期。</font>");
//         this.Pub1.AddLi("<a href='EditF.aspx?DoType=Add&MyPK=" + this.MyPK + "&FType=" + BP.DA.DataType.AppDateTime + "&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>日期时间型</b></a> - <font color=Note>如：发生日期时间</font>");
//         this.Pub1.AddLi("<a href='EditF.aspx?DoType=Add&MyPK=" + this.MyPK + "&FType=" + BP.DA.DataType.AppBoolean + "&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>Boole型(是/否)</b></a> - <font color=Note>如：是否完成、是否达标</font>");
//         this.Pub1.AddULEnd();
//         this.Pub1.AddFieldSetEnd();
//
//         this.Pub1.AddFieldSet("新增枚举字段(用来表示，状态、类型...的数据。)");
//         this.Pub1.AddUL();
//         this.Pub1.AddLi("<a href='Do.aspx?DoType=AddSysEnum&MyPK=" + this.MyPK + "&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>枚举型</b></a> -  比如：性别:男/女。请假类型：事假/病假/婚假/产假/其它。");
//         this.Pub1.AddULEnd();
//         this.Pub1.AddFieldSetEnd();
//
//         this.Pub1.AddFieldSet("新增外键字段(字典表，通常只有编号名称两个列)");
//         this.Pub1.AddUL();
//         this.Pub1.AddLi("<a href='Do.aspx?DoType=AddSFTable&MyPK=" + this.MyPK + "&FType=Class&IDX=" + this.IDX + "&GroupField=" + this.GroupField + "'><b>外键型</b></a> -  比如：岗位、税种、行业、经济性质。");
//         this.Pub1.AddULEnd();
//         this.Pub1.AddFieldSetEnd();
//
//         this.Pub1.AddFieldSet("<div onclick=\"javascript:HidShowSysField();\" >系统约定字段-隐藏/显示</div> ");
//         string info = DataType.ReadTextFile2Html(BP.Sys.SystemConfig.getPathOfData() + "SysFields.txt");
//         this.Pub1.Add("<div id='SysField' style='display:none' >" + info + "</div>");
//         this.Pub1.AddFieldSetEnd();
     }

     public void AddFEnum()
     {
//         this.Title = "增加新字段向导";
//         this.Pub1.AddTable();
//         this.Pub1.AddCaptionLeft("<a href='Do.aspx?DoType=AddF&MyPK=" + this.MyPK + "&IDX=" + this.IDX + "'>增加新字段向导</a> - <a href='SysEnum.aspx?DoType=New&MyPK=" + this.MyPK + "&IDX=" + this.IDX + "' ><img src='../Img/Btn/New.gif' />新建枚举</a>");
//         this.Pub1.AddTR();
//         this.Pub1.AddTDTitle("IDX");
//         this.Pub1.AddTDTitle("编号(点击增加到表单)");
//         this.Pub1.AddTDTitle("名称");
//         this.Pub1.AddTDTitle("操作");
//         this.Pub1.AddTDTitle();
//         this.Pub1.AddTREnd();
//
//         BP.Sys.SysEnumMains sems = new SysEnumMains();
//         QueryObject qo = new QueryObject(sems);
//         this.Pub2.BindPageIdx(qo.GetCount(), pageSize, this.PageIdx, "Do.aspx?DoType=AddSysEnum&MyPK=" + this.MyPK + "&IDX=&GroupField");
//         qo.DoQuery("No", pageSize, this.PageIdx);
//
//         bool is1 = false;
//         int idx = 0;
//         foreach (BP.Sys.SysEnumMain sem in sems)
//         {
//             BP.Web.Controls.DDL ddl = null;
//             try
//             {
//                 ddl = new BP.Web.Controls.DDL();
//                 ddl.BindSysEnum(sem.No);
//             }
//             catch
//             {
//                 sem.Delete();
//             }
//             idx++;
//             is1 = this.Pub1.AddTR(is1);
//             this.Pub1.AddTDIdx(idx);
//             this.Pub1.AddTD("<a  href=\"javascript:AddEnum('" + this.MyPK + "','" + this.IDX + "','" + sem.No + "')\" >" + sem.No + "</a>");
//             this.Pub1.AddTD(sem.Name);
//             this.Pub1.AddTD("[<a href='SysEnum.aspx?DoType=Edit&MyPK=" + this.MyPK + "&IDX=" + this.IDX + "&RefNo=" + sem.No + "' >编辑</a>]");
//             this.Pub1.AddTD(ddl);
//             this.Pub1.AddTREnd();
//         }
//         this.Pub1.AddTableEnd();
     }
     /// <summary>
     /// 增加分组.
     /// </summary>
     public void AddFGroup()
     {
//         this.Pub1.AddFieldSet("插入列组");
//
//         this.Pub1.AddUL();
//         BP.Sys.FieldGroupXmls xmls = new FieldGroupXmls();
//         xmls.RetrieveAll();
//         foreach (FieldGroupXml en in xmls)
//         {
//             this.Pub1.AddLi("<a href='Do.aspx?DoType=AddFG&RefNo=" + en.No + "&FK_MapData=" + this.FK_MapData + "' >" + en.Name + "</a><br>" + en.Desc);
//         }
//         this.Pub1.AddULEnd();
//         this.Pub1.AddFieldSetEnd();
     }
     int pageSize = 10;
     public void AddSFTable()
     {
//         this.Title = "增加新字段向导";
//
//         this.Pub1.AddTable();
//         this.Pub1.AddCaption("<a href='Do.aspx?DoType=AddF&MyPK=" + this.MyPK + "&IDX=" + this.IDX + "'>增加新字段向导</a> - 增加外键字段 - <a href='SFTable.aspx?DoType=New&MyPK=" + this.MyPK + "&IDX=" + this.IDX + "' > 新建表</a>");
//         this.Pub1.AddTR();
//         this.Pub1.AddTDTitle("IDX");
//         this.Pub1.AddTDTitle("编号(点击增加到表单)");
//         this.Pub1.AddTDTitle("名称");
//         this.Pub1.AddTDTitle("类别");
//         this.Pub1.AddTDTitle("描述/编辑");
//         this.Pub1.AddTDTitle("编辑数据");
//         this.Pub1.AddTREnd();
//
//         BP.Sys.SFTables ens = new SFTables();
//         QueryObject qo = new QueryObject(ens);
//         this.Pub2.BindPageIdx(qo.GetCount(), pageSize, this.PageIdx,
//             "Do.aspx?DoType=AddSFTable&MyPK=" + this.MyPK + "&IDX=&GroupField");
//         qo.DoQuery("No", pageSize, this.PageIdx);
//
//         bool is1 = false;
//         int idx = 0;
//         foreach (BP.Sys.SFTable sem in ens)
//         {
//             idx++;
//             //is1 = this.Pub1.AddTR(is1);
//             is1 = this.Pub1.AddTR(is1);
//             this.Pub1.AddTDIdx(idx);
//             this.Pub1.AddTD("<a  href=\"javascript:AddSFTable('" + this.MyPK + "','" + this.IDX + "','" + sem.No + "')\" >" + sem.No + "</a>");
//             this.Pub1.AddTD(sem.Name);
//
//             if (sem.IsClass)
//                 this.Pub1.AddTD("<a href=\"javascript:WinOpen('../Comm/Search.aspx?EnsName=" + sem.No + "','sg')\"  ><img src='../Img/Btn/Edit.gif' border=0/>" + sem.TableDesc + "</a>");
//             else
//                 this.Pub1.AddTD("<a href=\"javascript:WinOpen('SFTable.aspx?DoType=Edit&MyPK=" + this.MyPK + "&IDX=" + this.IDX + "&RefNo=" + sem.No + "','sg')\"  ><img src='../Img/Btn/Edit.gif' border=0/>" + sem.TableDesc + "</a>");
//
//             if (sem.No.Contains("."))
//                 this.Pub1.AddTD("&nbsp;");
//             else
//                 this.Pub1.AddTD("<a href=\"javascript:WinOpen('SFTableEditData.aspx?RefNo=" + sem.No + "');\" >编辑</a>");
//             this.Pub1.AddTREnd();
//         }
//         this.Pub1.AddTableEnd();
     }

}
