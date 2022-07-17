package tel.panfilov.http.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayHttpBody implements HttpBody {

    private final int size;

    private final InputStream stream;

    public ByteArrayHttpBody(int size) {
        this.size = size;
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = '#';
        }
        this.stream = new ByteArrayInputStream(data);
    }

    @Override
    public long getLength() {
        return size;
    }

    @Override
    public InputStream getStream() {
        return stream;
    }

    @Override
    public void close() throws IOException {

    }

}
