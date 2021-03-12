package Tgbot.Bot.Handlers.VideoHandler;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Video;

public interface IVideoHandler {

      String saveVideo();
      String processVideo(Message incomeMsg);


}
