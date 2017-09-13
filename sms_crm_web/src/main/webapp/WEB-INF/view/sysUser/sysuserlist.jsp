<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset='utf-8'>
	<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>

	<link
			href="${ctx}/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
	<!-- Custom Fonts -->
	<script src="${ctx}/static/js/commons/common.js"></script>
	<link href="${ctx}/static/css/bootstrap-table.css" rel="stylesheet">
	<link href="${ctx}/static/css/jquery.datetimepicker.css"
		  rel="stylesheet">
	<script src="${ctx}/static/js/jquery.datetimepicker.js"></script>

	<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzd.css">
	<!--[if IE]>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/default/dzdie8.css"/>
	<script src="${ctx}/static/js/appml5shiv.js"></script>
	<script src="${ctx}/static/js/applectivizr.js"></script>
	<![endif]-->
</head>
<body>
<input id="input_superAdmin" type="hidden" value="${session_user.superAdmin}">
<input type="hidden" value="${ctx}" id="server_path"/>
<input type="hidden" id="menu_id" value="${menuId}"/>

<div>
	<div class="com-content" style="text-align:center">

		<div class="com-menu inp-menu">
			<div class="inp-box" id="addSmsUsers"></div>
			<div class="inp-box inpt-box-btn">
				<span class="inp-title">账号:</span> <input type="text" class="inp-box-inp" id="email" placeholder=""/>
			</div>
			<div class="inp-box inpt-box-btn">
				<input class="f-btn" type="button" value="新增" id="add_btn">
			</div>
			<div class="inp-box inpt-box-btn">
				<input class="f-btn" type="button" value="查询" id="search_btn">
			</div>
			<div id="data_div"></div>
		</div>

	</div>
</div>
<div class="row">
	<table id="tb_data"></table>
	<div class="minlid">
		<a id="firstpage" href="javaScript:void(0)">首页</a><a href="javaScript:void(0)" id="lastpage">尾页</a><input
			class="page-inp" id="pagenum" type="text"/><a href="javaScript:void(0)" id="turnpage">跳转</a>
	</div>
</div>

</div>

<div class="modal fade" id="del">
	<div class="modal-dialog awl">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
				<h4 class="modal-title" id="del_h4_title">确定删除</h4>
			</div>
			<p class="aw-newcontent">
				<span class="aw-new">确认是否删除账号？</span>
			</p>

			<div class="modal-footer">
				<input type="button" class="f-btn"  id="btn_del" value="确  定" />
				<input type="button" class="f-btn" data-dismiss="modal" value="取  消">

			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="je_data_div">
	<div class="modal-dialog " style="margin-top: 11%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
				<h4 class="modal-title" id="h4_title">新增账号</h4>
			</div>
			<div class="modal-body">
				<form id="cz_merge_form" method="post"
					  class="form-horizontal required-validate">
					<div class="awcont">
						<div class="inp-box ">
							<span>昵称： </span>
							<input class='inp-box-inp'  id="nickName" name="nickName"
								   type="text" placeholder=""/>
						</div>
						<div class="inp-box ">
							<span>账号： </span>

							<input class='inp-box-inp' name="email" id="email_input"
								   type="text" placeholder=""/>
						</div>
						<div class="inp-box ">
							<span>角色： </span>
							<select  name="role" id="role">
								<option value="">请选择</option>
								<c:forEach var="item" items="${roleList}" varStatus="status">
									<option key="${item.id}" value="${item.id}">${item.roleName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="inp-box ">
							<span>状态： </span>
							<select id="state"  name="state">
								<option value="">--请选择--</option>
                                 <option value="0">启用</option>
                                 <option value="1">停用</option>
							</select>
						</div>
						<div class="inp-box ">
							<span>电话： </span>
							<input class='inp-box-inp' onkeyup="value=value.replace(/[^-\d]/g,'')" id="phone"
								   name="phone"/>
						</div>
						<div class="inp-box " style="display: none;" id="superiorIdDiv">
							<span>上级经理： </span>
							<select id="superiorId" name="superiorId">
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<input type="button" class=" f-btn"
							   data-dismiss="modal" id="cancel_btn" value="取  消"/>
						<input type="button" class="f-btn " id="cz_allot_btn" value="确  定"/>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<!-- Custom Theme JavaScript -->
<script src="${ctx}/static/js/commons/jquery.bootstrap.js"></script>
<script src="${ctx}/static/bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table.js"></script>
<script src="${ctx}/static/js/commons/bootstrap-table-zh-CN.min.js"></script>
<script src="${ctx}/static/js/sysuser/sysuserlist.js"></script>
<script type="text/javascript" src="${ctx}/static/dzd/turnpage.js"></script>
</body>
</html>