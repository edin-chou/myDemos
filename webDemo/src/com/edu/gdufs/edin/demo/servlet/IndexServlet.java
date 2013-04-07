package com.edu.gdufs.edin.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.edu.gdufs.edin.demo.lucene.LuceneSearcher;
import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.Nword;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class IndexServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public IndexServlet() {
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
		
		String fromDateString = request.getParameter("fromDate");
		String toDateString = request.getParameter("toDate");
		Date fromDate = null;
		Date toDate = null;
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DATE);
		c.set(year,month,date,0,0,0);
		long toDay = c.getTime().getTime();
		request.setAttribute("toDay",toDay);
		if(fromDateString==null||toDateString==null){
			toDate = c.getTime();
			c.add(Calendar.DATE, -3);
			fromDate = c.getTime();
		}else{
			fromDate = new Date(Long.parseLong(request.getParameter("fromDate")));
			toDate = new Date(Long.parseLong(request.getParameter("toDate")));
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
					.setFirstResult(0)
					.setMaxResults(30);
			nwordsList = query.list();
		}
		if(nwordsList!=null){
			Iterator i = nwordsList.iterator();
			LuceneSearcher lcs = new LuceneSearcher();
			while(i.hasNext()){
				Nword nw =(Nword)i.next(); 
				nw.setNewsList(lcs.getTitlesByWords(nw.getWord()));	
			}
		}
		request.setAttribute("nwordsList", nwordsList);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
	    rd.forward(request, response);
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

		doGet(request,response);
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
