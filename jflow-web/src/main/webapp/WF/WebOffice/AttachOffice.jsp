<%@page import="org.apache.http.HttpResponse"%>
<%@page import="org.apache.http.HttpRequest"%>
<%@page import="BP.Web.*"%>
<%@page import="cn.jflow.common.model.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	//属性
	String PKVal = request.getParameter("PKVal");
	String DelPKVal = request.getParameter("DelPKVal");
	String FK_FrmAttachment = request.getParameter("FK_FrmAttachment");
	String DoType = request.getParameter("DoType");
	String FK_Node = request.getParameter("FK_Node");
	String NoOfObj = request.getParameter("NoOfObj");
	String FK_MapData = request.getParameter("FK_MapData");
	String UserName = WebUser.getNo() == "Guest"
			? GuestUser.getName()
			: WebUser.getName();
	boolean ReadOnly = false;
	String FileSavePath = null;
	String RealFileName = null;
	String FileFullName = null;
	String NodeInfo = null;
	boolean IsCheck = false;
	boolean IsSavePDF = false;
	boolean IsMarks = false;
	boolean IsPostBack = false;
	AttachOfficeModel att = new AttachOfficeModel(PKVal, DelPKVal,
			FK_FrmAttachment, DoType, FK_Node, NoOfObj, FK_MapData,
			UserName, ReadOnly, FileSavePath, RealFileName,
			FileFullName, NodeInfo, IsCheck, IsSavePDF, IsMarks,
			IsPostBack, request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>附件编辑</title>

<script src="<%=basePath%>WF/Scripts/jquery-1.7.2.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/jBox/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<link href="<%=basePath%>WF/Scripts/jBox/Skins/Blue/jbox.css"
	rel="stylesheet" type="text/css" />

<style type="text/css">
.btn {
	border: 0;
	background: #4D77A7;
	color: #FFF;
	font-size: 12px;
	padding: 6px 10px;
	margin: 5px;
}
</style>
<script type="text/javascript">
        var isShowAll = false;
        var webOffice = null;
        var strTimeKey;
        var isOpen = false;
        var isInfo = false;
        var marksType = "doc,docx";

        $(function () {
            InitOffice();
        });
        //初始化公文
        function InitOffice() {
            webOffice = document.all.WebOffice1;

            if ($('#fileName').val() != "") {
                OpenWeb("1");
            }
            EnableMenu();
        }
        //设置当前操作用户
        function SetUsers() {
            try {
                webOffice.SetCurrUserName("<%=UserName%>");

                InitShowName();

            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
        }
        //显示指定用户的留痕
        function ShowUserName() {
            /// <summary>
            /// 显当前用户留痕
            /// </summary>

            try {

                var user = $("#marks option:selected").val();

                if (user == "全部" || user == undefined) {
                    if (isShowAll) {
                        webOffice.GetDocumentObject().Application.ActiveWindow.ToggleShowAllReviewers();
                        isShowAll = false;
                    } else {
                        webOffice.GetDocumentObject().Application.ActiveWindow.ToggleShowAllReviewers();
                    }

                } else {
                    //                    if (!isShowAll) {
                    //                        webOffice.GetDocumentObject().Application.ActiveWindow.ToggleShowAllReviewers();
                    //                        webOffice.GetDocumentObject().Application.ActiveWindow.ToggleShowAllReviewers();
                    //                        isShowAll = true;
                    //                    }
                    isShowAll = true;
                    webOffice.GetDocumentObject().Application.ActiveWindow.View.Reviewers(user).Visible = true;
                }
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
        }
        //加载所留痕用户
        function InitShowName() {
            try {
                var count = webOffice.GetRevCount();

                var showName = $("#marks");
                showName.empty();

                var list = "全部,";

                //GetRevInfo(i,int) int=1 获取时间  int=3 获取内容  int=0 获取名字
                for (var i = 1; i <= count; i++) {
                    var strOpt = webOffice.GetRevInfo(i, 0);

                    if (list.indexOf(strOpt) < 0) {
                        list += strOpt + ",";
                    }
                }
                var data = list.split(',');
                for (var i = 0; i < data.length; i++) {

                    if (data[i] != null && data[i] != "") {
                        var option = $("<option>").text(data[i]).val(data[i]);
                        showName.append(option);
                    }
                }

            } catch (e) {

            }
        }

        //隐藏 公文按钮
        function EnableMenu() {
            /// <summary>
            /// 设置按钮
            /// </summary>
            webOffice.HideMenuItem(0x01 + 0x02 + 0x04 + 0x10 + 0x20);
        }
        //设置留痕,显示所有的留痕用户,是否只读文档
        function SetTrack(track) {
            if ("<%=ReadOnly%>" == "True") {
                webOffice.ProtectDoc(1, 2, "");
            }
            else {
                webOffice.ProtectDoc(0, 1, "");
            }
            webOffice.SetTrackRevisions(track);

            SetUsers();
            var types = $('#fileType').val();
            if (marksType.indexOf(types) >= 0) {
                ShowUserName();
            }
        }
        //打开本地文件
        function OpenFile() {
            pageLoadding('正在打开...');
            try {
                if (readOnly()) {
                    return false;
                }

                OpenDoc("open", "doc");
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
            loaddingOut('打开完成');
            return false;
        }
        function OpenTempLate() {
            if (readOnly()) {
                return false;
            }
            LoadTemplate('word', '公文模板', OpenWeb);

            return false;
        }
        //打印公文
        function printOffice() {
            try {
                if (isOpen) {
                    webOffice.PrintDoc(1);
                } else {
                    $.jBox.alert('请打开公文!', '提示');
                }
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
            return false;
        }

        function pageLoadding(msg) {
            $.jBox.tip(msg, 'loading');
        }

        function loaddingOut(msg) {
            $.jBox.tip(msg, 'success');

        }
        function DownLoad() {
            try {
                if (isOpen) {

                    webOffice.ShowDialog(84);
                } else {
                    $.jBox.alert('请打开公文!', '提示');
                }
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
            return false;
        }
        //打开服务器文件
        function OpenWeb(loadtype) {
            pageLoadding('正在打开模板...');
            try {
                var type = $("#fileType").val();
                var fileName = $('#fileName').val();
                var url = location.href + "&action=LoadFile&LoadType=" + loadtype + "&fileName=" + fileName;
                OpenDoc(url, type);
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
            loaddingOut('打开完成。');

        }
        //打开文件
        function OpenDoc(url, type) {
            var openType = webOffice.LoadOriginalFile(url, type);
            if (openType > 0) {

                if ("<%=IsMarks%>" == "True")
                    SetTrack(1);
                else
                    SetTrack(0);

                isOpen = true;
            } else {
                $.jBox.alert('打开文档失败', '异常');
            }
        }
        //加载模板
        function LoadTemplate(type, title, callback) {
            try {
                $.jBox("iframe:/WF/WebOffice/TempLate.aspx?LoadType=" + type, {
                    title: title,
                    width: 800,
                    height: 350,
                    buttons: { '确定': 'ok' },
                    submit: function (v, h, f) {
                        var row = h[0].firstChild.contentWindow.getSelected();
                        if (row == null) {
                            $.jBox.info('请选一个模板');
                            return false;
                        } else {
                            pageLoadding('打开中...');
                            $("#fileName").val(row.Name);
                            $("#fileType").val(row.Type);
                            callback();
                            loaddingOut('打开完成...');

                            return true;
                        }
                    }
                });
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
        }
        //加载红头文件模板
        function overOffice() {
            if (readOnly()) {
                return false;
            }
            if (isOpen) {

                LoadTemplate('over', '套红模板', TaoHong);
            } else {
                $.jBox.alert('请打开公文!', '提示');
            }
            return false;
        }
        //套红头文件
        function TaoHong() {
            try {

                var mark = AddBooks();
                var type = $('#fileType').val();
                var name = $('#fileName').val();

                if (type == "png" || type == "jpg" || type == "bmp") {
                    webOffice.SetFieldValue(mark, window.location.protocol + "//" + window.location.host + "/DataUser/OfficeOverTemplate/" + name, "::JPG::");
                } else {
                    webOffice.SetFieldValue(mark, window.location.protocol + "//" + window.location.host + "/DataUser/OfficeOverTemplate/" + name, "::FILE::");
                }
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
        }
        //保存到服务器
        function saveOffice() {
            if (readOnly()) {
                return false;
            }
            pageLoadding('正在保存...');
            try {
                if (isOpen) {

                    if ("<%=IsCheck%>" == "True") {
                        if (isInfo) {
                            isInfo = false;
                            webOffice.GetDocumentObject().Application.ActiveDocument.Content.InsertAfter("\n<%=NodeInfo%>");
                        }

                    }

                    webOffice.HttpInit();
                    webOffice.HttpAddPostCurrFile("File", "");
                    var src = location.href + "&action=SaveFile";

                    webOffice.HttpPost(src);


                } else {
                    $.jBox.alert('请打开公文!', '提示');

                }
            } catch (e) {

                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');
            }
            loaddingOut('保存完成...');
            var types = $('#fileType').val();
            try {
                if (marksType.indexOf(types) >= 0) {
                    InitShowName();
                    if (isShowAll) {
                        webOffice.GetDocumentObject().Application.ActiveWindow.ToggleShowAllReviewers();
                        webOffice.GetDocumentObject().Application.ActiveWindow.ToggleShowAllReviewers();
                        isShowAll = false;
                    } else {
                        webOffice.GetDocumentObject().Application.ActiveWindow.ToggleShowAllReviewers();
                    }
                    ShowUserName();
                }
            } catch (e) {

            }
            return false;
        }
        //拒绝修订
        function refuseOffice() {
            try {
                if (readOnly()) {
                    return false;
                }
                var vCount = webOffice.GetRevCount();
                var strUserName;
                for (var i = 1; i <= vCount; i++) {
                    strUserName = webOffice.GetRevInfo(i, 0);
                    webOffice.AcceptRevision(strUserName, 1);
                }
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');

            }
            return false;

        }
        //接受修订
        function acceptOffice() {
            try {
                if (readOnly()) {
                    return false;
                }
                webOffice.SetTrackRevisions(4);
            } catch (e) {
                $.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number + "\r\nError Des:" + e.description, '异常');

            }
            return false;
        }
        //文档只读提示
        function readOnly() {
            if ("<%=ReadOnly%>
	" == "True") {
			$.jBox.alert('文档只读不能进行此操作!', '提示');
			return true;
		}
	}
	//加载所有的公文印章
	function sealOffice() {

		if (readOnly()) {
			return false;
		}
		if (isOpen) {
			LoadTemplate('seal', '公文盖章', seal);
		} else {
			$.jBox.alert('请打开公文!', '提示');
		}
		return false;
	}
	//盖章
	function seal() {
		try {
			var name = $('#fileName').val();
			var type = $('#fileType').val();
			var url = window.location.protocol + "//" + window.location.host
					+ "/DataUser/OfficeSeal/" + name;

			var mark = AddBooks();
			//webOffice.InSertFile(url, 8);
			AddPicture(mark, url, 5);

		} catch (e) {
			$.jBox.alert("异常\r\nError:" + e + "\r\nError Code:" + e.number
					+ "\r\nError Des:" + e.description, '异常');

		}
	}
	//添加书签
	function AddBooks() {

		var date = new Date().getFullYear() + "" + new Date().getMonth() + ""
				+ new Date().getDay() + "" + new Date().getHours() + ""
				+ new Date().getMinutes() + "" + new Date().getSeconds();
		var strMarkName = "mark_" + date;
		var obj = new Object(webOffice.GetDocumentObject());
		if (obj != null) {
			var pBookM;
			var pBookMarks;
			// VAB接口获取书签集合
			pBookMarks = obj.Bookmarks;
			try {
				pBookM = pBookMarks(strMarkName);
				return pBookM.Name;
			} catch (e) {
				webOffice.SetFieldValue(strMarkName, "", "::ADDMARK::");
			}
		}
		return strMarkName;
	}
	//通过VBA 来插入图片
	function AddPicture(strMarkName, strBmpPath, vType) {
		//定义一个对象，用来存储ActiveDocument对象
		var obj = new Object(webOffice.GetDocumentObject());
		if (obj != null) {
			var pBookMarks;
			// VAB接口获取书签集合
			pBookMarks = obj.Bookmarks;
			var pBookM;
			// VAB接口获取书签strMarkName
			pBookM = pBookMarks(strMarkName);
			var pRange;
			// VAB接口获取书签strMarkName的Range对象
			pRange = pBookM.Range;
			var pRangeInlines;
			// VAB接口获取书签strMarkName的Range对象的InlineShapes对象
			pRangeInlines = pRange.InlineShapes;
			var pRangeInline;
			// VAB接口通过InlineShapes对象向文档中插入图片
			pRangeInline = pRangeInlines.AddPicture(strBmpPath, 128);
			//设置图片的样式，5为浮动在文字上面
			pRangeInline.ConvertToShape().WrapFormat.TYPE = vType;
			delete obj;
		}
	}
	///插入文件测试
	function InsertFileWeb() {
		var url = window.location.protocol + "//" + window.location.host
				+ "/DataUser/OfficeFile/099/112.docx";
		webOffice.LoadOriginalFile(url, "docx");
	}

	function closeDoc() {
		webOffice.SetCurrUserName("");
		webOffice.closeDoc(0);
	}
</script>

</head>
<body>
	<H2>敬请期待！</H2>
</body>
</html>