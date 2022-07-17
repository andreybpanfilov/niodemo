package tel.panfilov.http.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHttpBody implements HttpBody {

    private final InputStream stream;

    private final long size;

    public FileHttpBody(Path path) {
        try {
            this.size = Files.size(path);
            this.stream = Files.newInputStream(path);
        } catch (IOException ex) {
            throw new RuntimeException();
        }
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
        if (stream != null) {
            stream.close();
        }
    }
}
