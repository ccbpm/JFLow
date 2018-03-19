// JavaScript Document

window.onerror=function(){return true;}

function menu_zzjsnet(id,onlyone){
if(!document.getElementById || !document.getElementsByTagName){return false;}
this.menu=document.getElementById(id);
this.submenu=this.menu.getElementsByTagName("ul");
this.speed=10;
this.time=5;
this.onlyone=onlyone==true?onlyone:false;
this.links = this.menu.getElementsByTagName("a");
}
menu_zzjsnet.prototype.init=function(){
var mainInstance = this;
for(var i=0;i<this.submenu.length;i++){
this.submenu[i].getElementsByTagName("span")[0].onclick=function(){
mainInstance.toogleMenu(this.parentNode);
};
}
for(var i=0;i<this.links.length;i++){
this.links[i].onclick=function(){
this.className = "current";
mainInstance.removeCurrent(this);
}
}
}
menu_zzjsnet.prototype.removeCurrent = function(link){
for (var i = 0; i < this.links.length; i++){
if (this.links[i] != link){
this.links[i].className = " ";
}
}
}
menu_zzjsnet.prototype.toogleMenu=function(submenu){
if(submenu.className=="open"){
this.closeMenu(submenu);
}else{
this.openMenu(submenu);
}
}
menu_zzjsnet.prototype.openMenu=function(submenu){
var fullHeight=submenu.getElementsByTagName("span")[0].offsetHeight;
var links = submenu.getElementsByTagName("a");
for (var i = 0; i < links.length; i++){
fullHeight += links[i].offsetHeight;
}
 var moveBy = Math.round(this.speed * links.length);
var mainInstance = this;
 var intId = setInterval(function() {
  var curHeight = submenu.offsetHeight;
  var newHeight = curHeight + moveBy;
  if (newHeight <fullHeight){
  submenu.style.height = newHeight + "px";
  }else {
clearInterval(intId);
submenu.style.height = "";
submenu.className = "open";
}
}, this.time);
this.collapseOthers(submenu);
}
menu_zzjsnet.prototype.closeMenu=function(submenu){
var minHeight=submenu.getElementsByTagName("span")[0].offsetHeight;
    var moveBy = Math.round(this.speed * submenu.getElementsByTagName("a").length);
var mainInstance = this;
 var intId = setInterval(function() {
  var curHeight = submenu.offsetHeight;
  var newHeight = curHeight - moveBy;
  if (newHeight > minHeight){
  submenu.style.height = newHeight + "px";
  }else {
clearInterval(intId);
submenu.style.height = "";
submenu.className = "";
}
}, this.time);
}
menu_zzjsnet.prototype.collapseOthers = function(submenu){
if(this.onlyone){
for (var i = 0; i < this.submenu.length; i++){
if (this.submenu[i] != submenu){
this.closeMenu(this.submenu[i]);
}
}
}
}

function delete_confirm(e)
{
    event.returnValue = confirm("删除是不可恢复的，你确认要删除吗？");
}