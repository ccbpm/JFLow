package bp.en;
import bp.en.*;
import bp.da.*;
import bp.*;

/** 
 编号实体集合。
*/
public abstract class EntitiesNo extends Entities
{
	@Override
	public int RetrieveAllFromDBSource() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.addOrderBy("No");
		return qo.DoQuery();
	}
}