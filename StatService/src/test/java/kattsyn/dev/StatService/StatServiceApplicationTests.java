package kattsyn.dev.StatService;

import kattsyn.dev.StatService.dtos.EventCreationDto;
import kattsyn.dev.StatService.entities.Event;
import kattsyn.dev.StatService.enums.Events;
import kattsyn.dev.StatService.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class StatServiceApplicationTests {

	@Autowired
	public EventService eventService;


    @Test
	void contextLoads() {
	}

	@Test
	void addEventTest() {
		EventCreationDto event = new EventCreationDto(Events.EVENT_LIKE, 1L, 1L);
		eventService.save(event);

		System.out.println(eventService.findAll());
	}

}
