package bp.da;

public enum XmlWriteMode
{
	/**
	 * 以 XML 数据形式写入 System.Data.DataSet 的当前内容，以关系结构作为内联 XSD 架构。如果
	 * System.Data.DataSet 只有架构而无数据，那么只写入内联架构。如果 System.Data.DataSet
	 * 没有当前架构，则不写入任何内容。
	 */
	WriteSchema,
	/**
	 * 以 XML 数据形式写入 System.Data.DataSet 的当前内容，不带 XSD 架构。如果无数据加载到
	 * System.Data.DataSet 中，则不写入任何内容。
	 */
	IgnoreSchema;
	/**
	 * 作为 DiffGram 写入整个 System.Data.DataSet，包括原始值和当前值。若要生成只包含已更改的值的 DiffGram，请调用
	 * System.Data.DataSet.GetChanges()，然后在返回的 System.Data.DataSet 上作为 DiffGram
	 * 调用 System.Data.DataSet.WriteXml(System.IO.Stream)。
	 */
	// DiffGram,
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static XmlWriteMode forValue(int value) throws Exception
	{
		return values()[value];
	}
}