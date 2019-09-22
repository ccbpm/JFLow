package BP.GPM;

import BP.DA.*;

/** 
 加密类
*/
public class Crypto
{
	/** 
	 加密字符串
	 
	 @param encryptString 传入加密字符串
	 @return 加密后字符
	*/
	public static String EncryptString(String encryptString)
	{
		String strEncrypt = encryptString;
		if (DataType.IsNullOrEmpty(encryptString))
		{
			return encryptString;
		}

		strEncrypt = Crypto.MD5_Encrypt(strEncrypt);
		strEncrypt = Crypto.MD5_Encrypt(strEncrypt);
		return strEncrypt.toUpperCase();
	}
	/**  
	 使用指定的编码将字符串散列 
	 
	 @param encryptString 要散列的字符串 
	 @return 散列后的字符串 
	*/
	public static String MD5_Encrypt(String encryptString)
	{
		System.Security.Cryptography.MD5 md5 = new System.Security.Cryptography.MD5CryptoServiceProvider();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] source = md5.ComputeHash(Encoding.Default.GetBytes(encryptString));
		byte[] source = md5.ComputeHash(Encoding.Default.GetBytes(encryptString));
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < source.length; i++)
		{
			sBuilder.append(String.format("%02X", source[i]));
		}
		return sBuilder.toString();
	}

	//SHA1 加密 （HASH算法没有解密）
	/** 
	 利用SHA1加密一个字符串
	*/
	public static String SHA1_Encrypt(String encryptString)
	{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] StrRes = Encoding.Default.GetBytes(encryptString);
		byte[] StrRes = Encoding.Default.GetBytes(encryptString);
		HashAlgorithm iSHA = new SHA1CryptoServiceProvider();
		StrRes = iSHA.ComputeHash(StrRes);
		StringBuilder EnText = new StringBuilder();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: foreach (byte iByte in StrRes)
		for (byte iByte : StrRes)
		{
			EnText.append(String.format("%02x", iByte));
		}
		return EnText.toString();
	}
}