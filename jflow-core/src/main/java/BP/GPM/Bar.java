package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.WebUser;

import java.util.*;

/** 
 信息块
*/
public class Bar extends EntityNoName
{
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 更多的URL
	 * @throws Exception 
	*/
	public final String getMoreUrl() throws Exception
	{
		return this.GetValStrByKey(BarAttr.MoreUrl);
	}
	public final void setMoreUrl(String value) throws Exception
	{
		this.SetValByKey(BarAttr.MoreUrl, value);
	}
	/** 
	 更多标签
	 * @throws Exception 
	*/
	public final String getMoreLab() throws Exception
	{
		return this.GetValStrByKey(BarAttr.MoreLab);
	}
	public final void setMoreLab(String value) throws Exception
	{
		this.SetValByKey(BarAttr.MoreLab, value);
	}
	/** 
	 标题
	 * @throws Exception 
	*/
	public final String getTitle() throws Exception
	{
		return this.GetValStrByKey(BarAttr.Title);
	}
	public final void setTitle(String value) throws Exception
	{
		this.SetValByKey(BarAttr.Title, value);
	}
	/** 
	 用户是否可以删除
	 * @throws Exception 
	*/
	public final boolean getIsDel() throws Exception
	{
		return this.GetValBooleanByKey(BarAttr.IsDel);
	}
	public final void setIsDel(boolean value) throws Exception
	{
		this.SetValByKey(BarAttr.IsDel, value);
	}
	/** 
	 类型
	 * @throws Exception 
	*/
	public final int getBarType() throws Exception
	{
		return this.GetValIntByKey(BarAttr.BarType);
	}
	public final void setBarType(int value) throws Exception
	{
		this.SetValByKey(BarAttr.BarType, value);
	}
	/** 
	 打开方式
	 * @throws Exception 
	*/
	public final int getOpenWay() throws Exception
	{
		return this.GetValIntByKey(BarAttr.OpenWay);
	}
	public final void setOpenWay(int value) throws Exception
	{
		this.SetValByKey(BarAttr.OpenWay, value);
	}
	/** 
	 顺序号
	 * @throws Exception 
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(BarAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(BarAttr.Idx, value);
	}
	/** 
	 Tag1
	*/
	public final String getTag1() throws Exception
	{
		return this.GetValStrByKey(BarAttr.Tag1);
	}
	public final void setTag1(String value) throws Exception
	{
		this.SetValByKey(BarAttr.Tag1, value);
	}
	/** 
	 Tag
	*/
	public final String getTag2() throws Exception
	{
		return this.GetValStrByKey(BarAttr.Tag2);
	}
	public final void setTag2(String value) throws Exception
	{
		this.SetValByKey(BarAttr.Tag2, value);
	}
	public final String getTag3() throws Exception
	{
		return this.GetValStrByKey(BarAttr.Tag3);
	}
	public final void setTag3(String value) throws Exception
	{
		this.SetValByKey(BarAttr.Tag3, value);
	}
	public final String getDocGenerRDT() throws Exception
	{
		return this.GetValStrByKey(BarAttr.DocGenerRDT);
	}
	public final void setDocGenerRDT(String value) throws Exception
	{
		this.SetValByKey(BarAttr.DocGenerRDT, value);
	}
	public final String getWidth() throws Exception
	{
		return this.GetValStrByKey(BarAttr.Width);
	}
	public final void setWidth(String value) throws Exception
	{
		this.SetValByKey(BarAttr.Width, value);
	}
	public final String getHeight() throws Exception
	{
		return this.GetValStrByKey(BarAttr.Height);
	}
	public final void setHeight(String value) throws Exception
	{
		this.SetValByKey(BarAttr.Height, value);
	}
	public final String getDoc() throws Exception
	{
		String html = "";
		switch (this.getBarType())
		{
			case 0:
				html += "\t\n<ul style='padding-left:13px;width:200px;overflow:hidden;'>";

				String sql = this.getTag1();
				sql = sql.replace("~", "'");
				sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
				sql = sql.replace("@WebUser.getName()", WebUser.getName());
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows)
				{
					String no = dr.get("No").toString();
					String name = dr.get("Name").toString();
					String url = this.getTag2().clone().toString();
						//url = url.Replace("@No", no);
					url = url.replace("~", "'");
						//if (url.Contains("@"))
						//{
						//   foreach (DataColumn dc in dt.Columns)
						//        url = url.Replace("@" + dc.ColumnName, dr[dc.ColumnName].ToString());
						//}
					url = this.GetParameteredString(url, dr);

					if (url.toLowerCase().startsWith("javascript:"))
					{
						html += "\t\n<li><a href=\"" + url + "\">" + name + "</a></li>";
					}
					else
					{
						switch (this.getOpenWay())
						{
							case 0: //新窗口
								html += "\t\n<li><a href=\"" + url + "\"  target='_blank' >" + name + "</a></li>";
								break;
							case 1: // 本窗口
								html += "\t\n<li><a href=\"" + url + "\" target='_self' >" + name + "</a></li>";
								break;
							case 2: //覆盖新窗口
								html += "\t\n<li><a href=\"" + url + "\" target=' " + this.getNo()+ " ' >" + name + "</a></li>";
								break;
							default:
								break;
						}
					}
				}
				html += "\t\n</ul>";
				return html;
			case 1:
				return this.getTag1();
			default:
				break;
		}
		return this.GetValStrByKey(BarAttr.Doc);
	}

	/** 
	 获取参数化的字符串
	 
	 @param stringInput
	 @param dr
	 @return 
	*/
	private String GetParameteredString(String stringInput, DataRow dr)
	{
		String regE = "@[a-zA-Z]([\\w-]*[a-zA-Z0-9])?"; //字母开始，字母+数字结尾，字母+数字+下划线+中划线中间
		//String regE = "@[\\w-]+";                               //字母+数字+下划线+中划线
		MatchCollection mc = Regex.Matches(stringInput, regE, RegexOptions.IgnoreCase);
		for (Match m : mc)
		{
			String v = m.Value;
			String f = m.Value.substring(1);
			stringInput = stringInput.replace(v, String.format("%1$s", dr.get(f)));
		}
		return stringInput;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 信息块
	*/
	public Bar()
	{
	}
	/** 
	 信息块
	 
	 @param mypk
	 * @throws Exception 
	*/
	public Bar(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_Bar");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("信息块");
		map.setEnType(EnType.Sys);

		map.AddTBStringPK(BarAttr.No, null, "编号", true, false, 1, 200, 200);
		map.AddTBString(BarAttr.Name, null, "名称", true, false, 0, 3900, 20);

		map.AddTBString(BarAttr.Title, null, "标题", true, false, 0, 3900, 20);

			//map.AddDDLSysEnum(BarAttr.BarType, 0, "信息块类型", true, true, 
			//BarAttr.BarType, "@0=标题消息列表(Tag1=SQL语句)@1=其它");
			//map.AddTBString(BarAttr.Tag1, null, "Tag1", true, false, 0, 3900, 300,true);
			//map.AddTBString(BarAttr.Tag2, null, "Tag2", true, false, 0, 3900, 300, true);
			//map.AddTBString(BarAttr.Tag3, null, "Tag3", true, false, 0, 3900, 300, true);
			//map.AddBoolean(BarAttr.IsDel, true, "用户是否可删除",true,true);


		map.AddDDLSysEnum(BarAttr.OpenWay, 0, "打开方式", true, true, BarAttr.OpenWay, "@0=新窗口@1=本窗口@2=覆盖新窗口");
		map.AddBoolean(BarAttr.IsLine, false, "是否独占一行", true, true);

			////map.AddDDLSysEnum(AppAttr.CtrlWay, 0, "控制方式", true, true,
			////   AppAttr.CtrlWay, "@0=游客@1=所有人员@2=按岗位@3=按部门@4=按人员@5=按SQL");

			//map.AddTBInt(BarAttr.Idx, 0, "显示顺序", false, true);

			// map.AddTBString(BarAttr.MoreLab, "更多...", "更多标签", true, false, 0, 900, 20);
		map.AddTBString(BarAttr.MoreUrl, null, "更多标签Url", true, false, 0, 3900, 20,true);
			 //map.AddTBString(BarAttr.Doc, null, "Doc", false, false, 0, 3900, 20, false);

			//map.AddTBDateTime(BarAttr.DocGenerRDT, null, "Doc生成日期", false, false);

			//map.AddTBInt(BarAttr.Width, 200, "显示宽度", false, true);
			//map.AddTBInt(BarAttr.Height, 100, "显示高度", false, true);

			//map.getAttrsOfOneVSM().Add(new ByStations(), new Stations(), ByStationAttr.RefObj, ByStationAttr.FK_Station, StationAttr.Name, StationAttr.No, "可访问的岗位");
			//map.getAttrsOfOneVSM().Add(new ByDepts(), new Depts(), ByStationAttr.RefObj, ByDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "可访问的部门");
			//map.getAttrsOfOneVSM().Add(new ByEmps(), new Emps(), ByStationAttr.RefObj, ByEmpAttr.FK_Emp, EmpAttr.Name, EmpAttr.No, "可访问的人员");

		map.AddSearchAttr(BarAttr.OpenWay);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final String GetDocument() throws Exception
	{

		BarBase barBase = (BarBase)ClassFactory.GetObject_OK(this.getNo());
		if (barBase == null)
		{
			this.Delete();
			return "err@已经删除.";
		}
		return barBase.getDocuments();
	}
	@Override
	protected void afterDelete() throws Exception
	{
		String sql = "DELETE FROM GPM_BarEmp WHERE FK_Bar=' " + this.getNo()+ " '";
		DBAccess.RunSQL(sql);
		super.afterDelete();
	}
}