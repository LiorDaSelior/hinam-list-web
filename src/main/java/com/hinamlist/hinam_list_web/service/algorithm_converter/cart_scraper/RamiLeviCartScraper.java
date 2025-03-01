package com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper;

import com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper.exception.APIResponseException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class RamiLeviCartScraper extends  AbstractCartScraper {

/*    private String getClearanceCookie() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        // Initialize the WebDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // Visit the protected website
            driver.get("https://www.rami-levy.co.il");

            // Wait for Cloudflare challenge to complete
            // You might need to adjust this timeout
            Thread.sleep(10000);

            // Get the cf_clearance cookie
            Cookie cfClearance = driver.manage().getCookieNamed("cf_clearance");

            if (cfClearance != null) {
                System.out.println("cf_clearance: " + cfClearance.getValue());
                System.out.println("User-Agent: " + ((JavascriptExecutor) driver).executeScript("return navigator.userAgent"));
                return cfClearance.getValue();
            } else {
                System.out.println("Failed to get cf_clearance cookie");
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return "";
    }*/

    public JSONObject getCartObject(Map<Integer, Float> idQuantityMap) throws IOException, InterruptedException, APIResponseException {
/*        String cfClearance = getClearanceCookie();
        HttpCookie cookie = new HttpCookie("cf_clearance", cfClearance);
        try {
            URI uri = new URI("https://www.rami-levy.co.il");
            cookieManager.getCookieStore().add(uri, cookie);
            System.out.println(cookieManager.getCookieStore().getCookies());
        }
        catch (Exception _) {}*/

        String uriString;
        HttpRequest request;
        String response;

        Map<String, String> headers = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();

        uriString = "https://www.rami-levy.co.il/api/v2/cart";

        JSONObject itemObject = new JSONObject();
        for (var item : idQuantityMap.entrySet()) {
            itemObject.put(String.valueOf(item.getKey()), String.valueOf(item.getValue()));
        }

        bodyMap.put("items", itemObject);
        bodyMap.put("isClub", 0);
        bodyMap.put("store", 331);
        bodyMap.put("supplyAt",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T00:00:00.000Z");
        bodyMap.put("meta",
                JSONObject.NULL);
        System.out.println(bodyMap.entrySet());
        request = createHttpPostRequest(uriString, headers, bodyMap);
        response = getResponse(request);
        return new JSONObject(response);
    }
}
