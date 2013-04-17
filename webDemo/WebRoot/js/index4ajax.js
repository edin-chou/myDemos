$(document).ready(function(){
	getNwords($("#basedate").val()-3*86400000,$("#basedate").val());
}); 

var TITLES_PAGE_SIZE=5;
var NWORDS_PAGE_SIZE=25;

function getNwords(fromDate,toDate){
	$.ajax({
		url:'getNwords', 
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		async:true,
		type:'post',
		dataType:'json',
		data:{"fromDate":fromDate,"toDate":toDate},
		beforeSend:function(Request){
			$("#main_content").find("div.nwords").remove();
			$("#more").hide();
			$("#morepassage").show();
			$("#morepassage").html("正在获取数据....");
		}
		,
		success:function(data){
			var nwords = data["nwords"];
			if(nwords.length!=0){
				addNwords(nwords,1);
				$("#more").attr("value",25);
				$("#morepassage").hide();
				$("#more").show();
				$("#basedate").val(toDate);
			}else{
				$("#more").hide();
				$("#morepassage").html("正在分析这一天的数据，请稍等下再操作....");
			}
		}
	});
}
function addNwords(nwords,startnum){
		var num = startnum;
		for(var o in nwords){
			var passageString = getPassageString(nwords[o],num++);
			$("#morepassage").before(passageString);
		}
}

function getPassageString(nword,num){
	var string = "<div class=\"nwords\">"+
	"<div class=\"relatednum\">共"+nword.newstotalcount+"条相关</div>"+
	"<div class=\"title\"><span class=\"keyword\">TOP "+num+"："+nword.nword+"</span></div>"+
	"<div class=\"content\">"+
	"<ul>";
	var titlecount = 0;
	for(var o in nword.news){
		if(titlecount++<TITLES_PAGE_SIZE){
			string+="<li>"+
				"<a href=\"Content?nwordsid="+nword.nwordid+"&newsid="+nword.news[o].newsid+"\">"+nword.news[o].newstitle+"</a>"+
				"("+nword.news[o].newsdate+")"+
			"</li>";
		}else{
			break;
		}
	}
	if(nword.newstotalcount>TITLES_PAGE_SIZE){
		string+="<li><a onclick=\"moretitles(this)\" href=\"javascript:void(0)\"><input type=\"hidden\" value=\""+TITLES_PAGE_SIZE+"\"\><input type=\"hidden\" value=\""+nword.nword+"\"\><input type=\"hidden\" value=\""+nword.nwordid+"\"\>点击获取更多新闻</a></li>"
	}
	string+="</ul>"+
	"</div>"+
	"<div class=\"passagebottomright\"></div>"+
	"<div class=\"passagebottomleft\"></div>"+
	"<div class=\"clear\"></div>"+
	"</div>";
	
	return 	string;
}

function moreNwords(){
		var toDate=$("#basedate").val();
		var fromDate=$("#basedate").val()-3*86400000;
		var startnum = $("#more").val();
		$.ajax({
		url:'getMoreNwords', 
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		async:true,
		type:'post',
		dataType:'json',
		data:{"fromDate":fromDate,"toDate":toDate,"startnum":startnum},
		beforeSend:function(Request){
			$("#morepassage").hide();
			$("#more").html("正在获取数据....");
		}
		,
		success:function(data){
			var nwords = data["nwords"];
			if(nwords.length!=0){
				$("#morepassage").show();
				addNwords(nwords,startnum+1);
				$("#more").attr("value",startnum+25);
				$("#morepassage").hide();
				$("#more").html("<a onClick=\"moreNwords()\" href=\"javascript:void(0)\" class=\"clickmore\">点击显示更多</a>");
				$("#more").show();
			}else{
				$("#morepassage").html("正在分析这一天的数据，请稍等下再操作....");
			}
		}
	});
}

function moretitles(a){
	
	var startNum = $(a).find("input").first().val();
	var nword = $(a).find("input").get(1).value;
	var nwordid = $(a).find("input").last().val();
	$.ajax({
		url:'getMoreNews',
		contentType: "application/x-www-form-urlencoded; charset=utf-8",  
		async:true,
		type:'post',
		dataType:'json',
		data:{"nword":nword,"startnum":startNum},
		beforeSend:function(Request){
			$(a).html("正在加载...");
		}
		,
		success:function(data){
			var titles = data["titles"];
			$(a).html("<input type=\"hidden\" value=\""+(Number(startNum)+ Number(TITLES_PAGE_SIZE))+"\"\><input type=\"hidden\" value=\""+nword+"\"\><input type=\"hidden\" value=\""+nwordid+"\"\>点击获取更多新闻");
			if(titles.length!=0){
				addTitles(titles,nwordid,a);
				if(titles.length<5){
					$(a).parent().hide();
				}
			}else{
				$(a).parent().hide();
			}
		}
	});
}

function addTitles(titles,nwordid,a){
	var liString = "";
	for(var o in titles){
		liString += "<li>"+
				"<a href=\"Content?nwordsid="+nwordid+"&newsid="+titles[o].newsid+"\">"+titles[o].newstitle+"</a>"+
				"("+titles[o].newsdate+")"+
			"</li>";
	}
	$(a).parent().before(liString);
}