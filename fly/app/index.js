const reply = (request) => {
    return new Response("Hello Wasm!");
}

// Subscribe to the Fetch event
addEventListener("fetch", event => {
    return event.respondWith(reply(event.request));
});