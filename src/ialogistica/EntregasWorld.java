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

	public ArrayList<ArrayList<Peticion>> getCentrosPeticiones() {
		return centrosPeticiones;
	}

	public void setCentrosPeticiones(
			ArrayList<ArrayList<Peticion>> centrosPeticiones) {
		this.centrosPeticiones = centrosPeticiones;
	}

	public Camion[][] getMatrizCentrosHoras() {
		return matrizCentrosHoras;
	}

	public void setMatrizCentrosHoras(Camion[][] matrizCentrosHoras) {
		this.matrizCentrosHoras = matrizCentrosHoras;
	}

	public int[] getTotalCamiones() {
		return totalCamiones;
	}

	public void setTotalCamiones(int[] totalCamiones) {
		this.totalCamiones = totalCamiones;
	}

	public void setLibresCamiones(int[] libresCamiones) {
		this.libresCamiones = libresCamiones;
	}

	public int[] getLibresCamiones() {
		return libresCamiones;
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

				camion.setPesoActual(camion.getPesoActual()
						+ peticiones.get(j).getCantidadPeticion());
				camion.addPeticion(peticiones.get(j));

				peticiones.get(j).setAsignada(true);
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
							camion.setPesoActual(camion.getPesoActual()
									+ peticiones.get(z).getCantidadPeticion());
							peticiones.get(z).setAsignada(true);
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
			result+="\n";
		}

		return result;
	}
}
