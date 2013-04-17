<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.edu.gdufs.edin.demo.model.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="css/style1.css" rel="stylesheet" type="text/css" />
  </head>
  <%
  Nword nword = (Nword)request.getAttribute("nword");
  News news = (News)request.getAttribute("news");
  String timeString = (String)request.getAttribute("timeString");
  
   %>
 
  <body>
          <div id="headbar">
          <h1 style="background:url(img/logo.jpg) left no-repeat">新闻推荐</h1>
    </div>
  
         <div id="pathbar"> 
          <h3><div><a href="<%=basePath %>Index4ajax">首页</a>&gt;<%=timeString %>
          &gt;<%=nword.getWord() %></div></h3>
        </div>

        <div id="main_content">
        
            <div class="details">
                <div id="title">标题:<%=news.getTitle() %></div>
                <hr/>
                <div id="subtitle">发布时间:<%=news.getDate() %>&#9;&#9;来源:<%=news.getFrom() %><br/><a href="<%=news.getSource() %>"><%=news.getSource() %></a></div>
            </div>
            
            <div class="passage">
               <%=news.getContent() %>
            </div>
        </div>
        <div id="more" > 
            <span>“<%=nword.getWord() %>”的其他新闻链接</span>
            <ul>
            <%
            Iterator i = nword.getNewsList().iterator();
            while(i.hasNext()){
            News n = (News)i.next();
             %>
                <li><a href="<%=basePath %>Content?nwordsid=<%=nword.getId() %>&newsid=<%=n.getId() %>"><%=n.getTitle() %></a></li>
            <%} %>
            </ul>
    </div>
        
<!--         <div id="news">          
          TOP热词搜索列表
          <ul>
                <li><a href="#">新词1</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">首条新闻</a></li>
                <li><a href="#">新词2</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="#">首条新闻</a></li>
                <li><a href="#">新词3</a>   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="#">首条新闻</a> </li>
          </ul>
        </div> -->
        
    <div id="bottombar"> 版权所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 联系我们：</div>
        
  
  </body>
</html>
