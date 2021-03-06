/*
 * Copyright 2012-2016 the original author or authors.
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

package org.springframework.boot.autoconfigure.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport.ConditionAndOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport.ConditionAndOutcomes;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * A condition evaluation report message that can logged or printed.
 *
 * @author Phillip Webb
 * @since 1.4.0
 */
public class ConditionEvalutionReportMessage {

	private StringBuilder message;

	public ConditionEvalutionReportMessage(ConditionEvaluationReport report) {
		this.message = getLogMessage(report);
	}

	private StringBuilder getLogMessage(ConditionEvaluationReport report) {
		StringBuilder message = new StringBuilder();
		message.append(String.format("%n%n%n"));
		message.append(String.format("=========================%n"));
		message.append(String.format("AUTO-CONFIGURATION REPORT%n"));
		message.append(String.format("=========================%n%n%n"));
		message.append(String.format("Positive matches:%n"));
		message.append(String.format("-----------------%n"));
		Map<String, ConditionAndOutcomes> shortOutcomes = orderByName(
				report.getConditionAndOutcomesBySource());
		for (Map.Entry<String, ConditionAndOutcomes> entry : shortOutcomes.entrySet()) {
			if (entry.getValue().isFullMatch()) {
				addLogMessage(message, entry.getKey(), entry.getValue());
			}
		}
		message.append(String.format("%n%n"));
		message.append(String.format("Negative matches:%n"));
		message.append(String.format("-----------------%n"));
		for (Map.Entry<String, ConditionAndOutcomes> entry : shortOutcomes.entrySet()) {
			if (!entry.getValue().isFullMatch()) {
				addLogMessage(message, entry.getKey(), entry.getValue());
			}
		}
		message.append(String.format("%n%n"));
		message.append(String.format("Exclusions:%n"));
		message.append(String.format("-----------%n"));
		if (report.getExclusions().isEmpty()) {
			message.append(String.format("%n    None%n"));
		}
		else {
			for (String exclusion : report.getExclusions()) {
				message.append(String.format("%n    %s%n", exclusion));
			}
		}
		message.append(String.format("%n%n"));
		message.append(String.format("Unconditional classes:%n"));
		message.append(String.format("----------------------%n"));
		if (report.getUnconditionalClasses().isEmpty()) {
			message.append(String.format("%n    None%n"));
		}
		else {
			for (String unconditionalClass : report.getUnconditionalClasses()) {
				message.append(String.format("%n    %s%n", unconditionalClass));
			}
		}
		message.append(String.format("%n%n"));
		return message;
	}

	private Map<String, ConditionAndOutcomes> orderByName(
			Map<String, ConditionAndOutcomes> outcomes) {
		Map<String, ConditionAndOutcomes> result = new LinkedHashMap<String, ConditionAndOutcomes>();
		List<String> names = new ArrayList<String>();
		Map<String, String> classNames = new HashMap<String, String>();
		for (String name : outcomes.keySet()) {
			String shortName = ClassUtils.getShortName(name);
			names.add(shortName);
			classNames.put(shortName, name);
		}
		Collections.sort(names);
		for (String shortName : names) {
			result.put(shortName, outcomes.get(classNames.get(shortName)));
		}
		return result;
	}

	private void addLogMessage(StringBuilder message, String source,
			ConditionAndOutcomes conditionAndOutcomes) {
		message.append(String.format("%n   %s", source));
		message.append(conditionAndOutcomes.isFullMatch() ? " matched" : " did not match")
				.append(String.format("%n"));
		for (ConditionAndOutcome conditionAndOutcome : conditionAndOutcomes) {
			message.append("      - ");
			if (StringUtils.hasLength(conditionAndOutcome.getOutcome().getMessage())) {
				message.append(conditionAndOutcome.getOutcome().getMessage());
			}
			else {
				message.append(conditionAndOutcome.getOutcome().isMatch() ? "matched"
						: "did not match");
			}
			message.append(" (");
			message.append(ClassUtils
					.getShortName(conditionAndOutcome.getCondition().getClass()));
			message.append(String.format(")%n"));
		}
	}

	@Override
	public String toString() {
		return this.message.toString();
	}

}
