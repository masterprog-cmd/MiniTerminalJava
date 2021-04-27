package ej;

import java.util.ArrayList;

public class planetas extends astros {
	private double distanciaSol;
	private double orbitaSol;
	private boolean sat;

	ArrayList<Satelites> listadoSatelites = new ArrayList<Satelites>();

	public void aadirSatelite(Satelites s) {
		if (sat == true) {
			listadoSatelites.add(s);
		}
	}

	public planetas(String nombre, double radio, double rotacionEje, double masa, double tempMedia, double gravedad,
			double distanciaSol, double orbitaSol, boolean sat) {
		super(nombre, radio, rotacionEje, masa, tempMedia, gravedad);
		this.distanciaSol = distanciaSol;
		this.orbitaSol = orbitaSol;
		this.sat = sat;

	}

	public double getDistanciaSol() {
		return distanciaSol;
	}

	public double getOrbitaSol() {
		return orbitaSol;
	}

	public void recorrerArray() {
		for (Satelites lista : listadoSatelites) {
			System.out.println(lista);
		}

	}

	@Override
	public String toString() {
		return "=====INFO PLANETA=====" + "\nNombre del astro: " + nombre + "\nRadio: " + radio + "\nRotaci�n eje: "
				+ rotacionEje + "\nMasa: " + masa + "\nTemperatura media: " + tempMedia + "\nGravedad: " + gravedad
				+ "\nDistancia del sol: " + distanciaSol + "\nOrbita planetaria: " + orbitaSol + "\nTiene satelite: "
				+ sat;
	}

}