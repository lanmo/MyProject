package com.yn.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

/**
 * Servlet implementation class TestServlet
 */
public class Test1Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Continuation continuation = ContinuationSupport.getContinuation(req);
		String time = req.getParameter("time");
		
		System.out.println(time);
		System.out.println(continuation);
		
		continuation.setTimeout(20000);
		if(continuation.isInitial()) {
			continuation.suspend();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!continuation.isInitial()) {
			continuation.resume();
		}
		
	}
	
	static class MyThread implements Runnable {
		
		public MyThread() {
		}

		public void run() {
			
		}
		
	}
	
}
