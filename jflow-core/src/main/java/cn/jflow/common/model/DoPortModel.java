package cn.jflow.common.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.XmlWriteMode;
import BP.Sys.MapData;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.FlowAppType;
import BP.WF.Node;
import BP.WF.NodeFormType;

public class DoPortModel extends BaseModel {

	public StringBuilder Pub1 = null;

	private HttpServletRequest resquest;

	private HttpServletResponse response;

	private String EnName;

	private String PK;

	private String DirType;

	private String ToNodeID;

	private String FK_Node;

	private String CondType;

	private String FK_Flow;

	private String RefNo;

	private String FK_MapData = this.get_request().getParameter("FK_MapData");

	private String FK_Attr;

	private String FK_MainNode;

	private String PassKey;

	private String Lang;

	private String doType;

	private String basePath;

	public DoPortModel(HttpServletRequest request,
			HttpServletResponse response, String EnName, String PK,
			String Lang, String DoType, String basePath) {
		super(request, response);
		this.response = response;
		this.resquest = resquest;
		this.EnName = EnName;
		this.Lang = Lang;
		this.PK = PK;
		this.doType = DoType;
		this.basePath = basePath;
		Pub1 = new StringBuilder();
	}

	public void init() throws Exception {
		// if (request.Browser.Cookies == false)
		// {
		// //this.Response.Write("您的浏览器不支持cookies功能，无法使用该系统。");
		// //return;
		// }

		if (this.PassKey != SystemConfig.getAppSettings().get("PassKey"))
			return;

		// BP.Sys.SystemConfig.DoClearCash();
//		BP.Port.Emp emp = new BP.Port.Emp("admin");
//		WebUser.SignInOfGenerLang(emp, "CH");

		String fk_flow = FK_Flow;
		String fk_Node = FK_Node;

		// String FK_MapData = FK_MapData
		if (StringHelper.isNullOrEmpty(FK_MapData)) {
			FK_MapData = this.get_request().getParameter("PK");
		}
		if (doType.equals("DownFormTemplete")) {
			DataSet ds = MapData.GenerHisDataSet(FK_MapData);

			MapData md = new MapData(FK_MapData);
			//DataSet ds = md.GenerHisDataSet();
			String file = SystemConfig.getPathOfTemp() + "/" + md.getNo()
					+ ".xml";
			ds.WriteXml(file, XmlWriteMode.IgnoreSchema, ds);
			BP.Sys.PubClass.DownloadFile(file, md.getName() + ".xml");
			
			return;
		} else if (doType.equals("Ens")) {
			response.sendRedirect("../../Comm/Batch.jsp?EnsName=" + EnName);
		} else if (doType.equals("En")) {
			//if (EnName.equals("BP.WF.Template")) {
			if (EnName.equals("BP.WF.Flow")) {
				Flow fl = new Flow(this.PK);
				if (fl.getFlowAppType() == FlowAppType.DocFlow)
					response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowDocs&No="
							+ this.PK);
				else
					response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowSheets&No="
							+ this.PK);
				return;
				
			} 
			
			else if ("BP.WF.Template.FlowSheet".equals(EnName)|| "BP.WF.Template.Ext.FlowSheet".equals(EnName)) {
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowSheets&No="
						+ this.PK);
				return;
			} 
			
			else if("BP.WF.Template.Ext.NodeExts".equals(EnName)){
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.NodeExts&No=" 
						+ this.PK);
				return;
			}
		
			else if("BP.WF.Rpt.MapRptExts".equals(EnName)){
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Rpt.MapRptExts&PK=" 
						+ this.PK);
				return;
			}
			
			else if (EnName.equals("BP.WF.Node")) {
				Node nd = new Node(this.PK);
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.NodeSheets&PK="
						+ this.PK);
				return;
			} 
			
			
			else if (EnName.equals("BP.WF.Template.FlowExt")) {
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowExts&PK="
						+ this.PK);
				return;
			}
			
			else if(EnName.equals("BP.WF.Template.NodeExt")){
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.NodeExts&PK=" + this.PK);
                  return;
			} 
			
			else if (EnName.equals("BP.WF.FlowSort")) {
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowSorts&PK="
						+ this.PK);
				return;
			}
			
			else {
				throw new Exception("err");
			}
			
		//"表单库"
		} else if (doType.equals("FrmLib")) { 
			response.sendRedirect("../BindFrms.jsp?ShowType=FrmLab&FK_Flow="
					+ this.get_request().getParameter("FK_Flow") + "&FK_Node="
					+ this.get_request().getParameter("FK_Node") + "&Lang="
					+ BP.Web.WebUser.getSysLang());
		} 
		//"独立表单"
		else if (doType.equals("FlowFrms")) {
			response.sendRedirect("../BindFrms.jsp?ShowType=FlowFrms&FK_Flow="
					+ this.get_request().getParameter("FK_Flow") + "&FK_Node="
					+ this.get_request().getParameter("FK_Node") + "&Lang="
					+ BP.Web.WebUser.getSysLang());
		}
		// 节点岗位.
		else if (doType.equals("StaDef")) {
			response.sendRedirect("../../Comm/RefFunc/Dot2Dot.jsp?EnName=BP.WF.Template.NodeSheet&AttrKey=BP.WF.Template.NodeStations&PK="
					+ this.PK
					+ "&NodeID="
					+ this.PK
					+ "&RunModel=0&FLRole=0&FJOpen=0&r=" + this.PK);
		} 
		// 新版本.. http://localhost:13432/WF/Comm/RefFunc/Dot2DotSingle.aspx
		else if (doType.equals("StaDefNew")) {
			response.sendRedirect("../../Comm/RefFunc/Dot2DotSingle.jsp?EnsName=BP.WF.Template.NodeSheets&EnName=BP.WF.Template.NodeSheet&AttrKey=BP.WF.Template.NodeStations&NodeID="
					+ this.PK
					+ "&r=0319061642&1=FK_StationType&ShowWay=None" + this.PK);
		} 
		// 报表设计.r
		else if (doType.equals("WFRpt")) {
			response.sendRedirect("../../Rpt/OneFlow.jsp?FK_MapData=ND"
					+ Integer.parseInt(this.PK) + "Rpt&FK_Flow=" + this.PK);
		} 
		//表单定义
		else if (doType.equals("MapDef")) {
			int nodeid = Integer.parseInt(this.PK.replace("ND", ""));
			Node nd1 = new Node();
			nd1.setNodeID(nodeid);
			nd1.RetrieveFromDBSources();
			if (nd1.getHisFormType() == NodeFormType.FreeForm)
				response.sendRedirect("../../MapDef/CCForm/Frm.jsp?FK_MapData="
						+ this.PK + "&FK_Flow=" + nd1.getFK_Flow()
						+ "&FK_Node=" + this.FK_Node);
			else
				response.sendRedirect("../../MapDef/MapDef.jsp?PK=" + this.PK
						+ "&FK_Flow=" + nd1.getFK_Flow() + "&FK_Node="
						+ this.FK_Node);
		}
		// 表单定义
		else if (doType.equals("MapDefFixModel") || doType.equals("FromFixModel")) {
			response.sendRedirect("../../MapDef/MapDef.jsp?FK_MapData="
					+ FK_MapData + "&FK_Flow=" + this.FK_Flow + "&FK_Node="
					+ this.FK_Node);
		} 
		// 表单定义.
		else if (doType.equals("MapDefFreeModel") || doType.equals("FormFreeModel")) {
			response.sendRedirect("../../MapDef/CCForm/Frm.jsp?FK_MapData="
					+ FK_MapData + "&FK_Flow=" + this.FK_Flow + "&FK_Node="
					+ this.FK_Node);
		} 
		//表单定义.
		else if (doType.equals("Map.DefFree")) {
			int nodeidFree = Integer.parseInt(this.PK.replace("ND", ""));
			Node ndFree = new Node(nodeidFree);
			response.sendRedirect("../MapDef/CCForm/Frm.jsp?FK_MapData="
					+ this.PK + "&FK_Flow=" + ndFree.getFK_Flow() + "&FK_Node="
					+ ndFree.getNodeID());

		} 
		//表单定义
		else if (doType.equals("MapDefF4")) {
			int nodeidF4 = Integer.parseInt(this.PK.replace("ND", ""));
			Node ndF4 = new Node(nodeidF4);
			response.sendRedirect("../../MapDef/MapDef.jsp?PK=" + this.PK
					+ "&FK_Flow=" + ndF4.getFK_Flow() + "&FK_Node=" + nodeidF4);

		}
		 // 方向。
		else if (doType.equals("Dir")) {
			this.response.sendRedirect("../Admin/Cond.jsp?CondType=" + CondType
					+ "&FK_Flow=" + FK_Flow + "&FK_MainNode=" + FK_MainNode
					+ "&FK_Node=" + FK_Node + "&FK_Attr=" + FK_Attr
					+ "&DirType=" + DirType + "&ToNodeID=" + ToNodeID);
		}
		 //运行流程
		else if (doType.equals("RunFlow")) {
			response.sendRedirect("../Admin/StartFlow.jsp?FK_Flow=" + fk_flow
					+ "&Lang=" + BP.Web.WebUser.getSysLang());
		}
		
		else if (doType.equals("FlowCheck")) {
			response.sendRedirect("../Admin/DoType.jsp?RefNo=" + RefNo
					+ "&DoType=" + doType);
		} else if (doType.equals("ExpFlowTemplete")) {
			Flow flow = new Flow(this.getFK_Flow());
			String fileXml = flow.GenerFlowXmlTemplete();

			BP.Sys.PubClass.DownloadFile(fileXml, flow.getName() + ".xml");
			BP.Sys.PubClass.WinClose();
			// this.winCloseWithMsg("导出完成");

		} else if (doType.equals("UploadShare")) {
			upload();

		} else if (doType.equals("ShareToFtp")) {

		} else {
			throw new Exception("Error:" + doType);
		}

	}
	
	/// <summary>
    /// 备份当前流程到用户xml文件
    /// 用户每次保存时调用
    /// </summary>
    public static void WriteToXmlMapData(String FK_MapData)//, boolean savePrtSC)
    {
        try
        {
        	String path = "", xmlName = "", ext = ".xml";
            if (!StringUtils.isNotBlank(FK_MapData))
            {
                if (FK_MapData.startsWith("ND"))
                {// 节点表单
                    int nodeNo = Integer.parseInt(FK_MapData.substring(2, FK_MapData.length() - 2));

                    String nodeName = "", FlowNo = "", FlowName = "";
                    String sql = "SELECT n.Name NodeName,FK_Flow FlowNo,f.Name FlowName FROM WF_Node n,WF_Flow f where NodeID ='"+nodeNo+"' and n.FK_Flow = f.No";
                    DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
                    if (dt != null && dt.Rows.size() == 1)
                    {
                        nodeName = dt.Rows.get(0).getValue("NodeName").toString();
                        FlowNo = dt.Rows.get(0).getValue("FlowNo").toString();
                        FlowName = dt.Rows.get(0).getValue("FlowName").toString();
                    }

                    nodeName = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(nodeName);
                    FlowName = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(FlowName);

                    path = BP.Sys.SystemConfig.getPathOfDataUser() + "FlowDesc/" + FlowNo + "." + FlowName + "/";
                    xmlName = nodeNo + "." + nodeName;
                }
                else
                { // 独立表单
                    String sql = "SELECT Name from sys_MapData WHERE No ='"+FK_MapData+"'";
                    xmlName = BP.DA.DBAccess.RunSQLReturnString(sql);
                    xmlName = BP.Tools.StringExpressionCalculate.ReplaceBadCharOfFileName(xmlName);
                    path = BP.Sys.SystemConfig.getPathOfDataUser() + "FlowDesc/FlowForm/" + xmlName + "/";// 独立表单
                }
            }

            if (!StringHelper.isNullOrEmpty(path))
            {
    				String file = path + xmlName + ext;

    				java.io.File f = (new java.io.File(file));
    				if (!f.isDirectory())
    				{
    					f.mkdirs();
    				}
    				else if (f.isFile())
    				{
    					
    					String time = new SimpleDateFormat("@yyyyMMddHHmmss").format(new Date(f.lastModified()));
    					String xmlNameOld = path + xmlName + time + ext;

    					//把文件重命名.
    					if (f.isFile())
    					{
    						f.delete();
    					}
    					f.renameTo(new File(xmlNameOld));

    				}

//    				if (savePrtSC)
//    				{
//    					file = path + xmlName + ".png";
//    					uploadPng(file);
//    				}
//    				else
//    				{
    					DataSet ds = MapData.GenerHisDataSet(FK_MapData);
    					if (!StringHelper.isNullOrEmpty(file) && null != ds)
    					{
    						ds.WriteXml(file);
    					}
//    				}
            }
        }
        catch (Exception e)
        {
            BP.DA.Log.DefaultLogWriteLineError("表单文件备份错误:"+e.getMessage(), e);
        }
    }

	public void upload() {
		String path = "", pngName = "";

		String FK_Flow =this.get_request().getParameter("FK_Flow");
		String FK_MapData = this.get_request().getParameter("FK_MapData");
		if (!StringHelper.isNullOrEmpty(FK_Flow))
		{ // 共享流程
			Flow flow = new Flow(FK_Flow);

			path = BP.Sys.SystemConfig.getPathOfDataUser() + "FlowDesc/" + flow.getNo() + "." + flow.getName() + "/";
			pngName = path + "Flow.png";
			// 判断文件夹是否存在，不存在则创建
			File dirPath = new File(path);
			if (!dirPath.exists()) {
				dirPath.mkdir();
			}

			/*if (!StringHelper.isNullOrEmpty(pngName)) {
				uploadPng(pngName);
			}*/
		}
		else if (!StringHelper.isNullOrEmpty(FK_MapData))
		{ // 共享表单
			WriteToXmlMapData(FK_MapData);
		}
	}

}
