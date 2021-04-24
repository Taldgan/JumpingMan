package application;

public interface Collidable {
	public boolean collide(double x, double y, double w, double h);
	public void setCollidable(boolean isCollidable);
	public boolean isCollidable();
}
