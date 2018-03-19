package cn.jflow.controller.wf.ccform;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.Sys.FrmEventList;
import BP.Sys.FrmEvents;
import BP.Sys.GEEntity;
import BP.Sys.MapData;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.Tools.WFSealData;
import BP.WF.Node;
import BP.WF.Work;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;
@Controller
@RequestMapping("/WF/CCForm")
@Scope("request")
public class FrmController extends BaseController{
	private HashMap<String, BaseWebControl> ctrlMap;
	public final String getFK_MapData()
	{
		String fk_mapdata = ContextHolderUtils.getRequest().getParameter("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata))
		{
			fk_mapdata = "ND" + getFK_Node();
		}
		return fk_mapdata;
	}
	
	public final String getOIDPKVal(){
		return ContextHolderUtils.getRequest().getParameter("OIDPKVal");
	}
	
	public final String getTB_SealData(){
		return ContextHolderUtils.getRequest().getParameter("TB_SealData");
	}
	
	public final String getTB_SingData(){
		return ContextHolderUtils.getRequest().getParameter("TB_SingData");
	}
	
	public final String getTB_SealFile(){
		return ContextHolderUtils.getRequest().getParameter("TB_SealFile");
	}
	@RequestMapping(value = "/FrmSave", method = RequestMethod.POST)
	public void Btn_Save_Click(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			ctrlMap = HtmlUtils.httpParser(ContextHolderUtils.getRequest().getParameter("FrmBody"), request);
			
			if (String.valueOf(this.getFK_Node()).equals(this.getFK_MapData().replace("ND", "")))
			{
				this.SaveNode();
				return;
			}

			MapData md = new MapData(this.getFK_MapData());
			GEEntity en = md.getHisGEEn();
			en.SetValByKey("OID", this.getOIDPKVal());
			int i = en.RetrieveFromDBSources();
			Object tempVar = BaseModel.Copy(ContextHolderUtils.getRequest(), en, null, en.getEnMap(), ctrlMap);
			en = (GEEntity)((tempVar instanceof GEEntity) ? tempVar : null);
			FrmEvents fes = md.getFrmEvents();
			//new FrmEvents(this.FK_MapData);
			fes.DoEventNode(FrmEventList.SaveBefore, en);

			///#region 检查是否有ca签名.
			if (md.getIsHaveCA() == true)
			{
				if (!StringHelper.isNullOrEmpty(getTB_SealData()))
				{
					WFSealData sealData = new BP.Tools.WFSealData();
					sealData.RetrieveByAttrAnd(BP.Tools.WFSealDataAttr.OID, getWorkID(), BP.Tools.WFSealDataAttr.FK_Node, getFK_Node());


					if (StringHelper.isNullOrEmpty(sealData.getOID()))
					{
						sealData.setMyPK(DBAccess.GenerGUID());
						sealData.setOID(String.valueOf(getWorkID()));
						sealData.setFK_Node(String.valueOf(new Integer(getFK_Node())));
						sealData.setSealData(getTB_SealData());
						sealData.setRDT(DataType.getCurrentDataTime());
						sealData.setFK_MapData(this.getFK_MapData());
						sealData.Insert();
					}
					else
					{
						sealData.setSealData(getTB_SealData());
						sealData.setRDT(DataType.getCurrentDataTime());
						sealData.Update();
					}


					byte[] data = Base64.decodeBase64(getTB_SingData());

					if (data.length != 0)
					{
						
						ByteArrayInputStream bais = new ByteArrayInputStream(data);      
			            BufferedImage bi =ImageIO.read(bais);      
			            File image_path = new File(getTB_SealFile());
			            //可以是jpg,png,gif格式      
			            ImageIO.write(bi, "jpg", image_path);
			            
//						System.IO.MemoryStream MS = new System.IO.MemoryStream(data);
//						System.Drawing.Bitmap image = new System.Drawing.Bitmap(MS);
//						image.Save(getTB_SealFile(), System.Drawing.Imaging.ImageFormat.Jpeg);
					}
				}

			}
			///#endregion 检查是否有ca签名.

			if (i == 0)
			{
				en.Insert();
			}
			else
			{
				en.Update();
			}
			fes.DoEventNode(FrmEventList.SaveAfter, en);

			//this.get_response().sendRedirect("Frm.jsp?OID=" + en.GetValStringByKey("OID") + "&FK_Node=" + this.FK_Node + "&FID=" + this.FID + "&FK_MapData=" + this.FK_MapData, true);
		}
		catch (RuntimeException ex)
		{
			try {
				wirteMsg(ContextHolderUtils.getResponse(), ex.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
//			this.UCEn1.AddMsgOfWarning("error:", ex.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 保存点
	 
	*/
	public final void SaveNode()
	{
		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(this.getFID());
		if (wk.getOID() == 0)
		{
			wk.setOID(Integer.valueOf(this.getOID()));
		}
		wk.RetrieveFromDBSources();
		Object tempVar = BaseModel.Copy(ContextHolderUtils.getRequest(), wk, null, wk.getEnMap(), ctrlMap);
		wk = (Work)((tempVar instanceof Work) ? tempVar : null);
		try
		{
			wk.BeforeSave(); //调用业务逻辑检查。
		}
		catch (RuntimeException ex)
		{
			if (SystemConfig.getIsDebug())
			{
				wk.CheckPhysicsTable();
			}
			throw new RuntimeException("@在保存前执行逻辑检查错误。@技术信息:" + ex.getMessage());
		}


		wk.setRec(WebUser.getNo());
		wk.SetValByKey("FK_Dept", WebUser.getFK_Dept());
		wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
		FrmEvents fes = nd.getMapData().getFrmEvents();
		fes.DoEventNode(FrmEventList.SaveBefore, wk);

		try
		{
			wk.Update();
			fes.DoEventNode(FrmEventList.SaveAfter, wk);
		}
		catch (RuntimeException ex)
		{
			try
			{
				wk.CheckPhysicsTable();
			}
			catch (RuntimeException ex1)
			{
				throw new RuntimeException("@保存错误:" + ex.getMessage() + "@检查物理表错误：" + ex1.getMessage());
			}
			try {
				wirteMsg(ContextHolderUtils.getResponse(), ex.getMessage() + "@有可能此错误被系统自动修复,请您从新保存一次.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			//AlertMsg_Warning("错误", ex.getMessage() + "@有可能此错误被系统自动修复,请您从新保存一次.");
			return;
		}


		// this.get_response().sendRedirect("Frm.jsp?OID=" + wk.GetValStringByKey("OID") + "&FK_Node=" + this.FK_Node + "&WorkID=" + this.OID + "&FID=" + this.FID + "&FK_MapData=" + this.FK_MapData, true);
		return;
	}
}
