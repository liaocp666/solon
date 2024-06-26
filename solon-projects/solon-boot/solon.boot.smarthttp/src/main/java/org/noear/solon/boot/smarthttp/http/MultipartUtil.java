package org.noear.solon.boot.smarthttp.http;

import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.http.HttpPartFile;
import org.noear.solon.boot.smarthttp.http.uploadfile.HttpMultipart;
import org.noear.solon.boot.smarthttp.http.uploadfile.HttpMultipartCollection;
import org.noear.solon.boot.io.LimitedInputStream;
import org.noear.solon.core.handle.UploadedFile;
import org.smartboot.http.server.HttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MultipartUtil {
    public static void buildParamsAndFiles(SmHttpContext context, Map<String, List<UploadedFile>> filesMap) throws IOException {
        HttpRequest request = (HttpRequest) context.request();
        HttpMultipartCollection parts = new HttpMultipartCollection(request);

        while (parts.hasNext()) {
            HttpMultipart part = parts.next();
            String name = ServerProps.urlDecode(part.getName());

            if (isFile(part)) {
                doBuildFiles(name, filesMap, part);
            } else {
                context.paramSet(name, part.getString());
            }
        }
    }

    private static void doBuildFiles(String name, Map<String, List<UploadedFile>> filesMap, HttpMultipart part) throws IOException {
        List<UploadedFile> list = filesMap.get(name);
        if (list == null) {
            list = new ArrayList<>();
            filesMap.put(name, list);
        }

        String contentType = part.getHeaders().get("Content-Type");
        String filename = part.getFilename();
        String extension = null;
        int idx = filename.lastIndexOf(".");
        if (idx > 0) {
            extension = filename.substring(idx + 1);
        }

        HttpPartFile partFile = new HttpPartFile(filename, new LimitedInputStream(part.getBody(), ServerProps.request_maxFileSize));
        UploadedFile f1 = new UploadedFile(partFile::delete, contentType, partFile.getSize(), partFile.getContent(), filename, extension);

        list.add(f1);
    }

    private static boolean isField(HttpMultipart filePart) {
        return filePart.getFilename() == null;
    }

    private static boolean isFile(HttpMultipart filePart) {
        return !isField(filePart);
    }
}