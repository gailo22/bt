package bt.bencoding.model;

import bt.bencoding.BEEncoder;
import bt.bencoding.BEType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * BEncoded string.
 *
 * @since 1.0
 */
public class BEString implements BEObject<byte[]> {
    private static final Charset defaultCharset = Charset.forName("UTF-8");

    private byte[] content;
    private BEEncoder encoder;

    private volatile String stringValue;
    private final Object lock;

    /**
     * @param content Binary representation of this string, as read from source.
     *                It is also the value of this string, being a UTF-8 encoded byte array.
     * @since 1.0
     */
    public BEString(byte[] content) {
        this.content = content;
        this.encoder = BEEncoder.encoder();
        this.lock = new Object();
    }

    @Override
    public BEType getType() {
        return BEType.STRING;
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public byte[] getValue() {
        return content;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        encoder.encode(this, out);
    }

    public String getValue(Charset charset) {
        return new String(content, charset);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof BEString)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        return Arrays.equals(content, ((BEString) obj).getContent());
    }

    @Override
    public String toString() {
        if (stringValue == null) {
            synchronized (lock) {
                stringValue = new String(content, defaultCharset);
            }
        }
        return stringValue;
    }
}
