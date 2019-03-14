package ua.griddynamics.geekshop.util;

import redis.clients.jedis.Jedis;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-12
 */
public class Jed {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
    }
}
