package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.Glo;
import bp.wf.template.*;

/** 
 页面功能实体
*/
public class WF_Admin_AttrNode_FrmSln extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_AttrNode_FrmSln()
	{

	}
	/** 
	 设置该流程的所有节点都是用该方案。
	 
	 @return 
	 * @throws Exception 
	*/
	public final void RefOneFrmTree_SetAllNodeFrmUseThisSln() throws Exception
	{
		String nodeID = GetRequestVal("FK_Node");
		Node currNode = new Node(nodeID);
		String flowNo = currNode.getFK_Flow();
		Nodes nds = new Nodes();
		nds.Retrieve("FK_Flow", flowNo);

		//求出来组件s.
		MapAttrs attrOfCommpents = new MapAttrs();
		QueryObject qo = new QueryObject(attrOfCommpents);
		qo.AddWhere(MapAttrAttr.FK_MapData, currNode.getNodeFrmID());
		qo.addAnd();
		qo.AddWhere(MapAttrAttr.UIContralType, ">=", 6);
		qo.DoQuery();

	 //   attrOfCommpents.Retrieve(MapAttrAttr.FK_MapData,
	   // currNode.)

		for (int i = 0; i < nds.size(); i++)
		{

			Node jsNode = nds.get(i) instanceof Node ? (Node)nds.get(i) : null;
			if (jsNode.getNodeID() == currNode.getNodeID())
			{
				continue;
			}

			//修改表单属性
			jsNode.setFormType(currNode.getFormType());

			jsNode.setNodeFrmID(currNode.getNodeFrmID());
			jsNode.Update();

			//节点表单属性
			//先删除掉已有的，避免换绑时出现垃圾数据.
			FrmNodes ens = new FrmNodes();
			ens.Retrieve("FK_Node", jsNode.getNodeID());

			//是不是该frmNode已经存在？
			boolean isHave = false;
			for (int idx = 0; idx < ens.size(); idx++)
			{
				FrmNode en = ens.get(idx) instanceof FrmNode ? (FrmNode)ens.get(idx) : null;
				if (!en.getFK_Frm().equals(currNode.getNodeFrmID()))
				{
					FrmNode Frm = new FrmNode(en.getMyPK());
					Frm.Delete();
					continue;
				}
				isHave = true;
			}
			if (isHave == true)
			{
				continue; //已经存在就不处理.
			}

			FrmNode frmNode = new FrmNode();
			frmNode.setMyPK(jsNode.getNodeFrmID() + "_" + jsNode.getNodeID() + "_" + jsNode.getFK_Flow());
			frmNode.setFK_Node(jsNode.getNodeID());
			frmNode.setFK_Flow(jsNode.getFK_Flow());
			frmNode.setFK_Frm(jsNode.getNodeFrmID());

			//判断是否为开始节点
			String nodeID1 = String.valueOf(jsNode.getNodeID());
			if (nodeID1.substring(nodeID1.length() - 2).equals("01"))
			{
				frmNode.setFrmSln(FrmSln.Default); //默认方案
			}
			else
			{
				frmNode.setFrmSln(FrmSln.Readonly); //只读方案
			}
			frmNode.Insert();

			//设置组件都是可用的.
			bp.wf.template.FrmField ff = new FrmField();
			for (MapAttr attr : attrOfCommpents.ToJavaList())
			{
				ff.setUIVisible(true);
				ff.setKeyOfEn(attr.getKeyOfEn());
				ff.setFK_Flow(currNode.getFK_Flow());
				ff.setFK_Node(jsNode.getNodeID());
				ff.setFK_MapData(jsNode.getNodeFrmID()); //表单ID.
				ff.setMyPK(ff.getFK_MapData() + "_" + ff.getFK_Node() + "_" + ff.getKeyOfEn());
				if (ff.getIsExits() == false)
				{
					ff.Insert();
				}
			}
		}
	}
	/** 
	 获得下拉框的值.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String BatchEditSln_InitDDLData() throws Exception
	{
		String fk_frm = GetRequestVal("Fk_Frm");
		DataSet ds = new DataSet();

		SysEnums ses = new SysEnums("FrmSln");
		ds.Tables.add(ses.ToDataTableField("FrmSln"));

		SysEnums se1s = new SysEnums("FWCSta");
		ds.Tables.add(se1s.ToDataTableField("FWCSta"));

		DataTable dt = DBAccess.RunSQLReturnTable(Glo.getSQLOfCheckField().replace("@FK_Frm", fk_frm));
		dt.TableName = "CheckFields";
		ds.Tables.add(dt);
		return bp.tools.Json.ToJson(ds);
	}

	/** 
	 返回
	 
	 @return 
	 * @throws Exception 
	*/
	public final String RefOneFrmTreeFrms_Init() throws Exception
	{
		String sql = "";
		//单机模式下
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql += "SELECT  b.NAME AS SortName, a.No, A.Name,";
			sql += "A.PTable,";
			sql += "A.OrgNo ";
			sql += "FROM ";
			sql += "Sys_MapData A, ";
			sql += "Sys_FormTree B ";
			sql += " WHERE ";
			sql += " A.FK_FormTree = B.NO ";
			//sql += " AND B.setOrgNo('" + WebUser.getOrgNo() + "'";
			sql += "ORDER BY B.IDX,A.IDX";

		}

		// 云服务器环境下
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			sql += "SELECT  b.NAME AS SortName, a.No, A.Name, ";
			sql += "A.PTable, ";
			sql += "A.OrgNo ";
			sql += "FROM ";
			sql += "Sys_MapData A, ";
			sql += "Sys_FormTree B ";
			sql += " WHERE ";
			sql += " A.FK_FormTree = B.NO ";
			sql += " AND B.setOrgNo('" + WebUser.getOrgNo() + "' ";
			sql += "ORDER BY B.IDX,A.IDX";
		}

		//集团模式下
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			sql += " SELECT  b.NAME AS SortName, a.No, A.Name,";
			sql += "A.PTable,";
			sql += "A.OrgNo, '" + WebUser.getOrgName() + "' as OrgName ";
			sql += "FROM ";
			sql += "Sys_MapData A, ";
			sql += "Sys_FormTree B, ";
			sql += "Port_Org C ";
			sql += " WHERE ";
			sql += " A.FK_FormTree = B.NO ";
			sql += " AND B.setOrgNo('" + WebUser.getOrgNo() + "' ";
			sql += " AND C.No =B.OrgNo ";

			sql += " UNION  ";

			sql += " SELECT  '- 共享 -' AS SortName, A.No, A.Name, ";
			sql += " A.PTable, A.OrgNo, '其他组织' as OrgName ";
			sql += " FROM ";
			sql += " Sys_MapData A,  WF_FrmOrg B, Port_Org C ";
			sql += " WHERE ";
			sql += "  A.No=B.FrmID  AND B.OrgNo=C.No ";
			sql += "  AND B.setOrgNo('" + WebUser.getOrgNo() + "' ";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);


			///#warning 需要判断不同的数据库类型
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.DM || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("SORTNAME").setColumnName("SortName");
			dt.Columns.get("NO").setColumnName("No");
			dt.Columns.get("NAME").setColumnName("Name");
			dt.Columns.get("PTABLE").setColumnName("PTable");
			dt.Columns.get("ORGNO").setColumnName("OrgNo");
		}

		return bp.tools.Json.ToJson(dt);
	}

}