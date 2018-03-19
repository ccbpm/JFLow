package BP.WF.Comm;

import java.util.*;

public class EasyuiTreeNode
{
			private String id;
			public final String getid()
			{
				return id;
			}
			public final void setid(String value)
			{
				id = value;
			}
			private String text;
			public final String gettext()
			{
				return text;
			}
			public final void settext(String value)
			{
				text = value;
			}
			private String state;
			public final String getstate()
			{
				return state;
			}
			public final void setstate(String value)
			{
				state = value;
			}
			private boolean checked;
			public final boolean getchecked()
			{
				return checked;
			}
			public final void setchecked(boolean value)
			{
				checked = value;
			}
			private String iconCls;
			public final String geticonCls()
			{
				return iconCls;
			}
			public final void seticonCls(String value)
			{
				iconCls = value;
			}
			private EasyuiTreeNodeAttributes attributes;
			public final EasyuiTreeNodeAttributes getattributes()
			{
				return attributes;
			}
			public final void setattributes(EasyuiTreeNodeAttributes value)
			{
				attributes = value;
			}
			private ArrayList<EasyuiTreeNode> children;
			public final ArrayList<EasyuiTreeNode> getchildren()
			{
				return children;
			}
			public final void setchildren(ArrayList<EasyuiTreeNode> value)
			{
				children = value;
			}
}
