package ialogistica;

import java.util.ArrayList;
import java.util.Collections;

public class EntregasWorld {
	private final int NUM_CENTROS = 6;
	private final int NUM_HORAS = 10;

	private ArrayList<ArrayList<Peticion>> centrosPeticiones = new ArrayList<ArrayList<Peticion>>(
			NUM_CENTROS);
	private ArrayList<Peticion> totalPeticiones = new ArrayList<Peticion>();
	private Camion[][] matrizCentrosHoras = new Camion[NUM_CENTROS][10];
	private int[] totalCamiones = new int[3];
	private int[] libresCamiones = new int[3];

	public EntregasWorld() {
	}

	public EntregasWorld(int numPeticiones, int[] numCamiones) {
		totalCamiones = numCamiones.clone();
		libresCamiones = numCamiones.clone();
		for (int i = 0; i < NUM_CENTROS; i++) {
			centrosPeticiones.add(new ArrayList<Peticion>());
		}

		int centroActual, horaActual, cantidadPeticion = 0;
		for (int i = 0; i < numPeticiones; ++i) {
			centroActual = Comunes.MyRandom.nextInt(NUM_CENTROS);
			horaActual = Comunes.MyRandom.nextInt(NUM_HORAS);
			cantidadPeticion = Comunes.MyRandom.nextInt(5) + 1;

			Peticion peticion = new Peticion();
			peticion.setIdPeticion(i);
			peticion.setHoraEntrega(horaActual);
			peticion.setCantidadPeticion(cantidadPeticion * 100);
			peticion.setAsignada(false);

			centrosPeticiones.get(centroActual).add(peticion);
			totalPeticiones.add(peticion);
		}

		for (int i = 0; i < NUM_CENTROS; ++i) {
			Collections.sort(centrosPeticiones.get(i), new ComparePeticiones());
			Collections.sort(totalPeticiones, new ComparePeticiones());
		}

		for (int i = 0; i < NUM_CENTROS; i++) {
			for (int j = 0; j < NUM_HORAS; j++) {
				matrizCentrosHoras[i][j] = null;
			}
		}
	}

	public void generateSimpleSolution() {
		ArrayList<Peticion> peticiones;
		int centroActual, horaActual, capacidadCamion = 0;
		Camion camion = null;
		int counterCamiones = 0;
		for (int i = 0; i < NUM_CENTROS; ++i) {
			peticiones = centrosPeticiones.get(i);
			for (int j = 0; j < peticiones.size(); j++) {
				centroActual = Comunes.MyRandom.nextInt(NUM_CENTROS);
				horaActual = Comunes.MyRandom.nextInt(NUM_HORAS);
				camion = matrizCentrosHoras[centroActual][horaActual];

				while (counterCamiones++ < matrizCentrosHoras.length
						&& camion != null
						&& camion.isFull(peticiones.get(j)
								.getCantidadPeticion())) {
					centroActual = Comunes.MyRandom.nextInt(NUM_CENTROS);
					horaActual = Comunes.MyRandom.nextInt(NUM_HORAS);
					camion = matrizCentrosHoras[centroActual][horaActual];
				}
				counterCamiones = 0;

				if (camion == null) {
					capacidadCamion = Comunes.MyRandom.nextInt(3);
					camion = new Camion();
					while (libresCamiones[capacidadCamion] <= 0) {
						capacidadCamion = Comunes.MyRandom.nextInt(3);
					}
					camion.setCapacidad(Comunes.CapacidadesMaximasCamiones[capacidadCamion]);
					libresCamiones[capacidadCamion]--;
					matrizCentrosHoras[centroActual][horaActual] = camion;
				}

				camion.addPeticion(peticiones.get(j));

				totalPeticiones.remove(peticiones.get(j));
			}
		}
	}

	public void generateBestSolution() {
		ArrayList<Peticion> peticiones;
		Camion camion;
		int capacidadCamion = 2;
		for (int i = 0; i < NUM_HORAS; i++) {
			for (int j = 0; j < NUM_CENTROS; j++) {
				if (pendentPeticiones(j)) {
					peticiones = centrosPeticiones.get(j);
					camion = new Camion();
					while (libresCamiones[capacidadCamion] <= 0)
						capacidadCamion--;
					camion.setCapacidad(Comunes.CapacidadesMaximasCamiones[capacidadCamion]);
					for (int z = 0; z < peticiones.size()
							&& !camion.isFull(peticiones.get(z)
									.getCantidadPeticion()); z++) {
						if (!peticiones.get(z).isAsignada()) {
							camion.addPeticion(peticiones.get(z));
							totalPeticiones.remove(peticiones.get(z));
						}
					}
					matrizCentrosHoras[j][i] = camion;
				}
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
		for (int i = 0; i < NUM_CENTROS; i++) {
			for (int j = 0; j < NUM_HORAS; j++) {
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
		for (int i = 0; i < NUM_CENTROS; i++) {
			for (int j = 0; j < NUM_HORAS; j++) {
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

	public void swap(int centro, int peticion1, int peticion2) {
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
			camion2.removePeticion(aux2);
			camion2.addPeticion(aux1);
			camion1.removePeticion(aux1);
			camion1.addPeticion(aux2);
		}

	}

	@Override
	public String toString() {
		String result = new String();
		ArrayList<Peticion> peticiones;
		for (int i = 0; i < NUM_CENTROS; i++) {
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
		for (int i = 0; i < NUM_CENTROS; i++) {
			for (int j = 0; j < NUM_HORAS; j++) {
				camion = matrizCentrosHoras[i][j];
				result += "Centro: " + i + " -- Hora: " + (j + 8) + "\n";
				if (camion != null)
					result += camion.toString() + "\n";
				else
					result += "NO HAY ENTREGAS\n";
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
		for (int i = 0; i < NUM_CENTROS; i++) {
			peticionesCloned = new ArrayList<Peticion>();
			peticiones = centrosPeticiones.get(i);
			for (int j = 0; j < peticiones.size(); j++) {
				peticionCloned = (Peticion) peticiones.get(j).clone();
				peticionesCloned.add(peticionCloned);
			}
			result.centrosPeticiones.add(peticionesCloned);
		}

		result.libresCamiones = this.libresCamiones.clone();
		result.matrizCentrosHoras = this.matrizCentrosHoras.clone();
		result.totalCamiones = this.totalCamiones.clone();

		result.totalPeticiones = new ArrayList<Peticion>();
		for (int j = 0; j < this.totalPeticiones.size(); j++) {
			peticionCloned = (Peticion) totalPeticiones.get(j).clone();
			result.totalPeticiones.add(peticionCloned);
		}

		return result;
	}
}
