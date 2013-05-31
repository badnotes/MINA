package client;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MyClient {
	public static void main(String args[]) {
		// create tcp/ip connector
		IoConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		chain.addLast("myChin", new ProtocolCodecFilter(
				new TextLineCodecFactory()));
		// connector.getFilterChain().addLast("codec", new
		// ProtocolCodecFilter(new
		// TextLineCodecFactory(Charset.forName("UTF-8"),LineDelimiter.WINDOWS.getValue(),LineDelimiter.WINDOWS.getValue())));
		connector.setHandler(new SamplMinaClientHandler());
		// set connect timeout
		connector.setConnectTimeoutMillis(30000);
		// connector.setConnectTimeout(30);
		ConnectFuture cf = connector.connect(new InetSocketAddress("localhost",
				7890));
		// wait for the connection attem to be finished
		cf.awaitUninterruptibly();
		cf.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
}
