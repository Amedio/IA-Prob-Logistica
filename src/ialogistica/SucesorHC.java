package ialogistica;

import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class SucesorHC implements SuccessorFunction {

	@Override
	public List getSuccessors(Object arg0) {
		ArrayList result = new ArrayList();
		EntregasWorld worldActual = (EntregasWorld) arg0;

		ArrayList<Peticion> peticiones = null;
		Peticion peticion = null;
		EntregasWorld worldCopia = null;

		// MOVE PETICIONES
		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			peticiones = worldActual.centrosPeticiones.get(i);
			for (int j = 0; j < peticiones.size(); j++) {
				peticion = peticiones.get(j);
				for (int z = 0; z < Comunes.NUM_HORAS; z++) {
					if (worldActual.matrizCentrosHoras[i][z]
							.getPeticion(peticion.getIdPeticion()) == null
							&& !worldActual.matrizCentrosHoras[i][z]
									.isFull(peticion.getCantidadPeticion())) {
						try {
							worldCopia = (EntregasWorld) worldActual.clone();
							if (worldCopia.move(i, z, peticion.getIdPeticion()))
								result.add(new Successor("MOVE", worldCopia));
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// SWAP CANTIDAD CAMIONES
		for (int centro1 = 0; centro1 < Comunes.NUM_CENTROS; centro1++) {
			for (int hora1 = 0; hora1 < Comunes.NUM_HORAS; hora1++) {
				for (int centro2 = centro1; centro2 < Comunes.NUM_CENTROS; centro2++) {
					for (int hora2 = hora1 + 1; hora2 < Comunes.NUM_HORAS; hora2++) {
						try {
							worldCopia = (EntregasWorld) worldActual.clone();
							if (worldCopia.swapCapacidadCamiones(centro1,
									hora1, centro2, hora2))
								result.add(new Successor("SWAP CANTIDAD CAMIONES", worldCopia));
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// SWAP PETICIONES
		for (int centro = 0; centro < Comunes.NUM_CENTROS; centro++) {
			for (int peticion1 = 0; peticion1 < worldActual.centrosPeticiones
					.get(centro).size(); peticion1++) {
				for (int peticion2 = (peticion1 + 1); peticion2 < worldActual.centrosPeticiones
						.get(centro).size(); peticion2++) {
					try {
						worldCopia = (EntregasWorld) worldActual.clone();
						if(worldCopia.swap(centro, peticion1, peticion2))
							result.add(new Successor("SWAP", worldCopia));
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return result;
	}

}
