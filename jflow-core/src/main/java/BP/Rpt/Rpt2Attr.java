package BP.Rpt;

import java.util.ArrayList;

import BP.DA.AtPara;
import BP.DA.DataTable;
import BP.Sys.Glo;
import BP.Tools.StringHelper;
import BP.Web.WebUser;

/**
 * 数据报表
 */
public class Rpt2Attr
{
	/**
	 * 数据报表
	 */
	public Rpt2Attr()
	{
	}
	
	private String _Title = "";
	
	/**
	 * 标题
	 * @throws Exception 
	 */
	public final String getTitle() throws Exception
	{
		if (StringHelper.isNullOrEmpty(_Title))
		{
			return "";
		}
		
		if (_Title.contains("@") == false)
		{
			return _Title;
		}
		
		String tempVar = _Title;
		String title = (String) ((tempVar instanceof String) ? tempVar : null);
		
		title = title.replace("WebUser.No", WebUser.getNo());
		title = title.replace("@WebUser.Name", WebUser.getName());
		title = title.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		title = title.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		if (title.contains("@") == false)
		{
			return title;
		}
		
		ArrayList<String> keys = BP.Sys.Glo.getQueryStringKeys();
		
		for (String key : keys)
		{
			title = title
					.replace("@" + key, Glo.getRequest().getParameter(key));
		}
		
		if (title.contains("@") == false)
		{
			return title;
		}
		
		if (title.contains("@") == false)
		{
			return title;
		}
		
		AtPara ap = new AtPara(this.DefaultParas);
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
	 * 左侧菜单标题
	 */
	public String LeftMenuTitle = "";
	/**
	 * 编码数据源
	 */
	public String DBSrc = "";
	/**
	 * 详细信息(可以为空)
	 */
	public String DBSrcOfDtl = "";
	/**
	 * 左边的菜单.
	 */
	public String LeftMenu = "";
	/**
	 * 高度.
	 */
	public int H = 420;
	/**
	 * 默认宽度
	 */
	public int W = 900;
	/**
	 * 底部文字.
	 */
	public String xAxisName = "";
	/**
	 * 右边文字
	 */
	public String yAxisName = "";
	/**
	 * 数值列的前缀
	 */
	public String numberPrefix = "";
	/**
	 * 图标的横向的参照线的条数.
	 */
	public int numDivLines = 8;
	/**
	 * 默认显示的数据图表.
	 */
	public DBAChartType DefaultShowChartType = DBAChartType.Column;
	/**
	 * 是否启用table.
	 */
	public boolean IsEnableTable = true;
	
	/**
	 * 柱图显示类型.
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
	 * 是否显示饼图
	 */
	public boolean IsEnablePie = true;
	/**
	 * 是否显示柱图
	 */
	public boolean IsEnableColumn = true;
	/**
	 * 是否显示折线图
	 */
	public boolean IsEnableLine = true;
	/**
	 * 折线图显示类型.
	 */
	public LineChartShowType lineChartShowType = LineChartShowType.HengXiang;
	/**
	 * 默认参数.
	 */
	public String DefaultParas = "";
	/**
	 * y最大值.
	 */
	public double MaxValue = 0;
	/**
	 * y最小值.
	 */
	public double MinValue = 0;
	/**
	 * 最底部的信息制表等信息.
	 */
	public String ChartInfo = null;
	/**
	 * 字段关系表达式
	 */
	public String ColExp = "";
	/**
	 * 说明
	 */
	public String DESC = "";
	// ---------------------------------------------------------------
	/**
	 * 设置图表背景的颜色
	 */
	public String canvasBgColor = "FF8E46";
	/**
	 * 设置图表基部的颜色
	 */
	public String canvasBaseColor = "008E8E";
	/**
	 * 设置图表基部的高度
	 */
	public String canvasBaseDepth = "5";
	/**
	 * 设置图表背景的深度
	 */
	public String canvasBgDepth = "5";
	/**
	 * 设置是否显示图表背景
	 */
	public String showCanvasBg = "1";
	/**
	 * 设置是否显示图表基部
	 */
	public String showCanvasBase = "1";
	// ---------------------------------------------------------------
	/**
	 * 数据源.
	 */
	public DataTable _DBDataTable = null;
	
	public final DataTable getDBDataTable() throws Exception
	{
		if (_DBDataTable == null)
		{
			// 获得数据表.
			// 执行SQL.
			String sql = this.DBSrc;
			sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sql = sql.replace("WebUser.No", WebUser.getNo());
			sql = sql.replace("@WebUser.Name", WebUser.getName());
			
			ArrayList<String> keys = BP.Sys.Glo.getQueryStringKeys();
			
			for (String k : keys)
			{
				sql = sql.replace("@" + k, Glo.getRequest().getParameter(k));
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
}
