# Your First Scenario (Spring)

This guide shows how to create and run your first **Scenario Weaver** test in a Spring Boot environment.

---

## 1. Add Dependencies

Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>be.moesmedia</groupId>
    <artifactId>scenario-weaver-core</artifactId>
    <version>{{ maven_version }}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>be.moesmedia</groupId>
    <artifactId>scenario-weaver-spring</artifactId>
    <version>{{ maven_version }}</version>
    <scope>test</scope>
</dependency>
```

---

## 2. Define the Test Scenario

Annotate your scenario class with `@ConfigureTestScenario` and provide the required beans by name and annotate your assertions:

```java
@ConfigureTestScenario(
    name = "helloSpring",
    stubsProvider = "myStubsProvider",
    contextProvider = "myPropertiesProvider",
    payloadProvider = "myPayloadProvider",
    actionProvider = "myActionProvider",
    description = "Hello Spring Test"
)
public class HelloSpringTestScenario {

    @Assertions
    public List<AssertionsProvider<ResponseEntity<String>, Void, Void>> assertions() {
        return List.of((actual, expected, unused) -> {
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals("Hello Spring!", actual.getBody());
        });
    }
}
```

---

## 3. Provide Scenario Beans

In your test configuration, define the required providers as Spring beans and Enable
scenario weaving by adding `@EnableTestScenarioWeaving`

```java
@TestConfiguration
@EnableTestScenarioWeaving(basePackages = "be.moesmedia.scenarioweaver.examples.hellospring")
static class MyTestConfig {
    @Bean
    public StubsProvider<Void, TestScenarioProperties, Void> myStubsProvider() {
        return (payload, properties) -> null;
    }

    @Bean
    public PropertiesProvider<TestScenarioProperties, Void> myPropertiesProvider() {
        return props -> null;
    }

    @Bean
    public PayloadProvider<Void, Void> myPayloadProvider() {
        return ctx -> null;
    }

    @Bean
    public ActionProvider<Void, Void, ResponseEntity<String>> myActionProvider(TestRestTemplate testRestTemplate) {
        return (payload, props) -> testRestTemplate.getForEntity("/hello-spring", String.class);
    }
}
```

---

## 4. Write the Spring Test

Use the Scenario Weaver Spring extension and inject your scenario:

```java
@SpringBootTest(
    classes = {HelloSpringApplication.class, HelloSpringTest.MyTestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringTestScenarioWeaverExtension.class)
class HelloSpringTest {

    private final TestScenarioExecutor executor = new DefaultTestScenarioExecutor();

    @Test
    void test_hello_spring(
            @InjectTestScenario("helloSpring")
            TestScenario<String, TestScenarioProperties, String, Void> testScenario) {
        executor.execute(testScenario);
    }
}
```

---

## 5. Run the Test

Run your test as a standard Spring Boot test.  
Scenario Weaver will inject the scenario and execute it using your Spring beans and executor.

---

## 6. Next Steps

- [See more examples](examples.md)