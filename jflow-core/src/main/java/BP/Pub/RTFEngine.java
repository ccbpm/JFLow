package BP.Pub;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import BP.DA.Cash;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.Attr;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.Port.Emps;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBs;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Tools.ConvertTools;
import BP.Tools.StringHelper;
import BP.WF.Glo;

/**
 * WebRtfReport 的摘要说明。
 */
public class RTFEngine
{
	// 数据实体
	private Entities _HisEns = null;
	
	public final Entities getHisEns()
	{
		if (_HisEns == null)
		{
			_HisEns = new Emps();
		}
		
		return _HisEns;
	}
	
	// 数据实体
	
	// 数据明细实体
	
	public final String GetCode(String str)
	{
		if (StringHelper.isNullOrEmpty(str))
		{
			return "";
		}
		
		String rtn = "";
		byte[] rr = null;
		try
		{
			rr = str.getBytes("gb2312");
			// rr = str.getBytes("GB2312").toString().getBytes("us-ascii");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		for (byte b : rr)
		{
			if (b > 122)
			{
				rtn += "\\'" + String.format("%x", b);
			} else
			{
				rtn += (char) b;
			}
		}
		return rtn.replace("\n", " \\par ");
	}
	
	private java.util.ArrayList _EnsDataDtls = null;
	
	public final java.util.ArrayList getEnsDataDtls()
	{
		if (_EnsDataDtls == null)
		{
			_EnsDataDtls = new java.util.ArrayList();
		}
		return _EnsDataDtls;
	}
	
	 //多附件数据
    private Hashtable _EnsDataAths = null;
    public Hashtable getEnsDataAths()
    {
       
        if (_EnsDataAths == null)
            _EnsDataAths = new Hashtable();
        return _EnsDataAths;
        
    }
	
	// 数据明细实体
	
	/**
	 * 增加一个数据实体
	 * 
	 * @param en
	 */
	public final void AddEn(Entity en)
	{
		this.getHisEns().AddEntity(en);
	}
	
	/**
	 * 增加一个Ens
	 * 
	 * @param ens
	 */
	public final void AddDtlEns(Entities dtlEns)
	{
		this.getEnsDataDtls().add(dtlEns);
	}
	
	public String CyclostyleFilePath = "";
	public String TempFilePath = "";
	
	// 获取特殊要处理的流程节点信息.
	public final String GetValueByKeyOfCheckNode(String[] strs)
	{
		for (Object en : this.getHisEns())
		{
			
			String val = ((Entity) en).GetValStringByKey(strs[2]);
			switch (strs.length)
			{
				case 1:
				case 2:
					throw new RuntimeException("step1参数设置错误" + strs.toString());
				case 3: // S.9001002.Rec
					return val;
				case 4: // S.9001002.RDT.Year
					if (strs[3].equals("Text"))
					{
						if (val.equals("0"))
						{
							return "否";
						} else
						{
							return "是";
						}
					} else if (strs[3].equals("YesNo"))
					{
						if (val.equals("1"))
						{
							return "[√]";
						} else
						{
							return "[×]";
						}
					} else if (strs[3].equals("Year"))
					{
						return val.substring(0, 4);
					} else if (strs[3].equals("Month"))
					{
						return val.substring(5, 7);
					} else if (strs[3].equals("Day"))
					{
						return val.substring(8, 10);
					} else if (strs[3].equals("NYR"))
					{
						// return
						// BP.DA.DataType.ParseSysDate2DateTime(val).ToString("yyyy年MM月dd日");
					} else if (strs[3].equals("RMB"))
					{
						DecimalFormat fnum = new DecimalFormat("##0.00");
						return fnum.format(val);
					} else if (strs[3].equals("RMBDX"))
					{
						return BP.DA.DataType.ParseFloatToCash(Float
								.parseFloat(val));
					} else
					{
						throw new RuntimeException("step2参数设置错误" + strs);
					}
				default:
					throw new RuntimeException("step3参数设置错误" + strs);
			}
		}
		throw new RuntimeException("step4参数设置错误" + strs);
	}
	
	/**
	 * 图片转换
	 * 
	 * @param image_path
	 * @return
	 */
	public static String ImageTo16String(String image_path)
	{
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try
		{
			StringBuilder imgs = new StringBuilder();
			fis = new FileInputStream(image_path);
			bos = new ByteArrayOutputStream();
			
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = fis.read(buff)) != -1)
			{
				bos.write(buff, 0, len);
			}
			// 得到图片的字节数组
			byte[] result = bos.toByteArray();
			for (int i = 0; i < result.length; i++)
			{
				if ((i % 32) == 0)
				{
					imgs.append("\n");
				} else if ((i % 8) == 0)
				{
					imgs.append(" ");
				}
				byte num2 = result[i];
				int num3 = (num2 & 240) >> 4;
				int num4 = num2 & 15;
				imgs.append("0123456789abcdef".substring(num3, num3 + 1));
				imgs.append("0123456789abcdef".substring(num4, num4 + 1));
			}
			return imgs.toString();
			// 字节数组转成十六进制
		} catch (IOException e)
		{
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bos!=null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				
		}
		
		return "";
	}
	
	public static String GetImgHexString(Image img, Image ext)
	{
		// StringBuilder imgs = new StringBuilder();
		// MemoryStream stream = new MemoryStream();
		// img.Save(stream, ext);
		// stream.Close();
		//
		// byte[] buffer = stream.toArray();
		//
		// for (int i = 0; i < buffer.length; i++)
		// {
		// if ((i % 32) == 0)
		// {
		// imgs.append("\n");
		// }
		// else if ((i % 8) == 0)
		// {
		// imgs.append(" ");
		// }
		// byte num2 = buffer[i];
		// int num3 = (num2 & 240) >> 4;
		// int num4 = num2 & 15;
		// imgs.append("0123456789abcdef"[num3]);
		// imgs.append("0123456789abcdef"[num4]);
		// }
		return "";
	}
	
	public Entity HisGEEntity = null;
	
	/**
	 * 获取ICON图片的数据。
	 * 
	 * @param key
	 * @return
	 */
	public final String GetValueImgStrs(String key)
	{
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");
		String web_path = SystemConfig.getPathOfWebApp();
		// 说明是图片文件.
		String path = key.replace("OID.Img@AppPath",
				web_path.substring(0, web_path.length() - 1)).replace("\\\\",
				"\\");
		// 定义rtf中图片字符串
		StringBuilder pict = new StringBuilder();
		// 获取要插入的图片
		// Image img = Image.FromFile(path);
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(path));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// 将要插入的图片转换为16进制字符串
		String imgHexString;
		// key = key.toLowerCase();
		//
		// if (key.contains(".png"))
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Png);
		// }
		// else if (key.contains(".jp"))
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Jpeg);
		// }
		// else if (key.contains(".gif"))
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Gif);
		// }
		// else if (key.contains(".ico"))
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Icon);
		// }
		// else
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Jpeg);
		// }
		
		imgHexString = ImageTo16String(path);
		
		// 生成rtf中图片字符串
		pict.append("\n");
		pict.append("{\\pict");
		pict.append("\\jpegblip");
		pict.append("\\picscalex100");
		pict.append("\\picscaley100");
		pict.append("\\picwgoal" + image.getWidth() * 15);
		pict.append("\\pichgoal" + image.getHeight() * 15);
		pict.append(imgHexString + "}");
		pict.append("\n");
		return pict.toString();
	}
 
	
	/**
	 * 获取写字版的数据
	 * 
	 * @param key
	 * @return
	 */
	public final String GetValueBPPaintStrs(String key)
	{
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");
		
		String[] strs = key.split("[.]", -1);
		String filePath = "";
		try
		{
			filePath = DBAccess
					.RunSQLReturnString("SELECT Tag2 From Sys_FrmEleDB WHERE RefPKVal="
							+ this.HisGEEntity.getPKVal()
							+ " AND EleID='"
							+ strs[2].trim() + "'");
			if (filePath == null)
			{
				return "";
			}
		} catch (java.lang.Exception e)
		{
			return "";
		}
		
		// 定义rtf中图片字符串
		StringBuilder pict = new StringBuilder();
		// 获取要插入的图片
		// System.Drawing.Image img = System.Drawing.Image.FromFile(filePath);
		
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(filePath));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		// 将要插入的图片转换为16进制字符串
		String imgHexString;
		filePath = filePath.toLowerCase();
		
		imgHexString = ImageTo16String(filePath);
		
		// if (filePath.contains(".png"))
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Png);
		// }
		// else if (filePath.contains(".jp"))
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Jpeg);
		// }
		// else if (filePath.contains(".gif"))
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Gif);
		// }
		// else if (filePath.contains(".ico"))
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Icon);
		// }
		// else
		// {
		// imgHexString = GetImgHexString(img,
		// System.Drawing.Imaging.ImageFormat.Jpeg);
		// }
		
		// 生成rtf中图片字符串
		pict.append("\n");
		pict.append("{\\pict");
		pict.append("\\jpegblip");
		pict.append("\\picscalex100");
		pict.append("\\picscaley100");
		pict.append("\\picwgoal" + image.getWidth() * 15);
		pict.append("\\pichgoal" + image.getHeight() * 15);
		pict.append(imgHexString + "}");
		pict.append("\n");
		return pict.toString();
	}
	
	/**
	 * 获取类名+@+字段格式的数据. 比如： Demo_Inc@ABC Emp@Name
	 * 
	 * @param key
	 * @return
	 */
	public final String GetValueByAtKey(String key)
	{
		for (Entity en : Entities.convertEntities(this.getHisEns()))
		{
			String enKey = en.toString();
			
			// 有可能是 BP.Port.Emp
			if (enKey.contains("."))
			{
				enKey = en.getClass().getName();
			}
			
			// 如果不包含.
			if (key.contains(enKey + "@") == false)
			{
				continue;
			}
			
			// 如果不包含 . 就说明，不需要转意。
			if (key.contains(".") == false)
			{
				return en
						.GetValStringByKey(key.substring(key.indexOf('@') + 1));
			}
			
			// 把实体名去掉
			key = key.replace(enKey + "@", "");
			// 把数据破开.
			String[] strs = key.split("[.]", -1);
			if (strs.length == 2)
			{
				if (strs[1].trim().equals("ImgAth"))
				{
					String path1 = BP.Sys.SystemConfig.getPathOfDataUser()
							+ "\\ImgAth\\Data\\" + strs[0].trim() + "_"
							+ en.getPKVal() + ".png";
					// 定义rtf中图片字符串.
					StringBuilder mypict = new StringBuilder();
					// 获取要插入的图片
					// System.Drawing.Image imgAth =
					// System.Drawing.Image.FromFile(path1);
					BufferedImage image = null;
					try
					{
						image = ImageIO.read(new File(path1));
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					
					// 将要插入的图片转换为16进制字符串
					// String imgHexStringImgAth = GetImgHexString(imgAth,
					// System.Drawing.Imaging.ImageFormat.Jpeg);
					String imgHexStringImgAth = ImageTo16String(path1);
					// 生成rtf中图片字符串
					mypict.append("\n");
					mypict.append("{\\pict");
					mypict.append("\\jpegblip");
					mypict.append("\\picscalex100");
					mypict.append("\\picscaley100");
					mypict.append("\\picwgoal" + image.getWidth() * 15);
					mypict.append("\\pichgoal" + image.getHeight() * 15);
					mypict.append(imgHexStringImgAth + "}");
					mypict.append("\n");
					return mypict.toString();
				}
				
				String val = en.GetValStringByKey(strs[0].trim());
				if (strs[1].trim().equals("Text"))
				{
					if (val.equals("0"))
					{
						return "否";
					} else
					{
						return "是";
					}
				} else if (strs[1].trim().equals("Year"))
				{
					return val.substring(0, 4);
				} else if (strs[1].trim().equals("Month"))
				{
					return val.substring(5, 7);
				} else if (strs[1].trim().equals("Day"))
				{
					return val.substring(8, 10);
				} else if (strs[1].trim().equals("NYR"))
				{
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy年MM月dd日");
					return format.format(DataType.ParseSysDate2DateTime(val));
				} else if (strs[1].trim().equals("RMB"))
				{
					return new DecimalFormat("##0.00").format(Float
							.parseFloat(val));
				} else if (strs[1].trim().equals("RMBDX"))
				{
					return DataType.ParseFloatToCash(Float.parseFloat(val));
				} else if (strs[1].trim().equals("ImgAth"))
				{
					String path1 = BP.Sys.SystemConfig.getPathOfDataUser()
							+ "\\ImgAth\\Data\\" + strs[0].trim() + "_"
							+ this.HisGEEntity.getPKVal() + ".png";
					
					// 定义rtf中图片字符串.
					StringBuilder mypict = new StringBuilder();
					// 获取要插入的图片
					// System.Drawing.Image imgAth =
					// System.Drawing.Image.FromFile(path1);
					BufferedImage image = null;
					try
					{
						image = ImageIO.read(new File(path1));
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					
					// 将要插入的图片转换为16进制字符串
					// String imgHexStringImgAth = GetImgHexString(imgAth,
					// System.Drawing.Imaging.ImageFormat.Jpeg);
					String imgHexStringImgAth = ImageTo16String(path1);
					// 生成rtf中图片字符串
					mypict.append("\n");
					mypict.append("{\\pict");
					mypict.append("\\jpegblip");
					mypict.append("\\picscalex100");
					mypict.append("\\picscaley100");
					mypict.append("\\picwgoal" + image.getWidth() * 15);
					mypict.append("\\pichgoal" + image.getHeight() * 15);
					mypict.append(imgHexStringImgAth + "}");
					mypict.append("\n");
					return mypict.toString();
				} else if (strs[1].trim().equals("Siganture"))
				{
					String path = BP.Sys.SystemConfig.getPathOfDataUser()
							+ "\\Siganture\\" + val + ".jpg";
					// 定义rtf中图片字符串.
					StringBuilder pict = new StringBuilder();
					// 获取要插入的图片
					// System.Drawing.Image imgAth =
					// System.Drawing.Image.FromFile(path1);
					BufferedImage image = null;
					try
					{
						image = ImageIO.read(new File(path));
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					
					// 将要插入的图片转换为16进制字符串
					// String imgHexString = GetImgHexString(imgAth,
					// System.Drawing.Imaging.ImageFormat.Jpeg);
					String imgHexString = ImageTo16String(path);
					// 生成rtf中图片字符串
					pict.append("\n");
					pict.append("{\\pict");
					pict.append("\\jpegblip");
					pict.append("\\picscalex100");
					pict.append("\\picscaley100");
					pict.append("\\picwgoal" + image.getWidth() * 15);
					pict.append("\\pichgoal" + image.getHeight() * 15);
					pict.append(imgHexString + "}");
					pict.append("\n");
					return pict.toString();
					// 替换rtf模板文件中的签名图片标识为图片字符串
					// str = str.replace(imgMark, pict.ToString());
				} else
				{
					throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
				}
			}
		} // 实体循环。
		
		throw new RuntimeException("参数设置错误 GetValueByKey ：" + key);
		
	}
	
	/**
	 * 审核节点的表示方法是 节点ID.Attr.
	 * 
	 * @param key
	 * @return
	 */
	public final String GetValueByKey(String key)
	{
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");
		
		if (key.contains("@"))
		{
			return GetValueByAtKey(key);
		}
		
		String[] strs = key.split("[.]", -1);
		
		// 如果不包含 . 就说明他是从Rpt中取数据。
		if (this.HisGEEntity != null && key.contains("ND") == false)
		{
			if (strs.length == 1)
			{
				return this.HisGEEntity.GetValStringByKey(key);
			}
			
			if (strs[1].trim().equals("ImgAth"))
			{
				String path1 = BP.Sys.SystemConfig.getPathOfDataUser()
						+ "\\ImgAth\\Data\\" + strs[0].trim() + "_"
						+ this.HisGEEntity.getPKVal() + ".png";
				
				// 定义rtf中图片字符串.
				StringBuilder mypict = new StringBuilder();
				// 获取要插入的图片
				// System.Drawing.Image imgAth =
				// System.Drawing.Image.FromFile(path1);
				BufferedImage image = null;
				try
				{
					image = ImageIO.read(new File(path1));
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
				// 将要插入的图片转换为16进制字符串
				// String imgHexStringImgAth = GetImgHexString(imgAth,
				// System.Drawing.Imaging.ImageFormat.Jpeg);
				String imgHexStringImgAth = ImageTo16String(path1);
				// 生成rtf中图片字符串
				mypict.append("\n");
				mypict.append("{\\pict");
				mypict.append("\\jpegblip");
				mypict.append("\\picscalex100");
				mypict.append("\\picscaley100");
				mypict.append("\\picwgoal" + image.getWidth() * 15);
				mypict.append("\\pichgoal" + image.getHeight() * 15);
				mypict.append(imgHexStringImgAth + "}");
				mypict.append("\n");
				return mypict.toString();
			}
			
			if (strs[1].trim().equals("BPPaint"))
			{
				String path1 = DBAccess
						.RunSQLReturnString("SELECT  Tag2 FROM Sys_FrmEleDB WHERE REFPKVAL="
								+ this.HisGEEntity.getPKVal()
								+ " AND EleID='"
								+ strs[0].trim() + "'");
				// string path1 = BP.Sys.SystemConfig.getPathOfDataUser() +
				// "\\BPPaint\\" + this.HisGEEntity.ToString().Trim() + "\\" +
				// this.HisGEEntity.PKVal + ".png";
				// 定义rtf中图片字符串.
				StringBuilder mypict = new StringBuilder();
				// 获取要插入的图片
				// System.Drawing.Image myBPPaint =
				// System.Drawing.Image.FromFile(path1);
				BufferedImage image = null;
				try
				{
					image = ImageIO.read(new File(path1));
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				// 将要插入的图片转换为16进制字符串
				// String imgHexStringImgAth = GetImgHexString(myBPPaint,
				// System.Drawing.Imaging.ImageFormat.Jpeg);
				String imgHexStringImgAth = ImageTo16String(path1);
				// 生成rtf中图片字符串
				mypict.append("\n");
				mypict.append("{\\pict");
				mypict.append("\\jpegblip");
				mypict.append("\\picscalex100");
				mypict.append("\\picscaley100");
				mypict.append("\\picwgoal" + image.getWidth() * 15);
				mypict.append("\\pichgoal" + image.getHeight() * 15);
				mypict.append(imgHexStringImgAth + "}");
				mypict.append("\n");
				return mypict.toString();
			}
			
			if (strs.length == 2)
			{
				String val = this.HisGEEntity.GetValStringByKey(strs[0].trim());
				
				if (strs[1].trim().equals("Text"))
				{
					if (val.equals("0"))
					{
						return "否";
					} else
					{
						return "是";
					}
				} else if (strs[1].trim().equals("Year"))
				{
					return val.substring(0, 4);
				} else if (strs[1].trim().equals("Month"))
				{
					return val.substring(5, 7);
				} else if (strs[1].trim().equals("Day"))
				{
					return val.substring(8, 10);
				} else if (strs[1].trim().equals("NYR"))
				{
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy年MM月dd日");
					return dateFormat.format(DataType
							.ParseSysDate2DateTime(val));
					// return
					// DataType.ParseSysDate2DateTime(val).ToString("yyyy年MM月dd日");
				} else if (strs[1].trim().equals("RMB"))
				{
					return new DecimalFormat("##0.00").format(Float
							.parseFloat(val));
				} else if (strs[1].trim().equals("RMBDX"))
				{
					return DataType.ParseFloatToCash(Float.parseFloat(val));
				} else if (strs[1].trim().equals("Siganture"))
				{
					String path = BP.Sys.SystemConfig.getPathOfDataUser()
							+ "\\Siganture\\" + val + ".jpg";
					// 定义rtf中图片字符串
					StringBuilder pict = new StringBuilder();
					// 获取要插入的图片
					// System.Drawing.Image img =
					// System.Drawing.Image.FromFile(path);
					BufferedImage image = null;
					try
					{
						image = ImageIO.read(new File(path));
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					
					// 将要插入的图片转换为16进制字符串
					// String imgHexStringImgAth = GetImgHexString(imgAth,
					// System.Drawing.Imaging.ImageFormat.Jpeg);
					String imgHexStringImgAth = ImageTo16String(path);
					// 生成rtf中图片字符串
					pict.append("\n");
					pict.append("{\\pict");
					pict.append("\\jpegblip");
					pict.append("\\picscalex100");
					pict.append("\\picscaley100");
					pict.append("\\picwgoal" + image.getWidth() * 15);
					pict.append("\\pichgoal" + image.getHeight() * 15);
					pict.append(imgHexStringImgAth + "}");
					pict.append("\n");
					return pict.toString();
					// 替换rtf模板文件中的签名图片标识为图片字符串
					// str = str.replace(imgMark, pict.ToString());
				}else if (strs[1].trim().equals("BoolenText")){
					 if (val == "0")
                         return "否";
                     else
                         return "是";
					
				}else if (strs[1].trim().equals("Boolen")){
					 if (val == "1")
                         return "[√]";
                     else
                         return "[×]";
				}
				else if (strs[1].trim().equals("YesNo"))
				{
					if (val.equals("1"))
					{
						return "[√]";
					} else
					{
						return "[×]";
					}
				} else if (strs[1].trim().equals("Yes"))
				{
					if (val.equals("0"))
					{
						return "[×]";
					} else
					{
						return "[√]";
					}
				} else if (strs[1].trim().equals("No"))
				{
					if (val.equals("0"))
					{
						return "[√]";
					} else
					{
						return "[×]";
					}
				} else
				{
					throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
				}
			} else
			{
				throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
			}
		}
		
		throw new RuntimeException("参数设置错误 GetValueByKey ：" + key);
	}
	
	// /#region 生成单据
	/**
	 * 生成单据
	 * 
	 * @param cfile
	 *            模板文件
	 * @throws Exception 
	 */
	public final void MakeDoc(String cfile, String replaceVal) throws Exception
	{
		String file = PubClass.GenerTempFileName("doc");
		this.MakeDoc(cfile, SystemConfig.getPathOfTemp(), file, replaceVal,
				true);
	}
	
	public String ensStrs = "";
	
	/**
	 * 轨迹表（用于输出打印审核轨迹,审核信息.）
	 */
	public DataTable dtTrack = null;
	
	/**
	 * 单据生成
	 * 
	 * @param cfile
	 *            模板文件
	 * @param path
	 *            生成路径
	 * @param file
	 *            生成文件
	 * @param isOpen
	 *            是否用IE打开？
	 */
	public final void MakeDoc(String cfile, String path, String file,
			String replaceVals, boolean isOpen)
	{
		cfile = cfile.replace(".rtf.rtf", ".rtf");
        
        if (new File(path).exists() == false)
        	new File(path).mkdirs();
        
		StringBuilder str = new StringBuilder(Cash.GetBillStr(cfile, false)
				.substring(0));
		if (this.getHisEns().size() == 0)
		{
			if (this.HisGEEntity == null)
			{
				throw new RuntimeException("@您没有为报表设置数据源...");
			}
		}
		
		this.ensStrs = "";
		if (this.getHisEns().size() != 0)
		{
			for (Entity en : Entities.convertEntities(this.getHisEns()))
			{
				ensStrs += en.toString();
			}
		} else
		{
			ensStrs = this.HisGEEntity.toString();
		}
		
		String error = "";
		String[] paras = null;
		if (this.HisGEEntity != null)
		{
			paras = Cash.GetBillParas(cfile, ensStrs, this.HisGEEntity);
		} else
		{
			paras = Cash.GetBillParas(cfile, ensStrs, this.getHisEns());
		}
		
		this.TempFilePath = path + file;
		try
		{
			String key = "";
			String ss = "";
			
			// 替换主表标记
			for (String para : paras)
			{
				if (para == null || para.equals(""))
				{
					continue;
				}
				try
				{
					if (para.contains("ImgAth"))
					{
						str = new StringBuilder(str.toString().replace(
								"<" + para + ">", this.GetValueByKey(para)));
					} else if (para.contains("Siganture"))
					{
						str = new StringBuilder(str.toString().replace(
								"<" + para + ">", this.GetValueByKey(para)));
					} else if (para.contains("Img@AppPath"))
					{
						str = new StringBuilder(str.toString().replace(
								"<" + para + ">", this.GetValueImgStrs(para)));
					} else if (para.contains(".BPPaint"))
					{
						str = new StringBuilder(str.toString().replace(
								"<" + para + ">",
								this.GetValueBPPaintStrs(para)));
					}  else if (para.contains(".RMB"))
					{
						str = new StringBuilder(str.toString().replace(
								"<" + para + ">", this.GetValueByKey(para)));
					} else if (para.contains(".RMBDX"))
					{
						str = new StringBuilder(str.toString().replace(
								"<" + para + ">", this.GetValueByKey(para)));
					}else if (para.contains(".Boolen"))
					{
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					}
					else if (para.contains(".BoolenText"))
					{
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					} else if (para.contains(".NYR"))
					{
						str = new StringBuilder(str.toString().replace(
								"<" + para + ">",
								this.GetValueByKey(para)));
					} else if (para.contains(".Year"))
					{
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					}
					else if (para.contains(".Month"))
					{
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					}
					else if (para.contains(".Day"))
					{
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					}
					else if (para.contains(".Yes") == true)
					{
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetCode(this.GetValueByKey(para))));
					}
					else if (para.contains("-EnumYes") == true)
					{
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetCode(this.GetValueByKey(para))));
					}else if (para.contains(".") == true)
					{
						continue; // 有可能是明细表数据.
					} else
					{
						str = new StringBuilder(str.toString().replace(
								"<" + para + ">",
								this.GetValueByKey(para)));
					}
				} catch (RuntimeException ex)
				{
					error += "替换主表标记取参数["
							+ para
							+ "]出现错误：有以下情况导致此错误;1你用Text取值时间，此属性不是外键。2,类无此属性。3,该字段是明细表字段但是丢失了明细表标记.<br>更详细的信息：<br>"
							+ ex.getMessage();
					Log.DebugWriteError("MakeDoc"+error);
					if (SystemConfig.getIsDebug())
					{
						throw new RuntimeException(error);
					}
				}
			}
			// 替换主表标记
			
			// 从表
			String shortName = "";
			ArrayList<Entities> al = this.getEnsDataDtls();
			for (Entities dtls : al)
			{
				Entity dtl = dtls.getGetNewEntity();
				String dtlEnName = dtl.toString();
				shortName = dtlEnName.substring(dtlEnName.lastIndexOf(".") + 1);
				
				if (str.toString().indexOf(shortName) == -1)
				{
					continue;
				}
				
				int pos_rowKey = str.indexOf(shortName);
				int  end_rowKey = str.lastIndexOf(shortName);
				int row_start = -1, row_end = -1;
				if (pos_rowKey != -1)
				{
					row_start = str.substring(0, pos_rowKey).lastIndexOf(
							"\\row");
					//获取从表表名出现的最后的位置
					//int end_rowKey = str.lastIndexOf(shortName);
					//获取row的位置 
					row_end = str.substring(end_rowKey).indexOf("\\row");
				}
				
				if (row_start != -1 && row_end != -1)
				{
					String row = str.substring(row_start, (end_rowKey)
							+ row_end);
					str = new StringBuilder(str.toString().replace(row, ""));
					
					Map map = dtls.getGetNewEntity().getEnMap();
					int i = dtls.size();
					while (i > 0)
					{
						i--;
						Object tempVar = row;
						String rowData = (String) ((tempVar instanceof String) ? tempVar
								: null);
						dtl = dtls.getItem(i);
						for (Attr attr : map.getAttrs())
						{
							if (!attr.getUIVisible())
							{
								continue;
							}
							switch (attr.getMyDataType())
							{
								case DataType.AppDouble:
								case DataType.AppFloat:
								case DataType.AppRate:
									rowData = rowData.replace("<" + shortName
											+ "." + attr.getKey() + ">", dtl
											.GetValStringByKey(attr.getKey()));
									break;
								case DataType.AppMoney:
									rowData = rowData.replace("<" + shortName
											+ "." + attr.getKey() + ">", dtl
											.GetValDecimalByKey(attr.getKey(),2)
											.toString());
									break;
								case DataType.AppInt:
									
									if (attr.getMyDataType() == DataType.AppBoolean)
									{
										rowData = rowData.replace(
												"<" + shortName + "."
														+ attr.getKey() + ">",
												dtl.GetValStrByKey(attr
														.getKey()));
										int v = dtl.GetValIntByKey(attr
												.getKey());
										if (v == 1)
										{
											rowData = rowData.replace(
													"<" + shortName + "."
															+ attr.getKey()
															+ "Text>", "是");
										} else
										{
											rowData = rowData.replace(
													"<" + shortName + "."
															+ attr.getKey()
															+ "Text>", "否");
										}
									} else
									{
										if (attr.getIsEnum())
										{
											rowData = rowData
													.replace(
															"<"
																	+ shortName
																	+ "."
																	+ attr.getKey()
																	+ "Text>",
															dtl.GetValRefTextByKey(attr.getKey()));
										} else
										{
											rowData = rowData.replace(
													"<" + shortName + "."
															+ attr.getKey()
															+ ">",
													dtl.GetValStrByKey(attr
															.getKey()));
										}
									}
									break;
								default:
									rowData = rowData.replace("<" + shortName
											+ "." + attr.getKey() + ">",
											dtl.GetValStrByKey(attr
													.getKey()));
									break;
							}
						}
						
						str = str.insert(row_start, rowData);
					}
				}
			}
			// 从表
			
			// 明细 合计信息。
			al = this.getEnsDataDtls();
			for (Entities dtls : al)
			{
				Entity dtl = dtls.getGetNewEntity();
				String dtlEnName = dtl.toString();
				shortName = dtlEnName.substring(dtlEnName.lastIndexOf(".") + 1);
				// shortName =
				// dtls.ToString().substing(dtls.ToString().LastIndexOf(".") +
				// 1);
				Map map = dtl.getEnMap();
				for (Attr attr : map.getAttrs())
				{
					switch (attr.getMyDataType())
					{
						case DataType.AppDouble:
						case DataType.AppFloat:
						case DataType.AppMoney:
						case DataType.AppRate:
							key = "<" + shortName + "." + attr.getKey()
									+ ".SUM>";
							if (str.indexOf(key) != -1)
							{
								str = new StringBuilder(str.toString().replace(
										key,
										(new Float(dtls.GetSumFloatByKey(attr
												.getKey()))).toString()));
							}
							
							key = "<" + shortName + "." + attr.getKey()
									+ ".SUM.RMB>";
							if (str.indexOf(key) != -1)
							{
								String value = new DecimalFormat("##0.00")
										.format(new Float(
												dtls.GetSumFloatByKey(attr
														.getKey())));
								str = new StringBuilder(str.toString().replace(
										key, value));
								// str = str.replace(key, (new
								// Float(dtls.GetSumFloatByKey(attr.getKey()))).ToString("0.00"));
							}
							
							key = "<" + shortName + "." + attr.getKey()
									+ ".SUM.RMBDX>";
							if (str.indexOf(key) != -1)
							{
								str = new StringBuilder(
										str.toString()
												.replace(
														key,
														GetCode(DataType
																.ParseFloatToCash(dtls
																		.GetSumFloatByKey(attr
																				.getKey())))));
							}
							break;
						case DataType.AppInt:
							key = "<" + shortName + "." + attr.getKey()
									+ ".SUM>";
							if (str.indexOf(key) != -1)
							{
								str = new StringBuilder(str.toString().replace(
										key,
										(new Integer(dtls.GetSumIntByKey(attr
												.getKey()))).toString()));
							}
							break;
						default:
							break;
					}
				}
			}
			// 从表合计
			//审核组件组合信息
			
			//根据track表获取审核的节点
			//节点单个审核人
		   if (dtTrack != null && str.toString().contains("<WorkCheckBegin>")== false && str.toString().contains("<WorkCheckEnd>") ==false){
				for(DataRow row : dtTrack.Rows) //此处的22是ActionType.WorkCheck的值，此枚举位于BP.WF项目中，此处暂写死此值
	            {
	                int acType = Integer.parseInt(row.getValue("ActionType").toString());
	                if (acType != 22)
	                    continue;
	                str = new StringBuilder(str.toString().replace(
							"<WorkCheck.Msg." + row.getValue("NDFrom") + ">", this.GetValueCheckWorkByKey(row, "Msg")));
	                str = new StringBuilder(str.toString().replace(
							"<WorkCheck.Rec." + row.getValue("NDFrom") + ">", this.GetValueCheckWorkByKey(row, "EmpFromT")));
	                str = new StringBuilder(str.toString().replace(
							"<WorkCheck.RDT." + row.getValue("NDFrom") + ">",this.GetValueCheckWorkByKey(row, "RDT")));
	                
	                
	            }
		   }

            
             //多附件
           
             for(Object athObjEnsName : this.getEnsDataAths().keySet())
             {
                 String athName = "Ath." + athObjEnsName.toString();
                 String athFilesName = "";
                 if (str.indexOf(athName) == -1)
                     continue;

                 FrmAttachmentDBs athDbs = (FrmAttachmentDBs) this.getEnsDataAths().get(athObjEnsName);
                 if (athDbs == null)
                     continue;
                 for(FrmAttachmentDB athDb : athDbs.ToJavaList())
                 {
                     if (athFilesName.length() > 0)
                         athFilesName += " ， ";

                     athFilesName += athDb.getFileName();
                 }
                 str =new StringBuilder(str.toString().replace(
                		 "<" + athName + ">",
                		 this.GetCode(athFilesName))); 
             }
             
			// 要替换的字段
			if (replaceVals != null && replaceVals.contains("@"))
			{
				String[] vals = replaceVals.split("[@]", -1);
				for (String val : vals)
				{
					if (val == null || val.equals(""))
					{
						continue;
					}
					
					if (val.contains("=") == false)
					{
						continue;
					}
					
					Object tempVar2 = val;
					String myRep = (String) ((tempVar2 instanceof String) ? tempVar2
							: null);
					
					myRep = myRep.trim();
					myRep = myRep.replace("null", "");
					String[] myvals = myRep.split("[=]", -1);
					str = new StringBuilder(str.toString().replace(
							"<" + myvals[0] + ">", "<" + myvals[1] + ">"));
				}
			}
			//
			str = new StringBuilder(str.toString().replace("<", ""));
			str = new StringBuilder(str.toString().replace(">", ""));
			try
			{
				ConvertTools.streamWriteConvertGBK(str.toString(), TempFilePath);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
			
			
		
		} catch (RuntimeException ex)
		{
			String msg = "";
			if (SystemConfig.getIsDebug())
			{ // 异常可能与单据的配置有关系。
				try
				{
					this.CyclostyleFilePath = SystemConfig.getPathOfDataUser()
							+ "/CyclostyleFile/" + cfile;
					str = new StringBuilder(Cash.GetBillStr(cfile, false));
					String s = RepBill.RepairBill(this.CyclostyleFilePath);
					msg = "@已经成功的执行修复线  RepairLineV2，您重新发送一次或者，退后重新在发送一次，是否可以解决此问题。@"
							+ s;
				} catch (RuntimeException ex1)
				{
					msg = "执行修复线失败.  RepairLineV2 " + ex1.getMessage();
				}
			}
			throw new RuntimeException("生成文档失败：单据名称[" + this.CyclostyleFilePath
					+ "] 异常信息：" + ex.getMessage() + " @自动修复单据信息：" + msg);
		}
		if (isOpen)
		{
			// PubClass.Print(BP.Sys.Glo.getRequest().ApplicationPath + "Temp/"
			// + file);
			PubClass.Print(Glo.getCCFlowAppPath() + "Temp/" + file);
		}
	}
	
	 private String GetValueCheckWorkByKey(DataRow row, String key)
     {
         key = key.replace(" ", "");
         key = key.replace("\r\n", "");

         switch (key)
         {
             case "RDT":
                 return row.getValue("RDT").toString(); //审核日期.
             case "RDT-NYR":
                 String rdt = row.getValue("RDT").toString(); //审核日期.
                 return BP.DA.DataType.ParseSysDate2DateTimeFriendly(rdt);
             case "Rec":
                 return row.getValue("EmpFrom").toString(); //记录人.
             case "RecName":
                 return row.getValue("EmpFromT").toString(); //审核人.
             case "Msg":
             case "Note":
                 return row.getValue("Msg").toString();
             default:
                 return row.getValue(key).toString();
         }
     }
	// 生成单据
	/**
	 * 生成单据根据
	 * 
	 * @param templeteFile
	 *            模板文件
	 * @param saveToFile
	 * @param mainDT
	 * @param dtls
	 */
	@Deprecated
	public final void MakeDocByDataSet(String templeteFile, String saveToPath,
			String saveToFileName, DataTable mainDT, DataSet dtlsDS)
	{
		
	}
	
	// 方法
	/**
	 * RTFEngine
	 */
	public RTFEngine()
	{
		this._EnsDataDtls = null;
		this._HisEns = null;
	}
	/**
	 * 修复线
	 * 
	 * @param line
	 * @return
	 */
}