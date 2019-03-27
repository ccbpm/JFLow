package BP.WF.Template;

public enum TemplateFileModel {
	/// <summary>
    /// 旧版本的rtf模版格式
    /// </summary>
    RTF(0),
    /// <summary>
    /// Word模版格式
    /// </summary>
    VSTOForWord(1),
    /// <summary>
    /// Excel模版格式
    /// </summary>
    VSTOForExcel(2);
    
    private int intValue;
	private static java.util.HashMap<Integer, TemplateFileModel> mappings;
	private synchronized static java.util.HashMap<Integer, TemplateFileModel> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, TemplateFileModel>();
		}
		return mappings;
	}

	private TemplateFileModel(int value)
	{
		intValue = value;
		TemplateFileModel.getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static TemplateFileModel forValue(int value)
	{
		return getMappings().get(value);
	}
}
