package com.hubspot.config;

import com.hubspot.resources.InvitationResource;
import com.hubspot.services.InvitationService;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.core.instrument.distribution.pause.PauseDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

import static org.easymock.EasyMock.createMock;

@Configuration
public class TestResourceApplicationConfig {

    @Bean
    public InvitationResource apiResource() { return new InvitationResource();}

    @Bean
    public InvitationService apiService() {
        return createMock(InvitationService.class);
    }

    @Bean
    public MeterRegistry MeterRegistry() {
        Clock clock = createMock(Clock.class);
        Counter counter = createMock(Counter.class);
        MeterRegistry mr = new MeterRegistry(clock) {
            @Override
            protected Timer newTimer(Meter.Id id, DistributionStatisticConfig distributionStatisticConfig,
                                     PauseDetector pauseDetector) {
                return null;
            }

            @Override
            protected Meter newMeter(Meter.Id id, Meter.Type type, Iterable<Measurement> measurements) {
                return null;
            }

            @Override
            protected LongTaskTimer newLongTaskTimer(Meter.Id id) {
                return null;
            }

            @Override
            protected <T> Gauge newGauge(Meter.Id id, T obj, ToDoubleFunction<T> valueFunction) {
                return null;
            }

            @Override
            protected <T> FunctionTimer newFunctionTimer(Meter.Id id, T obj, ToLongFunction<T> countFunction,
                                                         ToDoubleFunction<T> totalTimeFunction, TimeUnit totalTimeFunctionUnits) {
                return null;
            }

            @Override
            protected <T> FunctionCounter newFunctionCounter(Meter.Id id, T obj, ToDoubleFunction<T> countFunction) {
                return null;
            }

            @Override
            protected DistributionSummary newDistributionSummary(Meter.Id id, DistributionStatisticConfig distributionStatisticConfig,
                                                                 double scale) {
                return null;
            }

            @Override
            protected Counter newCounter(Meter.Id id) {
                return null;
            }

            @Override
            protected TimeUnit getBaseTimeUnit() {
                return null;
            }

            @Override
            protected DistributionStatisticConfig defaultHistogramConfig() {
                return null;
            }

            public Counter counter(String name, String... tags) {
                return counter;
            }
        };

        return mr;
    }

}
