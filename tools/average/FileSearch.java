package com.tb.ba.analysis.average;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSearch {

    /**
     * Ermittelt für eine gegebene Ergebnis-Datei das arithmetische Mittel.
     * @param filename Dateipfad zur tsv-Datei
     * @param lines
     * @return Das arithmetische Mittel
     * @throws IOException
     */
    public static double average(String filename, Integer lines) throws IOException {
        int counter = 0;
        double value = 0;
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
                        String[] line = new String(buf, startOfLine, len)
                                .split("	");
                        if (lines > counter) {
                            try {
                                value += Double.parseDouble(line[1]);
                                counter++;
                            } catch (NumberFormatException e) {
                            }
                        }


                    }
                    unparsedSize = index + 1;
                }
                --index;
            }
            if (lines <= counter) {
                break;
            }

            end = end - (chunkSize - unparsedSize);

            readMore = startPoint != 0;
        }

        raf.close();

        return value / counter;
    }


    /**
     * Ermittelt für eine gegebene Ergebnis-Datei das arithmetische Mittel.
     * @param filename Dateipfad zur tsv-Datei
     * @param lines
     * @param avg Der bereits ermittelte Durchschnitt
     * @return
     * @throws IOException
     */
    public static double variance(String filename, Integer lines, Double avg) throws IOException {
        int counter = 0;
        double value = 0;
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
                        String[] line = new String(buf, startOfLine, len)
                                .split("	");
                        if (lines > counter) {

                            try {
                                value += (Double.parseDouble(line[line.length - 1]) - avg) * (Double.parseDouble
                                        (line[line.length - 1]) -
                                        avg);
                            } catch (NumberFormatException e) {

                            }
                            counter++;
                        }


                    }
                    unparsedSize = index + 1;
                }
                --index;
            }
            if (lines <= counter) {
                break;
            }


            end = end - (chunkSize - unparsedSize);

            readMore = startPoint != 0;
        }

        raf.close();


        return Math.sqrt((Double) ((1.0 / (counter - 1))) * value);
    }

}
