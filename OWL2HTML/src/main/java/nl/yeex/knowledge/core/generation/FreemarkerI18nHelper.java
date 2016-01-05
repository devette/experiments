package nl.yeex.knowledge.core.generation;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FreemarkerI18nHelper implements TemplateMethodModel {
    private static final Logger LOG = LoggerFactory.getLogger(FreemarkerI18nHelper.class);

    Locale locale = Locale.getDefault();

    public FreemarkerI18nHelper() {
        super();
    }

    public FreemarkerI18nHelper(@Nonnull Locale locale) {
        super();
        this.locale = locale;
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Expecting 1 argument: the 'message key'");
        }

        ResourceBundle bundle = null;
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Lookup resource for locale: %s", locale));
            }
            bundle = getBundle();
        } catch (MissingResourceException e) {
            LOG.error("Cannot find resourcebundle: ", e);
        }
        String key = (String) args.get(0);
        String translatedString = "";
        if (bundle != null) {
            try {
                translatedString = bundle.getString(key);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Translated: %s to: '%s' using locale: %s and bundle: %s", key,
                            translatedString, locale, getBundle().getLocale()));
                }
            } catch (MissingResourceException e) {
                LOG.warn(String.format("Cannot find resource with key: %s", key), e);
                translatedString = "@" + key + "@";
            }
        }
        return translatedString;
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("i18n.messages", getLocale());
    }

    public String format(String message, Object... args) {
        MessageFormat format = new MessageFormat(message, getLocale());
        return format.format(args);
    }

    public Locale getLocale() {
        return locale;
    }

}
