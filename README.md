## 1. 테스트하기 (hey 사용)

- 테스트 데이터: 1000개의 게시글 (각 게시글 약 1KB 미만의 작은 크기)
- 작은 사이즈의 데이터를 여러개 조회하는 시나리오
- Redis: 로컬 환경(localhost), 단일 인스턴스
- DB: Postgres 로컬 환경

```bash
# 일반 조회
hey -n 1000 -c 100 http://localhost:8100/api/posts/basic

# Redis 캐싱 조회
hey -n 1000 -c 100 http://localhost:8100/api/posts/redis

# 인메모리 캐싱 조회
hey -n 1000 -c 100 http://localhost:8100/api/posts/local
```

- 10000개의 요청을 100개의 동시 요청으로 보내는 테스트

```bash
# 일반 조회
hey -n 10000 -c 100 http://localhost:8100/api/posts/basic

# Redis 캐싱 조회
hey -n 10000 -c 100 http://localhost:8100/api/posts/redis

# 인메모리 캐싱 조회
hey -n 10000 -c 100 http://localhost:8100/api/posts/local
```

## 1-1. 테스트 결과 (1000개의 요청일 경우)

- M2 Max RAM 64GB로 테스트 진행
- 1000개의 요청을 100개의 동시 요청으로 보냄

일반 조회 (캐시 없음)

- 총 실행 시간: 1.17초
- 평균 응답 시간: 110.9ms
- 초당 처리량: 854.28 req/s
- 95% 응답 시간: 152.1ms

Redis 캐시

- 총 실행 시간: 1.19초
- 평균 응답 시간: 114.1ms
- 초당 처리량: 837.32 req/s
- 95% 응답 시간: 128.4ms

Caffeine 로컬 캐시

- 총 실행 시간: 0.19초
- 평균 응답 시간: 17.8ms
- 초당 처리량: 5,119.41 req/s
- 95% 응답 시간: 34.3ms

## 1-2. 테스트 결과 (10000개의 요청일 경우)

- M2 Max RAM 64GB로 테스트 진행
- 10000개의 요청을 100개의 동시 요청으로 보냄

일반 조회 (캐시 없음)

- 총 실행 시간: 10.91초
- 평균 응답 시간: 107.7ms
- 초당 처리량: 916.52 req/s
- 95% 응답 시간: 126.5ms

Redis 캐시

- 총 실행 시간: 11.90초
- 평균 응답 시간: 118.5ms
- 초당 처리량: 840.09 req/s
- 95% 응답 시간: 129.1ms

Caffeine 로컬 캐시

- 총 실행 시간: 1.82초
- 평균 응답 시간: 16.1ms
- 초당 처리량: 5,484.82 req/s
- 95% 응답 시간: 49.3ms

## 1-3. 결론

처리량(Throughput) 비교

- Caffeine이 압도적으로 높은 처리량 (약 5,484 req/s)
- 일반 조회가 Redis보다 약간 높은 처리량 (916 vs 840 req/s)

응답 시간

- Caffeine이 가장 빠른 응답 시간 (평균 16.1ms)
- 일반 조회와 Redis는 비슷한 수준 (약 110ms대)

안정성

- 모든 방식이 95% 응답 시간이 200ms 이내로 안정적
- Caffeine이 가장 안정적인 응답 시간 분포를 보임

결론

- 단일 서버 환경에서는 Caffeine이 압도적으로 좋은 성능
- 현재 테스트 환경(작은 데이터/높은 동시성)에서 Redis는 단일 스레드 특성으로 인해 성능 제한
- 로컬 캐시의 이점이 분명히 드러남

## 1-4. Redis 성능 분석

1. 현재 테스트의 특성

- 작은 크기의 데이터를 대량으로 동시에 요청
- 네트워크 오버헤드 발생
- Redis의 단일 스레드 특성으로 인한 병목

2. Redis가 유리한 상황

- 대용량 단일 데이터 처리 (예: 큰 JSON 객체)
- 낮은 동시성 요청
- 분산 환경에서의 데이터 공유가 필요한 경우

3. Redis 성능에 영향을 미치는 요소

- 네트워크 지연 (로컬 환경이어도 발생)
- 직렬화/역직렬화 오버헤드
- Connection Pool 설정
- Redis 서버 설정 (maxmemory, maxclients 등)

## 2. 대용량 데이터 처리 테스트하기 (hey 사용)

- 테스트 데이터: 1000개의 게시글 (각 게시글 약 1MB 이상의 큰 크기)
- 큰 사이즈의 데이터를 여러개 조회하는 시나리오
- Redis: 로컬 환경(localhost), 단일 인스턴스
- DB: Postgres 로컬 환경

```bash

```

## 3. 실제 사용시 고려사항

Redis 사용이 유리한 경우

- 대용량 단일 데이터 처리
- 분산 환경에서의 데이터 공유
- 세션 저장소, 대규모 캐싱

Caffeine 사용이 유리한 경우

- 동시성이 높은 소규모 데이터 처리
- 단일 서버 환경
- 로컬 캐싱이 필요한 경우

둘 다 사용하는 경우

- Redis: 분산 필요한 대용량 데이터
- Caffeine: 자주 접근하는 소규모 데이터

## 4. 성능 최적화 전략

1. 하이브리드 캐싱

- 자주 접근하는 작은 데이터: Caffeine
- 분산 필요/큰 데이터: Redis

2. Redis 성능 최적화

- Redis Cluster 사용
- Pipeline 활용
- 적절한 데이터 구조 선택

3. 모니터링

- 캐시 히트율
- 응답 시간
- 메모리 사용량

4. 캐시 전략 최적화

- 적절한 TTL 설정
- 캐시 갱신 정책 (Write-through/Write-behind)
- 캐시 제거 정책 (LRU/LFU)
- 캐시 예열 (Cache Warming)