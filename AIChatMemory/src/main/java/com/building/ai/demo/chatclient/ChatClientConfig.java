package com.building.ai.demo.chatclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

	String DEFAULT_CONVERSATION_ID = "default";
	
	
	
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
    	
//    	        return chatClientBuilder
//    	                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
//    	                .build();

//    	        return chatClientBuilder
//                 .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
//                 .build();
    	
    	return chatClientBuilder
        .defaultAdvisors(
            new MessageChatMemoryAdvisor(new InMemoryChatMemory())) // 
        .build();
    	
    	return chatClientBuilder
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
//        return chatClientBuilder
//                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
//                .build();
    
//    	var chatClient = ChatClient.builder(chatClientBuilder)
//    	return chatClientBuilder.defaultAdvisors(
//    		        new MessageChatMemoryAdvisor.builder(new ChatMemory()) // chat-memory advisor
//    		        QuestionAnswerAdvisor.builder((vectorStore).builder() // RAG advisor
//    		    )
//    		    .build();
    	
//    	return chatClientBuilder
//              .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
//              .build();
//    	 chatClientBuilder.clone()
//         .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
//         .build();
    }

    @Bean
    public ChatMemory chatMemory() {
    	MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
    	        .maxMessages(10) // Configure the maximum number of messages to remember
    	        .build();
        return chatMemory;
    }
    
//	 @Bean
//	  ChatMemory chatMemory() {
//	    return new InMemoryChatMemory(); // Use in-memory chat memory
//	  }
//
//	  @Bean
//	  MessageChatMemoryAdvisor messageChatMemoryAdvisor(ChatMemory chatMemory) {
//	    return new MessageChatMemoryAdvisor(chatMemory); // create the advisor
//	  }

	  @Bean
	  ChatClient chatClient(ChatClient.Builder builder, MessageChatMemoryAdvisor advisor) {
	    return builder.defaultAdvisors(advisor).build(); // create a ChatClient
	  }
	  
	  /*
	    // === PROPER SPRING AI MEMORY SETUP ===
	    
	    @Bean
	    public ChatMemoryRepository chatMemoryRepository() {
	        return new InMemoryChatMemoryRepository();
	    }
	    
	    @Bean 
	    public ChatMemory chatMemory(ChatMemoryRepository repository) {
	        return new MessageWindowChatMemory(repository, 10); // Keep last 10 messages
	    }
	    
	    @Bean
	    public ChatClient chatClientWithMemory(ChatMemory chatMemory) {
	        return ChatClient.builder(chatModel)
	            .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
	            .build();
	    }
	    
	    // === USAGE ===
	    public String chatWithAutoMemory(String conversationId, String message) {
	        return chatClientWithMemory.prompt()
	            .advisors(a -> a.param("conversation_id", conversationId))
	            .user(message)
	            .call()
	            .content();
	    }
	    
	    // === BENEFITS ===
	    
	    1. **Automatic Context Management**: No manual prompt building
	    2. **Memory Limits**: Configurable message window prevents token overflow
	    3. **Session Management**: Built-in conversation_id handling
	    4. **Thread Safety**: Production-ready concurrent access
	    5. **Performance**: Optimized for large conversations
	    
	    // === WHY IT'S BETTER THAN CUSTOM IMPLEMENTATION ===
	    
	    ❌ Custom Implementation Problems:
	    - Manual message concatenation
	    - No memory limits (grows indefinitely)
	    - Manual conversation_id management
	    - Thread safety concerns
	    - No token counting
	    
	    ✅ Spring AI Memory Benefits:
	    - Automatic integration with ChatClient
	    - Built-in memory window management
	    - Optimized for LLM token limits
	    - Production-ready architecture
	    - Consistent with Spring AI patterns
	    
	    */
	    
	    /**
	     * Why my custom implementation was problematic:
	     * 
	     * 1. **No Message Window**: My SimpleChatMemory stored ALL messages forever
	     *    - Spring AI's MessageWindowChatMemory keeps only recent messages
	     *    - Prevents token limit exceeded errors
	     *    - Configurable window size (e.g., last 10 exchanges)
	     * 
	     * 2. **Manual Integration**: I manually built Prompt with history
	     *    - Spring AI's MessageChatMemoryAdvisor handles this automatically
	     *    - Just use .advisors(a -> a.param("conversation_id", "session"))
	     * 
	     * 3. **No Token Management**: My implementation could exceed model limits
	     *    - Spring AI memory components track token usage
	     *    - Automatically trim old messages when approaching limits
	     * 
	     * 4. **Repository Pattern**: Spring AI separates storage from logic
	     *    - InMemoryChatMemoryRepository for simple use cases
	     *    - Can swap to RedisChatMemoryRepository for production
	     *    - Consistent interface regardless of storage backend
	     */
	}
}