--[[key:当前访问key refillTime : 访问时间  limit : 限制次数  interval ： 间隔时间，一般为60s]]
local lock_key, owner, current_time_stamp, time_out = KEYS[1], tonumber(ARGV[1]), tonumber(ARGV[2]), tonumber(ARGV[3]);

local exist = redis.call("EXISTS", lock_key);
--[[如果存在锁]]
if (exist == 1) then
    --[[拿到锁的过期时间]]
    local expired_time = tonumber(redis.call("hget", lock_key, "expired_time"));
    local expired = current_time_stamp > expired_time;
    
    --[[存在的锁，超过过期时间了，可以直接加锁，不用区分锁的持有者是谁]]
    if (expired) then
        redis.call("hmset", lock_key, "owner", owner, "state", 1, "expired_time", time_out, "create_time", current_time_stamp);
        --[[0表示当前线程加锁成功]]
        return 0;
    end;
    
    --[[拿到锁的持有者]]
    local old_owner = tonumber(redis.call("hget", lock_key, "owner"));
    local hold_lock_by_current = old_owner == owner;

    --[[锁的持有者不是当前线程，且没有过期]]
    if (not hold_lock_by_current) then
        --[[2表示线程持有者不是当前线程]]
        return 2;
    else 
        --[[锁未过期，且持有者是当前线程，直接更新过期时间，并加锁次数加1]]
        local state = tonumber(redis.call("hget", lock_key, "state"));
        redis.call("hmset", lock_key, "state", state + 1, "expired_time", time_out, "latest_update_time", current_time_stamp);
        return 0;
    end;
else --[[如果redis里面不存在这个key，那么设置访问时间以及次数]]
    redis.call("hmset", lock_key, "owner", owner, "state", 1, "expired_time", time_out, "create_time", current_time_stamp);
    return 0;
end;