var path = $("#server_path").val();
$(function () {
    load_data();  //加载列表数据
    $("#search_btn").bind("click", function () {
        refresh_data();
    });
    $("#add_btn").bind("click", function () {
        addSysUser();
    });

    $('#role').change(function () {
        var role = $("#role").val();
        if(role==2 || role==""){
            $("#cDiv").hide();
        }else{
            $("#superiorIdDiv").show();
            querySuperiorManager();
        }
    })
    //确认删除
    $("#btn_del").on('click', function () {
        $.ajax({
            url: path + "/management/apply/delete.do",
            type: "POST",
            dataType: "json",
            data: {"ids": delIds, "sysUserIds": sysUserIds},
            success: function (data) {
                if (checkRes(data)) {
                    $("#del").modal("hide");
                    refresh_data();
                }
            },
            error: function () {
            },
            complete: function () {
            }
        });
    });
});
//查询经理
function querySuperiorManager() {
    $("#superiorId").empty();
    ajaxCall({
        url: "/sysUser/querySuperiorManager.do",
        type: "post",
        success: function (data) {
            if (checkRes(data)) {
                var obj = data["data"]
                var bid = $("#superiorId");
                var htl = "<option value=\"\">请选择</option>";
                for (var i = 0; i < obj.length; i++) {
                    var ss = obj[i];
                 /*   if ( != undefined && sysUserId != null && ss["sysUserId"] == sysUserId) {
                        htl += "<option selected=\"selected\"  value=\"" + ss["sysUserId"] + "\">" + ss["email"] + "</option>";
                    } else {*/
                        htl += "<option  value=\"" + ss["id"] + "\">" + ss["nickName"] + "</option>";
                   /* }*/
                }
                bid.html(htl);
            }
        },
        error: function () {
            alert("操作失败");
        }
    });

}
//新增
function addSysUser() {
    $("#je_data_div").modal("show");
}

//查询 按钮
function search() {
    refresh_data();
    if ($("div.pagination").is(":visible")) {
        $(".minlid").show();
    } else {
        $(".minlid").hide();
    }
    hideColumn();
};

//设置账号按钮(修改)
$("#szBtn").on('click', function () {
    btnTypeEvent("update");
});



//刷新数据
function refresh_data() {
    $('#tb_data').bootstrapTable('destroy');
    load_data();
}

function queryParamsVal(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pagesize: params.limit,   //页面大小
        pagenum: (params.offset / params.limit) + 1,  //页码
        sysUserName: $("#email").val() //账号信息
    };
    return temp;
}
//列表加载
function load_data() {
    $('#tb_data').bootstrapTable({
        url: 'userlist.do?menuId=' + $("#menu_id").val(),
        dataType: "json",
        cache: false,
        striped: true,
        pageSize: 50,
        pageList: [50],
        pagination: true,
        paginationPreText: "上一页",
        paginationNextText: "下一页",
        paginationHAlign: "left",
        clickToSelect: true,
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            if (totalRows > 50) {
                $(".minlid").show();
            } else {
                $(".minlid").hide();

            }
            return '总共 ' + totalRows + ' 条记录';
        },
        sidePagination: "server", //服务端处理分页
        method: "post",
        queryParams: queryParamsVal, //参数
        columns: [
            {
                field: "选择",
                checkbox: true,
                align: "center",
                valign: "middle",
                width: 30
            },
            {
                title: "账号",
                field: "email",
                align: "center",
                valign: "middle",
                width: 100
            },
            {
                title: "状态",
                field: "state",
                align: "center",
                valign: "middle",
                formatter: function (value, row) {
                    if (row.state == 1) {
                        return "停用";
                    } else  {
                        var close = "<span style='color:red'>启用</span>";
                        return close;
                    }
                }
            },
            {
                title: "昵称",
                field: "nickName",
                align: "center",
                valign: "middle",
                width: 70
            },
            {
                title: "电话",
                field: "telephone",
                align: "center",
                width: 120,
                valign: "middle",
                width: 100
            },{
                title: "角色",
                field: "roleName",
                align: "left",
                valign: "middle"
            },
            {
                title: "最后登录时间",
                field: "lastLoginTime",
                align: "center",
                valign: "middle",
                width: 80,
                formatter: function (value, row) {
                    if (!row.lastLoginTime) {
                        return "";
                    }
                    return new Date(row.lastLoginTime).Format("yyyy-MM-dd");
                }
            },
            {
                title: "创建时间",
                field: "createTime",
                align: "center",
                valign: "middle",
                width: 80,
                formatter: function (value, row) {
                    if (!row.createTime) {
                        return "";
                    }
                    return new Date(row.createTime).Format("yyyy-MM-dd");
                }
            },
            {
                title: '操作',
                field: 'operate',
                align: 'center',
                valign: "middle",
                width: 110,
                formatter: function (value, row) {
                    var html="";
                    html+= '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + row.id + '\');"><i class="fa fa-paste"></i>编辑</a> ';
                    if(row.state==1){
                        html+= '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + row.id + '\');"><i class="fa fa-paste"></i>重置密码</a> ';
                    }
                    html+= '<a class="btn btn-sm btn-white" href="javaScript:void(0);" onclick="editData(\'' + row.id + '\');"><i class="fa fa-paste"></i>删除</a> ';
                    return html;
                }
            }
        ],
        formatNoMatches: function () {
            return '无符合条件的记录';
        }
    });
    if ($("div.pagination").is(":visible")) {
        $(".minlid").show();

    } else {
        $(".minlid").hide();
    }
}

//另建（复制用户信息 然后保存）
function btnTypeEvent(type) {
    var value = type.value;
    var temp;
    if (value == "设置") {
        temp = "update";
    }
    var a = $('#tb_data').bootstrapTable('getSelections');
    var id = 0;
    var roleId;
    if (a.length == 1) {
        id = a[0].id;
        roleId = a[0].roleId;
    } else {
        alert("请选中一行")
        return;
    }
    $(".fileinput-remove-button").trigger("click");
    window.location.href = path + "/management/operate.do?menuId=" + $("#menu_id").val() + "&id=" + id + "&btnType=" + temp + "&roleId=" + roleId;
}

//删除
function del() {
    var delIds = new Array();
    var a = $('#tb_data').bootstrapTable('getSelections');
    if (a.length > 0) {
        for (var i = 0; i < a.length; i++) {
            delIds.push(a[i].id);
        }
        $("#del").modal("show");
    } else {
        alert("请选择要删除的数据");
        return;
    }
}