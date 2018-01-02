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