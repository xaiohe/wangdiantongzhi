$(function(){
	page = 1;                  //初始页数
	pageNum = 31;              //每页多少条
	enMark = true;
	
	if(localStorage.getItem("mcIds")){
		var mcIds = JSON.parse(localStorage.getItem("mcIds"));
		//区域编号
		var option = '';
		option += '<option>请选择</option>'
		mcIds.forEach(function(item){
			option += '<option>'+item+'</option>'
		})
		
		$(".search .qyNo_select").html(option);
	}
	
	if(localStorage.getItem("detailList")){
		var pageSize = $("#eachPageSize option:checked").val();
		var detailList = JSON.parse(localStorage.getItem("detailList"));
		var mr = detailList.mr;
		var rrList = detailList.rrList;
		var count = detailList.count;
		var pageIndex = Math.round(count/pageSize) <1 ? '1' : Math.ceil(count/pageSize);
		var isReadNum = detailList.isReadNum; //已读
		var isReplyNum = detailList.isReplyNum; //已回复
		var notReadNum = detailList.notReadNum; //未读
		var type = mr.type; //是否回复
		var options = '';
		$(".search .status_select").html('');
		if(type == 0){
			options += '<option value="0" selected="selected">请选择</option>'
			options += '<option value="1">未读</option>'
			options += '<option value="2">已读</option>'
		}else if(type == 1){
			options += '<option value="0">请选择</option>'
			options += '<option value="3">未回复</option>'
			options += '<option value="4">已回复</option>'
		}
		$(".search .status_select").html(options);
		
		
		$('#total').text(count);                   //总数
		$('#totalPage').text(pageIndex);                   //总页数
		$('#thisPage').text(page);
		$('.had_review').text("已回复"+isReplyNum+"条");
		$('.had_read').text("已读"+isReadNum+"条");
		$('.not_read').text("未读"+notReadNum+"条");
		$('.replyRate').text("回复率"+((isReplyNum/count)*100).toFixed(2)+"%");
		$('.readRate').text("已读率"+((isReadNum/count)*100).toFixed(2)+"%");
		
		
		detailsListHtml(rrList,type);
		detailsContentHtml(mr);
		
		localStorage.removeItem("detailList");
	}else{
		page = 1;
		if(enMark){
			enMark = false;
			var pageSize = $("#eachPageSize option:checked").val();
			infoDetails(search(),pageSize);
		}
	}
	
	
	
	//首页
	$('.Hpage').click(function(){
		page = 1;
		var pageSize = $("#eachPageSize option:checked").val();
		infoDetails(search(),pageSize);
	});
	
	//尾页
	$('.Lpage').click(function(){
		page = $('#totalPage').text();
		var pageSize = $("#eachPageSize option:checked").val();
		infoDetails(search(),pageSize);
	});
	
	//下一页
	$('.Npage').click(function(){
		var tlp = $('#totalPage').text();  //总页数
		
		if(page < tlp){
			page++;
		}
		var pageSize = $("#eachPageSize option:checked").val();
		infoDetails(search(),pageSize);
		
	});

	//上一页
	$('.Ppage').click(function(){
		if(page != 1){
			page --;
		}
		var pageSize = $("#eachPageSize option:checked").val();
		infoDetails(search(),pageSize);
	});

})


/**
 * 消息详情
 * @param {int} dataJson.pageNo - 页数
 * author:mjx
 * time:2017-11-24 11:17:06
 */
function infoDetails(search,pageSize,flag){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
    
    var data = JSON.parse(localStorage.getItem("dataJson"));
    var mcIds = search.mcIds || '';
    var readStatus = search.readStatus || 0;
	var mN = mcIds !='' ? '' : data.mcName;
	var messId = data.messId;
	var dataJson = '{"mcName":"'+mN+'","messId":"'+messId+'","page":"'+((page - 1)* pageSize)+'","mcId":"'+mcIds+'","pageSize":"'+pageSize+'","status":"'+readStatus+'"}';
	
	$.ajax({
		url : "../wxadmin/get/mess",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:dataJson,
		async : true,
		success : function(data){
			enMark = true;
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			
			if (data.resultCode == '000000I') {
				var detailList = data.data;
				var mr = detailList.mr;
				var rrList = detailList.rrList;
				var total = Number(detailList.count);
				
				var pageIndex = Math.round(total/pageSize) <1 ? '1' : Math.round(total/pageSize);
				
				
				var type = mr.type; //是否回复
				var options = '';
				var $selects = $(".search .status_select");
				if(!$selects.attr("data-done")){
					$selects.html('');
					if(type == 0){
						options += '<option value="0" selected="selected">请选择</option>'
						options += '<option value="1">未读</option>'
						options += '<option value="2">已读</option>'
					}else if(type == 1){
						options += '<option value="0">请选择</option>'
						options += '<option value="3">未回复</option>'
						options += '<option value="4">已回复</option>'
					}
					$selects.html(options);
					$selects.attr("data-done",true);
				}
				
				
				
				
				var isReadNum = detailList.isReadNum; //已读
				var isReplyNum = detailList.isReplyNum; //已回复
				var notReadNum = detailList.notReadNum; //未读
				
				$('#total').text(total);                   //总数
				$('#totalPage').text(pageIndex);                   //总页数
				$('#thisPage').text(page);  
				$('.had_review').text("已回复"+isReplyNum+"条");
				$('.had_read').text("已读"+isReadNum+"条");
				$('.not_read').text("未读"+notReadNum+"条");
				var replyRate = total === 0  ? 0 : ((isReplyNum/total)*100).toFixed(2);
				var readRate = total === 0  ? 0 : ((isReadNum/total)*100).toFixed(2)
				$('.replyRate').text("回复率"+replyRate+"%");
				$('.readRate').text("已读率"+readRate+"%");
				
				detailsListHtml(rrList,type);
				if(!flag) detailsContentHtml(mr);
				
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
function detailsListHtml(list,type){
	if(!list) return false;
	$('.intelligent_data_con_zi').html('');
	var imgs = [];
	list.forEach(function(e,i,arr){
		var status = '';
		var show = false;
		var img = false;
		var headImage = '';
		var nickName = '';
		var imageUrl = e.imageUrls || [];
		var imgHtml = '';
		if(!e.nickName && !e.headImage){
			status = type == 0 ? '未读' : '未回复';
		}else if(e.nickName && e.headImage && !e.imageUrls &&　e.content === null){
			status = type == 0 ? '已读' : '未回复';
			headImage = e.headImage;
			nickName = e.nickName;
			img = true;
		}else if(imageUrl.length != 0 ||　e.content !== null){
			status = '已回复';
			show = true;
			img = true;
			headImage = e.headImage;
			nickName = e.nickName;
			if(imageUrl && imageUrl.length != 0){
				imageUrl = JSON.parse(imageUrl);
				imageUrl.forEach(function(e){
					imgHtml += '<image src='+e+'/>';
					imgs.push({src: e,w: 1653,h: 2388});
				})
			}
		}
		
		var pageSize = $("#eachPageSize option:checked").val();
		
		$('.intelligent_data_con_zi').append(
			'<ul>'
		   +'<li><img class="draw" style="display:'+(show ? "block" : "none")+'" src="images/new_tx.png"/>'+((page - 1)* pageSize +i+1)+'</li>'  //序号
		   +'<li>'+ isNull(e.mcId) +'</li>'               //区域编号
		   +'<li>'+ isNull(e.storeCode) +'</li>'               //门店编号
		   +'<li>'+ status +'</li>'              //阅读状态
		   +'<li class="user"><img style="display:'+(img ? "inline-block" : "none")+'" src="'+headImage+'"><span>'+nickName+'</span></li>'                //头像昵称
		   +'<li>'+ isNull(formatDateDetail(e.modifyTime)) +'</li>'       //日期
		   + '<div class="reviewCon">'
		   + '	<div class="title">回复内容</div>'
		   + '	<div class="con">'
		   + '		<label>内容：</label>'
		   + '		<span class="info_con">'+isNull(e.content)+'</span>'
		   + '		<label>网点编号：</label>'
		   + '		<span class="wdNo">'+isNull(e.storeCode)+'</span>'
		   + '		<label>网点手机号：</label>'
		   + '		<span class="phone">'+isNull(e.phone)+'</span>'
		   + '		<p>上传图片：</p>'
		   + '		<div class="imageBox">'+imgHtml+'</div>'
		   + '	</div>'
		   + '</div>'
		   +'</ul>'
		);
	});
	
	$(".imageBox").click(function(){
		openPhotoSwipe(imgs);
	})
 	
	$(".draw").click(function(){
		if ($(this).parent().parent().find(".reviewCon").is(":visible")) {
            $(this).parent().parent().find(".reviewCon").slideUp(200);
            $(this).css({
                transform: "rotate(-90deg)"
            })
        } else {
            $(this).parent().parent().find(".reviewCon").slideDown(200);
            $(this).css({
                transform: "rotate(0deg)"
            })
        }
        window.event.cancelBubble = true;
        event.stopPropagation()
	})
}

function openPhotoSwipe(img) {
	$('.pswp').css('z-index','100000');
	$('.xuanzhuan_btn').show();
    var pswpElement = document.querySelectorAll('.pswp')[0];

    // build items array
    var items = img;
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
	var result = {};
	result.mcIds = $(".qyNo_select option:checked").val();
	result.readStatus = $(".status_select option:checked").val();
	if(result.mcIds == "请选择"){
		result.mcIds = '';
	}
	return result;
}









