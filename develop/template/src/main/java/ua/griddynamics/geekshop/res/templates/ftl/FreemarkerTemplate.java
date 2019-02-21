package ua.griddynamics.geekshop.res.templates.ftl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ua.griddynamics.geekshop.res.templates.TemplateEngine;
import ua.griddynamics.geekshop.res.templates.exception.TemplateEngineException;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-20
 */
public class FreemarkerTemplate implements TemplateEngine {
    private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);

    public FreemarkerTemplate(String templatePath) {
        cfg.setClassForTemplateLoading(getClass(), templatePath);
        cfg.setDefaultEncoding("UTF-8");
    }

    @Override
    public String render(String fileName, Object pageValue) throws TemplateEngineException {
        try {
            StringWriter writer = new StringWriter();

            writer.getBuffer().replace(0, writer.getBuffer().length(), "");

            Template templateEngine = cfg.getTemplate(fileName);
            templateEngine.process(pageValue, writer);

            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new TemplateEngineException("Can't render template", e);
        }
    }
}
