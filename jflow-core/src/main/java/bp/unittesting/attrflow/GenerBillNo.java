package bp.unittesting.attrflow;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.time.*;
import java.util.Date;

/** 
 产生单据编号
*/
public class GenerBillNo extends TestBase
{
	/** 
	 产生单据编号
	*/
	public GenerBillNo()
	{
		this.Title = "产生单据编号";
		this.DescIt = "流程: 以demo 流程023 为例测试产生单据编号。";
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
	 1， 不同的格式，生成不同的编号。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		this.fk_flow = "023";
		fl = new Flow("023");
		String sUser = "zhoupeng";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//删除数据.
		fl.DoDelData();

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//定义单据格式.
		String billFormat = "CN{yyyy}-{MM}-{dd}IDX-{LSH4}";
		fl.setBillNoFormat(billFormat);
		fl.Update();

		//执行发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);

		// 应该是。
		billFormat = billFormat.replace("{yyyy}", DataType.getCurrentDateByFormart("yyyy"));
		billFormat = billFormat.replace("{MM}", DataType.getCurrentDateByFormart("MM"));
		billFormat = billFormat.replace("{dd}", String.format("%1$d", new Date()));
		billFormat = billFormat.replace("{LSH4}", "0001");

		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		if (!gwf.getBillNo().equals(billFormat))
		{
			throw new RuntimeException("@应当是:" + billFormat + "现在是:" + gwf.getBillNo());
		}

		// 生成子流程。
		Flow flSub = new Flow("024");
		flSub.DoCheck();
		flSub.CheckRpt();

		// 产生子流程编号。
		for (int i = 1; i < 5; i++)
		{
			flSub.setBillNoFormat("{ParentBillNo}-{LSH4}");
			long subWorkID = bp.wf.Dev2Interface.Node_CreateBlankWork(flSub.getNo());
			objs = bp.wf.Dev2Interface.Node_SendWork(flSub.getNo(), subWorkID);

			//设置流程信息。
			bp.wf.Dev2Interface.SetParentInfo(flSub.getNo(), subWorkID, workID);
			if (i == 2)
			{
				continue;
			}

			String subFlowBillNo = DBAccess.RunSQLReturnStringIsNull("SELECT BillNo FROM " + flSub.getPTable() + " WHERE OID=" + subWorkID, "");
			if (!subFlowBillNo.equals(billFormat + "-000" + i))
			{
				throw new RuntimeException("@应当是:" + billFormat + "-000" + i + " , 现在是:" + subFlowBillNo);
			}
		}
	}
}