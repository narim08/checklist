2025.01.09
v1

메인 페이지, 날짜 페이지 생성
메인 페이지: 1월 1일부터 100개의 날짜가 칸으로 생성되고, 날짜 페이지에서 모든 체크리스트가 완료되면 메인페이지에서 해당 칸은 녹색으로 바뀐다. 또한 상단에 진행바로 완료일/100일로 표시된다.
날짜 페이지: 모두 동일한 3개의 항목의 체크리스트가 존재한다. 공부, 산책, 운동으로 구성되며 모두 완료할 시 메인 페이지에 반영된다.
![스크린샷 2025-01-12 163639](https://github.com/user-attachments/assets/c4c4c454-a2bb-49ed-bfe5-5f13f72f0714)
![스크린샷 2025-01-11 205830](https://github.com/user-attachments/assets/a2c376e2-1252-46aa-8654-09fd328d2254)


=====================================

2025.01.12

cloudType 프리티어를 통한 서버 배포 완료

=====================================

2025.01.13
v2

날짜 페이지: 산책 경로 저장 기능 추가
카카오 지도 api 활용, 사용자 현위치 기반으로 지도가 나타난다. 클릭시 마커로 위도, 경도 정보가 서버에 저장된다. 지도 위에 <경로 보기> 버튼을 클릭하면 마커끼리 선으로 이어져서 표시된다. 우클릭으로 마커를 각각 지울 수 있으며, <경로 지우기> 버튼을 통해 모든 마커를 삭제할 수 있다.(모바일 지원)
![스크린샷 2025-01-16 214011](https://github.com/user-attachments/assets/53bc8e56-b553-4782-a970-02a8c329fab1)


=====================================

2025.01.14
v3

날짜 페이지: 하루 요약 일기 기능 추가
체크리스트 완료 여부, 마커 위치 정보들을 바탕으로 일기를 생성해준다.
초기: huggingface gpt-2 이용하여 자동 생성 -> 답변이 적절하지 못함
수정: 체크리스트에 대한 내용은 조건문으로 정해진 답변 출력 + 위치는 좌표를 주소로 변환(카카오 지도 Rest API를 통해 address_name 추출)
![스크린샷 2025-01-21 122358](https://github.com/user-attachments/assets/8276fdac-c8c5-4fb1-9a11-6c36e26fe1b8)


=====================================

2025.01.15

메인 페이지: 체크리스트를 3개 모두 완료하면 녹색으로 표시하던 기존 버전에서 1~2개 완료시엔 노란색으로 표시하도록 추가
![스크린샷 2025-01-21 122653](https://github.com/user-attachments/assets/4dc4e9f1-7879-43b3-a876-1e629f256bc7)



=====================================

-ERD 설계
![스크린샷 2025-01-23 134346](https://github.com/user-attachments/assets/e7275c11-b78c-4e63-b34d-c4f08dd96387)


-API 명세서
![image](https://github.com/user-attachments/assets/bb2b8eb8-1bac-49d5-ab7b-865fd8a94529)
