package org.object.v1.reservation.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.object.v1.generic.TimeInterval;

public class DiscountCondition {

	public enum ConditionType {PERIOD_CONDITION, SEQUENCE_CONDITION, COMBINED_CONDITION}

	private Long id;
	private Long policyId;
	private ConditionType conditionType;
	private DayOfWeek dayOfWeek;
	private TimeInterval interval;
	private Integer sequence;

	public DiscountCondition() {
	}

	public DiscountCondition(Long policyId, ConditionType conditionType, DayOfWeek dayOfWeek, LocalTime startTime,
		LocalTime endTime, Integer sequence) {
		this(null, policyId, conditionType, dayOfWeek, TimeInterval.of(startTime, endTime), sequence);
	}

	public DiscountCondition(Long id, Long policyId, ConditionType conditionType, DayOfWeek dayOfWeek,
		TimeInterval interval, Integer sequence) {
		this.id = id;
		this.policyId = policyId;
		this.conditionType = conditionType;
		this.dayOfWeek = dayOfWeek;
		this.interval = interval;
		this.sequence = sequence;
	}

	public boolean isSatisfiedBy(Screening screening) {
		if (isPeriodCondition()) {
			if (screening.isPlayedIn(dayOfWeek, interval.getStartTime(), interval.getEndTime())) {
				return true;
			}
		} else if (isSequenceCondition()) {
			if (sequence.equals(screening.getSequence())) {
				return true;
			}
		}
		if (isCombinedCondition()) {
			if (screening.isPlayedIn(dayOfWeek, interval.getStartTime(), interval.getEndTime()) &&
				sequence.equals(screening.getSequence())) {
				return true;
			}
		}
		return false;
	}

	public Long getPolicyId() {
		return policyId;
	}

	private boolean isPeriodCondition() {
		return ConditionType.PERIOD_CONDITION.equals(conditionType);
	}

	private boolean isSequenceCondition() {
		return ConditionType.SEQUENCE_CONDITION.equals(conditionType);
	}

	private boolean isCombinedCondition() {
		return ConditionType.COMBINED_CONDITION.equals(conditionType);
	}

}
