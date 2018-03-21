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
					
					<form action="trans/order/editWithdrawCashSt.do" name="Form" id="Form" method="post">
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">订单号:</td>
								<td><input type="text" name="orderId" id="orderId" value="${transOrder.orderId}" maxlength="50"  style="width:98%;" readonly="readonly"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">提现金额:</td>
								<td><input type="text" name="transAmt" id="transAmt" value="${transOrder.transAmt/100}"  style="width:98%;"readonly="readonly"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">申请时间:</td>
								<td>
									<fmt:formatDate value="${transOrder.createDate}" pattern="yyyy年MM月dd日HH点mm分ss秒" />
								</td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">开户银行:</td>
								<td>
									${bankCard.BANK_NAME} - ${bankCard.BANK_BRANCH}
								</td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">开户人姓名:</td>
								<td>
									${bankCard.USER_REA_NAME}
								</td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">银行卡号:</td>
								<td>
									${bankCard.BANK_CARD_NO}
								</td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">银行流水号:</td>
								<td><input type="text" name="dmsRelatedKey" id="dmsRelatedKey" value="${transOrder.dmsRelatedKey}"  style="width:98%;"/></td>
							</tr>
							
							<tr>
							<td style="width:75px;text-align: right;padding-top: 13px;">发布状态:</td>
								<td>
								<span style="font-size:12px;">
									<select id="orderSt" name="orderSt">
										<option value="3" <c:if test="${transOrder.orderSt == '3' }">selected</c:if>>待打款</option>
										<option value="4" <c:if test="${transOrder.orderSt == '4' }">selected</c:if>>已取消</option>
										<option value="9" <c:if test="${transOrder.orderSt == '9' }">selected</c:if>>已完成</option>
									</select>
									</span>
								</td>
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
			if($("#dmsRelatedKey").val()==""){
				$("#dmsRelatedKey").tips({
					side:3,
		            msg:'请输入银行流水号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#dmsRelatedKey").focus();
			return false;
			}
            if($("#orderSt").val()==""){
                $("#orderSt").tips({
                    side:3,
                    msg:'请选择订单状态',
                    bg:'#AE81FF',
                    time:2
                });
                $("#orderSt").focus();
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
		$(function(){
		    $('#TOY_ID').chosen();
		});
		</script>
</body>
</html>