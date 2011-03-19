package ialogistica;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class ProbLogistica {

	public static final double P = 0.8;
	public static int tipo_heur;

	public static void main(String[] args) {
		/*
		 * if (args.length != 6 && args.length != 10) { System.out.println(
		 * "Parametros: random_seed numero_grupos numero_helicopteros solucion_inicial heuristico busqueda"
		 * ); System.out.println("");
		 * System.out.println("random_seed: Numero para generar la semilla");
		 * System
		 * .out.println("numeros_grupos: Numero de grupos en el problema");
		 * System
		 * .out.println("numero_helicopteros: Numero de helicopteros del problema"
		 * ); System.out.println(
		 * "solucion_inicial: algoritmo que genera la solucion inicial");
		 * System.out.println("                  1->solucionSimple");
		 * System.out.println("                  2->solucionMejor");
		 * System.out.println("heuristico: funcion heuristica a usar");
		 * System.out.println("            1-> tiempoTotal");
		 * System.out.println("            2-> tiempoHeuristico");
		 * System.out.println("busqueda: algoritmo de busqueda a usar");
		 * System.out.println("          1-> hill climbing");
		 * System.out.println("          2-> simulated annealing");
		 * System.out.println(""); System.out.println(
		 * "El algoritmo simulated annealing necesita 4 parametros extra: iteraciones, iteraciones por paso, k, lambda"
		 * ); System.out.println(""); System.exit(-1); }
		 */

		Comunes.MyRandom.setSeed(1);//Integer.parseInt(args[0]));
		// int numero_grupos = Integer.parseInt(args[1]);
		// int numero_helicopteros = Integer.parseInt(args[2]);

		int[] ggg = new int[3];
		ggg[0] = 20;
		ggg[1] = 20;
		ggg[2] = 20;

		EntregasWorld entregas = new EntregasWorld(10, ggg);

		//entregas.generateSimpleSolution();
		entregas.generateBestSolution();
		
		System.out.println(entregas.toString());
		/*
		 * if (Integer.parseInt(args[3]) == 1) {
		 * entregas.generateSimpleSolution(); } else if
		 * (Integer.parseInt(args[3]) == 2) { entregas.generateBestSolution(); }
		 * else { throw new IllegalArgumentException(
		 * "Parametros incorrectos. Ejecuta el programa sin parametros para ver la ayuda."
		 * ); }
		 * 
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
/*	private static void BusquedaHC(MapaBoard mapa, int funcion_heuristica) {

		long start = System.currentTimeMillis();
		System.out.println("Hill Climbing");
		System.out.println("-------------");

		try {

			Problem problem;

			// Funcion heuristica segun el parametro introducido
			if (funcion_heuristica == 1) {
				Main.tipo_heur = 1;
				problem = new Problem(mapa, new AimaSucesorHC(),
						new AimaGoalTest(), new AimaHeuristic1());
			} else if (funcion_heuristica == 2) {
				Main.tipo_heur = 2;
				problem = new Problem(mapa, new AimaSucesorHC(),
						new AimaGoalTest(), new AimaHeuristic2());
			} else {
				throw new IllegalArgumentException(
						"Parametros incorrectos. Ejecuta el programa sin parametros para ver la ayuda.");
			}

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
	}*/

	/**
	 * Algoritmo Simulated Annealing
	 */
	/*private static void BusquedaSA(MapaBoard mapa, int funcion_heuristica,
			int iteraciones, int iteraciones_por_paso, int k, double lambda) {

		long start = System.currentTimeMillis();
		System.out.println("Simulated Annealing");
		System.out.println("-------------------");

		try {

			Problem problem;

			// Funcion heuristica segun el parametro introducido
			if (funcion_heuristica == 1) {
				problem = new Problem(mapa, new AimaSucesorSA(),
						new AimaGoalTest(), new AimaHeuristic1());
			} else if (funcion_heuristica == 2) {
				problem = new Problem(mapa, new AimaSucesorSA(),
						new AimaGoalTest(), new AimaHeuristic2());
			} else {
				throw new IllegalArgumentException(
						"Parametros incorrectos. Ejecuta el programa sin parametros para ver la ayuda.");
			}

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
	}*/

	/*private static void printInstrumentation(Properties properties) {
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
	}*/

}
