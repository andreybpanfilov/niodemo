package tel.panfilov.http.handlers;

import tel.panfilov.http.session.HttpSession;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class ReadHeadersHandler extends AbstractHttpIOHandler {
    private StringBuilder headers = new StringBuilder();

    public ReadHeadersHandler(HttpSession httpSession) {
        super(httpSession);
    }

    @Override
    public void start() {
        ByteBuffer buffer = httpSession.buffer;
        AsynchronousSocketChannel channel = httpSession.channel;
        // todo: add timeout
        channel.read(buffer, null, this);
    }

    @Override
    protected void completed(Integer result, AsynchronousSocketChannel channel, ByteBuffer buffer) {
        buffer.flip();
        while (buffer.hasRemaining() && !eoh()) {
            headers.append((char) buffer.get());
        }
        if (!eoh()) {
            buffer.compact();
            channel.read(buffer, null, this);
        } else {
            httpSession.request.rawHeaders = headers.toString();
            httpSession.readBody();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean eoh() {
        int length = headers.length();
        if (length < 1 || headers.charAt(length - 1) != '\n') {
            return false;
        }
        if (length >= 4 && headers.charAt(length - 2) == '\r' && headers.charAt(length - 3) == '\n') {
            return true;
        }
        return length >= 2 && headers.charAt(length - 2) == '\n';
    }

}
