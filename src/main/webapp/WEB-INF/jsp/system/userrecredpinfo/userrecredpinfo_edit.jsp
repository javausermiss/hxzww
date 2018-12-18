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
					
					<form action="userrecredpinfo/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="USERRECREDPINFO_ID" id="USERRECREDPINFO_ID" value="${pd.USERRECREDPINFO_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">红包ID:</td>
								<td><input type="text" name="REDPACKAGE_ID" id="REDPACKAGE_ID" value="${pd.REDPACKAGE_ID}" maxlength="255" placeholder="这里输入红包ID" title="红包ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发送红包者ID:</td>
								<td><input type="text" name="REDUSERID" id="REDUSERID" value="${pd.REDUSERID}" maxlength="255" placeholder="这里输入发送红包者ID" title="发送红包者ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">领取者ID:</td>
								<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="255" placeholder="这里输入领取者ID" title="领取者ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">金额:</td>
								<td><input type="text" name="GOLD" id="GOLD" value="${pd.GOLD}" maxlength="255" placeholder="这里输入金额" title="金额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input type="text" name="CREATETIME" id="CREATETIME" value="${pd.CREATETIME}" maxlength="255" placeholder="这里输入创建时间" title="创建时间" style="width:98%;"/></td>
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
			if($("#REDPACKAGE_ID").val()==""){
				$("#REDPACKAGE_ID").tips({
					side:3,
		            msg:'请输入红包ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REDPACKAGE_ID").focus();
			return false;
			}
			if($("#REDUSERID").val()==""){
				$("#REDUSERID").tips({
					side:3,
		            msg:'请输入发送红包者ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REDUSERID").focus();
			return false;
			}
			if($("#USERID").val()==""){
				$("#USERID").tips({
					side:3,
		            msg:'请输入领取者ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USERID").focus();
			return false;
			}
			if($("#GOLD").val()==""){
				$("#GOLD").tips({
					side:3,
		            msg:'请输入金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOLD").focus();
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