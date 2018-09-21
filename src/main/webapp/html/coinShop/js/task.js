//获取用户id			
var href = window.location.href;
var userid = href.split('?')[1];
//选择收货信息js
var shouhuoIfo=JSON.parse(localStorage.getItem('info'));
if (shouhuoIfo) {
	$('.address-name input').val(shouhuoIfo.name);
	$('.address-phone input').val(shouhuoIfo.phoneNuber);
	$('#myAddrs').val(shouhuoIfo.address);
	//$('#address-detail').val(shouhuoIfo.addressDetail);
} 
//姓名验证
function isValidate(name){
    var nameyz = $.trim(name);
    if (nameyz=="")
    {
        bAlert('提示','请输入收货人姓名',{type:'info'});
        return false;
    }
    return true;
};	
//手机号验证
function isValidatePhone(phone) {
    var tel = /^1[34578]\d{9}$/;
    var phones = $.trim(phone);		   
    //验证手机号码是否为空
    if (phones == "") {
        bAlert('提示','手机号不能为空',{type:'info'});
        return false;
    }
   //验证手机号码格式
    else if (!tel.test(phones)) {
        bAlert('提示','请输入正确手机号',{type:'info'});
        return false;
    }
    return true;
};
//地区
function isaddress(address){
    var addre = $.trim(address);
    if (addre=="")
    {
        bAlert('提示','请选择省市区',{type:'info'});
        return false;
    }
    return true;
};
//详细地址
function isaddressDetail(addressDetail){
    var addressDetailss = $.trim(addressDetail);
    if (addressDetailss=="")
    {
        bAlert('提示','请填写详细地址',{type:'info'});
        return false;
    }
    return true;
};	
$('.address-baocunbtn').click(function () {
	var name=$('.address-name input').val();
	var phoneNuber=$('.address-phone input').val();
	var address=$('#myAddrs').val();
	var addressDetail=$('#address-detail').val();
	//姓名
	if (!isValidate(name)) {
        return false;
    };
    //手机号
	if(!isValidatePhone(phoneNuber)){
		return false;
	};
	//省市区
	if(!isaddress(address)){
		return false;
	};
	//详细地址
	if(!isaddressDetail(addressDetail)){
		return false;
	};
	var info = {
        name: name,
        phoneNuber: phoneNuber,
        address: address,
        addressDetail:addressDetail
    };
	localStorage.setItem('info', JSON.stringify(info));	
	history.go(-1);
});
//积分记录JS
//获取总积分(首页总积分也用到此函数)
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
        	$('#jifen').html(coin);
        	$('.alljifen').html(coin);
        },
        error: function (res) {
			console.log(res);
        }
    })
};
getPoints();
//积分记录列表
function getPointRecord () {
	var strRecord='';
	$.ajax({
        url: 'http://47.100.15.18:8080/pooh-web/app/goldMall/getappUserAcountList ',
        type: 'post',
		dataType: 'json',
        data: {
        	userId:userid
        },		            
        success: function (data) {			        	
        	var arr=data.data.goldGoodsList;
        	console.log(data);
        	for (var i=0;i<arr.length;i++) {
        		if (parseInt(arr[i].GOLD)>0) {
        			var why='奖励';
        		} else{
        			var why='支出'
        		};
        		strRecord+='<div class="jifen-record"><div class="jifen-record-name">'+arr[i].REMARK+'</div><div class="jifen-record-time">'+arr[i].CREATE_TIME+'</div><div class="jifen-record-why">'+why+'</div><div class="jifen-record-num">'+arr[i].GOLD+'</div></div>'
        	};
        	$('.jifen-recordOut').append($(strRecord));
        },
        error: function (res) {
			console.log(res);
        }
    })
};
getPointRecord();
//商品兑换记录
function getChangeRecord () {
	var strChangeRecord='';
	$.ajax({
        url: 'http://47.100.15.18:8080/pooh-web/app/goldMall/getGoldGoodsDetail',
        type: 'post',
		dataType: 'json',
        data: {
        	userId:userid
        },		            
        success: function (data) {			        	
        	var arr=data.data.GoldSendGoodsList;
        	console.log(data);
        	for (var i=0;i<arr.length;i++) {
        		strChangeRecord+='<div class="duihuan-list" data-id = "'+arr[i].goodsNum+'"><div class="duihuan-list-left"><img src="http://47.100.15.18:8888/'+arr[i].imgUrl+'" class="duihuan-list-img"/></div><div class="duihuan-list-right"><div>'+arr[i].goodsName+'</div><p>兑换成功</p></div></div>'
//      		strChangeRecord+='<div class="duihuan-list" data-id = "'+arr[i].goodsNum+'"><a href="pro-details.html?'+userid+'"><div class="duihuan-list-left"><img src="http://47.100.15.18:8888/'+arr[i].imgUrl+'" class="duihuan-list-img"/></div><div class="duihuan-list-right"><div>'+arr[i].goodsName+'</div><p>兑换成功</p></div><img src="img/15.png" alt="" class="duihuan-list-btn"/></a> </div>'
        	};
        	$('.duihuan-list-cont').append($(strChangeRecord));
        },
        error: function (res) {
			console.log(res);
        }
    })
};
getChangeRecord();
//$('.duihuan-list').live('click',function () {
//	var proId=$(this).attr('data-id');
//	localStorage.setItem('proId',proId)
//});
