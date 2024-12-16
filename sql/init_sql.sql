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

