//UEditor实例
var ue = UE.getEditor('editor');

$(function(){
                     var winW = $(document).width();
		             var winH = $(window).height();
	                 $('.newTest_nav').height(winH-56);
			         $('.intelligent_con').width(winW-200);
			         $('.intelligent_con').height(winH-56);
					 var top = $('.intelligent_data_con').offset().top;
	                 //$('.intelligent_data_con').css('max-height',winH-top-60);	
	    $(window).resize(function(){
							 var winW = $(document).width();
							 var winH = $(window).height();
						     $('.newTest_nav').height(winH-56);
							 $('.intelligent_con').width(winW-200);
						     $('.intelligent_con').height(winH-56);
						      var top = $('.intelligent_data_con').offset().top;
	                           //$('.intelligent_data_con').css('max-height',winH-top-60);	
						});
	 /*新搜索-start*/
		   $('.new_search').click(function(){
			       $('.operation_row').css({'background':'#f6f6f6'});
				   $(this).find('img').attr('src','images/search_black.png')
				   $(this).addClass('click');
				   $('.logo').hide();
				   $('.return_search').show();
				   $('.personalUser_name').css('color','#333');
				   $('.kemu').hide();
				   window.event.cancelBubble = true;
					//停止冒泡
					event.stopPropagation();
			   });
		 $('.return_search').click(function(){
			       $('.operation_row').css({'background':'#ff9800'});
				   $('.new_search').find('img').attr('src','images/search_white.png')
				   $('.new_search').removeClass('click');
				   $('.personalUser_name').css('color','#fff');
				   $('.logo').show();
				   $('.return_search').hide();
				   $('.kemu').show()
			 });
		 
		 
		 $(".cancle").click(function(){
			 $(".mask").remove();
			 $(".search_layer").hide();
			 $(".search_layer input").val('');
			 
			 $(".searching").css("borderColor","#ccc");
			 $(".searching").next().css("visibility","hidden");
		 }) 
		 
		 $(".confirm").click(function(){
			 var val = $(".search_layer input").val().trim();
			 var select = $(".qyNo_select option:checked").val();
			 if(select　== "请选择" && val == ''){
				 select = '';
				 val = '全部';
			 }
			 if(select　== "请选择"){
				 select = '';
			 }
			 var messId = $(this).parent().parent().attr("data-id");
			 var dataJson = '{"mcName":"'+val+'","messId":"'+messId+'","page":"0","mcId":"'+select+'","pageSize":"50","status":"0"}';
			 localStorage.setItem("dataJson",dataJson);
			 //消息详情页（用户阅读状态，上传状态）
			 searchWdInfo(dataJson);
			
			 
		 }) 
		 $(".searching").focus(function(){
			 $(this).css("borderColor",'#2196f3');
			 $(this).next().css("visibility","hidden");
		 }).blur(function(){
			 $(this).css("borderColor",'#ccc');
		 })
		 var bol = true;
		 var height = $(".nav ul li").outerHeight(true);
		 var size = $(".nav ul li").size();
		 $(".shrinkage").click(function(){
			 if(bol){
				 $(".nav").css("height",height*size+'px');
				 bol = false;
			 }else{
				 $(".nav").css("height",'0px');
				 bol = true;
			 }
			 event.stopPropagation();
		 })
		 $(document).click(function(){
			 if(!bol){
				 $(".nav").css("height",'0px');
				 bol = true;
			 }
			 
		 });
		 $('#AddMessage').click(function(){
	            $('.intelligent .con').hide();
				$('.AddMessage,.newTest_nav .return').show();
				  var ue = UE.getEditor('editor');
				  
				setTimeout(function(){
					   $('#edui1_bottombar').hide();
					   $('#editor').css('margin-bottom','50px');
					},10)
	    });
		 $('.returnA').click(function(){
	            $('.intelligent .con').show();
				$('.AddMessage,.newTest_nav .return').hide();
				UE.getEditor('editor').setContent("");
				$("#startTime").val("");
				$("#endTime").val("");
				$(".selectType option[value='1']").attr("selected","selected");
				$(".AddMessage .title").val("");
			
	    });
		 
		 
		 
		//新增确定
		 $(".AddMessageConfirm").click(function(){
			 var startTime = $("#startTime").val();
			 var endTime = $("#endTime").val();
			 var type = $(".selectType option:checked").val();
			 var title = $(".AddMessage .title").val().trim();
			 var msgID = change ? msgId : '';
			 var content = '';
			 ue.ready(function(){
				 content  = ue.getContent();
			 })
			
			 if(startTime == ''){
				 alert("请选择发布时间！")
				 return false;
			 }else if(endTime == ''){
				 alert("请选择结束时间！")
				 return false;
			 }else if(title == ''){
				 alert("请填写标题！")
				 return false;
			 }else if(content == ''){
				 alert("请填写内容或上传图片！")
				 return false;
			 }else{
				var date = new Date().getTime();
				var getStartTime = (new Date(startTime)).valueOf();
				var getEndTime = (new Date(endTime)).valueOf();
				
				if(getEndTime < getStartTime){
					alert("结束时间不能小于发布时间！");
					return false;
				}
				
				//组装数据
	        	dataJson = {
	        			"isPush":false,
	        			"mr":{
	        				"content": content,
	        				"createBy": 'center',
	        				"createTime": date,
	        				"endTime": (new Date(endTime)).valueOf(),
	        				"id": msgID,
	        				"modifyTime": date,
	        				"startTime": (new Date(startTime)).valueOf(),
	        				"status": 0,
	        				"title": title,
	        				"type": type
	        			}
	        		}
	        	//新增消息
	        	if(!change){
	        		addArticleMsg(dataJson);
	        	}else{
	        		msgId = '';
	        		updateStatus(dataJson,1);
	        	}
	        	
			 }
			 
		 })
		 
		 
		 //批量关闭
		 $(".closeMsg").click(function(){
			 var m = 0;
			 var data = [];
			 $(".intelligent_data_con_zi ul li").each(function(){
				 if($(this).find("input").attr("checked") == "checked"){
					 m++;
					 data.push($(this).parent().attr("data-messid"));
				 }
			 })
			 if(m >= 1){
				 batchCloseMsg(data);
			 }else{
				 alert("请选择需要关闭的消息！")
			 }
		 })
		 
		 
		 //搜索标题
		 $(".seachTitle").click(function(){
			 var search = $(this).prev().val();
			 if(search != ""){
				 $(".a_input").css("borderColor","#999");
				 activityData(page,search);
			 }else{
				 $(".a_input").css("borderColor","red");
			 }
		 })
		 
		 $(".a_input").on("input",function(){
			 $(".a_input").css("borderColor","#999");
		 })
		 
		 
		 //根据区域选择网点
		 $(".choice ul li input").click(function(){
			 var val = '';
			 $(".choice ul li").each(function(){
				 if($(this).find("input").attr("checked") == 'checked'){
					 val += $(this).find("input").val()+',';
				 }
			 })
			 val = val.substring(0,val.length -1);
			 //console.log(val)
			 var dataJson = {
					 "mcId":val,
					 "wdNo":"",
					 "phone":"",
					 "page":"0"
					 }
			 getTwd(dataJson);
		 })
		 
	});