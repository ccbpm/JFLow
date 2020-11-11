package bp.wf;

import bp.difference.SystemConfig;
public class GloSQL
{
	public static String getSQL()
	{
		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
				return sql;
			default:
				throw new RuntimeException("err@没有判断的类型.");
		}
	}
}