package com.yn.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.interfaces.RSAKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class ChatServlet
 */
public class ChatServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	class Member {
		String name;
		Continuation continuation;
		Queue<String> queue = new LinkedList<String>();
	}
	
	Map<String, Map<String, Member>> rooms = new HashMap<String, Map<String,Member>>();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * ajax calls
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		String username = request.getParameter("username");
		String message = request.getParameter("message");
		
		if("join".equals(action)) {
			System.out.println("【 username:"+username+",action:"+action+",message:"+message+"】 ");
			join(request,response,username);
		} else if("chat".equals(action)) {
			System.out.println("【 username:"+username+",action:"+action+",message:"+message+"】 ");
			chat(request,response,username,message);
		} else if("longPolling".equals(action)) {
			System.out.println("【 username:"+username+",action:"+action+",message:"+message+"】 ");
			longPolling(request,response,username);
		}
	}

	private synchronized void longPolling(HttpServletRequest request,HttpServletResponse response, String username) throws IOException {
		
		Map<String, Member> room = rooms.get(request.getPathInfo());
		if(room == null) {
			response.sendError(503);
			return;
		}
		
		Member member = room.get(username);
		if(member == null) {
			response.sendError(503);
			return;
		}
		
		synchronized (member) {
			
			System.err.println("size:"+member.queue.size());
			if(member.queue.size() > 0) {
				//发送信息 send one chat message
				response.setContentType("text/json;charset=utf-8");
				PrintWriter pw = response.getWriter();
				
				JSONObject json = new JSONObject();
				json.put("action", "longPolling");
				json.put("from", member.queue.poll());
				
				String message = member.queue.poll();
				int quote = message.indexOf('"');
				while (quote > 0) {
					message = message.substring(0,quote) + '\\' + message.substring(quote);
					quote = message.indexOf('"',quote+2);
				}
				
				json.put("chat", message);
				
//				byte[] bytes = json.toJSONString().getBytes("UTF-8");
//				response.setContentLength(bytes.length);
//				response.getOutputStream().write(bytes);
				
				pw.write(json.toJSONString());
				
			} else {
				Continuation continuation = ContinuationSupport.getContinuation(request);
				if(continuation.isInitial()) {
					continuation.setTimeout(2000000);
					continuation.suspend();
					member.continuation = continuation;
				} else {
					//timeout
					 response.setContentType("text/json;charset=utf-8");
			         PrintWriter out=response.getWriter();
			         JSONObject json = new JSONObject();
					 json.put("action", "longPolling");
	                 out.write(json.toJSONString());
				}
			}
		}
	}

	private synchronized void chat(HttpServletRequest request, HttpServletResponse response,String username, String message) throws IOException {
		
		Map<String, Member> room = rooms.get(request.getPathInfo());
		if(room != null) {
			//发送请求
			for(Member member : room.values()) {
				synchronized (member) {
					member.queue.add(username);//from;
					member.queue.add(message);//chat
					
					//唤醒长轮询等待的请求
					if(member.continuation != null) {
						member.continuation.resume();
						member.continuation = null;
					}
				}
			}
		}
		
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out=response.getWriter();
		
		JSONObject json = new JSONObject();
		json.put("action", "chat");
	    out.write(json.toJSONString()); 
		
	}

	private synchronized void join(HttpServletRequest request, HttpServletResponse response,String username) throws IOException {
		
		Member member = new Member();
		member.name = username;
		
		Map<String, Member> room = rooms.get(request.getPathInfo());
		if(room == null) {
			room = new HashMap<String,Member>();
			rooms.put(request.getPathInfo(),room);
		}
		
		room.put(username, member);
		response.setContentType("text/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		
		JSONObject json = new JSONObject();
		json.put("action", "join");
		json.put("username", username);
		
		pw.write(json.toJSONString());
		
	}

}
