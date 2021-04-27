package ej;

public class astros {
	protected String nombre;
	protected double radio;
	protected double rotacionEje;
	protected double masa;
	protected double tempMedia;
	protected double gravedad;

	public astros(String nombre, double radio, double rotacionEje, double masa, double tempMedia, double gravedad) {
		this.nombre = nombre;
		this.radio = radio;
		this.rotacionEje = rotacionEje;
		this.masa = masa;
		this.tempMedia = tempMedia;
		this.gravedad = gravedad;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setRadio(double radio) {
		this.radio = radio;
	}

	public void setRotacionEje(double rotacionEje) {
		this.rotacionEje = rotacionEje;
	}

	public void setMasa(double masa) {
		this.masa = masa;
	}

	public void setTempMedia(double tempMedia) {
		this.tempMedia = tempMedia;
	}

	public void setGravedad(double gravedad) {
		this.gravedad = gravedad;
	}

	public String getnombre() {
		return nombre;
	}

	public double getRadio() {
		return radio;
	}

	public double getRotacionEje() {
		return rotacionEje;
	}

	public double getMasa() {
		return masa;
	}

	public double getTempMedia() {
		return tempMedia;
	}

	public double getGravedad() {
		return gravedad;
	}

	@Override
	public String toString() {
		return "Nombre: " + nombre;
	}

}