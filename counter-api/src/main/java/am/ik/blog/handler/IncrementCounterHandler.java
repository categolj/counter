package am.ik.blog.handler;

import java.time.OffsetDateTime;
import java.util.function.BiFunction;

import am.ik.json.Json;
import am.ik.json.JsonArray;
import am.ik.json.JsonNode;
import am.ik.json.JsonObject;
import am.ik.wws.Cache;
import am.ik.wws.Request;
import am.ik.wws.Response;

public class IncrementCounterHandler implements BiFunction<Request, Cache, Response> {

	private static final String ENTRY_ID = "entryId";

	private static final String COUNTER = "counter";

	private static final String FROM = "from";

	@Override
	public Response apply(Request request, Cache cache) {
		final JsonNode body = Json.parse(request.body());
		if (body.isObject()) {
			return this.increment(body.asObject(), request, cache);
		}
		else if (body.isArray()) {
			return this.restore(body.asArray(), request, cache);
		}
		else {
			return Response.status(400).data("Unsupported body").build();
		}
	}

	Response increment(JsonObject body, Request request, Cache cache) {
		final String entryId = String.valueOf(body.get(ENTRY_ID).asInteger());
		final JsonObject counter = cache.get(entryId)
				.map(Json::parse)
				.map(JsonNode::asObject)
				.orElseGet(() -> initCounter(entryId));
		final String userAgent = request.header("user-agent").orElse("-");
		final String referer = request.header("referer").orElse("-");
		if (!(userAgent.startsWith("curl") ||
			  userAgent.startsWith("undici") ||
			  userAgent.startsWith("synthetic") ||
			  userAgent.contains("bot") ||
			  userAgent.contains("Bot") ||
			  referer.startsWith("http://127.0.0.1") ||
			  referer.startsWith("http://localhost"))) {
			counter.put(COUNTER, counter.get(COUNTER).asInteger() + 1);
		}
		cache.put(entryId, Json.stringify(counter));
		return Response.status(200)
				.header("X-Generated-By", "wasm-workers-server")
				.header("Content-Type", "application/json")
				.header("X-Generated-By", "wasm-workers-server")
				.header("Access-Control-Allow-Headers", "authorization,x-b3-sampled,x-b3-spanid, x-b3-traceid")
				.header("Access-Control-Allow-Methods", "GET,POST,OPTIONS")
				.header("Access-Control-Max-Age", "3600")
				.header("Access-Control-Allow-Origin", "*")
				.data(Json.stringify(counter))
				.build();
	}

	Response restore(JsonArray array, Request request, Cache cache) {
		array.values().forEach(node -> {
			final JsonObject counter = node.asObject();
			final String entryId = String.valueOf(counter.get(ENTRY_ID).asInteger());
			cache.put(entryId, Json.stringify(counter));
		});
		return new ViewCounterHandler().apply(request, cache);
	}

	JsonObject initCounter(String id) {
		return new JsonObject()
				.put(ENTRY_ID, Integer.parseInt(id))
				.put(COUNTER, 0)
				.put(FROM, OffsetDateTime.now().toString());
	}
}
