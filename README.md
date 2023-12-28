# phc-world-back
### Stack
> * JAVA 17
> * Spring-Boot 3.2.0
> * Spring-Security 6.2.0
> * JPA
> * JWT
> * Gradle
> * h2-database
> * Junit5
*** 
###
```mermaid
sequenceDiagram
    회원가입->>+회원: 회원 정보 저장
    회원->>+로그인: 아이디, 패스워드
    로그인-->>-회원: JWT Token
    회원->>+게시판: access token, 게시글
    게시판->>-회원: 게시글 등록 성공
    회원->>+게시판: 만료된 token, 게시글
    게시판-->>-회원: 401 오류    
    회원->>+게시판: refresh token
    게시판-->>-회원: 새로운 토큰
```
