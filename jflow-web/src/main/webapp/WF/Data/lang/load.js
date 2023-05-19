//定义多语言.
//zh-cn   zh-tw  zh-hk  en-us ja-jp ko-kr

$(function () {
    let currentLang = localStorage.getItem("Lange") || 'zh-cn';
    if(currentLang === "null" || currentLang=="undefined")
        currentLang = 'zh-cn';
    Skip.addJs(basePath + "/WF/Data/lang/js/" + currentLang + ".js");
    loadScript(basePath + "/WF/Data/lang/js/" + currentLang + ".js");
});
