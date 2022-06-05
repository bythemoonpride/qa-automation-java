import com.tcs.edu.domain.Message;
import com.tcs.edu.project_enum.Severity;
import com.tcs.edu.repository.HashMapMessageRepository;
import com.tcs.edu.repository.MessageRepository;
import com.tcs.edu.service.MessageService;
import com.tcs.edu.service.OrderedDistinctedMessageService;
import com.tcs.edu.service.ProcessException;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.UUID;

import static com.tcs.edu.project_enum.Severity.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


public class MessageServiceTest {

    MessageRepository messageRepository;
    MessageService messageService;

    @BeforeEach
    public void init() {
        messageRepository = new HashMapMessageRepository();
        messageService = new OrderedDistinctedMessageService(messageRepository);
    }

    @Nested
    @DisplayName("Tests for create method")
    class CreateMethodTests {

        @Test
        @DisplayName("Creating message in repository")
        public void createNewMessageInRepository() {
            Message message = new Message(Severity.MINOR, "Hello, world!");
            UUID messageId = messageService.create(message);
            Assertions.assertTrue(messageRepository.findAll().size() == 1);
            Assertions.assertEquals(message, messageRepository.findById(messageId));
        }
    }

    @Nested
    @DisplayName("Tests for get methods")
    class GetMethodTests {
        @Test
        @DisplayName("Get message by id")
        public void getMessageById() {
            Message message1 = new Message(MINOR, "HelloMINOR");
            UUID messageId = messageRepository.create(message1);

            Message foundedMessage = messageService.findById(messageId);

            assertThat(foundedMessage, is(message1));
        }

        @Test
        @DisplayName("Get message by null id")
        public void getMessageByNullId() {
            Message message1 = new Message(MINOR, "HelloMINOR");
            messageRepository.create(message1);

            Message foundedMessage = messageService.findById(null);

            assertThat(foundedMessage, equalTo(null));
        }

        @Test
        @DisplayName("Get message by severity")
        public void getMessageBySeverity() {
            Message message1 = new Message(MINOR, "HelloMINOR");
            Message message2 = new Message(REGULAR, "HelloREGULAR");
            Message message3 = new Message(MAJOR, "HelloMAJOR");

            messageRepository.create(message1);
            messageRepository.create(message2);
            messageRepository.create(message3);

            Collection<Message> messages = messageService.get(REGULAR);

            assertThat(messages, hasSize(1));
            assertThat(messages,
                    allOf(
                            hasItem(message2),
                            not(hasItem(message1)),
                            not(hasItem(message3))
                    ));
        }

        @Test
        @DisplayName("Get message by body")
        public void getMessageByBody() {
            Message message1 = new Message(MINOR, "HelloMINOR");
            Message message2 = new Message(REGULAR, "HelloREGULAR");
            Message message3 = new Message(MAJOR, "HelloMAJOR");

            messageRepository.create(message1);
            messageRepository.create(message2);
            messageRepository.create(message3);

            Collection<Message> messages = messageService.get("HelloREGULAR");

            assertThat(messages, hasSize(1));
            assertThat(messages,
                    allOf(
                            hasItem(message2),
                            not(hasItem(message1)),
                            not(hasItem(message3))
                    ));
        }

        @Test
        @DisplayName("Get message by severity and body")
        public void getMessageBySeverityAndBody() {
            Message message1 = new Message(MINOR, "Hello");
            Message message2 = new Message(REGULAR, "Hello");
            Message message3 = new Message(MAJOR, "Hello");

            messageRepository.create(message1);
            messageRepository.create(message2);
            messageRepository.create(message3);

            Collection<Message> messages = messageService.get(REGULAR, "Hello");

            assertThat(messages, hasSize(1));
            assertThat(messages,
                    allOf(
                            hasItem(message2),
                            not(hasItem(message1)),
                            not(hasItem(message3))
                    ));
        }

        @Test
        @DisplayName("Get message by null severity")
        public void getMessageByNullSeverity() {
            Message message1 = new Message(MINOR, "HelloMINOR");
            Message message2 = new Message(REGULAR, "HelloREGULAR");
            Message message3 = new Message(MAJOR, "HelloMAJOR");

            messageRepository.create(message1);
            messageRepository.create(message2);
            messageRepository.create(message3);

            Collection<Message> messages = messageService.get((Severity) null);

            assertThat(messages, hasSize(0));

        }

        @Test
        @DisplayName("Get message by null body")
        public void getMessageByNullBody() {
            Message message1 = new Message(MINOR, "HelloMINOR");
            Message message2 = new Message(REGULAR, "HelloREGULAR");
            Message message3 = new Message(MAJOR, "HelloMAJOR");

            messageRepository.create(message1);
            messageRepository.create(message2);
            messageRepository.create(message3);

            Collection<Message> messages = messageService.get((String) null);

            assertThat(messages, hasSize(0));

        }

        @Test
        @DisplayName("Get method throws exception when severity is null")
        public void getThrowsExceptionWhenSeverityIsNull() {
            Assertions.assertThrows(ProcessException.class, () -> messageService.get(null, "body"));
        }

        @Test
        @DisplayName("Get method throws exception when body is null")
        public void getThrowsExceptionWhenBodyIsNull() {
            Assertions.assertThrows(ProcessException.class, () -> messageService.get(MINOR, null));
        }

        @Test
        @DisplayName("Get all messages")
        public void getAllMessages() {
            Message message1 = new Message(MINOR, "HelloMINOR");
            Message message2 = new Message(REGULAR, "HelloREGULAR");
            Message message3 = new Message(MAJOR, "HelloMAJOR");

            messageRepository.create(message1);
            messageRepository.create(message2);
            messageRepository.create(message3);

            Collection<Message> messages = messageService.get();

            assertThat(messages, hasSize(3));
            assertThat(messages,
                    allOf(
                            hasItem(message1),
                            hasItem(message2),
                            hasItem(message3)
                    ));
        }

    }

    @Nested
    @DisplayName("Tests for put method")
    class PutMethodTests {

        @Test
        @DisplayName("put message in repository")
        public void putMessage() {
            Message messageBeforeUpdate = new Message(MINOR, "I'll be updated one day");
            Message messageAfterUpdate = new Message(MAJOR, "Now i am updated!!!");
            UUID messageId = messageRepository.create(messageBeforeUpdate);
            messageAfterUpdate.setId(messageId);

            messageService.put(messageAfterUpdate);

            assertThat(messageRepository.findById(messageId), is(messageAfterUpdate));
        }

        @Test
        @DisplayName("put message when id doesn't exists in repository")
        public void putMessageWhenIdDoesntExistsInRepository() {
            Message message = new Message(MINOR, "I am message");
            Message messageAfterUpdate = new Message(MAJOR, "Now i am updated!!!");

            UUID messageId = messageRepository.create(message);
            UUID randomId = UUID.randomUUID();
            messageAfterUpdate.setId(randomId);
            Message lastUpdatedMessage = messageService.put(messageAfterUpdate);

            assertThat(messageRepository.findById(messageId), is(message));
            assertThat(messageRepository.findAll(), hasSize(1));
            assertThat(lastUpdatedMessage, nullValue());
            assertThat(messageId, not(is(randomId)));

        }

        @Test
        @DisplayName("put message when id is null")
        public void putMessageWhenIdIsNull() {
            Message message = new Message(MINOR, "I am message");
            Message messageAfterUpdate = new Message(MAJOR, "Now i am updated!!!");

            UUID messageId = messageRepository.create(message);

            Message lastUpdatedMessage = messageService.put(messageAfterUpdate);

            assertThat(messageRepository.findById(messageId), is(message));
            assertThat(messageRepository.findAll(), hasSize(1));
            assertThat(lastUpdatedMessage, nullValue());

        }

    }

    @Nested
    @DisplayName("Tests for delete method")
    class DeleteMethodTests {

        @Test
        @DisplayName("delete message in repository")
        public void deleteMessage() {
            Message messageForDeleting = new Message(MINOR, "I'll be deleted one day");

            UUID messageId = messageRepository.create(messageForDeleting);

            Message deletedMessage = messageService.delete(messageId);

            assertThat(messageRepository.findAll(), hasSize(0));
            assertThat(deletedMessage, is(messageForDeleting));
        }

        @Test
        @DisplayName("delete message when id doesn't exists in repository")
        public void deleteMessageWhenIdDoesntExistsInRepository() {
            Message message = new Message(MINOR, "I am message");

            messageRepository.create(message);
            UUID randomId = UUID.randomUUID();

            Message lastDeletedMessage = messageService.delete(randomId);

            assertThat(messageRepository.findAll(), hasSize(1));
            assertThat(lastDeletedMessage, nullValue());

        }

        @Test
        @DisplayName("delete message when id is null")
        public void deleteMessageWhenIdIsNull() {
            Message message = new Message(MINOR, "I am message");

            messageRepository.create(message);

            Message lastDeletedMessage = messageService.delete(null);

            assertThat(messageRepository.findAll(), hasSize(1));
            assertThat(lastDeletedMessage, nullValue());

        }

    }
}
