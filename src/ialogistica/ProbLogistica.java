package ialogistica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import aima.basic.Agent;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class ProbLogistica {
	public int heur;
	public static final double P = 0.8;
	public static int tipo_heur;

	private static BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));
	private static PrintStream out = System.out;

	public static void main(String[] args) throws Exception {

		
		out.println("All your base are belong to us!");
		out.println("Introduzca el número de peticiones:");
		int numPeticiones = Integer.parseInt(in.readLine());

		int[] camiones = new int[3];
		int numCamiones = 0;
		while (numCamiones != 60) {
			out.println("Introduzca el número de camiones de las siguientes cargas:");
			out.print("500 kg:");
			camiones[0] = Integer.parseInt(in.readLine());
			numCamiones = camiones[0];
			out.print("1000 kg:");
			camiones[1] = Integer.parseInt(in.readLine());
			numCamiones += camiones[1];
			out.print("2000 kg:");
			camiones[2] = Integer.parseInt(in.readLine());
			numCamiones += camiones[2];
			if (numCamiones != 60)
				out.println("Cantidad de camiones incorrecta, por favor introduzca una cantidad de camiones TOTAL igual a 60, solo ha introducido "
						+ numCamiones);
		}

		out.println("Creando Mundo...");
		EntregasWorld world = new EntregasWorld(numPeticiones, camiones);
		out.println("Hello World!");

		out.println();
		int solucion = 0;
		while (solucion != 1 && solucion != 2) {
			out.println("Elija la solución inicial:");
			out.println("1. Solución simple");
			out.println("2. Solucion mejor");
			solucion = Integer.parseInt(in.readLine());
			if (solucion != 1 && solucion != 2)
				out.println("Tiene que escoger una de las dos opciones!!!");
		}

		out.println("Generando solución inicial...");

		switch (solucion) {
		case 1:
			world.generateSimpleSolution();
			break;
		case 2:
			world.generateBestSolution();
			break;
		default:
			break;
		}
		
		out.println("Solución inicial generada...");
		out.println(world);
		out.println(world.getMaximizedBenefit());
				
		out.println();
		int funcHeur = 0;
		while (funcHeur != 1 && funcHeur != 2) {
			out.println("Elija la heurística:");
			out.println("1. Maximizar beneficio");
			out.println("2. Minimizar valor absoluto del tiempo de entrega");
			funcHeur = Integer.parseInt(in.readLine());
			if (funcHeur != 1 && funcHeur != 2)
				out.println("Tiene que escoger una de las dos opciones!!!");
		}

		out.println();
		int alg = 0;
		while (alg != 1 && alg != 2) {
			out.println("Elija la heurística:");
			out.println("1. Hill Climbing");
			out.println("2. Simulated Annealing");
			alg = Integer.parseInt(in.readLine());
			if (alg != 1 && alg != 2)
				out.println("Tiene que escoger una de las dos opciones!!!");
		}

		switch (alg) {
		case 1:
			BusquedaHC(world, funcHeur);
			break;
		case 2:
			BusquedaSA(world, funcHeur);
			break;
		default:
			break;
		}

		/*
		 * // Imprime por pantalla el estado de la solucion inicial
		 * System.out.println(entregas.toString());
		 * 
		 * // Ejecuta el algoritmo de AIMA correspondiente if
		 * (Integer.parseInt(args[5]) == 1) { BusquedaHC(entregas,
		 * Integer.parseInt(args[4])); } else if (Integer.parseInt(args[5]) ==
		 * 2) { BusquedaSA(entregas, Integer.parseInt(args[4]),
		 * Integer.parseInt(args[6]), Integer.parseInt(args[7]),
		 * Integer.parseInt(args[8]), Double.parseDouble(args[9])); } else {
		 * throw new IllegalArgumentException(
		 * "Parametros incorrectos. Ejecuta el programa sin parametros para ver la ayuda."
		 * ); }
		 */
	}

	/**
	 * Algoritmo Hill Climbing
	 */
	private static void BusquedaHC(EntregasWorld world, int funcion_heuristica) {

		long start = System.currentTimeMillis();
		System.out.println("Hill Climbing");
		System.out.println("-------------");

		try {

			Problem problem;
			HeuristicFunction heuristica = null;
			// Funcion heuristica segun el parametro introducido
			if (funcion_heuristica == 1) {
				// Main.tipo_heur = 1;
				heuristica = new MaximizedBenefitHeur();
			} else if (funcion_heuristica == 2) {
				// Main.tipo_heur = 2;
				heuristica = new MinimizedDeliverTimeHeur();
			} else {
				throw new IllegalArgumentException("Parametros incorrectos.");
			}

			problem = new Problem(world, new SucesorHC(), new EntregasGoalTest(), heuristica);

			// Iniciamos la clase AIMA
			Search search = new HillClimbingSearch();
			SearchAgent agent = new SearchAgent(problem, search);

			// Imprimimos los resultados
			long end = System.currentTimeMillis();
			System.out.println();
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			System.out.println("Tiempo: " + ((long) (end - start) / 1000.0)
					+ " segundos");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Algoritmo Simulated Annealing
	 */
	private static void BusquedaSA(EntregasWorld world, int funcion_heuristica) throws Exception{

		
		
		

		out.println("Introduzca iteraciones:");
		int iteraciones = Integer.parseInt(in.readLine());
		out.println("Introduzca las iteraciones por paso:");
		int iteraciones_por_paso  = Integer.parseInt(in.readLine());;
		out.println("Introduzca el factor K:");
		int k = Integer.parseInt(in.readLine());
		out.println("Introduzca el valor lambda:");
		double lambda = Integer.parseInt(in.readLine());
		
		long start = System.currentTimeMillis();
		System.out.println("Simulated Annealing");
		System.out.println("-------------------");

		try {

			Problem problem;

			HeuristicFunction heuristica = null;
			// Funcion heuristica segun el parametro introducido
			if (funcion_heuristica == 1) {
				heuristica = new MaximizedBenefitHeur();
			} else if (funcion_heuristica == 2) {
				heuristica = new MinimizedDeliverTimeHeur();
			} else {
				throw new IllegalArgumentException("Parametros incorrectos.");
			}
			problem = new Problem(world, new SucesorSA(),
					new EntregasGoalTest(), heuristica);

			// Iniciamos la clase AIMA con los parametros especificos del
			// Simulated Annealing
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(
					iteraciones, iteraciones_por_paso, k, lambda);
			// search.traceOn();
			SearchAgent agent = new SearchAgent(problem, search);

			// Imprimimos los resultados
			long end = System.currentTimeMillis();
			System.out.println();
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			System.out.println("Tiempo: " + ((long) (end - start) / 1000.0)
					+ " segundos");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printInstrumentation(Properties properties) {
		Iterator keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}

	}

	private static void printActions(List actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = (String) actions.get(i);
			System.out.println(action);
		}
	}

}
