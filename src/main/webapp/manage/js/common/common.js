$('body').append('<script type="text/javascript" src="js/common/jquery.cookie.js"></script>');


/**
 *	mark:获取浏览器信息
 *	time: 2016/03/16 15:55
 *	author: keyuejia 
 */
function getBrower() {
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	if (window.ActiveXObject)
		Sys.ie = ua.match(/msie ([\d.]+)/)[1]
	else if (ua.indexOf("firefox")>0){
		Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1]
	}
	else if (window.MessageEvent && !document.getBoxObjectFor){
		if (ua.match(/chrome\/([\d.]+)/) == null) {
			Sys.ie = "ie";
		}else{
			Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1]
		}
	}
	else if (window.opera)
		Sys.opera = ua.match(/opera.([\d.]+)/)[1]
	else if (window.openDatabase)
		Sys.safari = ua.match(/version\/([\d.]+)/)[1];
	
	// 以下进行测试
	return Sys;
	
}

/*右侧导航效果脚本-end*/




/**
 * mark:获取考试的参考班级所有id
 * author: keyuejia
 * time: 2016/03/17 17:22
 */
function getClassIds(){
	var examInfo = JSON.parse(localStorage.getItem("examList"));
	var classes = examInfo.classes;
	var ids = [];
	for ( var i = 0; i < classes.length; i++) {
		ids.push(classes[i].id);
	}
	return ids;
	
}


/**
 * mark:对特殊字符进行转义
 * author: keyuejia
 * time:2016/03/14 
 * @param str 
 */
function disposeSpecialChar(str) {
	var specials = [];
	specials.push("'");
	specials.push("\\");
	specials.push("\"");
	var disStr = "";
	var charArray = str.split("");
	for ( var i = 0; i < charArray.length; i++) {
		var s = charArray[i]+"";
		for ( var j = 0; j < specials.length; j++) {
			if(charArray[i] == specials[j]){
				s = "\\"+charArray[i];
			}
		}
		disStr += s;
	}
	return disStr;
}

//数字转中文
var N = ["一", "二", "三", "四", "五", "六", "七"];

function convertToChinese(num) {
	num = num - 1;
	var str = num.toString();
	var len = num.toString().length;
	var C_Num = [];
	for (var i = 0; i < len; i++) {
		C_Num.push(N[str.charAt(i)]);
	}
	return C_Num.join('');
}


/**
 * mark:获取头像前缀
 * author: keyuejia
 * time: 2016/03/02
 */
function getImgUrlPre(){
	if (isformal()==true) {
		return "http://yeah100.cn:7001/yeah100/images/";
	} else {
		return "http://120.24.239.235:7001/yeah100-dit/images/";
	};
	
}


/**
 * mark:接口前缀
 * author: keyuejia
 * time: 2016/04/13
 */
function getInterfacePre(){
	return "remote/";
	
}

// 排序函数 (通用)
function createComparisonFunction(propertyName, flag) {
	if (flag == "asc") {
		return function(object1, object2) {
			var value1 = object1[propertyName];
			var value2 = object2[propertyName];
			if (value1 < value2) {
				return -1;
			} else if (value1 > value2) {
				return 1;
			} else {
				return 0;
			}
		};
	} else if (flag == "desc") {
		return function(object1, object2) {
			var value1 = object1[propertyName];
			var value2 = object2[propertyName];
			if (value1 > value2) {
				return -1;
			} else if (value1 < value2) {
				return 1;
			} else {
				return 0;
			}
		};
	}
}

// 排序函数 (字符数字)
function createComparisonFunction_stringNum(propertyName, flag) {
	if (flag == "asc") {
		return function(object1, object2) {
			var value1 = parseInt(object1[propertyName]);
			var value2 = parseInt(object2[propertyName]);
			if (value1 < value2) {
				return -1;
			} else if (value1 > value2) {
				return 1;
			} else {
				return 0;
			}
		};
	} else if (flag == "desc") {
		return function(object1, object2) {
			var value1 = parseInt(object1[propertyName]);
			var value2 = parseInt(object2[propertyName]);
			if (value1 > value2) {
				return -1;
			} else if (value1 < value2) {
				return 1;
			} else {
				return 0;
			}
		};
	}
}

// 搜索对象
function searchObj(obj, subObj) {
	var searchResult = true;
	for (var key in subObj) {
		if (obj[key] != subObj[key]) {
			searchResult = false;
			break;
		}
	}
	return searchResult;
}

// 数字转为字母
function decryptAnswer(sAnswer) {
	if (null == sAnswer)
		return "";
	var sDecryptAnswer = "";
	var sItemAnswer = sAnswer.split(",");
	var nItemCnt = sItemAnswer.length;
	for (var i = 0; i < nItemCnt; i++) {
		if (0 < i)
			sDecryptAnswer += ",";

		var nAns;

		if ("" == sItemAnswer[i])
			nAns = 0;
		else
			nAns = parseInt(sItemAnswer[i], 0);
		for (var j = 0; j < 8; j++) {
			var nBit = nAns & parseInt(Math.pow(2, j)); // ͬС���ȡ��
			if (0 < nBit) {
				sDecryptAnswer += intToStr(nBit);
			}
		}
	}
	return sDecryptAnswer;
}
function intToStr(nBit) {
	var sLetter;

	switch (nBit) {
	case 1:
		sLetter = "A";
		break;
	case 2:
		sLetter = "B";
		break;
	case 4:
		sLetter = "C";
		break;
	case 8:
		sLetter = "D";
		break;
	case 16:
		sLetter = "E";
		break;
	case 32:
		sLetter = "F";
		break;
	case 64:
		sLetter = "G";
		break;
	case 128:
		sLetter = "H";
		break;
	default:
		sLetter = "";
		break;
	}

	return sLetter;
}

//字母转数字
function encryptAnswer(sItemAnswer) {
	// 锟斤拷锟金案硷拷锟斤拷
	/*
	 * if (sItemAnswer.endsWith(",")) { sItemAnswer += " ";// 锟斤拷止
	 * ,,,,,,,锟叫割不锟斤拷 }
	 */
	var sEnAnswer = "";
	var arrAnswer = sItemAnswer.split(",");
	var nItemCnt, nLen, nResult;
	nItemCnt = arrAnswer.length;
	for ( var i = 0; i < nItemCnt; i++) {
		nResult = 0;
		var arrChrAns = toArray(arrAnswer[i]);
		nLen = arrChrAns.length;
		for ( var j = 0; j < nLen; j++) {
			nResult |= letterToInt(arrChrAns[j]);
		}

		if (0 < i) {
			sEnAnswer += ",";
		}

		sEnAnswer += nResult + "";
	}

	return sEnAnswer;
}

function toArray(str) {
	var strArray = [];
	for ( var i = 0; i < str.length; i++) {
		strArray.push(str.charAt(i));
	}
	return strArray;
}
function letterToInt(ch) {
	var chCode = ch.charCodeAt(0);
	if ('A' > ch || 'H' < ch)
		return 0;

	var nBitOff = chCode - "A".charCodeAt(0);
	return 1 << nBitOff;
}
var year = new Date().getFullYear();
var month = new Date().getMonth() + 1;
var day = new Date().getDate();


// 转换为yyyy-MM-dd格式的日期
function formatDate(pre_date) {
	var weekEndDate = new Date(pre_date);
	var weekEndYear = weekEndDate.getFullYear();
	var weekEndMonth = weekEndDate.getMonth() + 1;
	if (weekEndMonth < 10) {
		weekEndMonth = "0" + weekEndMonth;
	}
	var weekEndDay = weekEndDate.getDate();
	if (weekEndDay < 10) {
		weekEndDay = "0" + weekEndDay;
	}
	var h = weekEndDate.getHours();
	if (h < 10) {
		h = "0" + h;
	}
	var m = weekEndDate.getMinutes();
	if (m < 10) {
		m = "0" + m;
	}
	var s = weekEndDate.getSeconds();
	if (s < 10) {
		s = "0" + s;
	}
	return weekEndYear + "-" + weekEndMonth + "-" + weekEndDay + " " + h +":"+m+":"+s;
}
// 获取本月第一天
function getMonFirstDate() {
	var firstDate = new Date(year, month - 1, 1);
	return formatDate(firstDate) + " " + "00:00:00";
}
// 获取本月最后一天的日期
function getMonLastDate() {
	var lastDateTime = new Date(year, month, 1).getTime() - 1000 * 60 * 60 * 24;
	var lastDate = new Date(lastDateTime);
	return formatDate(lastDate) + " " + "23:59:59";
}

// 获取今年第一天
function getYearStart() {
	var yearFirstDate = new Date(year, 1, 1);
	return formatDate(yearFirstDate) + " " + "00:00:00";
}
// 获取今年最后一天
function getYearEnd() {
	var yearEndDate = new Date(year, 12, 31);
	return formatDate(yearEndDate) + " " + "23:59:59";
}
//增加随机数（用于get请求，浏览器缓存）
function addRandom() {
	return "?new=" + Math.random();
}

//术语统一：通过编辑，获取提示方案:无标题遮罩层
function getTipNoHide(temp) {
	var tipContent = "";
	$.ajax({
		type: "get",
		url: "configs/prompt_strings.xml",
		async: false,
		dataType: "xml",
		beforeSend: function(request) {
			request.setRequestHeader("Test", "Chenxizhang");
		},
		success: function(content) {
			var elements = content.getElementsByTagName("Tip");
			for (var i = 0; i < elements.length; i++) {
				var code = elements[i].getElementsByTagName("code")[0].firstChild.nodeValue;
				if (temp == code) {
					tipContent = elements[i].getElementsByTagName("tipContent")[0].firstChild.nodeValue;
					break;
				}
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {

		}
	});
	return tipContent;
}

/**
 * mark:登录页面-显示错误提示
 * @param Mesg 错误提示内容
 * author: keyuejia
 * time: 2016/02/28 14:00
 */
function showErrorMessge(Mesg,index) {
	/*$('.prompt span').html(Mesg);
	$('.prompt').show();
	$('.prompt').animate({
		'top': '-30px'
	}, 300);*/
	$(".register_tian ul li").eq(index).find('p').addClass("error");
    $(".register_tian ul li").eq(index).find('.xian').addClass("error");
    $(".register_tian ul li").eq(index).find('span').html(Mesg);
   $(".register_tian ul li").eq(index).find('span').show();
}


/**
 * mark:登录页面-去除错误提示
 * author: keyuejia
 * time: 2016/04/06 11:00
 */
function hideErrorMessge(index) {
	$(".register_tian ul li").eq(index).find('p').removeClass("error");
	$(".register_tian ul li").eq(index).find('.xian').removeClass("error");
	$(".register_tian ul li").eq(index).find('span').hide();
}

/**
 * 
 * mark:show error tip bottom slide
 * tipCode:错误编码
 * author: keyuejia
 * type:  true为成功，显示绿色背景提示，  false 为失败，显示红色背景提示
 * time:2016/03/02 15:16
 */
function showSlideErrorTip(tipCode, type) {
	$('.tip_message').text(getTipNoHide(tipCode));
	if (type) {
		$('.tip_message').css({
			'background': '#009900'
		});
	} else {
		$('.tip_message').css({
			'background': '#d32f2f'
		});
	}
	setTimeout(function() {
		$('.tip_message').css({
			'bottom': '0px'
		});
	}, 300);

	setTimeout(function() {
		$('.tip_message').css({
			'bottom': '-40px'
		});
	}, 4000);
}



/**
 * 
 * mark:show error tip bottom slide（文字提示）
 * tipCode:错误内容
 * author: keyuejia
 * type:  true为成功，显示绿色背景提示，  false 为失败，显示红色背景提示
 * time:2016/03/17 11：25
 */
function showSlideErrorTipWord(tipCode, type) {
	$('.tip_message').html(tipCode);
	if (type) {
		$('.tip_message').css({
			'background': '#009900'
		});
	} else {
		$('.tip_message').css({
			'background': '#d32f2f'
		});
	}
	setTimeout(function() {
		$('.tip_message').css({
			'bottom': '0px'
		});
	}, 300);
	
	setTimeout(function() {
		$('.tip_message').css({
			'bottom': '-40px'
		});
	}, 4000);
}

//连接错误提示-start
document.write("<div class='tip_message'></div>");
// 连接错误提示-end





//取消创建班级按钮事件
$("#creatClassCancel").click(function() {
	var parent = $(this).parents(".createClass_window").find("ul");
	parent.find('p').removeClass("error").removeClass("clickB").removeClass("clickB_1");
	parent.find('.xian').removeClass("error").removeClass("clickC");
	parent.find("span").hide();
	$("#creatClassName").val("");
});




/**
 * mark: 页面失效
 * author: keyuejia
 * time: 2016/03/23 10:26
 */
function page_disabled(){
	confirmDialog(getTipNoHide("html_disabled_relogin"),function(){
		window.location.href="WX_Login.html";
	},function(){});
}

/**
 * 	对Date的扩展，将 Date 转化为指定格式的String
 *	月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
 *	年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
 *	例子： 
 *	(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
 *	(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
 */ 
Date.prototype.Format = function (fmt) { //author: meizz 
	var o = {
     "M+": this.getMonth() + 1, //月份 
     "d+": this.getDate(), //日 
     "h+": this.getHours(), //小时 
     "m+": this.getMinutes(), //分 
     "s+": this.getSeconds(), //秒 
     "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
     "S": this.getMilliseconds() //毫秒 
 };
 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
 for (var k in o)
 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
 return fmt;

}

/**
 * 获取url?后面的参数
 * @String {String} name url参数
 */
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

/**
 * 获得用户id的参数
 * @returns {String}
 */
function getUserId(){
	return 'user';
}


$(".loginOut").click(function(){
	var userName = JSON.parse($.cookie("user")).username;
	queitLogin(userName)
})


  /*
   *退出登录
   *@param screenNo - 设备号
   *time 20180314 hgx
   */
   function queitLogin(userName){
   		$.ajax({
			type:"post",
			url:"../admin/store/logout/"+userName,
			contentType:"application/json",
			dataType : "json",
			async:false,
			success:function(data){
				if( data.resultCode == '20005E' ){
					
					window.location.href="login.html";
					
				}else if(data.resultCode == '20006E'){
					
				}else if(data.resultCode == 'E99999'){
					alert("系统异常");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if (XMLHttpRequest.responseText.indexOf("400")>-1) {
					//接口或参数出错
					alert("接口或参数出错");
				}else{
					//服务器重启
					alert("网络异常");
				}
			},
		});
   }






