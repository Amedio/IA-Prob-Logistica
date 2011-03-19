package ialogistica;

import java.util.ArrayList;

public class Camion {
	private int capacidad;
	private int pesoActual;
	private ArrayList<Peticion> peticiones;

	public Camion()
	{
		peticiones = new ArrayList<Peticion>();
	}
	
	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public int getPesoActual() {
		return pesoActual;
	}

	public void setPesoActual(int pesoActual) {
		this.pesoActual = pesoActual;
	}

	public ArrayList<Peticion> getPeticiones() {
		return peticiones;
	}

	public void setPeticiones(ArrayList<Peticion> peticiones) {
		this.peticiones = peticiones;
	}

	public void addPeticion(Peticion peticion) {
		this.peticiones.add(peticion);
	}

	@Override
	public String toString() {
		String result = "Capacidad: " + capacidad + " -- Peticiones: ";

		for (int i = 0; i < peticiones.size(); i++) {
			result += peticiones.get(i).getIdPeticion() + ", ";
		}

		return result;
	}

	public boolean isFull(int cantidadPeticion) {
		return capacidad < (pesoActual + cantidadPeticion);
	}
}
