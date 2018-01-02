package com.cg.tlab;

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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import java.awt.Point;
import java.util.ArrayList;

public class tlab extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	Pixmap pixmap,pmap;
	Texture texture;
	Sprite sprite;
	ArrayList<Point> arrl,arrAppr;
	float w;
	float h;
	int col;
	Point left;
	Point right;
	Point top;
	Point bottom;
	boolean flag = false;
	private Queue<Point> queue = new LinkedList<Point>();
	private int pickedColorInt = Color.rgba8888(Color.YELLOW);
	List<Point> mahList;
	ArrayList<List> intersections;
	ListIterator<List> intrLITR;
	ListIterator<Point> vertLITR;
	@Override
	public void create () {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		arrl = new ArrayList<Point>();
		arrAppr = new ArrayList<Point>();
		batch = new SpriteBatch();
		pixmap = new Pixmap(800,600,Pixmap.Format.RGBA8888);
		pmap = new Pixmap(800,600,Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		texture = new Texture(flipPixmap(pixmap));
		sprite = new Sprite(texture);
		sprite.setPosition(w/2 -sprite.getWidth()/2, h/2 - sprite.getHeight()/2);
		intersections = new ArrayList<List>();
		mahList = new Vector<Point>();
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
		//img.dispose();
	}

	public boolean keyDown (int keycode) {

		if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
			floodFill(pixmap,Gdx.input.getX() - (int)sprite.getWidth()/2 + Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() - Gdx.input.getY() - (int)sprite.getHeight()/2 + Gdx.graphics.getHeight()/2);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
			System.out.println("doing shit");
			verticesFill();
			pixmap.drawPixmap(flipPixmap(pmap),0,0);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
			System.out.println("doing shit");
			xorFill();
			//pixmap.drawPixmap(flipPixmap(pmap),0,0);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DEL)){
			arrl.clear();
			pixmap.dispose();
			texture.dispose();
			pixmap = new Pixmap(800,600,Pixmap.Format.RGBA8888);
			texture = new Texture(flipPixmap(pixmap));
			sprite = new Sprite(texture);
			pixmap.setColor(Color.WHITE);
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
			System.out.println("X " + Gdx.input.getX() + " Y " + Gdx.input.getY());
			arrl.add(new Point(Gdx.input.getX() - (int) sprite.getWidth() / 2 + Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - (int) sprite.getHeight() / 2 + Gdx.graphics.getHeight() / 2));
			System.out.println(arrl.size());
		}
		if(button == Input.Buttons.RIGHT){
			try {
				pixmap.setColor(Color.rgba8888(1,1,1,1));
				for (int i = 0; i < arrl.size()-1; i++){
					Point curr = arrl.get(i);
					Point next = arrl.get(i+1);
					pixmap.drawLine((int)curr.getX(),(int)curr.getY(),(int)next.getX(),(int)next.getY());
				}
				Point first = arrl.get(0);
				System.out.println("f "+first);
				Point last = arrl.get(arrl.size()-1);
				System.out.println("l "+last);
				pixmap.drawLine((int)last.getX(),(int)last.getY(),(int)first.getX(),(int)first.getY());
				texture = new Texture(flipPixmap(pixmap));
				sprite = new Sprite(texture);
				if(mahList != null)
					mahList.clear();
				for(int i = 0; i <= h; i++){
					int colorWhite = Color.rgba8888(Color.WHITE);
					for(int j = 0; j <= w ; j++){
						if (pixmap.getPixel(j,i) == colorWhite)
						{mahList.add(new Point(j,i));
							System.out.println("found");}
					}
					intersections.add(mahList);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean touchUp (int x, int y, int pointer, int button) {
//		if(button == Input.Buttons.LEFT){
//			arrl.add(findAverage(arrAppr));
//			System.out.println(arrl.size());
//		}
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

//	public boolean touchUp(int screenX,int screenY, int pointer){
//		if()
//	}

	private void floodFill(Pixmap pixmap, int x, int y){
		//set to true for fields that have been checked
		boolean[][] painted  = new boolean[pixmap.getWidth()][pixmap.getHeight()];

		//skip black pixels when coloring
		int whiteColor = Color.rgba8888(Color.WHITE);

		queue.clear();
		queue.add(new Point(x, y));

		while(!queue.isEmpty()){
			Point temp = queue.remove();
			int temp_x = (int) temp.getX();
			int temp_y = (int) temp.getY();


			if(temp_x >= 0 && temp_x < pixmap.getWidth() && temp_y >= 0 && temp_y < pixmap.getHeight()) {

				int pixel = pixmap.getPixel(temp_x, temp_y);
				if (!painted[temp_x][temp_y] && pixel != whiteColor) {
					painted[temp_x][temp_y] = true;
					pixmap.drawPixel(temp_x, temp_y, pickedColorInt);

					queue.add(new Point(temp_x + 1, temp_y));
					queue.add(new Point(temp_x - 1, temp_y));
					queue.add(new Point(temp_x, temp_y + 1));
					queue.add(new Point(temp_x, temp_y - 1));

				}
			}
		}
	}

	public void verticesFill(){
		System.out.println("ya hitla");
		intrLITR = intersections.listIterator();
		System.out.println("KEK "+intersections);
		while(intrLITR.hasNext()){
			System.out.println("me hitla");
			mahList = intrLITR.next();
			vertLITR = mahList.listIterator();
				while(vertLITR.hasNext()){
					System.out.println("me hitla");
					Point curr = vertLITR.next();
					Point next = curr;
					if(vertLITR.hasNext())
						next = vertLITR.next();
					pmap.setColor(Color.YELLOW);
					pmap.drawLine((int)curr.getX() - (int)sprite.getWidth()/2 + Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() - (int)curr.getY() - (int)sprite.getHeight()/2 + Gdx.graphics.getHeight()/2,(int)next.getX() - (int)sprite.getWidth()/2 + Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() - (int)next.getY() - (int)sprite.getHeight()/2 + Gdx.graphics.getHeight()/2);
					System.out.println("SX SY DX DY"+((int)curr.getX() - (int)sprite.getWidth()/2 + Gdx.graphics.getWidth()/2)+" "+(Gdx.graphics.getHeight() - (int)curr.getY() - (int)sprite.getHeight()/2 + Gdx.graphics.getHeight()/2)+" "+((int)next.getX() - (int)sprite.getWidth()/2 + Gdx.graphics.getWidth()/2)+" "+(Gdx.graphics.getHeight() - (int)next.getY() - (int)sprite.getHeight()/2 + Gdx.graphics.getHeight()/2));
					System.out.println("H "+Gdx.graphics.getHeight()+" Y "+(int)curr.getY()+" S/2 "+sprite.getHeight()/2+" H/2 "+Gdx.graphics.getHeight()/2);
				}
		}
    }

    public void xorFill(){
		ArrayList<Integer> count = new ArrayList<Integer>();
		int white = Color.rgba8888(Color.WHITE);
		for(int i = 0;i<= h;i++){
			flag = false;
			for(int j = 0;j <= w;j++){
				if(pixmap.getPixel(j,i) == white && pixmap.getPixel(j+1,i) != white) {
					flag = !flag;
					count.add(j);
				}
				if (flag)
					pixmap.drawPixel(j,i,Color.rgba8888(Color.YELLOW));
			}
			if(count.size() == 1){
				for(int x = count.get(0)+1; x <= w; x++)
					pixmap.drawPixel(x,i,Color.rgba8888(Color.BLACK));
			count.clear();
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
	public Pixmap rotatePixmap (Pixmap src, float angle){
		final int width = src.getWidth();
		final int height = src.getHeight();
		Pixmap rotated = new Pixmap(width, height, src.getFormat());

		final double radians = Math.toRadians(angle), cos = Math.cos(radians), sin = Math.sin(radians);


		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int
						centerx = width/2, centery = height / 2,
						m = x - centerx,
						n = y - centery,
						j = ((int) (m * cos + n * sin)) + centerx,
						k = ((int) (n * cos - m * sin)) + centery;
				if (j >= 0 && j < width && k >= 0 && k < height){
					rotated.drawPixel(x, y, src.getPixel(k, j));
				}
			}
		}
		return rotated;

	}
	public Point findAverage(ArrayList<Point> array){
		ListIterator itr = array.listIterator();
		int sumX = 0;
		int sumY = 0;
		while(itr.hasNext()){
			Point curr = (Point)itr.next();
			sumX += (int)curr.getX();
			sumY += (int)curr.getX();
		}
		return new Point((int)sumX/array.size(),(int)sumY/array.size());
	}
}
