<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%
        application.setAttribute("APP_PATH", request.getContextPath());
    %>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
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
                var filename = $("<td></td>").append(item.fileName);
                var downicon = $("<button></button>").addClass("btn btn-primary btn-sm").append($("<a></a>").attr("href", "/down?fileId="+item.fileId))
                    .append($("<span></span>").addClass("glyphicon glyphicon-download-alt"));
                var filesize = $("<td></td>").append(item.fileSize);


                $("<tr></tr>").append(filename).append(filesize).append(downicon).appendTo("#emp_table tbody");
            });
        }



        //点击下载按钮下载文件


    </script>
</head>
<body>
    <!-- 导航栏 -->
    <div class="navbar navbar-default">
        <div class="container">
            <div class="navbar-header">
                <div class="navbar-brand">拴蛋网盘</div>
            </div>

            <ul class="nav navbar-nav">
                <li><a href="#">首页</a></li>
            </ul>

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
                    <a href="#" class="list-group-item">图片</a>
                    <a onclick="searchFile(1)" class="list-group-item">文档</a>
                    <a href="#" class="list-group-item">视频</a>
                    <a href="#" class="list-group-item">音乐</a>
                    <a href="#" class="list-group-item">文本</a>
                    <a href="#" class="list-group-item">其他</a>
                    <a href="#" class="list-group-item">我的分享</a>
                    <a href="#" class="list-group-item">回收站</a>
                </div>
            </div>

            <%--右侧栏--%>
            <div class="col-sm-9">

                <%--上传按钮--%>
                <div class="navbar navbar-default">
                    <div class="container">
                        <form action="/file" method="post" enctype="multipart/form-data">
                            <input type="file" name="uploadFile" class="btn btn-default"/>
                            <input type="submit" value="上传" class="btn"/>
                        </form>
                    </div>
                </div>

                <%--显示文件列表--%>
                <table class="table table-hover" id="emp_table">
                    <thead>
                        <tr>
                            <th>文件名</th>
                            <th>文件大小</th>
                            <th>文件下载</th>
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
