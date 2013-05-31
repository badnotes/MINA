package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private ServerSocket ss;
	private Socket socket ;
	private BufferedReader in ;
	private PrintWriter out ;
	public Server() {
		ExecutorService exec = Executors.newFixedThreadPool(10);
		try {
			ss = new ServerSocket(8889);
			while (true) {
				socket = ss.accept();
				String RemoteIP = socket.getInetAddress().getHostAddress();
				String RemotePort = ":" + socket.getLocalPort();
				// serverthread
				System.out.println("A client come in!IP:" + RemoteIP
						+ RemotePort);
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(),
						true);
				exec.execute(new inserverthread(in));
				exec.execute(new outserverthread(out));				
			}
		} catch (IOException e) {
			System.out.println("wrong");
		}
		exec.shutdown();
	}

	public static void main(String[] args) {
		new Server();
	}
};
