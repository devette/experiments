package nl.yeex.knowledge;

import java.util.Locale;

import nl.yeex.knowledge.offline.OWL2Exporter;
import nl.yeex.knowledge.offline.generators.GeneratorContext;
import nl.yeex.knowledge.online.OWL2KnowledgeServer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * This class provides the commandline interface for starting the template based
 * generation.
 */
public class Main {

    private static final boolean NOT_REQUIRED = false;
    private static final boolean REQUIRED = true;

    private static final String OPTION_THEME = "theme";
    private static final String OPTION_HELP = "help";
    private static final String OPTION_THEMESDIRECTORY = "themesdirectory";
    private static final String OPTION_INPUTDIRECTORY = "inputdirectory";
    private static final String OPTION_OUTPUTDIRECTORY = "outputdirectory";
    private static final String OPTION_SOURCEFILE = "sourcefile";
    private static final String OPTION_GENERATION = "generation";
    private static final String OPTION_INFERENCE_ENABLED = "inference";
    private static final String OPTION_LOCALE = "locale";

    /**
     * The interface entrypoint for the commandline.
     * 
     * @param argv
     *            an arrays of strings given on the commandline.
     */
    public static void main(String[] argv) {

        // create the command line parser
        CommandLineParser parser = new PosixParser();
        Options options = Main.getOptions();
        CommandLine line = null;
        try {
            // parse the command line arguments
            line = parser.parse(options, argv);
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
            printHelpAndExit(options);
        }

        if (line.hasOption(OPTION_HELP)) {
            printHelpAndExit(options);
        }
        GeneratorContext configuration = new GeneratorContext();
        Locale locale = new Locale(line.getOptionValue(OPTION_LOCALE, GeneratorContext.DEFAULT_LOCALE.toString()));
        System.out.println("sourcefile:" + line.getOptionValue(OPTION_SOURCEFILE));

        System.out.println("inputdirectory:"
                + line.getOptionValue(OPTION_INPUTDIRECTORY, GeneratorContext.DEFAULT_INPUT_DIRECTORY));
        System.out.println("themesdirectory:"
                + line.getOptionValue(OPTION_THEMESDIRECTORY, GeneratorContext.DEFAULT_THEMES_DIRECTORY));
        System.out.println("theme:" + line.getOptionValue(OPTION_THEME, GeneratorContext.DEFAULT_THEME));
        System.out.println("locale:" + locale + " , " + locale.getDisplayLanguage());

        configuration.setOwlSourceFileName(line.getOptionValue(OPTION_SOURCEFILE));
        configuration.setOutputDirectory(line.getOptionValue(OPTION_OUTPUTDIRECTORY,
                GeneratorContext.DEFAULT_OUTPUT_DIRECTORY));
        configuration.setInputDirectory(line.getOptionValue(OPTION_INPUTDIRECTORY,
                GeneratorContext.DEFAULT_INPUT_DIRECTORY));
        configuration.setThemesDirectory(line.getOptionValue(OPTION_THEMESDIRECTORY,
                GeneratorContext.DEFAULT_THEMES_DIRECTORY));
        configuration.setEnableInference("true".equalsIgnoreCase(line.getOptionValue(OPTION_INFERENCE_ENABLED, ""
                + GeneratorContext.DEFAULT_ENABLE_INFERENCE)));
        configuration.setTheme(line.getOptionValue(OPTION_THEME, GeneratorContext.DEFAULT_THEME));
        configuration.setGeneration(line.getOptionValue(OPTION_GENERATION, GeneratorContext.DEFAULT_GENERATION));
        configuration.setLocale(locale);
        start(configuration);
    }

    /**
     * Get the commandline options configuration.
     * 
     * @return options the commandline options.
     */
    private static Options getOptions() {
        // create the Options
        Options options = new Options();
        options.addOption(createRequiredOptionSourcefile());
        options.addOption(createOptionHelp());
        options.addOption(createOptionGeneration());
        options.addOption(createOptionOuputdirectory());
        options.addOption(createOptionInputdirectory());
        options.addOption(createOptionThemesdirectory());
        options.addOption(createOptionTheme());
        options.addOption(createOptionLocale());
        options.addOption(createOptionInferenceEnabled());
        return options;
    }

    private static Option createOptionInferenceEnabled() {
        OptionBuilder.withArgName(OPTION_INFERENCE_ENABLED);
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(NOT_REQUIRED);
        OptionBuilder.withDescription("If offline generation shoudl materialze inferences. \n"
                + "When true, html files will be generated with inferences.\n"
                + "When not true (i.e. 'false') no inferences will be generated.\n"
                + "The default for inference enabled is \"" + GeneratorContext.DEFAULT_GENERATION + "\"");
        Option inferenceEnabledOption = OptionBuilder.create(OPTION_INFERENCE_ENABLED);
        return inferenceEnabledOption;
    }

    private static Option createOptionGeneration() {
        OptionBuilder.withArgName(OPTION_GENERATION);
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(NOT_REQUIRED);
        OptionBuilder
                .withDescription("If generation is offline of online. \n"
                        + "When generation is offline, html files will be generated for all knowledge objects in the outputfolder.\n"
                        + "When generation is online, a webserver will be started where knowledge can be browsed.\n"
                        + "The default generation method is \"" + GeneratorContext.DEFAULT_GENERATION + "\"");
        Option generationOption = OptionBuilder.create(OPTION_GENERATION);
        return generationOption;
    }

    private static Option createOptionLocale() {
        OptionBuilder.withArgName(OPTION_LOCALE);
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(NOT_REQUIRED);
        OptionBuilder
                .withDescription("Specify the preferred locale to use for templates, and ontology labels and annotations. \n"
                        + "The default locale is \"" + GeneratorContext.DEFAULT_LOCALE + "\"");
        Option generationOption = OptionBuilder.create(OPTION_LOCALE);
        return generationOption;
    }

    private static Option createOptionTheme() {
        OptionBuilder.withArgName(OPTION_THEME);
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(NOT_REQUIRED);
        OptionBuilder.withDescription("The themedirectory (a subdirectory of the themesdirectory)."
                + "The default value of theme is \"" + GeneratorContext.DEFAULT_THEME + "\"");
        Option themeOption = OptionBuilder.create(OPTION_THEME);
        return themeOption;
    }

    private static Option createOptionThemesdirectory() {
        OptionBuilder.withArgName(OPTION_THEMESDIRECTORY);
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(NOT_REQUIRED);
        OptionBuilder.withDescription("The directory where the themes are located. The themesdirectory "
                + "has a default value of \"" + GeneratorContext.DEFAULT_THEMES_DIRECTORY + "\".");
        Option themesDirectoryOption = OptionBuilder.create(OPTION_THEMESDIRECTORY);
        return themesDirectoryOption;
    }

    private static Option createOptionInputdirectory() {
        OptionBuilder.withArgName(OPTION_INPUTDIRECTORY);
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(NOT_REQUIRED);
        OptionBuilder.withDescription("The directory where input ontology files are read from. The inputdirectory "
                + "has a default value of \"" + GeneratorContext.DEFAULT_INPUT_DIRECTORY + "\".");
        Option inputDirectoryOption = OptionBuilder.create(OPTION_INPUTDIRECTORY);
        return inputDirectoryOption;
    }

    private static Option createOptionOuputdirectory() {
        OptionBuilder.withArgName(OPTION_OUTPUTDIRECTORY);
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(NOT_REQUIRED);
        OptionBuilder.withDescription("The directory where the results must be written. The outputdirectory "
                + "has a default value of \"" + GeneratorContext.DEFAULT_OUTPUT_DIRECTORY + "\"."
                + "(Only for offline generation).");
        Option outputDirectoryOption = OptionBuilder.create(OPTION_OUTPUTDIRECTORY);
        return outputDirectoryOption;
    }

    private static Option createRequiredOptionSourcefile() {
        OptionBuilder.withArgName(OPTION_SOURCEFILE);
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(REQUIRED);
        OptionBuilder.withDescription("The owl file that should be used as input source");
        Option sourcefileOption = OptionBuilder.create(OPTION_SOURCEFILE);
        return sourcefileOption;
    }

    private static Option createOptionHelp() {
        OptionBuilder.isRequired(NOT_REQUIRED);
        OptionBuilder.withDescription("Displays the command help");
        Option helpOption = OptionBuilder.create(OPTION_HELP);
        return helpOption;
    }

    /**
     * Print the help descriptions of the possible options.
     * 
     * @param options
     *            the configured options.
     */
    public static void printHelpAndExit(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java " + Main.class.getName(), options);
        System.exit(0);
    }

    public static void start(GeneratorContext configuration) {
        if (configuration.getGeneration().equals(GeneratorContext.GENERATION_OFFLINE)) {
            System.out.println("offline knowledge generation.");
            System.out.println("outputdirectory:" + configuration.getOutputDirectory());
            OWL2Exporter exporter = new OWL2Exporter();
            exporter.start(configuration);

        } else if (configuration.getGeneration().equals(GeneratorContext.GENERATION_ONLINE)) {
            System.out.println("online knowledge generation, starting server...");
            OWL2KnowledgeServer knowledgeServer = new OWL2KnowledgeServer();
            knowledgeServer.start(configuration);
        }
    }
}
