<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.edu.gdufs.edin.demo.model.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List<Nword> nwordsList = (List<Nword>)request.getAttribute("nwordsList");
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
	<link href="css/style.css" rel="stylesheet" type="text/css" />
  </head>
 
  <body>
        <div id="headbar">
          <h1 style="background:url(img/logo.jpg) left no-repeat">新闻推荐</h1>
    </div>
        <div id="datebar">
            <span class="date"><a class="date" href="#">今天</a></span>
            <span class="date"><a class="date" href="#">三天前</a></span>
            <span class="date"><a class="date" href="#">六天前</a></span>
            <span class="date"><a class="date" href="#">九天前</a></span>
        </div>

        <div id="main_content">
        
        <%
        Iterator<Nword> i = nwordsList.iterator();
        int count = 1;
        while(i.hasNext()){
        Nword nw = i.next();
        List NewsList = nw.getNewsList();
         %>
            <div class="passage">
                <div class="relatednum">共<%=NewsList.size() %>条相关</div>
                <div class="title"><span class="keyword">TOP <%=count++ %>：<%=nw.getWord() %>></span><span class="new">new</span></div>
                <div class="content">
                    <ul>
                    	<% 
                    	int c=0;
                    	for(Iterator i2 = NewsList.iterator();i2.hasNext()&&c<5;c++){
                    	News n = (News)i2.next();
                    	 %>
                        <li><a href="<%=basePath %>Content?nwordsid=<%=nw.getId() %>&newsid=<%=n.getId()%>"><%=n.getTitle() %></a></li>
                        <%
                        
                        } 
                        if(NewsList.size()>5){
                        %>
                        <li><a href="#">点击获取更多</a></li>
                        <%} %>
                    </ul>
                </div>
                <div class="passagebottomright"></div>
                <div class="passagebottomleft"></div>
                <div class="clear"></div>
            </div>
        <% 
        }
        %>
        </div>
        <div id="more" class="center"> <a href="#more" class="clickmore">点击显示更多</a> </div>
        <div id="bottombar"> 版权所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 联系我们：</div>
    </body>
</html>
