package cn.jflow.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;

public class FileUtils
{
	
	/**
	 * 将文件转成base64 字符串
	 * 
	 * @param path文件路径
	 * @return *
	 * @throws Exception
	 */
	
	public static String encodeBase64File(String path) throws Exception
	{
		File file = new File(path);
		;
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return new Base64().encodeToString(buffer);
	}
	
	/**
	 * 将base64字符解码保存文件
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	
	public static void decoderBase64File(String base64Code, String targetPath)
			throws Exception
	{
		byte[] buffer = Base64.decodeBase64(base64Code);
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
		
	}
	
	/**
	 * 将base64字符保存文本文件
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	
	public static void toFile(String base64Code, String targetPath)
			throws Exception
	{
		
		byte[] buffer = base64Code.getBytes();
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}
	
	/**
	 * 获取文件mime 类型
	 * 
	 * @param fileUrl
	 * @return
	 * @throws java.io.IOException
	 * @throws MalformedURLException
	 */
	public static String getMimeType(String fileUrl)
			throws java.io.IOException, MalformedURLException
	{
		String type = null;
		URL u = new URL("file://" + fileUrl);
		URLConnection uc = null;
		uc = u.openConnection();
		type = uc.getContentType();
		return type;
	}
	
	/**
	 * 获取byte
	 * 
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readAsByteArray(InputStream inStream)
			throws IOException
	{
		int size = 1024;
		byte[] buff = new byte[size];
		int readSoFar = 0;
		while (true)
		{
			int nRead = inStream.read(buff, readSoFar, size - readSoFar);
			if (nRead == -1)
			{
				break;
			}
			readSoFar += nRead;
			if (readSoFar == size)
			{
				int newSize = size * 2;
				byte[] newBuff = new byte[newSize];
				System.arraycopy(buff, 0, newBuff, 0, size);
				buff = newBuff;
				size = newSize;
			}
		}
		byte[] newBuff = new byte[readSoFar];
		System.arraycopy(buff, 0, newBuff, 0, readSoFar);
		return newBuff;
	}
}
