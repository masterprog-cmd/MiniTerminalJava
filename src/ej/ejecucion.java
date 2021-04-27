package ej;

import java.util.ArrayList;
import java.util.Scanner;

public class ejecucion {
	public static void main(String args[]) {
		Scanner teclado = new Scanner(System.in);
		ArrayList<astros> sistSolar = new ArrayList<astros>();
		int resp;
		// Creaci�n satelites(objetos)
		Satelites luna = new Satelites("Luna", 7.349E22, 3474.0, 27.5, 27.5, -153.0, 1.62, 384400.0, "Tierra");
		Satelites fobos = new Satelites("Fobos", 1.072E16, 11000.0, 0.0, 0.319, -40.15, 0.0084, 6000.0, "Marte");
		Satelites deimos = new Satelites("Deimos", 1.4762E15, 5687.0, 2.244E15, -40, 0.0039, 0.0143, 45634.0, "Marte");
		// Ceacion planetas(objetos)
		planetas tierra = new planetas("Tierra", 6371.0, 0.4119, 5.9736E24, 14.05, 9.8, 4.100, 1.000017421, true);
		planetas marte = new planetas("Marte", 3389.50, 1.850, 6.4185E23, -46.0, 3.711, 54600000, 24.077, true);
		planetas venus = new planetas("Venus", 5694.60, 0.7864, 5.783341, -37.0, 2.43, 34562434, 16.0, false);

		// A�adir en listado de astros
		sistSolar.add(tierra);
		sistSolar.add(venus);
		sistSolar.add(marte);
		sistSolar.add(luna);
		sistSolar.add(fobos);
		sistSolar.add(deimos);
		// Se a�aden los satelites a los planetas pertenecientes
		tierra.aadirSatelite(luna);
		marte.aadirSatelite(fobos);
		marte.aadirSatelite(deimos);

		// M�todo switch
		do {
			System.out.println("=====LISTA DE ASTROS====="
					+ "\nAstro 0 = Tierra\nAstro 1 = Venus\nAstro 2 = marte\n(Para salir -> -1)");
			resp = teclado.nextInt();
			switch (resp) {
			case -1:
				break;
			case 0:
				// Mostrar info planeta
				System.out.println(sistSolar.get(resp));
				System.out.println();
				// Mostrar info satelites si tiene
				tierra.recorrerArray();
				System.out.println();
				break;

			case 1:
				// Mostrar info planeta
				System.out.println(sistSolar.get(resp));
				System.out.println();
				// Mostrar info satelites si tiene
				venus.recorrerArray();
				System.out.println();
				break;

			case 2:
				// Mostrar info planeta
				System.out.println(sistSolar.get(resp));
				System.out.println();
				// Mostrar satelite
				marte.recorrerArray();
				System.out.println();
				break;

			default:
				break;
			}
		} while (resp != -1);
	}
}