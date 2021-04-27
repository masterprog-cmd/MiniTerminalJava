package ej;

public class Satelites extends astros {
	private double distPlaneta;
	private double orbitaplaneta;
	private String planetaPert;

	public Satelites(String nombre, double radio, double rotacionEje, double masa, double tempMedia, double gravedad,
			double disPlaneta, double orbitaplaneta, String planetaPert) {
		super(nombre, radio, rotacionEje, masa, tempMedia, gravedad);
		this.distPlaneta = disPlaneta;
		this.orbitaplaneta = orbitaplaneta;
		this.planetaPert = planetaPert;
	}

	public double getDistPlaneta() {
		return distPlaneta;
	}

	public double getOrbitaplaneta() {
		return orbitaplaneta;
	}

	public String getPlanetaPert() {
		return planetaPert;
	}

	@Override
	public String toString() {
		return "=====INFO SAT�LITE=====" + "\nNombre del astro: " + nombre + "\nRadio: " + radio + "\nRotaci�n eje: "
				+ rotacionEje + "\nMasa: " + masa + "\nTemperatura media: " + tempMedia + "\nGravedad: " + gravedad
				+ "\nDistancia del sol: " + getDistPlaneta() + "\n�rbita al planeta: " + getOrbitaplaneta()
				+ "\nPertenece planeta: " + getPlanetaPert();
	}
}
