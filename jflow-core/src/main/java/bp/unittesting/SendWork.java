package bp.unittesting;

import bp.wf.*;
import bp.en.*;
import bp.tools.DateUtils;
import bp.da.*;
import bp.web.*;
import java.time.*;
import java.util.Date;

/** 
 测试名称
*/
public class SendWork extends TestBase
{
	/** 
	 测试名称
	*/
	public SendWork()
	{
		this.Title = "效率测试：SendWork";
		this.DescIt = "流程: 005月销售总结(同表单分合流),执行发送后的数据是否符合预期要求.";
		this.editState = EditState.UnOK;
	}
	/** 
	 测试案例说明:
	 1, . 这是一个标准的发送效率测试.
	 2, . 执行了createworkid 之后进行发送.
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		this.AddNote("开始执行发送.....");

		String userNo = "admin";
		bp.port.Emp emp = new bp.port.Emp(userNo);
		WebUser.SignInOfGener(emp);

		bp.wf.unittesting.TestAPI api = new bp.wf.unittesting.TestAPI();
		api.setNo("SendWork");
		api.setName("标准的工作发送");
		api.Save();

		bp.wf.unittesting.TestVer apiVer = new bp.wf.unittesting.TestVer();
		apiVer.setNo("SendWork" + DataType.getCurrentDataTime());
		apiVer.setName("版本" + apiVer.getNo());

		try
		{
			//定义了10个样本. 对该过程执行10次。
			for (int idx = 0; idx < 5; idx++)
			{
				Date startTime = new Date();
				for (int i = 0; i <= 1000; i++)
				{
					long workid = bp.wf.Dev2Interface.Node_CreateBlankWork("230");
					bp.wf.SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork("230", workid, 0, "admin");
				}
				//doSomeThing();   //要运行的java程序
				Date endTime = new Date();
				long ts = endTime.getTime() - startTime.getTime();
				

				bp.wf.unittesting.TestSample dtl = new bp.wf.unittesting.TestSample();
				dtl.setMyPK(DBAccess.GenerGUID());
				dtl.setFK_API(api.getNo());
				dtl.setFK_Ver(apiVer.getNo());
				dtl.setDTFrom(DateUtils.format(startTime, "yyyy-MM-dd HH:mm:ss"));
				dtl.setDTTo(DateUtils.format(endTime, "yyyy-MM-dd HH:mm:ss"));
				dtl.setName(api.getName() + "-" + apiVer.getName());

				//dtl.setTimeUse(ts.TotalMilliseconds);
				//dtl.setTimesPerSecond(ts.TotalMilliseconds / 1000);
				dtl.Insert();
			}

			apiVer.Insert(); //执行成功后，版本号在插入里面.
		}
		catch (RuntimeException ex)
		{
			bp.wf.unittesting.TestSample dtl = new bp.wf.unittesting.TestSample();
			dtl.Delete(bp.wf.unittesting.TestSampleAttr.FK_Ver, apiVer.getNo());
			throw ex;
		}

		this.AddNote("查看数据： <a href=''></a>");
	}
}