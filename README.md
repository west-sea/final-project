# Weather Tracker
### Kaist MadCamp week1
- TAB 1 :
  - : 핸드폰 내 연락처를 연동한 전화번호부
- TAB 2 :
  - : 기존 이미지 데이터를 활용한 앱 자체의 이미지 갤러리
- TAB 3 :
  - : 날씨 알리미 및 도우미 챗
---
## TEAM MEM. 

서해린 [west-sea - Overview](https://github.com/west-sea)

우준석 [wminsoo1 - Overview](https://github.com/wminsoo1)

노션 주소 : https://www.notion.so/madcamp/Weather-Tracker-e4126985ab3a47ce80dfe17d8e87031c?pvs=4
## DEVELOPMENT ENV.
- Adobe Illustrator
- Android Studio (Java)
- API source : openWeatherMap, Chat GPT(3.5-Turbo)


- DEVELOPMENT ENV.
    - Adobe Illustrator
    - Android Studio (Java)
    - API source : openWeatherMap, Chat GPT(3.5-Turbo)

## PROJECT DETAIL
### 1.로딩창
로딩 화면 애니메이션을 Lottie 라이브러리를 사용했습니다

### 2.메인화면 
#### 주요 특징
- 로딩창 이후에 나타나는 메인화면으로 하단에 총 세개의 button이 있습니다.
- 각 버튼을 누르면 해당 tab으로 이동합니다.
- 첫번째 button은 tab 1의 전화번호부로 연결됩니다.
- 두번째 button은 tab 2의 이미지 갤러리로 연결됩니다.
- 세번째 button은 tab 3의 weather tracker로 연결됩니다.

#### 기술 설명
- Button.setOnClickListener을 이용해 tab을 이동합니다.

### NUMBER: 연락처
#### 주요 특징
 - 휴대폰 내의 데이터를 가져옵니다.
 - 연락처를 클릭 시 선택한 항목을 수정할 수 있습니다.
 - ADD CONTACT 버튼을 클릭 시 팝업창으로 연락처를 추가할 수 있습니다.
 - 전화 버튼 클릭 시 핸드폰 내 전화 앱이 연동되며, 클릭한 전화번호로 전화를 겁니다.
 - 문자 버튼 클릭 시 핸드폰 내 문자 앱이 연동되며, 클릭한 전화번호로 문자를 보냅니다.
#### 기술 설명
 - PhoneBook 클래스를 만들어 ArrayList<PhoneBook>를 만들었습니다. (PhoneBook에는 name,number 정보가 들어있습니다.)
 - 각각의 연락처를 클릭하면 AlertDialog를 이용해 수정 팝업창으로 이동합니다.
 - Add Contact를 클릭하면 AlertDialog를 이용해 추가 팝업창으로 이동합니다.
 - 전화,문자 버튼을 클릭하면 Intent을 이용해 전화,메세지를 수행합니다.


### IMAGE: 갤러리 
#### 주요 특징
 - 휴대폰 내의 데이터를 가져옵니다.
 - 이미지 클릭 시 이미지에 대한 데이터(제목, 날짜, 설명)를 추가할 수 있습니다.
 - 이미지 오래 클릭 시 이미지가 삭제됩니다.
 - Img Add 클릭 시 갤러리에 있는 사진을 추가합니다.
 - CAM 클릭 시 사진을 찍어 사진을 추가합니다.
#### 기술 설명
 - ImageDetails 클래스를 만들어 ArrayList<ImageDetails>를 만들었습니다. (ImageDetails에는 제목,날짜,설명 정보가 들어있습니다)

### Weather Tracker
####주요 특징
 - openWeatherMap에서 Api를 이용해 현재 위치에 따른 날씨 정보를 업데이트 합니다.


### ChatGpt
#### 주요 특징
 -  Chatgpt에서 Api를 이용해 ChatBot을 구현, 날씨 관련 정보 및 대처법을 chat-gpt turbo-model에게 물어볼 수 있습니다.
