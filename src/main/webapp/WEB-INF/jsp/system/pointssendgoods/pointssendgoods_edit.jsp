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
					
					<form action="pointssendgoods/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="POINTSSENDGOODS_ID" id="POINTSSENDGOODS_ID" value="${pd.POINTSSENDGOODS_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户ID:</td>
								<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="255" placeholder="这里输入用户ID" title="用户ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品编号:</td>
								<td><input type="text" name="GOODSNUM" id="GOODSNUM" value="${pd.GOODSNUM}" maxlength="255" placeholder="这里输入商品编号" title="商品编号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">礼品名称:</td>
								<td><input type="text" name="GOODSNAME" id="GOODSNAME" value="${pd.GOODSNAME}" maxlength="255" placeholder="这里输入礼品名称" title="礼品名称" style="width:98%;"/></td>
							</tr>
							<%--<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">礼品数量:</td>
								<td><input type="number" name="QUANTITYOFGOODS" id="QUANTITYOFGOODS" value="${pd.QUANTITYOFGOODS}" maxlength="32" placeholder="这里输入礼品数量" title="礼品数量" style="width:98%;"/></td>
							</tr>--%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收货人:</td>
								<td><input type="text" name="CONSIGNEE" id="CONSIGNEE" value="${pd.CONSIGNEE}" maxlength="255" placeholder="这里输入收货人" title="收货人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收货人地址:</td>
								<td><input type="text" name="CONSIGNEEADDRESS" id="CONSIGNEEADDRESS" value="${pd.CONSIGNEEADDRESS}" maxlength="255" placeholder="这里输入收货人地址" title="收货人地址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收货人电话:</td>
								<td><input type="text" name="CONSIGNEEPHONE" id="CONSIGNEEPHONE" value="${pd.CONSIGNEEPHONE}" maxlength="255" placeholder="这里输入收货人电话" title="收货人电话" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input type="text" name="CREATETIME" id="CREATETIME" value="${pd.CREATETIME}" maxlength="255" placeholder="这里输入创建时间" title="创建时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">物流单号:</td>
								<td><input type="text" name="LOGISTICS" id="LOGISTICS" value="${pd.LOGISTICS}" maxlength="255" placeholder="这里输入物流单号" title="物流单号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">物流公司:</td>
								<td><input type="text" name="LOGISTICSCOMPANY" id="LOGISTICSCOMPANY" value="${pd.LOGISTICSCOMPANY}" maxlength="255" placeholder="这里输入物流公司" title="物流公司" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">是否发货:</td>
								<td>
									<select id="SENDTAG" name="SENDTAG">
										<option value="0" <c:if test="${pd.SENDTAG == '0' || pd.SENDTAG == '' }">selected</c:if>>未发货</option>
										<option value="1" <c:if test="${pd.SENDTAG == '1' }">selected</c:if>>已发货</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="REMARK" id="REMARK" value="${pd.REMARK}" maxlength="255" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
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
			if($("#GOODSNUM").val()==""){
				$("#GOODSNUM").tips({
					side:3,
		            msg:'请输入商品编号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODSNUM").focus();
			return false;
			}
			if($("#GOODSNAME").val()==""){
				$("#GOODSNAME").tips({
					side:3,
		            msg:'请输入礼品名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODSNAME").focus();
			return false;
			}
			/*if($("#QUANTITYOFGOODS").val()==""){
				$("#QUANTITYOFGOODS").tips({
					side:3,
		            msg:'请输入礼品数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#QUANTITYOFGOODS").focus();
			return false;
			}*/
			if($("#CONSIGNEE").val()==""){
				$("#CONSIGNEE").tips({
					side:3,
		            msg:'请输入收货人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONSIGNEE").focus();
			return false;
			}
			if($("#CONSIGNEEADDRESS").val()==""){
				$("#CONSIGNEEADDRESS").tips({
					side:3,
		            msg:'请输入收货人地址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONSIGNEEADDRESS").focus();
			return false;
			}
			if($("#CONSIGNEEPHONE").val()==""){
				$("#CONSIGNEEPHONE").tips({
					side:3,
		            msg:'请输入收货人电话',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONSIGNEEPHONE").focus();
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
			if($("#LOGISTICS").val()==""){
				$("#LOGISTICS").tips({
					side:3,
		            msg:'请输入物流单号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOGISTICS").focus();
			return false;
			}
			if($("#LOGISTICSCOMPANY").val()==""){
				$("#LOGISTICSCOMPANY").tips({
					side:3,
		            msg:'请输入物流公司',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOGISTICSCOMPANY").focus();
			return false;
			}
			if($("#SENDTAG").val()==""){
				$("#SENDTAG").tips({
					side:3,
		            msg:'请输入是否发货',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SENDTAG").focus();
			return false;
			}
			/*if($("#REMARK").val()==""){
				$("#REMARK").tips({
					side:3,
		            msg:'请输入备注',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REMARK").focus();
			return false;
			}*/
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