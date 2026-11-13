TRUNCATE TABLE benefits RESTART IDENTITY CASCADE;

INSERT INTO benefits (title, category, summary, start_date, end_date, target_condition, apply_method, official_url, status, view_count, bookmark_count, click_count)
VALUES
    ('청년 미래적금, 이번주 금요일 마감', 'FINANCE', '월 최대 50만 원 납입 시 정부 기여금 지원', NULL, CURRENT_DATE + 2,
     '{"ageMin":19,"ageMax":34,"regions":[],"incomeLimit":true}', '은행 앱에서 신청', 'https://example.com/youth-saving', 'PUBLISHED', 1204, 31, 88),

    ('코리아 그랜드 페스티벌', 'DISCOUNT', '올해 가장 큰 할인 축제, 가전·여행·생활용품', CURRENT_DATE - 16, CURRENT_DATE + 18,
     '{"ageMin":null,"ageMax":null,"regions":[],"incomeLimit":false}', '행사 페이지에서 쿠폰 받기', 'https://example.com/festival', 'PUBLISHED', 980, 12, 54),

    ('스타벅스 겨울 e-프리퀀시', 'ETC', '17잔 모으면 겨울 굿즈 증정, 이득일까?', CURRENT_DATE - 10, CURRENT_DATE + 30,
     '{"ageMin":null,"ageMax":null,"regions":[],"incomeLimit":false}', '스타벅스 앱에서 적립', 'https://example.com/starbucks', 'PUBLISHED', 2310, 45, 120),

    ('무료 건강검진', 'SUPPORT', '올해 대상자라면 연말까지 무료로 받을 수 있어요', NULL, CURRENT_DATE + 48,
     '{"ageMin":20,"ageMax":null,"regions":[],"incomeLimit":false}', '지정 검진기관 방문', 'https://example.com/health-check', 'PUBLISHED', 640, 22, 31),

    ('청년 월세 지원', 'SUPPORT', '월 최대 20만 원, 최대 12개월 월세 지원', NULL, CURRENT_DATE + 7,
     '{"ageMin":19,"ageMax":34,"regions":[],"incomeLimit":true}', '복지로에서 온라인 신청', 'https://example.com/rent-support', 'PUBLISHED', 1530, 60, 97),

    ('겨울 난방비 지원금', 'SUPPORT', '에너지바우처로 난방비 부담 덜기', NULL, CURRENT_DATE + 1,
     '{"ageMin":null,"ageMax":null,"regions":[],"incomeLimit":true}', '주민센터 방문 신청', 'https://example.com/heating', 'PUBLISHED', 410, 8, 15),

    ('편의점 음료 1+1 페스티벌', 'DISCOUNT', '오늘까지 음료·간식 1+1', CURRENT_DATE - 6, CURRENT_DATE,
     '{"ageMin":null,"ageMax":null,"regions":[],"incomeLimit":false}', '매장에서 바로 적용', 'https://example.com/cvs', 'PUBLISHED', 350, 5, 40),

    ('서울사랑상품권 10% 할인', 'DISCOUNT', '충전하면 10% 추가 적립, 서울 시민 대상', NULL, CURRENT_DATE + 3,
     '{"ageMin":null,"ageMax":null,"regions":["서울특별시"],"incomeLimit":false}', '서울페이 앱에서 구매', 'https://example.com/seoul-pay', 'PUBLISHED', 720, 19, 63),

    ('파킹통장 금리 비교', 'FINANCE', '하루만 맡겨도 연 3% 이자, 언제든 가입 가능', NULL, NULL,
     '{"ageMin":null,"ageMax":null,"regions":[],"incomeLimit":false}', '은행 앱에서 가입', 'https://example.com/parking-account', 'PUBLISHED', 890, 27, 44),

    ('가을 여행 숙박 쿠폰', 'DISCOUNT', '국내 숙박 최대 5만 원 할인 쿠폰', CURRENT_DATE - 40, CURRENT_DATE - 5,
     '{"ageMin":null,"ageMax":null,"regions":[],"incomeLimit":false}', '여행 앱에서 쿠폰 받기', 'https://example.com/travel', 'PUBLISHED', 1780, 38, 102),

    ('[다음 주 혜택 제목]', NULL, NULL, NULL, NULL,
     '{"ageMin":null,"ageMax":null,"regions":[],"incomeLimit":false}', NULL, NULL, 'DRAFT', 0, 0, 0),

    ('[지난 혜택 제목]', 'ETC', '공개했다가 내린 혜택', NULL, CURRENT_DATE + 10,
     '{"ageMin":null,"ageMax":null,"regions":[],"incomeLimit":false}', NULL, 'https://example.com/hidden', 'HIDDEN', 120, 3, 4);