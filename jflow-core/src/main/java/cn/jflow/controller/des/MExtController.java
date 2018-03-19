package cn.jflow.controller.des;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jflow.common.model.BaseModel;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.model.designer.AutoFullModel;
import cn.jflow.model.designer.ListSelectionMode;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.RadioButton;
import cn.jflow.system.ui.core.TextBox;
import BP.DA.DataTable;
import BP.En.FieldTypeS;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.MapM2M;
import BP.Sys.MapM2Ms;
import BP.Sys.XML.RegularExpressionDtl;
import BP.Sys.XML.RegularExpressionDtls;
import BP.Tools.StringHelper;
import BP.WF.Glo;


@Controller
@RequestMapping(value="/DES")
public class MExtController extends BaseController{


	@RequestMapping(value="/SaveJiLian",method=RequestMethod.POST)
	public void btn_SaveJiLian_Click(HttpServletResponse response, HttpServletRequest request, String FK_MapData,String ExtType,TempObject object) throws IOException{
		String url = Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&RefNo = " + this.getRefNo();
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
		MapExt me = new MapExt();
		me.setMyPK(this.getMyPK());
		if (me.getMyPK().length() > 2)
			me.RetrieveFromDBSources();
		me = (MapExt) BaseModel.Copy(getRequest(), me, null, me.getEnMap(), controls);
		me.setExtType(ExtType);
		me.setDoc(this.getRequest().getParameter("TB_Doc"));
		DDL ddl=(DDL) controls.get("DDL_Oper");
		try {
			me.setAttrOfOper(ddl.getSelectedItemStringVal());
		} catch (Exception e) {
			
			printAlertReload(response, "没有与之对应的下拉框", url);
			return;
		}
		DDL ddl1=(DDL) controls.get("DDL_Attr");
		me.setAttrsOfActive(ddl1.getSelectedItemStringVal());
		if (me.getAttrsOfActive() == me.getAttrOfOper())
		{
			printAlertReload(response, "两个项目不能相同.", url);
			return ;
		}
		try
		{
			RadioButton rb=(RadioButton) controls.get("RB_1");
			if (rb.getChecked())
				me.setDoWay(1);
			else
				me.setDoWay(0);
		}
		catch (Exception ex)
		{
			me.setDoWay(0);
		}

		me.setFK_MapData(FK_MapData);
		try
		{
			me.setMyPK(FK_MapData + "_" + me.getExtType() + "_" + me.getAttrOfOper() + "_" + me.getAttrsOfActive());

			if (me.getDoc().contains("No") == false || me.getDoc().contains("Name") == false){
				printAlertReload(response, "在您的sql表达式里，必须有No,Name 还两个列。", url);
				return;
			}
				
			me.Save();
		}
		catch (Exception ex)
		{
			printAlertReload(response, ex.getMessage(), url);
			return;
		}
		BaseModel.sendRedirect(url);
	}

	@RequestMapping(value="/RBCheckChange",method=RequestMethod.POST)
	public String rbCheckChange(HttpServletRequest request, TempObject object,String DoType,String FK_MapData,String OperAttrKey,String ExtType){
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
		String path = BP.Sys.SystemConfig.getPathOfData() + "JSLib/";
		RadioButton rb = (RadioButton) controls.get("RB_0"); // sender as System.Web.UI.WebControls.RadioButton;
		if (!rb.getChecked())
			path = BP.Sys.SystemConfig.getPathOfDataUser() + "JSLib/";

		ListBox lb = (ListBox) controls.get("LB1");
		//        lb.setAutoPostBack(false);
		lb.setSelectionMode(ListSelectionMode.Multiple);
		//        lb.setRows(10);
		//        lb.SelectedIndexChanged += new EventHandler(lb_SelectedIndexChanged);
		String temFile = "s@xa";
		String file = temFile;
		if (!StringHelper.isNullOrEmpty(temFile))
		{
			file = file.substring(file.lastIndexOf('/') + 4);
			file = file.replace(".js", "");
		}
		else
		{
			file = "!!!";
		}

		MapExts mes = new MapExts();
		mes.Retrieve(MapExtAttr.FK_MapData, FK_MapData,
				MapExtAttr.AttrOfOper, OperAttrKey,
				MapExtAttr.ExtType, ExtType);


		File[] dics = new File(path).listFiles();

		//        String[] dirs = System.IO.Directory.GetDirectories(path);
		for(File f:dics){
			File[] ff=new File(f.getPath()).listFiles();
			for(File dir:ff)
			{
				String str=dir.getName();
				//                String[] strs = Directory.GetFiles(dir);
				//                for(String s:strs)
				//                {
				if (!str.contains(".js"))
					continue;

				ListItem li = new ListItem(str.replace(path, "").replace(".js", ""), str);
				if (str.contains(file))
					li.setSelected(true);

				lb.Items.add(li);
				//                }
			}
		}
		return "redirect:/WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&RefNo = " + this.getRefNo()+"&OperAttrKey="+OperAttrKey+"&DoType="+DoType;
	}

	@RequestMapping(value="/SaveInputCheck",method=RequestMethod.POST)
	public @ResponseBody void saveInputCheck(HttpServletRequest request,TempObject object,String DoType,String FK_MapData,String OperAttrKey,String ExtType) throws IOException{
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
		ListBox lb = (ListBox) controls.get("LB1");

		// 检查路径. 没有就创建它。
		String pathDir = BP.Sys.SystemConfig.getPathOfDataUser() + "JSLibData/";
		File file=new File(pathDir);
		if (!file.exists())
			file.mkdirs();
		
//		System.out.println("测试"+request.getParameter("LB1"));
		
		// 删除已经存在的数据.
		MapExt me = new MapExt();
		me.Retrieve(MapExtAttr.FK_MapData, FK_MapData,
				MapExtAttr.ExtType, ExtType,
				MapExtAttr.AttrOfOper, OperAttrKey);

//		for(ListItem li:lb.Items)
//		{
//			if (!li.getSelected())
//				continue;
			me = (MapExt) BaseModel.Copy(getRequest(), me, null, me.getEnMap(), controls);
			me.setExtType(ExtType);

			// 操作的属性.
			me.setAttrOfOper(OperAttrKey);
			//this.Pub2.GetDDLByID("DDL_Oper").SelectedItemStringVal;

			int doWay = 0;
			RadioButton rb=(RadioButton) controls.get("RB_0");
			if (!rb.getChecked())
				doWay = 1;

			me.setDoWay(doWay);
			me.setDoc(BP.DA.DataType.ReadTextFile(request.getParameter("LB1")));
			File info = new File(request.getParameter("LB1"));
			
			me.setTag2(info.getParent().substring(info.getParent().lastIndexOf("/")+1));

			//获取函数的名称.
			String func = me.getDoc();
			func = me.getDoc().substring(func.indexOf("function") + 8);
			func = func.substring(0, func.indexOf("("));
			me.setTag1(func.trim());

			// 检查路径,没有就创建它.
			File fi = new File(request.getParameter("LB1"));
			me.setTag(request.getParameter("LB1"));
			me.setFK_MapData(FK_MapData);
			me.setExtType(ExtType);
			me.setMyPK(FK_MapData + "_" + me.getExtType() + "_" + me.getAttrOfOper() + "_" + me.getTag1());
			try
			{
				me.Insert();
			}
			catch(Exception e)
			{
				me.Update();
			}
//		}

		//#region 把所有的js 文件放在一个文件里面。
		MapExts mes = new MapExts();
		mes.Retrieve(MapExtAttr.FK_MapData, FK_MapData,
				MapExtAttr.ExtType, ExtType);

		String js = "";
		for (MapExt me1: mes.ToJavaList())
		{
			js += "\r\n" + BP.DA.DataType.ReadTextFile(me1.getTag());
		}
		File f=new File(pathDir + "/" + FK_MapData + ".js");
		if (f.exists())
			f.delete();

		BP.DA.DataType.WriteFile(pathDir + "/" + FK_MapData + ".js", js.trim());
		//#endregion 把所有的js 文件放在一个文件里面。

		// 
		this.getResponse().sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&RefNo = " + this.getRefNo());
	}

	@RequestMapping(value="/ExpressionEdit",method=RequestMethod.POST)
	public void expressionSave(HttpServletResponse response,HttpServletRequest request,String FK_MapData,String ExtType,String OperAttrKey,TempObject object) throws IOException{
		
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
		
        BindRegularExpressionEdit_ClickSave("onblur",FK_MapData,ExtType,OperAttrKey,controls,request);
        BindRegularExpressionEdit_ClickSave("onchange",FK_MapData,ExtType,OperAttrKey,controls,request);

        BindRegularExpressionEdit_ClickSave("onclick",FK_MapData,ExtType,OperAttrKey,controls,request);

        BindRegularExpressionEdit_ClickSave("ondblclick",FK_MapData,ExtType,OperAttrKey,controls,request);

        BindRegularExpressionEdit_ClickSave("onkeypress",FK_MapData,ExtType,OperAttrKey,controls,request);
        BindRegularExpressionEdit_ClickSave("onkeyup",FK_MapData,ExtType,OperAttrKey,controls,request);
        BindRegularExpressionEdit_ClickSave("onsubmit",FK_MapData,ExtType,OperAttrKey,controls,request);
       

        response.sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + FK_MapData + "&ExtType=" + ExtType);
	}
	
	@RequestMapping(value="/SavePopVal",method=RequestMethod.POST)
	public void savePopVal(HttpServletResponse response,HttpServletRequest request,String FK_MapData,String ExtType,String OperAttrKey,TempObject object,String RefNo) throws IOException{

		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
		MapExt me = new MapExt();
        me.setMyPK(this.getMyPK());
        if (me.getMyPK().length() > 2)
            me.RetrieveFromDBSources();
        me = (MapExt)(MapExt) BaseModel.Copy(getRequest(), me, null, me.getEnMap(), controls);
        me.setExtType(ExtType);
        me.setDoc(request.getParameter("TB_Doc"));
        DDL ddl=(DDL) controls.get("DDL_Oper");
        
        me.setAttrOfOper(ddl.getSelectedItemStringVal());
        DDL ddl1=(DDL)controls.get("DDL_PopValFormat");
        me.SetPara("PopValFormat", ddl1.getSelectedItemStringVal());

        RadioButton rb = (RadioButton) controls.get("RB_Tag_0");
        if (rb.getChecked())
            me.setPopValWorkModel(0);
        else
            me.setPopValWorkModel(1);

        rb = (RadioButton) controls.get("RB_Tag3_0");
        if (rb.getChecked())
            me.setPopValSelectModel(0);
        else
            me.setPopValSelectModel(1);

        rb = (RadioButton) controls.get("RB_Tag4_0");
        if (rb.getChecked())
            me.setPopValShowModel(0);
        else
            me.setPopValShowModel(1);


        me.setFK_MapData(FK_MapData);
        me.setMyPK(FK_MapData + "_" + me.getExtType() + "_" + me.getAttrOfOper());
        me.Save();
        response.sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&RefNo = " + RefNo);
	}
	
    public void BindRegularExpressionEdit_ClickSave(String myEvent,String FK_MapData,String ExtType,String OperAttrKey,HashMap<String, BaseWebControl> controls,HttpServletRequest request)
    {
        MapExt me = new MapExt();
        me.setFK_MapData(FK_MapData);
        me.setExtType(ExtType);
        me.setTag(myEvent);
        me.setAttrOfOper(OperAttrKey);
        me.setMyPK(FK_MapData + "_" + OperAttrKey + "_" + me.getExtType() + "_" + me.getTag());
        me.Delete();
        
        me.setDoc(request.getParameter("TB_Doc_" + myEvent));
        me.setTag1(request.getParameter("TB_Tag1_" + myEvent));
        if (me.getDoc().trim().length() == 0)
            return;

        me.Insert();
    }
    
    @RequestMapping(value="/SaveAutoFull",method=RequestMethod.POST)
    public void SaveAutoFull(HttpServletResponse response,HttpServletRequest request,String FK_MapData,String ExtType,String MyPK,TempObject object) throws IOException{
    	
    	HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
    	
    	MapExt me = new MapExt();
        me.setMyPK(MyPK);
        if (me.getMyPK().length() > 2)
            me.RetrieveFromDBSources();
        BaseModel bm=new BaseModel(request,response);
        me = (MapExt)bm.Copy(getRequest(), me, null, me.getEnMap(), controls);
        me.setExtType(ExtType);
        me.setDoc(request.getParameter("TB_Doc"));
        DDL ddl=(DDL) controls.get("DDL_Oper");
        try {
        	me.setAttrOfOper(ddl.getSelectedItemStringVal());
		} catch (Exception e) {
			System.err.println("请选择下拉框");
			 this.getResponse().sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&RefNo=" + this.getRefNo());
		}
        me.setFK_MapData(FK_MapData);
        me.setMyPK(FK_MapData + "_" + me.getExtType() + "_" + me.getAttrOfOper());

        try
        {
            
            me.Save();
        }
        catch (Exception ex)
        {
            //this.Alert(ex.Message);
//            this.AlertMsg_Warning("SQL错误", ex.getMessage());
        	System.err.println("SQL错误");
            return;
        }
        this.getResponse().sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&RefNo=" + this.getRefNo());
    	
    }
    
    
    @RequestMapping(value="/SaveAutoFullDtl",method=RequestMethod.POST)
    public void SaveAutoFullDtl(HttpServletResponse response,HttpServletRequest request,String RefNo,String FK_MapData,String ExtType,String MyPK,TempObject object) throws IOException{

//    	HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
//				object.getFormHtml(), request);
    	
//    	var btn = sender as LinkBtn;
//        if (btn.ID.Contains("Cancel"))
//        {
//            this.Response.Redirect("MapExt.aspx?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo(), true);
//            return;
//        }

        MapExt myme = new MapExt(MyPK);
        MapDtls dtls = new MapDtls(myme.getFK_MapData());
        String info = "";
        String error = "";
        for (MapDtl dtl: dtls.ToJavaList())
        {
            if (StringHelper.isNullOrEmpty(request.getParameter("TB_" + dtl.getNo()).trim()))
                continue;
            try
            {
                //DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(tb.Text);
                //MapAttrs attrs = new MapAttrs(dtl.No);
                //string err = "";
                //foreach (DataColumn dc in dt.Columns)
                //{
                //    if (attrs.IsExits(MapAttrAttr.KeyOfEn, dc.ColumnName) == false)
                //    {
                //        err += "<br>列" + dc.ColumnName + "不能与从表 属性匹配.";
                //    }
                //}
                //if (err != "")
                //{
                //    error += "在为("+dtl.Name+")检查sql设置时出现错误:"+err;
                //}
            }
            catch (Exception ex)
            {
//                this.Alert("SQL ERROR: " + ex.getMessage());
            	System.err.println("SQL ERROR: " + ex.getMessage());
            	return;
            }
            info += "$" + dtl.getNo() + ":" + request.getParameter("TB_" + dtl.getNo()).trim();
        }

        if (error != "")
        {
            //this.Pub2.AddEasyUiPanelInfo("错误", "设置错误,请更正:<br />" + error, "icon-no");
            //this.Pub2.AddMsgOfWarning("设置错误,请更正:", error);
            return;
        }
        myme.setTag1(info);
        myme.Update();
        this.getResponse().sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&MyPK=" + MyPK + "&RefNo=" + RefNo);
    }
    
    @RequestMapping(value="/SaveAutoFullM2M",method=RequestMethod.POST)
    public void SaveAutoFullM2M(HttpServletResponse response,HttpServletRequest request,String RefNo,String FK_MapData,String ExtType,String MyPK,TempObject object) throws IOException{

//    	HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
//				object.getFormHtml(), true);
    	
        

        MapExt myme = new MapExt(MyPK);
        MapM2Ms m2ms = new MapM2Ms(myme.getFK_MapData());
        String info = "";
        String error = "";
        for (MapM2M m2m: m2ms.ToJavaList())
        {
            if (StringHelper.isNullOrEmpty(request.getParameter("TB_" + m2m.getNoOfObj())))
                continue;
            try
            {
                DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(request.getParameter("TB_" + m2m.getNoOfObj()));
                String err = "";
                if (!dt.Columns.get(0).ColumnName.equals("No"))
                    err += "第1列不是No.";
                if (!dt.Columns.get(1).ColumnName.equals("Name"))
                    err += "第2列不是Name.";

                if (!StringHelper.isNullOrEmpty(err))
                {
                    error += "在为(" + m2m.getName() + ")检查sql设置时出现错误：请确认列的顺序是否正确为大小写是否匹配。" + err;
                }
            }
            catch (Exception ex)
            {
//                this.Alert("SQL ERROR: " + ex.getMessage());
            	System.err.println("SQL ERROR: " + ex.getMessage());
                return;
            }
            info += "$" + m2m.getNoOfObj() + ":" + request.getParameter("TB_" + m2m.getNoOfObj());
        }

        if (error != "")
        {
            //this.Pub2.AddEasyUiPanelInfo("错误", "设置错误,请更正:<br />" + error, "icon-no");
            //this.Pub2.AddMsgOfWarning("设置错误,请更正:", error);
            return;
        }
        myme.setTag2(info);
        myme.Update();
        response.sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&MyPK=" + MyPK + "&RefNo=" + RefNo);
    }
    
    @RequestMapping(value="/AutoFullDLL",method=RequestMethod.POST)
    public String AutoFullDLL(HttpServletResponse response,HttpServletRequest request,String RefNo,String FK_MapData,String ExtType,String MyPK,TempObject object) throws IOException{
      
//    	HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
//				object.getFormHtml(), true);
    	String attr = request.getParameter("FK_Attr");
      MapExt me = new MapExt();
      me.setMyPK(MapExtXmlList.AutoFullDLL + "_" + this.getFK_MapData() + "_" + attr);
      me.RetrieveFromDBSources();
      me.setFK_MapData(FK_MapData);
      me.setAttrOfOper(attr);
      me.setExtType(MapExtXmlList.AutoFullDLL);
      me.setDoc(request.getParameter("TB_Doc").replace("'", "~"));

      try
      {
          DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(me.getDoc());
      }
      catch(Exception e)
      {
          //this.Alert("SQL不能被正确的执行，拼写有问题，请检查。");
    	  System.err.println("SQL不能被正确的执行，拼写有问题，请检查。");
    	  return "redirect:/WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + MapExtXmlList.AutoFullDLL;
      }

      me.Save();
      return "redirect:/WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + MapExtXmlList.AutoFullDLL;
    }
    
    
    @RequestMapping(value="/BindLinkEdit",method=RequestMethod.POST)
    public String BindLinkEdit(HttpServletResponse response,HttpServletRequest request,String RefNo,String FK_MapData,String ExtType,String OperAttrKey,String MyPK,TempObject object) throws IOException{
    	
    	HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
    	
    	MapExt me = new MapExt();
//      Button btn = sender as Button;
//      if (btn.ID == NamesOfBtn.Delete)
//      {
//          me.MyPK = this.getMyPK();
//          me.Delete();
//          this.Response.Redirect("MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType(), true);
//          return;
//      }

      me = (MapExt)BaseModel.Copy(getRequest(), me, null, me.getEnMap(), controls);
      me.setFK_MapData(FK_MapData);
      me.setAttrOfOper(OperAttrKey);
      //me.Tag = this.Pub2.GetTextBoxByID("TB_Tag").Text;
      //me.Tag1 = this.Pub2.GetTextBoxByID("TB_Tag1").Text;
      me.setExtType(ExtType);
      if (StringHelper.isNullOrEmpty(this.getMyPK()))
          me.setMyPK(me.getFK_MapData() + "_" + me.getAttrOfOper() + "_" + ExtType);
      else
          me.setMyPK(MyPK);
      me.Save();

      return "redirect:/WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + ExtType;
    	
    }

    @RequestMapping(value="/BindLinkDelete",method=RequestMethod.POST)
    public String BindLinkDelete(HttpServletResponse response,HttpServletRequest request,String RefNo,String FK_MapData,String ExtType,String OperAttrKey,String MyPK,TempObject object) throws IOException{
    	MapExt me = new MapExt();
    	me.setMyPK(MyPK);
    	me.Delete();
    	return "redirect:/WF/MapDef/MapExt.jsp?s=3&FK_MapData=" + this.getFK_MapData() + "&ExtType=" + ExtType;
    	
    }
    
    @RequestMapping(value="/SaveReTemplete",method=RequestMethod.POST)
    public void SaveReTemplete(HttpServletResponse response,HttpServletRequest request,String RefNo,String FK_MapData,String ExtType,String OperAttrKey,String MyPK,TempObject object) throws IOException{
    	
    	HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), true);
		ListBox lb = (ListBox) controls.get("LBReTemplete");
    	
    	
    	if (lb == null && lb.SelectedItem.getValue() == null) return;

    	String newMyPk = "";
    	RegularExpressionDtls reDtls = new RegularExpressionDtls();
    	reDtls.RetrieveAll();

    	//删除现有的逻辑.
    	MapExts exts = new MapExts();
    	exts.Delete(MapExtAttr.AttrOfOper, OperAttrKey,
    			MapExtAttr.ExtType, MapExtXmlList.RegularExpression);
    	String str="";
    	for(ListItem li:lb.Items)
		{
			if (!li.getSelected())
				continue;
			str=li.getValue();
		}
    	
    	// 开始装载.
    	for (RegularExpressionDtl dtl: reDtls.ToJavaList())
    	{
    		try {
    			if (!str.contains(dtl.getItemNo()))
        			continue;
			} catch (Exception e) {
				// 小周鹏修改 Start
				response.sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + ExtType + "&MyPK=" + newMyPk + "&OperAttrKey=" + OperAttrKey + "&DoType=New");
//				response.sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + ExtType + "&MyPK=" + newMyPk + "&OperAttrKey=" + OperAttrKey + "&DoType=New");
				// 小周鹏修改 End
			}

    		MapExt ext = new MapExt();
    		ext.setMyPK(FK_MapData + "_" + OperAttrKey + "_" + MapExtXmlList.RegularExpression + "_" + dtl.getForEvent());
    		ext.setFK_MapData(FK_MapData);
    		ext.setAttrOfOper(OperAttrKey);
    		ext.setDoc(dtl.getExp()); //表达公式.
    		ext.setTag(dtl.getForEvent()); //时间.
    		ext.setTag1(dtl.getMsg());  //消息
    		ext.setExtType(MapExtXmlList.RegularExpression); // 表达公式 .
    		ext.Insert();
    		newMyPk = ext.getMyPK();
    	}
    	// 小周鹏修改 Start
    	response.sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + ExtType + "&MyPK=" + newMyPk + "&OperAttrKey=" + OperAttrKey + "&DoType=New");
//    	response.sendRedirect(Glo.getCCFlowAppPath()+"WF/MapDef/MapExt.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + ExtType + "&MyPK=" + newMyPk + "&OperAttrKey=" + OperAttrKey + "&DoType=New");
    	// 小周鹏修改 End
    }

    @RequestMapping(value="/SaveAutoFullJilian",method=RequestMethod.POST)
    public String SaveAutoFullJilian(HttpServletResponse response,HttpServletRequest request,String RefNo,String FK_MapData,String ExtType,String OperAttrKey,String MyPK,TempObject object) throws IOException{

    	HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
				object.getFormHtml(), request);
    	
//      var btn = sender as LinkBtn;
//      if (btn.ID.Contains("Cancel"))
//      {
//          this.Response.Redirect("MapExt.aspx?FK_MapData=" + this.getFK_MapData() + "&ExtType=" + this.getExtType() + "&MyPK=" + this.getMyPK() + "&RefNo=" + this.getRefNo(), true);
//          return;
//      }

      MapExt myme = new MapExt(getMyPK());
      MapAttrs attrs = new MapAttrs(myme.getFK_MapData());
      String info = "";
      for (MapAttr attr: attrs.ToJavaList())
      {
          if (attr.getLGType() == FieldTypeS.Normal)
              continue;

          if (attr.getUIIsEnable() == false)
              continue;

          TextBox tb = (TextBox) controls.get("TB_" + attr.getKeyOfEn());
          if (StringHelper.isNullOrEmpty(tb.getText()))
              continue;

          try
          {
              DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(tb.getText());
              if (tb.getText().contains("@Key") == false)
                  throw new Exception("缺少@Key参数。");

              if (dt.Columns.contains("No") == false || dt.Columns.contains("Name") == false)
                  throw new Exception("在您的sql表单公式中必须有No,Name两个列，来绑定下拉框。");
          }
          catch (Exception ex)
          {
//              this.Alert("SQL ERROR: " + ex.getMessage());
        	  System.err.println("SQL ERROR: " + ex.getMessage());
        	  return "/WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&MyPK=" + MyPK + "&RefNo=" + RefNo;
          }
          info += "$" + attr.getKeyOfEn() + ":" + tb.getText();
      }
      myme.setTag(info);
      myme.Update();
//      this.Alert("保存成功.");
      return "/WF/MapDef/MapExt.jsp?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&MyPK=" + MyPK + "&RefNo=" + RefNo;
    }
    
    @RequestMapping(value="/SavePageLoadFull",method=RequestMethod.POST)
    public String SavePageLoadFull(HttpServletResponse response,HttpServletRequest request,String RefNo,String FK_MapData,String ExtType,String OperAttrKey,String MyPK,TempObject object,String url) throws IOException{
    	
    	Map<String,Object> map=new HashMap<String, Object>();
//    	System.out.println(url);
    	String[] s = url.split("&");
    	for(String ss:s){
    		String[] str=ss.split("=");
    		map.put(str[0], str[1]);
    	}
    	if(FK_MapData==null || FK_MapData.equals("")){
    		FK_MapData=(String) map.get("FK_MapData");
    	}
    	MapExt me = new MapExt();
    	me.setMyPK(FK_MapData + "_" + ExtType);
    	me.setFK_MapData(FK_MapData);
    	me.setExtType(ExtType);
    	me.RetrieveFromDBSources();
    	me.setTag(request.getParameter("TB_" + MapExtAttr.Tag));
    	String sql = "";
    	MapDtls dtls = new MapDtls(FK_MapData);
    	for (MapDtl dtl: dtls.ToJavaList())
    	{
    		sql += "*" + dtl.getNo() + "=" + request.getParameter("TB_" + dtl.getNo());
    	}
    	me.setTag1(sql);

    	me.setMyPK(FK_MapData + "_" + ExtType);

    	String info = me.getTag1() + me.getTag();
    	if (StringHelper.isNullOrEmpty(info))
    		me.Delete();
    	else
    		me.Save();
    	return "/WF/MapDef/MapExt.jsp?s=34&?FK_MapData=" + FK_MapData + "&ExtType=" + ExtType + "&RefNo=" + RefNo;
    }
    
    
//    public String getRefNo()
//	{
//		String s = getParamter("RefNo");
//		if (StringHelper.isNullOrEmpty(s))
//		{
//			s = getParamter("No");
//		}
//		return s;
//	}
    
	public final String getFK_MapData()
	{
		String fk_mapdata = getParamter("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata))
		{
			fk_mapdata = "ND" + getFK_Node();
		}
		return fk_mapdata;
	}
    
}
