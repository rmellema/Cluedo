import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import javax.swing.JTextArea;

public class JTextStream extends OutputStream {
    private JTextArea  area;
    private boolean    closed = false;
    private int        bufSize;
    private char[]     buffer;
    private int        curPos = 0;

    public JTextStream(JTextArea field) {
        this(10000, field);
    }

    public JTextStream(int bufSize, JTextArea field) {
        super();
        this.bufSize = bufSize;
        this.buffer = new char[this.bufSize];
        this.area = field;
    }

    public void write(char[] in, int start, int len) throws IOException {
        if (closed)
            throw new IOException();
        if (curPos + len > 200)
            this.flush();
        for (int i = 0; i < len; ++i) {
            curPos += 1;
            buffer[curPos] = in[start + i];
        }
        this.flush();
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular,
     *                     an <code>IOException</code> may be thrown if the
     *                     output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {
        this.write(new char[]{(char)b}, 0, 1);
    }

    public void flush() throws IOException {
        if (closed)
            throw new IOException();
        area.append(new String(Arrays.copyOfRange(buffer, 1, curPos + 1)));
        curPos = 0;
    }

    public void close() throws IOException {
        flush();
        closed = true;
    }
}