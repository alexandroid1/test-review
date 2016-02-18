package com.apofig.samples.spring.service;

import java.io.*;
import java.nio.charset.Charset;

public class ReplaceLRReader extends FilterReader {

    protected ReplaceLRReader(Reader reader) {
        super(reader);
    }

    @Override
    public int read(char cbuf[], int off, int len) throws IOException {
        int read = in.read(cbuf, off, len);

        for (int index = 0; index < cbuf.length; index++) {
            if (cbuf[index] == '\n') {
                if (index != 0 && cbuf[index - 1] != '\r') {
                    cbuf[index] = '^';
                }
            }
        }

        return read;
    }
}
