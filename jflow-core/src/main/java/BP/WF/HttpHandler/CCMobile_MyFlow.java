package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 页面功能实体
*/
public class CCMobile_MyFlow extends DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public CCMobile_MyFlow()
	{
	}
	/** 
	 获得工作节点
	 
	 @return 
	*/
	public final String GenerWorkNode()
	{
		WF_MyFlow en = new WF_MyFlow();
		return en.GenerWorkNode();

	}
	/** 
	 绑定多表单中获取节点表单的数据
	 
	 @return 
	*/
	public final String GetNoteValue()
	{
		int fk_node = this.getFK_Node();
		if (fk_node == 0)
		{
			fk_node = Integer.parseInt(this.getFK_Flow() + "01");
		}
		Node nd = new Node(fk_node);

			///#region  获取节点表单的数据
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();
		wk.ResetDefaultVal();
		if (BP.Sys.SystemConfig.getIsBSsystem() == true)
		{
			// 处理传递过来的参数。
			for (String k : HttpContextHelper.RequestQueryStringKeys)
			{
				if (DataType.IsNullOrEmpty(k) == true)
				{
					continue;
				}

				wk.SetValByKey(k, HttpContextHelper.RequestParams(k));
			}

			// 处理传递过来的frm参数。
			for (String k : HttpContextHelper.RequestParamKeys)
			{
				if (DataType.IsNullOrEmpty(k) == true)
				{
					continue;
				}

				wk.SetValByKey(k, HttpContextHelper.RequestParams(k));
			}
		}

			///#endregion 获取节点表单的数据
		//节点表单字段
		MapData md = new MapData(nd.getNodeFrmID());
		MapAttrs attrs = md.getMapAttrs();
		DataTable dt = new DataTable();
		dt.TableName = "Node_Note";
		dt.Columns.Add("KeyOfEn", String.class);
		dt.Columns.Add("NoteVal", String.class);
		String nodeNote = nd.GetParaString("NodeNote");

		for (MapAttr attr : attrs.ToJavaList())
		{
			if (nodeNote.contains("," + attr.getKeyOfEn() + ",") == false)
			{
				continue;
			}
			String text = "";
			switch (attr.getLGType())
			{
				case Normal: // 输出普通类型字段.
					if (attr.getMyDataType() == 1 && attr.getUIContralType().getValue() == DataType.AppString)
					{

						if (attrs.Contains(attr.getKeyOfEn() + "Text") == true)
						{
							text = wk.GetValRefTextByKey(attr.getKeyOfEn());
						}
						if (DataType.IsNullOrEmpty(text))
						{
							if (attrs.Contains(attr.getKeyOfEn() + "T") == true)
							{
								text = wk.GetValStrByKey(attr.getKeyOfEn() + "T");
							}
						}
					}
					else
					{
						text = wk.GetValStrByKey(attr.getKeyOfEn());
						if (attr.getIsRichText() == true)
						{
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
			dr.set("KeyOfEn", attr.getKeyOfEn());
			dr.set("NoteVal", text);
			dt.Rows.add(dr);

		}

		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 获得toolbar
	 
	 @return 
	*/
	public final String InitToolBar()
	{
		WF_MyFlow en = new WF_MyFlow();
		return en.InitToolBarForMobile();
	}
	public final String MyFlow_Init()
	{
		WF_MyFlow en = new WF_MyFlow();
		return en.MyFlow_Init();
	}
	public final String MyFlow_StopFlow()
	{
		WF_MyFlow en = new WF_MyFlow();
		return en.MyFlow_StopFlow();
	}
	public final String Save()
	{
		WF_MyFlow en = new WF_MyFlow();
		return en.Save();
	}
	public final String Send()
	{
		WF_MyFlow en = new WF_MyFlow();
		return en.Send();
	}
	public final String StartGuide_Init()
	{
		WF_MyFlow en = new WF_MyFlow();
		return en.StartGuide_Init();
	}
	public final String FrmGener_Init()
	{
		WF_CCForm ccfrm = new WF_CCForm();
		return ccfrm.FrmGener_Init();
	}
	public final String FrmGener_Save()
	{
		WF_CCForm ccfrm = new WF_CCForm();
		return ccfrm.FrmGener_Save();
	}

	public final String MyFlowGener_Delete()
	{
		BP.WF.Dev2Interface.Flow_DoDeleteFlowByWriteLog(this.getFK_Flow(), this.getWorkID(), WebUser.getName() + "用户删除", true);
		return "删除成功...";
	}

	public final String AttachmentUpload_Down()
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.AttachmentUpload_Down();
	}

}