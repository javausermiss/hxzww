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
					
					<form action="goldgoods/${msg }.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
						<input type="hidden" name="GOLDGOODS_ID" id="GOLDGOODS_ID" value="${pd.GOLDGOODS_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">编号:</td>
								<td><input type="number" name="GOODSNUM" id="GOODSNUM" value="${pd.GOODSNUM}" maxlength="32" placeholder="这里输入编号" title="编号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品名:</td>
								<td><input type="text" name="GOODSNAME" id="GOODSNAME" value="${pd.GOODSNAME}" maxlength="255" placeholder="这里输入商品名" title="商品名" style="width:98%;"/></td>
							</tr>
							
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">金币:</td>
								<td><input type="number" name="POINTS" id="POINTS" value="${pd.POINTS}" maxlength="32" placeholder="这里输入类型" title="类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品原始价值:</td>
								<td><input type="number" name="ORIGINALVALUEOFGOODS" id="ORIGINALVALUEOFGOODS" value="${pd.ORIGINALVALUEOFGOODS}" maxlength="32" placeholder="这里输入商品原始价值" title="商品原始价值" style="width:98%;"/></td>
							</tr>
							<td style="width:79px;text-align: right;padding-top: 13px;">状态:</td>
							<td>
								<select name="SHOWTAG" title="展示标签">
									<option value="1" <c:if test="${pd.SHOWTAG == '1' }">selected</c:if> >展示</option>
									<option value="0" <c:if test="${pd.SHOWTAG == '0' }">selected</c:if> >隐藏</option>
								</select>
							</td>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">商品图片：</td>
								<td><input type="file" id="IMGURL" name="IMGURL" /></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">详情头部图片：</td>
								<td><input type="file" id="IMGURL_GOODSDETAIL_TOP" name="IMGURL_GOODSDETAIL_TOP" /></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">详情中部图片：</td>
								<td><input type="file" id="IMGURL_GOODSDETAIL_MID" name="IMGURL_GOODSDETAIL_MID" /></td>
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
			if($("#GOODSNUM").val()==""){
				$("#GOODSNUM").tips({
					side:3,
		            msg:'请输入编号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODSNUM").focus();
			return false;
			}
			if($("#GOODSNAME").val()==""){
				$("#GOODSNAME").tips({
					side:3,
		            msg:'请输入商品名',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODSNAME").focus();
			return false;
			}
			if($("#POINTS").val()==""){
				$("#POINTS").tips({
					side:3,
		            msg:'请输入金币',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POINTS").focus();
			return false;
			}
			if($("#ORIGINALVALUEOFGOODS").val()==""){
				$("#ORIGINALVALUEOFGOODS").tips({
					side:3,
		            msg:'请输入商品原始价值',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORIGINALVALUEOFGOODS").focus();
			return false;
			}
			if($("#SHOWTAG").val()==""){
				$("#SHOWTAG").tips({
					side:3,
		            msg:'请输入是否展示',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHOWTAG").focus();
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