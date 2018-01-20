## Usage

Add the following snippet to your pom.

```xml
<plugin>
  <plugin>
    <groupId>net.catte</groupId>
    <artifactId>scalapb-maven-plugin</artifactId>
    <version>${project.version}</version>
    <executions>
      <execution>
        <goals>
          <goal>compile</goal>
        </goals>
        <phase>generate-sources</phase>
      </execution>
    </executions>
  </plugin>
</plugin>
```
