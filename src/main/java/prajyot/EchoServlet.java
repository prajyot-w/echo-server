package prajyot;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Collectors;

import static prajyot.Constants.BODY;
import static prajyot.Constants.CLIENT_IP;
import static prajyot.Constants.HEADERS;
import static prajyot.Constants.METHOD;
import static prajyot.Constants.NEW_LN;
import static prajyot.Constants.PATH;
import static prajyot.Constants.REQUEST_END;
import static prajyot.Constants.REQUEST_START;

public class EchoServlet extends HttpServlet {
    private static final String rootDirectory = System.getProperty("user.home");
    private static final String folderName = "echo";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);

        String dateTime = dateFormat.format(new Date());
        String fileName = "request_" + dateTime + ".txt";

        String requestURI = req.getRequestURI();
        String requestMethod = req.getMethod();
        String remoteAddress = req.getRemoteAddr();
        String requestHeaders = headers(req);
        String requestBody = body(req);

        StringBuffer sbuf = new StringBuffer();
        sbuf.append(REQUEST_START);
        sbuf.append(PATH + requestURI);
        sbuf.append(METHOD + requestMethod);
        sbuf.append(CLIENT_IP + remoteAddress);
        sbuf.append(HEADERS);
        sbuf.append(requestHeaders);
        sbuf.append(BODY);
        sbuf.append(requestBody);
        sbuf.append(REQUEST_END);

        File folder = new File(rootDirectory + File.separator + folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(folder, fileName);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(sbuf.toString());
            writer.close();
            System.out.println("File created and content written successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println(sbuf);
    }

    private String headers(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> headerKeyIterator = request.getHeaderNames().asIterator();
        while (headerKeyIterator.hasNext()) {
            String headerKey = headerKeyIterator.next();
            String headerValue = request.getHeader(headerKey);

            sb.append(headerKey);
            sb.append(": ");
            sb.append(headerValue);
            sb.append(NEW_LN);
        }

        return sb.toString();
    }

    private String body(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
