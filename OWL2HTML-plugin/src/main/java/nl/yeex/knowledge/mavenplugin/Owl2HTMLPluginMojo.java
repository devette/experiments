package nl.yeex.knowledge.mavenplugin;

import java.io.File;

import nl.yeex.knowledge.Main;
import nl.yeex.knowledge.offline.generators.GeneratorContext;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Start executing of the export as maven plugin.
 * 
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class Owl2HTMLPluginMojo extends AbstractMojo {

    @Parameter
    private String outputDirectory;
    @Parameter
    private String inputDirectory;
    @Parameter
    private String themesDirectory;
    @Parameter
    private String theme;
    @Parameter
    private String generation;
    @Parameter
    private String inferenceenabled;
    @Parameter
    private File owlSourceFile;

    public void execute() throws MojoExecutionException {
        getLog().info("plugin parameter: outputDirectory: " + outputDirectory);
        getLog().info("plugin parameter: inputDirectory: " + inputDirectory);
        getLog().info("plugin parameter: themesDirectory: " + themesDirectory);
        getLog().info("plugin parameter: theme: " + theme);
        getLog().info("plugin parameter: owlSourceFile: " + owlSourceFile);
        getLog().info("plugin parameter: generation: " + generation);
        getLog().info("plugin parameter: inferenceenabled: " + inferenceenabled);

        GeneratorContext configuration = new GeneratorContext();
        configuration.setOutputDirectory((outputDirectory != null) ? outputDirectory
                : GeneratorContext.DEFAULT_OUTPUT_DIRECTORY);
        configuration.setInputDirectory((outputDirectory != null) ? inputDirectory
                : GeneratorContext.DEFAULT_INPUT_DIRECTORY);
        configuration.setThemesDirectory((themesDirectory != null) ? themesDirectory
                : GeneratorContext.DEFAULT_THEMES_DIRECTORY);
        configuration.setTheme((theme != null) ? theme : GeneratorContext.DEFAULT_THEME);
        configuration.setOwlSourceFile(owlSourceFile);
        configuration.setGeneration(generation);
        configuration.setEnableInference("true".equalsIgnoreCase(inferenceenabled));
        Main.start(configuration);
    }
}