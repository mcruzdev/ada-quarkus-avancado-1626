package org.acme.ai;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = {BankTools.class})
public interface BankUaiService {

    @SystemMessage("You are a helpful bank assistant")
    String ask(String message);
}
