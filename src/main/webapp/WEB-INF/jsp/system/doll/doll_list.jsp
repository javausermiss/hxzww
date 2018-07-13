<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/ace/css/chosen.css"/>
    <!-- jsp文件头和头部 -->
    <%@ include file="../../system/index/top.jsp" %>
    <!-- 日期框 -->
    <link rel="stylesheet" href="static/ace/css/datepicker.css"/>
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

                        <!-- 检索  -->
                        <form action="doll/list.do" method="post" name="Form" id="Form">
                            <table style="margin-top:5px;">
                                <tr>
                                    <td>
                                        <div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input"
                                                   id="nav-search-input" autocomplete="off" name="keywords"
                                                   value="${pd.keywords }" placeholder="这里输入关键词"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
                                        </div>
                                    </td>
                                    <td style="padding-left:2px;"><input class="span10 date-picker" name="lastStart"
                                                                         id="lastStart" value="" type="text"
                                                                         data-date-format="yyyy-mm-dd"
                                                                         readonly="readonly" style="width:88px;"
                                                                         placeholder="开始日期" title="开始日期"/></td>
                                    <td style="padding-left:2px;"><input class="span10 date-picker" name="lastEnd"
                                                                         name="lastEnd" value="" type="text"
                                                                         data-date-format="yyyy-mm-dd"
                                                                         readonly="readonly" style="width:88px;"
                                                                         placeholder="结束日期" title="结束日期"/></td>
                                    <td style="vertical-align:top;padding-left:2px;">
                                        <select class="chosen-select form-control" name="RELEASE_STATUS" id="RELEASE_STATUS"
                                                data-placeholder="请选择发布状态" style="vertical-align:top;width: 120px;">
                                            <option value=""></option>
                                            <option value="">全部</option>
                                            <option value="0"
                                                    <c:if test="${pd.RELEASE_STATUS == '0' }">selected</c:if> >未发布
                                            </option>
                                            <option value="1"
                                                    <c:if test="${pd.RELEASE_STATUS == '1' }">selected</c:if> >已发布
                                            </option>
                                        </select>
                                    </td>
                                    
                                     <td style="vertical-align:top;padding-left:2px;">
                                        <select class="chosen-select form-control" name="DOLL_TYPE" id="DOLL_TYPE"
                                                data-placeholder="请选择H5是否可见" style="vertical-align:top;width: 120px;">
                                            <option value=""></option>
                                            <option value="Y"
                                                    <c:if test="${pd.DOLL_TYPE == 'Y' }">selected</c:if> >H5可见
                                            </option>
                                            <option value="N"
                                                    <c:if test="${pd.DOLL_TYPE == 'N' }">selected</c:if> >其他可见
                                            </option>
                                        </select>
                                    </td>
                                        <td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs"
                                                                                           onclick="tosearch();"
                                                                                           title="检索"><i
                                                id="nav-search-icon"
                                                class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
                                        </td>
                                        <td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs"
                                                                                            onclick="toExcel();"
                                                                                            title="导出到EXCEL"><i
                                                id="nav-search-icon"
                                                class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a>
                                        </td>
                                </tr>
                            </table>
                            <!-- 检索  -->

                            <table id="simple-table" class="table table-striped table-bordered table-hover"
                                   style="margin-top:5px;">
                                <thead>
                                <tr>
                                    <th class="center" style="width:35px;">
                                        <label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox"/><span
                                                class="lbl"></span></label>
                                    </th>
                                    <th class="center" style="width:50px;">序号</th>
                                    <th class="center">娃娃机SN号</th>
                                    <th class="center">娃娃机名字</th>
                                    <th class="center">娃娃机房间号</th>
                                    <th class="center">娃娃金币</th>
                                    <th class="center">娃娃可兑换金币</th>
                                    <th class="center">奖励金币</th>
                                    <th class="center">设备编号</th>
                                    <th class="center">概率</th>
                                    <th class="center">网关状态</th>
                                    <th class="center">H5可见</th>
                                    <th class="center">发布状态</th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <!-- 开始循环 -->
                                <c:choose>
                                    <c:when test="${not empty varList}">
                                            <c:forEach items="${varList}" var="var" varStatus="vs">
                                                <tr>
                                                    <td class='center'>
                                                        <label class="pos-rel"><input type='checkbox' name='ids'
                                                                                      value="${var.DOLL_ID}"
                                                                                      class="ace"/><span
                                                                class="lbl"></span></label>
                                                    </td>
                                                    <td class='center' style="width: 30px;">${vs.index+1}</td>
                                                    <td class='center'>${var.DOLL_SN}</td>
                                                    <td class='center'>${var.DOLL_NAME}</td>
                                                    <td class='center'>${var.DOLL_ID}</td>
                                                    <td class="center">${var.DOLL_GOLD}</td>
                                                    <td class="center">${var.DOLL_CONVERSIONGOLD}</td>
                                                    <td class="center">${var.COINPUSHER_REWORD}</td>
                                                    <td class="center">${var.ROOM_ID}</td>
                                                    <td class="center">${var.ROOMPROBABILITY}</td>
                                                    <td class="center">${var.DOLL_STATE}</td>
                                                    <td class="center">${var.DOLL_TYPE}</td>
                                                    <td style="width: 100px;" class='center'>
                                                        <c:if test="${var.RELEASE_STATUS == '0' }"><span
                                                                class="label label-important arrowed-in">未发布</span></c:if>
                                                        <c:if test="${var.RELEASE_STATUS == '1' }"><span
                                                                class="label label-success arrowed">已发布</span></c:if>
                                                    </td>
                                                    <td class="center">
                                                        <div class="hidden-sm hidden-xs btn-group">
                                                                <a class="btn btn-xs btn-success" title="编辑"
                                                                   onclick="edit('${var.DOLL_ID}');">
                                                                    <i class="ace-icon fa fa-pencil-square-o bigger-120"
                                                                       title="编辑"></i>
                                                                </a>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr class="main_info">
                                            <td colspan="100" class="center">没有相关数据</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                            <div class="page-header position-relative">
                                <table style="width:100%;">
                                    <tr>
                                        <td style="vertical-align:top;">
                                                <a class="btn btn-mini btn-success" onclick="add();">新增</a>
                                                <a class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');"
                                                   title="批量删除"><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
                                            	<a class="btn btn-mini btn-success" onclick="">发布</a>
                                        </td>
                                        <td style="vertical-align:top;">
                                            <div class="pagination"
                                                 style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div>
                                        </td>
                                    </tr>
                                </table>
                            </div>
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

    <!-- 返回顶部 -->
    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>

</div>
<!-- /.main-container -->

<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp" %>
<!-- 删除时确认窗口 -->
<script src="static/ace/js/bootbox.js"></script>
<!-- ace scripts -->
<script src="static/ace/js/ace/ace.js"></script>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
    $(top.hangge());//关闭加载状态
    //检索
    function tosearch() {
        top.jzts();
        $("#Form").submit();
    }

    $(function () {

        //日期框
        $('.date-picker').datepicker({
            autoclose: true,
            todayHighlight: true
        });

        //下拉框
        if (!ace.vars['touch']) {
            $('.chosen-select').chosen({allow_single_deselect: true});
            $(window)
                .off('resize.chosen')
                .on('resize.chosen', function () {
                    $('.chosen-select').each(function () {
                        var $this = $(this);
                        $this.next().css({'width': $this.parent().width()});
                    });
                }).trigger('resize.chosen');
            $(document).on('settings.ace.chosen', function (e, event_name, event_val) {
                if (event_name != 'sidebar_collapsed') return;
                $('.chosen-select').each(function () {
                    var $this = $(this);
                    $this.next().css({'width': $this.parent().width()});
                });
            });
            $('#chosen-multiple-style .btn').on('click', function (e) {
                var target = $(this).find('input[type=radio]');
                var which = parseInt(target.val());
                if (which == 2) $('#form-field-select-4').addClass('tag-input-style');
                else $('#form-field-select-4').removeClass('tag-input-style');
            });
        }


        //复选框全选控制
        var active_class = 'active';
        $('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function () {
            var th_checked = this.checked;//checkbox inside "TH" table header
            $(this).closest('table').find('tbody > tr').each(function () {
                var row = this;
                if (th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
                else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
            });
        });
    });

    //新增
    function add() {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "新增";
        diag.URL = '<%=basePath%>doll/goAdd.do';
        diag.Width = 450;
        diag.Height = 355;
        diag.Modal = true;				//有无遮罩窗口
        diag.ShowMaxButton = true;	//最大化按钮
        diag.ShowMinButton = true;		//最小化按钮
        diag.CancelEvent = function () { //关闭事件
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                if ('${page.currentPage}' == '0') {
                    tosearch();
                } else {
                    tosearch();
                }
            }
            diag.close();
        };
        diag.show();
    }

    //删除
    function del(Id) {
        bootbox.confirm("确定要删除吗?", function (result) {
            if (result) {
                top.jzts();
                var url = "<%=basePath%>doll/delete.do?DOLL_ID=" + Id + "&tm=" + new Date().getTime();
                $.get(url, function (data) {
                    tosearch();
                });
            }
        });
    }

    //修改
    function edit(Id) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "编辑";
        diag.URL = '<%=basePath%>doll/goEdit.do?DOLL_ID=' + Id;
        diag.Width = 450;
        diag.Height = 355;
        diag.Modal = true;				//有无遮罩窗口
        diag.ShowMaxButton = true;	//最大化按钮
        diag.ShowMinButton = true;		//最小化按钮
        diag.CancelEvent = function () { //关闭事件
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                tosearch();
            }
            diag.close();
        };
        diag.show();
    }

    //批量操作
    function makeAll(msg) {
        bootbox.confirm(msg, function (result) {
            if (result) {
                var str = '';
                for (var i = 0; i < document.getElementsByName('ids').length; i++) {
                    if (document.getElementsByName('ids')[i].checked) {
                        if (str == '') str += document.getElementsByName('ids')[i].value;
                        else str += ',' + document.getElementsByName('ids')[i].value;
                    }
                }
                if (str == '') {
                    bootbox.dialog({
                        message: "<span class='bigger-110'>您没有选择任何内容!</span>",
                        buttons:
                            {"button": {"label": "确定", "className": "btn-sm btn-success"}}
                    });
                    $("#zcheckbox").tips({
                        side: 1,
                        msg: '点这里全选',
                        bg: '#AE81FF',
                        time: 8
                    });
                    return;
                }if (msg == '发布') {
                	top.jzts();
                	
				}else {
                    if (msg == '确定要删除选中的数据吗?') {
                        top.jzts();
                        $.ajax({
                            type: "POST",
                            url: '<%=basePath%>doll/deleteAll.do?tm=' + new Date().getTime(),
                            data: {DATA_IDS: str},
                            dataType: 'json',
                            //beforeSend: validateData,
                            cache: false,
                            success: function (data) {
                                $.each(data.list, function (i, list) {
                                    tosearch();
                                });
                            }
                        });
                    }
                }
            }
        });
    };

    //导出excel
    function toExcel() {
        window.location.href = '<%=basePath%>doll/excel.do';
    }
</script>


</body>
</html>