package com.amaze.fileutilities.cast.cloud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudStreamer extends CloudStreamServer {

    private static final Logger log = LoggerFactory.getLogger(CloudStreamer.class);

    public static final int PORT = 7871;

    private InputStream inputStream;
    private String fileName;
    long length = 0;
    private static CloudStreamer instance;

    protected CloudStreamer(int port) throws IOException {
        super(port, new File("."));
    }

    public static CloudStreamer getInstance() {
        if (instance == null)
            try {
                instance = new CloudStreamer(PORT);
            } catch (IOException e) {
                log.error("Error initializing CloudStreamer", e);
            }
        return instance;
    }

  /*public static boolean isStreamMedia(SmbFile file) {
    return pattern.matcher(file.getName()).matches();
  }*/

    public void setStreamSrc(InputStream inputStream, String fileName, long length) {
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.length = length;
    }

    @Override
    public void stop() {
        super.stop();
        instance = null;
    }

    @Override
    public Response serve(
            String uri, String method, Properties header, Properties parms, Properties files) {
        Response res = null;

        if (inputStream == null)
            res = new com.amaze.fileutilities.cast.cloud.CloudStreamServer.Response(HTTP_NOTFOUND, MIME_PLAINTEXT, null);
        else {

            long startFrom = 0;
            long endAt = -1;
            String range = header.getProperty("range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf('-');
                    try {
                        if (minus > 0) {
                            startFrom = Long.parseLong(range.substring(0, minus));
                            endAt = Long.parseLong(range.substring(minus + 1));
                        }
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
            log.debug("Request: {} from: {}, to: {}", range, startFrom, endAt);

            // Change return code and add Content-Range header when skipping
            // is requested.
            final CloudStreamSource source = new CloudStreamSource(fileName, length, inputStream);
            long fileLen = source.length();
            if (range != null && startFrom > 0) {
                if (startFrom >= fileLen) {
                    res = new com.amaze.fileutilities.cast.cloud.CloudStreamServer.Response(HTTP_RANGE_NOT_SATISFIABLE, MIME_PLAINTEXT, null);
                    res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
                } else {
                    if (endAt < 0) endAt = fileLen - 1;
                    long newLen = fileLen - startFrom;

                    log.debug("start=" + startFrom + ", endAt=" + endAt + ", newLen=" + newLen);
                    source.moveTo(startFrom);
                    log.debug("Skipped " + startFrom + " bytes");

                    res = new com.amaze.fileutilities.cast.cloud.CloudStreamServer.Response(HTTP_PARTIALCONTENT, null, source);
                    res.addHeader("Content-length", "" + newLen);
                }
            } else {
                source.reset();
                res = new com.amaze.fileutilities.cast.cloud.CloudStreamServer.Response(HTTP_OK, null, source);
                res.addHeader("Content-Length", "" + fileLen);
            }
        }

        res.addHeader("Accept-Ranges", "bytes");
        return res;
    }
}
