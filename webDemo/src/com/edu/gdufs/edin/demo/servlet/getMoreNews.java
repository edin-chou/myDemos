package com.edu.gdufs.edin.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.edu.gdufs.edin.demo.lucene.LuceneSearcher;
import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.News;
import com.edu.gdufs.edin.demo.model.Nword;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class getMoreNews extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public getMoreNews() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost( request,  response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//filling fromDate and toDate
		String nword = request.getParameter("nword");
		String startNumString = request.getParameter("startnum");
		String outString = "";
		int startNum = 0 ;
		if(nword!=null&&startNumString!=null){
			startNum = Integer.parseInt(startNumString);
			List nwordsList = null;
			Nword nw = new Nword();
			nw.setWord(nword);
			LuceneSearcher lcs = new LuceneSearcher();
			lcs.getMoreTitlesByNword(nw,startNum);	
			outString = createStringBufferJSON(nw.getNewsList());
		}
		
		// setup the response
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("utf-8");
		// write out the xml string
		
		response.getWriter().write(outString);
	}

	
	public String createStringBufferJSON(List nwordsList) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy年MM月dd日");
		StringBuffer returnJSON = new StringBuffer("\r\n{\"titles\":[");
		if(nwordsList!=null){
			Iterator i = nwordsList.iterator();
			while(i.hasNext()){
				returnJSON.append("\r\n{");
				News n = (News)i.next();
				returnJSON.append("\r\n\"newsid\": \"" + n.getId()
						+ "\",");
				returnJSON.append("\r\n\"newstitle\": \"" + n.getTitle()
						+ "\",");
				returnJSON.append("\r\n\"newsdate\": \"" + sdf.format(n.getDate())
						+ "\"");
				returnJSON.append("\r\n},");
			}
			returnJSON.deleteCharAt(returnJSON.length()-1);
		}
		returnJSON.append("\r\n]}");
		return returnJSON.toString();
	}
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
