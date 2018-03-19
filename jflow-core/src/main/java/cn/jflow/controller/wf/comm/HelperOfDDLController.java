package cn.jflow.controller.wf.comm;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.Port.Depts;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ConvertTools;
import cn.jflow.system.ui.core.RadioButton;

@Controller
@RequestMapping("/Wf/Comm")
@Scope("request")
public class HelperOfDDLController {

	@RequestMapping(value = "/selectKuang", method = RequestMethod.POST)
	public void SetDataV2(HttpServletRequest request,
			HttpServletResponse response) {
		// UCSys1.Clear();
		// Entities ens =
		// BP.En.ClassFactory.GetEns(this.Request.QueryString["EnsName"]);
		// 下面新增了三行

		StringBuilder UCSys1 = new StringBuilder();
		StringBuilder UCSys2 = new StringBuilder();

		String ensName = request.getParameter("EnsName");
		// Entities ens = BP.En.ClassFactory.GetEns("BP.WF.Template.Flows");
		Entities ens = BP.En.ClassFactory.GetEns(ensName);
		ens.RetrieveAll();
		// Entity en = ens.GetNewEntity;
		Entity en = ens.getGetNewEntity();
		String space = "";
		String selectName = request.getParameter("selectName");
		String refKey = request.getParameter("RefKey");
		String refText = request.getParameter("RefText");
		if (selectName.equals("None")) {
			// boolean isGrade = ens.IsGradeEntities;
			boolean isGrade = ens.getIsGradeEntities();
			if (isGrade) {
				UCSys1.append("<a name='top' ></a>");
				int num = ens.GetCountByKey("Grade", 2);
				if (num > 1) {
					int i = 0;
					/*
					 * UCSys1.BaseModel.AddTable(); UCSys1.BaseModel.AddTR();
					 * UCSys1.BaseModel.AddTDTitle("序号");
					 * UCSys1.BaseModel.AddTDTitle
					 * ("<img src='../../images/Home.gif' border=0 />数据选择导航");
					 * UCSys1.AddTREnd();
					 */
					UCSys1.append(BaseModel.AddTable());
					UCSys1.append(BaseModel.AddTR());
					UCSys1.append(BaseModel.AddTDTitle("序号"));
					UCSys1.append(BaseModel
							.AddTDTitle("<img src='../../images/Home.gif' border=0 />数据选择导航"));
					UCSys1.append(BaseModel.AddTREnd());
					for (Entity myen : ens.ToJavaListEn()) {
						if (myen.GetValIntByKey("Grade") != 2) {
							continue;
						}
						i++;
						/*
						 * UCSys1.AddTR(); UCSys1.AddTDIdx(i);
						 * UCSys1.AddTD("<a href='#ID" +
						 * myen.GetValStringByKey(this.getRefKey()) +
						 * "' >&nbsp;&nbsp;" +
						 * myen.GetValStringByKey(this.getRefKey()) +
						 * "&nbsp;&nbsp;" + myen.GetValStringByKey(refText) +
						 * "</a>"); UCSys1.AddTREnd();
						 */
						UCSys1.append(BaseModel.AddTR());
						UCSys1.append(BaseModel.AddTDIdx(i));
						UCSys1.append(BaseModel.AddTD("<a href='#ID"
								+ myen.GetValStringByKey(refKey)
								+ "' >&nbsp;&nbsp;"
								+ myen.GetValStringByKey(refKey)
								+ "&nbsp;&nbsp;"
								+ myen.GetValStringByKey(refText) + "</a>"));
						UCSys1.append(BaseModel.AddTREnd());
					}
					// UCSys1.AddTableEnd();
					UCSys1.append(BaseModel.AddTableEnd());
				}
			}
			/*
			 * UCSys1.AddTable(); UCSys1.AddTR(); UCSys1.AddTDTitle("IDX");
			 * UCSys1.AddTDTitle(""); UCSys1.AddTREnd();
			 */
			UCSys1.append(BaseModel.AddTable());
			UCSys1.append(BaseModel.AddTR());
			UCSys1.append(BaseModel.AddTDTitle("IDX"));
			UCSys1.append(BaseModel.AddTDTitle(""));
			UCSys1.append(BaseModel.AddTREnd());
			boolean is1 = false;

			int idx = 0;
			// for (Entity myen : ens) {
			for (Entity myen : ens.ToJavaListEn()) {
				idx++;
				/*
				 * is1 = UCSys1.AddTR(is1); UCSys1.AddTDIdx(idx);
				 */
				// is1=UCSys1.append(AddTR(is1));
				UCSys1.append(BaseModel.AddTDIdx(idx));
				// RadioBtn rb = new RadioBtn();
				// rb.GroupName = "s";
				RadioButton rb = new RadioButton();
				rb.setGroupName("s");
				if (isGrade) {
					int grade = myen.GetValIntByKey("Grade");
					space = "";
					// space.PadLeft(grade - 1, '-')
					space = ConvertTools
							.padLeft(String.valueOf(grade), -1, "-");
					space = space.replace("-", "&nbsp;&nbsp;&nbsp;");
					// UCSys1.AddTD(space);
					switch (grade) {
					case 2:
						rb.setText("<a href='#top' name='ID"
								+ myen.GetValStringByKey(refKey)
								+ "' ><Img src='../../images/Top.gif' border=0 /></a><b><font color=green>"
								+ myen.GetValStringByKey(refKey)
								+ myen.GetValStringByKey(refText)
								+ "</font></b>");
						break;
					case 3:
						rb.setText("<b>" + myen.GetValStringByKey(refKey)
								+ myen.GetValStringByKey(refText) + "</b>");
						break;
					default:
						rb.setText(myen.GetValStringByKey(refKey)
								+ myen.GetValStringByKey(refText));
						break;
					}
				} else {
					rb.setText(myen.GetValStringByKey(refText));
				}
				// rb.ID = "RB_" + myen.GetValStringByKey(refKey);
				rb.setId("RB_" + myen.GetValStringByKey(refKey));
				rb.setName("RB_Group");
				String clientscript = "window.returnValue = '"
						+ myen.GetValStringByKey(refKey) + "';window.close();";
				// rb.Attributes["onclick"] = clientscript;
				rb.addAttr("onclick", clientscript);
				UCSys1.append(BaseModel.AddTD(rb));
				UCSys1.append(BaseModel.AddTREnd());
			}
			// UCSBaseModel.BaseModel.AddTBaseModel.AddTableEnd();
			UCSys1.append(BaseModel.AddTableEnd());
			PrintWriter out;
			try {
				out = response.getWriter();
				out.write(UCSys1.toString() + UCSys2.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		String key = request.getParameter("selectName");
		// Attr attr = en.EnMap.GetAttrByKey(key);
		Attr attr = en.getEnMap().GetAttrByKey(key);
		// if (attr.MyFieldType == FieldType.Enum || attr.MyFieldType ==
		// FieldType.PKEnum) {
		if (attr.getMyFieldType() == FieldType.Enum
				|| attr.getMyFieldType() == FieldType.PKEnum) {
			SysEnums ses = new SysEnums(attr.getKey());
			// UCSys1.BaseModel.AddTable(); //("<TABLE border=1 >");
			UCSys1.append(BaseModel.AddTable());
			// for (SysEnum se : ses) {
			for (SysEnum se : ses.ToJavaList()) {
				/*
				 * UCSys1.Add("<TR><TD class='Toolbar'>"); UCSys1.Add(se.Lab);
				 * UCSys1.Add("</TD></TR>"); UCSys1.Add("<TR><TD>");
				 * UCSys1.BaseModel.AddTable();
				 */
				UCSys1.append("<TR><TD class='Toolbar'>");
				UCSys1.append(se.getLab());
				UCSys1.append("</TD></TR>");
				UCSys1.append("<TR><TD>");
				UCSys1.append(BaseModel.AddTable());
				int i = -1;
				// for (Entity myen : ens){
				for (Entity myen : ens.ToJavaListEn()) {
					// if (myen.GetValIntByKey(attr.getKey()) != se.IntKey) {
					if (myen.GetValIntByKey(attr.getKey()) != se.getIntKey()) {
						continue;
					}

					i++;
					if (i == 3) {
						i = 0;
					}
					if (i == 0) {
						// UCSys1.Add("<TR>");
						UCSys1.append("<TR>");
					}

					// RadioBtn rb = new RadioBtn();
					RadioButton rb = new RadioButton();
					// rb.GroupName = "dsfsd";
					rb.setName("RB_Group");
					rb.setText(myen.GetValStringByKey(refText));
					// rb.ID = "RB_" + myen.GetValStringByKey(refKey);
					rb.setId("RB_" + myen.GetValStringByKey(refKey));
					String clientscript = "window.returnValue = '"
							+ myen.GetValStringByKey(refKey)
							+ "';window.close();";
					// rb.Attributes["ondblclick"] = clientscript;
					rb.addAttr("onclick", clientscript);
					UCSys1.append(rb);
					if (i == 2) {
						// UCSys1.Add("</TR>");
						UCSys1.append("</TR>");
					}
				}
				// UCSys1.Add("</TABLE>");
				// UCSys1.Add("</TD></TR>");
				UCSys1.append("</TABLE>");
				UCSys1.append("</TD></TR>");
			}
			// UCSys1.Add("</TABLE>");
			UCSys1.append("</TABLE>");

			PrintWriter out;
			try {
				out = response.getWriter();
				out.write(UCSys1.toString() + UCSys2.toString());
				// out.write(UCSys2.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if (attr.getKey().equals("FK_Dept")) {
			Depts depts = new Depts();
			depts.RetrieveAll();
			/*
			 * UCSys1.BaseModel.AddTR(); UCSys1.BaseModel.AddTDToolbar("一级分组");
			 * UCSys1.BaseModel.AddTREnd(); UCSys1.BaseModel.AddTR();
			 * UCSys1.BaseModel.AddTDBegin(); UCSys1.BaseModel.AddTable();
			 */
			UCSys1.append(BaseModel.AddTR());
			UCSys1.append(BaseModel.AddTDToolbar("一级分组"));
			UCSys1.append(BaseModel.AddTREnd());
			UCSys1.append(BaseModel.AddTR());
			UCSys1.append(BaseModel.AddTDBegin());
			UCSys1.append(BaseModel.AddTable());
			// 显示导航信息
			int i = 0;
			// int span = 2;
			// for (BP.Port.Dept Dept : Depts){
			for (BP.Port.Dept Dept : depts.ToJavaList()) {
				// if (Dept.Grade == 2 || Dept.Grade == 1) {
				if (Dept.getGrade() == 2 || Dept.getGrade() == 1) {
					i++;
					/*
					 * UCSys1.Add("<TR>"); UCSys1.BaseModel.AddTDIdx(i);
					 * UCSys1.BaseModel.AddTD("<a href='#ID_2" + Dept.No +
					 * "' >&nbsp;&nbsp;" + Dept.No + "&nbsp;&nbsp;" +
					 * Dept.getName() + "</a><BR>"); UCSys1.Add("</TR>");
					 */
					UCSys1.append("<TR>");
					UCSys1.append(BaseModel.AddTDIdx(i));
					UCSys1.append("<a href='#ID_2" + Dept.getNo()
							+ "' >&nbsp;&nbsp;" + Dept.getNo() + "&nbsp;&nbsp;"
							+ Dept.getName() + "</a><BR>");
					UCSys1.append("</TR>");
				}
			}
			/*
			 * UCSys1.BaseModel.AddTableEnd(); UCSys1.BaseModel.AddTDEnd();
			 * UCSys1.BaseModel.AddTREnd(); UCSys1.BaseModel.AddTR();
			 * UCSys1.BaseModel.AddTDToolbar("二级分组");
			 * UCSys1.BaseModel.AddTREnd(); UCSys1.BaseModel.AddTDBegin();
			 * UCSys1.BaseModel.AddTable();
			 */

			UCSys1.append(BaseModel.AddTableEnd());
			UCSys1.append(BaseModel.AddTDEnd());
			UCSys1.append(BaseModel.AddTREnd());
			UCSys1.append(BaseModel.AddTR());
			UCSys1.append(BaseModel.AddTDToolbar("二级分组"));
			UCSys1.append(BaseModel.AddTREnd());
			UCSys1.append(BaseModel.AddTDBegin());
			UCSys1.append(BaseModel.AddTable());
			// 显示导航信息
			// int i = 0;
			// int span = 2;
			i = 0;
			// for (BP.Port.Dept Dept : Depts)
			for (BP.Port.Dept Dept : depts.ToJavaList()) {
				i++;
				/*
				 * UCSys1.Add("<TR>"); UCSys1.BaseModel.AddTDIdx(i);
				 */
				UCSys1.append("<TR>");
				UCSys1.append(BaseModel.AddTDIdx(i));
				// if (Dept.Grade == 2) {
				if (Dept.getGrade() == 2) {
					// UCSys1.BaseModel.AddTD("&nbsp;&nbsp;<a name='ID_2" +
					// Dept.getNo() + "' >" + Dept.getNo() +
					// "</A>&nbsp;&nbsp;<a href='#ID" + Dept.getNo() + "' ><b>"
					// + Dept.getName() +
					// "</b></a><A HREF='#top'><Img src='../../images/Top.gif' border=0 /></a><BR>");
					UCSys1.append(BaseModel.AddTD("&nbsp;&nbsp;<a name='ID_2"
							+ Dept.getNo()
							+ "' >"
							+ Dept.getNo()
							+ "</A>&nbsp;&nbsp;<a href='#ID"
							+ Dept.getNo()
							+ "' ><b>"
							+ Dept.getName()
							+ "</b></a><A HREF='#top'><Img src='../../images/Top.gif' border=0 /></a><BR>"));
				} else {
					// UCSys1.BaseModel.AddTD("&nbsp;&nbsp;" + Dept.getNo() +
					// "&nbsp;&nbsp;<a href='#ID" + Dept.No + "' name='#ID_2" +
					// Dept.getNo() + "' >" + Dept.getName() + "</a><BR>");
					UCSys1.append(BaseModel.AddTD("&nbsp;&nbsp;" + Dept.getNo()
							+ "&nbsp;&nbsp;<a href='#ID" + Dept.getNo()
							+ "' name='#ID_2" + Dept.getNo() + "' >"
							+ Dept.getName() + "</a><BR>"));
				}

				// UCSys1.Add("</TR>");
				UCSys1.append("</TR>");
			}
			/*
			 * UCSys1.Add("</Table>"); UCSys1.Add("</TD></TR>");
			 */
			UCSys1.append("</Table>");
			UCSys1.append("</TD></TR>");
			// ============ 数据
			for (BP.Port.Dept groupen : depts.ToJavaList()) {
				/*
				 * UCSys1.Add("<TR><TD class='Toolbar' >");
				 * UCSys1.Add("<a href='#ID_2" + groupen.No + "' name='ID" +
				 * groupen.No +
				 * "' ><Img src='../../images/Top.gif' border=0 /></a>&nbsp;&nbsp;"
				 * + groupen.GetValStringByKey(attr.UIRefKeyText));
				 * UCSys1.Add("</TD></TR>"); UCSys1.Add("<TR><TD>");
				 * UCSys1.BaseModel.AddTable();
				 */
				UCSys1.append("<TR><TD class='Toolbar' >");
				UCSys1.append("<a href='#ID_2"
						+ groupen.getNo()
						+ "' name='ID"
						+ groupen.getNo()
						+ "' ><Img src='../../images/Top.gif' border=0 /></a>&nbsp;&nbsp;"
						+ groupen.GetValStringByKey(attr.getUIRefKeyText()));
				UCSys1.append("</TD></TR>");
				UCSys1.append("<TR><TD>");
				UCSys1.append(BaseModel.AddTable());
				i = -1;
				for (Entity myen : ens.ToJavaListEn()) {
					if (myen.GetValStringByKey(attr.getKey()) != groupen
							.GetValStringByKey(attr.getUIRefKeyValue())) {
						continue;
					}

					i++;
					if (i == 3) {
						i = 0;
					}

					if (i == 0) {
						// UCSys1.Add("<TR>");
						UCSys1.append("<TR>");
					}

					// RadioBtn rb = new RadioBtn();
					RadioButton rb = new RadioButton();
					rb.setGroupName("dsfsd");
					rb.setText(myen.GetValStringByKey(refText));
					// rb.ID = "RB_" + myen.GetValStringByKey(refKey);
					rb.setId("RB_" + myen.GetValStringByKey(refKey));
					String clientscript = "window.returnValue = '"
							+ myen.GetValStringByKey(refKey)
							+ "';window.close();";
					// rb.Attributes["ondblclick"] = clientscript;
					// rb.Attributes["onclick"] = clientscript;
					rb.addAttr("onclick", clientscript);
					// UCSys1.BaseModel.AddTD(rb);
					UCSys1.append(BaseModel.AddTD());

					if (i == 2) {
						// UCSys1.Add("</TR>");
						UCSys1.append("</TR>");
					}
				}
				/*
				 * UCSys1.Add("</Table>"); UCSys1.Add("</TD></TR>");
				 */
				UCSys1.append("</Table>");
				UCSys1.append("</TD></TR>");
			}
			// UCSys1.Add("</TABLE>");
			UCSys1.append("</TABLE>");
		} else {
			Entities groupens = ClassFactory.GetEns(attr.getUIBindKey());
			groupens.RetrieveAll();

			// UCSys1.BaseModel.AddTable(); //("<TABLE border=1 >");
			UCSys1.append(BaseModel.AddTable());
			int size = groupens.size();
			if (size > 19) {
				/*
				 * UCSys1.Add(
				 * "<TR><TD class='Toolbar' ><img src='../../images/Home.gif' border=0 />数据选择导航&nbsp;&nbsp;&nbsp;<font size='2'>提示:点分组连接就可到达分组数据</font></TD></TR>"
				 * ); UCSys1.Add("<TR><TD>"); UCSys1.BaseModel.AddTable();
				 */
				UCSys1.append("<TR><TD class='Toolbar' ><img src='../../images/Home.gif' border=0 />数据选择导航&nbsp;&nbsp;&nbsp;<font size='2'>提示:点分组连接就可到达分组数据</font></TD></TR>");
				UCSys1.append("<TR><TD>");
				UCSys1.append(BaseModel.AddTable());
				// 显示导航信息
				int i = 0;
				// int span = 2;
				for (Entity groupen : ens.ToJavaListEn()) {
					i++;
					/*
					 * UCSys1.BaseModel.AddTR(); UCSys1.BaseModel.AddTDIdx(i);
					 * UCSys1.BaseModel.AddTD("<a href='#ID" +
					 * groupen.GetValStringByKey(attr.UIRefKeyValue) +
					 * "' >&nbsp;&nbsp;" +
					 * groupen.GetValStringByKey(attr.UIRefKeyValue) +
					 * "&nbsp;&nbsp;" +
					 * groupen.GetValStringByKey(attr.UIRefKeyText) +
					 * "</a><BR>"); UCSys1.BaseModel.AddTREnd();
					 */
					UCSys1.append(BaseModel.AddTR());
					UCSys1.append(BaseModel.AddTDIdx(i));
					UCSys1.append(BaseModel.AddTD("<a href='#ID"
							+ groupen.GetValStringByKey(attr.getUIRefKeyValue())
							+ "' >&nbsp;&nbsp;"
							+ groupen.GetValStringByKey(attr.getUIRefKeyValue())
							+ "&nbsp;&nbsp;"
							+ groupen.GetValStringByKey(attr.getUIRefKeyText())
							+ "</a><BR>"));
					UCSys1.append(BaseModel.AddTREnd());
				}
				/*
				 * UCSys1.Add("</Table>"); UCSys1.Add("</TD></TR>");
				 */
				UCSys1.append("</Table>");
				UCSys1.append("</TD></TR>");
			}

			for (Entity groupen : groupens.ToJavaListEn()) {
				/*
				 * UCSys1.Add("<TR><TD class='Toolbar' >");
				 * UCSys1.Add("<a href='#top' name='ID" +
				 * groupen.GetValStringByKey(attr.UIRefKeyValue) +
				 * "' ><Img src='../../images/Top.gif' border=0 /></a>&nbsp;&nbsp;"
				 * + groupen.GetValStringByKey(attr.UIRefKeyText));
				 * UCSys1.Add("</TD></TR>"); UCSys1.Add("<TR><TD>");
				 * UCSys1.BaseModel.AddTable();
				 */

				UCSys1.append("<TR><TD class='Toolbar' >");
				UCSys1.append("<a href='#top' name='ID"
						+ groupen.GetValStringByKey(attr.getUIRefKeyValue())
						+ "' ><Img src='../../images/Top.gif' border=0 /></a>&nbsp;&nbsp;"
						+ groupen.GetValStringByKey(attr.getUIRefKeyText()));
				UCSys1.append("</TD></TR>");
				UCSys1.append("<TR><TD>");
				UCSys1.append(BaseModel.AddTable());
				int i = -1;
				// for (Entity myen : ens) {
				for (Entity myen : ens.ToJavaListEn()) {
					if (myen.GetValStringByKey(attr.getKey()) != groupen
							.GetValStringByKey(attr.getUIRefKeyValue())) {
						continue;
					}

					i++;
					if (i == 3) {
						i = 0;
					}

					if (i == 0) {
						// UCSys1.BaseModel.AddTR();
						UCSys1.append(BaseModel.AddTR());
					}

					// RadioBtn rb = new RadioBtn();
					RadioButton rb = new RadioButton();
					/*
					 * rb.GroupName = "dsfsd";
					 * rb.setText(myen.GetValStringByKey(refText)); rb.ID =
					 * "RB_" + myen.GetValStringByKey(refKey);
					 */
					rb.setGroupName("dsfsd");
					rb.setText(myen.GetValStringByKey(refText));
					rb.setId("RB_" + myen.GetValStringByKey(refKey));
					String clientscript = "window.returnValue = '"
							+ myen.GetValStringByKey(refKey)
							+ "';window.close();";
					// rb.Attributes["ondblclick"] = clientscript;
					// rb.Attributes["onclick"] = clientscript;
					rb.addAttr("onclick", clientscript);
					// UCSys1.BaseModel.AddTD(rb);
					UCSys1.append(BaseModel.AddTD(rb));
					if (i == 2) {
						// UCSys1.BaseModel.AddTREnd();
						UCSys1.append(BaseModel.AddTREnd());
					}
				}

				// UCSys1.BaseModel.AddTableEnd();
				// UCSys1.Add("</TD></TR>");
				UCSys1.append(BaseModel.AddTableEnd());
				UCSys1.append("</TD></TR>");
			}
			// UCSys1.BaseModel.AddTableEnd();
			UCSys1.append(BaseModel.AddTableEnd());
		}

		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(UCSys1.toString() + UCSys2.toString());
			// out.write(UCSys2.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// public void Save(HttpServletRequest request,HttpServletResponse response)
	// {
	// String pk1=request.getParameter("PK");
	// AttrOfOneVSM attr = this.getAttrOfOneVSM(request);
	//
	// //Entities ensOfMM = attr.EnsOfMM;
	// Entities ensOfMM = attr.getEnsOfMM();
	// QueryObject qo = new QueryObject(ensOfMM);
	// //qo.AddWhere(attr.AttrOfOneInMM,this.getPK());
	// qo.AddWhere(attr.getAttrOfOneInMM(),pk1);
	// qo.DoQuery();
	// try {
	// ensOfMM.Delete();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } // 删除以前保存得数据。
	//
	// AttrOfOneVSM attrOM = this.getAttrOfOneVSM(request);
	// //Entities ensOfM = attrOM.EnsOfM;
	// Entities ensOfM = attrOM.getEnsOfM();
	// ensOfM.RetrieveAll();
	// //for (Entity en : ensOfM) { // for(Entity en : ensOfM.ToJavaList()) {
	// //String pk = en.GetValStringByKey(attr.AttrOfMValue);
	// String pk = en.GetValStringByKey(attr.getAttrOfMValue());
	// String check_value = request.getParameter("CB_"+ pk);
	// if(null == check_value){
	// continue;
	// }
	// // CheckBox cb = (CheckBox)UCSys1.FindControl("CB_"+ pk);
	// // if (cb.getChecked()==false) {
	// // continue;
	// // }
	//
	// /* Entity en1 =ensOfMM.GetNewEntity;
	// en1.SetValByKey(attr.AttrOfOneInMM,this.getPK());
	// en1.SetValByKey(attr.AttrOfMInMM, pk);
	// en1.Insert();*/
	// Entity en1 =ensOfMM.getGetNewEntity();
	// en1.SetValByKey(attr.getAttrOfOneInMM(),pk1);
	// en1.SetValByKey(attr.getAttrOfMInMM(), pk);
	// en1.Insert();
	// }
	//
	// // Entity enP =
	// BP.En.ClassFactory.GetEn(this.Request.QueryString["EnsName"]);
	// Entity enP = BP.En.ClassFactory.GetEn(request.getParameter("EnsName"));
	// // if (enP.gEnMap.EnType!=EnType.View) {
	// if (enP.getEnMap().getEnType()!=EnType.View) {
	// // enP.SetValByKey(enP.PK, this.getPK()); // =this.PK;
	// enP.SetValByKey(enP.getPK(), pk1);
	// enP.Retrieve(); //查询。
	// enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
	// }
	// }
	/*
	 * public final AttrOfOneVSM getAttrOfOneVSM(HttpServletRequest request) {
	 * String attrKey = request.getParameter("AttrKey"); String ensNam =
	 * request.getParameter("EnsNam"); Entity en = ClassFactory.GetEn(ensNam);
	 * 
	 * 
	 * for(AttrOfOneVSM attr : en.EnMap.AttrsOfOneVSM) { if
	 * (attr.EnsOfMM.toString().equals(this.getAttrKey())) { return attr; } }
	 * 
	 * for (AttrOfOneVSM attr : en.getEnMap().getAttrsOfOneVSM()) {
	 * 
	 * if (attr.getEnsOfM().toString().equals(attrKey)) { return attr; } } throw
	 * new RuntimeException("错误没有找到属性． "); }
	 */
}
