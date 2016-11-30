package nl.yeex.knowledge.offline.generators;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import nl.yeex.knowledge.core.generation.FreemarkerHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The parameters to specify the context of generation.
 * 
 */
public final class GeneratorContext {
    private static final Logger LOG = LoggerFactory.getLogger(GeneratorContext.class);

    public static final String GENERATION_ONLINE = "online";
    public static final String GENERATION_OFFLINE = "offline";
    public static final String DEFAULT_OUTPUT_DIRECTORY = "output/";
    public static final String DEFAULT_INPUT_DIRECTORY = "input/";
    public static final String DEFAULT_THEMES_DIRECTORY = "themes/";
    public static final String DEFAULT_THEME = "plain";
    public static final Locale DEFAULT_LOCALE = Locale.getDefault();
    public static final boolean DEFAULT_ENABLE_INFERENCE = false;
    public static final String DEFAULT_GENERATION = GENERATION_ONLINE;

    private static final String TEMPLATES = "/templates";
    private static final String STATIC = "/static";
    private String owlSourceFileName;
    private String inputDirectory = DEFAULT_INPUT_DIRECTORY;
    private String outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
    private String themesDirectory = DEFAULT_THEMES_DIRECTORY;
    private String theme = DEFAULT_THEME;
    private Locale locale = DEFAULT_LOCALE;
    private boolean enableInference = DEFAULT_ENABLE_INFERENCE;

    private File owlSourceFile;
    private FreemarkerHelper freemarkerHelper;
    private List<Generator> generators;
    private String generation = DEFAULT_GENERATION;

    public void setOwlSourceFileName(String owlSourceFileName) {
        this.owlSourceFileName = owlSourceFileName;
    }

    public String getOwlSourceFileName() {
        return owlSourceFileName;
    }

    public void setOwlSourceFile(File owlSourceFile) {
        this.owlSourceFile = owlSourceFile;
    }

    public File getOwlSourceFile() {
        if (this.owlSourceFile == null) {
            this.owlSourceFile = new File(getOwlSourceFileName());
        }
        return owlSourceFile;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    public void setThemesDirectory(String themesDirectory) {
        this.themesDirectory = themesDirectory;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOutputDirectory() {
        return this.outputDirectory;
    }

    public String getInputDirectory() {
        return this.inputDirectory;
    }

    public String getTemplatesDirectory() {
        return getThemesDirectory() + this.theme + TEMPLATES;
    }

    public String getStaticDirectory() {
        return getThemesDirectory() + this.theme + STATIC;
    }

    public String getThemesDirectory() {
        return this.themesDirectory;
    }

    public FreemarkerHelper getTemplateEngine() {
        if (freemarkerHelper == null) {
            freemarkerHelper = new FreemarkerHelper(getTemplatesDirectory(), getOutputDirectory());
        }
        return freemarkerHelper;
    }

    public boolean templateExists(String templateName) {
        return getTemplateEngine().templateExists(templateName);
    }

    public void log(String logline) {
        LOG.info(logline);
    }

    public List<Generator> getGenerators() {
        if (generators == null) {
            generators = new ArrayList<Generator>();

            // TODO: move to setting the configuration
            generators.add(new StaticFilesGenerator()); // copy all static files
            generators.add(new OntologyGenerator()); // Generate an index.html file
            generators.add(new ClassGenerator()); // Generate an output file
                                                    // for each class
            generators.add(new IndividualGenerator()); // Generate an output
                                                        // file for each object
                                                        // property in the
                                                        // ontology
            generators.add(new ObjectPropertyGenerator()); // Generate an
                                                             // outputfile for
                                                             // each individual
                                                             // in the ontology
            generators.add(new DataPropertyGenerator()); // Generate an
                                                           // outputfile for
                                                           // each dataproperty
                                                           // in the ontology
        }
        return generators;
    }

    public void addGenerator(Generator generator) {
        getGenerators().add(generator);
    }

    public void generate(String templateFile, Map<String, Object> model, String outputFile) {
        getTemplateEngine().freemarkerTemplate(templateFile, model, outputFile);
    }

    public void generate(String templateFile, Map<String, Object> model, OutputStream outputStream) {
        getTemplateEngine().freemarkerTemplate(templateFile, model, outputStream);
    }

    public boolean isEnableInference() {
        return enableInference;
    }

    public void setEnableInference(boolean enableInference) {
        this.enableInference = enableInference;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setGeneration(String generation) {
        if (GENERATION_OFFLINE.equalsIgnoreCase(generation)) {
            this.generation = GENERATION_OFFLINE;
        } else if (GENERATION_ONLINE.equalsIgnoreCase(generation)) {
            this.generation = GENERATION_ONLINE;
        }
    }

    public String getGeneration() {
        return generation;
    }
}
