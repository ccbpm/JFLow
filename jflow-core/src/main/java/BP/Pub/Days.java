package BP.Pub;

import BP.En.Entity;
import BP.En.SimpleNoNameFixs;

public class Days extends SimpleNoNameFixs
{
	/**
	 * 日期集合
	 */
	public Days()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Day();
	}
	
	@Override
	public int RetrieveAll()
	{
		int num = super.RetrieveAll();
		
		if (num != 12)
		{
			BP.DA.DBAccess.RunSQL("DELETE FROM Pub_Day ");
			
			for (int i = 1; i <= 31; i++)
			{
				BP.Pub.Day yf = new Day();
				
				// String str = new Integer(i).toString();
				// if (str.length() == 1) {
				// str = "0" + str;
				// }
				
				String str = String.format("%02d", i);
				
				yf.setNo(str);
				yf.setName(str);
				yf.Insert();
			}
			
			return super.RetrieveAll();
		}
		return 12;
	}
}