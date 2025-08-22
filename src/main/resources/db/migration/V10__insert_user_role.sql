insert into user_role(user_id, role_id) select id, 1 from users where username = 'admin' on conflict (user_id, role_id) do nothing;
