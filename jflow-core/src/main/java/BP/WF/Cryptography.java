package BP.WF;

import java.security.MessageDigest;

public class Cryptography{
//	public static int HostToNetworkOrder(int inval)
//	{
//		int outval = 0;
//		for (int i = 0; i < 4; i++)
//		{
//			outval = (outval << 8) + ((inval >> (i * 8)) & 255);
//		}
//		return outval;
//	}
//	/** 
//	 解密方法
//	 
//	 @param Input 密文
//	 @param EncodingAESKey
//	 @return 
//	 
//	*/
//	public static String AES_decrypt(String Input, String EncodingAESKey, RefObject<String> corpid)
//	{
//		byte[] Key;
//		Key = Convert.FromBase64String(EncodingAESKey + "=");
//		byte[] Iv = new byte[16];
//		System.arraycopy(Key, 0, Iv, 0, 16);
//		byte[] btmpMsg = AES_decrypt(Input, Iv, Key);
//
//		int len = BitConverter.ToInt32(btmpMsg, 16);
//		len = IPAddress.NetworkToHostOrder(len);
//
//
//		byte[] bMsg = new byte[len];
//		byte[] bCorpid = new byte[btmpMsg.length - 20 - len];
//		System.arraycopy(btmpMsg, 20, bMsg, 0, len);
//		System.arraycopy(btmpMsg, 20+len, bCorpid, 0, btmpMsg.length - 20 - len);
//		String oriMsg = Encoding.UTF8.GetString(bMsg);
//		corpid.argvalue = Encoding.UTF8.GetString(bCorpid);
//
//
//		return oriMsg;
//	}
//
//	public static String AES_encrypt(String Input, String EncodingAESKey, String corpid)
//	{
//		byte[] Key;
//		Key = Convert.FromBase64String(EncodingAESKey + "=");
//		byte[] Iv = new byte[16];
//		System.arraycopy(Key, 0, Iv, 0, 16);
//		String Randcode = CreateRandCode(16);
//		byte[] bRand = Encoding.UTF8.GetBytes(Randcode);
//		byte[] bCorpid = Encoding.UTF8.GetBytes(corpid);
//		byte[] btmpMsg = Encoding.UTF8.GetBytes(Input);
//		byte[] bMsgLen = BitConverter.GetBytes(HostToNetworkOrder(btmpMsg.length));
//		byte[] bMsg = new byte[bRand.length + bMsgLen.length + bCorpid.length + btmpMsg.length];
//
//		System.arraycopy(bRand, 0, bMsg, 0, bRand.length);
//		System.arraycopy(bMsgLen, 0, bMsg, bRand.length, bMsgLen.length);
//		System.arraycopy(btmpMsg, 0, bMsg, bRand.length + bMsgLen.length, btmpMsg.length);
//		System.arraycopy(bCorpid, 0, bMsg, bRand.length + bMsgLen.length + btmpMsg.length, bCorpid.length);
//
//		return AES_encrypt(bMsg, Iv, Key);
//
//	}
//	private static String CreateRandCode(int codeLen)
//	{
//		String codeSerial = "2,3,4,5,6,7,a,c,d,e,f,h,i,j,k,m,n,p,r,s,t,A,C,D,E,F,G,H,J,K,M,N,P,Q,R,S,U,V,W,X,Y,Z";
//		if (codeLen == 0)
//		{
//			codeLen = 16;
//		}
//		String[] arr = codeSerial.split("[,]", -1);
//		String code = "";
//		int randValue = -1;
////TASK: There is no Java equivalent to 'unchecked' in this context:
////ORIGINAL LINE: Random rand = new Random(unchecked((int)DateTime.Now.Ticks));
//		java.util.Random rand = new java.util.Random((int)new java.util.Date().Ticks);
//		for (int i = 0; i < codeLen; i++)
//		{
//			randValue = rand.nextInt(arr.length - 1);
//			code += arr[randValue];
//		}
//		return code;
//	}
//
//	private static String AES_encrypt(String Input, byte[] Iv, byte[] Key)
//	{
//		RijndaelManaged aes = new RijndaelManaged();
//		//秘钥的大小，以位为单位
//		aes.KeySize = 256;
//		//支持的块大小
//		aes.BlockSize = 128;
//		//填充模式
//		aes.Padding = PaddingMode.PKCS7;
//		aes.Mode = CipherMode.CBC;
//		aes.setKey(Key);
//		aes.IV = Iv;
//
//		var encrypt = aes.CreateEncryptor(aes.getKey(), aes.IV);
//		byte[] xBuff = null;
//
//
////		using (var ms = new MemoryStream())
//		MemoryStream ms = new MemoryStream();
//		try
//		{
//
////			using (var cs = new CryptoStream(ms, encrypt, CryptoStreamMode.Write))
//			CryptoStream cs = new CryptoStream(ms, encrypt, CryptoStreamMode.Write);
//			try
//			{
//				byte[] xXml = Encoding.UTF8.GetBytes(Input);
//				cs.Write(xXml, 0, xXml.length);
//			}
//			finally
//			{
//				cs.dispose();
//			}
//			xBuff = ms.toArray();
//		}
//		finally
//		{
//			ms.dispose();
//		}
//		String Output = Convert.ToBase64String(xBuff);
//		return Output;
//	}
//
//	private static String AES_encrypt(byte[] Input, byte[] Iv, byte[] Key)
//	{
//		RijndaelManaged aes = new RijndaelManaged();
//		//秘钥的大小，以位为单位
//		aes.KeySize = 256;
//		//支持的块大小
//		aes.BlockSize = 128;
//		//填充模式
//		//aes.Padding = PaddingMode.PKCS7;
//		aes.Padding = PaddingMode.None;
//		aes.Mode = CipherMode.CBC;
//		aes.setKey(Key);
//		aes.IV = Iv;
//
//		var encrypt = aes.CreateEncryptor(aes.getKey(), aes.IV);
//		byte[] xBuff = null;
//
//
//		///#region 自己进行PKCS7补位，用系统自己带的不行
//		byte[] msg = new byte[Input.length + 32 - Input.length % 32];
//		System.arraycopy(Input, 0, msg, 0, Input.length);
//		byte[] pad = KCS7Encoder(Input.length);
//		System.arraycopy(pad, 0, msg, Input.length, pad.length);
//
//		///#endregion
//
//
//		///#region 注释的也是一种方法，效果一样
//		//ICryptoTransform transform = aes.CreateEncryptor();
//		//byte[] xBuff = transform.TransformFinalBlock(msg, 0, msg.Length);
//
//		///#endregion
//
//
////		using (var ms = new MemoryStream())
//		MemoryStream ms = new MemoryStream();
//		try
//		{
//
////			using (var cs = new CryptoStream(ms, encrypt, CryptoStreamMode.Write))
//			CryptoStream cs = new CryptoStream(ms, encrypt, CryptoStreamMode.Write);
//			try
//			{
//				cs.Write(msg, 0, msg.length);
//			}
//			finally
//			{
//				cs.dispose();
//			}
//			xBuff = ms.toArray();
//		}
//		finally
//		{
//			ms.dispose();
//		}
//
//		String Output = Convert.ToBase64String(xBuff);
//		return Output;
//	}
//
//	private static byte[] KCS7Encoder(int text_length)
//	{
//		int block_size = 32;
//		// 计算需要填充的位数
//		int amount_to_pad = block_size - (text_length % block_size);
//		if (amount_to_pad == 0)
//		{
//			amount_to_pad = block_size;
//		}
//		// 获得补位所用的字符
//		char pad_chr = chr(amount_to_pad);
//		String tmp = "";
//		for (int index = 0; index < amount_to_pad; index++)
//		{
//			tmp += pad_chr;
//		}
//		return Encoding.UTF8.GetBytes(tmp);
//	}
////        *
////         * 将数字转化成ASCII码对应的字符，用于对明文进行补码
////         * 
////         * @param a 需要转化的数字
////         * @return 转化得到的字符
////         
//	private static char chr(int a)
//	{
//
//		byte target = (byte)(a & 0xFF);
//		return (char)target;
//	}
//	private static byte[] AES_decrypt(String Input, byte[] Iv, byte[] Key)
//	{
//		RijndaelManaged aes = new RijndaelManaged();
//		aes.KeySize = 256;
//		aes.BlockSize = 128;
//		aes.Mode = CipherMode.CBC;
//		aes.Padding = PaddingMode.None;
//		aes.setKey(Key);
//		aes.IV = Iv;
//
//		var decrypt = aes.CreateDecryptor(aes.getKey(), aes.IV);
//		byte[] xBuff = null;
//
////		using (var ms = new MemoryStream())
//		MemoryStream ms = new MemoryStream();
//		try
//		{
//
////			using (var cs = new CryptoStream(ms, decrypt, CryptoStreamMode.Write))
//			CryptoStream cs = new CryptoStream(ms, decrypt, CryptoStreamMode.Write);
//			try
//			{
//				byte[] xXml = Convert.FromBase64String(Input);
//				byte[] msg = new byte[xXml.length + 32 - xXml.length % 32];
//				System.arraycopy(xXml, 0, msg, 0, xXml.length);
//				cs.Write(xXml, 0, xXml.length);
//			}
//			finally
//			{
//				cs.dispose();
//			}
//			xBuff = decode2(ms.toArray());
//		}
//		finally
//		{
//			ms.dispose();
//		}
//		return xBuff;
//	}
//	private static byte[] decode2(byte[] decrypted)
//	{
//		int pad = (int)decrypted[decrypted.length - 1];
//		if (pad < 1 || pad > 32)
//		{
//			pad = 0;
//		}
//		byte[] res = new byte[decrypted.length - pad];
//		System.arraycopy(decrypted, 0, res, 0, decrypted.length - pad);
//		return res;
//	}
	/** 加密字符串
	 
	 @param encryptString 传入加密字符串
	 @return 加密后字符
*/
	public static String EncryptString(String encryptString)
	{
		String strEncrypt = encryptString;
		if (DotNetToJavaStringHelper.isNullOrEmpty(encryptString))
		{
			return encryptString;
		}

		strEncrypt = MD5_Encrypt(strEncrypt);
		strEncrypt = MD5_Encrypt(strEncrypt);
		return strEncrypt.toUpperCase();
	}
	
	/** 使用指定的编码将字符串散列 
	 
	 @param encryptString 要散列的字符串 
	 @return 散列后的字符串 
*/
	public static String MD5_Encrypt(String encryptString)
	{
		/*Cryptography.MD5 md5 = new System.Security.Cryptography.MD5CryptoServiceProvider();
		byte[] source = md5.ComputeHash(Encoding.Default.GetBytes(encryptString));
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < source.length; i++)
		{
			sBuilder.append(String.format("%02X", source[i]));
		}*/
		MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = encryptString.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
		return hexValue.toString();
	}
}