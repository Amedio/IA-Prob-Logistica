package ialogistica;

import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class SucesorSA implements SuccessorFunction {

	@Override
	public List getSuccessors(Object arg0) {
		ArrayList result = new ArrayList();
		EntregasWorld worldActual = (EntregasWorld) arg0;

		EntregasWorld worldCopia = null;

		int funcionSucesor = Comunes.MyRandom.nextInt(3);

		switch (funcionSucesor) {
		case 0:
			int centro = Comunes.MyRandom.nextInt(Comunes.NUM_CENTROS);
			while (worldActual.centrosPeticiones.get(centro).size() < 2) {
				centro = Comunes.MyRandom.nextInt(Comunes.NUM_CENTROS);
			}
			ArrayList<Peticion> peticiones = worldActual.centrosPeticiones
					.get(centro);

			int peticionPos1 = Comunes.MyRandom.nextInt(peticiones.size());
			int peticionPos2 = Comunes.MyRandom.nextInt(peticiones.size());
			while (peticionPos1 == peticionPos2) {
				peticionPos2 = Comunes.MyRandom.nextInt(peticiones.size());
			}

			try {
				worldCopia = (EntregasWorld) worldActual.clone();
				if (worldCopia.swap(centro, peticiones.get(peticionPos1)
						.getIdPeticion(), peticiones.get(peticionPos2)
						.getIdPeticion()))
					result.add(new Successor("SWAP", worldCopia));
				else
					result = (ArrayList) getSuccessors(worldActual);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			centro = Comunes.MyRandom.nextInt(Comunes.NUM_CENTROS);
			int hora = Comunes.MyRandom.nextInt(Comunes.NUM_HORAS);
			peticiones = worldActual.centrosPeticiones.get(centro);
			peticionPos1 = Comunes.MyRandom.nextInt(peticiones.size());
			if (worldActual.matrizCentrosHoras[centro][hora]
					.getPeticion(peticiones.get(peticionPos1).getIdPeticion()) == null
					&& !worldActual.matrizCentrosHoras[centro][hora]
							.isFull(peticiones.get(peticionPos1)
									.getCantidadPeticion())) {
				try {
					worldCopia = (EntregasWorld) worldActual.clone();
					worldCopia.move(centro, hora, peticiones.get(peticionPos1)
							.getIdPeticion());
					result.add(new Successor("MOVE", worldCopia));
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			} else {
				result = (ArrayList) getSuccessors(worldActual);
			}
			break;
		case 2:
			int centro1 = Comunes.MyRandom.nextInt(Comunes.NUM_CENTROS);
			int hora1 = Comunes.MyRandom.nextInt(Comunes.NUM_HORAS);
			int centro2 = Comunes.MyRandom.nextInt(Comunes.NUM_CENTROS);
			int hora2 = Comunes.MyRandom.nextInt(Comunes.NUM_HORAS);

			try {
				worldCopia = (EntregasWorld) worldActual.clone();
				if (worldCopia.swapCapacidadCamiones(centro1, hora1, centro2,
						hora2))
					result.add(new Successor("SWAP CANTIDAD CAMIONES", worldCopia));
				else
					result = (ArrayList) getSuccessors(worldActual);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}

		return result;
	}
}
