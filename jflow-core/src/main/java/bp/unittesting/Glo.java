package bp.unittesting;

import java.util.*;

import bp.en.ClassFactory;

/** 
 全局
*/
public class Glo
{
	/** 
	 根据enName获取单元测试实例.
	 
	 @param enName
	 @return 
	*/
	public static bp.unittesting.TestBase GetTestEntity(String enName)
	{
		ArrayList al = null;
		al =ClassFactory.GetEns(enName);
		for (Object obj : al)
		{
			bp.unittesting.TestBase en = null;
			try
			{
				en = obj instanceof bp.unittesting.TestBase ? (bp.unittesting.TestBase)obj : null;
				if (en == null)
				{
					continue;
				}
				String s = en.Title;
				if (en == null)
				{
					continue;
				}
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			if (en.toString().equals(enName))
			{
				return en;
			}
		}

		throw new RuntimeException("err@单元测试名称拼写错误或者不存在[" + enName + "]");

	}
}