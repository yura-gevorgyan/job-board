package am.itspace.jobboard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver defaultContentTypeResolver = new DefaultContentTypeResolver();
        defaultContentTypeResolver.setDefaultMimeType(APPLICATION_JSON);
        MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setObjectMapper(new ObjectMapper());
        mappingJackson2MessageConverter.setContentTypeResolver(defaultContentTypeResolver);
        messageConverters.add(mappingJackson2MessageConverter);
        return false;
    }
}
