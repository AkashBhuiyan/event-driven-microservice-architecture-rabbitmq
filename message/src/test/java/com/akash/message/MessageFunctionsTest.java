package com.akash.message;

/**
 * Author: akash
 * Date: 5/2/25
 */
import com.akash.message.dto.AccountsMsgDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.cloud.function.definition=email;sms")
@Import(TestChannelBinderConfiguration.class) // Import the test binder configuration
public class MessageFunctionsTest {

    @Autowired
    private InputDestination inputDestination; // Used to send messages to the input channel

    @Autowired
    private OutputDestination outputDestination; // Used to receive messages from the output channel

    @Test
    public void testEmailFunction() throws IOException {

        AccountsMsgDto input = new AccountsMsgDto(123456L, "Akash", "akash@example.com", "1234567890");

        inputDestination.send(MessageBuilder.withPayload(input).build(), "email-in-0"); // Send the message to the input channel (email function)

        Message<byte[]> outputMessage = outputDestination.receive(1000, "email-out-0"); // Receive the message from the output channel

        assertNotNull(outputMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        AccountsMsgDto output = objectMapper.readValue(outputMessage.getPayload(), AccountsMsgDto.class);

        // Assert that the output matches the input
        assertEquals(input, output);
    }

    @Test
    public void testSmsFunction() {

        AccountsMsgDto input = new AccountsMsgDto(123456L, "Akash", "akash@example.com", "1234567890");

        inputDestination.send(MessageBuilder.withPayload(input).build(), "sms-in-0"); // Send the message to the input channel (sms function)

        Message<byte[]> outputMessage = outputDestination.receive(1000, "sms-out-0"); // Receive the message from the output channel

        assertNotNull(outputMessage);

        Long output = Long.parseLong(new String(outputMessage.getPayload())); // Deserialize the output message payload

        assertEquals(input.accountNumber(), output);
    }
}
