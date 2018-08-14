$(function () {
	
	var href = window.location.href;
	var useridO = href.split('?')[1];
	var userid=useridO.split('=')[1];
	//获取金币
	function getcoin () {
		$.ajax({
	        url: 'http://111.231.139.61:18081/pooh-web/api/user/getUserInfo',
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
	
	//用户信息以及充值信息给后台	
	$('.recharge-li').click(function () {				
		var payOutType=$(this).attr('data-id');
		var pid=$(this).find('.recharge-moneynum').attr('data-id')
		console.log('pid：'+pid);
		console.log(payOutType);
		$.ajax({
            url: 'http://111.231.139.61:18081/pooh-web/app/pay/getTradeOrderAlipayForIos',
            type: 'post',
			dataType: 'text',
            data: {
	        	userId:userid,
	        	pid:pid,
	        	ctype:'YSDK',
	        	channel:'IOS',
	        	payType:'R'
            },		            
            success: function (data) {
            	document.write(data);
            	console.log(data);		               
            },
            error: function (res) {
				console.log(res);
	        }
        })
	});
})
