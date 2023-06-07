package am.ik.blog.handler;

import java.util.function.BiFunction;

import am.ik.wws.Cache;
import am.ik.wws.Request;
import am.ik.wws.Response;

public class CorsHandler implements BiFunction<Request, Cache, Response> {
	@Override
	public Response apply(Request request, Cache cache) {
		return Response.status(200)
				.header("X-Generated-By", "wasm-workers-server")
				.header("Access-Control-Allow-Headers", "authorization,x-b3-sampled,x-b3-spanid, x-b3-traceid")
				.header("Access-Control-Allow-Methods", "GET,POST,OPTIONS")
				.header("Access-Control-Max-Age", "3600")
				.header("Access-Control-Allow-Origin", "*")
				.data("")
				.build();
	}
}
