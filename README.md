# Decentralized Signature Service

This repository is a client application for the FabAsset usage. For more information, read **FabAsset: Unique Digital Asset Management System for Hyperledger Fabric**, which is presented in BlockApp 2020 co-located with ICDCS 2020. To develop this project, we referred to [blockchain-application-using-fabric-java-sdk](https://github.com/IBM/blockchain-application-using-fabric-java-sdk).

## Prerequisites
- Environment: 4.15.0-122-generic (Linux Kernel), Ubuntu 18.04.3 LTS
- JDK: JDK 1.8
- Build Tool: Maven 3.6.0
- Docker 19.03.1

## MariaDB

MariaDB maintains metadata of the application 

### 1. Run Maria DB Docker Container 

```bash
docker run --name devmariadb -p 3306:3306 -v /data/mysql:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=test1234 -e MYSQL_DATABASE=hyperledger -e MYSQL_USER=test02 -e MYSQL_PASSWORD=test1234 -d mariadb:10.4.5 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --innodb_large_prefix=ON
```

### 2. Execute Maria DB Client with password 'test1234' 
```bash
docker exec -it devmariadb mysql -uroot -p
```

## Redis

Redis maintains users' enrollment

### 1. Install Redis Tools in Ubuntu 18.04

```bash
sudo apt-get install redis-tools
```

### 2. Run Redis Client with password 'test1234'

```bash
redis-cli
```

## Enter Spring Application 
```bash
http://localhost:8080/assets
```
