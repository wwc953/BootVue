<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>开始使用</title>
    <script src="/layui/layui/layui.js"></script>
    <link rel="stylesheet" href="/layui/layui/css/layui.css">

</head>
<body>

<script>
    layui.use(['layer', 'form'], function(){
        var layer = layui.layer
            ,form = layui.form;

        layer.msg('---111');
    });

</script>

<h4>${user.age}</h4>

</body>
</html>
