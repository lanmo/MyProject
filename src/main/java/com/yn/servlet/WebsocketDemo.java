package com.yn.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class WebsocketDemo extends WebSocketServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 508341482795960419L;

	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		return new MyWebsocket();
	}
	
	class MyWebsocket implements OnTextMessage {
		
		private Connection connection;
		
		public void onOpen(Connection connection) {
			this.connection = connection;
			System.out.println("open......");
		}

		public void onClose(int closeCode, String message) {
			System.out.println("close............");
		}

		public void onMessage(String data) {
			System.out.println(data+",data");
			try {
				this.connection.sendMessage("我是服务器。。。。。。。。");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
