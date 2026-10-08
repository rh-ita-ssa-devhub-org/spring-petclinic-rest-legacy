/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.metrics.client.MetricClient;

/**
 * Simple monitoring aspect that tracks call count and execution time.
 * In Quarkus, replaced with Micrometer/MicroProfile Metrics integration.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Michael Isvy
 * @since 2.5
 */
@ApplicationScoped
public class CallMonitoringAspect {

    private boolean enabled = true;
    private int callCount = 0;
    private long accumulatedCallTime = 0;

    @ConfigProperty(name = "quarkus.profile", defaultValue = "hsqldb")
    String profile;

    @PostConstruct
    public void init() {
        // Initialize monitoring
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void reset() {
        this.callCount = 0;
        this.accumulatedCallTime = 0;
    }

    public int getCallCount() {
        return callCount;
    }

    public long getCallTime() {
        if (this.callCount > 0)
            return this.accumulatedCallTime / this.callCount;
        else
            return 0;
    }

    public void recordCall(long durationMillis) {
        if (this.enabled) {
            synchronized (this) {
                this.callCount++;
                this.accumulatedCallTime += durationMillis;
            }
        }
    }
}
