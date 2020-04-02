package me.ranzeplay.mcgit.managers.zip;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFiles {
    public static void UnzipToDirectory(File zipFilePath, File destinationDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath.getAbsoluteFile()));
        try {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                File file = new File(destinationDirectory, entry.getName());

                if (!file.toPath().normalize().startsWith(destinationDirectory.toPath())) {
                    throw new IOException("Bad zip entry");
                }

                if (entry.isDirectory()) {
                    file.mkdirs();
                    continue;
                }

                byte[] buffer = new byte[1024];
                file.getParentFile().mkdirs();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                int count;

                while ((count = zis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }

                out.close();
            }
        } finally {
            zis.close();
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {


        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
