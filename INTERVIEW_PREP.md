# Weather Tracker - 기술면접 대비 문서

## 1. 기술 스택 분석

### 1.1 코어 기술 스택

#### **Android (Java)**
- **사용 이유:**
  - 네이티브 Android 앱 개발을 위한 공식 언어
  - 안정적이고 검증된 생태계
  - 풍부한 라이브러리와 커뮤니티 지원
  - Android Studio와의 완벽한 통합

- **대안 기술:**
  1. **Kotlin** (추천)
     - 장점: 현대적인 문법, null safety, 코루틴, Google의 공식 추천 언어
     - 단점: Java에 비해 학습 곡선이 있을 수 있음

  2. **React Native**
     - 장점: 크로스 플랫폼 개발 가능, JavaScript 생태계 활용
     - 단점: 네이티브 성능 대비 느릴 수 있음, 일부 네이티브 기능 제한

  3. **Flutter**
     - 장점: 빠른 개발 속도, 우수한 UI 성능, 크로스 플랫폼
     - 단점: Dart 언어 학습 필요, 커뮤니티가 상대적으로 작음

- **최적 선택:**
  - **Kotlin**: 현재 Android 개발의 표준이며, Java와 100% 호환되면서 더 안전하고 생산적인 코드 작성 가능
  - 이유: Google이 공식 지원하며, 코루틴을 통한 비동기 처리가 우수하고, null safety로 런타임 에러 감소

#### **Build System: Gradle (Kotlin DSL)**
- **사용 이유:**
  - Android 공식 빌드 시스템
  - 의존성 관리 자동화
  - Kotlin DSL로 타입 안정성 제공

- **주요 설정:**
  ```kotlin
  compileSdk = 34
  minSdk = 24
  targetSdk = 33
  Java Version = 1.8
  ```

### 1.2 UI 프레임워크 & 라이브러리

#### **Material Design Components (1.11.0)**
- **사용 이유:**
  - Google의 공식 디자인 가이드라인 구현
  - 일관된 UX 제공
  - 접근성과 사용성 향상

#### **ConstraintLayout (2.1.4)**
- **사용 이유:**
  - 복잡한 레이아웃을 평면 계층으로 구성 가능
  - 성능 최적화 (중첩 레이아웃 감소)
  - 반응형 UI 구현 용이

#### **CardView (1.0.0)**
- **사용 이유:**
  - Material Design 카드 패턴 구현
  - 그림자 효과와 모서리 둥글게 처리
  - 리스트 아이템 표시에 적합

#### **RecyclerView (AndroidX 포함)**
- **사용 이유:**
  - 대량의 데이터를 효율적으로 표시
  - 뷰 재사용으로 메모리 최적화
  - 채팅 메시지 목록 구현에 사용

### 1.3 애니메이션 라이브러리

#### **Lottie (5.0.2)**
- **사용 이유:**
  - After Effects 애니메이션을 JSON으로 내보내 사용
  - 벡터 기반으로 용량이 작고 품질 손실 없음
  - 다양한 날씨 애니메이션 표현 (맑음, 흐림, 비, 눈)

- **사용 위치:**
  - 로딩 화면 (open.json)
  - 날씨별 애니메이션 (sunny.json, cloud.json, rainy.json, snow.json)
  - 로딩 스피너 (loading.json)

- **대안:**
  - **Android Vector Drawable**: 간단한 애니메이션, 네이티브 지원
  - **GIF**: 용량이 크고 품질이 떨어짐
  - **Rive**: 더 복잡한 인터랙티브 애니메이션 가능하지만 학습 곡선 높음

### 1.4 이미지 처리 라이브러리

#### **Glide (4.12.0)**
- **사용 이유:**
  - 효율적인 이미지 로딩 및 캐싱
  - 메모리 관리 자동화
  - 다양한 이미지 소스 지원 (URL, URI, 로컬 파일)
  - 썸네일 생성 및 이미지 변환

- **사용 위치:**
  - 갤러리 이미지 로딩
  - GridView 이미지 표시

- **대안:**
  1. **Picasso**: 가볍지만 Glide보다 기능이 적음
  2. **Coil**: Kotlin 최적화, 가볍고 빠름 (Kotlin 프로젝트에 추천)
  3. **Fresco**: 대용량 이미지 처리에 강하지만 무거움

- **최적 선택:**
  - Java 프로젝트: **Glide** (현재 사용 중)
  - Kotlin 프로젝트: **Coil** (더 현대적이고 경량)

### 1.5 네트워킹 라이브러리

#### **Volley (1.2.1)**
- **사용 이유:**
  - Google 공식 HTTP 라이브러리
  - 간단한 REST API 호출에 적합
  - 자동 요청 스케줄링
  - RequestQueue로 효율적 관리

- **사용 위치:**
  - OpenWeatherMap API 호출

- **대안:**
  1. **Retrofit** (추천)
     - 장점: 타입 안전성, 자동 JSON 파싱, RxJava/Coroutine 통합
     - 단점: 설정이 복잡할 수 있음
  2. **OkHttp**: 저수준 HTTP 클라이언트, 더 많은 제어 가능
  3. **Ktor Client**: Kotlin 네이티브, 코루틴 기반

#### **OkHttp (4.10.0)**
- **사용 이유:**
  - 효율적인 HTTP/HTTPS 통신
  - 연결 풀링으로 성능 향상
  - GZIP 압축 자동 지원
  - 인터셉터로 로깅 및 인증 처리

- **사용 위치:**
  - ChatGPT API 호출

- **최적 선택:**
  - **Retrofit + OkHttp + Coroutines**: 현대적인 Android 개발의 표준
  - 이유: 타입 안전성, 코루틴을 통한 비동기 처리, 코드 가독성 향상

### 1.6 ML/AI 라이브러리

#### **Firebase ML Vision (24.0.3)**
- **사용 이유:**
  - 기기 내 머신러닝 수행
  - 얼굴 인식 기능
  - Google의 사전 학습된 모델 활용

- **사용 위치:**
  - 이미지 분석 (현재 코드에서는 미사용으로 보임)

- **참고:**
  - Firebase ML Vision은 deprecated 되었으며, ML Kit로 마이그레이션 권장

### 1.7 데이터 저장

#### **SharedPreferences**
- **사용 이유:**
  - 간단한 키-값 저장소
  - 소량의 데이터 영구 저장
  - 앱 설정 및 상태 저장

- **사용 위치:**
  - 이미지 메타데이터 저장 (제목, 날짜, 설명)

- **대안:**
  1. **Room Database**: 복잡한 데이터 구조, SQL 쿼리
  2. **DataStore**: SharedPreferences의 현대적 대체
  3. **SQLite**: 직접 SQL 작성

### 1.8 외부 API

#### **OpenWeatherMap API**
- **사용 이유:**
  - 무료로 실시간 날씨 데이터 제공
  - GPS 좌표 기반 날씨 정보
  - JSON 형식의 명확한 응답

#### **OpenAI API (GPT-3.5 Turbo)**
- **사용 이유:**
  - 자연어 처리 및 대화형 AI
  - 날씨 관련 질문 답변

- **참고:**
  - API 키가 코드에 하드코딩되어 있음 (보안 위험)
  - 환경 변수나 BuildConfig로 관리해야 함

---

## 2. 앱 기능 분석

### 2.1 Splash Screen (로딩 화면)

#### **구현 클래스:**
- `SplashActivity.java`

#### **사용 기술:**
- Lottie Animation
- Handler for delayed navigation

#### **기능:**
- 앱 시작 시 로딩 애니메이션 표시
- 일정 시간 후 MainActivity로 전환

#### **사용 이유:**
- 초기 데이터 로딩 시간 확보
- 브랜드 인지도 향상
- 사용자 경험 개선

#### **개선 방안:**
- 실제 데이터 로딩 작업과 연동
- 프로그레스 표시

---

### 2.2 Main Activity (메인 화면)

#### **구현 클래스:**
- `MainActivity.java`

#### **사용 기술:**
- Lottie Animation (open.json)
- Intent for navigation
- Button.setOnClickListener

#### **기능:**
1. 3개의 탭 버튼 표시
   - 전화번호부 (NUM)
   - 이미지 갤러리 (IMG)
   - 날씨 트래커 (FREE)

2. 각 버튼 클릭 시 해당 Activity로 이동

#### **구현 방식:**
```java
Button numButton = findViewById(R.id.numbut);
numButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), LinkNumActivity.class);
        startActivity(intent);
    }
});
```

---

### 2.3 TAB 1: 전화번호부 (Contact Manager)

#### **구현 클래스:**
- `NumActivity.java`
- `LinkNumActivity.java`
- `ContactAddActivity.java`
- `ListAdapter.java` (Adapter)
- `PhoneBook.java` (Model)

#### **사용 기술:**
1. **ListView**: 연락처 목록 표시
2. **Custom Adapter**: 연락처 데이터 바인딩
3. **AlertDialog**: 연락처 수정 팝업
4. **Intent (ACTION_CALL, ACTION_SENDTO)**: 전화/문자 앱 연동
5. **ContentResolver**: 기기 연락처 읽기

#### **주요 기능:**

##### **2.3.1 연락처 목록 표시**
- **사용 라이브러리:** ListView, Custom Adapter
- **데이터 모델:** `PhoneBook` (name, number)
- **구현 방식:** ArrayList<PhoneBook>를 Adapter로 바인딩

##### **2.3.2 연락처 추가**
- **UI:** AlertDialog 팝업
- **입력 필드:** 이름, 전화번호
- **데이터 저장:** ArrayList에 추가

##### **2.3.3 연락처 수정**
- **트리거:** ListView 아이템 클릭
- **UI:** AlertDialog로 수정 가능

##### **2.3.4 전화 걸기**
- **사용 API:** `Intent.ACTION_CALL`
- **권한:** `CALL_PHONE`
- **구현:** 전화 버튼 클릭 시 Intent로 전화 앱 실행

##### **2.3.5 문자 보내기**
- **사용 API:** `Intent.ACTION_SENDTO`
- **권한:** `SEND_SMS`
- **구현:** 문자 버튼 클릭 시 Intent로 문자 앱 실행

#### **사용한 Android 컴포넌트:**
- **ContentProvider**: 기기 연락처 접근
- **Intent**: 전화/문자 앱 연동
- **AlertDialog**: 팝업 UI

#### **개선 방안:**
1. Room Database로 연락처 영구 저장
2. 검색 기능 추가
3. RecyclerView로 성능 최적화
4. 연락처 삭제 기능

---

### 2.4 TAB 2: 이미지 갤러리 (Image Gallery)

#### **구현 클래스:**
- `ImgActivity.java`
- `ImageAdapter.java` (Adapter)
- `ImageDetails.java` (Model)

#### **사용 기술:**
1. **GridView**: 이미지 그리드 레이아웃
2. **Glide**: 이미지 로딩 및 캐싱
3. **Intent (ACTION_PICK)**: 갤러리 이미지 선택
4. **Intent (ACTION_IMAGE_CAPTURE)**: 카메라로 사진 촬영
5. **SharedPreferences**: 이미지 메타데이터 저장 (JSON)
6. **DatePickerDialog**: 날짜 선택 UI
7. **AlertDialog**: 이미지 정보 입력 팝업

#### **주요 기능:**

##### **2.4.1 갤러리에서 이미지 추가**
- **트리거:** "IMG ADD" 버튼 클릭
- **권한:** `READ_EXTERNAL_STORAGE`
- **사용 API:** `Intent.ACTION_PICK`
- **구현:**
  ```java
  Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
  startActivityForResult(intent, PICK_IMAGE_REQUEST);
  ```

##### **2.4.2 카메라로 사진 촬영**
- **트리거:** "CAM" 버튼 클릭
- **권한:** `CAMERA`
- **사용 API:** `MediaStore.ACTION_IMAGE_CAPTURE`
- **구현:** 카메라 인텐트로 사진 촬영 후 Bitmap 저장

##### **2.4.3 이미지 정보 추가/수정**
- **트리거:** 이미지 클릭
- **UI:** AlertDialog (커스텀 레이아웃)
- **입력 필드:**
  - 이미지 제목 (EditText)
  - 날짜 (DatePickerDialog)
  - 설명 (EditText)
- **데이터 모델:** `ImageDetails` (imageName, selectedDate, description)

##### **2.4.4 이미지 삭제**
- **트리거:** 이미지 롱 클릭
- **구현:** `OnItemLongClickListener`
- **동작:** ArrayList에서 제거 후 Adapter 갱신

##### **2.4.5 데이터 영구 저장**
- **사용 기술:** SharedPreferences + JSON
- **저장 시점:**
  - 이미지 추가 시
  - 정보 수정 시
  - Activity 종료 시 (onDestroy)
- **구현:**
  ```java
  JSONArray jsonArray = new JSONArray();
  for (ImageDetails details : imageDetailsList) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("id", details.getId());
      jsonObject.put("imageName", details.getImageName());
      // ...
      jsonArray.put(jsonObject);
  }
  editor.putString("imageDetails", jsonArray.toString());
  ```

#### **사용한 라이브러리:**
- **Glide**: 이미지 로딩, 캐싱, 리사이징
- **JSON**: 데이터 직렬화

#### **개선 방안:**
1. RecyclerView로 성능 최적화
2. Room Database 사용
3. 이미지 필터링/정렬 기능
4. 클라우드 백업 (Firebase Storage)
5. 이미지 압축으로 저장 공간 절약

---

### 2.5 TAB 3: 날씨 트래커 (Weather Tracker)

#### **구현 클래스:**
- `FreeActivity.java`

#### **사용 기술:**
1. **LocationManager**: GPS 위치 정보
2. **Volley**: HTTP 요청
3. **OpenWeatherMap API**: 날씨 데이터
4. **Lottie**: 날씨별 애니메이션
5. **Handler**: 주기적 데이터 업데이트
6. **SimpleDateFormat**: 날짜/시간 포맷팅

#### **주요 기능:**

##### **2.5.1 현재 위치 기반 날씨 정보**
- **권한:**
  - `ACCESS_FINE_LOCATION`
  - `ACCESS_COARSE_LOCATION`
- **위치 제공자:**
  - GPS_PROVIDER
  - NETWORK_PROVIDER
- **구현:**
  ```java
  LocationListener locationListener = new LocationListener() {
      public void onLocationChanged(Location location) {
          double cur_lat = location.getLatitude();
          double cur_lon = location.getLongitude();
          url = "https://api.openweathermap.org/data/2.5/weather?lat=" + cur_lat + "&lon=" + cur_lon + "&appid=API_KEY";
          fetchWeatherData(url);
      }
  };
  ```

##### **2.5.2 날씨 데이터 가져오기**
- **API:** OpenWeatherMap Current Weather API
- **HTTP 라이브러리:** Volley
- **요청 방식:** GET
- **응답 파싱:** JSONObject
- **표시 정보:**
  - 도시 이름
  - 날씨 상태 (Clear, Clouds, Rain, Snow)
  - 현재 온도 (Kelvin → Celsius 변환)
  - 현재 시간

##### **2.5.3 날씨별 애니메이션**
- **사용 라이브러리:** Lottie
- **조건부 애니메이션:**
  - Clear → sunny.json
  - Clouds → cloud.json
  - Rain → rainy.json
  - Snow → snow.json
  - Default → default.json
- **구현:**
  ```java
  if (weather.equals("Clear")) {
      animationView.setAnimation("sunny.json");
  } else if(weather.equals("Clouds")) {
      animationView.setAnimation("cloud.json");
  }
  animationView.playAnimation();
  animationView.loop(true);
  ```

##### **2.5.4 실시간 업데이트**
- **사용 기술:** Handler + Runnable
- **업데이트 주기:** 1초 (UPDATE_INTERVAL = 1000ms)
- **구현:**
  ```java
  private final Runnable updateDataRunnable = new Runnable() {
      @Override
      public void run() {
          CurrentCall();
          handler.postDelayed(this, UPDATE_INTERVAL);
      }
  };
  ```

##### **2.5.5 빠른 액션 버튼**
- **전화 버튼:** 전화 앱 실행
- **카메라 버튼:** 카메라 앱 실행
- **채팅 버튼:** ChatActivity 실행
- **새로고침 버튼:** 날씨 데이터 수동 업데이트

#### **사용한 Android API:**
- **Location Services**: GPS 위치
- **Volley HTTP**: API 호출
- **Handler/Looper**: 주기적 작업

#### **개선 방안:**
1. **코루틴 사용**: Handler 대신 더 안전한 비동기 처리
2. **Retrofit 사용**: Volley 대신 타입 안전성 향상
3. **ViewModel + LiveData**: MVVM 패턴으로 아키텍처 개선
4. **캐싱**: 네트워크 오류 시 마지막 데이터 표시
5. **배터리 최적화**: 업데이트 주기 조정 (1초는 너무 짧음)
6. **다양한 날씨 정보**: 습도, 풍속, 일출/일몰 시간 등

---

### 2.6 ChatGPT 챗봇

#### **구현 클래스:**
- `ChatActivity.java`
- `MessageAdapter.java` (Adapter)
- `Message.java` (Model)

#### **사용 기술:**
1. **RecyclerView**: 채팅 메시지 목록
2. **OkHttp**: HTTP 클라이언트
3. **OpenAI API**: GPT-3.5 텍스트 생성
4. **JSON**: API 요청/응답 파싱
5. **Custom Adapter**: 메시지 UI 바인딩

#### **주요 기능:**

##### **2.6.1 채팅 UI**
- **레이아웃:** RecyclerView with LinearLayoutManager
- **메시지 타입:**
  - SENT_BY_ME: 사용자 메시지
  - SENT_BY_BOT: AI 응답
- **스크롤 동작:** 새 메시지 추가 시 하단으로 자동 스크롤

##### **2.6.2 OpenAI API 호출**
- **엔드포인트:** `https://api.openai.com/v1/completions`
- **HTTP 메서드:** POST
- **인증:** Bearer 토큰 (API 키)
- **요청 본문:**
  ```json
  {
    "model": "text-davinci-003",
    "prompt": "사용자 질문",
    "max_tokens": 4000,
    "temperature": 0
  }
  ```
- **HTTP 클라이언트:** OkHttp

##### **2.6.3 비동기 처리**
- **사용 기술:** OkHttp Callback
- **스레딩:**
  - 네트워크 요청: 백그라운드 스레드
  - UI 업데이트: runOnUiThread()
- **구현:**
  ```java
  client.newCall(request).enqueue(new Callback() {
      @Override
      public void onResponse(@NonNull Call call, @NonNull Response response) {
          // 백그라운드 스레드
          JSONObject jsonObject = new JSONObject(response.body().string());
          String result = jsonArray.getJSONObject(0).getString("text");
          addResponse(result.trim());  // runOnUiThread 내부에서 UI 업데이트
      }
  });
  ```

##### **2.6.4 로딩 표시**
- **동작:** API 호출 시 "..." 메시지 표시
- **완료:** 응답 받으면 "..." 제거하고 실제 응답 추가

##### **2.6.5 에러 처리**
- **네트워크 오류:** "Failed to load response due to ..." 메시지 표시
- **API 오류:** 응답 본문의 에러 메시지 표시

#### **사용한 라이브러리:**
- **OkHttp (4.10.0)**: HTTP 통신
- **RecyclerView**: 효율적인 리스트 표시
- **JSON**: 데이터 파싱

#### **보안 이슈:**
- **API 키 노출:** 소스 코드에 하드코딩됨
- **해결 방법:**
  1. BuildConfig에 저장
  2. 환경 변수 사용
  3. 백엔드 서버를 통한 프록시 요청

#### **개선 방안:**
1. **GPT-4 사용**: 더 정확한 응답
2. **스트리밍 응답**: 응답을 실시간으로 표시
3. **대화 컨텍스트 유지**: 이전 대화 기억
4. **음성 입력**: Speech-to-Text
5. **메시지 저장**: Room Database
6. **백엔드 프록시**: API 키 보안 강화

---

## 3. 아키텍처 및 디자인 패턴

### 3.1 현재 아키텍처
- **패턴:** MVC (Model-View-Controller)
  - Model: PhoneBook, ImageDetails, Message
  - View: XML Layouts
  - Controller: Activities

### 3.2 개선 제안: MVVM (Model-View-ViewModel)

#### **장점:**
1. **관심사 분리**: UI 로직과 비즈니스 로직 분리
2. **테스트 용이성**: ViewModel 단위 테스트 가능
3. **생명주기 관리**: ViewModel이 구성 변경(회전) 시에도 데이터 유지
4. **LiveData/Flow**: 반응형 데이터 스트림

#### **필요한 라이브러리:**
```kotlin
// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
// LiveData
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
// Room Database
implementation("androidx.room:room-runtime:2.6.0")
kapt("androidx.room:room-compiler:2.6.0")
// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

---

## 4. 권한 관리

### 4.1 사용 중인 권한

#### **위험 권한 (런타임 요청 필요):**
1. `CALL_PHONE`: 전화 걸기
2. `CAMERA`: 카메라 접근
3. `ACCESS_FINE_LOCATION`: 정확한 위치
4. `ACCESS_COARSE_LOCATION`: 대략적 위치
5. `ACCESS_BACKGROUND_LOCATION`: 백그라운드 위치 (미사용)
6. `WRITE_EXTERNAL_STORAGE`: 외부 저장소 쓰기
7. `READ_EXTERNAL_STORAGE`: 외부 저장소 읽기
8. `SEND_SMS`: 문자 보내기
9. `READ_CONTACTS`: 연락처 읽기

#### **일반 권한:**
- `INTERNET`: 네트워크 접근

### 4.2 권한 요청 구현

#### **패턴:**
```java
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
} else {
    // 권한 있음
}
```

#### **결과 처리:**
```java
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // 권한 승인
    } else {
        // 권한 거부 - 설정으로 이동 유도
    }
}
```

---

## 5. 데이터 흐름 및 상태 관리

### 5.1 데이터 저장 방식

| 기능 | 저장 방식 | 데이터 |
|------|----------|--------|
| 연락처 | 메모리 (ArrayList) | PhoneBook 리스트 |
| 이미지 메타데이터 | SharedPreferences (JSON) | ImageDetails 리스트 |
| 채팅 메시지 | 메모리 (ArrayList) | Message 리스트 |
| 날씨 데이터 | 메모리 (TextView) | API 응답 |

### 5.2 문제점
1. **영구성 부족**: 앱 종료 시 데이터 손실 (이미지 메타데이터 제외)
2. **동기화 이슈**: SharedPreferences는 메인 스레드 블로킹
3. **확장성**: JSON 수동 파싱은 에러 발생 위험

### 5.3 개선 방안
- **Room Database 도입**:
  - 타입 안전성
  - SQL 쿼리 최적화
  - LiveData 통합

---

## 6. 성능 최적화 포인트

### 6.1 현재 성능 이슈

1. **메모리 누수:**
   - Handler가 Activity 참조 시 메모리 누수 가능
   - 해결: WeakReference 또는 코루틴 사용

2. **과도한 API 호출:**
   - 날씨 API를 1초마다 호출 (불필요)
   - 해결: 5-10분 간격으로 변경, 캐싱 추가

3. **ListView vs RecyclerView:**
   - ListView는 뷰 재사용 최적화가 약함
   - 해결: RecyclerView로 마이그레이션

4. **이미지 로딩:**
   - Glide 사용 중이지만 썸네일 크기 최적화 필요
   - 해결: override(width, height) 설정

### 6.2 최적화 체크리스트
- [ ] RecyclerView로 마이그레이션
- [ ] 코루틴으로 비동기 처리
- [ ] 이미지 썸네일 크기 지정
- [ ] API 호출 주기 조정 및 캐싱
- [ ] Room Database 도입
- [ ] ProGuard/R8 코드 난독화 및 최적화

---

## 7. 보안 고려사항

### 7.1 현재 보안 이슈

1. **API 키 노출:**
   - OpenAI API 키가 소스 코드에 하드코딩
   - OpenWeatherMap API 키도 노출

2. **권한 과다 요청:**
   - `ACCESS_BACKGROUND_LOCATION`은 사용하지 않음

3. **HTTPS 강제 미적용:**
   - 네트워크 보안 설정 없음

### 7.2 보안 강화 방안

1. **API 키 보호:**
   ```kotlin
   // local.properties에 저장
   OPENAI_API_KEY=sk-xxx

   // build.gradle.kts
   buildConfigField("String", "OPENAI_API_KEY", "\"${project.findProperty("OPENAI_API_KEY")}\"")

   // 코드에서 사용
   BuildConfig.OPENAI_API_KEY
   ```

2. **네트워크 보안 설정:**
   ```xml
   <!-- res/xml/network_security_config.xml -->
   <network-security-config>
       <base-config cleartextTrafficPermitted="false" />
   </network-security-config>
   ```

3. **ProGuard 난독화:**
   ```
   isMinifyEnabled = true
   ```

---

## 8. 테스트 전략

### 8.1 단위 테스트
- JUnit (4.13.2) 사용 중
- **테스트 대상:**
  - ViewModel 로직
  - 데이터 변환 (Kelvin → Celsius)
  - JSON 파싱

### 8.2 UI 테스트
- Espresso (3.5.1) 사용 중
- **테스트 시나리오:**
  - 버튼 클릭 네비게이션
  - 리스트 아이템 클릭
  - 다이얼로그 입력

### 8.3 통합 테스트
- API Mock 테스트 (MockWebServer)
- Room Database 테스트

---

## 9. 빌드 및 배포

### 9.1 빌드 타입
- **Debug**: 개발용, 디버깅 가능
- **Release**: 배포용, 코드 최적화

### 9.2 APK 서명
- 키스토어 생성 및 관리
- Google Play 업로드 키

### 9.3 CI/CD
- GitHub Actions
- 자동 빌드 및 테스트
- Firebase App Distribution

---

## 10. 면접 예상 질문 및 답변

### Q1: 왜 Volley와 OkHttp를 동시에 사용했나요?
**A:**
- **Volley**: 간단한 REST API 호출 (OpenWeatherMap)에 적합하며, RequestQueue로 효율적 관리
- **OkHttp**: OpenAI API는 더 복잡한 요청/응답 처리가 필요하며, OkHttp의 Callback이 세밀한 제어 가능
- **개선 방안**: Retrofit으로 통일하여 타입 안전성과 코드 일관성 향상

### Q2: SharedPreferences에 JSON을 저장하는 방식의 문제점은?
**A:**
- **문제점:**
  1. 메인 스레드 블로킹 (commit 사용 시)
  2. 수동 JSON 파싱으로 에러 위험
  3. 복잡한 쿼리 불가능
  4. 동시성 문제 (멀티스레딩 시)
- **해결책:** Room Database + Coroutines

### Q3: 날씨를 1초마다 업데이트하는 이유는?
**A:**
- **현재 구현:** 실시간 데이터 반영 목적
- **문제점:** 배터리 소모, 불필요한 API 호출
- **개선:**
  - 5-10분 간격으로 변경
  - WorkManager로 백그라운드 업데이트
  - API 응답 캐싱 (30분)

### Q4: MVVM 패턴으로 마이그레이션하려면?
**A:**
1. ViewModel 생성: UI 로직 분리
2. LiveData/Flow: 반응형 데이터 스트림
3. Repository 패턴: 데이터 소스 추상화
4. Room Database: 데이터 영속성
5. Dependency Injection: Hilt/Koin

### Q5: Glide를 선택한 이유와 Coil의 장점은?
**A:**
- **Glide 장점:** 성숙한 라이브러리, 풍부한 기능, Java 프로젝트에 적합
- **Coil 장점:** Kotlin 최적화, 경량, 코루틴 네이티브, 간결한 API
- **선택 기준:** Kotlin 프로젝트라면 Coil, Java 프로젝트는 Glide

### Q6: API 키 보안을 어떻게 개선하시겠습니까?
**A:**
1. **즉시 조치:** BuildConfig에 저장, local.properties에서 읽기
2. **중기:** 백엔드 프록시 서버 구축, API 키는 서버에만 저장
3. **장기:** Firebase Functions로 서버리스 프록시, Rate Limiting

### Q7: RecyclerView와 ListView의 차이는?
**A:**
- **RecyclerView:**
  - ViewHolder 패턴 강제 (성능 향상)
  - LayoutManager로 다양한 레이아웃 (Linear, Grid, Staggered)
  - ItemAnimator로 애니메이션
  - DiffUtil로 효율적 업데이트
- **ListView:**
  - 간단한 리스트에 적합
  - ViewHolder 패턴 선택적
  - 세로 스크롤만 가능

### Q8: 코루틴과 Handler의 차이는?
**A:**
- **Handler:**
  - 저수준 API, 메모리 누수 위험
  - 콜백 지옥 가능
- **Coroutines:**
  - 구조화된 동시성 (Structured Concurrency)
  - 순차적 코드 작성
  - 자동 취소 및 예외 처리
  - 코드 가독성 향상

---

## 11. 추가 학습 주제

### 11.1 Android 아키텍처 컴포넌트
- ViewModel
- LiveData
- Room
- WorkManager
- Navigation Component

### 11.2 Jetpack Compose
- 선언형 UI
- Kotlin DSL
- 상태 관리

### 11.3 Kotlin Coroutines & Flow
- suspend functions
- Flow vs LiveData
- Structured Concurrency

### 11.4 의존성 주입
- Hilt
- Koin

### 11.5 현대적 네트워킹
- Retrofit
- Ktor
- GraphQL

---

## 12. 프로젝트 강점 정리

### 12.1 기술적 강점
1. ✅ 다양한 Android API 활용 (Location, Camera, Contacts)
2. ✅ REST API 통합 (OpenWeatherMap, OpenAI)
3. ✅ 커스텀 UI 컴포넌트 (Adapter, Dialog)
4. ✅ 애니메이션 활용 (Lottie)
5. ✅ 권한 관리 구현

### 12.2 개선 가능 영역
1. 📌 MVVM 아키텍처 도입
2. 📌 Room Database 마이그레이션
3. 📌 Kotlin 전환
4. 📌 코루틴으로 비동기 처리
5. 📌 Retrofit으로 네트워킹 통일
6. 📌 API 키 보안 강화
7. 📌 테스트 커버리지 확대

---

## 13. 면접 시 강조할 점

1. **다양한 Android 컴포넌트 경험**
   - Location Services, Camera, Contacts, Storage

2. **외부 API 통합 능력**
   - RESTful API 호출, JSON 파싱, 에러 처리

3. **사용자 경험 중시**
   - Lottie 애니메이션, 직관적 UI, 권한 설명

4. **문제 해결 능력**
   - 이미지 메타데이터 저장을 SharedPreferences로 해결
   - 다양한 날씨 상태에 맞는 애니메이션 매핑

5. **개선 방향 인지**
   - MVVM, Room, Coroutines 등 현대적 기술 스택 이해
   - 보안 및 성능 최적화 필요성 인지

---

## 14. 마무리

이 프로젝트는 Android 기본 개념을 잘 이해하고 있음을 보여줍니다. 면접에서는 **사용한 기술을 왜 선택했는지**, **어떤 문제를 해결했는지**, **어떻게 개선할 수 있는지**를 명확히 설명하는 것이 중요합니다.

현재 구조에서 MVVM + Room + Coroutines로 마이그레이션하는 과정을 설명할 수 있다면, 면접관에게 **학습 능력과 개선 의지**를 강하게 어필할 수 있습니다.

화이팅하세요! 🚀
