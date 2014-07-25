package com.yn.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.AsyncContinuation;

/**
 * Servlet implementation class TestServlet
 */
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class Member {
		Continuation continuation;
	}
	
	private Map<String, Member> map = new HashMap<String, TestServlet.Member>();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Continuation continuation = ContinuationSupport.getContinuation(request);
		System.out.println("j加速那："+continuation);
		Member member = new Member();
		System.out.println("continuation:"+continuation);
		continuation.setTimeout(6000);
		
		continuation.suspend();
		
		member.continuation = continuation;
		member.continuation.addContinuationListener(new MyContinueListenter("a"));
		map.put("a", member);
		
		System.out.println("ember.continuation1:"+member.continuation);
		System.out.println("continuation status:"+continuation);
		
		response.getWriter().write("aaaa");
		
		//new Thread(new SubThread()).start();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	class SubThread implements Runnable {

		public void run() {
			try {
				Thread.sleep(12000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Member member = map.get("a");
			System.out.println("member:"+member);
			System.out.println("SubThread:"+member.continuation);
//			System.out.println("initnatial:"+member.continuation.isInitial());
		}
		
	}
	
	class MyContinueListenter implements ContinuationListener {
		
		private String key;
		
		public MyContinueListenter(String key) {
			this.key = key;
		}

		public void onComplete(Continuation continuation) {
			System.out.println("comletet:"+continuation);
			Member member = map.get(key);
			System.out.println("member="+member);
			if(member!= null) {
				System.out.println("member.continuation2="+member.continuation);
			}
		}

		public void onTimeout(Continuation continuation) {
			System.out.println("time out");
			Member member = map.get(key);
			System.out.println("member.continuation="+member.continuation);
			member.continuation.complete();
			member.continuation.notifyAll();
			member.continuation = null;
			System.out.println("time out :"+continuation);
			continuation = null;
		}
		
	}
 
}
