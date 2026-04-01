package tech.ada.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface UaiService {

    @SystemMessage("You are a helpful assistant that answers questions about the city of Uberlândia, Brazil.")
    @UserMessage("Answer the following question: {message}")
    String ask(String message);
}
