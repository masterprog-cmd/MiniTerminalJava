package EjerciciosProg;

import java.io.FileWriter;
import java.io.IOException;

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
}
