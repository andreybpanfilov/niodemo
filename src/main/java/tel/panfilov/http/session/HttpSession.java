package tel.panfilov.http.session;

import tel.panfilov.http.data.HttpRequest;
import tel.panfilov.http.data.HttpResponse;
import tel.panfilov.http.handlers.CloseConnectionHandler;
import tel.panfilov.http.handlers.ReadBodyHandler;
import tel.panfilov.http.handlers.ReadHeadersHandler;
import tel.panfilov.http.handlers.WriteBodyHandler;
import tel.panfilov.http.handlers.WriteHeadersHandler;
import tel.panfilov.http.data.HttpBody;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.Supplier;

public class HttpSession {

    public ByteBuffer buffer;

    public AsynchronousSocketChannel channel;

    public Supplier<HttpBody> responseFactory;

    public HttpRequest request;

    public HttpResponse response;

    private HttpSession() {

    }

    public static void start(AsynchronousSocketChannel client, int bufsz, Supplier<HttpBody> responseFactory) {
        HttpSession context = new HttpSession();
        context.buffer = ByteBuffer.allocateDirect(bufsz);
        context.channel = client;
        context.responseFactory = responseFactory;
        context.request = new HttpRequest();
        context.response = new HttpResponse();
        context.readHeaders();
    }

    public void processRequest() {
        this.response.body = responseFactory.get();
        this.response.headers = "HTTP/1.1 200 OK\r\n" +
                "Server: Cloud Auctioneers\r\n" +
                "Content-Type: text/plain\r\n" +
                // tell client to close socket
                "Connection: close\r\n" +
                "Content-Length: " + response.body.getLength() + "\r\n\r\n";
        writeHeaders();
    }

    public void readHeaders() {
        new ReadHeadersHandler(this).start();
    }

    public void readBody() {
        new ReadBodyHandler(this).start();
    }

    public void writeHeaders() {
        new WriteHeadersHandler(this).start();
    }

    public void writeBody() throws Exception {
        new WriteBodyHandler(this).start();
    }

    public void shutdown() {
        try {
            release();
            new CloseConnectionHandler(this).start();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public void abort() {
        try {
            release();
            channel.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    protected void release() {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public void onFail(Throwable ex) {
        ex.printStackTrace();
        abort();
    }

}
