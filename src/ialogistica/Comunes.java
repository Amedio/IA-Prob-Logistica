package ialogistica;

import java.util.Random;

public class Comunes {
	public static final int NUM_CENTROS = 6;
	public static final int NUM_HORAS = 10;
	
	public static final Random MyRandom = new Random(1);

	public static final int[] CapacidadesMaximasCamiones = new int[] { 500,
			1000, 2000 };

	public static final double[] PreciosPesosPeticion = new double[] { 1, 1,
			1.5, 1.5, 2 };
}
