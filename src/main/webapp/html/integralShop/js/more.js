var href = window.location.href;
var userid = href.split('?')[1];
var allpoint=0;
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
        	var coin=data.data.appUser.POINTS
        	allpoint=coin;                
        },
        error: function (res) {
			console.log(res);
        }
    })
};
getPoints();
function getMoreProList () {
	var strProList='';
	$.ajax({
        url: 'http://47.100.15.18:8080/pooh-web/app/pointsMall/getPointsMallDetail',
        type: 'get',
		dataType: 'json',	            
        success: function (data) {
        	var arrProList=data.data.pointsGoodsList;
        	console.log(arrProList);
        	for (var i=0;i<arrProList.length;i++) {
        		if (arrProList[i].POINTS>allpoint) {
        			strProList+='<div class="more-list" data-id = "'+arrProList[i].GOODSNUM+'"><a href="pro-details.html?'+userid+'"><div class="more-list-left"><img src="http://47.100.15.18:8888/'+arrProList[i].IMGURL+'" class="more-list-img"/></div><div class="more-list-right"><div class="pro-titlemore">'+arrProList[i].GOODSNAME+'</div><p id="pointsmore">'+arrProList[i].POINTS+'积分</p></div><div class="more-btnban">兑换</div></a></div>';			        			
        		} else{
        			strProList+='<div class="more-list" data-id = "'+arrProList[i].GOODSNUM+'"><a href="pro-details.html?'+userid+'"><div class="more-list-left"><img src="http://47.100.15.18:8888/'+arrProList[i].IMGURL+'" class="more-list-img"/></div><div class="more-list-right"><div class="pro-titlemore">'+arrProList[i].GOODSNAME+'</div><p id="pointsmore">'+arrProList[i].POINTS+'积分</p></div><div class="more-btn">兑换</div></a></div>';			        			
        		}
        	};
        	$('.more-list-cont').append($(strProList));
        },
        error: function (res) {
			console.log(res);
        }
    })
};
getMoreProList();
//点击加入商品编号到本地存储
$('.more-list').live('click',function () {
	var proId=$(this).attr('data-id');
	localStorage.setItem('proId',proId);
});