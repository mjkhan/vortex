use VORTEX;

DROP TABLE IF EXISTS PERSISTENT_LOGINS;
CREATE TABLE PERSISTENT_LOGINS (
	SERIES    VARCHAR(64) NOT NULL COMMENT '순번',
	USERNAME  VARCHAR(64) NOT NULL COMMENT '로그인 아이디',
	TOKEN     VARCHAR(64) NOT NULL COMMENT '사용자 토큰',
	LAST_USED TIMESTAMP   NOT NULL COMMENT '최종 로그인 시간',
	PRIMARY KEY (SERIES)
) COMMENT 'spring security 자동 로그인(remember-me) 정보';

DROP TABLE IF EXISTS TBL_USER;
CREATE TABLE TBL_USER (
	USER_ID   VARCHAR(32) NOT NULL COMMENT '사용자 아이디',
	USER_NAME VARCHAR(32) NOT NULL COMMENT '사용자 이름',
	ALIAS     VARCHAR(32)     NULL COMMENT '별명',
	PASSWD    VARCHAR(32) NOT NULL COMMENT '비밀번호',
	INS_TIME  DATETIME    NOT NULL COMMENT '생성 일시',
	UPD_TIME  DATETIME    NOT NULL COMMENT '수정 일시',
	STATUS    VARCHAR(3)  NOT NULL DEFAULT '001' COMMENT '상태(000:생성, 001:활성, 998:비활성, 999:제거)',
	PRIMARY KEY (USER_ID)
) COMMENT '사용자 정보';

DROP TABLE IF EXISTS TBL_GROUP;
CREATE TABLE TBL_GROUP (
	GRP_TYPE VARCHAR(3)  NOT NULL COMMENT '그룹 유형',
	GRP_ID   VARCHAR(3)  NOT NULL COMMENT '그룹 아이디',
	GRP_NAME VARCHAR(64) NOT NULL COMMENT '그룹 이름',
	DESCRP   TEXT            NULL COMMENT '그룹 설명',
	INS_ID   VARCHAR(32) NOT NULL COMMENT '생성자 아이디',
	INS_TIME DATETIME    NOT NULL COMMENT '생성 일시',
	UPD_ID   VARCHAR(32) NOT NULL COMMENT '수정자 아이디',
	UPD_TIME DATETIME    NOT NULL COMMENT '수정 일시',
	STATUS   VARCHAR(3)	 NOT NULL DEFAULT '001' COMMENT '상태(000:생성, 001:활성, 998:비활성, 999:제거)',
	PRIMARY KEY (GRP_TYPE, GRP_ID)
) COMMENT '그룹 정보';

INSERT INTO TBL_GROUP (GRP_TYPE, GRP_ID, GRP_NAME, DESCRP, INS_ID, INS_TIME, UPD_ID, UPD_TIME)
SELECT '000' GRP_TYPE, A.*, 'admin' INS_ID, CURRENT_TIMESTAMP INS_TIME, 'admin' UPD_ID, CURRENT_TIMESTAMP UPD_TIME
FROM (
	SELECT '000' GRP_ID, '그룹 유형' GRP_NAME, 'TBL_GROUP.GRP_TYPE' DESCRP UNION
	SELECT '001' GRP_ID, '공통 코드' GRP_NAME, 'TBL_CMN_CODE.GRP_ID' DESCRP UNION
	SELECT '002' GRP_ID, '액션 그룹' GRP_NAME, 'TBL_ACTION.GRP_ID' DESCRP
) A;

DROP TABLE IF EXISTS TBL_GROUP_MEMBER;
CREATE TABLE TBL_GROUP_MEMBER (
	GRP_TYPE VARCHAR(3)  NOT NULL COMMENT '그룹 유형',
	GRP_ID   VARCHAR(3)  NOT NULL COMMENT '그룹 아이디',
	MB_TYPE  VARCHAR(3)  NOT NULL COMMENT '소속원 유형',
	MB_ID    VARCHAR(32) NOT NULL COMMENT '소속원 아이디',
	SRT_ORD  INT         NOT NULL DEFAULT 99999 COMMENT '정렬 순서',
	INS_ID   VARCHAR(32) NOT NULL COMMENT '생성자 아이디',
	INS_TIME DATETIME    NOT NULL COMMENT '생성 일시',
	PRIMARY KEY (GRP_TYPE, GRP_ID, MB_TYPE, MB_ID)
) COMMENT '그룹 소속 정보';

DROP TABLE IF EXISTS TBL_CMN_CODE;
CREATE TABLE TBL_CMN_CODE (
	GRP_ID   VARCHAR(3)  NOT NULL COMMENT '(코드) 그룹 아이디',
	CD_ID    VARCHAR(32) NOT NULL COMMENT '코드',
	CD_VAL   VARCHAR(64) NOT NULL COMMENT '코드값',
	DESCRP   TEXT            NULL COMMENT '코드 설명',
	UPD_ID   VARCHAR(32) NOT NULL COMMENT '수정자 아이디',
	UPD_TIME DATETIME    NOT NULL COMMENT '수정 일시',
	PRIMARY KEY (GRP_ID, CD_ID)
) COMMENT '공통 코드';

DROP TABLE IF EXISTS TBL_ACTION;
CREATE TABLE TBL_ACTION (
	ACT_ID   VARCHAR(5)  NOT NULL COMMENT '액션 아이디',
	GRP_ID   VARCHAR(3)  NOT NULL COMMENT '(액션) 그룹 아이디',
	ACT_NAME VARCHAR(32) NOT NULL COMMENT '액션 이름',
	ACT_PATH VARCHAR(32) NOT NULL COMMENT '액션 경로',
	DESCRP   TEXT            NULL COMMENT '액션 설명',
	UPD_ID   VARCHAR(32) NOT NULL COMMENT '수정자 아이디',
	UPD_TIME DATETIME    NOT NULL COMMENT '수정 일시',
	PRIMARY KEY (ACT_ID),
	UNIQUE (ACT_PATH)
) COMMENT '액션 정보';

DROP TABLE IF EXISTS TBL_ROLE;
CREATE TABLE TBL_ROLE (
	ROLE_ID   VARCHAR(32) NOT NULL COMMENT '역할 아이디',
	ROLE_NAME VARCHAR(32) NOT NULL COMMENT '역할 이름',
	DESCRP    TEXT            NULL COMMENT '역할 설명',
	UPD_ID    VARCHAR(32) NOT NULL COMMENT '수정자 아이디',
	UPD_TIME  DATETIME    NOT NULL COMMENT '수정 일시',
	PRIMARY KEY (ROLE_ID),
	UNIQUE (ROLE_NAME)
) COMMENT '역할 정보';

DROP TABLE IF EXISTS TBL_ROLE_ACTION;
CREATE TABLE TBL_ROLE_ACTION (
	ROLE_ID  VARCHAR(32) NOT NULL COMMENT '역할 아이디',
	ACT_ID   VARCHAR(32) NOT NULL COMMENT '액션 아이디',
	INS_ID   VARCHAR(32) NOT NULL COMMENT '생성자 아이디',
	INS_TIME DATETIME    NOT NULL COMMENT '생성 일시',
	PRIMARY KEY (ROLE_ID, ACT_ID)
) COMMENT '역할-액션 매핑 정보';

DROP TABLE IF EXISTS TBL_ROLE_USER;
CREATE TABLE TBL_ROLE_USER (
	ROLE_ID   VARCHAR(32) NOT NULL COMMENT '역할 아이디',
	USER_ID   VARCHAR(32) NOT NULL COMMENT '사용자 아이디',
	INS_ID    VARCHAR(32) NOT NULL COMMENT '생성자 아이디',
	INS_TIME  DATETIME    NOT NULL COMMENT '생성 일시',
	PRIMARY KEY (ROLE_ID, USER_ID)
) COMMENT '역할 - 사용자 매핑 정보';

DROP TABLE IF EXISTS TBL_MENU;
CREATE TABLE TBL_MENU (
	MENU_ID   VARCHAR(5)   NOT NULL COMMENT '메뉴 아이디',
	MENU_NAME VARCHAR(32)      NULL COMMENT '메뉴 이름',
	ACT_ID    VARCHAR(5)       NULL COMMENT '액션 ID',
	IMG_CFG   VARCHAR(128)     NULL COMMENT '이미지 설정(경로, 스타일 등)',
	PRNT_ID   VARCHAR(5)   NOT NULL DEFAULT '00000' COMMENT '상위 메뉴 아이디',
	SRT_ORD   INT          NOT NULL DEFAULT 99999 COMMENT '정렬 순서',
	UPD_ID    VARCHAR(32)  NOT NULL COMMENT '수정자 아이디',
	UPD_TIME  DATETIME     NOT NULL COMMENT '수정 시간',
	STATUS    VARCHAR(3)   NOT NULL DEFAULT '001' COMMENT '상태(000:생성, 001:활성, 998:비활성, 999:제거)',
	PRIMARY KEY (MENU_ID)
) COMMENT '메뉴 정보';

DROP TABLE IF EXISTS TBL_FILE;
CREATE TABLE TBL_FILE (
	FILE_ID   VARCHAR(11)  NOT NULL COMMENT '파일 아이디',
	FILE_NAME VARCHAR(64)  NOT NULL COMMENT '파일 이름',
	FILE_PATH VARCHAR(256) NOT NULL COMMENT '파일 경로',
	INS_ID    VARCHAR(32)  NOT NULL COMMENT '생성자 아이디',
	INS_TIME  DATETIME     NOT NULL COMMENT '생성 일시',
	STATUS    VARCHAR(3)   NOT NULL DEFAULT '001' COMMENT '상태(000:생성, 001:활성, 998:비활성, 999:제거)',
	PRIMARY KEY (FILE_ID)
) COMMENT '파일 정보';

DROP TABLE IF EXISTS TBL_FILE_OWNER;
CREATE TABLE TBL_FILE_OWNER (
	OWNER_TYPE VARCHAR(3)  NOT NULL COMMENT '소유자 유형',
	OWNER_ID   VARCHAR(32) NOT NULL COMMENT '소유자 아이디',
	FILE_ID    VARCHAR(11) NOT NULL COMMENT '파일 아이디',
	SRT_ORD    INT         NOT NULL DEFAULT 99999 COMMENT '정렬 순서',
	INS_ID     VARCHAR(32) NOT NULL COMMENT '생성자 아이디',
	INS_TIME   DATETIME    NOT NULL COMMENT '생성 일시',
	PRIMARY KEY (OWNER_TYPE, OWNER_ID, FILE_ID)
) COMMENT '파일 소유(관계) 정보';

DROP TABLE IF EXISTS TBL_LINK;
CREATE TABLE TBL_LINK (
	LINK_ID      VARCHAR(11)  NOT NULL COMMENT '링크 아이디',
	GRP_ID       VARCHAR(3)       NULL COMMENT '그룹 아이디',
	LINK_NAME    VARCHAR(32)  NOT NULL COMMENT '링크 제목',
	STR_DATETIME VARCHAR(10)      NULL COMMENT '게시 시작일',
	END_DATETIME VARCHAR(10)      NULL COMMENT '게시 종료일',
	DESCRP       TEXT             NULL COMMENT '링크 설명',
	TXT_CNT      TEXT             NULL COMMENT '텍스트 컨텐트',
	IMG_URL      VARCHAR(256)     NULL COMMENT '이미지 URL',
	TGT_URL      VARCHAR(256)     NULL COMMENT '타겟 URL',
	SRT_ORD      INT          NOT NULL DEFAULT 99999 COMMENT '정렬 순서',
	INS_ID       VARCHAR(32)  NOT NULL COMMENT '생성자 아이디',
	INS_TIME     DATETIME     NOT NULL COMMENT '생성 일시',
	UPD_ID       VARCHAR(32)  NOT NULL COMMENT '수정자 아이디',
	UPD_TIME     DATETIME     NOT NULL COMMENT '수정 일시',
	STATUS       VARCHAR(3)   NOT NULL DEFAULT '001' COMMENT '상태(000:생성, 001:활성, 998:비활성, 999:제거)',
	PRIMARY KEY (LINK_ID)
) COMMENT '링크 정보';

DROP TABLE IF EXISTS TBL_COMMENT;
CREATE TABLE TBL_COMMENT (
	OWNER_TYPE VARCHAR(3)  NOT NULL COMMENT '소유자 유형',
	OWNER_ID   VARCHAR(32) NOT NULL COMMENT '소유자 아이디',
	CMT_ID     VARCHAR(11) NOT NULL COMMENT '댓글 아이디',
	TXT_CNT    TEXT        NOT NULL COMMENT '댓글 내용',
	INS_ID     VARCHAR(32) NOT NULL COMMENT '생성자 아이디',
	INS_TIME   DATETIME    NOT NULL COMMENT '생성 일시',
	UPD_ID     VARCHAR(32) NOT NULL COMMENT '수정자 아이디',
	UPD_TIME   DATETIME    NOT NULL COMMENT '수정 일시',
	STATUS     VARCHAR(3)  NOT NULL DEFAULT '001' COMMENT '상태(000:생성, 001:활성, 998:비활성, 999:제거)',
	PRIMARY KEY (OWNER_TYPE, OWNEER_ID, CMT_ID)
) COMMENT '댓글 정보';
