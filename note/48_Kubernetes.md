# Kubernetes 기초: 컨테이너를 선언하고 유지한다

> 중요도: **B** · 이 장의 목표: Pod, Deployment, Service의 관계를 설명하고, 원하는 상태(desired state)를 YAML로 선언한 뒤 롤링 배포와 헬스 체크를 이해한다.

> 2026-11-23 추가 장 (로드맵 M15 "로컬 K8s 배포" 항목). **kind 로컬 클러스터(Kubernetes 노드 이미지 v1.37.0)에서 실행해 확인했다.** 3절 매니페스트는 서버 측 검증(`--dry-run=server`)을 통과했다. 실제 이미지가 없으면 롤아웃이 멈추는 것도 확인했다. 4절의 롤링 업데이트와 롤백은 nginx 이미지로 바꾼 사본에서 확인했다. 확인 후 클러스터는 삭제했다.

---

## 1. Kubernetes가 하는 일

**정의** — 여러 컨테이너를 여러 서버에 나눠 띄우고, 죽으면 다시 띄우고, 트래픽을 나누고, 새 버전으로 천천히 바꾸는 **컨테이너 오케스트레이션 시스템**이다.

Docker(43장 Linux·Docker 내용 참고)는 **컨테이너 하나**를 실행한다. Kubernetes는 **컨테이너를 많이 관리**한다.

### 1.1 선언형 모델

명령형: "이 컨테이너를 지금 띄워라."
선언형: "이 앱은 복제본 3개가 항상 떠 있어야 한다."

Kubernetes는 선언된 상태(desired state)와 실제 상태(actual state)를 계속 비교하고, 다르면 고친다. 이것을 **조정 루프(reconciliation loop)**라고 한다.

---

## 2. 핵심 오브젝트

| 오브젝트 | 역할 | 비유 |
|---|---|---|
| Pod | 컨테이너 한 개 이상을 묶은 최소 실행 단위 | 함께 움직이는 프로세스 묶음 |
| ReplicaSet | 지정한 수의 Pod를 유지 | 정해진 인원 유지 |
| Deployment | ReplicaSet을 관리하고 버전 교체를 담당 | 배포 계획 |
| Service | 여러 Pod 앞에 고정 주소와 로드밸런싱 제공 | 안내 데스크 |
| ConfigMap / Secret | 설정과 비밀 값을 이미지 밖에서 주입 | 외부 설정 파일 |

Pod의 IP는 바뀔 수 있다. 다른 서비스는 Service 이름으로 접근한다.

---

## 3. 매니페스트 예제

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: khac-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: khac-api
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 1      # 교체 중 동시에 내려가도 되는 Pod 수
      maxSurge: 1            # 교체 중 추가로 띄울 수 있는 Pod 수
  template:
    metadata:
      labels:
        app: khac-api
    spec:
      containers:
        - name: api
          image: khac/api:1.0.0
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: prod
          readinessProbe:      # 준비되기 전에는 트래픽을 보내지 않는다
            httpGet:
              path: /actuator/health/readiness
              port: 8080
            initialDelaySeconds: 10
          livenessProbe:       # 응답이 없으면 컨테이너를 다시 띄운다
            httpGet:
              path: /actuator/health/liveness
              port: 8080
            initialDelaySeconds: 30
---
apiVersion: v1
kind: Service
metadata:
  name: khac-api
spec:
  selector:
    app: khac-api
  ports:
    - port: 80
      targetPort: 8080
```

**읽는 법**

- `selector.matchLabels`와 `template.labels`가 같아야 Deployment가 자기 Pod를 찾는다. 다르면 배포가 시작되지 않는다.
- `readinessProbe`는 **트래픽을 받을지**, `livenessProbe`는 **다시 띄울지**를 정한다. 둘을 같은 경로로 두면 느린 시작 중에 컨테이너가 계속 재시작될 수 있다.
- 이미지 태그 `1.0.0`처럼 버전을 고정한다. `latest`를 쓰면 롤백 기준이 사라진다.
- **실제로 확인한 결과:** 위 매니페스트를 그대로 적용하면 `khac/api:1.0.0` 이미지가 없어서 Pod가 `ErrImagePull`, `ImagePullBackOff`가 되고, `rollout status`가 시간 초과로 끝나지 않는다. 이미지 태그를 맞춰야 하는 이유다.

---

## 4. 롤링 배포와 롤백

```bash
kubectl apply -f khac-api.yaml          # 선언 적용
kubectl rollout status deployment/khac-api
kubectl get pods -l app=khac-api
kubectl rollout undo deployment/khac-api  # 이전 버전으로 되돌린다
```

**실행 결과(nginx 사본):** `rollout status`는 새 Pod가 준비될 때까지 기다렸고, `set image`로 `1.26`을 배포한 뒤 `rollout undo`로 `1.27`로 돌아갔다. `rollout undo`는 `last-applied-configuration` 주석을 갱신하지 않으므로 kubectl이 경고를 낸다. 이후 `apply`를 쓸 때는 주의해야 한다.

Deployment는 이미지를 바꾸면 새 ReplicaSet을 만들고, `maxSurge`와 `maxUnavailable` 규칙에 따라 Pod를 하나씩 교체한다. 새 Pod의 readiness가 통과해야 다음 교체가 진행된다.

---

## 5. Kubernetes와 앞 장의 연결

| 앞 장 | Kubernetes에서의 대응 |
|---|---|
| 46장 관측성 (헬스 체크, 메트릭) | readiness·liveness probe |
| 46장 CI/CD (배포 자동화) | 이미지 태그를 바꾼 `apply`가 배포 단계 |
| 44장 AWS 배포 | 관리형 클러스터(EKS)는 같은 오브젝트를 쓴다 |
| 45장 Terraform | 클러스터 자체를 IaC로 만든다 |

---

## 6. 실습 과제

1. 위 매니페스트를 `khac-api.yaml`로 저장하고 `kubectl apply --dry-run=client -f khac-api.yaml`로 문법만 확인한다. (클러스터 없이 가능)
2. `selector`와 `labels`를 일부러 다르게 쓰고, 어떤 오류가 나는지 적는다.
3. `readinessProbe`를 `/actuator/health/liveness`로 잘못 바꾼 경우 어떤 문제가 생기는지 한 문단으로 쓴다.

---

## 단원 통과 기준

- [ ] Pod, Deployment, Service의 역할 차이를 설명할 수 있다.
- [ ] 선언형 모델과 조정 루프를 설명할 수 있다.
- [ ] readiness와 liveness probe의 차이를 설명할 수 있다.
- [ ] 롤링 배포에서 `maxSurge`와 `maxUnavailable`의 의미를 설명할 수 있다.
