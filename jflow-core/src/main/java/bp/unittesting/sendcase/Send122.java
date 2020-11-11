package bp.unittesting.sendcase;

import bp.wf.*;
import bp.sys.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

public class Send122 extends TestBase
{
	/** 
	 一人处理多个子线程流程
	*/
	public Send122()
	{
		this.Title = "一人处理多个子线程流程(任务维度流程)";
		this.DescIt = "流程:122，一个人处理多个子线程的case, 带有任务维度的流程。";
		this.editState = EditState.UnOK;
	}


		///变量
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
	public long workid = 0;
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
	 1, 此流程针对于122分合流程进行， zhanghaicheng发起，zhoushengyu,zhangyifan, 两个人处理子线程，
		zhanghaicheng 接受子线程汇总数据.
	 2, 测试方法体分成三大部分. 发起，子线程处理，合流点执行，分别对应: Step1(), Step2_1(), Step2_2()，Step3() 方法。
	 3，针对发送测试，不涉及到其它的功能.
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//初始化变量.
		fk_flow = "122";
		userNo = "zhanghaicheng";

		fl = new Flow(fk_flow);

		//执行第1步检查，创建工作与发送.
		this.Step1();

	}
	/** 
	 创建流程，发送分流点第1步.
	 * @throws Exception 
	*/
	public final void Step1() throws Exception
	{
		Flow fl = new Flow("122");
		fl.DoDelData();


		// 让zhanghaicheng 登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建空白工作, 发起开始节点.
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//获得他的work.
		Node nd = new Node(12201);
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.Retrieve();

		// 初始化明细表的接收人数据.
		MapDtl dtl = wk.getHisMapDtls().get(0) instanceof MapDtl ? (MapDtl)wk.getHisMapDtls().get(0) : null;
		GEDtl enDtl = dtl.getHisGEDtl();
		enDtl.setRefPK(String.valueOf(workid));
		enDtl.SetValByKey("ChuLiRen", "zhangyifan");
		enDtl.SetValByKey("ChuLiRenMingCheng", "张一帆");
		enDtl.SetValByKey("PiCiHao", "AA"); //批次号.
		enDtl.Insert();

		 enDtl = dtl.getHisGEDtl();
			enDtl.setRefPK(String.valueOf(workid));
		enDtl.SetValByKey("ChuLiRen", "zhangyifan");
		enDtl.SetValByKey("ChuLiRenMingCheng", "张一帆");
		enDtl.SetValByKey("PiCiHao", "AA"); //批次号.
		enDtl.Insert();


		enDtl = dtl.getHisGEDtl();
		enDtl.setRefPK(String.valueOf(workid));
		enDtl.SetValByKey("ChuLiRen", "zhangyifan");
		enDtl.SetValByKey("ChuLiRenMingCheng", "张一帆");
		enDtl.SetValByKey("PiCiHao", "BB"); //批次号.
		enDtl.Insert();

		// 执行向下发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(this.fk_flow, this.workid);

		if (objs.getVarTreadWorkIDs().equals(""))
		{
			throw new RuntimeException("sss");
		}


	}

}