package com.apofig.samples.spring.service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

/**
 * User: sanja
 * Date: 11.09.13
 * Time: 23:29
 */
public class WithFile {
    public File file;
    private OutputStream writer;
    private InputStream reader;

    public WithFile(String fileName) {
        this.file = openFile(fileName);
    }

    private File openFile(String fileName) {
        try {
            URL resource = getClass().getClassLoader().getResource(fileName);
            if (resource == null) {
                File file = new File(fileName);
                file.createNewFile();
                return file;
            }
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("File name: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("File name: " + fileName, e);
        }
    }

    public static byte[] getUTF8Bytes(String string) throws UnsupportedEncodingException {
        return string.getBytes("UTF-8");
    }

    public void save(String data) {
        try {
            openWriter(false);
            writer.write(getUTF8Bytes(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    private void openWriter(boolean append) throws IOException {
        if (writer == null) {
            writer = new FileOutputStream(file, append);
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.flush();
                writer.close();
                writer = null;
            }
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WithFile saveAnd(String data) {
        try {
            openWriter(false);
            writer.write(getUTF8Bytes(data));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public void append(String data) {
        try {
            openWriter(true);
            writer.write(getUTF8Bytes(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public WithFile appendAnd(String data) {
        try {
            openWriter(true);
            writer.write(getUTF8Bytes(data));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public WithFile appendAnd(byte[] bytes) {
        try {
            openWriter(true);
            writer.write(bytes, 0, bytes.length);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public WithFile appendAnd(byte[] bytes, int count) {
        try {
            openWriter(true);
            writer.write(bytes, 0, count);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public WithFile appendAnd(File newFile) {
        try {
            InputStream reader2 = new FileInputStream(newFile);
            openWriter(true);
            byte[] buff = new byte[9000];

            while (true) {
                int read = reader2.read(buff);
                if (read == -1) {
                    break;
                }

                writer.write(buff, 0, read);
            }
            writer.flush();
            reader2.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public String load() {
        try {
            openReader();
            byte[] bytes = new byte[(int) file.length()];
            reader.read(bytes);
            return new String(bytes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    private void openReader() {
        try {
            if (reader == null) {
                reader = new FileInputStream(file);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> loadSplitted(String separator) {
        return Arrays.asList(load().split(separator));
    }

    public String readLineAnd() {
        openReader();
        try {
            byte[] buffer = new byte[1];
            int count = 0;
            String result = "";

            while (true) {
                count = reader.read(buffer);

                if (count == -1) break;

                result += (char)buffer[0];

                if (buffer[0] == '\n') break;
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove() {
        close();
        file.delete();
    }

    public void move(File to) throws IOException {
        close();
        Files.move(file.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public File file() {
        close();
        return file;
    }

    public WithFile clear() {
        try {
            file.delete();
            file.createNewFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public byte[] readAnd(int count) {
        byte[] buff = new byte[count];
        try {
            openReader();

            int read = reader.read(buff);
            if (read == -1) {
                throw new IllegalArgumentException("Unexpected EOF");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff;
    }

    public void write(byte[] chars) {
        try {
            openWriter(false);
            writer.write(chars);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public long length() {
        return file.length();
    }
}
