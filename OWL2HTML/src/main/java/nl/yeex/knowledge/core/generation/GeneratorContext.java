package nl.yeex.knowledge.core.generation;

import java.io.File;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The parameters to specify the context of generation.
 * 
 */
public class GeneratorContext {
    private static final Logger LOG = LoggerFactory.getLogger(GeneratorContext.class);

    public static final String DEFAULT_OUTPUT_DIRECTORY = "output/";
    public static final String DEFAULT_INPUT_DIRECTORY = "input/";
    public static final String DEFAULT_THEMES_DIRECTORY = "themes/";
    public static final String DEFAULT_THEME = "plain";
    public static final Locale DEFAULT_LOCALE = Locale.getDefault();
    public static final boolean DEFAULT_ENABLE_INFERENCE = false;

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
        File[] themes = new File(themesDirectory).listFiles();
        for (File existingTheme: themes) {
            if (existingTheme.isDirectory() && existingTheme.getName().equalsIgnoreCase(theme)) {
                // security: do not trust given parameter!
                this.theme = existingTheme.getName();
                // force refresh of template directory.
                freemarkerHelper = null;
                return;
            }
        }
    }

    public String getTheme() {
        return theme;
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

}
