package pl.hycom.pip.messanger.handler;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.github.messenger4j.send.MessengerSendClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pl.hycom.pip.messanger.service.ProductService;

/**
 * Created by patry on 09/03/2017.
 */
@Log4j2
@Component
public class ReturnThreeProductsHandler implements TextMessageEventHandler {

	private MessengerSendClient sendClient;
	private ProductService productService;

	public ReturnThreeProductsHandler(MessengerSendClient sendClient) {
		this.sendClient = sendClient;
	}

	@Override
	public void handle(TextMessageEvent msg) {

		sendTextMessage(msg.getSender().getId(), "test handlera");
	}

	private void sendTextMessage(String id, String message) {
		log.info("Sending message[" + message + "] to[" + id + "]");

		try {
			sendClient.sendTextMessage(id, message);
		} catch (MessengerApiException | MessengerIOException e) {
			log.error("Error during sending answer", e);
		}
	}
}
