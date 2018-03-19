package cn.jflow.controller.wf.mapdef;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.MapAttr;
import BP.Sys.MapData;
import BP.Sys.MapDatas;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/WF/MapDef")
public class EleBatchController extends BaseController{
	@RequestMapping(value = "/btn_Click4", method = RequestMethod.POST)
	public void btn_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		MapDatas mds = this.getGetMDs();
		HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
		MapAttr mattrOld = new MapAttr(object.getFK_MapData(), object.getKeyOfEn());
		MapAttr mattr = new MapAttr(object.getFK_MapData(), object.getKeyOfEn());
		for (MapData md : mds.ToJavaList())
		{
			CheckBox cb = (CheckBox) controls.get("CB_" + md.getNo());
			if (cb==null)
			{
				continue;
			}

			if (cb.getChecked() == false)
			{
				continue;
			}

			if (this.getDoType().equals("Copy"))
			{
				//执行批量Copy
				mattr.setFK_MapData(md.getNo());
				mattr.Insert();
				mattr.setIdx(mattrOld.getIdx());
			}

			if (this.getDoType().equals("Update"))
			{
				//执行批量Update
				MapAttr mattrUpdate = new MapAttr(md.getNo(), object.getKeyOfEn());
				int gID = mattrUpdate.getGroupID();
				mattrUpdate.Copy(mattrOld);
				mattrUpdate.setFK_MapData(md.getNo());
				mattrUpdate.setGroupID(gID);
				mattrUpdate.Update();
			}

			if (this.getDoType().equals("Delete"))
			{
				//执行批量 Delete 
				MapAttr mattrDelete = new MapAttr(md.getNo(), object.getKeyOfEn());
				mattrDelete.Delete();
			}

		}
		// 转向.
//		this.Response.Redirect(this.Request.RawUrl, true);
		try {
			response.sendRedirect("EleBatch.jsp?EleType="+object.getEleType()+"&FK_MapData="+object.getFK_MapData()+"&KeyOfEn="+object.getKeyOfEn()+"&DoType="+object.getDoType());
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	public MapDatas getGetMDs() {
		String sql = "SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getFK_Flow() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String nds = "";
		for (DataRow dr : dt.Rows) {
			nds += ",'ND" + dr.getValue(0).toString() + "'";
		}
		sql = "SELECT No FROM Sys_MapData WHERE No IN (" + nds.substring(1)
				+ ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL(sql);

		return mds;
	}

}
