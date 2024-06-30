# developer
[pro17 페이징 구현] 20240520
* 글 목록이 233개 있을 경우를 가정하고 만들었으나 동적으로 글 목록이 늘어나도록 구현하였습니다.

1. 페이지 번호 구현
페이지는 1-10, 글 목록은 10개씩 보여야한다.
단, 만약 233의 글 목록이 있다면 페이지는 21-30페이지가 아닌 24페이지까지만 보여야한다.

2. next 구현
페이지가 10이 넘을 경우 next(다음 페이지로 이동) 버튼이 보여야한다.

3. pre 구현
섹션이 1일 때(1-10page) pre(이전 페이지로 이동) 버튼이 없어야 하고, 섹션이 2이상일 경우 pre(이전 페
이지로 이동) 버튼이 있어야한다.
단, pre(이전 페이지로 이동) 버튼은 페이지 첫번째 번호(11, 21, 31, ```)에 한번만 나오게 해야한다.
