import java.time.Instant;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.springframework.scheduling.support.CronSequenceGenerator;

public interface CronUtils {

    static Date getPreviousExecutionTime(final String cronExpression, final TimeZone timeZone) {
        // Parse the Cron Expression
        final CronSequenceGenerator generator = new CronSequenceGenerator(cronExpression, timeZone);
        final Date now = Date.from(GregorianCalendar.getInstance(timeZone).toInstant().plusSeconds(1));
        // Get the next scheduled execution
        final Date nextExecution = generator.next(now);
        // How much time will pass until the next execution?
        final long diffToNext = nextExecution.getTime() - now.getTime();
        // Subtract that time from 'now' (times 1 would possibly get the exact previous time, we can go up to times 1.9 to be on the safe side.)
        final long checkFirstRunAfter = now.getTime() - (long) (1.9 * diffToNext);
        // Take that difference, and use the cronSequence generator to pass us the first match after that time.
        return generator.next(Date.from(Instant.ofEpochMilli(checkFirstRunAfter)));
    }

}
