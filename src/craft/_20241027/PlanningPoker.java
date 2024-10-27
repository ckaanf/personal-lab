package craft._20241027;

import java.util.Arrays;
import java.util.List;

public class PlanningPoker {
	public List<String> identifyExtremes(List<Estimate> estimates) {
		if (estimates == null) {
			throw new IllegalArgumentException("estimates cannot be null");
		}

		if (estimates.size() <= 1) {
			throw new IllegalArgumentException("there has to be more than 1 estimate in the list");
		}

		Estimate lowerEstimate = null;
		Estimate highestEstimate = null;

		for (Estimate estimate : estimates) {

			if (highestEstimate == null || estimate.getEstimate() > highestEstimate.getEstimate()) {
				highestEstimate = estimate;
			}
			if (lowerEstimate == null || estimate.getEstimate() < lowerEstimate.getEstimate()) {
				lowerEstimate = estimate;
			}
		}
		return Arrays.asList(
			lowerEstimate.getDeveloper(),
			highestEstimate.getDeveloper()
		);
	}
}
