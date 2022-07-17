package tel.panfilov.http.handlers;

import tel.panfilov.http.session.HttpSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class WriteBodyHandler extends AbstractHttpIOHandler {
    private ReadableByteChannel stream;

    private long length = -1;

    private int position = 0;

    public WriteBodyHandler(HttpSession httpSession) {
        super(httpSession);
    }

    @Override
    public void start() throws Exception {
        stream = Channels.newChannel(httpSession.response.body.getStream());
        length = httpSession.response.body.getLength();
        ByteBuffer buffer = httpSession.buffer;
        AsynchronousSocketChannel channel = httpSession.channel;
        buffer.clear();
        this.position += stream.read(buffer);
        buffer.flip();
        channel.write(buffer, null, this);
    }

    @Override
    protected void completed(Integer result, AsynchronousSocketChannel channel, ByteBuffer buffer) throws Exception {
        if (position < length) {
            // todo: optimize, both small an large buffers are evil
            buffer.compact();
            position += stream.read(buffer);
            buffer.flip();
        }
        if (buffer.hasRemaining()) {
            channel.write(buffer, null, this);
        } else {
            httpSession.shutdown();
        }
    }

    protected void release() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void failed(Throwable exc) throws IOException {
        try {
            super.failed(exc);
        } finally {
            release();
        }
    }

}
