/*
    Sound loader for JOrbis' OGG Vorbis decoder. Requires PulpCore 0.11.5.

    To use:
    1. Get the JOrbis binary, jorbis-0.0.17.jar
       - Source is located here: http://www.jcraft.com/jorbis/
       - Binaries may also be found: http://www.google.com/search?q=jorbis+maven
    2. Drop jorbis-0.0.17.jar in your lib/ directory 
    3. Drop this file in your src/ directory.
       - Your IDE may prefer you put the file in src/pulpcore/sound/
    4. Load sounds like normal:

    Sound sound = Sound.load("mysound.ogg");

    Ogg Vorbis is fully integrated with PulpCore, so you can pause playback and set the
    level and pan in realtime, just like with regular Sounds.
*/

// NOTE: Don't change the package name! This class is called via reflection.
package pulpcore.sound;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import com.jcraft.jorbis.JOrbisException;
import pulpcore.animation.Fixed;
import pulpcore.Build;
import pulpcore.CoreSystem;
import pulpcore.util.ByteArray;

public class JOrbisAdapter extends Sound {

    /**
        The decompress threshold, in seconds. Sounds with a duration less than or equal to this
        value are fully decompressed when loaded. Sounds with a duration greater than this value
        are decompressed on the fly as they are played.
    */
    private static final float DECOMPRESS_THRESHOLD = 4;

    // NOTE: Don't change the method name! This method is called via reflection.
    public static Sound decode(ByteArray input, String soundAsset) {
        VorbisFile file;
        try {
            file = new VorbisFile(input.getData());
        }
        catch (JOrbisException ex) {
            if (Build.DEBUG) {
                CoreSystem.print("Couldn't load Ogg Vorbis file: " + soundAsset, ex);
            }
            return null;
        }

        if (!isSupportedFormat(soundAsset, file.getSampleRate(), file.getNumChannels())) {
            return null;
        }

        JOrbisAdapter clip = new JOrbisAdapter(soundAsset, file);
        if (file.getDuration() <= DECOMPRESS_THRESHOLD) {
            return clip.decompress();
        }
        else {
            return clip;
        }
    }

    private static boolean isSupportedFormat(String soundAsset, int sampleRate, int numChannels) {
        if (numChannels < 1 || numChannels > 2) {
            if (Build.DEBUG) {
                CoreSystem.print("Not a mono or stereo sound: " + soundAsset);
            }
            return false;
        }

        return true;
    }

    // Sound interface

    private String filename;
    private VorbisFile file;

    JOrbisAdapter(String filename, VorbisFile file) {
        super(file.getSampleRate());
        this.filename = filename;
        this.file = file;
    }

    public int getNumFrames() {
        return (file == null) ? 0 : file.getNumFrames();
    }

    public void getSamples(byte[] dest, int destOffset, int destChannels,
        int srcFrame, int numFrames)
    {
        boolean clearDest = false;
        if (file == null) {
            clearDest = true;
        }
        else {
            try {
                if (file.getFramePosition() != srcFrame) {
                    file.setFramePosition(srcFrame);
                }
                int frameSize = destChannels * 2;
                int framesRemaining = numFrames;
                while (framesRemaining > 0) {
                    int f = file.read(dest, destOffset, destChannels, framesRemaining);
                    if (f < 0) {
                        if (Build.DEBUG) {
                            CoreSystem.print("Couldn't fully decompress Ogg Vorbis file: " +
                                    filename);
                        }
                        for (int i = 0; i < framesRemaining*frameSize; i++) {
                            dest[destOffset++] = 0;
                        }
                        framesRemaining = 0;
                    }
                    else {
                        framesRemaining -= f;
                        destOffset += f * frameSize;
                    }
                }

                if (file.getFramePosition() == file.getNumFrames()) {
                    file.rewind();
                }
            }
            catch (Exception ex) {
                CoreSystem.setTalkBackField("pulpcore.sound-exception", ex);
                // Internal JOrbis problem - happens rarely. (Notably on IBM 1.4 VMs)
                // Kill JOrbis and start over.
                clearDest = true;

                byte[] data = file.data;
                file = null;
                try {
                    file = new VorbisFile(data);
                }
                catch (JOrbisException ex2) {
                    file = null;
                }
            }
        }

        if (clearDest) {
            int frameSize = getSampleSize() * destChannels;
            int length = numFrames * frameSize;
            for (int i = 0; i < length; i++) {
                dest[destOffset++] = 0;
            }
        }
    }

    public Sound decompress() {
        if (file == null) {
            return Sound.load(new byte[0], 8000, false);
        }
        else {
            byte[] dest = new byte[2 * file.getNumChannels() * file.getNumFrames()];
            getSamples(dest, 0, file.getNumChannels(), 0, file.getNumFrames());
            return Sound.load(dest, file.getSampleRate(), (file.getNumChannels() == 2));
        }
    }

    public Playback play(Fixed level, Fixed pan, boolean loop) {
        if (file == null) {
            return null;
        }
        else if (!file.isRunning()) {
            // Optimization for apps that never stop this sound
            return playImpl(level, pan, loop);
        }
        else {
            // Simultaneous playback - create a new copy (compressed data is shared between copies)
            return new JOrbisAdapter(filename, file.duplicate()).playImpl(level, pan, loop);
        }
    }

    private Playback playImpl(Fixed level, Fixed pan, boolean loop) {
        return super.play(level, pan, loop);
    }

    public String toString() {
        return filename;
    }

    /*
        VorbisFile
        This static class does not use any PulpCore classes and could be used in other projects.
        Based on code from JOrbis, but without chained Ogg support.
    */
    public static class VorbisFile {

        private static final int CHUNK_SIZE = 4096;
        private static final int OV_FALSE = -1;
        private static final int OV_EOF = -2;

        private final byte[] data;
        private int dataStartOffset;
        private int dataEndOffset;
        private int dataPosition;

        private int nextPageOffset = 0;

        private final Info info;
        private final Comment comment;
        private int serialno;
        private int numFrames;
        private int framePosition;
        private boolean decodeReady;

        private final SyncState oy = new SyncState();
        private final StreamState os = new StreamState();
        private final DspState vd = new DspState();
        private final Block vb = new Block(vd);

        private int[] _index;
        private float[][][] _pcm = new float[1][][];

        private VorbisFile(VorbisFile file) {
            this.data = file.data;
            this.info = file.info;
            this.comment = file.comment;
            this.serialno = file.serialno;
            this.numFrames = file.numFrames;
            this.framePosition = 0;
            this.decodeReady = false;
            slice(file.dataStartOffset, file.dataEndOffset);
            this._index = new int[info.channels];
        }

        public VorbisFile(byte[] data) throws JOrbisException {
            this(data, 0, data.length);
        }

        public VorbisFile(byte[] data, int offset, int length) throws JOrbisException {
            this.data = data;
            this.info = new Info();
            this.comment = new Comment();
            slice(offset, offset + length);
            open();
        }

        /**
            Create a copy of this VorbisFile. The returned file will share the same data. This is
            useful for playing multiple copies of the same file simultaneously.
        */
        public VorbisFile duplicate() {
            return new VorbisFile(this);
        }

        public void rewind() {
            this.framePosition = 0;
            this.decodeReady = false;
            this.dataPosition = dataStartOffset;
            this.nextPageOffset = dataPosition;
        }

        public boolean isRunning() {
            return decodeReady;
        }

        // Data buffer methods

        private int remaining() {
            return dataEndOffset - dataPosition;
        }

        private int get(byte[] dest, int offset, int length) {
            if (length > remaining()) {
                length = remaining();
            }
            System.arraycopy(data, dataPosition, dest, offset, length);
            dataPosition += length;
            return length;
        }

        private void slice(int startOffset, int endOffset) {
            dataStartOffset = startOffset;
            dataEndOffset = endOffset;
            dataPosition = startOffset;
            nextPageOffset = startOffset;
        }

        // Public methods (file info)

        public int getSampleRate() {
            return info.rate;
        }

        public int getNumChannels() {
            return info.channels;
        }

        public int getNumFrames() {
            return numFrames;
        }

        public Comment getComment() {
            return comment;
        }

        public float getDuration() {
            return getNumFrames() / (float)getSampleRate();
        }

        public int getFramePosition() {
            return framePosition;
        }

        // Private methods

        private void open() throws JOrbisException {
            getHeaders();
            os.clear();
            getEnd();
            this._index = new int[info.channels];
        }

        private void getHeaders() throws JOrbisException {
            Page og = new Page();
            Packet op = new Packet();
            if (getNextPage(og, CHUNK_SIZE) < 0) {
                throw new JOrbisException();
            }

            this.serialno = og.serialno();
            os.init(serialno);
            info.init();
            comment.init();

            int i = 0;
            while (i < 3) {
                os.pagein(og);
                while (i < 3) {
                    int result = os.packetout(op);
                    if (result == 0) {
                        break;
                    }
                    else if (result == -1) {
                        throw new JOrbisException();
                    }
                    else if (info.synthesis_headerin(comment, op) != 0) {
                        throw new JOrbisException();
                    }
                    i++;
                }
                if (i < 3 && getNextPage(og, 1) < 0) {
                    throw new JOrbisException();
                }
            }
        }

        private void getEnd() throws JOrbisException {
            Page og = new Page();
            int startOffset = nextPageOffset;
            int endOffset = dataEndOffset;
            numFrames = -1;

            while (true) {
                int ret = getNextPage(og, CHUNK_SIZE);
                if (ret == OV_EOF) {
                    endOffset = nextPageOffset;
                    break;
                }
                else if (ret < 0) {
                    throw new JOrbisException();
                }
                else if (serialno == og.serialno()) {
                    if (og.granulepos() != -1) {
                        numFrames = (int)og.granulepos();
                    }
                    if (og.eos() != 0) {
                        endOffset = nextPageOffset;
                        break;
                    }
                }
                else {
                    endOffset = ret;
                    break;
                }
            }

            if (numFrames < 0) {
                throw new JOrbisException();
            }

            oy.reset();

            slice(startOffset, endOffset);
        }

        /**
            On success, nextPageOffset is set.
            @return negative value on error; otherwise, the offset of the start of the page.
        */
        private int getNextPage(Page page, int boundary) {
            if (boundary > 0) {
                boundary += nextPageOffset;
            }
            while (true) {
                if (boundary > 0 && nextPageOffset >= boundary) {
                    return OV_FALSE;
                }
                int more = oy.pageseek(page);
                if (more < 0) {
                    nextPageOffset -= more;
                }
                else if (more == 0) {
                    if (boundary == 0) {
                        return OV_FALSE;
                    }
                    int index = oy.buffer(CHUNK_SIZE);
                    int bytes = get(oy.data, index, CHUNK_SIZE);
                    oy.wrote(bytes);
                    if (bytes == 0) {
                        return OV_EOF;
                    }
                }
                else {
                    int ret = nextPageOffset;
                    nextPageOffset += more;
                    return ret;
                }
            }
        }

        /**
            @return -1 for lost packet, 0 not enough data, or 1 for success
        */
        private int processPacket(boolean readPage) {
            Page og = new Page();

            while (true) {
                if (decodeReady) {
                    Packet op = new Packet();
                    int result = os.packetout(op);
                    if (result > 0) {
                        int granulepos = (int)op.granulepos;
                        if (vb.synthesis(op) == 0) {
                            vd.synthesis_blockin(vb);
                            if (granulepos != -1 && op.e_o_s == 0) {
                                int samples = vd.synthesis_pcmout(null, null);
                                granulepos -= samples;
                                framePosition = granulepos;
                            }
                            return(1);
                        }
                    }
                }

                if (!readPage || getNextPage(og, -1) < 0) {
                    return(0);
                }

                if (!decodeReady) {
                    os.init(serialno);
                    os.reset();
                    vd.synthesis_init(info);
                    vb.init(vd);
                    decodeReady = true;
                }
                os.pagein(og);
            }
        }

        // Public read methods

        public void setFramePosition(int newFramePosition) {
            if (newFramePosition < 0) {
                newFramePosition = 0;
            }
            else if (newFramePosition > numFrames) {
                newFramePosition = numFrames;
            }

            if (newFramePosition < framePosition) {
                rewind();
            }

            int framesToSkip = newFramePosition - framePosition;
            while (framesToSkip > 0) {
                int f = skip(framesToSkip);
                if (f < 0) {
                    return;
                }
                else {
                    framesToSkip -= f;
                }
            }
        }

        /**
            @return number of frames skipped, or -1 on error.
        */
        public int skip(int numFrames) {
            while (true) {
                if (decodeReady) {
                    int frames = vd.synthesis_pcmout(_pcm, _index);
                    if (frames != 0) {
                        int channels = info.channels;
                        if (frames > numFrames) {
                            frames = numFrames;
                        }
                        vd.synthesis_read(frames);
                        framePosition += frames;
                        return frames;
                    }
                }

                if (processPacket(true) <= 0) {
                    return -1;
                }
            }
        }

        /**
            @param dest destination buffer
            @param destOffset offset in the destination buffer
            @param destChannels number of channels in the destination (either 1 or 2).
            @param numFrames number of frames to read.
            @return number of frames read, or -1 on error. Always fails if this Vorbis file has more
            than two channels.
        */
        public int read(byte[] dest, int destOffset, int destChannels, int numFrames) {
            while (true) {
                if (decodeReady) {
                    int frames = vd.synthesis_pcmout(_pcm, _index);
                    if (frames != 0) {
                        int channels = info.channels;
                        if (frames > numFrames) {
                            frames = numFrames;
                        }

                        // Convert to signed, little endian, 16-bit PCM format.
                        if (destChannels == channels) {
                            // Mono-to-mono or stereo-to-stereo
                            int frameSize = 2 * channels;
                            for (int i = 0; i < channels; i++) {
                                int ptr = destOffset + 2 * i;
                                int mono = _index[i];
                                float[] pcm_row = _pcm[0][i];
                                for (int j = 0; j < frames; j++) {
                                    int sample = (int) (pcm_row[mono + j] * 32767);
                                    if (sample > 32767) {
                                        sample = 32767;
                                    }
                                    else if (sample < -32768) {
                                        sample = -32768;
                                    }

                                    dest[ptr] = (byte)sample;
                                    dest[ptr + 1] = (byte)(sample >> 8);
                                    ptr += frameSize;
                                }
                            }
                        }
                        else if (channels == 1 && destChannels == 2) {
                            // Mono-to-stereo
                            int ptr = destOffset;
                            int mono = _index[0];
                            float[] pcm_row = _pcm[0][0];
                            for (int j = 0; j < frames; j++) {
                                int sample = (int)(pcm_row[mono + j] * 32767);
                                if (sample > 32767) {
                                    sample = 32767;
                                }
                                else if (sample < -32768) {
                                    sample = -32768;
                                }

                                byte a = (byte)sample;
                                byte b = (byte)(sample >> 8);
                                dest[ptr++] = a;
                                dest[ptr++] = b;
                                dest[ptr++] = a;
                                dest[ptr++] = b;
                            }
                        }
                        else if (destChannels == 1) {
                            // Mix all channels to 1 (not tested)
                            for (int j = 0; j < frames * 2; j++) {
                                dest[destOffset + j] = 0;
                            }

                            for (int i = 0; i < channels; i++) {
                                int ptr = destOffset;
                                int mono = _index[i];
                                float[] pcm_row = _pcm[0][i];
                                for (int j = 0; j < frames; j++) {
                                    int oldSample = (dest[ptr] & 0xff) | (dest[ptr+1] << 8);
                                    int sample = (int)(oldSample +
                                        pcm_row[mono + j] * 32767 / channels);
                                    if (sample > 32767) {
                                        sample = 32767;
                                    }
                                    else if (sample < -32768) {
                                        sample = -32768;
                                    }

                                    dest[ptr++] = (byte)sample;
                                    dest[ptr++] = (byte)(sample >> 8);
                                }
                            }
                        }
                        else {
                            return -1;
                        }

                        vd.synthesis_read(frames);
                        framePosition += frames;
                        return frames;
                    }
                }

                if (processPacket(true) <= 0) {
                    return -1;
                }
            }
        }
    }
}
