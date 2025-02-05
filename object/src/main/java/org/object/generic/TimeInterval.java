package org.object.generic;

import java.time.LocalTime;

public class TimeInterval {
	private LocalTime startTime;
	private LocalTime endTime;

	public static TimeInterval of(LocalTime startTime, LocalTime endTime) {
		return new TimeInterval(startTime, endTime);
	}

	private TimeInterval(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}


