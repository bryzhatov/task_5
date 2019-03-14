package ua.griddynamics.geekshop.controllers.entity;

import ua.griddynamics.httpserver.api.HttpRequest;
import ua.griddynamics.httpserver.api.HttpResponse;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-14
 */
public interface GeekReaction {
    String react(HttpRequest request, HttpResponse response, Model model);
}
