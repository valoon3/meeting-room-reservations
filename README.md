
# 🏢 회의실 예약 시스템 API 서버

사내 회의실 예약을 위한 RESTful API 서버입니다.

## 🛠️ 프로젝트 개요 및 기술 스택/버전
### 주요 기술 스택

| 기술 | 버전 |
| :--- | :--- |
| **Java** | 21 |
| **Spring Boot** | 3.5.4 |
| **Gradle** | 8.14.3 |
| **Database** | PostgreSQL 13 |
| **ORM** | Spring Data JPA (Hibernate) |
| **QueryDSL**| 5.0.0 |
| **API Docs**| SpringDoc (Swagger UI) 2.8.8 |

-----

## 🚀 실행 방법

### `docker-compose up` 실행 방법

프로젝트를 실행하기 위해 로컬 머신에 **Docker**와 **Docker Compose**가 설치되어 있어야 합니다.

1.  프로젝트의 최상위 디렉토리(`docker-compose.yml` 파일이 있는 위치)에서 터미널을 엽니다.

2.  아래 명령어를 실행하여 모든 서비스(주 애플리케이션, Mock 결제 서버, PostgreSQL DB)를 한 번에 빌드하고 실행합니다.

    ```bash
    docker-compose up --build
    ```

    * `--build` 옵션은 코드가 변경되었을 때 이미지를 새로 빌드하기 위해 사용합니다. 최초 실행 시 또는 코드 변경 후에 사용하는 것을 권장합니다.
    * 백그라운드에서 실행하려면 `-d` 옵션을 추가하세요: `docker-compose up --build -d`

3.  모든 서비스가 정상적으로 시작되면, 주 애플리케이션은 `localhost:8080`에서, Mock 결제 서버는 `localhost:8081`에서 실행됩니다.

4.  서비스를 종료하려면 터미널에서 `Ctrl + C`를 누르거나, 아래 명령어를 실행합니다.

    ```bash
    docker-compose down
    ```

-----

## 📄 Swagger UI 접속 방법

애플리케이션이 실행 중일 때, 아래 주소로 접속하여 API 문서를 확인하고 직접 테스트할 수 있습니다.

- **Swagger UI URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

-----

## ✅ 테스트 실행 방법

프로젝트의 단위 테스트와 통합 테스트를 실행하려면 아래 명령어를 사용하세요.

1.  `meeting-room-reservations` 프로젝트 디렉토리에서 터미널을 엽니다.

2.  아래 명령어를 실행합니다.

    ```bash
    ./gradlew test
    ```

3.  테스트가 성공적으로 완료되면 `BUILD SUCCESSFUL` 메시지가 출력됩니다. 테스트 결과 리포트는 `build/reports/tests/test/index.html` 파일에서 확인할 수 있습니다.