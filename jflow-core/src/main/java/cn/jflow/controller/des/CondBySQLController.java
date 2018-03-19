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
public class CondBySQLController {
	@RequestMapping(value = "/condBySQL_btn_Click", method = RequestMethod.GET)
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
		String sql=request.getParameter("sql");
        if (NamesOfBtn.getEnumByCode(btnId) == NamesOfBtn.Delete)
        {
            //#region songhonggang (2014-06-15) 修改点击删除的时候删除条件
            Cond deleteCond = new Cond();
            deleteCond.Delete(CondAttr.NodeID, FK_MainNode,
              CondAttr.ToNodeID, ToNodeID,
              CondAttr.CondType, Integer.parseInt(HisCondType));
           // #endregion
            mv.addObject("FK_MainNode", FK_MainNode);
            mv.addObject("ToNodeID", ToNodeID);
            mv.addObject("CondType", HisCondType);
            mv.addObject("FK_Flow", FK_Flow);
            mv.addObject("MyPK", MyPK);
            mv.addObject("FK_Node", FK_Node);
            mv.addObject("success", "删除成功");
            mv.setViewName("Admin/CondBySQL");
           //this.Response.Redirect(this.Request.RawUrl, true);

            return mv;
        }

//        String sql = this.GetTextBoxByID("TB_SQL").Text;
//
//        if (string.IsNullOrEmpty(sql))
//        {
//            this.Alert("请填写sql语句.");
//            return;
//        }

        Cond cond = new Cond();
        cond.Delete(CondAttr.NodeID, FK_MainNode,
          CondAttr.ToNodeID, ToNodeID,
          CondAttr.CondType, Integer.parseInt(HisCondType));

        cond.setMyPK(FK_MainNode + "_" + ToNodeID + "_" + CondType.forValue(Integer.parseInt(HisCondType)) + "_" + ConnDataFrom.SQL);
        cond.setHisDataFrom(ConnDataFrom.SQL);
        cond.setNodeID(Integer.parseInt(FK_MainNode));
        cond.setFK_Node(Integer.parseInt(FK_MainNode));
        cond.setFK_Flow(FK_Flow);
        cond.setToNodeID(Integer.parseInt(ToNodeID));
        cond.setOperatorValue(sql);
        cond.setFK_Flow(FK_Flow);
        cond.setHisCondType(CondType.forValue(Integer.parseInt(HisCondType)));

        cond.Insert();
        mv.addObject("FK_MainNode", FK_MainNode);
        mv.addObject("ToNodeID", ToNodeID);
        mv.addObject("CondType", HisCondType);
        mv.addObject("FK_Flow", FK_Flow);
        mv.addObject("MyPK", MyPK);
        mv.addObject("FK_Node", FK_Node);
        mv.addObject("success", "保存成功");
        mv.setViewName("Admin/CondBySQL");
        return mv;
       // EasyUiHelper.AddEasyUiMessager(this, "保存成功！");
	}

}
