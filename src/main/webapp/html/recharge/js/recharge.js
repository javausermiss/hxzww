$(function () {	
	var href = window.location.href;
	var useridO = href.split('?')[1];
	var userid=useridO.split('&')[0].split('=')[1];
	var appVersion=useridO.split('&')[1].split('=')[1];
	var payOutType,pid	
	//获取金币
	function getcoin () {
		$.ajax({
	        url: 'http://47.100.15.18:8080/pooh-web/api/user/getUserInfo',
	        type: 'post',
			dataType: 'json',
	        data: {
	        	userId:userid
	        },		            
	        success: function (data) {
	        	var coin=data.data.appUser.BALANCE
	        	$('#BALANCE').html(coin)	                
	        },
	        error: function (res) {
				console.log(res);
	        }
	    })
	};
	getcoin();
	$('.recharge-li').click(function () {
		payOutType=$(this).attr('data-id');
		pid=$(this).find('.recharge-moneynum').attr('data-id');
		console.log('pid：'+pid);
		console.log(payOutType);
		$('.cover').fadeIn();
	});
	$('.ximgCont').click(function () {
		$('.cover').hide();
	});
	//用户信息以及充值信息给后台	(微信)
	$('.weixin').click(function () {				
		$.ajax({
            url: 'http://47.100.15.18:8080/pooh-web/app/pay/getTradeOrderxdpayIOS',
            type: 'post',
			dataType: 'json',
            data: {
	        	userId:userid,
	        	pid:pid,
	        	ctype:'现在微信IOS',
	        	channel:'IOS',
	        	payType:'R',
	        	appVersion:appVersion,
	        	payOutType:payOutType,
	        	payChannelType:'13'
            },		            
            success: function (data) {
            	console.log(data.data); 
            	window.location.href=data.data;
            },
            error: function (res) {
				console.log(res);
	        }
        })
	});
	//用户信息以及充值信息给后台	(支付宝)
	$('.zhifubao').click(function () {				
		$.ajax({
            url: 'http://47.100.15.18:8080/pooh-web/app/pay/getTradeOrderAlipayForIos',
            type: 'post',
			dataType: 'text',
            data: {
	        	userId:userid,
	        	pid:pid,
	        	ctype:'支付宝IOS',
	        	channel:'IOS',
	        	payType:'R',
	        	payOutType:payOutType
            },		            
            success: function (data) {
            	console.log(data);            	
            	document.write(data);		               
            },
            error: function (res) {
				console.log(res);
	        }
        })
	});
})
