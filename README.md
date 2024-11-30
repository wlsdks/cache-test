## 1. 테스트하기 (hey 사용)

- 테스트 데이터: 1000개의 게시글 (각 게시글 약 1KB 미만의 작은 크기)
- 작은 사이즈의 데이터를 여러개 조회하는 시나리오
- Redis: 로컬 환경(localhost), 단일 인스턴스
- DB: Postgres 로컬 환경

#### hey의 동작 방식 이해하기
- 동시성 수준(-c)
  - 동시에 실행될 작업자(worker)의 수를 지정합니다. 즉, 동시에 몇 개의 요청을 보낼지를 결정합니다.
- 총 요청 수(-n) 
  - 테스트 동안 보낼 총 요청의 수를 지정합니다.
- 테스트 기간
  - hey는 지정된 총 요청 수를 모든 작업자가 최대한 빠르게 처리할 때까지 테스트를 진행합니다. 테스트 시간은 고정되어 있지 않으며, 서버의 응답 시간과 처리 능력에 따라 달라집니다.
  - 따라서, hey -n 1000 -c 100 명령을 실행하면, 100개의 동시 요청을 보내는 작업자들이 총 1000개의 요청을 최대한 빨리 처리합니다. 이때 테스트 기간은 서버의 성능에 따라 결정됩니다.

```bash
# 일반 조회
hey -n 3000 -c 50 "http://localhost:8100/api/posts/basic?page=0&size=20"

# Redis 캐시 조회
hey -n 3000 -c 50 "http://localhost:8100/api/posts/redis?page=0&size=20"

# 로컬 캐시 조회
hey -n 3000 -c 50 "http://localhost:8100/api/posts/local?page=0&size=20"
```

## 1-1. 테스트 결과
- M2 Max RAM 64GB로 테스트 진행
- 10000개의 요청을 100개의 동시 요청으로 보냄

일반 조회 (캐시 없음)
```bash
Summary:
Total:	3.3505 secs
Slowest:	0.3340 secs
Fastest:	0.0046 secs
Average:	0.0548 secs
Requests/sec:	895.3772

Status
[200]	3000 responses
```

Redis 캐시 조회
```bash
Summary:
Total:	2.6371 secs
Slowest:	0.3268 secs
Fastest:	0.0284 secs
Average:	0.0436 secs
Requests/sec:	1137.5926

Status
[200]	3000 responses
```
로컬 캐시 조회 (Caffeine)
```bash
Summary:
Total:	0.6202 secs
Slowest:	0.1816 secs
Fastest:	0.0012 secs
Average:	0.0090 secs
Requests/sec:	4837.0970

Status
[200]	3000 responses
````

## 2. 대용량 + 복잡한 쿼리 데이터 처리 테스트 (hey 사용)

- 테스트 데이터: 1000개의 게시글 (각 게시글 약 1MB 이상의 큰 크기)
- 큰 사이즈의 데이터를 여러개 조회하는 시나리오
- Redis: 로컬 환경(localhost), 단일 인스턴스
- DB: Postgres 로컬 환경

```bash
# 일반 조회
hey -n 3000 -c 50 "http://localhost:8100/api/reviews/basic?page=0&size=20&minRating=0"

# Redis 캐시 조회
hey -n 3000 -c 50 "http://localhost:8100/api/reviews/redis?page=0&size=20&minRating=0"

# 로컬 캐시 조회
hey -n 3000 -c 50 "http://localhost:8100/api/reviews/local?page=0&size=20&minRating=0"
```

## 2-1. 테스트 결과
일반 조회 (캐시 없음)
```bash
Summary:
Total:        35.9581 secs
Slowest:      1.1375 secs
Fastest:      0.1111 secs
Average:      0.5942 secs
Requests/sec: 83.4305

Status code distribution:
[200] 3000 responses
```
- 복잡한 쿼리와 대용량 데이터로 인해 데이터베이스 부하가 높습니다. 
- 응답 시간이 길고, 요청 처리 속도가 느립니다. 
- resp wait 시간이 길어 데이터베이스에서 결과를 받아오는 데 시간이 많이 소요됩니다.

Redis 캐시 조회
```bash
Summary:
Total:        0.2381 secs
Slowest:      0.0130 secs
Fastest:      0.0009 secs
Average:      0.0039 secs
Requests/sec: 12602.1276

Status code distribution:
[200] 3000 responses
```
- Redis 캐시를 사용하여 데이터베이스 접근 없이 메모리에서 데이터를 가져옵니다.
- 응답 시간이 매우 빠르고, 요청 처리 속도가 높습니다.
- resp wait 시간이 매우 짧아 캐시에서의 데이터 조회가 빠르게 이루어집니다.

로컬 캐시 조회 (Caffeine)
```bash
Summary:
Total:        0.0936 secs
Slowest:      0.0055 secs
Fastest:      0.0002 secs
Average:      0.0015 secs
Requests/sec: 32045.9469

Status code distribution:
[200] 3000 responses
```
- 로컬 캐시(Caffeine 등)를 사용하여 애플리케이션 메모리에서 데이터를 가져옵니다.
- 응답 시간이 가장 빠르고, 요청 처리 속도가 높습니다.
- Redis 캐시보다도 빠른 성능을 보입니다.

## 2-2. 동시성 수준 이해하기 (hey 요청 이해하기)
1. 동시성 수준 50이란?
- 동시성 수준 50은 한 번에 최대 50개의 요청이 동시에 처리된다는 것을 의미합니다. 
- 즉, 애플리케이션 서버는 동시에 50명의 사용자로부터 오는 요청을 처리할 수 있어야 합니다.

2. 총 3000개의 요청이란?
- 총 3000개의 요청은 테스트 기간 동안 전체로 보낸 요청의 수입니다.
- 이 요청들은 동시성 수준에 따라 분산되어 처리됩니다.

3. 테스트 기간과 요청 속도 계산 (예시를 든 설명)
- 테스트 총 소요 시간을 약 12.9초라고 가정합시다.
- 평균 초당 요청 수(Requests/sec)는 약 232개로 가정합니다. 
- 계산: 3000 요청 / 12.9초 ≈ 232 요청/초 
- 이는 테스트 기간 동안 매 초당 평균 232개의 요청이 처리되었다는 의미입니다.