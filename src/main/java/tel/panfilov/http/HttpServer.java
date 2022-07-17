package tel.panfilov.http;

import tel.panfilov.http.handlers.AcceptConnectionHandler;
import tel.panfilov.http.data.ByteArrayHttpBody;
import tel.panfilov.http.session.HttpSession;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class HttpServer {

    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        //server.setOption(StandardSocketOptions.SO_REUSEPORT, true);
        server.bind(new InetSocketAddress(5555));
        Consumer<AsynchronousSocketChannel> consumer = client -> {
            //HttpSession.start(client, 1_000_000, () -> new FileHttpBody(Paths.get("....")));
            HttpSession.start(client, 1_000_000, () -> new ByteArrayHttpBody(1_000_000));
        };
        server.accept(server, new AcceptConnectionHandler(consumer));
        new CountDownLatch(1).await();
    }

}
