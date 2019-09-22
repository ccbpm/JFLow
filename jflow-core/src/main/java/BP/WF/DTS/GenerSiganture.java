package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;
import java.io.*;
import java.nio.file.*;

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
		this.Help = "此功能需要用户对 " + BP.Sys.SystemConfig.PathOfDataUser + "\\Siganture\\ 有读写权限，否则会执行失败。";
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
	*/
	@Override
	public Object Do()
	{
		try
		{
			BP.Port.Emps emps = new Emps();
			emps.RetrieveAllFromDBSource();
			String path = BP.Sys.SystemConfig.PathOfDataUser + "\\Siganture\\T.JPG";
			String fontName = "宋体";
			String empOKs = "";
			String empErrs = "";
			for (Emp emp : emps)
			{
				String pathMe = BP.Sys.SystemConfig.PathOfDataUser + "\\Siganture\\" + emp.No + ".JPG";
				if ((new File(pathMe)).isFile())
				{
					continue;
				}

				Files.copy(Paths.get(BP.Sys.SystemConfig.PathOfDataUser + "\\Siganture\\Templete.JPG"), Paths.get(path), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

				System.Drawing.Image img = System.Drawing.Image.FromFile(path);
				Font font = new Font(fontName, 15);
				Graphics g = Graphics.FromImage(img);
				System.Drawing.SolidBrush drawBrush = new System.Drawing.SolidBrush(System.Drawing.Color.Black);
				System.Drawing.StringFormat drawFormat = new System.Drawing.StringFormat(StringFormatFlags.DirectionVertical); //文本
				g.DrawString(emp.Name, font, drawBrush, 3, 3);
				img.Save(pathMe);
				img.Dispose();
				g.Dispose();

				Files.copy(Paths.get(pathMe), Paths.get(BP.Sys.SystemConfig.PathOfDataUser + "\\Siganture\\" + emp.Name + ".JPG"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			}
			return "执行成功...";
		}
		catch (RuntimeException ex)
		{
			return "执行失败，请确认对 " + BP.Sys.SystemConfig.PathOfDataUser + "\\Siganture\\ 目录有访问权限？异常信息:" + ex.getMessage();
		}
	}
}