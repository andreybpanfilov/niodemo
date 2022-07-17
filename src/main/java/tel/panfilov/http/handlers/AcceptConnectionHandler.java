package tel.panfilov.http.handlers;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

public class AcceptConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private final Consumer<AsynchronousSocketChannel> consumer;

    public AcceptConnectionHandler(Consumer<AsynchronousSocketChannel> ctxFactory) {
        this.consumer = ctxFactory;
    }

    @Override
    public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel server) {
        server.accept(server, this);
        if (client.isOpen()) {
            consumer.accept(client);
        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        exc.printStackTrace();
    }

}
