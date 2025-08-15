-- SimplePolls - Initial Flyway schema (V1)
-- MySQL 8+ compatible schema
-- Tables: polls, poll_options, votes

-- Polls created by staff; players vote. Active = is_open AND (end_at IS NULL OR end_at > CURRENT_TIMESTAMP)
create table polls (
  id varchar(36) not null,
  question text not null,
  created_by varchar(36) not null,
  is_open boolean not null default true,
  created_at timestamp not null default current_timestamp,
  end_at timestamp null,              -- when the poll should auto-close (from duration)
  closed_at timestamp null,           -- when closed manually or by scheduler
  closed_by varchar(36) null,         -- who closed it (if applicable)
  close_reason text null,
  primary key (id)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

-- Helpful indexes for common queries
create index idx_polls_active on polls (is_open, end_at);
create index idx_polls_created_at on polls (created_at);

-- Options per poll (maximum 6 via CHECK on sort_order + unique per poll)
create table poll_options (
  id varchar(36) not null,
  poll_id varchar(36) not null,
  option_text varchar(255) not null,
  sort_order int not null,            -- 1..6 for GUI placement and to cap options to 6
  icon_material varchar(64) null,     -- optional item icon (e.g., 'EMERALD', 'PAPER')
  icon_custom_model_data int null,
  primary key (id),
  constraint fk_poll_options_poll
    foreign key (poll_id) references polls(id) on delete cascade,
  constraint uq_poll_options_order unique (poll_id, sort_order),
  constraint ck_poll_options_sort_order check (sort_order between 1 and 6),
  constraint uq_poll_options_pair unique (poll_id, id) -- to support composite FK from votes
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

-- Useful lookup index
create index idx_poll_options_poll_id on poll_options (poll_id);

-- Votes: one vote per voter per poll; option must belong to the same poll
create table votes (
  poll_id varchar(36) not null,
  option_id varchar(36) not null,
  voter_id varchar(36) not null,
  voted_at timestamp not null default current_timestamp,
  primary key (poll_id, voter_id),
  constraint fk_votes_poll_option foreign key (poll_id, option_id)
    references poll_options (poll_id, id) on delete cascade
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

create index idx_votes_option_id on votes (option_id);
create index idx_votes_voter on votes (voter_id);
