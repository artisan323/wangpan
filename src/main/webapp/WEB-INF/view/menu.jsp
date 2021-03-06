<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%><html>
<head>
    <title>个人文档</title>
    <meta charset="UTF-8">

    <%
        application.setAttribute("APP_PATH", request.getContextPath());
    %>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="http://libs.baidu.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

    <script type="text/javascript">
        //根据选择的文件类型进行显示文件
        function searchFile(f) {
            $.ajax({
                url : "${APP_PATH}/search",
                data : "f="+f,  //根据f的数值选择查询的文件格式
                type : "GET",
                success : function (result) {
                    //显示表格信息
                    build_emps_table(result);
                    if (result.code == "400"){
                        alert("没有此类型文件")
                    }
                }
            });
        }

        //显示表格
        function build_emps_table(result) {
            //每次加载页面都要先清空原理来数据
            $("#emp_table tbody").empty();
            //获取所有文件数据
            var files = result.extend.list;
            $.each(files, function (index, item) {

                var checkbox = $("<td><input type='checkbox' class='check_item'></td>")
                var fileid = $("<td></td>").append(item.fileId);
                var filename = $("<td></td>").append(item.fileName);
                var downicon = $("<button></button>").addClass("btn btn-primary btn-sm downfile")
                    .append($("<span></span>").addClass("glyphicon glyphicon-download-alt"));
                //var rename = $("<button></button>").addClass("btn btn-primary btn-sm renamefile").append("重命名");

                //为下载按钮添加自定义属性保存文件id
                downicon.attr("fileid", item.fileId);

                //为重命名按钮添加自定义属性保存文件id
                //rename.attr("renameid", item.fileId);


                var filesize = $("<td></td>").append(item.fileSize);

                $("<tr></tr>")
                    .append(checkbox)
                    .append(fileid)
                    .append(filename)
                    .append(filesize)
                    .append(downicon)
                    //.append(rename)
                    .appendTo("#emp_table tbody");

            });
        }

        //点击下载按钮下载文件添加绑定事件
        $(document).on("click", ".downfile", function () {
            //获取id值
            getfileId($(this).attr("fileid"))
        })
        function getfileId(id) {
            location.href="${APP_PATH}/down?fileId=" + id;
            // $.ajax({
            //     url:"/down",
            //     data:"fileId="+id,
            //     type:"GET",
            //     success:function (result) {
            //         if (result)
            //     }
            // })
        }

        //点击批量下载，下载多个文件
        function downcheck() {

            var downids = "";
            $.each($(".check_item:checked"), function () {
               downids += $(this).parents("tr").find("td").eq(1).text()+"-";
            });
            //去除多余-
            downids = downids.substring(0, downids.length-1);
            //发送请求
            location.href="${APP_PATH}/downs?downids="+downids;
        }

        //删除文件
        function deleteFile() {
            var delids = "";
            $.each($(".check_item:checked"), function () {
                delids += $(this).parents("tr").find("td").eq(1).text()+"-";
            });
            //去除多余-
            delids = delids.substring(0, delids.length-1);

            alert("删除文件");
            alert(delids);

            //发送请求
            //location.href="/del?delids="+delids;

            $.ajax({
                url:"${APP_PATH}/del",
                data:"delids="+delids,
                type:"GET",
                success:function (result) {
                    alert(result.extend.success);
                    //window.location.reload();
                    searchFile(6);
                }
            });
        }
        
        //重命名
        function rename() {
            var rename = $("#rename").val();
            alert(rename);
            var renameId = "";
            $.each($(".check_item:checked"), function () {
                renameId = $(this).parents("tr").find("td").eq(1).text();
            });
            alert(renameId);
            if (rename == null || renameId == null){
                alert("没有输入新名称或没有选中需要修改的文件");
            } else {
                $.ajax({
                    url:"${APP_PATH}/rename",
                    data:"renameId="+renameId + "&fileNewName="+rename,
                    type:"GET",
                    success:function (result) {
                        alert("返回成功");
                        searchFile(6)
                    }
                });
            }

        }

        //按照文件名称查找文件
        function selFile() {
            var selName = $("#selName").val();
            alert(selName);
            if (selName == null){
                alert("请输入需要查询的文件名称");
            } else {
                $.ajax({
                    url:"${APP_PATH}/selfile",
                    data:"selName="+selName,
                    type:"GET",
                    success:function (result) {
                        if (result.code == 400){
                            alert("查询结果为空");
                        }
                        build_emps_table(result);
                    }
                });
            }
        }

        //文件分享
        function share() {
            var shareId = "";
            $.each($(".check_item:checked"), function () {
                shareId = $(this).parents("tr").find("td").eq(1).text();
            });
            alert(shareId);
            $.ajax({
               url:"${APP_PATH}/share",
                data:"shareId="+shareId,
                type:"GET",
                success:function (result) {
                    alert("返回成功");
                    alert(result.extend.share.shareUrl);
                }
            });
        }


        //显示分享列表
        function shareList() {
            //每次加载页面都要先清空原理来数据
            $("#share_table tbody").empty();
            //发送ajax请求，获取数据
            $.ajax({
               url:"${APP_PATH}/sharelist",
               success:function (result) {
                   var share = result.extend.list;
                   $.each(share, function (index, item) {

                       var shareId = $("<td></td>").append(item.shareId)
                       var shareUrl = $("<td></td>").append(item.shareUrl);
                       var shareName = $("<td></td>").append(item.shareFileName);


                       $("<tr></tr>")
                           .append(shareId)
                           .append(shareName)
                           .append(shareUrl)
                           .appendTo("#share_table tbody");

                   });
               } 
            });
        }

    </script>
</head>
<body>
    <!-- 导航栏 -->
    <div class="navbar navbar-default">
        <div class="container">
            <div class="navbar-header">
                <div class="navbar-brand">个人文档</div>
            </div>

<%--            <ul class="nav navbar-nav">--%>
<%--                <li><a href`="#">网盘</a></li>--%>
<%--            </ul>--%>

            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">${sessionScope.user.username}</a> </li>
                <li><a href="/quit">退出</a></li>
            </ul>
        </div>
    </div>


    <!-- 主体栏 -->
    <div class="container">
        <div class="row">

            <%--左侧栏--%>
            <div class="col-sm-3">
                <div class="list-group">
                    <a onclick="searchFile(6)" class="list-group-item">所有文件</a>
                    <a onclick="searchFile(0)" class="list-group-item">图片</a>
                    <a onclick="searchFile(1)" class="list-group-item">文档</a>
                    <a onclick="searchFile(2)" class="list-group-item">视频</a>
                    <a onclick="searchFile(3)" class="list-group-item">音乐</a>
                    <a onclick="searchFile(4)" class="list-group-item">文本</a>
                    <a onclick="searchFile(5)" class="list-group-item">其他</a>
                    <a onclick="shareList()" class="list-group-item" data-toggle="modal" data-target="#exampleModal" data-whatever="@getbootstrap">我的分享</a>
                    <a href="#" class="list-group-item">回收站</a>
                    <a onclick="selFile()" class="list-group-item">文件查询</a>
                    <input type="text" id="selName" name="selName" value="输入查询名称" class="list-group-item">
                </div>
            </div>
            <%--我的分享模态框--%>
            <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="exampleModalLabel">我的分享</h4>
                            </div>
                            <div class="modal-body">
                                <table class="table table-hover" id="share_table">
                                    <thead>
                                        <tr>
                                            <th>Id</th>
                                            <th>分享文件名</th>
                                            <th>链接</th>

                                        </tr>
                                    </thead>

                                    <tbody>

                                    </tbody>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

            <%--右侧栏--%>
            <div class="col-sm-9">

                <%--顶部导航栏--%>
                <div class="navbar navbar-default">
                    <div class="container">

                        <%--上传文件--%>
                        <div class="nav navbar-nav">
                            <%--<form action="/file" method="post" enctype="multipart/form-data">--%>
                                <%--<input type="file" name="uploadFile" class="btn btn-default"/>--%>
                                <%--<input type="submit" value="上传" class="btn"/>--%>
                            <%--</form>--%>

                            <button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#upload_modall" data-whatever="@getbootstrap">
                                <span class="glyphicon glyphicon-cloud-upload"></span>
                                上传文件
                            </button>
                            <%--上传文件模态框--%>
                            <div class="modal fade" id="upload_modall" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title" id="exampleModalLabel">上传文件</h4>
                                        </div>
                                        <div class="modal-body">
                                            <form action="${APP_PATH}/file" method="post" enctype="multipart/form-data">
                                                <div class="form-group">
                                                    <label for="message-text" class="control-label">上传</label>
                                                    <input type="file" name="uploadFile" class="btn btn-default">
                                                </div>

                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                                    <button type="submit" class="btn btn-primary" >提交</button>
                                                </div>
                                            </form>
                                        </div>

                                    </div>
                                </div>
                            </div>

                        </div>


                        <%--新建文件夹--%>
                        <div class="nav navbar-nav">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#new_dir_modall" data-whatever="@getbootstrap">
                                <span class="glyphicon glyphicon-folder-open"></span>
                                新建文件夹
                            </button>
                        </div>

                        <%--删除文件--%>
                        <div class="nav navbar-nav">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <button id="del_check" class="btn btn-warning btn-sm" onclick="deleteFile()">
                                <span class="glyphicon glyphicon-trash"></span>
                                删除文件
                            </button>
                        </div>


                        <%--文件重命名--%>
                        <div class="nav navbar-nav">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <button  class="btn btn-primary btn-sm" onclick="rename()">
                                文件重命名
                            </button>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="text" name="rename" value="请输入新文件名称" id="rename">
                        </div>

                        <%--文件分享--%>
                        <div class="nav navbar-nav">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <button  class="btn btn-primary btn-sm" onclick="share()">
                                分享
                            </button>

                        </div>

                        <%--新建文件夹模态框--%>
                        <div class="modal fade" id="new_dir_modall" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    <h4 class="modal-title" id="exampleModalLabel">新建文件夹</h4>
                                </div>
                                <div class="modal-body">
                                    <form action="${APP_PATH}/mkdir" method="post">
                                        <div class="form-group">
                                            <label for="message-text" class="control-label">文件夹名称</label>
                                            <input class="form-control" id="message-text" name="dirname">
                                        </div>

                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                            <button type="submit" class="btn btn-primary" >确定</button>
                                        </div>
                                    </form>
                                </div>

                            </div>
                        </div>
                    </div>

                    </div>
                </div>

                <%--显示文件列表--%>
                <table class="table table-hover" id="emp_table">
                    <thead>
                        <tr>
                            <th>选择</th>
                            <th>文件id</th>
                            <th>文件名</th>
                            <th>文件大小</th>
                            <th>下载</th>
                            <th><button id="down_check" class="btn btn-primary btn-sm" onclick="downcheck()">
                                批量下载
                            </button></th>

                        </tr>
                    </thead>

                    <tbody>

                    </tbody>
                </table>

            </div>
        </div>

    </div>
</body>
</html>
