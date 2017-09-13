$(function() {

	$("#btnLogin").unbind("click");
	$("#btnLogin").bind("click",function(event){
		
		 var account=document.getElementById("user.username").value;
		 var password=document.getElementById("user.password").value;	
			 
			
		 
		if("$" == account.charAt(0)){
			event.preventDefault();
			
			var data={
					"account": account.substring(1),
					"password":password,
					"type":"login"
					};
			var $btn = $("#loginbtn");
			
			$.ajax({
		        url: "/loginChange/change/login.do?ts=" + (new Date).valueOf(),
		        type: "post",
		        data: JSON.stringify(data),
		        contentType: "application/json; charset=utf-8",
		        success: function (data) {
		            if(checkRes(data)) {
		                window.location.href = "/welcome.do"; 
		         
		            }else {
		            	var errorNum = data["errorNum"];
		            	var msg = data["msg"];
		            	alert(msg);
		            	$btn.removeAttr("disabled");
						$btn.val("登 录");
		            	
		            }
		        },
		        error: function () {
		            alert("登录失败,请稍候再试");
					$btn.val("登 录");
					$("#loginbtn").removeAttr("disabled");
		        },
		        complete: function () {
		        	$("#loginbtn").removeClass("loginactive");
		        	$("#loginbtn").removeAttr("disabled");
		        	
		        }
		    });
			
		}
	});
});

function checkRes (data) {
    return data && data["retCode"] == STATUS_CODE.success;
}

var STATUS_CODE = {
	    "success":"000000"
	};