# Terraform과 IaC: 인프라를 코드로 관리한다

> 중요도: **B** · 이 장의 목표: IaC가 필요한 이유를 설명하고, Terraform의 provider·resource·variable·state 개념을 구분하며, 계획(plan)을 먼저 읽고 적용(apply)하는 순서를 지킨다.

> 2026-11-23 추가 장 (로드맵 M14 "Terraform으로 VPC 구성" 항목). **3절 예제는 Terraform 1.17.2에서 `fmt`, `init`, `validate`를 실행해 통과를 확인했다.** `plan`과 `apply`는 AWS 자격 증명이 필요해서 실행하지 않았고, 실제 AWS 계정에는 적용하지 않았다. 5절의 원격 백엔드 설정은 실행하지 않았다.

---

## 1. IaC(Infrastructure as Code)란

**정의** — 서버, 네트워크, 데이터베이스 같은 인프라를 **콘솔 클릭이 아니라 코드 파일**로 선언하고, 그 코드로 만들고 바꾸고 지우는 방식이다.

콘솔로 만든 인프라의 문제:

| 문제 | 예 |
|---|---|
| 재현할 수 없다 | 개발 환경과 운영 환경의 보안 그룹이 조금씩 다르다 |
| 변경 이력이 없다 | 누가 언제 포트를 열었는지 모른다 |
| 되돌리기 어렵다 | 장애 후 어떤 설정이 원래였는지 기억에 의존한다 |

IaC는 이 설정을 Git에 두고, 리뷰하고, 같은 코드로 여러 환경을 만든다. 형상관리(41장)를 인프라까지 확장하는 방법이다.

---

## 2. 핵심 개념

| 개념 | 뜻 | 예 |
|---|---|---|
| Provider | 클라우드 API와 연결하는 플러그인 | `aws`, `google` |
| Resource | 만들고 관리할 인프라 한 개 | VPC, 서브넷, 보안 그룹 |
| Data source | 이미 있는 인프라를 읽기만 한다 | 최신 Ubuntu AMI 조회 |
| Variable | 입력값. 환경마다 다르게 준다 | 리전, CIDR |
| Output | 적용 후 밖으로 보여줄 값 | VPC ID, 로드밸런서 주소 |
| State | 코드와 실제 인프라의 대응 기록 | `terraform.tfstate` |
| Module | 재사용 가능한 resource 묶음 | "VPC 모듈" |

---

## 3. 최소 예제: VPC와 서브넷

```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.region
}

variable "region" {
  type    = string
  default = "ap-northeast-2"
}

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

resource "aws_vpc" "main" {
  cidr_block           = var.vpc_cidr
  enable_dns_hostnames = true

  tags = {
    Name = "khac-main"
  }
}

resource "aws_subnet" "public_a" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true

  tags = {
    Name = "khac-public-a"
  }
}

output "vpc_id" {
  value = aws_vpc.main.id
}
```

**읽는 법**

- `aws_subnet.public_a`는 `aws_vpc.main.id`를 참조한다. 이 참조 때문에 **VPC가 먼저 만들어진다.** 실행 순서를 직접 적지 않아도 된다.
- `10.0.1.0/24`는 49장의 CIDR 계산과 같은 방식이다. 호스트 수는 256 − 5(AWS 예약) = 251개다.

---

## 4. 실행 순서: plan → apply

```bash
terraform init      # provider 내려받기, 백엔드 설정
terraform fmt       # 형식 맞추기
terraform validate  # 문법과 참조 검사
terraform plan      # 무엇을 만들고, 바꾸고, 지울지 보여 준다 (변경 없음)
terraform apply     # plan 결과를 실제로 반영한다
```

**규칙:** `plan` 출력에서 `destroy`나 `-/+`(교체)가 보이면 **멈추고 이유를 확인한다.** VPC 같은 리소스를 교체하면 그 안의 서버까지 영향을 받는다.

---

## 5. State: 가장 민감한 파일

state 파일에는 **리소스 ID, 때로는 비밀 값까지** 들어간다.

- 로컬 `terraform.tfstate`를 Git에 커밋하지 않는다. `.gitignore`에 넣는다.
- 팀 작업에는 **원격 백엔드**(예: S3 + DynamoDB 잠금)를 쓴다. 잠금이 없으면 두 사람이 동시에 apply해서 state가 깨진다.
- state를 직접 편집하지 않는다. 필요하면 `terraform state` 명령을 쓴다.

```hcl
terraform {
  backend "s3" {
    bucket         = "khac-tfstate"      # 미리 만들어 둔 버킷
    key            = "network/terraform.tfstate"
    region         = "ap-northeast-2"
    dynamodb_table = "khac-tf-lock"      # 잠금용 테이블
    encrypt        = true
  }
}
```

---

## 6. 비밀 값 취급

- 비밀번호와 액세스 키를 `.tf` 파일에 쓰지 않는다.
- 민감한 변수는 `sensitive = true`로 표시하고, 환경 변수나 비밀 저장소에서 주입한다.
- 공개 저장소에 올리기 전에 `.tfstate`와 `.tfvars` 파일이 제외되었는지 확인한다.

---

## 7. 실습 과제

1. 이 장 3절의 코드를 `main.tf`로 저장하고 `terraform fmt`와 `terraform validate`만 실행한다. (AWS 계정 없이 가능)
2. `vpc_cidr`를 `10.0.0.0/16`에서 `10.0.0.0/8`로 바꾼 뒤 `plan`이 어떤 변경을 보여 주는지 적는다. (실제 적용하지 않는다.)
3. 19장(Spring Core)의 설정 파일과 비교해, 설정을 코드로 관리하는 원칙이 같은지 한 문단으로 적는다.

---

## 단원 통과 기준

- [ ] IaC가 콘솔 작업보다 나은 이유 세 가지를 설명할 수 있다.
- [ ] resource, data source, variable, output을 구분할 수 있다.
- [ ] `plan`과 `apply`의 역할을 설명하고, 교체(`-/+`)가 보이면 멈춰야 하는 이유를 설명할 수 있다.
- [ ] state 파일을 Git에 올리면 안 되는 이유와 원격 백엔드의 잠금 역할을 설명할 수 있다.
