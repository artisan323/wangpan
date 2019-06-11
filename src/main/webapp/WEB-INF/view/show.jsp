<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>显示列表</title>
</head>
<body>

    <form action="/file" method="post" enctype="multipart/form-data">
        选择一个文件:
        <input type="file" name="uploadFile"/>
        <br/><br/>
        <input type="submit" value="上传"/>
    </form>

</body>
</html>
