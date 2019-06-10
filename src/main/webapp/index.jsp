<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
<head>
    <title>登陆注册</title>
</head>
<body>

<form action="login" method="post">
    <p>用户名：<input type="text" name="username"></p>
    <p>密码：<input type="password" name="password"></p>
    <p><input type="submit" value="登陆"></p>
    <p><a href="res">注册</a></p>
</form>
<p>${requestScope.msg}</p>

</body>
</html>
