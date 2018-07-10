package BP.WF.Template;




public enum SelectorModel
{
	 
	/// <summary>
    /// 岗位
    /// </summary>
    Station,
    /// <summary>
    /// 部门
    /// </summary>
    Dept,
    /// <summary>
    /// 操作员
    /// </summary>
    Emp,
    /// <summary>
    /// SQL
    /// </summary>
    SQL,
    /// <summary>
    /// SQL模版计算
    /// </summary>
    SQLTemplate,
    /// <summary>
    /// 通用的人员选择器.
    /// </summary>
    GenerUserSelecter,
    /// <summary>
    /// 按部门与岗位的交集
    /// </summary>
    DeptAndStation,
    /// <summary>
    /// 自定义链接
    /// </summary>
    Url,
	/// <summary>
    /// 通用部门岗位人员选择器
    /// </summary>
    AccepterOfDeptStationEmp;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SelectorModel forValue(int value)
	{
		return values()[value];
	}
}