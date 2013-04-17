<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Long toDay = (Long)request.getAttribute("toDay");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>基于新词推荐的新闻阅读系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="css/style.css" rel="stylesheet" type="text/css" />
    <script src="js/jquery-1.6.4.min.js" language="javascript" ></script>
	<script src="js/index4ajax.js" language="javascript" ></script>
  </head>
 
  <body>
        <div id="headbar">
          <h1 style="background:url(img/logo.jpg) left no-repeat">新闻推荐</h1>
    </div>
        <div id="datebar">
            <span ><a onClick="getNwords(<%=(toDay-3*86400000)%>,<%=(toDay)%>)" value="0" href="javascript:void(0)">今天</a></span>
            <span ><a onClick="getNwords(<%=(toDay-4*86400000)%>,<%=(toDay-1*86400000)%>)" value="1" href="javascript:void(0)">一天前</a></span>
            <span ><a onClick="getNwords(<%=(toDay-5*86400000)%>,<%=(toDay-2*86400000)%>)" value="2" href="javascript:void(0)">二天前</a></span>
            <span ><a onClick="getNwords(<%=(toDay-6*86400000)%>,<%=(toDay-3*86400000)%>)" value="3" href="javascript:void(0)">三天前</a></span>
            <span ><a onClick="getNwords(<%=(toDay-7*86400000)%>,<%=(toDay-4*86400000)%>)" value="4" href="javascript:void(0)">四天前</a></span>
            <span ><a onClick="getNwords(<%=(toDay-8*86400000)%>,<%=(toDay-5*86400000)%>)" value="5" href="javascript:void(0)">五天前</a></span>
            <span ><a onClick="getNwords(<%=(toDay-9*86400000)%>,<%=(toDay-6*86400000)%>)" value="6" href="javascript:void(0)">六天前</a></span>
        </div>
        <div id="main_content">
			<div id="test" class="nwords">
	        	<div class="relatednum">共1条相关</div>
	            <div class="title"><span class="keyword">TOP 1：地沟油</span></div>
	        	<div class="content">
	           	<ul>
	            	<li>
	            		<a href="">滴滴滴滴滴滴滴</a>
	                    (2013年4月14日)
	            	</li>
	            	<li><a href="#">点击获取更多</a></li>
	           	</ul>
	            </div>
	            <div class="passagebottomright">1</div>
	            <div class="passagebottomleft">2</div>
	            <div class="clear"></div>
	    	</div>
	    <div id="morepassage" class="passage">
	             <front>无相关信息！</front>  
	    </div>
<div id="more" class="center"> <a onClick="moreNwords()" href="javascript:void(0)" class="clickmore">点击显示更多</a> 
</div>
        </div>
        
        <div id="bottombar"> 版权所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 联系我们：</div>
        <input id="basedate" type="hidden" value="<%=toDay%>">
    </body>
</html>
