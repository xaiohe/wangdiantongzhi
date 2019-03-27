 $(function(){
                     var winW = $(document).width();
                     console.log(winW)
		             var winH = $(window).height();
	                 $('.newTest_nav').height(winH-56);
			         $('.intelligent_con').width(winW-200);
			         $('.intelligent_con').height(winH-56);
		            var top = $('.intelligent_data_con').offset().top;
	                 //$('.intelligent_data_con').css('max-height',winH-top);		
	    $(window).resize(function(){
							 var winW = $(document).width();
							 var winH = $(window).height();
						     $('.newTest_nav').height(winH-56);
							 $('.intelligent_con').width(winW-200);
						     $('.intelligent_con').height(winH-56);
						     var top = $('.intelligent_data_con').offset().top;
	                 //$('.intelligent_data_con').css('max-height',winH-top);	
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
			 
			 
			  $('.tab ul li').click(function(){
			     $(this).addClass('click').siblings().removeClass();
			     
			 });
			$('.checkAll').click(function(){
			       if($(this).attr('checked') == 'checked'){
					      $('.intelligent_data_con_zi input').attr('checked',true);
					   }else{
						       $('.intelligent_data_con_zi input').attr('checked',false);
						   }
			  });	 
		 	$('.intelligent_data_con_zi input').click(function(){
				     var num = 0;
					var length = $('.intelligent_data_con_zi input').size();
					$('.intelligent_data_con_zi input').each(function() {
                          if($(this).attr('checked') == 'checked'){
							     num ++;
							  }
                    });
					
					if(length == num){
						    $('.checkAll').attr('checked',true);
						}else{
							 $('.checkAll').attr('checked',false);
							}
						
						
				})  
		
		$(".search .renew").click(function(){
			page = 1;
			var pageSize = $("#eachPageSize option:checked").val();
			if(enMark){
				enMark = false;
				infoDetails(search(),pageSize,true);
			}
		})
		$(".search .query").click(function(){
			var mcId = search();
			page = 1;
			var pageSize = $("#eachPageSize option:checked").val();
			if(enMark){
				enMark = false;
				infoDetails(mcId,pageSize,true);
			}
		})
		$(".search .reset").click(function(){
			var options = $('.search').find("option");
			options.first().attr("selected", true);
			var pageSize = $("#eachPageSize option:checked").val();
			page = 1;
			if(enMark){
				enMark = false;
				infoDetails('',pageSize,true);
			}
		})
		
		$("#eachPageSize").change(function(){
			var mcId = search();
			page = 1;
			var pageSize = $("#eachPageSize option:checked").val();
			if(enMark){
				enMark = false;
				infoDetails(mcId,pageSize,true);
			}
		})
		
		 //手机端导航栏
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
			 
		 })
		 
		 //收起展开
		 var upBol = true;
		 var h = null;
		 $(".shrink").click(function(){
			 if(upBol){
				 if(!h){
					 h = $(".header").outerHeight(true) - $(this).outerHeight(true)+20; //原本的高度
				 }
				 var height = $(".header .title").outerHeight(true) + $(".header .isReview").outerHeight(true)+ $(".header .date").outerHeight(true);
				 $(".detail .header").css("height",height+'px');
				 $(this).html("展开");
				 upBol = false;
			 }else{
				 $(".detail .header").css("height",h+'px');
				 $(this).html("收起");
				 upBol = true;
			 }
			 
		 })
		 
		 
		 
				
	});