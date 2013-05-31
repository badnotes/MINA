package client;   
  
import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

  
public class MainServer {   
    public static void main(String args[]) throws IOException{   
        SocketAcceptor acceptor = new NioSocketAcceptor();   
        
        DefaultIoFilterChainBuilder chain= acceptor.getFilterChain();   
        chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));   
        acceptor.setHandler(new SamplMinaServerHandler());   
        int bindport=9988;   
        acceptor.bind(new InetSocketAddress(bindport));   
        System.out.println("start ok,listen on:="+bindport);   
           
    }   
  
}  
