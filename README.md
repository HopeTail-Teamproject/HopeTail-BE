# HopeTail BE - 1:1 Chatting Backend

> **SW창업캡스톤디자인2**를 위해 만든, Spring Boot 기반 1:1 채팅 백엔드입니다.  
> REST API로 **채팅방을 생성**하고, WebSocket **STOMP**를 이용해 **실시간으로 메시지를 교환**합니다.

## 목차 (Table of Contents)

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

## 프로젝트 개요
- 카카오톡과 유사한 1:1 채팅 기능을 구현한 **Spring Boot** 백엔드입니다.
- **사용자**가 채팅방을 직접 만들 수 있으며, 실시간(WebSocket)으로 메시지를 보낼 수 있습니다.
- 이 코드를 **프론트엔드**와 연결하면, 브라우저(또는 앱)에서 사용자 간 **양방향 채팅**이 가능합니다.

---

## 기술 스택
- **Spring Boot** 3.4.0
- **Java** 17
- **JPA (Hibernate)**
- **MariaDB** / **MySQL**
- **Spring WebSocket (STOMP)**
- **SpringDoc OpenAPI** (Swagger)
- **Lombok**, **Gradle** (혹은 Maven)

---

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

## 빌드 & 실행 방법

### 1) Clone & Build
```bash
git clone https://github.com/HopeTail-Teamproject/HopeTail-BE.git
cd HopeTail-BE

# Gradle 빌드
./gradlew clean build
```

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

### 3) Run
```bash
# JAR 실행
java -jar build/libs/hope-tail-be-0.0.1-SNAPSHOT.jar

# 혹은 IDE(인텔리J, 이클립스 등)에서 직접 Run
```
- 실행 후 **http://localhost:8080**에서 서버 확인

---

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

---

## API 명세 (Swagger)
- **SpringDoc OpenAPI**를 사용 중입니다.
- 서버 구동 후, 브라우저에서 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)** (또는 `/swagger-ui.html`) 접근 시, **REST API** 목록이 표시됩니다.
    - `POST /chatrooms` : 채팅방 생성
    - `GET /chatrooms/{roomId}` : 채팅방 조회

위 API로 **채팅방을 생성**하거나, **조회** 할 수 있습니다.

---

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

---

### 테스트 방법

#### 1) Swagger로 REST API 테스트
- `POST /chatrooms` → 채팅방 생성 확인

#### 2) WebSocket(STOMP) 테스트
- 예제 HTML 페이지(단일 페이지 1:1 채팅 테스트) 사용
    - [**/test-chat.html**](./test-chat.html) (프로젝트 내 위치)
    - 브라우저에서 열고, 서로 다른 Sender/Receiver 설정 후 **Connect** → **Send**
    - `"/topic/chatroom/{roomId}"`를 통해 양쪽에서 메시지가 실시간으로 수신되는지 확인 또는 DB 내의 `chat_message` 테이블에서 확인
    - 

---