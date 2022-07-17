package tel.panfilov.http.data;

public class HttpRequest {

    public String rawHeaders;

    public String httpVersion;

    public String getHttpVersion() {
        if (httpVersion == null) {
            httpVersion = rawHeaders.split("(\r)?\n")[0].split("\\s")[2];
        }
        return httpVersion;
    }


}
