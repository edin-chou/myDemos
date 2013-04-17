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

public class getMoreNwords extends HttpServlet {

	private static final int NWORD_PAGE_SIZE=25;
	
	/**
	 * Constructor of the object.
	 */
	public getMoreNwords() {
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
		String fromDateString = request.getParameter("fromDate");
		String toDateString = request.getParameter("toDate");
		String startNumString = request.getParameter("startnum");
		Date fromDate = null;
		Date toDate = null;
		int startNum = NWORD_PAGE_SIZE;
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DATE);
		c.set(year,month,date,0,0,0);
		long toDay = c.getTime().getTime();
		if(fromDateString==null||toDateString==null||startNumString==null){
			toDate = c.getTime();
			c.add(Calendar.DATE, -3);
			fromDate = c.getTime();
		}else{
			fromDate = new Date(Long.parseLong(request.getParameter("fromDate")));
			toDate = new Date(Long.parseLong(request.getParameter("toDate")));
			startNum = Integer.parseInt(request.getParameter("startnum"));
		}
		
		List nwordsList = null;
		Session sess = HibernateUtil.currentSession();
		Query query= sess.createQuery("from NwordsCounter nwc where nwc.fromDate=:fromDate and nwc.toDate=:toDate")
				.setDate("fromDate", fromDate)
				.setDate("toDate", toDate);
		NwordsCounter nwc = (NwordsCounter)query.uniqueResult();
		if(nwc!=null){
			query = sess.createQuery("from Nword nw where nw.nwordsCountId=:nwcid order by nw.nwordCount desc")
					.setInteger("nwcid", nwc.getId())
					.setFirstResult(startNum)
					.setMaxResults(NWORD_PAGE_SIZE);
			nwordsList = query.list();
			if(nwordsList!=null){
				Iterator i = nwordsList.iterator();
				LuceneSearcher lcs = new LuceneSearcher();
				while(i.hasNext()){
					Nword nw =(Nword)i.next(); 
					lcs.getTitlesByNword(nw);	
				}
			}
		}
		// setup the response
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("utf-8");
		// write out the xml string
		String outString = createStringBufferJSON(nwordsList);
		response.getWriter().write(outString);
	}
	
	public String createStringBufferJSON(List nwordsList) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy年MM月dd日");
		StringBuffer returnJSON = new StringBuffer("\r\n{\"nwords\":[");
		if(nwordsList!=null){
			Iterator i = nwordsList.iterator();
			int nwordscount = 0;
			while(i.hasNext()&&nwordscount++<NWORD_PAGE_SIZE){
				Nword nw = (Nword)i.next();
				returnJSON.append("\r\n{");
				returnJSON.append("\r\n\"nwordid\": \"" + nw.getId()
						+ "\",");
				returnJSON.append("\r\n\"nword\": \"" + nw.getWord()
						+ "\",");
				returnJSON.append("\r\n\"nwordLDOF\": \"" + nw.getLentropy()
						+ "\",");
				returnJSON.append("\r\n\"nwordRDOF\": \"" + nw.getRentropy()
						+ "\",");
				returnJSON.append("\r\n\"nwordDOC\": \"" + nw.getMutualInfo()
						+ "\",");
				returnJSON.append("\r\n\"nwordCount\": \"" + nw.getNwordCount()
						+ "\",");
				returnJSON.append("\r\n\"newstotalcount\": \"" + nw.getNewsTotalCount()
						+ "\",");
				returnJSON.append("\r\n\"news\": [");
				if(nw.getNewsList()!=null){
					Iterator i2 = nw.getNewsList().iterator();
					while(i2.hasNext()){
						returnJSON.append("\r\n{");
						News n = (News)i2.next();
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
				returnJSON.append("\r\n]");
				returnJSON.append("\r\n},");
			}
			returnJSON.deleteCharAt(returnJSON.length()-1);
		}
		returnJSON.append("\r\n]\r\n}");
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
