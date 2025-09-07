package world.jdl.gateway.compression;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class ZLibCompression implements Compressor
{
    private static final byte[] ZLIB_FLUSH = { 0x00, 0x00, (byte) 0xff, (byte) 0xff };

    private final Inflater inflater = new Inflater();
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);

    @Override
    public String chunk(final byte[] bytes)
    {
        if (isEndOfStream(bytes))
        {
            inflater.setInput(bytes);
            final int length;
            try
            {
                length = inflater.inflate(buffer);
            } catch (DataFormatException e)
            {
                throw new RuntimeException(e);
            }
            final String outputString = new String(
                    buffer.array(),
                    0,
                    length,
                    StandardCharsets.UTF_8);
            buffer.clear();
            return outputString;
        }
        final int last = buffer.capacity() - buffer.remaining();
        if (last == 0)
        {
            buffer.put(bytes);
        } else
        {
            buffer.put(last, bytes, 0, bytes.length);
        }
        return null;
    }

    private boolean isEndOfStream(final byte[] bytes)
    {
        if (buffer.remaining() < ZLIB_FLUSH.length)
        {
            return false;
        }
        // check if the last 4 bytes signify to flush & return whole chunk
        final int pos = bytes.length - ZLIB_FLUSH.length;
        for (int i = pos; i <= bytes.length - 1; ++i)
        {
            final int flushByte = ZLIB_FLUSH.length - 1 - (bytes.length - 1 - i);
            if (bytes[i] != ZLIB_FLUSH[flushByte])
            {
                return false;
            }
        }
        return true;
    }
}
