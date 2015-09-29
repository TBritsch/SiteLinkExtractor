package com.tb.ba.analysis.grep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * Durchsucht eine Datei nach einem Wert und gibt den dazu passenden PageRank aus.
 * Created by T. Britsch on 26.08.2015.
 */
public class FileSearch {

    public static Result search(String filename, String page) {
        if (page.charAt(page.length() - 1) == ' ') {
            char[] pageChars = page.toCharArray();
            pageChars[page.length() - 1] = '	';
            page = String.valueOf(pageChars);
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(new File(filename), "r");


            final int chunkSize = 1024 * 32;
            long end = raf.length();
            boolean readMore = true;
            while (readMore) {
                byte[] buf = new byte[chunkSize];

                long startPoint = end - chunkSize;
                long readLen = chunkSize;
                if (startPoint < 0) {
                    readLen = chunkSize + startPoint;
                    startPoint = 0;
                }
                raf.seek(startPoint);
                readLen = raf.read(buf, 0, (int) readLen);
                if (readLen <= 0) {
                    break;
                }

                int unparsedSize = (int) readLen;
                int index = unparsedSize - 1;
                while (index >= 0) {
                    if (buf[index] == '\n') {
                        int startOfLine = index + 1;
                        int len = (unparsedSize - startOfLine);
                        if (len > 0) {
                            String search = new String(buf, startOfLine, len);
                            if (search.contains(page)) {
                                readMore = false;
                                raf.close();
                                String[] split = search.split("\t");
                                return new Result(split[split.length - 2], split[split.length - 1]
                                        .substring(0, 10));
                            }

                        }
                        unparsedSize = index + 1;
                    }
                    --index;
                }

                end = end - (chunkSize - unparsedSize);

                readMore = startPoint != 0;
            }

            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result("error", "-");
    }

}
