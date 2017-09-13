<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">

	<head>

		<title>全网信通</title>

		<link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/admin.css">
		<!--[if IE]>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/dzd/themes/dzdie8.css"/>
    <script src="${ctx}/static/js/app/html5shiv.js"></script>
    <script src="${ctx}/static/js/app/selectivizr.js"></script>
    <style>
        

        #captchaImage {
            position: relative;
            top: 2px;
        }
    </style>
    <![endif]-->
    <style type="text/css">
    	input[type="text"]:focus {
    border: 1px solid #4284DA;
    height: 20px;
    width: 161px;
    }
    .inp-inp {
   border: 1px solid #d4d4d4;
    height: 20px;
    width: 161px;
	}
	.dxtype{
		display:none;
	}
    </style>
	</head>

	<body style="background-color: #FFFFFF;font-family:'微软雅黑，宋体'">
		<input type="hidden" value="${ctx}" id="server_path" />
		<input type="hidden" value="${ctx}" id="path" />
		<div class="pageheader">
			<div class="pageh-left">
				<a href="logout.do"><div class="logo"></div></a>
				<div class="logotext">全网信通——即时通信平台：验证码短信，营销群发短信，短信通知，短信接口</div>
			</div>
			<div class="pageh-right">
				<ul class="pageh-menu">
					<li>
						<a href="logout.do">返回首页</a>
					</li>
					<li class="pagechange">
						<a href="#forgetpsw" id="forgetpsw" data-tar="forgetpsw">找回密码</a>
					</li>
				</ul>
			</div>
		</div>
		<p style="border-width: 1px; border-color: #d4d4d4;hieght:1px;width:100%;border-top:1px solid #d4d4d4;margin-top: 5px" ></p>
		<!-- 找回密码 -->
		<div class="dxtype forgetpsw">
			<div class="dxtitle">找回密码</div>
			<div class="tzcontent">
				<form id="zxForm">
					<table class="helpbox">
						<tr>
							<td style="text-align: right;"><span class="cfd">*</span>账号名称：</td>
							<td style="text-align: left;">
								<input style="text-align: left;" class="inp-inp pimp" type="text" name="emailc" id="emailc" readonly onfocus="this.removeAttribute('readonly');" placeholder="" />
							</td>
						</tr>
						<tr>
							<td style="text-align: right;"><span class="cfd">*</span>手机号码：</td>
							<td style="text-align: left;">
								<input maxlength="11" style="text-align: left;" class="inp-inp pimp" type="text" name="phonec" id="phonec" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="" />
							</td>
						</tr>
						<tr>
							<td style="text-align: right;"><span class="cfd">*</span>图文验证码：</td>
							<td style="text-align: left;">
								<input maxlength="5" class="inp-inp pimp" type="text" name="imgCode" id="imgCodec" readonly onfocus="this.removeAttribute('readonly');" placeholder="" />
								<img id="captchaImage2" src="captcha.do" style="vertical-align:top;height:22px;" title="点击图片刷新验证码">
							</td>
						</tr>
						<tr>
							<td style="text-align: right;"><span class="cfd">*</span>手机验证码：</td>
							<td style="text-align: left;">
								<input maxlength="8" class="inp-inp pimp reg-code" type="text" name="verifyCodec" id="verifyCodec" readonly onfocus="this.removeAttribute('readonly');" placeholder="" />
								<input id="pwd-getcodec" class="f-btn" type="button" value="获取验证码" />
							</td>
						</tr>
					</table>
					<p style="padding-top: 10px;padding-bottom: 20px;">

					<input type="button" style="margin-right: 20px;"   class="f-btn"  id="updatePwdEnter" value="确  定"/>
						<input type="reset"   class="f-btn"  value="重  置"/>
						
					</p>
					<div >
						<ul style="padding:0;text-align:left;width:320px">
							<li>提示：</li>
							<li>1、须准确输入账户名，勿含有空格或其他文字、符号；</li>
							<li>2、手机号码须为账户绑定号码，否则无法获取验证码短信；</li>
							<li>3、正确输入验证码，点击“确定”按钮后将会收到系统随机重置的账户密码短信，请登录账户及时修改密码。</li>
							
						</ul>						
					</div>
				</form>
			</div>
		</div>

		<script src="${ctx}/static/js/commons/jquery-2.1.1.js"></script>
		<script type="text/javascript" src="${ctx}/static/dzd/dzdcom.js"></script>

		<script type="text/javascript">
			$('#captchaImage2').click(function() {
				$('#captchaImage2').attr("src", "captcha.do?timestamp=" + (new Date()).valueOf());
			});
		</script>

		<script src="${ctx}/static/js/consult/consult.js"></script>


		<script type="text/javascript">
			$('#captchaImage2,#captchaImage').click(function() {
				$(this).attr("src", "captcha.do?timestamp=" + (new Date()).valueOf());
			});
			
		</script>
	</body>

</html>