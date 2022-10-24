package bp.wf.rpt;

import bp.da.DataType;

/**
 报表导出模板字段与单元格绑定信息对象
*/
public class RptExportTemplateCell
{
	private String _cellName;

	/** 
	 单元格行号
	*/
	private int RowIdx;
	public final int getRowIdx()throws Exception
	{
		return RowIdx;
	}
	public final void setRowIdx(int value) throws Exception
	{
		RowIdx = value;
	}

	/** 
	 单元格列号
	*/
	private int ColumnIdx;
	public final int getColumnIdx()throws Exception
	{
		return ColumnIdx;
	}
	public final void setColumnIdx(int value) throws Exception
	{
		ColumnIdx = value;
	}

	/** 
	 获取单元格名称
	*/
	public final String getCellName()throws Exception
	{
		if (DataType.IsNullOrEmpty(_cellName))
		{
			_cellName = GetCellName(getColumnIdx(), getRowIdx());
		}

		return _cellName;
	}

	/** 
	  单元格所属sheet表名
	*/
	private String SheetName;
	public final String getSheetName()throws Exception
	{
		return SheetName;
	}
	public final void setSheetName(String value) throws Exception
	{
		SheetName = value;
	}

	/** 
	 字段所属fk_mapdata
	*/
	private String FK_MapData;
	public final String getFK_MapData()throws Exception
	{
		return FK_MapData;
	}
	public final void setFK_MapData(String value) throws Exception
	{
		FK_MapData = value;
	}

	/** 
	 字段英文名
	*/
	private String KeyOfEn;
	public final String getKeyOfEn()throws Exception
	{
		return KeyOfEn;
	}
	public final void setKeyOfEn(String value) throws Exception
	{
		KeyOfEn = value;
	}

	/** 
	 明细表字段所属fk_mapdata
	*/
	private String FK_DtlMapData;
	public final String getFK_DtlMapData()throws Exception
	{
		return FK_DtlMapData;
	}
	public final void setFK_DtlMapData(String value) throws Exception
	{
		FK_DtlMapData = value;
	}

	/** 
	 明细表字段英文名
	*/
	private String DtlKeyOfEn;
	public final String getDtlKeyOfEn()throws Exception
	{
		return DtlKeyOfEn;
	}
	public final void setDtlKeyOfEn(String value) throws Exception
	{
		DtlKeyOfEn = value;
	}

	/** 
	 获取单元格的显示名称，格式如A1,B2
	 
	 param columnIdx 单元格列号
	 param rowIdx 单元格行号
	 @return 
	*/
	public static String GetCellName(int columnIdx, int rowIdx)
	{
		int[] maxs = new int[] {26, 26 * 26 + 26, 26 * 26 * 26 + (26 * 26 + 26) + 26};
		int col = columnIdx + 1;
		int row = rowIdx + 1;

		if (col > maxs[2])
		{
			throw new RuntimeException("列序号不正确，超出最大值");
		}

		int alphaCount = 1;

		for (int m : maxs)
		{
			if (m < col)
			{
				alphaCount++;
			}
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