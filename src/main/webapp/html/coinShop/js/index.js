var href = window.location.href;
var useridO = href.split('?')[1];
var userid=useridO.split('=')[1];
function getProList () {
	var strProList='';
	$.ajax({
        url: 'http://47.100.15.18:8080/pooh-web/app/goldMall/getGoldMallDetail',
        type: 'get',
		dataType: 'json',	            
        success: function (data) {			        	
        	var arrProList=data.data.goldGoodsList;
        	console.log(data);
        	for (var i=0;i<arrProList.length;i++) {
        		strProList+='<li class="proli" data-id = "'+arrProList[i].GOODSNUM+'"><a href="pro-details.html?'+userid+'"><div class="shop-procont"><img src="http://47.100.15.18:8888/'+arrProList[i].IMGURL+'"/><p>'+arrProList[i].GOODSNAME+'</p><div>'+arrProList[i].POINTS+'金币</div></div></a></li>';
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
        url: 'http://47.100.15.18:8080/pooh-web/api/user/getUserInfo',
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