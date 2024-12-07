INSERT INTO members (id, nickname, email) VALUES (1, 'account1', '&account1@mail.com');
INSERT INTO members (id, nickname, email) VALUES (2, 'account2', '&account2@mail.com');
INSERT INTO members (id, nickname, email) VALUES (3, 'account3', '&account3@mail.com');
INSERT INTO members (id, nickname, email) VALUES (4, 'account4', '&account4@mail.com');
INSERT INTO members (id, nickname, email) VALUES (5, 'account5', '&account5@mail.com');

INSERT INTO members (id, nickname, email) VALUES (6, 'sample1', '&sample1@mail.com');
INSERT INTO members (id, nickname, email) VALUES (7, 'sample2', '&sample2@mail.com');
INSERT INTO members (id, nickname, email) VALUES (8, 'sample3', '&sample3@mail.com');
INSERT INTO members (id, nickname, email) VALUES (9, 'sample4', '&sample4@mail.com');
INSERT INTO members (id, nickname, email) VALUES (10, 'sample5', '&sample5@mail.com');
INSERT INTO profiles (member_id, profile_nickname, profile_email, profile_genre) VALUES (6, 'sample1', '&sample1@mail.com', '100000000');
INSERT INTO profiles (member_id, profile_nickname, profile_email, profile_genre) VALUES (7, 'sample2', '&sample2@mail.com', '110000000');
INSERT INTO profiles (member_id, profile_nickname, profile_email, profile_genre) VALUES (8, 'sample3', '&sample3@mail.com', '110000010');
INSERT INTO profiles (member_id, profile_nickname, profile_email, profile_genre) VALUES (9, 'sample4', '&sample4@mail.com', '000000011');
INSERT INTO profiles (member_id, profile_nickname, profile_email, profile_genre) VALUES (10, 'sample5', '&sample5@mail.com', '111111111');

-- Perform 데이터 삽입
INSERT INTO performs (mt20id, prfnm, prfpdfrom, prfpdto, genrenm, prfstate, fcltynm, openrun, area, poster, like_count)
VALUES ('P12345', '뮤지컬 오페라의 유령', '2024.01.01', '2024.03.31', '뮤지컬', '예정', '세종문화회관', TRUE, '서울', 'https://example.com/poster1.jpg', 10);

INSERT INTO performs (mt20id, prfnm, prfpdfrom, prfpdto, genrenm, prfstate, fcltynm, openrun, area, poster, like_count)
VALUES ('P67890', '클래식 콘서트', '2024.02.01', '2024.02.28', '클래식', '예정', '롯데콘서트홀', FALSE, '서울', 'https://example.com/poster2.jpg', 15);

INSERT INTO performs (mt20id, prfnm, prfpdfrom, prfpdto, genrenm, prfstate, fcltynm, openrun, area, poster, like_count)
VALUES ('P11111', '연극 햄릿', '2024.03.01', '2024.03.15', '연극', '진행중', '국립극장', FALSE, '서울', 'https://example.com/poster3.jpg', 5);