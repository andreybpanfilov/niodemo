package tel.panfilov.http.data;

import java.io.Closeable;
import java.io.InputStream;

public interface HttpBody extends Closeable {

    long getLength();

    InputStream getStream();

}
