package BP.GPM;

import java.security.MessageDigest;

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

	//SHA1 加密 （HASH算法没有解密）
	/** 
	 利用SHA1加密一个字符串
	 
	*/
	public static String SHA1_Encrypt(String encryptString)
	{
		if(encryptString==null||encryptString.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(encryptString.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];      
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
	}
}