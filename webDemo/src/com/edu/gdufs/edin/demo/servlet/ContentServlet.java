package com.edu.gdufs.edin.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.edu.gdufs.edin.demo.model.HibernateUtil;
import com.edu.gdufs.edin.demo.model.News;
import com.edu.gdufs.edin.demo.model.Nword;
import com.edu.gdufs.edin.demo.model.NwordsCounter;

public class ContentServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ContentServlet() {
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
		try{
				Session sess = HibernateUtil.currentSession();
				Integer nwordid =  Integer.parseInt((String)request.getParameter("nwordsid"));
				Integer newsid = Integer.parseInt((String)request.getParameter("newsid"));
				Nword nword = (Nword)sess.load(Nword.class, nwordid);
				News news = (News)sess.load(News.class, newsid);
				Query query = sess.createQuery("from NwordsCounter nwc where nwc.id=:nwcid")
						.setInteger("nwcid", nword.getNwordsCountId());
				NwordsCounter nwordcounter = (NwordsCounter)query.uniqueResult();
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern("yyyy年MM月dd日");
				String timeString = 
						/*(sdf.format(nwordcounter.getFromDate())+"-"+*/
						sdf.format(nwordcounter.getToDate())
						/*)*/
						;
				query = sess.createQuery("from News n where n.content like '%"+nword.getWord()+"%'")
						.setFirstResult(0)
						.setMaxResults(5);
				nword.setNewsList((query.list()));	
				request.setAttribute("nword", nword);
				request.setAttribute("timeString", timeString);
				request.setAttribute("news", news);
				
		}catch (Exception e){
			e.printStackTrace();
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Index");
		    rd.forward(request, response);
		}
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/content.jsp");
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
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
