package am.ik.blog;

import java.util.function.BiFunction;

import am.ik.blog.handler.CorsHandler;
import am.ik.blog.handler.IncrementCounterHandler;
import am.ik.blog.handler.ViewCounterHandler;
import am.ik.wws.Cache;
import am.ik.wws.Request;
import am.ik.wws.Response;
import am.ik.wws.Worker;

public class Main {
	public static void main(String[] args) {
		Worker.serve((request, cache) -> {
			BiFunction<Request, Cache, Response> handler;
			switch (request.method()) {
				case GET:
					handler = new ViewCounterHandler();
					break;
				case OPTIONS:
					handler = new CorsHandler();
					break;
				default:
					handler = new IncrementCounterHandler();
					break;
			}
			return handler.apply(request, cache);
		});
	}
}