# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.example.sprignboot-3-todo-app' is invalid and this project uses 'com.example.APS_simulation_tool' instead.

# Getting Started this is a test of the new item creationt
@Autowired
private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var todoItemService: TodoItemService // Mock the service used in your controller

    @Test
    fun `should create a new todo item and redirect`() {
        // Given
        val description = "Test Todo Item"
        val isComplete = "true"

        // When & Then
        mockMvc.perform(post("/todo")
                .param("description", description)
                .param("isComplete", isComplete)
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(redirectedUrl("/"))

        // Verify that the service's save method was called once
        verify(todoItemService, times(1)).save(any(TodoItem::class.java))
    }
### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.8/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.8/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.1.8/reference/htmlsingle/index.html#using.devtools)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.1.8/reference/htmlsingle/index.html#web.servlet.spring-mvc.template-engines)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.8/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.8/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/3.1.8/reference/htmlsingle/index.html#io.validation)

### Guides
The following guides illustrate how to use some features concretely:

* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)

