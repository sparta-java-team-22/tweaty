-- 관리자 계정 샘플
INSERT INTO p_user (id, username, password, name, phone_number, email, role, created_at, modified_at, is_deleted)
VALUES ('11111111-1111-1111-1111-111111111111', 'admin1',
        '$2a$10$KyJ.ARKB9o.raK9RCZTX0eqqRzBKFpPBpQn3/E7Lu12f6P75q8lk2', '관리자1', '01011111111', 'admin1@example.com',
        'ROLE_ADMIN', now(), null, false);
INSERT INTO p_user (id, username, password, name, phone_number, email, role, created_at, modified_at, is_deleted)
VALUES ('22222222-2222-2222-2222-222222222222', 'admin2',
        '$2a$10$KyJ.ARKB9o.raK9RCZTX0eqqRzBKFpPBpQn3/E7Lu12f6P75q8lk2', '관리자2', '01022222222', 'admin2@example.com',
        'ROLE_ADMIN', now(), null, false);

-- 일반 사용자 계정 샘플
INSERT INTO p_user (id, username, password, name, phone_number, email, role, created_at, modified_at, is_deleted)
VALUES ('33333333-3333-3333-3333-333333333333', 'user1', '$2a$10$KyJ.ARKB9o.raK9RCZTX0eqqRzBKFpPBpQn3/E7Lu12f6P75q8lk2',
        '사용자1', '01033333333', 'user1@example.com', 'ROLE_CUSTOMER', now(), null, false);
INSERT INTO p_user (id, username, password, name, phone_number, email, role, created_at, modified_at, is_deleted)
VALUES ('44444444-4444-4444-4444-444444444444', 'user2', '$2a$10$KyJ.ARKB9o.raK9RCZTX0eqqRzBKFpPBpQn3/E7Lu12f6P75q8lk2',
        '사용자2', '01044444444', 'user2@example.com', 'ROLE_CUSTOMER', now(), null, false);

-- 가게 주인 사용자 계정 샘플
INSERT INTO p_user (id, username, password, name, phone_number, email, role, created_at, modified_at, is_deleted)
VALUES ('55555555-5555-5555-5555-555555555555', 'owner1',
        '$2a$10$KyJ.ARKB9o.raK9RCZTX0eqqRzBKFpPBpQn3/E7Lu12f6P75q8lk2', '주인1', '01055555555', 'owner1@example.com',
        'ROLE_OWNER', now(), null, false);
INSERT INTO p_user (id, username, password, name, phone_number, email, role, created_at, modified_at, is_deleted)
VALUES ('66666666-6666-6666-6666-666666666666', 'owner2',
        '$2a$10$KyJ.ARKB9o.raK9RCZTX0eqqRzBKFpPBpQn3/E7Lu12f6P75q8lk2', '주인2', '01066666666', 'owner2@example.com',
        'ROLE_OWNER', now(), null, false);
INSERT INTO p_owner (id, business_number, business_license_url, approval_status)
VALUES ('55555555-5555-5555-5555-555555555555', '1111111111', 'https://s3-url.com/license/123.png', 'APPROVED');
INSERT INTO p_owner (id, business_number, business_license_url, approval_status)
VALUES ('66666666-6666-6666-6666-666666666666', '2222222222', 'https://s3-url.com/license/234.png', 'APPROVED');