package BP.WF.Template;

import BP.En.BtnType;
import BP.Tools.StringHelper;
import cn.jflow.system.ui.core.Button;

/** 
GenerButton 的摘要说明。
*/
public class Btn extends Button
{
	private BtnType _ShowType = BtnType.Normal;
	
	public final BtnType getShowType()
	{
		return _ShowType;
	}
	public final void setShowType(BtnType value)
	{
		this._ShowType = value;
	}
	
	public Btn(String key){
		this.setId(key);
	}
	/** 
	 提示信息。
	*/
	private String _Hit ;
	public final String getHit()
	{
		return _Hit;
	}
	public final void setHit(String value)
	{
		 this._Hit = value;
	}
	
	private String accessKey ;
	
	public final String getaccessKey()
	{
		return accessKey;
	}
	public final void setaccessKey(String value)
	{
		 this.accessKey = value;
	}
	
	private String style ;
	public final String getStyle()
	{
		return accessKey;
	}
	public final void setStyle(String value)
	{
		 this.accessKey = value;
	}
	
	/** 
	 Btn
	 @param btntype btntype
	*/
	public Btn(BtnType btntype)
	{
		this.setShowType(btntype);
		//this.PreRender += new System.EventHandler(this.BtnPreRender);
	}
	/** 
	 Btn
	*/
	public Btn()
	{
		//this.Attributes["class"] = "Btn";
		// this.PreRender += new System.EventHandler(this.BtnPreRender);
	}
	/**
	 * 摘要:
	 * 获取或设置一个布尔值，该值指示 System.Web.UI.WebControls.Button 控件使用客户端浏览器的提交机制还是 ASP.NET回发机制。
	 * 返回结果:
	 * 如果该控件使用了客户端浏览器的提交机制，则为 true；否则为 false。默认值为 true。
	 */
	private boolean UseSubmitBehavior;
	public boolean getUseSubmitBehavior()
	{
		return UseSubmitBehavior;
	}
	public void setUseSubmitBehavior(boolean value)
	{
		UseSubmitBehavior = value;
	}
	/**
	 * 摘要:
	 * 获取由 ASP.NET 生成的 HTML 标记的控件 ID。
	 * 返回结果:由 ASP.NET 生成的 HTML 标记的控件 ID。
	 */
	private String ClientID;
	public String getClientID()
	{
		return ClientID;
	}
	
	private void BtnPreRender(Object sender)
	{
		//this.Attributes["onclick"] +="javascript:showRuning();";
//			if (this.Hit!=null)
//				this.Attributes["onclick"] = "javascript: return confirm('是否继续？'); ";
		switch (this.getShowType())
		{
			case ConfirmHit :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("确认(A)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "a";
				}

				this.attributes.put("onclick"," return confirm('" + this.getHit() + "');");
				break;

			case Refurbish :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("刷新(R)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "r";
				}
				break;
			case Back :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("返回(B)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "b";
				}
				break;
			case Edit :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("修改(E)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "e";
				}
				break;
			case Close :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("关闭(Q)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "q";
				}

				this.attributes.put("onclick"," window.close(); return false");

				break;
			case Cancel :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("取消(C)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "c";
				}
				break;
                   
			case Confirm :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("确定(O)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "o";
				}
				break;
			case Search :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("查找(F)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "f";
				}
				break;
			case New :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("新建(N)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "n";
				}
				break;
			case SaveAndNew :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("保存并新建(R)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "n";
				}
				break;
			case Delete :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("删除(D)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "c";
				}
				if (this.getHit() == null)
				{
					this.attributes.put("onclick"," return confirm('此操作要执行删除，是否继续？');");
				}
				else
				{
					this.attributes.put("onclick"," return confirm('此操作要执行删除　[" + this.getHit() + "]，是否继续？');");
				}
				break;
			case Export :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("导出(G)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "g";
				}
				break;
			case Insert :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("插入(I)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "i";
				}
				break;
			case Print :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("打印(P)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "p";
				}

				if (this.getHit() == null)
				{
					this.attributes.put("onclick"," return confirm('此操作要执行打印，是否继续？');");
				}
				else
				{
					this.attributes.put("onclick"," return confirm('此操作要执行打印　[" + this.getHit() + "]，是否继续？');");
				}
				break;
			case Save :
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("保存(S)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "s";
				}
				break;
			case View:
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("浏览(V)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "v";
				}
				break;
			case Add:
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("增加(A)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "a";
				}
				break;
			case SelectAll:
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("全选择(A)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "a";
				}
				break;
			case SelectNone:
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("全不选(N)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "n";
				}
				break;
			case Reomve:
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("移除(M)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "m";
				}

				if (this.getHit() == null)
				{
					this.attributes.put("onclick"," return confirm('此操作要执行移除，是否继续？');");
				}
				else
				{
					this.attributes.put("onclick"," return confirm('此操作要执行移除　[" + this.getHit() + "]，是否继续？');");
				}

				break;
			default:
				if (this.getText() == null || this.getText().equals(""))
				{
					this.setText("确定(O)");
				}
				if (this.accessKey == null)
				{
					this.accessKey = "o";
				}
				break;
		}

	}
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		String sType = "";
		if(this.getType().equals(""))
			sType = "button";
		else
			sType = this.getType();
		str.append("<input type=\""+sType+"\" ");
		str.append(" id=\""+this.getId()+"\" ");
		str.append(" name = \""+this.getName()+"\" ");
		str.append(" value = \""+this.getText()+"\"");
		if(!this.getVisible())
		{
			if(StringHelper.isNullOrEmpty(style)){
				str.append(" style = \"display:none; \"");
			}else{
				str.append(style);
			}
		}
		if(!this.getCssClass().equals(""))
			str.append(" class = \""+this.getCssClass()+"\" ");
		if(this.getReadOnly() || !this.getEnabled())
			str.append(" readonly=\"readonly\" ");
//		if(this.getImageUrl()!=null && !this.getImageUrl().equals(""))
//			str.append("scr="+this.getImageUrl()+" ");
		str.append(this.buildAttributes());
		str.append(">");
		str.append("</input>");
		return str.toString();
	}


}