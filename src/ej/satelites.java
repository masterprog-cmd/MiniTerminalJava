package ej;

public class satelites extends astros {
	private double distanciaSol;
	private double orbitaPlanetaria;
	private String planetaPerteneciente;

	public satelites(String nombre, double radio, double rotacionEje, double masa, double tempMedia, double gravedad,
			double distanciaSol, double orbitaPlanetaria, String planetaPerteneciente) {
		super(nombre, radio, rotacionEje, masa, tempMedia, gravedad);
		this.distanciaSol = distanciaSol;
		this.orbitaPlanetaria = orbitaPlanetaria;
		this.planetaPerteneciente = planetaPerteneciente;
	}

	public double getDistanciaSol() {
		return distanciaSol;
	}

	public double getOrbitaPlanetaria() {
		return orbitaPlanetaria;
	}

	public String getPlanetaPerteneciente() {
		return planetaPerteneciente;
	}

	@Override
	public String toString() {
		return "=====INFO SATELITE=====\nNombre del astro: " + nombre + "\nRadio: " + radio + "\nRotaci�n eje: "
				+ rotacionEje + "\nMasa: " + masa + "\nTemperatura media: " + tempMedia + "\nGravedad: " + gravedad
				+ "\nDistancia del sol: " + distanciaSol + "\nOrbita planetaria: " + orbitaPlanetaria
				+ "\nPertenece al planeta: " + planetaPerteneciente;
	}

	public void setDistanciaSol(double distanciaSol) {
		this.distanciaSol = distanciaSol;
	}

	public void setOrbitaPlanetaria(double orbitaPlanetaria) {
		this.orbitaPlanetaria = orbitaPlanetaria;
	}

	public void setPlanetaPerteneciente(String planetaPerteneciente) {
		this.planetaPerteneciente = planetaPerteneciente;
	}

}