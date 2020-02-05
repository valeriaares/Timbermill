package com.datorama.timbermill;

import com.datorama.timbermill.pipe.EventOutputPipe;
import com.datorama.timbermill.unit.Event;

import java.util.concurrent.atomic.AtomicLong;

class StatisticsCollectorOutputPipe implements EventOutputPipe {

	private final EventOutputPipe delegate;
	private final AtomicLong eventsAmount = new AtomicLong(0);
	private final AtomicLong totalSubmitDuration = new AtomicLong(0);
	private final AtomicLong maxSubmitDuration = new AtomicLong(0);

	StatisticsCollectorOutputPipe(EventOutputPipe delegate) {
		this.delegate = delegate;
	}

	@Override public void send(Event e) {
		long start = System.currentTimeMillis();
		delegate.send(e);
		long end = System.currentTimeMillis();
		updateCounters(end-start);
	}

	private void updateCounters(long duration) {
		totalSubmitDuration.addAndGet(duration);
		long curMax = maxSubmitDuration.get();
		if (duration > curMax) {
			// This update might "fail" if cur max has changed in the meantime.
			// Since this is not mission critical, we will live with this situation
			maxSubmitDuration.compareAndSet(curMax, duration);
		}
		eventsAmount.incrementAndGet();
	}

	@Override public int getCurrentBufferSize() {
		return delegate.getCurrentBufferSize();
	}

	void initCounters() {
		eventsAmount.set(0);
		totalSubmitDuration.set(0);
		maxSubmitDuration.set(0);
	}

	long getEventsAmount() {
		return eventsAmount.get();
	}

	long getMaxSubmitDuration() {
		return maxSubmitDuration.get();
	}

	private long getTotalSubmitDuration() {
		return totalSubmitDuration.get();
	}

	double getAvgSubmitDuration() {
		if (getEventsAmount() > 0) {
			return (double) getTotalSubmitDuration() / getEventsAmount();
		} else {
			return 0;
		}
	}

	@Override public String toString() {
		return String.format("eventsAmount:%d, avgSubmitDuration: %f, maxSubmitDuration:%d",
				getEventsAmount(), getAvgSubmitDuration(), getMaxSubmitDuration());
	}
}
