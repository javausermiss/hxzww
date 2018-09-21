$(function () {
	var href = window.location.href;
	var userid = href.split('?')[1];
	var allPoints=0;
	//获取总积分
	function getPoints () {
		$.ajax({
	        url: 'http://47.100.15.18:8080/pooh-web/api/user/getUserInfo',
	        type: 'post',
	        async:false,
			dataType: 'json',
	        data: {
	        	userId:userid
	        },		            
	        success: function (data) {
	        	allPoints=data.data.appUser.BALANCE;
	        	console.log(allPoints);
	        },
	        error: function (res) {
				console.log(res);
	        }
	    });
	};	
	getPoints();
	function getProDetail () {
		var proId=localStorage.getItem('proId');
		$.ajax({
	        url: 'http://47.100.15.18:8080/pooh-web/app/goldMall/getGoldMallDetail',
	        type: 'get',
			dataType: 'json',	            
	        success: function (data) {			        	
	        	var arrProList=data.data.goldGoodsList;
	        	console.log(arrProList);
	        	for (var i=0;i<arrProList.length;i++) {
	        		if (arrProList[i].GOODSNUM==proId) {
	//      			alert(arrProList[i].GOODSNUM);
	        			$('#pro-banner').attr('src','http://47.100.15.18:8888/'+arrProList[i].IMGURL_GOODSDETAIL_TOP);
	        			$('.pro-title').html(arrProList[i].GOODSNAME);
	        			$('#points').html(arrProList[i].POINTS);
	        			$('.jiage').html('￥'+arrProList[i].ORIGINALVALUEOFGOODS);
	        			$('#pro-detail-img').attr('src','http://47.100.15.18:8888/'+arrProList[i].IMGURL_GOODSDETAIL_MID);
						if (parseInt($('#points').html())>allPoints) {	
							$('.buy-btn').html('金币不足').css('background-color','#ccc');
						} 
	        		} else{
	        			
	        		}
	        	};
	        },
	        error: function (res) {
				console.log(res);
	        }
	    })
	};
	getProDetail();
	var info=JSON.parse(localStorage.getItem('info'));
	if (info) {
		$('#address').html(info.address);
	} else{
		$('#address').html('你还未填写收货信息，马上去填写');
	};
	//兑换按钮
	$('.buy-btn').click(function () {				
		if ($('#address').html()=='你还未填写收货信息，马上去填写') {
			bAlert('提示','请填写收货信息',{type:'info'});
		} else{
	//		localStorage.removeItem('info');
			var goodsNum=localStorage.getItem('proId');
			var proPoints=$('#points').html();
			var proName=$('.pro-title').html();
			var addressDet=info.address+info.addressDetail;
			$.ajax({
		        url: 'http://47.100.15.18:8080/pooh-web/app/goldMall/exchangeGoldGoods',
		        type: 'post',
				dataType: 'json',
		        data: {
		        	userId:userid,
		        	gold:proPoints,
		        	goodsName:proName,
		        	consignee:info.name,
		        	consigneePhone:info.phoneNuber,
		        	consigneeAddress:addressDet,
		        	goodsNum:goodsNum
		        },
		        success: function (data) {
		        	bAlert('提示', data.data,{type:'info'},function () {
		        		history.go(0);
		        	});
		        	console.log(data);
		        },
		        error: function (res) {
					console.log(res);
		        }
		   });
		};				
	});	
})		