package Tgbot.Bot.Handlers.VideoHandler;


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

    private String apiURL;

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }
    //todo сделать бин со скоупом реквест и чтоб после завершения реквеста происходил дестрой метод очищающий все сохраненные вебмки
    //todo перенести вебм в отдельные каталоги
    @Override
    public String processVideo(Message incomeMsg) {

        logger.debug("begining processing video: {}", incomeMsg.getText());
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
            logger.debug("encoded video url: {}", encodedUrl);


            URL apiUrl = new URL(apiURL + encodedUrl);
            logger.debug("api request: {}", apiUrl.toString());

            ReadableByteChannel rbc = Channels.newChannel(apiUrl.openStream());

            String fileName = url.substring(url.lastIndexOf("/") + 1);
            FileOutputStream fos = new FileOutputStream(fileName + ".mp4");
            logger.debug("video saving to: {}", fileName + ".mp4");

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            logger.debug("video downloading completed!");
            fos.close();


            return "video: " + fileName + ".mp4";
        } catch (IOException e) {
            logger.error("SOME ERROR IN PROCESS VIDEO! {}", e);
        }
        return null;
    }


    @Override
    public String saveVideo() {
        return null;
    }
}
