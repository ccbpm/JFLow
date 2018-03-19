package cn.jflow.common.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.w3c.dom.Document;

import BP.DA.DataSet;

public class OfficeToPDF {
	public static final String OFFICE_DOC = "doc";
	public static final String OFFICE_DOCX = "docx";
	public static final String OFFICE_XLS = "xls";
	public static final String OFFICE_XLSX = "xlsx";
	public static final String OFFICE_PPT = "ppt";
	public static final String OFFICE_PPTX = "pptx";
	public static final String OFFICE_TO_PDF = "pdf";

	/**
	 * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件<br>
	 *
	 * @param inputFilePath
	 *            源文件路径，如："e:/test.docx"
	 * @param outputFilePath
	 *            目标文件路径，如："e:/test_docx.pdf"
	 * @return
	 */
	public static boolean openOfficeToPDF(String inputFilePath,
			String outputFilePath) {
		return office2pdf(inputFilePath, outputFilePath);
	}

	/**
	 * 获取OpenOffice.org 3的安装目录
	 *
	 * @return OpenOffice.org 3的安装目录
	 */
	public static String getOfficeHome() {
		String osName = System.getProperty("os.name");
		if (Pattern.matches("Linux.*", osName)) {
			return "/opt/openoffice.org3";
		} else if (Pattern.matches("Windows.*", osName)) {
			return "C:/Program Files (x86)/OpenOffice 4";
		}
		return null;
	}

	/**
	 * 连接OpenOffice.org 并且启动OpenOffice.org
	 *
	 * @return
	 */
	public static OfficeManager getOfficeManager() {
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		// 获取OpenOffice.org 3的安装目录
		String officeHome = getOfficeHome();
		config.setOfficeHome(officeHome);
		// 启动OpenOffice的服务
		OfficeManager officeManager = config.buildOfficeManager();
		officeManager.start();
		return officeManager;
	}

	/**
	 * 转换文件
	 *
	 * @param inputFile
	 * @param outputFilePath_end
	 * @param inputFilePath
	 * @param outputFilePath
	 * @param converter
	 */
	public static void converterFile(File inputFile, String outputFilePath_end,
			String inputFilePath, String outputFilePath,
			OfficeDocumentConverter converter) {
		File outputFile = new File(outputFilePath_end);
		// 假如目标路径不存在,则新建该路径
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		converter.convert(inputFile, outputFile);
	}

	/**
	 * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件
	 *
	 * @param inputFilePath
	 *            源文件路径，如："e:/test.docx"
	 * @param outputFilePath
	 *            目标文件路径，如："e:/test_docx.pdf"
	 * @return
	 */
	public static boolean office2pdf(String inputFilePath, String outputFilePath) {
		boolean flag = false;
		OfficeManager officeManager = getOfficeManager();
		// 连接OpenOffice
		OfficeDocumentConverter converter = new OfficeDocumentConverter(
				officeManager);
		if (null != inputFilePath) {
			File inputFile = new File(inputFilePath);
//			System.out.println(inputFile.getName());
			File outFile = new File(outputFilePath);
//			System.out.println(outFile.getName());
			// 判断目标文件路径是否为空
			if (null == outputFilePath) {
				// 转换后的文件路径
				String outputFilePath_end = getOutputFilePath(inputFilePath);
				if (inputFile.exists()) {// 找不到源文件, 则返回
					converterFile(inputFile, outputFilePath_end, inputFilePath,
							outputFilePath, converter);
					flag = true;
				}
			} else {
				if (inputFile.exists()) {// 找不到源文件, 则返回
					converterFile(inputFile, outputFilePath, inputFilePath,
							outputFilePath, converter);
					flag = true;
				}
			}
			officeManager.stop();
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 获取输出文件
	 *
	 * @param inputFilePath
	 * @return
	 */
	public static String getOutputFilePath(String inputFilePath) {
		String outputFilePath = inputFilePath.replaceAll("."
				+ getPostfix(inputFilePath), ".pdf");
		return outputFilePath;
	}

	/**
	 * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"
	 *
	 * @param inputFilePath
	 * @return
	 */
	public static String getPostfix(String inputFilePath) {
		return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
	}

	public static boolean copy(String fileFrom, String fileTo) {
		try {
			FileInputStream in = new java.io.FileInputStream(fileFrom);
			FileOutputStream out = new FileOutputStream(fileTo);
			byte[] bt = new byte[1024];
			int count;
			while ((count = in.read(bt)) > 0) {
				out.write(bt, 0, count);
			}
			in.close();
			out.close();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	public static void main(String[] args) throws IOException {

		// OfficeToPDF.copy("F:/JJFlow/test.txt", "F:/JJFlow/test.doc");
		String str="F:/JJFlow/test.doc";
		FileInputStream f=new FileInputStream(str);
//		if(file_put_contents(f,null,"F:/JJFlow/test.pdf"))
//		{
//			System.out.println("yes");
//		}
//		else
//		{
//			System.out.println("no");
//		}
		if (file_put_contents(f,"F:/JJFlow/t.doc", "F:/JJFlow/test.pdf")) {
			System.out.println("成功");
		}

	}

	/*
	 * 将InputStream写入到文件 file_name:文件的路径 is:传一个流
	 */
	public static boolean file_put_contents(InputStream is,String wordSrc,String pdfSrc) throws IOException {
		String str = wordSrc;
		FileOutputStream out = new FileOutputStream(str);
		byte[] bt = new byte[1024];
		int count;
		while ((count = is.read(bt)) > 0) {
			out.write(bt, 0, count);
		}
		try {
//			System.out.println(str);
//			System.out.println(pdfSrc);
			if (office2pdf(str, pdfSrc)) {
				return true;
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
