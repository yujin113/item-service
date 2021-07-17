# Spring으로 crud 기능 구현

### 상품 도메인 개발
1. Item - 상품 객체
2. ItemRepository - 상품 저장소
3. ItemRepositoryTest - 상품 저장소 테스트

### 상품 목록 - 타임리프 이용
타임리프는 순수 HTML을 그대로 유지하면서 뷰 템플릿도 사용할 수 있는 네츄럴 템플릿의 특징을 가지고 있다
1. 속성 변경 - th:href, th: onclick   
   타임리프 뷰 템플릿을 거치면 원래 값을 th:xxx 값으로 변경시킴   
   th:xxx가 붙은 부분은 서버사이드에서 렌더링되고, 기존 것을 대체
   
2. URL 링크 표현식 - @{...}   
   ```
   th:href="@{/basic/items/{itemId}(itemId=${item.id})}"
   th:href="@{|/basic/items/${item.id}|}"
   ```
   URL 링크를 사용하는 경우 @{ }안에 링크 넣기    
   () 안에 경로 변수 뿐만 아니라 쿼리 파라미터도 생성 가능    
   리터럴 대체 문법 사용해서 간단히 표현도 가
   
3. 리터럴 대체 - |...|   
   타임리프에서 문자와 표현식 등은 분리되어 있기 때문에 더해서 사용해야 함    
   리터럴 대체 문법을 사용하면, 더하기 없이 편리하게 사용할 수 있음
   ```
   th:onclick="'location.href=' + '/'' + @{/basic/items/add} + '/''"
   th:onclick="|location.href='@{/basic/items/add}'|"
   ```

4. 반복 출력 - th:each    
   ```
   <tr th:each="item : ${items}">
   ```
   모델에 포함된 items 컬렉션 데이터가 item 변수에 하나씩 포함되고, 반복문 안에서 item 변수를 이용할 수 있음

5. 변수 표현식 - ${...}    
   모델에 포함된 값이나, 타임리프 변수로 선언한 값을 조회할 수 있음    
   프로퍼티 접근법 사용
   
6. 내용 변경 - th:text    
   ```
   <td th:text="${item.price}">10000</td>
   ```
   내용의 값을 th:text의 값으로 변경
   10000을 ${item.price} 값으로 변경하게 됨
   
### 상품 상세
#### @GetMapping("/{itemId}")
PathVariable로 넘어온 상품 ID로 상품 조회 후 모델에 담고, 뷰 템플릿 호출

#### @GetMapping("/add")
상품 등록 폼은 단순히 뷰 템플릿만 호출

#### @PostMapping("/add")
4가지 버전으로 개발
- addItemV1    
  @RequestParam으로 요청 파라미터 데이터를 변수에 받음    
  Item 객체 생성하고 itemRepository 통해 저장    
  저장된 item을 Model에 담아서 뷰로 전달
  
- addItemV2    
  @ModelAttribute로 한 번에 처리   
  : Item 객체 생성 후 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해줌
  : Model에 @ModelAttribute로 지정한 객체를 자동으로 넣어줌 (따로 model.addAttribute를 하지 않아도 됨)
  
- addItemV3    
  ModelAttribute 이름 생략 가능    
  Model에 저장될 때 클래스명의 첫 글자만 소문자로 변경해서 등록해줌
  
- addItemV4      
  ModelAttribute 전체 생략
  @ModelAttribute 자체도 생략 가능, 대상 객체는 Model에 자동 등록
  
#### @GetMapping("/{itemId}/edit")
수정에 필요한 정보 조회 후, 수정용 폼 뷰 호출

#### @PostMapping("/{itemId}/edit")
PathVariable로 넘어온 상품 ID를 이용하여 수정한 객체를 itemRepository 통해 update    
상품 상세 화면으로 이동하도록 redirect 호출    
스프링은 redirect:/... 로 편리하게 리다이렉트 지원     
컨트롤러에 매핑된 @PathVariable의 값은 redirect에도 사용 가능

#### @RequestMapping("/{itemId}/delete")
PathVaribale로 넘어온 상품 ID를 이용하여 itemRepository 통해 delete    
삭제 후 상품 전체 화면으로 redirect

