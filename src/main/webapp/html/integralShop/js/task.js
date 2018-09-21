//获取用户id			
var href = window.location.href;
var userid = href.split('?')[1];
//积分任务JS
function getTask () {
	var strhtml='';
	$.ajax({
        url: 'http://47.100.15.18:8080/pooh-web/app/pointsMall/pointsTask',
        type: 'post',
		dataType: 'json',
        data: {
        	userId:userid
        },		            
        success: function (data) {			        	
        	var obj=data.data;
        	console.log(data);
        	$('.task-tasknum').html(obj.userPoints.todayPoints);        	
        	//设置进度条
        	if (obj.userPoints.todayPoints>=0&&obj.userPoints.todayPoints<5) {
        		$('.task-img').attr('src','img/jindu0.png');
        	} else if(obj.userPoints.todayPoints>=5&&obj.userPoints.todayPoints<10){
        		$('.task-img').attr('src','img/jindu1.png');
        	}else if(obj.userPoints.todayPoints>=10&&obj.userPoints.todayPoints<15){
        		$('.task-img').attr('src','img/jindu2.png');
        	}else if(obj.userPoints.todayPoints>=15&&obj.userPoints.todayPoints<20){
        		$('.task-img').attr('src','img/jindu3.png');
        	}else if(obj.userPoints.todayPoints>=20&&obj.userPoints.todayPoints<50){
        		$('.task-img').attr('src','img/jindu4.png');
        	}else if(obj.userPoints.todayPoints>=50){
        		$('.task-img').attr('src','img/jindu5.png');
        	};
        	//列表
        	if (obj.userPoints.loginGame=='0') {
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[0].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[0].pointsValue+'积分</div><div class="task-record-stateyes">去完成</div></div>'
        	} else{
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[0].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[0].pointsValue+'积分</div><div class="task-record-stateyes">已完成</div></div>'		        		
        	};
        	if (obj.userPoints.shareGame=='0') {
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[1].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[1].pointsValue+'积分</div><div class="task-record-stateyes" onclick="share()">去完成</div></div>'
        	} else{
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[1].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[1].pointsValue+'积分</div><div class="task-record-stateyes">已完成</div></div>'		        		
        	};
        	if (obj.userPoints.inviteGame=='0') {
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[2].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[2].pointsValue+'积分</div><div class="task-record-stateyes" onclick="invite()">去完成</div></div>'
        	} else{
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[2].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[2].pointsValue+'积分</div><div class="task-record-stateyes">已完成</div></div>'		        		
        	};
        	if (obj.userPoints.poohGame=='0') {
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[3].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[3].pointsValue+'积分</div><div class="task-record-stateyes" onclick="fist()">去完成</div></div>'
        	} else{
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[3].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[3].pointsValue+'积分</div><div class="task-record-stateyes">已完成</div></div>'		        		
        	};
        	if (obj.userPoints.pusherGame<10) {
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[4].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[4].pointsValue+'积分</div><div class="task-record-stateyes">'+obj.userPoints.pusherGame+'/10</div></div>'
        	} else{
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[4].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[4].pointsValue+'积分</div><div class="task-record-stateyes">已完成</div></div>'		        		
        	};
        	if (obj.userPoints.costGoldSum<200) {
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[5].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[5].pointsValue+'积分</div><div class="task-record-stateyes">'+obj.userPoints.costGoldSum+'/200</div></div>'
        	} else{
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[5].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[5].pointsValue+'积分</div><div class="task-record-stateyes">已完成</div></div>'		        		
        	};
        	if (obj.userPoints.firstPay=='0') {
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[6].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[6].pointsValue+'积分</div><div class="task-record-stateyes" onclick="recharge()">去完成</div></div>'
        	} else{
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[6].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[6].pointsValue+'积分</div><div class="task-record-stateyes">已完成</div></div>'		        		
        	};
        	if (obj.userPoints.betGame=='0') {
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[7].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[7].pointsValue+'积分</div><div class="task-record-stateyes" onclick="betGame()">去完成</div></div>'
        	} else{
        		strhtml+='<div class="task-record"><div class="task-record-name">'+obj.pointsMall[7].pointsName+'</div><div class="task-record-jifennum">+'+obj.pointsMall[7].pointsValue+'积分</div><div class="task-record-stateyes">已完成</div></div>'		        		
        	};
        	$('.task-list-cont').append($(strhtml));
        	$('.task-record-stateyes:contains("去完成")').css('backgroundColor','#fed201');
			$('.task-record-stateyes:contains("已完成")').css('background','url(img/dui.png) 1.1rem center no-repeat').css('background-size','0.24rem 0.18rem').css('backgroundColor','#66ec1c');
        },
        error: function (res) {
			console.log(res);
        }
    })
};
getTask();
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
        	var coin=data.data.appUser.POINTS
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
        url: 'http://47.100.15.18:8080/pooh-web/app/pointsMall/getPointsDetail',
        type: 'post',
		dataType: 'json',
        data: {
        	userId:userid
        },		            
        success: function (data) {			        	
        	var arr=data.data.pointsDetail;
//      	console.log(arr);
        	for (var i=0;i<arr.length;i++) {
        		if (arr[i].type=='+') {
        			var why='奖励';
        		} else{
        			var why='支出'
        		};
        		strRecord+='<div class="jifen-record"><div class="jifen-record-name">'+arr[i].channel+'</div><div class="jifen-record-time">'+arr[i].createTime+'</div><div class="jifen-record-why">'+why+'</div><div class="jifen-record-num">'+arr[i].type+arr[i].pointsValue+'</div></div>'
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
        url: 'http://47.100.15.18:8080/pooh-web/app/pointsMall/getPointsGoodsDetail',
        type: 'post',
		dataType: 'json',
        data: {
        	userId:userid
        },		            
        success: function (data) {			        	
        	var arr=data.data.PointsSendGoodsList;
        	console.log(arr);
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
