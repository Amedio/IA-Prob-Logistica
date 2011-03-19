package ialogistica;

import java.util.ArrayList;

public class Camion {
	private int capacidad;
	private int pesoActual;
	private ArrayList<Peticion> peticiones;

	public Camion() {
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
		if (this.peticiones.add(peticion)){
			this.pesoActual += peticion.getCantidadPeticion();
			peticion.setAsignada(true);
		}
	}

	public void removePeticion(Peticion peticion) {
		if (this.peticiones.remove(peticion))
		{
			this.pesoActual -= peticion.getCantidadPeticion();
			peticion.setAsignada(false);
		}
	}

	public Peticion getPeticion(int idPeticion) {
		for (int j = 0; j < peticiones.size(); ++j) {
			if (peticiones.get(j).getIdPeticion() == idPeticion)
				return peticiones.get(j);
		}
		return null;
	}

	public boolean isFull(int cantidadPeticion) {
		return capacidad < (pesoActual + cantidadPeticion);
	}

	@Override
	public String toString() {
		String result = "Capacidad: " + capacidad + " -- Peticiones: ";

		for (int i = 0; i < peticiones.size(); i++) {
			result += peticiones.get(i).getIdPeticion() + ", ";
		}

		return result;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Camion result = new Camion();

		result.capacidad = this.capacidad;
		result.pesoActual = this.pesoActual;
		result.peticiones = new ArrayList<Peticion>();

		for (int i = 0; i < this.peticiones.size(); i++) {
			result.peticiones.add((Peticion) this.peticiones.get(i).clone());
		}

		return result;
	}
}
