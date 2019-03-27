// JavaScript Document

  $(function(){
	       var winW = $(document).width();
		   var winH = $(window).height();
			 $('.screening').height(winH-56);
  
  	$(window).resize(function(){
				     var winW = $(document).width();
					   var winH = $(window).height();
					    $('.screening').height(winH-56);
       				});
	  
	         $('.screening_show').click(function(){
				      if($('.screening').css('right') != 0+'px'){
					       $(this).parent().css('background','rgba(0,0,0,0.5)');
					       $('.screening').css('right','0px');
						   $('#information').parent().css('background','none');
						   $('.Classinformation').css('right','-380px');
						
					 }else{
						    $(this).parent().css('background','none');
						    $('.screening').css('right','-380px');
						 }
				 });
	         
	   $('.newTest_nav').hover(function(){
		       $(this).css('overflow-y','auto');
		   },function(){
			       $(this).css('overflow-y','hidden');
			   });
	  
	     /*搜索条件框里的select-start*/
		      $(document).click(function(){
				     $('.select ol').hide();
				 });
			 $('.select').click(function(){
				     $(this).find('ol').show();
					 window.event.cancelBubble = true;
					//停止冒泡
					event.stopPropagation();
				 });	
		     $('.select ol li').click(function(){
				    var type_text = $(this).text();
					$(this).parent().prev().text(type_text);
					$(this).parent().hide();
					window.event.cancelBubble = true;
					//停止冒泡
					event.stopPropagation();
				});	
		    /*搜索条件框里的select-end*/
			 
			 $('.user .user_img').click(function(){
				     if($('.modify_user').is(':visible')){
						   $('.modify_user').hide();
						 }else{
							   $('.modify_user').show();
							   
							 }
				 });
			 
		/*搜索条件弹窗和缩回-start*/ 
			 $('#searchConditions').click(function(){
				   /* $('.search_con').slideDown(200);*/
				  $('.search_con').slideToggle(100);
				 });
			  $('.shut_down').click(function(){
				       $('.search_con').slideUp(100);
				  });
				$('#searchConditions').dblclick(function(){
					  alert(1);
					});
		/*搜索条件弹窗和缩回-end*/ 	
			
				 $('.personalUser .personalUser_img,.top_name').click(function(){
				     if($('.modify_personalUser').is(':visible')){
						   $('.modify_personalUser').hide();
						 }else{
							   $('.modify_personalUser').show();
							   
							 }
				 });
				 
		
		
		 //多选
			  $('.multi_select').click(function(){
				     $(this).find('ol').show();
					 window.event.cancelBubble = true;
					//停止冒泡
					event.stopPropagation();
				 });	
		       $('.multi_select ol li').click(function(){
				    var type_text = $(this).text();
					$(this).append("<img src='images/error.png'/>");
					if($(this).parent().prev().text() == '请选择'){
						   $(this).parent().prev().html('');
						}
					if( $(this).parent().prev().html().indexOf(type_text) < 0){
						   /* $(this).parent().prev().append('<b>'+type_text+'</b>');*/
						      $(this).parent().prev().append(type_text+"　");
						}else{
							   var aa = $(this).parent().prev().html();
							    $(this).find('img').remove();
							    $(this).parent().prev().html(aa.replace(type_text+'　',""));
								if($(this).parent().prev().html() == ''){
									  $(this).parent().prev().html('请选择');
									}
							}
					
/*					$(this).parent().hide();
*/					window.event.cancelBubble = true;
					//停止冒泡
					event.stopPropagation();
				});	
				
				 $('.multi_select ol').hover(function(){
					      $(this).show();
					 },function(){
						    $(this).hide();
						 });
						 
						 
		 $('.SortingChoose').click(function(){
			     if($('.sorting_con').is(':visible')){
					     $('.sorting_con').hide();
					 }else{
						    $('.sorting_con').show();
						 }
			 });
		
		 $('.sorting_con ul li').click(function(){
			      $(this).find('.sorting_data').slideToggle(200);
				  $(this).siblings().find('.sorting_data').slideUp(200);
			  });
		 $('.sorting_data span').click(function(){
			      var val = $(this).text();
				  $(this).parent().prev().prev().text(val);
				  $(this).find('img').show();
				  $(this).siblings().find('img').hide();
			 });
			 
			 /*新搜索-start*/
		   
		/*   $('.new_search').click(function(){
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
		 
		 
		 $(document).click(function(){
			       $('.operation_row').css({'background':'#4285f4'});
				   $('.new_search').find('img').attr('src','images/search_white.png')
				   $('.new_search').removeClass('click');
				    $('.personalUser_name').css('color','#fff');
				    $('.logo').show();
				   $('.return_search').hide();
				    $('.kemu').show()
			 });*/
			 
		/*左侧导航收缩-start*/
		$('.shrinkage').click(function(){
			    if($('.newTest_nav').css('left') == 0+'px'){
					  $('.newTest_nav').animate({'left':'-200px'},500);
					   $('.result_con').animate({'padding-right':'100px','left':'100px'},500);
					   $('.TitleAnalysis_con').animate({'padding-right':'100px','left':'100px'},500);
					   $('.ClassAnalyze').animate({'padding-right':'100px','left':'100px'},500);
					   $('.StudentAnswer').animate({'left':'100px'},500);
					   $('.basicInformation_con').animate({'left':'100px'},500);
					    $('.QuestionsSettings_con').animate({'padding-right':'100px','left':'100px'},500);
						   $('.intelligent_con').animate({'left':'100px'},500);
					}else{
						   $('.newTest_nav').animate({'left':'0px'},500);
					       $('.result_con').animate({'padding-right':'0px','left':'200px'},500);
					       $('.TitleAnalysis_con').animate({'padding-right':'0px','left':'200px'},500);
					       $('.ClassAnalyze').animate({'padding-right':'0px','left':'200px'},500);
					       $('.StudentAnswer').animate({'left':'200px'},500);
						    $('.basicInformation_con').animate({'left':'200px'},500);
							  $('.QuestionsSettings_con').animate({'padding-right':'0px','left':'200px'},500);
							    $('.intelligent_con').animate({'left':'200px'},500);
						}
			});
			
			
			
			
	//帮助和反馈的拖动		
			
			  $('.HelpFeedback_head').mousedown(function(e) {
              var positionDiv = $('.HelpFeedback').offset();
              var distenceX = e.pageX - positionDiv.left;
              var distenceY = e.pageY - positionDiv.top;
        

   	$(document).mousemove(function(e) { 
              var x=e.pageX - distenceX;
              var y=e.pageY - distenceY;
              if(x<0){
                  x=0;
              }else if(x>$(document).width()- $('.HelpFeedback').outerWidth(true)){

                  x=$(document).width()- $('.HelpFeedback').outerWidth(true);
              }
              if(y<0){
              	y=0;
              }else if(y>$(document).height()- $('.HelpFeedback').outerHeight(true)){

                  y=$(document).height()- $('.HelpFeedback').outerHeight(true)
              }
			  x= x<0 ? 0 : x;
			  x= x >$(document).width()- $('.HelpFeedback').outerWidth(true) ? $(document).width()- $('.HelpFeedback').outerWidth(true) : x;
			  y= y<0 ? 0 : y;
			  y= y >$(document).height()- $('.HelpFeedback').outerHeight(true) ? $(document).height()- $('.HelpFeedback').outerHeight(true) : y;
              $('.HelpFeedback').css({
              	'left' : x +'px',
              	'top' :y +'px'
              });
       });
   	$('.HelpFeedback_head').mouseup(function() {
   	    $(document).unbind('mousemove');
   	});
   });
       
	   $('.HelpFeedback_search').mousemove(function(){
		        $(document).unbind('mousemove');
		   });
	   
        /*帮助与反馈-start*/
		
		$('#HelpAndFeedback').click(function(){
			     $('.HelpFeedback').fadeIn(200);
				 $('.HelpFeedback').css({'top':'70px','left':winW-430});
			});
		$('.helpSD').click(function(){
			       $('.HelpFeedback').hide();
			});
		
        /*反馈意见*/
		$('.HelpFeedback_bottom').click(function(){
			       $('body').append("<div class='mask'></div>");
				   $('.mask').show();
				   $('.feedback').show();
				   $('.HelpFeedback').hide();
			});
	    $('.feedback .pass_cancel').click(function(){
						 $('.mask').remove();
						 $('.feedback').hide();
						 $('.HelpFeedback').show();
		   });
			
			
			
 });
