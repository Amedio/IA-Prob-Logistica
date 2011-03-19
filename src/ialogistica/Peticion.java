package ialogistica;

public class Peticion {
	private int idPeticion;
	private int cantidadPeticion;
	private int horaEntrega;
	private boolean asignada;

	public void setIdPeticion(int idPeticion) {
		this.idPeticion = idPeticion;
	}

	public int getIdPeticion() {
		return idPeticion;
	}

	public int getCantidadPeticion() {
		return cantidadPeticion;
	}

	public void setCantidadPeticion(int cantidadPeticion) {
		this.cantidadPeticion = cantidadPeticion;
	}

	public void setHoraEntrega(int horaEntrega) {
		this.horaEntrega = horaEntrega;
	}

	public int getHoraEntrega() {
		return horaEntrega;
	}

	public void setAsignada(boolean asignada) {
		this.asignada = asignada;
	}

	public boolean isAsignada() {
		return asignada;
	}

	public double getPrecio() {
		double result = cantidadPeticion
				* Comunes.PreciosPesosPeticion[(cantidadPeticion / 100) - 1];

		return result;
	}

	@Override
	public String toString() {
		return "ID:" + idPeticion + " -- Cantidad: " + cantidadPeticion
				+ " -- Hora deseada: " + horaEntrega;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Peticion result = new Peticion();
		result.asignada = this.asignada;
		result.cantidadPeticion = this.cantidadPeticion;
		result.horaEntrega = this.horaEntrega;
		result.idPeticion = this.idPeticion;
		return result;
	}
}
