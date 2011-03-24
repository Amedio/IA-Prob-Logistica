package ialogistica;

import java.util.ArrayList;
import java.util.List;

import aima.search.framework.SuccessorFunction;

public class SucesorHC implements SuccessorFunction {

	@Override
	public List getSuccessors(Object arg0) {
		ArrayList<EntregasWorld> result = new ArrayList<EntregasWorld>();
		EntregasWorld worldActual = (EntregasWorld) arg0;

		ArrayList<Peticion> peticiones = null;
		Peticion peticion = null;
		EntregasWorld worldCopia = null;
		
		//MOVE PETICIONES
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
							worldCopia = (EntregasWorld)worldActual.clone();
							worldCopia.move(i, z, peticion.getIdPeticion());
							result.add(worldCopia);
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		// TODO: SWAP CANTIDAD CAMIONES
		
		// TODO: SWAP PETICIONES

		return null;
	}

}
