package EjerciciosProg;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EjProg {
	public static void Fibonacci() throws Exception {
		int f = 0;
		int t1 = 1;
		int t2;
		FileWriter Fabonacci = null;
		try {
			Fabonacci = new FileWriter("/home/xavgom/Escritorio/Fibonacci.txt");
			for (int i = 0; i < 100; i++) {
				if (t1 % 2 == 0)
					Fabonacci.write("" + t1 + "\n");
				t2 = f;
				f = t1 + f;
				t1 = t2;

			}
			Fabonacci.flush();
		} catch (IOException e) {
			System.out.println("El error es: " + e);
		} finally {
			Fabonacci.close();
		}
	}

	public static void MaxyMin(int arg, int arg1) {
		if (arg <= arg1)
			System.out.println(arg + " es menor que " + arg1);
		else
			System.out.println(arg + " es mayor que " + arg1);
	}

	public static void palindromo(String arg) throws Exception {
		PrintWriter salida = null;

		try {
			salida = new PrintWriter("Palindromo.txt");
			String invertida = "";

			String PalabraSinEspacios = arg.replace(" ", "");

			StringBuffer StringF = new StringBuffer(PalabraSinEspacios);

			StringF = StringF.reverse();
			for (int indice = arg.length() - 1; indice >= 0; indice--) {

				invertida += arg.charAt(indice);
			}

			if (PalabraSinEspacios.equalsIgnoreCase(StringF.toString())) {

				salida.print(arg + " al reves " + invertida + " es palindromo ");
			} else {
				salida.print(arg + " al reves " + invertida + " no es palindromo ");
			}

			salida.flush();
		} finally {
			salida.close();
		}
	}
}
