package BP.WF.UnitTesting;

public class Glo {

	/** 
	 根据enName获取单元测试实例.
	 
	 @param enName
	 @return 
	*/
	public static TestBase GetTestEntity(String enName)
	{
		java.util.ArrayList al = null;
		al = BP.En.ClassFactory.GetObjects("BP.UnitTesting.TestBase");
		for (Object obj : al)
		{
			TestBase en = null;
			try
			{
				en = (TestBase)((obj instanceof TestBase) ? obj : null);
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

		throw new RuntimeException("err@单元测试名称拼写错误或者不存在["+enName+"]");

	}

}
