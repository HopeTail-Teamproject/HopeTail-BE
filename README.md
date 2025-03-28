---

# HopeTail 채팅 API 문서

## 개요
이 API는 Spring Boot 기반의 WebSocket과 STOMP 프로토콜을 사용하여 실시간 채팅 기능(그룹 채팅 및 1:1 채팅)을 제공합니다.  
프론트엔드에서는 SockJS와 STOMP JS 라이브러리를 사용해 이 API에 연결하여, 메시지 전송 및 수신 기능을 구현할 수 있습니다.

---

## 1. WebSocket 연결

### 엔드포인트
- **URL:** `/ws-stomp`
- **설명:** 이 엔드포인트는 SockJS를 사용해 WebSocket 연결을 수립합니다.
- **예시 (JavaScript):**
  ```js
  var socket = new SockJS('/ws-stomp');
  ```

### WebSocket 설정 (서버 측)
WebSocket 설정은 [WebSocketConfig.java](src/main/java/hello/hello_spring/config/WebSocketConfig.java)에서 다음과 같이 구성되어 있습니다:
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 그룹 메시지 구독 경로
        registry.enableSimpleBroker("/topic", "/queue");
        // 클라이언트가 보낼 때 사용하는 prefix
        registry.setApplicationDestinationPrefixes("/app");
        // 사용자 전용 destination 접두사 (1:1 채팅용)
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
```

---

## 2. 메시지 전송 및 구독

### 2.1 그룹 채팅

- **메시지 전송 (그룹)**
    - **클라이언트가 보낼 destination:** `/app/chat/message`
    - **서버 처리:**  
      [ChatController.java](src/main/java/hello/hello_spring/controller/ChatController.java)
      ```java
      @Controller
      public class ChatController {
  
          @MessageMapping("/chat/message")
          @SendTo("/topic/chat")
          public ChatMessage handleMessage(ChatMessage message) {
              // 필요한 로직 수행 (예: DB 저장 등)
              return message; // 모든 구독자에게 브로드캐스트
          }
      }
      ```
- **메시지 구독 (그룹)**
    - **클라이언트가 구독할 destination:** `/topic/chat`
    - **클라이언트 예제 (HTML/JS)**
      ```js
      stompClient.subscribe('/topic/chat', function(message) {
        var msgObj = JSON.parse(message.body);
        document.getElementById("chatLogs").innerHTML +=
           "<p><b>Group Received (" + msgObj.senderId + ")</b>: " + msgObj.content + "</p>";
      });
      ```

### 2.2 1:1 (개인) 채팅

1:1 채팅을 위해서는 서버에서 **특정 사용자에게만 메시지를 전송**해야 합니다. 이를 위해 별도의 컨트롤러(예: PrivateChatController)를 사용합니다.

- **메시지 전송 (개인)**
    - **클라이언트가 보낼 destination:** `/app/chat/private`
    - **메시지 형식 예시 (JSON):**
      ```json
      {
        "chatRoomId": "room1",
        "senderId": "user123",
        "receiverId": "user456",
        "content": "Hello, how are you?"
      }
      ```
- **서버 처리 (PrivateChatController)**
    - 서버에서는 SimpMessagingTemplate을 사용해, `convertAndSendToUser()`로 특정 사용자에게 메시지를 전달합니다.
    - 예시:
      ```java
      @Controller
      public class PrivateChatController {
  
          private final SimpMessagingTemplate messagingTemplate;
  
          @Autowired
          public PrivateChatController(SimpMessagingTemplate messagingTemplate) {
              this.messagingTemplate = messagingTemplate;
          }
  
          @MessageMapping("/chat/private")
          public void handlePrivateMessage(ChatMessage message) {
              // 예: message 객체에 receiverId 필드가 있다고 가정.
              messagingTemplate.convertAndSendToUser(
                  message.getReceiverId(), // 수신자의 로그인 ID 또는 식별자
                  "/queue/private",        // 사용자 전용 구독 경로
                  message
              );
          }
      }
      ```
- **메시지 구독 (개인)**
    - **클라이언트가 구독할 destination:** `/user/queue/private`
    - **클라이언트 예제 (HTML/JS)**
      ```js
      stompClient.subscribe('/user/queue/private', function(message) {
        var msgObj = JSON.parse(message.body);
        document.getElementById("chatLogs").innerHTML +=
           "<p><b>Private Received (" + msgObj.senderId + ")</b>: " + msgObj.content + "</p>";
      });
      ```

---

## 3. 메시지 형식 (ChatMessage 엔티티)

서버의 `ChatMessage` 엔티티는 다음과 같이 정의되어 있습니다.
(이미 존재하는 코드 예시)

```java
package hello.hello_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 채팅방의 메시지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    // 누가 보냈는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    // 메시지 내용
    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

*참고:*  
1:1 채팅을 위해서는, 이 엔티티에 `receiverId`와 같은 필드를 추가하거나, 별도의 DTO를 사용해 프론트로부터 받은 JSON을 처리할 수 있습니다.

---

## 4. 예제 HTML/JS 클라이언트

아래는 그룹 채팅과 1:1 채팅 기능을 모두 테스트할 수 있는 HTML 예제입니다.
이 예제에서는 전송이 잘 되는지 F12->Console로 확인할 수 있습니다.

추가로, 깃허브 minsung 브랜치에 있는 chat-test.html 코드는 1:1 채팅 기능을 테스트 할 수 있는 HTML 예제입니다. 
이 코드에서는 보내고 받는 것을 http://localhost:8080/chat-test.html 화면 상에서 확인할 수 있습니다.

현재 그룹 채팅 기능을 쓸지는 모르겠으며, 필요없으시면 말씀해주시면 1:1 채팅 기능만 살려서 드리겠습니다.

```html
<!DOCTYPE html>
<html>
<head>
  <title>Chat Test</title>
  <!-- 로컬 파일 사용 -->
  <script src="/js/sockjs.min.js"></script>
  <script src="/js/stomp.umd.js"></script>
</head>
<body>
  <div>
    <input type="text" id="roomId" placeholder="Room ID">
    <input type="text" id="senderId" placeholder="Sender ID">
    <!-- 개인 채팅용: 받는 사람 ID 입력 -->
    <input type="text" id="receiverId" placeholder="Receiver ID">
    <input type="text" id="content" placeholder="Message">
    <button onclick="sendGroupMessage()">Send Group</button>
    <button onclick="sendPrivateMessage()">Send Private</button>
  </div>
  <div id="chatLogs"></div>

  <script>
    var stompClient = null;

    function connect() {
      var socket = new SockJS('/ws-stomp');
      // UMD 빌드는 전역에 StompJs를 생성하므로, 아래와 같이 호출
      stompClient = StompJs.Stomp.over(socket);

      stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        // 그룹 채팅 구독
        stompClient.subscribe('/topic/chat', function(message) {
          var msgObj = JSON.parse(message.body);
          document.getElementById("chatLogs").innerHTML +=
            "<p><b>Group Received (" + msgObj.senderId + ")</b>: " + msgObj.content + "</p>";
        });

        // 개인 채팅 구독 (현재 사용자)
        stompClient.subscribe('/user/queue/private', function(message) {
          var msgObj = JSON.parse(message.body);
          document.getElementById("chatLogs").innerHTML +=
            "<p><b>Private Received (" + msgObj.senderId + ")</b>: " + msgObj.content + "</p>";
        });
      });
    }

    function sendGroupMessage() {
      var roomId = document.getElementById('roomId').value;
      var senderId = document.getElementById('senderId').value;
      var content = document.getElementById('content').value;

      // 그룹 메시지는 /app/chat/message로 전송
      stompClient.send("/app/chat/message", {}, JSON.stringify({
        chatRoomId: roomId,
        senderId: senderId,
        content: content
      }));
    }

    function sendPrivateMessage() {
      var roomId = document.getElementById('roomId').value;
      var senderId = document.getElementById('senderId').value;
      var receiverId = document.getElementById('receiverId').value;
      var content = document.getElementById('content').value;

      // 개인 메시지는 /app/chat/private로 전송 (서버의 PrivateChatController에서 처리)
      stompClient.send("/app/chat/private", {}, JSON.stringify({
        chatRoomId: roomId,
        senderId: senderId,
        receiverId: receiverId,
        content: content
      }));
    }

    window.onload = connect;

    // 진단용: 파일들이 제대로 로드되었는지 확인
    window.addEventListener('load', () => {
      console.log('SockJS = ', window.SockJS);
      console.log('StompJs = ', window.StompJs);
      console.log('StompJs.Stomp = ', window.StompJs?.Stomp);
    });
  </script>
</body>
</html>
```

---

## 결론

- 이 문서를 통해 프론트엔드 개발자는 WebSocket+STOMP 기반 채팅 API의 엔드포인트, 메시지 형식, 구독 경로 등을 파악할 수 있습니다.
- 제공한 HTML/JS 예제는 그룹 채팅과 개인(1:1) 채팅을 테스트하는 데 사용할 수 있으며, 실제 채팅 클라이언트 개발에 참고할 수 있습니다.
- REST API 관련 부분은 Swagger로 문서화하여 별도로 제공할 수 있습니다.