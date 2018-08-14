<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="userpoints/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="USERPOINTS_ID" id="USERPOINTS_ID" value="${pd.USERPOINTS_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户ID:</td>
								<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="255" placeholder="这里输入用户ID" title="用户ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">登录游戏:</td>
								<td><input type="text" name="LOGINGAME" id="LOGINGAME" value="${pd.LOGINGAME}" maxlength="255" placeholder="这里输入登录游戏" title="登录游戏" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">分享游戏:</td>
								<td><input type="text" name="SHAREGAME" id="SHAREGAME" value="${pd.SHAREGAME}" maxlength="255" placeholder="这里输入分享游戏" title="分享游戏" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">邀请好友:</td>
								<td><input type="text" name="INVITEGAME" id="INVITEGAME" value="${pd.INVITEGAME}" maxlength="255" placeholder="这里输入邀请好友" title="邀请好友" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">首次抓娃娃:</td>
								<td><input type="text" name="POOHGAME" id="POOHGAME" value="${pd.POOHGAME}" maxlength="255" placeholder="这里输入首次抓娃娃" title="首次抓娃娃" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">推币机游戏:</td>
								<td><input type="text" name="PUSHERGAME" id="PUSHERGAME" value="${pd.PUSHERGAME}" maxlength="255" placeholder="这里输入推币机游戏" title="推币机游戏" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">每日充值:</td>
								<td><input type="text" name="FIRSTPAY" id="FIRSTPAY" value="${pd.FIRSTPAY}" maxlength="255" placeholder="这里输入每日充值" title="每日充值" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">累计消耗金币:</td>
								<td><input type="number" name="GOLDSUM" id="GOLDSUM" value="${pd.GOLDSUM}" maxlength="32" placeholder="这里输入累计消耗金币" title="累计消耗金币" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
					</form>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			if($("#USERID").val()==""){
				$("#USERID").tips({
					side:3,
		            msg:'请输入用户ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USERID").focus();
			return false;
			}
			if($("#LOGINGAME").val()==""){
				$("#LOGINGAME").tips({
					side:3,
		            msg:'请输入登录游戏',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOGINGAME").focus();
			return false;
			}
			if($("#SHAREGAME").val()==""){
				$("#SHAREGAME").tips({
					side:3,
		            msg:'请输入分享游戏',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHAREGAME").focus();
			return false;
			}
			if($("#INVITEGAME").val()==""){
				$("#INVITEGAME").tips({
					side:3,
		            msg:'请输入邀请好友',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INVITEGAME").focus();
			return false;
			}
			if($("#POOHGAME").val()==""){
				$("#POOHGAME").tips({
					side:3,
		            msg:'请输入首次抓娃娃',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POOHGAME").focus();
			return false;
			}
			if($("#PUSHERGAME").val()==""){
				$("#PUSHERGAME").tips({
					side:3,
		            msg:'请输入推币机游戏',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PUSHERGAME").focus();
			return false;
			}
			if($("#FIRSTPAY").val()==""){
				$("#FIRSTPAY").tips({
					side:3,
		            msg:'请输入每日充值',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#FIRSTPAY").focus();
			return false;
			}
			if($("#GOLDSUM").val()==""){
				$("#GOLDSUM").tips({
					side:3,
		            msg:'请输入累计消耗金币',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOLDSUM").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>