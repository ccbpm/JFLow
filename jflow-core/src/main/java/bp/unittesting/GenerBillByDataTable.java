package bp.unittesting;

import bp.wf.*;
import bp.en.*;
import bp.pub.RTFEngine;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import java.util.*;
import java.math.*;

/** 
 根据datatable 生成单据
*/
public class GenerBillByDataTable extends TestBase
{
	/** 
	 根据datatable 生成单据
	*/
	public GenerBillByDataTable()
	{
		this.Title = "根据 datatable 生成单据";
		this.DescIt = "流程: 财务报销流程.";
		this.editState = EditState.Passed;
	}


		///全局变量
	/** 
	 流程编号
	*/
	public String fk_flow = "";
	/** 
	 用户编号
	*/
	public String userNo = "";
	/** 
	 所有的流程
	*/
	public Flow fl = null;
	/** 
	 主线程ID
	*/
	public long workID = 0;
	/** 
	 发送后返回对象
	*/
	public SendReturnObjs objs = null;
	/** 
	 工作人员列表
	*/
	public GenerWorkerList gwl = null;
	/** 
	 流程注册表
	*/
	public GenerWorkFlow gwf = null;

		/// 变量

	/** 
	 测试案例说明:
	 1, .
	 2, .
	 3，.
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{

			///初始化数据。
		//初始化变量.
		fk_flow = "001";
		userNo = "zhangyifan";
		fl = new Flow(fk_flow);
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建空白的流程.
		this.workID=bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, null, null);

		//创建主表数据. （费用主表）。
		Hashtable ht = new Hashtable();
		ht.put("ZhaiYaoShuoMing", "摘要说明:ZhaiYaoShuoMing.");
		ht.put("RPI", 1);

		// 创建从表数据。（费用明细）
		DataSet ds = new DataSet();

		DataTable dt = new DataTable();
		dt.TableName = "ND101Dtl1";
		dt.Columns.Add("RefPK", Integer.class); //关联的主键，这里是workid.
		dt.Columns.Add("FYLX", Integer.class); // 费用类型.
		dt.Columns.Add("JinE", BigDecimal.class); // 金额
		dt.Columns.Add("ShuLiang", BigDecimal.class); //数量.
		dt.Columns.Add("XiaoJi", BigDecimal.class); // 小计。

		DataRow dr = dt.NewRow();
		dr.setValue("RefPK", this.workID);
		dr.setValue("FYLX", 1);
		dr.setValue("JinE", 150);
		dr.setValue("ShuLiang", 2);
		dr.setValue("XiaoJi", 300);
		dt.Rows.add(dr);

		dr = dt.NewRow();
		dr.setValue("RefPK", this.workID);
		dr.setValue("FYLX", 2);
		dr.setValue("JinE", 250);
		dr.setValue("ShuLiang", 3);
		dr.setValue("XiaoJi", 750);
		dt.Rows.add(dr);
		ds.Tables.add(dt);

		//执行发送.
		bp.wf.Dev2Interface.Node_SendWork(fk_flow, this.workID, ht, ds, 0, null);

			/// 初始化数据。


			///生成测试数据。
		//查询到保存数据库的数据源。
		String sql = "SELECT * FROM ND101 WHERE OID=" + this.workID;
		DataTable dtMain = DBAccess.RunSQLReturnTable(sql);

		sql = "SELECT * FROM ND101Dtl1 WHERE RefPK=" + this.workID;
		DataTable dtDtl = DBAccess.RunSQLReturnTable(sql);
		dtDtl.TableName = "ND101Dtl1";

		DataSet myds = new DataSet();
		myds.Tables.add(dtDtl);

			/// 生成测试数据。

		String templeteFilePath = "D:/ccflow/trunk/CCFlow/DataUser/CyclostyleFile/单据打印样本.rtf";
		RTFEngine rpt = new RTFEngine();

		rpt.MakeDocByDataSet(templeteFilePath, "C:/", this.workID + ".doc", dtMain, myds);
	}
}