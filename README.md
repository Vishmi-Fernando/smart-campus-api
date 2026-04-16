# Smart Campus API

## API Design Overview

The Smart Campus API is designed following RESTful principles using JAX-RS (Jersey). The system is structured around resource-based architecture, where each resource (e.g., rooms) is represented by a unique URI.

The API uses HTTP methods to perform operations:

* GET → Retrieve data
* POST → Create new resources
* DELETE → Remove resources

All endpoints are versioned using the base path:
http://localhost:8080/api/v1

The system uses an in-memory data store (HashMap) to simulate persistent storage, as databases are not allowed in this coursework.

Each resource is designed to return JSON responses, ensuring compatibility with modern web clients.

Additionally, the API includes a discovery endpoint that provides metadata and navigational links to available resources, supporting RESTful best practices such as HATEOAS.

 



## How to Build and Run the Project

Follow these steps to set up and run the application:

1. Clone the repository from GitHub:
   git clone https://github.com/Vishmi-Fernando/smart-campus-api.git

2. Open the project in Apache NetBeans IDE.

3. Ensure Java JDK 17 is installed and configured.

4. Build the project:

   * Right-click project ->  Clean and Build

5. Run the application:

   * Right-click Main.java ->  Run File

6. The server will start at:
   http://localhost:8080/api/v1

7. Test the API using:

   * Web browser (for GET requests)
   * Postman or curl (for POST and DELETE requests)

Note:
If port 8080 is already in use, restart the IDE or change the port in Main.java.

## Sample cURL Commands

1. Discovery Endpoint:
    curl -X GET http://localhost:8080/api/v1

2. Get All Rooms:
    curl -X GET http://localhost:8080/api/v1/rooms

3. Create Room:
    curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"name\":\"Lab A\",\"capacity\":30}"

4. Get Room by ID:
    curl -X GET http://localhost:8080/api/v1/rooms/1

5. Delete Room
    curl -X DELETE http://localhost:8080/api/v1/rooms/1
    

At least five sample curl commands demonstrating successful interactions with dif
ferent parts of the API.


## Answers to Question on the CW Specification

### Answer for Part 1 Question1 – JAX-RS Resource Lifecycle

In JAX-RS, the default lifecycle of a resource class is **per-request**, meaning that a new instance of the resource class is created for each incoming HTTP request. This approach enhances thread safety because each request operates on its own independent object instance, preventing unintended interference between concurrent requests.

However, in this project, shared data structures such as `HashMap` are used to store application data in memory. Since these structures are shared across multiple requests, they introduce potential risks such as race conditions and data inconsistency in a multi-threaded environment.

To manage this effectively, developers must ensure safe access to shared data by:

* Using thread-safe collections (e.g., `ConcurrentHashMap`)
* Applying synchronization when modifying shared resources
* Minimizing shared mutable state where possible

This architectural design ensures both scalability and data integrity while handling multiple concurrent requests.



### Answer for Part 1 Question2 – HATEOAS (Hypermedia)

HATEOAS (Hypermedia as the Engine of Application State) is a key principle of RESTful API design, where responses include links to related resources. This allows clients to dynamically navigate the API without relying on predefined or hardcoded URLs.

In this implementation, the discovery endpoint (`GET /api/v1`) provides links to available resources such as `/api/v1/rooms` and `/api/v1/sensors`. This makes the API self-descriptive and easier to explore.

The advantages of HATEOAS include:

* Reducing dependency on static documentation
* Allowing APIs to evolve without breaking clients
* Providing dynamic navigation between resources
* Improving developer experience and usability

Compared to static documentation, HATEOAS enables clients to discover available actions at runtime, making the system more flexible and maintainable.



### Answer for Part 2 Question1 – IDs vs Full Objects

When designing API responses, there are two common approaches: returning only resource identifiers (IDs) or returning full resource objects.

Returning only IDs reduces the size of the response payload, which improves network efficiency and performance, especially when dealing with large datasets. However, this approach requires additional API calls to retrieve full resource details, increasing client-side complexity.

On the other hand, returning full objects provides complete information in a single response, making it easier for clients to process data without additional requests. This simplifies development but increases bandwidth usage and response size.

Therefore, the choice between these approaches depends on the trade-off between performance (minimizing network usage) and usability (reducing client-side complexity).



### Answer for Part 2 Question2 – DELETE Idempotency

The DELETE operation in REST is considered **idempotent**, meaning that making the same DELETE request multiple times results in the same final system state.

In this implementation:

* The first DELETE request successfully removes the room from the system.
* Any subsequent DELETE requests for the same room ID do not alter the system further, as the resource no longer exists.

Even though the response may differ (e.g., "Room not found"), the overall state of the system remains unchanged after the initial deletion. This satisfies the definition of idempotency.

Idempotency is important for reliability, as it allows clients to safely retry requests without causing unintended side effects or inconsistencies in the system.


### Answer for Part 3 Question 1 - Content-Type Handling in JAX-RS

In this implementation, the POST endpoint for creating a sensor explicitly uses the annotation:

@Consumes(MediaType.APPLICATION_JSON)

This indicates that the resource method only accepts request bodies formatted as JSON. When a client sends a request, JAX-RS inspects the `Content-Type` header to determine whether the incoming data matches the expected format.

If a client attempts to send data in a different format, such as `text/plain` or `application/xml`, JAX-RS will not be able to find a suitable message body reader to convert the request into a Java object. As a result, the request will be rejected automatically by the framework.

The typical response returned by JAX-RS in such cases is:

HTTP Status Code: 415 Unsupported Media Type

This behavior ensures strict enforcement of API contracts and prevents invalid or improperly formatted data from being processed by the application.

By restricting the accepted media type to JSON, the API achieves:
- Consistency in data exchange format
- Simplified parsing and validation logic
- Improved reliability and predictability for client applications

Therefore, the use of @Consumes(MediaType.APPLICATION_JSON) plays a crucial role in ensuring data integrity and enforcing correct communication between client and server.


### Answer for Part 3 Question 2 - Query Parameter vs Path Parameter for Filtering

In this implementation, sensor filtering is achieved using a query parameter:

GET /api/v1/sensors?type=CO2

This is implemented using the @QueryParam annotation, which allows optional filtering of a resource collection without altering the fundamental structure of the endpoint.

An alternative design could use a path parameter, such as:

/api/v1/sensors/type/CO2

However, the query parameter approach is generally considered superior for filtering and searching operations for several reasons:

1. Semantic Clarity  
   Query parameters represent optional criteria applied to a collection, whereas path parameters typically identify a specific resource. Filtering is not identifying a new resource but narrowing down an existing collection.

2. Flexibility  
   Query parameters allow multiple filters to be combined easily:
   /api/v1/sensors?type=CO2&status=active  
   This would be difficult and less readable using path parameters.

3. Scalability  
   As the number of filtering options increases, query parameters provide a clean and scalable approach without complicating the URL structure.

4. RESTful Best Practices  
   REST design principles recommend using query parameters for filtering, sorting, and searching operations on collections.

5. Client Usability  
   Query parameters are easier for clients to construct dynamically and integrate into applications, especially when dealing with multiple optional conditions.

Therefore, using @QueryParam for filtering aligns with RESTful design standards and provides a more flexible, scalable, and user-friendly approach compared to embedding filters within the URL path.



### Answer for Part 4 Question 1 - Sub-Resource Locator Pattern and Its Architectural Benefits

In this implementation, the Sub-Resource Locator pattern is used to handle nested resources through the following structure:

/api/v1/sensors/{id}/readings

Instead of defining all endpoints within a single resource class, the SensorResource delegates the responsibility of handling readings to a separate class, SensorReadingResource. This is achieved through a sub-resource locator method that returns an instance of SensorReadingResource for a specific sensor.

This architectural approach provides several important benefits:

1. Separation of Concerns  
   Each resource class is responsible for a specific domain. SensorResource handles sensor-related operations, while SensorReadingResource manages reading-related operations. This separation improves code clarity and maintainability.

2. Improved Modularity  
   By splitting functionality across multiple classes, the system becomes more modular. Each class can be developed, tested, and modified independently without affecting unrelated parts of the application.

3. Reduced Complexity  
   Without the sub-resource locator pattern, all nested paths such as `/sensors/{id}/readings/{rid}` would need to be handled within a single large controller class. This would lead to complex, hard-to-maintain code. Delegating logic prevents the creation of overly large and unmanageable classes.

4. Scalability  
   As the API grows, additional nested resources can be introduced easily by creating new resource classes. This makes the architecture scalable and adaptable to future requirements.

5. Better Readability and Maintainability  
   Smaller, focused classes are easier to read and debug. Developers can quickly locate relevant logic without navigating through a large monolithic controller.

Overall, the Sub-Resource Locator pattern enables a clean, maintainable, and scalable API design by distributing responsibilities across specialized classes rather than centralizing all logic in a single resource.