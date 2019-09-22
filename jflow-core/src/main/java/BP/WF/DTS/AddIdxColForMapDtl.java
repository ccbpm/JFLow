package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 生成模版的垃圾数据 
*/
public class AddIdxColForMapDtl extends Method
{
	/** 
	 生成模版的垃圾数据
	*/
	public AddIdxColForMapDtl()
	{
		this.Title = "为所有的从表增加一个隐藏的Id列.";
		this.Help = "用户VSTO表单.";

		this.GroupName = "系统维护";

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
		MapDtls dtls = new MapDtls();
		dtls.RetrieveAll();

		for (MapDtl item : dtls.ToJavaList())
		{
			MapAttr ma = new MapAttr();
			ma.setMyPK( item.No + "_Idx";
			if (ma.IsExits == true)
			{
				continue;
			}

			ma.FK_MapData = item.No;
			ma.KeyOfEn = "Idx";
			ma.Name = "Idx";
			ma.LGType = FieldTypeS.Normal;
			ma.UIVisible = false;
			ma.DefVal = "0";
			ma.MyDataType = DataType.AppInt;
			ma.Insert();

			GEDtl dtl = new GEDtl(item.No);
			dtl.CheckPhysicsTable();
		}
		return "执行成功.";
	}
}