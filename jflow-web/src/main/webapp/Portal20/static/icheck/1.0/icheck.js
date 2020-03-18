/*!
 * iCheck v1.0.1, http://git.io/arlzeA
 * =================================
 * Powerful jQuery and Zepto plugin for checkboxes and radio buttons customization
 *
 * (c) 2013 Damir Sultanov, http://fronteed.com
 * MIT Licensed
 */
(function(c){var g="iCheck",e=g+"-helper",q="checkbox",a="radio",s="checked",x="un"+s,i="disabled",h="determinate",b="in"+h,r="update",t="type",d="click",w="touchbegin.i touchend.i",p="addClass",f="removeClass",l="trigger",z="label",o="cursor",n=/ipad|iphone|ipod|android|blackberry|windows phone|opera mini|silk/i.test(navigator.userAgent);c.fn[g]=function(N,E){var J='input[type="'+q+'"], input[type="'+a+'"]',L=c(),B=function(O){O.each(function(){var P=c(this);if(P.is(J)){L=L.add(P)}else{L=L.add(P.find(J))}})};if(/^(check|uncheck|toggle|indeterminate|determinate|disable|enable|update|destroy)$/i.test(N)){N=N.toLowerCase();B(this);return L.each(function(){var O=c(this);if(N=="destroy"){u(O,"ifDestroyed")}else{v(O,true,N)}if(c.isFunction(E)){E()}})}else{if(typeof N=="object"||!N){var F=c.extend({checkedClass:s,disabledClass:i,indeterminateClass:b,checkboxClass:"icheckbox_minimal-grey",radioClass:"iradio_minimal-grey",labelHover:true,aria:false},N),G=F.handle,I=F.hoverClass||"hover",M=F.focusClass||"focus",K=F.activeClass||"active",C=!!F.labelHover,H=F.labelHoverClass||"hover",D=(""+F.increaseArea).replace("%","")|0;if(G==q||G==a){J='input[type="'+G+'"]'}if(D<-50){D=-50}B(this);return L.each(function(){var Z=c(this);u(Z);var R=this,O=R.id,S=-D+"%",aa=100+(D*2)+"%",T={position:"absolute",top:S,left:S,display:"block",width:aa,height:aa,margin:0,padding:0,background:"#fff",border:0,opacity:0},U=n?{position:"absolute",visibility:"hidden"}:D?T:{position:"absolute",opacity:0},V=R[t]==q?F.checkboxClass||"i"+q:F.radioClass||"i"+a,X=c(z+'[for="'+O+'"]').add(Z.closest(z)),W=!!F.aria,Q=g+"-"+Math.random().toString(36).replace("0.",""),Y='<div class="'+V+'" '+(W?'role="'+R[t]+'" ':""),P;if(X.length&&W){X.each(function(){Y+='aria-labelledby="';if(this.id){Y+=this.id}else{this.id=Q;Y+=Q}Y+='"'})}Y=Z.wrap(Y+"/>")[l]("ifCreated").parent().append(F.insert);P=c('<ins class="'+e+'"/>').css(T).appendTo(Y);Z.data(g,{o:F,s:Z.attr("style")}).css(U);!!F.inheritClass&&Y[p](R.className||"");!!F.inheritID&&O&&Y.attr("id",g+"-"+O);Y.css("position")=="static"&&Y.css("position","relative");v(Z,true,r);if(X.length){X.on(d+".i mouseover.i mouseout.i "+w,function(ad){var ab=ad[t],ac=c(this);if(!R[i]){if(ab==d){if(c(ad.target).is("a")){return}v(Z,false,true)}else{if(C){if(/ut|nd/.test(ab)){Y[f](I);ac[f](H)}else{Y[p](I);ac[p](H)}}}if(n){ad.stopPropagation()}else{return false}}})}Z.on(d+".i focus.i blur.i keyup.i keydown.i keypress.i",function(ad){var ac=ad[t],ab=ad.keyCode;if(ac==d){return false}else{if(ac=="keydown"&&ab==32){if(!(R[t]==a&&R[s])){if(R[s]){y(Z,s)}else{k(Z,s)}}return false}else{if(ac=="keyup"&&R[t]==a){!R[s]&&k(Z,s)}else{if(/us|ur/.test(ac)){Y[ac=="blur"?f:p](M)}}}}});P.on(d+" mousedown mouseup mouseover mouseout "+w,function(ad){var ac=ad[t],ab=/wn|up/.test(ac)?K:I;if(!R[i]){if(ac==d){v(Z,false,true)}else{if(/wn|er|in/.test(ac)){Y[p](ab)}else{Y[f](ab+" "+K)}if(X.length&&C&&ab==I){X[/ut|nd/.test(ac)?f:p](H)}}if(n){ad.stopPropagation()}else{return false}}})})}else{return this}}};function v(B,G,F){var C=B[0],D=/er/.test(F)?b:/bl/.test(F)?i:s,E=F==r?{checked:C[s],disabled:C[i],indeterminate:B.attr(b)=="true"||B.attr(h)=="false"}:C[D];if(/^(ch|di|in)/.test(F)&&!E){k(B,D)}else{if(/^(un|en|de)/.test(F)&&E){y(B,D)}else{if(F==r){for(var D in E){if(E[D]){k(B,D,true)}else{y(B,D,true)}}}else{if(!G||F=="toggle"){if(!G){B[l]("ifClicked")}if(E){if(C[t]!==a){y(B,D)}}else{k(B,D)}}}}}}function k(K,D,B){var G=K[0],M=K.parent(),L=D==s,C=D==b,H=D==i,N=C?h:L?x:"enabled",F=m(K,N+j(G[t])),J=m(K,D+j(G[t]));if(G[D]!==true){if(!B&&D==s&&G[t]==a&&G.name){var E=K.closest("form"),I='input[name="'+G.name+'"]';I=E.length?E.find(I):c(I);I.each(function(){if(this!==G&&c(this).data(g)){y(c(this),D)}})}if(C){G[D]=true;if(G[s]){y(K,s,"force")}}else{if(!B){G[D]=true}if(L&&G[b]){y(K,b,false)}}A(K,L,D,B)}if(G[i]&&!!m(K,o,true)){M.find("."+e).css(o,"default")}M[p](J||m(K,D)||"");H?M.attr("aria-disabled","true"):M.attr("aria-checked",C?"mixed":"true");M[f](F||m(K,N)||"")}function y(I,D,B){var F=I[0],K=I.parent(),J=D==s,C=D==b,G=D==i,L=C?h:J?x:"enabled",E=m(I,L+j(F[t])),H=m(I,D+j(F[t]));if(F[D]!==false){if(C||!B||B=="force"){F[D]=false}A(I,J,L,B)}if(!F[i]&&!!m(I,o,true)){K.find("."+e).css(o,"pointer")}K[f](H||m(I,D)||"");G?K.attr("aria-disabled","false"):K.attr("aria-checked","false");K[p](E||m(I,L)||"")}function u(B,C){if(B.data(g)){B.parent().html(B.attr("style",B.data(g).s||""));if(C){B[l](C)}B.off(".i").unwrap();c(z+'[for="'+B[0].id+'"]').add(B.closest(z)).off(".i")}}function m(B,D,C){if(B.data(g)){return B.data(g).o[D+(C?"":"Class")]}}function j(B){return B.charAt(0).toUpperCase()+B.slice(1)}function A(C,D,E,B){if(!B){if(D){C[l]("ifToggled")}C[l]("ifChanged")[l]("if"+j(E))}}})(window.jQuery||window.Zepto);