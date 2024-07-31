package fr.nassime.endmc.web;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fr.nassime.endmc.EndMc;
import fr.nassime.endmc.api.user.User;
import fr.nassime.endmc.exceptions.RequestError;
import fr.nassime.endmc.exceptions.ServerError;
import lombok.Getter;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Getter
public class WebBridge {

    private ExecutorService executor;
    private HttpClient client;

    private String url;

    public WebBridge(String url) {
        this.url = url;
        String redirectPolicy = "ALWAYS";
        String version = "HTTP_1_1";

        if (url == null) throw new IllegalArgumentException("URL is null");

        if (!url.endsWith("/")) url += "/";

        this.executor = Executors.newFixedThreadPool(4,
                new ThreadFactoryBuilder().setNameFormat("EndMc %d").build());

        client = HttpClient.newBuilder()
                .followRedirects(get(HttpClient.Redirect.class, redirectPolicy, HttpClient.Redirect.NEVER))
                .version(get(HttpClient.Version.class, version, HttpClient.Version.HTTP_1_1))
                .authenticator(new Authenticator() {
                    @Override
                    public PasswordAuthentication requestPasswordAuthenticationInstance(String host, InetAddress address, int port, String protocol, String prompt, String scheme, URL url, RequestorType reqType) {
                        return new PasswordAuthentication("user", "password".toCharArray());
                    }
                })
                .executor(executor).build();
    }

    public CompletableFuture<User> executeRequest(String endpoint, Consumer<HttpRequest.Builder> builderConsumer) {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url + endpoint));

        builderConsumer.accept(builder);
        return client.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenCompose(response -> {
                    Throwable error = handleError(response.statusCode());

                    if (error != null) return CompletableFuture.failedFuture(error);
                    return CompletableFuture.completedFuture(EndMc.get().getGson().fromJson(response.body(), User.class));
                });
    }

    public void close() {
        executor.shutdownNow();
        executor = null;
        client = null;
    }

    private Throwable handleError(int statusCode) {
        int error = statusCode / 100;

        if(error == 4) {
            return new RequestError();
        } else if(error == 5) {
            return new ServerError();
        }
        return null;
    }

    private  <E extends Enum<E>> E get(Class<E> enumType, String type, E defaultValue) {
        try {
            return Enum.valueOf(enumType, type);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

}
