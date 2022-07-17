package tel.panfilov.http.handlers;

import tel.panfilov.http.session.HttpSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class CloseConnectionHandler extends AbstractHttpIOHandler {

    private boolean http10;

    public CloseConnectionHandler(HttpSession httpSession) {
        super(httpSession);
    }

    @Override
    public void start() throws IOException {
        ByteBuffer buffer = httpSession.buffer;
        AsynchronousSocketChannel channel = httpSession.channel;
        this.http10 = !"HTTP/1.1".equalsIgnoreCase(httpSession.request.getHttpVersion());
        if (this.http10) {
            channel.shutdownOutput();
        }
        buffer.clear();
        // todo: add timeout
        channel.read(buffer, null, this);
    }

    @Override
    protected void completed(Integer result, AsynchronousSocketChannel channel, ByteBuffer buffer) throws Exception {
        buffer.clear();
        channel.read(buffer, null, this);
    }

}
