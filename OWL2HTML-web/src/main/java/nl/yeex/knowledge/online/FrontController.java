package nl.yeex.knowledge.online;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.yeex.knowledge.core.adapters.ClassVO;
import nl.yeex.knowledge.core.adapters.DataPropertyVO;
import nl.yeex.knowledge.core.adapters.IndividualVO;
import nl.yeex.knowledge.core.adapters.ObjectPropertyVO;
import nl.yeex.knowledge.core.generation.FreemarkerI18nHelper;
import nl.yeex.knowledge.core.generation.GeneratorContext;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/select")
public class FrontController extends HttpServlet {

    // freemarker models.
    private static final String MODEL_CONTEXT = "context";
    private static final String MODEL_SESSION = "session";
    private static final String MODEL_INDIVIDUAL = "individual";
    private static final String MODEL_OBJECTPROPERTY = "objectProperty";
    private static final String MODEL_DATAPROPERTY = "dataProperty";
    private static final String MODEL_CLAZZ = "clazz";
    private static final String MODEL_ONTOLOGY = "ontology";
    private static final String MODEL_MESSAGES = "messages";

    private static final String PARAMETER_CLASS = "class";
    private static final String PARAMETER_INDIVIDUAL = "individual";
    private static final String PARAMETER_DATAPROPERTY = "dataproperty";
    private static final String PARAMETER_OBJECTPROPERTY = "objectproperty";
    private static final String PARAMETER_ONTOLOGY = "ontology";
    private static final String PARAMETER_GRAPHDEPTH = "graphdepth";
    private static final String PARAMETER_INFERRED = "inferred";

    private static final Logger LOG = LoggerFactory.getLogger(FrontController.class);

    private static final String ATTRIBUTE_SESSIONDATA = "SESSIONDATA";
    private static final String documentRoot;

    private static final long serialVersionUID = 1L;


    static   {
        documentRoot = System.getProperty("DOCUMENT_ROOT");
        if (documentRoot == null)  {
            String errorMessage = "Enviroment variable 'DOCUMENT_ROOT' must be set";
            LOG.error(errorMessage);
            throw new ExceptionInInitializerError(errorMessage);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logRequestHeaders(request);
        SessionData sessionData = getSessionData(request);
        updateGraphDepthInSession(request, sessionData);

        if (request.getParameter(PARAMETER_CLASS) != null) {
            generateClassResponse(request, response);
        } else if (request.getParameter(PARAMETER_INDIVIDUAL) != null) {
            generateIndividualResponse(request, response);
        } else if (request.getParameter(PARAMETER_OBJECTPROPERTY) != null) {
            generateObjectPropertyResponse(request, response);
        } else if (request.getParameter(PARAMETER_DATAPROPERTY) != null) {
            generateDataPropertyResponse(request, response);
        } else if (request.getParameter(PARAMETER_ONTOLOGY) != null) {
            // TODO: security!!!!
            sessionData.setCurrentOntology(sessionData.getLocalBasePath() + "/owl/"
                    + request.getParameter(PARAMETER_ONTOLOGY));
            generateOntology(request, response);
        } else if (request.getParameter(PARAMETER_INFERRED) != null) {
            boolean enableInference = "true".equalsIgnoreCase(request.getParameter(PARAMETER_INFERRED));
            sessionData.setUseInferredOntology(enableInference);
            generateOntology(request, response);
        } else {
            generateOntology(request, response);
        }

    }

    private void updateGraphDepthInSession(HttpServletRequest request, SessionData sessionData) {
        if (request.getParameter(PARAMETER_GRAPHDEPTH) != null) {
            Integer graphDepth = 3;
            try {
                graphDepth = Integer.valueOf(request.getParameter(PARAMETER_GRAPHDEPTH));
            } catch (NumberFormatException ne) {
                LOG.warn("not a number: " + request.getParameter(PARAMETER_GRAPHDEPTH));
            }
            sessionData.setGraphDepth(graphDepth);
        }
    }

    private void logRequestHeaders(HttpServletRequest request) {
        LOG.info(String.format("Content Negotiation - Request Accept Header: %s", request.getHeader("accept")));
        LOG.info(String.format("Preferred Requested Language: %s", request.getLocale()));
        Enumeration<Locale> locales = request.getLocales();
        while (locales.hasMoreElements()) {
            LOG.info(String.format("Alternative Language: %s", locales.nextElement()));
        }
    }

    private SessionData getSessionData(HttpServletRequest request) {
        SessionData sessionData = (SessionData) request.getSession().getAttribute(ATTRIBUTE_SESSIONDATA);
        LOG.debug("Session Data: " + sessionData);
        if (sessionData == null) {
            LOG.info("Session Data is null, creating new sessiondata.");
            sessionData = new SessionData();
            sessionData.setLocalBasePath(documentRoot);
            sessionData.setCurrentOntology(documentRoot + "/owl/calim.owl");
            request.getSession().setAttribute(ATTRIBUTE_SESSIONDATA, sessionData);
        }
        sessionData.getContext().setLocale(request.getLocale());
        return sessionData;
    }

    private void generateIndividualResponse(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response)
            throws IOException {
        SessionData sessionData = getSessionData(request);
        OWLOntology ontology = sessionData.getOntology();
        GeneratorContext context = sessionData.getContext();

        OWLIndividual owlIndividual = sessionData.getIndividualByIRI(request.getParameter(PARAMETER_INDIVIDUAL));
        IndividualVO individualVO = new IndividualVO(ontology, owlIndividual);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_INDIVIDUAL, individualVO);
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__individual.ftl"; // default;

        for (ClassVO individualClassVO : individualVO.getClasses()) {

            // it might be really useful to have
            // "class specific layout templates",
            // so if there is a template with the name of the class, it will be
            // used
            // instead of the default.
            String classSpecificTemplate = "individual_" + individualClassVO.getFilename() + ".ftl";
            if (context.templateExists(classSpecificTemplate)) {
                templateFile = classSpecificTemplate;
            }
        }

        // GENERATE
        context.generate(templateFile, model, response.getOutputStream());
    }

    private void generateOntology(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response)
            throws IOException {
        SessionData sessionData = getSessionData(request);
        GeneratorContext context = sessionData.getContext();

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // GENERATE
        context.generate("__ontology.ftl", model, response.getOutputStream());

    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    private void generateClassResponse(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response)
            throws IOException {
        SessionData sessionData = getSessionData(request);
        OWLOntology ontology = sessionData.getOntology();
        GeneratorContext context = sessionData.getContext();

        OWLClass classByName = sessionData.getClassByIri(request.getParameter(PARAMETER_CLASS));
        ClassVO classVO = new ClassVO(ontology, classByName);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_CLAZZ, classVO);
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        LOG.debug("Class: " + classVO);

        // DETERMINE TEMPLATE:
        String templateFile = "__class.ftl"; // default;

        String specificTemplate = "class_" + classVO.getFilename() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE
        context.generate(templateFile, model, response.getOutputStream());
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    private void generateObjectPropertyResponse(@Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response) throws IOException {
        SessionData sessionData = getSessionData(request);
        OWLOntology ontology = sessionData.getOntology();
        GeneratorContext context = sessionData.getContext();

        OWLObjectProperty objectPropertyByName = sessionData.getObjectPropertyByIri(request
                .getParameter(PARAMETER_OBJECTPROPERTY));
        ObjectPropertyVO objectPropertyVO = new ObjectPropertyVO(ontology, objectPropertyByName);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_OBJECTPROPERTY, objectPropertyVO);
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__objectproperty.ftl"; // default;

        String specificTemplate = "objectproperty_" + objectPropertyVO.getPropertyLabel() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE
        context.generate(templateFile, model, response.getOutputStream());
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    private void generateDataPropertyResponse(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response)
            throws IOException {
        SessionData sessionData = getSessionData(request);
        OWLOntology ontology = sessionData.getOntology();
        GeneratorContext context = sessionData.getContext();

        OWLDataProperty dataPropertyByName = sessionData.getDataPropertyByIri(request
                .getParameter(PARAMETER_DATAPROPERTY));
        DataPropertyVO dataPropertyVO = new DataPropertyVO(ontology, dataPropertyByName);

        // SETUP TEMPLATE MODEL
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(MODEL_DATAPROPERTY, dataPropertyVO);
        model.put(MODEL_ONTOLOGY, sessionData.getOntologyVO());
        model.put(MODEL_SESSION, sessionData);
        model.put(MODEL_CONTEXT, sessionData.getContext());
        model.put(MODEL_MESSAGES, new FreemarkerI18nHelper(context.getLocale()));

        // DETERMINE TEMPLATE:
        String templateFile = "__dataproperty.ftl"; // default;

        String specificTemplate = "dataproperty_" + dataPropertyVO.getPropertyLabel() + ".ftl";
        if (context.templateExists(specificTemplate)) {
            templateFile = specificTemplate;
        }

        // GENERATE
        context.generate(templateFile, model, response.getOutputStream());
    }

}