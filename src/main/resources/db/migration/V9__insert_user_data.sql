insert into users(created_date, last_modified_date, account_expired, account_locked, credentials_expired, disabled, username, nickname, password, phone)
values (now(), now(), false, false, false, false, 'admin', 'admin', '$2a$10$V.YJtWqY0ezTBwjf0UQpBudnnvFxcQ0/vRF/Ole6Wb08bFuFyIEL.', '010-0000-0000')
on CONFLICT (username) do nothing;

