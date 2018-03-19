package cn.jflow.system.ui.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.lexer.PageAttribute;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import BP.Tools.StringHelper;
import cn.jflow.common.util.ContextHolderUtils;

public class HtmlUtils {
	
	public static HashMap<String, ArrayList<String>> radioGroupMap;

	public static boolean hasNode(Node aNode)
	{
		
		if(aNode instanceof SelectTag)
		{
			String id = ((SelectTag)aNode).getAttribute("id");
			if(StringHelper.isNullOrEmpty(id))
				return false;
			else if(id.indexOf("DDL_")>=0)
				return true;
			else if(id.indexOf("LB")>=0){
				return true;
			}
		}
		if(aNode instanceof InputTag)
		{
			String id = ((InputTag)aNode).getAttribute("id");
			if(StringHelper.isNullOrEmpty(id))
				return false;
			else if(id.indexOf("TB_")>=0 || id.indexOf("CB_")>=0 || id.indexOf("RB_")>=0)
				return true;
			else if(id.contains("HID")){
				return true;
			}
		}
		if(aNode instanceof TextareaTag)
		{
			if(((TextareaTag)aNode).getAttribute("id") == null)
				return false;
			else 
				return true;
		}
		if(aNode instanceof LabelTag){
			String id = ((LabelTag)aNode).getAttribute("for");
			if(id.indexOf("RB_")>=0 || id.indexOf("CB_")>=0){
				return true;
			}
		}
		return false;
	}
	
	private static HttpServletRequest mRequest = ContextHolderUtils.getRequest();
	
	/**
	 * 解析获取的html字符串 
	 * @param content
	 * @param request
	 * @return
	 */
	public static HashMap<String,BaseWebControl> httpParser(String content, HttpServletRequest request) {
		if(null != request){
			mRequest = request;
			return httpParser(content, true);
		}
		return httpParser(content, false);
	}
	
	/**
	 * html属性 遍历到 map
	 * @param v
	 * @return
	 */
	private static HashMap<String, String> parserVectorToMap(Vector v){
		HashMap<String, String> attrs = new HashMap<String, String>();
		int size = v.size();
		for (int i = 0; i < size; i++) {
			if(v.get(i) instanceof PageAttribute){
				PageAttribute pageAttribute = (PageAttribute) v.get(i);
				if(null == pageAttribute || null == pageAttribute.getName()|| null == pageAttribute.getValue()){
					continue;
				}
				String name = pageAttribute.getName();
				if(name.equals("id")
					|| name.equals("name")
					|| name.equals("value")
					|| name.equals("text")
					|| name.equals("type") 
					|| name.equals("class")
					|| name.equals("disabled")
					|| name.equals("readonly")
					|| name.equals("checked")){
					continue;
				}
				attrs.put(name, pageAttribute.getValue());
			}
		}
		return attrs;
	}
	
	/**
	 * 修改控件到拼接html
	 * @param control 被指定修改控件
	 * @param content 当前原拼接html(最新)
	 * @return 修改后的html
	 */
	public static String setCtrlHtml(BaseWebControl control, String content){
		if(null == content){
			return content;
		}
		
		NodeList nodeList = null;
		
		NodeFilter inputFilter = new NodeClassFilter(InputTag.class);
		NodeFilter labelFilter = new NodeClassFilter(LabelTag.class);
		NodeFilter selectFilter = new NodeClassFilter(SelectTag.class);
		NodeFilter textareaFilter = new NodeClassFilter(TextareaTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] {labelFilter, selectFilter, inputFilter, textareaFilter});
		try {
			Parser myParser = Parser.createParser(control.toString(), "UTF-8");
			nodeList = myParser.parse(lastFilter);
		} catch (ParserException e) {
			e.printStackTrace();
		}

		Node[] nodes = nodeList.toNodeArray();
		for (int j = 0; j < nodes.length; j++) {
			NodeFilter tagFilter = null; 
			TagNode ctrlTag = (TagNode) nodes[j];
			if(ctrlTag instanceof InputTag){
				tagFilter = new NodeClassFilter(InputTag.class);
			}else if(ctrlTag instanceof LabelTag){
				tagFilter = new NodeClassFilter(LabelTag.class);
			}else if(ctrlTag instanceof SelectTag){
				tagFilter = new NodeClassFilter(SelectTag.class);
			}else if(ctrlTag instanceof TextareaTag){
				tagFilter = new NodeClassFilter(TextareaTag.class);
			}
			
			content = setCtrl(control, content, tagFilter, ctrlTag);
		}
		return content;
	}

	private static String setCtrl(BaseWebControl control, String content, NodeFilter tagFilter, TagNode ctrlTag) {
		StringBuffer htmlBuffer = new StringBuffer();
		try {
			Parser parser = Parser.createParser(content, "UTF-8");
			Node node = null;
			NodeIterator iterator = parser.elements();
			while(iterator.hasMoreNodes()){
				node =  iterator.nextNode();
				NodeList list = new NodeList();
				node.collectInto(list, tagFilter);
				for (int i = 0; i < list.size(); i++) {
					Node nodeEle = list.elementAt(i);
					if(nodeEle instanceof TagNode){
						TagNode tagNode = (TagNode) nodeEle;
						if(control.getId().equals(tagNode.getAttribute("id"))){
							tagNode.getAttributesEx().clear();
							tagNode.setAttributesEx(ctrlTag.getAttributesEx());
							if(null != tagNode.getChildren() && null != ctrlTag.getChildren()){
								tagNode.getChildren().removeAll();
								tagNode.setChildren(ctrlTag.getChildren());
							}
							if(tagNode instanceof TextareaTag){
								TextareaTag textareaTag = (TextareaTag)ctrlTag;
								if(null != textareaTag && null != textareaTag.getChildren()){
									tagNode.setChildren(textareaTag.getChildren());
								}
							}
							break;
						}else if(tagNode instanceof LabelTag){
							LabelTag label = (LabelTag)tagNode;
							if(StringHelper.isNullOrEmpty(label.getAttribute("for"))){
								break;
							}
							if(label.getAttribute("for").equals(ctrlTag.getAttribute("for"))){
								NodeList nodelist = label.getChildren();
								Node nodeLabel = nodelist.elementAt(0);
								nodeLabel.setText(control.getText());
								break;
							}
						}
					}
				}
				htmlBuffer.append(node.toHtml());
			}
			return htmlBuffer.toString();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	/**
	 * 此方法获取body 失败，不可用
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Deprecated()
	public static String getBodyString(HttpServletRequest request) throws IOException {
		BufferedReader br = request.getReader();
		String inputLine;
		String str = "";
		while ((inputLine = br.readLine()) != null) {
			str += inputLine;
		}
		br.close();
		return str;
	}
	
	public static HashMap<String,BaseWebControl> controlsMap ;
	
	/**
	 * 
	 * @param ret
	 * @param content
	 * @param isRequest
	 * @return
	 */
	public static String httpParserReturnUpdateHtml( String content){
		controlsMap = new HashMap<String, BaseWebControl>();
		
		NodeFilter inputFilter = new NodeClassFilter(InputTag.class);
		NodeFilter labelFilter = new NodeClassFilter(LabelTag.class);
		NodeFilter selectFilter = new NodeClassFilter(SelectTag.class);
		NodeFilter textareaFilter = new NodeClassFilter(TextareaTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] {labelFilter, selectFilter, inputFilter, textareaFilter});
		
		StringBuffer htmlBuffer = new StringBuffer();
		Parser parser = Parser.createParser(content, "UTF-8");
		Node node = null;
		NodeIterator iterator;
		try {
			iterator = parser.elements();
			while(iterator.hasMoreNodes()){
				node =  iterator.nextNode();
				NodeList list = new NodeList();
				node.collectInto(list, lastFilter);
				int size = list.size();
				for (int i = 0; i < size; i++) {
					Node anode = list.elementAt(i);
					if(!hasNode(anode))
						continue;
					if (anode instanceof SelectTag) {
						
						SelectTag selectnode = (SelectTag) anode;
						
						Vector v = selectnode.getAttributesEx();
						String optionText = mRequest.getParameter(selectnode.getAttribute("id"));
						if(((SelectTag)anode).getAttribute("id").indexOf("DDL_")>=0){
							DDL ddl = new DDL();
							NodeList nl = selectnode.getChildren();
							Node[] nl_nodes = nl.toNodeArray();
							for (int j = 0; j < nl_nodes.length; j++) {
								Node optnode = (Node) nl_nodes[j];
								if (optnode instanceof OptionTag) {
									OptionTag opttag = (OptionTag) optnode;
									if(null != opttag.getAttribute("selected")){
										opttag.removeAttribute("selected");
									}
								
									Vector vv = opttag.getAttributesEx();
									ListItem item = new ListItem(opttag.getOptionText(), opttag.getValue());
									ddl.Items.add(item);
									if(!StringHelper.isNullOrEmpty(optionText)){
										if(optionText.equals(opttag.getValue())){
											((OptionTag) optnode).setAttribute("selected", "\"selected\" ");
											ddl.SetSelectItem(opttag.getValue());
										}
									}
								}
							}
							ddl.setName(selectnode.getAttribute("name"));
							ddl.setId(selectnode.getAttribute("id"));
							ddl.setCssClass(selectnode.getAttribute("class"));
							if(selectnode.toHtml().contains("disabled")){
								ddl.setEnabled(false);
							}
							if(selectnode.toHtml().contains("readonly")){
								ddl.setReadOnly(true);
							}
							ddl.attributes = parserVectorToMap(v);
							controlsMap.put(ddl.getId(),ddl);
						} 
					} else if (anode instanceof InputTag) {
						InputTag inputnode = (InputTag) anode;
						Vector v = inputnode.getAttributesEx();
						if (inputnode.toHtml().contains("checkbox")) {
							CheckBox cb = new CheckBox();
							cb.setId(inputnode.getAttribute("id"));
							cb.setName(inputnode.getAttribute("name"));
							cb.setCssClass(inputnode.getAttribute("class"));
							cb.setValue(StringHelper.isEmpty(inputnode.getAttribute("value"), ""));
							if(null != inputnode.getAttribute("checked")){
								inputnode.removeAttribute("checked");
							}
							String value = mRequest.getParameter(inputnode.getAttribute("id"));
							if( null == value){
								cb.setChecked(false);
							}else{
								cb.setChecked(true);
								inputnode.setAttribute("checked", "\"checked\"");
							}
							
							if(inputnode.toHtml().contains("disabled")){
								cb.setEnabled(false);
							}
								
							if(inputnode.toHtml().contains("readonly")){
								cb.setReadOnly(true);
							}
							cb.attributes = parserVectorToMap(v);
							controlsMap.put(cb.getId(), cb);
							continue;
						} else if (inputnode.toHtml().contains("radio")) {
							RadioButton rb = new RadioButton();
							rb.setId(inputnode.getAttribute("id"));
							rb.setName(inputnode.getAttribute("name"));
							rb.setCssClass(inputnode.getAttribute("class"));
							rb.setValue(StringHelper.isEmpty(inputnode.getAttribute("value"), ""));
							if(null != inputnode.getAttribute("checked")){
								inputnode.removeAttribute("checked");
							}
							String value = mRequest.getParameter(rb.getId());
							if(null == value){
								rb.setChecked(false);
							}else{
								rb.setChecked(true);
								inputnode.setAttribute("checked", "\"checked\"");
							}
							if(inputnode.toHtml().contains("disabled")){
								rb.setEnabled(false);
							}
								
							if(inputnode.toHtml().contains("readonly")){
								rb.setReadOnly(true);
							}
							rb.attributes = parserVectorToMap(v);
							controlsMap.put(rb.getId(), rb);
							continue;
						} else {
							// fe.set_elementType("input");
							TextBox tb = new TextBox();
							tb.setId(inputnode.getAttribute("id"));
							tb.setName(inputnode.getAttribute("name"));
							String value = mRequest.getParameter(inputnode.getAttribute("id"));
							inputnode.setAttribute("value", value);
							tb.setText(value);
							tb.setCssClass(inputnode.getAttribute("class"));
							if(inputnode.toHtml().contains("disabled")){
								tb.setEnabled(false);
							}
								
							if(inputnode.toHtml().contains("readonly")){
								tb.setReadOnly(true);
							}
							tb.attributes = parserVectorToMap(v);
							controlsMap.put(tb.getId(), tb);
						}
					}else if(anode instanceof TextareaTag){
						TextareaTag textareaTagNode = (TextareaTag) anode;
						TextBox texttarea = new TextBox();
						texttarea.setId(textareaTagNode.getAttribute("id"));
						texttarea.setTextMode(TextBoxMode.MultiLine);
						texttarea.setName(textareaTagNode.getAttribute("name"));
						String value = mRequest.getParameter(textareaTagNode.getAttribute("id"));
						 
						textareaTagNode.setText(value);
						texttarea.setText(value);
						texttarea.setCssClass(textareaTagNode.getAttribute("class"));
						if(textareaTagNode.toHtml().contains("disabled")){
							texttarea.setEnabled(false);
						}
						if(textareaTagNode.toHtml().contains("readonly")){
							texttarea.setReadOnly(true);
						}
							
						// 设置其他属性
						Vector v = textareaTagNode.getAttributesEx();
						texttarea.attributes = parserVectorToMap(v);
						textareaTagNode.getText();
						controlsMap.put(texttarea.getId(), texttarea);
						
					} else if(anode instanceof LabelTag){
						LabelTag labelTag = (LabelTag) anode;
						String id = labelTag.getAttribute("for");
						BaseWebControl control = controlsMap.get(id);
						if(null != control){
							control.setText(labelTag.getLabel());
							control.setTitle(StringHelper.isEmpty(labelTag.getAttribute("title"), ""));
						}
					}
				
				}
				htmlBuffer.append(node.toHtml());
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return htmlBuffer.toString();
	
	}
	/**
	 * 解析获取的html字符串
	 * @param content
	 * @param isRequest 当前操作是否执行了request请求
	 * @return
	 */
	public static HashMap<String,BaseWebControl> httpParser(String content, boolean isRequest) {
		if(StringHelper.isNullOrEmpty(content)){
			System.out.println("HttpParser 解析的内容为空！");
			return null;
		}
		// 放置radionGroup分组id,Key:Name,Value:Ids
		radioGroupMap = new HashMap<String, ArrayList<String>>();
		HashMap<String,BaseWebControl> ret = new HashMap<String,BaseWebControl>();

		Parser myParser;
		NodeList nodeList = null;

		myParser = Parser.createParser(content, "UTF-8");

		NodeFilter inputFilter = new NodeClassFilter(InputTag.class);
		NodeFilter labelFilter = new NodeClassFilter(LabelTag.class);
		NodeFilter selectFilter = new NodeClassFilter(SelectTag.class);
		NodeFilter textareaFilter = new NodeClassFilter(TextareaTag.class);
		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] {labelFilter, selectFilter, inputFilter, textareaFilter});
		try {
			nodeList = myParser.parse(lastFilter);
			myParser.elements();
		} catch (ParserException e) {
			e.printStackTrace();
		}

		Node[] nodes = nodeList.toNodeArray();

		for (int i = 0; i < nodes.length; i++) {
			Node anode = (Node) nodes[i];
			if(!hasNode(anode))
					continue;
			if (anode instanceof SelectTag) {
				
				SelectTag selectnode = (SelectTag) anode;
				
				Vector v = selectnode.getAttributesEx();
				String optionText = "";
				if(isRequest){
					optionText = mRequest.getParameter(selectnode.getAttribute("id"));
				}
				if(((SelectTag)anode).getAttribute("id").indexOf("DDL_")>=0){
					DDL ddl = new DDL();
					NodeList nl = selectnode.getChildren();
					// 如果为空，这个下拉菜单没有选择项
					if(null != nl){
						Node[] nl_nodes = nl.toNodeArray();
						for (int j = 0; j < nl_nodes.length; j++) {
							Node optnode = (Node) nl_nodes[j];
							if (optnode instanceof OptionTag) {
								OptionTag opttag = (OptionTag) optnode;
								Vector vv = opttag.getAttributesEx();
								ListItem item = new ListItem(opttag.getOptionText(), opttag.getValue());
								ddl.Items.add(item);
								if(isRequest && !StringHelper.isNullOrEmpty(optionText)){
									if(optionText.equals(opttag.getValue())){
										ddl.SetSelectItem(opttag.getValue());
									}
								}else{
									
									if (vv.toString().indexOf("selected") != -1)
										ddl.SetSelectItem(opttag.getValue());
								}
							}
						}
					}
					ddl.setName(selectnode.getAttribute("name"));
					ddl.setId(selectnode.getAttribute("id"));
					ddl.setCssClass(selectnode.getAttribute("class"));
					if(selectnode.toHtml().contains("disabled")){
						ddl.setEnabled(false);
					}
					if(selectnode.toHtml().contains("readonly")){
						ddl.setReadOnly(true);
					}
					ddl.attributes = parserVectorToMap(v);
					ret.put(ddl.getId(),ddl);
				}else{
					ListBox lb=new ListBox();
					NodeList nl = selectnode.getChildren();
					Node[] nl_nodes = nl.toNodeArray();
					for (int j = 0; j < nl_nodes.length; j++) {
						Node optnode = (Node) nl_nodes[j];
						if (optnode instanceof OptionTag) {
							OptionTag opttag = (OptionTag) optnode;
							Vector vv = opttag.getAttributesEx();
							ListItem item = new ListItem(opttag.getOptionText(), opttag.getValue());
							lb.Items.add(item);
							if(isRequest && !StringHelper.isNullOrEmpty(optionText)){
								if(optionText.equals(opttag.getValue())){
									lb.SetSelectItem(opttag.getValue());
								}
							}else{
								
								if (vv.toString().indexOf("selected") != -1)
									lb.SetSelectItem(opttag.getValue());
							}
						}
					}
					lb.setName(selectnode.getAttribute("name"));
					lb.setId(selectnode.getAttribute("id"));
					lb.setCssClass(selectnode.getAttribute("class"));
					if(selectnode.toHtml().contains("disabled")){
						lb.setEnabled(false);
					}
						
					if(selectnode.toHtml().contains("readonly")){
						lb.setReadOnly(true);
					}
					
					lb.attributes = parserVectorToMap(v);
					ret.put(lb.getId(),lb);
				}
			} else if (anode instanceof InputTag) {
				InputTag inputnode = (InputTag) anode;
				Vector v = inputnode.getAttributesEx();
				if (inputnode.toHtml().contains("checkbox")) {
					CheckBox cb = new CheckBox();
					cb.setId(inputnode.getAttribute("id"));
					cb.setName(inputnode.getAttribute("name"));
					cb.setCssClass(inputnode.getAttribute("class"));
					cb.setValue(StringHelper.isEmpty(inputnode.getAttribute("value"), ""));
					if(isRequest){
						String value = mRequest.getParameter(inputnode.getAttribute("id"));
						if( null == value){
							cb.setChecked(false);
						}else{
							cb.setChecked(true);
						}
							
					}else{
						if(inputnode.toHtml().contains("checked")){
							cb.setChecked(true);
						} else{
							cb.setChecked(false);
						}
					}
					
					if(inputnode.toHtml().contains("disabled")){
						cb.setEnabled(false);
					}
						
					if(inputnode.toHtml().contains("readonly")){
						cb.setReadOnly(true);
					}
					cb.attributes = parserVectorToMap(v);
					ret.put(cb.getId(), cb);
					continue;
				} else if (inputnode.toHtml().contains("radio")) {
					RadioButton rb = new RadioButton();
					rb.setId(inputnode.getAttribute("id"));
					rb.setName(inputnode.getAttribute("name"));
					rb.setCssClass(inputnode.getAttribute("class"));
					
					String [] valueArr = StringHelper.isEmpty(inputnode.getAttribute("value"), "").split("_");
					if(null != valueArr && valueArr.length >= 3){
						rb.setValue(valueArr[2]);
					}else{
						rb.setValue(StringHelper.isEmpty(inputnode.getAttribute("value"), ""));
					}
					if(isRequest){
						String value_id = mRequest.getParameter(rb.getId());
						if(null == value_id){
							
							// 过滤分组数据
							String value_name = mRequest.getParameter(rb.getName());
							if(null == value_name){
								rb.setChecked(false);
							}else if(rb.getId().contains(value_name)){
								rb.setChecked(true);
							}else{
								rb.setChecked(false);
							}
							
						}else{
							rb.setChecked(true);
						}
						
					}else{
						if(inputnode.toHtml().contains("checked")){
							rb.setChecked(true);
						} else{
							rb.setChecked(false);
						}
					}
					if(inputnode.toHtml().contains("disabled")){
						rb.setEnabled(false);
					}
						
					if(inputnode.toHtml().contains("readonly")){
						rb.setReadOnly(true);
					}
					rb.attributes = parserVectorToMap(v);
					ret.put(rb.getId(), rb);
					
					// 存储一组RadioGroup中所有的id
					boolean isExit = radioGroupMap.containsKey(rb.getName());
					ArrayList<String> list = null;
					if(isExit){
						list = radioGroupMap.get(rb.getName());
					}else{
						list = new ArrayList<String>();
					}
					list.add(rb.getId());
					radioGroupMap.put(rb.getName(), list);
					continue;
				} else {
					// fe.set_elementType("input");
					TextBox tb = new TextBox();
					tb.setId(inputnode.getAttribute("id"));
					tb.setName(inputnode.getAttribute("name"));
					String value = "";
					if(isRequest){
						value = mRequest.getParameter(inputnode.getAttribute("id"));
					}else{
						value = inputnode.getAttribute("value");
					}
					tb.setText(value);
					tb.setCssClass(inputnode.getAttribute("class"));
					if(inputnode.toHtml().contains("disabled")){
						tb.setEnabled(false);
					}
						
					if(inputnode.toHtml().contains("readonly")){
						tb.setReadOnly(true);
					}
					tb.attributes = parserVectorToMap(v);
					ret.put(tb.getId(), tb);
				}
			}else if(anode instanceof TextareaTag){
				TextareaTag textareaTagNode = (TextareaTag) anode;
				TextBox texttarea = new TextBox();
				texttarea.setId(textareaTagNode.getAttribute("id"));
				texttarea.setTextMode(TextBoxMode.MultiLine);
				texttarea.setName(textareaTagNode.getAttribute("name"));
				String value = "";
				if(isRequest){
					value = mRequest.getParameter(textareaTagNode.getAttribute("id"));
				}else{
					value = textareaTagNode.getValue();
				}
				texttarea.setText(value);
				texttarea.setCssClass(textareaTagNode.getAttribute("class"));
				if(textareaTagNode.toHtml().contains("disabled")){
					texttarea.setEnabled(false);
				}
				if(textareaTagNode.toHtml().contains("readonly")){
					texttarea.setReadOnly(true);
				}
					
				// 设置其他属性
				Vector v = textareaTagNode.getAttributesEx();
				texttarea.attributes = parserVectorToMap(v);
				textareaTagNode.getText();
				ret.put(texttarea.getId(), texttarea);
				
			} else if(anode instanceof LabelTag){
				LabelTag labelTag = (LabelTag) anode;
				String id = labelTag.getAttribute("for");
				BaseWebControl control = ret.get(id);
				if(null != control){
					control.setText(labelTag.getLabel());
					control.setTitle(StringHelper.isEmpty(labelTag.getAttribute("title"), ""));
				}
			}
		}
		return ret;
	}
	
}
