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
					
					<form action="channelmanage/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="CHANNEL_CODE" id="CHANNEL_CODE" value="${pd.CHANNEL_CODE}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">渠道名称:</td>
								<td><input type="text" name="CHANNEL_NAME" id="CHANNEL_NAME" value="${pd.CHANNEL_NAME}" maxlength="255" placeholder="这里输入渠道名称" title="渠道名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">渠道KEY:</td>
								<td><input type="text" name="CHANNEL_KEY" id="CHANNEL_KEY" value="${pd.CHANNEL_KEY}" maxlength="255" placeholder="这里输入渠道KEY" title="渠道KEY" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">渠道状态:</td>
								<td><input type="text" name="CHANNEL_STAT" id="CHANNEL_STAT" value="${pd.CHANNEL_STAT}" maxlength="2" placeholder="这里输入渠道状态" title="渠道状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">渠道类型:</td>
								<td><input type="text" name="CHANNEL_TYPE" id="CHANNEL_TYPE" value="${pd.CHANNEL_TYPE}" maxlength="1" placeholder="这里输入渠道类型" title="渠道类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">渠道等级:</td>
								<td><input type="text" name="CHANNEL_LEVEL" id="CHANNEL_LEVEL" value="${pd.CHANNEL_LEVEL}" maxlength="1" placeholder="这里输入渠道等级" title="渠道等级" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">渠道分成:</td>
								<td><input type="text" name="CHANNEL_PROBABILITY" id="CHANNEL_PROBABILITY" value="${pd.CHANNEL_PROBABILITY}" maxlength="12" placeholder="这里输入渠道分成" title="渠道分成" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="REMARKS" id="REMARKS" value="${pd.REMARKS}" maxlength="255" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
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
			if($("#CHANNEL_NAME").val()==""){
				$("#CHANNEL_NAME").tips({
					side:3,
		            msg:'请输入渠道名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHANNEL_NAME").focus();
			return false;
			}
			if($("#CHANNEL_KEY").val()==""){
				$("#CHANNEL_KEY").tips({
					side:3,
		            msg:'请输入渠道KEY',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHANNEL_KEY").focus();
			return false;
			}
			if($("#CHANNEL_STAT").val()==""){
				$("#CHANNEL_STAT").tips({
					side:3,
		            msg:'请输入渠道状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHANNEL_STAT").focus();
			return false;
			}
			if($("#CHANNEL_TYPE").val()==""){
				$("#CHANNEL_TYPE").tips({
					side:3,
		            msg:'请输入渠道类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHANNEL_TYPE").focus();
			return false;
			}
			if($("#CHANNEL_LEVEL").val()==""){
				$("#CHANNEL_LEVEL").tips({
					side:3,
		            msg:'请输入渠道等级',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHANNEL_LEVEL").focus();
			return false;
			}
			if($("#CHANNEL_PROBABILITY").val()==""){
				$("#CHANNEL_PROBABILITY").tips({
					side:3,
		            msg:'请输入渠道分成',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHANNEL_PROBABILITY").focus();
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