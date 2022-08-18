layui.define([ 'jquery'], function (exports) {
    "use strict";
    var plant = 'CCFlow',
        $ = layui.jquery;
    var IsIELower10 = true;
    var dynamicHandler = '';
    var HttpHandler = (function () {

        var parameters;
        var basePath = basePath();

        if (IsIELower10 == true)
            parameters = {};
        else
            parameters = new FormData();

        var formData;
        var params = "&";

        function HttpHandler(handlerName) {
            this.handlerName = handlerName;
            if (IsIELower10 == true)
                parameters = {};
            else
                parameters = new FormData();

            formData = undefined;
            params = "&";
        }

        function basePath() {

            //获取当前网址，如： http://localhost:80/jflow-web/index.jsp  
            var curPath = window.location.href; // GetHrefUrl();
            //获取主机地址之后的目录，如： jflow-web/index.jsp  
            var pathName = window.document.location.pathname;
            if (pathName == "/") { //说明不存在项目名
                if ("undefined" != typeof ccbpmPath && ccbpmPath != null && ccbpmPath != "") {
                    if (ccbpmPath != curPath)
                        return ccbpmPath;
                }
                return curPath;
            }
            var pos = curPath.indexOf(pathName);
            //获取主机地址，如： http://localhost:80  
            var localhostPath = curPath.substring(0, pos);
            //获取带"/"的项目名，如：/jflow-web
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

            if ("undefined" != typeof ccbpmPath && ccbpmPath != null && ccbpmPath != "") {
                if (ccbpmPath != localhostPath)
                    return ccbpmPath;
            }

            return localhostPath;

        }

        var webUserJsonString = null;
        var WebUser = function () {
            if (dynamicHandler == "")
                return;
            if (webUserJsonString != null) {
                var self = this;
                $.each(webUserJsonString, function (n, o) {
                    self[n] = o;
                });
                return;
            }


            if (plant == "CCFlow") {
                // CCFlow
                dynamicHandler = basePath + "/WF/Comm/Handler.ashx";
            } else {
                // JFlow
                dynamicHandler = basePath + "/WF/Comm/ProcessRequest.do";
            }
            $.ajax({
                type: 'post',
                async: false,
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                url: dynamicHandler + "?DoType=WebUser_Init&t=" + new Date().getTime(),
                dataType: 'html',
                success: function (data) {

                    if (data.indexOf("err@") != -1) {
                        if (data.indexOf('登录信息丢失') != -1) {
                            mui.alert("登录信息丢失，请重新登录。");
                            return false;
                        } else {
                            mui.alert(data.replace("err@", ""));
                        }
                        SetHref(basePath + "/CCMobilePortal/Login.htm");
                    }

                    try {
                        webUserJsonString = JSON.parse(data);

                    } catch (e) {
                        alert("json解析错误: " + data);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    var url = dynamicHandler + "?DoType=WebUser_Init&t=" + new Date().getTime();
                    ThrowMakeErrInfo("WebUser-WebUser_Init", textStatus, url);
                }
            });
            var self = this;
            $.each(webUserJsonString, function (n, o) {
                self[n] = o;
            });

        };

        function validate(s) {
            if (s == null || typeof s === "undefined") {
                return false;
            }
            s = s.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "");
            if (s == "" || s == "null" || s == "undefined") {
                return false;
            }
            return true;
        }

        if (plant == "CCFlow") {
            // CCFlow
            dynamicHandler = basePath + "/WF/Comm/Handler.ashx";
        } else {
            // JFlow
            dynamicHandler = basePath + "/WF/Comm/ProcessRequest.do";
        }

        HttpHandler.prototype = {

            constructor: HttpHandler,
            AddUrlData: function (url) {
                var queryString = url;
                if (url == null || url == undefined || url == "")
                    queryString = document.location.search.substr(1);
                queryString = decodeURI(queryString);
                var self = this;
                $.each(queryString.split("&"), function (i, o) {
                    var param = o.split("=");
                    if (param.length == 2 && validate(param[1])) {

                        (function (key, value) {

                            if (key == "DoType" || key == "DoMethod" || key == "HttpHandlerName")
                                return;

                            self.AddPara(key, value);

                        })(param[0], param[1]);
                    }
                });

            },

            AddFormData: function () {
                if ($("form").length == 0)
                    throw Error('必须是Form表单才可以使用该方法');

                formData = $("form").serialize();
                //序列化时把空格转成+，+转义成％２Ｂ，在保存时需要把+转成空格  
                formData = formData.replace(/\+/g, " ");
                //form表单序列化时调用了encodeURLComponent方法将数据编码了
                // formData = decodeURIComponent(formData, true);
                if (formData.length > 0) {
                    var self = this;
                    $.each(formData.split("&"), function (i, o) {
                        var param = o.split("=");
                        if (param.length == 2 && validate(param[1])) {
                            (function (key, value) {
                                self.AddPara(key, decodeURIComponent(value, true));
                            })(param[0], param[1]);
                        }
                    });
                }
            },
            AddFileData: function () {
                var files = $("input[type=file]");
                for (var i = 0; i < files.length; i++) {
                    var fileObj = files[i].files[0]; // js 获取文件对象
                    if (typeof (fileObj) == "undefined" || fileObj.size <= 0) {
                        alert("请选择上传的文件.");
                        return;
                    }
                    //parameters["file"] = fileObj;
                    parameters.append("file", fileObj)
                }
            },
            AddPara: function (key, value) {
                if (params.indexOf("&" + key + "=") == -1) {
                    if (IsIELower10 == true)
                        parameters[key] = value;
                    else
                        parameters.append(key, value);
                    params += key + "=" + value + "&";
                }

            },

            AddJson: function (json) {

                for (var key in json) {
                    this.AddPara(key, json[key]);
                }
            },

            Clear: function () {
                if (IsIELower10 == true)
                    parameters = {};
                else
                    parameters = new FormData();
                formData = undefined;
                params = "&";
            },

            getParams: function () {
                //    var params = [];
                //   /* $.each(parameters, function (key, value) {

                //        if (value.indexOf('<script') != -1)
                //            value = '';

                //        params.push(key + "=" + value);

                //    });
                //*/

                //    for (let [name, value] of formData) {
                //        alert(`${name} = ${value}`); // key1=value1，然后是 key2=value2
                //        if (value.indexOf('<script') != -1)
                //            value = '';
                //        params.push(name + "=" + value);
                //    }

                //    //for (var key of parameters.keys()) {
                //    //    var val = formData.get(key);
                //    //    if (val.indexOf('<script') != -1)
                //    //        val = '';
                //    //    params.push(key + "=" + val);

                //    //}


                return params;
            },
            DoMethodReturnString: function (methodName) {
                if (dynamicHandler == "")
                    return;
                var self = this;
                var jsonString;

                if (IsIELower10 == false)
                    $.ajax({
                        type: 'post',
                        async: false,
                        xhrFields: {
                            withCredentials: IsIELower10 == true ? false : true
                        },
                        crossDomain: IsIELower10 == true ? false : true,
                        url: dynamicHandler + "?DoType=HttpHandler&DoMethod=" + methodName + "&HttpHandlerName=" + self.handlerName + "&t=" + Math.random(),
                        data: parameters,
                        dataType: 'html',
                        contentType: false,
                        processData: false,
                        success: function (data) {
                            jsonString = data;

                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            var url = dynamicHandler + "?DoType=HttpHandler&DoMethod=" + methodName + "&HttpHandlerName=" + self.handlerName + "&t=" + Math.random();
                            ThrowMakeErrInfo("HttpHandler-DoMethodReturnString-" + methodName, textStatus, url);


                        }
                    });
                else
                    $.ajax({
                        type: 'post',
                        async: false,
                        xhrFields: {
                            withCredentials: false
                        },
                        crossDomain: false,
                        url: dynamicHandler + "?DoType=HttpHandler&DoMethod=" + methodName + "&HttpHandlerName=" + self.handlerName + "&t=" + Math.random(),
                        data: parameters,
                        dataType: 'html',
                        success: function (data) {
                            jsonString = data;
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            var url = dynamicHandler + "?DoType=HttpHandler&DoMethod=" + methodName + "&HttpHandlerName=" + self.handlerName + "&t=" + Math.random();
                            ThrowMakeErrInfo("HttpHandler-DoMethodReturnString-" + methodName, textStatus, url);


                        }
                    });
                return jsonString;

            },

            DoMethodReturnJSON: function (methodName) {

                var jsonString = this.DoMethodReturnString(methodName);

                if (jsonString.indexOf("err@") == 0) {
                    alert(jsonString);

                    //alert('请查看控制台(DoMethodReturnJSON):' + jsonString);
                    console.log(jsonString);
                    return jsonString;
                }

                try {

                    jsonString = ToJson(jsonString);

                    //jsonString = JSON.parse(jsonString);
                } catch (e) {
                    jsonString = "err@json解析错误: " + jsonString;
                    alert(jsonString);
                    //  console.log(jsonString);
                }
                return jsonString;
            }
        }
        return HttpHandler;

    })();

    exports('HttpHandler', HttpHandler);
})