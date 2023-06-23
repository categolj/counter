package am.ik.blog.handler;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import am.ik.json.Json;
import am.ik.json.JsonNode;
import am.ik.json.JsonObject;
import am.ik.wws.Cache;
import am.ik.wws.Request;
import am.ik.wws.Response;

public class MetricsHandler implements BiFunction<Request, Cache, Response> {
	@Override
	public Response apply(Request request, Cache cache) {
		final List<JsonObject> counters = cache.asMap().values().stream()
				.map(Json::parse)
				.map(JsonNode::asObject)
				.collect(Collectors.toList());
		final StringBuilder body = new StringBuilder()
				.append("# HELP request_total  \n")
				.append("# TYPE request_total counter\n");
		for (JsonObject counter : counters) {
			final Integer count = counter.get("counter").asInteger();
			final Integer entryId = counter.get("entryId").asInteger();
			body.append("request_total{id=\"").append(entryId).append("\"} ").append((double) count).append("\n");
		}
		return Response.status(200)
				.header("X-Generated-By", "wasm-workers-server")
				.header("Content-Type", "text/plain;version=0.0.4;charset=utf-8")
				.header("X-Generated-By", "wasm-workers-server")
				.data(body.toString())
				.build();
	}
}
