package cn.jflow.controller.wf.mapdef;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Sys.AttachmentUploadType;
import BP.Sys.FrmAttachment;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;
import BP.Sys.MapDtl;
import BP.Sys.MapDtlAttr;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping(value="/WF/MapDef")
public class MapDtlController {
	
	public final String getDoType(){
		String v = ContextHolderUtils.getRequest().getParameter("DoType");
		if (StringHelper.isNullOrEmpty(v)){
			v = "New";
		}
		return v;
	}
	
	private String getParameter(String key){
		 return ContextHolderUtils.getRequest().getParameter(key);
	}
	/**
	 * 保存
	 */
	@RequestMapping(value="/SaveMapDtl",method=RequestMethod.POST)
	public @ResponseBody void saveMapDtl(HttpServletRequest request, HttpServletResponse response){
		doSave(request, false);
	}
	
	/**
	 * 保存关闭
	 */
	@RequestMapping(value="/SaveCancelMapDtl",method=RequestMethod.POST)
	public @ResponseBody void saveCancelMapDtl(HttpServletRequest request, HttpServletResponse response){
		doSave(request, true);
	}
	
	/**
	 * 保存数据，是否关闭当前窗口
	 * @param request
	 * @param isCancel
	 */
	public void doSave(HttpServletRequest request, boolean isCancel){
		HashMap<String, BaseWebControl> controls = null;
		try
		{
			controls = HtmlUtils.httpParser(request.getParameter("ddl_html"), request);
			
			if(StringHelper.isNullOrEmpty(getDoType()) || getDoType().equals("New")){
			 
				
				MapDtl dtlN = new MapDtl();
				dtlN = (MapDtl)BaseModel.Copy(request, dtlN, null, dtlN.getEnMap(), controls);
				try{
					dtlN.setGroupField(getParameter("DDL_GroupField"));
				}
				catch (java.lang.Exception e){}

				if (this.getDoType().equals("New")){
					if (dtlN.getIsExits()){
						BaseModel.Alert("已存在编号：" + dtlN.getNo());
						BaseModel.backPage();
						return;
					}
				}
				
				dtlN.setFK_MapData(getFK_MapData());
				dtlN.setGroupID(0);
				dtlN.setRowIdx(0);
				GroupFields gfs1 = new GroupFields(getFK_MapData());
				if (gfs1.size() == 1){
					GroupField gf = (GroupField)gfs1.get(0);
					dtlN.setGroupID((int)gf.getOID());
				}else{
					dtlN.setGroupID(Integer.valueOf(getParameter("DDL_GroupID")));
				}
				dtlN.Insert();
				if (isCancel){
					BaseModel.WinClose();
					return;
				}
				BaseModel.sendRedirect("MapDtl.jsp?DoType=Edit&FK_MapDtl=" + dtlN.getNo() + "&FK_MapData=" + this.getFK_MapData());
			} else if (this.getDoType().equals("Edit")) {
					MapDtl dtl = new MapDtl(this.getFK_MapDtl());
					dtl = (MapDtl)BaseModel.Copy(request, dtl, null, dtl.getEnMap(), controls);

					//参数保存.
					boolean isEnableLink = false;
					if(null != getParameter("CB_" + MapDtlAttr.IsEnableLink)){
						isEnableLink = true;
					}
					dtl.setIsEnableLink(isEnableLink);
					dtl.setLinkLabel(getParameter("TB_" + MapDtlAttr.LinkLabel));
					dtl.setLinkTarget(getParameter("TB_" + MapDtlAttr.LinkTarget));
					dtl.setLinkUrl(getParameter("TB_" + MapDtlAttr.LinkUrl));

					//锁定.
					boolean isRowLock = false;
					if(null != getParameter("CB_" + MapDtlAttr.IsRowLock)){
						isRowLock = true;
					}
					dtl.setIsRowLock(isRowLock);

					//分组字段。
					try{
						dtl.setGroupField(getParameter("DDL_GroupField"));
					}
					catch (java.lang.Exception e2){}

					if (this.getDoType().equals("New")){
						if (dtl.getIsExits()){
							BaseModel.Alert("已存在编号：" + dtl.getNo());
							return;
						}
					}

					dtl.setFK_MapData(this.getFK_MapData());
					GroupFields gfs = new GroupFields(dtl.getFK_MapData());
					if (gfs.size() > 1){
						dtl.setGroupID(Integer.valueOf(getParameter("DDL_GroupID")));
					}

					if (this.getDoType().equals("New")){
						dtl.Insert();
					} else {
						dtl.Update();
					}

					if (isCancel) {
						BaseModel.WinClose();
						return;
					}
					BaseModel.sendRedirect("MapDtl.jsp?DoType=Edit&FK_MapDtl=" + dtl.getNo() + "&FK_MapData=" + this.getFK_MapData());
			}
		}
		catch (RuntimeException ex)
		{
			BaseModel.Alert(ex.getMessage());
			BaseModel.backPage();
			ex.printStackTrace();
		}
	
	}
	
	/**
	 * 删除事件
	 */
	@RequestMapping(value="/DelMapDtl",method=RequestMethod.POST)
	private @ResponseBody void delMapDtl()
	{
		try {
			MapDtl dtl = new MapDtl();
			dtl.setNo(this.getFK_MapDtl());
			dtl.Delete();
			BaseModel.WinClose();
		} catch (RuntimeException ex) {
			BaseModel.Alert(ex.getMessage());
			BaseModel.backPage();
			ex.printStackTrace();
		}
	}
	
	/**
	 * 附件属性事件
	 */
	@RequestMapping(value="/MapAthMapDtl",method=RequestMethod.POST)
	public @ResponseBody void mapAthMapDtl() {

		FrmAttachment ath = new FrmAttachment();
		ath.setMyPK(this.getFK_MapDtl() + "_AthM");
		if (ath.RetrieveFromDBSources() == 0){
			ath.setFK_MapData(this.getFK_MapDtl());
			ath.setNoOfObj("AthM");
			ath.setName("我的从表附件");
			ath.setUploadType(AttachmentUploadType.Multi);
			ath.Insert();
		}
		BaseModel.sendRedirect("Attachment.jsp?DoType=Edit&FK_MapData=" + this.getFK_MapDtl() + "&UploadType=1&Ath=AthM");
	}
	
	/**
	 * 扩展事件
	 */
	@RequestMapping(value="/MapExtMapDtl",method=RequestMethod.POST)
	private @ResponseBody void mapExtMapDtl(){
		BaseModel.sendRedirect("MapExt.jsp?DoType=New&FK_MapData=" + this.getFK_MapDtl());
	}
	
	/**
	 * 新建事件
	 */
	@RequestMapping(value="/NewMapDtl",method=RequestMethod.POST)
	private @ResponseBody void newMapDtl(){
		BaseModel.sendRedirect("MapDtl.jsp?DoType=New&FK_MapData=" + this.getFK_MapData());
	}
	
	
//	@RequestMapping(value="/GoMapDtl",method=RequestMethod.POST)
//	private void goMapDtl(){
//		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//		dtl.IntMapAttrs();
//		
//		BaseModel.sendRedirect("MapDtlDe.jsp?DoType=Edit&FK_MapData=" + this.getFK_MapData() + "&FK_MapDtl=" + this.getFK_MapDtl());
//	}
	
	public final String getFK_MapDtl(){
		return getParameter("FK_MapDtl");
	}
	
	public final String getFK_MapData(){
		String fk_mapdata = getParameter("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata))
		{
			fk_mapdata = "ND" + getParameter("FK_Node");
		}
		return fk_mapdata;
	}
	
}
