package ialogistica;

import aima.search.framework.HeuristicFunction;

public class MaximizedBenefitHeur implements HeuristicFunction {

	@Override
	public double getHeuristicValue(Object arg0) {
		EntregasWorld mundoEntregas = (EntregasWorld) arg0;
		return mundoEntregas.getMaximizedBenefit();
	}

}
