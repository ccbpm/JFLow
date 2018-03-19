package BP.Pub;

import BP.En.Entity;
import BP.En.SimpleNoNameFixs;

/**
 * NDs
 */
public class YFs extends SimpleNoNameFixs
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 月份集合
	 */
	public YFs()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new YF();
	}
	
	@Override
	public int RetrieveAll()
	{
		int num = super.RetrieveAll();
		
		if (num != 12)
		{
			try
			{
				BP.DA.DBAccess.RunSQL("DELETE FROM Pub_YF ");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			String tmpStr = "";
			
			for (int i = 1; i <= 12; i++)
			{
				BP.Pub.YF yf = new YF();
				if (i <= 9)
					tmpStr = "0" + Integer.toString(i);
				else
					tmpStr = Integer.toString(i);
				yf.setNo(tmpStr);
				yf.setName(tmpStr);
				yf.Insert();
			}
			
			return super.RetrieveAll();
		}
		return 12;
	}
}