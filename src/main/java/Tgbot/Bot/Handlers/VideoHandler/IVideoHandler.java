package Tgbot.Bot.Handlers.VideoHandler;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface IVideoHandler {

      String saveVideo();
      String processVideo(Message incomeMsg);


}
