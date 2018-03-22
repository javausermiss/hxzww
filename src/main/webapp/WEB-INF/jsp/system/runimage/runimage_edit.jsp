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
					
					<form action="runimage/${msg }.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
						<input type="hidden" name="RUNIMAGE_ID" id="RUNIMAGE_ID" value="${pd.RUNIMAGE_ID}"/>
						<input type="hidden" name="CONTENT" id="CONTENT" value=""/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图片标题:</td>
								<td><input type="text" name="RUN_NAME" id="RUN_NAME" value="${pd.RUN_NAME}" maxlength="255" placeholder="这里输入图片标题" title="图片标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图像地址:</td>
								<td><input type="text" name="IMAGE_URL" id="IMAGE_URL" value="${pd.IMAGE_URL}" maxlength="255" placeholder="这里输入图像地址" title="图像地址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">图片上传:</td>
								<td><input type="file" id="RUN_FILE" name="RUN_FILE" /></td>
							</tr>
							<tr>
							<td style="width:75px;text-align: right;padding-top: 13px;">新闻路径:</td>
							<td><input type="text" name="HREF_ST" id="HREF_ST" value="${pd.HREF_ST}" maxlength="255" placeholder="这里输入图像跳转地址" title="图像跳转地址" style="width:98%;"/></td>
							</tr>
							<!-- <tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">新闻内容:</td>
								<td><script id="editor"  type="text/plain" style="width:100%;height:200px;"></script></td>
							</tr> -->
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">流媒体名称:</td>
								<td><input type="text" name="LIVESTREAM" id="LIVESTREAM" value="${pd.LIVESTREAM}" 
								maxlength="200" placeholder="这里输入流媒体名称" title="LIVESTREAM" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">推流服务名称:</td>
								<td><input type="text" name="SERVER_NAME" id="SERVER_NAME" value="${pd.SERVER_NAME}" 
								maxlength="200" placeholder="这里输入推流服务名称" title="SERVER_NAME" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">RTMP拉流地址:</td>
								<td><input type="text" name="RTMP_URL" id="RTMP_URL" value="${pd.RTMP_URL}" 
								maxlength="200" placeholder="这里输入RTMP拉流地址" title="RTMP_URL" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">H5拉流地址:</td>
								<td><input type="text" name="H5_URL" id="H5_URL" value="${pd.H5_URL}" 
								maxlength="200" placeholder="这里输入H5拉流地址" title="H5_URL" style="width:98%;"/></td>
							</tr>
							<tr>
							<td style="width:75px;text-align: right;padding-top: 13px;">设备状态:</td>
								<td>
								<span style="font-size:12px;">
									<select id="DEVICE_STATE" name="DEVICE_STATE">
										<option value="1" <c:if test="${pd.DEVICE_STATE == '1' }">selected</c:if>>不可用</option>
										<option value="0" <c:if test="${pd.DEVICE_STATE == '0' }">selected</c:if>>可用</option>
									</select>
									</span>
								</td>
							</tr>
							<tr>
							<td style="width:75px;text-align: right;padding-top: 13px;">设备状态:</td>
								<td>
								<span style="font-size:12px;">
									<select id="STATE" name="STATE">
										<option value="1" <c:if test="${pd.STATE == '1' }">selected</c:if>>视频路径</option>
										<option value="0" <c:if test="${pd.STATE == '0' }">selected</c:if>>图片路径</option>
									</select>
									</span>
								</td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="2">
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
	
<!--     <script type="text/javascript" charset="utf-8" src="static/ueditor/ueditor.config.js"></script> -->
<!--     <script type="text/javascript" charset="utf-8" src="static/ueditor/ueditor.all.min.js"> </script> -->
<!--     <script type="text/javascript" charset="utf-8" src="static/ueditor/lang/zh-cn/zh-cn.js"></script> -->
	
		<script type="text/javascript">
		$(top.hangge());
		
// 		  //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
// 	    var ue = UE.getEditor('editor');
// 	    UE.getEditor('editor').addListener("ready", function () {
// 	    	// editor准备好之后才可以使用
// 	    	UE.getEditor('editor').setContent('${pd.CONTENT}');
// 	    });
		
// 	    function getContent() {
// 	      return  ue.getContent();
// 	    }
	    
		//保存
		function save(){
			if($("#RUN_NAME").val()==""){
				$("#RUN_NAME").tips({
					side:3,
		            msg:'请输入图片名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RUN_NAME").focus();
			return false;
			}
//             var content=getContent();
//             $('#CONTENT').val(content);
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