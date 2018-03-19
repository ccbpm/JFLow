/*
说明:
1. 该文件被嵌入到 /WF/WorkOpt/WorkCheck.htm 里面去，与WorkCheck.js 一起工作.
2. 为了适合不同的电子签名的需要,集成不同的电子签名厂家.
3. 如果不需要电子签名该文件保留为空.
4, WorkCheck.htm文件引用了jquery 在这里可以使用jQuery 的函数.
*/

//重写电子签名.
function GenerSiganture() {

    if (SignType == null || SignType == undefined)
        return;

    //遍历电子签名的配置数据，里面两个属性 No签名人, SignType=0不签名, 1图片签名, 2电子签名.
    for (var i = 0; i < SignType.length; i++) {
        var st = SignType[i];
        if (st.SignType != 2)
            continue;


        alert(st.No + '   ' + st.SignType);
    }
}

$(document).ready(function () {

    //alert(SignType);

   // alert('请填写电子签名的组件代码..');

});
