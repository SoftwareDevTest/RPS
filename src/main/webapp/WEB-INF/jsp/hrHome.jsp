<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>HR</title>
    <link rel="stylesheet" href="/pintuer.css">
    <link rel="stylesheet" href="/admin.css">
    <script src="/jquery.js"></script>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main">
    <div class="logo margin-big-left fadein-top" style="float:left">
        <h1><img src="/HR.png" class="radius-circle rotate-hover" height="50" alt="" />HR</h1>
    </div>
    <div class="head-l" style="float: right">
        <a class="button button-little bg-red" href="/login/selectLogin?btn=HR入口">
            <span class="icon-power-off"></span> 退出登录</a>
    </div>
</div>
<div class="leftnav">
    <div class="leftnav-title"><strong><span class="icon-list"></span>功能列表</strong></div>
    <h2><span class="icon-user"></span>发布招聘</h2>
    <ul style="display:block">
        <li><a href="fabu1.html" target="right"><span class="icon-caret-right"></span>普通招聘</a></li>
        <li><a href="fabu2.html" target="right"><span class="icon-caret-right"></span>紧急招聘</a></li>
    </ul>

    <h2><span class="icon-user"></span>招聘处理</h2>
    <ul style="display:block">
        <li><a href="chuli1.html" target="right"><span class="icon-caret-right"></span>待处理</a></li>
        <li><a href="chuli2.html" target="right"><span class="icon-caret-right"></span>已处理</a></li>
    </ul>

</div>
<script type="text/javascript">
    $(function() {
        $(".leftnav h2").click(function() {
            $(this).next().slideToggle(200);
            $(this).toggleClass("on");
        })
        $(".leftnav ul li a").click(function() {
            $("#a_leader_txt").text($(this).text());
            $(".leftnav ul li a").removeClass("on");
            $(this).addClass("on");
        })
    });
</script>
<ul class="bread">

    <li><a href="/hr/homeDetail" target="right" class="icon-home"> 已发布招聘信息</a></li>
    <li><a href="##" id="a_leader_txt">网站信息</a></li>
    <li><b>当前语言：</b><span style="color:darkred;">中文
                </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切换语言:&nbsp;&nbsp;
        <a href="##">中文</a> &nbsp;&nbsp;<a href="##">英文</a> </li>
</ul>
<div class="admin">
    <iframe scrolling="auto" rameborder="0" src="/hr/homeDetail" name="right" width="100%" height="100%"></iframe>
</div>
</body>

</html>
