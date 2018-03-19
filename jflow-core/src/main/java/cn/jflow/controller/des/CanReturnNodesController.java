package cn.jflow.controller.des;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import BP.WF.Node;
import BP.WF.Template.NodeReturn;
import BP.WF.Template.NodeReturnAttr;
import BP.WF.Template.NodeReturns;
import BP.WF.Nodes;
import BP.WF.Template.NodeAttr;

@Controller
@RequestMapping("/DES")
public class CanReturnNodesController extends BaseController {

	@RequestMapping(value = "/CanReturnNodesSave", method = RequestMethod.POST)
	public void canReturnNodesSave(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			//HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			NodeReturns rnds = new NodeReturns();
            rnds.Delete(NodeReturnAttr.FK_Node, map.get("FK_Node"));
            
            Nodes nds = new Nodes();
            nds.Retrieve(NodeAttr.FK_Flow, map.get("FK_Flow"));
            for (Node nd : nds.ToJavaList()){
        	   Object cb = request.getParameter("CB_" + nd.getNodeID());// 选种了为on 不选中为null
			   if (null == cb)continue;
			   
			   NodeReturn nr = new NodeReturn();
               nr.setFK_Node(Integer.parseInt(map.get("FK_Node")));
               nr.setReturnTo(nd.getNodeID());
               nr.Insert();
            }
            
            this.winCloseWithMsg(response, "设置成功");
           
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private HashMap<String, String> getParamsMap(String queryString, String enc) {
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		if (queryString != null && queryString.length() > 0) {
			int ampersandIndex, lastAmpersandIndex = 0;
			String subStr, param, value;
			String[] paramPair;
			do {
				ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
				if (ampersandIndex > 0) {
					subStr = queryString.substring(lastAmpersandIndex,
							ampersandIndex - 1);
					lastAmpersandIndex = ampersandIndex;
				} else {
					subStr = queryString.substring(lastAmpersandIndex);
				}
				paramPair = subStr.split("=");
				param = paramPair[0];
				value = paramPair.length == 1 ? "" : paramPair[1];
				try {
					value = URLDecoder.decode(value, enc);
				} catch (UnsupportedEncodingException ignored) {
				}
				paramsMap.put(param, value);
			} while (ampersandIndex > 0);
		}
		return paramsMap;
	}
}
