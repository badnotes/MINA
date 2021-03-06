package testcase;

import static testcase.MinaRegressionTest.MSG_COUNT;
import static testcase.MinaRegressionTest.OPEN;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Document me !
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 *
 */
public class MyIoHandler extends IoHandlerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MyIoHandler.class);
  public static AtomicInteger received = new AtomicInteger(0);
  public static AtomicInteger closed = new AtomicInteger(0);
  private final Object LOCK;

  public MyIoHandler(Object lock) {
    LOCK = lock;
  }

  @Override
  public void exceptionCaught(IoSession session, Throwable cause) {
    if (!(cause instanceof IOException)) {
      logger.error("Exception: ", cause);
    } else {
      logger.info("I/O error: " + cause.getMessage());
    }
    session.close(true);
  }

  @Override
  public void sessionOpened(IoSession session) throws Exception {
      logger.info( "Session " + session.getId() + " is opened" );
    session.resumeRead();
  }

  @Override
  public void sessionCreated(IoSession session) throws Exception {
      logger.info( "Creation of session " + session.getId() );
    session.setAttribute(OPEN);
    session.suspendRead();
  }

  @Override
  public void sessionClosed(IoSession session) throws Exception {
    session.removeAttribute(OPEN);
    logger.info("{}> Session closed", session.getId());
    final int clsd = closed.incrementAndGet();
    
    if (clsd == MSG_COUNT) {
      synchronized (LOCK) {
        LOCK.notifyAll();
      }
    }
    
    int i = 0;
    
    try
    {
        int j = 2 / i;
    } 
    catch ( Exception e )
    {
        //e.printStackTrace();
    }
  }

  @Override
  public void messageReceived(IoSession session, Object message) throws Exception {
    IoBuffer msg = (IoBuffer) message;
    long i=msg.remaining();
	Charset ch=Charset.forName("utf-8");
	CharsetDecoder decoder = ch.newDecoder();
	String str = msg.getString(decoder);
	
	logger.info("MESSAGE: " + i + ":"+str+"on session " + session.getId() );
    final int rec = received.incrementAndGet();
    
//    if (rec == MSG_COUNT) {
      synchronized (LOCK) {
        LOCK.notifyAll();
      }
//    }
    
    session.close(true);
  }
}

