--[[key:当前访问key refillTime : 访问时间  limit : 限制次数  interval ： 间隔时间，一般为60s]]
local lock_key, owner, current_time_stamp = KEYS[1], tonumber(ARGV[1]), tonumber(ARGV[2]);

local exist = redis.call("EXISTS", lock_key);
--[[如果存在锁]]
if (exist == 1) then
    --[[拿到锁的过期时间]]
    local expired_time = tonumber(redis.call("hget", lock_key, "expired_time"));
    local expired = current_time_stamp > expired_time;
    
    --[[拿到锁的持有者]]
    local old_owner = tonumber(redis.call("hget", lock_key, "owner"));
    local hold_lock_by_current = old_owner == owner;

    --[[锁过期]]
    if (expired) then
        --[[已过期的锁可以被任何线程释放]]
        redis.call("del", lock_key);
        return 0;
    end;

    --[[锁的持有者不是当前线程]]
    if (not hold_lock_by_current) then
        --[[1表示释放锁的线程不是持有锁的线程]]
        return 1;
    end;

    local state = tonumber(redis.call("hget", lock_key, "state")) - 1;
    if (state == 0) then
        redis.call("del", lock_key);
        return 0;
    else 
        --[[锁未过期，且持有者是当前线程，直接更新过期时间，并加锁次数减1]]
        redis.call("hmset", lock_key, "state", state, "latest_update_time", current_time_stamp);
        return 0;
    end;
else
    return 0;
end;