package cn.jflow.controller.wf.comm.Utilities;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Tools.StringHelper;

public class NpoiFuncs {

	// <summary>
	// 将DataTable输出到Excel文件
	// <para>added by liuxc,2017-05-20</para>
	// </summary>
	// <param name="dt">DataTable数据
	// <para></para>
	// <para>DataColumn.ExtendedProperties中Key值定义如下：</para>
	// <para>width:列宽（像素）</para>
	// <para>sum:是否合计该列</para>
	// <para>isdate:是否是日期格式（false为日期时间格式）</para>
	// <para>k:此列使用千位分隔符（只对数值格式列有效）</para>
	// <para>dots:该列显示的小数位数（只对数值格式列有效）</para>
	// <para></para>
	// </param>
	// <param name="filename">导出Excel文件的保存路径，为本地绝对路径</param>
	// <param name="header">文件标题，null则不显示文件标题行</param>
	// <param name="creator">创建人，null则不显示创建人行</param>
	// <param name="date">是否显示导出日期行</param>
	// <param name="index">是否自动添加“序”号列</param>
	// <param name="download">生成文件后，是否自动下载</param>
	// <returns></returns>
	public static void DataTableToExcel(HttpServletRequest request, HttpServletResponse response, DataTable dt, String filename, String header, String creator, boolean date, boolean index, boolean download) {
		long len = 0;
		HSSFRow row = null, headerRow = null, dateRow = null, sumRow = null, creatorRow = null;
		HSSFCell cell = null;
		int r = 0;
		int c = 0;
		int headerRowIndex = 0; // 文件标题行序
		int dateRowIndex = 0; // 日期行序
		int titleRowIndex = 0; // 列标题行序
		int sumRowIndex = 0; // 合计行序
		int creatorRowIndex = 0; // 创建人行序
		float DEF_ROW_HEIGHT = 20; // 默认行高
		float charWidth = 0; // 单个字符宽度
		int columnWidth = 0; // 列宽，像素
		boolean isDate; // 是否是日期格式，否则是日期时间格式
		int decimalPlaces = 2; // 小数位数
		boolean qian; // 是否使用千位分隔符
		List<Integer> sumColumns = new ArrayList<Integer>(); // 合计列序号集合

		File file = new File(filename);
		String name = file.getName();
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		// 一个字符的像素宽度，以Arial，10磅，i进行测算
		// using (Bitmap bmp = new Bitmap(10, 10))
		// {
		// using (Graphics g = Graphics.FromImage(bmp))
		// {
		// charWidth = g.MeasureString("i", new Font("Arial", 10)).Width;
		// }
		// }
		{
			Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2d = (Graphics2D) image.getGraphics();
			FontMetrics fontMetrics = graphics2d.getFontMetrics();
			Rectangle2D rectangle2D = fontMetrics.getStringBounds("i", graphics2d);
			charWidth = (float) rectangle2D.getWidth();
		}

		// 序
		if (index && !dt.Columns.contains("序")) {
			dt.Columns.Add("序", Integer.class).ExtendedProperties.Add("width", 50);
			dt.Columns.get("序").setOrdinal(0);
			for (int i = 0; i < dt.Rows.size(); i++) {
				dt.Rows.get(i).setValue("序", i + 1);
			}
		}
		// 合计列
		for (DataColumn col : dt.Columns) {
			if (!col.ExtendedProperties.ContainsKey("sum")) {
				continue;
			}
			sumColumns.add(col.getOrdinal());
		}

		headerRowIndex = StringHelper.isNullOrEmpty(header) ? -1 : 0;
		dateRowIndex = date ? (headerRowIndex + 1) : -1;
		titleRowIndex = date ? dateRowIndex + 1 : headerRowIndex == -1 ? 0 : 1;
		sumRowIndex = sumColumns.size() == 0 ? -1 : titleRowIndex + dt.Rows.size() + 1;
		creatorRowIndex = StringHelper.isNullOrEmpty(creator) ? -1 : sumRowIndex == -1 ? titleRowIndex + dt.Rows.size() + 1 : sumRowIndex + 1;

		// using (FileStream fs = new FileStream(filename, FileMode.Create))
		{
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Sheet1");
			sheet.setDefaultRowHeightInPoints(DEF_ROW_HEIGHT);
			HSSFFont font = null;
			HSSFDataFormat fmt = wb.createDataFormat();

			if (headerRowIndex != -1) {
				headerRow = sheet.createRow(headerRowIndex);
			}
			if (date) {
				dateRow = sheet.createRow(dateRowIndex);
			}
			if (sumRowIndex != -1) {
				sumRow = sheet.createRow(sumRowIndex);
			}
			if (creatorRowIndex != -1) {
				creatorRow = sheet.createRow(creatorRowIndex);
			}

			// 列标题单元格样式设定
			HSSFCellStyle titleStyle = wb.createCellStyle();
			titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			font = wb.createFont();
			font.setBold(true);
			titleStyle.setFont(font);

			// “序”列标题样式设定
			HSSFCellStyle idxTitleStyle = wb.createCellStyle();
			idxTitleStyle.cloneStyleFrom(titleStyle);
			idxTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 文件标题单元格样式设定
			HSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			font = wb.createFont();
			font.setFontHeightInPoints((short) 12);
			font.setBold(true);
			headerStyle.setFont(font);

			// 制表人单元格样式设定
			HSSFCellStyle userStyle = wb.createCellStyle();
			userStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			userStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			// 单元格样式设定
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			// 数字单元格样式设定
			HSSFCellStyle numCellStyle = wb.createCellStyle();
			numCellStyle.cloneStyleFrom(cellStyle);
			numCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

			// “序”列单元格样式设定
			HSSFCellStyle idxCellStyle = wb.createCellStyle();
			idxCellStyle.cloneStyleFrom(cellStyle);
			idxCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 日期单元格样式设定
			HSSFCellStyle dateCellStyle = wb.createCellStyle();
			dateCellStyle.cloneStyleFrom(cellStyle);
			dateCellStyle.setDataFormat(fmt.getFormat("yyyy-m-d;@"));

			// 日期时间单元格样式设定
			HSSFCellStyle timeCellStyle = wb.createCellStyle();
			timeCellStyle.cloneStyleFrom(cellStyle);
			timeCellStyle.setDataFormat(fmt.getFormat("yyyy-m-d h:mm;@"));

			// 千分位单元格样式设定
			HSSFCellStyle qCellStyle = wb.createCellStyle();
			qCellStyle.cloneStyleFrom(cellStyle);
			qCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			qCellStyle.setDataFormat(fmt.getFormat("#,##0_ ;@"));

			// 小数点、千分位单元格样式设定
			Dictionary<String, HSSFCellStyle> cstyles = new Hashtable<String, HSSFCellStyle>();
			HSSFCellStyle cstyle = null;

			// 输出列标题
			row = sheet.createRow(titleRowIndex);
			row.setHeightInPoints(DEF_ROW_HEIGHT);

			for (DataColumn col : dt.Columns) {
				cell = row.createCell(c++);
				cell.setCellValue(col.ColumnName);
				cell.setCellStyle(col.ColumnName == "序" ? idxTitleStyle : titleStyle);

//				columnWidth = col.ExtendedProperties.ContainsKey("width") ? (int) col.ExtendedProperties.get("width") : 100;
				columnWidth = col.ExtendedProperties.ContainsKey("width") ? Integer.parseInt((String)col.ExtendedProperties.get("width")) : 100;
				sheet.setColumnWidth(c - 1, (int) (Math.ceil(columnWidth / charWidth) + 0.72) * 256);

				if (headerRow != null) {
					headerRow.createCell(c - 1);
				}
				if (dateRow != null) {
					dateRow.createCell(c - 1);
				}
				if (sumRow != null) {
					sumRow.createCell(c - 1);
				}
				if (creatorRow != null) {
					creatorRow.createCell(c - 1);
				}

				// 定义数字列单元格样式
				if (col.DataType == Double.class
						|| col.DataType == BigDecimal.class
						|| "Single".equals(col.DataType)
						|| "Double".equals(col.DataType)
						|| "Decimal".equals(col.DataType)
						|| "BigDecimal".equals(col.DataType)) {
					if (col.ExtendedProperties.ContainsKey("dots")) {
						Object dots = col.ExtendedProperties.get("dots");
						if (dots instanceof Integer) {
							decimalPlaces = ((Integer) dots).intValue();
						} else if (dots instanceof Double) {
							decimalPlaces = ((Double) dots).intValue();
						} else if (dots instanceof BigDecimal) {
							decimalPlaces = ((BigDecimal) dots).intValue();
						}
					}
//					qian = col.ExtendedProperties.ContainsKey("k") ? (boolean) col.ExtendedProperties.get("k") : false;
					qian = col.ExtendedProperties.ContainsKey("k") ? Boolean.parseBoolean((String)col.ExtendedProperties.get("k")) : false;

					if (decimalPlaces > 0 && !qian) {
						cstyle = wb.createCellStyle();
						cstyle.cloneStyleFrom(qCellStyle);
						// cstyle.setDataFormat(fmt.GetFormat("0." + string.Empty.PadLeft(decimalPlaces, '0') + "_ ;@"));
						cstyle.setDataFormat(fmt.getFormat("0." + StringHelper.padLeft("0", decimalPlaces) + "_ ;@"));
					} else if (decimalPlaces == 0 && qian) {
						cstyle = wb.createCellStyle();
						cstyle.cloneStyleFrom(qCellStyle);
					} else if (decimalPlaces > 0 && qian) {
						cstyle = wb.createCellStyle();
						cstyle.cloneStyleFrom(qCellStyle);
						// cstyle.setDataFormat(fmt.GetFormat("#,##0." + string.Empty.PadLeft(decimalPlaces, '0') + "_ ;@"));
						cstyle.setDataFormat(fmt.getFormat("#,##0." + StringHelper.padLeft("0", decimalPlaces) + "_ ;@"));
					}
					cstyles.put(col.ColumnName, cstyle);
				}
			}
			// 输出文件标题
			if (headerRow != null) {
				sheet.addMergedRegion(new CellRangeAddress(headerRowIndex, headerRowIndex, 0, dt.Columns.size() - 1));
				cell = headerRow.getCell(0);
				cell.setCellValue(header);
				cell.setCellStyle(headerStyle);
				headerRow.setHeightInPoints(26);
			}
			// 输出日期
			if (dateRow != null) {
				sheet.addMergedRegion(new CellRangeAddress(dateRowIndex, dateRowIndex, 0, dt.Columns.size() - 1));
				cell = dateRow.getCell(0);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				cell.setCellValue("日期：" + df.format(System.currentTimeMillis()));
				cell.setCellStyle(userStyle);
				dateRow.setHeightInPoints(DEF_ROW_HEIGHT);
			}
			// 输出制表人
			if (creatorRow != null) {
				sheet.addMergedRegion(new CellRangeAddress(creatorRowIndex, creatorRowIndex, 0, dt.Columns.size() - 1));
				cell = creatorRow.getCell(0);
				cell.setCellValue("制表人：" + creator);
				cell.setCellStyle(userStyle);
				creatorRow.setHeightInPoints(DEF_ROW_HEIGHT);
			}

			r = titleRowIndex + 1;
			// 输出查询结果
			for (DataRow dr : dt.Rows) {
				row = sheet.createRow(r++);
				row.setHeightInPoints(DEF_ROW_HEIGHT);
				c = 0;

				for (DataColumn col : dt.Columns) {
					cell = row.createCell(c++);

					if (col.DataType == Boolean.class || "Boolean".equals(col.DataType)) {
						cell.setCellStyle(cellStyle);
						cell.setCellValue(Boolean.TRUE.equals(dr.getValue(col.ColumnName)) ? "是" : "否");
					} else if (col.DataType == java.util.Date.class
							|| col.DataType == java.sql.Date.class
							|| col.DataType == Timestamp.class
							|| "DateTime".equals(col.DataType)) {
//						isDate = col.ExtendedProperties.ContainsKey("isdate") ? (boolean) col.ExtendedProperties.get("isdate") : false;
						isDate = col.ExtendedProperties.ContainsKey("isdate") ? Boolean.getBoolean((String)col.ExtendedProperties.get("isdate")) : false;
						cell.setCellStyle(isDate ? dateCellStyle : timeCellStyle);
						cell.setCellValue(dr.getValue(col.ColumnName).toString());
					} else if (col.DataType == Integer.class
							|| "Int16".equals(col.DataType)
							|| "Int32".equals(col.DataType)
							|| "Int64".equals(col.DataType)) {
//						qian = col.ExtendedProperties.ContainsKey("k") ? (boolean)col.ExtendedProperties.get("k") : false;
						qian = col.ExtendedProperties.ContainsKey("k") ? Boolean.parseBoolean((String)col.ExtendedProperties.get("k")) : false;
						cell.setCellStyle("序".equals(col.ColumnName) ? idxCellStyle : qian ? qCellStyle : numCellStyle);
						Object value = dr.getValue(col.ColumnName);
						if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						}
					} else if (col.DataType == Double.class
							|| col.DataType == BigDecimal.class
							|| "Single".equals(col.DataType)
							|| "Double".equals(col.DataType)
							|| "Decimal".equals(col.DataType)
							|| "BigDecimal".equals(col.DataType)) {
						cell.setCellStyle(cstyles.get(col.ColumnName));
						Object value = dr.getValue(col.ColumnName);
						if (value instanceof Double) {
							cell.setCellValue((Double) value);
						} else if (value instanceof BigDecimal) {
							cell.setCellValue(((BigDecimal) value).doubleValue());
						}
					} else {
						cell.setCellStyle(cellStyle);
						cell.setCellValue(dr.getValue(col.ColumnName).toString());
					}
				}
			}
			// 合计
			if (sumRow != null) {
				sumRow.setHeightInPoints(DEF_ROW_HEIGHT);
				for (c = 0; c < dt.Columns.size(); c++) {
					cell = sumRow.getCell(c);
					cell.setCellStyle(cellStyle);

					if (!sumColumns.contains(c)) {
						continue;
					}
					try {
						cell.setCellFormula(String.format("SUM(%s:%s)", GetCellName(c, titleRowIndex + 1), GetCellName(c, titleRowIndex + dt.Rows.size())));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			OutputStream out = null;
			try {
				out = new FileOutputStream(file);
				wb.write(out);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
					} finally {
						out = null;
					}
				}
				if (wb != null) {
					try {
						wb.close();
					} catch (IOException e) {
					} finally {
						wb = null;
					}
				}
			}
			len = file.length();
		}

		// 弹出下载
		if (download) {
			try {
				String agent = request.getHeader("User-Agent");
				boolean isFireFox = (agent != null && agent.toLowerCase().indexOf("firefox") != -1);
				if (!isFireFox) {
					name = new String(name.getBytes("UTF-8"), "ISO8859-1");
				}
			} catch (UnsupportedEncodingException e) {
			}	
			response.setHeader("Content-Length", String.valueOf(len));
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment; filename=" + name);
			OutputStream out = null;
			InputStream in = null;
			try {
				out = response.getOutputStream();
				in = new FileInputStream(file);
				int length;
				byte[] buff = new byte[8848];
				while ((length = in.read(buff)) != -1) {
					out.write(buff, 0, length);
				}
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					} finally {
						in = null;
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
					} finally {
						out = null;
					}
				}
			}
		}
	}

	// <summary>
	// 获取单元格的显示名称，格式如A1,B2
	// </summary>
	// <param name="columnIdx">单元格列号</param>
	// <param name="rowIdx">单元格行号</param>
	// <returns></returns>
	public static String GetCellName(int columnIdx, int rowIdx) throws Exception {
		int[] maxs = new int[] { 26, 26 * 26 + 26, 26 * 26 * 26 + (26 * 26 + 26) + 26 };
		int col = columnIdx + 1;
		int row = rowIdx + 1;

		if (col > maxs[2])
			throw new Exception("列序号不正确，超出最大值");

		int alphaCount = 1;

		for (int m : maxs) {
			if (m < col)
				alphaCount++;
		}

		switch (alphaCount) {
		case 1:
			return (char) (col + 64) + "" + row;
		case 2:
			return (char) ((col / 26) + 64) + "" + (char) ((col % 26) + 64) + row;
		case 3:
			return (char) ((col / 26 / 26) + 64) + "" + (char) (((col - col / 26 / 26 * 26 * 26) / 26) + 64) + "" + (char) ((col % 26) + 64) + row;
		}
		return "Unkown";
	}

}
