package org.object.reservation.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.object.generic.TimeInterval;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public boolean isPeriodCondition() {
		return ConditionType.PERIOD_CONDITION.equals(conditionType);
	}

	public boolean isSequenceCondition() {
		return ConditionType.SEQUENCE_CONDITION.equals(conditionType);
	}

	public boolean isCombinedCondition() {
		return ConditionType.COMBINED_CONDITION.equals(conditionType);
	}

	public ConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public TimeInterval getInterval() {
		return interval;
	}

	public void setInterval(TimeInterval interval) {
		this.interval = interval;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
