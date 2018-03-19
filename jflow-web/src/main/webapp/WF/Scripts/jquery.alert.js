/**
 * jQuery alert plugin
 * Version 1.0.3  (11/27/2009)
 * @requires jQuery v1.2.6+
 *
 */
;(function($) {
/**
* @name alert
* @type jQuery
* @cat Plugins/alerttip
* @return jQuery
* @author   li xiangyang <lxy19791111@163.com>
* @param    options 可选参数
*/

/**
 * 
 * 此插件旨在替代javascript原生的alert弹出窗口,使我们在表单校验时显示的提示信息对用户来说更友好. 
 * 
 * 
 * @example $('#tip).alert('hello world.');
 * @desc 这是一个最基本的使用方法,它将会在id为tip的元素左边显示一个DIV浮动层.
 *
 *
 * @example $('#tip').alert({
 *  position: 'left',
 *  focus: true,
 *  alertzIndex: 999,
 *  alertClass: 'corner'
 * });
 * @desc 我们可能通过插件提供的options参数,使DIV浮动层显示在我们想要的地方,或者是否在同时使其获得焦点,调整其显示的样式(样式定义请参照jquery.alert.css)
 * 
 */

$.alert = {version: '1.0.3'};
var $alertContainer,$alertMsg,$alertTable;
$.fn.alert = function(msg, options) {
	return this.each(function(){
		var opts = $.extend(true, {}, $.fn.alert.defaults, options || {});
		var alertzIndex = +opts.alertzIndex;
		if(!$('#alertContainer').length){
			$(['<div id="alertContainer">',
					'<table cellspacing="0" cellpadding="0" border="0">',
							'<tbody>',
								'<tr>',
									'<td class="alert_tl" />',
									'<td class="alert_tc">',
										'<div />',
									'</td>',
									'<td class="alert_tr" />',
								'</tr>',
								'<tr>',
									'<td class="alert_ml" />',
									'<td class="alert_mc" id="alertMsg"></td>',
									'<td class="alert_mr" />',
								'</tr>',
								'<tr>',
									'<td class="alert_bl" />',
									'<td class="alert_bc">',
										'<div />',
									'</td>',
									'<td class="alert_br" />',
								'</tr>',
							'</tbody>',
				    '</table>',
			   '</div>'].join('')).appendTo('body');
			$alertContainer = $('#alertContainer').css({position:'absolute',overflow:'hidden'}).hide();
			$alertTable = $alertContainer.find('table');
			$alertMsg = $('#alertMsg');
			if($.fn.bgiframe){
				$alertContainer.bgiframe();
			}
		}// end if
		var $obj = $(this);
		//第二次使用，重新赋值
		if($alertContainer == undefined || $alertTable == undefined || $alertMsg == undefined){
			$alertContainer = $('#alertContainer').css({position:'absolute',overflow:'hidden'}).hide();
			$alertTable = $alertContainer.find('table');
			$alertMsg = $('#alertMsg');
			if($.fn.bgiframe){
				$alertContainer.bgiframe();
			}
		}
		// 提示内容
	    $alertMsg.html(msg);
	    // 显示tuna_alert 为提示信息的容器
	    $alertTable.removeClass().addClass('alert-' + opts.alertClass);
	    // 设置zIndex并显示提示DIV
	    $alertContainer.css({zIndex:alertzIndex}).show();
	    // 设置位置
	    var offset = $obj.offset();
	    var alertTop = offset.top;
	    var alertLeft = offset.left + 3;
	    if(opts.position == 'left'){
		    alertTop = alertTop + ($obj.height()/2 - $alertContainer.height()/2) + 2;
		    alertLeft = alertLeft + $obj.width();
	    }else if(opts.position == 'top'){
	    	alertTop = alertTop - $alertContainer.height() - 2;
	    }else if(opts.position == 'bottom'){
	    	alertTop = alertTop + $obj.height() + 2;
	    }
	    $alertContainer.css('left', alertLeft + 'px').css('top', alertTop+4 + 'px');
	    // 给对象增加样式
	    //$obj.addClass('invalid');	
	    /** 校验通过调用此函数 */
	    function hideMsg(event) {
	    	// 给对象增加删除样式
	        //$obj.removeClass('invalid');
	        // 隐藏校验提示框
	        $alertContainer.hide();
	        // 删除对象上blur事件对m函数的绑定
	        $obj.unbind('blur', hideMsg);
	        // 删除body上blur事件对hideMsg函数的绑定
	        $('body').unbind('mousedown', hideMsg);
	    }
	
	    var flag = 1;
	    var events = $obj.data("events");
	    // 如果元素不可用 flag=0
	    if ($obj[0].disabled) {
	        flag = 0;
	    } else {
	    	// 如果对象上已有blur事件,则将flag设为0,使不在对象上将hideMsg方法注册给blur事件
	    	// 避免触发对象上的blur事件时同时执行hideMsg方法将本来已显示的提示DIV隐藏
	    	if(events && events['blur']){
	    		flag = 0;
	    	}
	    	// 解决blur事件上有校验时死循环问题
	    	if(opts.focus){
		    	// 如果可用则马上执行校验对象上的焦点事件
		        setTimeout(function () {
		        	try {
		        		$obj.trigger('focus');
		        	} catch (e) {
		        		flag = 0;
		        	}}, 0);
	    	}
	    }
	    if (flag) {
	    	// 如果对象可用则给对象注册hideMsg函数给blur事件
	        $obj.bind('blur', setTimeout(hideMsg,2000));
	    } else {
	    	// 如果不可用则在body上注册hideMsg函数给mousedown事件
	        $('body').bind('mousedown', hideMsg);
	    }
	});//end each
};


// 默认参数
$.fn.alert.defaults = {
	position: 			'left', 	// 位置字符串(可选)默认为left,可选为top,bottom
	focus: 				true,  		// 是否让校验对象获得焦点(解决blur事件上有校验时死循环问题)默认为true让对象获得焦点
	alertzIndex: 		999,
	alertClass:			'corner'	// 'alert-' + alertClass.
};
})(jQuery);