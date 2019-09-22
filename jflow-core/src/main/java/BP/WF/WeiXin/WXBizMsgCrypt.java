package BP.WF.WeiXin;

import BP.Tools.*;
import BP.WF.*;
import java.util.*;

//using System.Web;
//-40001 ： 签名验证错误
//-40002 :  xml解析失败
//-40003 :  sha加密生成签名失败
//-40004 :  AESKey 非法
//-40005 :  corpid 校验错误
//-40006 :  AES 加密失败
//-40007 ： AES 解密失败
//-40008 ： 解密后得到的buffer非法
//-40009 :  base64加密异常
//-40010 :  base64解密异常


   public class WXBizMsgCrypt
   {
		private String m_sToken;
		private String m_sEncodingAESKey;
		private String m_sCorpID;
		private enum WXBizMsgCryptErrorCode
		{
			WXBizMsgCrypt_OK(0),
			WXBizMsgCrypt_ValidateSignature_Error(-40001),
			WXBizMsgCrypt_ParseXml_Error(-40002),
			WXBizMsgCrypt_ComputeSignature_Error(-40003),
			WXBizMsgCrypt_IllegalAesKey(-40004),
			WXBizMsgCrypt_ValidateCorpid_Error(-40005),
			WXBizMsgCrypt_EncryptAES_Error(-40006),
			WXBizMsgCrypt_DecryptAES_Error(-40007),
			WXBizMsgCrypt_IllegalBuffer(-40008),
			WXBizMsgCrypt_EncodeBase64_Error(-40009),
			WXBizMsgCrypt_DecodeBase64_Error(-40010);

			public static final int SIZE = java.lang.Integer.SIZE;

			private int intValue;
			private static java.util.HashMap<Integer, WXBizMsgCryptErrorCode> mappings;
			private static java.util.HashMap<Integer, WXBizMsgCryptErrorCode> getMappings()
			{
				if (mappings == null)
				{
					synchronized (WXBizMsgCryptErrorCode.class)
					{
						if (mappings == null)
						{
							mappings = new java.util.HashMap<Integer, WXBizMsgCryptErrorCode>();
						}
					}
				}
				return mappings;
			}

			private WXBizMsgCryptErrorCode(int value)
			{
				intValue = value;
				getMappings().put(value, this);
			}

			public int getValue()
			{
				return intValue;
			}

			public static WXBizMsgCryptErrorCode forValue(int value)
			{
				return getMappings().get(value);
			}
		}

		//构造函数
		// @param sToken: 公众平台上，开发者设置的Token
		// @param sEncodingAESKey: 公众平台上，开发者设置的EncodingAESKey
		// @param sCorpID: 企业号的CorpID
		public WXBizMsgCrypt(String sToken, String sEncodingAESKey, String sCorpID)
		{
			m_sToken = sToken;
			m_sCorpID = sCorpID;
			m_sEncodingAESKey = sEncodingAESKey;
		}

		//验证URL
		// @param sMsgSignature: 签名串，对应URL参数的msg_signature
		// @param sTimeStamp: 时间戳，对应URL参数的timestamp
		// @param sNonce: 随机串，对应URL参数的nonce
		// @param sEchoStr: 随机串，对应URL参数的echostr
		// @param sReplyEchoStr: 解密之后的echostr，当return返回0时有效
		// @return：成功0，失败返回对应的错误码
		public final int VerifyURL(String sMsgSignature, String sTimeStamp, String sNonce, String sEchoStr, tangible.RefObject<String> sReplyEchoStr)
		{
			int ret = 0;
			if (m_sEncodingAESKey.length() != 43)
			{
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_IllegalAesKey.getValue();
			}
			ret = VerifySignature(m_sToken, sTimeStamp, sNonce, sEchoStr, sMsgSignature);
			if (0 != ret)
			{
				return ret;
			}
			sReplyEchoStr.argValue = "";
			String cpid = "";
			try
			{
				tangible.RefObject<String> tempRef_cpid = new tangible.RefObject<String>(cpid);
				sReplyEchoStr.argValue = Cryptography.AES_decrypt(sEchoStr, m_sEncodingAESKey, tempRef_cpid); //m_sCorpID);
				cpid = tempRef_cpid.argValue;
			}
			catch (RuntimeException e)
			{
				sReplyEchoStr.argValue = "";
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_DecryptAES_Error.getValue();
			}
			if (!m_sCorpID.equals(cpid))
			{
				sReplyEchoStr.argValue = "";
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_ValidateCorpid_Error.getValue();
			}
			return 0;
		}

		// 检验消息的真实性，并且获取解密后的明文
		// @param sMsgSignature: 签名串，对应URL参数的msg_signature
		// @param sTimeStamp: 时间戳，对应URL参数的timestamp
		// @param sNonce: 随机串，对应URL参数的nonce
		// @param sPostData: 密文，对应POST请求的数据
		// @param sMsg: 解密后的原文，当return返回0时有效
		// @return: 成功0，失败返回对应的错误码
		public final int DecryptMsg(String sMsgSignature, String sTimeStamp, String sNonce, String sPostData, tangible.RefObject<String> sMsg)
		{
			if (m_sEncodingAESKey.length() != 43)
			{
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_IllegalAesKey.getValue();
			}
			XmlDocument doc = new XmlDocument();
			XmlNode root;
			String sEncryptMsg;
			try
			{
				doc.LoadXml(sPostData);
				root = doc.FirstChild;
				sEncryptMsg = root.get("Encrypt").InnerText;
			}
			catch (RuntimeException e)
			{
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_ParseXml_Error.getValue();
			}
			//verify signature
			int ret = 0;
			ret = VerifySignature(m_sToken, sTimeStamp, sNonce, sEncryptMsg, sMsgSignature);
			if (ret != 0)
			{
				return ret;
			}
			//decrypt
			String cpid = "";
			try
			{
				tangible.RefObject<String> tempRef_cpid = new tangible.RefObject<String>(cpid);
				sMsg.argValue = Cryptography.AES_decrypt(sEncryptMsg, m_sEncodingAESKey, tempRef_cpid);
				cpid = tempRef_cpid.argValue;
			}
			catch (NumberFormatException e2)
			{
				sMsg.argValue = "";
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_DecodeBase64_Error.getValue();
			}
			catch (RuntimeException e3)
			{
				sMsg.argValue = "";
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_DecryptAES_Error.getValue();
			}
			if (!m_sCorpID.equals(cpid))
			{
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_ValidateCorpid_Error.getValue();
			}
			return 0;
		}

		//将企业号回复用户的消息加密打包
		// @param sReplyMsg: 企业号待回复用户的消息，xml格式的字符串
		// @param sTimeStamp: 时间戳，可以自己生成，也可以用URL参数的timestamp
		// @param sNonce: 随机串，可以自己生成，也可以用URL参数的nonce
		// @param sEncryptMsg: 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce, encrypt的xml格式的字符串,
		//						当return返回0时有效
		// return：成功0，失败返回对应的错误码
		public final int EncryptMsg(String sReplyMsg, String sTimeStamp, String sNonce, tangible.RefObject<String> sEncryptMsg)
		{
			if (m_sEncodingAESKey.length() != 43)
			{
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_IllegalAesKey.getValue();
			}
			String raw = "";
			try
			{
				raw = Cryptography.AES_encrypt(sReplyMsg, m_sEncodingAESKey, m_sCorpID);
			}
			catch (RuntimeException e)
			{
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_EncryptAES_Error.getValue();
			}
			String MsgSigature = "";
			int ret = 0;
			tangible.RefObject<String> tempRef_MsgSigature = new tangible.RefObject<String>(MsgSigature);
			ret = GenarateSinature(m_sToken, sTimeStamp, sNonce, raw, tempRef_MsgSigature);
			MsgSigature = tempRef_MsgSigature.argValue;
			if (0 != ret)
			{
				return ret;
			}
			sEncryptMsg.argValue = "";

			String EncryptLabelHead = "<Encrypt><![CDATA[";
			String EncryptLabelTail = "]]></Encrypt>";
			String MsgSigLabelHead = "<MsgSignature><![CDATA[";
			String MsgSigLabelTail = "]]></MsgSignature>";
			String TimeStampLabelHead = "<TimeStamp><![CDATA[";
			String TimeStampLabelTail = "]]></TimeStamp>";
			String NonceLabelHead = "<Nonce><![CDATA[";
			String NonceLabelTail = "]]></Nonce>";
			sEncryptMsg.argValue = sEncryptMsg.argValue + "<xml>" + EncryptLabelHead + raw + EncryptLabelTail;
			sEncryptMsg.argValue = sEncryptMsg.argValue + MsgSigLabelHead + MsgSigature + MsgSigLabelTail;
			sEncryptMsg.argValue = sEncryptMsg.argValue + TimeStampLabelHead + sTimeStamp + TimeStampLabelTail;
			sEncryptMsg.argValue = sEncryptMsg.argValue + NonceLabelHead + sNonce + NonceLabelTail;
			sEncryptMsg.argValue += "</xml>";
			return 0;
		}

		public static class DictionarySort implements Comparator
		{
			public final int compare(Object oLeft, Object oRight)
			{
				String sLeft = oLeft instanceof String ? (String)oLeft : null;
				String sRight = oRight instanceof String ? (String)oRight : null;
				int iLeftLength = sLeft.length();
				int iRightLength = sRight.length();
				int index = 0;
				while (index < iLeftLength && index < iRightLength)
				{
					if (sLeft.charAt(index) < sRight.charAt(index))
					{
						return -1;
					}
					else if (sLeft.charAt(index) > sRight.charAt(index))
					{
						return 1;
					}
					else
					{
						index++;
					}
				}
				return iLeftLength - iRightLength;
			}
		}
		//Verify Signature
		private static int VerifySignature(String sToken, String sTimeStamp, String sNonce, String sMsgEncrypt, String sSigture)
		{
			String hash = "";
			int ret = 0;
			tangible.RefObject<String> tempRef_hash = new tangible.RefObject<String>(hash);
			ret = GenarateSinature(sToken, sTimeStamp, sNonce, sMsgEncrypt, tempRef_hash);
			hash = tempRef_hash.argValue;
			if (ret != 0)
			{
				return ret;
			}
			if (sSigture.equals(hash))
			{
				return 0;
			}
			else
			{
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_ValidateSignature_Error.getValue();
			}
		}

		public static int GenarateSinature(String sToken, String sTimeStamp, String sNonce, String sMsgEncrypt, tangible.RefObject<String> sMsgSignature)
		{
			ArrayList AL = new ArrayList();
			AL.add(sToken);
			AL.add(sTimeStamp);
			AL.add(sNonce);
			AL.add(sMsgEncrypt);
			Collections.sort(AL, new DictionarySort());
			String raw = "";
			for (int i = 0; i < AL.size(); ++i)
			{
				raw += AL.get(i);
			}

			SHA1 sha;
			ASCIIEncoding enc;
			String hash = "";
			try
			{
				sha = new SHA1CryptoServiceProvider();
				enc = new ASCIIEncoding();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] dataToHash = enc.GetBytes(raw);
				byte[] dataToHash = enc.GetBytes(raw);
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] dataHashed = sha.ComputeHash(dataToHash);
				byte[] dataHashed = sha.ComputeHash(dataToHash);
				hash = BitConverter.toString(dataHashed).replace("-", "");
				hash = hash.toLowerCase();
			}
			catch (RuntimeException e)
			{
				return WXBizMsgCryptErrorCode.WXBizMsgCrypt_ComputeSignature_Error.getValue();
			}
			sMsgSignature.argValue = hash;
			return 0;
		}
   }