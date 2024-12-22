create table user
(
    id          bigint                                                                      not null comment '主键'
        primary key,
    username    varchar(20)                                                                 not null comment '用户名(账号)',
    password    varchar(255)                                                                not null comment '密码',
    nickname    varchar(10)  default '无名'                                                 not null comment '昵称',
    avatar      varchar(255) default 'https://avatars.githubusercontent.com/u/93258134?v=4' null comment '用户头像',
    gender      tinyint      default 1                                                      not null comment '性别(1：男  0：女)',
    age         int                                                                         null comment '年龄',
    phone       char(11)                                                                    null comment '手机号',
    email       varchar(50)                                                                 null comment '邮箱',
    role        tinyint      default 2                                                      not null comment '角色(1：管理员  2：普通用户)',
    create_time datetime     default CURRENT_TIMESTAMP                                      not null comment '创建时间',
    update_time datetime                                                                    null on update CURRENT_TIMESTAMP,
    is_delete   tinyint      default 0                                                      not null comment '是否删除',
    constraint user_pk_2
        unique (username)
)
    comment '用户表';


create table picture
(
    id           bigint auto_increment comment 'id'
        primary key,
    url          varchar(512)                       not null comment '图片 url',
    name         varchar(128)                       not null comment '图片名称',
    introduction varchar(512)                       null comment '简介',
    category     varchar(64)                        null comment '分类',
    tags         varchar(512)                       null comment '标签（JSON 数组）',
    size         bigint                             null comment '图片体积',
    width        int                                null comment '图片宽度',
    height       int                                null comment '图片高度',
    scale        double                             null comment '图片宽高比例',
    format       varchar(32)                        null comment '图片格式',
    user_id      bigint                             not null comment '创建用户 id',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint  default 0                 not null comment '是否删除'
)
    comment '图片表' collate = utf8mb4_unicode_ci;

create index idx_category
    on picture (category);

create index idx_introduction
    on picture (introduction);

create index idx_name
    on picture (name);

create index idx_tags
    on picture (tags);

create index idx_userId
    on picture (user_id);



