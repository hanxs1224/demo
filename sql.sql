

CREATE TABLE USERS
(
  user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  username NVARCHAR(255) NOT NULL COMMENT '用户名',
  password NVARCHAR(255) NOT NULL COMMENT '密码',
  token NVARCHAR(255) COMMENT '令牌',
  token_time DATETIME COMMENT '令牌有效截止时间',
  status BOOLEAN DEFAULT 0 NOT NULL COMMENT '激活状态（0：未激活,1：已激活）',
  email NVARCHAR(255) COMMENT '邮箱',
  mobile NVARCHAR(255) COMMENT '手机号',
  roles NVARCHAR(255) NOT NULL COMMENT '用户角色英文名（以逗号分隔）',
  creator NVARCHAR(255) COMMENT '创建者',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '创建时间',
  updated_by NVARCHAR(255) COMMENT '更新者',
  updated_time DATETIME COMMENT '更新时间',
  version BIGINT DEFAULT 1 NOT NULL COMMENT '版本号'
);
CREATE UNIQUE INDEX USERS_username_uindex ON USERS (username);
ALTER TABLE USERS COMMENT = '用户表';


CREATE TABLE user_role
(
  user_role_id bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  role_name_cn nvarchar(255) NOT NULL COMMENT '角色中文名',
  role_name_en nvarchar(255) NOT NULL COMMENT '角色英文名',
  status boolean DEFAULT 1 NOT NULL COMMENT '状态（0：无效，1：有效）',
  creator nvarchar(255),
  created_time datetime DEFAULT current_timestamp(),
  updated_by nvarchar(255),
  updated_time datetime,
  version bigint DEFAULT 1 COMMENT '版本号'
);
ALTER TABLE user_role COMMENT = '用户角色';

commit ;


INSERT INTO `mydb`.`user_role` (`user_role_id`, `role_name_cn`, `role_name_en`, `status`, `creator`, `created_time`, `updated_by`, `updated_time`, `version`)
VALUES (1, '普通用户', 'ROLE_USER', DEFAULT, 'NULL', DEFAULT, 'NULL', 'NULL', DEFAULT);
COMMIT ;


ALTER TABLE users ADD register_type bigint(1) DEFAULT 0 NOT NULL COMMENT '注册类型（0：邮箱，1：手机）';
commit ;