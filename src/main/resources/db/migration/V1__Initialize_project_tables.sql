create table orders
(
    id           bigint not null auto_increment,
    shipping_fee bigint not null,
    primary key (id)
);

create table order_line_item
(
    id         bigint       not null auto_increment,
    name       varchar(255) not null,
    price      bigint       not null,
    product_id bigint       not null,
    order_id   bigint       not null,
    primary key (id)
);

create table refund
(
    id                  bigint       not null auto_increment,
    order_id            bigint       not null,
    status              varchar(255) not null,
    return_shipping_fee bigint       not null,
    reason              varchar(255) not null,
    detailed_reason     longtext,
    primary key (id)
);

create table refund_line_item
(
    refund_id          bigint not null,
    order_line_item_id bigint not null
);


create table exchange
(
    id                  bigint       not null auto_increment,
    order_id            bigint       not null,
    status              varchar(255) not null,
    return_shipping_fee bigint       not null,
    reason              varchar(255) not null,
    detailed_reason     longtext,
    primary key (id)
);

create table exchange_line_item
(
    exchange_id        bigint not null,
    order_line_item_id bigint not null
);

alter table order_line_item
    add constraint fk_order_line_item_to_orders
        foreign key (order_id)
            references orders (id);

alter table refund_line_item
    add constraint fk_refund_line_item_to_refund
        foreign key (refund_id)
            references refund (id);

alter table exchange_line_item
    add constraint fk_exchange_line_item_to_exchange
        foreign key (exchange_id)
            references exchange (id);




