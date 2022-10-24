package bp.pub;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.Attr;
import bp.en.Entities;
import bp.en.Entity;
import bp.en.Map;
import bp.port.Emps;
import bp.sys.FrmAttachmentDB;
import bp.sys.FrmAttachmentDBs;
import bp.tools.ConvertTools;
import bp.tools.StringHelper;
import bp.wf.ActionType;
import bp.wf.GenerWorkerLists;
import bp.wf.httphandler.WF_WorkOpt_OneWork;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * WebRtfReport 的摘要说明。
 */
public class RTFEngine {
	// 数据实体
	private Entities _HisEns = null;

	public final Entities getHisEns()  {
		if (_HisEns == null) {
			_HisEns = new Emps();
		}

		return _HisEns;
	}

	// 数据实体

	// 数据明细实体

	public final String GetCode(String str) {
		if (StringHelper.isNullOrEmpty(str)) {
			return "";
		}

		String rtn = "";
		byte[] rr = null;
		try {
			rr = str.getBytes("gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for (byte b : rr) {
			if (b > 122) {
				rtn += "\\'" + String.format("%x", b);
			} else {
				rtn += (char) b;
			}
		}
		return rtn.replace("\n", " \\par ");
	}

	private java.util.ArrayList _EnsDataDtls = null;

	public final java.util.ArrayList getEnsDataDtls() {
		if (_EnsDataDtls == null) {
			_EnsDataDtls = new java.util.ArrayList();
		}
		return _EnsDataDtls;
	}

	// 多附件数据
	private Hashtable _EnsDataAths = null;

	public Hashtable getEnsDataAths() {

		if (_EnsDataAths == null)
			_EnsDataAths = new Hashtable();
		return _EnsDataAths;

	}

	// 数据明细实体

	/**
	 * 增加一个数据实体
	 *
	 * param en
	 */
	public final void AddEn(Entity en) throws Exception{
		this.getHisEns().AddEntity(en);
	}

	/**
	 * 增加一个Ens
	 *
	 * param dtlEns
	 */
	public final void AddDtlEns(Entities dtlEns) {
		this.getEnsDataDtls().add(dtlEns);
	}

	public String CyclostyleFilePath = "";
	public String TempFilePath = "";

	// 获取特殊要处理的流程节点信息.
	public final String GetValueByKeyOfCheckNode(String[] strs) throws Exception {
		for (Object en : this.getHisEns()) {

			String val = ((Entity) en).GetValStringByKey(strs[2]);
			switch (strs.length) {
				case 1:
				case 2:
					throw new RuntimeException("step1参数设置错误" + strs.toString());
				case 3: // S.9001002.Rec
					return val;
				case 4: // S.9001002.RDT.Year
					if (strs[3].equals("Text")) {
						if (val.equals("0")) {
							return "否";
						} else {
							return "是";
						}
					} else if (strs[3].equals("YesNo")) {
						if (val.equals("1")) {
							return "[√]";
						} else {
							return "[×]";
						}
					} else if (strs[3].equals("Year")) {
						return val.substring(0, 4);
					} else if (strs[3].equals("Month")) {
						return val.substring(5, 7);
					} else if (strs[3].equals("Day")) {
						return val.substring(8, 10);
					} else if (strs[3].equals("NYR")) {
						// return
						// bp.da.DataType.ParseSysDate2DateTime(val).ToString("yyyy年MM月dd日");
					} else if (strs[3].equals("RMB")) {
						DecimalFormat fnum = new DecimalFormat("##0.00");
						return fnum.format(val);
					} else if (strs[3].equals("RMBDX")) {
						return bp.da.DataType.ParseFloatToCash(Float.parseFloat(val));
					} else {
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
	 * param image_path
	 * @return
	 * @throws IOException
	 */
	public static String ImageTo16String(String image_path){
		/*try{
			FileInputStream fis = new FileInputStream(image_path);
			BufferedInputStream bis = new BufferedInputStream(fis);
			java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();

			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = fis.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
			// 得到图片的字节数组
			byte[] result = bos.toByteArray();

			System.out.println(byte2HexStr(result));
			// 字节数组转成十六进制
			String str = byte2HexStr(result);
			return str;
		}catch(Exception e){
			e.printStackTrace();
		}*/



		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			StringBuilder imgs = new StringBuilder();
			fis = new FileInputStream(image_path);
			bos = new ByteArrayOutputStream();

			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = fis.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
			// 得到图片的字节数组
			byte[] result = bos.toByteArray();
			for (int i = 0; i < result.length; i++) {
				if ((i % 32) == 0) {
					imgs.append("\n");
				} else if ((i % 8) == 0) {
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
		} catch (IOException e) {
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return "";
	}

	/*
	 * 实现字节数组向十六进制的转换方法一
	 */
	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	@SuppressWarnings("unused")
	private static byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}

	/*
	 * 实现字节数组向十六进制的转换的方法二
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();

	}


	public static String GetImgHexString(Image img, Image ext) {

		return "";
	}

	public Entity HisGEEntity = null;

	/**
	 * 获取ICON图片的数据。
	 *
	 * param key
	 * @return
	 */
	public final String GetValueImgStrs(String key) throws Exception {
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");
		String web_path = SystemConfig.getPathOfWebApp();
		// 说明是图片文件.
		String path = key.replace("OID.Img@AppPath", web_path.substring(0, web_path.length() - 1)).replace("\\\\",
				"\\");
		// 定义rtf中图片字符串
		StringBuilder pict = new StringBuilder();
		// 获取要插入的图片
		// Image img = Image.FromFile(path);
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 将要插入的图片转换为16进制字符串
		String imgHexString;


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

	/// <summary>
	/// 输入轨迹表.
	/// </summary>
	/// <returns></returns>
	public StringBuilder GetFlowTrackTable(StringBuilder str) throws Exception {
		WF_WorkOpt_OneWork oneWork = new WF_WorkOpt_OneWork();
		DataTable dt = null;
		GenerWorkerLists gwls = null;
		try {
			//获取轨迹信息
			dt=oneWork.getTimeBase();
			// 获取人员信息
			gwls = oneWork.getGwf();

			String shortName = "Track";

			int pos_rowKey = str.indexOf(shortName);
			int end_rowKey = str.lastIndexOf(shortName);
			int row_start = -1, row_end = -1;
			if (pos_rowKey != -1) {
				row_start = str.substring(0, pos_rowKey).lastIndexOf("\\row");
				// 获取从表表名出现的最后的位置
				// int end_rowKey = str.lastIndexOf(shortName);
				// 获取row的位置
				row_end = str.substring(end_rowKey).indexOf("\\row");
			}

			if (row_start != -1 && row_end != -1) {
				String row = str.substring(row_start, (end_rowKey) + row_end);
				str = new StringBuilder(str.toString().replace(row, ""));

				int i = dt.Rows.size();
				//增加等待审核的人员, 在所有的人员循环以后.
				Entity gwl = new GenerWorkerLists().getGetNewEntity();
				for (int ii = 0; ii < gwls.size(); ii++) {

					gwl = gwls.get(ii);
					if (gwl.GetValStringByKey("IsPass").equals("1"))
						continue;

					Object tempVar = row;
					String rowData = (String) ((tempVar instanceof String) ? tempVar : null);
					//替换序号
					rowData = rowData.replace("<" + shortName + "." +"Idx>", i+1+"");
					rowData = rowData.replace("<" + shortName + "." +"NDFromT>", gwl.GetValStringByKey("FK_NodeText").toString());

					if (gwl.GetValStringByKey("IsRead").equals("1")) {
						rowData = rowData.replace("<" + shortName + "." +"Action>", "已阅读");
					} else {
						rowData = rowData.replace("<" + shortName + "." +"Action>", "尚未阅读");
					}
					rowData = rowData.replace("<" + shortName + "." +"ActionTypeText>", "等待审批");
					rowData = rowData.replace("<" + shortName + "." +"EmpFromT>", gwl.GetValStringByKey("FK_EmpText").toString());
					rowData = rowData.replace("<" + shortName + "." +"StartTime>", gwl.GetValStringByKey("RDT").toString());
					rowData = rowData.replace("<" + shortName + "." +"EndTime>", "");
					rowData = rowData.replace("<" + shortName + "." +"PassTime>", "");
					str = str.insert(row_start, rowData);
				}

				//增加已审核人员
				while (i > 0)
				{
					i--;
					DataRow dr = dt.Rows.get(i);
					if (String.valueOf(ActionType.FlowBBS.getValue()).equals(dr.getValue("actiontype").toString()))
						continue;
					if (String.valueOf(ActionType.WorkCheck.getValue()).equals(dr.getValue("actiontype").toString()))
						continue;
					Object tempVar = row;
					String rowData = (String) ((tempVar instanceof String) ? tempVar : null);
					//替换序号
					rowData = rowData.replace("<" + shortName + "." +"Idx>", String.valueOf(i + 1));
					rowData = rowData.replace("<" + shortName + "." +"NDFromT>", dr.getValue("ndfromt").toString());
					rowData = rowData.replace("<" + shortName + "." +"Action>", "已处理");
					rowData = rowData.replace("<" + shortName + "." +"ActionTypeText>", dr.getValue("actiontypetext").toString());
					rowData = rowData.replace("<" + shortName + "." +"EmpFromT>", dr.getValue("empfromt").toString());
					String startTime = "";
					String endTime = "";
					String passTime = "";
					//获取轨迹中上一个节点的时间
					if (i == 0) {
						startTime = dr.getValue("rdt").toString();
						endTime = dr.getValue("rdt").toString();
					} else {
						//上一节点的到达时间就是本节点的开始时间
						startTime= dt.Rows.get(i - 1).get("rdt").toString();
						endTime = dt.Rows.get(i).get("rdt").toString();
					}
					//求得历时时间差
					SimpleDateFormat sf = new SimpleDateFormat(DataType.getSysDateTimessFormat());
					passTime = DataType.getDatePoor(sf.parse(endTime), sf.parse(startTime));

					rowData = rowData.replace("<" + shortName + "." +"StartTime>", startTime);
					rowData = rowData.replace("<" + shortName + "." +"EndTime>", endTime);
					rowData = rowData.replace("<" + shortName + "." +"PassTime>", passTime);

					str = str.insert(row_start, rowData);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  str;
	}
	/**
	 * 获取写字版的数据
	 *
	 * param key
	 * @return
	 */
	public final String GetValueBPPaintStrs(String key) {
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");

		String[] strs = key.split("[.]", -1);
		String filePath = "";
		try {
			filePath = DBAccess.RunSQLReturnString("SELECT Tag2 From Sys_FrmEleDB WHERE RefPKVal="
					+ this.HisGEEntity.getPKVal() + " AND EleID='" + strs[2].trim() + "'");
			if (filePath == null) {
				return "";
			}
		} catch (java.lang.Exception e) {
			return "";
		}

		// 定义rtf中图片字符串
		StringBuilder pict = new StringBuilder();
		// 获取要插入的图片
		// System.Drawing.Image img = System.Drawing.Image.FromFile(filePath);

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(filePath));
		} catch (IOException e) {
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
	 * param key
	 * @return
	 * @throws Exception
	 */
	public final String GetValueByAtKey(String key) throws Exception {
		for (Entity en : Entities.convertEntities(this.getHisEns())) {
			String enKey = en.toString();

			// 有可能是 bp.port.Emp
			if (enKey.contains(".")) {
				enKey = en.getClass().getName();
			}

			// 如果不包含.
			if (key.toString().contains(enKey + "@") == false) {
				continue;
			}

			// 如果不包含 . 就说明，不需要转意。
			if (key.toString().contains(".") == false) {
				return en.GetValStringByKey(key.substring(key.toString().indexOf('@') + 1));
			}

			// 把实体名去掉
			key = key.replace(enKey + "@", "");
			// 把数据破开.
			String[] strs = key.split("[.]", -1);
			if (strs.length == 2) {
				if (strs[1].trim().equals("ImgAth")) {
					String path1 = SystemConfig.getPathOfDataUser() + "\\ImgAth\\Data\\" + strs[0].trim() + "_"
							+ en.getPKVal() + ".png";
					// 定义rtf中图片字符串.
					StringBuilder mypict = new StringBuilder();
					// 获取要插入的图片
					// System.Drawing.Image imgAth =
					// System.Drawing.Image.FromFile(path1);
					BufferedImage image = null;
					try {
						image = ImageIO.read(new File(path1));
					} catch (IOException e) {
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
				if (strs[1].trim().equals("Text")) {
					if (val.equals("0")) {
						return "否";
					} else {
						return "是";
					}
				} else if (strs[1].trim().equals("Year")) {
					return val.substring(0, 4);
				} else if (strs[1].trim().equals("Month")) {
					return val.substring(5, 7);
				} else if (strs[1].trim().equals("Day")) {
					return val.substring(8, 10);
				} else if (strs[1].trim().equals("NYR")) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
					return format.format(DataType.ParseSysDate2DateTime(val));
				} else if (strs[1].trim().equals("RMB")) {
					return new DecimalFormat("##0.00").format(Float.parseFloat(val));
				} else if (strs[1].trim().equals("RMBDX")) {
					return DataType.ParseFloatToCash(Float.parseFloat(val));
				} else if (strs[1].trim().equals("ImgAth")) {
					String path1 = SystemConfig.getPathOfDataUser() + "\\ImgAth\\Data\\" + strs[0].trim() + "_"
							+ this.HisGEEntity.getPKVal() + ".png";

					// 定义rtf中图片字符串.
					StringBuilder mypict = new StringBuilder();
					// 获取要插入的图片
					// System.Drawing.Image imgAth =
					// System.Drawing.Image.FromFile(path1);
					BufferedImage image = null;
					try {
						image = ImageIO.read(new File(path1));
					} catch (IOException e) {
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
				} else if (strs[1].trim().equals("Siganture")) {
					String path = SystemConfig.getPathOfDataUser() + "\\Siganture\\" + val + ".jpg";
					// 定义rtf中图片字符串.
					StringBuilder pict = new StringBuilder();
					// 获取要插入的图片
					// System.Drawing.Image imgAth =
					// System.Drawing.Image.FromFile(path1);
					BufferedImage image = null;
					try {
						image = ImageIO.read(new File(path));
					} catch (IOException e) {
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
				} else {
					throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
				}
			}
		} // 实体循环。

		throw new RuntimeException("参数设置错误 GetValueByKey ：" + key);

	}

	/**
	 * 审核节点的表示方法是 节点ID.Attr.
	 *
	 * param key
	 * @return
	 * @throws Exception
	 */
	public final String GetValueByKey(String key) throws Exception {
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");

		if (key.toString().contains("@")) {
			return GetValueByAtKey(key);
		}

		String[] strs = key.split("[.]", -1);

		// 如果不包含 . 就说明他是从Rpt中取数据。
		if (this.HisGEEntity != null && key.toString().contains("ND") == false) {
			if (strs.length == 1) {
				return this.HisGEEntity.GetValStringByKey(key);
			}

			if (strs[1].trim().equals("ImgAth")) {
				String path1 = SystemConfig.getPathOfDataUser() + "\\ImgAth\\Data\\" + strs[0].trim() + "_"
						+ this.HisGEEntity.getPKVal() + ".png";

				// 定义rtf中图片字符串.
				StringBuilder mypict = new StringBuilder();
				// 获取要插入的图片
				// System.Drawing.Image imgAth =
				// System.Drawing.Image.FromFile(path1);
				BufferedImage image = null;
				try {
					image = ImageIO.read(new File(path1));
				} catch (IOException e) {
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

			if (strs[1].trim().equals("BPPaint")) {
				String path1 = DBAccess.RunSQLReturnString("SELECT  Tag2 FROM Sys_FrmEleDB WHERE REFPKVAL="
						+ this.HisGEEntity.getPKVal() + " AND EleID='" + strs[0].trim() + "'");
				// string path1 = SystemConfig.getPathOfDataUser() +
				// "\\BPPaint\\" + this.HisGEEntity.ToString().Trim() + "\\" +
				// this.HisGEEntity.PKVal + ".png";
				// 定义rtf中图片字符串.
				StringBuilder mypict = new StringBuilder();
				// 获取要插入的图片
				// System.Drawing.Image myBPPaint =
				// System.Drawing.Image.FromFile(path1);
				BufferedImage image = null;
				try {
					image = ImageIO.read(new File(path1));
				} catch (IOException e) {
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

			if (strs.length == 2) {
				String val = this.HisGEEntity.GetValStringByKey(strs[0].trim());

				if (strs[1].trim().equals("Text")) {
					if (val.equals("0")) {
						return "否";
					} else {
						return "是";
					}
				} else if (strs[1].trim().equals("Year")) {
					return val.substring(0, 4);
				} else if (strs[1].trim().equals("Month")) {
					return val.substring(5, 7);
				} else if (strs[1].trim().equals("Day")) {
					return val.substring(8, 10);
				} else if (strs[1].trim().equals("NYR")) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
					return dateFormat.format(DataType.ParseSysDate2DateTime(val));
					// return
					// DataType.ParseSysDate2DateTime(val).ToString("yyyy年MM月dd日");
				} else if (strs[1].trim().equals("RMB")) {
					return new DecimalFormat("##0.00").format(Float.parseFloat(val));
				} else if (strs[1].trim().equals("RMBDX")) {
					return DataType.ParseFloatToCash(Float.parseFloat(val));
				} else if (strs[1].trim().equals("Siganture")) {
					String path = SystemConfig.getPathOfDataUser() + "\\Siganture\\" + val + ".jpg";
					// 定义rtf中图片字符串
					StringBuilder pict = new StringBuilder();
					// 获取要插入的图片
					// System.Drawing.Image img =
					// System.Drawing.Image.FromFile(path);
					BufferedImage image = null;
					try {
						image = ImageIO.read(new File(path));
					} catch (IOException e) {
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
				} else if (strs[1].trim().equals("BoolenText")) {
					if (val.equals("0"))
						return "否";
					else
						return "是";

				} else if (strs[1].trim().equals("Boolen")) {
					if (val.equals("1"))
						return "[√]";
					else
						return "[×]";
				} else if (strs[1].trim().equals("YesNo")) {
					if (val.equals("1")) {
						return "[√]";
					} else {
						return "[×]";
					}
				} else if (strs[1].trim().equals("Yes")) {
					if (val.equals("0")) {
						return "[×]";
					} else {
						return "[√]";
					}
				} else if (strs[1].trim().equals("No")) {
					if (val.equals("0")) {
						return "[√]";
					} else {
						return "[×]";
					}
				} else {
					throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
				}
			} else {
				throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
			}
		}

		throw new RuntimeException("参数设置错误 GetValueByKey ：" + key);
	}

	// /#region 生成单据
	/**
	 * 生成单据
	 *
	 * param cfile
	 *            模板文件
	 * @throws Exception
	 */
	public final void MakeDoc(String cfile) throws Exception {
		String file = PubClass.GenerTempFileName("doc");
		this.MakeDoc(cfile, SystemConfig.getPathOfTemp(), file,null);
	}

	public String ensStrs = "";

	/**
	 * 轨迹表（用于输出打印审核轨迹,审核信息.）
	 */
	public DataTable dtTrack = null;
	public DataTable wks = null;

	/**
	 * 单据生成
	 *
	 * param cfile
	 *            模板文件
	 * param path
	 *            生成路径
	 * param file
	 *            生成文件
	 * param isOpen
	 *            是否用IE打开？
	 * @throws Exception
	 */
	public final void MakeDoc(String cfile, String path, String file,  String billUrl) throws Exception {
		cfile = cfile.replace(".rtf.rtf", ".rtf");

		if (new File(path).exists() == false)
			new File(path).mkdirs();

		StringBuilder str = new StringBuilder(Cash.GetBillStr(cfile, false).substring(0));
		if (this.getHisEns().size() == 0) {
			if (this.HisGEEntity == null) {
				throw new RuntimeException("@您没有为报表设置数据源...");
			}
		}

		this.ensStrs = "";
		if (this.getHisEns().size() != 0) {
			for (Entity en : Entities.convertEntities(this.getHisEns())) {
				ensStrs += en.toString();
			}
		} else {
			ensStrs = this.HisGEEntity.toString();
		}

		String error = "";
		String[] paras = null;
		if (this.HisGEEntity != null) {
			paras = Cash.GetBillParas(cfile, ensStrs, this.HisGEEntity);
		} else {
			paras = Cash.GetBillParas(cfile, ensStrs, this.getHisEns());
		}

		this.TempFilePath = path + file;
		try {
			String key = "";
			String ss = "";

			// 替换主表标记
			for (String para : paras) {
				if (para == null || para.equals("")) {
					continue;
				}
				try {
					if (para.contains("ImgAth"))
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					else if (para.contains("Siganture"))
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					else if (para.contains("Img@AppPath"))
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueImgStrs(para)));
					/*else if (para.contains("Img@QR"))
						str = new StringBuilder(
								str.toString().replace("<" + para + ">", this.GetValueImgStrsOfQR(billUrl)));*/
					else if (para.contains(".BPPaint"))
						str = new StringBuilder(
								str.toString().replace("<" + para + ">", this.GetValueBPPaintStrs(para)));
					else if (para.contains(".RMB"))
						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					else if (para.contains(".RMBDX"))

						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					else if (para.contains(".Boolen"))

						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));

					else if (para.contains(".BoolenText"))

						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					else if (para.contains(".NYR"))

						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));
					else if (para.contains(".Year"))

						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));

					else if (para.contains(".Month"))

						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));

					else if (para.contains(".Day"))

						str = new StringBuilder(str.toString().replace("<" + para + ">", this.GetValueByKey(para)));

					else if (para.contains(".Yes") == true)

						str = new StringBuilder(
								str.toString().replace("<" + para + ">", this.GetCode(this.GetValueByKey(para))));

					else if (para.contains("-EnumYes") == true)

						str = new StringBuilder(
								str.toString().replace("<" + para + ">", this.GetCode(this.GetValueByKey(para))));
					else if (para.contains(".") == true)

						continue; // 有可能是明细表数据.

					else {
						str = new StringBuilder(str.toString().replace("<" +
								para + ">", this.GetValueByKey(para)));

					}
				} catch (RuntimeException ex) {
					error += "替换主表标记取参数[" + para
							+ "]出现错误：有以下情况导致此错误;1你用Text取值时间，此属性不是外键。2,类无此属性。3,该字段是明细表字段但是丢失了明细表标记.<br>更详细的信息：<br>"
							+ ex.getMessage();
					Log.DebugWriteError("MakeDoc" + error);
					if (SystemConfig.getIsDebug()) {
						throw new RuntimeException(error);
					}
				}
			}
			// 替换主表标记

			// 从表
			String shortName = "";
			ArrayList<Entities> al = this.getEnsDataDtls();
			for (Entities dtls : al) {
				Entity dtl = dtls.getGetNewEntity();
				String dtlEnName = dtl.toString();
				shortName = dtlEnName.substring(dtlEnName.lastIndexOf(".") + 1);

				if (str.toString().indexOf(shortName) == -1) {
					continue;
				}

				int pos_rowKey = str.indexOf(shortName);
				int end_rowKey = str.lastIndexOf(shortName);
				int row_start = -1, row_end = -1;
				if (pos_rowKey != -1) {
					row_start = str.substring(0, pos_rowKey).lastIndexOf("\\row");
					// 获取从表表名出现的最后的位置
					// int end_rowKey = str.lastIndexOf(shortName);
					// 获取row的位置
					row_end = str.substring(end_rowKey).indexOf("\\row");
				}

				if (row_start != -1 && row_end != -1) {
					String row = str.substring(row_start, (end_rowKey) + row_end);
					str = new StringBuilder(str.toString().replace(row, ""));

					Map map = dtls.getGetNewEntity().getEnMap();
					int i = dtls.size();
					while (i > 0) {
						i--;
						Object tempVar = row;
						String rowData = (String) ((tempVar instanceof String) ? tempVar : null);
						dtl = dtls.get(i);
						//替换序号
						int rowIdx = i + 1;
						rowData = rowData.replace("<IDX>", String.valueOf(rowIdx));
						for (Attr attr : map.getAttrs()) {
							if (!attr.getUIVisible()) {
								continue;
							}
							switch (attr.getMyDataType()) {
								case DataType.AppDouble:
								case DataType.AppFloat:
								case DataType.AppRate:
									rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">",
											dtl.GetValStringByKey(attr.getKey()));
									break;
								case DataType.AppMoney:
									rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">",
											dtl.GetValDecimalByKey(attr.getKey(), 2).toString());
									break;
								case DataType.AppInt:

									if (attr.getMyDataType() == DataType.AppBoolean) {
										rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">",
												dtl.GetValStrByKey(attr.getKey()));
										int v = dtl.GetValIntByKey(attr.getKey());
										if (v == 1) {
											rowData = rowData.replace("<" + shortName + "." + attr.getKey() + "Text>", "是");
										} else {
											rowData = rowData.replace("<" + shortName + "." + attr.getKey() + "Text>", "否");
										}
									} else {
										if (attr.getIsEnum()) {
											rowData = rowData.replace("<" + shortName + "." + attr.getKey() + "Text>",
													dtl.GetValRefTextByKey(attr.getKey()));
										} else {
											rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">",
													dtl.GetValStrByKey(attr.getKey()));
										}
									}
									break;
								default:
									rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">",
											dtl.GetValStrByKey(attr.getKey()));
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
			for (Entities dtls : al) {
				Entity dtl = dtls.getGetNewEntity();
				String dtlEnName = dtl.toString();
				shortName = dtlEnName.substring(dtlEnName.lastIndexOf(".") + 1);
				Map map = dtl.getEnMap();
				for (Attr attr : map.getAttrs()) {
					switch (attr.getMyDataType()) {
						case DataType.AppDouble:
						case DataType.AppFloat:
						case DataType.AppMoney:
						case DataType.AppRate:
							key = "<" + shortName + "." + attr.getKey() + ".SUM>";
							if (str.indexOf(key) != -1) {
								str = new StringBuilder(str.toString().replace(key,
										(new Float(dtls.GetSumFloatByKey(attr.getKey()))).toString()));
							}

							key = "<" + shortName + "." + attr.getKey() + ".SUM.RMB>";
							if (str.indexOf(key) != -1) {
								String value = new DecimalFormat("##0.00")
										.format(new Float(dtls.GetSumFloatByKey(attr.getKey())));
								str = new StringBuilder(str.toString().replace(key, value));
								// str = str.replace(key, (new
								// Float(dtls.GetSumFloatByKey(attr.getKey()))).ToString("0.00"));
							}

							key = "<" + shortName + "." + attr.getKey() + ".SUM.RMBDX>";
							if (str.indexOf(key) != -1) {
								str = new StringBuilder(str.toString().replace(key,
										GetCode(DataType.ParseFloatToCash(dtls.GetSumFloatByKey(attr.getKey())))));
							}
							break;
						case DataType.AppInt:
							key = "<" + shortName + "." + attr.getKey() + ".SUM>";
							if (str.indexOf(key) != -1) {
								str = new StringBuilder(str.toString().replace(key,
										(new Integer(dtls.GetSumIntByKey(attr.getKey()))).toString()));
							}
							break;
						default:
							break;
					}
				}
			}
			// 从表合计
			// 审核组件组合信息

			// 根据track表获取审核的节点
			// 节点单个审核人
			if (dtTrack != null && str.toString().contains("<WorkCheckBegin>") == false
					&& str.toString().contains("<WorkCheckEnd>") == false) {
				if(this.wks !=null && this.wks.Rows.size() !=0) {
					for (DataRow nd : wks.Rows) // 此处的22是ActionType.WorkCheck的值，此枚举位于BP.WF项目中，此处暂写死此值
					{
						int nodeID = nd.getValue(0) != null ? Integer.parseInt(nd.getValue(0).toString()) : 0;
						//判断是否存在一个节点多个签批意见，需要循环显示，第一个意见替换，其余的节点追加在后面
						boolean isHaveNote = str.equals("WorkCheck.Note." + nodeID);
						boolean isHaveRec = str.equals("WorkCheck.Rec." + nodeID);
						boolean isHaveRecName = str.equals("WorkCheck.RecName." + nodeID);
						boolean isHaveRDT = str.equals("WorkCheck.RDT." + nodeID);
						boolean isHaveWriteDB = str.equals("WorkCheck.WriteDB." + nodeID);
						if (isHaveNote == false && isHaveRec == false && isHaveRecName == false && isHaveRDT == false && isHaveWriteDB == false)
							continue;
						//把track信息分组
						DataRow[] tracks = tracks = dtTrack.Select("NDFrom=" + nodeID);
						if (tracks.length == 0)
						{ //该节点没有签名，替换签名的内容
							String wkKey = "<WorkCheck.Note." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.Rec." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RecName." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RDT." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RDT-NYR." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.Siganture." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.WriteDB." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							continue;
						}
						String workCheckStr = "";
						int idx = 0;
						for (DataRow row : tracks)
						{
							int acType = Integer.parseInt(row.getValue("ActionType").toString());
							if (acType != 22)
								continue;
							String empNo = row.getValue("EmpFrom") == null ? "" : row.getValue("EmpFrom").toString();

							if (isHaveNote)
								workCheckStr += this.GetValueCheckWorkByKey(row, "Msg") + "\\par";
							if (isHaveRec)
								workCheckStr += this.GetValueCheckWorkByKey(row, "EmpFrom") + "\\par";
							if (isHaveRecName)
								workCheckStr += this.GetValueCheckWorkByKey(row, "EmpFromT") + "\\par";
							if (isHaveRDT)
								workCheckStr += this.GetValueCheckWorkByKey(row, "RDT") + "\\par";
							if (isHaveWriteDB) {
								String wkVal = this.GetValueCheckWorkByKey(row, "WriteDB");
								String RDT = this.GetValueCheckWorkByKey(row, "RDT").replace("-", "");
								RDT = RDT.replace(":", "");
								RDT = RDT.replace(" ", "");
								int rdttime = Integer.parseInt(RDT);
								if (DataType.IsNullOrEmpty(wkVal) == true)
									wkVal = "";

								BufferedImage image = null;
								//定义rtf中图片字符串.
								StringBuilder mypict = new StringBuilder();
								//将要插入的图片转换为16进制字符串
								String imgHexString;
								imgHexString = ImageTo16String(path);
								//转换
								//生成rtf中图片字符串
								mypict.append("\n");
								mypict.append("{\\pict");
								mypict.append("\\jpegblip");
								mypict.append("\\picscalex100");
								mypict.append("\\picscaley100");
								mypict.append("\\picwgoal" + image.getWidth() * 3);
								mypict.append("\\pichgoal" + image.getHeight() * 3);
								mypict.append(imgHexString + "}");
								mypict.append("\n");
								workCheckStr += mypict.toString() + "\\par";
							}
							idx++;
						}
						if (workCheckStr.contains(""))
						{
							String wkKey = "<WorkCheck.Note." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.Rec." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RecName." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RDT." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RDT-NYR." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.Siganture." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.WriteDB." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							continue;
						}
						if (isHaveNote == true || isHaveRec == true || isHaveRecName == true || isHaveRDT == true || isHaveWriteDB == true)
						{
							boolean isHaveChange = false;
							if (isHaveNote == true)
							{
								str = new StringBuilder(str.toString().replace("<WorkCheck.Note." + nodeID + ">", workCheckStr));
								isHaveChange = true;
							}

							if (isHaveRec == true && isHaveChange == false)
							{
								str = new StringBuilder(str.toString().replace("<WorkCheck.Rec." + nodeID + ">", workCheckStr));
								isHaveChange = true;
							}

							if (isHaveRecName == true && isHaveChange == false)
							{
								str = new StringBuilder(str.toString().replace("<WorkCheck.RecName." + nodeID + ">", workCheckStr));
								isHaveChange = true;
							}

							if (isHaveRDT == true && isHaveChange == false)
							{
								str = new StringBuilder(str.toString().replace("<WorkCheck.RDT." + nodeID + ">", workCheckStr));
								isHaveChange = true;
							}

							if (isHaveWriteDB == true && isHaveChange == false)
							{
								str = new StringBuilder(str.toString().replace("<WorkCheck.WriteDB." + nodeID + ">", workCheckStr));
								isHaveChange = true;
							}

							String wkKey = "<WorkCheck.Note." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.Rec." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RecName." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RDT." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.RDT-NYR." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.Siganture." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));
							wkKey = "<WorkCheck.WriteDB." + nodeID + ">";
							str = new StringBuilder(str.toString().replace(wkKey, ""));

						}

//
//						int acType = Integer.parseInt(row.getValue("ActionType").toString());
//						if (acType != 22)
//							continue;
//						str = new StringBuilder(str.toString().replace("<WorkCheck.Msg." + row.getValue("NDFrom") + ">",
//								this.GetValueCheckWorkByKey(row, "Msg")));
//						str = new StringBuilder(str.toString().replace("<WorkCheck.Rec." + row.getValue("NDFrom") + ">",
//								this.GetValueCheckWorkByKey(row, "EmpFromT")));
//						str = new StringBuilder(str.toString().replace("<WorkCheck.RDT." + row.getValue("NDFrom") + ">",
//								this.GetValueCheckWorkByKey(row, "RDT")));

					}
				}
			}

			// 轨迹信息
			str = GetFlowTrackTable(str);
			// 多附件

			for (Object athObjEnsName : this.getEnsDataAths().keySet()) {
				String athName = "Ath." + athObjEnsName.toString();
				String athFilesName = "";
				if (str.indexOf(athName) == -1)
					continue;

				FrmAttachmentDBs athDbs = (FrmAttachmentDBs) this.getEnsDataAths().get(athObjEnsName);
				if (athDbs == null)
					continue;
				for (FrmAttachmentDB athDb : athDbs.ToJavaList()) {
					if (athFilesName.length() > 0)
						athFilesName += " ， ";

					athFilesName += athDb.getFileName();
				}
				str = new StringBuilder(str.toString().replace("<" + athName + ">", this.GetCode(athFilesName)));
			}

			str = new StringBuilder(str.toString().replace("<", ""));
			str = new StringBuilder(str.toString().replace(">", ""));
			try {
				ConvertTools.streamWriteConvertGBK(str.toString(), TempFilePath);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (RuntimeException ex) {
			String msg = "";
			if (SystemConfig.getIsDebug()) { // 异常可能与单据的配置有关系。
				try {
					this.CyclostyleFilePath = SystemConfig.getPathOfDataUser() + "/CyclostyleFile/" + cfile;
					str = new StringBuilder(Cash.GetBillStr(cfile, false));
					msg = "@已经成功的执行修复线  RepairLineV2，您重新发送一次或者，退后重新在发送一次，是否可以解决此问题。@" + str;
				} catch (RuntimeException ex1) {
					msg = "执行修复线失败.  RepairLineV2 " + ex1.getMessage();
				}
			}
			throw new RuntimeException(
					"生成文档失败：单据名称[" + this.CyclostyleFilePath + "] 异常信息：" + ex.getMessage() + " @自动修复单据信息：" + msg);
		}

	}

	private String GetValueCheckWorkByKey(DataRow row, String key) {
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");

		switch (key) {
			case "RDT":
				return row.getValue("RDT").toString(); // 审核日期.
			case "RDT-NYR":
				String rdt = row.getValue("RDT").toString(); // 审核日期.
				return rdt;
			case "Rec":
				return row.getValue("EmpFrom").toString(); // 记录人.
			case "RecName":
				return row.getValue("EmpFromT").toString(); // 审核人.
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
	 * param templeteFile
	 *            模板文件
	 * param saveToFile
	 * param mainDT
	 * param dtls
	 */
	@Deprecated
	public final void MakeDocByDataSet(String templeteFile, String saveToPath, String saveToFileName, DataTable mainDT,
									   DataSet dtlsDS) {

	}

	// 方法
	/**
	 * RTFEngine
	 */
	public RTFEngine() {
		this._EnsDataDtls = null;
		this._HisEns = null;
	}
	/**
	 * 修复线
	 *
	 * param line
	 * @return
	 */
}