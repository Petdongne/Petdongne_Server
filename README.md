
# 🏡 **펫동네 (PetDongne)** [![codecov](https://codecov.io/gh/Petdongne/Petdongne_Server/branch/develop/graph/badge.svg)](https://codecov.io/gh/Petdongne/Petdongne_Server)

> **반려동물과 함께 살기 좋은 동네를 찾아주는 부동산 서비스**

> 🧭 **프로젝트 목표:**
> 반려동물 친화적인 거주 지역을 쉽게 탐색할 수 있는 **지도 기반 부동산 서비스** 구축.

---

## ✨ 주요 기능

* 🔍 **행정구역(법정동) 주소 검색** — *구현 완료*
* 🗺️ **지도 뷰포트 내 행정구역 / 건물 조회** — *구현 완료*
* 🏢 **건물 건축물대장 조회** — *구현 중*
* 🐶 **가까운 동물병원 / 공원 조회** — *구현 예정*
* 📝 **반려동물과 함께 거주한 리뷰 등록 / 조회** — *구현 예정*

---

## ⚙️ **기술 스택**

| 구분                     | 사용 기술                                                                  |
| ---------------------- | ---------------------------------------------------------------------- |
| **Language**           | Java 21                                                                |
| **Framework**          | Spring Boot 3.4.5 <br> → Spring Web / Data JPA / Security / Validation |
| **ORM & Query**        | QueryDSL 5.1.0                                                         |
| **Database**           | PostgreSQL · PostGIS                                                   |
| **Caching**            | Caffeine Cache                                                         |
| **Spatial Processing** | Hibernate Spatial · Geolatte-geom                                      |
| **Testing**            | Testcontainers · JUnit5 · AssertJ                                      |
| **API Docs**           | Springdoc OpenAPI (Swagger UI)                                         |
| **Coverage**           | JaCoCo                                                                 |

---




