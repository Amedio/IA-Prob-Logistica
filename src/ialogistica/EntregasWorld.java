package ialogistica;

import java.util.ArrayList;
import java.util.Collections;

public class EntregasWorld {

	public ArrayList<ArrayList<Peticion>> centrosPeticiones = new ArrayList<ArrayList<Peticion>>(
			Comunes.NUM_CENTROS);
	public ArrayList<Peticion> totalPeticiones = new ArrayList<Peticion>();
	public Camion[][] matrizCentrosHoras = new Camion[Comunes.NUM_CENTROS][10];
	public int[] totalCamiones = new int[3];
	public int[] libresCamiones = new int[3];

	public EntregasWorld() {
	}

	public EntregasWorld(int numPeticiones, int[] numCamiones) {
		totalCamiones = numCamiones.clone();
		libresCamiones = numCamiones.clone();
		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			centrosPeticiones.add(new ArrayList<Peticion>());
		}

		int centroActual, horaActual, cantidadPeticion = 0;
		for (int i = 0; i < numPeticiones; ++i) {
			centroActual = Comunes.MyRandom.nextInt(Comunes.NUM_CENTROS);
			horaActual = Comunes.MyRandom.nextInt(Comunes.NUM_HORAS);
			cantidadPeticion = Comunes.MyRandom.nextInt(5) + 1;

			Peticion peticion = new Peticion();
			peticion.setIdPeticion(i);
			peticion.setHoraEntrega(horaActual);
			peticion.setCantidadPeticion(cantidadPeticion * 100);
			peticion.setAsignada(false);

			centrosPeticiones.get(centroActual).add(peticion);
			totalPeticiones.add(peticion);
		}

		for (int i = 0; i < Comunes.NUM_CENTROS; ++i) {
			Collections.sort(centrosPeticiones.get(i), new ComparePeticiones());
			Collections.sort(totalPeticiones, new ComparePeticiones());
		}

		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			for (int j = 0; j < Comunes.NUM_HORAS; j++) {
				matrizCentrosHoras[i][j] = new Camion();
			}
		}
	}

	public void generateSimpleSolution() {
		ArrayList<Peticion> peticiones;
		int horaActual, capacidadCamion = 0;
		Camion camion = null;
		int counterCamiones = 0;
		for (int i = 0; i < Comunes.NUM_CENTROS; ++i) {
			for (int j = 0; j < Comunes.NUM_HORAS; j++) {
				capacidadCamion = Comunes.MyRandom.nextInt(3);
				camion = new Camion();
				while (libresCamiones[capacidadCamion] <= 0) {
					capacidadCamion = Comunes.MyRandom.nextInt(3);
				}
				camion.setCapacidad(Comunes.CapacidadesMaximasCamiones[capacidadCamion]);
				libresCamiones[capacidadCamion]--;
				matrizCentrosHoras[i][j] = camion;
			}

			peticiones = centrosPeticiones.get(i);
			for (int j = 0; j < peticiones.size(); j++) {
				horaActual = Comunes.MyRandom.nextInt(Comunes.NUM_HORAS);
				camion = matrizCentrosHoras[i][horaActual];

				while (counterCamiones++ < matrizCentrosHoras.length
						&& camion != null
						&& camion.isFull(peticiones.get(j)
								.getCantidadPeticion())) {
					horaActual = Comunes.MyRandom.nextInt(Comunes.NUM_HORAS);
					camion = matrizCentrosHoras[i][horaActual];
				}
				counterCamiones = 0;

				camion.addPeticion(peticiones.get(j));

				totalPeticiones.remove(peticiones.get(j));
			}
		}
	}

	public void generateBestSolution() {
		ArrayList<Peticion> peticiones;
		Camion camion;
		int capacidadCamion = 2;
		for (int i = 0; i < Comunes.NUM_HORAS; i++) {
			for (int j = 0; j < Comunes.NUM_CENTROS; j++) {
				camion = new Camion();
				while (libresCamiones[capacidadCamion] <= 0)
					capacidadCamion--;
				camion.setCapacidad(Comunes.CapacidadesMaximasCamiones[capacidadCamion]);
				if (pendentPeticiones(j)) {
					peticiones = centrosPeticiones.get(j);
					for (int z = 0; z < peticiones.size()
							&& !camion.isFull(peticiones.get(z)
									.getCantidadPeticion()); z++) {
						if (!peticiones.get(z).isAsignada()) {
							camion.addPeticion(peticiones.get(z));
							totalPeticiones.remove(peticiones.get(z));
						}
					}
				}
				matrizCentrosHoras[j][i] = camion;
			}
		}
	}

	private boolean pendentPeticiones(int numCentro) {
		boolean result = false;
		for (int i = 0; i < centrosPeticiones.get(numCentro).size() && !result; i++) {
			result = !centrosPeticiones.get(numCentro).get(i).isAsignada();
		}
		return result;
	}

	public double getMaximizedBenefit() {
		double result = 0;

		Camion camion;
		ArrayList<Peticion> peticionesCamion;
		double precioPeticion;
		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			for (int j = 0; j < Comunes.NUM_HORAS; j++) {
				camion = matrizCentrosHoras[i][j];
				if (camion != null) {
					peticionesCamion = camion.getPeticiones();
					for (int z = 0; z < peticionesCamion.size(); z++) {
						precioPeticion = peticionesCamion.get(z).getPrecio();
						if (j >= peticionesCamion.get(z).getHoraEntrega())
							result += precioPeticion;
						else
							result += (precioPeticion - (precioPeticion * (0.2 * (j - peticionesCamion
									.get(z).getHoraEntrega()))));
					}
				}
			}
		}

		for (int i = 0; i < totalPeticiones.size(); i++) {
			precioPeticion = totalPeticiones.get(i).getPrecio();
			result -= (precioPeticion + (precioPeticion * (0.2 * (9 - totalPeticiones
					.get(i).getHoraEntrega()))));
		}

		return result;
	}

	public double getMinimizedDeliverTime() {
		double result = 0;

		Camion camion;
		ArrayList<Peticion> peticionesCamion;
		double horaPeticion;
		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			for (int j = 0; j < Comunes.NUM_HORAS; j++) {
				camion = matrizCentrosHoras[i][j];
				if (camion != null) {
					peticionesCamion = camion.getPeticiones();
					for (int z = 0; z < peticionesCamion.size(); z++) {
						horaPeticion = peticionesCamion.get(z).getHoraEntrega();
						result -= Math.abs(j - horaPeticion);
					}
				}
			}
		}

		for (int i = 0; i < totalPeticiones.size(); i++) {
			horaPeticion = totalPeticiones.get(i).getHoraEntrega();
			result -= (24 - horaPeticion);
		}

		return result;
	}

	public boolean swap(int centro, int peticion1, int peticion2) {
		Peticion aux1 = null, aux2 = null;
		Camion camion1 = null, camion2 = null;
		for (int i = 0; i < matrizCentrosHoras[centro].length
				&& (aux1 == null || aux2 == null); ++i) {
			if (aux1 == null) {
				camion1 = matrizCentrosHoras[centro][i];
				if (camion1 != null)
					aux1 = camion1.getPeticion(peticion1);
			}
			if (aux2 == null) {
				camion2 = matrizCentrosHoras[centro][i];
				if (camion2 != null)
					aux2 = camion2.getPeticion(peticion2);
			}
		}

		if (camion1 != null && camion2 != null && aux1 != null && aux2 != null) {
			if (!camion1.isFull(aux2.getCantidadPeticion()
					- aux1.getCantidadPeticion())
					&& !camion2.isFull(aux1.getCantidadPeticion()
							- aux2.getCantidadPeticion())) {
				camion1.removePeticion(aux1);
				camion2.removePeticion(aux2);
				camion1.addPeticion(aux2);
				camion2.addPeticion(aux1);
				return true;
			}
		}

		if (aux1 == null)
			camion1 = null;

		if (aux2 == null)
			camion2 = null;

		for (int i = 0; i < centrosPeticiones.get(centro).size()
				&& (aux1 == null || aux2 == null); i++) {
			if (aux1 == null
					&& centrosPeticiones.get(centro).get(i).getIdPeticion() == peticion1)
				aux1 = centrosPeticiones.get(centro).get(i);
			if (aux2 == null
					&& centrosPeticiones.get(centro).get(i).getIdPeticion() == peticion2)
				aux2 = centrosPeticiones.get(centro).get(i);
		}

		if (aux1 != null && aux2 != null) {
			if (camion1 != null
					&& !camion1.isFull(aux2.getCantidadPeticion()
							- aux1.getCantidadPeticion())) {
				camion1.removePeticion(aux1);
				totalPeticiones.remove(aux2);
				camion1.addPeticion(aux2);
				totalPeticiones.add(aux1);
				return true;
			} else if (camion2 != null
					&& !camion2.isFull(aux1.getCantidadPeticion()
							- aux2.getCantidadPeticion())) {
				camion2.removePeticion(aux2);
				totalPeticiones.remove(aux1);
				camion2.addPeticion(aux1);
				totalPeticiones.add(aux2);
				return true;
			}
		}
		return false;
	}

	public boolean move(int centro, int hora, int peticion) {
		Camion camion = null;
		Peticion aux1 = null;
		for (int i = 0; i < Comunes.NUM_HORAS; i++) {
			camion = matrizCentrosHoras[centro][i];
			if (camion != null) {
				aux1 = camion.getPeticion(peticion);
				camion.removePeticion(aux1);
			}
		}

		if (aux1 == null) {
			for (int i = 0; i < centrosPeticiones.get(centro).size(); i++) {
				if (centrosPeticiones.get(centro).get(i).getIdPeticion() == peticion) {
					aux1 = centrosPeticiones.get(centro).get(i);
					totalPeticiones.remove(aux1);
				}
			}
		}

		camion = matrizCentrosHoras[centro][hora];
		camion.addPeticion(aux1);
		return true;
	}

	public boolean swapCapacidadCamiones(int centro1, int hora1, int centro2,
			int hora2) {
		Camion camion1 = matrizCentrosHoras[centro1][hora1];
		Camion camion2 = matrizCentrosHoras[centro2][hora2];

		if (camion1.swappable(camion2)) {
			int capacidad1 = camion1.getCapacidad();
			camion1.setCapacidad(camion2.getCapacidad());
			camion2.setCapacidad(capacidad1);
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		String result = new String();
		ArrayList<Peticion> peticiones;
		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			result += "Centro (" + i + "):\n";
			peticiones = centrosPeticiones.get(i);
			for (int j = 0; j < peticiones.size(); j++) {
				result += peticiones.get(j).toString() + "\n";
			}
			result += "\n\r";
		}

		result += "\n\r";
		result += "\n\r";

		Camion camion;
		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			for (int j = 0; j < Comunes.NUM_HORAS; j++) {
				camion = matrizCentrosHoras[i][j];
				if(camion.getPesoActual()!=0){
					result += "Centro: " + i + " -- Hora: " + (j + 8) + "\n";
					result += camion.toString() + "\n";
				}
			}
			result += "\n";
		}

		return result;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		EntregasWorld result = new EntregasWorld();
		result.centrosPeticiones = new ArrayList<ArrayList<Peticion>>();
		ArrayList<Peticion> peticiones;
		ArrayList<Peticion> peticionesCloned;
		Peticion peticionCloned;
		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			peticionesCloned = new ArrayList<Peticion>();
			peticiones = centrosPeticiones.get(i);
			for (int j = 0; j < peticiones.size(); j++) {
				peticionCloned = (Peticion) peticiones.get(j).clone();
				peticionesCloned.add(peticionCloned);
			}
			result.centrosPeticiones.add(peticionesCloned);
		}

		result.libresCamiones = this.libresCamiones.clone();
		result.matrizCentrosHoras = new Camion[Comunes.NUM_CENTROS][Comunes.NUM_HORAS];
		for (int i = 0; i < Comunes.NUM_CENTROS; i++) {
			for (int j = 0; j < Comunes.NUM_HORAS; j++) {
				result.matrizCentrosHoras[i][j] = (Camion) matrizCentrosHoras[i][j]
						.clone();
			}
		}

		result.totalCamiones = this.totalCamiones.clone();

		result.totalPeticiones = new ArrayList<Peticion>();
		for (int j = 0; j < this.totalPeticiones.size(); j++) {
			peticionCloned = (Peticion) totalPeticiones.get(j).clone();
			result.totalPeticiones.add(peticionCloned);
		}

		return result;
	}
}
