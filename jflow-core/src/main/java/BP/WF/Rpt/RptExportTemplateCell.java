package BP.WF.Rpt;

import BP.Tools.StringHelper;

/** 
 报表导出模板字段与单元格绑定信息对象
 
*/
public class RptExportTemplateCell
{
	private String _cellName;

	/** 
	 单元格行号
	*/
	private int privateRowIdx;
	public final int getRowIdx()
	{
		return privateRowIdx;
	}
	public final void setRowIdx(int value)
	{
		privateRowIdx = value;
	}

	/** 
	 单元格列号
	*/
	private int privateColumnIdx;
	public final int getColumnIdx()
	{
		return privateColumnIdx;
	}
	public final void setColumnIdx(int value)
	{
		privateColumnIdx = value;
	}

	/** 
	 获取单元格名称
	*/
	public final String getCellName()
	{
		if (StringHelper.isNullOrWhiteSpace(_cellName))
		{
			_cellName = GetCellName(getColumnIdx(), getRowIdx());
		}

		return _cellName;
	}

	/** 
	  单元格所属sheet表名
	*/
	private String privateSheetName;
	public final String getSheetName()
	{
		return privateSheetName;
	}
	public final void setSheetName(String value)
	{
		privateSheetName = value;
	}

	/** 
	 字段所属fk_mapdata
	*/
	private String privateFK_MapData;
	public final String getFK_MapData()
	{
		return privateFK_MapData;
	}
	public final void setFK_MapData(String value)
	{
		privateFK_MapData = value;
	}

	/** 
	 字段英文名
	*/
	private String privateKeyOfEn;
	public final String getKeyOfEn()
	{
		return privateKeyOfEn;
	}
	public final void setKeyOfEn(String value)
	{
		privateKeyOfEn = value;
	}

	/** 
	 明细表字段所属fk_mapdata
	*/
	private String privateFK_DtlMapData;
	public final String getFK_DtlMapData()
	{
		return privateFK_DtlMapData;
	}
	public final void setFK_DtlMapData(String value)
	{
		privateFK_DtlMapData = value;
	}

	/** 
	 明细表字段英文名
	*/
	private String privateDtlKeyOfEn;
	public final String getDtlKeyOfEn()
	{
		return privateDtlKeyOfEn;
	}
	public final void setDtlKeyOfEn(String value)
	{
		privateDtlKeyOfEn = value;
	}

	/** 
	 获取单元格的显示名称，格式如A1,B2
	 @param columnIdx 单元格列号
	 @param rowIdx 单元格行号
	 @return 
	*/
	public static String GetCellName(int columnIdx, int rowIdx)
	{
		int[] maxs = new int[] { 26, 26 * 26 + 26, 26 * 26 * 26 + (26 * 26 + 26) + 26 };
		int col = columnIdx + 1;
		int row = rowIdx + 1;

		if (col > maxs[2])
		{
			throw new RuntimeException("列序号不正确，超出最大值");
		}

		//int alphaCount = maxs.Where(m => m < col).Count() + 1;
		int alphaCount = 1;
		for (int m : maxs)
        {
            if (m < col)
                alphaCount++;
        }

		switch (alphaCount)
		{
			case 1:
				return (char)(col + 64) + "" + row;
			case 2:
				return (char)((col / 26) + 64) + "" + (char)((col % 26) + 64) + row;
			case 3:
				return (char)((col / 26 / 26) + 64) + "" + (char)(((col - col / 26 / 26 * 26 * 26) / 26) + 64) + "" + (char)((col % 26) + 64) + row;
		}

		return "Unkown";
	}
}