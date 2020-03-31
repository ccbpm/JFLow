function Search() {
    var url = "Search.htm?FrmID=" + GetQueryString("FrmID");
    window.location.href = url;
}

function Group() {
    var url = "Group.htm?FrmID=" + GetQueryString("FrmID");
    window.location.href = url;
}

function DraftBox() {
    var url = "Draft.htm?FrmID=" + GetQueryString("FrmID");
    window.location.href = url;
}

function RefBill() {
    alert('尚未完成.');
}


function StartFlow() {
    alert('尚未完成.');
}

function PrintHtml() {
    window.print();
}

function PrintPDF() {
    //需要引入js
    //    <script src="https://cdn.bootcss.com/jspdf/1.5.3/jspdf.debug.js"></script>
    //     <script src="https://cdn.bootcss.com/html2canvas/0.5.0-beta4/html2canvas.min.js"></script>
    html2canvas(
        document.getElementById("export_content"),
        {
            dpi: 172,//导出pdf清晰度
            onrendered: function (canvas) {
                var contentWidth = canvas.width;
                var contentHeight = canvas.height;

                //一页pdf显示html页面生成的canvas高度;
                var pageHeight = contentWidth / 592.28 * 841.89;
                //未生成pdf的html页面高度
                var leftHeight = contentHeight;
                //pdf页面偏移
                var position = 0;
                //html页面生成的canvas在pdf中图片的宽高（a4纸的尺寸[595.28,841.89]）
                var imgWidth = 595.28;
                var imgHeight = 592.28 / contentWidth * contentHeight;

                var pageData = canvas.toDataURL('image/jpeg', 1.0);
                var pdf = new jsPDF('', 'pt', 'a4');

                //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
                //当内容未超过pdf一页显示的范围，无需分页
                if (leftHeight < pageHeight) {
                    pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight);
                } else {
                    while (leftHeight > 0) {
                        pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
                        leftHeight -= pageHeight;
                        position -= 841.89;
                        //避免添加空白页
                        if (leftHeight > 0) {
                            pdf.addPage();
                        }
                    }
                }
                pdf.save('content.pdf');
            },
            //背景设为白色（默认为黑色）
            background: "#fff"
        });
    alert('尚未完成-PrintPDF');
    return;
    var url = "Group.htm?FrmID=" + GetQueryString("FrmID");
    window.location.href = url;
}

function PrintRTF() {
    alert('尚未完成 - PrintRTF');
    return;
    var url = "Group.htm?FrmID=" + GetQueryString("FrmID");
    window.location.href = url;
}

function PrintCCWord() {
    alert('尚未完成 - PrintCCWord');
    return;
    var url = "Group.htm?FrmID=" + GetQueryString("FrmID");
    window.location.href = url;
}

function ExpToZip() {
    alert('尚未完成 - ExpToZip');
    return;
    var url = "Group.htm?FrmID=" + GetQueryString("FrmID");
    window.location.href = url;
}
