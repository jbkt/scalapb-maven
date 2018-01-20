package net.catte.scalapb.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mojo(name = "compile")
public class CompileMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "false")
    private boolean skip;

    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Skip flag set, skipping protobuf compilation.");
            return;
        }

        String baseDir = project.getBasedir().getPath();
        String buildDir = project.getBuild().getOutputDirectory();

        Path protoPath= Paths.get(baseDir, "src", "main", "protobuf");
        getLog().info("Reading proto files in '" + protoPath +"'.");
        Path scalaOut = Paths.get(buildDir, "generated-sources","protobuf");
        getLog().info("Writing Scala files in '" + scalaOut +"'.");
        project.addCompileSourceRoot(scalaOut.toString());

        try {
            Files.createDirectories(scalaOut);
            ProtoCompiler.compile(protoPath, scalaOut);
        } catch (Exception e) {
            throw new MojoExecutionException("Error compiling protobuf files", e);
        }
    }
}
