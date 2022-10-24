package bp.wf.dts;

import bp.da.*;
import bp.web.WebUser;
import bp.en.*;
import bp.wf.*;

/**
 升级ccflow6 要执行的调度
 */
public class UpTrack extends Method
{
	/**
	 不带有参数的方法
	 */
	public UpTrack()throws Exception
	{
		this.Title = "升级ccflow6要执行的调度(仅仅处理了wf_track部分,不可反复执行.)";
		this.Help = "执行此过程把ccflow4 升级到ccflow6 此过程仅解决wf_track表的问题.";
	}
	/**
	 设置执行变量

	 @return
	 */
	@Override
	public void Init() {
	}
	/**
	 当前的操纵员是否可以执行这个方法
	 * @throws Exception
	 */
	@Override
	public boolean getIsCanDo()
	{
		if (WebUser.getNo().equals("admin") == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 执行

	 @return 返回执行结果
	  * @throws Exception
	 */
	@Override
	public Object Do() throws Exception
	{
		Flows fls = new Flows();
		fls.RetrieveAllFromDBSource();

		String info = "";
		for (Flow fl : fls.ToJavaList())
		{
			// 检查报表.
			Track.CreateOrRepairTrackTable(fl.getNo());

			// 查询.
			String sql = "SELECT * FROM WF_Track WHERE FK_Flow='" + fl.getNo() + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (int i = 0; i < dt.Rows.size(); i++)
			{
				Track tk = new Track();
				tk.FK_Flow = fl.getNo();
				tk.getRow().LoadDataTable(dt, dt.Rows.get(0));
				tk.DoInsert(0); // 执行insert.
			}
		}
		return "执行完成，您不能在执行它了，否则就会出现重复的轨迹数据。";
	}
}