package tel.panfilov.http.handlers;

import tel.panfilov.http.HttpPhase;
import tel.panfilov.http.session.HttpSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public abstract class AbstractHttpIOHandler implements HttpPhase, CompletionHandler<Integer, Void> {

    protected final HttpSession httpSession;

    public AbstractHttpIOHandler(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public void completed(Integer result, Void attachment) {
        try {
            ByteBuffer buffer = httpSession.buffer;
            AsynchronousSocketChannel channel = httpSession.channel;
            if (result < 0) {
                // socket closed
                httpSession.abort();
                return;
            }
            completed(result, channel, buffer);
        } catch (Exception ex) {
            httpSession.onFail(ex);
        }
    }

    protected abstract void completed(Integer result, AsynchronousSocketChannel channel, ByteBuffer buffer) throws Exception;

    @Override
    public void failed(Throwable exc, Void attachment) {
        try {
            failed(exc);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void failed(Throwable exc) throws IOException {
        httpSession.onFail(exc);
    }

}
