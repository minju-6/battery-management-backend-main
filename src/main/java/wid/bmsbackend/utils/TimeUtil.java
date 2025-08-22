package wid.bmsbackend.utils;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Component
public class TimeUtil {
    private final Clock clock;
    private final ZoneOffset zoneOffset = ZoneOffset.of("+09:00");

    public TimeUtil() {
        this(null);
    }

    public TimeUtil(Clock clock) {
        this.clock = Objects.requireNonNullElseGet(clock, Clock::systemDefaultZone);
    }

    public LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(clock);
    }

    public long getCurrentTimeMillis() {
        return getCurrentLocalDateTime().toInstant(zoneOffset).toEpochMilli();
    }

}
