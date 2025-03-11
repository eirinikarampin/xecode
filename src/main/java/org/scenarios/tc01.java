package org.scenarios;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class tc01 {
    // Shared between all tests in this class.
    Playwright playwright;
    Browser browser;

    // New instance for each test method.
    BrowserContext context;
    Page page;

    @BeforeClass
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true)
                .setSlowMo(500));
    }

    @AfterClass
    void closeBrowser() {
        playwright.close();
    }

    @BeforeMethod
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();

        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
    }

    @AfterMethod
    void closeContext(org.testng.ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && context != null) {
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace.zip")));
        }
        context.close();
    }

    public void SearchAndSelectAllResults(String givenPlace, int min_price, int max_price, int min_size, int max_size) throws InterruptedException {
        page.navigate("https://www.xe.gr/");
        //Accept cookies
        page.waitForSelector(".qc-cmp2-summary-buttons>[mode=\"primary\"]");
        page.locator(".qc-cmp2-summary-buttons>[mode=\"primary\"]").click();
        //set "Rent" property
        page.locator("button[data-testid=\"open-property-transaction-dropdown\"]").click();
        page.locator("button[data-id=\"rent\"]").click();
        //search for input "Παγκρατι"
        page.locator("input[name=\"geo_place_id\"]").fill(givenPlace);
        Thread.sleep(2000); //need sleep for dropdown menu
        int totalCount = page.locator("[data-testid=\"geo_place_id_dropdown_panel\"] button").count();
        System.out.println("Total places: " + totalCount );

        if (totalCount == 0) {
            System.out.println("No options found in autocomplete dropdown.");
        } else {
            for (int i = 1; i <= totalCount; i++) {
                page.locator("[data-testid=\"geo_place_id_dropdown_panel\"] button:nth-child(" + i + ")").click();
                page.locator("input[name=\"geo_place_id\"]").fill(givenPlace);
            }
        }
        //click search button
        page.locator("input[data-testid=\"submit-input\"]").click();
        //set "min_price" to "max_price" in price field
        page.locator("button[data-testid=\"price-filter-button\"]").click();
        page.locator("input[data-testid=\"minimum_price_input\"]").fill(String.valueOf(min_price));
        page.locator("input[data-testid=\"maximum_price_input\"]").fill(String.valueOf(max_price));
        //set min_size to max_size in price field
        page.locator("button[data-testid=\"size-filter-button\"]").click();
        page.locator("input[data-testid=\"minimum_size_input\"]").fill(String.valueOf(min_size));
        page.locator("input[data-testid=\"maximum_size_input\"]").fill(String.valueOf(max_size));

        Locator areaTag = page.locator(".area-tag");
        String text = areaTag.innerText();

        assert text.equals(totalCount + " περιοχές");
    }

    public void PhoneContact(){
        String PhoneContact = page.locator("[data-testid=\"call-action-button\"]").innerText();
        if (!PhoneContact.matches(".*\\d.*")) {
            System.out.println("No numbers are included");
        } else {
            System.out.println("Includes numbers");
        }
        page.locator("[data-testid=\"call-action-button\"]").click();

        if (page.locator(".xe-phone").isVisible()) {
            System.out.println("Phone is visible");
            String phoneText = page.locator(".xe-phone").innerText();
            String cleanPhone = phoneText.replaceAll("[^0-9]", "");
            int phone = Integer.parseInt(cleanPhone);
            System.out.println("phonenumber: " + phone);
        } else if (page.locator(".xe-mobile").isVisible()) {
            System.out.println("Mobile is visible");
            String mobileText = page.locator(".xe-mobile").innerText();
            String cleanMobile = mobileText.replaceAll("[^0-9]", "");
            int mobile = Integer.parseInt(cleanMobile);
            System.out.println("mobilenumber: " + mobile);

        } else {
            System.out.println("No phone or mobile number for this ad");
        }
    }

    @Test
    void Searchtest1() throws InterruptedException {
        //Search for rent properties in Παγκράτι and related areas
        // between 200euro and 700euro
        // and between 75 and 150 size
        page.navigate("https://www.xe.gr/");
        //Accept cookies
        page.waitForSelector(".qc-cmp2-summary-buttons>[mode=\"primary\"]");
        page.locator(".qc-cmp2-summary-buttons>[mode=\"primary\"]").click();
        //set "Rent" property
        page.locator("button[data-testid=\"open-property-transaction-dropdown\"]").click();
        page.locator("button[data-id=\"rent\"]").click();
        //search for input "Παγκρατι"
        page.locator("input[name=\"geo_place_id\"]").fill("Παγκράτι");
        Thread.sleep(2000); //need sleep for dropdown menu
        int totalCount = page.locator("[data-testid=\"geo_place_id_dropdown_panel\"] button").count();
        System.out.println("Total places: " + totalCount );

        if (totalCount == 0) {
            System.out.println("No options found in autocomplete dropdown.");
        } else {
            for (int i = 1; i <= totalCount; i++) {
                page.locator("[data-testid=\"geo_place_id_dropdown_panel\"] button:nth-child(" + i + ")").click();
                page.locator("input[name=\"geo_place_id\"]").fill("Παγκράτι");
            }
        }
        //click search button
        page.locator("input[data-testid=\"submit-input\"]").click();
        //set "200 to 700" in price field
        page.locator("button[data-testid=\"price-filter-button\"]").click();
        page.locator("input[data-testid=\"minimum_price_input\"]").fill("200");
        page.locator("input[data-testid=\"maximum_price_input\"]").fill("700");
        //set "75m2 to 150m2" in price field
        page.locator("button[data-testid=\"size-filter-button\"]").click();
        page.locator("input[data-testid=\"minimum_size_input\"]").fill("75");
        page.locator("input[data-testid=\"maximum_size_input\"]").fill("150");
        //System.out.println("URl:" + page.url());

        Locator areaTag = page.locator(".area-tag");
        String text = areaTag.innerText();

        assert text.equals(totalCount + " περιοχές");
    }

    @Test
    void VerifyResults() throws InterruptedException {
        //Verify all search results are within the specified criteria
        System.out.println("VerifyResults");
        SearchAndSelectAllResults("Παγκρατι", 200, 700, 75, 150);
        int totalPage = page.locator("[data-testid=\"pagination\"]").count();
        System.out.println("Total page number:" + totalPage);
        if (totalPage == 0) {
            System.out.println("No options found");
        } else {
            for (int i = 1; i <= totalPage; i++) {
                page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
                page.locator("[data-testid=\"description\"]").click();

                page.waitForSelector("[data-testid=\"property-ad-price\"]", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
                List<ElementHandle> elementsPrice = page.querySelectorAll("[data-testid=\"property-ad-price\"]");

//                boolean newElementsLoaded = true;
//                while (newElementsLoaded) {
//                    int currentSize = elementsPrice.size();
//                    page.waitForSelector("[data-testid=\"property-ad-price\"]", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
//                    List<ElementHandle> newElementsPrice = page.querySelectorAll("[data-testid=\"property-ad-price\"]");
//                    if (newElementsPrice.size() > currentSize) {
//                        System.out.println("New elements loaded, updating list.");
//                        elementsPrice.addAll(newElementsPrice.subList(currentSize, newElementsPrice.size()));
//                    } else {
//                        newElementsLoaded = false;
//                    }
//                }
                for (ElementHandle elementPrice : elementsPrice) {
                    try {
                        String priceText = elementPrice.innerText();
                        String cleanPrice = priceText.replaceAll("[^0-9]", "");
                        int price = Integer.parseInt(cleanPrice);
                        System.out.println("price: " + price);
                        assert price >= 200 && price <= 700 : "Price is out of range! Found: " + price;
                    } catch (Exception e) {
                        System.out.println("Continue to ad ");
                    }
                }

                page.waitForSelector("[data-testid=\"property-ad-title\"]", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
                List<ElementHandle> elementsSize = page.querySelectorAll("[data-testid=\"property-ad-title\"]");
                for (ElementHandle elementSize : elementsSize) {
                    try {
                        String priceText = elementSize.innerText();
                        String cleanPrice = priceText.replaceAll("[^0-9]", "");
                        int size = Integer.parseInt(cleanPrice);
                        System.out.println("size: " + size);
                        assert size >= 75 && size <= 150 : "Size is out of range! Found: " + size;
                    } catch (Exception e) {
                        System.out.println("Continue to ad ");
                    }
                }
                while ((page.locator("[aria-label=\"Next page\"]")).isVisible()){
                    page.locator("[aria-label=\"Next page\"]").click();
                    i++;
                }
            }
        }
    }

    @Test
    void CheckImages() throws InterruptedException {
        //No ad contains more than 30 pictures
        System.out.println("CheckImages");
        SearchAndSelectAllResults("Παγκρατι", 200, 700, 75, 150);
        int totalPage = page.locator("[data-testid=\"pagination\"]").count();
        System.out.println("Total page number:" + totalPage);
        if (totalPage == 0) {
            System.out.println("No options found");
        } else {
            for (int i = 1; i <= totalPage; i++) {
                page.waitForSelector("[data-testid=\"property-ad-images-carousel\"]", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
                List<ElementHandle> elementsPic = page.querySelectorAll("[data-testid=\"property-ad-images-carousel\"]");
                for (ElementHandle elementPic : elementsPic) {
                    elementPic.hover();
                    List<ElementHandle> imageDivs = elementPic.querySelectorAll(".slick-track > div:not(.slick-slide.slick-cloned)");
                    int totalImageAmount = imageDivs.size();
                    //System.out.println("totalImageAmount: " + totalImageAmount);
                    assert totalImageAmount <= 30 : "totalImageAmount is out of range! Found: " + totalImageAmount;
                }

                while ((page.locator("[aria-label=\"Next page\"]")).isVisible()){
                    page.locator("[aria-label=\"Next page\"]").click();
                    i++;
                    }
            }
        }
    }

    @Test
    void DescendingPriceResult() throws InterruptedException {
        //No ad contains more than 30 pictures
        System.out.println("DescendingPriceResult");
        SearchAndSelectAllResults("Παγκρατι", 200, 700, 75, 150);
        int totalPage = page.locator("[data-testid=\"pagination\"]").count();
        System.out.println("Total page number:" + totalPage);

        if (totalPage == 0) {
            System.out.println("No options found");
        } else {
            page.locator("[data-testid=\"open-property-sorting-dropdown\"]").click();
            page.locator("[data-id=\"price_desc\"]").click();
            Thread.sleep(2000);
            List<Integer> prices = new ArrayList<>();

            for (int i = 1; i <= totalPage; i++) {
                page.waitForSelector("[data-testid=\"property-ad-price\"]", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
                List<ElementHandle> elementsPrice = page.querySelectorAll("[data-testid=\"property-ad-price\"]");

//                boolean newElementsLoaded = true;
//                while (newElementsLoaded) {
//                    int currentSize = elementsPrice.size();
//                    page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
//                    page.waitForSelector("[data-testid=\"property-ad-price\"]", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
//                    List<ElementHandle> newElementsPrice = page.querySelectorAll("[data-testid=\"property-ad-price\"]");
//                    Thread.sleep(2000);
//                    if (newElementsPrice.size() > currentSize) {
//                        System.out.println("New elements loaded, updating list.");
//                        elementsPrice.addAll(newElementsPrice.subList(currentSize, newElementsPrice.size()));
//                    } else {
//                        newElementsLoaded = false;
//                    }
//                }
                for (ElementHandle elementPrice : elementsPrice) {
                    String priceText = elementPrice.innerText();
                    String cleanPrice = priceText.replaceAll("[^0-9]", "");
                    int price = Integer.parseInt(cleanPrice);
                    //System.out.println("elementPrice: " + elementPrice);
                    prices.add(price);
                }
                System.out.println("All prices list: " + prices);
                for (int priceValue=1; priceValue<prices.size(); priceValue++) {
                    if (prices.get(i) > prices.get(i - 1)){
                        System.out.println("DescendingPrice: " + prices.get(i) + "And DescendingPrice: " + prices.get(i - 1));
                        break;
                    }
                }

                while ((page.locator("[aria-label=\"Next page\"]")).isVisible()){
                    page.locator("[aria-label=\"Next page\"]").click();
                    i++;
                }
            }
        }
    }

    @Test
    void ContactPhone() throws InterruptedException {
        // Verify contact number is not visible
        System.out.println("ContactPhone");
        SearchAndSelectAllResults("Παγκρατι", 200, 700, 75, 150);
        int totalPage = page.locator("[data-testid=\"pagination\"]").count();
        System.out.println("Total page number:" + totalPage);

        if (totalPage == 0) {
            System.out.println("No options found");
        } else {
            for (int i = 1; i <= totalPage; i++) {
                page.waitForSelector("[data-testid=\"property-ad-price\"]", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
                List<ElementHandle> elementsPhone = page.querySelectorAll("[data-testid=\"property-ad-price\"]");
                boolean newElementsLoaded = true;
                while (newElementsLoaded) {
                    int currentSize = elementsPhone.size();
                    page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
                    page.waitForSelector("[data-testid=\"property-ad-price\"]", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
                    List<ElementHandle> newElementsPhone = page.querySelectorAll("[data-testid=\"property-ad-price\"]");
                    Thread.sleep(2000);
                    if (newElementsPhone.size() > currentSize) {
                        System.out.println("New elements loaded, updating list.");
                        elementsPhone.addAll(newElementsPhone.subList(currentSize, newElementsPhone.size()));
                    } else {
                        newElementsLoaded = false;
                    }
                }
                for (ElementHandle elementPhone : elementsPhone) {
                    elementPhone.click();

                    if(page.locator("[data-testid=\"call-action-button\"]").isVisible())
                        PhoneContact();
                    else{
                        //SELECT FIRST AD
                        PhoneContact();
                    }
                }

                while ((page.locator("[aria-label=\"Next page\"]")).isVisible()){
                    page.locator("[aria-label=\"Next page\"]").click();
                    i++;
                }
            }
        }
    }

}

