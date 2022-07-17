package tel.panfilov.http.handlers;

import tel.panfilov.http.session.HttpSession;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class WriteHeadersHandler extends AbstractHttpIOHandler {

    private byte[] headers;

    private int position = 0;

    public WriteHeadersHandler(HttpSession httpSession) {
        super(httpSession);
    }

    @Override
    public void start() {
        headers = httpSession.response.headers.getBytes();
        ByteBuffer buffer = httpSession.buffer;
        AsynchronousSocketChannel channel = httpSession.channel;
        buffer.clear();
        buffer.put(headers, 0, Math.min(headers.length, buffer.capacity()));
        this.position = buffer.limit();
        buffer.flip();
        channel.write(buffer, null, this);
    }

    @Override
    protected void completed(Integer result, AsynchronousSocketChannel channel, ByteBuffer buffer) throws Exception {
        if (position < headers.length) {
            buffer.compact();
            int len = Math.min(headers.length - position, buffer.remaining());
            buffer.put(headers, position, len);
            position += len;
            buffer.flip();
        }
        if (buffer.hasRemaining()) {
            channel.write(buffer, null, this);
        } else {
            httpSession.writeBody();
        }
    }

}
