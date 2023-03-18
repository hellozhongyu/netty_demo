package netty.config;

import netty.protocol.Serializer;

import java.io.InputStream;
import java.util.Properties;

public abstract class Config {

    static Properties properties;

    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")){
            properties = new Properties();
            properties.load(in);
        } catch (Exception e){
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        String port = properties.getProperty("server.port");
        if (port == null)
            return 8080;
        else
            return Integer.parseInt(port);
    }

    public static Serializer.Algorithm getSerializerAlgorithm(){
        String seri_method = properties.getProperty("serializer.algorithm");
        if (seri_method == null)
            return Serializer.Algorithm.Java;
        else
            return Serializer.Algorithm.valueOf(seri_method);
    }
}
