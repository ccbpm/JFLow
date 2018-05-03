package BP.WF.DTS;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import BP.DA.Log;
import BP.En.Method;
import BP.Port.Emp;
import BP.Port.Emps;
import BP.Sys.SystemConfig;

/** 
 Method 的摘要说明
 
*/
public class GenerSiganture extends Method
{
	/** 
	 不带有参数的方法
	 
	*/
	public GenerSiganture()
	{
		this.Title = "为没有设置数字签名的用户设置默认的数字签名";
		this.Help = "此功能需要用户对 "+ BP.Sys.SystemConfig.getPathOfDataUser() + "/Siganture/ 有读写权限，否则会执行失败。";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
		//this.Warning = "您确定要执行吗？";
		//HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	 
	*/
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		try
		{
			BP.Port.Emps emps = new Emps();
			emps.RetrieveAllFromDBSource();
			String path = BP.Sys.SystemConfig.getPathOfDataUser() + "/Siganture/T.JPG";
			String fontName = "宋体";
			String empOKs = "";
			String empErrs = "";
			for (Emp emp : emps.ToJavaList())
			{
				String pathMe = BP.Sys.SystemConfig.getPathOfDataUser() + "/Siganture/" + emp.getNo() + ".JPG";
				File file = new File(pathMe);
				if (file.exists())
				{
					continue;
				}
				fileChannelCopy(new File(SystemConfig.getPathOfDataUser()
						+ "/Siganture/Templete.JPG"), new File(path));
				writeImageLocal(
						pathMe,
						modifyImage(loadImageLocal(path), emp.getName(), 90, 90));
				fileChannelCopy(new File(pathMe),
						new File(SystemConfig.getPathOfDataUser()
								+ "/Siganture/" + emp.getName() + ".JPG"));
				/*File.Copy(BP.Sys.SystemConfig.getPathOfDataUser() + "\\Siganture\\Templete.JPG", path, true);

				System.Drawing.Image img = System.Drawing.Image.FromFile(path);
				Font font = new Font(fontName, 15);
				Graphics g = Graphics.FromImage(img);
				System.Drawing.SolidBrush drawBrush = new System.Drawing.SolidBrush(System.Drawing.Color.Black);
				System.Drawing.StringFormat drawFormat = new System.Drawing.StringFormat(StringFormatFlags.DirectionVertical); //文本
				g.DrawString(emp.getName(), font, drawBrush, 3, 3);
				img.Save(pathMe);
				img.dispose();
				g.dispose();

				File.Copy(pathMe, BP.Sys.SystemConfig.getPathOfDataUser() + "\\Siganture\\" + emp.getName() + ".JPG", true);*/
			}
			return "执行成功...";
		}
		catch(RuntimeException ex)
		{
			return "执行失败，请确认对 " + BP.Sys.SystemConfig.getPathOfDataUser() + "/Siganture/ 目录有访问权限？异常信息:"+ex.getMessage();
		}
	}
	public static BufferedImage modifyImage(BufferedImage img, Object content,
			int x, int y)
	{
		int xx = 0;
		int yy = 0;
		Graphics2D g = null;
		try
		{
			int w = img.getWidth();
			int h = img.getHeight();
			g = img.createGraphics();
			g.setBackground(Color.black);
			g.setColor(Color.black);// 设置字体颜色
			g.setFont(new Font("宋体", Font.PLAIN, 15));
			// 验证输出位置的纵坐标和横坐标
			if (x >= h || y >= w)
			{
				xx = h + 2;
				yy = w;
			} else
			{
				xx = x;
				yy = y;
			}
			if (content != null)
			{
				g.drawString(content.toString(), xx, yy);
			}
			g.dispose();
		} catch (Exception e)
		{
			Log.DefaultLogWriteLineError("modifyImage "+e.getMessage());
		}
		
		return img;
	}
	
	
	public static BufferedImage loadImageLocal(String imgName)
	{
		try
		{
			return ImageIO.read(new File(imgName));
		} catch (IOException e)
		{
			Log.DefaultLogWriteLineError("loadImageLocal "+e.getMessage());
		}
		return null;
	}
	
	public static void writeImageLocal(String newImage, BufferedImage img)
	{
		if (newImage != null && img != null)
		{
			try
			{
				File outputfile = new File(newImage);
				ImageIO.write(img, "jpg", outputfile);
			} catch (IOException e)
			{
				Log.DefaultLogWriteLineError(e.getMessage());
			}
		}
	}
	
	public static void fileChannelCopy(File s, File t)
	{
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try
		{
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}