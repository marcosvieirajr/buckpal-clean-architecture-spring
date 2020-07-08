create table activity (
    id bigserial,
    ownerAccountId bigint not null,
    sourceAccountId bigint not null,
    targetAccountId bigint not null,
    timestamp timestamp not null,
    amount numeric(5, 2) not null,

    primary key (id),
    foreign key (ownerAccountId) references account (id),
    foreign key (sourceAccountId) references account (id),
    foreign key (targetAccountId) references account (id)

);
