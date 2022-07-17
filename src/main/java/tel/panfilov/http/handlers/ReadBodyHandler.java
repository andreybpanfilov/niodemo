package tel.panfilov.http.handlers;

import tel.panfilov.http.session.HttpSession;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class ReadBodyHandler extends AbstractHttpIOHandler {
    public ReadBodyHandler(HttpSession httpSession) {
        super(httpSession);
    }

    @Override
    public void start() {
        ByteBuffer buffer = httpSession.buffer;
        AsynchronousSocketChannel channel = httpSession.channel;
        channel.read(buffer, null, this);
    }

    @Override
    protected void completed(Integer result, AsynchronousSocketChannel channel, ByteBuffer buffer) {
        // todo: implement read body

        // next handler in chain
        httpSession.processRequest();
    }

}
