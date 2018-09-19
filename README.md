## Usage

Add the following snippet to your pom.

```xml
<plugins>
  <plugin>
    <groupId>net.catte</groupId>
    <artifactId>scalapb-maven-plugin</artifactId>
    <version>${scalapb-maven-plugin.version}</version>
    <executions>
      <execution>
        <goals>
          <goal>compile</goal>
        </goals>
        <phase>generate-sources</phase>
      </execution>
    </executions>
  </plugin>
</plugins>
```

### Configuration

| Maven property       | Description                                | Default                                                 |
| :------------------- | :----------------------------------------- | :------------------------------------------------------ |
| `skip`               | `true` to skip protobuf compilation        | `false`                                                 |
| `protocVersion`      | Protoc binary version                      | `v300`                                                  |
| `inputDirectory`     | Input directory containing `*.proto` files | `${project.basedir}/src/main/protobuf`                  |
| `includeDirectories` | Additional include directories (array)     | `[]`                                                    |
| `outputDirectory`    | Output directory for Scala files           | `${project.build.directory}/generated-sources/protobuf` |
| `flatPackage`        | `true` to flatten packages                 | `false`                                                 |
| `grpc`               | `true` to generate GRPC sources            | `false`                                                 |

For Java compatibility configuration, see the next section.

### Java compatibility

#### Configuration

| Maven property        | Description                                        | Default                                                 |
| :-------------------- | :------------------------------------------------- | :------------------------------------------------------ |
| `javaOutput`          | `true` to also generate Java classes               | `false`                                                 |
| `javaConversions`     | `true` to enable Java conversions in Scala classes | `false`                                                 |
| `javaOutputDirectory` | Output directory for Java files                    | `${project.build.directory}/generated-sources/protobuf` |

#### Usage

To generate Java classes along with the Scala ones, use the following
configuration.

```xml
<plugins>
  <plugin>
    <groupId>net.catte</groupId>
    <artifactId>scalapb-maven-plugin</artifactId>
    <version>${scalapb-maven-plugin.version}</version>
    <configuration>
      <javaOutput>true</javaOutput>
      <javaConversions>true</javaConversions>
    </configuration>
    <executions>
      <execution>
        <goals>
          <goal>compile</goal>
        </goals>
        <phase>generate-sources</phase>
      </execution>
    </executions>
  </plugin>
</plugins>
```
