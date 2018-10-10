var href = window.location.href;
var useridO = href.split('?')[1];
var userid=useridO.split('=')[1];
function getProList () {
	var strProList='';
	$.ajax({
        url: 'http://111.231.139.61:18081/pooh-web/app/goldMall/getGoldMallDetail',
        type: 'get',
		dataType: 'json',	            
        success: function (data) {			        	
        	var arrProList=data.data.goldGoodsList;
        	console.log(data);
        		strProList+='<li class="proli" data-id = "'+arrProList[0].GOODSNUM+'"><a href="pro-details.html?'+userid+'"><div class="shop-procont"><img src="http://111.231.139.61:8888/'+arrProList[0].IMGURL+'"/><p>'+arrProList[0].GOODSNAME+'</p><div>'+arrProList[0].POINTS+'金币</div></div></a><span class="duihuan-num">已兑换76件</span></li>';
        		strProList+='<li class="proli" data-id = "'+arrProList[1].GOODSNUM+'"><a href="pro-details.html?'+userid+'"><div class="shop-procont"><img src="http://111.231.139.61:8888/'+arrProList[1].IMGURL+'"/><p>'+arrProList[1].GOODSNAME+'</p><div>'+arrProList[1].POINTS+'金币</div></div></a><span class="duihuan-num">已兑换129件</span></li>';
        		strProList+='<li class="proli" data-id = "'+arrProList[2].GOODSNUM+'"><a href="pro-details.html?'+userid+'"><div class="shop-procont"><img src="http://111.231.139.61:8888/'+arrProList[2].IMGURL+'"/><p>'+arrProList[2].GOODSNAME+'</p><div>'+arrProList[2].POINTS+'金币</div></div></a><span class="duihuan-num">已兑换468件</span></li>';
        		strProList+='<li class="proli" data-id = "'+arrProList[3].GOODSNUM+'"><a href="pro-details.html?'+userid+'"><div class="shop-procont"><img src="http://111.231.139.61:8888/'+arrProList[3].IMGURL+'"/><p>'+arrProList[3].GOODSNAME+'</p><div>'+arrProList[3].POINTS+'金币</div></div></a><span class="duihuan-num">已兑换288件</span></li>';
        	
        	for (var i=4;i<arrProList.length;i++) {
        		strProList+='<li class="proli" data-id = "'+arrProList[i].GOODSNUM+'"><a href="pro-details.html?'+userid+'"><div class="shop-procont"><img src="http://111.231.139.61:8888/'+arrProList[i].IMGURL+'"/><p>'+arrProList[i].GOODSNAME+'</p><div>'+arrProList[i].POINTS+'金币</div></div></a></li>';
        	};
        	$('.shop-pro-list').append($(strProList));
        },
        error: function (res) {
			console.log(res);
        }
    })
};
getProList();
//获取总积分
function getPoints () {
	$.ajax({
        url: 'http://111.231.139.61:18081/pooh-web/api/user/getUserInfo',
        type: 'post',
		dataType: 'json',
        data: {
        	userId:userid
        },		            
        success: function (data) {
        	console.log(data)
        	var coin=data.data.appUser.BALANCE
        	$('#jifen').html(coin)	                
        },
        error: function (res) {
			console.log(res);
        }
    })
};
getPoints();
$('.proli').live('click',function () {
	var proId=$(this).attr('data-id');
	localStorage.setItem('proId',proId);
});