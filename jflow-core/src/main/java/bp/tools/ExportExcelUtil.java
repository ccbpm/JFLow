package bp.tools;


import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.en.Attr;
import bp.en.Attrs;
import bp.en.Entity;
import bp.en.FieldType;
import bp.pub.PubClass;
import bp.sys.UIConfig;
import bp.web.WebUser;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.text.DecimalFormat;
public class ExportExcelUtil{

		public static String ExportDGToExcel(DataSet ds, String title, String params) throws Exception {

			DataTable dt = ds.GetTableByName("GroupSearch");
			DataTable AttrsOfNum = ds.GetTableByName("AttrsOfNum");
			DataTable AttrsOfGroup = ds.GetTableByName("AttrsOfGroup");

			String fileName = title+"Ep" + title + ".xls";
			String fileDir = SystemConfig.getPathOfTemp();
			String filePth = SystemConfig.getPathOfTemp();
			// 参数及变量设置
			// 如果导出目录没有建立，则建立.
			File file = new File(fileDir);
			if (!file.exists()) {
				file.mkdirs();
			}

			filePth = filePth + "/" + fileName;
			file = new File(filePth);
			if (file.exists()) {
				file.delete();
			}

			// String httpFilePath =
			// Glo.getCCFlowAppPath()+"DataUser/Temp/"+fileName;
			int headerRowIndex = 0; // 文件标题行序
			int titleRowIndex = 1; // 列标题行序
			int countCell = 0;// 显示的列数
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet(title+"Ep" + title);
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row = null;
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
			HSSFFont font = null;
			HSSFDataFormat fmt = wb.createDataFormat();
			HSSFCell cell = null;

			// 生成标题

			row = sheet.createRow((int) titleRowIndex);
			int index = 0;// 控制列 qin 15.9.21
			//添加序号
			cell = row.createCell(index);
			cell.setCellStyle(style);
			cell.setCellValue("序号");
			index += 1;
			countCell++;
			for (DataRow attr : AttrsOfGroup.Rows) {
				cell = row.createCell(index);
				cell.setCellStyle(style);
				cell.setCellValue(attr.getValue("Name").toString());
				index += 1;
				countCell++;
			}
			for (DataRow attr : AttrsOfNum.Rows) {
				cell = row.createCell(index);
				cell.setCellStyle(style);
				cell.setCellValue(attr.getValue("Name").toString());
				index += 1;
				countCell++;
			}
			DataRow dr = null;
			for (int i = 2; i <= dt.Rows.size() + 1; i++) {
				dr = dt.Rows.get(i - 2);
				row = sheet.createRow(i);
				// 生成文件内容
				index = 0;
				cell = row.createCell(index);
				cell.setCellStyle(style);
				cell.setCellValue(dr.getValue("IDX").toString());
				index += 1;
				for (DataRow attr : AttrsOfGroup.Rows) {

					cell = row.createCell(index);
					cell.setCellStyle(style);
					cell.setCellValue(dr.getValue(attr.getValue("KeyOfEn")+"T").toString());
					index += 1;
				}
				for (DataRow attr : AttrsOfNum.Rows) {

					cell = row.createCell(index);
					cell.setCellStyle(style);
					cell.setCellValue(dr.getValue(attr.getValue("KeyOfEn").toString()).toString());
					index += 1;
				}

			}
			int creatorRowIndex = titleRowIndex + dt.Rows.size() + 1;

			row = sheet.createRow((int) creatorRowIndex);

			// 生成文件内容
			index = 0;
			cell = row.createCell(index);
			cell.setCellStyle(style);
			cell.setCellValue("汇总");
			index += 1;
			for (DataRow attr : AttrsOfGroup.Rows) {

				cell = row.createCell(index);
				cell.setCellStyle(style);
				cell.setCellValue("");
				index += 1;
			}

			for (DataRow attr : AttrsOfNum.Rows) {
				double d =0;
				cell = row.createCell(index);
				cell.setCellStyle(style);
				for(DataRow dtr : dt.Rows){
					d += Double.parseDouble(dtr.getValue(attr.getValue("KeyOfEn").toString()).toString());
				}
				if(params.contains(attr.getValue("KeyOfEn")+"=AVG")){
					if(dt.Rows.size()!=0){
						DecimalFormat df = new DecimalFormat("#.0000");
						d = Double.valueOf(df.format(d/dt.Rows.size()));
					}

				}

				if(Integer.parseInt(attr.getValue("MyDataType").toString()) == DataType.AppInt){
					if(params.contains(attr.getValue("KeyOfEn")+"=AVG"))
						cell.setCellValue(d);
					else
						cell.setCellValue((int)d);
				}else{
					cell.setCellValue(d);
				}

				index += 1;
			}


			// 列标题单元格样式设定
			HSSFCellStyle titleStyle = wb.createCellStyle();
			/*
			 * titleStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
			 */
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			font = wb.createFont();
			font.setBold(true);
			titleStyle.setFont(font);
			row = sheet.createRow((int) 0);
			sheet.addMergedRegion(new CellRangeAddress(headerRowIndex, (short) headerRowIndex, 0, (short) (countCell - 1)));
			cell = row.createCell(headerRowIndex);
			cell.setCellValue(title);
			cell.setCellStyle(titleStyle);

			// 生成制单人
			// 制表人单元格样式设定
			HSSFCellStyle userStyle = wb.createCellStyle();
			userStyle.setAlignment(HorizontalAlignment.RIGHT);
			userStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			creatorRowIndex = creatorRowIndex+1;

			row = sheet.createRow((int) creatorRowIndex);

			sheet.addMergedRegion(new CellRangeAddress(creatorRowIndex, (short) 0, creatorRowIndex, (short) (countCell - 1)));
			cell = row.createCell(0);
			cell.setCellValue("制表人：" + WebUser.getName() + "日期：" + bp.da.DataType.getCurrentDateTimeCNOfShort());
			cell.setCellStyle(userStyle);
			// 第六步，将文件存到指定位置
			try {
				FileOutputStream fout = new FileOutputStream(filePth);
				wb.write(fout);
				fout.flush();
				fout.close();
				return "/DataUser/Temp/" + fileName;
			} catch (Exception e) {
				e.printStackTrace();
				return fileName;
			}

		}

		public static String ExportDGToExcel(DataTable dt, Entity en, String fileName, Attrs mapAttrs) throws Exception {
			int headerRowIndex = 0; // 文件标题行序
			int titleRowIndex = 1; // 列标题行序
			int countCell = 0;// 显示的列数
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet(fileName);
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row = null;
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
			HSSFFont font = null;
			HSSFDataFormat fmt = wb.createDataFormat();
			HSSFCell cell = null;

			// 生成标题
			Attrs attrs = null;
			if(mapAttrs!=null)
				attrs = mapAttrs;
			else
				attrs = en.getEnMap().getAttrs();
			Attrs selectedAttrs = null;
			UIConfig cfg = new UIConfig(en);
			if (cfg.getShowColumns().length == 0)
				selectedAttrs = attrs;
			else {
				selectedAttrs = new Attrs();

				for (Attr attr : attrs) {

					boolean contain = false;

					for (String col : cfg.getShowColumns()) {
						if (col.equals(attr.getKey())) {
							contain = true;
							break;
						}
					}

					if (contain)
						selectedAttrs.Add(attr);
				}
			}
			row = sheet.createRow((int) titleRowIndex);
			int index = 0;// 控制列 qin 15.9.21
			for (int i = 0; i < selectedAttrs.size(); i++) {
				Attr attr = selectedAttrs.get(i);
				if (attr.getKey().equals("MyNum"))
					continue;
				if (attr.getKey().equals("OID"))
					continue;

				if (attr.getUIVisible() == false && attr.getMyFieldType() != FieldType.RefText)
					continue;

				if (attr.getIsFKorEnum())
					continue;
				if (attr.getKey().equals("MyFilePath") || attr.getKey().equals("MyFileExt")
						|| attr.getKey().equals("WebPath") || attr.getKey().equals("MyFileH")
						|| attr.getKey().equals("MyFileW") || attr.getKey().equals("MyFileSize"))
					continue;

				cell = row.createCell(index);
				cell.setCellStyle(style);
				if(attr.getMyFieldType() == FieldType.RefText)
					cell.setCellValue(attr.getDesc().replace("名称",""));
				else
					cell.setCellValue(attr.getDesc());
				index += 1;
				countCell++;
			}
			DataRow dr = null;
			for (int i = 2; i <= dt.Rows.size() + 1; i++) {
				dr = dt.Rows.get(i - 2);
				row = sheet.createRow(i);
				// 生成文件内容
				index = 0;

				for (int j = 0; j < selectedAttrs.size(); j++) {
					Attr attr = selectedAttrs.get(j);
					if (attr.getKey().equals("MyNum"))
						continue;
					if (attr.getKey().equals("OID"))
						continue;
					if (attr.getIsFKorEnum())
						continue;

					if (attr.getUIVisible() == false && attr.getMyFieldType() != FieldType.RefText)
						continue;

					if (attr.getKey().equals("MyFilePath") || attr.getKey().equals("MyFileExt")
							|| attr.getKey().equals("WebPath") || attr.getKey().equals("MyFileH")
							|| attr.getKey().equals("MyFileW") || attr.getKey().equals("MyFileSize"))
						continue;


					String str = "";
					if (attr.getMyDataType() == DataType.AppBoolean) {
						if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
							str = dr.getValue(attr.getKey().toUpperCase()).equals(1) ? "是" : "否";
						else
							str = dr.getValue(attr.getKey()).equals(1) ? "是" : "否";
					} else {
						String text ="";
						if (SystemConfig.getAppCenterDBType() == DBType.Oracle){
							Object obj = dr.getValue((attr.getIsFKorEnum() ? (attr.getKey() + "Text") : attr.getKey()).toUpperCase());
							if(obj == null)
								text = "";
							else
								text =  obj.toString();
						}else{
							Object obj = dr.getValue(attr.getIsFKorEnum() ? (attr.getKey() + "Text") : attr.getKey());
							if(obj == null)
								text = "";
							else
								text =  obj.toString();
						}

						if(DataType.IsNullOrEmpty(text)==false && (text.contains("\n")==true ||text.contains("\r")==true)){
							str =""+text.replaceAll("\n", "  ");
							str =""+text.replaceAll("\r", "  ");
						}else{
							str = text;
						}
					}
					if (str == null || str.equals("") || str.equals("null")) {
						str = " ";
					}
					cell = row.createCell(index);
					cell.setCellStyle(style);
					cell.setCellValue(str);
					index += 1;
				}

			}

			// 列标题单元格样式设定
			HSSFCellStyle titleStyle = wb.createCellStyle();
			/*
			 * titleStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
			 */
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			font = wb.createFont();
			font.setBold(true);
			titleStyle.setFont(font);
			row = sheet.createRow((int) 0);
			sheet.addMergedRegion(new CellRangeAddress(headerRowIndex, (short) headerRowIndex, 0, (short) (countCell - 1)));
			cell = row.createCell(headerRowIndex);
			cell.setCellValue(fileName);
			cell.setCellStyle(titleStyle);

			// 生成制单人
			// 制表人单元格样式设定
			HSSFCellStyle userStyle = wb.createCellStyle();
			userStyle.setAlignment(HorizontalAlignment.RIGHT);
			userStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			int creatorRowIndex = titleRowIndex + dt.Rows.size() + 1;

			row = sheet.createRow((int) creatorRowIndex);

			sheet.addMergedRegion(new CellRangeAddress(creatorRowIndex, (short) 0, creatorRowIndex, (short) (countCell - 1)));
			cell = row.createCell(0);
			cell.setCellValue("制表人：" + WebUser.getName() + "日期：" + bp.da.DataType.getCurrentDateTimeCNOfShort());
			cell.setCellStyle(userStyle);
			// 第六步输出文本
			try {
//			String downloadFileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");
//			String headStr ="attachment; filename=\"" + downloadFileName +"\"";
//			this.getResponse().setContentType("application/force-download");
//			this.getResponse().setHeader("Content-Disposition", headStr);
//			OutputStream out = this.getResponse().getOutputStream();
//			//请一定要write一下，千万别忘记！！！！否则会一片空白，白到你怀疑人生
//			wb.write(out);
				//return "";

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				wb.write(bos);
				//byte[] array = bos.toByteArray();
				//return new String(array);
				byte[] brray = bos.toByteArray();
				InputStream is = new ByteArrayInputStream(brray);
				BufferedInputStream in=new BufferedInputStream(is);

				// 设置响应类型为html，编码为utf-8，处理相应页面文本显示的乱码
				ContextHolderUtils.getResponse().setContentType("application/octet-stream;charset=utf8");
				ContextHolderUtils.getResponse().setCharacterEncoding("UTF-8");
				// 设置文件头：最后一个参数是设置下载文件名
				ContextHolderUtils.getResponse().setHeader("Content-disposition", "attachment;filename="
						+ PubClass.toUtf8String(ContextHolderUtils.getRequest(), fileName+".xls"));
				ServletOutputStream out = ContextHolderUtils.getResponse().getOutputStream();
				// 读取文件流
				int len = 0;
				byte[] buffer = new byte[1024 * 10];
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
				in.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

            return fileName;
        }

		public static String ExportTempToExcel(DataTable dt, Entity en, String title, Attrs mapAttrs) throws Exception {

			String fileName = title + "_" + bp.da.DataType.getCurrentDateCNOfLong() + "_" + WebUser.getNo() + ".xls";
			String fileDir = SystemConfig.getPathOfTemp();
			String filePth = SystemConfig.getPathOfTemp();
			// 参数及变量设置
			// 如果导出目录没有建立，则建立.
			File file = new File(fileDir);
			if (!file.exists()) {
				file.mkdirs();
			}

			filePth = filePth + "/" + fileName;
			file = new File(filePth);
			if (file.exists()) {
				file.delete();
			}

			int headerRowIndex = 0; // 文件标题行序
			int titleRowIndex = 1; // 列标题行序
			int countCell = 0;// 显示的列数
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet(en.getEnMap().getPhysicsTable());
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row = null;
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
			HSSFFont font = null;
			HSSFDataFormat fmt = wb.createDataFormat();
			HSSFCell cell = null;

			// 生成标题
			Attrs attrs = null;
			if(mapAttrs!=null)
				attrs = mapAttrs;
			else
				attrs = en.getEnMap().getAttrs();
			Attrs selectedAttrs = null;
			UIConfig cfg = new UIConfig(en);
			if (cfg.getShowColumns().length == 0)
				selectedAttrs = attrs;
			else {
				selectedAttrs = new Attrs();

				for (Attr attr : attrs) {

					boolean contain = false;

					for (String col : cfg.getShowColumns()) {
						if (col.equals(attr.getKey())) {
							contain = true;
							break;
						}
					}

					if (contain)
						selectedAttrs.Add(attr);
				}
			}
			row = sheet.createRow((int) titleRowIndex);
			int index = 0;// 控制列 qin 15.9.21
			for (int i = 0; i < selectedAttrs.size(); i++) {
				Attr attr = selectedAttrs.get(i);
				if (attr.getKey().equals("MyNum"))
					continue;
				if (attr.getKey().equals("OID"))
					continue;

				if (attr.getUIVisible() == false && attr.getMyFieldType() != FieldType.RefText)
					continue;

				if (attr.getIsFKorEnum())
					continue;
				if (attr.getKey().equals("MyFilePath") || attr.getKey().equals("MyFileExt")
						|| attr.getKey().equals("WebPath") || attr.getKey().equals("MyFileH")
						|| attr.getKey().equals("MyFileW") || attr.getKey().equals("MyFileSize"))
					continue;

				cell = row.createCell(index);
				cell.setCellStyle(style);
				if(attr.getMyFieldType() == FieldType.RefText)
					cell.setCellValue(attr.getDesc().replace("名称",""));
				else
					cell.setCellValue(attr.getDesc());
				index += 1;
				countCell++;
			}
			DataRow dr = null;
			int temp=0;
			for (int i = 2; i <= dt.Rows.size() + 1; i++) {
				dr = dt.Rows.get(i - 2);
				row = sheet.createRow(i);
				// 生成文件内容
				index = 0;
				if(temp>1)
					continue;
				for (int j = 0; j < selectedAttrs.size(); j++) {
					Attr attr = selectedAttrs.get(j);
					if (attr.getKey().equals("MyNum"))
						continue;
					if (attr.getKey().equals("OID"))
						continue;
					if (attr.getIsFKorEnum())
						continue;

					if (attr.getUIVisible() == false && attr.getMyFieldType() != FieldType.RefText)
						continue;

					if (attr.getKey().equals("MyFilePath") || attr.getKey().equals("MyFileExt")
							|| attr.getKey().equals("WebPath") || attr.getKey().equals("MyFileH")
							|| attr.getKey().equals("MyFileW") || attr.getKey().equals("MyFileSize"))
						continue;


					String str = "";
					if (attr.getMyDataType() == DataType.AppBoolean) {
						if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
							str = dr.getValue(attr.getKey().toUpperCase()).equals(1) ? "是" : "否";
						else
							str = dr.getValue(attr.getKey()).equals(1) ? "是" : "否";
					} else {
						String text ="";
						if (SystemConfig.getAppCenterDBType() == DBType.Oracle){
							Object obj = dr.getValue((attr.getIsFKorEnum() ? (attr.getKey() + "Text") : attr.getKey()).toUpperCase());
							if(obj == null)
								text = "";
							else
								text =  obj.toString();
						}else{
							Object obj = dr.getValue(attr.getIsFKorEnum() ? (attr.getKey() + "Text") : attr.getKey());
							if(obj == null)
								text = "";
							else
								text =  obj.toString();
						}

						if(DataType.IsNullOrEmpty(text)==false && (text.contains("\n")==true ||text.contains("\r")==true)){
							str =""+text.replaceAll("\n", "  ");
							str =""+text.replaceAll("\r", "  ");
						}else{
							str = text;
						}
					}
					if (str == null || str.equals("") || str.equals("null")) {
						str = " ";
					}
					cell = row.createCell(index);
					cell.setCellStyle(style);
					cell.setCellValue(str);
					index += 1;
				}
				temp+=1;
			}

			// 列标题单元格样式设定
			HSSFCellStyle titleStyle = wb.createCellStyle();
			/*
			 * titleStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
			 * titleStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
			 */
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			font = wb.createFont();
			font.setBold(true);
			titleStyle.setFont(font);
			row = sheet.createRow((int) 0);
			sheet.addMergedRegion(new CellRangeAddress(headerRowIndex, (short) headerRowIndex, 0, (short) (countCell - 1)));
			cell = row.createCell(headerRowIndex);
			cell.setCellValue(title);
			cell.setCellStyle(titleStyle);

			// 生成制单人
			// 制表人单元格样式设定
			HSSFCellStyle userStyle = wb.createCellStyle();
			userStyle.setAlignment(HorizontalAlignment.RIGHT);
			userStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			int creatorRowIndex = titleRowIndex + dt.Rows.size() + 1;

			row = sheet.createRow((int) creatorRowIndex);

			sheet.addMergedRegion(new CellRangeAddress(creatorRowIndex, (short) 0, creatorRowIndex, (short) (countCell - 1)));
			cell = row.createCell(0);
			//cell.setCellValue("制表人：" + WebUser.getName() + "日期：" + bp.da.DataType.getCurrentDataTimeCNOfShort());
			cell.setCellStyle(userStyle);
			// 第六步，将文件存到指定位置
			try {
				if (!file.exists()) {
					file.createNewFile();
				}

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				wb.write(bos);
				byte[] brray = bos.toByteArray();
				InputStream is = new ByteArrayInputStream(brray);

				FileOutputStream fout = new FileOutputStream(filePth);

				BufferedInputStream in=null;
				BufferedOutputStream out=null;
				in=new BufferedInputStream(is);
				out=new BufferedOutputStream(fout);
				int len=-1;
				byte[] b=new byte[1024];
				while((len=in.read(b))!=-1){
					out.write(b,0,len);
				}
				in.close();
				out.close();


			/*BufferedWriter br = new BufferedWriter(osw);
			br.write(printMe);
			fout.flush();
			br.close();
			fout.close();
			osw.close();*/

				//wb.write(fout);
				//fout.flush();
				//fout.close();
				return "/DataUser/Temp/" + fileName;
			} catch (Exception e) {
				e.printStackTrace();
				return fileName;
			}

		}
}