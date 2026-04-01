package org.acme.ai;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class BankTools {

    @Tool(name = "transferMoney")
    public String transfer(@P("the sender's name or account") String from,
                           @P("the recipient's name or account") String to,
                           @P("the amount to transfer") String amount) {
        Log.info("Transfer from: " + from + " to: " + to + " amount: " + amount);

        // logic
        return "Money transferred successfully";
    }

    @Tool("listAllAccounts")
    public List<String> accounts() {
        return List.of("Milena", "Tiago", "Ruy", "Matheus");
    }
}
