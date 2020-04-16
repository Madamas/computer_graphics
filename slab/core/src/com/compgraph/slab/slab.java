package com.compgraph.slab;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;

public class slab extends ApplicationAdapter {
	SpriteBatch batch;
	Texture texture;
	Pixmap pixmap;
	ArrayList<Point> arrl;
	OrthographicCamera camera;
	Sprite sprite;
	double gamma = 0;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		arrl = new ArrayList<Point>();
		batch = new SpriteBatch();
		pixmap = new Pixmap(1280,720,Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		texture = new Texture(flipPixmap(pixmap));
		sprite = new Sprite(texture);
		sprite.setPosition(w/2 -sprite.getWidth()/2, h/2 - sprite.getHeight()/2);
//		Gdx.graphics.setContinuousRendering(false);
//		Gdx.graphics.requestRendering();
	}

	@Override
	public void render () {

		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			arrl.add(new Point(Gdx.input.getX() - (int)sprite.getWidth()/2 + Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() - Gdx.input.getY() - (int)sprite.getHeight()/2 + Gdx.graphics.getHeight()/2));
			pixmap.drawPixel(Gdx.input.getX() - (int)sprite.getWidth()/2 + Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() - Gdx.input.getY() - (int)sprite.getHeight()/2 + Gdx.graphics.getHeight()/2);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
			bezierGoGo(arrl,pixmap);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
			//Gdx.graphics.requestRendering();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DEL)){
			arrl.clear();
			pixmap.dispose();
			texture.dispose();
			pixmap = new Pixmap(1280,720,Pixmap.Format.RGBA8888);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
			pixmap.setColor(Color.WHITE);
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				gamma = 10;
			for(Iterator<Point> i = arrl.iterator(); i.hasNext();){
				Point item = i.next();
				double x = item.getX();
				double y = item.getY();
				item.x = (int)x*(int)Math.cos(gamma);
				item.y = (int)x*(int)Math.sin(gamma)+(int)y*(int)Math.cos(gamma);
			}
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			sprite.rotate((float)gamma);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				gamma = -10;
				for(Iterator<Point> i = arrl.iterator(); i.hasNext();){
					Point item = i.next();
					double x = item.getX();
					double y = item.getY();
					item.x = (int)x*(int)Math.cos(gamma);
					item.y = (int)x*(int)Math.sin(gamma)+(int)y*(int)Math.cos(gamma);
				}
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				sprite.rotate((float)gamma);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.UP)){
				double M = 0.1;
				for(Iterator<Point> i = arrl.iterator(); i.hasNext();){
					Point item = i.next();
					item.x *= M;
					item.y *= M;
				}
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				sprite.scale((float)M);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				double M = -0.1;
				for(Iterator<Point> i = arrl.iterator(); i.hasNext();){
					Point item = i.next();
					item.x /= Math.abs(M);
					item.y /= Math.abs(M);
				}
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				sprite.scale((float)M);
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
				if(!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
					double delta = -10;
					for (Iterator<Point> i = arrl.iterator(); i.hasNext(); ) {
						Point item = i.next();
						double x = item.getX();
						item.x += delta;
					}
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					sprite.translateX((float) delta);
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
				if(!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
					double delta = 10;
					for (Iterator<Point> i = arrl.iterator(); i.hasNext(); ) {
						Point item = i.next();
						double x = item.getX();
						item.x += delta;
					}
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					sprite.translateX((float) delta);
				}
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
				if(!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
					double delta = -10;
					for (Iterator<Point> i = arrl.iterator(); i.hasNext(); ) {
						Point item = i.next();
						double y = item.getY();
						item.y += delta;
					}
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					sprite.translateY((float) delta);
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP)){
			if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
				if(!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
					double delta = 10;
					for (Iterator<Point> i = arrl.iterator(); i.hasNext(); ) {
						Point item = i.next();
						double y = item.getY();
						item.y += delta;
					}
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					sprite.translateY((float) delta);
				}
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
				if(Gdx.input.isKeyPressed(Input.Keys.UP)){
					for (Iterator<Point> i = arrl.iterator(); i.hasNext(); ) {
						Point item = i.next();
						double x = item.getX();
						item.x = (int)-x;
					}
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					sprite.flip(false,true);
				}
				if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
					for (Iterator<Point> i = arrl.iterator(); i.hasNext(); ) {
						Point item = i.next();
						double y = item.getY();
						item.x = (int)-y;
					}
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					sprite.flip(true,false);
				}
			}
		}
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
	}

	private void bezierGoGo(ArrayList<Point> sourcePoints, Pixmap painter){
		// ф-ия расчитывает финальный набор точек, по которым будет строится кривуля, а затем рисует ее
		ArrayList<Point> finalPoints = new ArrayList<>();

		for (double t=0; t<=1; t += 0.01)
			finalPoints.add(calculateBezierFunction(t, sourcePoints));
			drawCurve(finalPoints, painter);
	}

	private Point calculateBezierFunction(double t, ArrayList<Point> srcPoints)
	{   // ф-ия расчитывает очередную точку на кривой исходя из входного набора управляющих точек
		double x = 0;
		double y = 0;

		int n = srcPoints.size() - 1;
		for (int i=0; i <= n; i++)
		{
			x += fact(n)/(fact(i)*fact(n-i)) * srcPoints.get(i).getX() * Math.pow(t, i) * Math.pow(1-t, n-i);
			y += fact(n)/(fact(i)*fact(n-i)) * srcPoints.get(i).getY() * Math.pow(t, i) * Math.pow(1-t, n-i);
		}
		return new Point((int)x, (int)y);
	}

	private double fact(double arg){
		if (arg < 0) throw new RuntimeException("negative argument.");
		if (arg == 0) return 1;

		double result = 1;
		for (int i=1; i<=arg; i++)
			result *= i;
		return result;
	}

	private void drawCurve(ArrayList<Point> points, Pixmap painter){
		for (int i = 1; i < points.size(); i++)
		{
			int x1 = (int)(points.get(i-1).getX());
			int y1 = (int)(points.get(i-1).getY());
			int x2 = (int)(points.get(i).getX());
			int y2 = (int)(points.get(i).getY());
			painter.drawLine(x1, y1, x2, y2);
		}
	}

	public Pixmap flipPixmap(Pixmap src) {
		final int width = src.getWidth();
		final int height = src.getHeight();
		Pixmap flipped = new Pixmap(width, height, src.getFormat());

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				flipped.drawPixel(x, y, src.getPixel(x,height - y - 1));
			}
		}
		return flipped;
	}
}