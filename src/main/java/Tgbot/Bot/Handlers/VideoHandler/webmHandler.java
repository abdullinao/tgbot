package Tgbot.Bot.Handlers.VideoHandler;

import Tgbot.Bot.bot;
import Tgbot.externalAPIs.httpRequestor;
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
import java.util.Base64;

public class webmHandler implements IVideoHandler {
    private final Logger logger = LoggerFactory.getLogger(webmHandler.class);


    @Override
    public String processVideo(Message incomeMsg) {
//todo отправлять на вебм сервис урл видео в б64
        try {

//            logger.debug("starting webm process video");
//            URL url = new URL(incomeMsg.getText());
//            logger.debug("video url: {}", url);
//            logger.debug("opening stream");
//            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
//            String fileName = url.toString().substring(url.toString().lastIndexOf("/") + 1);
//            logger.debug("saving file to " + fileName);
//            FileOutputStream fos = new FileOutputStream(fileName);//+ ".mp4"
//            logger.debug("saving file 2");
//            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//            fos.close();
//
            String url = incomeMsg.getText();
            byte[] urlByte = url.getBytes();

            String encodedUrl = (new String(Base64.getEncoder().encode(urlByte)));
            URL apiUrl = new URL("http://ooo-idi-nahuy.keenetic.pro:5001/getfile/" + encodedUrl);
            ReadableByteChannel rbc = Channels.newChannel(apiUrl.openStream());

            String fileName = url.substring(url.lastIndexOf("/") + 1);
            FileOutputStream fos = new FileOutputStream(fileName + ".mp4");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();


            return fileName + ".mp4";// fileName + ".mp4";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public String saveVideo() {
        return null;
    }
}
