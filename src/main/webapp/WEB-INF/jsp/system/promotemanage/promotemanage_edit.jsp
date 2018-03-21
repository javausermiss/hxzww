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
					
					<form action="promotemanage/${msg }.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
						<input type="hidden" name="PRO_MANAGE_ID" id="PRO_MANAGE_ID" value="${pd.PRO_MANAGE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">活动名称:</td>
								<td><input type="text" name=PRO_MANAGE_NAME id="PRO_MANAGE_NAME" value="${pd.PRO_MANAGE_NAME}" maxlength="30" placeholder="这里输入活动名称" title="活动名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">支付金币数:</td>
								<td><input type="number" name="PAY_GOLD" id="PAY_GOLD" value="${pd.PAY_GOLD}" maxlength="12" placeholder="这里输入支付金币数" title="支付金币数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">下级可获金币数:</td>
								<td><input type="number" name="CONVER_GOLD" id="CONVER_GOLD" value="${pd.CONVER_GOLD}" maxlength="12" placeholder="这里输入下级可获金币数" title="下级可获金币数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图片地址:</td>
								<td><input type="text" name="IMG_URL" id="IMG_URL" value="${pd.IMG_URL}" maxlength="32" placeholder="这里输入图片地址" title="图片地址" style="width:98%;"/> &nbsp;&nbsp;<input type="file" id="IMG_FILE" name="IMG_FILE" /></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">返回比例:</td>
								<td><input type="number" name="RETURN_RATIO" id="RETURN_RATIO" value="${pd.RETURN_RATIO}" maxlength="12" placeholder="这里输入返回比例" title="返回比例" style="width:98%;"/></td>
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
			
			if($("#PRO_MANAGE_NAME").val()==""){
				$("#PRO_MANAGE_NAME").tips({
					side:3,
		            msg:'请输入活动名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PRO_MANAGE_NAME").focus();
			return false;
			}
			if($("#PAY_GOLD").val()==""){
				$("#PAY_GOLD").tips({
					side:3,
		            msg:'请输入获取金币数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAY_GOLD").focus();
			return false;
			}
			if($("#CONVER_GOLD").val()==""){
				$("#CONVER_GOLD").tips({
					side:3,
		            msg:'请输入获取金币数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONVER_GOLD").focus();
			return false;
			}
			if($("#RETURN_RATIO").val()==""){
				$("#RETURN_RATIO").tips({
					side:3,
		            msg:'请输入返回比例',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RETURN_RATIO").focus();
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