# Installation

## 1. Add Dependencies

Add the core package and one extension (Junit or Spring) to your `pom.xml`:

```xml
<dependency>
    <groupId>be.moesmedia</groupId>
    <artifactId>scenario-weaver-core</artifactId>
    <version>{{ maven_version }}</version>
    <scope>test</scope>
</dependency>
<!-- Choose one extension: -->
<dependency>
    <groupId>be.moesmedia</groupId>
    <artifactId>scenario-weaver-junit</artifactId>
    <version>{{ maven_version }}</version>
    <scope>test</scope>
</dependency>
<!-- or -->
<dependency>
    <groupId>be.moesmedia</groupId>
    <artifactId>scenario-weaver-spring</artifactId>
    <version>{{ maven_version }}</version>
    <scope>test</scope>
</dependency>
```

---

## 2. Using SNAPSHOT Versions (from GitHub Packages)

If you want the latest SNAPSHOT version, you need to add the GitHub Packages repository and authenticate Maven:

### a. Add the repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub be.moesmedia Packages</name>
        <url>https://maven.pkg.github.com/moes-media/scenario-weaver</url>
    </repository>
</repositories>
```

### b. Authenticate Maven to GitHub Packages

Add this to your `~/.m2/settings.xml`:

```xml
<servers>
    <server>
        <id>github</id>
        <username>YOUR_GITHUB_USERNAME</username>
        <password>YOUR_GITHUB_TOKEN</password>
    </server>
</servers>
```
- Use a [GitHub personal access token](https://github.com/settings/tokens) with `read:packages` scope.

### c. Use the SNAPSHOT version in your dependencies:

```xml
<dependency>
    <groupId>be.moesmedia</groupId>
    <artifactId>scenario-weaver-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
<!-- and one extension as above -->
```

---

## 3. Next Steps

- [Create your first TestScenario (jUnit)](first-scenario-junit.md)
- [Create your first TestScenario (Spring)](first-scenario-spring.md)
- [See more examples](examples.md)