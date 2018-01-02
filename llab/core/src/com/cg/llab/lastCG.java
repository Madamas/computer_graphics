package com.cg.llab;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Vector;

public class lastCG extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	Pixmap pixmap;
	Texture texture;
	Sprite sprite;
	ArrayList<Point> arrl,arrAppr;
	float w;
	float h;
	boolean flag = false;
	private Queue<Point> queue = new LinkedList<Point>();
	List<Point> mahList;
	ArrayList<List> intersections;
	private static final double SX = 0.008;

	private static final double SY = 0.008;

	private static final double DX = -350;

	private static final double DY = -235;

	private static final int COUNT_ITER = 5000;

	private static final int BAIL_OUT = 1;

	private static final int STEP_X = 10;

	private static final int STEP_Y = 10;
	static int ITERATIONS = 23;
	
	@Override
	public void create () {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		batch = new SpriteBatch();
		pixmap = new Pixmap(800,600,Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		texture = new Texture(flipPixmap(pixmap));
		sprite = new Sprite(texture);
		sprite.setPosition(w/2 -sprite.getWidth()/2, h/2 - sprite.getHeight()/2);
		intersections = new ArrayList<List>();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public boolean keyDown (int keycode) {

		if (Gdx.input.isKeyPressed(Input.Keys.DEL)){
			pixmap.dispose();
			texture.dispose();
			pixmap = new Pixmap(800,600,Pixmap.Format.RGBA8888);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
			pixmap.setColor(Color.WHITE);
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
			paint(pixmap);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
			drawFractals(pixmap);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
			int x0 = (int) w / 4; // задаем координаты центра фрактала
			int y0 = (int) (h - h / 4);
			int d = 1; // задаем изначальную длину
			drawFiboWord(pixmap,x0,y0,d,ITERATIONS);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
		}
		return true;
	}

	public boolean keyUp (int keycode) {
		return false;
	}

	public boolean keyTyped (char character) {
		return false;
	}

	public boolean touchDown (int x, int y, int pointer, int button) {
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			int x0 = Gdx.input.getX() - (int) sprite.getWidth() / 2 + Gdx.graphics.getWidth() / 2; // задаем координаты центра фрактала
			int y0 = Gdx.graphics.getHeight() - Gdx.input.getY() - (int) sprite.getHeight() / 2 + Gdx.graphics.getHeight() / 2;
			int d = 1; // задаем изначальную длину
			drawFiboWord(pixmap,x0,y0,d,ITERATIONS);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
		}
		return true;
	}

	public boolean touchUp (int x, int y, int pointer, int button) {

		return false;
	}

	public boolean touchDragged (int x, int y, int pointer) {
		return false;
	}

	public boolean mouseMoved (int x, int y) {
		return false;
	}

	public boolean scrolled (int amount) {
		return false;
	}

	public void paint(Pixmap pixmap) {
		pixmap.setColor(Color.WHITE);

		Point a = new Point(0, 10);
		Point b = new Point((int)w, 10);

		drawKochLine(pixmap, a, b, 0, 10);
	}
	public void drawKochLine(Pixmap pixmap, Point a, Point b, double fi, int n) {

		if (n <= 0) {
			// рисуем прямую, если достигнута необходимая глубина рекурсии.
			pixmap.drawLine(a.x, a.y, b.x, b.y);
		} else {
			// находим длину отрезка (a; b).
			double length = Math.pow(Math.pow(b.y - a.y, 2)
					+ Math.pow(b.x - a.x, 2), 0.5);
			// находим длину 1/3 отрезка (a; b)
			double length1of3 = length / 3;

			// находим т., делящую отрезок как 1:3.
			Point a1 = new Point(a.x
					+ (int) Math.round((length1of3 * Math.cos(fi))), a.y
					+ (int) Math.round((length1of3 * Math.sin(fi))));

			// находим т., делящую отрезок как 2:3.
			Point b1 = new Point(a1.x
					+ (int) Math.round((length1of3 * Math.cos(fi))), a1.y
					+ (int) Math.round((length1of3 * Math.sin(fi))));

			// находим т., которая будет вершиной равностороннего
			// треугольника.
			Point c = new Point(a1.x
					+ (int) Math
					.round((length1of3 * Math.cos(fi + Math.PI / 3))),
					a1.y
							+ (int) Math.round((length1of3 * Math.sin(fi
							+ Math.PI / 3))));

			n--;
			drawKochLine(pixmap, a1, c, fi + Math.PI / 3, n);
			drawKochLine(pixmap, c, b1, fi - Math.PI / 3, n);

			n--;
			drawKochLine(pixmap, a, a1, fi, n);
			drawKochLine(pixmap, b1, b, fi, n);
		}
	}

	private static void drawFractals(Pixmap pixmap) {

		pixmap.setColor(Color.WHITE); // устанавливаем цвет черный

		for (int i = 0; i < Gdx.graphics.getWidth(); i += STEP_X) {
			for (int j = 0; j < Gdx.graphics.getHeight(); j += STEP_Y) {
				double c = SX * (i + DX); // центрируем по X
				double d = SY * (j + DY); // центрируем по Y
				double x = c; // ось х
				double y = d; // ось y
				double t;
				int k = 0;
				pixmap.setColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),(float)1));
				while (x * x + y * y < BAIL_OUT && k < COUNT_ITER) { // алгоритм
					t = x * x - y * y + c;
					y = 2 * x * y + d;
					x = t;
					pixmap.drawCircle((int) (x / SX - DX), (int) (y / SY - DY), 1);
					++k;
				}
			}
		}
	}

	public static void drawFiboWord(Pixmap pixmap, int x0, int y0, int a, int n) {
		//x0 y0 - исходные координаты
		//a - длина сегмента в пикселях
		//n - количество итераций

		String s;
		String s1 = "1";
		String s2 = "0";
		for (int i = 0; i < n - 2; i++) {
			s = s2 + s1;
			s1 = s2;
			s2 = s;
		}
		if (n == 1) {
			s = "1";
		} else {
			s = s2;
		}

		int vx = 0, vy = -1;
		int x1 = x0;
		int y1 = y0;
		int x2, y2, temp;

		for (int i = 0; i < s.length(); i++) {
			x2 = x1 + a * vx;
			y2 = y1 + a * vy;
			pixmap.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
			if (s.charAt(i) == '0') {
				if (i % 2 == 0) {
					temp = vx;
					vx = -1 * vy;
					vy = temp;
				} else {
					temp = vx;
					vx = vy;
					vy = -1 * temp;
				}
			}
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

//	private double steps(Complex c, int max){
//		Complex z = new Complex(0, 0);
//		for (int i = 0; i < max; i++){
//			z = c.plus(z.mult(z));
//			if (z.mod2() > 4)
//				return i;
//		}
//		return 0;
//	}
//
//	private void mandelbrot(int max){
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		PixelWriter pw = gc.getPixelWriter();
//
//		double pmin = -2;
//		double pmax = 1;
//		double qmin = -1;
//		double eps = (pmax - pmin)/w;
//		double p, q;
//		double color;
//		Complex c;
//
//		for (int y = 0; y < h; y++){
//			for (int x = 0; x < w; x++){
//				p = pmin + x*eps;
//				q = qmin + y*eps;
//				c = new Complex(p, q);
//				color = steps(c, max);
//				pw.setColor(x, y, Color.rgba8888((int) color/(max*1.5),(int) color/(max*4), (int)color/(max),1));
//			}
//		}
//	}
}
//class Complex {
//
//	double re;
//	double im;
//
//	Complex(double re, double im){
//		this.re = re;
//		this.im = im;
//	}
//	Complex plus(Complex c){
//		return new Complex(re + c.re, im + c.im);
//	}
//	Complex minus(Complex c){
//		return new Complex(re - c.re, im - c.im);
//	}
//	Complex mult(Complex c){
//		return new Complex(re * c.re - im * c.im, 2*re * c.im);
//	}
//	double mod2(){
//		return re*re + im*im;
//	}
//}