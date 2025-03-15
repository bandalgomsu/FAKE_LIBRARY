## 가짜 책 도서관 !

---

## 기술 스택

- Kotlin , Spring WebFlux , Coroutine , R2DBC
- MySql , Redis
- Gemini API

---

## ERD

<img width="1064" alt="ERD" src="https://github.com/user-attachments/assets/1d6a61c7-5c34-4bdc-a098-4deb88f0e7d2" />


## Cording Standard

### Package Structure

- app
    - 도메인 패키지 (Book 등등)
    - common
        - 공통 예외 처리 등 공통된 내부 기술 관리
    - Infrastructure 패키지를 의존 하면 안됨
- Infrastructure
    - 외부 기술 (DB , Redis 등등)
    - app에서 interface를 구현하고 이를 Infrastructure에서 구현

### Layer

위에서 아래로 의존

- Controller
- Service
- Implement
- Dao
