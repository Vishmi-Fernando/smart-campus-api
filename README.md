Answer for Question 01:
In JAX-RS, the default lifecycle of a resource class is per-request. This means that a new instance of the resource class is created for each incoming HTTP request rather than using a singleton instance.

This design improves thread safety because each request is handled independently, avoiding shared state issues within the resource instance itself. However, when using in-memory data structures such as HashMap or ArrayList to store application data, these structures must be shared across requests (e.g., declared as static or managed by a shared service).

Since multiple requests can access these shared data structures concurrently, proper synchronization mechanisms (such as thread-safe collections or synchronization techniques) should be considered to prevent race conditions, inconsistent data, or data loss.


Answer for Question 02:
Hypermedia (HATEOAS - Hypermedia as the Engine of Application State) is considered a hallmark of advanced RESTful design because it allows clients to dynamically discover available actions and resources through links provided in API responses.

Instead of relying on static documentation, the API becomes self-descriptive by including navigation links within responses. This enables client applications to interact with the system more flexibly and adapt to changes without requiring updates to hardcoded endpoints.

For client developers, this approach simplifies integration, reduces dependency on external documentation, and improves maintainability, as the API itself guides how it should be used.