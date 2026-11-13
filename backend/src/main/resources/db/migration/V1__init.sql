-- ==========================================
-- 머닝(Meoning) MVP 초기 스키마
-- 기준: Final DBML (2026-10-28)
-- 첫 배포 전까지는 수정 후 DB 초기화 허용, 배포 이후에는 V2부터 추가만 한다.
-- ==========================================

-- ------------------------------------------
-- members
-- ------------------------------------------
CREATE TABLE members (
    id                      BIGSERIAL    PRIMARY KEY,
    email                   VARCHAR(255) NOT NULL,              -- 소문자 정규화 저장
    password_hash           VARCHAR(255) NOT NULL,              -- BCrypt
    role                    VARCHAR(20)  NOT NULL DEFAULT 'USER',
    email_verified          BOOLEAN      NOT NULL DEFAULT FALSE,
    is_notification_enabled BOOLEAN      NOT NULL DEFAULT TRUE,
    notify_d3               BOOLEAN      NOT NULL DEFAULT TRUE,
    notify_d1               BOOLEAN      NOT NULL DEFAULT TRUE,
    notify_d0               BOOLEAN      NOT NULL DEFAULT FALSE,
    birth_year              INT,
    region                  VARCHAR(50),
    interest_categories     JSONB,
    created_at              TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at              TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT chk_members_role
        CHECK (role IN ('USER', 'ADMIN'))
);

-- 대소문자만 다른 이메일 중복 가입 방지 (함수 인덱스는 JPA로 표현 불가 → SQL로 작성)
CREATE UNIQUE INDEX uk_members_email_lower ON members (lower(email));


-- ------------------------------------------
-- benefits
-- ------------------------------------------
CREATE TABLE benefits (
    id                BIGSERIAL     PRIMARY KEY,
    title             VARCHAR(200)  NOT NULL,                  -- 임시저장 시 유일한 필수값
    category          VARCHAR(30),
    summary           VARCHAR(500),
    start_date        DATE,
    end_date          DATE,                                    -- NULL이면 상시 혜택
    target_condition  JSONB,
    apply_method      TEXT,
    official_url      VARCHAR(1000),
    insta_url         VARCHAR(1000),
    thumbnail_url     VARCHAR(1000),
    status            VARCHAR(20)   NOT NULL DEFAULT 'DRAFT',
    source_type       VARCHAR(30)   NOT NULL DEFAULT 'MANUAL',
    view_count        BIGINT        NOT NULL DEFAULT 0,
    bookmark_count    BIGINT        NOT NULL DEFAULT 0,
    click_count       BIGINT        NOT NULL DEFAULT 0,
    publish_at        TIMESTAMPTZ,                             -- A-09 예약 공개: MVP 미사용
    deleted_at        TIMESTAMPTZ,                             -- 소프트 삭제
    created_at        TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ   NOT NULL DEFAULT now(),

    CONSTRAINT chk_benefits_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'HIDDEN')),
    CONSTRAINT chk_benefits_category
        CHECK (category IS NULL OR category IN ('SUPPORT', 'DISCOUNT', 'FINANCE', 'ETC')),
    CONSTRAINT chk_benefits_source_type
        CHECK (source_type IN ('MANUAL', 'PUBLIC_API')),
    CONSTRAINT chk_published_required
        CHECK (status <> 'PUBLISHED'
               OR (category IS NOT NULL AND summary IS NOT NULL AND official_url IS NOT NULL)),
    CONSTRAINT chk_benefits_date_order
        CHECK (start_date IS NULL OR end_date IS NULL OR start_date <= end_date),
    CONSTRAINT chk_benefits_counts_non_negative
        CHECK (view_count >= 0 AND bookmark_count >= 0 AND click_count >= 0)
);

-- 부분 인덱스: 삭제되지 않은 행만 인덱싱 (사용자 조회와 스케줄러가 항상 이 조건을 씀)
CREATE INDEX idx_benefits_status_end_date_active
    ON benefits (status, end_date) WHERE deleted_at IS NULL;
CREATE INDEX idx_benefits_category_status_active
    ON benefits (category, status) WHERE deleted_at IS NULL;


-- ------------------------------------------
-- bookmarks
-- ------------------------------------------
CREATE TABLE bookmarks (
    id          BIGSERIAL    PRIMARY KEY,
    member_id   BIGINT       NOT NULL,
    benefit_id  BIGINT       NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT uk_bookmarks_member_benefit UNIQUE (member_id, benefit_id),
    CONSTRAINT fk_bookmarks_member
        FOREIGN KEY (member_id)  REFERENCES members (id)  ON DELETE CASCADE,
    CONSTRAINT fk_bookmarks_benefit
        FOREIGN KEY (benefit_id) REFERENCES benefits (id) ON DELETE RESTRICT
);

-- PostgreSQL은 FK에 인덱스를 자동 생성하지 않음. 스케줄러의 "이 혜택을 찜한 회원" 조회용
CREATE INDEX idx_bookmarks_benefit_id ON bookmarks (benefit_id);


-- ------------------------------------------
-- notification_logs
-- 발송 순서: PENDING 선점 INSERT(ON CONFLICT DO NOTHING) → 발송 → SUCCESS/FAILED UPDATE
-- ------------------------------------------
CREATE TABLE notification_logs (
    id               BIGSERIAL    PRIMARY KEY,
    member_id        BIGINT       NOT NULL,
    benefit_id       BIGINT       NOT NULL,
    notify_type      VARCHAR(20)  NOT NULL,
    channel          VARCHAR(20)  NOT NULL DEFAULT 'EMAIL',
    target_end_date  DATE         NOT NULL,                    -- 마감일 스냅샷 (연장 시 재발송 허용)
    status           VARCHAR(20)  NOT NULL,
    retry_count      INT          NOT NULL DEFAULT 0,
    sent_at          TIMESTAMPTZ,                              -- SUCCESS일 때만 값 있음
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT chk_notification_type
        CHECK (notify_type IN ('D3', 'D1', 'D0')),
    CONSTRAINT chk_notification_channel
        CHECK (channel IN ('EMAIL', 'WEB_PUSH')),
    CONSTRAINT chk_notification_status
        CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    CONSTRAINT uk_notification_dedup
        UNIQUE (member_id, benefit_id, notify_type, target_end_date, channel),
    CONSTRAINT fk_notification_logs_member
        FOREIGN KEY (member_id)  REFERENCES members (id)  ON DELETE CASCADE,
    CONSTRAINT fk_notification_logs_benefit
        FOREIGN KEY (benefit_id) REFERENCES benefits (id) ON DELETE RESTRICT
);

CREATE INDEX idx_notification_retry ON notification_logs (status, retry_count);


-- ------------------------------------------
-- benefit_click_logs
-- ------------------------------------------
CREATE TABLE benefit_click_logs (
    id          BIGSERIAL    PRIMARY KEY,
    benefit_id  BIGINT       NOT NULL,
    member_id   BIGINT,                                        -- 비회원 NULL, 탈퇴 시 SET NULL
    utm_source  VARCHAR(50),
    clicked_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT fk_click_logs_benefit
        FOREIGN KEY (benefit_id) REFERENCES benefits (id) ON DELETE RESTRICT,
    CONSTRAINT fk_click_logs_member
        FOREIGN KEY (member_id)  REFERENCES members (id)  ON DELETE SET NULL
);

CREATE INDEX idx_click_logs_benefit_clicked_at ON benefit_click_logs (benefit_id, clicked_at);
