$(function(){
	ChatPage = 1;//聊天窗口初始页数
	scrollStatus = true;//聊天窗口滚动条状态
	page = 1;                  //初始页数
	pageNum = 31;              //每页多少条
	

	infoDetails(search(),key());//加载申报列表
	
	
	
	
	//首页
	$('.Hpage').click(function(){
		page = 1;
		var key = $(".a_input input").val();
		infoDetails(search(),key);
	});
	
	//尾页
	$('.Lpage').click(function(){
		page = $('#totalPage').text();
		var key = $(".a_input input").val();
		infoDetails(search(),key);
	});
	
	//下一页
	$('.Npage').click(function(){
		var tlp = $('#totalPage').text();  //总页数
		
		if(page < tlp){
			page++;
		}
		var key = $(".a_input input").val();
		infoDetails(search(),key);
		
	});

	//上一页
	$('.Ppage').click(function(){
		if(page != 1){
			page --;
		}
		var key = $(".a_input input").val();
		infoDetails(search(),key);
	});
	
	
	$('.fsxxbtn a').click(function(){
		 var wdNo = $(this).attr("data-wd");
		 var decId = $(this).attr("data-id");
		 var timestamp=new Date().getTime();
		 var content = $('.fsxx textarea').val();
		 var dataJson = {"decId":decId,"createTime":timestamp,"content":content,"modifyTime":timestamp,"wdNo":wdNo,"createBy":"center","usReadStatus":0,"ceReadStatus":1};
		 SendMessage(JSON.stringify(dataJson),decId);
		 $('.fsxx textarea').val('');
	});

})


/**
 * 消息详情
 * @param {int} dataJson.pageNo - 页数
 * author:mjx
 * time:2017-11-24 11:17:06
 */
function infoDetails(mcIds,key){
	
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
    var data = JSON.parse(localStorage.getItem("dataJson"));
	var mN = mcIds !='' ? '' : data.mcName;
	var messId = data.messId;
	var dataJson = '{"page":"'+page+'","mcId":"'+mcIds+'","key":"'+key+'"}';
	
	$.ajax({
		url : "../wxadmin/get/dec/record",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:dataJson,
		async : true,
		success : function(data){
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			
			if (data.resultCode == '000000I') {
				var detailList = data.data.decList;
				localStorage.setItem("fwdetailList",JSON.stringify(detailList));
				var mr = detailList.mr;
				var total =data.data.count;
				var pageIndex = Math.round(total/10) <1 ? '1' : Math.ceil(total/10);
			
				
				$('#total').text(total);                   //总数
				$('#totalPage').text(pageIndex);                   //总页数
				$('#thisPage').text(page);  
			
				detailsListHtml(detailList);
				
			} else if (data.resultCode == 'E000002') {
				//查询失败
				alert('参数错误');
			} else if (data.resultCode == 'E000003') {
				//查询失败
				alert('网点不存在');
			} else if (data.resultCode == 'E000005') {
				//查询失败
				alert('专管员找不到！');
			} else if (data.resultCode == 'E99999') {
				//查询失败
				alert('查询失败');
			}  
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.responseText.indexOf("400")>-1) {
				//接口或参数出错
				alert("网络异常");
			}else if(XMLHttpRequest.status == "403"){
				alert("登录失效，请重新登录！");
				window.location.href="login.html";
				setTimeout(function(){
					$('#loadingMark').remove();
				},300);
			}else{
				//服务器重启
				alert("网络异常");
			}
		}
	});
}



/**
 * 消息详情列表
 *
 * */
function detailsListHtml(list){
	if(!list) return false;
	$('.intelligent_data_con_zi').html('');
	var imgs = [];
	var statusArr = ["默认","屏蔽","关闭"];
	var typeArr =["业务","投诉","其他"];
	list.forEach(function(e,i,arr){

		var headImage = '';
		var nickName = '';
		var imageUrl = JSON.parse(e.image_url) || "";
		var imgHtml = '';
		var title = "["+typeArr[e.type-1]+"] "+e.content;
		
		if(imageUrl){
			imageUrl.forEach(function(e,i){
				imgHtml +='<img src="'+e+'" class="imageBox"/>';
			});
		}
		

		$('.intelligent_data_con_zi').append(
			'<ul>'
		   +'<li>'+((page - 1)* 10 +i+1)+'</li>'  //序号
		   +'<li>'+ isNull(e.mcId) +'</li>'               //区域编号
		   +'<li>'+ isNull(e.create_by) +'</li>'               //门店编号
		   +'<li class="yc" title="'+isNull(title)+'">'+isNull(title) +'</li>'              //标题
		   +'<li>'+isNull(formatDateDetail(e.modify_time))+'</li>'                //时间
		   +'<li>'+ statusArr[e.status] +'</li>'              //阅读状态
		   +'<li>'+imgHtml+'</li>'     //附件
		   +'<li><a href="javascript:;" class="hf" data-id="'+e.id+'">回复</a></li>'       //操作
		   +'</ul>'
		);
	});
	$(".imageBox").click(function(){
		var imgs = $(this).attr("src");
		openPhotoSwipe(imgs);
	});
	$(".hf").click(function(){
		
		$('.intelligent .con').hide();
		$('.Reply,.newTest_nav .return').show();
		var decId =$(this).attr('data-id');
		var fwdetailList = JSON.parse(localStorage.getItem("fwdetailList"));
		fwdetailList.forEach(function(e){
			if(e.id == decId){
				$('.title_text').html(isNull(e.title));
				$('.date_text').html(isNull(formatDateDetail(e.modify_time)));
				$('.content_text').html(isNull(e.content));
				var imageUrl = JSON.parse(e.image_url) || "";
				var imgHtml = '';
				if(imageUrl){
					imageUrl.forEach(function(e,i){
						imgHtml +='<img src="'+e+'" class="imageBox"/>';
					});
				}
				$('.fj_con').html(imgHtml);
				$('.fsxxbtn a').attr('data-id',decId);
				$('.fsxxbtn a').attr('data-wd',e.create_by);
			}
		});
		$(".imageBox").click(function(){
			var imgs = $(this).attr("src");
			openPhotoSwipe(imgs);
		});
		ChatPage =1;
		ChatRoomData(decId);
		
	})
	
}


/**
 * 聊天记录
 * @param {int} - 页数
 * author:mjx
 * time:2017-11-24 11:17:06
 */
function ChatRoomData(decId){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
	var dataJson = '{"page":"'+ChatPage+'","decId":"'+decId+'"}';
	
	$.ajax({
		url : "../wxapp/get/dec/read",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:dataJson,
		async : true,
		success : function(data){
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			$('.Reply_con_data').off('scroll');
			if (data.resultCode == '000000I') {
				var count = data.data.count;
			    var drrList =data.data.drrList.reverse();
			    var drrHtml="";
			    drrList.forEach(function(e){
			    	if(e.createBy == "center"){
			    		drrHtml+='<div class="Reply_conZi">'
				               	+'    <span class="tx"><img src="images/logo.jpg"/> </span>'
				               	+'    <div class="Rcon">'+e.content+'</div>'
				               	+'</div>'
			    	}else{
			    		drrHtml+='<div class="Reply_conZi">'
			    			    +' <span class="tx fr"><img src="'+e.headImage+'"/> </span>'
			    		if(e.content.indexOf('http') > -1){
			    		drrHtml+=' <div class="Rcon fr"><img src="'+e.content+'"/></div>';
			    		}else{
			    			drrHtml+=' <div class="Rcon fr">'+e.content+'</div>';
			    		}    		   
			    		drrHtml+='</div>'		
			    	}
			    });
			    if(ChatPage == 1){
			    	$('.Reply_con_data .Reply_con_data_zi').html('');
			    	$('.Reply_con_data .Reply_con_data_zi').append(drrHtml);
				    setTimeout(function(){ 
				    	$('.Reply_con_data').scrollTop(10000);
				    	heightOld =$('.Reply_con_data_zi').height();
				    },100);
			    }else{
			    	$('.Reply_con_data .Reply_conZi').eq(0).before(drrHtml);
			    	heightNew =$('.Reply_con_data_zi').height();
			    	$('.Reply_con_data').scrollTop(heightNew - heightOld);
			    	heightOld = heightNew;
			    	scrollStatus = true;
				 
			    }
			    
			    $('.Reply_con_data').scroll(function(){
			    	var num =  $('.Reply_con_data .Reply_conZi').size();
			    	if(num != count){
				    	if(scrollStatus){
							var top = $(this).scrollTop();
							if( top <10){
								scrollStatus = false;
								ChatPage++;
								ChatRoomData(decId);
							}
				    	}
			    	}
				 })
			    
				
			} else if (data.resultCode == 'E000002') {
				//查询失败
				alert('参数错误');
			} else if (data.resultCode == 'E000003') {
				//查询失败
				alert('网点不存在');
			} else if (data.resultCode == 'E000005') {
				//查询失败
				alert('专管员找不到！');
			} else if (data.resultCode == 'E99999') {
				//查询失败
				alert('查询失败');
			}  
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.responseText.indexOf("400")>-1) {
				//接口或参数出错
				alert("网络异常");
			}else if(XMLHttpRequest.status == "403"){
				alert("登录失效，请重新登录！");
				window.location.href="login.html";
				setTimeout(function(){
					$('#loadingMark').remove();
				},300);
			}else{
				//服务器重启
				alert("网络异常");
			}
		}
	});
}


/**
 * 发送消息
 * @param {int} - 页数
 * author:mjx
 * time:2017-11-24 11:17:06
 */
function SendMessage(data,decId){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
   
	
	$.ajax({
		url : "../wxapp/add/dec/read",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:data,
		async : true,
		success : function(data){
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			
			if (data.resultCode == '000000I') {
				ChatPage = 1;
				ChatRoomData(decId);//重新加载聊天信息
				
			} else if (data.resultCode == 'E000002') {
				//查询失败
				alert('参数错误');
			} else if (data.resultCode == 'E000003') {
				//查询失败
				alert('网点不存在');
			} else if (data.resultCode == 'E000005') {
				//查询失败
				alert('专管员找不到！');
			} else if (data.resultCode == 'E99999') {
				//查询失败
				alert('查询失败');
			}  
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.responseText.indexOf("400")>-1) {
				//接口或参数出错
				alert("网络异常");
			}else if(XMLHttpRequest.status == "403"){
				alert("登录失效，请重新登录！");
				window.location.href="login.html";
				setTimeout(function(){
					$('#loadingMark').remove();
				},300);
			}else{
				//服务器重启
				alert("网络异常");
			}
		}
	});
}

function openPhotoSwipe(img) {
	$('.pswp').css('z-index','100000');
	$('.xuanzhuan_btn').show();
    var pswpElement = document.querySelectorAll('.pswp')[0];

    // build items array
    var items = [{src: img,w: 1653,h: 2388}];
    // define options (if needed)
    var options = {
             // history & focus options are disabled on CodePen
        history: false,
        focus: false,

        showAnimationDuration: 0,
        hideAnimationDuration: 0
    };
    var gallery = new PhotoSwipe( pswpElement, PhotoSwipeUI_Default, items, options);
	
    gallery.init();
};



/**
 * 消息详情
 *
 * */
function detailsContentHtml(mr){
	if(mr){
		var title = mr.title;
		var review = mr.type;
		var date = formatDate(mr.createTime);
		var content = mr.content
		$('.title_text').html(title);
		$('.review_text').html(review != 1 ? '否': '是');
		$('.date_text').html(date);
		$('.content_text').html(content);
	}
}




/**
 * 导出报表
 */
function exportReport(){
	var dataJson = JSON.parse(localStorage.getItem("dataJson"));
	var mcName = dataJson.mcName;
	var messId = dataJson.messId;
	
	window.location.href="../wxadmin/report/online?messId="+messId+"&mcName="+mcName;
}

/**
 * 判断是否为空
 * @param str
 */
function isNull(str){
	if(!str || str == 'null'){
		str = '--';
	}
	
	return str;
}

/**
 * 绑定事件
 */
function bingFF(){
	var winW = $(document).width();
	var winH = $(window).height();
	$('.newTest_nav').height(winH - 56);
	$('.intelligent_con').width(winW - 200);
	$('.intelligent_con').height(winH - 56);
	var top = $('.intelligent_data_con').offset().top;
    $('.intelligent_data_con').css('max-height',winH-top-60);
	
}


//转换为yyyy-MM-dd格式的日期
function formatDateDetail(pre_date) {
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
	return  weekEndMonth + "-" + weekEndDay + " " + h +":"+m+":"+s;
}

function search(){
	var select = $(".qyNo_select option:checked").val();
	if(select == "请选择"){
		select = '';
	}
	return select;
}
function key(){
	var key = $(".a_input input").val();
	return key;
}






