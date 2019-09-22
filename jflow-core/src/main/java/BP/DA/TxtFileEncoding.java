package BP.DA;

import java.io.*;

//  作者：袁晓辉
//  2005-8-8
// // // // // //
/** 
 用于取得一个文本文件的编码方式(Encoding)。
*/
public class TxtFileEncoding
{
	public TxtFileEncoding()
	{
		//
		// TODO: 在此处添加构造函数逻辑
		//
	}
	/** 
	 取得一个文本文件的编码方式。如果无法在文件头部找到有效的前导符，Encoding.Default将被返回。
	 
	 @param fileName 文件名。
	 @return 
	*/
	public static Encoding GetEncoding(String fileName)
	{
		return GetEncoding(fileName, Encoding.Default);
	}
	/** 
	 取得一个文本文件流的编码方式。
	 
	 @param stream 文本文件流。
	 @return 
	*/
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.FileStream is input or output:
	public static Encoding GetEncoding(FileStream stream)
	{
		return GetEncoding(stream, Encoding.Default);
	}
	/** 
	 取得一个文本文件的编码方式。
	 
	 @param fileName 文件名。
	 @param defaultEncoding 默认编码方式。当该方法无法从文件的头部取得有效的前导符时，将返回该编码方式。
	 @return 
	*/
	public static Encoding GetEncoding(String fileName, Encoding defaultEncoding)
	{
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.FileStream is input or output:
		FileStream fs = new FileStream(fileName, FileMode.Open);
		Encoding targetEncoding = GetEncoding(fs, defaultEncoding);
		fs.Close();
		return targetEncoding;
	}
	/** 
	 取得一个文本文件流的编码方式。
	 
	 @param stream 文本文件流。
	 @param defaultEncoding 默认编码方式。当该方法无法从文件的头部取得有效的前导符时，将返回该编码方式。
	 @return 
	*/
	public static Encoding GetEncoding(FileInputStream stream, Encoding defaultEncoding)
	{
		Encoding targetEncoding = defaultEncoding;
		if (stream != null && stream.Length >= 2)
		{
			//保存文件流的前4个字节
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte byte1 = 0;
			byte byte1 = 0;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte byte2 = 0;
			byte byte2 = 0;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte byte3 = 0;
			byte byte3 = 0;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte byte4 = 0;
			byte byte4 = 0;
			//保存当前Seek位置
			long origPos = stream.Seek(0, SeekOrigin.Begin);
			stream.Seek(0, SeekOrigin.Begin);

			int nByte = stream.read();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte1 = Convert.ToByte(nByte);
			byte1 = (byte)nByte;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte2 = Convert.ToByte(stream.ReadByte());
			byte2 = (byte)stream.read();
			if (stream.Length >= 3)
			{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte3 = Convert.ToByte(stream.ReadByte());
				byte3 = (byte)stream.read();
			}

			if (stream.Length >= 4)
			{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte4 = Convert.ToByte(stream.ReadByte());
				byte4 = (byte)stream.read();
			}
			//根据文件流的前4个字节判断Encoding
			//Unicode {0xFF, 0xFE};
			//BE-Unicode {0xFE, 0xFF};
			//UTF8 = {0xEF, 0xBB, 0xBF};
			if (byte1 == 0xFE && byte2 == 0xFF) //UnicodeBe
			{
				targetEncoding = Encoding.BigEndianUnicode;
			}
			if (byte1 == 0xFF && byte2 == 0xFE && byte3 != 0xFF) //Unicode
			{
				targetEncoding = Encoding.Unicode;
			}
			if (byte1 == 0xEF && byte2 == 0xBB && byte3 == 0xBF) //UTF8
			{
				targetEncoding = Encoding.UTF8;
			}
			//恢复Seek位置       
			stream.Seek(origPos, SeekOrigin.Begin);
		}
		return targetEncoding;
	}
}