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
					
					<form action="pushergamedetail/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="PUSHERGAMEDETAIL_ID" id="PUSHERGAMEDETAIL_ID" value="${pd.PUSHERGAMEDETAIL_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">场次ID:</td>
								<td><input type="text" name="GAMEID" id="GAMEID" value="${pd.GAMEID}" maxlength="255" placeholder="这里输入场次ID" title="场次ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">房间ID:</td>
								<td><input type="text" name="ROOMID" id="ROOMID" value="${pd.ROOMID}" maxlength="255" placeholder="这里输入房间ID" title="房间ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户ID:</td>
								<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="255" placeholder="这里输入用户ID" title="用户ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">总消费:</td>
								<td><input type="number" name="EXPENDITURE" id="EXPENDITURE" value="${pd.EXPENDITURE}" maxlength="32" placeholder="这里输入总消费" title="总消费" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">总收入:</td>
								<td><input type="number" name="INCOME" id="INCOME" value="${pd.INCOME}" maxlength="32" placeholder="这里输入总收入" title="总收入" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">完成标签:</td>
								<td><input type="text" name="TAG" id="TAG" value="${pd.TAG}" maxlength="255" placeholder="这里输入完成标签" title="完成标签" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">开始时间:</td>
								<td><input type="text" name="CREATETIME" id="CREATETIME" value="${pd.CREATETIME}" maxlength="255" placeholder="这里输入开始时间" title="开始时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">结束时间:</td>
								<td><input type="text" name="UPDATETIME" id="UPDATETIME" value="${pd.UPDATETIME}" maxlength="255" placeholder="这里输入结束时间" title="结束时间" style="width:98%;"/></td>
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
			if($("#GAMEID").val()==""){
				$("#GAMEID").tips({
					side:3,
		            msg:'请输入场次ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GAMEID").focus();
			return false;
			}
			if($("#ROOMID").val()==""){
				$("#ROOMID").tips({
					side:3,
		            msg:'请输入房间ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ROOMID").focus();
			return false;
			}
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
			if($("#EXPENDITURE").val()==""){
				$("#EXPENDITURE").tips({
					side:3,
		            msg:'请输入总消费',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#EXPENDITURE").focus();
			return false;
			}
			if($("#INCOME").val()==""){
				$("#INCOME").tips({
					side:3,
		            msg:'请输入总收入',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INCOME").focus();
			return false;
			}
			if($("#TAG").val()==""){
				$("#TAG").tips({
					side:3,
		            msg:'请输入完成标签',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TAG").focus();
			return false;
			}
			if($("#CREATETIME").val()==""){
				$("#CREATETIME").tips({
					side:3,
		            msg:'请输入开始时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CREATETIME").focus();
			return false;
			}
			if($("#UPDATETIME").val()==""){
				$("#UPDATETIME").tips({
					side:3,
		            msg:'请输入结束时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#UPDATETIME").focus();
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