package BP.Pub;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * NDs
 */
public class YFs extends EntitiesNoName
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
	public Entity getNewEntity()
	{
		return new YF();
	}
	

	public int RetrieveAll() throws Exception
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
				YF yf = new YF();
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