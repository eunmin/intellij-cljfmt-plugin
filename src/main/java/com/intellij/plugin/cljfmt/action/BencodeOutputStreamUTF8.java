package com.intellij.plugin.cljfmt.action;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;

public class BencodeOutputStreamUTF8 extends FilterOutputStream  {

    static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static final char NUMBER = 'i';

    static final char LIST = 'l';

    static final char DICTIONARY = 'd';

    static final char TERMINATOR = 'e';

    static final char SEPARATOR = ':';

    private final Charset charset;

    public BencodeOutputStreamUTF8(final OutputStream out, final Charset charset) {
        super(out);

        if (charset == null) throw new NullPointerException("charset cannot be null");
        this.charset = charset;
    }

    public BencodeOutputStreamUTF8(final OutputStream out) {
        this(out, DEFAULT_CHARSET);
    }

    public Charset getCharset() {
        return charset;
    }


    public void writeString(final String s) throws IOException {
        write(encode(s));
    }

    public void writeString(final ByteBuffer buff) throws IOException {
        write(encode(buff.array()));
    }

    public void writeNumber(final Number n) throws IOException {
        write(encode(n));
    }

    public void writeList(final Iterable<?> l) throws IOException {
        write(encode(l));
    }

    public void writeDictionary(final Map<?, ?> m) throws IOException {
        write(encode(m));
    }

    private byte[] encode(final String s) throws IOException {
        if (s == null) throw new NullPointerException("s cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(Integer.toString(s.getBytes().length).getBytes(charset));
        buffer.write(SEPARATOR);
        buffer.write(s.getBytes(charset));

        return buffer.toByteArray();
    }

    private byte[] encode(final byte[] b) throws IOException {
        if (b == null) throw new NullPointerException("b cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        buffer.write(Integer.toString(b.length).getBytes(charset));
        buffer.write(SEPARATOR);
        buffer.write(b);

        return buffer.toByteArray();
    }

    private byte[] encode(final Number n) throws IOException {
        if (n == null) throw new NullPointerException("n cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(NUMBER);
        buffer.write(Long.toString(n.longValue()).getBytes(charset));
        buffer.write(TERMINATOR);

        return buffer.toByteArray();
    }

    private byte[] encode(final Iterable<?> l) throws IOException {
        if (l == null) throw new NullPointerException("l cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(LIST);
        for (Object o : l)
            buffer.write(encodeObject(o));
        buffer.write(TERMINATOR);

        return buffer.toByteArray();
    }

    private byte[] encode(final Map<?, ?> map) throws IOException {
        if (map == null) throw new NullPointerException("map cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(DICTIONARY);
        for (Map.Entry<?, ?> e : map.entrySet()) {
            buffer.write(encode(e.getKey().toString()));
            buffer.write(encodeObject(e.getValue()));
        }
        buffer.write(TERMINATOR);

        return buffer.toByteArray();
    }

    private byte[] encodeObject(final Object o) throws IOException {
        if (o == null) throw new NullPointerException("Cannot write null objects");

        if (o instanceof Number)
            return encode((Number) o);
        if (o instanceof Iterable<?>)
            return encode((Iterable<?>) o);
        if (o instanceof Map<?, ?>)
            return encode((Map<?, ?>) o);
        if (o instanceof ByteBuffer)
            return encode(((ByteBuffer) o).array());

        return encode(o.toString());
    }
}
