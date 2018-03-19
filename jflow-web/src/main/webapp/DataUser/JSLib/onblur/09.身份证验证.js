function IdCardRegCheck(s) {
	var str = obj.value;
	var reg = /^([0-9]{15}|[0-9]{18})$/;
	var flag = reg.test(str);
	return flag;
}