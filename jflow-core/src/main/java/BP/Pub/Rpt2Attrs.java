package BP.Pub;

import BP.DA.*;
import BP.En.*;
import BP.Web.Controls.*;
import BP.Web.*;
import BP.Sys.*;

public class Rpt2Attrs extends ArrayList<Object>
{
	public final void Add(Rpt2Attr en)
	{
		this.InnerList.add(en);
	}
	public final Rpt2Attr GetD2(int idx)
	{
		return (Rpt2Attr)this.InnerList[idx];
	}
}