-- 기본 계좌 등록
insert into account_tb(number, password, balance, user_id, created_at)
values('1111', '1234', '1300', 1, now());

insert into account_tb(number, password, balance, user_id, created_at)
values('2222', '1234', '1100', 2, now());

insert into account_tb(number, password, balance, user_id, created_at)
values('3333', '1234', 0, 3, now());

select * from account_tb;

-- 1번계좌 1000원
-- 2번계좌 1000원
-- 3번계좌 0원

-- 이체 내역을 기록
-- (1번 계좌에서 2번계좌로 100원 이체한다)
insert into history_tb(amount, w_balance, d_balance, w_account_id,
d_account_id, created_at)
values(100, 900, 1100, 1, 2, now());

-- ATM 출금만 1번 계좌에서 100원만 출금 하는 히스토리를 만드세요
insert into history_tb(amount, w_balance, d_balance, w_account_id, 
d_account_id, created_at)
values(100, 800, null, 1, null, now());

-- 입금 내역만 (1번계좌에 500원 입금)
insert into history_tb(amount,w_balance,d_balance,w_account_id,
d_account_id,created_at) values(500,1300,null,1,null,now());
