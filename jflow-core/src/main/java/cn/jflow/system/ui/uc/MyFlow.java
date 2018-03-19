package cn.jflow.system.ui.uc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.PageBase;

public class MyFlow extends PageBase {

	private ToolBar1 toolbar1;

	public MyFlow(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);

		toolbar1 = new ToolBar1(request, response);
	}

	public ToolBar1 getToolbar1() {
		return toolbar1;
	}

	public void setToolbar1(ToolBar1 toolbar1) {
		this.toolbar1 = toolbar1;
	}

	public List<String> totalInclude() {
		List<String> list = new ArrayList<String>();
		if (this.get_include().size() > 0)
			list.addAll(this.get_include());
		if (this.getToolbar1().get_include().size() > 0)
			list.addAll(this.getToolbar1().get_include());

		return list;
	}

	public List<String> totalScript() {
		List<String> list = new ArrayList<String>();
		if (this.get_script().size() > 0)
			list.addAll(this.get_script());
		if (this.getToolbar1().get_script().size() > 0)
			list.addAll(this.getToolbar1().get_script());

		return list;
	}

}
