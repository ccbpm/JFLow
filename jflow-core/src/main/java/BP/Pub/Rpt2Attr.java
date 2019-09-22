package BP.Pub;

import BP.DA.*;
import BP.En.*;
import BP.Web.Controls.*;
import BP.Web.*;
import BP.Sys.*;

/** 
 数据报表
*/
public class Rpt2Attr
{
	/** 
	 数据报表
	*/
	public Rpt2Attr()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性.
	/** 
	 子标题
	*/
	public String SubTitle = "SubTitle：子标题没有设置";
	/** 
	 统计纬度
	*/
	public String TongJiWeiDu = "TongJiWeiDu：统计维度,属性没有设置.";

	private String _Title = "";
	/** 
	 标题
	*/
	public final String getTitle()
	{
		if (DataType.IsNullOrEmpty(_Title))
		{
			return "";
		}

		if (_Title.contains("@") == false)
		{
			return _Title;
		}

		Object tempVar = _Title.Clone();
		String title = tempVar instanceof String ? (String)tempVar : null;

		title = title.replace("@WebUser.No", BP.Web.WebUser.getNo());
		title = title.replace("@WebUser.Name", BP.Web.WebUser.getName());
		title = title.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
		title = title.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
		if (title.contains("@") == false)
		{
			return title;
		}

			//foreach (string key in Glo.Request.QueryString)
			//{
			//    title = title.Replace("@" + key, Glo.Request.QueryString[key]);
			//}

		if (title.contains("@") == false)
		{
			return title;
		}

		if (title.contains("@") == false)
		{
			return title;
		}

		BP.DA.AtPara ap = new AtPara(this.DefaultParas);
		for (String key : ap.getHisHT().keySet())
		{
			title = title.replace("@" + key, ap.GetValStrByKey(key));
		}

		return title;
	}
	public final void setTitle(String value)
	{
		_Title = value;
	}

	/** 
	 左侧菜单标题
	*/
	public String LeftMenuTitle = "";
	/** 
	 编码数据源
	*/
	public String DBSrc = "";
	/** 
	 详细信息(可以为空)
	*/
	public String DBSrcOfDtl = "";
	/** 
	 左边的菜单.
	*/
	public String LeftMenu = "";
	/** 
	 高度.
	*/
	public int H = 420;
	/** 
	 默认宽度
	*/
	public int W = 900;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 基本属性.

	/** 
	 底部文字.
	*/
	public String xAxisName = "";
	/** 
	 右边文字
	*/
	public String yAxisName = "";
	/** 
	 数值列的前缀
	*/
	public String numberPrefix = "";
	/** 
	 图标的横向的参照线的条数.
	*/
	public int numDivLines = 8;
	/** 
	 默认显示的数据图表.
	*/
	public DBAChartType DefaultShowChartType = DBAChartType.Column;
	/** 
	 是否启用table.
	*/
	public boolean IsEnableTable = true;
	/** 
	 柱图显示类型.
	*/
	private ColumnChartShowType _ColumnChartShowType = ColumnChartShowType.ShuXiang;
	public final ColumnChartShowType getColumnChartShowType()
	{
		return _ColumnChartShowType;
	}
	public final void setColumnChartShowType(ColumnChartShowType value)
	{
		_ColumnChartShowType = value;
	}
	/** 
	 是否显示饼图
	*/
	public boolean IsEnablePie = true;
	/** 
	 是否显示柱图
	*/
	public boolean IsEnableColumn = true;
	/** 
	 是否显示折线图
	*/
	public boolean IsEnableLine = true;
	/** 
	 折线图显示类型.
	*/
	public LineChartShowType LineChartShowType = LineChartShowType.HengXiang;
	/** 
	 默认参数.
	*/
	public String DefaultParas = "";
	/** 
	 y最大值.
	*/
	public double MaxValue = 0;
	/** 
	 y最小值.
	*/
	public double MinValue = 0;
	/** 
	 最底部的信息制表等信息.
	*/
	public String ChartInfo = null;
	/** 
	 字段关系表达式
	*/
	public String ColExp = "";
	/** 
	 说明
	*/
	public String DESC = "";
	//---------------------------------------------------------------
   /** 
	 设置图表背景的颜色
   */
	public String canvasBgColor = "FF8E46";
	/** 
	 设置图表基部的颜色
	*/
	public String canvasBaseColor = "008E8E";
	/** 
	 设置图表基部的高度 
	*/
	public String canvasBaseDepth = "5";
	/** 
	 设置图表背景的深度 
	*/
	public String canvasBgDepth = "5";
	/** 
	 设置是否显示图表背景 
	*/
	public String showCanvasBg = "1";
	/** 
	 设置是否显示图表基部
	*/
	public String showCanvasBase = "1";
	//---------------------------------------------------------------
	/** 
	 数据源.
	*/
	public DataTable _DBDataTable = null;
	public final DataTable getDBDataTable()
	{
		if (_DBDataTable == null)
		{
				//获得数据表.
				// 执行SQL.
			String sql = this.DBSrc;
			sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
			sql = sql.replace("@WebUser.No", BP.Web.WebUser.getNo());
			sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
			for (String k : HttpContextHelper.getRequestParamKeys())
			{
				sql = sql.replace("@" + k, HttpContextHelper.RequestParams(k));
			}
			if (sql.contains("@") == true)
			{
				BP.DA.AtPara ap = new BP.DA.AtPara(this.DefaultParas);
				for (String k : ap.getHisHT().keySet())
				{
					sql = sql.replace("@" + k, ap.getHisHT().get(k).toString());
				}
			}

			_DBDataTable = BP.DA.DBAccess.RunSQLReturnTable(sql);
		}
		return _DBDataTable;
	}
	public final void setDBDataTable(DataTable value)
	{
		_DBDataTable = value;
	}
	/** 
	 转化成Json
	 
	 @return string
	*/
	public final String ToJson()
	{

		DataTable dt = this.getDBDataTable();

		//图示列数据.
		String series_Data = "[";
		for (DataRow dr : dt.Rows)
		{
			series_Data += ",'" + dr.get(1).toString() + "'";
		}
		series_Data += "]";
		series_Data = series_Data.replace("[,", "[");

		//行程数据源.
		String dbData = "[";
		for (DataRow dr : dt.Rows)
		{
			dbData += "{value:" + dr.get(2).toString() + ",name:'" + dr.get(1) + "'},";
		}
		dbData = dbData.substring(0, dbData.length() - 1);
		dbData = dbData + "]";


		//组合成要生成的json格式.
		String str = "";
		str += "{";
		str += "title: {";

		str += " text: '" + this.getTitle() + "',";
		str += " subtext: '" + this.SubTitle + "',";
		str += " x: 'center'";
		str += " },";
		str += "tooltip: {";
		str += "    trigger: 'item',";
		str += "    formatter: \"{a} <br/>{b} : {c} ({d}%)\"";
		str += "},";
		str += " legend: {";
		str += "     orient: 'vertical',";
		str += "     left: 'left',";
		str += "     data:" + series_Data + "";
		str += "  },";
		str += "  series: [";
		str += "    { ";
		str += "     name: '" + this.TongJiWeiDu + "',";
		str += "     type: 'pie',";
		str += "    radius: '55%',";
		str += "   center: ['50%', '60%'],";
		str += "    data: " + dbData + ",";

		str += "   itemStyle: {";
		str += "     emphasis: {";
		str += "         shadowBlur: 10,";
		str += "        shadowOffsetX: 0,";
		str += "       shadowColor: 'rgba(0, 0, 0, 0.5)'";
		str += "    }";
		str += "  }";
		str += " }";
		str += "  ] ";
		str += "}; ";

	   // BP.DA.DataType.WriteFile("c:\\111.txt", str);
		return str;
	}
}