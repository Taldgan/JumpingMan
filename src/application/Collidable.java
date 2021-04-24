package application;

public interface Collidable {
	public boolean collide(int x, int y, int width, int height);
	public void setCollidable();
	public boolean isCollidable();
}
