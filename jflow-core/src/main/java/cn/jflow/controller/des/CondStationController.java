package cn.jflow.controller.des;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.model.designer.CondStationModel;
import cn.jflow.system.ui.core.NamesOfBtn;
import BP.Port.Stations;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondType;
import BP.WF.Template.ConnDataFrom;

@Controller
@RequestMapping("/des")
public class CondStationController {
	@RequestMapping(value = "/condStation_btn_Save_Click", method = RequestMethod.GET)
	public ModelAndView btn_Save_Click(HttpServletRequest request,HttpServletResponse respone) throws Exception
    {
		ModelAndView mv=new ModelAndView();
		String FK_MainNode=request.getParameter("FK_MainNode");
		String ToNodeID=request.getParameter("ToNodeID");
		String HisCondType=request.getParameter("CondType");
		String FK_Flow=request.getParameter("FK_Flow");
		String MyPK =request.getParameter("MyPK")==null?"":request.getParameter("MyPK");
		String FK_Node=request.getParameter("FK_Node");
		String FK_Attr=request.getParameter("FK_Attr");
		String str=request.getParameter("str")==null?"":request.getParameter("str");
		String[] list=new String[10];
		list=str.split(",");
        Cond cond = new Cond();
        cond.Delete(CondAttr.NodeID, FK_MainNode,
          CondAttr.ToNodeID, ToNodeID,
          CondAttr.CondType, Integer.parseInt(HisCondType));
        String btnId=request.getParameter("btnId");
        //var btn = sender as LinkBtn;

        if (NamesOfBtn.getEnumByCode(btnId) == NamesOfBtn.Delete)
        {
        	mv.addObject("MyPK", MyPK);
            mv.addObject("FK_Flow", FK_Flow);
            mv.addObject("FK_Node", FK_Node);
            mv.addObject("FK_MainNode", FK_MainNode);
            mv.addObject("CondType", HisCondType);
            mv.addObject("FK_Attr", FK_Attr);
            mv.addObject("ToNodeID", ToNodeID);
        	mv.addObject("success", "删除成功!");
        	mv.setViewName("redirect:" + "/WF/Admin/CondStation.jsp");
           // this.Response.Redirect(this.Request.RawUrl, true);
            return mv;
        }

        // 删除岗位条件.
        cond.setMyPK(FK_MainNode + "_" + ToNodeID + "_" + CondType.forValue(Integer.parseInt(HisCondType)) + "_" + ConnDataFrom.Stas);

        if (cond.RetrieveFromDBSources() == 0)
        {
            cond.setHisDataFrom(ConnDataFrom.Stas);
            cond.setNodeID(Integer.parseInt(FK_MainNode));
            cond.setFK_Flow(FK_Flow);
            cond.setToNodeID(Integer.parseInt(ToNodeID));
            cond.Insert();
        }

        String val = "";
        Stations sts = new Stations();
        sts.RetrieveAllFromDBSource();

        CondStationModel con=new CondStationModel(request,respone);
        String num="";
        String num1="";
        if(!str.equals(""))
        {
       	 for (int i = 0; i < list.length; i++) {
     			num=list[i];
     			num1=num.substring(3, num.length());
     			val += "@" + num1;
     		 }
        }
//        for (int i = 0; i < sts.size(); i++) {
//        	Station st=(Station) sts.get(i);
//        	 if (con.map.get("CB_" + st.getNo()) == null)
//                 continue;
//             if (((CheckBox)con.map.get("CB_" + st.getNo())).getChecked())
//                 val += "@" + st.getNo();
//		}
//        for (Station st : sts)
//        {
//            if (this.Pub1.IsExit("CB_" + st.getNo()) == false)
//                continue;
//            if (this.Pub1.GetCBByID("CB_" + st.getNo()).Checked)
//                val += "@" + st.getNo();
//        }

        if ("".equals(val))
        {
            cond.Delete();
        }

        val += "@";
        cond.setOperatorValue(val);
        cond.setHisDataFrom(ConnDataFrom.Stas);
        cond.setFK_Flow(FK_Flow);
        cond.setHisCondType(CondType.forValue(Integer.parseInt(HisCondType)));
        cond.setFK_Node(Integer.parseInt(FK_MainNode));

        switch (CondType.forValue(Integer.parseInt(HisCondType)))
        {
            case Flow:
            case Node:
                cond.Update();
                mv.addObject("MyPK", cond.getMyPK());
                mv.addObject("FK_Flow", cond.getFK_Flow());
                mv.addObject("FK_Node", cond.getFK_Node());
                mv.addObject("FK_MainNode", cond.getNodeID());
                mv.addObject("CondType", cond.getHisCondType().getValue());
                mv.addObject("FK_Attr", cond.getFK_Attr());
                mv.addObject("ToNodeID", ToNodeID);
                //respone.sendRedirect("CondStation.jsp?MyPK=" + cond.getMyPK() + "&FK_Flow=" + cond.getFK_Flow() + "&FK_Node=" + cond.getFK_Node() + "&FK_MainNode=" + cond.getNodeID() + "&CondType=" + cond.getHisCondType().getValue() + "&FK_Attr=" + cond.getFK_Attr() + "&ToNodeID=" + ToNodeID);
                mv.setViewName("redirect:" + "/WF/Admin/CondStation.jsp");
                mv.addObject("success", "保存成功!");
                return mv;
            case Dir:
            	mv.addObject("MyPK", cond.getMyPK());
                mv.addObject("FK_Flow", cond.getFK_Flow());
                mv.addObject("FK_Node", cond.getFK_Node());
                mv.addObject("FK_MainNode", cond.getNodeID());
                mv.addObject("CondType",HisCondType);
                mv.addObject("FK_Attr", cond.getFK_Attr());
                cond.setToNodeID(Integer.parseInt(ToNodeID));
                mv.addObject("ToNodeID", cond.getToNodeID());
                cond.Update();
                //this.Response.Redirect("CondStation.aspx?MyPK=" + cond.MyPK + "&FK_Flow=" + cond.FK_Flow + "&FK_Node=" + cond.FK_Node + "&FK_MainNode=" + cond.NodeID + "&CondType=" + (int)cond.HisCondType + "&FK_Attr=" + cond.FK_Attr + "&ToNodeID=" + this.Request.QueryString["ToNodeID"], true);
                mv.setViewName("redirect:" + "/WF/Admin/CondStation.jsp");
                mv.addObject("success", "保存成功!");
                return mv;
            default:
                throw new Exception("未设计的情况。");
        }
    }
}
