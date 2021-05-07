package application.model;

/**
 * Collidable interface. Contains set of methods for any Object that should be collidable.
 * @author Thomas White
 *
 */
public interface Collidable {
	public boolean collide(double x, double y, double w, double h);
	public void setCollidable(boolean isCollidable);
	public boolean isCollidable();
}
