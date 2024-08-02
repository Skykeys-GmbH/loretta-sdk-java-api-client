package aero.loretta.client.api;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class IOUtils {
    private static final int EOF = -1;
    private static final byte[] ZIP_MAGIC = {0x50, 0x4b, 0x03, 0x04};

    public static Map<String, byte[]> readZipArchive(byte[] content) throws IOException {
        Map<String, byte[]> result = new HashMap<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(content))) {
            ZipEntry zipEntry;
            while((zipEntry = zipInputStream.getNextEntry()) != null) {
                try {
                    if (!zipEntry.isDirectory()) {
                        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                            copy(zipInputStream, out);
                            result.put(zipEntry.getName(), out.toByteArray());
                        }
                    }
                } finally {
                    zipInputStream.closeEntry();
                }
            }
        }

        return result;
    }

    public static boolean isZipArchive(byte[] content) {
        if (content.length <= ZIP_MAGIC.length) {
            return false;
        }
        for(int i = 0; i < ZIP_MAGIC.length; i++) {
            if (ZIP_MAGIC[i] != content[i]) {
                return false;
            }
        }
        return true;
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while((read = in.read(buffer)) != EOF) {
            out.write(buffer, 0, read);
        }
    }
}
