package cn.jflow.controller.wf.comm;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.ToolBar;

@Controller
@RequestMapping("/WF/Comm")
public class CommSearchController extends BaseController {

	private Entity _HisEn;
	
	private Entities _HisEns;
	
	public StringBuilder UCSys1, UCSys2;
	
	private ToolBar toolbar;
	
	@Override
	public String getEnsName() {
		String str = super.getEnsName();
		if (StringHelper.isNullOrEmpty(str)) {
			str = super.getEnName();
		}
		if (StringHelper.isNullOrEmpty(str)) {
			PubClass.Alert("类名无效。", getResponse());
			try {
				winClose(getResponse());
			} catch (IOException e) {
				e.printStackTrace();
			}
			throw new RuntimeException("类名无效。");
		}
		return str;
	}
	
	public final Entity getHisEn()
	{
		if (_HisEn == null)
		{
			_HisEn = this.getHisEns().getGetNewEntity();
		}
		return _HisEn;
	}
	
	public final Entities getHisEns() {
		if (getEnsName() != null) {
			if (this._HisEns == null || !getEnsName().equals(_HisEns.getClass().getName())) {
				_HisEns = BP.En.ClassFactory.GetEns(this.getEnsName());
			}
		}
		return _HisEns;
	}

	private void SetDGData(int pageIdx) {
		Entities ens = this.getHisEns();
        Entity en = ens.getGetNewEntity();
//        QueryObject qo = new QueryObject(ens);
        QueryObject qo = this.toolbar.GetnQueryObject(ens, en);
        int maxPageNum = 0;
        try{
        	this.UCSys1 = new StringBuilder();
            this.UCSys2 = new StringBuilder();
              maxPageNum = BaseModel.BindPageIdx(UCSys2, qo.GetCount(),
                  SystemConfig.getPageSize(), pageIdx, "Search.jsp?EnsName=" + this.getEnsName());
            if (maxPageNum > 1)
                this.UCSys2.append("翻页键:← → PageUp PageDown");
        }catch(Exception e){
        	e.printStackTrace();
            try{
                en.CheckPhysicsTable();
            }catch(Exception wx){
            	wx.printStackTrace();
                BP.DA.Log.DefaultLogWriteLineError(wx.getMessage());
            }
            maxPageNum = BaseModel.BindPageIdx(UCSys2, qo.GetCount(), SystemConfig.getPageSize(), pageIdx, "Search.jsp?EnsName=" + this.getEnsName());
        }

        qo.DoQuery(en.getPK(), SystemConfig.getPageSize(), pageIdx);

        if (en.getEnMap().IsShowSearchKey){
            String keyVal = this.toolbar.GetTBByID("TB_Key").getText().trim();
            if (keyVal.length() >= 1){
                Attrs attrs = en.getEnMap().getAttrs();
                for(Object obj : ens.subList(0, ens.size())){
                    for(Attr attr : attrs){
                        if (attr.getIsFKorEnum())
                            continue;

//                        if (attr.getIsPK())
//                            continue;

                        switch (attr.getMyDataType()){
                            case DataType.AppRate:
                            case DataType.AppMoney:
                            case DataType.AppInt:
                            case DataType.AppFloat:
                            case DataType.AppDouble:
                            case DataType.AppBoolean:
                                continue;
                            default:
                                break;
                        }
                        Entity myen = (Entity) obj;
                        myen.SetValByKey(attr.getKey(), myen.GetValStrByKey(attr.getKey()).replace(keyVal, "<font color=red>" + keyVal + "</font>"));
                    }
                }
            }
        }
        BaseModel.DataPanelDtl(UCSys1, ens, null);

        int ToPageIdx = this.getPageIdx() + 1;
        int PPageIdx = this.getPageIdx() - 1;

        this.UCSys1.append("<SCRIPT language=javascript>");
        this.UCSys1.append("\t\n document.onkeydown = chang_page;");
        this.UCSys1.append("\t\n function chang_page() { ");
        //  this.UCSys3.Add("\t\n  alert(event.keyCode); ");
        if (this.getPageIdx() == 1)
        {
            this.UCSys1.append("\t\n if (event.keyCode == 37 || event.keyCode == 33) alert('已经是第一页');");
        }
        else
        {
            this.UCSys1.append("\t\n if (event.keyCode == 37  || event.keyCode == 38 || event.keyCode == 33) ");
            this.UCSys1.append("\t\n     location='Search.jsp?EnsName=" + this.getEnsName() + "&PageIdx=" + PPageIdx + "';");
        }

        if (this.getPageIdx() == maxPageNum)
        {
            this.UCSys1.append("\t\n if (event.keyCode == 39 || event.keyCode == 40 || event.keyCode == 34) alert('已经是最后一页');");
        }
        else
        {
            this.UCSys1.append("\t\n if (event.keyCode == 39 || event.keyCode == 40 || event.keyCode == 34) ");
            this.UCSys1.append("\t\n     location='Search.jsp?EnsName=" + this.getEnsName() + "&PageIdx=" + ToPageIdx + "';");
        }

        this.UCSys1.append("\t\n } ");
        this.UCSys1.append("</SCRIPT>");
	}

	@RequestMapping(value = "/SearchUI", method = RequestMethod.POST)
	public ModelAndView Search(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("PageIdx", "1");
		// this.ToolBar1.SaveSearchState(this.RptNo, null);
		java.util.Map controlMap = HtmlUtils.httpParser(getParamter("FormHtml"), request);
		UiFatory uiFatory = new UiFatory();
		uiFatory.setTmpMap(controlMap);
		
		toolbar = new ToolBar(getRequest(), getResponse(), uiFatory);
		this.SetDGData(1);
		toolbar.SaveSearchState(this.getEnsName(), null);
		
		// 转换json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("uc_sys_01", UCSys1.toString());
		jsonObject.put("uc_sys_02", UCSys2.toString());
		
		try {
			wirteMsg(getResponse(), jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(value = "/ExpExel", method = RequestMethod.POST)
	public void ExpExel(HttpServletRequest request, HttpServletResponse response) {
		java.util.Map controlMap = HtmlUtils.httpParser(getParamter("FormHtml"), request);
		UiFatory uiFatory = new UiFatory();
		uiFatory.setTmpMap(controlMap);
		
		toolbar = new ToolBar(getRequest(), getResponse(), uiFatory);
	
		Entities ens = this.getHisEns();
		Entity en = ens.getGetNewEntity();
		QueryObject qo = new QueryObject(ens);
		qo = toolbar.GetnQueryObject(ens, en);
		// 导出
		if (getDoType().equals("Exp")) {
			/* 如果是导出，就把它导出到excel. */
			try {
				String httpFilePath = BaseModel.ExportDGToExcel(qo.DoQueryToTable(), en.getEnMap(), en.getEnDesc());
				wirteMsg(getResponse(), httpFilePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

