# HopeTail 채팅 API 문서
## 목차 (Table of Contents)

## 개요
이 API는 Spring Boot 기반의 WebSocket과 STOMP 프로토콜을 사용하여 실시간 채팅 기능(그룹 채팅 및 1:1 채팅)을 제공합니다.  
프론트엔드에서는 SockJS와 STOMP JS 라이브러리를 사용해 이 API에 연결하여, 메시지 전송 및 수신 기능을 구현할 수 있습니다.
1. [프로젝트 개요](#프로젝트-개요)
2. [기술 스택](#기술-스택)
3. [프로젝트 구조](#프로젝트-구조)
4. [빌드 & 실행 방법](#빌드--실행-방법)
5. [데이터베이스 구성](#데이터베이스-구성)
6. [API 명세 (Swagger)](#api-명세-swagger)
7. [채팅 기능 소개](#채팅-기능-소개)
    - [채팅방 생성 (REST)](#채팅방-생성-rest)
    - [실시간 채팅 (WebSocket/STOMP)](#실시간-채팅-websocketstomp)
    - [테스트 방법](#테스트-방법)
8. [기타](#기타)

 ---

## 1. WebSocket 연결
## 프로젝트 개요
- 카카오톡과 유사한 1:1 채팅 기능을 구현한 **Spring Boot** 백엔드입니다.
- **사용자**가 채팅방을 직접 만들 수 있으며, 실시간(WebSocket)으로 메시지를 보낼 수 있습니다.
- 이 코드를 **프론트엔드**와 연결하면, 브라우저(또는 앱)에서 사용자 간 **양방향 채팅**이 가능합니다.

### 엔드포인트
- **URL:** `/ws-stomp`
- **설명:** 이 엔드포인트는 SockJS를 사용해 WebSocket 연결을 수립합니다.
- **예시 (JavaScript):**
  ```js
  var socket = new SockJS('/ws-stomp');
  ```
 ---

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
## 기술 스택
- **Spring Boot** 3.4.0
- **Java** 17
- **JPA (Hibernate)**
- **MySQL**
- **Spring WebSocket (STOMP)**
- **SpringDoc OpenAPI** (Swagger)
- **Lombok**, **Gradle**

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
## 프로젝트 구조

예시:

 ```
 src
  ┣ main
  ┃ ┣ java
  ┃ ┃ ┗ hello.hello_spring
  ┃ ┃   ┣ config
  ┃ ┃   ┣ controller
  ┃ ┃   ┣ domain
  ┃ ┃   ┣ dto
  ┃ ┃   ┣ repository
  ┃ ┃   ┣ service
  ┃ ┗ resources
  ┃   ┣ application.yml
  ┃   ┗ static
  ┗ test
      ┗ (테스트코드)
 ```

- `config` : WebSocketConfig, SwaggerConfig 등 설정 파일
- `controller` : REST API용 Controller, WebSocket Controller (`@MessageMapping`)
- `service` : 비즈니스 로직(ChatService 등)
- `domain` : 엔티티 클래스(ChatRoom, ChatMessage, Member 등)
- `repository` : JPA 인터페이스(DAO)
- `dto` : 요청/응답 DTO 클래스

 ---

## 3. 메시지 형식 (ChatMessage 엔티티)
## 빌드 & 실행 방법

서버의 `ChatMessage` 엔티티는 다음과 같이 정의되어 있습니다.
(이미 존재하는 코드 예시)
### 1) Clone & Build
 ```bash
 git clone https://github.com/HopeTail-Teamproject/HopeTail-BE.git
 cd HopeTail-BE
 
 ```java
 package hello.hello_spring.domain;
 # Gradle 빌드
 ./gradlew clean build
 ```

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
### 2) DB 준비
- MySQL(MariaDB) 설치 후, `application.yml`(또는 properties) 파일에서 **DB 접속 정보**를 올바르게 설정하세요.
- 예:
  ```yaml
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/hopetail-db?autoReconnect=true
      username: minsung
      password: minsung1234 
    jpa:
      hibernate:
        ddl-auto: update
      show-sql: true
  ```

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
### 3) Run
 ```bash
 # JAR 실행
 java -jar build/libs/hope-tail-be-0.0.1-SNAPSHOT.jar
 
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
 # 혹은 IDE(인텔리J, 이클립스 등)에서 직접 Run
 ```
- 실행 후 **http://localhost:8080**에서 서버 확인

  // 어느 채팅방의 메시지인지
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;
 ---

     // 누가 보냈는지
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "sender_id")
     private Member sender;
## 데이터베이스 구성

- **주요 테이블**
    1. **Member**: 사용자(회원) 정보
    2. **ChatRoom**: 1:1 채팅방 (member1_id, member2_id, createdAt 등)
    3. **ChatMessage**: 메시지 (sender_id, receiver_id, chat_room_id, content, createdAt 등)

- 예:
  ```sql
  CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    ...
  );

  CREATE TABLE chat_room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member1_id BIGINT,
    member2_id BIGINT,
    created_at DATETIME,
    ...
  );

  CREATE TABLE chat_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chat_room_id BIGINT,
    sender_id BIGINT,
    receiver_id BIGINT,
    content TEXT,
    created_at DATETIME,
    ...
  );
  ```
  (실제로는 JPA가 `ddl-auto: update`에 따라 생성/수정해줄 것입니다.)

  // 메시지 내용
  @Column(nullable = false)
  private String content;
 ---

     @CreationTimestamp
     private LocalDateTime createdAt;
}
 ```
 ## API 명세 (Swagger)
 - **SpringDoc OpenAPI**를 사용 중입니다.
 - 서버 구동 후, 브라우저에서 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)** (또는 `/swagger-ui.html`) 접근 시, **REST API** 목록이 표시됩니다.
     - `POST /chatrooms` : 채팅방 생성
     - `GET /chatrooms/{roomId}` : 채팅방 조회
 
 *참고:*  
 1:1 채팅을 위해서는, 이 엔티티에 `receiverId`와 같은 필드를 추가하거나, 별도의 DTO를 사용해 프론트로부터 받은 JSON을 처리할 수 있습니다.
 위 API로 **채팅방을 생성**하거나, **조회** 할 수 있습니다.
 
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
 ## 채팅 기능 소개
 
 ### 채팅방 생성 (REST)
 ```bash
 POST /chatrooms
 {
   "member1Id": 1,
   "member2Id": 2
 }
 ```
- 이미 두 멤버가 방이 있다면 **기존 방** 반환, 없으면 **새로운 방** 생성
- 응답은 `{"chatRoomId": 5, "member1Username": "...", "member2Username": "...", "createdAt": "..."}` 형태

### 실시간 채팅 (WebSocket/STOMP)

1. **연결**:
    - WebSocket 엔드포인트: `"/ws-stomp"`
    - 클라이언트에서 SockJS + STOMP를 이용해 `new SockJS('/ws-stomp')` → `stompClient.connect(...)`

2. **구독(Subscribe)**: (Topic 브로드캐스트 방식)
    - `"/topic/chatroom/{roomId}"`를 구독 → 해당 방에 메시지가 오면 실시간으로 수신

3. **메시지 전송(Send)**:
    - `"/app/chat/private"` 경로로 STOMP 메시지를 전송
    - 서버 `@MessageMapping("/chat/private")` 메서드가 받음 → DB 저장 후, `messagingTemplate.convertAndSend("/topic/chatroom/"+roomId, msg)`
    - 방을 구독 중인 사용자 모두가 메세지를 받음

#### 샘플 DTO (PrivateChatMessageDto)
 ```json
 {
   "chatRoomId": 2,
   "senderId": 1,
   "receiverId": 2,
   "content": "Hello world"
 }
 ```