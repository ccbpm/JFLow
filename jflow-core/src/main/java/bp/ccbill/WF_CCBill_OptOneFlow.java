package bp.ccbill;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.wf.*;
import bp.ccbill.template.*;

/** 
 页面功能实体
*/
public class WF_CCBill_OptOneFlow extends WebContralBase
{

		///#region 构造方法.
	/** 
	 构造函数
	*/
	public WF_CCBill_OptOneFlow() throws Exception {
	}

		///#endregion 构造方法.

	/** 
	 基础资料修改流程
	 
	 @return 
	*/
	public final String FlowBaseData_Init() throws Exception {
		MethodFlowBaseData method = new MethodFlowBaseData();
		return "";
	}
	/** 
	 单个实体流程记录.
	 
	 @return 
	*/
	public final String SingleDictGenerWorkFlows_Init() throws Exception {
		DataSet ds = new DataSet();

		String sql = "SELECT DISTINCT A.FK_Flow as No, A.FlowName as Name, B.Icon  FROM WF_GenerWorkFlow A, WF_Flow B  WHERE  A.FK_Flow=B.No AND A.PWorkID=" + this.getWorkID();
		DataTable dtGroup = DBAccess.RunSQLReturnTable(sql);
		dtGroup.TableName = "Flows";
		if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dtGroup.Columns.get(0).setColumnName("No");
			dtGroup.Columns.get(1).setColumnName("Name");
			dtGroup.Columns.get(2).setColumnName("Icon");
		}

		ds.Tables.add(dtGroup);

		//获得所有的子流程数据.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.getWorkID(), null);

		DataTable mydt = gwfs.ToDataTableField("GenerWorkFlows");
		mydt.Columns.Add("Icon");
		ds.Tables.add(mydt);

		return bp.tools.Json.ToJson(ds);
	}

}