# PosLedger Assets

## 1. 선행 조건
- JDK : SUN JAVA 1.8 or Open JDK 1.8
- Build Tool : Maven 3.x
- IDE : Eclipse or IntelliJ
- Docker 18.x
- MariaDB 10.x
- MariaDB Client for Window : HeidiSQL - https://www.heidisql.com/download.php

## 2. MariaDB 실행
Maria DB 도커 컨테이너 생성 및 시작
```bash
docker run --name devmariadb -p 3306:3306 -v /data/mysql:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=test1234 -e MYSQL_DATABASE=hyperledger -e MYSQL_USER=test02 -e MYSQL_PASSWORD=test1234 -d mariadb:10.4.5 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --innodb_large_prefix=ON
```

Maria DB Client 실행, 비밀번호 test1234 입력
```bash
docker exec -it devmariadb mysql -uroot -p
```

## 3. Spring Boot Application 데이터소스 변경
```
192.168.193.237 를 MariaDB 실행 IP 로 변경
```

## 4. 실행 방법
메이븐 플러그인 
```bash
maven clean spring-boot:run
```

## 5. 단위 테스트 실행
로그인 화면 테스트
```bash
mvn test -Dtest=com.poscoict.posledger.assets.test.MockRestAPITest#loginTest
```

## 6. 브라우저 접속 주소
```bash
http://localhost:8080/assets
```

## 7. 프로그램 실행 흐름
```bash
Browser Request -> HttpFilter.java -> Controller.java -> Service.java -> Repository.java -> JSP
```
