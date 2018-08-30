package BP.GPM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BP.DA.*;
import BP.En.*;

/** 
 信息块
 
*/
public class Bar extends EntityNoName
{
	/** 
	 控制权限
	 * @throws Exception 
	 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsInsert = false;

		return super.getHisUAC();
	}

	///#region 属性
	/** 
	 更多的URL
	 
	*/
	public final String getMoreUrl()
	{
		return this.GetValStrByKey(BarAttr.MoreUrl);
	}
	public final void setMoreUrl(String value)
	{
		this.SetValByKey(BarAttr.MoreUrl, value);
	}
	/** 
	 更多标签
	 
	*/
	public final String getMoreLab()
	{
		return this.GetValStrByKey(BarAttr.MoreLab);
	}
	public final void setMoreLab(String value)
	{
		this.SetValByKey(BarAttr.MoreLab, value);
	}
	/** 
	 标题
	 
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(BarAttr.Title);
	}
	public final void setTitle(String value)
	{
		this.SetValByKey(BarAttr.Title, value);
	}
	/** 
	 用户是否可以删除
	 
	*/
	public final boolean getIsDel()
	{
		return this.GetValBooleanByKey(BarAttr.IsDel);
	}
	public final void setIsDel(boolean value)
	{
		this.SetValByKey(BarAttr.IsDel, value);
	}
	/** 
	 类型
	 
	*/
	public final int getBarType()
	{
		return this.GetValIntByKey(BarAttr.BarType);
	}
	public final void setBarType(int value)
	{
		this.SetValByKey(BarAttr.BarType, value);
	}
	/** 
	 打开方式
	 
	*/
	public final int getOpenWay()
	{
		return this.GetValIntByKey(BarAttr.OpenWay);
	}
	public final void setOpenWay(int value)
	{
		this.SetValByKey(BarAttr.OpenWay, value);
	}
	/** 
	 顺序号
	 
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(BarAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(BarAttr.Idx, value);
	}
	/** 
	 Tag1
	 
	*/
	public final String getTag1()
	{
		return this.GetValStrByKey(BarAttr.Tag1);
	}
	public final void setTag1(String value)
	{
		this.SetValByKey(BarAttr.Tag1, value);
	}
	/** 
	 Tag
	 
	*/
	public final String getTag2()
	{
		return this.GetValStrByKey(BarAttr.Tag2);
	}
	public final void setTag2(String value)
	{
		this.SetValByKey(BarAttr.Tag2, value);
	}
	public final String getTag3()
	{
		return this.GetValStrByKey(BarAttr.Tag3);
	}
	public final void setTag3(String value)
	{
		this.SetValByKey(BarAttr.Tag3, value);
	}
	public final String getDocGenerRDT()
	{
		return this.GetValStrByKey(BarAttr.DocGenerRDT);
	}
	public final void setDocGenerRDT(String value)
	{
		this.SetValByKey(BarAttr.DocGenerRDT, value);
	}
	public final String getWidth()
	{
		return this.GetValStrByKey(BarAttr.Width);
	}
	public final void setWidth(String value)
	{
		this.SetValByKey(BarAttr.Width, value);
	}
	public final String getHeight()
	{
		return this.GetValStrByKey(BarAttr.Height);
	}
	public final void setHeight(String value)
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
				sql = sql.replace("@WebUser.No", BP.Web.WebUser.getNo());
				sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows)
				{
					String no = dr.getValue("No").toString();
					String name = dr.getValue("Name").toString();
					String url = this.getTag2().toString();
						
					url = url.replace("~", "'");
						
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
								html += "\t\n<li><a href=\"" + url + "\" target='" + this.getNo() + "' >" + name + "</a></li>";
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
		//字母+数字+下划线+中划线
		 // 编译正则表达式
	    Pattern pattern = Pattern.compile(regE);
	    // 忽略大小写的写法
	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(stringInput);
		while (matcher.find())
		{
			String v = matcher.group();
			String f = matcher.group().substring(1);
			stringInput = stringInput.replace(v, String.format("%1$s", dr.getValue(f).toString()));
		}
		return stringInput;
	}
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
		//    BarAttr.BarType, "@0=标题消息列表(Tag1=SQL语句)@1=其它");

		//map.AddTBString(BarAttr.Tag1, null, "Tag1", true, false, 0, 3900, 300,true);
		//map.AddTBString(BarAttr.Tag2, null, "Tag2", true, false, 0, 3900, 300, true);
		//map.AddTBString(BarAttr.Tag3, null, "Tag3", true, false, 0, 3900, 300, true);

		//map.AddBoolean(BarAttr.IsDel, true, "用户是否可删除",true,true);

		map.AddDDLSysEnum(BarAttr.OpenWay, 0, "打开方式", true, true, BarAttr.OpenWay, "@0=新窗口@1=本窗口@2=覆盖新窗口");

		////map.AddDDLSysEnum(AppAttr.CtrlWay, 0, "控制方式", true, true,
		////   AppAttr.CtrlWay, "@0=游客@1=所有人员@2=按岗位@3=按部门@4=按人员@5=按SQL");

		//map.AddTBInt(BarAttr.Idx, 0, "显示顺序", false, true);

		// map.AddTBString(BarAttr.MoreLab, "更多...", "更多标签", true, false, 0, 900, 20);
		map.AddTBString(BarAttr.MoreUrl, null, "更多标签Url", true, false, 0, 3900, 20,true);
		//map.AddTBString(BarAttr.Doc, null, "Doc", false, false, 0, 3900, 20, false);

		//map.AddTBDateTime(BarAttr.DocGenerRDT, null, "Doc生成日期", false, false);

		map.AddTBInt(BarAttr.Width, 200, "显示宽度", false, true);
		map.AddTBInt(BarAttr.Height, 100, "显示高度", false, true);

		//map.AttrsOfOneVSM.Add(new ByStations(), new Stations(), ByStationAttr.RefObj, ByStationAttr.FK_Station, StationAttr.Name, StationAttr.No, "可访问的岗位");
		//map.AttrsOfOneVSM.Add(new ByDepts(), new Depts(), ByStationAttr.RefObj, ByDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "可访问的部门");
		//map.AttrsOfOneVSM.Add(new ByEmps(), new Emps(), ByStationAttr.RefObj, ByEmpAttr.FK_Emp, EmpAttr.Name, EmpAttr.No, "可访问的人员");

		map.AddSearchAttr(BarAttr.OpenWay);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion

	public final String GetDocument() throws Exception
	{
		BarBase barBase = (BarBase)ClassFactory.GetObject_OK(this.getNo());
		return barBase.getDocuments();
	}

}