import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.regex.Pattern;
import java.util.Set;


// -need- to implement visit() and shouldVisit()
public class BasicCrawler extends WebCrawler {

//    private static final Pattern FILTER = Pattern.compile(".*(\\.(css|js|"
//            + "|mp3|zip|gz))");
    private static final Pattern FILTER = Pattern.compile(".*\\.(css|js||mp3|zip|gz)$");

    //private final AtomicInteger numSeenImages;

    /**
     * Creates a new crawler instance.
     *
     * @param-numSeenImages This is just an example to demonstrate how you can pass objects to crawlers. In this
     * example, we pass an AtomicInteger to all crawlers and they increment it whenever they see a url which points
     * to an image.
     */
    public BasicCrawler() {
        //this.numSeenImages = 10;
    }

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();

        boolean resideInWebsite = false;
        if (href.startsWith(Main.HTTPS_USATODAY) ||
                href.startsWith(Main.HTTP_USATODAY)) resideInWebsite = true;
        String resideInValue = resideInWebsite ? "OK" : "N_OK";

        String replaceCommaURL = url.getURL().replace(',', '-');
        Data.urlDiscovered.put(replaceCommaURL, resideInValue);
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (FILTER.matcher(href).matches()) {
            //numSeenImages.incrementAndGet();
            return false;
        }

        Data.shouldVisitCallNum++;
        if (Data.shouldVisitCallNum % 1000 == 0) {
            System.out.println("Called shouldVisit " + Data.shouldVisitCallNum + " times.");
        }
        // only do pages in our course site
//        return href.startsWith("https://bytes.usc.edu/cs572/s23-sear-chhh");
        return true;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by our program.
     */
    @Override
    public void visit(Page page) {
        Data.visitCallNum++;
        if (Data.visitCallNum % 1000 == 0) {
            System.out.println("Called visit " + Data.visitCallNum + " times.");
        }

        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();
        String contentType =  page.getContentType();
        int pageSize = page.getContentData().length;

        int outLinkSize = 0;
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            outLinkSize = links.size();
        }

        url = url.replace(',', '-');

        Data.urlPageSize.put(url, pageSize);
        Data.urlOutlinkSize.put(url, outLinkSize);
        Data.urlContentType.put(url, contentType);


        System.out.println("Docid: " + docid);
        System.out.println("URL: " + url);
        System.out.println("Domain: " +  domain);
        System.out.println("Sub-domain: " + subDomain);
//        System.out.println("Path: "+ path);
//        System.out.println("Parent page: " + parentUrl);
//        System.out.println("Anchor text: " +  anchor);

//        if (page.getParseData() instanceof HtmlParseData) {
//            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//            String text = htmlParseData.getText();
//            String html = htmlParseData.getHtml();
//            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            /*logger.debug("Text length: {}", text.length());
            logger.debug("Html length: {}", html.length());
            logger.debug("Number of outgoing links: {}", links.size());*/
//        }

//        Header[] responseHeaders = page.getFetchResponseHeaders();
//        if (responseHeaders != null) {
//            System.out.println("Response headers:");
//            for (Header header : responseHeaders) {
//                //logger.debug("\t{}: {}", header.getName(), header.getValue());
//                System.out.println(header.getName() + "," + header.getValue());
//            }
//        }

        System.out.println("\n==========================\n");
    }

    @Override
    protected WebURL handleUrlBeforeProcess(WebURL curURL) {
//        System.out.println("URL:" + curURL);
        return super.handleUrlBeforeProcess(curURL);
    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        System.out.println("URL:" + webUrl);
        System.out.println("Status:" + statusCode);
        String url = webUrl.getURL();
        url = url.replace(',', '-');
        Data.urlStatus.put(url, statusCode);
    }
}// BasicCrawler