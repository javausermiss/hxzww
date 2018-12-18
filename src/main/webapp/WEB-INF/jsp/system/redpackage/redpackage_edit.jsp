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
					
					<form action="redpackage/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="REDPACKAGE_ID" id="REDPACKAGE_ID" value="${pd.REDPACKAGE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户Id:</td>
								<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="255" placeholder="这里输入用户Id" title="用户Id" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">红包金额:</td>
								<td><input type="number" name="REDGOLD" id="REDGOLD" value="${pd.REDGOLD}" maxlength="32" placeholder="这里输入红包金额" title="红包金额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">红包数量:</td>
								<td><input type="number" name="REDNUM" id="REDNUM" value="${pd.REDNUM}" maxlength="32" placeholder="这里输入红包数量" title="红包数量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">锁:</td>
								<td><input type="text" name="VERSION" id="VERSION" value="${pd.VERSION}" maxlength="255" placeholder="这里输入锁" title="锁" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">领取完成标签:</td>
								<td><input type="text" name="TAG" id="TAG" value="${pd.TAG}" maxlength="255" placeholder="这里输入领取完成标签" title="领取完成标签" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input type="text" name="CREATETIME" id="CREATETIME" value="${pd.CREATETIME}" maxlength="255" placeholder="这里输入创建时间" title="创建时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">更新时间:</td>
								<td><input type="text" name="UPDATETIME" id="UPDATETIME" value="${pd.UPDATETIME}" maxlength="255" placeholder="这里输入更新时间" title="更新时间" style="width:98%;"/></td>
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
		            msg:'请输入用户Id',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USERID").focus();
			return false;
			}
			if($("#REDGOLD").val()==""){
				$("#REDGOLD").tips({
					side:3,
		            msg:'请输入红包金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REDGOLD").focus();
			return false;
			}
			if($("#REDNUM").val()==""){
				$("#REDNUM").tips({
					side:3,
		            msg:'请输入红包数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REDNUM").focus();
			return false;
			}
			if($("#VERSION").val()==""){
				$("#VERSION").tips({
					side:3,
		            msg:'请输入锁',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#VERSION").focus();
			return false;
			}
			if($("#TAG").val()==""){
				$("#TAG").tips({
					side:3,
		            msg:'请输入领取完成标签',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TAG").focus();
			return false;
			}
			if($("#CREATETIME").val()==""){
				$("#CREATETIME").tips({
					side:3,
		            msg:'请输入创建时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CREATETIME").focus();
			return false;
			}
			if($("#UPDATETIME").val()==""){
				$("#UPDATETIME").tips({
					side:3,
		            msg:'请输入更新时间',
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