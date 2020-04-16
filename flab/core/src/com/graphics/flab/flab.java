package com.graphics.flab;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

import java.util.concurrent.Callable;

public class flab extends ApplicationAdapter {
	public class MyActor extends Actor{
		Texture texture = new Texture("jet.png");
		@Override
		public void draw(Batch batch, float alpha){
			batch.draw(texture,0,0);
		}
	}
	Stage stage;
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture texture;
	Pixmap pixmap,pmap;
	Sprite sprite;
	private static final int X0 = 0;
	private static final int X1 = 350;
	private static final int Y0 = 0;
	private static final int Y1 = 250;
	private static final int R = 50;
	private static final int CYCLES = 10000;
	@Override
	public void create () {
		camera = new OrthographicCamera(1280,720);
		batch = new SpriteBatch();
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		MyActor actor = new MyActor();

		pixmap = new Pixmap(1280,720, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pmap = brezLine(X0,X1,Y0,Y1,pixmap);
		pmap = ddaLine(X0+50,X1+50,Y0,Y1,pmap);
		pmap = wuLine(X0+100,X1+100,Y0,Y1,pmap);
		pmap = brezCircle(pmap,X1+R+20,Y1+R+20,R);
		pmap = michCircle(pmap,X1+3*R+40,Y1+R+20,R);
		texture = new Texture(flipPixmap(pmap));
		sprite = new Sprite(texture);
		measureTime();
		sprite.setOrigin(0,0);
		sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2);
		pixmap.dispose();
		stage.addActor(actor);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();
		}

	@Override
	public void dispose () {
		if(texture != null)
			texture.dispose();
		batch.dispose();
//		pixmap.dispose();
//		pmap.dispose();
		//textureAtlas.dispose();
	}

	public Pixmap brezLine(int x0,int x1,int y0,int y1,Pixmap pixmap){
		int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

		dx = x1 - x0;//проекция на ось икс
		dy = y1 - y0;//проекция на ось игрек

		incx = sign(dx);
	/*
	 * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
	 * справа налево по иксу, то incx будет равен -1.
	 * Это будет использоваться в цикле постороения.
	 */
		incy = sign(dy);
	/*
	 * Аналогично. Если рисуем отрезок снизу вверх -
	 * это будет отрицательный сдвиг для y (иначе - положительный).
	 */

		if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
		if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
		//эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

		if (dx > dy)
		//определяем наклон отрезка:
		{
	 /*
	  * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
	  * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
	  * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
	  * по y сдвиг такой отсутствует.
	  */
			pdx = incx;	pdy = 0;
			es = dy;	el = dx;
		}
		else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
		{
			pdx = 0;	pdy = incy;
			es = dx;	el = dy;//тогда в цикле будем двигаться по y
		}

		x = x0;
		y = y0;
		err = el/2;
		pixmap.drawPixel(x,y);
		//g.drawLine (x, y, x, y);//ставим первую точку
		//все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

		for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
		{
			err -= es;
			if (err < 0)
			{
				err += el;
				x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
				y += incy;//или сместить влево-вправо, если цикл проходит по y
			}
			else
			{
				x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
				y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
			}

			//g.drawLine (x, y, x, y);
			pixmap.drawPixel(x,y);
		}
		return pixmap;
	}

	public Pixmap ddaLine(double x0,double x1,double y0,double y1,Pixmap pixmap)
	{
		double dx,dy,steps,x,y,k;
		double xc,yc;
		dx=x1-x0;
		dy=y1-y0;
		if(Math.abs(dx)>Math.abs(dy))
			steps=Math.abs(dx);
		else
			steps=Math.abs(dy);
		xc=(dx/steps);
		yc=(dy/steps);
		x=x0;
		y=y0;
		pixmap.drawPixel((int)x0,(int)y0);
		for(k=1;k<=steps;k++)
		{
			x=x+xc;
			y=y+yc;
			pixmap.drawPixel((int)x,(int)y);
		}
		return pixmap;
	}

	private Pixmap wuLine(int x1, int x2, int y1, int y2, Pixmap pixmap) {
		if (x2 < x1) {
			x1 += x2;
			x2 = x1 - x2;
			x1 -= x2;
			y1 += y2;
			y2 = y1 - y2;
			y1 -= y2;
		}
		int dx = x2 - x1;
		int dy = y2 - y1;
		//Горизонтальные и вертикальные линии не нуждаются в сглаживании
		if (dx == 0 || dy == 0) {
			pixmap.setColor(Color.WHITE);
			pixmap.drawLine(x1, y1, x2, y2);
			return pixmap;
		}
		float gradient = 0;
		if (dx > dy) {
			gradient = (float) dy / dx;
			float intery = y1 + gradient;
			pixmap.setColor(Color.BLACK);
			pixmap.drawLine(x1, y1, x1, y1);
			for (int x = x1; x < x2; ++x) {
				pixmap.setColor(new Color(1, 1, 1, (int) (255 - fractionalPart(intery) * 255))); //Меняем прозрачность
				pixmap.drawLine(x, (int)intery, x, (int)intery);
				pixmap.setColor(new Color(1, 1, 1, (int) (fractionalPart(intery) * 255)));
				pixmap.drawLine(x, (int)intery + 1, x, (int)intery + 1);
				intery += gradient;
			}
			pixmap.setColor(Color.WHITE);
			pixmap.drawLine(x2, y2, x2, y2);
		}
		else {
			gradient = (float) dx / dy;
			float interx = x1 + gradient;
			pixmap.setColor(Color.WHITE);
			pixmap.drawLine(x1, y1, x1, y1);
			for (int y = y1; y < y2; ++y) {
				pixmap.setColor(new Color(0, 0, 0, (int) (255 - fractionalPart(interx) * 255)));
				pixmap.drawLine((int)interx, y, (int)interx, y);
				pixmap.setColor(new Color(0, 0, 0, (int) (fractionalPart(interx) * 255)));
				pixmap.drawLine((int)interx + 1, y, (int)interx + 1, y);
				interx += gradient;
			}
			pixmap.setColor(Color.BLACK);
			pixmap.drawLine(x2, y2, x2, y2);
		}
		return pixmap;
	}

	private float fractionalPart(float x) {
		int tmp = (int) x;
		return x - tmp; //вернёт дробную часть числа
	}

	private int sign (int x) {
		return (x > 0) ? 1 : (x < 0) ? -1 : 0;
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

	private Pixmap brezCircle(Pixmap pixmap, int x, int y, int r) {
		int sx=0;
		int sy=r;
		int d=3-2*r;
		while(sx<=sy) {
			pixmap.drawLine(x+sx, y-sy, x+sx, y-sy);
			pixmap.drawLine(x+sx, y+sy, x+sx, y+sy);
			pixmap.drawLine(x-sx, y-sy, x-sx, y-sy);
			pixmap.drawLine(x-sx, y+sy, x-sx, y+sy);
			pixmap.drawLine(x+sy, y+sx, x+sy, y+sx);
			pixmap.drawLine(x-sy, y+sx, x-sy, y+sx);
			pixmap.drawLine(x+sy, y-sx, x+sy, y-sx);
			pixmap.drawLine(x-sy, y-sx, x-sy, y-sx);

			if (d<0) {
				d = d + 4 * sx + 6;
			} else {
				d = d + 4 * (sx - sy) + 10;
				sy = sy - 1;
			}
			sx += 1;
		}

		return pixmap;
	}
	private Pixmap draw(Pixmap pixmap,int xc,int yc,int x,int y){
		pixmap.drawPixel(xc+x, yc+y);
		pixmap.drawPixel(xc+y, yc+x);
		pixmap.drawPixel(xc+x, yc-y);
		pixmap.drawPixel(xc+y, yc-x);
		pixmap.drawPixel(xc-x, yc-y);
		pixmap.drawPixel(xc-y, yc-x);
		pixmap.drawPixel(xc-y, yc+x);
		pixmap.drawPixel(xc-x, yc+y);
		return pixmap;
	}
	public Pixmap michCircle(Pixmap pixmap, int xc,int yc,int r)
	{
		int  x, y, d;
		x= 0;  y= r;  d= 3 - 2*r;
		while (x < y) {
			pixmap = draw(pixmap,xc,yc,x,y);
			if (d < 0) d= d + 4*x + 6; else {
				d= d + 4*(x-y) + 10;
				--y;
			}
			++x;
		}
		if (x == y) pixmap = draw(pixmap,xc,yc,x,y);

		return pixmap;
	}

	public void measureTime(){
		try {
			PrintWriter out = new PrintWriter(new FileWriter("out.txt"));
			long start = System.currentTimeMillis();
			for(int i = 0; i<CYCLES ; i++){
				pmap = brezLine(X0,X1,Y0,Y1,pmap);
			}
			long end = System.currentTimeMillis();
			out.println(start-end);
			start = System.currentTimeMillis();
			for(int i = 0; i<CYCLES ; i++){
				pmap = ddaLine(X0+50,X1+50,Y0,Y1,pmap);
			}
			end = System.currentTimeMillis();
			System.out.println(start-end);
			start = System.currentTimeMillis();
			for(int i = 0; i<CYCLES ; i++){
				pmap = wuLine(X0+100,X1+100,Y0,Y1,pmap);
			}
			end = System.currentTimeMillis();
			System.out.println(start-end);
			start = System.currentTimeMillis();
			for(int i = 0; i<CYCLES ; i++){
				pmap = brezCircle(pmap,X1+R+20,Y1+R+20,R);
			}
			end = System.currentTimeMillis();
			System.out.println(start-end);
			start = System.currentTimeMillis();
			for(int i = 0; i<CYCLES ; i++){
				pmap = michCircle(pmap,X1+3*R+40,Y1+R+20,R);
			}
			end = System.currentTimeMillis();
			System.out.println(start-end);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
