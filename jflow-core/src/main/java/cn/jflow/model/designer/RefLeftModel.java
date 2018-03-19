package cn.jflow.model.designer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.En.AttrOfOneVSM;
import BP.En.AttrsOfOneVSM;
import BP.En.ClassFactory;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.RefMethods;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;

public class RefLeftModel {

	private String basePath;
	private int ItemCount = 0;
	private HttpServletRequest _request = null;
	// / <summary>
	// / 结点属性左侧功能菜单第一项的默认图标
	// / </summary>
	private String IconFirstDefault = "WF/Img/Home.gif";

	// / <summary>
	// / 结点属性左侧功能菜单多对多的默认图标
	// / </summary>
	private String IconM2MDefault = "WF/Img/M2M.png";

	// / <summary>
	// / 结点属性左侧功能菜单明细的默认图标
	// / </summary>
	private String IconDtlDefault = "WF/Img/Btn/Dtl.gif";

	// / <summary>
	// / 是否显示结点属性左侧功能菜单默认图标
	// / </summary>
	private boolean ShowIconDefault = true;

	public RefLeftModel(HttpServletRequest request, String basePath) {
		this.basePath = basePath;
		this._request = request;
	}

	/*
	 * public final String getEnName() throws Exception { String enName =
	 * this._request.getParameter("EnName"); String ensName =
	 * this._request.getParameter("EnsName"); if
	 * (StringHelper.isNullOrEmpty(enName) &&
	 * StringHelper.isNullOrEmpty(ensName)) throw new Exception("@缺少参数"); if
	 * (StringHelper.isNullOrEmpty(enName)) { Entities ens =
	 * ClassFactory.GetEns(this.getEnsName()); enName =
	 * ens.getGetNewEntity().toString(); } return enName; }
	 */
	public final String getEnName() throws Exception {
		String enName = this._request.getParameter("EnName");
		String ensName = this._request.getParameter("EnsName");
		if (StringHelper.isNullOrEmpty(enName)
				&& StringHelper.isNullOrEmpty(ensName))
			throw new Exception("@缺少参数");
		if (StringHelper.isNullOrEmpty(enName)) {
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			enName = ens.getGetNewEntity().toString();
		}
		return enName;
	}

	public final String getEnsName() throws Exception {
		String enName = this._request.getParameter("EnName");
		String ensName = this._request.getParameter("EnsName");
		if (StringHelper.isNullOrEmpty(enName)
				&& StringHelper.isNullOrEmpty(ensName))
			throw new Exception("@缺少参数");

		if (StringHelper.isNullOrEmpty(ensName)) {
			Entity en = ClassFactory.GetEn(this.getEnName());
			ensName = en.getGetNewEntities().toString();
		}
		return ensName;
	}

	public final String getPK() throws Exception {
		String pk = this._request.getParameter("PK");
		if (StringHelper.isNullOrEmpty(pk)) {
			pk = this._request.getParameter("No");
		}
		if (StringHelper.isNullOrEmpty(pk)) {
			pk = this._request.getParameter("RefNo");
		}
		if (StringHelper.isNullOrEmpty(pk)) {
			pk = this._request.getParameter("OID");
		}
		if (StringHelper.isNullOrEmpty(pk)) {
			pk = this._request.getParameter("MyPK");
		}
		if (StringHelper.isNullOrEmpty(pk)) {
			Entity mainEn = ClassFactory.GetEn(this.getEnName());
			pk = this._request.getParameter(mainEn.getPK());
		}
		return pk;
	}
	
	public final String getFk_Flow() {
		return this._request.getParameter("FK_Flow");
	}
	public final String getWFSta() {
		return this._request.getParameter("WFSta");
	}
	public final String getWFState() {
		return this._request.getParameter("WFState");
	}
	public final String TSpan() {
		return this._request.getParameter("TSpan");
	}
	public final String getAttrKey() {
		if (_request.getParameter("AttrKey") == null)
			return "";
		return _request.getParameter("AttrKey");
	}

	public StringBuilder Pub1 = null;

	public void init() {
		this.Pub1 = new StringBuilder();
		try {
			Entity en = BP.En.ClassFactory.GetEn(this.getEnName());
			if (StringHelper.isNullOrEmpty(getPK()))
				return;

			if (en == null)
				throw new Exception(this.getEnsName() + " " + this.getEnName());

			if (en.getEnMap().getAttrsOfOneVSM().size()
					+ en.getEnMap().getDtls().size()
					+ en.getEnMap().getHisRefMethods().size() == 0)
				return;

			en.setPKVal(this.getPK());
			String keys = "&" + en.getPK() + "=" + this.getPK() + "&FK_Flow="+this.getFk_Flow()+"&WFSta="+this.getWFSta()+"&WFState="+this.getWFState()+"&TSpan="+this.TSpan()+"&r="
					+ DataType.dateToStr(new Date(), "MMddhhmmss");
			String titleKey = "";
			if (en.getEnMap().getAttrs().Contains("Name"))
				titleKey = "Name";
			else if (en.getEnMap().getAttrs().Contains("Title"))
				titleKey = "Title";
			String desc = en.getEnDesc();
			if (!"".equals(titleKey)) {
				en.RetrieveFromDBSources();
				desc = en.GetValStrByKey(titleKey);
				if (desc.length() > 30)
					desc = en.getEnDesc();
			}
			Map<String, List<LeftMenuItem>> dictDefs = new HashMap<String, List<LeftMenuItem>>();
			// 新实例化的Map进行遍历，无意义，故注释掉 2016-7-8
//			for(Map.Entry<String, List<LeftMenuItem>> entry :dictDefs.entrySet())
//			{
//				List<LeftMenuItem> list =  entry.getValue();
//				for (int i =0; i<list.size();i++)
//				{
//					System.out.println("entry.getKey() : "+entry.getKey()+" i : "+list.get(i));
//				}
//				
//			}
			AddGroupedLeftItem(
					dictDefs,
					"默认组",
					new LeftMenuItem(this.basePath,
							(titleKey.equals("Title") ? "主页" : desc), String
									.format("UIEn.jsp?EnName=%1$s&PK=%2$s",
											getEnName(), getPK()),
							IconFirstDefault, false, null));

			// #region 加入一对多的实体编辑
			AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
			String sql = "";
			int i = 0;

			if (oneVsM.size() > 0) {
				for (AttrOfOneVSM vsM : oneVsM) {
					String url = basePath
							+ "WF/Comm/RefFunc/Dot2Dot.jsp?EnsName="
							+ en.getGetNewEntities().getClass().getName()
							+ "&EnName=" + this.getEnName() + "&AttrKey="
							+ vsM.getEnsOfMM().getClass().getName() + keys;
					try {
						sql = "SELECT COUNT(*) as NUM FROM "
								+ vsM.getEnsOfMM().getGetNewEntity().getEnMap()
										.getPhysicsTable() + " WHERE "
								+ vsM.getAttrOfOneInMM() + "='" + en.getPKVal()
								+ "'";
						i = DBAccess.RunSQLReturnValInt(sql);
					} catch (Exception e) {
						sql = "SELECT COUNT(*) as NUM FROM "
								+ vsM.getEnsOfMM().getGetNewEntity().getEnMap()
										.getPhysicsTable() + " WHERE "
								+ vsM.getAttrOfOneInMM() + "=" + en.getPKVal();
						try {
							i = DBAccess.RunSQLReturnValInt(sql);
						} catch (Exception ee) {
							vsM.getEnsOfMM().getGetNewEntity()
									.CheckPhysicsTable();
						}
					}
					if (i == 0) {
						if ((this.getAttrKey()).equals(vsM.getEnsOfMM()
								.getClass().getName())) {

							AddGroupedLeftItem(
									dictDefs,
									"默认组",
									new LeftMenuItem(this.basePath, vsM
											.getDesc(), url, IconM2MDefault,
											true, null));
							setItemCount(getItemCount() + 1);

						} else {

							AddGroupedLeftItem(
									dictDefs,
									"默认组",
									new LeftMenuItem(this.basePath, vsM
											.getDesc(), url, IconM2MDefault,
											false, null));
							setItemCount(getItemCount() + 1);

						}
					} else {
						if ((this.getAttrKey()).equals(vsM.getEnsOfMM()
								.getClass().getName())) {

							AddGroupedLeftItem(
									dictDefs,
									"默认组",
									new LeftMenuItem(this.basePath, vsM
											.getDesc() + "[" + i + "]", url,
											IconM2MDefault, true, null));
							setItemCount(getItemCount() + 1);

						} else {
							AddGroupedLeftItem(
									dictDefs,
									"默认组",
									new LeftMenuItem(this.basePath, vsM
											.getDesc() + "[" + i + "]", url,
											IconM2MDefault, false, null));
							setItemCount(getItemCount() + 1);

						}
					}
				}
			}
			// #region 加入它们的方法
			Map<String, List<LeftMenuItem>> dictGrps = new HashMap<String, List<LeftMenuItem>>();

			boolean haveGroup = false;

			RefMethods myreffuncs = en.getEnMap().getHisRefMethods();
			for (RefMethod func : myreffuncs) {
				if (!func.Visable || null != func.RefAttrKey)
					continue;

				haveGroup = !StringHelper.isNullOrEmpty(func.GroupName);

				if (func.refMethodType != RefMethodType.Func) {
					String myurl = func.Do(null).toString();
//					 int h = func.Height;

					if (func.refMethodType == RefMethodType.RightFrameOpen) {
						AddGroupedLeftItem(haveGroup ? dictGrps : dictDefs,
								haveGroup ? func.GroupName : "默认组",
								new LeftMenuItem(this.basePath, func.Title,
										"javascript:OpenUrlInRightFrame(this,'"
												+ myurl + "')", func.Icon,
										false, func.ToolTip));
						setItemCount(getItemCount() + 1);

						continue;
					}
					if (func.Target == null || "".equals(func.Target)) {

						AddGroupedLeftItem(haveGroup ? dictGrps : dictDefs,
								haveGroup ? func.GroupName : "默认组",
								new LeftMenuItem(this.basePath, func.Title,
										myurl, func.Icon, false, func.ToolTip));
						setItemCount(getItemCount() + 1);

					} else {

						AddGroupedLeftItem(haveGroup ? dictGrps : dictDefs,
								haveGroup ? func.GroupName : "默认组",
								new LeftMenuItem(this.basePath, func.Title,
										"javascript:WinOpen('" + myurl + "', '"
												+ func.Target + "')",
										func.Icon, false, func.ToolTip));
						setItemCount(getItemCount() + 1);

					}
					continue;
				}

				//String url = basePath + "WF/Comm/RefMethod.jsp?Index="
				/*String url = basePath + "WF/WorkOpt/OneWork/Track.jsp?Index="
						+ func.Index + "&EnsName="
						+ en.getGetNewEntities().getClass().getName() + keys;*/
				String url = basePath + "WF/Comm/RefMethod.htm?Index=" + func.Index + "&EnsName=" + en.getGetNewEntities() + keys;

				if (func.Warning == null || "".equals(func.Warning)) {
					if (func.Target == null || "".equals(func.Target)) {

						AddGroupedLeftItem(haveGroup ? dictGrps : dictDefs,
								haveGroup ? func.GroupName : "默认组",
								new LeftMenuItem(this.basePath, func.Title,
										url, func.Icon, false, func.ToolTip));
						setItemCount(getItemCount() + 1);

					} else {

						AddGroupedLeftItem(haveGroup ? dictGrps : dictDefs,
								haveGroup ? func.GroupName : "默认组",
								new LeftMenuItem(this.basePath, func.Title,
										"javascript:WinOpen('" + url + "', '"
												+ func.Target + "')",
										func.Icon, false, func.ToolTip));
						setItemCount(getItemCount() + 1);
					}
				} else {
					if (func.Target == null || "".equals(func.Target)) {

						AddGroupedLeftItem(
								haveGroup ? dictGrps : dictDefs,
								haveGroup ? func.GroupName : "默认组",
								new LeftMenuItem(
										this.basePath,
										func.Title,
										"javascript: if ( confirm('"
												+ func.Warning
												+ "')){{ window.location.href='"
												+ url + "' }}", func.Icon,
										false, func.ToolTip));
						setItemCount(getItemCount() + 1);
					} else {
						AddGroupedLeftItem(haveGroup ? dictGrps : dictDefs,
								haveGroup ? func.GroupName : "默认组",
								new LeftMenuItem(this.basePath, func.Title,
										"javascript: if ( confirm('"
												+ func.Warning
												+ "')){{ WinOpen('" + url
												+ "', '" + func.Target
												+ "') }}", func.Icon, false,
										func.ToolTip));
						setItemCount(getItemCount() + 1);

					}
				}
			}
			// #endregion

			// #region 加入他的明细
			EnDtls enDtls = en.getEnMap().getDtls();
			for (EnDtl enDtl : enDtls) {
				String url = basePath + "WF/Comm/RefFunc/Dtl.jsp?EnName="
						+ this.getEnName() + "&PK=" + this.getPK()
						+ "&EnsName=" + enDtl.getEns().getClass().getName()
						+ "&RefKey=" + enDtl.getRefKey() + "&RefVal="
						+ en.getPKVal().toString() + "&MainEnsName="
						+ en.getClass().getName() + keys;

				try {
					i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM "
							+ enDtl.getEns().getGetNewEntity().getEnMap()
									.getPhysicsTable() + " WHERE "
							+ enDtl.getRefKey() + "='" + en.getPKVal() + "'");
				} catch (Exception e) {
					try {
						i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM "
								+ enDtl.getEns().getGetNewEntity().getEnMap()
										.getPhysicsTable() + " WHERE "
								+ enDtl.getRefKey() + "=" + en.getPKVal());
					} catch (Exception ee) {
						enDtl.getEns().getGetNewEntity().CheckPhysicsTable();
					}
				}

				if (i == 0) {

					AddGroupedLeftItem(dictDefs, "默认组", new LeftMenuItem(
							this.basePath, enDtl.getDesc(), url,
							IconDtlDefault, false, null));
					setItemCount(getItemCount() + 1);

				} else {

					AddGroupedLeftItem(dictDefs, "默认组", new LeftMenuItem(
							this.basePath, enDtl.getDesc() + "[" + i + "]",
							url, IconDtlDefault, false, null));
					setItemCount(getItemCount() + 1);

				}
			}

			if (dictGrps.isEmpty()) {
				this.Pub1
						.append(BaseModel
								.Add("<div class='easyui-accordion' data-options='fit:true'>"));
				// 增加默认组
				this.Pub1.append(BaseModel
						.Add("<div title='基本功能' style='overflow:auto;'>"));
				this.Pub1.append(BaseModel.AddUL("class='navlist'"));

				for (LeftMenuItem item : dictDefs.get("默认组")) {
					this.Pub1.append(BaseModel.AddLi(item.getLiString()));
				}

				this.Pub1.append(BaseModel.AddULEnd());
				this.Pub1.append(BaseModel.AddDivEnd());
				this.Pub1.append(BaseModel.AddDivEnd());
			} else {
				this.Pub1
						.append(BaseModel
								.Add("<div class='easyui-accordion' data-options='fit:true'>"));

				// 增加默认组
				this.Pub1.append(BaseModel
						.Add("<div title='基本功能' style='overflow:auto;'>"));
				this.Pub1.append(BaseModel.AddUL("class='navlist'"));

				for (LeftMenuItem item : dictDefs.get("默认组")) {
					this.Pub1.append(BaseModel.AddLi(item.getLiString()));
				}

				this.Pub1.append(BaseModel.AddULEnd());
				this.Pub1.append(BaseModel.AddDivEnd());

				// 增加RefMethod分组
				for (java.util.Map.Entry<String, List<LeftMenuItem>> grp : dictGrps
						.entrySet()) {
					this.Pub1.append(BaseModel.Add("<div title='"
							+ grp.getKey() + "' style='overflow:auto;'>"));
					this.Pub1.append(BaseModel.AddUL("class='navlist'"));

					for (LeftMenuItem item : grp.getValue()) {
						this.Pub1.append(BaseModel.AddLi(item.getLiString()));
					}

					this.Pub1.append(BaseModel.AddULEnd());
					this.Pub1.append(BaseModel.AddDivEnd());
				}

				this.Pub1.append(BaseModel.AddDivEnd());
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.Pub1.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}

	}

	// / <summary>
	// / 获取结点属性左侧功能菜单项默认前置图标
	// / <para></para>
	// / <para>根据本页中设置的ShowIconDefault与IconXXXDefault来生成</para>
	// / </summary>
	// / <param name="imgPath">图标的相对路径，空则为默认明细的图标</param>
	// / <returns></returns>
	private String GetIcon(String imgPath) {
		if (!this.ShowIconDefault)
			return "";

		imgPath = StringHelper.isNullOrEmpty(imgPath) ? IconDtlDefault
				: imgPath;
		if (!imgPath.contains("http")) {
			return "<img src='" + this.basePath + imgPath
					+ "' width='16' border='0' />";
		} else {
			return "<img src='" + imgPath + "' width='16' border='0' />";
		}
	}

	private String GetBasePathIcon(String imgPath) {
		if (!this.ShowIconDefault)
			return "";

		imgPath = StringHelper.isNullOrEmpty(imgPath) ? IconDtlDefault
				: imgPath;
		if (!imgPath.contains("http")) {
			return "<img src='" + this.basePath + imgPath
					+ "' width='16' border='0' />";
		} else {
			return "<img src='" + imgPath + "' width='16' border='0' />";
		}
	}

	public int getItemCount() {
		return ItemCount;
	}

	public void setItemCount(int itemCount) {
		ItemCount = itemCount;
	}

	/**
	 * 左侧功能菜单项对象
	 */
	private class LeftMenuItem {
		/**
		 * 左侧功能菜单项对象
		 * 
		 * @param ccflowPath
		 *            CCFlow的根Url路径，如http://192.168.1.1/
		 * @param text
		 *            文本
		 * @param url
		 *            链接URL
		 * @param iconImg
		 *            图标的路径
		 * @param isSelection
		 *            是否处于选中状态
		 * @param tooltip
		 *            鼠标移动到功能项上的鼠标显示信息
		 */
		// string url, string iconImg, bool isSelection, string tooltip = null)
		public LeftMenuItem(String ccflowPath, String text, String url,
				String iconImg, boolean isSelection, String tooltip) {
			setCCFlowPath(ccflowPath);
			setText(text);
			setUrl(url);
			setIconImg(iconImg);
			setIsSelection(isSelection);
		}

		/**
		 * CCFlow的根Url路径，如http://192.168.1.1/
		 */
		private String privateCCFlowPath;

		public final String getCCFlowPath() {
			return privateCCFlowPath;
		}

		public final void setCCFlowPath(String value) {
			privateCCFlowPath = value;
		}

		/**
		 * 文本
		 */
		private String privateText;

		public final String getText() {
			return privateText;
		}

		public final void setText(String value) {
			privateText = value;
		}

		/**
		 * 链接URL
		 */
		private String privateUrl;

		public final String getUrl() {
			return privateUrl;
		}

		public final void setUrl(String value) {
			privateUrl = value;
		}

		/**
		 * 图标的路径
		 */
		private String privateIconImg;

		public final String getIconImg() {
			return privateIconImg;
		}

		public final void setIconImg(String value) {
			privateIconImg = value;
		}

		/**
		 * 是否处于选中状态
		 */
		private boolean privateIsSelection;

		public final boolean getIsSelection() {
			return privateIsSelection;
		}

		public final void setIsSelection(boolean value) {
			privateIsSelection = value;
		}

		/**
		 * 鼠标移动到功能项上的鼠标显示信息
		 */
		private String privateToolTip;

		public final String getToolTip() {
			return privateToolTip;
		}

		public final void setToolTip(String value) {
			privateToolTip = value;
		}

		/**
		 * 获取增加li的字符串
		 */
		public final String getLiString() {
			return String
					.format("<div%5$s><a href=\"%1$s\"%6$s>%4$s<span class='nav'>%2$s</span></a></div>%3$s",
							getUrl(),
							getText(),
							"\r\n",
							GetIcon(getIconImg()),
							getIsSelection() ? " style='font-weight:bold'" : "",
							StringHelper.isNullOrEmpty(getToolTip()) ? ""
									: (" title='" + getToolTip() + "'"));
		}

	}

	/**
	 * 增加分组中的项
	 * 
	 * @param 
	 *            分组集合
	 * @param group
	 *            所加项的组名
	 * @param item
	 *            所加项
	 */
	private void AddGroupedLeftItem(Map<String, List<LeftMenuItem>> dictGrps,
			String group, LeftMenuItem item) {
		if (StringHelper.isNullOrEmpty(group)) {
			group = "默认组";
		}

		if (!dictGrps.containsKey(group)) {
			dictGrps.put(group, new ArrayList<LeftMenuItem>());
		}

		dictGrps.get(group).add(item);
	}
}
