package tel.panfilov.http.data;

import java.io.Closeable;
import java.io.IOException;

public class HttpResponse implements Closeable {

    public String headers;
    public HttpBody body;

    @Override
    public void close() throws IOException {
        if (body != null) {
            body.close();
        }
    }
}
