package pulpcore.sound;

import com.jcraft.jorbis.JOrbisException;
import pulpcore.animation.Fixed;
import pulpcore.Build;
import pulpcore.CoreSystem;
import pulpcore.util.ByteArray;

/**

 Sound loader for JOrbis' OGG Vorbis decoder. Requires PulpCore 0.12.0.

 This class is called via reflection from PulpCore. It exists as a separate project so that
 projects that don't use Ogg Vorbis have a smaller resulting jar file.
 When this class is included, Ogg Vorbis sounds can be loaded like normal:

 <p><code>Sound sound = Sound.load("mysound.ogg");</code></p>

 Ogg Vorbis is fully integrated with PulpCore, so you can pause playback and set the
 level and pan in realtime, just like with regular Sounds.

 */
public class JOrbisAdapter extends Sound {

    /**
        The decompress threshold, in seconds. Sounds with a duration less than or equal to this
        value are fully decompressed when loaded. Sounds with a duration greater than this value
        are decompressed on the fly as they are played.
    */
    private static final float DECOMPRESS_THRESHOLD = 4;

    private static boolean needsWarmup = true;

    // NOTE: This method is called via reflection.
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
            needsWarmup = false;
            return clip.decompress();
        }
        else {
            if (needsWarmup) {
                needsWarmup = false;
                // Decompress a small amount to warmup HotSpot
                new JOrbisAdapter(clip).warmup();
            }
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

    JOrbisAdapter(JOrbisAdapter src) {
        super(src.file.getSampleRate());
        this.filename = src.filename;
        this.file = src.file;
        // setSimultaneousPlaybackCount(src.getSimultaneousPlaybackCount());
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

                byte[] data = file.getData();
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

    // Warmup for HotSpot
    void warmup() {
        if (file != null) {
            int frames = Math.min(4096, file.getNumFrames());
            byte[] dest = new byte[2 * file.getNumChannels() * frames];
            getSamples(dest, 0, file.getNumChannels(), 0, frames);
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
            return new JOrbisAdapter(this).playImpl(level, pan, loop);
        }
    }

    private Playback playImpl(Fixed level, Fixed pan, boolean loop) {
        return super.play(level, pan, loop);
    }

    public boolean equals(Object obj) {
        return (obj instanceof JOrbisAdapter) && file != null && file.equals(((JOrbisAdapter)obj).file);
    }

    public String toString() {
        return filename;
    }
}