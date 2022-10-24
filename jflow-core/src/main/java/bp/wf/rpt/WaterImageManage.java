package bp.wf.rpt;

import java.util.*;
import java.io.*;

/** 
 水印图片的操作管理 Design by Gary Gong From Demetersoft.com
*/
public class WaterImageManage
{
	/** 
	 生成一个新的水印图片制作实例
	*/
	public WaterImageManage()  {
		//
		// TODO: Add constructor logic here
		//
	}

	/** 
	 添加图片水印
	 
	 param sourcePicture 源图片文件名
	 param waterImage 水印图片文件名
	 param alpha 透明度(0.1-1.0数值越小透明度越高)
	 param position 位置
	 param PicturePath  图片的路径
	 @return 返回生成于指定文件夹下的水印文件名
	*/
//	public final String DrawImage(String sourcePicture, String waterImage, float alpha, ImagePosition position, String PicturePath)
//	{
//		//
//		// 判断参数是否有效
//		//
//		if (sourcePicture.equals("") || waterImage.equals("") || alpha == 0.0 || PicturePath.equals(""))
//		{
//			return sourcePicture;
//		}
//
//		//
//		// 源图片，水印图片全路径
//		//
//		String sourcePictureName = PicturePath + sourcePicture;
//		String waterPictureName = PicturePath + waterImage;
//		String fileSourceExtension = System.IO.Path.GetExtension(sourcePictureName).toLowerCase();
//		String fileWaterExtension = System.IO.Path.GetExtension(waterPictureName).toLowerCase();
//		//
//		// 判断文件是否存在,以及类型是否正确
//		//
//		if ((new File(sourcePictureName)).isFile() == false || (new File(waterPictureName)).isFile() == false || (!fileSourceExtension.equals(".gif") && !fileSourceExtension.equals(".jpg") && !fileSourceExtension.equals(".png")) || (!fileWaterExtension.equals(".gif") && !fileWaterExtension.equals(".jpg") && !fileWaterExtension.equals(".png")))
//		{
//			return sourcePicture;
//		}
//
//		//
//		// 目标图片名称及全路径
//		//
//		String targetImage = sourcePictureName.replace(System.IO.Path.GetExtension(sourcePictureName), "") + "_1101.jpg";
//
//		//
//		// 将需要加上水印的图片装载到Image对象中
//		//
//		Image imgPhoto = Image.FromFile(sourcePictureName);
//		//
//		// 确定其长宽
//		//
//		int phWidth = imgPhoto.Width;
//		int phHeight = imgPhoto.Height;
//
//		//
//		// 封装 GDI+ 位图，此位图由图形图像及其属性的像素数据组成。
//		//
//		Bitmap bmPhoto = new Bitmap(phWidth, phHeight, PixelFormat.Format24bppRgb);
//
//		//
//		// 设定分辨率
//		//
//		bmPhoto.SetResolution(imgPhoto.HorizontalResolution, imgPhoto.VerticalResolution);
//
//		//
//		// 定义一个绘图画面用来装载位图
//		//
//		Graphics grPhoto = Graphics.FromImage(bmPhoto);
//
//		//
//		//同样，由于水印是图片，我们也需要定义一个Image来装载它
//		//
//		Image imgWatermark = new Bitmap(waterPictureName);
//
//		//
//		// 获取水印图片的高度和宽度
//		//
//		int wmWidth = imgWatermark.Width;
//		int wmHeight = imgWatermark.Height;
//
//		//SmoothingMode：指定是否将平滑处理（消除锯齿）应用于直线、曲线和已填充区域的边缘。
//		// 成员名称  说明
//		// AntiAlias   指定消除锯齿的呈现。
//		// Default    指定不消除锯齿。
//
//		// HighQuality 指定高质量、低速度呈现。
//		// HighSpeed  指定高速度、低质量呈现。
//		// Invalid    指定一个无效模式。
//		// None     指定不消除锯齿。
//		grPhoto.SmoothingMode = SmoothingMode.AntiAlias;
//
//		//
//		// 第一次描绘，将我们的底图描绘在绘图画面上
//		//
//		grPhoto.DrawImage(imgPhoto, new Rectangle(0, 0, phWidth, phHeight), 0, 0, phWidth, phHeight, GraphicsUnit.Pixel);
//
//		//
//		// 与底图一样，我们需要一个位图来装载水印图片。并设定其分辨率
//		//
//		Bitmap bmWatermark = new Bitmap(bmPhoto);
//		bmWatermark.SetResolution(imgPhoto.HorizontalResolution, imgPhoto.VerticalResolution);
//
//		//
//		// 继续，将水印图片装载到一个绘图画面grWatermark
//		//
//		Graphics grWatermark = Graphics.FromImage(bmWatermark);
//
//		//
//		//ImageAttributes 对象包含有关在呈现时如何操作位图和图元文件颜色的信息。
//		//
//
//		ImageAttributes imageAttributes = new ImageAttributes();
//
//		//
//		//Colormap: 定义转换颜色的映射
//		//
//		ColorMap colorMap = new ColorMap();
//
//		//
//		//我的水印图被定义成拥有绿色背景色的图片被替换成透明
//		//
//		colorMap.OldColor = Color.FromArgb(255, 0, 255, 0);
//		colorMap.NewColor = Color.FromArgb(0, 0, 0, 0);
//
//		ColorMap[] remapTable = {colorMap};
//
//		imageAttributes.SetRemapTable(remapTable, ColorAdjustType.Bitmap);
//
//		float[][] colorMatrixElements =
//		{
//			new float[] {1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
//			new float[] {0.0f, 1.0f, 0.0f, 0.0f, 0.0f},
//			new float[] {0.0f, 0.0f, 1.0f, 0.0f, 0.0f},
//			new float[] {0.0f, 0.0f, 0.0f, alpha, 0.0f},
//			new float[] {0.0f, 0.0f, 0.0f, 0.0f, 1.0f}
//		};
//
//		// ColorMatrix:定义包含 RGBA 空间坐标的 5 x 5 矩阵。
//		// ImageAttributes 类的若干方法通过使用颜色矩阵调整图像颜色。
//		ColorMatrix wmColorMatrix = new ColorMatrix(colorMatrixElements);
//
//
//		imageAttributes.SetColorMatrix(wmColorMatrix, ColorMatrixFlag.Default, ColorAdjustType.Bitmap);
//
//		//
//		//上面设置完颜色，下面开始设置位置
//		//
//		int xPosOfWm;
//		int yPosOfWm;
//
//		switch (position)
//		{
//			case BottomMiddle:
//				xPosOfWm = (phWidth - wmWidth) / 2;
//				yPosOfWm = phHeight - wmHeight - 10;
//				break;
//
//			case Center:
//				xPosOfWm = (phWidth - wmWidth) / 2;
//				yPosOfWm = (phHeight - wmHeight) / 2;
//				break;
//			case LeftBottom:
//				xPosOfWm = 10;
//				yPosOfWm = phHeight - wmHeight - 10;
//				break;
//			case LeftTop:
//				xPosOfWm = 10;
//				yPosOfWm = 10;
//				break;
//			case RightTop:
//				xPosOfWm = phWidth - wmWidth - 10;
//				yPosOfWm = 10;
//				break;
//			case RigthBottom:
//				xPosOfWm = phWidth - wmWidth - 10;
//				yPosOfWm = phHeight - wmHeight - 10;
//				break;
//			case TopMiddle:
//				xPosOfWm = (phWidth - wmWidth) / 2;
//				yPosOfWm = 10;
//				break;
//			default:
//				xPosOfWm = 10;
//				yPosOfWm = phHeight - wmHeight - 10;
//				break;
//		}
//
//		// 第二次绘图，把水印印上去
//		//
//		grWatermark.DrawImage(imgWatermark, new Rectangle(xPosOfWm, yPosOfWm, wmWidth, wmHeight), 0, 0, wmWidth, wmHeight, GraphicsUnit.Pixel, imageAttributes);
//
//		imgPhoto = bmWatermark;
//		grPhoto.Dispose();
//		grWatermark.Dispose();
//
//		//
//		// 保存文件到服务器的文件夹里面
//		//
//		imgPhoto.Save(targetImage, ImageFormat.Jpeg);
//		imgPhoto.Dispose();
//		imgWatermark.Dispose();
//		return targetImage.replace(PicturePath, "");
//	}
//
//	/*
//	*
//	* 使用说明：
//	* 　建议先定义一个WaterImage实例
//	* 　然后利用实例的属性，去匹配需要进行操作的参数
//	* 　然后定义一个WaterImageManage实例
//	* 　利用WaterImageManage实例进行DrawImage（），印图片水印
//	* 　DrawWords（）印文字水印
//	*
//	-*/
//
//	/**
//	 在图片上添加水印文字
//
//	 param sourcePicture 源图片文件(文件名，不包括路径)
//	 param waterWords 需要添加到图片上的文字
//	 param alpha 透明度
//	 param position 位置
//	 param targetImage 目标图片名称及全路径
//	 @return
//	*/
//	public final String DrawWords(String sourcePictureName, String waterWords, float alpha, ImagePosition position, String targetImage)
//	{
//		//
//		// 判断参数是否有效
//		//
//		if (sourcePictureName.equals("") || waterWords.equals("") || alpha == 0.0 || sourcePictureName.equals(""))
//		{
//			throw new RuntimeException("@参数错误");
//		}
//
//		//
//		// 源图片全路径
//		//
//		String fileExtension = System.IO.Path.GetExtension(sourcePictureName).toLowerCase();
//
//		//
//		// 判断文件是否存在,以及文件名是否正确
//		//
//		if ((new File(sourcePictureName)).isFile() == false || (!fileExtension.equals(".gif") && !fileExtension.equals(".jpg") && !fileExtension.equals(".png")))
//		{
//			throw new RuntimeException("@源文件格式错误" + sourcePictureName);
//		}
//
//
//		//创建一个图片对象用来装载要被添加水印的图片
//		Image imgPhoto = Image.FromFile(sourcePictureName);
//
//		//获取图片的宽和高
//		int phWidth = imgPhoto.Width;
//		int phHeight = imgPhoto.Height;
//
//		//
//		//建立一个bitmap，和我们需要加水印的图片一样大小
//		Bitmap bmPhoto = new Bitmap(phWidth, phHeight, PixelFormat.Format24bppRgb);
//
//		//SetResolution：设置此 Bitmap 的分辨率
//		//这里直接将我们需要添加水印的图片的分辨率赋给了bitmap
//		bmPhoto.SetResolution(imgPhoto.HorizontalResolution, imgPhoto.VerticalResolution);
//
//		//Graphics：封装一个 GDI+ 绘图图面。
//		Graphics grPhoto = Graphics.FromImage(bmPhoto);
//
//		//设置图形的品质
//		grPhoto.SmoothingMode = SmoothingMode.AntiAlias;
//
//		//将我们要添加水印的图片按照原始大小描绘（复制）到图形中
//		grPhoto.DrawImage(imgPhoto, new Rectangle(0, 0, phWidth, phHeight), 0, 0, phWidth, phHeight, GraphicsUnit.Pixel); // 描绘的单位，这里用的是像素
//
//		//根据图片的大小我们来确定添加上去的文字的大小
//		//在这里我们定义一个数组来确定
//		int[] sizes = new int[] {16, 14, 12, 10, 8, 6, 4};
//
//		//字体
//		Font crFont = null;
//		//矩形的宽度和高度，SizeF有三个属性，分别为Height高，width宽，IsEmpty是否为空
//		SizeF crSize = new SizeF();
//
//		//利用一个循环语句来选择我们要添加文字的型号
//		//直到它的长度比图片的宽度小
//		for (int i = 0; i < 7; i++)
//		{
//			crFont = new Font("arial", sizes[i], FontStyle.Italic);
//
//
//			//测量用指定的 Font 对象绘制并用指定的 StringFormat 对象格式化的指定字符串。
//			crSize = grPhoto.MeasureString(waterWords, crFont);
//
//			// ushort 关键字表示一种整数数据类型
//
////ORIGINAL LINE: if ((ushort)crSize.Width < (ushort)phWidth)
//			if ((short)crSize.Width < (short)phWidth)
//			{
//				break;
//			}
//		}
//
//		//截边5%的距离，定义文字显示(由于不同的图片显示的高和宽不同，所以按百分比截取)
//		int yPixlesFromBottom = (int)(phHeight * .05);
//
//		//定义在图片上文字的位置
//		float wmHeight = crSize.Height;
//		float wmWidth = crSize.Width;
//		float xPosOfWm;
//		float yPosOfWm;
//
//		switch (position)
//		{
//			case BottomMiddle:
//				xPosOfWm = phWidth / 2;
//				yPosOfWm = phHeight - wmHeight - 10;
//				break;
//			case Center:
//				xPosOfWm = phWidth / 2;
//				yPosOfWm = phHeight / 2;
//				break;
//			case LeftBottom:
//				xPosOfWm = wmWidth;
//				yPosOfWm = phHeight - wmHeight - 10;
//				break;
//			case LeftTop:
//				xPosOfWm = wmWidth / 2;
//				yPosOfWm = wmHeight / 2;
//				break;
//			case RightTop:
//				xPosOfWm = phWidth - wmWidth - 10;
//				yPosOfWm = wmHeight;
//				break;
//			case RigthBottom:
//				xPosOfWm = phWidth - wmWidth - 10;
//				yPosOfWm = phHeight - wmHeight - 10;
//				break;
//			case TopMiddle:
//				xPosOfWm = phWidth / 2;
//				yPosOfWm = wmWidth;
//
//				break;
//			default:
//				xPosOfWm = wmWidth;
//				yPosOfWm = phHeight - wmHeight - 10;
//				break;
//		}
//
//		//封装文本布局信息（如对齐、文字方向和 Tab 停靠位），显示操作（如省略号插入和国家标准 (National) 数字替换）和 OpenType 功能。
//		StringFormat StrFormat = new StringFormat();
//
//
//		//定义需要印的文字居中对齐
//		StrFormat.Alignment = StringAlignment.Center;
//
//		//SolidBrush:定义单色画笔。画笔用于填充图形形状，如矩形、椭圆、扇形、多边形和封闭路径。
//		//这个画笔为描绘阴影的画笔，呈灰色
//		int m_alpha = FloatingPointToInteger.ToInt32(256 * alpha);
//		SolidBrush semiTransBrush2 = new SolidBrush(Color.FromArgb(m_alpha, 0, 0, 0));
//
//		//描绘文字信息，这个图层向右和向下偏移一个像素，表示阴影效果
//		//DrawString 在指定矩形并且用指定的 Brush 和 Font 对象绘制指定的文本字符串。
//		grPhoto.DrawString(waterWords, crFont, semiTransBrush2, new PointF(xPosOfWm + 1, yPosOfWm + 1), StrFormat);
//
//		//从四个 ARGB 分量（alpha、红色、绿色和蓝色）值创建 Color 结构，这里设置透明度为153
//		//这个画笔为描绘正式文字的笔刷，呈白色
//		SolidBrush semiTransBrush = new SolidBrush(Color.FromArgb(153, 255, 255, 255));
//
//		//第二次绘制这个图形，建立在第一次描绘的基础上
//		grPhoto.DrawString(waterWords, crFont, semiTransBrush, new PointF(xPosOfWm, yPosOfWm), StrFormat);
//
//		//imgPhoto是我们建立的用来装载最终图形的Image对象
//		//bmPhoto是我们用来制作图形的容器，为Bitmap对象
//		imgPhoto = bmPhoto;
//		//释放资源，将定义的Graphics实例grPhoto释放，grPhoto功德圆满
//		grPhoto.Dispose();
//
//		//将grPhoto保存
//		imgPhoto.Save(targetImage, ImageFormat.Jpeg);
//		imgPhoto.Dispose();
//
//		return targetImage;
//
//		//return targetImage.Replace(PicturePath, "");
//	}
//
//	private SolidBrush RndBrush(Color BackColor, Random rnd)
//	{
//		int r, g, b;
//		do
//		{
//			r = rnd.nextInt(255);
//		} while (Math.abs(r - BackColor.R) < 50); //这是为了控制颜色不要和背景色太接近
//		do
//		{
//			g = rnd.nextInt(255);
//		} while (Math.abs(g - BackColor.G) < 50);
//		do
//		{
//			b = rnd.nextInt(255);
//		} while (Math.abs(b - BackColor.B) < 50);
//		return new SolidBrush(Color.FromArgb(255, r, g, b));
//	}
}