package com.arep.framework;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class MiniSpark {
    private static final int PORT = 8080;
    private static Path staticFolder = Paths.get("www").toAbsolutePath().normalize();
    private static final Map<String, Route> getRoutes = new HashMap<>();

    public static void staticfiles(String folder) {
        staticFolder = Paths.get(folder).toAbsolutePath().normalize();
    }

    public static void get(String path, Route route) {
        getRoutes.put(path, route);
    }

    public static void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor en http:localhost:" + PORT);
            System.out.println("Static folder:" + staticFolder);

            while (true) {
                Socket client = serverSocket.accept();
                new Thread(()->{
                    try { handle(client); } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private static void handle(Socket socket) throws Exception {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.US_ASCII));

        String requestLine = in.readLine();
        if (requestLine == null ) return;
        String[] parts = requestLine.split(" ");
        if (parts.length < 2) return;
        String method = parts[0];
        URI uri = new URI(parts[1]);

        String line;
        int contentLength = 0;
        while (!(line = in.readLine()).isEmpty()){
            if (line.toLowerCase().startsWith("content-length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        char[] bodyChars = new char[contentLength];
        if (contentLength > 0) in.read(bodyChars);
        String body = new String(bodyChars);

        Request req = new Request(method, uri, body);
        Response res = new Response();

        if ("GET".equals(method) && getRoutes.containsKey(uri.getPath())) {
            String result = getRoutes.get(req.getPath()).handle(req, res);
            byte[] data = result.getBytes(StandardCharsets.UTF_8);
            String headers = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + res.getType() + "\r\n" +
                    "Content-Length: " + data.length + "\r\n\r\n";
            os.write(headers.getBytes(StandardCharsets.UTF_8));
            os.write(data);
        } else {
            serveStatic(uri.getPath(), os);
        }

        os.flush();
        socket.close();
    }

    private static void serveStatic(String path, OutputStream os) throws IOException {
        if (path.equals("/")) path = "/index.html";
        Path file = staticFolder.resolve("." + path).normalize();

        if (!file.startsWith(staticFolder) || !Files.exists(file)) {
            String notFound = "HTTP/1.1 404 Not Found\r\n\r\nArchivo no encontrado";
            os.write(notFound.getBytes(StandardCharsets.UTF_8));
            return;
        }

        String mime = guessMime(path);
        byte[] bytes = Files.readAllBytes(file);
        String headers = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + mime + "\r\n" +
                "Content-Length: " + bytes.length + "\r\n\r\n";
        os.write(headers.getBytes(StandardCharsets.UTF_8));
        os.write(bytes);
    }

    private static String guessMime(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".gif")) return "image/gif";
        return "text/plain";
    }
}
