$(function(){
	page = 1;                  //初始页数
	pageNum = 31;              //每页多少条
	enMark = true;
	total = 0;
	change = false;
	msgId = '';
	
	if(enMark){
		enMark = false;
		activityData(page);
	}
	
	
	//首页
	$('.Hpage').click(function(){
		page = 1;
		activityData(page);
		$('.intelligent_data_title ul input[type="checkbox"]').attr("checked",false);
	});
	
	//尾页
	$('.Lpage').click(function(){
		page = $('#totalPage').text();
		activityData(page);
		$('.intelligent_data_title ul input[type="checkbox"]').attr("checked",false);
	});
	
	//下一页
	$('.Npage').click(function(){
		var tlp = $('#totalPage').text();  //总页数
		
		if(page < tlp){
			page++;
		}
		activityData(page);
		$('.intelligent_data_title ul input[type="checkbox"]').attr("checked",false);
		
	});

	//上一页
	$('.Ppage').click(function(){
		if(page != 1){
			page --;
		}
		activityData(page);
		$('.intelligent_data_title ul input[type="checkbox"]').attr("checked",false);
	});
	
})



/**
 * 消息列表
 * @param {int} dataJson.pageNo - 页数
 * author:mjx
 * time:2017-11-24 11:17:06
 */
function activityData(page,key){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
    
    key = key || '';
    
	$.ajax({
		url : "../wxadmin/get/mess/list/"+page,
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:'{"key":"'+key+'"}',
		async : true,
		success : function(data){
			enMark = true;
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			
			if (data.resultCode == '000000I') {
				var data = data.data;
				var info = data.mrList;
				var mcIds = data.mcIds;
				var mcNo = [];
				var status = ["默认","屏蔽","关闭"];
				//区域编号
				var option = '';
				option += '<option>请选择</option>'
				mcIds.forEach(function(item){
					mcNo.push(item[0]);
					option += '<option>'+item[0]+'</option>'
				})
				//保存区域编号，跳转到详情页的时候
				localStorage.setItem("mcIds",JSON.stringify(mcNo));
				
				localStorage.setItem("mrList",JSON.stringify(info));
				
				$(".search_layer .qyNo_select").html(option);
				
				
				total = Number(data.count);
				var date = new Date().getTime();
				$('.intelligent_data_con_zi').html('');
				info.forEach(function(e,i,arr){
					var html = '';
					var s = '';
					if(e.status == 0){
						if(e.startTime > date){
							s = '未发布'
						}else if(e.startTime < date){
							s = '已发布'
						}
					}else{
						s = status[e.status]
					}
					html ='<ul data-messid="'+e.id+'">'
						   +'<li><input type="checkbox" data-show="false"></li>'
						   +'<li>'+ (i+1) +'</li>'               //序号
						   +'<li title='+isNull(e.title)+'>'+ isNull(e.title) +'</li>'              //标题
						   +'<li>'+ isNull(formatDate(e.startTime)) +'</li>'                //日期
						   +'<li>'+ s +'</li>'                //日期
						   +'<li>'
						   +'   <a class="view" data-idx="0" href="javascript:;">查看</a>'
						   if(e.status != 2){
					html  +='   <a class="view change" data-idx="1" href="javascript:;">修改</a>'
						   +'   <a class="view shield" data-idx="2" href="javascript:;">屏蔽</a>'
						   +'   <a class="view publish" data-idx="3" href="javascript:;">发布</a>'
						   +'   <a class="view closed" data-idx="4" href="javascript:;">关闭</a>'
						   }
					html +='</li>'       //消息详情
						   +'</ul>';
					$('.intelligent_data_con_zi').append(html);
				});
				
				$('#total').text(total);                   //总数
				$('#totalPage').text(Math.round(total/10) <1 ? '1' : Math.ceil(total/10));                   //总页数
				$('#thisPage').text(page);                   //当前页
				
				
				bingFF();
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
 * 消息详情
 * @param {int} dataJson.pageNo - 页数
 * author:mjx
 * time:2017-11-24 11:17:06
 */
function searchWdInfo(dataJson){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
	
	$.ajax({
		url : "../wxadmin/get/mess",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:dataJson,
		async : true,
		success : function(data){
			if (data.resultCode == '000000I') {
				$(".mask").remove();
				$(".mask").remove();
				$(".search_layer").hide();
				$(".search_layer input").val('');
			 
				var data = data.data;
				localStorage.setItem("detailList",JSON.stringify(data));
			 
				window.location.href="WDactivityData.html";
		
				
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
 * 新增消息
 * @param
 * author:ljsos
 * time:2017-11-24 11:17:06
 */
function addArticleMsg(dataJson){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
	
	$.ajax({
		url : "../wxadmin/add/mess",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:JSON.stringify(dataJson),
		async : true,
		success : function(data){
			
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			
			if (data.resultCode == '000000I') {
				
				alert("新增成功！")
				setTimeout(function(){
					$('.intelligent .con').show();
					$('.AddMessage,.newTest_nav .return').hide();
					UE.getEditor('editor').setContent("");
					$("#startTime").val("");
					$("#endTime").val("");
					$(".selectType option[value='1']").attr("selected","selected");
					$(".AddMessage .title").val("");
					
					page = 1;
					//加载列表
					activityData(page);
				},1000)
				
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
 * 修改消息状态
 * @param dataJson - json
 * @param index - 屏蔽，发布，关闭
 * author:ljsos
 * time:2017-11-24 11:17:06
 */
function updateStatus(dataJson,index){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
	
	$.ajax({
		url : "../wxadmin/update/mess",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:JSON.stringify(dataJson),
		async : true,
		success : function(data){
			
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			
			if (data.resultCode == '000000I') {
				
				if(index == "3"){
					alert("发布成功！");
				}else if(index == '1'){
					alert("修改成功！");
					change = false;
					setTimeout(function(){
						$('.intelligent .con').show();
						$('.AddMessage,.newTest_nav .return').hide();
						UE.getEditor('editor').setContent("");
						$("#startTime").val("");
						$("#endTime").val("");
						$(".selectType option[value='1']").attr("selected","selected");
						$(".AddMessage .title").val("");
					},1000)
				}
				 activityData(page);
				 
				
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
    //$('.intelligent_data_con').css('max-height',winH-top-60);
    var oldMsgId = '';
    
    $(".intelligent_data_con_zi ul li .view").click(function(){
    	var _index = $(this).attr("data-idx");
    	var messId = $(this).parent().parent().attr("data-messid");
    	var list = '';
    	var dataJson = '';
    	var isPush = false;  //是否发布
    	var status = 0; //状态，0：发布，1：屏蔽，2：关闭
    	if(_index != "0"){
    		//取出对应的数据
    		oldMsgId = messId;
    		var mrList = JSON.parse(localStorage.getItem("mrList"));
    		mrList.forEach(function(item,i){
    			if(item.id == messId){
    				list = item;
    			}
    		})
    	}
    	switch(_index){
    		case "0" :  //查看
    			$(".mask").remove();
    			$("body").append("<div class='mask'></div>");
    			$(".mask").show();
    			$(".search_layer").attr("data-id",messId);
    			$(".search_layer").show();
    			break;
    		case "1" :  //修改
    			change = true;
    			msgId = messId;
    			changeCon(list);
    			break;
    		case "2" :	//屏蔽
    			isPush = false;
    			status = 1;
    			break;
    		case "3" :	//发布
    			isPush = true;
    			status = 0;
    			break;
    		case "4" :	//关闭
    			isPush = false;
    			status = 2;
    			break;
    	}
    	
    	if(_index != 0 && _index != 1){
    		//组装数据
        	dataJson = {
        			"isPush":isPush,
        			"mr":{
        				"content": list.content,
        				"createBy": list.createBy,
        				"createTime": list.createTime,
        				"endTime": list.endTime,
        				"id": messId,
        				"modifyTime": list.modifyTime,
        				"startTime": list.startTime,
        				"status": status,
        				"title": list.title,
        				"type": list.type
        			}
        		}
        	//修改消息状态
        	updateStatus(dataJson,_index);
    	}
    	

	 });
    $('.intelligent_data_title ul input[type="checkbox"],.intelligent_data_con_zi ul input[type="checkbox"]').off('click');
    $('.intelligent_data_con_zi ul input[type="checkbox"]').click(function(event){
	       var inputSize =   $('.intelligent_data_con_zi ul input[type="checkbox"]').size();
		   var num =0;
		    $('.intelligent_data_con_zi ul input[type="checkbox"]').each(function() {
              if($(this).attr("checked") == "checked"){
					  num ++;
				  }
           });
			if(num == inputSize){
				     $('.intelligent_data_title ul input').attr('checked',true);
				}else{
					    $('.intelligent_data_title ul input').attr('checked',false);
					}
			event.stopPropagation();
	  });
    $('.intelligent_data_title ul input[type="checkbox"]').click(function(event){
	          if($(this).attr("checked")=="checked"){
						$('.intelligent_data_con_zi ul input[type="checkbox"]').attr('checked',true);
				  }else{
						$('.intelligent_data_con_zi ul input[type="checkbox"]').attr('checked',false);
					  }
		  
			
			event.stopPropagation();
	  });
	
}


function changeCon(list){
	$('.intelligent .con').hide();
	$('.AddMessage,.newTest_nav .return').show();
	//var ue = UE.getEditor('editor');
	  
	setTimeout(function(){
	   $('#edui1_bottombar').hide();
	   $('#editor').css('margin-bottom','50px');
	},10)
	
	
	var mrList = list;
	var startTime = formatDate(list.startTime);
	var endTime = formatDate(list.endTime);
	var type = list.type;
	$("#startTime").val(startTime);
	$("#endTime").val(endTime);
	$(".selectType option[value='"+type+"']").attr("selected",'selected');
	$(".AddMessage .title").val(list.title);
	
	ue.setContent(list.content);
}









/**
 * 批量关闭
 * author:hgx
 * time:2018-09-12 10:53:55 
 */
function batchCloseMsg(dataJson){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
	
	$.ajax({
		url : "../wxadmin/close/mess",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:JSON.stringify(dataJson),
		async : true,
		success : function(data){
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			if (data.resultCode == '000000I') {
				
				 alert("关闭成功！");
				 activityData(page);
				 
				
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
			}else{
				//服务器重启
				alert("网络异常");
			}
		}
	});
}


/**
 * 根据区域筛选条件加载网点
 * author:hgx
 * time:2018-09-12 10:53:55 
 */
function getTwd(dataJson){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
	
	$.ajax({
		url : "../wxadmin/get/twd",
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:JSON.stringify(dataJson),
		async : true,
		success : function(data){
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			if (data.resultCode == '000000I') {
				var list = data.data;
				var wd = $(".choose_body textarea").html();
				var wdNo = '';
				if(wd == ''){
					list.forEach(function(e){
						wdNo += e.wdNo +",";
					})
					wdNo = wdNo.substring(0,wdNo.length -1);
				}else{
					var content = wd.split(",");
					content.forEach(function(e,i){
						list.forEach(function(item){
							if(e == item.wdNo){
								e.splice(i,1)
							}
						})
					})
				}
				
				
				
				$(".choose_body textarea").html(wdNo);
				
				
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
			}else{
				//服务器重启
				alert("网络异常");
			}
		}
	});
}



/**
 * 加载消息联系人
 * author:hgx
 * time:2018-09-12 10:53:55 
 */
function getContact(msgId,page){
	$('body').append("<div id='loadingMark' style='width:50px;height:50px;position:fixed;top:90%;left:50%;z-index:10000'></div>");
    $('#loadingMark').CircleLoader({color:"#4184f3"});
	
	$.ajax({
		url : "../wxadmin/get/contacts/"+msgId+"/"+page,
		type : "post",
		contentType: "application/json",
		dataType: "json",
		data:JSON.stringify(dataJson),
		async : true,
		success : function(data){
			setTimeout(function(){
				$('#loadingMark').remove();
			},300);
			if (data.resultCode == '000000I') {
				
				
				
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
			}else{
				//服务器重启
				alert("网络异常");
			}
		}
	});
}