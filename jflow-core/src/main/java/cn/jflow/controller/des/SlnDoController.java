package cn.jflow.controller.des;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
@Controller
@RequestMapping("/WF/MapDef")
public class SlnDoController extends BaseController{
	@RequestMapping(value = "/btn_Click", method = RequestMethod.POST)
	public void btn_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
			FrmField sln = new FrmField();
			sln.Retrieve(FrmFieldAttr.FK_MapData, object.getFK_MapData(), FrmFieldAttr.KeyOfEn, object.getKeyOfEn(), FrmFieldAttr.FK_Node, this.getFK_Node());

			String CB_Readonly=request.getParameter("CB_Readonly");
			if(CB_Readonly!=null&&CB_Readonly.equals("on")){
				sln.setUIIsEnable(true);
			}else{
				sln.setUIIsEnable(false);
			}
			String CB_Visable=request.getParameter("CB_Visable");
			if(CB_Visable!=null&&CB_Readonly.equals("on")){
				sln.setUIVisible(true);
			}else{
				sln.setUIVisible(false);
			}
			String CB_IsSigan=request.getParameter("CB_IsSigan");
//			System.out.println("----------"+CB_IsSigan);
			if(CB_IsSigan!=null&&CB_IsSigan.equals("on")){
				sln.setIsSigan(true);
			}else{
				sln.setIsSigan(false);
			}
			String TB_DefVal=request.getParameter("TB_DefVal");
			sln.setDefVal(TB_DefVal);
//			sln.UIIsEnable = this.Pub1.GetCBByID("CB_Readonly").Checked;
//			sln.UIVisible = this.Pub1.GetCBByID("CB_Visable").Checked;
//			
//			if (this.Pub1.IsExit("CB_IsSigan"))
//			{
//				sln.IsSigan = this.Pub1.GetCBByID("CB_IsSigan").Checked;
//			}
//			sln.DefVal = this.Pub1.GetTextBoxByID("TB_DefVal").getText();

			sln.setFK_MapData(object.getFK_MapData());
			sln.setKeyOfEn(object.getKeyOfEn());
			sln.setFK_Node(object.getFK_Node());
			sln.setFK_Flow(String.valueOf(object.getFK_Node()));

			sln.setMyPK(object.getFK_MapData() +"_"+object.getFK_Flow()+ "_" + object.getFK_Node() + "_" + object.getKeyOfEn());
			sln.CheckPhysicsTable();
			sln.Save();
			//this.WinClose();
		}
}
