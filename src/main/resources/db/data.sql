-- OurTime Sample Data
-- 테스트 및 개발용 샘플 데이터

USE ourtime;

-- 샘플 사용자 (비밀번호: password123)
-- BCrypt 해시: $2a$10$N9qo8uLOickgx2ZMRZoMye/IcEhOl3zEIMTnQpVULP8Gh/H.Ldu4W
INSERT INTO users (email, password, nickname, profile_image, created_at, updated_at) VALUES
    ('kim@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/IcEhOl3zEIMTnQpVULP8Gh/H.Ldu4W', '김철수', NULL, NOW(), NOW()),
    ('lee@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/IcEhOl3zEIMTnQpVULP8Gh/H.Ldu4W', '이영희', NULL, NOW(), NOW()),
    ('park@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/IcEhOl3zEIMTnQpVULP8Gh/H.Ldu4W', '박민수', NULL, NOW(), NOW()),
    ('choi@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/IcEhOl3zEIMTnQpVULP8Gh/H.Ldu4W', '최지영', NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE email=email;

-- 샘플 그룹
INSERT INTO `groups` (name, type, invite_code, description, created_by, created_at, updated_at) VALUES
    ('우리 가족', 'FAMILY', UUID(), '사랑하는 우리 가족의 추억', 1, NOW(), NOW()),
    ('여행 동호회', 'FRIEND', UUID(), '함께 여행하는 친구들', 2, NOW(), NOW()),
    ('커플 다이어리', 'COUPLE', UUID(), '우리 둘만의 추억', 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=name;

-- 그룹 멤버 매핑
INSERT INTO user_group (user_id, group_id, role, created_at, updated_at) VALUES
    (1, 1, 'ADMIN', NOW(), NOW()),   -- 김철수 - 우리 가족 (ADMIN)
    (2, 1, 'MEMBER', NOW(), NOW()),  -- 이영희 - 우리 가족 (MEMBER)
    (2, 2, 'ADMIN', NOW(), NOW()),   -- 이영희 - 여행 동호회 (ADMIN)
    (3, 2, 'MEMBER', NOW(), NOW()),  -- 박민수 - 여행 동호회 (MEMBER)
    (4, 2, 'MEMBER', NOW(), NOW()),  -- 최지영 - 여행 동호회 (MEMBER)
    (3, 3, 'ADMIN', NOW(), NOW()),   -- 박민수 - 커플 다이어리 (ADMIN)
    (4, 3, 'MEMBER', NOW(), NOW())   -- 최지영 - 커플 다이어리 (MEMBER)
ON DUPLICATE KEY UPDATE user_id=user_id;

-- 샘플 추억
INSERT INTO memories (group_id, user_id, title, description, latitude, longitude, location_name, visited_at, created_at, updated_at) VALUES
    (1, 1, '제주도 가족 여행', '온 가족이 함께한 첫 제주도 여행! 날씨도 좋고 너무 즐거웠어요.', 33.4996, 126.5312, '제주 한라산', '2024-08-15 10:00:00', NOW(), NOW()),
    (1, 2, '아빠 생신', '아빠 생신을 축하하며 맛있는 저녁 식사', 37.5665, 126.9780, '서울 명동', '2024-07-20 18:00:00', NOW(), NOW()),
    (2, 2, '부산 여행', '친구들과 함께한 부산 여행 1박 2일', 35.1796, 129.0756, '부산 해운대', '2024-09-10 14:00:00', NOW(), NOW()),
    (2, 3, '경주 문화 탐방', '경주 불국사와 석굴암 방문', 35.7898, 129.3322, '경주 불국사', '2024-06-05 11:00:00', NOW(), NOW()),
    (3, 3, '처음 만난 날', '운명적으로 만난 그날, 한강 공원에서', 37.5326, 126.9619, '서울 한강공원', '2023-05-14 16:00:00', NOW(), NOW()),
    (3, 4, '100일 기념', '우리의 첫 100일을 축하하며', 37.5172, 127.0473, '서울 강남', '2023-08-22 19:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE title=title;

-- 추억 이미지 (샘플 URL)
INSERT INTO memory_images (memory_id, image_url) VALUES
    (1, 'https://example.com/images/jeju1.jpg'),
    (1, 'https://example.com/images/jeju2.jpg'),
    (2, 'https://example.com/images/birthday.jpg'),
    (3, 'https://example.com/images/busan1.jpg'),
    (3, 'https://example.com/images/busan2.jpg'),
    (4, 'https://example.com/images/gyeongju.jpg'),
    (5, 'https://example.com/images/hangang.jpg'),
    (6, 'https://example.com/images/100days.jpg')
ON DUPLICATE KEY UPDATE memory_id=memory_id;

-- 추억-태그 매핑
INSERT INTO memory_tag (memory_id, tag_id, created_at, updated_at) VALUES
    (1, 2, NOW(), NOW()),  -- 제주도 여행 - 여행
    (1, 3, NOW(), NOW()),  -- 제주도 여행 - 가족
    (2, 3, NOW(), NOW()),  -- 아빠 생신 - 가족
    (2, 5, NOW(), NOW()),  -- 아빠 생신 - 생일
    (2, 7, NOW(), NOW()),  -- 아빠 생신 - 맛집
    (3, 2, NOW(), NOW()),  -- 부산 여행 - 여행
    (3, 4, NOW(), NOW()),  -- 부산 여행 - 친구
    (4, 2, NOW(), NOW()),  -- 경주 문화 탐방 - 여행
    (4, 4, NOW(), NOW()),  -- 경주 문화 탐방 - 친구
    (5, 1, NOW(), NOW()),  -- 처음 만난 날 - 데이트
    (5, 6, NOW(), NOW()),  -- 처음 만난 날 - 기념일
    (6, 1, NOW(), NOW()),  -- 100일 기념 - 데이트
    (6, 6, NOW(), NOW())   -- 100일 기념 - 기념일
ON DUPLICATE KEY UPDATE memory_id=memory_id;

-- 샘플 댓글
INSERT INTO comments (memory_id, user_id, content, created_at, updated_at) VALUES
    (1, 2, '정말 즐거웠어요! 다음엔 오빠도 같이 가요~', NOW(), NOW()),
    (1, 1, '날씨가 너무 좋았지! 다음에 또 가자', NOW(), NOW()),
    (3, 3, '해운대 해수욕장 최고였어요!', NOW(), NOW()),
    (3, 4, '다음에 또 같이 가요!', NOW(), NOW()),
    (5, 4, '그날이 정말 운명이었네요 💕', NOW(), NOW()),
    (6, 3, '벌써 100일이라니 믿기지 않아!', NOW(), NOW())
ON DUPLICATE KEY UPDATE content=content;

-- 샘플 좋아요
INSERT INTO likes (memory_id, user_id, created_at) VALUES
    (1, 1, NOW()),
    (1, 2, NOW()),
    (2, 1, NOW()),
    (2, 2, NOW()),
    (3, 2, NOW()),
    (3, 3, NOW()),
    (3, 4, NOW()),
    (4, 2, NOW()),
    (4, 3, NOW()),
    (5, 3, NOW()),
    (5, 4, NOW()),
    (6, 3, NOW()),
    (6, 4, NOW())
ON DUPLICATE KEY UPDATE memory_id=memory_id;

-- 샘플 데이터 생성 완료
SELECT 'Sample Data Inserted Successfully!' AS message;
SELECT '테스트 계정: kim@example.com / password123' AS login_info;
