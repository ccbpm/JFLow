package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.model.designer.UCEnModel;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.Tools.StringHelper;
import BP.WF.Glo;

public class RefMethodModel extends UCEnModel{
	
	public String Label1;
	
	public String uc_en="";
	
	public RefMethodModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	
	public String getRefEnKey(){
		String str = getParameter("No");
        if (str == null)
            str = getParameter("OID");

        if (str == null)
            str = getParameter("MyPK");

        if (str == null)
            str = getParameter("PK");
		return str;
	}
	
	public void loadPage()
	{
		String ensName = getParameter("EnsName");
		int index = Integer.parseInt(getParameter("Index"));
		Entities ens = BP.En.ClassFactory.GetEns(ensName);
		Entity en = ens.getGetNewEntity();
		RefMethod rm = en.getEnMap().getHisRefMethods().get(index);

		if (rm.getHisAttrs() == null || rm.getHisAttrs().size() == 0)
		{
			String pk = this.getRefEnKey();
			if (pk == null)
			{
				pk = getParameter(en.getPK());
			}

			en.setPKVal(pk);
			en.Retrieve();
			rm.HisEn = en;

			// 如果是link.
			if (rm.refMethodType.equals(RefMethodType.LinkModel))
			{
				Object tempVar = null;
				try {
					tempVar = rm.Do(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String url = (String)((tempVar instanceof String) ? tempVar : null);
				if (StringHelper.isNullOrEmpty(url))
				{
					ToErrorPage("@应该返回的url.");
					return;
					//throw new RuntimeException("@应该返回的url.");
				}
				sendRedirect(url);
				return;
			}

			Object obj = null;
			try {
				obj = rm.Do(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (obj == null)
			{
				 WinClose();
				return;
			}

			String info = obj.toString();
			info = info.replace("@", "<br>@");
			if (info.contains("<"))
			{
				ToMsgPage(info);
			}
			else
			{
				if(info.contains("http:")){
					BaseModel.sendRedirect(info);
				}else{
					winCloseWithMsg(info);
				}
			}
			return;
		}
		this.Bind(rm);
		Label1 = GenerCaption(en.getEnMap().getEnDesc() + "=>" + rm.GetIcon(Glo.getCCFlowAppPath()) + rm.Title);
	}
	public final void Bind(RefMethod rm)
	{
		try {
			BindAttrs(rm.getHisAttrs());
			uc_en = pub.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//检查是否有选择项目。
	}


}
