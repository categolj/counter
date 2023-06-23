package am.ik.blog;

import am.ik.blog.handler.MetricsHandler;
import am.ik.wws.Worker;

public class Main {
	public static void main(String[] args) {
		Worker.serve(new MetricsHandler());
	}
}