# Your First Scenario (JUnit)

This guide shows how to create and run your first **Scenario Weaver** test using JUnit.

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
    <artifactId>scenario-weaver-junit</artifactId>
    <version>{{ maven_version }}</version>
    <scope>test</scope>
</dependency>
```

---

## 2. Implement a Test Scenario

Create a class that implements `TestScenario`:

```java
public class HelloWorldTestScenario implements TestScenario<String, TestScenarioProperties, String, Void> {
    @Override
    public String description() {
        return "Hello World Test";
    }

    @Override
    public PayloadProvider<Void, String> payloadProvider() {
        return ctx -> "Hello, World!";
    }

    @Override
    public PropertiesProvider<TestScenarioProperties, Void> contextProvider() {
        return ctx -> null;
    }

    @Override
    public StubsProvider<String, TestScenarioProperties, Void> stubs() {
        return (payload, props) -> null;
    }

    @Override
    public List<AssertionsProvider<String, String, Void>> assertions() {
        return List.of((expected, actual, ctx) -> assertEquals(expected, actual));
    }

    @Override
    public ActionProvider<String, TestScenarioProperties, String> actionProvider() {
        return (payload, props) -> payload;
    }
}
```

---

## 3. Provide the Scenario

Implement a `TestScenarioProvider` that returns your scenario by name:

```java
public class HelloWorldTestScenarioProvider implements TestScenarioProvider {
    @Override
    public Optional<TestScenario<String, TestScenarioProperties, String, Void>> getTestScenario(String name) {
        if ("hello-world".equals(name)) {
            return Optional.of(new HelloWorldTestScenario());
        }
        return Optional.empty();
    }
}
```

---

## 4. Write the JUnit Test

Use the Scenario Weaver JUnit extension to inject and execute your scenario:

```java
@ExtendWith(TestScenarioWeaverExtension.class)
class HelloWorldTest {

    @TestScenarioSource
    private final HelloWorldTestScenarioProvider testScenarioProvider = new HelloWorldTestScenarioProvider();

    private final TestScenarioExecutor testScenarioExecutor = new DefaultTestScenarioExecutor();

    @Test
    void test_hello_world(
            @InjectTestScenario("hello-world")
            TestScenario<String, TestScenarioProperties, String, Void> testScenario) {
        testScenarioExecutor.execute(testScenario);
    }
}
```

--- 

## 5. Run the Test

Run your test as a standard JUnit test.  
Scenario Weaver will inject the scenario and execute it using your provider and executor.

---

## 6. Next Steps

- [Create your first TestScenario (Spring)](first-scenario-spring.md)
- [See more examples](examples.md)