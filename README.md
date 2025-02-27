# 자판기 관리 프로그램

본 자판기 관리 프로그램은 2학년 1학기 JAVA 프로그래밍 실습 과목의 텀 프로젝트 과제로 개발되었습니다. 이 프로그램은 사용자에게 음료를 판매하고, 관리자에게는 음료 및 화폐 관리를 할 수 있는 기능을 제공합니다. 프로그램 개발을 통해 JAVA 프로그래밍 능력을 향상시키고 실제 응용 프로그램 개발 경험을 쌓는 것을 목표로 하였습니다.

https://youtu.be/17Ck7AgB6aY

## 주요 기능

### 음료 관리
- **음료 종류**: 물, 커피, 이온음료, 고급커피, 탄산음료, 특화음료 (총 6종류)
- **가격 및 초기 재고**: 각각 450원, 500원, 550원, 700원, 750원, 800원 / 재고 10개
- **재고 관리**: 품절 시 선택 불가능, 재고 보충 시 판매 재개

### 화폐 관리
- **입력 가능 화폐 단위**: 10원, 50원, 100원, 500원, 1000원
- **입력 상한선**: 지폐 5000원, 총 7000원
- **거스름돈 관리**: 기본 동전 재고 관리, 거스름돈 반환

### 관리자 메뉴
- **접근 방법**: 비밀번호 확인
- **기능**: 매출 산출, 화폐 현황 보충 및 수금, 음료 정보 가격 변경 및 재고, 비밀번호 변경
- **데이터 관리**: 파일 읽기/쓰기를 통한 매출 및 재고 관리 (매출, 재고 소진, 비밀번호)

### 사용자 인터페이스
- **GUI 환경**: GUI 환경에서의 동작
- **화면 분리**: 관리자 및 사용자 화면 분리
- **예외 처리 및 주석**: JAVA 예외 처리 방식 적용, 코드에 주석 포함

## 시스템 구조

### 전체 구조
- [사용자 인터페이스] - [음료 관리 모듈] - [화폐 관리 모듈]
  - [관리자 인터페이스] - [관리자 기능 모듈] - [데이터 관리 모듈]

### 주요 클래스
- `OperateVM`: 자판기 프로그램 실행 메인 클래스
- `VendingMachineGUI`: 자판기 GUI 및 기능 클래스
- `ManagerGUI`: 관리자 GUI 및 기능 클래스
- `MoneyManageGUI`: 화폐 입력 및 거스름돈 GUI 및 기능 클래스
- `DrinkManageGUI`: 음료 재고 관리 GUI 및 기능 클래스
- `PurchaseGUI`: 매출 관리 및 보고 GUI 및 기능 클래스
- `Money`: 화폐 정보 클래스
- `Drink`: 음료 정보 클래스

## 개발 기간
- 2024.05.18. ~ 2024.06.13.

## 기능 설명

### 사용자 인터페이스
- **GUI 구현**: Java Swing 라이브러리를 사용하여 구현
- **주요 화면 구성 요소**: 음료 정보 라벨, 음료 선택 버튼, 화폐 입력 버튼, 잔액 표시, 반환 버튼, 관리자 로그인 버튼

### 음료 관리
- **`Drink` 클래스**: 음료의 이름, 가격, 재고 수량 관리
- **음료 선택 시 재고 감소**: 재고가 0이 되면 품절 표시

### 화폐 관리
- **`Money` 클래스**: 화폐 입력, 거스름돈 계산 및 반환 기능 담당

### 관리자 기능
- **`ManagerGUI` 클래스**: 매출 산출, 화폐 현황 보충 및 수금, 음료 정보 가격 변경 및 재고, 비밀번호 변경 등 기능 제공

### 예외 처리 및 보안
- 모든 입력 필드와 데이터 처리 과정에 적절한 예외 처리 구현
- 관리자 로그인 시 비밀번호 암호화 처리 및 비밀번호 입력 오류 시 명확한 피드백 제공

## 클래스 설명

### VendingMachineGUI
- 사용자 인터페이스를 제공하는 메인 GUI 클래스
- 음료 패널, 구매 버튼 패널, 화폐 버튼 패널, 금액 반환 패널로 구성

### PasswordGUI
- 관리자 로그인 GUI
- SHA-256 해시 함수를 사용하여 비밀번호 암호화

### ManageGUI
- 관리자 기능을 제공하는 GUI
- 비밀번호 변경, 매출 관리, 재고 관리, 화폐 관리 기능 포함

### PasswordChangeGUI
- 비밀번호 변경 GUI
- 비밀번호 조건 검증 및 암호화 저장

### PurchaseGUI
- 매출 관리 GUI
- 일별/월별 매출 데이터를 JTable로 표시

### DrinkManageGUI
- 재고 관리 GUI
- 음료 이름, 가격 변경 및 재고 보충 기능 제공

### MoneyManageGUI
- 화폐 관리 GUI
- 자판기 내 거스름돈 보충 및 수금 기능 제공

### Money
- 화폐 단위와 개수를 필드로 가지는 클래스

### Drink
- 음료 정보를 담고 있는 클래스

## 결과

### VendingMachineGUI
![VendingMachineGUI](https://github.com/zzfbwoals/JAVA-Vending-Machine-Project/blob/main/imgVM/1.png)

### PasswordGUI
![PasswordGUI](https://github.com/zzfbwoals/JAVA-Vending-Machine-Project/blob/main/imgVM/7.png)

### ManagerGUI
![ManagerGUI](https://github.com/zzfbwoals/JAVA-Vending-Machine-Project/blob/main/imgVM/9.png)

### PasswordChagneGUI
![PasswordChangeGUI](https://github.com/zzfbwoals/JAVA-Vending-Machine-Project/blob/main/imgVM/10.png)

### PurchaseGUI
![PurchaseGUI](https://github.com/zzfbwoals/JAVA-Vending-Machine-Project/blob/main/imgVM/12.png)

### DrinkManageGUI
![DrinkManageGUI](https://github.com/zzfbwoals/JAVA-Vending-Machine-Project/blob/main/imgVM/16.png)

### MoneyManageGUI
![MoneyManageGUI](https://github.com/zzfbwoals/JAVA-Vending-Machine-Project/blob/main/imgVM/20.png)
