package Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebManager {
	public static void wget(String arg) throws Exception {
		File file = new File("Escritorio/DescargasWeb/");
		URLConnection url = new URL(arg).openConnection();

		if (!file.exists())
			try {
				url.connect();
				System.out.println("\nempezando descarga: \n");
				System.out.println(">> URL: " + url);
				System.out.println(">> Nombre: " + arg);
				System.out.println(">> tamaño: " + url.getContentLength() + " bytes");
				InputStream in = url.getInputStream();
				OutputStream out = new FileOutputStream(file);
				int b = 0;
				while (b != -1) {
					b = in.read();
					if (b != -1)
						out.write(b);
				}
				out.close();
				in.close();

			} catch (MalformedURLException e) {
				System.out.println("El URL " + url + "no existe");
			} catch (IOException e) {
				e.getMessage();
			}
	}

}
