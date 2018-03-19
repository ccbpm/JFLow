<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WF/head/head1.jsp"%>
<%  
	ImgAthModel iam=new ImgAthModel(request,response);
	String imgFile=request.getParameter("sourceFile");
	iam.init();
	String src=iam.getSourceFile().equals("")?iam.getMSG():iam.getSourceFile();
	request.setAttribute("src",src);
// 	System.out.print(src);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript">
        //关闭窗体
        function closeWin() {
            window.close();
        }

        function ajaxFileUpload() {
            var ImgAth = "<%=iam.getImgAths() %>";
            var MyPK = "<%=iam.getMyPK() %>";

            $.ajaxFileUpload({
                url: 'UploadFile.jsp?ImgAth=' + ImgAth + '&MyPK=' + MyPK,
                secureuri: false,
                type:"post",
                fileElementId: 'fileToUpload',
                dataType: 'json',
                success: function (data, status) {
                    if (typeof (data.error) != 'undefined') {
                        if (data.error != '') {
                            alert(data.error);
                        }
                        else {
                        	alert(123);
                            var cutW = document.getElementById("cutW").value;
                            var cutH = document.getElementById("cutH").value;
                            document.getElementById('container').innerHTML = '';
                            ImageCut(data.msg, cutW, cutH);
                            document.getElementById('ContentPlaceHolder1_sourceImg').value = data.msg;
                            document.getElementById('ContentPlaceHolder1_newImgUrl').value = data.msg;
                            var btn = document.getElementById("ContentPlaceHolder1_refresh");
                            if (btn) {
                                btn.click();
                            }
                        }
                    }
                },
                error: function (data, status, e) {
                    alert(e);
                }
            });
            return false;
        }
        //照片裁剪代码
        function ImageCut(src, width, height) {
            $("#ContentPlaceHolder1_refresh").hide();
            var cutW = 320;
            var cutH = 220;
            if (width) {
                cutW = parseFloat(width);
            }
            if (height) {
                cutH = parseFloat(height);
            }

            $("#container").html('');
            src = src.replace(/http\:\/\/([\w\W]*?)\//g, "");
            $.fn.bitmapCutter({
                src: src,
                renderTo: '#container',
                holderSize: { width: 420, height: 400 },
                cutterSize: { width: cutW, height: cutH },
                onGenerated: function (newSrc) {//裁完并保存后返回保存后图片地址
                    document.getElementById('ContentPlaceHolder1_newImgUrl').value = newSrc;
                },
                rotateAngle: 90,
                lang: { clockwise: '顺时针旋转{0}度.' }
            });
        }
      
        
        
    </script>
    <style type="text/css">
        img
        {
            border: none;
        }
    </style>
<body>
	<form action="/jflow-web/DES/btnSubmit.do?MyPK=<%=iam.getMyPK() %>&FK_MapData=<%=iam.getFK_MapData() %>&ImgAths=<%=iam.getImgAths() %>&img=<%=src %>" method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td>
					<div style="font-size: 12px; margin-left: 0px; margin-right: 0px;">
						&nbsp;&nbsp;&nbsp; 上传图片:<input id="fileToUpload"
							name="fileToUpload" type="file" />
						<!-- <asp:FileUpload ID="fileToUpload" runat="server" onchange="imageSave()" /> -->
						<!-- <button ID="btnSubmit" class="Btn" runat="server" Text="保 存" OnClick="btnSubmit_Click" /> -->
						<input type="submit" class="Btn" value="保存">
						<!-- <asp:Button ID="btnImageSave" class="Btn" runat="server" Text="保 存" style="display:none;" OnClick="btnImaeSave_Click" /> -->
						&nbsp;&nbsp; <input type="button" class="Btn" value="关闭"
							onclick="closeWin()" />
						<!--  <asp:Button ID="refresh" Text="刷新" class="Btn" Width="0" Height="0" runat="server"
                        OnClick="refresh_Click" /> -->
						<!-- <input type="button" value="刷新" class="Btn" width="0" height="0"> -->
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div id="container"></div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="cutW" id="cutW" />
		<input type="hidden" name="cutH" id="cutH" />
		<input type="hidden" id="sourceImg" name="sourceImg" value="<%=imgFile %>" /> <input
			type="hidden" id="newImgUrl" name="newImgUrl" value="<%=imgFile %>" />
	</form>
</body>
</html>