package cn.jflow.controller.des;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondType;
import BP.WF.Template.ConnDataFrom;
import cn.jflow.system.ui.core.NamesOfBtn;

@Controller
@RequestMapping("/des")
public class CondByParaController {

	@RequestMapping(value = "/condByPara_btn_Click", method = RequestMethod.GET)
	public ModelAndView btn_Click(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView mv=new ModelAndView();
		String btnId=request.getParameter("btnId");
		String FK_MainNode=request.getParameter("FK_MainNode");
		String ToNodeID=request.getParameter("ToNodeID");
		String HisCondType=request.getParameter("CondType");
		String FK_Flow=request.getParameter("FK_Flow");
		String MyPK =request.getParameter("MyPK")==null?"":request.getParameter("MyPK");
		String FK_Node=request.getParameter("FK_Node");
		String exp=request.getParameter("exp");
//		String exp = this.GetTextBoxByID("TB_Para").Text;
//        if (string.IsNullOrEmpty(exp))
//        {
//            this.Alert("请按格式填写表达式.");
//            return;
//        }

//        exp = exp.Trim();
//        string[] strs = exp.Split(' ');
//        if (strs.Length != 3)
//        {
//            this.Alert("表达式格式错误,请参考格式要求");
//            return;
//        }

        Cond cond = new Cond();
        cond.Delete(CondAttr.NodeID, FK_MainNode,
            CondAttr.ToNodeID, ToNodeID,
            CondAttr.CondType, Integer.parseInt(HisCondType));

//        var btn = sender as LinkBtn;
        if (NamesOfBtn.getEnumByCode(btnId) == NamesOfBtn.Delete)
        {
        	mv.addObject("FK_MainNode", FK_MainNode);
            mv.addObject("ToNodeID", ToNodeID);
            mv.addObject("CondType", HisCondType);
            mv.addObject("FK_Flow", FK_Flow);
            mv.addObject("MyPK", MyPK);
            mv.addObject("FK_Node", FK_Node);
            mv.addObject("success", "删除成功!");
            mv.setViewName("Admin/CondByPara");
            //this.Response.Redirect(this.Request.RawUrl, true);
            return mv;
        }

        cond.setMyPK(FK_MainNode + "_" + ToNodeID + "_" + CondType.forValue(Integer.parseInt(HisCondType)) + "_" + ConnDataFrom.Paras);
        cond.setHisDataFrom(ConnDataFrom.Paras);
        cond.setNodeID(Integer.parseInt(FK_MainNode));
        cond.setFK_Node(Integer.parseInt(FK_MainNode));
        cond.setFK_Flow(FK_Flow);
        cond.setToNodeID(Integer.parseInt(ToNodeID));
        cond.setOperatorValue(exp);
        cond.setFK_Flow(FK_Flow);
        cond.setHisCondType(CondType.forValue(Integer.parseInt(HisCondType)));
        cond.setFK_Node(Integer.parseInt(FK_Node));
        cond.Insert();
        mv.addObject("FK_MainNode", FK_MainNode);
        mv.addObject("ToNodeID", ToNodeID);
        mv.addObject("CondType", HisCondType);
        mv.addObject("FK_Flow", FK_Flow);
        mv.addObject("MyPK", MyPK);
        mv.addObject("FK_Node", FK_Node);
        mv.addObject("success", "保存成功!");
        mv.setViewName("Admin/CondByPara");
        return mv;
	}
}
