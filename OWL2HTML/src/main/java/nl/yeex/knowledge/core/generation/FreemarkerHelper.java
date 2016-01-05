package nl.yeex.knowledge.core.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * This class is is the primary class for finding and generating output from the
 * Freemarker templates.
 * 
 */
public class FreemarkerHelper {
    private static final Logger LOG = LoggerFactory.getLogger(FreemarkerHelper.class);

    private Configuration configuration;
    private String templateDirectory;
    private String outputDirectory;

    /**
     * Constructor that creates the freemarker helper with the given template
     * directory and given output directory.
     * 
     * @param templateDirectory
     * @param outputDirectory
     */
    public FreemarkerHelper(String templateDirectory, String outputDirectory) {
        this.templateDirectory = templateDirectory;
        this.outputDirectory = outputDirectory;
    }

    /**
     * Gets the current Freemarker configuration. If the configuration does not
     * exist it will be initialized.
     * 
     * @return configuration The current freemarker configuration.
     * @throws IOException
     *             thrown When the template directory doen not exist.
     */
    private Configuration getConfiguration() throws IOException {
        if (configuration == null) {
            configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File(templateDirectory));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
        }
        return configuration;
    }

    /**
     * Merges the template with name templateName with the given model.
     * 
     * @param templateName
     *            The name of the freemarker template.
     * @param templateModel
     *            The model that contains the merge data.
     */
    public void freemarkerTemplate(String templateName, Map<String, Object> templateModel) {
        try {
            writeSystemOut(getConfiguration(), templateName, templateModel);
        } catch (IOException e) {
            LOG.error("Error writing freemarker template", e);
        } catch (TemplateException e) {
            LOG.error("Error with freemarker template", e);
        }
    }

    /**
     * Merges the template with name templateName with the given model.
     * 
     * @param templateName
     *            The name of the freemarker template.
     * @param templateModel
     *            The model that contains the merge data.
     * @param outputFile
     *            the name of the file where the merged data should be written.
     */
    public void freemarkerTemplate(String templateName, Map<String, Object> templateModel, String outputFile) {
        try {
            writeFile(getConfiguration(), templateName, templateModel, outputFile);
        } catch (IOException e) {
            LOG.error("Error writing freemarker template", e);
        } catch (TemplateException e) {
            LOG.error("Error with freemarker template", e);
        }
    }

    /**
     * Merges the template with name templateName with the given model.
     * 
     * @param templateName
     *            The name of the freemarker template.
     * @param templateModel
     *            The model that contains the merge data.
     * @param outputFile
     *            the name of the file where the merged data should be written.
     */
    public void freemarkerTemplate(String templateName, Map<String, Object> templateModel, OutputStream out) {
        try {
            writeToOutputStream(getConfiguration(), templateName, templateModel, out);
        } catch (IOException e) {
            LOG.error("Error writing freemarker template", e);
        } catch (TemplateException e) {
            LOG.error("Error with freemarker template", e);
        }
    }

    /**
     * Check if the template exists.
     * 
     * @param templateName
     *            the name of the template.
     * @return true if the template exists in the configured directory,
     *         otherwise false.
     */
    public boolean templateExists(String templateName) {
        Template template;
        boolean exists = false;
        try {
            template = getConfiguration().getTemplate(templateName);
            if (template != null) {
                exists = true;
            }
        } catch (ParseException e) {
            LOG.error("Error parsing freemarker template: ", e);
        } catch (IOException e) {
            LOG.debug("Template not found: ", templateName, e);
            exists = false;
        }
        return exists;
    }

    /**
     * Write the output of merging a template with the template model to the
     * specified outputFile.
     * 
     * @param configuration
     *            The freemarker configuration
     * @param templateName
     *            The name of the template to merge the model with.
     * @param templateModel
     *            The model, containing the data to merge.
     * @param outputFile
     *            The name of the output file.
     * @throws IOException
     *             is thrown when writing to the outputfile fails.
     * @throws TemplateException
     *             is thrown when the template contains errors.
     * @throws FileNotFoundException
     *             is thrown when the outputFile does not exists
     */
    private void writeFile(Configuration configuration, String templateName, Map<String, Object> templateModel,
            String outputFile) throws IOException, TemplateException, FileNotFoundException {
        ensureOutputDirectory();
        OutputStream outputStream = new FileOutputStream(outputDirectory + outputFile);
        writeToOutputStream(configuration, templateName, templateModel, outputStream);
    }

    private void ensureOutputDirectory() {
        File outputDir = new File(outputDirectory);

        // if the directory does not exist, create it
        if (!outputDir.exists()) {
            LOG.info("Creating output directory: " + outputDirectory);
            boolean result = false;
            try {
                outputDir.mkdir();
                result = true;
            } catch (SecurityException securityException) {
                LOG.info("Failed to create output directory, check access rights.", securityException);
            }
            if (result) {
                LOG.info("Created output directory: " + outputDir.getAbsolutePath());
            }
        }
    }

    /**
     * Writes the results output of merging a template with the template model
     * to the specified outputstream.
     * 
     * @param templateModel
     * @param outputFile
     * @param template
     * 
     * @throws FileNotFoundException
     * @throws TemplateException
     * @throws IOException
     */
    private void writeToOutputStream(Configuration configuration, String templateName,
            Map<String, Object> templateModel, OutputStream outputStream) throws FileNotFoundException,
            TemplateException, IOException {
        Template template = configuration.getTemplate(templateName);
        Writer writer = new OutputStreamWriter(outputStream);
        template.process(templateModel, writer);
        writer.flush();
        writer.close();
    }

    /**
     * Writes the merged output to the console (System.out). Useful for
     * debugging.
     * 
     * @param configuration
     *            The freemarker configuration
     * @param templateName
     *            The name of the template to merge the model with.
     * @param templateModel
     *            The model, containing the data to merge.
     * @throws IOException
     *             is thrown when writing to the console fails.
     * @throws TemplateException
     *             is thrown when the template contains errors.
     * @throws FileNotFoundException
     *             ?
     */
    private void writeSystemOut(Configuration configuration, String templateName, Map<String, Object> templateModel)
            throws IOException, TemplateException, FileNotFoundException {
        Template template = configuration.getTemplate(templateName);
        Writer out = new OutputStreamWriter(System.out);
        template.process(templateModel, out);
        out.flush();
    }

}
