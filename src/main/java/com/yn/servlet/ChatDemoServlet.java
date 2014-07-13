package com.yn.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import com.yn.handler.MyAsyncHandler;
import com.yn.handler.MyHandler;

public class ChatDemoServlet extends HttpServlet {

	private MyAsyncHandler myAsyncHandler;
	private static final long serialVersionUID = 8453299347415077927L;

	public void init() throws ServletException {
		myAsyncHandler = new MyAsyncHandler() {
			public void register(MyHandler myHandler) {

			}
		};
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final Continuation continuation = ContinuationSupport
				.getContinuation(request);
		continuation.setTimeout(2000);
		Object result = continuation.getAttribute("result");
		System.out.println("result1:" + result);

		if (request == null) {
			System.out.println("expire:" + continuation.isExpired());
			// 判断是否超时,已超时
			if (continuation.isExpired()) {
				// 返回超时response
				sendMyTimeoutResponse(response);
				return;
			}

			System.err.println("continuation:" + continuation);

			// 挂起http连接
			continuation.suspend();
			System.out.println("suspend:");
			// 注册一个异步事件处理器
			myAsyncHandler.register(new MyHandler() {
				public void onMyEvent(Object result) {
					System.out.println("aaaaaaaaaaa");
					continuation.setAttribute("result", "我们好啊");
					continuation.resume();
				}
			});
			
			continuation.setAttribute("result", "我们好啊");
			continuation.resume();
			
//			return;
		}

		result = continuation.getAttribute("result");
		System.out.println("result2:" + result);
		sendMyResultResponse(response, result);

	}

	private void sendMyTimeoutResponse(HttpServletResponse response)
			throws IOException {
		response.getWriter().write("timeout");
	}

	private void sendMyResultResponse(HttpServletResponse response,
			Object results) throws IOException {

		response.getWriter().write("results:" + results);
		response.getWriter().flush();

	}

}
