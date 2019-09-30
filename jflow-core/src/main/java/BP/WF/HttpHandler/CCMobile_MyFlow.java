package BP.WF.HttpHandler;

import java.util.Enumeration;

import BP.DA.*;
import BP.Difference.ContextHolderUtils;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Web.*;
import BP.WF.*;

/**
 * 页面功能实体
 */
public class CCMobile_MyFlow extends WebContralBase {
	/**
	 * 构造函数
	 */
	public CCMobile_MyFlow() {
	}

	/**
	 * 获得工作节点
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String GenerWorkNode() throws Exception {
		WF_MyFlow en = new WF_MyFlow();
		return en.GenerWorkNode();

	}

	/**
	 * 绑定多表单中获取节点表单的数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String GetNoteValue() throws Exception {
		int fk_node = this.getFK_Node();
		if (fk_node == 0) {
			fk_node = Integer.parseInt(this.getFK_Flow() + "01");
		}
		Node nd = new Node(fk_node);

		/// #region 获取节点表单的数据
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();
		wk.ResetDefaultVal();
		if (BP.Sys.SystemConfig.getIsBSsystem() == true) {
			// 处理传递过来的参数。
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String k = (String) enu.nextElement();
				wk.SetValByKey(k, ContextHolderUtils.getRequest().getParameter(k));
			}
		}

		/// #endregion 获取节点表单的数据
		// 节点表单字段
		MapData md = new MapData(nd.getNodeFrmID());
		MapAttrs attrs = md.getMapAttrs();
		DataTable dt = new DataTable();
		dt.TableName = "Node_Note";
		dt.Columns.Add("KeyOfEn", String.class);
		dt.Columns.Add("NoteVal", String.class);
		String nodeNote = nd.GetParaString("NodeNote");

		for (MapAttr attr : attrs.ToJavaList()) {
			if (nodeNote.contains("," + attr.getKeyOfEn() + ",") == false) {
				continue;
			}
			String text = "";
			switch (attr.getLGType()) {
			case Normal: // 输出普通类型字段.
				if (attr.getMyDataType() == 1 && attr.getUIContralType().getValue() == DataType.AppString) {

					if (attrs.Contains(attr.getKeyOfEn() + "Text") == true) {
						text = wk.GetValRefTextByKey(attr.getKeyOfEn());
					}
					if (DataType.IsNullOrEmpty(text)) {
						if (attrs.Contains(attr.getKeyOfEn() + "T") == true) {
							text = wk.GetValStrByKey(attr.getKeyOfEn() + "T");
						}
					}
				} else {
					text = wk.GetValStrByKey(attr.getKeyOfEn());
					if (attr.getIsRichText() == true) {
						text = text.replace("white-space: nowrap;", "");
					}
				}

				break;
			case Enum:
			case FK:
				text = wk.GetValRefTextByKey(attr.getKeyOfEn());
				break;
			default:
				break;
			}
			DataRow dr = dt.NewRow();
			dr.setValue("KeyOfEn", attr.getKeyOfEn());
			dr.setValue("NoteVal", text);
			dt.Rows.add(dr);

		}

		return BP.Tools.Json.ToJson(dt);
	}

	/**
	 * 获得toolbar
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String InitToolBar() throws Exception {
		WF_MyFlow en = new WF_MyFlow();
		return en.InitToolBarForMobile();
	}

	public final String MyFlow_Init() throws Exception {
		WF_MyFlow en = new WF_MyFlow();
		return en.MyFlow_Init();
	}

	public final String MyFlow_StopFlow() throws Exception {
		WF_MyFlow en = new WF_MyFlow();
		return en.MyFlow_StopFlow();
	}

	public final String Save() throws Exception {
		WF_MyFlow en = new WF_MyFlow();
		return en.Save();
	}

	public final String Send() throws Exception {
		WF_MyFlow en = new WF_MyFlow();
		return en.Send();
	}

	public final String StartGuide_Init() throws Exception {
		WF_MyFlow en = new WF_MyFlow();
		return en.StartGuide_Init();
	}

	public final String FrmGener_Init() throws Exception {
		WF_CCForm ccfrm = new WF_CCForm();
		return ccfrm.FrmGener_Init();
	}

	public final String FrmGener_Save() throws Exception {
		WF_CCForm ccfrm = new WF_CCForm();
		return ccfrm.FrmGener_Save();
	}

	public final String MyFlowGener_Delete() throws Exception {
		BP.WF.Dev2Interface.Flow_DoDeleteFlowByWriteLog(this.getFK_Flow(), this.getWorkID(), WebUser.getName() + "用户删除",
				true);
		return "删除成功...";
	}

	public final String AttachmentUpload_Down() throws Exception {
		WF_CCForm ccform = new WF_CCForm();
		return ccform.AttachmentUpload_Down();
	}

}