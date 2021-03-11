package Tgbot.Bot.Handlers.VideoHandler;

import Tgbot.Bot.bot;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class webmHandler implements IVideoHandler {
    private final Logger logger = LoggerFactory.getLogger(webmHandler.class);


    @Override
    public String processVideo() {
//todo отправлять на вебм сервис урл видео в б64
        try {
            Message incomeMsg = null;
            logger.debug("starting webm process video");
            URL url = new URL(incomeMsg.getText());
            logger.debug("video url: {}", url);
            logger.debug("opening stream");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            String fileName = url.toString().substring(url.toString().lastIndexOf("/") + 1);
            logger.debug("saving file to " + fileName);
            FileOutputStream fos = new FileOutputStream(fileName);//+ ".mp4"
            logger.debug("saving file 2");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            return convert(fileName);// fileName + ".mp4";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String convert(String fileName) throws IOException {
        logger.debug("ffmpeg initiated");
        FFmpeg ffmpeg = new FFmpeg("/ffmpeg/ffmpeg");
        FFprobe ffprobe = new FFprobe("/ffmpeg/ffprobe");
        logger.debug("done fftools");
        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput(fileName)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput("output.mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                //  .setTargetSize(250_000)  // Aim for a 250KB file

                //   .disableSubtitle()       // No subtiles

                .setAudioChannels(1)         // Mono audio
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(48_000)  // at 48KHz
                .setAudioBitRate(32768)      // at 32 kbit/s

                .setVideoCodec("libx264")     // Video using x264
                //   .setVideoFrameRate(24, 1)     // at 24 frames per second
                //  .setVideoResolution(640, 480) // at 640x480 resolution

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();
        logger.debug("done builder");
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

// Run a one-pass encode
        executor.createJob(builder).run();

// Or run a two-pass encode (which is better quality at the cost of being slower)
        executor.createTwoPassJob(builder).run();

        return "null";
    }

    @Override
    public String saveVideo() {
        return null;
    }
}
